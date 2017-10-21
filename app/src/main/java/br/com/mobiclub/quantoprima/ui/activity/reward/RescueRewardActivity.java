package br.com.mobiclub.quantoprima.ui.activity.reward;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.squareup.otto.Subscribe;

import java.util.Date;

import br.com.mobiclub.quantoprima.R;
import br.com.mobiclub.quantoprima.core.service.MobiClubServiceApi;
import br.com.mobiclub.quantoprima.core.service.api.entity.ApiError;
import br.com.mobiclub.quantoprima.core.service.api.entity.ApiResult;
import br.com.mobiclub.quantoprima.core.service.api.entity.EstablishmentLocation;
import br.com.mobiclub.quantoprima.core.service.api.entity.Reward;
import br.com.mobiclub.quantoprima.events.NavItemSelectedEvent;
import br.com.mobiclub.quantoprima.events.NetworkErrorEvent;
import br.com.mobiclub.quantoprima.events.RestErrorEvent;
import br.com.mobiclub.quantoprima.facade.Facade;
import br.com.mobiclub.quantoprima.facebook.FacebookFacade;
import br.com.mobiclub.quantoprima.ui.activity.MenuActivity;
import br.com.mobiclub.quantoprima.ui.activity.qrcode.CaptureActivity;
import br.com.mobiclub.quantoprima.ui.adapter.DialogResultAdapter;
import br.com.mobiclub.quantoprima.ui.adapter.DialogResultAdapterImpl;
import br.com.mobiclub.quantoprima.ui.factory.AlertDialogFactory;
import br.com.mobiclub.quantoprima.ui.factory.ProgressDialogFactory;
import br.com.mobiclub.quantoprima.ui.fragment.reward.RescueRewardFragment;
import br.com.mobiclub.quantoprima.ui.view.DialogListener;
import br.com.mobiclub.quantoprima.ui.view.DialogResultData;
import br.com.mobiclub.quantoprima.ui.view.GainMobiDialogListener;
import br.com.mobiclub.quantoprima.ui.view.ResultDialog;
import br.com.mobiclub.quantoprima.domain.Constants;
import br.com.mobiclub.quantoprima.util.Ln;
import br.com.mobiclub.quantoprima.util.SafeAsyncTask;
import br.com.mobiclub.quantoprima.util.Util;
import retrofit.RetrofitError;

public class RescueRewardActivity extends MenuActivity implements GainMobiDialogListener {

    private static final int RESCUE_REWARD_REQUEST = 0;

    private Reward reward;
    private ResultDialog resultDialog;

    private DialogResultAdapter dialogResultAdapter =
            new DialogResultAdapterImpl(R.layout.dialog_reward_rescue_success,
                    R.layout.dialog_error) {
                @Override
                public void onCreateView(View view, DialogResultData data) {
                    Reward reward = (Reward) data;
                    if(isSuccess()) {
                        TextView labelCode = (TextView) view.findViewById(R.id.label_code);
                        String idToHexa = reward.getIdToHexa();
                        if(idToHexa != null)
                            labelCode.setText(idToHexa);

                        TextView labelTime = (TextView) view.findViewById(R.id.label_time);
                        Date time = reward.getTime();
                        if(time != null)
                            labelTime.setText(Util.getDateTimeString(time));

                        TextView labelMessage = (TextView) view.findViewById(R.id.label_user_attention_message);
                        String messageValue = getString(R.string.rescue_reward_label_user_attention_message, reward.getTitle());
                        labelMessage.setText(Html.fromHtml(messageValue));
                    } else if(isError()) {
                        //TODO:
                    }
                }
            };
    private Facade facade;
    private ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        resultDialog = new ResultDialog();

        facade = Facade.getInstance();
    }

    @Override
    protected Fragment createFragment() {
        Intent intent = getIntent();
        reward = Facade.getInstance().getReward();
        RescueRewardFragment rescueRewardFragment = new RescueRewardFragment();
        return rescueRewardFragment;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RESCUE_REWARD_REQUEST) {
            EstablishmentLocation location = new EstablishmentLocation();
            location.setId(reward.getLocationId());
            reward.setLocation(location);
            dialogResultAdapter.setData(reward);
            if(resultCode == Activity.RESULT_OK) {
                Bundle extras = null;
                String code = null;
                if(data != null) {
                    extras = data.getExtras();
                    if(extras != null) {
                        code = extras.getString(Constants.Extra.QR_CODE);
                        rescueReward(code);
                    }
                }
            }
            else if(resultCode == RESULT_CANCELED) {
                //onRescueRewardError();
            }
        }
    }

    private void showResultDialog(DialogResultAdapter dialogResultAdapter) {
        Bundle args = new Bundle();
        args.putSerializable(Constants.Extra.DIALOG_RESULTER, dialogResultAdapter);
        resultDialog.setArguments(args);
        resultDialog.show(getSupportFragmentManager(), "resultDialog");
    }

    private void rescueReward(final String qrCode) {
        progress = ProgressDialogFactory.createProgress(this, R.string.loading_message);
        progress.show();

        new SafeAsyncTask<ApiResult>() {

            @Override
            public ApiResult call() throws Exception {
                MobiClubServiceApi service = serviceProvider.getService(RescueRewardActivity.this);
                if(reward == null) {
                    return null;
                }

                int idToRescue = reward.getIdToRescue();

                ApiResult apiResult = null;
                if(reward.isRewardBuy())
                    apiResult = service.rescueRewardBuy(idToRescue, qrCode);
                else if(reward.isRewardShot()) {
                    apiResult = service.rescueRewardShot(idToRescue, qrCode);
                } else if(reward.isRewardGift()) {
                    apiResult = service.rescueRewardGift(idToRescue, qrCode);
                }

                return apiResult;
            }

            @Override
            protected void onException(Exception e) throws RuntimeException {
                super.onException(e);
                if(!(e instanceof RetrofitError)) {
                    Ln.e(e);
                    onRescueRewardError(getString(R.string.error_on_rescue_reward));
                }
                progress.dismiss();
            }

            @Override
            protected void onSuccess(ApiResult result) throws Exception {
                super.onSuccess(result);
                if(result != null && result.isSuccess())
                    onRescueRewardSuccess();
                else if(result != null) {
                    Ln.d("Result da resposta foi invalido");
                    String message = result.getMessage();
                    if(message == null) {
                        message = getString(R.string.error_on_rescue_reward);
                    }
                    onRescueRewardError(message);
                }
                progress.dismiss();
            }

        }.execute();
    }

    private void onRescueRewardError(String message) {
        dialogResultAdapter.setResult(DialogResultAdapter.RESULT_ERROR);
        dialogResultAdapter.setErrorMessage(message);
        showResultDialog(dialogResultAdapter);
    }

    private void onRescueRewardSuccess() {
        dialogResultAdapter.setResult(DialogResultAdapter.RESULT_SUCCESS);
        showResultDialog(dialogResultAdapter);
    }

    public void onRescue(View view) {
        Intent intent = new Intent(this, CaptureActivity.class);
        Facade.getInstance().setReward(reward);
        intent.putExtra(Constants.Extra.QRCODE_READ_TYPE, isConnected(true));
        startActivityForResult(intent, RESCUE_REWARD_REQUEST);
    }

    @Subscribe
    public void onNetworkErrorEvent(NetworkErrorEvent e) {
        //nesse caso tenta offline
        progress.dismiss();
        AlertDialogFactory.createDefaultError(this,
                R.string.service_comunication_error).show();
    }

    /**
     * Quando ocorre erro na chamada do servico
     *
     * @param e
     */
    @Subscribe
    public void onRestErrorEvent(RestErrorEvent e) {
        ApiError apiError = e.getApiError();
        String message = null;
        if (apiError != null)
            message = apiError.getMessage();
        onRescueRewardError(message);
    }

    @Override
    public void onCloseResult(int result) {
        if(result == DialogListener.RESULT_OK) {
            resultDialog.dismiss();
            //TODO: melhorar isso
            facade.removeLastIntent();
            facade.removeLastIntent();
            facade.removeLastIntent();
            navigateTo(RewardActivity.class);
        }
    }

    /**
     * Aqui por enquanto pois o otto nao consegue enchergar
     * a anotacao de subscribe em PrincipalActivity
     *
     * @param event
     */
    @Subscribe
    @Override
    public void onNavigationItemSelected(NavItemSelectedEvent event) {
        super.onNavigationItemSelectedImpl(event);
    }

    @Override
    public void onEvaluate(EstablishmentLocation location) {
        //nothing
    }

    @Override
    public void onShare(DialogResultData data) {
        Ln.d("onShare");
        //TODO: melhorar isso
        facade.removeLastIntent();
        facade.removeLastIntent();
        facade.removeLastIntent();
        super.setNextActivity(RewardActivity.class);

        Reward reward = Facade.getInstance().getReward();
        int postId = FacebookFacade.FacebookPostType.STORE_ITEM.getId();
        if(reward.isRewardBuy()) {
            postId = FacebookFacade.FacebookPostType.REWARD_BUY.getId();
        } else if(reward.isRewardGift()) {
            postId = FacebookFacade.FacebookPostType.REWARD_GIFT.getId();
        } else if(reward.isRewardShot()) {
            postId = FacebookFacade.FacebookPostType.REWARD_SHOT.getId();
        }

        super.facebookShare(data, postId);
    }

    @Override
    public void onGoToReward() {
        Ln.d("onGoToReward");
        navigateTo(RewardActivity.class);
    }

}
