package br.com.mobiclub.quantoprima.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;

import br.com.mobiclub.quantoprima.R;
import br.com.mobiclub.quantoprima.config.Injector;
import butterknife.ButterKnife;

/**
 *
 */
public abstract class MobiClubActionBarActivity extends ActionBarActivity {

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Injector.inject(this);
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

}
