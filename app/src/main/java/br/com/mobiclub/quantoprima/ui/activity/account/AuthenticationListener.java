package br.com.mobiclub.quantoprima.ui.activity.account;

/**
 *
 */
public interface AuthenticationListener {
    public void onAuthentication(boolean authenticated);

    public void onAuthenticationCancelled();

    public void onAuthenticationFail();
}
