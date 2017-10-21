
package br.com.mobiclub.quantoprima.service.authenticator;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import static android.accounts.AccountManager.ACTION_AUTHENTICATOR_INTENT;

/**
 * Authenticator service that returns a subclass of AbstractAccountAuthenticator in onBind().
 */
public class AccountAuthenticatorService extends Service {

    private static MobiClubAccountAuthenticator authenticator = null;

    @Override
    public IBinder onBind(final Intent intent) {
        if (intent != null && ACTION_AUTHENTICATOR_INTENT.equals(intent.getAction())) {
            return getAuthenticator().getIBinder();
        }
        return null;
    }

    private MobiClubAccountAuthenticator getAuthenticator() {
        if (authenticator == null) {
            authenticator = new MobiClubAccountAuthenticator(this);
        }
        return authenticator;
    }
}