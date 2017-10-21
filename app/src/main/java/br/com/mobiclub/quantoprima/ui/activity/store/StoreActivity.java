package br.com.mobiclub.quantoprima.ui.activity.store;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;

import com.squareup.otto.Subscribe;

import br.com.mobiclub.quantoprima.R;
import br.com.mobiclub.quantoprima.core.service.api.entity.Establishment;
import br.com.mobiclub.quantoprima.core.service.api.entity.EstablishmentLocation;
import br.com.mobiclub.quantoprima.domain.Mobi;
import br.com.mobiclub.quantoprima.events.NavItemSelectedEvent;
import br.com.mobiclub.quantoprima.facade.Facade;
import br.com.mobiclub.quantoprima.facebook.FacebookFacade;
import br.com.mobiclub.quantoprima.ui.activity.GainMobiListener;
import br.com.mobiclub.quantoprima.ui.activity.MenuActivity;
import br.com.mobiclub.quantoprima.ui.activity.qrcode.CaptureActivity;
import br.com.mobiclub.quantoprima.ui.activity.reward.RewardActivity;
import br.com.mobiclub.quantoprima.ui.activity.store.survey.SurveyActivity;
import br.com.mobiclub.quantoprima.ui.fragment.store.StoreFragment;
import br.com.mobiclub.quantoprima.ui.fragment.store.StoreRewardListFragment;
import br.com.mobiclub.quantoprima.ui.helper.GainMobiHelper;
import br.com.mobiclub.quantoprima.ui.view.DialogListener;
import br.com.mobiclub.quantoprima.ui.view.DialogResultData;
import br.com.mobiclub.quantoprima.ui.view.GainMobiDialogListener;
import br.com.mobiclub.quantoprima.domain.Constants;
import br.com.mobiclub.quantoprima.util.Ln;

public class StoreActivity extends MenuActivity
        implements StoreRewardListFragment.StoreRewardListListener,
        GainMobiListener, DialogListener, GainMobiDialogListener {

    private static final int QRCODE_REQUEST_CODE = 0;
    private static final int MOBIS_OFFLINE_CODE = 1;
    private GainMobiHelper gainMobiHelper;
    private boolean gainedMobi;
    private StoreFragment storeFragment;
    private EstablishmentLocation gainedLocation;
    private EstablishmentLocation location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        gainMobiHelper = new GainMobiHelper(this, serviceProvider);
        gainedMobi = false;

        Intent intent = getIntent();
        location = Facade.getInstance().getLocation();
    }

    @Override
    protected Fragment createFragment() {
        storeFragment = new StoreFragment();
        return storeFragment;
    }

    //TODO: refatorar unificando esse comportamento com o do home e locationactivity
    public void onGainMobi(View view) {
        Ln.d("onGainMobi");
        Intent qrCodeIntent = new Intent(this, CaptureActivity.class);
        qrCodeIntent.putExtra(Constants.Extra.QRCODE_READ_TYPE, isConnected());
        startActivityForResult(qrCodeIntent, QRCODE_REQUEST_CODE);
        btnMoreMobis.setImageResource(R.drawable.t08_btn_pontuar_highlighted);
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
                    gainMobiHelper.gainMobi(code);
                } else if (resultCode == RESULT_CANCELED) {

                }
            }
        }
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
    public void onGoRewards() {
        Intent intent = new Intent(this, RewardActivity.class);
        startActivity(intent);
        finish();
    }


    @Override
    public void onGainedMobi(EstablishmentLocation location) {
        gainedLocation = location;
        Ln.d("gainedLocation = %s", gainedLocation.getId());
        gainedMobi = true;
    }

    @Override
    public void onCloseResult(int result) {
        Ln.d("onCloseResult");
        updateLocal();
    }

    private void updateLocal() {
        if(gainedMobi && gainedLocation != null && location != null) {
            Ln.d("gainedLocation = %s", gainedLocation.getId());
            Ln.d("location = %s", location.getId());
            if(gainedLocation.getId().intValue() != location.getId().intValue()) {
                Ln.e("Nao foi possivel atualizar store. Motivo: QRcode de outra loja");
                return;
            }
            gainedMobi = false;
            Establishment establishment = Facade.getInstance().getEstablishment();
            EstablishmentLocation location = Facade.getInstance().getLocation();
            int totalMobis = location.getScore();
            totalMobis += GainMobiHelper.MOBIS_TO_GAIN_VALUE;
            location.setScore(totalMobis);

            Facade.getInstance().setEstablishment(establishment);
            establishment.setLocation(location);
            Facade.getInstance().setLocation(location);

            final FragmentManager fragmentManager = getSupportFragmentManager();
            Fragment fragmentById = fragmentManager.findFragmentById(R.id.container);
            if(fragmentById != null && fragmentById instanceof StoreFragment) {
                StoreFragment storeFragment = (StoreFragment) fragmentById;
                storeFragment.updateLocationMobis(location);
            } else if(storeFragment != null) {
                storeFragment.updateLocationMobis(location);
            }
        } else {
            Ln.e("Nao foi possivel atualizar store");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void updateLocationMobis(EstablishmentLocation newScore,
                                    Integer mobisType, int buyValue) {
        Establishment establishment = Facade.getInstance().getEstablishment();
        EstablishmentLocation location = Facade.getInstance().getLocation();

        if(newScore.getId().intValue() != location.getId().intValue()) {
            Ln.e("Nao foi possivel atualizar store. Motivo: QRcode de outra loja");
            return;
        }

        int totalMobis = location.getScore();
        if(mobisType == Mobi.MOBIS_GAIN_MOBI_TYPE) {
            totalMobis += GainMobiHelper.MOBIS_TO_GAIN_VALUE;
        } else if(mobisType == Mobi.MOBIS_BUY_REWARD_TYPE) {
            totalMobis -= buyValue;
        }
        location.setScore(totalMobis);
        newScore.setScore(totalMobis);
        Facade.getInstance().setEstablishment(establishment);
        establishment.setLocation(location);
        Facade.getInstance().setLocation(location);

        final FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragmentById = fragmentManager.findFragmentById(R.id.container);
        if(fragmentById != null && fragmentById instanceof StoreFragment) {
            StoreFragment storeFragment = (StoreFragment) fragmentById;
            storeFragment.updateLocationMobis(location);
        }
    }

    @Override
    protected boolean showGainMobis() {
        return true;
    }

    @Override
    public void onEvaluate(EstablishmentLocation location) {
        updateLocal();
        Ln.d("onEvaluate");
        Intent intent = new Intent(this, SurveyActivity.class);
        Facade.getInstance().setLocation(location);
        Facade.getInstance().setEstablishment(location.getEstablishment());
        navigateTo(intent);
    }

    @Override
    public void onShare(DialogResultData data) {
        updateLocal();
        super.facebookShare(data, FacebookFacade.FacebookPostType.MOBI.getId());
    }

    @Override
    public void onGoToReward() {
        Ln.d("onGoToReward");
        navigateTo(RewardActivity.class);
    }
}
