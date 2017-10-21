package br.com.mobiclub.quantoprima.config.module;

import com.google.gson.Gson;

import javax.inject.Singleton;

import br.com.mobiclub.quantoprima.core.service.MobiClubService;
import br.com.mobiclub.quantoprima.core.service.MobiClubServiceApi;
import br.com.mobiclub.quantoprima.core.service.RestAdapterRequestInterceptor;
import br.com.mobiclub.quantoprima.core.service.RestErrorHandler;
import br.com.mobiclub.quantoprima.core.service.api.entity.ConstantsApi;
import dagger.Module;
import dagger.Provides;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;
/**
 */
@Module(library=true, complete = false, includes = {ServiceModule.class})
public class MobiClubServiceModule {

    @Provides
    @Singleton
    MobiClubServiceApi provideMobiClubService(RestAdapter restAdapter) {
        return new MobiClubService(restAdapter);
    }

    @Provides
    @Singleton
    RestAdapter provideRestAdapter(RestErrorHandler restErrorHandler,
                                   RestAdapterRequestInterceptor restRequestInterceptor,
                                   Gson gson) {
        return new RestAdapter.Builder()
                .setEndpoint(ConstantsApi.Http.MOBICLUB_URL_BASE)
                .setErrorHandler(restErrorHandler)
                .setRequestInterceptor(restRequestInterceptor)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setConverter(new GsonConverter(gson))
                .build();
    }

}
