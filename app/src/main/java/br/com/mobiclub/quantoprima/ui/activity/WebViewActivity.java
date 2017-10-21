package br.com.mobiclub.quantoprima.ui.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.squareup.otto.Subscribe;

import br.com.mobiclub.quantoprima.R;
import br.com.mobiclub.quantoprima.events.NavItemSelectedEvent;
import br.com.mobiclub.quantoprima.ui.factory.AlertDialogFactory;
import br.com.mobiclub.quantoprima.domain.Constants;
import br.com.mobiclub.quantoprima.util.Ln;
import br.com.mobiclub.quantoprima.util.NetworkUtil;

public class WebViewActivity extends MenuActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Let's display the progress in the activity title bar, like the
        // browser app does.
        getWindow().requestFeature(Window.FEATURE_PROGRESS);

        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        String title = intent.getStringExtra(Constants.Extra.WEB_VIEW_TITLE);
        if(title != null) {
            getSupportActionBar().setTitle(title);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        boolean connected = NetworkUtil.isConnected(this);
        if(!connected) {
            AlertDialog defaultError = AlertDialogFactory.createDefaultError(this, R.string.alert_title_attention,
                    R.string.alert_title_webview_no_connection);
            defaultError.setCancelable(true);
            defaultError.show();
            return;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected Fragment createFragment() {
        MobiClubWebViewFragment mobiClubWebViewFragment = new MobiClubWebViewFragment();
        Intent intent = getIntent();
        String stringExtra = intent.getStringExtra(Constants.Extra.WEB_VIEW_URL);
        Bundle args = new Bundle();
        args.putString(stringExtra, Constants.Extra.WEB_VIEW_URL);
        mobiClubWebViewFragment.setArguments(args);
        mobiClubWebViewFragment.setURL(stringExtra);
        return mobiClubWebViewFragment;
    }

    @Subscribe
    @Override
    public void onNavigationItemSelected(NavItemSelectedEvent event) {
        super.onNavigationItemSelectedImpl(event);
    }

    public static class MobiClubWebViewFragment extends Fragment {

        private String URL;
        private WebView webview;

        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_web_view, container, false);


            final ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.pb_loading);
            progressBar.setVisibility(View.VISIBLE);

            final AnimationDrawable anim = (AnimationDrawable) progressBar.getBackground();
            if (anim != null) anim.start();

            webview = (WebView) view.findViewById(R.id.web_view);
            webview.getSettings().setJavaScriptEnabled(true);

            webview.setWebChromeClient(new WebChromeClient() {
                public void onProgressChanged(WebView view, int progress) {
                    // Activities and WebViews measure progress with different scales.
                    // The progress meter will automatically disappear when we reach 100%
                    if(progress >= 100) {
                        progressBar.setVisibility(View.GONE);
                    }
                }
            });
            webview.setWebViewClient(new WebViewClient() {
                public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                }
            });

            webview.setWebViewClient(new WebViewClient() {
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    Ln.i("Processing webview url click...");
                    view.loadUrl(url);
                    return true;
                }

                public void onPageFinished(WebView view, String url) {
                    Ln.i("Finished loading URL: " +url);
                    progressBar.setVisibility(View.GONE);
                    //if (progressBar.isShowing()) {
                    //    progressBar.dismiss();
                    //}
                }

                public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                    Ln.e("Error: " + description);
                    progressBar.setVisibility(View.GONE);
                    //TODO:
//                Toast.makeText(activity, "Oh no! " + description, Toast.LENGTH_SHORT).show();
//                alertDialog.setTitle("Error");
//                alertDialog.setMessage(description);
//                alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        return;
//                    }
//                });
//                alertDialog.show();
                }
            });

            Bundle arguments = getArguments();
            if(arguments != null) {
                String url = (String) arguments.getString(Constants.Extra.WEB_VIEW_URL);
                if(url == null) {
                    url = this.URL;
                }
                if(url != null && !url.startsWith("http://") && !url.startsWith("https://")) {
                    url = "http://" + url;
                }
                Ln.d("WebView URL = %s", url);
                webview.loadUrl(url);
            }

            return view;
        }

        @Override
        public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
        }

        public void setURL(String URL) {
            this.URL = URL;
        }

        public String getURL() {
            return URL;
        }

        public void reload() {
            webview.reload();
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        boolean b = super.onCreateOptionsMenu(menu);
//        getMenuInflater().inflate(R.menu.resfresh, menu);
//        return b;
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.refresh) {
            Ln.d("refresh");
            FragmentManager fragmentManager = getSupportFragmentManager();
            Fragment fragmentById = fragmentManager.findFragmentById(R.id.container);
            if(fragmentById != null && fragmentById instanceof MobiClubWebViewFragment) {
                MobiClubWebViewFragment f = (MobiClubWebViewFragment) fragmentById;
                f.reload();
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
