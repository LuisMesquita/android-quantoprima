package br.com.mobiclub.quantoprima.ui.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import br.com.mobiclub.quantoprima.R;
import br.com.mobiclub.quantoprima.domain.MobiClubUser;
import br.com.mobiclub.quantoprima.facade.Facade;
import br.com.mobiclub.quantoprima.ui.factory.AlertDialogFactory;
import br.com.mobiclub.quantoprima.domain.Constants;
import br.com.mobiclub.quantoprima.util.Ln;
import br.com.mobiclub.quantoprima.util.NetworkUtil;
import br.com.mobiclub.quantoprima.util.UIUtils;

public class AppInactiveActivity extends ActionBarActivity {

    private static final int REQUEST = 0;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //Facade.getInstance().removeLastIntent();
        ActivityCompat.finishAffinity(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_inactive);

        Facade.getInstance().newIntent(getIntent());

        MobiClubUser user = Facade.getInstance().getLoggedUser();
        TextView labelUserWelcome = (TextView) findViewById(R.id.labelUserWelcome);
        String userWelcome = null;
        if(user != null) {
            userWelcome = UIUtils.getColoredMessage(user.getName(), this,
                    R.string.outdated_label_user_welcome);
        } else {
            userWelcome = getString(R.string.outdated_label_user_welcome2);
        }

        labelUserWelcome.setText(Html.fromHtml(userWelcome));
    }

    public void onKnowMore(View view) {
        Ln.d("onKnowMore");
        if(!NetworkUtil.isConnected(this)) {
            AlertDialog defaultError = AlertDialogFactory.createDefaultError(this, R.string.alert_title_attention,
                    R.string.alert_title_webview_no_connection);
            defaultError.setCancelable(true);
            defaultError.show();
            return;
        }
        Intent intent = new Intent(this, WebViewActivity.class);
        intent.putExtra(Constants.Extra.WEB_VIEW_URL, Constants.URL.APP_INATIVO);
        startActivityForResult(intent, REQUEST);
    }

}
