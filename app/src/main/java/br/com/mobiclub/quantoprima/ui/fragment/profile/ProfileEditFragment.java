package br.com.mobiclub.quantoprima.ui.fragment.profile;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import java.util.Date;

import javax.inject.Inject;

import br.com.mobiclub.quantoprima.R;
import br.com.mobiclub.quantoprima.config.Injector;
import br.com.mobiclub.quantoprima.core.service.MobiClubServiceApi;
import br.com.mobiclub.quantoprima.core.service.MobiClubServiceProvider;
import br.com.mobiclub.quantoprima.core.service.api.entity.ApiError;
import br.com.mobiclub.quantoprima.core.service.api.entity.ApiResult;
import br.com.mobiclub.quantoprima.core.service.api.entity.Image;
import br.com.mobiclub.quantoprima.core.service.api.exception.AppBlockedException;
import br.com.mobiclub.quantoprima.core.service.api.exception.AppOutdatedException;
import br.com.mobiclub.quantoprima.core.service.api.exception.UserBlockedException;
import br.com.mobiclub.quantoprima.domain.MobiClubUser;
import br.com.mobiclub.quantoprima.events.NetworkErrorEvent;
import br.com.mobiclub.quantoprima.events.RestErrorEvent;
import br.com.mobiclub.quantoprima.facade.Facade;
import br.com.mobiclub.quantoprima.ui.activity.AppInactiveActivity;
import br.com.mobiclub.quantoprima.ui.activity.account.AccountBlockedActivity;
import br.com.mobiclub.quantoprima.ui.activity.launch.OutdatedActivity;
import br.com.mobiclub.quantoprima.ui.factory.AlertDialogFactory;
import br.com.mobiclub.quantoprima.ui.factory.ProgressDialogFactory;
import br.com.mobiclub.quantoprima.ui.helper.SignupHelper;
import br.com.mobiclub.quantoprima.ui.view.ImageLoader;
import br.com.mobiclub.quantoprima.domain.Constants;
import br.com.mobiclub.quantoprima.util.Ln;
import br.com.mobiclub.quantoprima.util.Mask;
import br.com.mobiclub.quantoprima.util.SafeAsyncTask;
import br.com.mobiclub.quantoprima.util.Util;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import retrofit.RetrofitError;

/**
 * A simple {@link Fragment} subclass.
 *
 */
public class ProfileEditFragment extends Fragment {

    @InjectView(R.id.profile_image)
    ImageView profileImage;

    @InjectView(R.id.btn_change_password)
    TextView btnChangePassword;

    @InjectView(R.id.label_edit_password_message)
    TextView labelEditPasswordMessage;

    @Inject
    MobiClubServiceProvider serviceProvider;

    @InjectView(R.id.et_email)
    AutoCompleteTextView etEmail;

    @InjectView(R.id.et_name)
    EditText etName;

    @InjectView(R.id.et_birth)
    EditText etBirth;

    @InjectView(R.id.et_cpf)
    EditText etCpf;

    @InjectView(R.id.spinner)
    Spinner spinner;

    @Inject
    Bus bus;

    private SignupHelper helper;
    private String message;
    private ProfileEditFragmentListener listener;
    private ProgressDialog dialog;

    public ProfileEditFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Injector.inject(this);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            listener = (ProfileEditFragmentListener) activity;
        } catch (Exception e) {
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    @Override
    public void onPause() {
        super.onPause();
        bus.unregister(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        bus.register(this);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        helper = new SignupHelper(getActivity(), etEmail, null, etCpf, true);
    }

    private static void loadImage(Image image, ImageView imageItem, int placeholder) {
        ImageLoader.getInstance().loadImage(imageItem, image,
                placeholder);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_edit, container, false);

        ButterKnife.inject(this, view);

        String changePasswordMessage = getString(R.string.profile_edit_label_edit_password_message);
        Spanned text1 = Html.fromHtml(changePasswordMessage);
        labelEditPasswordMessage.setText(text1);

        String changePasswordString = getString(R.string.profile_edit_btn_edit_password);
        Spanned text2 = Html.fromHtml(changePasswordString);
        btnChangePassword.setText(text2);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.gender_values_for_editing, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        MobiClubUser loggedUser = Facade.getInstance().getLoggedUser();
        if(loggedUser != null) {
            String profilePicture = loggedUser.getProfilePicture();
            loadImage(new Image(profilePicture), profileImage, ImageLoader.Placeholder.USER_PHOTO);

            etName.setText(loggedUser.getName().toString());
            etEmail.setText(loggedUser.getEmail().toString());
            etBirth.setText(loggedUser.getBirthdayString().toString());

            Mask.insert("###.###.###-##", etCpf);
            etCpf.setText(loggedUser.getCpf().toString());

            String sexo = loggedUser.getGenderString();
            if(sexo.equals(MobiClubUser.GENDER_FEMALE_TYPE)) {
                spinner.setSelection(1);
            } else if(sexo.equals(MobiClubUser.GENDER_MALE_TYPE)) {
                spinner.setSelection(2);
            }else if(sexo.equals(MobiClubUser.GENDER_FEMALE_STRING)) {
                spinner.setSelection(1);
            } else if(sexo.equals(MobiClubUser.GENDER_MALE_STRING)) {
                spinner.setSelection(2);
            } else {
                spinner.setSelection(0);
            }

        }

        etCpf.addTextChangedListener(Mask.insert("###.###.###-##", etCpf));

        return view;
    }

    @OnClick(R.id.btn_update_profile)
    public void updateProfile() {
        Ln.d("updateProfile");
        if(getActivity() == null)
            return;

        dialog = ProgressDialogFactory.createProgress(getActivity(),
                R.string.message_edit_profile);
        dialog.show();

        new SafeAsyncTask<Boolean>() {

            @Override
            public Boolean call() throws Exception {
                MobiClubUser user = helper.getUser();
                if(user != null) {

                    MobiClubServiceApi service = serviceProvider.getService(getActivity());
//                    String birthdayString = user.getSignUptBirthdayString();
//                    String birthdayString = etBirth.getText().toString();

                    Date birthDate = Util.parseDate(etBirth.getText().toString());
                    String birthDateString = Util.getSignUpDateString(birthDate);

                    String sexo = spinner.getSelectedItem().toString();
                    String sexoType = "";
                    if(sexo.equals(MobiClubUser.GENDER_FEMALE_STRING)) {
                        sexoType = MobiClubUser.GENDER_FEMALE_TYPE;
                    } else if(sexo.equals(MobiClubUser.GENDER_MALE_STRING)) {
                        sexoType = MobiClubUser.GENDER_MALE_TYPE;
                    }

                    String cpfString = etCpf.getText().toString();

                    ApiResult result = service.editUser(etName.getText().toString(),
                            birthDateString, sexoType, cpfString);
                    message = result.getMessage();
                    return result.isSuccess();

                } else {
                    return false;
                }
            }

            @Override
            protected void onException(Exception e) throws RuntimeException {
                super.onException(e);
                if(!(e instanceof RetrofitError)) {
                    if(message == null)
                        message = getString(R.string.profile_edit_error);
                    AlertDialogFactory.createDefaultError(getActivity(), message).show();
                    dialog.dismiss();
                }
            }

            @Override
            protected void onSuccess(Boolean aBoolean) throws Exception {
                super.onSuccess(aBoolean);
                dialog.dismiss();
                if(aBoolean) {
                    // Edit Local
                    Facade facade = Facade.getInstance();
                    MobiClubUser loggedUser = facade.getLoggedUser();

                    loggedUser.setName(etName.getText().toString());
                    loggedUser.setEmail(etEmail.getText().toString());

                    Date birthDate = Util.parseDate(etBirth.getText().toString());
                    loggedUser.setBirthday(birthDate);

                    String cpfString = etCpf.getText().toString();
                    loggedUser.setCpf(cpfString);

                    String sexo = spinner.getSelectedItem().toString();
                    String sexoType = "";
                    if(sexo.equals(MobiClubUser.GENDER_FEMALE_STRING)) {
                        sexoType = MobiClubUser.GENDER_FEMALE_TYPE;
                    } else if(sexo.equals(MobiClubUser.GENDER_MALE_STRING)) {
                        sexoType = MobiClubUser.GENDER_MALE_TYPE;
                    }
                    loggedUser.setGenderType(sexoType);

                    facade.insertOrUpdateUser(loggedUser);
                    Facade.getInstance().setLoggedUser(loggedUser);

                    // Alert
                    AlertDialog.Builder builder = AlertDialogFactory.createDefaultBuilder(getActivity(),
                            R.string.alert_title_attention, R.string.alert_edit_profile_success);
                    builder.setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            listener.profileEditedSuccess();
                        }
                    }).setCancelable(false);
                    builder.create().show();
                } else if(helper.getUser() == null) {
                    String cause = helper.getValidationError();
                    AlertDialogFactory.createDefaultError(getActivity(),
                            R.string.signup_data_incomplete, cause).show();
                    Ln.d("Error ao criar usuario, Motivo: %s", cause);
                } else {
                    String cause = helper.getValidationError();
                    if (cause == null) {
                        cause = getString(R.string.msg_problem_editing_with_no_cause);
                    }
                    AlertDialogFactory.createDefaultError(getActivity(),
                            R.string.signup_data_incomplete, cause).show();
                }
            }
        }.execute();
    }

    @Subscribe
    public void onNetworkErrorEvent(NetworkErrorEvent e) {
        //nesse caso tenta offline
        AlertDialogFactory.createDefaultError(getActivity(),
                R.string.service_comunication_error).show();
        dialog.dismiss();
    }

    /**
     * Quando ocorre erro na chamada do servico
     *
     * @param e
     */
    @Subscribe
    public void onRestErrorEvent(RestErrorEvent e) {
        if (!e.isProfileEdit()) {
            return;
        }
        ApiError apiError = e.getApiError();
        String message = null;
        message = getString(R.string.profile_edit_error);
        if (apiError != null && apiError.getMessage() != null) {
            message = apiError.getMessage();
        }
        AlertDialogFactory.createDefaultError(getActivity(), message).show();
        dialog.dismiss();
    }

    @OnClick(R.id.btn_change_password)
    public void changePassword() {
        Ln.d("changePassword");
        if(getActivity() == null)
            return;

        final ProgressDialog dialog = ProgressDialogFactory.createProgress(getActivity(),
                R.string.message_request_password);
        dialog.show();

        new SafeAsyncTask<Boolean>() {

            @Override
            public Boolean call() throws Exception {
                MobiClubUser user = Facade.getInstance().getLoggedUser();
                if(user != null) {
                    MobiClubServiceApi service = serviceProvider.getService(getActivity());
                    ApiResult result = service.requestPassword(user.getEmail());
                    return result.isSuccess();
                } else {
                    return false;
                }
            }

            @Override
            protected void onException(Exception e) throws RuntimeException {
                super.onException(e);
                dialog.dismiss();
                Intent intent = null;
                if(e instanceof AppBlockedException) {
                    Ln.d("AppBlockedException");
                    intent = new Intent(getActivity(), AppInactiveActivity.class);
                } else if(e instanceof AppOutdatedException) {
                    Ln.d("AppOutdatedException");
                    intent = new Intent(getActivity(), OutdatedActivity.class);
                } else if(e instanceof UserBlockedException) {
                    Ln.d("UserBlockedException");
                    intent = new Intent(getActivity(), AccountBlockedActivity.class);
                }
                if(intent != null) {
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra(Constants.Extra.REASON_USER_BLOCKED, e.getMessage());
                    startActivity(intent);
                    getActivity().finish();
                    return;
                }
                if(!(e instanceof RetrofitError)) {
                    AlertDialogFactory.createDefaultError(getActivity(),
                            R.string.dialog_title_error, R.string.error_on_app).show();
                }
            }

            @Override
            protected void onSuccess(Boolean aBoolean) throws Exception {
                super.onSuccess(aBoolean);
                dialog.dismiss();
                if(aBoolean) {
                    AlertDialogFactory.createDefaultError(getActivity(),
                            R.string.lost_password_email_sent_title, R.string.lost_password_email_sent_msg).show();
                } else {
                    AlertDialogFactory.createDefaultError(getActivity(),
                            R.string.dialog_title_error, R.string.error_on_app).show();
                }
            }
        }.execute();

    }

}
