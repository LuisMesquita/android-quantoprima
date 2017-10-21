package br.com.mobiclub.quantoprima.ui.fragment.store;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import br.com.mobiclub.quantoprima.R;
import br.com.mobiclub.quantoprima.config.Injector;
import br.com.mobiclub.quantoprima.core.service.MobiClubServiceProvider;
import br.com.mobiclub.quantoprima.core.service.api.entity.Establishment;
import br.com.mobiclub.quantoprima.core.service.api.entity.EstablishmentLocation;
import br.com.mobiclub.quantoprima.facade.Facade;
import br.com.mobiclub.quantoprima.service.authenticator.LogoutService;
import br.com.mobiclub.quantoprima.ui.activity.store.StoreActivity;
import br.com.mobiclub.quantoprima.ui.helper.LocationDetailsHelper;
import butterknife.ButterKnife;

public class StoreFrontFragment extends Fragment implements GainMobiFragmentListener {

    @Inject protected MobiClubServiceProvider serviceProvider;
    @Inject protected LogoutService logoutService;
    private Establishment establishment;

    private EstablishmentLocation establishmentLocation;
    private LocationDetailsHelper helper;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Injector.inject(this);

        establishment = Facade.getInstance().getEstablishment();
        establishmentLocation = Facade.getInstance().getLocation();

        StoreRewardListFragment fragment = new StoreRewardListFragment();

        FragmentManager fm = getChildFragmentManager();
        fm.beginTransaction()
                .replace(R.id.reward_list, fragment).commit();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_store_front, container, false);

        ButterKnife.inject(this, view);

        helper = new LocationDetailsHelper(view,
                establishmentLocation, establishment);
        helper.show();

        return view;
    }

    @Override
    public void onActivityCreated(final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(establishment == null || establishmentLocation == null) {
            //TODO: show error
            return;
        }
        //setEmptyText(R.string.no_users);
    }

    @Override
    public void onResume() {
        super.onResume();
        establishment = Facade.getInstance().getEstablishment();
        establishmentLocation = Facade.getInstance().getLocation();
        LocationDetailsHelper helper = new LocationDetailsHelper(getView(),
                establishmentLocation, establishment);
        helper.updateLocationMobis(establishmentLocation);
    }

    @Override
    public void updateLocationMobis(EstablishmentLocation location,
                                    Integer mobisType, Integer buyValue) {
        if(getActivity() != null) {
            StoreActivity sa = (StoreActivity) getActivity();
            sa.updateLocationMobis(location, mobisType, buyValue);
        }
    }
}
