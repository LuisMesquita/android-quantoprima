package br.com.mobiclub.quantoprima.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.squareup.otto.Subscribe;

import br.com.mobiclub.quantoprima.R;
import br.com.mobiclub.quantoprima.core.service.api.entity.Establishment;
import br.com.mobiclub.quantoprima.core.service.api.entity.EstablishmentLocation;
import br.com.mobiclub.quantoprima.events.NavItemSelectedEvent;
import br.com.mobiclub.quantoprima.facade.Facade;
import br.com.mobiclub.quantoprima.facebook.FacebookFacade;
import br.com.mobiclub.quantoprima.ui.activity.qrcode.CaptureActivity;
import br.com.mobiclub.quantoprima.ui.activity.reward.RewardActivity;
import br.com.mobiclub.quantoprima.ui.activity.store.StoreActivity;
import br.com.mobiclub.quantoprima.ui.activity.store.survey.SurveyActivity;
import br.com.mobiclub.quantoprima.ui.fragment.home.HomeFragment;
import br.com.mobiclub.quantoprima.ui.helper.GainMobiHelper;
import br.com.mobiclub.quantoprima.ui.view.DialogResultData;
import br.com.mobiclub.quantoprima.ui.view.GainMobiDialogListener;
import br.com.mobiclub.quantoprima.domain.Constants;
import br.com.mobiclub.quantoprima.util.Ln;

/**
 *
 */
public class LocationActivity extends MenuActivity
        implements HomeFragment.HomeFragmentListener,
        GainMobiDialogListener,
        GainMobiListener {

    private Establishment establishment;

    private GainMobiHelper gainMobiHelper;

    private static final int QRCODE_REQUEST_CODE = 0;
    private static final int MOBIS_OFFLINE_CODE = 1;
    private static final int FACEBOOK_SHARE_REQUEST = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        gainMobiHelper = new GainMobiHelper(this, serviceProvider);

        findViewById(R.id.img_degrade).setVisibility(View.VISIBLE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 20000) {
            gainMobiHelper.gainMobiFromCPF();
        } else {
            if (requestCode == QRCODE_REQUEST_CODE) {
                if (resultCode == Activity.RESULT_OK) {
                    Bundle extras = null;
                    String code = null;
                    if (data != null) {
                        extras = data.getExtras();
                        if (extras != null)
                            code = extras.getString(Constants.Extra.QR_CODE);
                    }
                    Ln.d("Chamando helper para ganhar mobi");
                    gainMobiHelper.gainMobi(code);
                } else if (resultCode == RESULT_CANCELED) {

                }
            } else if (requestCode == MOBIS_OFFLINE_CODE) {
            } else if (requestCode == FACEBOOK_SHARE_REQUEST) {
                if (resultCode == Activity.RESULT_OK) {
                } else if (resultCode == RESULT_CANCELED) {
                    Ln.d("Voltei do share. com result_canceled.");
                }
            }
        }
    }

    //@OnClick(R.id.btn_gain_mobis)
    public void onGainMobi(View view) {
        Intent qrCodeIntent = new Intent(this, CaptureActivity.class);
        qrCodeIntent.putExtra(Constants.Extra.QRCODE_READ_TYPE, isConnected(true));
        startActivityForResult(qrCodeIntent, QRCODE_REQUEST_CODE);

//        Intent qrCodeIntent = new Intent(this, QRCodeActivityMock.class);
//        qrCodeIntent.putExtra(Constants.Extra.QRCODE_READ_TYPE, isConnected(true));
//        startActivityForResult(qrCodeIntent, QRCODE_REQUEST_CODE);

        btnMoreMobis.setImageResource(R.drawable.t08_btn_pontuar_default);

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
        super.facebookShare(data, FacebookFacade.FacebookPostType.MOBI.getId());
    }

    @Override
    public void onCloseResult(int result) {
        //do nothing
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
    public void noResult(boolean b) {
        noResult = b;
    }

    @Subscribe
    @Override
    public void onNavigationItemSelected(NavItemSelectedEvent event) {
        super.onNavigationItemSelectedImpl(event);
    }

    @Override
    protected Fragment createFragment() {
        HomeFragment homeFragment = new HomeFragment();
        return homeFragment;
    }

    @Override
    public void onItemClicked(Establishment establishment) {
        Intent intent = new Intent(this, StoreActivity.class);
        Facade.getInstance().setLocation(establishment.getLocation());
        Facade.getInstance().setEstablishment(establishment);
        startActivity(intent);
    }

    @Override
    public void onNewNotifications(Integer notifications) {
        //nao faz nada
    }

    @Override
    protected boolean showGainMobis() {
        return true;
    }

    @Override
    public void onGoToReward() {
        Ln.d("onGoToReward");
        navigateTo(RewardActivity.class);
    }
}
