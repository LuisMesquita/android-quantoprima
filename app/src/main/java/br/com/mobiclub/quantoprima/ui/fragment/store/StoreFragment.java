package br.com.mobiclub.quantoprima.ui.fragment.store;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.viewpagerindicator.TabPageIndicator;

import br.com.mobiclub.quantoprima.R;
import br.com.mobiclub.quantoprima.core.service.api.entity.EstablishmentLocation;
import br.com.mobiclub.quantoprima.ui.adapter.store.StorePagerAdapter;
import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 *
 */
public class StoreFragment extends Fragment {

    @InjectView(R.id.store_pages_header)
    protected TabPageIndicator indicator;

    @InjectView(R.id.store_pages)
    protected ViewPager pager;

    private StorePagerAdapter adapter;

    public StoreFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_store, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ButterKnife.inject(this, getView());

        adapter = new StorePagerAdapter(getActivity(), getResources(), getChildFragmentManager());
        pager.setAdapter(adapter);
        indicator.setViewPager(pager);
        ViewGroup vg = (ViewGroup) indicator.getChildAt(0);
        int vgChildCount = vg.getChildCount();
        for (int j = 0; j < vgChildCount; j++) {
            View vgChild = vg.getChildAt(j);
            if (vgChild instanceof TextView) {
                Typeface font = Typeface.createFromAsset(getActivity().getAssets(),
                        "fonts/regular.otf");
                ((TextView) vgChild).setTypeface(font);
            }
        }
        pager.setCurrentItem(0);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void updateLocationMobis(EstablishmentLocation location) {
        if(pager != null) {
            int currentItem = pager.getCurrentItem();
            adapter = new StorePagerAdapter(getActivity(), getResources(),
                    getChildFragmentManager());
            adapter.updateLocationMobis(location);
            pager.setAdapter(adapter);
            pager.setCurrentItem(currentItem);
        }
    }
}

