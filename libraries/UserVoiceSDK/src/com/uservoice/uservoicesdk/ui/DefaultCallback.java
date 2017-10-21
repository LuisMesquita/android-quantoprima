package com.uservoice.uservoicesdk.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;

import com.uservoice.uservoicesdk.R;
import com.uservoice.uservoicesdk.rest.Callback;
import com.uservoice.uservoicesdk.rest.RestResult;

public abstract class DefaultCallback<T> extends Callback<T> {

    private static final String TAG = "com.uservoice.uservoicesdk";

    private final Context context;

    public DefaultCallback(Context context) {
        this.context = context;
    }

    @Override
    public void onError(RestResult error) {
        Log.e(TAG, error.getMessage());
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.setTitle("Erro");
            builder.setMessage(R.string.uv_network_error).create().show();
        } catch (Exception e) {
            // This can happen if the activity is already gone
            Log.e(TAG, "Failed trying to show alert: " + e.getMessage());
        }
    }

}
