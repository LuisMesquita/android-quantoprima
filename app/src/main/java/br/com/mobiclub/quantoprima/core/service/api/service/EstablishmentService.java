package br.com.mobiclub.quantoprima.core.service.api.service;

import br.com.mobiclub.quantoprima.core.service.api.entity.ConstantsApi.Http;
import br.com.mobiclub.quantoprima.core.service.api.entity.EstablishmentWrapper;
import br.com.mobiclub.quantoprima.core.service.api.entity.LocationDetails;
import br.com.mobiclub.quantoprima.core.service.api.entity.LocationRewardWrapper;
import retrofit.http.POST;
import retrofit.http.Query;
/**
 *
 */
public interface EstablishmentService {

    @POST(Http.URL_ESTABLISHMENTS)
    public EstablishmentWrapper getEstablishmentsByApp(@Query(Http.PARAM_SEARCH) String search,
                                                       @Query(Http.PARAM_LAT) double lat,
                                                       @Query(Http.PARAM_LON) double lon);

    @POST(Http.URL_LOCATION)
    public LocationDetails getLocation(@Query(Http.PARAM_ID_LOCATION) int idLocation);

    @POST(Http.URL_REWARDS_BY_LOCATION)
    public LocationRewardWrapper getRewardsByLocation(@Query(Http.PARAM_ID_LOCATION) int idLocation);
}
