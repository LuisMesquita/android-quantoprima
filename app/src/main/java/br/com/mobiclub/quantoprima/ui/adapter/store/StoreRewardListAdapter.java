package br.com.mobiclub.quantoprima.ui.adapter.store;

import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.kevinsawicki.wishlist.SingleTypeAdapter;

import java.util.List;

import br.com.mobiclub.quantoprima.R;
import br.com.mobiclub.quantoprima.core.service.api.entity.Image;
import br.com.mobiclub.quantoprima.core.service.api.entity.LocationReward;
import br.com.mobiclub.quantoprima.ui.view.ImageLoader;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 *
 */
public class StoreRewardListAdapter extends SingleTypeAdapter<LocationReward> {

    private final StoreRewardListAdapterListener listener;

    @InjectView(R.id.button_buy)
    ImageButton buttonBuy;

    private Resources resources;

    public StoreRewardListAdapter(StoreRewardListAdapterListener listener,
                                  LayoutInflater layoutInflater, Resources resources,
                                  List<LocationReward> items) {
        super(layoutInflater, R.layout.list_item_store_reward);
        this.resources = resources;
        this.listener = listener;
        setItems(items);
    }

    @Override
    public long getItemId(final int position) {
        final int id = getItem(position).getId();
        return id != 0 ? id : super.getItemId(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);

        ButterKnife.inject(this, view);

        buttonBuy.setTag(position);

        return view;
    }

    @OnClick(R.id.button_buy)
    public void buyReward(ImageButton button) {
        int position = (int) button.getTag();
        LocationReward locationReward = getItem(position);
        listener.onBuyReward(locationReward);
    }

    @Override
    protected int[] getChildViewIds() {
        return new int[] {
            R.id.label_title, R.id.label_description, R.id.label_price,
                R.id.image_logo, R.id.button_buy
        };
    }

    private void loadImage(Image image, ImageView imageItem) {
        ImageLoader.getInstance().loadImage(imageItem, image,
                ImageLoader.Placeholder.STORE_REWARD_LIST);
    }

    @Override
    protected void update(final int position, final LocationReward locationReward) {
        //TODO: receber de acordo com o dpi atual
        loadImage(locationReward.getImage(), imageView(3));

        setText(0, locationReward.getTitle());
        setText(1, locationReward.getDescription());
        String price = locationReward.getPrice();
        String priceValue = resources.getString(R.string.store_front_label_reward_price, price);
        setText(2, priceValue);

        if(!locationReward.canBuy()) {
            ImageButton view = getView(4, ImageButton.class);
            view.setEnabled(false);
            view.setClickable(false);
            view.setImageResource(R.drawable.t25_btn_vitrine_comprar_disabled);
            getView(0, TextView.class).setTextColor(resources.getColor(R.color.grey11));
            getView(1, TextView.class).setTextColor(resources.getColor(R.color.grey16));
        } else {
            ImageButton view = getView(4, ImageButton.class);
            view.setImageResource(R.drawable.t25_btn_vitrine_comprar_active);
            getView(0, TextView.class).setTextColor(resources.getColor(R.color.theme_principal));
            getView(1, TextView.class).setTextColor(resources.getColor(R.color.grey16));
            view.setEnabled(true);
            view.setClickable(true);
        }
    }

}
