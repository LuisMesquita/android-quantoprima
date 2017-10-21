package br.com.mobiclub.quantoprima.ui.factory;

import android.content.res.Resources;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;

import br.com.mobiclub.quantoprima.R;
import br.com.mobiclub.quantoprima.core.service.api.entity.Establishment;
import br.com.mobiclub.quantoprima.core.service.api.entity.EstablishmentLocation;
import br.com.mobiclub.quantoprima.core.service.api.entity.Gift;
import br.com.mobiclub.quantoprima.core.service.api.entity.Image;
import br.com.mobiclub.quantoprima.ui.adapter.DialogResultAdapter;
import br.com.mobiclub.quantoprima.ui.adapter.DialogResultAdapterImpl;
import br.com.mobiclub.quantoprima.ui.helper.GainMobiHelper;
import br.com.mobiclub.quantoprima.ui.view.DialogResultData;
import br.com.mobiclub.quantoprima.ui.view.ImageLoader;

/**
 *  Uma fabricazinha para construir diversos tipos de dialog de resultado
 */
public class DialogResultFactory {

    private static DialogResultAdapter dialogResultGainMobi = new DialogResultAdapterImpl() {

        private Random rnd = new Random();

        private int emotions[] = { R.drawable.emoticon_lingua1, R.drawable.emoticon_nerd1,
                R.drawable.emoticon_nerd2, R.drawable.emoticon_oculos1,
                R.drawable.emoticon_semdente1, R.drawable.emoticon_sorriso1,
                R.drawable.emoticon_sorriso2, R.drawable.emoticon_sorriso3,
                R.drawable.emoticon_sorriso4, R.drawable.emoticon_sorriso5,
                R.drawable.emoticon_sorriso6 };

        @Override
        public void onCreateView(View view, DialogResultData data) {
            super.onCreateView(view, data);

            if(data == null)
                return;
            GainMobiHelper.GainMobiDialogData gainMobiData = (GainMobiHelper.GainMobiDialogData) data;

            TextView congrat = (TextView) view.findViewById(R.id.label_congrat_user);
            String congratUserMessage = gainMobiData.congratUserMessage;
            if(congrat != null && congratUserMessage != null) {
                //congrat.setText(Html.fromHtml(congratUserMessage));
                congrat.setText(((GainMobiHelper.GainMobiDialogData) data).mobiResponse.getMessageHead());
            }

            if(gainMobiData.shot) {
                TextView congratMessage = (TextView) view.findViewById(R.id.label_congrat_user_message);
                if(congratMessage != null) {
                    Resources resources = view.getResources();
                    String string = resources.getString(R.string.label_congrat_user_message_string);
                    // congratMessage.setText(Html.fromHtml(string));
                    congratMessage.setText(((GainMobiHelper.GainMobiDialogData) data).mobiResponse.getMessageBody());
                }
                return;
            }

            TextView totalMobis = (TextView) view.findViewById(R.id.label_total_mobis);
            if(totalMobis != null) {
                totalMobis.setText(((GainMobiHelper.GainMobiDialogData) data).mobiResponse.getTitleBody());
            }

            ImageView imgEmotion = (ImageView) view.findViewById(R.id.image_emotion_icon);
            int emotion = emotions[rnd.nextInt(emotions.length)];
            imgEmotion.setImageResource(emotion);
        }

    };

    private static void loadImage(Image image, ImageView imageItem, int placeholder) {
        ImageLoader.getInstance().loadImage(imageItem, image,
                placeholder);
    }

    private static DialogResultAdapter dialogResultGainGift = new DialogResultAdapterImpl() {

        @Override
        public void onCreateView(View view, DialogResultData data) {
            super.onCreateView(view, data);
            if(data != null && data instanceof Gift) {
                Gift gift = (Gift) data;
                TextView labelGift = (TextView) view.findViewById(R.id.label_gift);
                if(gift.getDescription() != null) {
                    labelGift.setText(gift.getDescription());
                }
                TextView labelValid = (TextView) view.findViewById(R.id.label_valid);
                if(gift.getExpiration() != null) {
                    labelValid.setText(gift.getExpirationString());
                }

                Image image = gift.getImage();
                ImageView imageReward = (ImageView) view.findViewById(R.id.image_reward);
                if(image != null && image != null) {
                    loadImage(image, imageReward, ImageLoader.Placeholder.RECOMPENSA);
                } else {
                    imageReward.setImageResource(R.drawable.t28_1_recompensa_placeholder_bg);
                }

                Establishment establishment = gift.getEstablishment();
                ImageView imageLocation = (ImageView) view.findViewById(R.id.image_location);
                String logoUrl;
                if(establishment != null && establishment.getLogoUrl() != null) {
                    logoUrl = establishment.getLogoUrl();
                } else {
                    logoUrl = gift.getEstablishmentLogoURL();
                }
                loadImage(new Image(logoUrl),
                        imageLocation, ImageLoader.Placeholder.DEFAULT);

                String reference = "";
                if(establishment != null) {
                    EstablishmentLocation location = establishment.getLocation();
                    if(location.getReference() != null) {
                        reference = location.getReference();
                    } else {
                        reference = "";
                    }
                } else {
                    reference = gift.getEstablishmentName();
                }
                TextView labelReference = (TextView) view.findViewById(R.id.label_reference);
                labelReference.setText(reference);
            }
        }

    };


    private static DialogResultAdapter dialogResultGainShotFailed = new DialogResultAdapterImpl() {

        @Override
        public void onCreateView(View view, DialogResultData data) {
            super.onCreateView(view, data);
            TextView v = (TextView) view.findViewById(R.id.label_congrat_user);
            String string = view.getResources().getString(R.string.dialog_gain_shot_not_gained_label_message_retry);
            v.setText(Html.fromHtml(string));
        }

    };


    public static DialogResultAdapter createGainShotFailed() {
        dialogResultGainShotFailed.setSuccessLayout(R.layout.dialog_gain_mobi_shot_not_gained);
        dialogResultGainShotFailed.setResult(DialogResultAdapter.RESULT_SUCCESS);
        return dialogResultGainShotFailed;
    }

    public static DialogResultAdapter createErrorDefault() {
        DialogResultAdapter dialogResult = new DialogResultAdapterImpl();
        dialogResult.setResult(DialogResultAdapter.RESULT_ERROR);
        return dialogResult;
    }

    public static DialogResultAdapter createGainMobiSuccess(GainMobiHelper.GainMobiDialogData data) {
        dialogResultGainMobi.setData(data);
        dialogResultGainMobi.setResult(DialogResultAdapter.RESULT_SUCCESS);
        if(data.shot) {
            dialogResultGainMobi.setSuccessLayout(R.layout.dialog_gain_mobi_shot);
            return dialogResultGainMobi;
        }
        if(data.online) {
            dialogResultGainMobi.setSuccessLayout(R.layout.dialog_gain_mobi_online);
            return dialogResultGainMobi;
        } else {
            dialogResultGainMobi.setSuccessLayout(R.layout.dialog_gain_mobi_offline);
            return dialogResultGainMobi;
        }
    }

    public static DialogResultAdapter createSurveySuccess(int layout) {
        dialogResultGainMobi.setData(null);
        dialogResultGainMobi.setResult(DialogResultAdapter.RESULT_SUCCESS);
        dialogResultGainMobi.setSuccessLayout(layout);
        return dialogResultGainMobi;
    }

    public static DialogResultAdapter createGainGiftSuccess(Gift gift) {
        dialogResultGainGift.setData(gift);
        dialogResultGainGift.setResult(DialogResultAdapter.RESULT_SUCCESS);
        dialogResultGainGift.setSuccessLayout(R.layout.dialog_gain_gift);
        return dialogResultGainGift;
    }

}
