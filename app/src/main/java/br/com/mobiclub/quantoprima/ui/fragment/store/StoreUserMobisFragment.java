package br.com.mobiclub.quantoprima.ui.fragment.store;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Date;

import javax.inject.Inject;

import br.com.mobiclub.quantoprima.R;
import br.com.mobiclub.quantoprima.config.Injector;
import br.com.mobiclub.quantoprima.core.service.MobiClubServiceProvider;
import br.com.mobiclub.quantoprima.core.service.api.entity.Establishment;
import br.com.mobiclub.quantoprima.core.service.api.entity.EstablishmentLocation;
import br.com.mobiclub.quantoprima.facade.Facade;
import br.com.mobiclub.quantoprima.ui.helper.LocationDetailsHelper;
import br.com.mobiclub.quantoprima.util.Ln;
import br.com.mobiclub.quantoprima.util.Util;
import butterknife.ButterKnife;
import butterknife.InjectView;

public class StoreUserMobisFragment extends Fragment implements GainMobiFragmentListener {

    @Inject protected MobiClubServiceProvider serviceProvider;

    private Establishment establishment;

    private EstablishmentLocation establishmentLocation;

    @InjectView(R.id.label_mobis_to_expire_message)
    TextView labelMobisExpireMessage;
    private LocationDetailsHelper helper;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Injector.inject(this);

        establishment = Facade.getInstance().getEstablishment();
        establishmentLocation = Facade.getInstance().getLocation();

        StoreUserMobisExtractListFragment fragment = new StoreUserMobisExtractListFragment();
        FragmentManager fm = getChildFragmentManager();
        fm.beginTransaction().replace(R.id.extract_list, fragment).commit();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_store_user_mobis, container, false);

        ButterKnife.inject(this, view);

        helper = new LocationDetailsHelper(view,
                establishmentLocation, establishment);
        helper.show();

        return view;
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
    public void onActivityCreated(final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(establishment == null) {
            //TODO: show error
            return;
        }
        //setEmptyText(R.string.no_users);
    }

    public void updateMobisToExpire(Date reference, int willLose) {
        String mobisToExpire = String.valueOf(willLose);
        String currentMonthEnd = Util.getDateString(reference);
        String message = getString(R.string.store_user_mobis_label_mobis_to_expire_message, currentMonthEnd, mobisToExpire);
        labelMobisExpireMessage.setText(Html.fromHtml(message));
    }

    @Override
    public void updateLocationMobis(EstablishmentLocation location, Integer mobisType, Integer buyValue) {
        if(location != null) {
            establishment = Facade.getInstance().getEstablishment();
            establishmentLocation = Facade.getInstance().getLocation();
        } else {
            Ln.e("Não foi possivel atualizar mobis.");
        }
    }
}
