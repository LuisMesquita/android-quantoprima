

package br.com.mobiclub.quantoprima.domain;

/**
 * Bootstrap constants
 */
public final class Constants {

    public static final String MOBICLUB_PHONE = "08130253883";

    private Constants() {}

    public static final class Extra {

        public static final String MENU_ACTION = "menu_action";
        public static final String NOTIFICATION_COUNT = "notification_count";
        public static final String NOTIFICATION_PAGE = "notification_page";
        public static final String CONNECTION_STATE = "connection_state";
        public static final String GPSBAR_SHOW = "gps_bar_show";
        public static final String PREFS = "mobiclub_prefs";
        public static final String FACEBOOK_SIGNIN_BUTTON = "facebook_signin_button";
        public static final String USER_NAME = "user_name";
        public static final String REASON_USER_BLOCKED = "reason_user_blocked";
        public static final String LOCATIONS_MAPS = "locations_maps";
        public static final String HAS_GIFT = "has_gift";
        public static final String MOBI_SYNCED_ERROR = "mobi_synced_error";
        public static final String CARDAPIO_CATEGORY = "cardapio_category";
        public static final String LAST_TRY_SYNC = "last_try_sync";

        private Extra() {}

        public static final String CARDAPIO_LANGUAGES = "cardapio_languages";

        public static final String NEWS_ITEM = "news_item";

        public static final String USER = "user";

        //public static final String ESTABLISHMENT = "establishment_item";

        //public static final String LOCATION = "location_item";

        //public static final String REWARD = "reward";

        public static final String DIALOG_RESULTER = "dialog_layouter";

        public static final String QR_CODE = "qr_code";

        //public static final String CARDAPIO_CATEGORY = "cardapio_category";
        //public static final String CARDAPIO_ITEM = "cardapio_item";

        public static final String MOBI_SYNC_BROADCAST_ACTION = "br.com.mobiclub.fastfood.service.SYNC_BROADCAST";
        public static final String MOBI_SYNCED = "br.com.mobiclub.fastfood.service.QUANTITY";

        public static final String NOTIFICATION = "notification";

        public static final String QRCODE_READ_TYPE = "qrcode_read_type";

        public static final String QUERY_LOCATION = "query_location";

        public static final String NO_CONTENT_LAYOUT = "no_content_layout";

        public static final String GIFT = "gift";
        public static final String TAKE_GIFT_ERROR = "take_gift_error";

        public static final String WEB_VIEW_URL = "web_view_url";
        public static final String WEB_VIEW_TITLE = "web_view_title";
        public static final String LANG_TITLE = "lang_title";
    }

    public static final class URLMobiClub {

        private URLMobiClub() {}

        //MENU
        public static String USE_TERMS = "http://www.mobiclub.com.br/TermosDeUso.aspx";

        public static String POLICY_PRIVACY = "http://www.mobiclub.com.br/PoliticaDePrivacidade.aspx";

        public static String HELP = "http://www.mobiclub.uservoice.com/knowledgebase";

        public static String FALE_COM_A_MOBICLUB = "https://www.mobiclub.uservoice.com/clients/widgets/classic_widget";

        //MENU -> "Sobre a MobiClub"
        public static String INDIQUE_LOJA = "https://mobiclub.uservoice.com/forums/204676-central-de-sugestoes";

        public static String FANPAGE_FACEBOOK = "http://www.facebook.com/mobiclubbrasil";

        public static String FANPAGE_TWITTER = "http://www.twitter.com/mobiclubbrasil";

        public static String FANPAGE_INSTAGRAM = "http://www.instagram.com/mobiclubbrasil";

        public static String SITE = "http://www.mobiclub.com.br";

        public static String AVALIE_NA_PLAY_STORE = "https://play.google.com/store/apps/details?id=br.com.mobiclub.fastfood";

    }

    public static final class URL {

        private URL() {}

        //MENU
        public static String USE_TERMS = "http://www.mobiclub.com.br/TermosDeUso.aspx";

        public static String POLICY_PRIVACY = "http://www.mobiclub.com.br/PoliticaDePrivacidade.aspx";

        public static String HELP = "http://www.quantoprima.com.br";

        public static String FALE_COM_A_MOBICLUB = "https://www.mobiclub.uservoice.com/clients/widgets/classic_widget";

        //MENU -> "Sobre a MobiClub"
        public static String INDIQUE_LOJA = "https://mobiclub.uservoice.com/forums/204676-central-de-sugestoes";

        public static String FANPAGE_FACEBOOK = "https://www.facebook.com/quantoprima.plaza";

        public static String FANPAGE_TWITTER = "https://twitter.com/quanto_prima";

        public static String FANPAGE_INSTAGRAM = "http://instagram.com/quantoprima";

        public static String SITE = "http://www.quantoprima.com.br";

        public static String AVALIE_NA_PLAY_STORE = "http://play.google.com/store/apps/details?id=br.com.mobiclub.quantoprima";

        //TELA CRIAR CONTA
        public static String TERMS_AND_PRIVACY_POLICY = "http://54.213.206.238/termopolitica/quantoprima";

        //​TELA RECOMPENSAS VAZIA
        public static String COMO_GANHAR_RECOMPENSA = "http://www.quantoprima.com.br";

        public static final String APP_INATIVO = "http://www.quantoprima.com.br";
    }

    public static final class Intent {
        private Intent() {}

        /**
         * Action prefix for all intents created
         */
        public static final String INTENT_PREFIX = "br.com.mobiclub.fastfood.";

    }

    public static class Notification {
        private Notification() {
        }

        public static final int TIMER_NOTIFICATION_ID = 1000; // Why 1000? Why not? :)
    }

    public class Loader {
        public static final int LIST = 0;
        public static final int CARDAPIO = 1;
        public static final int CARDAPIO_LIST = 2;
        public static final int STORE_INFORMATION = 3;
        public static final int SURVEY_FRAGMENT = 4;
    }
}


