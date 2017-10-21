package br.com.mobiclub.quantoprima.ui.activity.profile;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.squareup.otto.Subscribe;

import br.com.mobiclub.quantoprima.events.NavItemSelectedEvent;
import br.com.mobiclub.quantoprima.ui.activity.MenuActivity;
import br.com.mobiclub.quantoprima.ui.activity.notification.NotificationsConfigActivity;
import br.com.mobiclub.quantoprima.ui.fragment.profile.ProfileFragment;
import br.com.mobiclub.quantoprima.ui.fragment.profile.ProfileFragmentListener;
import br.com.mobiclub.quantoprima.util.Ln;

public class ProfileActivity extends MenuActivity implements ProfileFragmentListener {

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
        return new ProfileFragment();
    }

    public void editProfile(View view) {
        Ln.d("editProfile");
        Intent intent = new Intent(this, ProfileEditActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onConfigNotifications() {
        Ln.d("configNotifications");
        Intent intent = new Intent(this, NotificationsConfigActivity.class);
        startActivity(intent);
    }
}
