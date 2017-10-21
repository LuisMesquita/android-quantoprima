

package br.com.mobiclub.quantoprima.service.authenticator;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerFuture;
import android.accounts.AccountsException;
import android.app.Activity;
import android.os.Bundle;

import java.io.IOException;

import static android.accounts.AccountManager.KEY_AUTHTOKEN;
import static br.com.mobiclub.quantoprima.core.service.api.entity.ConstantsApi.Auth.AUTHTOKEN_TYPE;
import static br.com.mobiclub.quantoprima.core.service.api.entity.ConstantsApi.Auth.BOOTSTRAP_ACCOUNT_TYPE;

/**
 * Bridge class that obtains a API key for the currently configured account
 */
public class ApiKeyProvider {

    private AccountManager accountManager;

    public ApiKeyProvider(AccountManager accountManager) {
        this.accountManager = accountManager;
    }

    /**
     * This call blocks, so shouldn't be called on the UI thread.
     * This call is what makes the login screen pop up. If the user has
     * not logged in there will no accounts in the {@link android.accounts.AccountManager}
     * and therefore the Activity that is referenced in the
     * {@link MobiClubAccountAuthenticator} will get started.
     * If you want to remove the authentication then you can comment out the code below and return a string such as
     * "foo" and the authentication process will not be kicked off. Alternatively, you can remove this class
     * completely and clean up any references to the authenticator.
     *
     *
     * @return API key to be used for authorization with a
     * {@link br.com.mobiclub.quantoprima.core.service.MobiClubService} instance
     * @throws AccountsException
     * @throws IOException
     */
    public String getAuthKey(final Activity activity) throws AccountsException, IOException {
        final AccountManagerFuture<Bundle> accountManagerFuture
                = accountManager.getAuthTokenByFeatures(BOOTSTRAP_ACCOUNT_TYPE,
                AUTHTOKEN_TYPE, new String[0], activity, null, null, null, null);

        return accountManagerFuture.getResult().getString(KEY_AUTHTOKEN);
    }

    public String peekAuthKey(Activity activity) {
        //TODO: permitir apenas uma conta
        AccountManager accountManager = AccountManager.get(activity);
        Account[] accountsByType = accountManager.getAccountsByType(BOOTSTRAP_ACCOUNT_TYPE);
        if(accountsByType.length == 1) {
            Account account = accountsByType[0];
            return accountManager.peekAuthToken(account, AUTHTOKEN_TYPE);
        }
        return null;
    }
}
