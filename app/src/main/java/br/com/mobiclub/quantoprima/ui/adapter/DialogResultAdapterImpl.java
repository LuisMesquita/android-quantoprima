package br.com.mobiclub.quantoprima.ui.adapter;

import android.view.View;

import br.com.mobiclub.quantoprima.R;
import br.com.mobiclub.quantoprima.ui.view.DialogResultData;

/**
 *
 */
public class DialogResultAdapterImpl implements DialogResultAdapter {

    private String type;
    private int layoutSuccess;
    private int layoutError;
    private DialogResultData data;
    private String errorMessage;

    public String titleHead = "";
    public String titleBody = "";
    public String messageHead = "";
    public String messageBody = "";

    public DialogResultAdapterImpl(int layoutSuccess, int layoutError) {
        this.layoutSuccess = layoutSuccess;
        this.layoutError = layoutError;
    }

    /**
     * Constroi uma onGainMobiSuccess result com error
     */
    public DialogResultAdapterImpl() {
        type = RESULT_ERROR;
        layoutError = R.layout.dialog_error;
    }

    @Override
    public int getSuccessLayout() {
        return layoutSuccess;
    }

    @Override
    public int getErrorLayout() {
        return layoutError;
    }

    @Override
    public void setResult(String type) {
        this.type = type;
    }

    @Override
    public void onCreateView(View view, DialogResultData data) {
    }

    @Override
    public boolean isSuccess() {
        return type != null && type.equals(RESULT_SUCCESS) ? true : false;
    }

    @Override
    public boolean isError() {
        return !isSuccess();
    }

    @Override
    public void setData(DialogResultData data) {
        this.data = data;
    }

    @Override
    public DialogResultData getData() {
        return data;
    }

    @Override
    public String getErrorMessage() {
        return errorMessage;
    }

    @Override
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public String[] getErrorMessageFromServer() {
        String[] msgs = new String[4];

        msgs[0] = this.titleHead;
        msgs[1] = this.titleBody;
        msgs[2] = this.messageHead;
        msgs[3] = this.messageBody;

        return msgs;
    }

    @Override
    public void setErrorMessageFromServer(String titlehead, String titlebody, String messagehead, String messagebody) {
        titleHead = titlehead;
        titleBody = titlebody;
        messageHead = messagehead;
        messageBody = messagebody;
    }

    @Override
    public void setSuccessLayout(int successLayout) {
        this.layoutSuccess = successLayout;
    }
}
