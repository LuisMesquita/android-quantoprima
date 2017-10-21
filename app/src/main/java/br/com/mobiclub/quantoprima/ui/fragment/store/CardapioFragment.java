package br.com.mobiclub.quantoprima.ui.fragment.store;


import android.accounts.OperationCanceledException;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import javax.inject.Inject;

import br.com.mobiclub.quantoprima.MobiClubApplication;
import br.com.mobiclub.quantoprima.R;
import br.com.mobiclub.quantoprima.config.Injector;
import br.com.mobiclub.quantoprima.core.service.MobiClubServiceApi;
import br.com.mobiclub.quantoprima.core.service.MobiClubServiceProvider;
import br.com.mobiclub.quantoprima.core.service.api.entity.Cardapio;
import br.com.mobiclub.quantoprima.core.service.api.entity.CardapioCategory;
import br.com.mobiclub.quantoprima.core.service.api.entity.EstablishmentLocation;
import br.com.mobiclub.quantoprima.core.service.api.exception.AppBlockedException;
import br.com.mobiclub.quantoprima.core.service.api.exception.AppOutdatedException;
import br.com.mobiclub.quantoprima.core.service.api.exception.UserBlockedException;
import br.com.mobiclub.quantoprima.facade.Facade;
import br.com.mobiclub.quantoprima.ui.activity.AppInactiveActivity;
import br.com.mobiclub.quantoprima.ui.activity.account.AccountBlockedActivity;
import br.com.mobiclub.quantoprima.ui.activity.launch.OutdatedActivity;
import br.com.mobiclub.quantoprima.ui.view.ThrowableLoader;
import br.com.mobiclub.quantoprima.domain.Constants;
import br.com.mobiclub.quantoprima.util.Ln;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 *
 */
public class CardapioFragment extends Fragment
        implements ViewPager.OnPageChangeListener,
        LoaderManager.LoaderCallbacks<Cardapio> {

    @InjectView(R.id.label_category)
    TextView labelCategory;

    @InjectView(R.id.nav_bar)
    RelativeLayout navBar;

    @InjectView(R.id.pager)
    ViewPager pager;

    @Inject
    MobiClubServiceProvider serviceProvider;

    @InjectView(R.id.btn_prev)
    ImageButton prev;

    @InjectView(R.id.btn_next)
    ImageButton next;

    private EstablishmentLocation location;

    private Cardapio cardapio;
    private CardapioPagerAdapter adapter;
    private ProgressBar progressBar;
    private TextView emptyView;
    private int currentItem;

    public CardapioFragment() {
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Injector.inject(this);

        location = Facade.getInstance().getLocation();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getLoaderManager().initLoader(Constants.Loader.CARDAPIO, null, this);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        progressBar = (ProgressBar) view.findViewById(R.id.pb_loading);
        final AnimationDrawable anim = (AnimationDrawable) progressBar.getBackground();
        if (anim != null) anim.start();

        emptyView = (TextView) view.findViewById(android.R.id.empty);
        emptyView.setVisibility(View.GONE);

        //hidenavbar
        navBar.setVisibility(View.GONE);
    }

    @Override
    public Loader<Cardapio> onCreateLoader(int id, Bundle args) {
        final Cardapio initialItems = cardapio;
        return new ThrowableLoader<Cardapio>(getActivity(), cardapio) {
            @Override
            public Cardapio loadData() throws Exception {
                try {
                    Cardapio latest = null;

                    if (getActivity() != null && location != null) {
                        MobiClubServiceApi service = serviceProvider.getService(getActivity());
                        int language = MobiClubApplication.getInstance().getLanguage();
                        Ln.d("Pegando cardapio para %s", language);
                        latest = service.getCardapioByLocation(location.getId());
                    }

                    if (latest != null) {
                        return latest;
                    } else {
                        return null;
                    }
                } catch (final OperationCanceledException e) {
                    final Activity activity = getActivity();
                    if (activity != null) {
                        activity.finish();
                    }
                    return initialItems;
                }
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<Cardapio> loader, Cardapio cardapio) {
        getActionBarActivity().setSupportProgressBarIndeterminateVisibility(false);

        progressBar.setVisibility(View.GONE);

        final Exception exception = getException(loader);
        if (exception != null) {
            showError(getErrorMessage(exception));
            Intent intent = null;
            if(exception instanceof AppBlockedException) {
                Ln.d("AppBlockedException");
                intent = new Intent(getActivity(), AppInactiveActivity.class);
            } else if(exception instanceof AppOutdatedException) {
                Ln.d("AppOutdatedException");
                intent = new Intent(getActivity(), OutdatedActivity.class);
            } else if(exception instanceof UserBlockedException) {
                Ln.d("UserBlockedException");
                intent = new Intent(getActivity(), AccountBlockedActivity.class);
                intent.putExtra(Constants.Extra.REASON_USER_BLOCKED, exception.getMessage());
            }
            if(intent != null) {
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                getActivity().finish();
            }
            return;
        }

        if(cardapio != null && cardapio.getHttpStatus() != 200) {
            showError(R.string.server_connection_error);
            return;
        }

        this.cardapio = cardapio;
        showCardapio(cardapio);
    }

    private void showCardapio(Cardapio cardapio) {
        List<CardapioCategory> categories = cardapio.getCategories();
        if(categories.isEmpty()) {
            pager.setAdapter(null);
            navBar.setVisibility(View.GONE);
            showError(R.string.cardapio_cardapio_no_cardapio);
            return;
        }

        setEmptyText("");

        FragmentManager fm = getFragmentManager();
        adapter = new CardapioPagerAdapter(fm, categories);
        pager.setAdapter(adapter);

        if(currentItem < 0 || currentItem > adapter.getCount())
            currentItem = 0;
        pager.setCurrentItem(currentItem);

        CardapioCategory category = categories.get(0);
        if(category != null && category.getName() != null) {
            labelCategory.setText(category.getName());
        }

        showNavBar(pager.getCurrentItem());
    }

    private void showNavBar(int current) {
        navBar.setVisibility(View.VISIBLE);
        if(current > 0 && current < adapter.getCount() - 1) {
            prev.setEnabled(true);
            next.setEnabled(true);
        }
        if(current == 0) {
            next.setEnabled(true);
            prev.setEnabled(false);
        }
        if(current == adapter.getCount() - 1) {
            prev.setEnabled(true);
            next.setEnabled(false);
        }
        this.currentItem = current;
    }

    private int getErrorMessage(Exception exception) {
        return R.string.server_connection_error;
    }

    /**
     * Show exception in a Toast
     *
     * @param message
     */
    protected void showError(final int message) {
        setEmptyText(getString(message));
    }

    /**
     * Set empty text on list fragment
     *
     * @param message
     * @return this fragment
     */
    protected void setEmptyText(final String message) {
        if (emptyView != null) {
            emptyView.setVisibility(View.VISIBLE);
            emptyView.setText(message);
        }
    }

    private ActionBarActivity getActionBarActivity() {
        return (ActionBarActivity) getActivity();
    }

    protected Exception getException(final Loader<Cardapio> loader) {
        if (loader instanceof ThrowableLoader) {
            return ((ThrowableLoader<Cardapio>) loader).clearException();
        } else {
            return null;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cardapio> loader) {

    }

    @Override
    public void onPageSelected(int position) {
        CardapioCategory category = adapter.getCategory(position);
        labelCategory.setText(category.getName());
        showNavBar(position);
    }

    @OnClick(R.id.btn_next)
    public void onNext() {
        Ln.d("onNext");
        if(pager.getCurrentItem() < adapter.getCount() - 1)
            pager.setCurrentItem(pager.getCurrentItem() + 1);
        showNavBar(pager.getCurrentItem());
    }

    @OnClick(R.id.btn_prev)
    public void onPrev() {
        Ln.d("onPrev");
        if(pager.getCurrentItem() > 0)
            pager.setCurrentItem(pager.getCurrentItem() - 1);
        showNavBar(pager.getCurrentItem());
    }

    public void changeLanguage(int id) {
        MobiClubApplication.getInstance().setLanguage(id);
        getLoaderManager().restartLoader(Constants.Loader.CARDAPIO, null, this);
    }

    /**
     */
    private class CardapioPagerAdapter extends FragmentStatePagerAdapter {

        private List<CardapioCategory> categories;

        public CardapioPagerAdapter(FragmentManager fm, List<CardapioCategory> categories) {
            super(fm);
            this.categories = categories;
        }

        @Override
        public Fragment getItem(int position) {
            CardapioListFragment cardapioListFragment = new CardapioListFragment();
            Bundle args = new Bundle();
            args.putSerializable(Constants.Extra.CARDAPIO_CATEGORY, categories.get(position));
            cardapioListFragment.setArguments(args);
            return cardapioListFragment;
        }

        @Override
        public int getCount() {
            return categories.size();
        }

        public CardapioCategory getCategory(int position) {
            return categories.get(position);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cadapio, container, false);

        ButterKnife.inject(this, view);

        pager.setOnPageChangeListener(this);

        prev.setEnabled(false);

        return view;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        //faz nada
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        //faz nada
    }

}
