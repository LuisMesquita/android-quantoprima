package br.com.mobiclub.quantoprima.ui.activity;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerFuture;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.FacebookException;
import com.facebook.FacebookOperationCanceledException;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.widget.FacebookDialog;
import com.facebook.widget.WebDialog;
import com.squareup.otto.Subscribe;

import java.util.List;

import br.com.mobiclub.quantoprima.MobiClubApplication;
import br.com.mobiclub.quantoprima.R;
import br.com.mobiclub.quantoprima.core.service.api.entity.ConstantsApi;
import br.com.mobiclub.quantoprima.core.service.api.entity.Gift;
import br.com.mobiclub.quantoprima.domain.MobiClubUser;
import br.com.mobiclub.quantoprima.events.NavItemSelectedEvent;
import br.com.mobiclub.quantoprima.facade.Facade;
import br.com.mobiclub.quantoprima.facebook.FacebookFacade;
import br.com.mobiclub.quantoprima.service.MobiSyncingService;
//import br.com.mobiclub.quantoprima.service.map.PositionListener;
import br.com.mobiclub.quantoprima.ui.activity.account.AccountBlockedActivity;
import br.com.mobiclub.quantoprima.ui.activity.launch.LaunchActivity;
import br.com.mobiclub.quantoprima.ui.activity.notification.NotificationsActivity;
import br.com.mobiclub.quantoprima.ui.activity.profile.ProfileActivity;
import br.com.mobiclub.quantoprima.ui.activity.reward.RewardActivity;
import br.com.mobiclub.quantoprima.ui.factory.AlertDialogFactory;
import br.com.mobiclub.quantoprima.ui.fragment.NavigationDrawerFragment;
import br.com.mobiclub.quantoprima.ui.fragment.SearchListener;
import br.com.mobiclub.quantoprima.ui.fragment.common.ListScrollListener;
import br.com.mobiclub.quantoprima.ui.view.DialogResultData;
import br.com.mobiclub.quantoprima.ui.view.ErrorDialogListener;
import br.com.mobiclub.quantoprima.domain.Constants;
import br.com.mobiclub.quantoprima.util.Ln;
import br.com.mobiclub.quantoprima.util.NetworkUtil;
import br.com.mobiclub.quantoprima.util.SafeAsyncTask;
import br.com.mobiclub.quantoprima.util.UIUtils;
import butterknife.ButterKnife;

/**
 * Principal activity to represent all activities with menu
 *
 */
public abstract class MenuActivity extends MobiClubFragmentActivity
        implements ErrorDialogListener,
        ListScrollListener {

    protected ActionBarDrawerToggle drawerToggle;
    private DrawerLayout drawerLayout;
    private CharSequence drawerTitle;
    private CharSequence title;
    private NavigationDrawerFragment navigationDrawerFragment;

    private FacebookFacade facebook;

    //assuma primeiro que esta conectado
    private boolean isConnected = true;

    protected ImageView imgDegrade;

    private BroadcastReceiver networdReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(final Context context, final Intent intent) {
            String status = NetworkUtil.getConnectivityTypeString(context);
            //Toast.makeText(context, status, Toast.LENGTH_LONG).show();
            Ln.d(status);
            setConnected(NetworkUtil.isConnected(context));
        }

    };
    private boolean syncStarted = false;
    private boolean mobisOffline = false;

    private boolean userBlocked = false;
    protected LinearLayout gpsBar;
    private LinearLayout searchBar;
    private Facade facade;
    private TextView labelNotifCount;
    private int mNotifCount = 0;
    private TextView frameNotifCount;
    private EditText etSearch;
    private RelativeLayout container;
    protected boolean searching = false;
    protected FrameLayout darkener;
    private DialogResultData entitySharing;
    protected SharedPreferences preferences;
    private UiLifecycleHelper uiFacebookHelper;
    private Class<? extends Activity> nextActivity;
    protected boolean noResult = false;
    private int postType;
    protected Fragment fragment;

    protected FrameLayout btnGainMobis;
    protected ImageButton btnMoreMobis;
    private ImageView imgAlert;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

        super.onCreate(savedInstanceState);

        facade = Facade.getInstance();
        facebook = FacebookFacade.getInstance();
        uiFacebookHelper = new UiLifecycleHelper(this, null);
        uiFacebookHelper.onCreate(savedInstanceState);

        Intent intent = getIntent();
        customNavigation(intent);

        preferences = getSharedPreferences(Constants.Extra.PREFS, MODE_MULTI_PROCESS);

        if(savedInstanceState != null) {
            isConnected = savedInstanceState.getBoolean(Constants.Extra.CONNECTION_STATE);
            return;
        }

        // View injection with Butterknife
        ButterKnife.inject(this);

        // Set up navigation drawer
        title = drawerTitle = getTitle();
        int titleId = getResources().getIdentifier("action_bar_title", "id", "android");
        TextView titleTextView = (TextView) findViewById(titleId);
        if(titleTextView != null) {
            titleTextView.setTextColor(getResources().getColor(R.color.black));
            Typeface font = Typeface.createFromAsset(getAssets(), "fonts/bold.ttf");
            titleTextView.setTypeface(font);
        }

        if(!isTablet()) {
            drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawerToggle = new ActionBarDrawerToggle(
                    this,                    /* Host activity */
                    drawerLayout,            /* DrawerLayout object */
                    R.drawable.ic_drawer,    /* nav drawer icon to replace 'Up' caret */
                    R.string.navigation_drawer_open,    /* "open drawer" description */
                    R.string.navigation_drawer_close) { /* "close drawer" description */

                /** Called when a drawer has settled in a completely closed state. */
                public void onDrawerClosed(View view) {
                    getSupportActionBar().setTitle(title);
                    supportInvalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
                }

                /** Called when a drawer has settled in a completely open state. */
                public void onDrawerOpened(View drawerView) {
                    getSupportActionBar().setTitle(drawerTitle);
                    supportInvalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
                }
            };
            //estamos acessando o menu secundario por uma acao do
            //menu
            drawerToggle.setDrawerIndicatorEnabled(false);
            // Set the drawer toggle as the DrawerListener
            drawerLayout.setDrawerListener(drawerToggle);

            navigationDrawerFragment = (NavigationDrawerFragment)
                    getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);

            // Set up the drawer.
            navigationDrawerFragment.setUp(
                    R.id.navigation_drawer,
                    (DrawerLayout) findViewById(R.id.drawer_layout));

        }
        //cache ui
        configBars();

        darkener = (FrameLayout) findViewById(R.id.darkener);

        darkener.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelSearch();
                darkener.setVisibility(View.GONE);
            }
        });

        container = (RelativeLayout) findViewById(R.id.principal);

        btnGainMobis = (FrameLayout) findViewById(R.id.btn_gain_mobis);
        if(showGainMobis()) {
            btnGainMobis.setVisibility(View.VISIBLE);
        } else {
            btnGainMobis.setVisibility(View.GONE);
        }

        btnMoreMobis = (ImageButton) findViewById(R.id.btn_more_mobis);
        imgDegrade = (ImageView) findViewById(R.id.img_degrade);
        imgDegrade.setVisibility(View.GONE);

        imgAlert = (ImageView) findViewById(R.id.pontuar_alerta);
        AnimationDrawable animDrawable = (AnimationDrawable) imgAlert.getDrawable();
        if(animDrawable != null && !animDrawable.isRunning()) {
            animDrawable.start();
        }

        checkAuth();

        registerConnectionListener();
    }

    @Override
    public int getLayout() {
        if(isTablet()) {
            return R.layout.activity_principal;
        } else {
            return R.layout.activity_principal;
        }
    }

    protected boolean showGainMobis() {
        return false;
    }

    private void configBars() {
        gpsBar = (LinearLayout) findViewById(R.id.gps_bar);
        gpsBar.setVisibility(View.GONE);
        //melhorar isso
//        PositionListener positionListener = Facade.getInstance().getPositionListener();
//        if(positionListener != null)
//            positionListener.setGpsBar(gpsBar);
        searchBar = (LinearLayout) findViewById(R.id.search_bar);
        etSearch = (EditText) findViewById(R.id.et_search);
        if(etSearch != null) {
            searching = false;
            etSearch.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if(hasFocus) {
                        setDarkenerVisible();
                        darkener.getForeground().setAlpha(130);
                        searching = true;
                        etSearch.setHint("");
                    } else {
                        searching = false;
                        etSearch.setHint(getString(R.string.search_bar_hint));
                    }
                }
            });
        }
        searchBar.setVisibility(View.GONE);
    }

    protected void setDarkenerVisible() {
        darkener.setVisibility(View.VISIBLE);
        darkener.getForeground().setAlpha(130);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        customNavigation(intent);
    }

    protected void customNavigation(Intent intent) {
        boolean isMenuAction = intent.getBooleanExtra(Constants.Extra.MENU_ACTION, false);
        if(isMenuAction) {
            if(//Facade.getInstance().isParent(HomeActivity.class) &&
                    !(this instanceof HomeActivity)) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }
            else {
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                getSupportActionBar().setHomeButtonEnabled(false);
                //Facade.getInstance().clearParents();
            }
        } else if(!(this instanceof HomeActivity)) {
            if(Facade.getInstance().canBack()) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }
            //else {
            //    getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            //    getSupportActionBar().setHomeButtonEnabled(false);
            //}
        }
        intent.putExtra(Constants.Extra.MENU_ACTION, false);
        Facade.getInstance().newIntent(intent);
    }

    private void checkBlocked() {
        //TODO: verificar se conta foi bloqueada
        if(this instanceof HomeActivity && userBlocked) {
            startActivity(new Intent(this, AccountBlockedActivity.class));
            finish();
        }
    }

    protected void registerConnectionListener() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        registerReceiver(networdReceiver, filter);
    }

    protected boolean isTablet() {
        return UIUtils.isTablet(this);
    }

    @Override
    protected void onPostCreate(final Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        if(!isTablet()) {
            // Sync the toggle state after onRestoreInstanceState has occurred.
            drawerToggle.syncState();
        }
    }

    @Override
    public void onConfigurationChanged(final Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if(!isTablet()) {
            drawerToggle.onConfigurationChanged(newConfig);
        }
    }

    protected boolean isConnected() {
        return isConnected;
    }

    public boolean isConnected(boolean force) {
        if(force) {
            return NetworkUtil.isConnected(this);
        }
        return isConnected();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        isConnected = savedInstanceState.getBoolean(Constants.Extra.CONNECTION_STATE);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(Constants.Extra.CONNECTION_STATE, isConnected);
        uiFacebookHelper.onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        uiFacebookHelper.onDestroy();
        try {
            unregisterReceiver(networdReceiver);
        } catch (Exception e) {
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        uiFacebookHelper.onResume();
        btnMoreMobis.setImageResource(R.drawable.t08_btn_pontuar_default);
        isConnected = preferences.getBoolean(Constants.Extra.CONNECTION_STATE, true);
        syncMobis();
    }

    @Override
    protected void onPause() {
        super.onPause();
        uiFacebookHelper.onPause();
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(Constants.Extra.CONNECTION_STATE, isConnected); // value to store
        // Commit to storage
        editor.commit();
    }

    protected void setConnected(boolean isConnected) {
        boolean local = this.isConnected;
        this.isConnected = isConnected;
        //if(local != isConnected) {
        //    syncMobis();
        //    changeFragment(false);
        //}
    }

    @Override
    protected void onStart() {
        super.onStart();
        changeFragment(false);
    }


    private void addeFragment() {
        fragment = createFragment();
        if(fragment != null) {
            try {
                final FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction()
                        .replace(R.id.container, fragment);
                transaction.commit();
                Ln.e("Adicionando fragmento %s", fragment.toString());
            } catch (IllegalStateException e) {
                Ln.e(e);
            }
        }
    }

    @Override
    protected void initScreen() {
        checkBlocked();
        changeFragment(false);
    }

    private void changeFragment(boolean allowStateLoss) {
        this.fragment = createFragment();
        if(fragment != null) {
            try {
                final FragmentManager fragmentManager = getSupportFragmentManager();

                Fragment actual = fragmentManager.findFragmentById(R.id.container);
                if(actual != null) {
                    if(actual.getClass() != fragment.getClass()) {
                        FragmentTransaction transaction = fragmentManager.beginTransaction()
                                .replace(R.id.container, fragment);
                        if(!allowStateLoss) {
                            transaction.commit();
                        }
                        else {
                            transaction.commitAllowingStateLoss();
                        }
                        Ln.e("Usando fragmento %s", fragment.toString());
                    }
                } else {
                    FragmentTransaction transaction = fragmentManager.beginTransaction()
                            .add(R.id.container, fragment);
                    transaction.commit();
                    Ln.e("Adicionando fragmento %s", fragment.toString());
                }
            } catch (IllegalStateException e) {
                Ln.e(e);
            }
        }
    }

    private void cancelSearch() {
        searching = false;
        InputMethodManager imm = (InputMethodManager)getSystemService(
                Context.INPUT_METHOD_SERVICE);
        if(etSearch != null) {
            imm.hideSoftInputFromWindow(etSearch.getWindowToken(), 0);
            //etSearch.setText("");
            etSearch.setHint(getString(R.string.search_bar_hint));
            etSearch.clearFocus();
            if(this instanceof HomeActivity && noResult){
                Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.container);
                if(fragment != null && fragment instanceof SearchListener) {
                    etSearch.setText("");
                    SearchListener sl = (SearchListener) fragment;
                    sl.onQueryLocationChanged("");
                    noResult = false;
                }
            }
        }
    }

    protected void showSearchBar(boolean visible) {
        if(searchBar != null) {
            if(visible) {
                searchBar.setVisibility(View.VISIBLE);
            } else {
                searchBar.setVisibility(View.GONE);
            }
        }
    }

    public void showGPSBar(boolean show) {
        if(gpsBar == null) {
            return;
        }
        if(!show) {
            gpsBar.setVisibility(View.GONE);
            return;
        }
//        PositionListener positionListener = Facade.getInstance().getPositionListener();
//        if(positionListener != null) {
//            boolean gpsEnabled = positionListener.isGPSEnabled();
//            if(!gpsEnabled) {
//                gpsBar.setVisibility(View.VISIBLE);
//            } else {
//                gpsBar.setVisibility(View.GONE);
//            }
//        }

    }

    private void syncMobis() {
        Intent syncService = new Intent(this, MobiSyncingService.class);
        if(isConnected() && existsMobisOffline() && !isSyncingRunning()) {
            startService(syncService);
            syncStarted = true;
        }
    }

    private boolean isSyncingRunning() {
        MobiClubApplication app = MobiClubApplication.getInstance();
        if(syncStarted || app.isSyncingRunning()) {
            return true;
        }
        return false;
    }

    @Subscribe
    public abstract void onNavigationItemSelected(NavItemSelectedEvent event);

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        Ln.d("onOptionsItemSelected");
        if (item != null && item.getItemId() == R.id.menu_action) {
            if (drawerLayout.isDrawerOpen(Gravity.RIGHT)) {
                drawerLayout.closeDrawer(Gravity.RIGHT);
            } else {
                drawerLayout.openDrawer(Gravity.RIGHT);
            }
        }
        if (!isTablet() && drawerToggle.onOptionsItemSelected(item)) {
        }

        switch (item.getItemId()) {
            case android.R.id.home:
                //menuDrawer.toggleMenu();
                if(!(this instanceof HomeActivity)) {
                    //onBackPressed();
                    Intent parentActivityIntent = facade.getParent();
                    if(parentActivityIntent == null) {
                        parentActivityIntent = new Intent(this, HomeActivity.class);
                    }
                    parentActivityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                            Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(parentActivityIntent);
                    finish();
                }
                return true;
            case R.id.notification_action:
                navigateTo(NotificationsActivity.class);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onNavigationItemSelectedImpl(NavItemSelectedEvent event) {
        Ln.d("Selected: %1$s", event.getItemId());

        switch(event.getItemId()) {
            case R.id.menu_home:
                navigateTo(HomeActivity.class);
                break;
            case R.id.menu_notification:
                navigateTo(NotificationsActivity.class);
                break;
            case R.id.menu_reward:
                navigateTo(RewardActivity.class);
                break;
            case R.id.menu_profile:
                navigateTo(ProfileActivity.class);
                break;
            case R.id.menu_about_mobiclub:
                navigateTo(AboutMobiClubActivity.class);
                break;
            case R.id.menu_about:
                navigateTo(AboutActivity.class);
                break;
            case R.id.menu_contact:
                navigateTo(CentralActivity.class);
                break;
            case R.id.menu_help:
                navigateToWebView(Constants.URL.HELP, getString(R.string.web_view_help_title));
                break;
            case R.id.menu_tutorial:
                navigateTo(TutorialActivity.class);
                break;
            case R.id.menu_terms_privacy:
                navigateToWebView(Constants.URL.TERMS_AND_PRIVACY_POLICY, getString(R.string.web_view_terms_and_privacy_policy_title));
                break;
            case R.id.menu_logout:
                signout();
                break;
//            case R.id.menu_push:
//                //TODO: retirar isso
//                Intent intent = new Intent(this, PushNotificationsActivity.class);
//                String jsonTest = "{ \"alert\": \"Planetário (Shopping Guararapes Edit) enviou um presente pra você\", \"type\": \"gift\", \"object\": { \"Id\": \"41\", \"GiftId\": 24 } }";
//                intent.putExtra("com.parse.Data", jsonTest);
//                startActivity(intent);
//                break;
//            case Menu.OFFLINE_MOBIS:
//                navigateTo(MobiOfflineActivity.class);
//                break;
        }
    }

    private void signout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.signout_title_alert);
        builder.setMessage(R.string.signout_message_alert);
        builder.setPositiveButton(R.string.button_yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final AccountManager accountManager = AccountManager.get(MenuActivity.this);
                Integer userId = Facade.getInstance().getLoggedUser().getUserId();
                accountManager.invalidateAuthToken(ConstantsApi.Auth.AUTHTOKEN_TYPE,
                        String.valueOf(userId));
                final Account[] accountsByType = accountManager.getAccountsByType(ConstantsApi.Auth.BOOTSTRAP_ACCOUNT_TYPE);
                accountManager.clearPassword(accountsByType[0]);

                //TODO: mudar isso para a classe LogoutService
                new SafeAsyncTask<Boolean>() {

                    @Override
                    public Boolean call() throws Exception {
                        AccountManagerFuture<Boolean> managerFuture = accountManager.removeAccount(accountsByType[0], null, null);
                        Boolean result = managerFuture.getResult();
                        return result;
                    }

                    @Override
                    protected void onSuccess(Boolean aBoolean) throws Exception {
                        super.onSuccess(aBoolean);
                        if(aBoolean) {
                            onDeleteAccountSuccess();
                        } else {
                            AlertDialogFactory.createDefaultError(MenuActivity.this,
                                    R.string.signout_error).show();
                        }
                    }
                }.execute();
            }
        });
        builder.setNegativeButton(R.string.button_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    private void onDeleteAccountSuccess() {
        facade.deleteUserLocal();
        facade.setLoggedUser(null);
        Intent intent = new Intent(this, LaunchActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        final Session session = Session.getActiveSession();
        session.closeAndClearTokenInformation();
        finish();
    }

    private void navigateToWebView(String url, String title) {
        if(!isConnected(true)) {
            AlertDialog defaultError = AlertDialogFactory.createDefaultError(this, R.string.alert_title_attention,
                    R.string.alert_title_webview_no_connection);
            defaultError.setCancelable(true);
            defaultError.show();
            return;
        }
        Intent browserIntent = new Intent(this, WebViewActivity.class);
        browserIntent.putExtra(Constants.Extra.WEB_VIEW_URL, url);
        browserIntent.putExtra(Constants.Extra.WEB_VIEW_TITLE, title);
        start(browserIntent);
    }

    public void navigateTo(Class<? extends Activity> activity) {
        final Intent intent = new Intent(this, activity);
        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(Constants.Extra.HAS_GIFT, false);
        start(intent);
    }

    private void navigateToNext() {
        Intent intent = facade.getNextIntent();
        if(intent == null) {
            intent = new Intent(this, HomeActivity.class);
        }
        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        start(intent);
    }

    public void navigateTo(Intent intent) {
        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        start(intent);
    }

    private void start(Intent intent) {
        intent.putExtra(Constants.Extra.MENU_ACTION, true);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        //finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //TODO: achar melhor maneira para isso
        Facade.getInstance().removeLastIntent();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MobiClubUser loggedUser = facade.getLoggedUser();
        if(loggedUser != null) {
            MenuInflater menuInflater = getMenuInflater();
            menuInflater.inflate(R.menu.global, menu);

            final MenuItem item = menu.findItem(R.id.notification_action);
            MenuItemCompat.setActionView(item, R.layout.action_layout_notifications_update_count);
            View actionView = MenuItemCompat.getActionView(item);
            frameNotifCount = (TextView) actionView.findViewById(R.id.menu_text);
            labelNotifCount = (TextView) actionView.findViewById(R.id.menu_text);

            actionView.setClickable(true);
            actionView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onOptionsItemSelected(item);
                }
            });
            Integer notifications = loggedUser.getNotifications();
            if(notifications != null && notifications > 0) {
                if(frameNotifCount != null) {
                    frameNotifCount.setVisibility(View.VISIBLE);
                }
                if(labelNotifCount != null) {
                    labelNotifCount.setText(String.valueOf(notifications));
                }
            } else {
                if(frameNotifCount != null) {
                    frameNotifCount.setVisibility(View.GONE);
                }
            }
            navigationDrawerFragment.updateNotifications(notifications);
        }

        return super.onCreateOptionsMenu(menu);
    }

    public void setNotifications(final Integer notifications) {
        MobiClubUser loggedUser = facade.getLoggedUser();
        if(loggedUser != null) {
            loggedUser.setNotifications(notifications);
        }
        this.mNotifCount = notifications;
        if(labelNotifCount != null && frameNotifCount != null) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(notifications != null && notifications > 0) {
                        frameNotifCount.setVisibility(View.VISIBLE);
                        labelNotifCount.setText(String.valueOf(notifications));
                    } else {
                        frameNotifCount.setVisibility(View.GONE);
                    }
                }
            });
        }
        navigationDrawerFragment.updateNotifications(notifications);
    }

    protected abstract Fragment createFragment();

    protected boolean existsMobisOffline() {
        mobisOffline = facade.existsMobisOffline();
        return mobisOffline;
    }

    @Override
    public void onCentral() {
        Ln.d("onCentral");
        navigateTo(CentralActivity.class);
    }

    public void finishOnDialog() {
        facade.removeLastIntent();
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
        uiFacebookHelper.onActivityResult(requestCode, resultCode, data, new FacebookDialog.Callback() {
            @Override
            public void onError(FacebookDialog.PendingCall pendingCall, Exception error, Bundle data) {
                Log.e("Activity", String.format("Error: %s", error.toString()));

                AlertDialog.Builder builder = AlertDialogFactory.createDefaultBuilder(MenuActivity.this, R.string.facebook_share_error,
                        R.string.facebook_share_error_alert_try_again);

                builder.setPositiveButton(R.string.button_yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //tentar novamente
                        dialog.dismiss();
                        facebookShare(entitySharing, postType);
                    }
                }).setNegativeButton(R.string.button_no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        entitySharing = null;
                        Class<? extends Activity> redirect = getNextActivity();
                        if (redirect != null) {
                            navigateTo(redirect);
                            redirect = null;
                        }
                    }
                });

                builder.show();
            }

            @Override
            public void onComplete(FacebookDialog.PendingCall pendingCall, Bundle data) {
                if (entitySharing != null) {
                    Ln.d("Postagem facebook do servico.");
                    String postId = FacebookDialog.getNativeDialogPostId(data);
                    Ln.d("postid = %s", postId);
                    if (postId != null) {
                        facebook.shareOnWall(MenuActivity.this, entitySharing, postId, postType);
                        entitySharing = null;
                    }
                    Class<? extends Activity> redirect = getNextActivity();
                    if (redirect != null) {
                        navigateTo(redirect);
                        redirect = null;
                    }
                }
            }
        });

    }

    protected void facebookShare(final DialogResultData entity, final int postType) {
        try {
            this.postType = postType;
            final Session activeSession = Session.getActiveSession();
            if(activeSession == null) {
                return;
            }

            if(!activeSession.isOpened()) {
                activeSession.open(AccessToken.createFromExistingAccessToken(activeSession.getAccessToken(),
                                activeSession.getExpirationDate(), null,
                                null, activeSession.getPermissions()),
                        new Session.StatusCallback() {
                            @Override
                            public void call(Session session, SessionState state, Exception exception) {
                                if(session.isOpened()) {
                                    facebookShare(entity, postType);
                                } else {
                                    entitySharing = null;
                                    AlertDialog defaultError = AlertDialogFactory.createDefaultError(MenuActivity.this,
                                            R.string.alert_title_attention,
                                            R.string.facebook_connect_message);
                                    defaultError.show();
                                    return;
                                }
                            }
                        });
            }

            String permissionList = "publish_actions";
            List<String> permissions = activeSession.getPermissions();
            if (activeSession.isOpened() && !permissions.contains("publish_actions")) {
                Ln.d("Requisitando novas permissoes");
                Session.NewPermissionsRequest newPermissionsRequest = new Session.NewPermissionsRequest(MenuActivity.this,
                        permissionList);
                newPermissionsRequest.setCallback(new Session.StatusCallback() {
                    @Override
                    public void call(Session session, SessionState state, Exception exception) {
                        publish(entity);
                    }
                });
                activeSession.requestNewPublishPermissions(newPermissionsRequest);
            } else {
                publish(entity);
            }
        } catch (Exception e) {
            AlertDialog defaultError = AlertDialogFactory.createDefaultError(MenuActivity.this,
                    R.string.alert_title_attention,
                    R.string.facebook_not_share_message);
            defaultError.show();
        }

    }

    private void publish(final DialogResultData entity) {
        Ln.d("FacebookDialog");
        MenuActivity.this.entitySharing = entity;
        if (FacebookDialog.canPresentShareDialog(getApplicationContext(),
                FacebookDialog.ShareDialogFeature.SHARE_DIALOG)) {
            Ln.d("Usando native share");
            nativeShare(entitySharing);
        } else {
            Ln.d("Usando feed dialog");
            publishFeedDialog();
        }
    }

    private void publishFeedDialog() {
        Bundle params = new Bundle();
        //params.putString("name", getString(R.string.facebook_feed_dialog_title));
        //params.putString("caption", "Build great social apps and get more installs.");
        //params.putString("description", "The Facebook SDK for Android makes it easier and faster to develop Facebook integrated Android apps.");
        //params.putString("link", "https://developers.facebook.com/android");
        //params.putString("picture", "https://raw.github.com/fbsamples/ios-3.x-howtos/master/Images/iossdk_logo.png");

        Session activeSession = Session.getActiveSession();
        WebDialog feedDialog = (
                new WebDialog.FeedDialogBuilder(this,
                        activeSession,
                        params))
                .setOnCompleteListener(new WebDialog.OnCompleteListener() {

                    @Override
                    public void onComplete(Bundle values,
                                           FacebookException error) {
                        if (error == null) {
                            // When the story is posted, echo the success
                            // and the post Id.
                            final String postId = values.getString("post_id");
                            if (postId != null) {
                                Ln.d("Posted story, id: " + postId);
                                facebook.shareOnWall(MenuActivity.this, entitySharing, postId, postType);
                            } else {
                                // User clicked the Cancel button
                                Ln.d("Publish cancelled");
                            }
                        } else if (error instanceof FacebookOperationCanceledException) {
                            // User clicked the "x" button
                            Ln.d("Publish cancelled");
                        } else {
                            // Generic, ex: network error
                            Ln.d("Error posting story");
                            AlertDialog defaultError = AlertDialogFactory.createDefaultError(MenuActivity.this, getString(R.string.server_connection_error));
                            defaultError.show();
                        }
                    }

                })
                .build();
        feedDialog.show();
    }

    private void nativeShare(DialogResultData entitySharing) {
        FacebookDialog shareDialog = new FacebookDialog.ShareDialogBuilder(this)
                .setDescription(entitySharing.getFacebookPost())
                .build();
        uiFacebookHelper.trackPendingDialogCall(shareDialog.present());
    }

    public void hideDarkener() {
        if(darkener != null) {
            darkener.getForeground().setAlpha(0);
        }
    }

    @Override
    public void showButtonGainMobis(boolean show) {
        if(btnGainMobis == null)
            return;
        if(show) {
            slideToAbove();
        } else {
            slideToDown();
        }
    }

    public void slideToAbove() {
        Animation slide = null;
        slide = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.ABSOLUTE, 0.0f,
                Animation.ABSOLUTE, -1.0f);

        slide.setDuration(3000);
        slide.setFillAfter(true);
        slide.setFillEnabled(true);
        btnGainMobis.startAnimation(slide);
    }

    public void slideToDown() {
        Animation slide = null;
        slide = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                0.0f, Animation.RELATIVE_TO_SELF, btnGainMobis.getHeight());

        slide.setDuration(3000);
        slide.setFillAfter(true);
        slide.setFillEnabled(true);
        btnGainMobis.startAnimation(slide);
    }

    public void setNextActivity(Class<? extends Activity> nextActivity) {
        this.nextActivity = nextActivity;
    }

    public Class<? extends Activity> getNextActivity() {
        return nextActivity;
    }

    public void cleanGift() {
        getIntent().putExtra(Constants.Extra.HAS_GIFT, false);
        Gift gift = null;
        getIntent().putExtra(Constants.Extra.GIFT, gift);
    }

    @Override
    public void reachListEnd(boolean b) {
        if(b) {
            //slideToDown();
        } else {
            //slideToAbove();
        }
    }
}
