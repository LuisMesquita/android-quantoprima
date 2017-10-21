package br.com.mobiclub.quantoprima.core.service.api.service;

import br.com.mobiclub.quantoprima.core.service.api.entity.ConstantsApi;
import br.com.mobiclub.quantoprima.core.service.api.entity.ConstantsApi.Http;
import br.com.mobiclub.quantoprima.core.service.api.entity.GainMobiResponse;
import retrofit.http.POST;
import retrofit.http.Query;

/**
 *
 */
public interface GainScoreService {

    @POST(ConstantsApi.Http.URL_GAIN_SCORE_QR_CODE)
    GainMobiResponse gainScoreQRCode(@Query(Http.PARAM_NET_STATUS) int netStatus,
                              @Query(Http.PARAM_SO) int so,
                              @Query(Http.PARAM_CODE) String qrCode,
                              @Query(Http.PARAM_HASH) int hourSeconds,
                              @Query(Http.PARAM_LESS_MINUTE) long lessMinute);

}
