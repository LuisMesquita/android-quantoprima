package br.com.mobiclub.quantoprima.ui.activity.account;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.facebook.AppEventsLogger;
import com.facebook.FacebookAuthorizationException;
import com.facebook.FacebookOperationCanceledException;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.android.dialog.DialogError;
import com.facebook.android.dialog.Facebook;
import com.facebook.android.dialog.FacebookError;
import com.facebook.model.GraphUser;
import com.facebook.widget.FacebookDialog;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

import javax.inject.Inject;

import br.com.mobiclub.quantoprima.MobiClubApplication;
import br.com.mobiclub.quantoprima.R;
import br.com.mobiclub.quantoprima.config.Injector;
import br.com.mobiclub.quantoprima.core.service.MobiClubServiceApi;
import br.com.mobiclub.quantoprima.core.service.MobiClubServiceProvider;
import br.com.mobiclub.quantoprima.core.service.api.entity.ConstantsApi;
import br.com.mobiclub.quantoprima.core.service.api.entity.User;
import br.com.mobiclub.quantoprima.core.service.api.exception.AppBlockedException;
import br.com.mobiclub.quantoprima.core.service.api.exception.AppOutdatedException;
import br.com.mobiclub.quantoprima.core.service.api.exception.UserBlockedException;
import br.com.mobiclub.quantoprima.domain.MobiClubUser;
import br.com.mobiclub.quantoprima.events.NetworkErrorEvent;
import br.com.mobiclub.quantoprima.facade.Facade;
import br.com.mobiclub.quantoprima.facebook.FacebookFacade;
import br.com.mobiclub.quantoprima.facebook.MeRequestListener;
import br.com.mobiclub.quantoprima.facebook.SessionStore;
import br.com.mobiclub.quantoprima.ui.activity.AppInactiveActivity;
import br.com.mobiclub.quantoprima.ui.activity.launch.OutdatedActivity;
import br.com.mobiclub.quantoprima.ui.activity.launch.SplashActivity;
import br.com.mobiclub.quantoprima.ui.adapter.DialogResultAdapter;
import br.com.mobiclub.quantoprima.ui.factory.AlertDialogFactory;
import br.com.mobiclub.quantoprima.ui.view.ResultDialog;
import br.com.mobiclub.quantoprima.domain.Constants;
import br.com.mobiclub.quantoprima.util.Ln;
import br.com.mobiclub.quantoprima.util.SafeAsyncTask;
import br.com.mobiclub.quantoprima.util.Util;
import retrofit.RetrofitError;

import static android.accounts.AccountManager.KEY_ACCOUNT_NAME;
import static android.accounts.AccountManager.KEY_ACCOUNT_TYPE;
import static android.accounts.AccountManager.KEY_AUTHTOKEN;

/**
 *
 */
public class SigninFacebookHelper {

    private FacebookFacade facebook;

    @Inject
    MobiClubServiceProvider serviceProvider;

    @Inject
    Bus bus;

    private UiLifecycleHelper uiHelper;
    private String email;
    private String password;
    private String authToken;
    private AccountManager accountManager;
    private FragmentActivity activity;
    private ProgressDialog dialog;
    private GraphUser user;
    private Resources resources;
    private Facade facade;
    private boolean connecting = false;
    private MobiClubUser mobiUser;

    public void onCreate(FragmentActivity activity, Bundle savedInstanceState) {
        this.activity = activity;
        uiHelper = new UiLifecycleHelper(activity, callback);
        uiHelper.onCreate(savedInstanceState);

        Injector.inject(this);

        createDialog();

        facade = Facade.getInstance();
        resources = activity.getResources();

        accountManager = AccountManager.get(activity);
    }

    public void onSigninToFacebook() {
        //connectWithDialog();
    }

    private Session.StatusCallback callback = new Session.StatusCallback() {
        @Override
        public void call(Session session, SessionState state, Exception exception) {
            onSessionStateChange(session, state, exception);
        }
    };

    private void onSessionStateChange(Session session, SessionState state,
                                      Exception exception) {
        if (exception instanceof FacebookOperationCanceledException ||
                exception instanceof FacebookAuthorizationException) {
            new AlertDialog.Builder(activity)
                    .setTitle(R.string.alert_title_cancelled)
                    .setMessage(R.string.facebook_alert_message_permission_not_granted)
                    .setPositiveButton(R.string.button_ok, null)
                    .show();
        } else if (state == SessionState.OPENED_TOKEN_UPDATED) {
            Ln.d("User logged id");
        }
    }

    protected void createDialog() {
        final ProgressDialog dialog = new ProgressDialog(activity);
        dialog.setMessage(activity.getText(R.string.message_signing_in));
        dialog.setIndeterminate(true);
        dialog.setCancelable(true);
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            public void onCancel(final DialogInterface dialog) {
                //oncancel
            }
        });
        this.dialog = dialog;
    }

    protected void hideProgress() {
        dialog.dismiss();
    }

    /**
     * Show progress
     */
    @SuppressWarnings("deprecation")
    protected void showProgress() {
        dialog.show();
    }

    /**
     *
     * @deprecated
     */
    private void connectWithDialog() {
        showProgress();
        this.facebook = FacebookFacade.getInstance();
        facebook.authorize(activity, new Facebook.DialogListener() {
            public void onComplete(Bundle values) {
                facebook.saveSession(activity);

                facebook.requestUser(new MeRequestListener(SessionStore.getAccessToken(activity),
                        SessionStore.getAccessExpires(activity)) {
                    @Override
                    public void onComplete(FacebookUserInfo data) {
                        if (data.getEmail() != null && data.getEmail().isEmpty()) {

                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    AlertDialogFactory.createDefaultError(activity,
                                            R.string.facebook_conection_error_email_blank).show();
                                    hideProgress();
                                }
                            });

                            return;
                        }
                        authUserFromDialog(data);
                    }

                });
            }

            public void onFacebookError(FacebookError error) {
                hideProgress();
                AlertDialogFactory.createDefaultError(activity,
                        R.string.facebook_conection_error).show();
            }

            public void onError(DialogError e) {
                //TODO: test activity
                hideProgress();
                AlertDialogFactory.createDefaultError(activity,
                        R.string.facebook_conection_error).show();
            }

            public void onCancel() {
                hideProgress();
            }
        });
    }

    private void authUserFromDialog(MeRequestListener.FacebookUserInfo data) {
        MobiClubApplication.mobiUser = createMobiClubUserFromUserInfo(data);

//        MobiClubUser mobiUser = createMobiClubUserFromUserInfo(data);
//        authUser(mobiUser);
        activity.startActivityForResult(new Intent(activity.getApplicationContext(), CadastrarCpfActivity.class), 10000);
    }

    private void authUser(final MobiClubUser mobiUser) {
        new SafeAsyncTask<User>() {

            @Override
            public User call() throws Exception {
                MobiClubServiceApi service = serviceProvider.getService();
                if(service == null) {
                    onFacebookError(activity.getString(R.string.error_on_app));
                    return null;
                }
                Ln.d("service.signinFacebook");
                User user = service.signinFacebook(mobiUser.getFacebookId(),
                        mobiUser.getName(),mobiUser.getEmail(),
                        mobiUser.getSignUptBirthdayString(), mobiUser.getHometown(),
                        mobiUser.getLocation(),
                        mobiUser.getGenderType(), mobiUser.getLocale(),
                        mobiUser.getFacebookAccessToken(),
                        mobiUser.getFacebookAccessExpires());

                return user;
            }

            @Override
            protected void onException(Exception e) throws RuntimeException {
                Ln.e(e);
                super.onException(e);
                Intent intent = null;
                if(e instanceof AppBlockedException) {
                    Ln.d("AppBlockedException");
                    intent = new Intent(activity, AppInactiveActivity.class);
                } else if(e instanceof AppOutdatedException) {
                    Ln.d("AppOutdatedException");
                    intent = new Intent(activity, OutdatedActivity.class);
                } else if(e instanceof UserBlockedException) {
                    Ln.d("UserBlockedException");
                    intent = new Intent(activity, AccountBlockedActivity.class);
                }
                if(intent != null) {
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra(Constants.Extra.REASON_USER_BLOCKED, e.getMessage());
                    activity.startActivity(intent);
                    activity.finish();
                    return;
                }
                if(!(e instanceof RetrofitError)) {
                    onFacebookError(activity.getString(R.string.message_facebook_cant_connect));
                }
            }

            @Override
            protected void onSuccess(User user) throws Exception {
                super.onSuccess(user);
                Ln.d("onSuccess");
                if(user != null) {
                    if(user.isSuccess() && user != null && user.getId() != null) {
                        Ln.d("onSuccess: signinFacebookSuccess");
                        mobiUser.setUserId(user.getId());
                        mobiUser.setProfilePicture(MobiClubUser.FACEBOOK_PROFILE_PHOTO);
                        mobiUser.setHttpStatus(200);
                        signinFacebookSuccess(mobiUser);
                        MobiClubApplication.statusHttp = 200;
                    } else if (user.is412() && user != null && user.getId() != null) {
                        Log.e("LOGADO", "412");

                        MobiClubApplication.statusHttp = 412;
                        mobiUser.setHttpStatus(412);

                        mobiUser.setUserId(user.getId());
                        mobiUser.setProfilePicture(MobiClubUser.FACEBOOK_PROFILE_PHOTO);
                        signinFacebookSuccessNeedCPF(mobiUser);
                    } else{
                        Ln.d("erro");
                        String message = activity.getString(R.string.message_facebook_auth_failed);
                        if(user.getMessage() != null) {
                            message = user.getMessage();
                        }
                        onFacebookError(message);
                    }
                } else {
                    onFacebookError(activity.getString(R.string.message_facebook_auth_failed));
                }
            }
        }.execute();
    }

    private MobiClubUser createMobiClubUserFromUserInfo(MeRequestListener.FacebookUserInfo data) {
        String facebookEmail = "";
        MobiClubUser mobiUser = new MobiClubUser(data.getFacebookId(),facebookEmail,
                data.getName());

        mobiUser.setEmail(data.getEmail());
        String birthday = data.getBirthday();
        Date birthDate = Util.parseFacebookDate(birthday);
        mobiUser.setBirthday(birthDate);
        mobiUser.setHometown(data.getHometown());
        mobiUser.setLocation(data.getLocation());
        mobiUser.setGenderType(data.getGender());
        mobiUser.setLocale(data.getLocale());
        mobiUser.setFacebookAccessToken(data.getAccessToken());
        mobiUser.setFacebookAccessExpires(data.getAccessExpires());
        return mobiUser;
    }

    private MobiClubUser createMobiClubUserFromGraphUser(GraphUser user) {
        String id = user.getId();
        String facebookId = "";
        MobiClubUser mobiUser = new MobiClubUser(Long.parseLong(id),facebookId,
                user.getName());

        Ln.d("userName %s", user);

        if(mobiUser.getName() == null && !connecting) {
            return null;
        }

        try {
            String birthday = user.getBirthday();
            Date birthDate = Util.parseFacebookDate(birthday);
            mobiUser.setBirthday(birthDate);

            String name = (String) user.getProperty("name");
            if(name != null) {
                mobiUser.setName(name);
            }

            String email = (String) user.getProperty("email");
            if(email != null) {
                mobiUser.setEmail(email);
            } else {
                mobiUser.setEmail("");
            }

            String lastName = (String) user.getProperty("last_name");
            if(lastName != null) {
                mobiUser.setLastName(lastName);
            }

            JSONObject hometown = (JSONObject) user.getProperty("hometown");
            if(hometown != null)
                mobiUser.setHometown(hometown.getString("name"));

            JSONObject location = (JSONObject) user.getProperty("location");
            if(location != null)
                mobiUser.setLocation(location.getString("name"));

            String gender = (String) user.getProperty("gender");
            if(gender != null) {
                mobiUser.setGenderType(gender);
            } else {
                mobiUser.setGenderType("");
            }

            String locale = (String) user.getProperty("locale");
            if(locale != null)
                mobiUser.setLocale(locale);

            final Session session = Session.getActiveSession();
            mobiUser.setFacebookAccessToken(session.getAccessToken());
            mobiUser.setFacebookAccessExpires(session.getExpirationDate().getTime());
            return mobiUser;
        } catch (JSONException e) {
            return null;
        }

    }

    public MobiClubUser connect(GraphUser user) {
        MobiClubUser mobiClubUser = null;
        try {
            connecting = true;
            mobiClubUser = createMobiClubUserFromGraphUser(user);
            if(mobiClubUser == null) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        onFacebookError(resources.getString(R.string.facebook_conection_error));
                    }
                });
                throw new RuntimeException("Error ao conectar-se");
            }
            mobiClubUser = onFacebookConnect(mobiClubUser);
        } catch (Exception e) {
            onFacebookError(resources.getString(R.string.facebook_conection_error));
        }
        return mobiClubUser;
    }

    private MobiClubUser onFacebookConnect(MobiClubUser facebookUser) {
        MobiClubUser loggedUser = facade.getLoggedUser();
        if(loggedUser == null) {
            AlertDialogFactory.createDefaultError(activity,
                    R.string.facebook_conection_error_on_update).show();
            return null;
        }
        if(loggedUser != null) {
            //loggedUser.setName(facebookUser.getName());
            //loggedUser.setLastName(facebookUser.getLastName());
            //loggedUser.setEmail(facebookUser.getEmail());
            //loggedUser.setBirthday(facebookUser.getBirthday());
            loggedUser.setFacebookEmail(facebookUser.getFacebookEmail());
            loggedUser.setHometown(facebookUser.getHometown());
            loggedUser.setLocation(facebookUser.getLocation());
            //loggedUser.setGenderType(facebookUser.getGenderType());
            loggedUser.setLocale(facebookUser.getLocale());
            loggedUser.setFacebookId(facebookUser.getFacebookId());
            loggedUser.setFacebookAccessToken(facebookUser.getFacebookAccessToken());
            loggedUser.setFacebookAccessExpires(facebookUser.getFacebookAccessExpires());
            loggedUser.setProfilePicture(MobiClubUser.FACEBOOK_PROFILE_PHOTO);
            facade.insertOrUpdateUser(loggedUser);
        }
        return loggedUser;
    }

    public void authenticate(GraphUser user) {
        connecting = false;
        showProgress();
        if(user == null)
            return;
        MobiClubUser mobiClubUser = createMobiClubUserFromGraphUser(user);
        if(mobiClubUser == null) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    AlertDialogFactory.createDefaultError(activity,
                            R.string.facebook_conection_error_email_blank).show();
                    hideProgress();
                }
            });
            return;
        }
        Ln.d("mobiClubUser %s" , mobiClubUser);
        authUser(mobiClubUser);
    }

    private void signinFacebookSuccess(MobiClubUser mobiUser) {
        Ln.d("signinFacebookSuccess");
        email = mobiUser.getName();
        password = null;
        authToken = String.valueOf(mobiUser.getUserId());
        Facade fachada = Facade.getInstance();
        fachada.createFacebookUser(mobiUser);
        hideProgress();
        finishLogin();
    }

    private void signinFacebookSuccessNeedCPF(MobiClubUser mobiUser) {
        Ln.d("signinFacebookSuccess");
        email = mobiUser.getName();
        password = null;
        authToken = String.valueOf(mobiUser.getUserId());
        Facade fachada = Facade.getInstance();
        fachada.createFacebookUser(mobiUser);
        hideProgress();

        Intent intent = new Intent(activity.getApplicationContext(), CadastrarCpfToStoreActivity.class);
        activity.startActivityForResult(intent, 20000);

        finishLoginNeedCPF();
    }

    protected void finishLogin() {
        Ln.d("Finishing login.");
        final Account account = new Account(email, ConstantsApi.Auth.BOOTSTRAP_ACCOUNT_TYPE);

        accountManager.addAccountExplicitly(account, password, null);
        accountManager.setAuthToken(account, ConstantsApi.Auth.BOOTSTRAP_ACCOUNT_TYPE,
                authToken);

        final Intent intent = new Intent();
        intent.putExtra(KEY_ACCOUNT_NAME, email);
        intent.putExtra(KEY_ACCOUNT_TYPE, ConstantsApi.Auth.BOOTSTRAP_ACCOUNT_TYPE);

        Ln.d("authTokenType=%s", ConstantsApi.Auth.AUTHTOKEN_TYPE);
        //if (authTokenType != null
        //        && authTokenType.equals(ConstantsApi.Auth.AUTHTOKEN_TYPE)) {
        intent.putExtra(KEY_AUTHTOKEN, authToken);
        //}
        serviceProvider.setUser(authToken);

        activity.setResult(activity.RESULT_OK, intent);
        Intent splashIntent = new Intent(activity, SplashActivity.class);
        activity.startActivity(splashIntent);
        activity.finish();
    }

    protected void finishLoginNeedCPF() {
        Ln.d("Finishing login but need CPF.");
        final Account account = new Account(email, ConstantsApi.Auth.BOOTSTRAP_ACCOUNT_TYPE);

        accountManager.addAccountExplicitly(account, password, null);
        accountManager.setAuthToken(account, ConstantsApi.Auth.BOOTSTRAP_ACCOUNT_TYPE,
                authToken);

        final Intent intent = new Intent();
        intent.putExtra(KEY_ACCOUNT_NAME, email);
        intent.putExtra(KEY_ACCOUNT_TYPE, ConstantsApi.Auth.BOOTSTRAP_ACCOUNT_TYPE);

        Ln.d("authTokenType=%s", ConstantsApi.Auth.AUTHTOKEN_TYPE);
        //if (authTokenType != null
        //          && authTokenType.equals(ConstantsApi.Auth.AUTHTOKEN_TYPE)) {
        intent.putExtra(KEY_AUTHTOKEN, authToken);
        //}
        serviceProvider.setUser(authToken);

        activity.setResult(activity.RESULT_OK, intent);
        Intent splashIntent = new Intent(activity, SplashActivity.class);
        splashIntent.putExtra("needCPF", true);
        activity.startActivity(splashIntent);
        activity.finish();
    }

    @Subscribe
    public void onNetworkErrorEvent(NetworkErrorEvent e) {
        //nesse caso tenta offline
        onFacebookError(activity.getString(R.string.service_comunication_error));
    }

    private void onFacebookError(String message) {
        final Session session = Session.getActiveSession();
        session.closeAndClearTokenInformation();
        hideProgress();
        AlertDialogFactory.createDefaultError(activity, message).show();
    }

    private void showDialogResult(DialogResultAdapter result) {
        //TODO: make a factory ou builder
        ResultDialog dialog = new ResultDialog();
        Bundle args = new Bundle();
        args.putSerializable(Constants.Extra.DIALOG_RESULTER, result);
        dialog.setArguments(args);
        dialog.show(activity.getSupportFragmentManager(), "resultDialog");
    }

    private FacebookDialog.Callback dialogCallback = new FacebookDialog.Callback() {
        @Override
        public void onError(FacebookDialog.PendingCall pendingCall,
                            Exception error, Bundle data) {
            Ln.d("Error");
        }

        @Override
        public void onComplete(FacebookDialog.PendingCall pendingCall, Bundle data) {
            Ln.d("Success!");
        }
    };

    public void onResume() {
        bus.register(this);
        // Call the 'activateApp' method to log an app event for use in
        // analytics and advertising reporting.  Do so in
        // the onResume methods of the primary Activities that an app
        // may be launched into.
        AppEventsLogger.activateApp(activity);

        // For scenarios where the main activity is launched and user
        // session is not null, the session state change notification
        // may not be triggered. Trigger it if it's open/closed.
        Session session = Session.getActiveSession();
        if (session != null &&
                (session.isOpened() || session.isClosed()) ) {
            onSessionStateChange(session, session.getState(), null);
        }

        uiHelper.onResume();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode==10000){
            if (resultCode==10000){

            }
        }else{
            uiHelper.onActivityResult(requestCode, resultCode, data, dialogCallback);
        }
    }

    public void onPause() {
        // Call the 'deactivateApp' method to log an app event for
        // use in analytics and advertising
        // reporting.  Do so in the onPause methods of the primary
        // Activities that an app may be launched into.
        AppEventsLogger.deactivateApp(activity);

        bus.unregister(this);

        uiHelper.onPause();
    }

    public void onDestroy() {
        uiHelper.onDestroy();
    }

    public void onSaveInstanceState(Bundle outState) {
        uiHelper.onSaveInstanceState(outState);
    }

    public void setUser(GraphUser user) {
        this.user = user;
    }

    public GraphUser getUser() {
        return user;
    }



}
