package br.com.mobiclub.quantoprima.ui.activity.reward;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.squareup.otto.Subscribe;

import br.com.mobiclub.quantoprima.core.service.api.entity.Gift;
import br.com.mobiclub.quantoprima.core.service.api.entity.Reward;
import br.com.mobiclub.quantoprima.events.NavItemSelectedEvent;
import br.com.mobiclub.quantoprima.ui.activity.MenuActivity;
import br.com.mobiclub.quantoprima.ui.fragment.reward.RewardFragment;
import br.com.mobiclub.quantoprima.ui.helper.GainGiftHelper;
import br.com.mobiclub.quantoprima.ui.view.GainGiftDialogListener;
import br.com.mobiclub.quantoprima.util.Ln;

public class RewardActivity extends MenuActivity
        implements GainGiftDialogListener {

    private GainGiftHelper gainGiftHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gainGiftHelper = new GainGiftHelper(this, serviceProvider);
    }

    @Override
    protected Fragment createFragment() {
        return new RewardFragment();
    }

    /**
     * Aqui por enquanto pois o otto nao consegue enxergar
     * a anotacao de subscribe em MenuActivity
     *
     * @param event
     */
    @Subscribe
    @Override
    public void onNavigationItemSelected(NavItemSelectedEvent event) {
        super.onNavigationItemSelectedImpl(event);
    }

    @Override
    public void onRejectGift() {
        Ln.d("onRejectGift");
        gainGiftHelper.onRejectGift();
    }

    @Override
    public void onAcceptGift() {
        Ln.d("onAcceptGift");
        gainGiftHelper.onAcceptGift();
    }

    public void showGainGift(Reward reward) {
        Gift gift = new Gift();

        gift.setGiftId(reward.getIdToRescue());
        gift.setReward(reward);
        gift.setExpiration(reward.getExpirationAt());
        gift.setDescription(reward.getDescription());
        gift.setEstablishmentLogoURL(reward.getEstablishmentLogoURL());
        gift.setEstablishmentName(reward.getEstablishmentName());
        gift.setImage(reward.getImage());
        gainGiftHelper.showGainGift(gift);
    }

}
