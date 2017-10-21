package br.com.mobiclub.quantoprima.ui.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.squareup.otto.Subscribe;

import java.util.List;

import br.com.mobiclub.quantoprima.core.service.api.entity.EstablishmentLocation;
import br.com.mobiclub.quantoprima.events.NavItemSelectedEvent;
//import br.com.mobiclub.quantoprima.service.map.PositionListener;
//import br.com.mobiclub.quantoprima.ui.fragment.MapFragment;

public class MapActivity extends MenuActivity {

//    private PositionListener positionListener;
    private List<EstablishmentLocation> latest;

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

//        positionListener = Facade.getInstance().getPositionListener();
	}

    @Override
    protected Fragment createFragment() {
//        MapFragment mapFragment = new MapFragment();
//        if(positionListener != null)
//            positionListener.setMapFragment(mapFragment);
        return null;
    }

    @Override
    protected void onStart() {
        super.onStart();
//        positionListener.initLocalizationService();
    }

    @Override
	protected void onDestroy() {
		super.onDestroy();
//		positionListener.cancel();
	}

    @Subscribe
    @Override
    public void onNavigationItemSelected(NavItemSelectedEvent event) {
        super.onNavigationItemSelectedImpl(event);
    }

}
