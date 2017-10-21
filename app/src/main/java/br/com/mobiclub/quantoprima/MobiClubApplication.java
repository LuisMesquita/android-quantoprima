

package br.com.mobiclub.quantoprima;

import android.app.Application;
import android.app.Instrumentation;
import android.content.Context;

//import com.google.android.gms.analytics.GoogleAnalytics;
//import com.google.android.gms.analytics.Logger;
//import com.google.android.gms.analytics.Tracker;
import com.onesignal.OSPermissionSubscriptionState;
import com.onesignal.OneSignal;
import com.parse.Parse;
import com.parse.ParseInstallation;
import com.uservoice.uservoicesdk.Config;
import com.uservoice.uservoicesdk.UserVoice;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import br.com.mobiclub.quantoprima.config.ApiLevel;
import br.com.mobiclub.quantoprima.config.Injector;
import br.com.mobiclub.quantoprima.config.module.RootModule;
import br.com.mobiclub.quantoprima.core.service.api.entity.Establishment;
import br.com.mobiclub.quantoprima.core.service.api.entity.EstablishmentLocation;
import br.com.mobiclub.quantoprima.core.service.api.entity.PushNotificationGift;
import br.com.mobiclub.quantoprima.database.MobiClubDatabase;
import br.com.mobiclub.quantoprima.domain.LanguageEnum;
import br.com.mobiclub.quantoprima.domain.MobiClubUser;
import br.com.mobiclub.quantoprima.util.Ln;

/**
 * MobiClub application
 */
public class MobiClubApplication extends Application {

    public static final String APPLICATION_ID = "cYUlg4gE6r7E1sbzi3OO40PGFYu1kZ7AWawRatpJ";
    public static final String CLIENT_KEY = "4aW4h6O3veQMEecomZLZPJKjDTvFmYBZfSNPMmnq";
    private static final String PROPERTY_ID = "UA-53880050-1";
    private static MobiClubApplication instance;
    public static MobiClubUser mobiUser = null;
    private LanguageEnum languageEnum;

    private int language;
    public static PushNotificationGift pushNotificationGift = null;
    private boolean syncingRunning;
    private int so = 1;
    private boolean firstTime;
    private String languageString;
    public static int pontosGanhados = 0;
    private Establishment establishment;
    private EstablishmentLocation establishmentLocation;
    public static Integer statusHttp = 0;

    public void setLanguage(int language) {
        this.language = language;
    }

    public String getLanguageDefaultString() {
        return LanguageEnum.PT.name();
    }

    public void setLanguageString(String languageString) {
        this.languageString = languageString;
    }

    public String getLanguageString() {
        return languageString;
    }

    public void setEstablishment(Establishment establishment) {
        if(establishment != null)
            this.establishment = establishment;
    }

    public Establishment getEstablishment() {
        return establishment;
    }

    public void setEstablishmentLocation(EstablishmentLocation establishmentLocation) {
        if(establishmentLocation != null)
            this.establishmentLocation = establishmentLocation;
    }

    public EstablishmentLocation getEstablishmentLocation() {
        return establishmentLocation;
    }

    /**
     * Enum used to identify the tracker that needs to be used for tracking.
     *
     * A single tracker is usually enough for most purposes. In case you do need multiple trackers,
     * storing them all in Application object helps ensure that they are created only once per
     * application instance.
     */
    public enum TrackerName {
        APP_TRACKER, // Tracker used only in this app.
        GLOBAL_TRACKER, // Tracker used by all the apps from a company. eg: roll-up tracking.
        MOBICLUB_TRACKER, // Tracker used by all mobiclub transactions.
    }

//    HashMap<TrackerName, Tracker> mTrackers = new HashMap<TrackerName, Tracker>();

    /**
     * Create main application
     */
    public MobiClubApplication() {
        //TODO: enum this
        languageEnum = LanguageEnum.PT;
        language = 1;
        languageString = LanguageEnum.PT.name();
    }

    /**
     * Create main application
     *
     * @param context
     */
    public MobiClubApplication(final Context context) {
        this();
        attachBaseContext(context);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;
        Locale ptBr = new Locale("pt", "BR"); //Locale para o Brasil
        Locale.setDefault(ptBr);

        //
        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();

        OSPermissionSubscriptionState status = OneSignal.getPermissionSubscriptionState();
        status.getPermissionStatus().getEnabled();

        status.getSubscriptionStatus().getSubscribed();
        status.getSubscriptionStatus().getUserSubscriptionSetting();
        status.getSubscriptionStatus().getUserId();
        status.getSubscriptionStatus().getPushToken();

        MobiClubDatabase.createDatabase(this);

        Config config = new Config("coffeeshopquantoprima.uservoice.com");
        UserVoice.init(config, this);

        Ln.d("Inicializando PARSER...");
        Parse.initialize(this, APPLICATION_ID, CLIENT_KEY);
        ParseInstallation.getCurrentInstallation().saveInBackground();
        String dt = ParseInstallation.getCurrentInstallation().getString("deviceToken");
        Ln.d("Device token = %s", dt);
        Ln.d("PARSER incializado...");

        // Perform injection
        Injector.init(getModules(), this);

        firstTime = false;

    }

    private List<Object> getModules() {
        List<Object> modules = new ArrayList<Object>();
        modules.add(getRootModule());
        modules.add(getApiLevel().module());
        return modules;
    }

    protected ApiLevel getApiLevel() {
        return ApiLevel.PRODUCTION;
    }

    private Object getRootModule() {
        return new RootModule();
    }

    /**
     * Create main application
     *
     * @param instrumentation
     */
    public MobiClubApplication(final Instrumentation instrumentation) {
        this();
        attachBaseContext(instrumentation.getTargetContext());
    }

//    public synchronized Tracker getTracker(TrackerName trackerId) {
//        if (!mTrackers.containsKey(trackerId)) {
//
//            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
//            analytics.getLogger().setLogLevel(Logger.LogLevel.VERBOSE);
//            Tracker t = (trackerId == TrackerName.APP_TRACKER) ? analytics.newTracker(PROPERTY_ID)
//                    : (trackerId == TrackerName.GLOBAL_TRACKER) ? analytics.newTracker(
//                    R.xml.global_tracker)
//                    : analytics.newTracker(R.xml.mobiclub_tracker);
//            t.enableAdvertisingIdCollection(true);
//            mTrackers.put(trackerId, t);
//        }
//        return mTrackers.get(trackerId);
//    }

    public static MobiClubApplication getInstance() {
        return instance;
    }

    public int getLanguage() {
        return language;
    }

    public synchronized boolean isSyncingRunning() {
        return syncingRunning;
    }

    public synchronized void setSyncingRunning(boolean syncingRunning) {
        this.syncingRunning = syncingRunning;
    }

    public int getSO() {
        return so;
    }

    public void setFirstTime(boolean firstTime) {
        this.firstTime = firstTime;
    }

    public boolean isFirstTime() {
        return firstTime;
    }

}
