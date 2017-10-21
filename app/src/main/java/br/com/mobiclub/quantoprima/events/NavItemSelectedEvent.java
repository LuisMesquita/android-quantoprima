package br.com.mobiclub.quantoprima.events;

import android.view.MenuItem;

import br.com.mobiclub.quantoprima.R;

/**
 * Pub/Sub event used to communicate between fragment and activity.
 * Subscription occurs in the {@link br.com.mobiclub.quantoprima.ui.activity.HomeActivity}
 */
public class NavItemSelectedEvent {

    private final MenuItem menuItem;

    public NavItemSelectedEvent(MenuItem menuItem) {
        this.menuItem = menuItem;
    }

    public int getItemId() {
        if(menuItem == null)
            return R.id.menu_home; //assume o inicio
        return menuItem.getItemId();
    }
}
