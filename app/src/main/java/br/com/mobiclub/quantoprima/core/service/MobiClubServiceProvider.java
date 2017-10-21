
package br.com.mobiclub.quantoprima.core.service;

import android.accounts.AccountsException;
import android.app.Activity;
import android.app.Service;

import java.io.IOException;

import br.com.mobiclub.quantoprima.domain.MobiClubUser;
import br.com.mobiclub.quantoprima.facade.Facade;
import br.com.mobiclub.quantoprima.service.authenticator.ApiKeyProvider;
import br.com.mobiclub.quantoprima.util.Ln;
import retrofit.RestAdapter;

/**
 * Provider for a {@link br.com.mobiclub.quantoprima.core.service.MobiClubService} instance
 */
public class MobiClubServiceProvider {

    private MobiClubServiceApi mobiClubService;
    private RestAdapter restAdapter;
    private ApiKeyProvider keyProvider;
    private Facade facade = Facade.getInstance();

    public MobiClubServiceProvider(MobiClubServiceApi mobiClubService,
                                   RestAdapter restAdapter, ApiKeyProvider keyProvider) {
        this.mobiClubService = mobiClubService;
        this.restAdapter = restAdapter;
        this.keyProvider = keyProvider;
    }

    /**
     * Get service for configured key provider
     * <p/>
     * This method gets an auth key and so it blocks and shouldn't be called on the main thread.
     *
     * @return bootstrap service
     * @throws IOException
     * @throws AccountsException
     */
    public MobiClubServiceApi getService(final Activity activity)
            throws IOException, AccountsException {
        if(facade.getLoggedUser() != null) {
            return mobiClubService;
        }
        // The call to keyProvider.getAuthKey(...) is what initiates the login screen. Call that now.
        Ln.d("Pegando token.");
        String token = keyProvider.getAuthKey(activity);
        Ln.d("Token=%s", token);
        if(token == null) {
            return null;
        }
        if(!setUser(token)) return null;
        // TODO: See how that affects the bootstrap service.
        return mobiClubService;
    }

    public boolean setUser(String token) {
        if(token != null) {
            MobiClubUser user = facade.getUserByToken(token);
            if(user != null) {
                Ln.e("Usuario encontrado = %s", user);
                facade.setLoggedUser(user);
            } else {
                //TODO: ver casos para isso quando usuario removido por exemplo
                facade.setLoggedUser(new MobiClubUser(Integer.parseInt(token)));
                Ln.e("Nenhum usuario encontrado, mas token == %s", token);
            }
        } else {
            facade.setLoggedUser(null);
            Ln.e("Nenhum usuario encontrado e token == null");
            return false;
        }
        return true;
    }

    public MobiClubServiceApi getService(Service service) {
        return mobiClubService;
    }

    public MobiClubServiceApi getService() {
        return mobiClubService;
    }

    public boolean checkAuth(Activity activity) {
        Ln.d("Pegando token com checkAuth.");
        String token = keyProvider.peekAuthKey(activity);
        Ln.d("Token=%s", token);
        return setUser(token);
    }

}
