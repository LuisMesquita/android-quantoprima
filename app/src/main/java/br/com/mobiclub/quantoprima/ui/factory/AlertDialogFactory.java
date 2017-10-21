package br.com.mobiclub.quantoprima.ui.factory;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

import br.com.mobiclub.quantoprima.R;

/**
 *
 */
public class AlertDialogFactory {
    public static AlertDialog createDefaultError(Activity activity, int message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setTitle(R.string.dialog_title_error);
        builder.setMessage(message);
        return builder.create();
    }

    public static AlertDialog createDefaultError(Activity activity, String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setTitle(title);
        if(message != null)
            builder.setMessage(message);
        return builder.create();
    }

    public static AlertDialog createDefaultError(Activity activity, int title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setTitle(title);
        builder.setMessage(message);
        return builder.create();
    }

    public static AlertDialog createDefaultError(Activity activity,
                                                 int title, int message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setTitle(title);
        builder.setMessage(message);
        return builder.create();
    }

    public static AlertDialog.Builder createDefaultBuilder(Activity activity,
                                                           int title, int message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(title);
        builder.setMessage(message);
        return builder;
    }

    public static AlertDialog createDefaultError(Activity activity, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setTitle(R.string.dialog_title_error);
        builder.setMessage(message);
        return builder.create();
    }

    public static AlertDialog.Builder createListAlertDialog(Activity activity, int title,
                                                            String[] items) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(title).setCancelable(true);
        return builder;
    }

    public static AlertDialog createAlertDialog(Activity activity,
                                                Integer title, int message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setTitle(title);
        builder.setMessage(message);
        return builder.create();
    }
}
