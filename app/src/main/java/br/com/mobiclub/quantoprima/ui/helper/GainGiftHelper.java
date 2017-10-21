package br.com.mobiclub.quantoprima.ui.helper;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import java.io.Serializable;
import java.util.Date;

import br.com.mobiclub.quantoprima.R;
import br.com.mobiclub.quantoprima.core.service.MobiClubServiceApi;
import br.com.mobiclub.quantoprima.core.service.MobiClubServiceProvider;
import br.com.mobiclub.quantoprima.core.service.api.entity.ApiResult;
import br.com.mobiclub.quantoprima.core.service.api.entity.Gift;
import br.com.mobiclub.quantoprima.core.service.api.entity.Reward;
import br.com.mobiclub.quantoprima.facade.Facade;
import br.com.mobiclub.quantoprima.ui.activity.MenuActivity;
import br.com.mobiclub.quantoprima.ui.activity.reward.RewardActivity;
import br.com.mobiclub.quantoprima.ui.activity.reward.RewardDetailActivity;
import br.com.mobiclub.quantoprima.ui.adapter.DialogResultAdapter;
import br.com.mobiclub.quantoprima.ui.factory.AlertDialogFactory;
import br.com.mobiclub.quantoprima.ui.factory.DialogResultFactory;
import br.com.mobiclub.quantoprima.ui.view.ResultDialog;
import br.com.mobiclub.quantoprima.domain.Constants;
import br.com.mobiclub.quantoprima.util.Ln;
import br.com.mobiclub.quantoprima.util.SafeAsyncTask;
import br.com.mobiclub.quantoprima.util.Util;
import retrofit.RetrofitError;

/**
 */
public class GainGiftHelper implements Serializable {

    private final MobiClubServiceProvider serviceProvider;
    private Gift gift = null;
    private ResultDialog gainGiftDialog;
    private MenuActivity activity;

    public GainGiftHelper(MenuActivity activity, MobiClubServiceProvider serviceProvider) {
        this.serviceProvider = serviceProvider;
        this.activity = activity;
    }

    public void showGainGift(Gift gift) {
        this.gift = gift;
        if(gainGiftDialog == null) {
            gainGiftDialog = createGainGiftDialog();
        }
        setDataToGainGiftDialog(gift, gainGiftDialog);
        gainGiftDialog.show(activity.getSupportFragmentManager(), "resultDialog");
    }

    public void onRejectGift() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage(R.string.gain_gift_confirm_reject_message).setCancelable(false).
                setPositiveButton(R.string.button_yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        onGiftAction(gift, false);
                    }
                }).setNegativeButton(R.string.button_no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                showGainGift(gift);
            }
        });
        builder.create().show();
    }

    public void onAcceptGift() {
        onGiftAction(gift, true);
    }

    private ResultDialog createGainGiftDialog() {
        ResultDialog gainGiftDialog = new ResultDialog();
        return gainGiftDialog;
    }

    private void setDataToGainGiftDialog(Gift gift, ResultDialog gainGiftDialog) {
        Date expirationDate = Util.paserTPatternDate(gift.getExpiration());
        String expirationDateString = Util.getDateString(expirationDate);
        String giftExpiration = activity.getString(R.string.dialog_gain_gift_label_valid,
                expirationDateString);
        gift.setExpirationString(giftExpiration);
        DialogResultAdapter gainGiftSuccess = DialogResultFactory.createGainGiftSuccess(gift);
        Bundle args = new Bundle();
        args.putSerializable(Constants.Extra.DIALOG_RESULTER, gainGiftSuccess);
        gainGiftDialog.setArguments(args);
    }

    private void onGiftAction(final Gift gift, final boolean accept) {
        new SafeAsyncTask<ApiResult>() {

            @Override
            public ApiResult call() throws Exception {
                MobiClubServiceApi service = serviceProvider.getService(activity);
                ApiResult result;
                if(accept) {
                    Ln.d("Aceitando presente %s", gift.getGiftId());
                    Reward reward = gift.getReward();
                    if(reward != null)
                        reward.setResponsed(true);
                    result = service.rewardGiftAccept(gift.getGiftId());
                } else {
                    Ln.d("Recusando presente %s", gift.getGiftId());
                    result = service.rewardGiftReject(gift.getGiftId());
                }
                return result;
            }

            @Override
            protected void onException(Exception e) throws RuntimeException {
                super.onException(e);
                if(!(e instanceof RetrofitError)) {
                    final Throwable cause = e.getCause() != null ? e.getCause() : e;
                    if(cause != null) {
                        onGiftAcceptError(activity.getString(R.string.accept_gift_error), null);
                    }
                }
            }

            @Override
            protected void onSuccess(ApiResult apiResult) throws Exception {
                super.onSuccess(apiResult);
                if(apiResult != null && apiResult.isSuccess()) {
                    onGiftAcceptSuccess(accept);
                } else if(apiResult != null) {
                    onGiftAcceptError(activity.getString(R.string.accept_gift_error), apiResult.getMessage());
                }
            }

        }.execute();
    }

    private void onGiftAcceptError(String title, String message) {
        gift = null;
        activity.getIntent().putExtra(Constants.Extra.HAS_GIFT, false);
        Intent intent = activity.getIntent();
        intent.putExtra(Constants.Extra.GIFT, gift);
        gainGiftDialog.dismiss();
        AlertDialogFactory.createDefaultError(activity,
                title, message).show();
    }

    private void onGiftAcceptSuccess(boolean accept) {
        gainGiftDialog.dismiss();
        if(accept) {
            if(gift.getReward() == null) {
                activity.cleanGift();
                activity.navigateTo(RewardActivity.class);
                //activity.finish();
            } else {
                Intent intentReward = new Intent(activity, RewardDetailActivity.class);
                Facade.getInstance().setReward(gift.getReward());
                activity.navigateTo(intentReward);
            }
        } else {
            activity.navigateTo(RewardActivity.class);
            Facade.getInstance().removeLastIntent();
            //activity.finish();
        }
        gift = null;
    }

}
