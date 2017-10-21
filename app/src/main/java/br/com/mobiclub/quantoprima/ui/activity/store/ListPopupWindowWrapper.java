package br.com.mobiclub.quantoprima.ui.activity.store;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.widget.ListPopupWindow;

/**
 * Created by Thales Ferreira on 11/09/2014.
 */
public class ListPopupWindowWrapper {

    private ListPopupWindow listPopupWindow;

    @SuppressLint("NewApi")
    public ListPopupWindowWrapper(Activity activity) {
        this.listPopupWindow = new ListPopupWindow(activity);
    }

    public ListPopupWindow getWrapper() {
        return listPopupWindow;
    }
}
