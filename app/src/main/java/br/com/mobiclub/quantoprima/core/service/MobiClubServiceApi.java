package br.com.mobiclub.quantoprima.core.service;

import java.util.List;

import br.com.mobiclub.quantoprima.core.service.api.entity.ApiResult;
import br.com.mobiclub.quantoprima.core.service.api.entity.Cardapio;
import br.com.mobiclub.quantoprima.core.service.api.entity.Establishment;
import br.com.mobiclub.quantoprima.core.service.api.entity.EstablishmentWrapper;
import br.com.mobiclub.quantoprima.core.service.api.entity.ExtractDetails;
import br.com.mobiclub.quantoprima.core.service.api.entity.GainMobiResponse;
import br.com.mobiclub.quantoprima.core.service.api.entity.Gift;
import br.com.mobiclub.quantoprima.core.service.api.entity.LocationDetails;
import br.com.mobiclub.quantoprima.core.service.api.entity.LocationReward;
import br.com.mobiclub.quantoprima.core.service.api.entity.Notification;
import br.com.mobiclub.quantoprima.core.service.api.entity.NotifyLocationsWrapper;
import br.com.mobiclub.quantoprima.core.service.api.entity.Reward;
import br.com.mobiclub.quantoprima.core.service.api.entity.Survey;
import br.com.mobiclub.quantoprima.core.service.api.entity.User;
import br.com.mobiclub.quantoprima.domain.SurveyReply;

/**
 * Facade do servico. Agrupa todos os metodos.
 */
public interface MobiClubServiceApi {

    public List<User> getUsers();

    public User signin(String email, String password);

    public EstablishmentWrapper getEstablishmentsByApp(String search, double lat, double lon);

    public LocationDetails getLocation(int locationId);

    public List<LocationReward> getRewardsByLocation(int idLocation);

    public List<ExtractDetails> getUserMobisExtract(int idLocation);

    public User signup(String name, String email, String surname, String gender, String password, String confirmPassword, String birth, String cpf);

    public ApiResult requestPassword(String email);

    public List<Reward> getRewardsByUser();

    public ApiResult rescueRewardBuy(int idToRescue, String qrCode);

    public ApiResult rescueRewardShot(int idToRescue, String qrCode);

    public ApiResult rescueRewardGift(int idToRescue, String qrCode);

    public ApiResult buyReward(int buyId, int locationId);

    public GainMobiResponse shotReward(String qrCode);

    public GainMobiResponse gainOnlineScoreQRCode(int netStatus, String qrCode);

    public GainMobiResponse gainOfflineScore(String code, int lessMinute);

    public Cardapio getCardapioByLocation(int idLocation);

    public Survey getSurveyByLocation(int id);

    public ApiResult answerSurvey(SurveyReply surveyReply);

    public User signinFacebook(long facebookId, String name,
                               String email, String birthday,
                               String hometown, String location,
                               String gender, String locale, String accessToken,
                               long accessExpires);

    public List<Notification> getNotifications(int page);

    public ApiResult readNotifyMessage(Integer notifyId);

    public ApiResult editUser(String name, String birthday, String gender, String cpf);

    public NotifyLocationsWrapper getNotifyLocations();

    public ApiResult notifyUnBlock(int locationId);

    public ApiResult notifyBlock(int locationId, int reason);

    public Gift getGiftById(int giftId);

    public ApiResult rewardGiftAccept(Integer giftId);

    public ApiResult rewardGiftReject(Integer giftId);

    public Integer getNewNotifications();

    public ApiResult facebookPost(Long facebookId, int type,
                                  String comment, int locationId, int reference, int estId);

    public List<Establishment> getLocationsPositions();

    public ApiResult removeNotifyMessage(Integer notifyId);
    public ApiResult PostRewardGiftDiscart(Integer giftId);
    public ApiResult PostRewardShotDiscart(Integer shotId);
    public ApiResult PostRewardBuyDiscart(Integer shotId);

    public User PostUpdateCPF(int userId, String cpf);
}
