package br.com.mobiclub.quantoprima.ui.activity.store;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.PopupMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListPopupWindow;
import android.widget.TextView;

import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

import br.com.mobiclub.quantoprima.MobiClubApplication;
import br.com.mobiclub.quantoprima.R;
import br.com.mobiclub.quantoprima.domain.Constants;
import br.com.mobiclub.quantoprima.domain.Language;
import br.com.mobiclub.quantoprima.events.NavItemSelectedEvent;
import br.com.mobiclub.quantoprima.ui.activity.MenuActivity;
import br.com.mobiclub.quantoprima.ui.factory.AlertDialogFactory;
import br.com.mobiclub.quantoprima.ui.fragment.store.CardapioFragment;
import br.com.mobiclub.quantoprima.ui.fragment.store.LanguageWrapper;
import br.com.mobiclub.quantoprima.util.Ln;
import br.com.mobiclub.quantoprima.util.UIUtils;

public class CardapioActivity extends MenuActivity
        implements PopupMenu.OnMenuItemClickListener,
        AdapterView.OnItemClickListener {

    private MenuItem menuItemLang;
    private ListPopupWindowWrapper listPopupWindowWrapper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected Fragment createFragment() {
        CardapioFragment cardapioFragment = new CardapioFragment();
        return cardapioFragment;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean b = super.onCreateOptionsMenu(menu);
        menu.getItem(0).setVisible(false);
        getMenuInflater().inflate(R.menu.cardapio_languages, menu);
        menuItemLang = menu.findItem(R.id.menu_new_form);

        final int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        MenuItemCompat.setActionView(menuItemLang, R.layout.action_layout_menu_lang);
        View actionView = MenuItemCompat.getActionView(menuItemLang);
        TextView text = (TextView) actionView.findViewById(R.id.menu_label_lang);
        String languageString = MobiClubApplication.getInstance().getLanguageString();
        text.setText(languageString);

        if (currentapiVersion >= Build.VERSION_CODES.HONEYCOMB) {
            listPopupWindowWrapper = new ListPopupWindowWrapper(this);

            List<MenuItem> menuItems = UIUtils.createMenu(this, R.menu.langs);

            createMenu(menuItems);

            final ListPopupWindow listPopupWindow = listPopupWindowWrapper.getWrapper();
            listPopupWindow.setAdapter(new ArrayAdapter(this,
                    R.layout.list_item_menu_lang, menuItems));
            listPopupWindow.setAnchorView(actionView);
            listPopupWindow.setWidth(android.widget.ListPopupWindow.MATCH_PARENT);
            listPopupWindow.setHeight(android.widget.ListPopupWindow.WRAP_CONTENT);

            listPopupWindow.setModal(true);
            listPopupWindow.setOnItemClickListener(this);

            actionView.setClickable(true);
            actionView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listPopupWindow.show();
                }
            });
        } else {
            actionView.setClickable(true);
            actionView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showPopup(v);
                }
            });
        }

        return b;
    }

    private void createMenu(List<MenuItem> menuItems) {
        try {
            LanguageWrapper lw = (LanguageWrapper) getIntent().getSerializableExtra(Constants.Extra.CARDAPIO_LANGUAGES);
            Language[] langs = lw.getLanguages();
            List<MenuItem> toRemove = new ArrayList<>();
            for (int j = 0; j < menuItems.size(); j++) {
                MenuItem menuItem = menuItems.get(j);
                String title = (String) menuItem.getTitle();
                boolean exists = false;
                for (int i = 0; i < langs.length; i++) {
                    Language lang = langs[i];
                    MenuItem mi = menuItems.get(i);
                    Intent intent = new Intent();
                    intent.putExtra(Constants.Extra.LANG_TITLE, lang.getLanguageShort());
                    mi.setIntent(intent);
                    char c = Character.forDigit(lang.getId().intValue(), 10);
                    mi.setNumericShortcut(c);
                    if(title.equalsIgnoreCase(lang.getLanguage())) {
                        exists = true;
                    }
                }
                if(!exists) {
                    toRemove.add(menuItem);
                }
            }
            for (int i = 0; i < toRemove.size(); i++) {
                menuItems.remove(toRemove.get(i));
            }
        } catch (Exception e) {
            Ln.e(e);
        }
    }

    public void showPopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        MenuInflater inflater = popup.getMenuInflater();
        Menu menu = popup.getMenu();
        inflater.inflate(R.menu.langs, menu);

        createMenu(menu);

        popup.setOnMenuItemClickListener(this);
        popup.show();
    }

    private void createMenu(Menu menu) {
        try {
            Language[] langs = (Language[]) getIntent().getSerializableExtra(Constants.Extra.CARDAPIO_LANGUAGES);
            List<MenuItem> toRemove = new ArrayList<>();
            for (int j = 0; j < menu.size(); j++) {
                MenuItem item = menu.getItem(j);
                String title = (String) item.getTitle();
                boolean exists = false;
                for (int i = 0; i < langs.length; i++) {
                    Language lang = langs[i];
                    MenuItem mi = menu.getItem(i);
                    Intent intent = new Intent();
                    intent.putExtra(Constants.Extra.LANG_TITLE, lang.getLanguageShort());
                    mi.setIntent(intent);
                    char c = Character.forDigit(lang.getId().intValue(), 10);
                    mi.setNumericShortcut(c);
                    if(title.equalsIgnoreCase(lang.getLanguage())) {
                        exists = true;
                    }
                }
                if(!exists) {
                    toRemove.add(item);
                }
            }
            for (int i = 0; i < toRemove.size(); i++) {
                menu.removeItem(toRemove.get(i).getItemId());
            }
        } catch (Exception e) {
            Ln.e(e);
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        boolean b = onOptionsItemSelected(menuItem);
        return b;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean b = false;
        try {
            String s = String.valueOf(item.getNumericShortcut());

            int id = Integer.parseInt(s, 10);

            b = changeLanguage(id);

            //return b;

            View actionView = MenuItemCompat.getActionView(menuItemLang);

            TextView text = (TextView) actionView.findViewById(R.id.menu_label_lang);
            Intent intent = item.getIntent();
            if(intent != null) {
                String languageString = intent.getStringExtra(Constants.Extra.LANG_TITLE);
                MobiClubApplication.getInstance().setLanguageString(languageString);
                text.setText(languageString);
            } else {
                text.setText("");
            }
        } catch (NumberFormatException e) {
            Ln.d(e);
        }

        return super.onOptionsItemSelected(item);
    }

    private boolean changeLanguage(int id) {
        FragmentManager fm = getSupportFragmentManager();
        Fragment f = fm.findFragmentById(R.id.container);
        if(f == null) {
            AlertDialogFactory.createDefaultError(this, R.string.alert_change_lang_error_message);
            return false;
        }
        if(f instanceof CardapioFragment) {
            CardapioFragment cf = (CardapioFragment) f;
            cf.changeLanguage(id);
        }
        return true;
    }

    @Subscribe
    @Override
    public void onNavigationItemSelected(NavItemSelectedEvent event) {
        super.onNavigationItemSelectedImpl(event);
    }

    @SuppressLint("NewApi")
    @Override
    public void onItemClick(AdapterView parent, View view, int position, long id) {
        MenuItem item = (MenuItem) parent.getAdapter().getItem(position);
        onOptionsItemSelected(item);
        listPopupWindowWrapper.getWrapper().dismiss();
    }

}