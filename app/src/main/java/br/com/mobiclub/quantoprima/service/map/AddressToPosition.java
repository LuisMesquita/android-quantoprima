/**
 * 
 */
package br.com.mobiclub.quantoprima.service.map;

import android.content.Context;

//import com.google.android.gms.maps.model.LatLng;

/**
 *
 */
public class AddressToPosition {

	private Context context;
	
	public AddressToPosition(Context context) {
		super();
		this.context = context;
	}

//	public LatLng getPosition(String locationName) {
//		try {
//			Geocoder geocoder = new Geocoder(context);
//
//			List<Address> addresses = geocoder.getFromLocationName(locationName, 1);
//			if(!addresses.isEmpty()) {
//				Address address = addresses.get(0);
//				return new LatLng(address.getLatitude(), address.getLongitude());
//			}
//
//			return null;
//		} catch (Exception e) {
//			return null;
//		}
//	}

}
