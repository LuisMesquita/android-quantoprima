

package br.com.mobiclub.quantoprima.ui.adapter.store;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import br.com.mobiclub.quantoprima.R;
import br.com.mobiclub.quantoprima.core.service.api.entity.Establishment;
import br.com.mobiclub.quantoprima.core.service.api.entity.EstablishmentLocation;
import br.com.mobiclub.quantoprima.facade.Facade;
import br.com.mobiclub.quantoprima.ui.fragment.store.StoreFrontFragment;
import br.com.mobiclub.quantoprima.ui.fragment.store.StoreInformationFragment;
import br.com.mobiclub.quantoprima.ui.fragment.store.StoreUserMobisFragment;

/**
 * Store pager adapter
 */
public class StorePagerAdapter extends FragmentPagerAdapter {

    private final Activity activity;
    private final Resources resources;
    private final FragmentManager fragmentManager;

    /**
     * Create pager adapter
     *
     * @param resources
     * @param fragmentManager
     */
    public StorePagerAdapter(Activity activity, final Resources resources,
                             final FragmentManager fragmentManager) {
        super(fragmentManager);
        this.activity = activity;
        this.resources = resources;
        this.fragmentManager = fragmentManager;
    }

    public void updateLocationMobis(EstablishmentLocation location) {
        for (int i = 0; i < getCount(); i++) {
            Fragment fragment = getItem(i);

            Establishment establishment = Facade.getInstance().getEstablishment();
            EstablishmentLocation establishmentLocation = Facade.getInstance().getLocation();

            establishmentLocation.setScore(location.getScore());
            establishment.setLocation(establishmentLocation);
        }
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        return super.instantiateItem(container, position);
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public Fragment getItem(final int position) {
        final Fragment result;
        switch (position) {
            case 0:
                result = new StoreInformationFragment();
                break;
            case 1:
                result = new StoreFrontFragment();
                break;
            case 2:
                result = new StoreUserMobisFragment();
                break;
            default:
                result = null;
                break;
        }
        if (result != null) {
            Bundle args = new Bundle();
            result.setArguments(args);
        }
        return result;
    }

    @Override
    public CharSequence getPageTitle(final int position) {
        switch (position) {
            case 0:
                return resources.getString(R.string.page_store_information);
            case 1:
                return resources.getString(R.string.page_store_storefront);
            case 2:
                return resources.getString(R.string.page_store_mobis);
            default:
                return null;
        }
    }
}
