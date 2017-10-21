package br.com.mobiclub.quantoprima.ui.activity.account;

import android.accounts.OperationCanceledException;
import android.app.Activity;

import br.com.mobiclub.quantoprima.core.service.MobiClubServiceApi;
import br.com.mobiclub.quantoprima.core.service.MobiClubServiceProvider;
import br.com.mobiclub.quantoprima.util.SafeAsyncTask;

/**
 * Responsavel por executar a autenticacao no android.
 * Caso nao esteja autenticado a janela de autenticacao aparecera.
 *
 * Listeners deverao se registarem para receberem o resultado da autenticacao
 *
 */
public class AuthenticatorTask {

    private MobiClubServiceProvider serviceProvider;
    private Activity activity;
    private AuthenticationListener listener;

    public AuthenticatorTask(MobiClubServiceProvider serviceProvider, Activity activity) {
        this.serviceProvider = serviceProvider;
        this.activity = activity;
        try {
            listener = (AuthenticationListener) this.activity;
        } catch(ClassCastException e) {
            throw new RuntimeException("Para criar AuthenticatorTask a atividade " +
                    "deve implementar AuthenticationListener");
        }
    }

    public void checkAuth() {
        new SafeAsyncTask<Boolean>() {

            @Override
            public Boolean call() throws Exception {
                final MobiClubServiceApi svc = serviceProvider.getService(activity);
                return svc != null;
            }

            @Override
            protected void onException(final Exception e) throws RuntimeException {
                super.onException(e);
                if (e instanceof OperationCanceledException) {
                    // User cancelled the authentication process (back button, etc).
                    // Since auth could not take place, lets finish this activity.
                    listener.onAuthenticationCancelled();
                }
            }

            @Override
            protected void onSuccess(final Boolean hasAuthenticated) throws Exception {
                super.onSuccess(hasAuthenticated);
                if(hasAuthenticated) {
                    listener.onAuthentication(hasAuthenticated);
                } else {
                    //token == null
                    listener.onAuthenticationFail();
                }
            }
        }.execute();
    }

    public void checkLocalAuth() {
        new SafeAsyncTask<Boolean>() {

            @Override
            public Boolean call() throws Exception {
                return serviceProvider.checkAuth(activity);
            }

            @Override
            protected void onException(final Exception e) throws RuntimeException {
                super.onException(e);
                if (e instanceof OperationCanceledException) {
                    // User cancelled the authentication process (back button, etc).
                    // Since auth could not take place, lets finish this activity.
                    listener.onAuthenticationCancelled();
                }
            }

            @Override
            protected void onSuccess(final Boolean hasAuthenticated) throws Exception {
                super.onSuccess(hasAuthenticated);
                if(hasAuthenticated) {
                    listener.onAuthentication(hasAuthenticated);
                } else {
                    //token == null
                    listener.onAuthenticationFail();
                }
            }
        }.execute();
    }
}
