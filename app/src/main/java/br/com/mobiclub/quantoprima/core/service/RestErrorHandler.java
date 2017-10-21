package br.com.mobiclub.quantoprima.core.service;

import com.squareup.otto.Bus;

import br.com.mobiclub.quantoprima.core.service.api.entity.ApiError;
import br.com.mobiclub.quantoprima.core.service.api.entity.ConstantsApi;
import br.com.mobiclub.quantoprima.core.service.util.Util;
import br.com.mobiclub.quantoprima.events.NetworkErrorEvent;
import br.com.mobiclub.quantoprima.events.RestErrorEvent;
import br.com.mobiclub.quantoprima.events.SignupFailedEvent;
import br.com.mobiclub.quantoprima.events.UnAuthorizedErrorEvent;
import retrofit.ErrorHandler;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class RestErrorHandler implements ErrorHandler {

    public static final int HTTP_INVALID_REQUEST = 400;
    public static final int INVALID_LOGIN_PARAMETERS = 101;
    private static final int HTTP_NOT_FOUND = 404;
    private static final int HTTP_LENGTH_REQUIRED = 411;

    private Bus bus;
    private ApiError apiError;

    public RestErrorHandler(Bus bus) {
        this.bus = bus;
    }

    @Override
    public Throwable handleError(RetrofitError cause) {
        if(cause != null) {
            if (cause.isNetworkError()) {
                bus.post(new NetworkErrorEvent(cause));
            } else if(isUnAuthorized(cause)) {
                bus.post(new UnAuthorizedErrorEvent(apiError));
            } else if(isSignupFailed(cause)) {
                bus.post(new SignupFailedEvent(apiError));
            }
            else {
                bus.post(new RestErrorEvent(cause));
            }
        }

        // Example of how you'd check for a unauthorized result
        // if (cause != null && cause.getStatus() == 401) {
        //     return new UnauthorizedException(cause);
        // }

        // You could also put some generic error handling in here so you can start
        // getting analytics on error rates/etc. Perhaps ship your logs off to
        // Splunk, Loggly, etc

        return cause;
    }

    private boolean isSignupFailed(RetrofitError cause) {
        boolean signupFailed = false;

        Response response = cause.getResponse();
        if(response != null && response.getStatus() == HTTP_INVALID_REQUEST &&
                Util.urlContains(cause, ConstantsApi.Http.URL_USER_SIGNUP)) {
            final ApiError err = (ApiError) cause.getBodyAs(ApiError.class);
            signupFailed = true;
            apiError = err;
        }

        return signupFailed;
    }

    /**
     * If a user passes an incorrect name/password combo in we could
     * get a unauthorized error back from the API. On parse.com this means
     * we get back a HTTP 404 with an error as JSON in the body as such:
     *
     *  {
     *     code: 101,
     *     error: "invalid login parameters"
     *  }
     *
     *  }
     *
     * Therefore we need to check for the 101 and the 404.
     *
     * @param cause The initial error.
     * @return
     */
    private boolean isUnAuthorized(RetrofitError cause) {
        boolean authFailed = false;

        if(cause == null)
            return false;

        Response response = cause.getResponse();

        if(response == null)
            return false;

        int status = response.getStatus();
        if(status == HTTP_INVALID_REQUEST || status == HTTP_NOT_FOUND ||
                status == HTTP_LENGTH_REQUIRED) {
            try {
                final ApiError err = (ApiError) cause.getBodyAs(ApiError.class);
                if(Util.urlContains(cause, ConstantsApi.Http.URL_USER_SIGNIN)) {
                    authFailed = true;
                    apiError = err;
                }
            } catch (Exception e) {
            }
        }

        return authFailed;
    }

}
