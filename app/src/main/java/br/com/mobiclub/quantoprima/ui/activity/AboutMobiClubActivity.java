package br.com.mobiclub.quantoprima.ui.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.otto.Subscribe;

import br.com.mobiclub.quantoprima.R;
import br.com.mobiclub.quantoprima.events.NavItemSelectedEvent;
import br.com.mobiclub.quantoprima.domain.Constants;
import br.com.mobiclub.quantoprima.ui.factory.AlertDialogFactory;

public class AboutMobiClubActivity extends MenuActivity {

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
        navigateToUrl(Constants.URLMobiClub.SITE, getString(R.string.web_view_site_sao_braz_title));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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
        navigateToUrl(Constants.URLMobiClub.FANPAGE_FACEBOOK, getString(R.string.web_view_facebook_title));
    }

    public void onTwitter(View view) {
        navigateToUrl(Constants.URLMobiClub.FANPAGE_TWITTER, getString(R.string.web_view_twitter_title));
    }

    public void onInstagram(View view) {
        navigateToUrl(Constants.URLMobiClub.FANPAGE_INSTAGRAM, getString(R.string.web_view_instagram_title));
    }

    public void onEvaluate(View view) {
        navigateToUrl(Constants.URLMobiClub.AVALIE_NA_PLAY_STORE, getString(R.string.web_view_avalie_na_play_store_title));
    }

    public void onIndicate(View view) {
        navigateToUrl(Constants.URLMobiClub.INDIQUE_LOJA, getString(R.string.web_view_indique_loja_title));
    }

    public static class AboutFragment extends Fragment {

        public AboutFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_about_mobiclub, container, false);
        }

    }

}
