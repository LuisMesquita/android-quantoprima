package br.com.mobiclub.quantoprima.ui.activity.notification;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.squareup.otto.Subscribe;

import br.com.mobiclub.quantoprima.events.NavItemSelectedEvent;
import br.com.mobiclub.quantoprima.ui.activity.MenuActivity;
import br.com.mobiclub.quantoprima.ui.fragment.notification.NotificationsConfigFragment;

public class NotificationsConfigActivity extends MenuActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Subscribe
    @Override
    public void onNavigationItemSelected(NavItemSelectedEvent event) {
        super.onNavigationItemSelectedImpl(event);
    }

    @Override
    protected Fragment createFragment() {
        return new NotificationsConfigFragment();
    }

}
