package br.com.mobiclub.quantoprima.ui.activity;

import android.os.Bundle;

import javax.inject.Inject;

import br.com.mobiclub.quantoprima.core.service.MobiClubServiceProvider;
import br.com.mobiclub.quantoprima.facade.Facade;
import br.com.mobiclub.quantoprima.ui.activity.account.AuthenticationListener;
import br.com.mobiclub.quantoprima.ui.activity.account.AuthenticatorTask;
import br.com.mobiclub.quantoprima.ui.common.MobiClubActivity;
import br.com.mobiclub.quantoprima.util.Ln;

/**
 * Base class for all Activities that need fragments.
 */
public abstract class MobiClubFragmentActivity extends InjectorActivity
        implements AuthenticationListener, MobiClubActivity {

    @Inject protected MobiClubServiceProvider serviceProvider;
    private Facade facade;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayout());
        facade = Facade.getInstance();
    }

    @Override
    public void setContentView(final int layoutResId) {
        super.setContentView(layoutResId);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    protected boolean isUserHasAuthenticated() {
        return facade.isUserLogged();
    }

    protected void checkAuth() {
        if(!isUserHasAuthenticated() && !(this instanceof CentralActivity) &&
                !(this instanceof WebViewActivity)) {
            AuthenticatorTask authenticatorTask = new AuthenticatorTask(serviceProvider, this);
            authenticatorTask.checkAuth(); //async
        } else {
            initScreen();
        }
    }

    @Override
    public void onAuthenticationFail() {
        Ln.e("onAuthenticationFail");
    }

    @Override
    public void onAuthentication(boolean authenticated) {
        if(authenticated) {
            initScreen();
        }
    }

    @Override
    public void onAuthenticationCancelled() {
        finish();
    }

    /**
     * Callback para quando o processo de autenticacao
     * eh realizado
     *
     * @param authenticated se foi autenticado ou nao
     *
     * @doc authentication
     */
    protected void authCallback(boolean authenticated) {
        if(authenticated)
            initScreen();
    }

    /**
     * Quando sucesso na autenticacao
     */
    protected void initScreen() {

    }
}

