package br.com.mobiclub.quantoprima.ui.activity.reward;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.squareup.otto.Subscribe;

import br.com.mobiclub.quantoprima.R;
import br.com.mobiclub.quantoprima.core.service.api.entity.Reward;
import br.com.mobiclub.quantoprima.domain.MobiClubUser;
import br.com.mobiclub.quantoprima.events.NavItemSelectedEvent;
import br.com.mobiclub.quantoprima.facade.Facade;
import br.com.mobiclub.quantoprima.facebook.FacebookFacade;
import br.com.mobiclub.quantoprima.ui.activity.MenuActivity;
import br.com.mobiclub.quantoprima.ui.factory.AlertDialogFactory;
import br.com.mobiclub.quantoprima.ui.fragment.reward.RewardDetailFragment;
import br.com.mobiclub.quantoprima.util.Ln;

public class RewardDetailActivity extends MenuActivity {

    private Reward reward;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected Fragment createFragment() {
        reward = Facade.getInstance().getReward();
        RewardDetailFragment rewardDetailFragment = new RewardDetailFragment();
        return rewardDetailFragment;
    }

    public void onRescue(View view) {
        Intent intent = new Intent(this, RescueRewardActivity.class);
        Facade.getInstance().setReward(reward);
        startActivity(intent);
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

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        if(item.getItemId() == R.id.share_action) {
//            Intent intent = getIntent();
//            Reward reward = (Reward) intent.getSerializableExtra(Constants.Extra.REWARD);
//            Ln.d("onShare");
//            super.facebookShare(reward, FacebookFacade.FacebookPostType.STORE_ITEM.getId());
//        }
//        return super.onOptionsItemSelected(item);
//    }

    public void onShare(View view) {
        Ln.d("onShare");
        MobiClubUser loggedUser = Facade.getInstance().getLoggedUser();
        if(!loggedUser.isConnectedToFacebook()) {
            AlertDialog defaultError = AlertDialogFactory.createDefaultError(this,
                    R.string.alert_title_attention,
                    R.string.facebook_connect_message);
            defaultError.show();
            return;
        }
        Reward reward = Facade.getInstance().getReward();
        int postId = FacebookFacade.FacebookPostType.STORE_ITEM.getId();
        if(reward.isRewardBuy()) {
            postId = FacebookFacade.FacebookPostType.REWARD_BUY.getId();
        } else if(reward.isRewardGift()) {
            postId = FacebookFacade.FacebookPostType.REWARD_GIFT.getId();
        } else if(reward.isRewardShot()) {
            postId = FacebookFacade.FacebookPostType.REWARD_SHOT.getId();
        }
        super.facebookShare(reward, postId);
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.share, menu);
//        boolean b = super.onCreateOptionsMenu(menu);
//        menu.getItem(1).setVisible(false);
//        return b;
//    }

}
