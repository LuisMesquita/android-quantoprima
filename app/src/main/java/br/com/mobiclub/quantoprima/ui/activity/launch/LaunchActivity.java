package br.com.mobiclub.quantoprima.ui.activity.launch;

import android.accounts.AccountsException;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;

import java.io.IOException;

import javax.inject.Inject;

import br.com.mobiclub.quantoprima.R;
import br.com.mobiclub.quantoprima.core.service.MobiClubServiceApi;
import br.com.mobiclub.quantoprima.core.service.MobiClubServiceProvider;
import br.com.mobiclub.quantoprima.core.service.api.exception.AppOutdatedException;
import br.com.mobiclub.quantoprima.domain.DpiEnum;
//import br.com.mobiclub.quantoprima.service.map.PositionListener;
import br.com.mobiclub.quantoprima.ui.activity.FullScreenMobiClubActivity;
import br.com.mobiclub.quantoprima.ui.activity.account.AuthenticationListener;
import br.com.mobiclub.quantoprima.ui.activity.account.AuthenticatorTask;
import br.com.mobiclub.quantoprima.ui.activity.account.SigninActivity;
import br.com.mobiclub.quantoprima.ui.view.ImageLoader;
import br.com.mobiclub.quantoprima.util.Ln;

public class LaunchActivity extends FullScreenMobiClubActivity
        implements AuthenticationListener {

    private static final int OUTDATED_REQUEST = 0;
    private static final int SIGN_REQUEST = 1;
    @Inject
    MobiClubServiceProvider serviceProvider;
    private TextView labelLoadings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        TextView labelLoadings = (TextView) findViewById(R.id.label_loading);
        labelLoadings.setVisibility(View.GONE);

        try {
            Crashlytics.start(this);
        } catch (Exception e) {
        }

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        int densityDpi = metrics.densityDpi;

        ImageLoader instance = ImageLoader.getInstance();
        if(densityDpi == DisplayMetrics.DENSITY_MEDIUM) {
            instance.setDpi(DpiEnum.MDPI);
        } else if(densityDpi == DisplayMetrics.DENSITY_XHIGH ||
                densityDpi == DisplayMetrics.DENSITY_XXHIGH ||
                densityDpi == DisplayMetrics.DENSITY_XXXHIGH) {
            instance.setDpi(DpiEnum.XDPI);
        } else if(densityDpi == DisplayMetrics.DENSITY_HIGH) {
            instance.setDpi(DpiEnum.HDPI);
        } else if(densityDpi == DisplayMetrics.DENSITY_LOW) {
            instance.setDpi(DpiEnum.LDPI);
        }

        new Loading().execute(new Void[0]);
    }

    @Override
    public int getLayout() {
        return 0;
    }

    public class Loading extends AsyncTask<Void, Void, Boolean> {
        public Loading() {
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            //check if outdated
            //TODO: armazenar se jah atualizou
            return isUpdated();
            //return true;
        }

        protected void onPostExecute(Boolean isUpdated) {
//            Facade.getInstance().createPositionListener(LaunchActivity.this);
            //labelThings.setText(R.string.activity_launch_starting);
            if(isUpdated) {
                AuthenticatorTask authenticatorTask =
                        new AuthenticatorTask(serviceProvider, LaunchActivity.this);
                authenticatorTask.checkLocalAuth(); //async
            } else {
                startOutdated();
            }
        }

        protected void onPreExecute() {
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == OUTDATED_REQUEST || requestCode == SIGN_REQUEST) {
            finish();
        }
    }

    private void startOutdated() {
        Intent updateIntent = new Intent(this, OutdatedActivity.class);
        startActivityForResult(updateIntent, OUTDATED_REQUEST);
    }

    @Override
    public void onAuthenticationFail() {
        Ln.e("onAuthenticationFail");
        final Intent intent = new Intent(this, SigninActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onAuthenticationCancelled() {
        //finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        switch(requestCode) {
//            case PositionListener.GPS_PERMS_REQUEST:
//                Facade.getInstance().createPositionListener(LaunchActivity.this);
//                break;
//        }
    }

    @Override
    public void onAuthentication(boolean authenticated) {
        if(authenticated) {
            Ln.d("Iniciou Splash pela autenticacao ja existente");
            startActivity(new Intent(this, SplashActivity.class));
        }
        finish();
    }

    private boolean isUpdated() {
        if (!isLastVersion()) {
            return false;
        }
        return true;
    }

    private boolean isLastVersion() {
        boolean isLastVersion = true;

        try {
            boolean auth = serviceProvider.checkAuth(this);
            if(auth) {
                MobiClubServiceApi service = serviceProvider.getService(this);
                service.getEstablishmentsByApp("", 0.0, 0.0);
            }
        } catch (IOException e) {
            Ln.e(e);
        } catch (AccountsException e) {
            Ln.e(e);
        } catch(AppOutdatedException e) {
            isLastVersion = false;
        } catch (Exception e) {

        }

        return isLastVersion;
    }

}
