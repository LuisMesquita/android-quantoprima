package br.com.mobiclub.quantoprima.events;

import br.com.mobiclub.quantoprima.core.service.api.entity.ApiError;

/**
 *
 */
public class SignupFailedEvent {
    private ApiError apiError;

    public SignupFailedEvent(ApiError apiError) {
        this.apiError = apiError;
    }

    public ApiError getApiError() {
        return apiError;
    }
}
