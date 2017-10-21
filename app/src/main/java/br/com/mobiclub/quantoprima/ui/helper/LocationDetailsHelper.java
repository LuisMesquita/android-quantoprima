package br.com.mobiclub.quantoprima.ui.helper;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import br.com.mobiclub.quantoprima.R;
import br.com.mobiclub.quantoprima.core.service.api.entity.Establishment;
import br.com.mobiclub.quantoprima.core.service.api.entity.EstablishmentLocation;
import br.com.mobiclub.quantoprima.core.service.api.entity.Image;
import br.com.mobiclub.quantoprima.ui.view.ImageLoader;
import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 *
 */
public class LocationDetailsHelper {

    @InjectView(R.id.image_logo)
    ImageView imageLogo;

    @InjectView(R.id.label_location_name)
    TextView labelLocationName;

    @InjectView(R.id.label_location_reference)
    TextView labelLocationReference;

    @InjectView(R.id.label_score_value)
    TextView labelScoreValue;

    @InjectView(R.id.lyt_score)
    RelativeLayout lytScore;

    private Establishment establishment;

    private EstablishmentLocation establishmentLocation;

    private View view;
    private boolean showZeroMobis;

    public LocationDetailsHelper(View view,
                                 EstablishmentLocation establishmentLocation,
                                 Establishment establishment) {
        this.view = view;
        this.establishmentLocation = establishmentLocation;
        this.establishment = establishment;
        this.showZeroMobis = true;
        createView();
    }

    public LocationDetailsHelper(View view, EstablishmentLocation location,
                                 Establishment establishment, boolean showZeroMobis) {
        this(view, location, establishment);
        this.showZeroMobis = showZeroMobis;
    }

    private void createView() {
        ButterKnife.inject(this, view);
    }

    private static void loadImage(Image image, ImageView imageItem, int placeholder) {
        ImageLoader.getInstance().loadImage(imageItem, image,
                placeholder);
    }

    public void show() {
        if(establishment == null || establishmentLocation == null)
            return;
        loadImage(new Image(establishment.getLogoUrl()), imageLogo,
                ImageLoader.Placeholder.DEFAULT);
        labelLocationName.setText(establishment.getName());
        labelLocationReference.setText(establishmentLocation.getReference());
        int score = establishmentLocation.getScore();
        if(showZeroMobis) {
            lytScore.setVisibility(View.VISIBLE);
            labelScoreValue.setText(String.valueOf(score));
        } else {
            lytScore.setVisibility(View.GONE);
        }
    }

    public void updateLocationMobis(EstablishmentLocation location) {
        int value = location.getScore();
        labelScoreValue.setText(String.valueOf(value));
    }
}
