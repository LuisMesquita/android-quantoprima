package br.com.mobiclub.quantoprima.ui.fragment.profile;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.facebook.Session;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;

import br.com.mobiclub.quantoprima.R;
import br.com.mobiclub.quantoprima.core.service.api.entity.Image;
import br.com.mobiclub.quantoprima.domain.MobiClubUser;
import br.com.mobiclub.quantoprima.facade.Facade;
import br.com.mobiclub.quantoprima.facebook.FacebookFacade;
import br.com.mobiclub.quantoprima.ui.activity.account.SigninFacebookHelper;
import br.com.mobiclub.quantoprima.ui.view.ImageLoader;
import br.com.mobiclub.quantoprima.util.Ln;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 *
 */
public class ProfileFragment extends Fragment {

    @InjectView(R.id.label_name)
    TextView labelName;

    @InjectView(R.id.label_email)
    TextView labelEmail;

    @InjectView(R.id.label_birth)
    TextView labelBirth;

    @InjectView(R.id.label_gender)
    TextView labelGender;

    @InjectView(R.id.label_facebook_email)
    TextView labelFacebookEmail;

    @InjectView(R.id.btn_facebook_connect)
    LoginButton btnFacebookConnect;

    @InjectView(R.id.btn_facebook_disconnect)
    ImageButton btnFacebookDiconnect;

    @InjectView(R.id.lyt_facebook_info)
    LinearLayout lytFacebookInfo;

    @InjectView(R.id.profile_image)
    ImageView profileImage;

    @InjectView(R.id.row_birthday)
    TableRow rowBirthday;

    @InjectView(R.id.label_cpf)
    TextView labelCpf;

    private Facade facade;
    private FacebookFacade facebook;
    private ProgressDialog progress;
    private boolean connected;
    private ProfileFragmentListener listener;
    private SigninFacebookHelper facebookHelper;

    public ProfileFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        facade = Facade.getInstance();
        facebook = FacebookFacade.getInstance();

        facebookHelper = new SigninFacebookHelper();
        facebookHelper.onCreate(getActivity(), savedInstanceState);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            listener = (ProfileFragmentListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("ProfileActivity deve implementar ProfileFragmentListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        Ln.d("onCreateView");
        ButterKnife.inject(this, view);
        MobiClubUser loggedUser = facade.getLoggedUser();
        if(loggedUser != null) {
            String fullName = loggedUser.getFullName();
            if(fullName != null) {
                labelName.setText(fullName);
            }

            if(loggedUser.getBirthday() == null) {
                rowBirthday.setVisibility(View.GONE);
            }

            String email = loggedUser.getEmail();
            if(email != null) {
                labelEmail.setText(email);
            }

            String birthday = loggedUser.getBirthdayString();
            if(birthday != null) {
                labelBirth.setText(birthday);
            }

            String gender = loggedUser.getGenderString();
            if(gender != null) {
                labelGender.setText(gender);
            } else {
                labelGender.setText(getString(R.string.unknown));
            }

            String cpf = loggedUser.getCpf();
            if(cpf != null) {
                labelCpf.setText(cpf);
                labelCpf.setTextColor(Color.parseColor("#ff0000"));
            } else {
                labelCpf.setText(getString(R.string.label_cpf_nao_cadastrado));
                labelCpf.setTextColor(Color.parseColor("#ffffff"));
            }


            if(loggedUser.isConnectedToFacebook()) {
                viewConnected(loggedUser);
            } else {
                Ln.d("Usuario não esta conectado pelo face.");
                viewDisconnected(loggedUser);
            }
        }

        btnFacebookConnect.setLogoutText("");
        btnFacebookConnect.setLoginText(getResources().getString(R.string.profile_facebook_btn_connect));
        btnFacebookConnect.setReadPermissions(facebook.getPermissions());

        Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/bold.ttf");
        btnFacebookConnect.setTypeface(font);

        btnFacebookConnect.setUserInfoChangedCallback(new LoginButton.UserInfoChangedCallback() {
            @Override
            public void onUserInfoFetched(GraphUser user) {
                Ln.d("onUserInfoFetched");
                if(user != null) {
                    try {
                        MobiClubUser loggedUser = facebookHelper.connect(user);
                        updateUI(loggedUser, true);
                    } catch (Exception e) {

                    }
                }
            }
        });

        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    @OnClick(R.id.btn_conf_notifications)
    public void onConfigNotifications() {
        if(listener != null)
            listener.onConfigNotifications();
    }

    @OnClick(R.id.btn_facebook_disconnect)
    public void onFacebookDisconnect() {
        Ln.d("Desconectando...");
        facebookDisconnect();
    }

    private void facebookDisconnect() {
        MobiClubUser loggedUser = facade.getLoggedUser();
        if(loggedUser != null) {
            loggedUser.setFacebookId(null);
            loggedUser.setFacebookAccessExpires(null);
            loggedUser.setFacebookAccessToken(null);
            loggedUser.setProfilePicture(null);
            facade.insertOrUpdateUser(loggedUser);
            final Session session = Session.getActiveSession();
            session.closeAndClearTokenInformation();
            updateUI(loggedUser, false);
        }
    }

    private void updateUI(final MobiClubUser loggedUser, final boolean connect) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                labelFacebookEmail.setVisibility(View.VISIBLE);
                labelFacebookEmail.setText(loggedUser.getEmail());
                //labelBirth.setText(loggedUser.getBirthdayString());
                //labelEmail.setText(loggedUser.getEmail());
                //labelName.setText(loggedUser.getFullName());
                //labelGender.setText(loggedUser.getGenderString());
                if(connect) {
                    viewConnected(loggedUser);
                    hideProgress();
                } else {
                    viewDisconnected(loggedUser);
                }
            }
        });
    }

    private void hideProgress() {
        if(progress != null)
            progress.dismiss();
    }

    private static void loadImage(Image image, ImageView imageItem, int placeholder) {
        ImageLoader.getInstance().loadImage(imageItem, image,
                placeholder);
    }

    private void viewDisconnected(MobiClubUser loggedUser) {
        connected = false;

        String profilePicture = loggedUser.getProfilePicture();
        if (profilePicture != null) {
            loadImage(new Image(profilePicture), profileImage, ImageLoader.Placeholder.USER_PHOTO);
        } else {
            loggedUser.setProfilePicture(null);
            profileImage.setImageResource(R.drawable.foto_usuario_placeholder);
        }

        btnFacebookConnect.setVisibility(View.VISIBLE);
        btnFacebookDiconnect.setVisibility(View.GONE);
        labelFacebookEmail.setVisibility(View.GONE);
    }

    private void viewConnected(MobiClubUser loggedUser) {
        String profilePicture = loggedUser.getProfilePicture();
        loadImage(new Image(profilePicture), profileImage, ImageLoader.Placeholder.USER_PHOTO);
        Ln.d("Usuario esta conectado pelo face.");
        //loggedUser.setProfilePicture(MobiClubUser.FACEBOOK_PROFILE_PHOTO);
        labelFacebookEmail.setVisibility(View.VISIBLE);
        btnFacebookConnect.setVisibility(View.GONE);
        btnFacebookDiconnect.setVisibility(View.VISIBLE);
        labelFacebookEmail.setText(loggedUser.getFacebokEmail());
        connected = true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        facebookHelper.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onResume() {
        super.onResume();
        facebookHelper.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        facebookHelper.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        facebookHelper.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        facebookHelper.onSaveInstanceState(outState);
    }

}
