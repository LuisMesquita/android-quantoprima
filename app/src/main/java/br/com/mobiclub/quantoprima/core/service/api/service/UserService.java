package br.com.mobiclub.quantoprima.core.service.api.service;

import br.com.mobiclub.quantoprima.core.service.api.entity.ApiResult;
import br.com.mobiclub.quantoprima.core.service.api.entity.ConstantsApi.Http;
import br.com.mobiclub.quantoprima.core.service.api.entity.Extract;
import br.com.mobiclub.quantoprima.core.service.api.entity.NotificationWrapper;
import br.com.mobiclub.quantoprima.core.service.api.entity.NotifyLocationsWrapper;
import br.com.mobiclub.quantoprima.core.service.api.entity.User;
import br.com.mobiclub.quantoprima.core.service.api.entity.UsersWrapper;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;
/**
 * User service for connecting the the REST API and
 * getting the users.
 */
public interface UserService {

    @GET(Http.URL_USERS_FRAG)
    UsersWrapper getUsers();

    /**
     * The {@link retrofit.http.Query} values will be transform into query string paramters
     * via Retrofit
     *
     * @param email The users email
     * @param password The users password
     * @return A login response.
     */
    @POST(Http.URL_USER_SIGNIN)
    User signin(@Query(Http.PARAM_USERNAME) String email,
                @Query(Http.PARAM_PASSWORD) String password);

    @POST(Http.URL_USER_MOBIS_EXTRACT)
    public Extract getExtract(@Query(Http.PARAM_LOCATION_ID) int idLocation);

    @POST(Http.URL_USER_SIGNUP)
    public User signup(@Query(Http.PARAM_USER_NAME) String name,
                          @Query(Http.PARAM_USER_EMAIL)String email,
                          @Query(Http.PARAM_USER_SURNAME) String surname,
                          @Query(Http.PARAM_USER_GENDER) String gender,
                          @Query(Http.PARAM_USER_PASSWORD) String password,
                          @Query(Http.PARAM_USER_PASSWORD2) String confirmPassword,
                          @Query(Http.PARAM_USER_BIRTH) String birth,
                          @Query(Http.PARAM_USER_CPF) String cpf);

    @POST(Http.URL_USER_REQUEST_PASSWORD)
    public ApiResult requestPassword(@Query(Http.PARAM_USER_EMAIL) String email);

    @POST(Http.URL_USER_SIGNIN_FACEBOOK)
    public User signinFacebook(@Query(Http.PARAM_FACEBOOK_ID) long facebookId,
                                    @Query(Http.PARAM_USER_NAME) String name,
                                    @Query(Http.PARAM_USER_EMAIL) String email,
                                    @Query(Http.PARAM_USER_BIRTHDAY) String birthday,
                                    @Query(Http.PARAM_USER_HOMETOWN) String hometown,
                                    @Query(Http.PARAM_USER_LOCATION) String location,
                                    @Query(Http.PARAM_USER_GENDER) String gender,
                                    @Query(Http.PARAM_USER_LOCALE) String locale,
                                    @Query(Http.PARAM_FACEBOOK_ACCESS_TOKEN) String accessToken,
                                    @Query(Http.PARAM_FACEBOOK_ACCESS_EXPIRES) long accessExpires);

    @POST(Http.URL_USER_NOTIFICATIONS)
    public NotificationWrapper getNotifications(@Query(Http.PARAM_PAGE) int page);

    @POST(Http.URL_USER_READ_NOTIFY_MESSAGE)
    public ApiResult readNotifyMessage(@Query(Http.PARAM_NOTIFY_ID) Integer notifyId);

    @POST(Http.URL_USER_EDIT)
    public ApiResult editUser(@Query(Http.PARAM_USER_NAME) String name,
                              @Query(Http.PARAM_USER_BIRTH) String birthday,
                              @Query(Http.PARAM_USER_GENDER) String gender,
                              @Query(Http.PARAM_USER_CPF) String cpf);

    @POST(Http.URL_NOTIFY_LOCATIONS)
    public NotifyLocationsWrapper getNotifyLocations();

    @POST(Http.URL_FACEBOOK_POST)
    public ApiResult facebookPost(@Query(Http.PARAM_FB_ID) Long facebookId,
                                  @Query(Http.PARAM_TYPE) int type,
                                  @Query(Http.PARAM_COMMENT) String comment,
                                  @Query(Http.PARAM_LOCATION_ID) int locationId,
                                  @Query(Http.PARAM_REFERENCE) int reference,
                                  @Query(Http.PARAM_EST_ID) int estId);
    @POST(Http.URL_UPDATE_CPF)
    public User updateCpf(@Query(Http.PARAM_UP_USER_CPF) String cpf);

    @POST(Http.URL_REMOVER_NOTIFICACAO)
    public ApiResult removeNotifyMessage(@Query(Http.PARAM_NOTIFY_ID) Integer notifyId);


    @POST(Http.URL_PostRewardBuyDiscart)
    public ApiResult removePostRewardBuyDiscart(@Query(Http.PARAM_buyId) Integer notifyId);

    @POST(Http.URL_PostRewardGiftDiscart)
    public ApiResult removePostRewardGiftDiscart(@Query(Http.PARAM_giftId) Integer notifyId);

    @POST(Http.URL_PostRewardShotDiscart)
    public ApiResult removePostRewardShotDiscart(@Query(Http.PARAM_shotId) Integer notifyId);


}
