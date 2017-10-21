package br.com.mobiclub.quantoprima.ui.activity.account;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;

import javax.inject.Inject;

import br.com.mobiclub.quantoprima.R;
import br.com.mobiclub.quantoprima.core.service.MobiClubServiceProvider;
import br.com.mobiclub.quantoprima.facebook.FacebookFacade;
import br.com.mobiclub.quantoprima.ui.activity.FullScreenMobiClubActivity;
import br.com.mobiclub.quantoprima.ui.activity.launch.SplashActivity;
import br.com.mobiclub.quantoprima.ui.factory.ProgressDialogFactory;
import br.com.mobiclub.quantoprima.util.Ln;

public class SigninActivity extends FullScreenMobiClubActivity
        implements AuthenticationListener {

    private static final int SIGNIN_EMAIL_REQUEST = 0;
    private Bundle accountExtras;

    @Inject
    MobiClubServiceProvider serviceProvider;
    private LoginButton btnFacebookSignIn;

    private SigninFacebookHelper facebookHelper;
    private FacebookFacade facebook;
    private ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        facebook = FacebookFacade.getInstance();

        facebookHelper = new SigninFacebookHelper();
        facebookHelper.onCreate(this, savedInstanceState);

        btnFacebookSignIn = (LoginButton) findViewById(R.id.btn_facebook);
        btnFacebookSignIn.setReadPermissions(facebook.getPermissions());
        btnFacebookSignIn.setTextAppearance(this, R.style.FacebookButton);
        btnFacebookSignIn.setBackgroundResource(R.drawable.t02_btn_login_facebook);
        btnFacebookSignIn.setText(R.string.signin_facebook_label);
        btnFacebookSignIn.setUserInfoChangedCallback(new LoginButton.UserInfoChangedCallback() {
            @Override
            public void onUserInfoFetched(GraphUser user) {
                Ln.d("onUserInfoFetched");
                if(user != null) {
                    btnFacebookSignIn.setUserInfoChangedCallback(null);
                    Ln.d("authenticate");
                    facebookHelper.authenticate(user);
                }
            }
        });

        accountExtras = getIntent().getExtras();
    }

    @Override
    public int getLayout() {
        return R.layout.activity_signin;
    }

    public void onSigninEmail(View view) {
        AuthenticatorTask authenticatorTask =
                new AuthenticatorTask(serviceProvider, SigninActivity.this);
        authenticatorTask.checkAuth(); //async
    }

    @Override
    public void onAuthentication(boolean authenticated) {
        btnFacebookSignIn.setUserInfoChangedCallback(null);
        progress = ProgressDialogFactory.createProgress(this, R.string.message_signing_in);
        progress.setCancelable(true);
        progress.show();
        Ln.e("onAuthentication em SignActivity");
        Intent intent = new Intent(this, SplashActivity.class);
        startActivity(intent);
        progress.dismiss();
        finish();
    }

    @Override
    public void onAuthenticationCancelled() {
        Ln.e("onAuthenticationCancelled em SignActivity");
        //Intent intent = new Intent(this, SigninActivity.class);
        //startActivity(intent);
        //finish();
    }

    @Override
    public void onAuthenticationFail() {
        Ln.e("onAuthenticationFail em SignActivity");
        Intent intent = new Intent(this, SigninActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ( keyCode == KeyEvent.KEYCODE_MENU ) {
            // do nothing
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void onLostPassword(View view) {
        Ln.d("onLostPasswordClicked");
        Intent intent = new Intent(this, LostPasswordActivity.class);
        startActivity(intent);
    }

    public void onSignup(View view) {
        Ln.d("onSignupClicked");
        btnFacebookSignIn.setUserInfoChangedCallback(null);
        Intent intent = new Intent(this, SignupActivity.class);
        if(accountExtras != null)
            intent.putExtras(accountExtras);
        startActivity(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        facebookHelper.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onResume() {
        super.onResume();
        facebookHelper.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        facebookHelper.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        facebookHelper.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        facebookHelper.onSaveInstanceState(outState);
    }

}
