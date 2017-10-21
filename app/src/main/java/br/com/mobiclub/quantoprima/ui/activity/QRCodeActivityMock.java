package br.com.mobiclub.quantoprima.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import javax.inject.Inject;

import br.com.mobiclub.quantoprima.R;
import br.com.mobiclub.quantoprima.core.service.MobiClubServiceProvider;
import br.com.mobiclub.quantoprima.ui.view.DialogListener;
import br.com.mobiclub.quantoprima.domain.Constants;
import br.com.mobiclub.quantoprima.util.UIUtils;

public class QRCodeActivityMock extends MobiClubFragmentActivity
        implements DialogListener {

    private static final int RESULT_REQUEST_CODE = 0;

    @Inject
    MobiClubServiceProvider serviceProvider;
    private int layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        boolean online = getIntent().getBooleanExtra(Constants.Extra.QRCODE_READ_TYPE, false);
        if(online) {
            layout = R.layout.activity_qrcode_online;
        } else {
            layout = R.layout.activity_qrcode_offline;
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    public int getLayout() {
        return layout;
    }

    /**
     * Metodo chamado quando ocorre a leitura do qrcode
     *
     * @param view
     */
    public void onReadCode(View view) {
        String qrCode = UIUtils.generateQrCodeShot();
        Intent data = new Intent();
        data.putExtra(Constants.Extra.QR_CODE, qrCode);
        setResult(Activity.RESULT_OK, data);
        finish();
    }

    @Override
    public void onCloseResult(int result) {
        finish();
    }

}
