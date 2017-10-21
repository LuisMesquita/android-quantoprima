package br.com.mobiclub.quantoprima.service;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;

import br.com.mobiclub.quantoprima.MobiClubApplication;
import br.com.mobiclub.quantoprima.config.Injector;
import br.com.mobiclub.quantoprima.core.service.MobiClubServiceApi;
import br.com.mobiclub.quantoprima.core.service.MobiClubServiceProvider;
import br.com.mobiclub.quantoprima.core.service.api.entity.Establishment;
import br.com.mobiclub.quantoprima.core.service.api.entity.GainMobiResponse;
import br.com.mobiclub.quantoprima.database.MobiClubDatabase;
import br.com.mobiclub.quantoprima.database.table.MobiTable;
import br.com.mobiclub.quantoprima.domain.Mobi;
import br.com.mobiclub.quantoprima.facade.Facade;
import br.com.mobiclub.quantoprima.domain.Constants;
import br.com.mobiclub.quantoprima.util.Ln;

public class MobiSyncingService extends IntentService {

    @Inject
    MobiClubServiceProvider serviceProvider;

    @Override
    public void onCreate() {
        super.onCreate();

        Injector.inject(this);
    }

    /**
     * An IntentService must always have a constructor that calls the super constructor. The
     * string supplied to the super constructor is used to give a name to the IntentService's
     * background thread.
     */
    public MobiSyncingService() {
        super("MobiSyncingService");
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        // Gets data from the incoming Intent
        //String dataString = intent.getDataString();
        // Do work here, based on the contents of dataString
        try {
            MobiClubApplication.getInstance().setSyncingRunning(true);
            MobiClubDatabase database = MobiClubDatabase.getInstance();
            if (database == null) {
                MobiClubDatabase.createDatabase(getApplicationContext());
            }
            int quantity = syncMobis();
            Ln.d("Sincronizado %s mobis", quantity);

            Intent localIntent = new Intent(Constants.Extra.MOBI_SYNC_BROADCAST_ACTION).
                    putExtra(Constants.Extra.MOBI_SYNCED, quantity);
            // Broadcasts the Intent to receivers in this app.
            LocalBroadcastManager.getInstance(this).sendBroadcast(localIntent);
        } catch (Exception ex) {
            Ln.e(ex);
        } finally {
            MobiClubApplication.getInstance().setSyncingRunning(false);
        }
    }

    private int syncMobis() {
        Facade facade = Facade.getInstance();
        List<Mobi> mobis = facade.getMobisToSync();
        if (mobis.isEmpty()) {
            return 0;
        }
        MobiClubServiceApi service = serviceProvider.getService(this);
        if(service == null) {
            throw new NullPointerException("Não foi possivel usar o serviço. service == null");
        }

        long millis = System.currentTimeMillis();
        int currentTimeMin = -1;
        Mobi mobi = null;
        Ln.d("Sincronizando %s mobis", mobis.size());
        int synced = 0;
        for (int i = 0; i < mobis.size(); i++) {
            mobi = mobis.get(i);
            //mudar status do mobi para atualizando
            mobi.setStatus(MobiTable.STATUS_SYNCING_TYPE);
            Facade.getInstance().insertOrUpdateMobi(mobi);

            currentTimeMin = (int) (millis / 60000);
            int lessMinute = currentTimeMin - mobi.getTimeMinute();

            GainMobiResponse result = null;
            try {
                result = service.gainOfflineScore(mobi.getCode(), lessMinute);
                Establishment establishment = result.getEstablishment();
                if (result.isSuccess() && establishment != null) {
                    Ln.d("Setando location %s", establishment.getName());
                    mobi.setLocationName(establishment.getName());
                    mobi.setLogo(establishment.getLogoUrl());
                    mobi.setStatus(MobiTable.STATUS_SYNCED_TYPE);
                    Calendar c = Calendar.getInstance();
                    mobi.setTimeUpdate(c.getTime());
                    facade.insertOrUpdateMobi(mobi);
                    synced++;
                } else {
                    Ln.d("Ponto inválido.");
                    if(establishment != null) {
                        Ln.d("Setando location %s", establishment.getName());
                        mobi.setLocationName(establishment.getName());
                        mobi.setLogo(establishment.getLogoUrl());
                    } else {
                        Ln.d("Servidor não mandou o estabelecimento.");
                    }
                    mobi.setStatus(MobiTable.STATUS_SYNCED_ERROR_TYPE_EXPIRED);
                    facade.insertOrUpdateMobi(mobi);
                }
            } catch (Exception e) {
                Ln.e("Error ao atualizar mobi %s", e.getMessage());
                mobi.setStatus(MobiTable.STATUS_SYNCED_ERROR_TYPE_NO_CONNECTION);
                facade.insertOrUpdateMobi(mobi);
            }

        }
        return synced;
    }

}
