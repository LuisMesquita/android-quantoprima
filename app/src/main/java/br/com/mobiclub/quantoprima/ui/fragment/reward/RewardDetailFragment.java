package br.com.mobiclub.quantoprima.ui.fragment.reward;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import br.com.mobiclub.quantoprima.R;
import br.com.mobiclub.quantoprima.core.service.api.entity.Image;
import br.com.mobiclub.quantoprima.core.service.api.entity.Reward;
import br.com.mobiclub.quantoprima.facade.Facade;
import br.com.mobiclub.quantoprima.ui.view.ImageLoader;
import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 *
 */
public class RewardDetailFragment extends Fragment {

    private Reward reward;

    @InjectView(R.id.label_name)
    TextView name;

    @InjectView(R.id.label_description)
    TextView description;

    @InjectView(R.id.image_reward)
    ImageView imageReward;

    public RewardDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        reward = Facade.getInstance().getReward();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private static void loadImage(Image image, ImageView imageItem, int placeholder) {
        image.setPreferedType(null);
        ImageLoader.getInstance().loadImage(imageItem, image,
                placeholder);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reward_details, container, false);
        ButterKnife.inject(this, view);

        if(reward != null) {
            name.setText(reward.getTitle());
            description.setText(reward.getDescription());
            //TODO: retrieve dpi
            Image rewardImage = reward.getImage();
            if(rewardImage == null) {
                imageReward.setImageResource(R.drawable.t28_1_recompensa_placeholder);
            } else {
                String hdpi = rewardImage.getHDPI();
                loadImage(rewardImage, imageReward, ImageLoader.Placeholder.DEFAULT);
            }
        }
        return view;
    }

}
