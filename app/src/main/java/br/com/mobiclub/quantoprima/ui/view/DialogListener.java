package br.com.mobiclub.quantoprima.ui.view;

public interface DialogListener {
    public static int RESULT_OK = 0;
    public static int RESULT_ERROR = 1;

    public void onCloseResult(int result);
}