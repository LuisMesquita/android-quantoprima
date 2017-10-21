package br.com.mobiclub.quantoprima.facade;

import android.content.Intent;

//import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Stack;

import javax.inject.Inject;

import br.com.mobiclub.quantoprima.config.Injector;
import br.com.mobiclub.quantoprima.core.service.api.entity.CardapioCategory;
import br.com.mobiclub.quantoprima.core.service.api.entity.CardapioItem;
import br.com.mobiclub.quantoprima.core.service.api.entity.Establishment;
import br.com.mobiclub.quantoprima.core.service.api.entity.EstablishmentLocation;
import br.com.mobiclub.quantoprima.core.service.api.entity.Localization;
import br.com.mobiclub.quantoprima.core.service.api.entity.Reward;
import br.com.mobiclub.quantoprima.core.service.api.entity.User;
import br.com.mobiclub.quantoprima.dao.MobiDAO;
import br.com.mobiclub.quantoprima.dao.UserDAO;
import br.com.mobiclub.quantoprima.database.table.MobiTable;
import br.com.mobiclub.quantoprima.domain.Mobi;
import br.com.mobiclub.quantoprima.domain.MobiClubUser;
//import br.com.mobiclub.quantoprima.service.map.PositionListener;
import br.com.mobiclub.quantoprima.ui.activity.HomeActivity;
import br.com.mobiclub.quantoprima.util.Ln;

/**
 *
 */
public class Facade {

    private static Facade instance;

    private MobiClubUser loggerUser = null;

    @Inject
    MobiDAO mobiDAO;

    @Inject
    UserDAO userDAO;

    private Gson gson;

    private Stack<Intent> parents = new Stack<Intent>();
//    private PositionListener positionListener;
    private boolean registeredListeners;
    private Intent nextIntent;
    private Establishment establishment;
    private EstablishmentLocation location;
    private Reward reward;
    private CardapioItem cardapioItem;
    private CardapioCategory category;

    private Facade() {
        Injector.inject(this);
    }

    public static Facade getInstance() {
        if(instance == null) {
            instance = new Facade();
        }
        return instance;
    }

    public void invalidateSession() {
        this.loggerUser = null;
    }

    public void setLoggedUser(MobiClubUser loggedMobiClubUser) {
        this.loggerUser = loggedMobiClubUser;
    }

    public MobiClubUser getLoggedUser() {
        if(!isUserLogged())
            return null;
        return loggerUser;
    }

    public void insertOrUpdateMobi(Mobi mobi) {
        MobiClubUser loggedUser = getLoggedUser();
        if(loggedUser != null)
            mobiDAO.save(mobi, loggedUser.getUserId());
    }

    public boolean existsMobisOffline() {
        List<Mobi> mobis = getMobisToSync();
        Ln.d("Existe %s mobis offline.", mobis.size());
        return !mobis.isEmpty();
    }

    /**
     * Recupera os mobis nao sincronizados ainda ou com error
     *
     * @return
     */
    public List<Mobi> getMobisToSync() {
        List<Mobi> mobis = new ArrayList<Mobi>();
        MobiClubUser loggedUser = getLoggedUser();
        if (loggedUser != null && mobiDAO != null) {
            mobis = mobiDAO.getMobisToSyncByUser(loggedUser.getUserId());
        } else {
            Ln.e("Nao foi possivel obter mobistosync. mobiDAO == null ou nenhum usuario logado.");
        }
        return mobis;
    }

    public List<Mobi> getAllMobis() {
        List<Mobi> result = new ArrayList<Mobi>();
        MobiClubUser loggedUser = getLoggedUser();
        if (loggedUser != null) {
            List<Mobi> mobis = mobiDAO.getAllMobisByUser(loggedUser.getUserId());
            for (int i = 0; i < mobis.size(); i++) {
                Mobi mobi = mobis.get(i);
                if((mobi.getLogo() != null &&
                        mobi.getLocationName() != null) ||
                        mobi.getStatus() == MobiTable.STATUS_SYNCED_ERROR_TYPE_EXPIRED) {
                    result.add(mobi);
                }
            }
        }
        return result;
    }

    public void seed() {
        mobiDAO.seed(1);
    }

    public void deleteAllMobis() {
        mobiDAO.deleteAllmobis(1);
    }

    public void createUser(MobiClubUser user) {
        userDAO.insert(user);
    }

    public void createFacebookUser(MobiClubUser mobiUser) {
        deleteUserLocal();
        createUser(mobiUser);
    }

    public MobiClubUser createMobiClubUser(User user) {
        MobiClubUser mobiUser = new MobiClubUser(user.getId());
        Date birth = user.getBirth();
        mobiUser.setName(user.getName());
        mobiUser.setLastName(user.getLastName());
        mobiUser.setBirthday(birth);
        mobiUser.setEmail(user.getEmail());
        mobiUser.setCpf(user.getCpf());
        mobiUser.setFacebookAccessExpires(user.getFacebookAccessExpires());
        mobiUser.setFacebookAccessToken(user.getFacebookAccessToken());
        mobiUser.setFacebookId(user.getFacebookId());
        mobiUser.setGenderType(user.getGender());
        return mobiUser;
    }

    public void deleteUserLocal() {
        userDAO.delete();
    }

    public MobiClubUser getUser(String email) {
        return userDAO.findByEmail(email);
    }

    public MobiClubUser getUserByToken(String token) {
        return userDAO.findByToken(token);
    }

    public Gson getGSON() {
        if(gson == null) {
            gson = new Gson();
        }
        return gson;
    }

    public void insertOrUpdateUser(MobiClubUser user) {
        userDAO.insert(user);
    }

    public void newIntent(Intent intent) {
        //if(parents.isEmpty()) {
        parents.push(intent);
        //} else if(!parents.peek().getComponent().getClassName().equals(intent.getComponent().getClassName())) {
        //    parents.push(intent);
        //}
        Ln.d("Estado dos parents:");
        for (int i = 0; i < parents.size(); i++) {
            Ln.d("    %d - %s", i, parents.get(i).getComponent().getClassName());
        }
    }

    public boolean isParent(Class<HomeActivity> homeActivity) {
        if(parents.isEmpty()) {
            return false;
        }
        return parents.peek().getComponent().getClassName().equals(homeActivity.getName());
    }

    public Intent getParent() {
        if(parents.isEmpty()) {
            return null;
        }
        parents.pop();
        if(parents.isEmpty()) {
            return null;
        }
        return parents.pop();
    }

    public void clearParents() {
        if(!parents.isEmpty())
            parents.clear();
    }

    public void removeLastIntent() {
        if(!parents.isEmpty())
            parents.pop();
    }

    public boolean isUserLogged() {
        return loggerUser != null || (loggerUser != null &&
                loggerUser.getUserId() != null);
    }

//    public void updateUserPosition(LatLng locationLatlng) {
//        if(isUserLogged()) {
//            MobiClubUser loggedUser = getLoggedUser();
//            LocalizationBuilder builder = new LocalizationBuilder();
//            Localization localization = builder.setLatitude(locationLatlng.latitude)
//                    .setLongitude(locationLatlng.longitude)
//                    .createLocalization();
//            Ln.d("Atualizando posição do usuario com lat=%f long=%f", localization.getLatitude(),
//                    localization.getLatitude());
//            loggedUser.setLocalization(localization);
//        }
//    }

    public Localization getUserLocation() {
        if(isUserLogged()) {
            return getLoggedUser().getLocalization();
        }
        return null;
    }

//    public void createPositionListener(LaunchActivity activity) {
//        this.positionListener = PositionListener.createPositionListener(activity);
//    }
//
//    public PositionListener getPositionListener() {
//        return this.positionListener;
//    }

    public boolean isRegisteredListeners() {
        return registeredListeners;
    }

    public void setRegisteredListeners(boolean registeredListeners) {
        this.registeredListeners = registeredListeners;
    }

    public boolean canBack() {
        if(parents.size() >= 1)
            return true;
        return false;
    }

    public Intent getNextIntent() {
        if(!parents.isEmpty())
            return parents.peek();
        return null;
    }

    public void setNextIntent(Intent nextIntent) {
        this.nextIntent = nextIntent;
    }

    public Establishment getEstablishment() {
        return establishment;
    }

    public void setEstablishment(Establishment establishment) {
        this.establishment = establishment;
    }

    public void setLocation(EstablishmentLocation location) {
        this.location = location;
    }

    public EstablishmentLocation getLocation() {
        return location;
    }

    public void setReward(Reward reward) {
        this.reward = reward;
    }

    public Reward getReward() {
        return reward;
    }

    public CardapioItem getCardapioItem() {
        return cardapioItem;
    }

    public void setCardapioItem(CardapioItem cardapioItem) {
        this.cardapioItem = cardapioItem;
    }

    public void setCategory(CardapioCategory category) {
        this.category = category;
    }

    public CardapioCategory getCategory() {
        return category;
    }
}
