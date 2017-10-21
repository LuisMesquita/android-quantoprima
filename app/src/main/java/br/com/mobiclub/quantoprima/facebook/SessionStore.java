package br.com.mobiclub.quantoprima.facebook;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.facebook.android.dialog.Facebook;

public class SessionStore {

    private static final String TOKEN = "access_token";
    private static final String EXPIRES = "access_expires";
    private static final String KEY = "facebook-session";

    public static boolean save(Facebook session, Context context) {
        Editor editor = context.getSharedPreferences(KEY, Context.MODE_PRIVATE).edit();
        editor.putString(TOKEN, session.getAccessToken());
        editor.putLong(EXPIRES, session.getAccessExpires());
        return editor.commit();
    }
    
    public static boolean restore(Facebook session, Context context) {
        SharedPreferences savedSession = context.getSharedPreferences(KEY, Context.MODE_PRIVATE);
        session.setAccessToken(savedSession.getString(TOKEN, null));
        session.setAccessExpires(savedSession.getLong(EXPIRES, 0));
        return session.isSessionValid();
    }

    public static void clear(Context context) {
        Editor editor = context.getSharedPreferences(KEY, Context.MODE_PRIVATE).edit();
        editor.clear();
        editor.commit();
    }
    
    public static String getAccessToken(Context context) {
    	SharedPreferences sPref = context.getSharedPreferences(KEY, Context.MODE_PRIVATE);    			
        String accessToken = sPref.getString(TOKEN, null);
        
        return accessToken;
    }
    
    public static long getAccessExpires(Context context) {
    	SharedPreferences sPref = context.getSharedPreferences(KEY, Context.MODE_PRIVATE);    			
        long accessExpires = sPref.getLong(EXPIRES, 0);
        
        return accessExpires;
    } 

}
