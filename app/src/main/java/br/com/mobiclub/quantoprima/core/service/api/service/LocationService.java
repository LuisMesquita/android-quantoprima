package br.com.mobiclub.quantoprima.core.service.api.service;

import br.com.mobiclub.quantoprima.core.service.api.entity.ApiResult;
import br.com.mobiclub.quantoprima.core.service.api.entity.Cardapio;
import br.com.mobiclub.quantoprima.core.service.api.entity.ConstantsApi;
import br.com.mobiclub.quantoprima.core.service.api.entity.EstablishmentMapWrapper;
import br.com.mobiclub.quantoprima.core.service.api.entity.Survey;
import retrofit.http.POST;
import retrofit.http.Query;

/**
 *
 */
public interface LocationService {

    @POST(ConstantsApi.Http.URL_CARDAPIO_BY_LOCATION)
    public Cardapio getCardapioByLocation(@Query(ConstantsApi.Http.PARAM_ID_LOCATION) int idLocation,
                                   @Query(ConstantsApi.Http.PARAM_ID_LANGUAGE) int language);

    @POST(ConstantsApi.Http.URL_SURVEY_BY_LOCATION)
    public Survey getSurveyByLocation(@Query(ConstantsApi.Http.PARAM_ID_LOCATION) int idLocation);

    @POST(ConstantsApi.Http.URL_SURVEY_ANSWER)
    public ApiResult answerSurvey(@Query(ConstantsApi.Http.PARAM_SURVEY_ANSWERS) String answersJSON,
                                  @Query(ConstantsApi.Http.PARAM_SURVEY_APPRECIATIONS) String appreciationsJSON);

    @POST(ConstantsApi.Http.URL_NOTIFY_UNBLOCK)
    public ApiResult notifyUnBlock(@Query(ConstantsApi.Http.PARAM_LOCATION_ID) int locationId);

    @POST(ConstantsApi.Http.URL_NOTIFY_BLOCK)
    public ApiResult notifyBlock(@Query(ConstantsApi.Http.PARAM_LOCATION_ID) int locationId,
                                 @Query(ConstantsApi.Http.PARAM_REASON_ID) int reason);

    @POST(ConstantsApi.Http.URL_LOCATIONS_POSITIONS)
    public EstablishmentMapWrapper getLocationsPositions();
}
