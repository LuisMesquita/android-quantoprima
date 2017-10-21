package br.com.mobiclub.quantoprima.ui.fragment.reward;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Calendar;

import br.com.mobiclub.quantoprima.R;
import br.com.mobiclub.quantoprima.core.service.api.entity.Image;
import br.com.mobiclub.quantoprima.core.service.api.entity.Reward;
import br.com.mobiclub.quantoprima.facade.Facade;
import br.com.mobiclub.quantoprima.ui.view.ImageLoader;
import br.com.mobiclub.quantoprima.util.Util;
import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * A simple {@link Fragment} subclass.
 *
 */
public class RescueRewardFragment extends Fragment {

    private Reward reward;

    @InjectView(R.id.image_reward)
    ImageView imageReward;

    @InjectView(R.id.label_name)
    TextView labelName;

    @InjectView(R.id.label_establishment)
    TextView labelLocation;

    @InjectView(R.id.label_expiration)
    TextView labelExpiration;

    @InjectView(R.id.button_rescue)
    Button btnRescue;

    @InjectView(R.id.label_code)
    TextView labelCode;

    @InjectView(R.id.label_time)
    TextView labelTime;

    public RescueRewardFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        reward = Facade.getInstance().getReward();
    }

    private static void loadImage(Image image, ImageView imageItem, int placeholder) {
        ImageLoader.getInstance().loadImage(imageItem, image,
                placeholder);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_rescue_reward, container, false);

        ButterKnife.inject(this, view);

        if(reward == null) {
            btnRescue.setVisibility(View.INVISIBLE);
            return view;
        }

        loadImage(reward.getImage(), imageReward, ImageLoader.Placeholder.DEFAULT);

        String title = reward.getTitle();
        if(title != null)
            labelName.setText(title);
        String establishmentName = reward.getEstablishmentName();
        if(establishmentName != null)
            labelLocation.setText(establishmentName);
        String expirationAtDate = reward.getExpirationAt();
        java.util.Date date = Util.paserTPatternDate(expirationAtDate);
        expirationAtDate = Util.getDateTimeString(date);
        String expirationAt = getResources().getString(R.string.list_item_reward_label_expiration,
                expirationAtDate);
        if(expirationAt != null)
            labelExpiration.setText(expirationAt);

        String idToRescue = reward.getIdToHexa();
        if(idToRescue != null) {
            labelCode.setText(idToRescue);
        }
        Calendar calendar = Calendar.getInstance();
        reward.setTime(calendar.getTime());
        String time = Util.getDateTimeString(calendar.getTime());
        labelTime.setText(time);

        return view;
    }

}
