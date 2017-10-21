package br.com.mobiclub.quantoprima.ui.activity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import br.com.mobiclub.quantoprima.R;
import br.com.mobiclub.quantoprima.core.service.api.entity.User;
import butterknife.InjectView;

import static br.com.mobiclub.quantoprima.domain.Constants.Extra.USER;

public class UserActivity extends MobiClubActionBarActivity {

    @InjectView(R.id.iv_avatar) protected ImageView avatar;
    @InjectView(R.id.tv_name) protected TextView name;

    private User user;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.user_view);

        if (getIntent() != null && getIntent().getExtras() != null) {
            user = (User) getIntent().getExtras().getSerializable(USER);
        }

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        name.setText(String.format("%s %s", user.getName(), user.getLastName()));
    }

}
