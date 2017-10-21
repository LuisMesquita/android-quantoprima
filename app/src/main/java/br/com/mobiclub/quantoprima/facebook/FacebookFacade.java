package br.com.mobiclub.quantoprima.facebook;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.android.dialog.AsyncFacebookRunner;
import com.facebook.android.dialog.AsyncFacebookRunner.RequestListener;
import com.facebook.android.dialog.Facebook;
import com.facebook.android.dialog.Facebook.DialogListener;
import com.facebook.model.GraphObject;

import java.io.IOException;
import java.net.MalformedURLException;

import javax.inject.Inject;

import br.com.mobiclub.quantoprima.R;
import br.com.mobiclub.quantoprima.config.Injector;
import br.com.mobiclub.quantoprima.core.service.MobiClubServiceApi;
import br.com.mobiclub.quantoprima.core.service.MobiClubServiceProvider;
import br.com.mobiclub.quantoprima.core.service.api.entity.ApiResult;
import br.com.mobiclub.quantoprima.core.service.api.entity.EstablishmentLocation;
import br.com.mobiclub.quantoprima.domain.FacebookPost;
import br.com.mobiclub.quantoprima.domain.MobiClubUser;
import br.com.mobiclub.quantoprima.facade.Facade;
import br.com.mobiclub.quantoprima.ui.factory.AlertDialogFactory;
import br.com.mobiclub.quantoprima.ui.view.DialogResultData;
import br.com.mobiclub.quantoprima.util.Ln;
import br.com.mobiclub.quantoprima.util.SafeAsyncTask;
import retrofit.RetrofitError;

public class FacebookFacade {

    private static FacebookFacade instance;

    public static final String APP_ID = "753745628005524";
    private final Facade facade;

    private Facebook facebook;
    private AsyncFacebookRunner mAsyncRunner;
    private String[] permissions;

    private Activity activity;

    public enum FacebookPostType {
        STORE_ITEM(1), REWARD_BUY(2), REWARD_GIFT(3),
        REWARD_SHOT(4), MOBI(5), CHECKIN(6);

        private final int id;

        FacebookPostType(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }
    }

    @Inject
    MobiClubServiceProvider serviceProvider;

    private FacebookFacade () {
        this.facebook = new Facebook(APP_ID);
//        this.permissions = new String[] { "email", "offline_access", "user_birthday", "user_hometown", "user_location", "user_likes" };
        this.permissions = new String[] { "email", "user_birthday", "user_hometown", "user_location" };
        this.facade = Facade.getInstance();
        Injector.inject(this);
    }

    public static FacebookFacade getInstance() {
        if (instance == null) {
            instance = new FacebookFacade();
        }
        return instance;
    }

    public boolean isSessionValid() {
        return this.facebook.isSessionValid();
    }

    public void authorize(Activity activity, DialogListener dl){
        this.facebook.authorize(activity, this.permissions, Facebook.FORCE_DIALOG_AUTH, dl);
    }

    public void authorizeCallback(int requestCode, int resultCode, Intent data) {
        facebook.authorizeCallback(requestCode, resultCode, data);
    }

    public void requestUser(MeRequestListener me) {
        this.mAsyncRunner = new AsyncFacebookRunner(this.facebook);
        this.mAsyncRunner.request("me", me);
    }

    public boolean saveSession(Context context) {
        return SessionStore.save(facebook, context);
    }

    public void logout(Context context) {
        try {
            facebook.logout(context);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateFacebookAccess(Context context) {

        String accessToken = SessionStore.getAccessToken(context);
        long accessExpires = SessionStore.getAccessExpires(context);

        if (accessToken != null) {
            this.facebook.setAccessToken(accessToken);
        }

        if (accessExpires  != 0) {
            this.facebook.setAccessExpires(accessExpires);
        }
    }

    public void openPostDialog (Context context, FacebookPost shared,  PostRequestDialogListener postListenet) {
        Bundle parameteres = new Bundle();
        parameteres.putString("link", shared.getLink());
        parameteres.putString("picture", shared.getPicture());
        parameteres.putString("name", shared.getName());
        parameteres.putString("caption", shared.getCaption());
        parameteres.putString("description", shared.getDescription());
        parameteres.putString("message", shared.getMessage());

        this.facebook.dialog(context, "feed", parameteres, postListenet);
    }

    public void publishToFeed (Context context, final FacebookPost shared, final RequestListener postRequest) {
        this.mAsyncRunner = new AsyncFacebookRunner(this.facebook);
        this.mAsyncRunner.request("me/feed", shared.getParameters(), "POST", postRequest, null);
    }

    public void getFriendList(RequestListener friendRquest) {
        this.mAsyncRunner = new AsyncFacebookRunner(this.facebook);
        mAsyncRunner.request("me/friends", friendRquest);
    }

    public void shareOnWall(final Activity activity, final DialogResultData entity,
                            final String postId, final int postType) {
        final MobiClubUser loggedUser = facade.getLoggedUser();
        if(loggedUser == null && entity == null)
            return;

        this.activity = activity;

        new SafeAsyncTask<ApiResult>() {

            public String comment;

            @Override
            public ApiResult call() throws Exception {
                //get user message
                MobiClubServiceApi service = serviceProvider.getService(activity);
                comment = getPostMessage(postId);
                Long facebookId = loggedUser.getFacebookId();
                int type = postType;
                int reference = entity.getId();
                int estId = 0;
                EstablishmentLocation location = entity.getLocation();
                int locationId;
                if(location != null) {
                    locationId = location.getId();
                } else {
                    locationId = entity.getId(); //entity eh o proprio location
                }
                Ln.d("entity %s", entity);
                Ln.d("Chamando servico para compartilhar. " +
                                "mensagem: %s. posttype = %d. locationid = %d, reference = %d",
                        comment, postType, locationId, reference);
                ApiResult result = service.facebookPost(facebookId, type, comment,
                        locationId, reference, estId);
                return result;
            }

            @Override
            protected void onException(Exception e) throws RuntimeException {
                super.onException(e);
                if(!(e instanceof RetrofitError)) {
                    String message = activity.getResources().getString(R.string.post_facebook_error);
                    showPostError(message);
                }
            }

            @Override
            protected void onSuccess(ApiResult result) throws Exception {
                super.onSuccess(result);
                if(result != null && result.isSuccess()) {
                    Ln.d("Sucesso ao compartilhar no servico. Mensagem: %s.", comment);
                } else if(result != null && !result.isSuccess()) {
                    showPostError(result.getMessage());
                }
            }
        }.execute();
    }

    private String getPostMessage(String postId) {
        final String[] message = {""};
        try {
            new Request(
                    Session.getActiveSession(),
                    "/" + postId,
                    null,
                    HttpMethod.GET,
                    new Request.Callback() {
                        public void onCompleted(Response response) {
                        /* handle the result */
                            GraphObject graphObject = response.getGraphObject();
                            if(graphObject != null) {
                                Ln.d(graphObject);
                                Ln.d("messagem recuperada %s", graphObject.getProperty("message"));
                                message[0] = (String) graphObject.getProperty("message");
                            }
                        }
                    }
            ).executeAndWait();
        } catch (Exception e) {
            Ln.e(e);
        }
        return message[0];
    }

    private void showPostError(String message) {
        Ln.e("Nao foi possivel compartilhar no servico. Motivo: %s", message);
        AlertDialog defaultError = AlertDialogFactory.createDefaultError(activity, R.string.alert_title_attention, message);
        defaultError.show();
    }

    public String[] getPermissions() {
        return permissions;
    }
}