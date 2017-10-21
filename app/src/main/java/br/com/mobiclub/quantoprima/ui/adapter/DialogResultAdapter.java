package br.com.mobiclub.quantoprima.ui.adapter;

import android.view.View;

import java.io.Serializable;

import br.com.mobiclub.quantoprima.ui.view.DialogResultData;

/**
 *
 */
public interface DialogResultAdapter extends Serializable {
    public static final String RESULT = "result";

    public static final String RESULT_SUCCESS = "result_success";
    public static final String RESULT_ERROR = "result_error";

    public int getSuccessLayout();

    public int getErrorLayout();

    public void setResult(String type);

    public void onCreateView(View view, DialogResultData data);

    public boolean isSuccess();

    public boolean isError();

    public void setData(DialogResultData data);

    public DialogResultData getData();

    public String getErrorMessage();

    public void setErrorMessage(String errorMessage);

    public String[] getErrorMessageFromServer();

    public void setErrorMessageFromServer(String titleHead, String titleBody, String messageHead, String messageBody);

    public void setSuccessLayout(int successLayout);
}
