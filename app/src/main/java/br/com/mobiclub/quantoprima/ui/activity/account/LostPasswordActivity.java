package br.com.mobiclub.quantoprima.ui.activity.account;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import javax.inject.Inject;

import br.com.mobiclub.quantoprima.R;
import br.com.mobiclub.quantoprima.core.service.MobiClubServiceApi;
import br.com.mobiclub.quantoprima.core.service.MobiClubServiceProvider;
import br.com.mobiclub.quantoprima.core.service.api.entity.ApiError;
import br.com.mobiclub.quantoprima.core.service.api.entity.ApiResult;
import br.com.mobiclub.quantoprima.core.service.api.exception.AppBlockedException;
import br.com.mobiclub.quantoprima.core.service.api.exception.AppOutdatedException;
import br.com.mobiclub.quantoprima.core.service.api.exception.UserBlockedException;
import br.com.mobiclub.quantoprima.events.NetworkErrorEvent;
import br.com.mobiclub.quantoprima.events.RestErrorEvent;
import br.com.mobiclub.quantoprima.facade.Facade;
import br.com.mobiclub.quantoprima.ui.activity.AppInactiveActivity;
import br.com.mobiclub.quantoprima.ui.activity.MobiClubActionBarActivity;
import br.com.mobiclub.quantoprima.ui.activity.launch.OutdatedActivity;
import br.com.mobiclub.quantoprima.ui.factory.AlertDialogFactory;
import br.com.mobiclub.quantoprima.ui.factory.ProgressDialogFactory;
import br.com.mobiclub.quantoprima.domain.Constants;
import br.com.mobiclub.quantoprima.util.Ln;
import br.com.mobiclub.quantoprima.util.SafeAsyncTask;
import butterknife.InjectView;
import retrofit.RetrofitError;

public class LostPasswordActivity extends MobiClubActionBarActivity {

    private ApiResult item;

    @Inject
    MobiClubServiceProvider serviceProvider;

    @InjectView(R.id.et_email)
    EditText etEmail;

    private String email;

    @Inject
    Bus bus;
    private String message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lost_password);

        int titleId = getResources().getIdentifier("action_bar_title", "id", "android");
        TextView titleTextView = (TextView) findViewById(titleId);
        titleTextView.setTextColor(getResources().getColor(R.color.text_black));
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/bold.ttf");
        titleTextView.setTypeface(font);

        Facade.getInstance().newIntent(getIntent());

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Facade.getInstance().removeLastIntent();
    }

    @Override
    protected void onPause() {
        super.onPause();
        bus.unregister(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        bus.register(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.account_central, menu);
        return true;
    }

    @Subscribe
    public void onNetworkErrorEvent(NetworkErrorEvent e) {
        //nesse caso tenta offline
        AlertDialogFactory.createDefaultError(this, R.string.service_comunication_error).show();
    }

    /**
     * Quando ocorre erro na chamada do servico
     *
     * @param e
     */
    @Subscribe
    public void onRestErrorEvent(RestErrorEvent e) {
        if (!e.isRequestPassword()) {
            return;
        }
        ApiError apiError = e.getApiError();
        String message = null;
        if (apiError != null)
            message = apiError.getMessage();
        AlertDialogFactory.createDefaultError(this, message).show();
    }

    public void changePassword(View view) {
        Ln.d("changePassword");
        String e = etEmail.getText().toString();
        if(e != null && !e.isEmpty()) {
            email = e;
            change();
        } else {
            AlertDialogFactory.createDefaultError(this, R.string.email_required).show();
        }
    }

    private void change() {
        final ProgressDialog dialog = ProgressDialogFactory.createProgress(this,
                R.string.message_request_password);
        dialog.show();
        new SafeAsyncTask<Boolean>() {

            @Override
            public Boolean call() throws Exception {
                ApiResult result = null;

                MobiClubServiceApi service = serviceProvider.getService();
                if(service == null)
                    return false;
                result = service.requestPassword(email);
                if(result != null) {
                    message = result.getMessage();
                }
                if(result != null && result.isSuccess()) {
                    return true;
                }
                return false;
            }

            @Override
            protected void onException(Exception e) throws RuntimeException {
                super.onException(e);
                dialog.dismiss();
                Intent intent = null;
                if(e instanceof AppBlockedException) {
                    Ln.d("AppBlockedException");
                    intent = new Intent(LostPasswordActivity.this, AppInactiveActivity.class);
                } else if(e instanceof AppOutdatedException) {
                    Ln.d("AppOutdatedException");
                    intent = new Intent(LostPasswordActivity.this, OutdatedActivity.class);
                } else if(e instanceof UserBlockedException) {
                    Ln.d("UserBlockedException");
                    intent = new Intent(LostPasswordActivity.this, AccountBlockedActivity.class);
                }
                if(intent != null) {
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra(Constants.Extra.REASON_USER_BLOCKED, e.getMessage());
                    startActivity(intent);
                    LostPasswordActivity.this.finish();
                    return;
                }
                if(!(e instanceof RetrofitError)) {
                    AlertDialogFactory.createDefaultError(LostPasswordActivity.this,
                            R.string.dialog_title_error, R.string.server_connection_error).show();
                }
            }

            @Override
            protected void onSuccess(Boolean aBoolean) throws Exception {
                super.onSuccess(aBoolean);
                dialog.dismiss();
                if(aBoolean) {
                    AlertDialogFactory.createDefaultError(LostPasswordActivity.this,
                            R.string.lost_password_email_sent_title, R.string.lost_password_email_sent_msg).show();
                } else {
                    if(message != null) {
                        AlertDialogFactory.createDefaultError(LostPasswordActivity.this,
                                R.string.dialog_title_error, message).show();
                    } else {
                        AlertDialogFactory.createDefaultError(LostPasswordActivity.this,
                                R.string.dialog_title_error, R.string.server_connection_error).show();
                    }
                }
            }
        }.execute();
    }

}
