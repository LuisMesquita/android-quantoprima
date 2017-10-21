package br.com.mobiclub.quantoprima.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.MenuItem;

//import com.google.android.gms.analytics.HitBuilders;
//import com.google.android.gms.analytics.Tracker;
import com.squareup.otto.Bus;

import javax.inject.Inject;

import br.com.mobiclub.quantoprima.R;
import br.com.mobiclub.quantoprima.config.Injector;
import br.com.mobiclub.quantoprima.ui.common.MobiClubActivity;
import butterknife.ButterKnife;

/**
 *
 */
public abstract class FullScreenMobiClubActivity extends FragmentActivity
        implements MobiClubActivity {

    @Inject
    protected Bus eventBus;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int layout = getLayout();
        if(layout != 0) {
            setContentView(layout);
        }

        Injector.inject(this);
    }

    @Override
    public void onStart() {
        super.onStart();

//        Tracker t = ((MobiClubApplication) getApplication()).getTracker(
//                MobiClubApplication.TrackerName.APP_TRACKER);
//        t.setScreenName(this.getClass().getSimpleName());
//        t.send(new HitBuilders.AppViewBuilder().build());

    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void setContentView(final int layoutResId) {
        super.setContentView(layoutResId);

        // Used to inject views with the Butterknife library
        ButterKnife.inject(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.central_action:
                onCentral();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void onCentral() {
        final Intent intent = new Intent(this, CentralActivity.class);
        startActivity(intent);
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

    @Override
    public abstract int getLayout();

}
