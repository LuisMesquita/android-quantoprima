package br.com.mobiclub.quantoprima.config.module;

import android.content.Context;

import com.google.gson.Gson;

import javax.inject.Singleton;

import br.com.mobiclub.quantoprima.core.service.MobiClubServiceApi;
import br.com.mobiclub.quantoprima.core.service.RestAdapterRequestInterceptor;
import br.com.mobiclub.quantoprima.core.service.RestErrorHandler;
import br.com.mobiclub.quantoprima.core.service.RetrofitClientMock;
import br.com.mobiclub.quantoprima.core.service.api.entity.ConstantsApi;
import br.com.mobiclub.quantoprima.core.service.mock.MockMobiClubService;
import dagger.Module;
import dagger.Provides;
import retrofit.RestAdapter;
import retrofit.client.Client;
import retrofit.converter.GsonConverter;

/**
 *
 */
@Module(library=true, complete = false, includes = {ServiceModule.class})
public class MockMobiClubServiceModule {

    @Provides
    @Singleton
    MobiClubServiceApi provideMockMobiClubService(RestAdapter mockRestAdapter) {
        return new MockMobiClubService(mockRestAdapter);
    }

    @Provides
    @Singleton
    RestAdapter provideMockRestAdapter(RestErrorHandler restErrorHandler,
                                   RestAdapterRequestInterceptor restRequestInterceptor,
                                   Gson gson, Client mockClient) {
        return new RestAdapter.Builder()
                .setEndpoint(ConstantsApi.Http.MOBICLUB_URL_BASE)
                .setClient(mockClient)
                .setLogLevel(RestAdapter.LogLevel.NONE)
                .setErrorHandler(restErrorHandler)
                .setRequestInterceptor(restRequestInterceptor)
                .setConverter(new GsonConverter(gson))
                .build();
    }

    @Provides
    Client provideMockClient(final Context context) {
        return new RetrofitClientMock(context);
    }
}
