package br.com.mobiclub.quantoprima.ui.adapter;

import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.kevinsawicki.wishlist.SingleTypeAdapter;

import java.util.List;

import br.com.mobiclub.quantoprima.R;
import br.com.mobiclub.quantoprima.core.service.api.entity.Establishment;
import br.com.mobiclub.quantoprima.core.service.api.entity.EstablishmentLocation;
import br.com.mobiclub.quantoprima.core.service.api.entity.Image;
import br.com.mobiclub.quantoprima.facade.Facade;
import br.com.mobiclub.quantoprima.ui.view.ImageLoader;
import br.com.mobiclub.quantoprima.util.Util;

/**
 *
 */
public class EstablishmentListAdapter extends SingleTypeAdapter<Establishment> {

    private final Facade facade;
    private Resources resources;

    public EstablishmentListAdapter(LayoutInflater layoutInflater, Resources resources,
                                    List<Establishment> items) {
        super(layoutInflater, R.layout.list_item_establishment);
        this.resources = resources;
        setItems(items);

        facade = Facade.getInstance();
    }

    @Override
    public long getItemId(final int position) {
        final int id = getItem(position).getId();
        return id != 0 ? id : super.getItemId(position);
    }

    @Override
    protected int[] getChildViewIds() {
        return new int[]{
                //establishment with one location
                R.id.item_location, R.id.image_logo, R.id.label_location_name,
                R.id.label_location_reference, R.id.label_location_distance, R.id.label_score_value,

                //establishment with more locations
                R.id.item_locations, R.id.label_name, R.id.label_distance,
                R.id.item_locations_bar_indicator, R.id.label_location_premium,
                R.id.image_distance, R.id.image_distance_locations,
                R.id.lyt_score
        };
    }

    private void loadImage(Image image, ImageView imageItem) {
        ImageLoader.getInstance().loadImage(imageItem, image,
                ImageLoader.Placeholder.ESTABLISHMENT_LIST);
    }

    @Override
    protected void update(final int position, final Establishment establishment) {
        loadImage(new Image(establishment.getLogoUrl()), imageView(1));
        if(establishment == null) {
            return;
        }
        if(establishment.isGroup()) {
            showItem(true);
            setText(7, String.format("%s", establishment.getName()));
            Double distance = establishment.getMoreProximityLocation();
            if(distance != null && distance != 0) {
                setText(8, Util.getDistanceString(distance));
                getView(12, ImageView.class).setVisibility(View.VISIBLE);
                if(distance > 1.0) {
                    getView(12, ImageView.class).setImageResource(R.drawable.ic_distancia_longe);
                    getView(8, TextView.class).setTextColor(resources.getColor(R.color.label_distance_color));
                }
            } else {
                getView(12, ImageView.class).setVisibility(View.GONE);
                getView(8, TextView.class).setVisibility(View.GONE);
            }
        } else if(!establishment.getLocations().isEmpty()) {
            showItem(false);
            setText(2, String.format("%s", establishment.getName()));
            EstablishmentLocation location = establishment.getLocations().get(0);
            if(location == null)
                return;
            setText(3, String.format("%s", location.getReference()));
            Double distance = location.getDistance();
            if(distance != null && distance != 0) {
                setText(4, Util.getDistanceString(distance));
                getView(4, TextView.class).setVisibility(View.VISIBLE);
                getView(11, ImageView.class).setVisibility(View.VISIBLE);
                if(distance > 1.0) {
                    getView(11, ImageView.class).setImageResource(R.drawable.ic_distancia_longe);
                    getView(4, TextView.class).setTextColor(resources.getColor(R.color.label_distance_color));
                }
            } else {
                getView(11, ImageView.class).setVisibility(View.GONE);
                getView(4, TextView.class).setVisibility(View.GONE);
            }

//            int score = location.getScore();
//            if(score > 0) {
//                setText(5, String.format("%d", score));
//                getView(13, RelativeLayout.class).setVisibility(View.VISIBLE);
//            } else {
//                getView(13, RelativeLayout.class).setVisibility(View.GONE);
//            }

            TextView labelPremium = getView(10, TextView.class);
            if(location.getHasReward() != null && location.getHasReward()) {
                labelPremium.setVisibility(View.VISIBLE);
            } else {
                labelPremium.setVisibility(View.GONE);
            }
        } else {
            //estabelecimento sem locations
            showItem(true);
            setText(2, String.format("%s", establishment.getName()));
            setText(3, String.format("%s", "Desconhecido"));
        }
    }

    private void showItem(boolean locations) {
        RelativeLayout itemLocations = getView(6, RelativeLayout.class);
        LinearLayout itemLocation = getView(0, LinearLayout.class);
        FrameLayout barIndicator = getView(9, FrameLayout.class);
        if(locations) {
            itemLocations.setVisibility(View.VISIBLE);
            itemLocation.setVisibility(View.GONE);
            barIndicator.setVisibility(View.VISIBLE);
        } else {
            itemLocations.setVisibility(View.GONE);
            itemLocation.setVisibility(View.VISIBLE);
            barIndicator.setVisibility(View.GONE);
        }
    }

}
