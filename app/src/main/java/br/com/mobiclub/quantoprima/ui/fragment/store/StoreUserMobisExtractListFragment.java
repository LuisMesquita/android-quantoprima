package br.com.mobiclub.quantoprima.ui.fragment.store;

import android.accounts.OperationCanceledException;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.widget.ListView;

import com.github.kevinsawicki.wishlist.SingleTypeAdapter;

import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import br.com.mobiclub.quantoprima.R;
import br.com.mobiclub.quantoprima.config.Injector;
import br.com.mobiclub.quantoprima.core.service.MobiClubServiceApi;
import br.com.mobiclub.quantoprima.core.service.MobiClubServiceProvider;
import br.com.mobiclub.quantoprima.core.service.api.entity.EstablishmentLocation;
import br.com.mobiclub.quantoprima.core.service.api.entity.ExtractDetails;
import br.com.mobiclub.quantoprima.facade.Facade;
import br.com.mobiclub.quantoprima.service.authenticator.LogoutService;
import br.com.mobiclub.quantoprima.ui.adapter.store.StoreUserMobisExtractListAdapter;
import br.com.mobiclub.quantoprima.ui.view.ThrowableLoader;

public class StoreUserMobisExtractListFragment extends ScrollGestureFragment<ExtractDetails> {

    @Inject protected MobiClubServiceProvider serviceProvider;

    private EstablishmentLocation establishmentLocation;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Injector.inject(this);

        Intent intent = getActivity().getIntent();
        establishmentLocation = Facade.getInstance().getLocation();
    }

    @Override
    protected LogoutService getLogoutService() {
        return null;
    }

    @Override
    public void onActivityCreated(final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setEmptyText(R.string.no_store_user_mobis_extracts);
    }

    @Override
    protected void configureList(final Activity activity, final ListView listView) {
        super.configureList(activity, listView);

        listView.setDividerHeight(2);
    }

    @Override
    public Loader<List<ExtractDetails>> onCreateLoader(final int id, final Bundle args) {
        final List<ExtractDetails> initialItems = items;
        return new ThrowableLoader<List<ExtractDetails>>(getActivity(), items) {
            @Override
            public List<ExtractDetails> loadData() throws Exception {

                try {
                    List<ExtractDetails> latest = null;

                    if (getActivity() != null && establishmentLocation != null &&
                            establishmentLocation.getId() > 0) {
                        MobiClubServiceApi service = serviceProvider.getService(getActivity());
                        latest = service.getUserMobisExtract(establishmentLocation.getId());
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
    public void onLoadFinished(final Loader<List<ExtractDetails>> loader,
                               final List<ExtractDetails> items) {
        super.onLoadFinished(loader, items);

        StoreUserMobisFragment storeUserMobisFragment = (StoreUserMobisFragment) getParentFragment();
        Calendar actualDate = Calendar.getInstance();
        Calendar reference = Calendar.getInstance();
        for (int i = 0; i < items.size(); i++) {
            ExtractDetails extractDetails = items.get(i);
            reference.setTime(extractDetails.getReference());
            if(isActualMonth(reference, actualDate)) {
                storeUserMobisFragment.updateMobisToExpire(extractDetails.getReference(),
                        extractDetails.getWillLose());
                break;
            }
        }
    }

    private boolean isActualMonth(Calendar reference, Calendar actual) {
        Calendar c = Calendar.getInstance();
        if(reference.get(Calendar.MONTH) == actual.get(Calendar.MONTH) &&
                reference.get(Calendar.YEAR) == actual.get(Calendar.YEAR)) {
            return true;
        }
        return false;
    }

    @Override
    protected int getErrorMessage(final Exception exception) {
        return R.string.server_connection_error;
    }

    @Override
    protected SingleTypeAdapter<ExtractDetails> createAdapter(final List<ExtractDetails> items) {
        return new StoreUserMobisExtractListAdapter(getActivity().getLayoutInflater(),
                getResources(), items);
    }
}
