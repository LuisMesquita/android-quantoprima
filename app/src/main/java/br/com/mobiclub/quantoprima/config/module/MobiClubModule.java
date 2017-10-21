package br.com.mobiclub.quantoprima.config.module;

import com.squareup.otto.Bus;

import javax.inject.Singleton;

import br.com.mobiclub.quantoprima.MobiClubApplication;
import br.com.mobiclub.quantoprima.dao.MobiDAO;
import br.com.mobiclub.quantoprima.dao.UserDAO;
import br.com.mobiclub.quantoprima.events.PostFromAnyThreadBus;
import br.com.mobiclub.quantoprima.facade.Facade;
import br.com.mobiclub.quantoprima.facebook.FacebookFacade;
import br.com.mobiclub.quantoprima.service.MobiSyncingService;
import br.com.mobiclub.quantoprima.ui.activity.AboutActivity;
import br.com.mobiclub.quantoprima.ui.activity.AboutMobiClubActivity;
import br.com.mobiclub.quantoprima.ui.activity.CentralActivity;
import br.com.mobiclub.quantoprima.ui.activity.FullScreenMobiClubActivity;
import br.com.mobiclub.quantoprima.ui.activity.HomeActivity;
import br.com.mobiclub.quantoprima.ui.activity.LocationActivity;
import br.com.mobiclub.quantoprima.ui.activity.MapActivity;
import br.com.mobiclub.quantoprima.ui.activity.NewsActivity;
import br.com.mobiclub.quantoprima.ui.activity.QRCodeActivityMock;
import br.com.mobiclub.quantoprima.ui.activity.TutorialActivity;
import br.com.mobiclub.quantoprima.ui.activity.UserActivity;
import br.com.mobiclub.quantoprima.ui.activity.WebViewActivity;
import br.com.mobiclub.quantoprima.ui.activity.account.AccountBlockedActivity;
import br.com.mobiclub.quantoprima.ui.activity.account.CadastrarCpfActivity;
import br.com.mobiclub.quantoprima.ui.activity.account.CadastrarCpfToStoreActivity;
import br.com.mobiclub.quantoprima.ui.activity.account.LostPasswordActivity;
import br.com.mobiclub.quantoprima.ui.activity.account.SigninActivity;
import br.com.mobiclub.quantoprima.ui.activity.account.SigninEmailActivity;
import br.com.mobiclub.quantoprima.ui.activity.account.SigninFacebookHelper;
import br.com.mobiclub.quantoprima.ui.activity.account.SignupActivity;
import br.com.mobiclub.quantoprima.ui.activity.launch.LaunchActivity;
import br.com.mobiclub.quantoprima.ui.activity.launch.OutdatedActivity;
import br.com.mobiclub.quantoprima.ui.activity.launch.SplashActivity;
import br.com.mobiclub.quantoprima.ui.activity.mobi.MobiOfflineActivity;
import br.com.mobiclub.quantoprima.ui.activity.notification.NotificationItemActivity;
import br.com.mobiclub.quantoprima.ui.activity.notification.NotificationsActivity;
import br.com.mobiclub.quantoprima.ui.activity.notification.NotificationsConfigActivity;
import br.com.mobiclub.quantoprima.ui.activity.notification.PushNotificationsActivity;
import br.com.mobiclub.quantoprima.ui.activity.profile.ProfileActivity;
import br.com.mobiclub.quantoprima.ui.activity.profile.ProfileEditActivity;
import br.com.mobiclub.quantoprima.ui.activity.qrcode.CaptureActivity;
import br.com.mobiclub.quantoprima.ui.activity.reward.RescueRewardActivity;
import br.com.mobiclub.quantoprima.ui.activity.reward.RewardActivity;
import br.com.mobiclub.quantoprima.ui.activity.reward.RewardDetailActivity;
import br.com.mobiclub.quantoprima.ui.activity.store.CardapioActivity;
import br.com.mobiclub.quantoprima.ui.activity.store.CardapioItemActivity;
import br.com.mobiclub.quantoprima.ui.activity.store.StoreActivity;
import br.com.mobiclub.quantoprima.ui.activity.store.survey.SurveyActivity;
//import br.com.mobiclub.quantoprima.ui.fragment.MapFragment;
import br.com.mobiclub.quantoprima.ui.fragment.NavigationDrawerFragment;
import br.com.mobiclub.quantoprima.ui.fragment.UserListFragment;
import br.com.mobiclub.quantoprima.ui.fragment.home.HomeFragment;
import br.com.mobiclub.quantoprima.ui.fragment.notification.NotificationListFragment;
import br.com.mobiclub.quantoprima.ui.fragment.notification.NotifyLocationListFragment;
import br.com.mobiclub.quantoprima.ui.fragment.profile.ProfileEditFragment;
import br.com.mobiclub.quantoprima.ui.fragment.reward.RewardFragment;
import br.com.mobiclub.quantoprima.ui.fragment.store.CardapioFragment;
import br.com.mobiclub.quantoprima.ui.fragment.store.StoreFragment;
import br.com.mobiclub.quantoprima.ui.fragment.store.StoreFrontFragment;
import br.com.mobiclub.quantoprima.ui.fragment.store.StoreInformationFragment;
import br.com.mobiclub.quantoprima.ui.fragment.store.StoreRewardListFragment;
import br.com.mobiclub.quantoprima.ui.fragment.store.StoreUserMobisExtractListFragment;
import br.com.mobiclub.quantoprima.ui.fragment.store.StoreUserMobisFragment;
import br.com.mobiclub.quantoprima.ui.fragment.store.SurveyFragment;
import br.com.mobiclub.quantoprima.ui.helper.GainMobiHelper;
import dagger.Module;
import dagger.Provides;

/**
 * Dagger module for setting up provides statements.
 * Register all of your entry points below.
 */
@Module(
        complete = false,

        injects = {
                MobiClubApplication.class,

                //activities
                LaunchActivity.class,
                SplashActivity.class,
                OutdatedActivity.class,

                SigninActivity.class,
                SigninEmailActivity.class,
                SigninFacebookHelper.class,
                SignupActivity.class,
                LostPasswordActivity.class,
                AccountBlockedActivity.class,

                CadastrarCpfActivity.class,
                CadastrarCpfToStoreActivity.class,

                HomeActivity.class,
                MobiOfflineActivity.class,
                StoreActivity.class,
                CardapioActivity.class,
                CardapioItemActivity.class,
                SurveyActivity.class,
                CaptureActivity.class,
                NotificationsActivity.class,
                NotificationItemActivity.class,
                NewsActivity.class,
                UserActivity.class,
                ProfileActivity.class,
                ProfileEditActivity.class,
                NotificationsConfigActivity.class,
                PushNotificationsActivity.class,
                TutorialActivity.class,
                AboutActivity.class,
                AboutMobiClubActivity.class,
                WebViewActivity.class,
                CentralActivity.class,
                LocationActivity.class,
                MapActivity.class,
                FullScreenMobiClubActivity.class,
                QRCodeActivityMock.class,

                RewardActivity.class,
                RewardDetailActivity.class,
                RescueRewardActivity.class,

                //fragments
                HomeFragment.class,
                NotificationListFragment.class,

                StoreFragment.class,
                CardapioFragment.class,
                SurveyFragment.class,
                StoreInformationFragment.class,
                StoreFrontFragment.class,
                StoreUserMobisFragment.class,
                StoreRewardListFragment.class,
                StoreUserMobisFragment.class,
                StoreUserMobisExtractListFragment.class,
                ProfileEditFragment.class,
                NotifyLocationListFragment.class,

                RewardFragment.class,
//                MapFragment.class,

                NavigationDrawerFragment.class,
                UserListFragment.class,

                //services
                MobiSyncingService.class,

                MobiDAO.class,
                Facade.class,
                FacebookFacade.class,

                GainMobiHelper.class
        }
)
public class MobiClubModule {

    @Singleton
    @Provides
    Bus provideOttoBus() {
        return new PostFromAnyThreadBus();
    }

    @Singleton
    @Provides
    MobiDAO provideMobiDAO() {
        return new MobiDAO();
    }

    @Singleton
    @Provides
    UserDAO provideUserDAO() {
        return new UserDAO();
    }
}
