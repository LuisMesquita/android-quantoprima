package br.com.mobiclub.quantoprima.ui.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.Serializable;

import br.com.mobiclub.quantoprima.R;
import br.com.mobiclub.quantoprima.core.service.api.entity.EstablishmentLocation;
import br.com.mobiclub.quantoprima.core.service.api.entity.GainMobiResponse;
import br.com.mobiclub.quantoprima.domain.MobiClubUser;
import br.com.mobiclub.quantoprima.facade.Facade;
import br.com.mobiclub.quantoprima.ui.adapter.DialogResultAdapter;
import br.com.mobiclub.quantoprima.ui.factory.AlertDialogFactory;
import br.com.mobiclub.quantoprima.domain.Constants;
import br.com.mobiclub.quantoprima.util.Ln;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.Optional;

public class ResultDialog extends DialogFragment
        implements View.OnClickListener, Serializable {

    private int layoutSuccess;

    private int layoutError;

    private DialogResultAdapter dialogResultAdapter;

    @InjectView(R.id.label_congrat_user_message)
    @Optional
    TextView labelCongratUserMessage;

    @InjectView(R.id.label_congrat_user)
    @Optional
    TextView labelCongratUser;

    @InjectView(R.id.label_total_mobis)
    @Optional
    TextView labelTotalMobis;

    @InjectView(R.id.labelOff)
    @Optional
    TextView labelOff;

    @InjectView(R.id.label_error)
    @Optional
    TextView labelError;

    @InjectView(R.id.label_ops_message)
    @Optional
    TextView labelOpsMessage;

    @InjectView(R.id.label_error_message)
    @Optional
    TextView labelErrorMessage;

    @InjectView(R.id.button_central)
    @Optional
    Button btnCentral;

    @InjectView(R.id.btn_share)
    @Optional
    Button btnShare;

    @InjectView(R.id.btn_evaluate)
    @Optional
    Button btnEvaluate;

    @InjectView(R.id.btn_accept)
    @Optional
    Button btnAcceptGift;

    @InjectView(R.id.btn_reject)
    @Optional
    Button btnRejectGift;

    @InjectView(R.id.btn_reward)
    @Optional
    Button btnReward;

    public GainMobiResponse gainMobiResponse = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.Theme_Mobiclub_Dialog);

        Bundle args = getArguments();
        if(args != null) {
            dialogResultAdapter = (DialogResultAdapter) args.getSerializable(Constants.Extra.DIALOG_RESULTER);
        }
        //TODO: tratar quando nao passar o onGainMobiSuccess resulter
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(dialogResultAdapter == null)
            return null;
        int layout = layoutError;
        if(dialogResultAdapter.isSuccess())
            layout = dialogResultAdapter.getSuccessLayout();
        else if(dialogResultAdapter.isError())
            layout = dialogResultAdapter.getErrorLayout();

        View view = inflater.inflate(layout, container, false);

        ButterKnife.inject(this, view);

        ImageButton imageClose = (ImageButton) view.findViewById(R.id.image_close);
        imageClose.setOnClickListener(this);

        if(dialogResultAdapter.isError()) {
            String message = dialogResultAdapter.getErrorMessage();
            if(message == null) {
                message = getString(R.string.error_unknown);
            }
            if(btnCentral != null)
                btnCentral.setOnClickListener(this);
            labelErrorMessage.setText(message);

            if (gainMobiResponse != null){

//            labelOpsMessage.setText(gainMobiResponse.getTitleBody());
//            labelError.setText(gainMobiResponse.getMessageHead());
//            labelErrorMessage.setText(gainMobiResponse.getMessageBody());
                //  try {
//                labelTotalMobis.setText(gainMobiResponse.getTitleHead().toString());

                String msgBody = gainMobiResponse.getMessageBody();
                String titleBody = gainMobiResponse.getTitleBody();
                String msgHead = gainMobiResponse.getMessageHead();
                String titleHead = gainMobiResponse.getTitleHead();
                labelErrorMessage.setText(msgBody);
                labelOpsMessage.setText(titleBody);
                labelError.setText(msgHead);
                if (msgHead!=null){
                    // labelCongratUser.setText(msgHead);
                }

                labelOff.setVisibility(View.GONE);
                labelTotalMobis.setText(titleHead);

//            }catch (Exception e){

                //          }
            }
        } else {
            if(btnShare != null)
                btnShare.setOnClickListener(this);
            if(btnEvaluate != null)
                btnEvaluate.setOnClickListener(this);
            if(btnAcceptGift != null)
                btnAcceptGift.setOnClickListener(this);
            if(btnRejectGift != null)
                btnRejectGift.setOnClickListener(this);
            if(btnReward != null)
                btnReward.setOnClickListener(this);


            if (gainMobiResponse != null) {


                String msgBody = gainMobiResponse.getMessageBody();
                String titleBody = gainMobiResponse.getTitleBody();
                String msgHead = gainMobiResponse.getMessageHead();
                String titleHead = gainMobiResponse.getTitleHead();

                if(!titleHead.equalsIgnoreCase("")){
                    labelTotalMobis.setText(titleHead);
                }else{
                    if(labelOff!=null) {
                        labelOff.setVisibility(View.VISIBLE);
                    }
                }
                if(labelCongratUserMessage!=null) {
                    labelCongratUserMessage.setText(msgBody);
//                labelOpsMessage.setText(titleBody);
                }
                if(labelCongratUser!=null) {
                    labelCongratUser.setText(msgHead);
                }
                if (gainMobiResponse.getEstablishment()!=null){
                    if (gainMobiResponse.getEstablishment().getLocation().getSurvey()==0){
                        btnEvaluate.setVisibility(View.GONE);
                    }
                }


            }

            dialogResultAdapter.onCreateView(view, dialogResultAdapter.getData());
        }
        if (gainMobiResponse != null) {
            if (gainMobiResponse.getHttpStatus() == 1000) {
                labelOff.setVisibility(View.VISIBLE);
            } else if (dialogResultAdapter.isSuccess()) {
                if(labelOff!=null) {
                    labelOff.setVisibility(View.GONE);
                }
            }

        }



        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.image_close:
                onClose(v);
                break;
            case R.id.btn_share:
                onShare(v);
                break;
            case R.id.btn_evaluate:
                onEvaluate(v);
                break;
            case R.id.button_central:
                onCentral(v);
                break;
            case R.id.btn_accept:
                onAcceptGift();
                break;
            case R.id.btn_reject:
                onRejectGift();
                break;
            case R.id.btn_reward:
                onGoToReward();
                break;
        }
    }

    private void onRejectGift() {
        try {
            GainGiftDialogListener listener = (GainGiftDialogListener) getActivity();
            listener.onRejectGift();
        } catch (Exception e) {
        }
        this.dismiss();
    }

    private void onAcceptGift() {
        try {
            GainGiftDialogListener listener = (GainGiftDialogListener) getActivity();
            listener.onAcceptGift();
        } catch (Exception e) {
        }
        this.dismiss();
    }

    private void onGoToReward() {
        try {
            GainMobiDialogListener listener = (GainMobiDialogListener) getActivity();
            listener.onGoToReward();
        } catch (Exception e) {
            Ln.e(e);
        }
        this.dismiss();
    }

    private void onCentral(View v) {
        try {
            ErrorDialogListener listener = (ErrorDialogListener) getActivity();
            listener.onCentral();
        } catch (Exception e) {
            Ln.e(e);
        }
        this.dismiss();
    }

    private void onEvaluate(View v) {
        try {
            GainMobiDialogListener listener = (GainMobiDialogListener) getActivity();
            DialogResultData data = dialogResultAdapter.getData();
            EstablishmentLocation location = data.getLocation();
            listener.onEvaluate(location);
        } catch (Exception e) {
            Ln.e(e);
        }
        this.dismiss();
    }

    private void onShare(View v) {
        try {
            Ln.d("Preparando para postar no facebook.");
            GainMobiDialogListener listener = (GainMobiDialogListener) getActivity();
            DialogResultData data = dialogResultAdapter.getData();
            EstablishmentLocation location = null;
            if(data != null) {
                location = data.getLocation();
                Ln.d("Postagem no facebook para %s.", location);
            } else {
                Ln.e("Nao foi encontrar location para postar");
                return;
            }
            MobiClubUser loggedUser = Facade.getInstance().getLoggedUser();
            if(!loggedUser.isConnectedToFacebook()) {
                AlertDialog defaultError = AlertDialogFactory.createDefaultError(getActivity(),
                        R.string.alert_title_attention,
                        R.string.facebook_connect_message);
                defaultError.show();
                return;
            }
            listener.onShare(data);
        } catch (Exception e) {
            Ln.w(e);
        }
        this.dismiss();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        onCreate(null);
    }

    public void onClose(View view) {
        try {
            DialogListener listener = (DialogListener) getActivity();
            if(dialogResultAdapter.isSuccess())
                listener.onCloseResult(DialogListener.RESULT_OK);
            else
                listener.onCloseResult(DialogListener.RESULT_ERROR);
        } catch (Exception e) {
        }
        this.dismiss();
    }

}
