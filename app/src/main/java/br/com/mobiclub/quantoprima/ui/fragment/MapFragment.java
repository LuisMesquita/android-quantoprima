//package br.com.mobiclub.quantoprima.ui.fragment;
//
//import android.annotation.SuppressLint;
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.view.View;
//
//import com.google.android.gms.maps.CameraUpdateFactory;
//import com.google.android.gms.maps.GoogleMap;
//import com.google.android.gms.maps.SupportMapFragment;
//import com.google.android.gms.maps.model.BitmapDescriptorFactory;
//import com.google.android.gms.maps.model.CameraPosition;
//import com.google.android.gms.maps.model.LatLng;
//import com.google.android.gms.maps.model.MarkerOptions;
//
//import java.util.List;
//
//import javax.inject.Inject;
//
//import br.com.mobiclub.quantoprima.R;
//import br.com.mobiclub.quantoprima.config.Injector;
//import br.com.mobiclub.quantoprima.core.service.MobiClubServiceProvider;
//import br.com.mobiclub.quantoprima.core.service.api.entity.Establishment;
//import br.com.mobiclub.quantoprima.core.service.api.entity.EstablishmentLocation;
//import br.com.mobiclub.quantoprima.core.service.api.entity.Localization;
//import br.com.mobiclub.quantoprima.facade.Facade;
//import br.com.mobiclub.quantoprima.service.map.PositionListener;
//import br.com.mobiclub.quantoprima.service.map.RouteDrawer;
//import br.com.mobiclub.quantoprima.domain.Constants;
//import br.com.mobiclub.quantoprima.util.Ln;
//
//@SuppressLint("ValidFragment")
//public class MapFragment extends SupportMapFragment {
//
//    private final PositionListener positionListener;
//    private GoogleMap map;
//
//	private RouteDrawer routeDrawer;
//
//	private LatLng locationPosition;
//
//    @Inject
//    protected MobiClubServiceProvider serviceProvider;
//    private List<EstablishmentLocation> locations;
//    private boolean drawRoute = false;
//
//    public MapFragment() {
//        positionListener = Facade.getInstance().getPositionListener();
//	}
//
//	@Override
//	public void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//        Injector.inject(this);
//	}
//
//    @Override
//    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//
//    }
//
//    @Override
//    public void onActivityCreated(Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        map = getMap();
//    }
//
//	@Override
//	public void onResume() {
//		super.onResume();
//        showMap();
//	}
//
//    private void showMap() {
//        map = getMap();
//
//        if(map == null) {
//            return;
//        }
//
//        Intent intent = getActivity().getIntent();
//        if(intent == null) {
//            Ln.e("Nenhuma location");
//            return;
//        }
//        Establishment establishment = (Establishment) intent.getSerializableExtra(Constants.Extra.LOCATIONS_MAPS);
//        if(establishment == null) {
//            Ln.e("Nenhuma location");
//            return;
//        }
//        List<EstablishmentLocation> locations = establishment.getLocations();
//        if(locations == null) {
//            Ln.e("Nenhuma location");
//            return;
//        }
//
//        Ln.d("Exibindo %d lojas no mapa", locations.size());
//        LatLng origin = null;
//        Localization userLocalization = positionListener.getLocalization();
//        if(userLocalization != null) {
//            origin = new LatLng(userLocalization.getLatitude(),
//                    userLocalization.getLongitude());
//        }
//        drawRoute(origin, locations);
//    }
//
//    public void drawRoute(LatLng origin, List<EstablishmentLocation> locations) {
//        if(map == null) {
//            return;
//        }
//
//		map.clear();
//
//        this.locations = locations;
//
//        if(origin != null) {
//            addUserMarker(origin);
//        }
//        if(locations.size() == 1) {
//            drawRoute = true;
//        }
//        routeDrawer = new RouteDrawer(map);
//        for (int i = 0; i < locations.size(); i++) {
//            EstablishmentLocation l = locations.get(i);
//            if(l != null) {
//                double latitude = l.getLatitude();
//                double longitude = l.getLongitude();
//
//                Ln.d("A loja %s esta em %f e %f", l.getReference(), latitude, longitude);
//                locationPosition = new LatLng(latitude, longitude);
//                //apenas loja
//                addCoffeeMarker(locationPosition, l.getReference());
//
//                if(origin != null && drawRoute) {
//                    Ln.d("Desenhando rota de lat=%f para lat=%f", origin.latitude, locationPosition.latitude);
//                    routeDrawer.draw(origin, locationPosition);
//                }
//            }
//        }
//
//        if(drawRoute) {
//            CameraPosition cameraPosition;
//            cameraPosition = new CameraPosition.Builder().target(
//                    locationPosition).zoom(12).build();
//            map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
//        } else {
//            CameraPosition cameraPosition;
//            if (origin != null) {
//                cameraPosition = new CameraPosition.Builder().target(origin).zoom(12).build();
//                map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
//            }
//        }
//
//	}
//
//    private void addUserMarker(LatLng origin) {
//        MarkerOptions marcador = new MarkerOptions();
//        marcador.position(origin);
//        map.addMarker(marcador);
//    }
//
//    private void addMarkers(LatLng origin, LatLng destination, String title) {
//        addCoffeeMarker(origin, null);
//        addCoffeeMarker(destination, title);
//    }
//
//    private void addCoffeeMarker(LatLng destination, String title) {
//        MarkerOptions marcador = new MarkerOptions();
//        if(title != null)
//            marcador.title(title);
//        marcador.icon(BitmapDescriptorFactory.fromResource(R.drawable.t27_pino_mapa_quantoprima));
//        marcador.position(destination);
//        map.addMarker(marcador);
//    }
//
//    /**
//     * Chamado quando uma nova posicao eh obtida
//     *
//     * @param position
//     */
//    public void center(LatLng position) {
//		drawRoute(position, locations);
//	}
//
//
//}