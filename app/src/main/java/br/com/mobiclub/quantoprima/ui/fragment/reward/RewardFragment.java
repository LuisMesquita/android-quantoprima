package br.com.mobiclub.quantoprima.ui.fragment.reward;

import android.accounts.OperationCanceledException;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.github.kevinsawicki.wishlist.TypeAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import br.com.mobiclub.quantoprima.R;
import br.com.mobiclub.quantoprima.config.Injector;
import br.com.mobiclub.quantoprima.core.service.MobiClubServiceApi;
import br.com.mobiclub.quantoprima.core.service.MobiClubServiceProvider;
import br.com.mobiclub.quantoprima.core.service.api.entity.ApiResult;
import br.com.mobiclub.quantoprima.core.service.api.entity.EstablishmentLocation;
import br.com.mobiclub.quantoprima.core.service.api.entity.Reward;
import br.com.mobiclub.quantoprima.facade.Facade;
import br.com.mobiclub.quantoprima.service.authenticator.LogoutService;
import br.com.mobiclub.quantoprima.ui.activity.reward.RewardActivity;
import br.com.mobiclub.quantoprima.ui.activity.reward.RewardDetailActivity;
import br.com.mobiclub.quantoprima.ui.adapter.reward.RewardListAdapter;
import br.com.mobiclub.quantoprima.ui.fragment.ItemListFragment;
import br.com.mobiclub.quantoprima.ui.view.ThrowableLoader;
import br.com.mobiclub.quantoprima.domain.Constants;
import br.com.mobiclub.quantoprima.util.Ln;
import br.com.mobiclub.quantoprima.util.SafeAsyncTask;

/**
 *
 */
public class RewardFragment extends ItemListFragment<Reward> implements View.OnClickListener{
    private Intent starterIntent;

    @Inject
    protected MobiClubServiceProvider serviceProvider;

    private MobiClubServiceApi service;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Injector.inject(this);
    }

    @Override
    protected LogoutService getLogoutService() {
        return null;
    }

    @Override
    protected void configureList(final Activity activity, final ListView listView) {
        super.configureList(activity, listView);

        listView.setFastScrollEnabled(true);
        listView.setDivider(this.getResources().getDrawable(R.drawable.transparent_drawable));
        listView.setDividerHeight(2);

        listView.setSelector(R.drawable.transparent_drawable);
    }

    @Override
    public Loader<List<Reward>> onCreateLoader(final int id, final Bundle args) {
        final List<Reward> initialItems = items;
        return new ThrowableLoader<List<Reward>>(getActivity(), items) {
            @Override
            public List<Reward> loadData() throws Exception {

                FragmentActivity activity = getActivity();
                try {
                    List<Reward> latest = null;
                    List<Reward> finalList = null;

                    if (activity != null) {
                        MobiClubServiceApi service = serviceProvider.getService(activity);
                        latest = service.getRewardsByUser();
                    }

                    if (latest != null) {
//                        Collections.sort(latest, new Comparator<Reward>() {
//                            @Override
//                            public int compare(Reward lhs, Reward rhs) {
//                                if(lhs.getExpired()) {
//                                    return 1;
//                                } else if(rhs.getExpired()) {
//                                    return -1;
//                                }
//                                return 0;
//                            }
//                        });

                        /*
                        List<Reward> latest1 = new ArrayList<Reward>();
                        List<Reward> latest2 = new ArrayList<Reward>();
                        List<Reward> latest3 = new ArrayList<Reward>();
                        for (int i = 0; i < latest.size(); i++) {
                            if (!latest.get(i).isRewardExpired() && !latest.get(i).isRewardRedeemed()) {
                                latest1.add(latest.get(i));
                            }
                        }

                        for (int j= 0; j < latest.size(); j++) {
                            if (latest.get(j).isRewardExpired() && !latest.get(j).isRewardRedeemed()) {
                                latest3.add(latest.get(j));

                            }
                        }

                        for (int y= 0; y < latest.size(); y++) {
                            if (latest.get(y).isRewardRedeemed()) {
                                latest3.add(latest.get(y));
                            }
                        }

                        latest = new ArrayList<Reward>();

                        for (int i = 0; i < latest1.size(); i++) {
                            latest.add(latest1.get(i));
                        }

                        for (int i = 0; i < latest3.size(); i++) {
                            latest.add(latest3.get(i));
                        }

                        for (int i = 0; i < latest2.size(); i++) {
                            latest.add(latest2.get(i));
                        }

                        */

                        // Not Expired and Not Redeemed (Válidas)
                        List<Reward> rewardListValid = new ArrayList<Reward>();
                        // Expired (Expirada)
                        List<Reward> rewardListExpired = new ArrayList<Reward>();
                        // Redeemed (Resgatadas)
                        List<Reward> rewardListRedeemed = new ArrayList<Reward>();

                        for (int i = 0; i < latest.size(); i++) {
                            if (latest.get(i).isRewardRedeemed()) {
                                rewardListRedeemed.add(latest.get(i));
                            } else {
                                if (latest.get(i).isRewardExpired()) {
                                    rewardListExpired.add(latest.get(i));
                                } else {
                                    rewardListValid.add(latest.get(i));
                                }
                            }
                        }

                        finalList = new ArrayList<Reward>();

                        for (int i = 0; i < rewardListValid.size(); i++) {
                            finalList.add(rewardListValid.get(i));
                        }

                        for (int i = 0; i < rewardListRedeemed.size(); i++) {
                            finalList.add(rewardListRedeemed.get(i));
                        }

                        for (int i = 0; i < rewardListExpired.size(); i++) {
                            finalList.add(rewardListExpired.get(i));
                        }

                        return finalList;
                    } else {
                        return Collections.emptyList();
                    }
                } catch (final OperationCanceledException e) {
                    if (activity != null) {
                        activity.finish();
                    }
                    return initialItems;
                }
            }
        };

    }

    @Override
    public void onListItemClick(final ListView l, final View v, final int position, final long id) {
        final Reward reward = ((Reward) l.getItemAtPosition(position));

        Log.e("Presente", Integer.toString(reward.getIdToRescue()));

        RewardActivity activity = (RewardActivity) getActivity();
        if(reward.getResponsed() == false && reward.isRewardGift()) {
            activity.showGainGift(reward);
            return;
        }
        Intent intent = new Intent(activity, RewardDetailActivity.class);
        EstablishmentLocation location = new EstablishmentLocation();
        location.setId(reward.getLocationId());
        reward.setLocation(location);
        Facade.getInstance().setReward(reward);
        startActivity(intent);
    }

    @Override
    public void onLoadFinished(final Loader<List<Reward>> loader, final List<Reward> items) {
        if(items.isEmpty()) {
            showNoContentFragment(R.layout.no_content_rewards);
        }
        super.onLoadFinished(loader, items);
    }

    @Override
    public void onResume() {
        super.onResume();
        ListAdapter listAdapter = getListAdapter();
        ((RewardListAdapter)listAdapter).notifyDataSetChanged();
    }

    @Override
    protected int getErrorMessage(final Exception exception) {
        return R.string.error_loading_rewards;
    }

    @Override
    protected void addItems(List<Reward> items) {
        ListAdapter listAdapter = getListAdapter();
        RewardListAdapter adapter = (RewardListAdapter) listAdapter;
        adapter.addMultiTypeItems(items);
    }

    @Override
    protected TypeAdapter createAdapter(List<Reward> items) {
        return new RewardListAdapter(getActivity().getLayoutInflater(),
                getResources(), items, this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imgBtExcluir:
                Reward reward = (Reward) v.getTag();
                removeRecompensa(reward);
                break;
        }
    }

    private void removeRecompensa(final Reward reward) {
        new SafeAsyncTask<Boolean>() {

            @Override
            public Boolean call() throws Exception {
                service = serviceProvider.getService(getActivity());
                ApiResult result = null;

                if (reward.isRewardBuy()){
                    result = service.PostRewardBuyDiscart(reward.getIdToRescue());
                }else if (reward.isRewardGift()){
                    result = service.PostRewardGiftDiscart(reward.getIdToRescue());
                }else if (reward.isRewardShot()){
                    result = service.PostRewardShotDiscart(reward.getIdToRescue());
                }

                return result.isSuccess();
            }

            @Override
            protected void onException(Exception e) throws RuntimeException {
                super.onException(e);
                Ln.e("Error ao ler mensagem: %s", reward.getId());
            }

            @Override
            protected void onSuccess(Boolean aBoolean) throws Exception {
                super.onSuccess(aBoolean);
                if(aBoolean) {
                    Ln.d("Sucesso ao ler mensagem: %s", reward.getId());
                    navigateTo(RewardActivity.class);
//                    startActivity(starterIntent);
                    getActivity().finish();
                } else {
                    Ln.e("Error ao ler mensagem: %s", reward.getId());
                }
            }

        }.execute();
    }

    public void navigateTo(Class<? extends Activity> activity) {
        final Intent intent = new Intent(getActivity(), activity);
        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(Constants.Extra.HAS_GIFT, false);
        start(intent);
    }
    private void start(Intent intent) {
        intent.putExtra(Constants.Extra.MENU_ACTION, true);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        getActivity().startActivity(intent);
        getActivity().finish();
        //finish();
    }

}