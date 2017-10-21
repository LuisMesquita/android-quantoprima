package br.com.mobiclub.quantoprima.ui.fragment.store;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

import br.com.mobiclub.quantoprima.R;
import br.com.mobiclub.quantoprima.core.service.api.entity.CardapioItem;
import br.com.mobiclub.quantoprima.core.service.api.entity.Image;
import br.com.mobiclub.quantoprima.core.service.api.entity.Price;
import br.com.mobiclub.quantoprima.facade.Facade;
import br.com.mobiclub.quantoprima.ui.view.ImageLoader;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 */
public class CardapioItemFragment extends Fragment {

    private CardapioItem cardapioItem;

    @InjectView(R.id.frame_image)
    FrameLayout frameImage;

    @InjectView(R.id.label_name)
    TextView labelName;

    @InjectView(R.id.image_item)
    ImageView imageItem;

    @InjectView(R.id.btn_next)
    Button next;

    @InjectView(R.id.btn_prev)
    Button prev;

    @InjectView(R.id.label_description)
    TextView labelDescription;

    @InjectView(R.id.frame_next)
    FrameLayout frameNext;

    @InjectView(R.id.frame_prev)
    FrameLayout framePrev;

    private int current = 0;
    private List<Image> images;
    private LayoutInflater layoutInflater;
    private Resources resources;

    public CardapioItemFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        cardapioItem = Facade.getInstance().getCardapioItem();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @OnClick(R.id.btn_prev)
    public void onBtnPrev() {
        if(current != 0) {
            current--;
            Image image = images.get(current);
            loadImage(image, imageItem);
            showNav();
        }
    }

    @OnClick(R.id.btn_next)
    public void onBtnNext() {
        if(images != null && current != images.size()) {
            current++;
            Image image = images.get(current);
            loadImage(image, imageItem);
            showNav();
        }
    }

    private void showNav() {
        if(current > 0 && current < images.size() - 1) {
            prev.setEnabled(true);
            next.setEnabled(true);
        }
        if(current == 0) {
            prev.setEnabled(false);
        }
        if(current == images.size() - 1) {
            next.setEnabled(false);
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (cardapioItem != null) {

            labelName.setText(cardapioItem.getName());

            images = new ArrayList<Image>();
            images.add(cardapioItem.getImage());
            if(images != null) {
                if(images.size() == 1 || images.isEmpty()) {
                    frameNext.setVisibility(View.INVISIBLE);
                    framePrev.setVisibility(View.INVISIBLE);
                    frameImage.setVisibility(View.VISIBLE);
                } else if(images.isEmpty()) {
                    frameNext.setVisibility(View.GONE);
                    framePrev.setVisibility(View.GONE);
                    frameImage.setVisibility(View.GONE);
                } else {
                    frameImage.setVisibility(View.VISIBLE);
                }
                loadImages(imageItem, images);
            }

            String description = cardapioItem.getDescription();
            if(description != null)
                labelDescription.setText(description);

            List<Price> prices = cardapioItem.getPrice();

//            List<Price> prices = Arrays.asList(new Price("P:", 3.0), new Price("M:", 3.33),
//                    new Price("G:", 4.50));
            //List<Price> prices = Arrays.asList(new Price("P:", 3.0));

            FragmentActivity activity = getActivity();
            layoutInflater = activity.getLayoutInflater();
            resources = activity.getResources();
            LinearLayout lytPrice = (LinearLayout) view.findViewById(R.id.lyt_cardapio_item_price);

            if(prices != null) {
                //TODO: put in util
                Currency currency = Currency.getInstance("BRL");
                DecimalFormat formato = new DecimalFormat(currency.getSymbol() + "#,##0.00");
                int i = 0;
                if(prices.size() == 1) {
                    Price price = prices.get(0);
                    addPrice(lytPrice, formato, price, prices.size(), 0);
                } else {
                    for (Price price : prices) {
                        addPrice(lytPrice, formato, price, prices.size(), i);
                        i++;


                    }
                }
            }
        }
    }

    private void addPrice(LinearLayout lytPrice, DecimalFormat formato, Price price, int size, int i) {
        if(price.getPrice() == 0)
            return;
        View cardapioPriceView = layoutInflater.inflate(R.layout.lyt_item_cardapio_item_price, null);

        TextView labelPrice = (TextView) cardapioPriceView.findViewById(R.id.label_price);

        String priceString = resources.getString(R.string.list_item_cardapio_front_label_price, price);
        priceString = formato.format(price.getPrice());
        labelPrice.setText(priceString);

        if(size > 1) {
            TextView labelName = (TextView) cardapioPriceView.findViewById(R.id.label_name);
            labelName.setText(price.getSize());
            labelName.setVisibility(View.VISIBLE);
        } else {
            cardapioPriceView.findViewById(R.id.bar).setVisibility(View.GONE);
            View viewById = cardapioPriceView.findViewById(R.id.lyt_item_cardapio_item_price);
            viewById.setBackgroundResource(R.drawable.transparent_drawable);
        }
        lytPrice.addView(cardapioPriceView);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cardapio_item, container, false);

        ButterKnife.inject(this, view);

        return view;
    }

    private void loadImages(ImageView imageView, List<Image> images) {
        ImageLoader.getInstance().loadImages(imageView, images, ImageLoader.Placeholder.CARDAPIO_ITEM);
    }

    private void loadImage(Image image, ImageView imageItem) {
        image.setPreferedType(null);
        ImageLoader.getInstance().loadImage(imageItem, image,
                ImageLoader.Placeholder.CARDAPIO_ITEM);
    }

}
