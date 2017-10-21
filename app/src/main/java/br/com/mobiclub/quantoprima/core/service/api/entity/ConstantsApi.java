

package br.com.mobiclub.quantoprima.core.service.api.entity;

/**
 * Bootstrap constants
 */
public final class ConstantsApi {
    private ConstantsApi() {}

    public static final class Auth {
        private Auth() {}

        /**
         * Account type id
         */
        public static final String BOOTSTRAP_ACCOUNT_TYPE = "br.com.mobiclub.quantoprima";

        /**
         * Account name
         */
        public static final String BOOTSTRAP_ACCOUNT_NAME = "MobiClub";

        /**
         * Provider id
         */
        public static final String BOOTSTRAP_PROVIDER_AUTHORITY = "br.com.mobiclub.quantoprima.sync";

        /**
         * Auth token type
         */
        public static final String AUTHTOKEN_TYPE = BOOTSTRAP_ACCOUNT_TYPE;
    }

    /**
     * All HTTP is done through a REST style API built for demonstration purposes on Parse.com
     * Thanks to the nice people at Parse for creating such a nice system for us to use for bootstrap!
     */
    public static final class Http {

        private Http() {}

        /**
         * Base URL for all requests
         */
        public static final String MOBICLUB_URL_BASE = "http://mobiclub.com.br/api/Service";

//        public static final String MOBICLUB_URL_BASE = "http://54.213.206.238/api/Service";

        //staging ip
        //public static final String MOBICLUB_URL_BASE = "http://54.186.228.61/api/Service";

        /**
         * Authentication URL
         */
        public static final String URL_USER_SIGNIN = "/PostLoginByEmail";
        public static final String URL_USER_SIGNUP = "/PostCreateUserByEmail";

        public static final String PARAM_USER_NAME = "name";
        public static final String PARAM_USER_EMAIL = "email";
        public static final String PARAM_USER_SURNAME = "surname";
        public static final String PARAM_USER_GENDER = "gender";
        public static final String PARAM_USER_PASSWORD = "password";
        public static final String PARAM_USER_PASSWORD2 = "password2";
        public static final String PARAM_USER_BIRTH = "birth";
        public static final String PARAM_USER_CPF = "cpf";

        public static final String URL_USER_REQUEST_PASSWORD = "/PostRequestPassword";

        /**
         * List Users URL
         */
        public static final String URL_USERS_FRAG =  "/1/users";

        /**
         * List News URL
         */
        public static final String URL_NEWS_FRAG = "/1/classes/News";


        /**
         * List Checkin's URL
         */
        public static final String URL_CHECKINS_FRAG = "/1/classes/Locations";

        /**
         * PARAMS for auth
         */
        public static final String PARAM_USERNAME = "email";
        public static final String PARAM_PASSWORD = "password";


        public static final String PARSE_APP_ID = "zHb2bVia6kgilYRWWdmTiEJooYA17NnkBSUVsr4H";
        public static final String PARSE_REST_API_KEY = "N2kCY1T3t3Jfhf9zpJ5MCURn3b25UpACILhnf5u9";
        public static final String HEADER_PARSE_REST_API_KEY = "X-Parse-REST-API-Key";
        public static final String HEADER_PARSE_APP_ID = "X-Parse-Application-Id";
        public static final String CONTENT_TYPE_JSON = "application/json";
        public static final String USERNAME = "name";
        public static final String PASSWORD = "password";
        public static final String SESSION_TOKEN = "sessionToken";

        public static final String HASH_APP = "7vwITD7QRMABPwR7j7SavpV4rbh97fdc";
        public static final String CURRENT_VERSION_APP = "1.3";

        //home
        public static final String URL_ESTABLISHMENTS = "/PostEstablishmentByApp";
        public static final String PARAM_USER_ID = "userId";
        public static final String PARAM_SEARCH = "search";
        public static final String PARAM_LAT = "lat";
        public static final String PARAM_LON = "lon";

        //loja
        public static final String URL_LOCATION = "/PostLocation";
        public static final String PARAM_ID_LOCATION = "idLocation";
        public static final String URL_REWARDS_BY_LOCATION = "/PostRewardsByLocation";
        public static final String URL_USER_MOBIS_EXTRACT = "/PostExtract";
        public static final String PARAM_LOCATION_ID = "locationId";

        //user rewardsByUser
        public static final String URL_REWARDS_BY_USER = "/PostRewardsByUser";

        public static final String URL_REWARD_BUY_RESCUE = "/PostRewardBuy_Rescue";
        public static final String URL_REWARD_SHOT_RESCUE = "/PostRewardShot_Rescue";
        public static final String URL_REWARD_GIFT_RESCUE = "/PostRewardGift_Rescue";

        public static final String PARAM_ID_RESCUE = "idRescue";
        public static final String PARAM_QR_CODE = "qrCode";

        //buy
        public static final String URL_REWARD_BUY_PREFFIX = "_Buy";
        public static final String URL_REWARD_BUY_BUY = "/PostRewardBuy_Buy";
        public static final String PARAM_ID_BUY = "buyId";

        //gain qrcode
        public static final String URL_GAIN_SCORE_QR_CODE = "/PostGainScoreQrCode";
        public static final String PARAM_NET_STATUS = "netStatus";
        public static final String PARAM_SO = "so";
        public static final String PARAM_CODE = "code";
        public static final String PARAM_LESS_MINUTE = "lessMinute";

        public static final String URL_CARDAPIO_BY_LOCATION = "/PostStoreByLocation";
        public static final String PARAM_ID_LANGUAGE = "idLanguage";

        public static final String URL_SURVEY_BY_LOCATION = "/PostSurveyByLocation";

        public static final String URL_USER_SIGNIN_FACEBOOK = "/PostLoginFacebookUser";
        public static final String PARAM_FACEBOOK_ID = "facebookId";
        public static final String PARAM_USER_BIRTHDAY = "birthday";
        public static final String PARAM_USER_HOMETOWN = "hometown";
        public static final String PARAM_USER_LOCATION = "location";
        public static final String PARAM_USER_LOCALE = "locale";
        public static final String PARAM_FACEBOOK_ACCESS_TOKEN = "facebookAccessToken";
        public static final String PARAM_FACEBOOK_ACCESS_EXPIRES = "facebookAccessExpires";

        public static final String URL_USER_NOTIFICATIONS = "/PostNotifyByUser";

        public static final String PARAM_PAGE = "page";

        public static final String URL_USER_READ_NOTIFY_MESSAGE = "/PostNotifyReadMessage";
        public static final String PARAM_NOTIFY_ID = "notifyId";

        public static final String URL_REWARD_SHOT_SHOT = "/PostRewardShot_Shot";

        public static final String URL_SURVEY_ANSWER = "/PostSurveyAnswer";
        public static final String PARAM_SURVEY_ANSWERS = "answers";
        public static final String PARAM_SURVEY_APPRECIATIONS = "appreciations";

        public static final String URL_USER_EDIT = "/PostEditUser";

        public static final String URL_NOTIFY_LOCATIONS = "/PostGetNotifyLocations";

        public static final String URL_NOTIFY_UNBLOCK = "/PostNotifyUnBlock";
        public static final String URL_NOTIFY_BLOCK = "/PostNotifyBlock";
        public static final String PARAM_REASON_ID = "reasonId";

        public static final String URL_GET_GIFT_BY_ID = "/PostGiftById";
        public static final String PARAM_GIFT_ID = "giftId";

        public static final String URL_REWARD_GIFT_ACCEPT = "/PostRewardGiftAccept";
        public static final String URL_REWARD_GIFT_REJECT = "/PostRewardGiftDiscart";

        public static final String PARAM_HASH = "hash";

        public static final String URL_FACEBOOK_POST = "/PostFacebookPost";
        public static final String PARAM_FB_ID = "fbId";
        public static final String PARAM_TYPE = "type";
        public static final String PARAM_COMMENT = "comment";
        public static final String PARAM_REFERENCE = "reference";
        public static final String PARAM_EST_ID = "estId";

        public static final String URL_LOCATIONS_POSITIONS = "/PostLocationsMap";

        public static final String URL_UPDATE_CPF = "/PostUpdateCPF";
        public static final String PARAM_UP_USER_ID = "userId";
        public static final String PARAM_UP_USER_CPF = "Cpf";

        public static final String URL_REMOVER_NOTIFICACAO = "/PostNotifyRemoveMessage";

        public static final String URL_PostRewardGiftDiscart = "/PostRewardGiftDiscart";

        public static final String URL_PostRewardShotDiscart = "/PostRewardShotDiscart";
        public static final String URL_PostRewardBuyDiscart = "/PostRewardBuyDiscart";
        public static final String PARAM_buyId = "buyId";
        public static final String PARAM_giftId = "giftId";
        public static final String PARAM_shotId = "shotId";
    }

}


