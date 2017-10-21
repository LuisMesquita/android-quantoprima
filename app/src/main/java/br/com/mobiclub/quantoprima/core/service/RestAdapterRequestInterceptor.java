package br.com.mobiclub.quantoprima.core.service;


import br.com.mobiclub.quantoprima.core.service.api.entity.ConstantsApi;
import br.com.mobiclub.quantoprima.domain.MobiClubUser;
import br.com.mobiclub.quantoprima.domain.UserAgentProvider;
import br.com.mobiclub.quantoprima.facade.Facade;
import br.com.mobiclub.quantoprima.util.Ln;
import retrofit.RequestInterceptor;

import static br.com.mobiclub.quantoprima.core.service.api.entity.ConstantsApi.Http.CURRENT_VERSION_APP;
import static br.com.mobiclub.quantoprima.core.service.api.entity.ConstantsApi.Http.HASH_APP;

public class RestAdapterRequestInterceptor implements RequestInterceptor {

    private UserAgentProvider userAgentProvider;

    public RestAdapterRequestInterceptor(UserAgentProvider userAgentProvider) {
        this.userAgentProvider = userAgentProvider;
    }

    @Override
    public void intercept(RequestFacade request) {

        // Add header to set content type of JSON
        request.addHeader("Content-Type", "application/json");

        request.addHeader("Content-Length", "0");

        // Add auth info for PARSE, normally this is where you'd add your auth info for this request (if needed).
        //request.addHeader(HEADER_PARSE_REST_API_KEY, PARSE_REST_API_KEY);
        //request.addHeader(HEADER_PARSE_APP_ID, PARSE_APP_ID);

        request.addQueryParam("hashApp", HASH_APP);
        request.addQueryParam("currentVersionApp", CURRENT_VERSION_APP);
        MobiClubUser loggedUser = Facade.getInstance().getLoggedUser();
        if(loggedUser != null) {
            String stringId = String.valueOf(loggedUser.getUserId());
            request.addQueryParam(ConstantsApi.Http.PARAM_USER_ID, stringId);
        } else {
            Ln.e("Nenhum usuario logado. " + request.toString());
        }

        // Add the user agent to the request.
        request.addHeader("User-Agent", userAgentProvider.get());
    }
}
