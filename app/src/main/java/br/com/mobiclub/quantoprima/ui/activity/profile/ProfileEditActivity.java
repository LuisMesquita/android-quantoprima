package br.com.mobiclub.quantoprima.ui.activity.profile;

import android.support.v4.app.Fragment;

import com.squareup.otto.Subscribe;

import br.com.mobiclub.quantoprima.events.NavItemSelectedEvent;
import br.com.mobiclub.quantoprima.facade.Facade;
import br.com.mobiclub.quantoprima.ui.activity.MenuActivity;
import br.com.mobiclub.quantoprima.ui.fragment.profile.ProfileEditFragment;
import br.com.mobiclub.quantoprima.ui.fragment.profile.ProfileEditFragmentListener;

public class ProfileEditActivity extends MenuActivity implements ProfileEditFragmentListener {

    @Subscribe
    @Override
    public void onNavigationItemSelected(NavItemSelectedEvent event) {
        super.onNavigationItemSelectedImpl(event);
    }

    @Override
    protected Fragment createFragment() {
        return new ProfileEditFragment();
    }

    @Override
    public void profileEditedSuccess() {
        //TODO: melhorar isso
        Facade.getInstance().removeLastIntent();
        Facade.getInstance().removeLastIntent();
//        navigateTo(ProfileActivity.class);
    }
}
