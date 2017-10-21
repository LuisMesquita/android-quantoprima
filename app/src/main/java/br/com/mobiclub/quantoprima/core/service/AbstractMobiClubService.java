package br.com.mobiclub.quantoprima.core.service;

import java.util.List;

import br.com.mobiclub.quantoprima.MobiClubApplication;
import br.com.mobiclub.quantoprima.core.service.api.entity.ApiResult;
import br.com.mobiclub.quantoprima.core.service.api.entity.Cardapio;
import br.com.mobiclub.quantoprima.core.service.api.entity.Establishment;
import br.com.mobiclub.quantoprima.core.service.api.entity.EstablishmentMapWrapper;
import br.com.mobiclub.quantoprima.core.service.api.entity.EstablishmentWrapper;
import br.com.mobiclub.quantoprima.core.service.api.entity.Extract;
import br.com.mobiclub.quantoprima.core.service.api.entity.ExtractDetails;
import br.com.mobiclub.quantoprima.core.service.api.entity.GainMobiResponse;
import br.com.mobiclub.quantoprima.core.service.api.entity.Gift;
import br.com.mobiclub.quantoprima.core.service.api.entity.LocationDetails;
import br.com.mobiclub.quantoprima.core.service.api.entity.LocationReward;
import br.com.mobiclub.quantoprima.core.service.api.entity.LocationRewardWrapper;
import br.com.mobiclub.quantoprima.core.service.api.entity.Notification;
import br.com.mobiclub.quantoprima.core.service.api.entity.NotificationWrapper;
import br.com.mobiclub.quantoprima.core.service.api.entity.NotifyLocationsWrapper;
import br.com.mobiclub.quantoprima.core.service.api.entity.Reward;
import br.com.mobiclub.quantoprima.core.service.api.entity.RewardWrapper;
import br.com.mobiclub.quantoprima.core.service.api.entity.Survey;
import br.com.mobiclub.quantoprima.core.service.api.entity.User;
import br.com.mobiclub.quantoprima.core.service.api.exception.AppBlockedException;
import br.com.mobiclub.quantoprima.core.service.api.exception.AppOutdatedException;
import br.com.mobiclub.quantoprima.core.service.api.exception.UserBlockedException;
import br.com.mobiclub.quantoprima.core.service.api.service.EstablishmentService;
import br.com.mobiclub.quantoprima.core.service.api.service.GainScoreService;
import br.com.mobiclub.quantoprima.core.service.api.service.LocationService;
import br.com.mobiclub.quantoprima.core.service.api.service.RewardService;
import br.com.mobiclub.quantoprima.core.service.api.service.UserService;
import br.com.mobiclub.quantoprima.domain.Mobi;
import br.com.mobiclub.quantoprima.domain.SurveyReply;
import retrofit.RestAdapter;

/**
 *
 */
public class AbstractMobiClubService implements MobiClubServiceApi {
    protected RestAdapter restAdapter;
    private LocationService locationService;
    private RewardService rewardService;
    private EstablishmentService establishmentService;
    private UserService userService;
    private GainScoreService gainScoreService;
    private Integer newNotification = null;

    private UserService getUserService() {
        if(userService == null)
            userService = getRestAdapter().create(UserService.class);
        return userService;
    }

    private EstablishmentService getEstablishmentService() {
        if(establishmentService == null)
            establishmentService = getRestAdapter().create(EstablishmentService.class);
        return establishmentService;
    }

    private LocationService getLocationService() {
        if(locationService == null)
            locationService = getRestAdapter().create(LocationService.class);
        return locationService;
    }

    private RewardService getRewardService() {
        if(rewardService == null)
            rewardService = getRestAdapter().create(RewardService.class);
        return rewardService;
    }

    private GainScoreService getGainScoreService() {
        if(gainScoreService == null)
            gainScoreService = getRestAdapter().create(GainScoreService.class);
        return gainScoreService;
    }

    private RestAdapter getRestAdapter() {
        return restAdapter;
    }

    /**
     * Get all bootstrap Users that exist on Parse.com
     */
    @Override
    public List<User> getUsers() {
        return getUserService().getUsers().getResults();
    }

    @Override
    public User signin(String email, String password) {
        User signin = getUserService().signin(email, password);
        checkState(signin.getHttpStatus(), signin.getMessage());
        return signin;
    }

    @Override
    public EstablishmentWrapper getEstablishmentsByApp(String search, double lat, double lon) {
        EstablishmentService establishmentService = getEstablishmentService();
        EstablishmentWrapper establishmentsByApp = establishmentService.getEstablishmentsByApp(search, lat, lon);
        checkState(establishmentsByApp.getHttpStatus(), establishmentsByApp.getMessage());
        Integer newNotificationServer = establishmentsByApp.getNewNotifys();
        newNotification = 0;
        if(newNotificationServer != null && newNotificationServer > 0) {
            newNotification = newNotificationServer;
        }
        return establishmentsByApp;
    }

    private void checkState(Integer httpStatus, String message) {
        if(httpStatus == null) {
            return;
        }
        if(httpStatus == 505) {
            throw new AppOutdatedException(message);
        } else if(httpStatus == 403) {
            throw new UserBlockedException(message);
        } else if(httpStatus == 401) {
            throw new AppBlockedException(message);
        }
    }

    @Override
    public Integer getNewNotifications() {
        return newNotification;
    }

    @Override
    public LocationDetails getLocation(int locationId) {
        LocationDetails location = getEstablishmentService().getLocation(locationId);
        checkState(location.getHttpStatus(), location.getMessage());
        return location;
    }

    @Override
    public List<LocationReward> getRewardsByLocation(int idLocation) {
        LocationRewardWrapper rewardsByLocation = getEstablishmentService().getRewardsByLocation(idLocation);
        checkState(rewardsByLocation.getHttpStatus(), rewardsByLocation.getMessage());
        List<LocationReward> locationRewards = rewardsByLocation.getLocationRewards();
        return locationRewards;
    }

    @Override
    public List<ExtractDetails> getUserMobisExtract(int idLocation) {
        Extract extract = getUserService().getExtract(idLocation);
        checkState(extract.getHttpStatus(), extract.getMessage());
        return extract.getExtractDetails();
    }

    @Override
    public User signup(String name, String email, String surname, String gender,
                       String password, String confirmPassword, String birth, String cpf) {
        User user = getUserService().signup(name, email, surname, gender,
                password, confirmPassword, birth, cpf);
        checkState(user.getHttpStatus(), user.getMessage());
        return user;
    }

    @Override
    public ApiResult requestPassword(String email) {
        ApiResult apiResult = getUserService().requestPassword(email);
        checkState(apiResult.getHttpStatus(), apiResult.getMessage());
        return apiResult;
    }

    @Override
    public List<Reward> getRewardsByUser() {
        RewardWrapper rewardWrapper = getRewardService().rewardsByUser();
        checkState(rewardWrapper.getHttpStatus(), rewardWrapper.getMessage());
        return rewardWrapper.getRewards();
    }

    @Override
    public ApiResult rescueRewardBuy(int idToRescue, String qrCode) {
        ApiResult apiResult = getRewardService().rescueRewardBuy(idToRescue, qrCode);
        checkState(apiResult.getHttpStatus(), apiResult.getMessage());
        return apiResult;
    }

    @Override
    public ApiResult rescueRewardShot(int idToRescue, String qrCode) {
        ApiResult apiResult = getRewardService().rescueRewardShot(idToRescue, qrCode);
        checkState(apiResult.getHttpStatus(), apiResult.getMessage());
        return apiResult;
    }

    @Override
    public ApiResult rescueRewardGift(int idToRescue, String qrCode) {
        ApiResult apiResult = getRewardService().rescueRewardGift(idToRescue, qrCode);
        checkState(apiResult.getHttpStatus(), apiResult.getMessage());
        return apiResult;
    }

    @Override
    public ApiResult buyReward(int buyId, int locationId) {
        ApiResult apiResult = getRewardService().buyReward(buyId, locationId);
        checkState(apiResult.getHttpStatus(), apiResult.getMessage());
        return apiResult;
    }

    @Override
    public GainMobiResponse shotReward(String qrCode) {
        GainMobiResponse gainMobiResponse = getRewardService().shotReward(qrCode);
        checkState(gainMobiResponse.getHttpStatus(), gainMobiResponse.getMessage());
        return gainMobiResponse;
    }

    @Override
    public GainMobiResponse gainOnlineScoreQRCode(int netStatus, String qrCode) {
        MobiClubApplication app = MobiClubApplication.getInstance();
        //ultimo parametro sempre zero
        long millis = System.currentTimeMillis();
        int hourSeconds = (int) (millis / 1000);
        GainMobiResponse gainMobiResponse = getGainScoreService().gainScoreQRCode(netStatus, app.getSO(),
                qrCode, hourSeconds, 0);
        checkState(gainMobiResponse.getHttpStatus(), gainMobiResponse.getMessage());
        return gainMobiResponse;
    }

    @Override
    public GainMobiResponse gainOfflineScore(String qrCode, int lessMinute) {
        MobiClubApplication app = MobiClubApplication.getInstance();
        return gainScoreQRCode(Mobi.MOBI_OFFLINE_TYPE, qrCode, lessMinute);
    }

    private GainMobiResponse gainScoreQRCode(int netStatus, String qrCode, long lessMinute) {
        MobiClubApplication app = MobiClubApplication.getInstance();
        long millis = System.currentTimeMillis();
        int hourSeconds = (int) (millis / 1000);
        GainMobiResponse gainMobiResponse = getGainScoreService().gainScoreQRCode(netStatus, app.getSO(),
                qrCode, hourSeconds, lessMinute);
        checkState(gainMobiResponse.getHttpStatus(), gainMobiResponse.getMessage());
        return gainMobiResponse;
    }

    @Override
    public Cardapio getCardapioByLocation(int idLocation) {
        int language = MobiClubApplication.getInstance().getLanguage();
        Cardapio cardapioByLocation = getLocationService().getCardapioByLocation(idLocation, language);
        checkState(cardapioByLocation.getHttpStatus(), cardapioByLocation.getMessage());
        return cardapioByLocation;
    }

    @Override
    public Survey getSurveyByLocation(int idLocation) {
        Survey surveyByLocation = getLocationService().getSurveyByLocation(idLocation);
        checkState(surveyByLocation.getHttpStatus(), surveyByLocation.getMessage());
        return surveyByLocation;
    }

    @Override
    public ApiResult answerSurvey(SurveyReply surveyReply) {
        if(surveyReply == null)
            throw new IllegalArgumentException("surveyReply == null");
        return getLocationService().answerSurvey(surveyReply.getAnswersJSON(),
                surveyReply.getAppreciationsJSON());
    }

    @Override
    public User signinFacebook(long facebookId, String name, String email,
                               String birthday, String hometown, String location,
                               String gender, String locale, String accessToken,
                               long accessExpires) {
        User user = getUserService().signinFacebook(facebookId, name, email, birthday,
                hometown, location, gender, locale, accessToken, accessExpires);
        checkState(user.getHttpStatus(), user.getMessage());
        return user;
    }

    @Override
    public List<Notification> getNotifications(int page) {
        NotificationWrapper wrapper = getUserService().getNotifications(page);
        checkState(wrapper.getHttpStatus(), wrapper.getMessage());
        return wrapper.getNotifications();
    }

    @Override
    public ApiResult readNotifyMessage(Integer notifyId) {
        ApiResult apiResult = getUserService().readNotifyMessage(notifyId);
        checkState(apiResult.getHttpStatus(), apiResult.getMessage());
        return apiResult;
    }

    @Override
    public ApiResult editUser(String name, String birthday, String gender, String cpf) {
        ApiResult apiResult = getUserService().editUser(name, birthday, gender, cpf);
        checkState(apiResult.getHttpStatus(), apiResult.getMessage());
        return apiResult;
    }

    @Override
    public NotifyLocationsWrapper getNotifyLocations() {
        NotifyLocationsWrapper notifyLocations = getUserService().getNotifyLocations();
        checkState(notifyLocations.getHttpStatus(), notifyLocations.getMessage());
        return notifyLocations;
    }

    @Override
    public ApiResult notifyUnBlock(int locationId) {
        ApiResult apiResult = getLocationService().notifyUnBlock(locationId);
        checkState(apiResult.getHttpStatus(), apiResult.getMessage());
        return apiResult;
    }

    @Override
    public ApiResult notifyBlock(int locationId, int reason) {
        ApiResult apiResult = getLocationService().notifyBlock(locationId, reason);
        checkState(apiResult.getHttpStatus(), apiResult.getMessage());
        return apiResult;
    }

    @Override
    public Gift getGiftById(int giftId) {
        Gift giftById = getRewardService().getGiftById(giftId);
        checkState(giftById.getHttpStatus(), giftById.getMessage());
        return giftById;
    }

    @Override
    public ApiResult rewardGiftAccept(Integer giftId) {
        ApiResult apiResult = getRewardService().rewardGiftAccept(giftId);
        checkState(apiResult.getHttpStatus(), apiResult.getMessage());
        return apiResult;
    }

    @Override
    public ApiResult rewardGiftReject(Integer giftId) {
        ApiResult apiResult = getRewardService().rewardGiftReject(giftId);
        checkState(apiResult.getHttpStatus(), apiResult.getMessage());
        return apiResult;
    }

    @Override
    public ApiResult facebookPost(Long facebookId, int type, String comment, int locationId,
                                  int reference, int estId) {
        ApiResult apiResult = getUserService().facebookPost(facebookId, type, comment,
                locationId, reference, estId);
        checkState(apiResult.getHttpStatus(), apiResult.getMessage());
        return apiResult;
    }

    @Override
    public List<Establishment> getLocationsPositions() {
        EstablishmentMapWrapper wrapper = getLocationService().getLocationsPositions();
        checkState(wrapper.getHttpStatus(), wrapper.getMessage());
        return wrapper.getEstablishments();
    }
    @Override
    public User PostUpdateCPF(int userId, String cpf) {
        User user = getUserService().updateCpf(cpf);
        checkState(user.getHttpStatus(), user.getMessage());
        return user;
    }

    @Override
    public ApiResult removeNotifyMessage(Integer notifyId) {
        ApiResult apiResult = getUserService().removeNotifyMessage(notifyId);
        checkState(apiResult.getHttpStatus(), apiResult.getMessage());
        return apiResult;
    }

    @Override
    public ApiResult PostRewardGiftDiscart(Integer giftId) {
        ApiResult apiResult = getUserService().removePostRewardGiftDiscart(giftId);
        checkState(apiResult.getHttpStatus(), apiResult.getMessage());
        return apiResult;
    }

    @Override
    public ApiResult PostRewardShotDiscart(Integer shotId) {
        ApiResult apiResult = getUserService().removePostRewardShotDiscart(shotId);
        checkState(apiResult.getHttpStatus(), apiResult.getMessage());
        return apiResult;
    }

    @Override
    public ApiResult PostRewardBuyDiscart(Integer shotId) {
        ApiResult apiResult = getUserService().removePostRewardBuyDiscart(shotId);
        checkState(apiResult.getHttpStatus(), apiResult.getMessage());
        return apiResult;
    }
}
