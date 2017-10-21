package br.com.mobiclub.quantoprima.util;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.support.v7.internal.view.menu.MenuBuilder;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import br.com.mobiclub.quantoprima.R;
import br.com.mobiclub.quantoprima.core.service.api.entity.Establishment;
import br.com.mobiclub.quantoprima.core.service.api.entity.EstablishmentLocation;
import br.com.mobiclub.quantoprima.domain.MobiClubUser;
import br.com.mobiclub.quantoprima.facade.Facade;

public class UIUtils {

    /**
     * Helps determine if the app is running in a Tablet context.
     *
     * @param context
     * @return
     */
    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    public static String getColoredMessage(String string, Activity activity, int message) {
        String userWelcome;
        if (string != null && !string.isEmpty()) {
            int color = activity.getResources().getColor(R.color.outdated_user_color);
            String userNameColor = String.format("#%06X", (0xFFFFFF & color));
            String userColored = String.format("<font color='%s'>%s</font>", userNameColor,
                    string);
            userWelcome = activity.getResources().getString(message, " " + userColored);
        } else {
            userWelcome = activity.getResources().getString(message, "");
        }
        return userWelcome;
    }

    public static String generateQrCode() {
        return "mobiclub" + "ADdvgtdSJHSdkljdlkdkslKJçsklSSJD";
    }

    public static String generateQrCodeShot() {
        return "mobiclubshot" + "gtdSJHSdkljdlkdkslKJçsklSSJD";
    }

    public static List<Establishment> createLocationsFromEstablishment(Establishment establishment) {
        List<Establishment> establishments = new ArrayList<Establishment>();
        List<EstablishmentLocation> locations = establishment.getLocations();

        //cria estabelecimentos para cada location do estabelecimento
        for (int i = 0; i < locations.size(); i++) {
            EstablishmentLocation el = locations.get(i);

            Establishment e = new Establishment();
            e.setId(establishment.getId());
            e.setLogoUrl(establishment.getLogoUrl());
            e.setName(establishment.getName());

            List<EstablishmentLocation> newLocations = new ArrayList<EstablishmentLocation>();
            newLocations.add(el);
            e.setLocations(newLocations);

            establishments.add(e);
        }
        return establishments;
    }

    public static List<MenuItem> createAppMenu(Activity activity, int menuId) {
        List<MenuItem> menuItems = createMenu(activity, menuId);
        MobiClubUser loggedUser = Facade.getInstance().getLoggedUser();
        if (loggedUser != null) {
            String fullName = loggedUser.getFullName();
            if (fullName != null) {
                menuItems.get(0).setTitle(fullName);
            }
        } else {
            Ln.e("Error: nenhum usuario logado no momento de construir o menu.");
        }
        return menuItems;
    }

    public static List<MenuItem> createMenu(Activity activity, int menuId) {
        Menu menu = new MenuBuilder(activity);
        MenuInflater menuInflater = activity.getMenuInflater();
        menuInflater.inflate(menuId, menu);
        List<MenuItem> menuItems = new ArrayList<MenuItem>();
        for (int i = 0; i < menu.size(); i++) {
            menuItems.add(menu.getItem(i));
        }
        return menuItems;
    }

}
