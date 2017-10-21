package br.com.mobiclub.quantoprima.ui.activity.launch;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import br.com.mobiclub.quantoprima.R;
import br.com.mobiclub.quantoprima.domain.Constants;
import br.com.mobiclub.quantoprima.domain.MobiClubUser;
import br.com.mobiclub.quantoprima.facade.Facade;
import br.com.mobiclub.quantoprima.ui.activity.FullScreenMobiClubActivity;
import br.com.mobiclub.quantoprima.util.UIUtils;

public class OutdatedActivity extends FullScreenMobiClubActivity {

    private static final int PLAY_STORE_REQUEST_CODE = 0;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //Facade.getInstance().removeLastIntent();
        ActivityCompat.finishAffinity(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Facade.getInstance().newIntent(getIntent());

        TextView labelUserWelcome = (TextView) findViewById(R.id.labelUserWelcome);
        TextView labelUpdateMsg = (TextView) findViewById(R.id.labelUpdateMsg);
        TextView labelDownloadMsg = (TextView) findViewById(R.id.labelDownloadMsg);

        //TODO: change this
        MobiClubUser user = Facade.getInstance().getLoggedUser();

        String userWelcome = null;
        if(user != null) {
            userWelcome = UIUtils.getColoredMessage(user.getName(), this,
                    R.string.outdated_label_user_welcome);
        } else {
            userWelcome = UIUtils.getColoredMessage("", this,
                    R.string.outdated_label_user_welcome);
        }

        labelUserWelcome.setText(Html.fromHtml(userWelcome));
        labelUpdateMsg.setText(R.string.label_update_msg);
        labelDownloadMsg.setText(R.string.label_download_msg);
    }

    @Override
    public int getLayout() {
        return R.layout.activity_outdated;
    }

    public void updateAction(View view) {
        goToPlayStore();
    }

    private void goToPlayStore() {
        Intent browserIntent = new Intent(
                Intent.ACTION_VIEW,
                Uri.parse(Constants.URL.AVALIE_NA_PLAY_STORE));
        startActivity(browserIntent);
        ActivityCompat.finishAffinity(this);
    }

}
