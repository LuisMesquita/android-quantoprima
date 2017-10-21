package br.com.mobiclub.quantoprima.ui.activity.account;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import br.com.mobiclub.quantoprima.R;
import br.com.mobiclub.quantoprima.domain.MobiClubUser;
import br.com.mobiclub.quantoprima.facade.Facade;
import br.com.mobiclub.quantoprima.ui.activity.CentralActivity;
import br.com.mobiclub.quantoprima.ui.activity.InjectorActivity;
import br.com.mobiclub.quantoprima.domain.Constants;
import br.com.mobiclub.quantoprima.util.UIUtils;
import butterknife.InjectView;

public class AccountBlockedActivity extends InjectorActivity {

    @InjectView(R.id.label_user_message)
    TextView userMessage;

    @InjectView(R.id.label_reason)
    TextView labelReason;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //Facade.getInstance().removeLastIntent();
        ActivityCompat.finishAffinity(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_blocked);

        Facade.getInstance().newIntent(getIntent());

        MobiClubUser user = Facade.getInstance().getLoggedUser();
        String userMessageValue = null;
        if(user != null) {
            userMessageValue = UIUtils.getColoredMessage(user.getName(), this,
                    R.string.account_blocked_label_user_message);
        } else {
            userMessageValue = getString(R.string.account_blocked_label_user_message2);
        }

        userMessage.setText(Html.fromHtml(userMessageValue));

        String reasonValue = getIntent().getStringExtra(Constants.Extra.REASON_USER_BLOCKED);
        String reasonString = getString(R.string.account_blocked_label_reason);
        labelReason.setText(reasonString);
    }

    public void onCentral(View view) {
        final Intent intent = new Intent(this, CentralActivity.class);
        startActivity(intent);
    }

}
