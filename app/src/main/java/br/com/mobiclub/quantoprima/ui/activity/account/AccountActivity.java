package br.com.mobiclub.quantoprima.ui.activity.account;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import com.github.kevinsawicki.wishlist.Toaster;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import javax.inject.Inject;

import br.com.mobiclub.quantoprima.R;
import br.com.mobiclub.quantoprima.config.Injector;
import br.com.mobiclub.quantoprima.core.service.MobiClubServiceApi;
import br.com.mobiclub.quantoprima.core.service.api.entity.ConstantsApi;
import br.com.mobiclub.quantoprima.core.service.api.entity.User;
import br.com.mobiclub.quantoprima.core.service.api.exception.AppBlockedException;
import br.com.mobiclub.quantoprima.core.service.api.exception.AppOutdatedException;
import br.com.mobiclub.quantoprima.core.service.api.exception.UserBlockedException;
import br.com.mobiclub.quantoprima.domain.MobiClubUser;
import br.com.mobiclub.quantoprima.events.NetworkErrorEvent;
import br.com.mobiclub.quantoprima.events.UnAuthorizedErrorEvent;
import br.com.mobiclub.quantoprima.facade.Facade;
import br.com.mobiclub.quantoprima.service.authenticator.ActionBarAccountAuthenticatorActivity;
import br.com.mobiclub.quantoprima.ui.activity.AppInactiveActivity;
import br.com.mobiclub.quantoprima.ui.activity.launch.OutdatedActivity;
import br.com.mobiclub.quantoprima.ui.adapter.TextWatcherAdapter;
import br.com.mobiclub.quantoprima.ui.factory.AlertDialogFactory;
import br.com.mobiclub.quantoprima.domain.Constants;
import br.com.mobiclub.quantoprima.util.Ln;
import br.com.mobiclub.quantoprima.util.SafeAsyncTask;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.Optional;
import retrofit.RetrofitError;

import static android.accounts.AccountManager.KEY_ACCOUNT_NAME;
import static android.accounts.AccountManager.KEY_ACCOUNT_TYPE;
import static android.accounts.AccountManager.KEY_AUTHTOKEN;
import static android.accounts.AccountManager.KEY_BOOLEAN_RESULT;

/**
 *
 */
public abstract class AccountActivity extends ActionBarAccountAuthenticatorActivity {
    /**
     * PARAM_CONFIRM_CREDENTIALS
     */
    public static final String PARAM_CONFIRM_CREDENTIALS = "confirmCredentials";
    /**
     * PARAM_PASSWORD
     */
    public static final String PARAM_PASSWORD = "password";
    /**
     * PARAM_USERNAME
     */
    public static final String PARAM_USERNAME = "name";
    /**
     * PARAM_AUTHTOKEN_TYPE
     */
    public static final String PARAM_AUTHTOKEN_TYPE = "authtokenType";

    protected final TextWatcher watcher = validationTextWatcher();

    @Optional
    @InjectView(R.id.et_email) protected AutoCompleteTextView emailText;
    @Optional
    @InjectView(R.id.et_password) EditText passwordText;
    @Optional
    @InjectView(R.id.b_signin) protected Button signInButton;

    /**
     * Was the original caller asking for an entirely new account?
     */
    protected boolean requestNewAccount = false;
    @Inject
    MobiClubServiceApi mobiClubService;
    @Inject
    Bus bus;
    private AccountManager accountManager;
    private SafeAsyncTask<User> authenticationTask;
    private String authToken;
    private String authTokenType;
    /**
     * If set we are just checking that the user knows their credentials; this
     * doesn't cause the user's password to be changed on the device.
     */
    private Boolean confirmCredentials = false;
    private String email;
    private String password;
    /**
     * In this instance the token is simply the sessionId returned from Parse.com. This could be a
     * oauth token or some other type of timed token that expires/etc. We're just using the parse.com
     * sessionId to prove the example of how to utilize a token.
     */
    private String token;

    @Inject
    protected SharedPreferences prefs;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        if(getAccountLayout() != null)
            setContentView(getAccountLayout());

        Injector.inject(this);
        ButterKnife.inject(this);

        accountManager = AccountManager.get(this);

        final Intent intent = getIntent();
        email = intent.getStringExtra(PARAM_USERNAME);
        authTokenType = intent.getStringExtra(PARAM_AUTHTOKEN_TYPE);
        confirmCredentials = intent.getBooleanExtra(PARAM_CONFIRM_CREDENTIALS, false);

        requestNewAccount = email == null;

    }

    protected abstract Integer getAccountLayout();

    private TextWatcher validationTextWatcher() {
        return new TextWatcherAdapter() {
            public void afterTextChanged(final Editable gitDirEditText) {
                updateUIWithValidation();
            }

        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        bus.register(this);
        updateUIWithValidation();
    }

    @Override
    protected void onPause() {
        super.onPause();
        bus.unregister(this);
    }

    /**
     * Valida UI
     */
    protected void updateUIWithValidation() {
        //TODO: pegar imagens de botao desabilitado
        //final boolean populated = populated(emailText) && populated(passwordText);
        //signInButton.setEnabled(populated);
    }

    private boolean populated(final EditText editText) {
        return editText.length() > 0;
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage(getText(R.string.message_signing_in));
        dialog.setIndeterminate(true);
        dialog.setCancelable(true);
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            public void onCancel(final DialogInterface dialog) {
                if (authenticationTask != null) {
                    authenticationTask.cancel(true);
                }
            }
        });
        return dialog;
    }

    @Subscribe
    public void onNetworkErrorEvent(NetworkErrorEvent e) {
        //nesse caso tenta offline
        AlertDialogFactory.createDefaultError(AccountActivity.this,
                R.string.service_comunication_error).show();
    }

    @Subscribe
    public void onUnAuthorizedErrorEvent(UnAuthorizedErrorEvent unAuthorizedErrorEvent) {
        // Could not authorize for some reason.
        onAutorizationError();
    }

    private void onAutorizationError() {
        AlertDialogFactory.createDefaultError(this, R.string.message_bad_credentials).show();
    }

    /**
     * Handles onClick event on the Submit button. Sends name/password to
     * the server for authentication.
     * <p/>
     * Specified by android:onClick="handleSign" in the layout xml
     *
     * @param view
     */
    public void handleSign(final View view) {
        String emailValue = emailText.getText().toString();
        String passwordValue = passwordText.getText().toString();

        if(emailValue.isEmpty()) {
            AlertDialogFactory.createDefaultError(this, R.string.email_required).show();
            return;
        }
        if(passwordValue.isEmpty()) {
            AlertDialogFactory.createDefaultError(this, R.string.password_required).show();
            return;
        }

        signin();
    }

    protected void signin() {
        if (authenticationTask != null) {
            return;
        }

        if (requestNewAccount) {
            email = emailText.getText().toString();
        }

        password = passwordText.getText().toString();
        showProgress();
        authenticate();
    }

    private void authenticate() {
        authenticationTask = new SafeAsyncTask<User>() {
            public User call() throws Exception {
                User loginResponse = mobiClubService.signin(email, password);
                return loginResponse;
            }

            @Override
            protected void onException(final Exception e) throws RuntimeException {
                // Retrofit Errors are handled inside of the RestErrorHandler
                Intent intent = null;
                if(e instanceof AppBlockedException) {
                    Ln.d("AppBlockedException");
                    intent = new Intent(AccountActivity.this, AppInactiveActivity.class);
                } else if(e instanceof AppOutdatedException) {
                    Ln.d("AppOutdatedException");
                    intent = new Intent(AccountActivity.this, OutdatedActivity.class);
                } else if(e instanceof UserBlockedException) {
                    Ln.d("UserBlockedException");
                    intent = new Intent(AccountActivity.this, AccountBlockedActivity.class);
                }
                if(intent != null) {
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra(Constants.Extra.REASON_USER_BLOCKED, e.getMessage());
                    startActivity(intent);
                    AccountActivity.this.finish();
                    return;
                }
                if(!(e instanceof RetrofitError)) {
                    final Throwable cause = e.getCause() != null ? e.getCause() : e;
                    if(cause != null) {
                        AlertDialogFactory.createDefaultError(AccountActivity.this,
                                R.string.server_connection_error).show();
                    }
                }
            }

            @Override
            public void onSuccess(final User user) {
                if(user.isSuccess()) {
                    Ln.d("Auth isSuccess.");
                    token = String.valueOf(user.getId());

                    Facade facade = Facade.getInstance();
                    MobiClubUser mobiUser = facade.getUserByToken(token);
                    if(mobiUser == null) {
                        Ln.d("Criando novamente usuario.");
                        mobiUser = facade.createMobiClubUser(user);
                        facade.insertOrUpdateUser(mobiUser);
                    }

                    onAuthenticationResult(user.isSuccess());
                } else {

                    onAutorizationError();
                }
            }

            @Override
            protected void onFinally() throws RuntimeException {
                hideProgress();
                authenticationTask = null;
            }
        };
        authenticationTask.execute();
    }

    /**
     * Called when response is received from the server for confirm credentials
     * request. See onAuthenticationResult(). Sets the
     * AccountAuthenticatorResult which is sent back to the caller.
     *
     * @param result
     */
    protected void finishConfirmCredentials(final boolean result) {
        final Account account = new Account(email, ConstantsApi.Auth.BOOTSTRAP_ACCOUNT_TYPE);
        accountManager.setPassword(account, password);

        final Intent intent = new Intent();
        intent.putExtra(KEY_BOOLEAN_RESULT, result);
        setAccountAuthenticatorResult(intent.getExtras());
        setResult(RESULT_OK, intent);
        finish();
    }

    /**
     * Called when response is received from the server for authentication
     * request. See onAuthenticationResult(). Sets the
     * AccountAuthenticatorResult which is sent back to the caller. Also sets
     * the authToken in AccountManager for this account.
     */

    protected void finishLogin() {
        Ln.d("Finishing login.");
        final Account account = new Account(email, ConstantsApi.Auth.BOOTSTRAP_ACCOUNT_TYPE);

        authToken = token;

        if (requestNewAccount) {
            accountManager.addAccountExplicitly(account, password, null);
            accountManager.setAuthToken(account, ConstantsApi.Auth.BOOTSTRAP_ACCOUNT_TYPE,
                    authToken);
        } else {
            accountManager.setAuthToken(account, ConstantsApi.Auth.BOOTSTRAP_ACCOUNT_TYPE,
                    authToken);
            accountManager.setPassword(account, password);
        }

        final Intent intent = new Intent();
        intent.putExtra(KEY_ACCOUNT_NAME, email);
        intent.putExtra(KEY_ACCOUNT_TYPE, ConstantsApi.Auth.BOOTSTRAP_ACCOUNT_TYPE);

        Ln.d("authTokenType=%s", authTokenType);
        if (authTokenType != null
                && authTokenType.equals(ConstantsApi.Auth.AUTHTOKEN_TYPE)) {
            intent.putExtra(KEY_AUTHTOKEN, authToken);
        }

        setAccountAuthenticatorResult(intent.getExtras());
        setResult(RESULT_OK, intent);
        finish();
    }

    /**
     * Hide progress onGainMobiSuccess
     */
    @SuppressWarnings("deprecation")
    protected void hideProgress() {
        dismissDialog(0);
    }

    /**
     * Show progress
     */
    @SuppressWarnings("deprecation")
    protected void showProgress() {
        showDialog(0);
    }

    /**
     * Called when the authentication process completes (see attemptLogin()).
     *
     * @param result
     */
    public void onAuthenticationResult(final boolean result) {
        if (result) {
            if (!confirmCredentials) {
                finishLogin();
            } else {
                finishConfirmCredentials(true);
            }
        } else {
            Ln.d("onAuthenticationResult: failed to signin");
            if (requestNewAccount) {
                Toaster.showLong(AccountActivity.this,
                        R.string.message_auth_failed_new_account);
            } else {
                Toaster.showLong(AccountActivity.this,
                        R.string.message_auth_failed);
            }
        }
    }

    public AccountManager getAccountManager() {
        return accountManager;
    }

    public MobiClubServiceApi getMobiClubService() {
        return mobiClubService;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setToken(String id) {
        this.token = id;
    }
}
