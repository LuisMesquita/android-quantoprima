package br.com.mobiclub.quantoprima.ui.activity.store;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.squareup.otto.Subscribe;

import br.com.mobiclub.quantoprima.R;
import br.com.mobiclub.quantoprima.core.service.api.entity.CardapioItem;
import br.com.mobiclub.quantoprima.core.service.api.entity.EstablishmentLocation;
import br.com.mobiclub.quantoprima.domain.MobiClubUser;
import br.com.mobiclub.quantoprima.events.NavItemSelectedEvent;
import br.com.mobiclub.quantoprima.facade.Facade;
import br.com.mobiclub.quantoprima.facebook.FacebookFacade;
import br.com.mobiclub.quantoprima.ui.activity.MenuActivity;
import br.com.mobiclub.quantoprima.ui.factory.AlertDialogFactory;
import br.com.mobiclub.quantoprima.ui.fragment.store.CardapioItemFragment;
import br.com.mobiclub.quantoprima.util.Ln;

public class CardapioItemActivity extends MenuActivity {

    private CardapioItem cardapioItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected Fragment createFragment() {
        EstablishmentLocation location = Facade.getInstance().getLocation();
        cardapioItem = Facade.getInstance().getCardapioItem();
        cardapioItem.setLocation(location);
        CardapioItemFragment cardapioitemFragment = new CardapioItemFragment();
        return cardapioitemFragment;
    }

    /**
     * Aqui por enquanto pois o otto nao consegue enchergar
     * a anotacao de subscribe em PrincipalActivity
     *
     * @param event
     */
    @Subscribe
    @Override
    public void onNavigationItemSelected(NavItemSelectedEvent event) {
        super.onNavigationItemSelectedImpl(event);
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        if(item.getItemId() == R.id.share_action) {
//            Intent intent = getIntent();
//            CardapioItem cardapioItem = (CardapioItem) intent.getSerializableExtra(Constants.Extra.CARDAPIO_ITEM);
//            super.facebookShare(cardapioItem, FacebookFacade.FacebookPostType.STORE_ITEM.getId());
//        }
//        return super.onOptionsItemSelected(item);
//    }

    public void onShare(View view) {
        Ln.d("onShare");
        MobiClubUser loggedUser = Facade.getInstance().getLoggedUser();
        if(!loggedUser.isConnectedToFacebook()) {
            AlertDialog defaultError = AlertDialogFactory.createDefaultError(this,
                    R.string.alert_title_attention,
                    R.string.facebook_connect_message);
            defaultError.show();
            return;
        }
        CardapioItem cardapioItem = Facade.getInstance().getCardapioItem();
        super.facebookShare(cardapioItem, FacebookFacade.FacebookPostType.STORE_ITEM.getId());
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.share, menu);
//        boolean b = super.onCreateOptionsMenu(menu);
//        menu.getItem(1).setVisible(false);
//        return b;
//    }

}
