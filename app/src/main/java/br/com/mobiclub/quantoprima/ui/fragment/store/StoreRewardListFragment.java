package br.com.mobiclub.quantoprima.ui.fragment.store;

import android.accounts.OperationCanceledException;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.Loader;
import android.widget.ListView;

import com.github.kevinsawicki.wishlist.SingleTypeAdapter;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import br.com.mobiclub.quantoprima.R;
import br.com.mobiclub.quantoprima.config.Injector;
import br.com.mobiclub.quantoprima.core.service.MobiClubServiceApi;
import br.com.mobiclub.quantoprima.core.service.MobiClubServiceProvider;
import br.com.mobiclub.quantoprima.core.service.api.entity.ApiError;
import br.com.mobiclub.quantoprima.core.service.api.entity.ApiResult;
import br.com.mobiclub.quantoprima.core.service.api.entity.EstablishmentLocation;
import br.com.mobiclub.quantoprima.core.service.api.entity.LocationReward;
import br.com.mobiclub.quantoprima.domain.Mobi;
import br.com.mobiclub.quantoprima.events.NetworkErrorEvent;
import br.com.mobiclub.quantoprima.events.RestErrorEvent;
import br.com.mobiclub.quantoprima.facade.Facade;
import br.com.mobiclub.quantoprima.service.authenticator.LogoutService;
import br.com.mobiclub.quantoprima.ui.adapter.DialogResultAdapterImpl;
import br.com.mobiclub.quantoprima.ui.adapter.store.StoreRewardListAdapter;
import br.com.mobiclub.quantoprima.ui.adapter.store.StoreRewardListAdapterListener;
import br.com.mobiclub.quantoprima.ui.factory.AlertDialogFactory;
import br.com.mobiclub.quantoprima.ui.view.ResultDialog;
import br.com.mobiclub.quantoprima.ui.view.ThrowableLoader;
import br.com.mobiclub.quantoprima.domain.Constants;
import br.com.mobiclub.quantoprima.util.Ln;
import br.com.mobiclub.quantoprima.util.SafeAsyncTask;
import butterknife.ButterKnife;
import retrofit.RetrofitError;

public class StoreRewardListFragment extends ScrollGestureFragment<LocationReward>
        implements StoreRewardListAdapterListener {

    @Inject MobiClubServiceProvider serviceProvider;

    @Inject
    Bus bus;

    private EstablishmentLocation establishmentLocation;
    private AlertDialog successAlert;
    private StoreRewardListListener listener;

    public interface StoreRewardListListener {

        public void onGoRewards();

    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Injector.inject(this);

        Intent intent = getActivity().getIntent();
        establishmentLocation = Facade.getInstance().getLocation();
    }

    @Override
    public void onResume() {
        super.onResume();
        bus.register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        bus.unregister(this);
    }

    @Override
    protected LogoutService getLogoutService() {
        return null;
    }

    @Override
    public void onActivityCreated(final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setEmptyText(R.string.error_loading_store_products);

        ButterKnife.inject(this, getView());

        try {
            StoreRewardListListener activity = (StoreRewardListListener) getActivity();
            listener = activity;
        } catch (ClassCastException e) {
            throw new RuntimeException("StoreActivity must implement StoreRewardListListener");
        }
    }

    @Override
    protected void configureList(final Activity activity, final ListView listView) {
        super.configureList(activity, listView);

        listView.setDivider(this.getResources().getDrawable(R.drawable.store_color));
        listView.setDividerHeight(2);
    }

    @Subscribe
    public void onNetworkErrorEvent(NetworkErrorEvent e) {
        //AlertDialogFactory.createDefaultError(getActivity(),
        //        R.string.service_comunication_error).show();
    }

    @Subscribe
    public void onRestErrorEvent(RestErrorEvent e) {
        if(!e.isRewardBuyFailed()) {
            return;
        }
        ApiError apiError = e.getApiError();
        String message = null;
        if(apiError != null)
            message = apiError.getMessage();
        rewardBuyError(message);
    }

    @Override
    public void onBuyReward(final LocationReward locationReward) {
        Ln.d("onBuyReward: " + locationReward.getTitle());

        int score = establishmentLocation.getScore();
        int price = Integer.parseInt(locationReward.getPrice());
        if(score < price) {
            int title = R.string.store_reward_list_alert_no_mobis_title_message;
            int message = R.string.store_reward_list_alert_no_mobis_message;
            AlertDialogFactory.createDefaultError(getActivity(), title, message).show();
            return;
        } else {
            int title = R.string.store_reward_list_alert_buy_title_message;
            int message = R.string.store_reward_list_alert_buy_message;
            AlertDialog.Builder builder = AlertDialogFactory.createDefaultBuilder(getActivity(), title, message);
            builder.setPositiveButton(R.string.alert_yes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    onAcceptBuyReward(locationReward);
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
    }

    private void onAcceptBuyReward(final LocationReward locationReward) {
        new SafeAsyncTask<ApiResult>() {

            @Override
            public ApiResult call() throws Exception {
                FragmentActivity activity = getActivity();
                if(activity != null) {
                    MobiClubServiceApi service = serviceProvider.getService(activity);
                    int buyId = locationReward.getId();
                    int locationId = locationReward.getLocation().getId();
                    ApiResult result = service.buyReward(buyId, locationId);
                    return result;
                }
                return null;
            }

            @Override
            protected void onException(Exception e) throws RuntimeException {
                super.onException(e);
                if(!(e instanceof RetrofitError)) {
                    Ln.d(e);
                    rewardBuyError(getString(R.string.error_unknown));
                }
            }

            @Override
            protected void onSuccess(ApiResult result) throws Exception {
                super.onSuccess(result);
                if(result != null && result.isSuccess()) {
                    rewardBuySuccess(locationReward);
                } else if(result != null) {
                    Ln.e("Houve sucesso na requisicao mas o HttpStatus da mensagem não foi igual a 200");
                    rewardBuyError(result.getMessage());
                }
            }

        }.execute();
    }

    private void rewardBuySuccess(LocationReward locationReward) {
        successAlert = createSuccessAlert(locationReward);
        successAlert.show();
        //TODO: tocar som
    }

    private AlertDialog createSuccessAlert(final LocationReward locationReward) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.store_reward_alert_dialog_message_success);
        builder.setTitle(R.string.alert_dialog_title_success);

        builder.setPositiveButton(R.string.alert_yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                onSuccessAlertYes(locationReward);
            }
        });
        builder.setNegativeButton(R.string.alert_no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                onSuccessAlertNo(locationReward);
            }
        });
        return builder.create();
    }

    private void onSuccessAlertNo(LocationReward locationReward) {
        updateUI(locationReward);
        successAlert.dismiss();
    }

    private void updateUI(LocationReward locationReward) {
        if(getActivity() != null) {
            Fragment fragment = getParentFragment();
            if(fragment != null && fragment instanceof StoreFrontFragment) {
                StoreFrontFragment sff = (StoreFrontFragment) fragment;
                EstablishmentLocation location = Facade.getInstance().getLocation();
                sff.updateLocationMobis(location, Mobi.MOBIS_BUY_REWARD_TYPE,
                        Integer.parseInt(locationReward.getPrice()));
            }
        }
    }

    private void onSuccessAlertYes(LocationReward locationReward) {
        updateUI(locationReward);
        successAlert.dismiss();
        listener.onGoRewards();
    }

    private void rewardBuyError(String message) {
        DialogResultAdapterImpl dialogResultError = new DialogResultAdapterImpl();
        dialogResultError.setErrorMessage(message);

        ResultDialog dialog = new ResultDialog();
        Bundle args = new Bundle();
        args.putSerializable(Constants.Extra.DIALOG_RESULTER, dialogResultError);
        dialog.setArguments(args);

        dialog.show(getFragmentManager(), "resultDialog");
    }

    @Override
    public Loader<List<LocationReward>> onCreateLoader(final int id, final Bundle args) {
        final List<LocationReward> initialItems = items;
        return new ThrowableLoader<List<LocationReward>>(getActivity(), items) {
            @Override
            public List<LocationReward> loadData() throws Exception {
                try {
                    List<LocationReward> latest = null;

                    if (getActivity() != null && establishmentLocation != null &&
                            establishmentLocation.getId() > 0) {
                        MobiClubServiceApi service = serviceProvider.getService(getActivity());
                        latest = service.getRewardsByLocation(establishmentLocation.getId());
                        if(latest != null) {
                            for (int i = 0; i < latest.size(); i++) {
                                LocationReward locationReward = latest.get(i);
                                locationReward.setLocation(establishmentLocation);
                            }
                        }
                    }

                    if (latest != null) {
                        return latest;
                    } else {
                        return Collections.emptyList();
                    }
                } catch (final OperationCanceledException e) {
                    final Activity activity = getActivity();
                    if (activity != null) {
                        activity.finish();
                    }
                    return initialItems;
                }
            }
        };
    }

    @Override
    public void onLoadFinished(final Loader<List<LocationReward>> loader, final List<LocationReward> items) {
        super.onLoadFinished(loader, items);
    }

    @Override
    protected int getErrorMessage(final Exception exception) {
        if(exception instanceof RetrofitError) {
            return R.string.server_connection_error;
        }
        return R.string.error_loading_store_products;
    }

    @Override
    protected SingleTypeAdapter<LocationReward> createAdapter(final List<LocationReward> items) {
        return new StoreRewardListAdapter(this, getActivity().getLayoutInflater(),
                getResources(), items);
    }
}