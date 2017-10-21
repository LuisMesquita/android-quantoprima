package br.com.mobiclub.quantoprima.dao;

import android.content.ContentValues;

import java.util.Date;
import java.util.List;

import br.com.mobiclub.quantoprima.database.MobiClubDatabase;
import br.com.mobiclub.quantoprima.database.table.MobiTable;
import br.com.mobiclub.quantoprima.domain.Mobi;
import br.com.mobiclub.quantoprima.util.Ln;
import br.com.mobiclub.quantoprima.util.Util;

public class MobiDAO {

    private MobiClubDatabase db;

    public MobiDAO() {
        this.db = MobiClubDatabase.getInstance();
    }

    public synchronized void save(Mobi mobi, int userId) {
        if (mobi != null) {
            if (mobi.getId() == null) {
                mobi.setStatus(MobiTable.STATUS_NOT_SYNC_TYPE);
                ContentValues values = getValues(mobi, userId);
                Ln.d("Adicionando mobi %s", mobi);
                this.db.insertMobi(values);
            } else {
                Ln.d("Atualizando mobi %s", mobi);
                ContentValues values = getValues(mobi, userId);
                db.updateMobi(mobi.getId(), values);
            }
        }
    }

    private ContentValues getValues(Mobi mobi, int userId) {
        ContentValues values = new ContentValues();
        values.put(MobiTable.USER_ID_COL, userId);
        values.put(MobiTable.CODE_COL, mobi.getCode());
        values.put(MobiTable.TIME_MINUTE_COL, mobi.getTimeMinute());
        values.put(MobiTable.STATUS_COL, mobi.getStatus());
        values.put(MobiTable.LOCATION_COL, mobi.getLocationName());
        values.put(MobiTable.LOGO_COL, mobi.getLogo());

        Date timeUpdate = mobi.getTimeUpdate();
        if(timeUpdate != null) {
            values.put(MobiTable.TIME_UPDATE_COL, Util.getSQLDateString(timeUpdate));
        }
        return values;
    }

    public synchronized List<Mobi> getMobisToSyncByUser(int userId) {
        return db.selectMobisToSync(userId);
    }

    public synchronized List<Mobi> getAllMobisByUser(int userId) {
        return db.selectAllMobis(userId);
    }

    //TODO: remove
    public synchronized void seed(int userId) {
        Mobi[] mobis = {new Mobi("MOBI1", 0), new Mobi("MOBI2", 2333),
                new Mobi("MOBI3", 12),
                new Mobi("MOBI4", 14)};
        for (Mobi m : mobis) {
            save(m, 1);
        }
    }

    public void deleteAllmobis(int userId) {
        db.deleteAllmobis(userId);
    }

}
