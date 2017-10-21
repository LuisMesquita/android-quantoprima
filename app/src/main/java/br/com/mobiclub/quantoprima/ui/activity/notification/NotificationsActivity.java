package br.com.mobiclub.quantoprima.ui.activity.notification;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Menu;

import com.squareup.otto.Subscribe;

import br.com.mobiclub.quantoprima.events.NavItemSelectedEvent;
import br.com.mobiclub.quantoprima.ui.activity.MenuActivity;
import br.com.mobiclub.quantoprima.ui.fragment.notification.NotificationListFragment;

public class NotificationsActivity extends MenuActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //showSearchBar();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean result = super.onCreateOptionsMenu(menu);
        menu.getItem(0).setVisible(false);
        return result;
    }

    @Override
    protected Fragment createFragment() {
        return new NotificationListFragment();
    }

    @Subscribe
    @Override
    public void onNavigationItemSelected(NavItemSelectedEvent event) {
        super.onNavigationItemSelectedImpl(event);
    }
}
