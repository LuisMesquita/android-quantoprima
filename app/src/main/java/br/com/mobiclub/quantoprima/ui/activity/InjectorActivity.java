package br.com.mobiclub.quantoprima.ui.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

//import com.google.android.gms.analytics.HitBuilders;
//import com.google.android.gms.analytics.Tracker;
import com.squareup.otto.Bus;

import javax.inject.Inject;

import br.com.mobiclub.quantoprima.config.Injector;
import butterknife.ButterKnife;

/**
 *
 */
public class InjectorActivity extends ActionBarActivity {
    @Inject
    protected Bus eventBus;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Injector.inject(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

//        Tracker t = ((MobiClubApplication) getApplication()).getTracker(
//                MobiClubApplication.TrackerName.APP_TRACKER);
//        t.setScreenName(this.getClass().getSimpleName());
//        t.send(new HitBuilders.AppViewBuilder().build());

    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void setContentView(final int layoutResId) {
        super.setContentView(layoutResId);

        ButterKnife.inject(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        eventBus.register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        eventBus.unregister(this);
    }
}
