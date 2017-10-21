package br.com.mobiclub.quantoprima.ui.fragment.home;

import android.accounts.OperationCanceledException;
import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.Loader;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.github.kevinsawicki.wishlist.SingleTypeAdapter;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import br.com.mobiclub.quantoprima.R;
import br.com.mobiclub.quantoprima.config.Injector;
import br.com.mobiclub.quantoprima.core.service.MobiClubServiceApi;
import br.com.mobiclub.quantoprima.core.service.MobiClubServiceProvider;
import br.com.mobiclub.quantoprima.core.service.api.entity.Establishment;
import br.com.mobiclub.quantoprima.core.service.api.entity.EstablishmentLocation;
import br.com.mobiclub.quantoprima.core.service.api.entity.EstablishmentWrapper;
import br.com.mobiclub.quantoprima.core.service.api.entity.Localization;
import br.com.mobiclub.quantoprima.facade.Facade;
import br.com.mobiclub.quantoprima.service.authenticator.LogoutService;
//import br.com.mobiclub.quantoprima.service.map.PositionListener;
import br.com.mobiclub.quantoprima.ui.adapter.EstablishmentListAdapter;
import br.com.mobiclub.quantoprima.ui.fragment.SearchListener;
import br.com.mobiclub.quantoprima.ui.fragment.store.ScrollGestureFragment;
import br.com.mobiclub.quantoprima.ui.helper.GainMobiHelper;
import br.com.mobiclub.quantoprima.ui.view.ThrowableLoader;
import br.com.mobiclub.quantoprima.domain.Constants;
import br.com.mobiclub.quantoprima.util.Ln;
import br.com.mobiclub.quantoprima.util.UIUtils;
import retrofit.RetrofitError;

/**
 * A fragment representing a list of Items.
 * <p />
 * <p />
 * interface.
 */
public class HomeFragment extends ScrollGestureFragment<Establishment>
        implements SearchListener {

    @Inject
    protected MobiClubServiceProvider serviceProvider;
    @Inject
    protected LogoutService logoutService;

    private HomeFragmentListener listener;
    private String query;
    private Establishment establishment;
    private LinearLayout gpsBar;
//    private PositionListener positionListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        establishment = Facade.getInstance().getEstablishment();

//        positionListener = Facade.getInstance().getPositionListener();

        Injector.inject(this);
    }

    @Override
    public void onStart() {
        super.onStart();
//        if(positionListener != null)
//            positionListener.initLocalizationService();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        if(positionListener != null)
//            positionListener.cancel();
    }

    @Override
    public void onRefresh() {
//        if(positionListener == null) {
//            positionListener = Facade.getInstance().getPositionListener();
//        }
//        if(positionListener != null)
//            positionListener.initLocalizationService();
        super.onRefresh();
    }

    @Override
    protected Drawable getListDivider() {
        return this.getResources().getDrawable(R.drawable.transparent_drawable);
    }

    @Override
    protected void configureList(Activity activity, ListView listView) {
        super.configureList(activity, listView);
    }

    @Override
    protected LogoutService getLogoutService() {
        return logoutService;
    }

    @Override
    public void onDestroyView() {
        setListAdapter(null);

        super.onDestroyView();
    }

    @Override
    protected SingleTypeAdapter<Establishment> createAdapter(List<Establishment> items) {
        return new EstablishmentListAdapter(getActivity().getLayoutInflater(), getResources(), items);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        gpsBar = (LinearLayout) view.findViewById(R.id.gps_bar);
    }

    @Override
    public void onQueryLocationChanged(String query) {
        Ln.d("Query %s", query);
        if(this.query != null && !this.query.equals(query)) {
            this.query = query;
            Bundle args = new Bundle();
            args.putString(Constants.Extra.QUERY_LOCATION, query);
            getLoaderManager().restartLoader(0, args, this);
        } else {
            this.query = query;
        }
    }

    public void updateLocationMobis(EstablishmentLocation location) {
        Ln.d("updateLocationMobis");
        getLoaderManager().restartLoader(0, null, this);
        //updateLocal(location);
    }

    private void updateLocal(EstablishmentLocation location) {
        final EstablishmentListAdapter listAdapter = (EstablishmentListAdapter) getListAdapter();
        for (int i = 0; i < items.size(); i++) {
            List<EstablishmentLocation> locations = listAdapter.getItem(i).getLocations();
            for (int j = 0; j < locations.size(); j++) {
                EstablishmentLocation item = locations.get(j);
                if(item != null && item.getId() == location.getId()) {
                    Ln.d("Atualizando score de %s", item.getReference());
                    Boolean hasReward = location.getHasReward();
                    Ln.d("hasReward = %s", hasReward);
                    item.setScore(item.getScore() + GainMobiHelper.MOBIS_TO_GAIN_VALUE);
                }
            }
        }
        FragmentActivity activity = getActivity();
        if(activity != null) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    listAdapter.setItems(items);
                    listAdapter.notifyDataSetChanged();
                }
            });
        }
    }

    public interface HomeFragmentListener {
        public void onItemClicked(Establishment establishment);

        public void onNewNotifications(Integer notifications);

        public void hideDarkener();

        public void noResult(boolean b);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            listener = (HomeFragmentListener) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement FragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    public void onListItemClick(ListView l, View v, int position, long id) {
        Establishment establishment = ((Establishment) l.getItemAtPosition(position));
        listener.onItemClicked(establishment);
    }

    @Override
    protected int getErrorMessage(Exception exception) {
        return R.string.server_connection_error;
    }

    @Override
    public Loader<List<Establishment>> onCreateLoader(int id, final Bundle args) {
        final List<Establishment> initialItems = items;
        if(establishment == null) {
            Ln.d("Recuperando locations do servidor");
            return getFromServer(args, initialItems);
        } else {
            Ln.d("Locations locais");
            return getLocal(initialItems);
        }
    }

    private Loader<List<Establishment>> getLocal(final List<Establishment> initialItems) {
        return new ThrowableLoader<List<Establishment>>(getActivity(), items) {
            @Override
            public List<Establishment> loadData() throws Exception {
                try {
                    List<Establishment> latest = null;
                    if (getActivity() != null) {
                        Ln.d("Carregando estabelecimentos");
                        List<Establishment> establishments = UIUtils.createLocationsFromEstablishment(establishment);
                        latest = establishments;
                    }
                    if(latest != null) {
                        return latest;
                    } else {
                        Ln.d("Nao encontrou nada");
                        return Collections.emptyList();
                    }
                } catch (Exception e) {
                    Ln.d(e);
                    return Collections.emptyList();
                }
            }
        };
    }

    private Loader<List<Establishment>> getFromServer(final Bundle args, final List<Establishment> initialItems) {
        return new ThrowableLoader<List<Establishment>>(getActivity(), items) {
            @Override
            public List<Establishment> loadData() throws Exception {
                try {
                    List<Establishment> latest = null;
                    if (getActivity() != null) {
                        Ln.d("Carregando estabelecimentos");
                        if(args != null) {
                            HomeFragment.this.query = (String) args.get(Constants.Extra.QUERY_LOCATION);
                        }
                        Double lat = 0.;
                        Double lon = 0.;
                        Localization userLocation = null;
//                        if(positionListener != null) {
//                            userLocation = positionListener.getLocalization();
//                        }
                        if(userLocation != null) {
                            lat = userLocation.getLatitude();
                            lon = userLocation.getLongitude();
                        }
                        MobiClubServiceApi service = serviceProvider.getService(getActivity());
                        Integer newNotifications = 0;
                        if(service != null && query != null && !query.isEmpty()) {
                            Ln.d("Searching %s", query);
                            EstablishmentWrapper response = service.getEstablishmentsByApp(query, lat, lon);
                            latest = response.getEstablishments();
                            newNotifications = service.getNewNotifications();
                        } else if(service != null) {
                            Ln.d("Busca/gps com %f %f", lat, lon);
                            EstablishmentWrapper response = service.getEstablishmentsByApp("", lat, lon);
                            latest = response.getEstablishments();
                            newNotifications = service.getNewNotifications();
                        }
                        if(newNotifications != null && listener != null) {
                            listener.onNewNotifications(newNotifications);
                        }
                    }
                    //trata
                    if(latest != null && !latest.isEmpty()) {
                        Establishment establishment = latest.get(0);
                        latest = UIUtils.createLocationsFromEstablishment(establishment);
                        return latest;
                    } else {
                        Ln.d("Nao encontrou nada");
                        return Collections.emptyList();
                    }
                } catch (OperationCanceledException e) {
                    Ln.d(e);
                    Activity activity = getActivity();
                    if (activity != null)
                        activity.finish();
                    return initialItems;
                }
            }
        };
    }

    @Override
    protected boolean enableSwipe() {
        return true;
    }

    @Override
    public void onLoadFinished(Loader<List<Establishment>> loader, List<Establishment> items) {
        Ln.d("onLoadFinished");
        listener.hideDarkener();
        if(items.isEmpty()) {
            listener.noResult(true);
        } else {
            listener.noResult(false);
        }
        Exception exception = getException(loader);
        if(exception != null && exception instanceof RetrofitError) {
            showNoLocations(false, false);
            items = null;
        }
        if(items != null && items.isEmpty()) {
            //nenhum resultado
            if(query == null) {
                showNoLocations(true, false);
            }
            else if(query != null) {
                showNoLocations(false, true);
            }
            Collections.sort(items);
        }
        super.onLoadFinished(loader, items);
    }

    private void showNoLocations(boolean gps, boolean query) {
        if(gps) {
            super.showNoContentFragment(R.layout.no_content_locations_gps);
        } else if(query) {
            super.showNoContentFragment(R.layout.no_content_locations_query);
        } else {
            super.showNoContentFragment(R.layout.fragment_home_offline);
        }
    }
}
