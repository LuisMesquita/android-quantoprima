package br.com.mobiclub.quantoprima.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import br.com.mobiclub.quantoprima.database.table.MobiTable;
import br.com.mobiclub.quantoprima.database.table.UserTable;
import br.com.mobiclub.quantoprima.domain.Mobi;
import br.com.mobiclub.quantoprima.domain.MobiClubUser;
import br.com.mobiclub.quantoprima.util.Ln;
import br.com.mobiclub.quantoprima.util.Util;

public class MobiClubDatabase extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "mobiclub.db";
    private static final int DATABASE_VERSION = 1;

    //Tables
    private UserTable userTable;
    private MobiTable mobiTable;

    private static MobiClubDatabase instance;

    private MobiClubDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

        this.userTable = new UserTable();
        this.mobiTable = new MobiTable();

    }

    public static void createDatabase(Context context) {
        instance = new MobiClubDatabase(context);
    }

    public static MobiClubDatabase getInstance() {
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(userTable.getCreateCommandString());
        db.execSQL(mobiTable.getCreateCommandString());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(userTable.getDropCommandString());
        db.execSQL(mobiTable.getDropCommandString());
        onCreate(db);
    }

    public synchronized void insertUser(ContentValues values) {
        SQLiteDatabase db = null;
        try {
            db = getWritableDatabase();
            db.insert(this.userTable.getName(), null, values);
        } catch (Exception e) {
            Ln.e(e);
        } finally {
            if(db != null && db.isOpen())
                db.close();
        }
    }

    public synchronized void deleteUser() {
        SQLiteDatabase db = null;
        try {
            db = getWritableDatabase();
            //remove all users
            db.delete(this.userTable.getName(), "1", null);
        } catch (Exception e) {
            Ln.e(e);
        } finally {
            if(db != null && db.isOpen())
                db.close();
        }
    }

    public synchronized void insertMobi(ContentValues values) {
        SQLiteDatabase db = null;
        try {
            db = getWritableDatabase();

            db.insert(this.mobiTable.getName(), null, values);
        } catch (Exception e) {
            Ln.e(e);
        } finally {
            if(db != null && db.isOpen())
                db.close();
        }
    }

    public synchronized List<Mobi> selectMobisToSync(int userId) {
        return selectAllMobisCondition(userId, true);
    }

    private synchronized List<Mobi> selectAllMobisCondition(Integer userId,
                                                            boolean condition) {
        SQLiteDatabase db = null;
        try {
            if(userId == null)
                return Collections.emptyList();
            ArrayList<Mobi> returnValue = new ArrayList<Mobi>();

            db = getWritableDatabase();
            Cursor cursor = db.query(this.mobiTable.getName(),
                    mobiTable.getColumnsName(), MobiTable.USER_ID_COL + " = " + userId,
                    null, null, null, null);

            Mobi mobiTemp = null;
            while (cursor.moveToNext()) {
                mobiTemp = new Mobi();

                int id = cursor.getInt(MobiTable.ID_COL_INDEX);
                String code = cursor.getString(MobiTable.CODE_COL_INDEX);
                int timeMinute = cursor.getInt(MobiTable.TIME_MINUTE_COL_INDEX);
                int status = cursor.getInt(MobiTable.STATUS_COL_INDEX);
                String locationName = cursor.getString(MobiTable.LOCATION_COL_INDEX);
                String logo = cursor.getString(MobiTable.LOGO_COL_INDEX);
                String timeUpdateString = cursor.getString(MobiTable.TIME_UPDATE_COL_INDEX);

                mobiTemp.setId(id);
                mobiTemp.setCode(code);
                mobiTemp.setTimeMinute(timeMinute);
                mobiTemp.setStatus(status);
                mobiTemp.setUserId(userId);

                mobiTemp.setLocationName(locationName);
                mobiTemp.setLogo(logo);

                Date timeUpdate = Util.getDate(timeUpdateString);
                mobiTemp.setTimeUpdate(timeUpdate);

                //melhorar isso depois
                if(condition) {
                    if(status == MobiTable.STATUS_SYNCED_ERROR_TYPE ||
                            status == MobiTable.STATUS_NOT_SYNC_TYPE ||
                            status == MobiTable.STATUS_SYNCED_ERROR_TYPE_NO_CONNECTION)
                        returnValue.add(mobiTemp);
                }
                else {
                    returnValue.add(mobiTemp);
                }
            }

            cursor.close();

            return returnValue;
        } catch (Exception e) {
            Ln.e(e);
        } finally {
            if(db != null && db.isOpen())
                db.close();
        }
        return Collections.emptyList();
    }

    public void updateUser(int userId, ContentValues values) {
        SQLiteDatabase db = null;
        try {
            db = getWritableDatabase();
            String[] id = { String.valueOf(userId) };
            db.update(userTable.getName(), values, "id=?", id);
        } catch (Exception e) {
            Ln.e(e);
        } finally {
            if(db != null && db.isOpen())
                db.close();
        }
    }

    public synchronized void updateMobi(int mobiId, ContentValues values) {
        SQLiteDatabase db = null;
        try {
            db = getWritableDatabase();
            String[] id = { String.valueOf(mobiId) };
            db.update(mobiTable.getName(), values, "id=?", id);
        } catch (Exception e) {
            Ln.e(e);
        } finally {
            if(db != null && db.isOpen())
                db.close();
        }
    }

    public synchronized List<Mobi> selectAllMobis(int userId) {
        return selectAllMobisCondition(userId, false);
    }

    public synchronized void deleteAllmobis(int userId) {
        SQLiteDatabase db = null;
        try {
            List<Mobi> mobis = selectAllMobis(userId);

            db = getWritableDatabase();
            for(Mobi mobi : mobis) {
                String[] params = { String.valueOf(mobi.getId()) };
                db.delete(mobiTable.getName(), "id=?", params);
            }
        } catch (Exception e) {
            Ln.e(e);
        } finally {
            if(db != null && db.isOpen())
                db.close();
        }
    }

    public MobiClubUser selectByUserId(String userId) {
        SQLiteDatabase db = null;
        try {
            db = getWritableDatabase();

            String[] args = { userId };
            Cursor cursor = db.query(this.userTable.getName(),
                    userTable.getColumnsName(), "UserId=?", args, null, null, null);

            MobiClubUser mobiClubUser = createUserFromCursor(cursor);
            cursor.close();
            return mobiClubUser;
        } catch (Exception e) {
            Ln.e(e);
        } finally {
            if(db != null && db.isOpen())
                db.close();
        }
        return null;
    }

    private MobiClubUser createUserFromCursor(Cursor cursor) {
        MobiClubUser mobiClubUser = null;
        if (cursor.moveToFirst()) {

            int id = cursor.getInt(UserTable.ID_COL_INDEX);
            String name = cursor.getString(UserTable.NAME_COL_INDEX);
            Integer userIdInt = cursor.getInt(UserTable.USER_ID_COL_INDEX);
            String email = cursor.getString(UserTable.EMAIL_COL_INDEX);

            String birth = cursor.getString(UserTable.BIHTHDAY_COL_INDEX);
            String gender = cursor.getString(UserTable.GENDER_COL_INDEX);
            long facebookId = cursor.getLong(UserTable.FACEBOOK_ID_COL_INDEX);
            String lastName = cursor.getString(UserTable.LAST_NAME_COL_INDEX);
            long accessExpires = cursor.getLong(UserTable.FACEBOOK_ACCESS_EXPIRES_COL_INDEX);
            String accessToken = cursor.getString(UserTable.FACEBOOK_ACCESS_TOKEN_COL_INDEX);
            String photo = cursor.getString(UserTable.PHOTO_COL_INDEX);
            String facebookEmail = cursor.getString(UserTable.FACEBOOK_EMAIL_COL_INDEX);
            String cpf = cursor.getString(UserTable.CPF_COL_INDEX);

            mobiClubUser = new MobiClubUser(userIdInt);
            mobiClubUser.setId(id);
            mobiClubUser.setUserId(userIdInt);
            mobiClubUser.setName(name);
            mobiClubUser.setEmail(email);
//            mobiClubUser.setFacebokEmail(facebookEmail);
            mobiClubUser.setCpf(cpf);

            Date birthDate = Util.parseDate(birth);
            mobiClubUser.setBirthday(birthDate);

            mobiClubUser.setGenderType(gender);
            mobiClubUser.setFacebookId(facebookId);
            mobiClubUser.setLastName(lastName);
            mobiClubUser.setFacebookAccessExpires(accessExpires);
            mobiClubUser.setFacebookAccessToken(accessToken);
            mobiClubUser.setProfilePicture(photo);
        }
        return mobiClubUser;
    }

    public MobiClubUser findUserByEmail(String email) {
        SQLiteDatabase db = null;
        try {
            db = getWritableDatabase();

            String[] args = { email };
            Cursor cursor = db.query(this.userTable.getName(),
                    userTable.getColumnsName(), "Email=?", args, null, null, null);

            MobiClubUser mobiClubUser = createUserFromCursor(cursor);
            cursor.close();
            return mobiClubUser;
        } catch (Exception e) {
            Ln.e(e);
        } finally {
            if(db != null && db.isOpen())
                db.close();
        }
        return null;
    }

    public MobiClubUser findUserByToken(String token) {
        SQLiteDatabase db = null;
        MobiClubUser mobiClubUser = null;
        try {
            db = getWritableDatabase();

            String[] args = { token };

            Cursor cursor = db.query(this.userTable.getName(),
                    userTable.getColumnsName(), "UserId=?", args, null, null, null);

            mobiClubUser = createUserFromCursor(cursor);
            cursor.close();
        } catch (Exception e) {
            Ln.e(e);
        } finally {
            if(db != null && db.isOpen())
                db.close();
        }
        return mobiClubUser;
    }

}
