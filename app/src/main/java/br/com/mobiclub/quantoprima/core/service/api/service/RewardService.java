package br.com.mobiclub.quantoprima.core.service.api.service;

import br.com.mobiclub.quantoprima.core.service.api.entity.ApiResult;
import br.com.mobiclub.quantoprima.core.service.api.entity.ConstantsApi.Http;
import br.com.mobiclub.quantoprima.core.service.api.entity.GainMobiResponse;
import br.com.mobiclub.quantoprima.core.service.api.entity.Gift;
import br.com.mobiclub.quantoprima.core.service.api.entity.RewardWrapper;
import retrofit.http.POST;
import retrofit.http.Query;
/**
 *
 */
public interface RewardService {

    @POST(Http.URL_REWARDS_BY_USER)
    public RewardWrapper rewardsByUser();

    @POST(Http.URL_REWARD_BUY_RESCUE)
    public ApiResult rescueRewardBuy(@Query(Http.PARAM_ID_RESCUE) int idToRescue,
                                     @Query(Http.PARAM_QR_CODE) String qrCode);

    @POST(Http.URL_REWARD_SHOT_RESCUE)
    public ApiResult rescueRewardShot(@Query(Http.PARAM_ID_RESCUE) int idToRescue,
                                      @Query(Http.PARAM_QR_CODE) String qrCode);

    @POST(Http.URL_REWARD_GIFT_RESCUE)
    public ApiResult rescueRewardGift(@Query(Http.PARAM_ID_RESCUE) int idToRescue,
                                      @Query(Http.PARAM_QR_CODE) String qrCode);

    @POST(Http.URL_REWARD_BUY_BUY)
    public ApiResult buyReward(@Query(Http.PARAM_ID_BUY) int buyId,
                               @Query(Http.PARAM_LOCATION_ID) int locationId);

    @POST(Http.URL_REWARD_SHOT_SHOT)
    public GainMobiResponse shotReward(@Query(Http.PARAM_QR_CODE) String qrCode);

    @POST(Http.URL_GET_GIFT_BY_ID)
    public Gift getGiftById(@Query(Http.PARAM_GIFT_ID) int giftId);

    @POST(Http.URL_REWARD_GIFT_ACCEPT)
    public ApiResult rewardGiftAccept(@Query(Http.PARAM_GIFT_ID) Integer giftId);

    @POST(Http.URL_REWARD_GIFT_REJECT)
    public ApiResult rewardGiftReject(@Query(Http.PARAM_GIFT_ID) Integer giftId);

}
