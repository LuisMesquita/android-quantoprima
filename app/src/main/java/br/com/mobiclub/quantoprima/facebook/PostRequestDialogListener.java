package br.com.mobiclub.quantoprima.facebook;

import android.os.Bundle;

import com.facebook.android.dialog.DialogError;
import com.facebook.android.dialog.Facebook.DialogListener;
import com.facebook.android.dialog.FacebookError;

import br.com.mobiclub.quantoprima.util.Ln;

public abstract class PostRequestDialogListener implements DialogListener {

	@Override
	public abstract void onComplete(Bundle values);
	
	@Override
	public void onFacebookError(FacebookError e) {
		Ln.e(e);
	}

	@Override
	public void onError(DialogError e) {
        Ln.e(e);
	}

	@Override
	public void onCancel() {
	}

}
