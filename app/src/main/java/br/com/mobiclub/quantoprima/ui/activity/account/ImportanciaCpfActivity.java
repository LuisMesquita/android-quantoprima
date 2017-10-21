package br.com.mobiclub.quantoprima.ui.activity.account;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import br.com.mobiclub.quantoprima.R;
import br.com.mobiclub.quantoprima.facade.Facade;
import br.com.mobiclub.quantoprima.ui.activity.CentralActivity;
import br.com.mobiclub.quantoprima.ui.helper.SignupHelper;


public class ImportanciaCpfActivity extends ActionBarActivity {

    private SignupHelper helper;
    private String errorMessage;
    private ProgressDialog progress;
    private int cadastroUsuario = 0;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_importancia_cpf);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Facade.getInstance().newIntent(getIntent());

        Intent it = getIntent();

        if(it != null){
            cadastroUsuario = it.getIntExtra("cadastroUsuario", 0);
        }


       // helper = new SignupHelper(this, emailText, passwordText);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.account_central, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.central_action:
                onCentral();
                return true;
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void onCentral() {
        final Intent intent = new Intent(this, CentralActivity.class);
        startActivity(intent);
    }


    public void onCadastrarCpf(View view){
        if(cadastroUsuario == 1){
            setResult(1001);
            finish();
        }else{
            final Intent intent = new Intent(this, CadastrarCpfActivity.class);
            startActivity(intent);
        }
    }
    public void onNaoCadastrarCpf(View view){
        setResult(1002);
        finish();
    }

/*
    public void onUserTerms(View view) {
        if(!NetworkUtil.isConnected(this)) {
            AlertDialog defaultError = AlertDialogFactory.createDefaultError(this, R.string.alert_title_attention,
                    R.string.alert_title_webview_no_connection);
            defaultError.setCancelable(true);
            defaultError.show();
            return;
        }
        Intent browserIntent = new Intent(this, WebViewActivity.class);
        browserIntent.putExtra(Constants.Extra.WEB_VIEW_URL, Constants.URL.TERMS_AND_PRIVACY_POLICY);
        browserIntent.putExtra(Constants.Extra.WEB_VIEW_TITLE, getString(R.string.web_view_terms_and_privacy_policy_title));
        startActivity(browserIntent);
    }

    @Subscribe
    public void onUnAuthorizedErrorEvent(UnAuthorizedErrorEvent event) {
        super.onUnAuthorizedErrorEvent(event);
    }

    @Subscribe
    public void onSignupFailedEvent(SignupFailedEvent event) {
        String message = getString(R.string.signup_error);
        ApiError apiError = event.getApiError();
        if(apiError.getMessage() != null)
            message = apiError.getMessage();
        onSignupError(message);
    }

    private void onSignupError(String message) {
        AlertDialogFactory.createDefaultError(this, message).show();
    }

    @OnClick(R.id.b_signin)
    public void handleSignup(View view) {
        Ln.d("handleSign");
        progress = ProgressDialogFactory.createProgress(this, R.string.signup_user);
        progress.setCancelable(true);
        progress.show();
        MobiClubUser user = helper.getUser();
        if(user == null) {
            String cause = helper.getValidationError();
            AlertDialogFactory.createDefaultError(this, R.string.signup_data_incomplete, cause).show();
            Ln.d("Error ao criar usuario, Motivo: %s", cause);
            progress.dismiss();
            return;
        }
        //cria conta
        signup(user, helper.getPassword(), view); //async
    }

    private void createUser(MobiClubUser user) {
        Facade.getInstance().createUser(user);
    }

    private void signup(final MobiClubUser user, final String password, final View view) {
        new SafeAsyncTask<Boolean>() {

            @Override
            public Boolean call() throws Exception {
                String name = user.getName();
                String surname = user.getLastName();
                String email = user.getEmail();
                String gender = user.getGenderType();

                String birth = user.getSignUptBirthdayString();

                User userCreated = getMobiClubService().signup(name, email, surname,
                        gender, password, password, birth);
                user.setUserId(userCreated.getId());

                ImportanciaCpfActivity.this.setEmail(email);
                ImportanciaCpfActivity.this.setPassword(password);

                errorMessage = userCreated.getMessage();

                return userCreated.isSuccess();
            }

            @Override
            protected void onException(final Exception e) throws RuntimeException {
                // Retrofit Errors are handled inside of the RestErrorHandler
                progress.dismiss();
                Crashlytics.logException(e);
                Ln.e(e);
                Intent intent = null;
                if(e instanceof AppBlockedException) {
                    Ln.d("AppBlockedException");
                    intent = new Intent(ImportanciaCpfActivity.this, AppInactiveActivity.class);
                } else if(e instanceof AppOutdatedException) {
                    Ln.d("AppOutdatedException");
                    intent = new Intent(ImportanciaCpfActivity.this, OutdatedActivity.class);
                } else if(e instanceof UserBlockedException) {
                    Ln.d("UserBlockedException");
                    intent = new Intent(ImportanciaCpfActivity.this, AccountBlockedActivity.class);
                }
                if(intent != null) {
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra(Constants.Extra.REASON_USER_BLOCKED, e.getMessage());
                    startActivity(intent);
                    ImportanciaCpfActivity.this.finish();
                    return;
                }
                if(!(e instanceof RetrofitError)) {
                    final Throwable cause = e.getCause() != null ? e.getCause() : e;
                    if(cause != null) {
                        onSignupError(getString(R.string.error_on_app));
                    }
                } else {
                    onSignupError(getString(R.string.server_connection_error));
                }
            }

            @Override
            public void onSuccess(final Boolean success) {
                progress.dismiss();
                if(success) {
                    Ln.d("Signup isSuccess.");
                    onSignupSuccess(user, view);
                } else {
                    Ln.d("Signup error.");
                    if(errorMessage != null)
                        onSignupError(errorMessage);
                    else
                        onSignupError(getString(R.string.signup_error));
                }
            }

        }.execute();

    }

    private void onSignupSuccess(MobiClubUser user, View view) {
        createUser(user);
        MobiClubApplication.getInstance().setFirstTime(true);
        Ln.d("Signup user created");
        //faz o sign
        super.handleSign(view);
    }

    @Override
    protected Integer getAccountLayout() {
        return R.layout.activity_signup;
    }

    public void onPrivacyPolicy(View view) {
        Ln.d("onPrivacyPolicy");
        if(!NetworkUtil.isConnected(this)) {
            AlertDialog defaultError = AlertDialogFactory.createDefaultError(this, R.string.alert_title_attention,
                    R.string.alert_title_webview_no_connection);
            defaultError.setCancelable(true);
            defaultError.show();
            return;
        }
        Intent intent = new Intent(this, WebViewActivity.class);
        intent.putExtra(Constants.Extra.WEB_VIEW_URL, Constants.URL.TERMS_AND_PRIVACY_POLICY);
        intent.putExtra(Constants.Extra.WEB_VIEW_TITLE, getString(R.string.web_view_terms_and_privacy_policy_title));
        startActivity(intent);
    }*/

}
