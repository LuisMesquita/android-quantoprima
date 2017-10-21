

package br.com.mobiclub.quantoprima.ui.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;

import com.squareup.otto.Subscribe;

import javax.inject.Inject;

import br.com.mobiclub.quantoprima.R;
import br.com.mobiclub.quantoprima.core.service.MobiClubServiceProvider;
import br.com.mobiclub.quantoprima.core.service.api.entity.Establishment;
import br.com.mobiclub.quantoprima.core.service.api.entity.EstablishmentLocation;
import br.com.mobiclub.quantoprima.core.service.api.entity.Gift;
import br.com.mobiclub.quantoprima.events.NavItemSelectedEvent;
import br.com.mobiclub.quantoprima.facade.Facade;
import br.com.mobiclub.quantoprima.facebook.FacebookFacade;
//import br.com.mobiclub.quantoprima.service.map.PositionListener;
import br.com.mobiclub.quantoprima.ui.activity.mobi.MobiOfflineActivity;
import br.com.mobiclub.quantoprima.ui.activity.qrcode.CaptureActivity;
import br.com.mobiclub.quantoprima.ui.activity.reward.RewardActivity;
import br.com.mobiclub.quantoprima.ui.activity.store.StoreActivity;
import br.com.mobiclub.quantoprima.ui.activity.store.survey.SurveyActivity;
import br.com.mobiclub.quantoprima.ui.adapter.TextWatcherAdapter;
import br.com.mobiclub.quantoprima.ui.factory.AlertDialogFactory;
import br.com.mobiclub.quantoprima.ui.fragment.SearchListener;
import br.com.mobiclub.quantoprima.ui.fragment.home.HomeFragment;
import br.com.mobiclub.quantoprima.ui.fragment.home.HomeOfflineFragment;
import br.com.mobiclub.quantoprima.ui.helper.GainGiftHelper;
import br.com.mobiclub.quantoprima.ui.helper.GainMobiHelper;
import br.com.mobiclub.quantoprima.ui.view.DialogResultData;
import br.com.mobiclub.quantoprima.ui.view.GainGiftDialogListener;
import br.com.mobiclub.quantoprima.ui.view.GainMobiDialogListener;
import br.com.mobiclub.quantoprima.domain.Constants;
import br.com.mobiclub.quantoprima.util.Ln;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;

/**
 * Initial activity for the application.
 *
 * If you need to remove the authentication from the application please see
 * {@link br.com.mobiclub.quantoprima.service.authenticator.ApiKeyProvider#getAuthKey(android.app.Activity)}
 */
public class HomeActivity extends MenuActivity
        implements HomeFragment.HomeFragmentListener,
        GainMobiDialogListener, GainGiftDialogListener,
        GainMobiListener, Animation.AnimationListener {

    private static final int QRCODE_REQUEST_CODE = 0;
    private static final int MOBIS_OFFLINE_CODE = 1;
    private static final int FACEBOOK_SHARE_REQUEST = 2;
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 9000;

    @Inject
    MobiClubServiceProvider serviceProvider;

    private GainMobiHelper gainMobiHelper;
    private EditText etSearch;
    private Facade facade;

    private boolean gpsClosed;
    private GainGiftHelper gainGiftHelper;
    private Gift gift;
    private boolean hasGift;
    private Intent newIntent;
    private Animation animFadeIn;
    private Animation animFadeOut;
    private boolean updatingMobisOffline;
    private long lastTrySync;

    // Persist view for GainMobi
    private View mView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        facade = Facade.getInstance();
        super.onCreate(savedInstanceState);

        ButterKnife.inject(this);

        gainMobiHelper = new GainMobiHelper(this, serviceProvider);
        lastTrySync = preferences.getLong(Constants.Extra.LAST_TRY_SYNC, 0);

        super.showSearchBar(true);

        Facade.getInstance().setEstablishment(null);
        Facade.getInstance().setLocation(null);

        imgDegrade = (ImageView) findViewById(R.id.img_degrade);
        if(imgDegrade != null) {
            imgDegrade.setVisibility(View.VISIBLE);
        }

        if(savedInstanceState != null) {
            if(gpsClosed && gpsBar != null) {
                showGPSBar(false);
            }
            return;
        }

        boolean closeGPS = preferences.getBoolean(Constants.Extra.GPSBAR_SHOW, gpsClosed);
        if(!closeGPS) {
            super.showGPSBar(true);
        } else {
            super.showGPSBar(false);
        }

        etSearch = (EditText) findViewById(R.id.et_search);
        etSearch.addTextChangedListener(new TextWatcherAdapter() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(!searching)
                    return;
                setDarkenerVisible();
                if(start == 0 && count == 1 && after == 0) {
                    Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.container);
                    if(fragment != null && fragment instanceof SearchListener) {
                        SearchListener sl = (SearchListener) fragment;
                        sl.onQueryLocationChanged("");
                    }
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!searching)
                    return;
                setDarkenerVisible();
                if(count > 1) {
                    Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.container);
                    if(fragment != null && fragment instanceof SearchListener) {
                        SearchListener sl = (SearchListener) fragment;
                        sl.onQueryLocationChanged(String.valueOf(s));
                    }
                }
            }
        });
        gainGiftHelper = new GainGiftHelper(this, serviceProvider);
        onNewGift(getIntent());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void noResult(boolean b) {
        noResult = b;
        if(noResult) {
            if(imgDegrade != null) {
                imgDegrade.setVisibility(View.GONE);
            }
        } else {
            if(imgDegrade != null) {
                imgDegrade.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    protected void customNavigation(Intent intent) {
        facade.clearParents();
        super.customNavigation(intent);
    }

    @OnClick(R.id.close_gps_bar)
    @Optional
    public void onCloseGPSBar() {
        if(gpsBar != null) {
            gpsClosed = true;
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean(Constants.Extra.GPSBAR_SHOW, gpsClosed);
            editor.commit();
            gpsBar.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 20000) {
            gainMobiHelper.gainMobiFromCPF();
        } else {

            if(requestCode == QRCODE_REQUEST_CODE) {
                if(resultCode == Activity.RESULT_OK) {
                    Bundle extras = null;
                    String code = null;
                    if(data != null) {
                        extras = data.getExtras();
                        if(extras != null)
                            code = extras.getString(Constants.Extra.QR_CODE);
                    }
                    Ln.d("Chamando helper para ganhar mobi");
                    gainMobiHelper.gainMobi(code);
                } else if(resultCode == RESULT_CANCELED) {

                }
            } else if(requestCode == MOBIS_OFFLINE_CODE) {
                if(resultCode == Activity.RESULT_OK) {
                    lastTrySync = 0;
                } else if(resultCode == Activity.RESULT_CANCELED) {
                    lastTrySync = System.currentTimeMillis();
                }
            }
        }
    }

    public void onGainMobi(View view) {
        if ( mView != null ) {
            mView = view;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int permissionCamera = this.checkSelfPermission(Manifest.permission.CAMERA);
            if (permissionCamera == PackageManager.PERMISSION_GRANTED) {
                onGainMobiWithPermission(view);
            } else {
                String[] listPermissionsNeeded = {
                        Manifest.permission.CAMERA
                };
                this.requestPermissions(listPermissionsNeeded, CAMERA_PERMISSION_REQUEST_CODE);
            }
        } else {
            onGainMobiWithPermission(view);
        }

    }

    public void onGainMobiWithPermission(View view) {
        Intent qrCodeIntent = new Intent(this, CaptureActivity.class);
        qrCodeIntent.putExtra(Constants.Extra.QRCODE_READ_TYPE, isConnected(true));
        startActivityForResult(qrCodeIntent, QRCODE_REQUEST_CODE);

//        Intent qrCodeIntent = new Intent(this, QRCodeActivityMock.class);
//        qrCodeIntent.putExtra(Constants.Extra.QRCODE_READ_TYPE, isConnected(true));
//        startActivityForResult(qrCodeIntent, QRCODE_REQUEST_CODE);

        btnMoreMobis.setImageResource(R.drawable.t08_btn_pontuar_highlighted);
    }

    @Override
    public void onItemClicked(Establishment establishment) {
        //devera verificar se o item eh agrupado ou nao
        //realizando a navegacao de acordo
        if(establishment.isGroup()) {
            //abrir uma nova atividade
            Intent intent = new Intent(this, LocationActivity.class);
            Facade.getInstance().setEstablishment(establishment);
            startActivity(intent);
        } else {
            Intent intent = new Intent(this, StoreActivity.class);
            Facade.getInstance().setLocation(establishment.getLocation());
            Facade.getInstance().setEstablishment(establishment);
            startActivity(intent);
        }
    }

    @Override
    public void onNewNotifications(Integer notifications) {
        super.setNotifications(notifications);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Ln.d("onNewIntent");
        this.newIntent = intent;
        onNewGift(intent);
    }

    private void onNewGift(Intent intent) {
        //verifica se existe presente
        gift = null;
        hasGift = intent.getBooleanExtra(Constants.Extra.HAS_GIFT, false);
        if(!hasGift) {
            gift = null;
            intent.putExtra(Constants.Extra.HAS_GIFT, false);
            getIntent().putExtra(Constants.Extra.HAS_GIFT, false);
            return;
        }
        gift = (Gift) intent.getSerializableExtra(Constants.Extra.GIFT);
        Ln.d("Recebi um novo presente %s", gift);
        if(gift == null) {
            AlertDialogFactory.createDefaultError(this, R.string.get_gift_error2).show();
        }
        if(gift != null && !gift.isSuccess()) {
            String message = getString(R.string.get_gift_error, gift.getMessage());
            AlertDialogFactory.createDefaultError(this, message).show();
            gift = null;
            intent.putExtra(Constants.Extra.HAS_GIFT, false);
            getIntent().putExtra(Constants.Extra.HAS_GIFT, false);
            return;
        } else {
            intent.putExtra(Constants.Extra.HAS_GIFT, false);
            getIntent().putExtra(Constants.Extra.HAS_GIFT, true);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        hasGift = getIntent().getBooleanExtra(Constants.Extra.HAS_GIFT, false);
        Facade.getInstance().setLocation(null);
        Facade.getInstance().setEstablishment(null);
        if(hasGift) {
            gainGiftHelper.showGainGift(gift);
            if(newIntent != null)
                newIntent.putExtra(Constants.Extra.HAS_GIFT, false);
            getIntent().putExtra(Constants.Extra.HAS_GIFT, false);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        Log.d("Home Activity", "Resultado do pedido de permissão");
        switch (requestCode) {
            case CAMERA_PERMISSION_REQUEST_CODE: {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (this.checkSelfPermission(Manifest.permission.CAMERA)
                            == PackageManager.PERMISSION_GRANTED) {

                        Log.e("Permissão concedida", Integer.toString(grantResults[0]));
                        onGainMobi(mView);
                    } else {
                        Log.e("Permissão negada", Integer.toString(grantResults[0]));
                        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
                        alertBuilder.setMessage(getString(R.string.alert_we_need_camera_permission))
                                .setCancelable(false)
                                .setPositiveButton(getString(R.string.texto_OK), new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // Silence is golden
                                    }
                                });
                        alertBuilder.show();
                    }
                }
            }
        }
    }

    @Override
    public void onRejectGift() {
        Ln.d("onRejectGift");
        gainGiftHelper.onRejectGift();
        if(newIntent != null)
            newIntent.putExtra(Constants.Extra.HAS_GIFT, false);
        getIntent().putExtra(Constants.Extra.HAS_GIFT, false);
    }

    @Override
    public void onAcceptGift() {
        Ln.d("onAcceptGift");
        gainGiftHelper.onAcceptGift();
        if(newIntent != null)
            newIntent.putExtra(Constants.Extra.HAS_GIFT, false);
        getIntent().putExtra(Constants.Extra.HAS_GIFT, false);
    }

    @Override
    protected Fragment createFragment() {
        Fragment fragment = null;
        if (!isUserHasAuthenticated()) {
            Ln.d("Nao eh possivel criar fragmento. Usuario não esta autenticado");
            return null;
        }
        if(!isConnected(true)) {
            if(imgDegrade != null)
                imgDegrade.setVisibility(View.GONE);
            fragment = new HomeOfflineFragment();
            showSearchBar(false);
        } else {
            showSearchBar(true);
            if(existsMobisOffline() && !updatingMobisOffline && passOneMinuteFromLastTry()) {
                Intent intent = new Intent(this, MobiOfflineActivity.class);
                startActivityForResult(intent, MOBIS_OFFLINE_CODE);
                updatingMobisOffline = true;
            } else {
                if(imgDegrade != null)
                    imgDegrade.setVisibility(View.VISIBLE);
                fragment = new HomeFragment();
                updatingMobisOffline = false;
            }
        }
        return fragment;
    }

    @Override
    protected void setConnected(boolean isConnected) {
        super.setConnected(isConnected);
    }

    private boolean passOneMinuteFromLastTry() {
        long now = System.currentTimeMillis();

        if(lastTrySync == 0) {
            return true;
        }

        if(now - lastTrySync > 1000 * 60) {
            lastTrySync = 0;
            return true;
        }

        return false;
    }

    /**
     * Aqui por enquanto pois o otto nao consegue enchergar
     * a anotacao de subscribe em PrincipalActivity
     *
     * @param event
     */
    @Subscribe
    @Override
    public void onNavigationItemSelected(NavItemSelectedEvent event) {
        super.onNavigationItemSelectedImpl(event);
    }

    @Override
    public void onEvaluate(EstablishmentLocation location) {
        Ln.d("onEvaluate");
        Intent intent = new Intent(this, SurveyActivity.class);

        Facade.getInstance().setLocation(location);
        Facade.getInstance().setEstablishment(location.getEstablishment());

        navigateTo(intent);
    }

    @Override
    public void onShare(DialogResultData data) {
        Ln.d("onShare");
        int postType;
        if(gift != null) {
            postType = FacebookFacade.FacebookPostType.REWARD_GIFT.getId();
        } else {
            postType = FacebookFacade.FacebookPostType.MOBI.getId();
        }
        super.facebookShare(data, postType);
    }

    @Override
    public void onGoToReward() {
        Ln.d("onGoToReward");
        navigateTo(RewardActivity.class);
    }

    @Override
    public void onCloseResult(int result) {
        //do nothing
        gift = null;
        getIntent().putExtra(Constants.Extra.HAS_GIFT, false);
    }

    @Override
    public void onGainedMobi(EstablishmentLocation location) {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.container);
        if(fragment != null && fragment instanceof HomeFragment) {
            HomeFragment mf = (HomeFragment) fragment;
            mf.updateLocationMobis(location);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK) {
            doExit();
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        doExit();
    }

    private void doExit() {
        int title = R.string.alert_title_attention;
        int message = R.string.alert_title_exit_app;
        AlertDialog.Builder builder = AlertDialogFactory.createDefaultBuilder(this, title, message);
        builder.setPositiveButton(R.string.alert_yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                onAcceptExit();
            }
        });
        builder.setNegativeButton(R.string.button_no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    private void onAcceptExit() {
        gpsClosed = false;
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(Constants.Extra.GPSBAR_SHOW, gpsClosed);
        editor.commit();
//        PositionListener positionListener = facade.getPositionListener();
//        if(positionListener != null)
//            positionListener.cancel();
        ActivityCompat.finishAffinity(this);
    }

    @Override
    protected boolean showGainMobis() {
        return true;
    }

    @Override
    public void reachListEnd(boolean b) {
        super.reachListEnd(b);
        if(b) {
            animFadeOut = AnimationUtils.loadAnimation(this, R.anim.anim_fade_out);
            animFadeOut.setAnimationListener(this);
            imgDegrade.startAnimation(animFadeOut);
        } else {
            if(imgDegrade != null && imgDegrade.getVisibility() == View.GONE) {
                animFadeIn = AnimationUtils.loadAnimation(this, R.anim.anim_fade_in);
                animFadeIn.setAnimationListener(this);
                imgDegrade.startAnimation(animFadeIn);
            }
        }
    }

    @Override
    public void onAnimationEnd(Animation animation) {
        if(animation == animFadeIn && imgDegrade != null) {
            imgDegrade.setVisibility(View.VISIBLE);
        }
        if(animation == animFadeOut && imgDegrade != null) {
            imgDegrade.setVisibility(View.GONE);
        }
    }

    @Override
    public void onAnimationRepeat(Animation arg0) {
    }

    @Override
    public void onAnimationStart(Animation arg0) {
    }
}
