package br.com.mobiclub.quantoprima.events;

import br.com.mobiclub.quantoprima.core.service.api.entity.ApiError;
import br.com.mobiclub.quantoprima.core.service.api.entity.ConstantsApi;
import br.com.mobiclub.quantoprima.core.service.util.Util;
import retrofit.RetrofitError;

/**
 * Error that is posted when a non-network error event occurs in the {@link retrofit.RestAdapter}
 */
public class RestErrorEvent {

    public static final int HTTP_NOT_FOUND = 404;
    public static final int HTTP_OK = 200;

    private RetrofitError cause;
    private ApiError apiError;

    public RestErrorEvent(RetrofitError cause) {
        this.cause = cause;
    }

    public RetrofitError getCause() {
        return cause;
    }

    public boolean isRewardBuyFailed() {
        boolean failed = false;

        if(Util.urlContains(cause, ConstantsApi.Http.URL_REWARD_BUY_BUY)) {
            failed = true;
        }

        return failed;
    }

    public ApiError getApiError() {
        try {
            ApiError err = (ApiError) cause.getBodyAs(ApiError.class);
            return err;
        } catch (Exception e) {
        }
        return null;
    }

    public boolean isGainScoreFailed() {
        boolean failed = false;

        if(Util.urlContains(cause, ConstantsApi.Http.URL_GAIN_SCORE_QR_CODE)) {
            failed = true;
        }

        return failed;
    }

    public boolean isRequestPassword() {
        boolean failed = false;

        if(Util.urlContains(cause, ConstantsApi.Http.URL_USER_REQUEST_PASSWORD)) {
            failed = true;
        }

        return failed;
    }

    public boolean isProfileEdit() {
        boolean failed = false;

        if(Util.urlContains(cause, ConstantsApi.Http.URL_USER_EDIT)) {
            failed = true;
        }

        return failed;
    }
}
