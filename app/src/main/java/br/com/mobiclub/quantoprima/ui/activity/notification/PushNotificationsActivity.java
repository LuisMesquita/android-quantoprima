package br.com.mobiclub.quantoprima.ui.activity.notification;

import android.app.ProgressDialog;
import android.content.Intent;
import android.util.Log;

import com.google.gson.Gson;
import com.onesignal.OSNotificationAction;
import com.onesignal.OSNotificationOpenResult;
import com.onesignal.OneSignal;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.inject.Inject;

import br.com.mobiclub.quantoprima.MobiClubApplication;
import br.com.mobiclub.quantoprima.R;
import br.com.mobiclub.quantoprima.core.service.MobiClubServiceApi;
import br.com.mobiclub.quantoprima.core.service.MobiClubServiceProvider;
import br.com.mobiclub.quantoprima.core.service.api.entity.Gift;
import br.com.mobiclub.quantoprima.core.service.api.entity.Notification;
import br.com.mobiclub.quantoprima.core.service.api.entity.PushNotification;
import br.com.mobiclub.quantoprima.core.service.api.entity.PushNotificationGift;
import br.com.mobiclub.quantoprima.facade.Facade;
import br.com.mobiclub.quantoprima.ui.activity.FullScreenMobiClubActivity;
import br.com.mobiclub.quantoprima.ui.activity.reward.RewardActivity;
import br.com.mobiclub.quantoprima.domain.Constants;
import br.com.mobiclub.quantoprima.util.Ln;
import br.com.mobiclub.quantoprima.util.SafeAsyncTask;
import retrofit.RetrofitError;

/**
 *
 */
public class PushNotificationsActivity extends FullScreenMobiClubActivity implements OneSignal.NotificationOpenedHandler {
    @Inject
    MobiClubServiceProvider serviceProvider;
    private ProgressDialog progress;
    // This fires when a notification is opened by tapping on it.
    @Override
    public void notificationOpened(OSNotificationOpenResult result) {
        OSNotificationAction.ActionType actionType = result.action.type;
        JSONObject data = result.notification.payload.additionalData;
        String customKey;
        Log.i("OneSignalExample", "customkey set with value: ");

        Intent intent = getIntent();
        if(intent != null)
            printIntent(intent);

        if (data != null) {
//            String json = intent.getExtras().getString("com.parse.Data");
            customKey = data.optString("type", null);
            Facade facade = Facade.getInstance();
            Gson gson = facade.getGSON();
            JSONObject jsonObject = data;

            if (customKey != null)
                Log.i("OneSignalExample", "customkey set with value: " + data);

            if(customKey.equals("gift")){
                PushNotification pushNotification = gson.fromJson(String.valueOf(jsonObject), PushNotification.class);
                PushNotificationGift gift = gson.fromJson(String.valueOf(jsonObject), PushNotificationGift.class);
//                startHomeActivity(gift, null);
            }else if(customKey.equals("msg")){
                Log.i("OneSignalExample", "customkey set with value: " + data);
                startNotificacaoActivity(jsonObject);
            }else{
//                startHomeActivity(null, null);
            }
        }

        if (actionType == OSNotificationAction.ActionType.ActionTaken)
            Log.i("OneSignalExample", "Button pressed with id: " + result.action.actionID);
//
//         Intent intent = new Intent(Androi, NotificationItemActivity.class);
//         intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
//         startActivity(intent);

    }

    @Override
    public int getLayout() {
        return 0;
    }

    private void startHomeActivity(PushNotificationGift gift, String errorCause) {
//        Intent mainAppIntent = new Intent(this, HomeActivity.class);
//        ResultDialog gainGiftDialog = new ResultDialog();
//        gainGiftDialog.show(getSupportFragmentManager(), "resultDialog");
        MobiClubApplication.pushNotificationGift = gift;


        Intent mainAppIntent = new Intent(this, RewardActivity.class);
        mainAppIntent.putExtras(getIntent().getExtras());
        mainAppIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        Ln.d("Colocando presente %s", gift);
        if(gift != null) {
            mainAppIntent.putExtra(Constants.Extra.HAS_GIFT, true);
            mainAppIntent.putExtra("gift", true);
        } else {
            mainAppIntent.putExtra(Constants.Extra.HAS_GIFT, false);
        }

        if(errorCause != null) {
            mainAppIntent.putExtra(Constants.Extra.TAKE_GIFT_ERROR, errorCause);
        }
        progress.dismiss();
        startActivity(mainAppIntent);
        finish();

    }

    private void startResgate(JSONObject json) {
        Facade facade = Facade.getInstance();
        Gson gson = facade.getGSON();
        Notification notification = null;
        try {
            notification = gson.fromJson(json.getJSONObject("Notify").toString(), Notification.class);
            Intent it = new Intent(PushNotificationsActivity.this, NotificationItemActivity.class);
            it.putExtra(Constants.Extra.NOTIFICATION, notification);
            startActivity(it);
            progress.dismiss();
            finish();
        } catch (JSONException e) {
            //e.printStackTrace();
        }

    }

    private void startNotificacaoActivity(JSONObject json) {
        Facade facade = Facade.getInstance();
        Notification notification = null;
        try {
            JSONObject notify = new JSONObject(json.getString("Notify"));

            notification = new Notification();
            notification.setId(notify.getInt("Id"));
            notification.setName(notify.getString("Name").toString());
            notification.setReference(notify.getString("Reference").toString());
            notification.setText(notify.getString("Text").toString());
            notification.setTitle(notify.getString("Title").toString());

            String dt = notify.getString("SendAt").toString();

            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
            Date date = format.parse(dt);
            System.out.println(date);
            notification.setSendAt(date);

            notification.setSendAt(date);

//            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
//            notification = gson.fromJson(notify.toString(), Notification.class);

            Intent intent = new Intent(getApplicationContext(), NotificationItemActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(Constants.Extra.NOTIFICATION, notification);
            startActivity(intent);

            Log.i("notification", "dale " + notification);
            progress.dismiss();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // The following can be used to open an Activity of your choice.
        finish();

    }




    private void takeGift(final Integer id, final Integer giftId) {
        new SafeAsyncTask<Gift>() {

            @Override
            public Gift call() throws Exception {
                MobiClubServiceApi service = serviceProvider.getService(PushNotificationsActivity.this);
                Gift gift = service.getGiftById(giftId);
                gift.setGiftId(giftId); //dont trust
                return gift;
            }

            @Override
            protected void onException(Exception e) throws RuntimeException {
                super.onException(e);
                if(!(e instanceof RetrofitError)) {
                    Ln.d(e);
                    onTakenGiftError(getString(R.string.take_gift_error));
                }
            }

            @Override
            protected void onSuccess(Gift gift) throws Exception {
                super.onSuccess(gift);
                onTakenGiftSuccess(gift);
            }
        }.execute();
    }

    private void onTakenGiftError(String message) {
        startHomeActivity(null, message);
    }

    private void onTakenGiftSuccess(Gift gift) {
//        startHomeActivity(gift, null);
    }

    public static void printIntent(Intent intent) {
        try {
            Ln.d("action = ", intent.getAction());
            if (intent.getExtras() != null) {
                String channel = intent.getExtras().getString("com.parse.Channel");
                JSONObject json = new JSONObject(intent.getExtras().getString("com.parse.Data"));

                Ln.d("==============================================");
                Ln.d(String.format("channel:    %s", channel));
                Ln.d(String.format("json:       %s", json));
                Ln.d("==============================================");
            }
        } catch (Exception e) {
        }
    }

}
