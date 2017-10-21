package br.com.mobiclub.quantoprima.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import br.com.mobiclub.quantoprima.R;

public class NetworkUtil {

    public static int TYPE_WIFI = 1;
    public static int TYPE_MOBILE = 2;
    public static int TYPE_NOT_CONNECTED = 3;

    public static int getConnectivityType(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
 
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (null != activeNetwork) {
            int type = TYPE_NOT_CONNECTED;
            if(activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
                type = TYPE_WIFI;
             
            if(activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
                type = TYPE_MOBILE;

            if(isActive(activeNetwork)) {
                return type;
            } else {
                return TYPE_NOT_CONNECTED;
            }
        }
        return TYPE_NOT_CONNECTED;
    }

    private static boolean isActive(NetworkInfo activeNetwork) {
        return activeNetwork.isConnected() && activeNetwork.isAvailable();
    }

    public static boolean isConnected(Context context) {
        if(getConnectivityType(context) != TYPE_NOT_CONNECTED) {
            return true;
        }
        return false;
    }
     
    public static String getConnectivityTypeString(Context context) {
        int conn = NetworkUtil.getConnectivityType(context);
        String status = null;
        if (conn == NetworkUtil.TYPE_WIFI) {
            status = context.getString(R.string.wifi_connected);
        } else if (conn == NetworkUtil.TYPE_MOBILE) {
            status = context.getString(R.string.mobile_connected);
        } else if (conn == NetworkUtil.TYPE_NOT_CONNECTED) {
            status = context.getString(R.string.no_connection);
        }
        return status;
    }

}