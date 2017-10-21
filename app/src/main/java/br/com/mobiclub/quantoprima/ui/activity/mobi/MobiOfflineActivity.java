package br.com.mobiclub.quantoprima.ui.activity.mobi;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.squareup.otto.Subscribe;

import br.com.mobiclub.quantoprima.events.NavItemSelectedEvent;
import br.com.mobiclub.quantoprima.ui.activity.MenuActivity;
import br.com.mobiclub.quantoprima.ui.fragment.MobisOfflineFragment;

;

public class MobiOfflineActivity extends MenuActivity {

    private boolean errorOnUpdate = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        errorOnUpdate = false;
    }

    @Override
    protected Fragment createFragment() {
        return new MobisOfflineFragment();
    }

    @Override
    @Subscribe
    public void onNavigationItemSelected(NavItemSelectedEvent event) {
        super.onNavigationItemSelectedImpl(event);
    }

    @Override
    public void onBackPressed() {
        if(errorOnUpdate) {
            setResult(RESULT_CANCELED);
        } else {
            setResult(RESULT_OK);
        }
        super.onBackPressed();
    }

    public void onUpdateError() {
        errorOnUpdate = true;
    }
}
