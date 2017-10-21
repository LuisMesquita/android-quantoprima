package br.com.mobiclub.quantoprima.config.module;

import android.accounts.AccountManager;
import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.otto.Bus;

import javax.inject.Singleton;

import br.com.mobiclub.quantoprima.core.service.MobiClubServiceApi;
import br.com.mobiclub.quantoprima.core.service.MobiClubServiceProvider;
import br.com.mobiclub.quantoprima.core.service.RestAdapterRequestInterceptor;
import br.com.mobiclub.quantoprima.core.service.RestErrorHandler;
import br.com.mobiclub.quantoprima.domain.UserAgentProvider;
import br.com.mobiclub.quantoprima.service.authenticator.ApiKeyProvider;
import br.com.mobiclub.quantoprima.service.authenticator.LogoutService;
import dagger.Module;
import dagger.Provides;
import retrofit.RestAdapter;

/**
 *
 */
@Module(library = true, complete = false, includes = {MobiClubModule.class, AndroidModule.class})
public class ServiceModule {

    @Provides
    MobiClubServiceProvider provideBootstrapServiceProvider(MobiClubServiceApi mobiClubService,
                                                            RestAdapter restAdapter,
                                                            ApiKeyProvider apiKeyProvider) {
        return new MobiClubServiceProvider(mobiClubService, restAdapter, apiKeyProvider);
    }

    @Provides
    @Singleton
    LogoutService provideLogoutService(final Context context, final AccountManager accountManager) {
        return new LogoutService(context, accountManager);
    }

    @Provides
    ApiKeyProvider provideApiKeyProvider(AccountManager accountManager) {
        return new ApiKeyProvider(accountManager);
    }

    @Provides
    Gson provideGson() {
        /**
         * GSON instance to use for all request  with date format set up for proper parsing.
         * <p/>
         * You can also configure GSON with different naming policies for your API.
         * Maybe your API is Rails API and all json values are lower case with an underscore,
         * like this "first_name" instead of "firstName".
         * You can configure GSON as such below.
         * <p/>
         *
         * public static final Gson GSON = new GsonBuilder().setDateFormat("yyyy-MM-dd")
         *         .setFieldNamingPolicy(LOWER_CASE_WITH_UNDERSCORES).create();
         */
        //2014-07-31T23:59:59.997
        return new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
    }

    @Provides
    RestErrorHandler provideRestErrorHandler(Bus bus) {
        return new RestErrorHandler(bus);
    }

    @Provides
    RestAdapterRequestInterceptor provideRestAdapterRequestInterceptor(UserAgentProvider userAgentProvider) {
        return new RestAdapterRequestInterceptor(userAgentProvider);
    }

}
