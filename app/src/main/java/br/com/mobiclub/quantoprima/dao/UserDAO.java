package br.com.mobiclub.quantoprima.dao;

import android.content.ContentValues;

import br.com.mobiclub.quantoprima.database.MobiClubDatabase;
import br.com.mobiclub.quantoprima.database.table.UserTable;
import br.com.mobiclub.quantoprima.domain.MobiClubUser;
import br.com.mobiclub.quantoprima.util.Ln;

public class UserDAO {

    private MobiClubDatabase db;

    public UserDAO() {
        this.db = MobiClubDatabase.getInstance();
    }

    public synchronized void insert(MobiClubUser mobiClubUser) {
        if (mobiClubUser != null) {
            ContentValues values;
            if(mobiClubUser.getId() == null) {
                values = createValuesFromUser(mobiClubUser);
                this.db.insertUser(values);
            } else {
                values = createValuesFromUser(mobiClubUser);
                this.db.updateUser(mobiClubUser.getId(), values);
            }
            Ln.d("Criou/Atualizou usuario: %s", mobiClubUser);
        }
    }

    private ContentValues createValuesFromUser(MobiClubUser mobiClubUser) {
        ContentValues values = new ContentValues();
        values.put(UserTable.ID_COL, mobiClubUser.getId());
        values.put(UserTable.USER_ID_COL, mobiClubUser.getUserId());
        values.put(UserTable.NAME_COL, mobiClubUser.getName());
        values.put(UserTable.LAST_NAME_COL, mobiClubUser.getLastName());
        values.put(UserTable.EMAIL_COL, mobiClubUser.getEmail());
        values.put(UserTable.BIHTHDAY_COL, mobiClubUser.getBirthdayString());
        values.put(UserTable.GENDER_COL, mobiClubUser.getGenderType());
        values.put(UserTable.FACEBOOK_ID_COL, mobiClubUser.getFacebookId());
        values.put(UserTable.FACEBOOK_ACCESS_EXPIRES_COL,
                mobiClubUser.getFacebookAccessExpires());
        values.put(UserTable.FACEBOOK_ACCESS_TOKEN_COL,
                mobiClubUser.getFacebookAccessToken());
        values.put(UserTable.PHOTO_COL,
                mobiClubUser.getProfilePicture());
        values.put(UserTable.FACEBOOK_EMAIL_COL,
                mobiClubUser.getFacebookEmail());
        values.put(UserTable.CPF_COL, mobiClubUser.getCpf());
        return values;
    }

    public synchronized void delete() {
        if (db != null) {
            db.deleteUser();
        }
    }

    public synchronized MobiClubUser selectByUserId(String userId) {
        if(db == null)
            return null;
        return db.selectByUserId(userId);
    }

    public MobiClubUser findByEmail(String email) {
        if(db != null) {
            return db.findUserByEmail(email);
        }
        return null;
    }

    public MobiClubUser findByToken(String token) {
        if(db != null) {
            return db.findUserByToken(token);
        }
        return null;
    }
}
