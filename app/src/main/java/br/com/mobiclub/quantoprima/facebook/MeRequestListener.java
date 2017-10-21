package br.com.mobiclub.quantoprima.facebook;

import com.facebook.android.dialog.AsyncFacebookRunner.RequestListener;
import com.facebook.android.dialog.FacebookError;
import com.facebook.android.dialog.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;

import br.com.mobiclub.quantoprima.util.Ln;

public abstract class MeRequestListener implements RequestListener {
	
	private String accessToken;
	private long accessExpires;
	
	public MeRequestListener (String accessToken, long accessExpires) {
		this.accessToken = accessToken;
		this.accessExpires = accessExpires;
	}

	@Override
	public void onComplete(String response, Object state) {
		try {
			JSONObject json = Util.parseJson(response);
			
			long facebookId = json.getLong("id");

			String name = json.getString("first_name");

            String lastName = json.getString("last_name");
			
			String email = "";
			if (!json.isNull("email")) {
				email = json.getString("email");
			}
			
			String birthday = "";
			if (!json.isNull("birthday")) {
				birthday = json.getString("birthday");
			}
			
			String hometown = "";
			if (!json.isNull("hometown")) {
				hometown = json.getJSONObject("hometown").getString("name");
			}
			
			String location = "";
			if (!json.isNull("location")) {
				location = json.getJSONObject("location").getString("name");
			}
			
			String gender = "";
			if (!json.isNull("gender")) {
				gender = json.getString("gender");
			}
			
			String locale = "";
			if (!json.isNull("locale")) {
				locale = json.getString("locale");
			}

			this.onComplete(
                    new FacebookUserInfo(facebookId, name, lastName, email, birthday, hometown, location, gender, locale, accessToken, accessExpires));
		} catch (JSONException e) {
			Ln.e(e);
		} catch (FacebookError e) {
            Ln.e(e);
		}
		
	}
	
	public abstract void onComplete(FacebookUserInfo facebookUserInfo);

	@Override
	public void onIOException(IOException e, Object state) {
	}

	@Override
	public void onFileNotFoundException(FileNotFoundException e, Object state) {
	}

	@Override
	public void onMalformedURLException(MalformedURLException e, Object state) {
	}

	@Override
	public void onFacebookError(FacebookError e, Object state) {
	}

    public static class FacebookUserInfo {
        private final long facebookId;
        private final String name;
        private final String email;
        private final String birthday;
        private final String hometown;
        private final String location;
        private final String gender;
        private final String locale;
        private final String accessToken;
        private final long accessExpires;
        private final String lastName;

        private FacebookUserInfo(long facebookId, String name, String lastName, String email, String birthday, String hometown, String location, String gender, String locale, String accessToken, long accessExpires) {
            this.facebookId = facebookId;
            this.name = name;
            this.lastName = lastName;
            this.email = email;
            this.birthday = birthday;
            this.hometown = hometown;
            this.location = location;
            this.gender = gender;
            this.locale = locale;
            this.accessToken = accessToken;
            this.accessExpires = accessExpires;
        }

        public long getFacebookId() {
            return facebookId;
        }

        public String getName() {
            return name;
        }

        public String getEmail() {
            return email;
        }

        public String getBirthday() {
            return birthday;
        }

        public String getHometown() {
            return hometown;
        }

        public String getLocation() {
            return location;
        }

        public String getGender() {
            return gender;
        }

        public String getLocale() {
            return locale;
        }

        public String getAccessToken() {
            return accessToken;
        }

        public long getAccessExpires() {
            return accessExpires;
        }

        public String getLastName() {
            return lastName;
        }
    }
}
