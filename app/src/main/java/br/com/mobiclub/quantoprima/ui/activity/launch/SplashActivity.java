package br.com.mobiclub.quantoprima.ui.activity.launch;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.onesignal.OneSignal;
import com.parse.ParsePush;

import org.json.JSONException;
import org.json.JSONObject;

import br.com.mobiclub.quantoprima.MobiClubApplication;
import br.com.mobiclub.quantoprima.R;
import br.com.mobiclub.quantoprima.domain.MobiClubUser;
import br.com.mobiclub.quantoprima.facade.Facade;
import br.com.mobiclub.quantoprima.ui.activity.FullScreenMobiClubActivity;
import br.com.mobiclub.quantoprima.ui.activity.HomeActivity;
import br.com.mobiclub.quantoprima.ui.activity.account.CadastrarCpfActivity;
import br.com.mobiclub.quantoprima.ui.activity.TutorialActivity;
import br.com.mobiclub.quantoprima.util.Ln;

public class SplashActivity extends FullScreenMobiClubActivity {
    //private ProgressBar progressBar;
    private int progress = 0;
    private TextView labelLoading;
    private TextView labelThings;
    private boolean doLoading = false;
    private int index = 0;
    private Facade facade;

    private Boolean needCPF = false;

    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);

        needCPF = getIntent().getBooleanExtra("needCPF", false);

        try {
            sendUserTags();
        } catch (Exception e) {
        }

        facade = Facade.getInstance();
        //this.progressBar = ((ProgressBar) findViewById(R.id.progress_bar));
        //this.progressBar.setProgress(this.progress);
    }

    @Override
    public int getLayout() {
        return R.layout.activity_splash;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        labelLoading = (TextView) findViewById(R.id.label_loading);
        labelThings = (TextView) findViewById(R.id.label_things);

//        new Loading().execute(new Void[0]);

        if(MobiClubApplication.statusHttp==412){
            startActivityForResult(new Intent(this, CadastrarCpfActivity.class), 1030);
        }else{
            new Loading().execute(new Void[0]);
        }
    }

    private void sendUserTags() throws JSONException {
        MobiClubUser user = Facade.getInstance().getLoggedUser();
        JSONObject tags = new JSONObject();
        tags.put("user_id", "q"+user.getUserId());
        tags.put("mobiclub", "quantoprima");
        tags.put("global", "global");
        OneSignal.sendTags(tags);
        Ln.d("Registrando nas tags %s", tags);
    }


    public class Loading extends AsyncTask<Void, Void, Boolean> {
        public Loading() {
        }

        protected Boolean doInBackground(Void[] paramArrayOfVoid) {
            //TODO: init database and app tasks
            final String[] messages = getResources().getStringArray(R.array.loading_stuffs);

            while (true) {
                if (SplashActivity.this.progress >= 100)
                    break;
                try {
                    //TODO: descomentar
                    Thread.sleep(600L);
                    //Thread.sleep(1L);
                    SplashActivity localActivitySplash = SplashActivity.this;
                    localActivitySplash.progress = (10 + localActivitySplash.progress);

                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            if(index >= messages.length) {
                                index = 0;
                            }
                            labelThings.setText(messages[index++]);
                        }

                    });

                    //SplashActivity.this.progressBar.setProgress(SplashActivity.this.progress);
                } catch (InterruptedException localInterruptedException) {
                    localInterruptedException.printStackTrace();
                }
            }
            return true;
        }

        protected void onPostExecute(Boolean success) {
            //TODO: checar se conta bloqueada
            startApplication();
            //close this
            finish();
        }

        protected void onPreExecute() {
            labelLoading.setVisibility(View.VISIBLE);
            labelThings.setVisibility(View.VISIBLE);
        }
    }

    private void startApplication() {
        MobiClubUser loggedUser = facade.getLoggedUser();
        if(loggedUser != null) {
            String userId = String.valueOf(loggedUser.getUserId());
            String pushId = String.format("s%s", userId);
            Ln.d("Registrando canal %s", pushId);
            try {
                ParsePush.subscribeInBackground(pushId);
            } catch (Exception e) {
            }
        }

        if (needCPF) {
            Intent localIntent = new Intent(this, CadastrarCpfActivity.class);
            startActivityForResult(localIntent, 20000);
        } else {
            if(MobiClubApplication.getInstance().isFirstTime()) {
                Intent localIntent = new Intent(this, TutorialActivity.class);
                startActivity(localIntent);
            } else {
                Intent localIntent = new Intent(this, HomeActivity.class);
                startActivity(localIntent);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode==10000){
            new Loading().execute(new Void[0]);
        }

        if (resultCode==20000){
            if(MobiClubApplication.getInstance().isFirstTime()) {
                Intent localIntent = new Intent(this, TutorialActivity.class);
                startActivity(localIntent);
            } else {
                Intent localIntent = new Intent(this, HomeActivity.class);
                startActivity(localIntent);
            }
        }
    }

}
