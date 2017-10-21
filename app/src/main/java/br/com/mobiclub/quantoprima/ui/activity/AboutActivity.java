package br.com.mobiclub.quantoprima.ui.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.otto.Subscribe;

import java.util.List;

import javax.inject.Inject;

import br.com.mobiclub.quantoprima.R;
import br.com.mobiclub.quantoprima.core.service.MobiClubServiceApi;
import br.com.mobiclub.quantoprima.core.service.MobiClubServiceProvider;
import br.com.mobiclub.quantoprima.core.service.api.entity.Establishment;
import br.com.mobiclub.quantoprima.events.NavItemSelectedEvent;
import br.com.mobiclub.quantoprima.ui.factory.AlertDialogFactory;
import br.com.mobiclub.quantoprima.domain.Constants;
import br.com.mobiclub.quantoprima.ui.factory.ProgressDialogFactory;
import br.com.mobiclub.quantoprima.util.SafeAsyncTask;

public class AboutActivity extends MenuActivity {

    @Inject
    MobiClubServiceProvider serviceProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected Fragment createFragment() {
        return new AboutFragment();
    }

    @Subscribe
    @Override
    public void onNavigationItemSelected(NavItemSelectedEvent event) {
        super.onNavigationItemSelectedImpl(event);
    }

    public void onSite(View view) {
        navigateToUrl(Constants.URL.SITE, getString(R.string.web_view_site_sao_braz_title));
    }

    private void navigateToUrl(String url, String title) {
        if(!isConnected(true)) {
            AlertDialog defaultError = AlertDialogFactory.createDefaultError(this, R.string.alert_title_attention,
                    R.string.alert_title_webview_no_connection);
            defaultError.setCancelable(true);
            defaultError.show();
            return;
        }
        Intent browserIntent = new Intent(this, WebViewActivity.class);
        browserIntent.putExtra(Constants.Extra.WEB_VIEW_URL, url);
        browserIntent.putExtra(Constants.Extra.WEB_VIEW_TITLE, title);
        startActivity(browserIntent);
    }

    public void onFacebook(View view) {
        navigateToUrl(Constants.URL.FANPAGE_FACEBOOK, getString(R.string.web_view_facebook_title));
    }

    public void onTwitter(View view) {
        navigateToUrl(Constants.URL.FANPAGE_TWITTER, getString(R.string.web_view_twitter_title));
    }

    public void onInstagram(View view) {
        navigateToUrl(Constants.URL.FANPAGE_INSTAGRAM, getString(R.string.web_view_instagram_title));
    }

    public void onEvaluate(View view) {
        navigateToUrl(Constants.URL.AVALIE_NA_PLAY_STORE, getString(R.string.web_view_avalie_na_play_store_title));
    }

    public void onFind(View view) {
        final ProgressDialog dialog = ProgressDialogFactory.createProgress(AboutActivity.this,
                R.string.loading_message);
        dialog.show();

        new SafeAsyncTask<Establishment>() {

            @Override
            public Establishment call() throws Exception {

                MobiClubServiceApi service = serviceProvider.getService(AboutActivity.this);
                List<Establishment> establishments = service.getLocationsPositions();
                if(establishments != null && !establishments.isEmpty()) {
                    Establishment establishment = establishments.get(0);
                    return establishment;
                }
                return null;
            }

            @Override
            protected void onException(Exception e) throws RuntimeException {
                super.onException(e);
                AlertDialogFactory.createDefaultError(AboutActivity.this, R.string.server_connection_error).show();
                dialog.dismiss();
            }

            @Override
            protected void onSuccess(Establishment establishment) throws Exception {
                super.onSuccess(establishment);
                dialog.dismiss();
                if(establishment != null) {
                    if(!establishment.getLocations().isEmpty()) {
                        Intent intent = new Intent(AboutActivity.this, MapActivity.class);
                        intent.putExtra(Constants.Extra.LOCATIONS_MAPS, establishment);
                        navigateTo(intent);
                    } else {
                        AlertDialogFactory.createDefaultError(AboutActivity.this, R.string.no_data).show();
                    }
                } else {
                    AlertDialogFactory.createDefaultError(AboutActivity.this, R.string.no_data).show();
                }
            }

        }.execute();
    }

    public static class AboutFragment extends Fragment {

        public AboutFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_about, container, false);
        }

    }

}
