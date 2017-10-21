package br.com.mobiclub.quantoprima.events;

import br.com.mobiclub.quantoprima.core.service.api.entity.ApiError;

public class UnAuthorizedErrorEvent {
    private ApiError apiError;

    public UnAuthorizedErrorEvent(ApiError apiError) {
        this.apiError = apiError;
    }

    public ApiError getError() {
        return apiError;
    }
}
