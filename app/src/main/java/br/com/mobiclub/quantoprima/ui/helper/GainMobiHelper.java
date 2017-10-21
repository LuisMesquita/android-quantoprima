package br.com.mobiclub.quantoprima.ui.helper;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import java.io.Serializable;

import javax.inject.Inject;

import br.com.mobiclub.quantoprima.MobiClubApplication;
import br.com.mobiclub.quantoprima.R;
import br.com.mobiclub.quantoprima.config.Injector;
import br.com.mobiclub.quantoprima.core.service.MobiClubServiceApi;
import br.com.mobiclub.quantoprima.core.service.MobiClubServiceProvider;
import br.com.mobiclub.quantoprima.core.service.api.entity.ApiError;
import br.com.mobiclub.quantoprima.core.service.api.entity.Establishment;
import br.com.mobiclub.quantoprima.core.service.api.entity.EstablishmentLocation;
import br.com.mobiclub.quantoprima.core.service.api.entity.GainMobiResponse;
import br.com.mobiclub.quantoprima.core.service.api.exception.AppBlockedException;
import br.com.mobiclub.quantoprima.core.service.api.exception.AppOutdatedException;
import br.com.mobiclub.quantoprima.core.service.api.exception.UserBlockedException;
import br.com.mobiclub.quantoprima.domain.Mobi;
import br.com.mobiclub.quantoprima.domain.MobiClubUser;
import br.com.mobiclub.quantoprima.events.NetworkErrorEvent;
import br.com.mobiclub.quantoprima.events.RestErrorEvent;
import br.com.mobiclub.quantoprima.facade.Facade;
import br.com.mobiclub.quantoprima.ui.activity.AppInactiveActivity;
import br.com.mobiclub.quantoprima.ui.activity.GainMobiListener;
import br.com.mobiclub.quantoprima.ui.activity.account.AccountBlockedActivity;
import br.com.mobiclub.quantoprima.ui.activity.account.CadastrarCpfToStoreActivity;
import br.com.mobiclub.quantoprima.ui.activity.launch.OutdatedActivity;
import br.com.mobiclub.quantoprima.ui.adapter.DialogResultAdapter;
import br.com.mobiclub.quantoprima.ui.factory.AlertDialogFactory;
import br.com.mobiclub.quantoprima.ui.factory.DialogResultFactory;
import br.com.mobiclub.quantoprima.ui.factory.ProgressDialogFactory;
import br.com.mobiclub.quantoprima.ui.view.DialogResultData;
import br.com.mobiclub.quantoprima.ui.view.ResultDialog;
import br.com.mobiclub.quantoprima.domain.Constants;
import br.com.mobiclub.quantoprima.util.Ln;
import br.com.mobiclub.quantoprima.util.SafeAsyncTask;
import br.com.mobiclub.quantoprima.util.Util;
import retrofit.RetrofitError;

/**
 *
 */
public class GainMobiHelper implements Serializable {

    public static final String MOBIS_TO_GAIN = "10";
    public static final int MOBIS_TO_GAIN_VALUE = 10;
    private final GainMobiListener listener;
    private final FragmentActivity activity;

    private MobiClubServiceProvider serviceProvider;
    private String code;

    @Inject
    Bus bus;
    private String message;
    private ProgressDialog progress;

    public GainMobiHelper(FragmentActivity activity, MobiClubServiceProvider serviceProvider) {
        try {
            this.listener = (GainMobiListener) activity;
            this.activity = activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Atividade deve implementar GainMobiListener");
        }
        this.serviceProvider = serviceProvider;
        if (serviceProvider == null)
            throw new IllegalArgumentException("Service provider can not be null");

        Injector.inject(this);
    /*
        @Override
        this.onActivityResult(int requestCode, int resultCode, Intent data) {
            switch (requestCode) {
                case 20000:
                    Log.d("RESULT", this.code);
                    gainMobi(this.code);
                    break;
                default:
                    break;
            }
        }
        */
    }

    /**
     * Executado quando o QRCode eh capturado com sucesso
     */
    public void gainMobi(String code) {
        progress = ProgressDialogFactory.createProgress(activity, R.string.loading_message);
        progress.show();
        bus.register(this);
        this.code = code;
        if (code == null) {
            String message = activity.getString(R.string.home_gain_mobi_invalid_qr_code);

            GainMobiResponse gainMobiResponse = new GainMobiResponse();
            gainMobiResponse.setTitleHead("Opa!");
            gainMobiResponse.setTitleBody("Ocorreu um erro.");
            gainMobiResponse.setMessageHead("Erro:");
            gainMobiResponse.setMessageBody(message);
            onGainMobiError(gainMobiResponse);
            return;
        }
        /* Validação do Código
        boolean valid = false;
        if (code.length() == Mobi.QR_CODE_LENGTH) {
            String subcode = code.substring(0, 8);
            if (subcode.equals("mobiclub")) {
                valid = true;
                this.code = code.substring(8);
            }
        }
        */
        boolean valid = true;

        if (valid) {
            // Checa a conexão
            if (listener.isConnected(true)) {
                gainMobiOnline(this.code);
            } else {
                gainMobiOffline(this.code);
            }
        } else {
            String message = activity.getString(R.string.home_gain_mobi_invalid_qr_code);

            GainMobiResponse gainMobiResponse = new GainMobiResponse();
            gainMobiResponse.setTitleHead("Opa!");
            gainMobiResponse.setTitleBody("Ocorreu um erro.");
            gainMobiResponse.setMessageHead("Erro:");
            gainMobiResponse.setMessageBody(message);
            onGainMobiError(gainMobiResponse);
        }
    }

    /**
     * Reexecutando, quando this.code já está setado e estamos voltando da tela de CPF
     */
    public void gainMobiFromCPF() {
        gainMobi(this.code);
    }

    private void gainMobiOnline(final String qrCode) {
        new SafeAsyncTask<GainMobiResponse>() {

            @Override
            public GainMobiResponse call() throws Exception {
                MobiClubServiceApi service = serviceProvider.getService(activity);
                int netStatus = getNetStatus();

                GainMobiResponse result = null;
                if (!Util.isShotMobi(qrCode)) {
                    result = service.gainOnlineScoreQRCode(netStatus, qrCode);
                    result.setShotMobi(false);
                    message = result.getMessage();
                } else {
                    //ganhou shot mobi
                    Ln.d("Ganhou um shotmobi");
                    result = service.shotReward(qrCode);
                    result.setShotMobi(true);
                    message = result.getMessage();
                }
                return result;
            }

            @Override
            protected void onException(Exception exception) throws RuntimeException {
                super.onException(exception);
                Intent intent = null;
                if (exception instanceof AppBlockedException) {
                    Ln.d("AppBlockedException");
                    intent = new Intent(activity, AppInactiveActivity.class);
                } else if (exception instanceof AppOutdatedException) {
                    Ln.d("AppOutdatedException");
                    intent = new Intent(activity, OutdatedActivity.class);
                } else if (exception instanceof UserBlockedException) {
                    Ln.d("UserBlockedException");
                    intent = new Intent(activity, AccountBlockedActivity.class);
                    intent.putExtra(Constants.Extra.REASON_USER_BLOCKED, exception.getMessage());
                }
                if (intent != null) {
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    activity.startActivity(intent);
                    return;
                }
                if (!(exception instanceof RetrofitError)) {
                    onGainMobiError(null);
                }
                Ln.e(exception);
            }

            @Override
            protected void onSuccess(GainMobiResponse result) throws Exception {
                super.onSuccess(result);
                Establishment establishment = result.getEstablishment();

                String titleHead = result.getTitleHead();
                String titleBody = result.getTitleBody();
                String messageHead = result.getMessageHead();
                String messageBody = result.getMessageBody();

                if(result.getHttpStatus() == 412) {
                    onGainNeedCPF();
                    return;
                }

                if (result.isSuccess() && establishment != null
                        && establishment.getLocation() != null) {
                    onGainMobiSuccess(result, true, result.isShotMobi());
                } else {
                    if(result.isShotMobi() && result.getHttpStatus() == 200) {
                        onGainMobiSuccess(result, true, result.isShotMobi());
                        return;
                    } else {
                        if(result.isShotMobi() && result.getHttpStatus() == 202) {
                            onGainShotFailed(result);
                            return;
                        }
                    }
                    if (titleHead != null && titleBody != null && messageHead != null && messageBody != null) {
                        onGainMobiError(result);
                    } else {
                        Ln.e("Houve sucesso na requisicao mas o HttpStatus da mensagem não foi igual a 200");
                        onGainMobiError(result);
                    }
                }
            }

        }.execute();
    }

    private int getNetStatus() {
        return listener.isConnected(true) ? Mobi.MOBI_ONLINE_TYPE : Mobi.MOBI_OFFLINE_TYPE;
    }

    private void gainMobiOffline(String code) {
        // Provisório para essa versão:
        progress.dismiss();
        AlertDialogFactory.createDefaultError(activity,
                R.string.service_comunication_error_on_always).show();
        return;
        /*
        Ln.d("Tentando ganhar mobi offline");
        if(Util.isShotMobi(code)) {
            Ln.d("É um hotmobi");
            progress.dismiss();
            AlertDialogFactory.createDefaultError(activity,
                    R.string.service_comunication_error_on_shot).show();
            return;
        }
        try {
            Ln.d("Não é shotmobi");
            long millis = System.currentTimeMillis();
            int currentTimeMin = (int) (millis / 60000);

            Facade facade = Facade.getInstance();
            Mobi mobi = new Mobi(code, currentTimeMin);
            facade.insertOrUpdateMobi(mobi);

            showDialog(false, false, null);
            progress.dismiss();
        } catch (Exception e) {
            Ln.e(e);
            onGainMobiError(null);
        }
        */
    }

    public class GainMobiDialogData implements DialogResultData {
        public String congratUserMessage;
        public GainMobiResponse mobiResponse;
        public boolean online;
        public boolean shot;
        private EstablishmentLocation location;

        public GainMobiDialogData(GainMobiResponse mobiResponse,
                                  boolean online, boolean shot) {
            this.mobiResponse = mobiResponse;
            this.online = online;
            this.shot = shot;
        }

        @Override
        public EstablishmentLocation getLocation() {
            return location;
        }

        @Override
        public void setLocation(EstablishmentLocation location) {
            this.location = location;
        }

        @Override
        public Integer getId() {
            return location.getId();
        }

        @Override
        public String getFacebookPost() {
            return "";
        }
    }

    public void onGainMobiSuccess(GainMobiResponse result, boolean online, boolean shot) {
        EstablishmentLocation location = null;
        if (online && !shot) {
            Establishment establishment = result.getEstablishment();
            location = establishment.getLocation();
            MobiClubApplication.pontosGanhados = result.getPlusScore();
            location.setEstablishment(establishment);
            if (result != null && listener != null) {
                //atualiza a ui com novo mobi
                location.plusScore = result.getPlusScore();
                listener.onGainedMobi(location);
            }
        }
        progress.dismiss();
        showDialog(online, shot, result);
    }

    private void showDialog(boolean online, boolean shot, GainMobiResponse mobiResponse) {
        DialogResultAdapter dialogResultAdapter = createGainMobiDialog(online, shot, mobiResponse);

        if(mobiResponse==null){
            MobiClubUser loggedUser = Facade.getInstance().getLoggedUser();
            String congratMsg = activity.getString(R.string.qrcode_success_label_congrat_user,
                    loggedUser.getName());

            String titulo = activity.getString(R.string.dialog_gain_mobi_online_label_total_mobis);
            mobiResponse = new GainMobiResponse();
            mobiResponse.setTitleHead(titulo);
            mobiResponse.setMessageHead(congratMsg);
            mobiResponse.setMessageBody(activity.getString(R.string.qrcode_result_label_congrat_user_message));
            mobiResponse.setHttpStatus(1000);

        }

        showDialogResult(dialogResultAdapter, mobiResponse);
        bus.unregister(this);
    }

    private DialogResultAdapter createGainMobiDialog(boolean online, boolean shot, GainMobiResponse mobiResponse) {
        MobiClubUser loggedUser = Facade.getInstance().getLoggedUser();

        /*
        String congratMsg = activity.getString(R.string.qrcode_success_label_congrat);
        if (loggedUser != null && loggedUser.getName() != null)
            congratMsg = activity.getString(R.string.qrcode_success_label_congrat_user,
                    loggedUser.getName());

        String totalMobisString = activity.getString(R.string.dialog_gain_mobi_online_label_total_mobis,
                MOBIS_TO_GAIN);
        String mobisTotalMessage = totalMobisString;
        */

        GainMobiDialogData data = null;

        if(mobiResponse==null){
            String congratMsg = activity.getString(R.string.qrcode_success_label_congrat_user,
                    loggedUser.getName());

            String titulo = activity.getString(R.string.dialog_gain_mobi_online_label_total_mobis);
            mobiResponse = new GainMobiResponse();
            // mobiResponse.setTitleHead(titulo);
            mobiResponse.setMessageHead(congratMsg);

            data = new GainMobiDialogData(mobiResponse, online, shot);
            data.setLocation(null);

        } else {

            data = new GainMobiDialogData(mobiResponse, online, shot);
            String congratMsg = activity.getString(R.string.qrcode_success_label_congrat_user,
                    loggedUser.getName());
            data.mobiResponse.setMessageHead(congratMsg);

            if(mobiResponse.getEstablishment()!=null) {
                data.setLocation(mobiResponse.getEstablishment().getLocation());
            }
        }

        return DialogResultFactory.createGainMobiSuccess(data);
    }

    private void onGainShotFailed(GainMobiResponse result) {
        DialogResultAdapter dialogResultAdapter = DialogResultFactory.createGainShotFailed();
        showDialogResult(dialogResultAdapter, result);
        bus.unregister(this);
        progress.dismiss();
    }

    private void onGainNeedCPF() {
        progress.dismiss();
        Intent intent = new Intent(activity.getApplicationContext(), CadastrarCpfToStoreActivity.class);
        activity.startActivityForResult(intent, 20000);
    }

    /**
     * Dialogo de erro.
     * Quando erro na app ou codigo invalido
     *
     */
    public void onGainMobiError(GainMobiResponse mobiResponse) {
        DialogResultAdapter dialogResultAdapter = DialogResultFactory.createErrorDefault();
        showDialogResult(dialogResultAdapter, mobiResponse);
        bus.unregister(this);
        progress.dismiss();
    }

    private void showDialogResult(DialogResultAdapter result, GainMobiResponse mobiResponse) {
        //TODO: make a factory ou builder
        ResultDialog dialog = new ResultDialog();
        Bundle args = new Bundle();
        dialog.gainMobiResponse = mobiResponse;
        args.putSerializable(Constants.Extra.DIALOG_RESULTER, result);
        dialog.setArguments(args);
        dialog.show(getSupportFragmentManager(), "resultDialog");
    }

    private FragmentManager getSupportFragmentManager() {
        return activity.getSupportFragmentManager();
    }

    @Subscribe
    public void onNetworkErrorEvent(NetworkErrorEvent e) {
        //nesse caso tenta offline
        gainMobiOffline(code);
    }

    /**
     * Quando ocorre erro na chamada do servico
     *
     * @param e
     */
    @Subscribe
    public void onRestErrorEvent(RestErrorEvent e) {
        if (!e.isGainScoreFailed()) {
            return;
        }
        ApiError apiError = e.getApiError();
        String message = null;
        if (apiError != null)
            message = apiError.getMessage();

        GainMobiResponse gainMobiResponse = new GainMobiResponse();
        gainMobiResponse.setTitleHead("Opa!");
        gainMobiResponse.setTitleBody("Ocorreu um erro.");
        gainMobiResponse.setMessageHead("Erro");
        gainMobiResponse.setMessageBody(message);
        onGainMobiError(gainMobiResponse);
    }

}