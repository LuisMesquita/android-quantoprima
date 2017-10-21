package br.com.mobiclub.quantoprima.ui.fragment.store;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.Loader;
import android.view.View;
import android.widget.ListView;

import com.github.kevinsawicki.wishlist.TypeAdapter;

import java.util.Collections;
import java.util.List;

import br.com.mobiclub.quantoprima.R;
import br.com.mobiclub.quantoprima.core.service.api.entity.CardapioCategory;
import br.com.mobiclub.quantoprima.core.service.api.entity.CardapioItem;
import br.com.mobiclub.quantoprima.core.service.api.entity.EstablishmentLocation;
import br.com.mobiclub.quantoprima.facade.Facade;
import br.com.mobiclub.quantoprima.service.authenticator.LogoutService;
import br.com.mobiclub.quantoprima.ui.activity.store.CardapioItemActivity;
import br.com.mobiclub.quantoprima.ui.adapter.store.CardapioListAdapter;
import br.com.mobiclub.quantoprima.ui.fragment.ItemListFragment;
import br.com.mobiclub.quantoprima.ui.view.ThrowableLoader;
import br.com.mobiclub.quantoprima.domain.Constants;
import br.com.mobiclub.quantoprima.util.Ln;

/**
 * A simple {@link Fragment} subclass.
 *
 */
public class CardapioListFragment extends ItemListFragment<CardapioItem> {

    private CardapioCategory category;
    private EstablishmentLocation location;

    public CardapioListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle arguments = getArguments();
        if(arguments != null) {
            category = (CardapioCategory) arguments.getSerializable(Constants.Extra.CARDAPIO_CATEGORY);
        }

        location = Facade.getInstance().getLocation();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getLoaderManager().initLoader(Constants.Loader.CARDAPIO_LIST, null, this);
    }

    @Override
    protected LogoutService getLogoutService() {
        return null;
    }

    @Override
    protected int getErrorMessage(Exception exception) {
        return R.string.cardapio_cardapio_no_cardapio_item;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Ln.d("Item clicked: " + items.get(position).getName());
        FragmentActivity activity = getActivity();
        if(isAdded()) {
            Intent intent = new Intent(activity, CardapioItemActivity.class);

            Facade.getInstance().setLocation(location);
            Facade.getInstance().setCardapioItem(items.get(position));

            startActivity(intent);
        }
    }

    @Override
    public Loader<List<CardapioItem>> onCreateLoader(int id, Bundle args) {
        final List<CardapioItem> initialItems = items;
        return new ThrowableLoader<List<CardapioItem>>(getActivity(), items) {
            @Override
            public List<CardapioItem> loadData() throws Exception {
                try {
                    List<CardapioItem> latest = null;

                    if (getActivity() != null && category != null) {
                        latest = category.getItems();
                    }
                    if (latest != null) {
                        return latest;
                    } else {
                        return Collections.emptyList();
                    }
                } catch (final Exception e) {
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
    protected TypeAdapter createAdapter(List<CardapioItem> items) {
        return new CardapioListAdapter(getActivity().getLayoutInflater(),
                getResources(), items);
    }

}
