package br.com.mobiclub.quantoprima.ui.factory;

import android.app.Activity;
import android.app.ProgressDialog;

/**
 *
 */
public class ProgressDialogFactory {
    public static ProgressDialog createProgress(Activity activity, int message) {
        final ProgressDialog dialog = new ProgressDialog(activity);
        dialog.setCancelable(true);
        dialog.setMessage(activity.getText(message));
        dialog.setIndeterminate(true);
        return dialog;
    }
}
