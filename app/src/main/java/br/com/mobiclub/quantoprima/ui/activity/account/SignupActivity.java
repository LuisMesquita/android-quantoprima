package br.com.mobiclub.quantoprima.ui.activity.account;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.squareup.otto.Subscribe;

import br.com.mobiclub.quantoprima.MobiClubApplication;
import br.com.mobiclub.quantoprima.R;
import br.com.mobiclub.quantoprima.core.service.api.entity.ApiError;
import br.com.mobiclub.quantoprima.core.service.api.entity.User;
import br.com.mobiclub.quantoprima.core.service.api.exception.AppBlockedException;
import br.com.mobiclub.quantoprima.core.service.api.exception.AppOutdatedException;
import br.com.mobiclub.quantoprima.core.service.api.exception.UserBlockedException;
import br.com.mobiclub.quantoprima.domain.MobiClubUser;
import br.com.mobiclub.quantoprima.events.SignupFailedEvent;
import br.com.mobiclub.quantoprima.events.UnAuthorizedErrorEvent;
import br.com.mobiclub.quantoprima.facade.Facade;
import br.com.mobiclub.quantoprima.ui.activity.AppInactiveActivity;
import br.com.mobiclub.quantoprima.ui.activity.CentralActivity;
import br.com.mobiclub.quantoprima.ui.activity.WebViewActivity;
import br.com.mobiclub.quantoprima.ui.activity.launch.OutdatedActivity;
import br.com.mobiclub.quantoprima.ui.factory.AlertDialogFactory;
import br.com.mobiclub.quantoprima.ui.factory.ProgressDialogFactory;
import br.com.mobiclub.quantoprima.ui.helper.SignupHelper;
import br.com.mobiclub.quantoprima.domain.Constants;
import br.com.mobiclub.quantoprima.util.Ln;
import br.com.mobiclub.quantoprima.util.Mask;
import br.com.mobiclub.quantoprima.util.NetworkUtil;
import br.com.mobiclub.quantoprima.util.SafeAsyncTask;
import br.com.mobiclub.quantoprima.util.Util;
import butterknife.OnClick;
import retrofit.RetrofitError;


public class SignupActivity extends SignupAccountActivity  implements View.OnClickListener{

    private SignupHelper helper;
    private String errorMessage;
    private ProgressDialog progress;
    private EditText edtCpf;
    private Button bttnNaoCadastraCpf, bttnCadastraCpf;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Facade.getInstance().newIntent(getIntent());

        helper = new SignupHelper(this, emailText, passwordText, edtCpf);

        bttnNaoCadastraCpf = (Button) findViewById(R.id.bttnNaoCadastar);
        bttnCadastraCpf = (Button) findViewById(R.id.bttnCadastraCpf);
        edtCpf = (EditText) findViewById(R.id.et_cpf);
        edtCpf.addTextChangedListener(Mask.insert("###.###.###-##", edtCpf));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.account_central, menu);
        (menu.findItem(R.id.termo_uso)).setVisible(true);
        (menu.findItem(R.id.central_action)).setVisible(false);


//        for(int i = 0, c = menu.size(); i < c; ++i) {
        MenuItem item = menu.findItem(R.id.termo_uso);
        TextView actionLayout = (TextView) item.getActionView();
        actionLayout.setOnClickListener(this);
//        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.central_action:
                onCentral();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onPqCadastrarCpf(View view) {
        Intent intent = new Intent(SignupActivity.this, ImportanciaCpfActivity.class);
        intent.putExtra("cadastroUsuario", 1);
        startActivityForResult(intent, 1000);
    }
    public void onNaoCadastrarCpf(View view) {
//        Intent intent = new Intent(this, ImportanciaCpfActivity.class);
//        intent.putExtra("cadastroUsuario", 1);
//        startActivityForResult(intent, 1000);
        bttnNaoCadastraCpf.setVisibility(View.GONE);
        bttnCadastraCpf.setVisibility(View.VISIBLE);
        edtCpf.setVisibility(View.GONE);
    }
    public void onCadastrarCpf(View view) {
        bttnNaoCadastraCpf.setVisibility(View.VISIBLE);
        bttnCadastraCpf.setVisibility(View.GONE);
        edtCpf.setVisibility(View.VISIBLE);
    }


    private void onCentral() {
        final Intent intent = new Intent(this, CentralActivity.class);
        startActivity(intent);
    }

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

        String cpf = edtCpf.getText().toString();

        if(cpf.equalsIgnoreCase("") && edtCpf.getVisibility()==View.GONE){
            cpf = null;
            user.setCpf(cpf);

            //cria conta
            signup(user, helper.getPassword(), view); //async

        }else if (!Util.isValidarCpf(cpf) && edtCpf.getVisibility()==View.VISIBLE){
            progress.dismiss();

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setNegativeButton(R.string.button_ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    progress.dismiss();
                    dialog.dismiss();
                }
            });
            builder.setPositiveButton("Prefiro não \ninformar meu CPF", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    progress.dismiss();
                    dialog.dismiss();
                    Intent intent = new Intent(SignupActivity.this, ImportanciaCpfActivity.class);
                    intent.putExtra("cadastroUsuario", 1);
                    startActivityForResult(intent, 1000);
                }
            });
            builder.setTitle(getString(R.string.alert_titulo_erro_cpf));
            builder.setMessage(getString(R.string.alert_msg_erro_cpf));
            builder.create();
            builder.show();
        }else{
            user.setCpf(cpf);

            //cria conta
            signup(user, helper.getPassword(), view); //async
        }

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
                String cpf = user.getCpf();
                User userCreated = getMobiClubService().signup(name, email, surname,
                        gender, password, password, birth, cpf);
                user.setUserId(userCreated.getId());

                SignupActivity.this.setEmail(email);
                SignupActivity.this.setPassword(password);

                errorMessage = userCreated.getMessage();

                return userCreated.isSuccess();
            }

            @Override
            protected void onException(final Exception e) throws RuntimeException {
                // Retrofit Errors are handled inside of the RestErrorHandler
                progress.dismiss();
                Ln.e(e);
                Intent intent = null;
                if(e instanceof AppBlockedException) {
                    Ln.d("AppBlockedException");
                    intent = new Intent(SignupActivity.this, AppInactiveActivity.class);
                } else if(e instanceof AppOutdatedException) {
                    Ln.d("AppOutdatedException");
                    intent = new Intent(SignupActivity.this, OutdatedActivity.class);
                } else if(e instanceof UserBlockedException) {
                    Ln.d("UserBlockedException");
                    intent = new Intent(SignupActivity.this, AccountBlockedActivity.class);
                }
                if(intent != null) {
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra(Constants.Extra.REASON_USER_BLOCKED, e.getMessage());
                    startActivity(intent);
                    SignupActivity.this.finish();
                    return;
                }
                if(!(e instanceof RetrofitError)) {
                    final Throwable cause = e.getCause() != null ? e.getCause() : e;
                    if(cause != null) {
                        onSignupError(getString(R.string.server_connection_error));
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
        Intent intent = new Intent(this, WebViewActivity.class);
        intent.putExtra(Constants.Extra.WEB_VIEW_URL, Constants.URL.TERMS_AND_PRIVACY_POLICY);
        intent.putExtra(Constants.Extra.WEB_VIEW_TITLE, getString(R.string.web_view_terms_and_privacy_policy_title));
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        onUserTerms(null);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000){
            edtCpf.setText("");
            if(resultCode == 1001){
                bttnNaoCadastraCpf.setVisibility(View.VISIBLE);
                bttnCadastraCpf.setVisibility(View.GONE);
                edtCpf.setVisibility(View.VISIBLE);

            }else if(resultCode == 1002){
                bttnNaoCadastraCpf.setVisibility(View.GONE);
                bttnCadastraCpf.setVisibility(View.VISIBLE);
                edtCpf.setVisibility(View.GONE);
            }
        }
    }

}
