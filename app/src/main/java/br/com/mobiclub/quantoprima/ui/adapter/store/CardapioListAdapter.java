package br.com.mobiclub.quantoprima.ui.adapter.store;

import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.kevinsawicki.wishlist.SingleTypeAdapter;

import java.text.DecimalFormat;
import java.util.Currency;
import java.util.List;

import br.com.mobiclub.quantoprima.R;
import br.com.mobiclub.quantoprima.core.service.api.entity.CardapioItem;
import br.com.mobiclub.quantoprima.core.service.api.entity.Image;
import br.com.mobiclub.quantoprima.core.service.api.entity.Price;
import br.com.mobiclub.quantoprima.ui.view.ImageLoader;
import butterknife.ButterKnife;

/**
 *
 */
public class CardapioListAdapter extends SingleTypeAdapter<CardapioItem> {

    private final LayoutInflater layoutInflater;
    private Resources resources;

    public CardapioListAdapter(LayoutInflater layoutInflater, Resources resources,
                               List<CardapioItem> items) {
        super(layoutInflater, R.layout.list_item_cardapio);
        this.layoutInflater = layoutInflater;
        this.resources = resources;
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

        return view;
    }

    @Override
    protected int[] getChildViewIds() {
        return new int[] {
                R.id.label_title, R.id.label_description, R.id.label_price,
                R.id.image_logo, R.id.lyt_price
        };
    }

    private void loadImage(Image image, ImageView imageItem) {
        ImageLoader.getInstance().loadImage(imageItem, image,
                ImageLoader.Placeholder.CARDAPIO_LIST);
    }

    @Override
    protected void update(final int position, final CardapioItem cardapioItem) {
        //TODO: receber de acordo com o dpi atual
        loadImage(cardapioItem.getImage(), imageView(3));

        String name = cardapioItem.getName();
        if(name != null)
            setText(0, name);

        String description = cardapioItem.getDescription();
        if(description != null)
            setText(1, description);

        LinearLayout lytPrice = getView(4, LinearLayout.class);

        List<Price> prices = cardapioItem.getPrice();
        //List<Price> prices = Arrays.asList(new Price("P:", 3.0), new Price("M:", 3.33),
        //                                   new Price("G:", 4.50));
        //List<Price> prices = Arrays.asList(new Price("P:", 3.0));

        /*
        if(prices != null && lytPrice.getChildCount() == 0) {
            Currency currency = Currency.getInstance("BRL");
            DecimalFormat formato = new DecimalFormat(currency.getSymbol() + "#,##0.00");
            int i = 0;
            if(prices.size() == 1) {
                Log.e("ITEM1", stringhuds);
                Price price = prices.get(0);
                addPrice(lytPrice, formato, price, prices.size(), 0);
            } else {
                Log.e("ITEM2", stringhuds);
                for (Price price : prices) {
                    addPrice(lytPrice, formato, price, prices.size(), i);
                    i++;
                }
            }
        }
        */

        // Limpa as filhas antes de reusar...
        lytPrice.removeAllViews();
        if(prices != null && lytPrice.getChildCount() == 0) {
            Currency currency = Currency.getInstance("BRL");
            DecimalFormat formato = new DecimalFormat(currency.getSymbol() + "#,##0.00");
            int i = 0;

            for (Price price : prices) {
                addPrice(lytPrice, formato, price, prices.size(), i);
                i++;
            }
        }
    }

    private void addPrice(LinearLayout lytPrice, DecimalFormat formato,
                          Price price, int size, int i) {
        if(price.getPrice() == 0)
            return;
        View cardapioPriceView = layoutInflater.inflate(R.layout.lyt_item_cardapio_price, null);

        TextView labelPrice = (TextView) cardapioPriceView.findViewById(R.id.label_price);

        String priceString = resources.getString(R.string.list_item_cardapio_front_label_price, price);
        priceString = formato.format(price.getPrice());
        labelPrice.setText(priceString);

        if(size > 1) {
            TextView labelName = (TextView) cardapioPriceView.findViewById(R.id.label_name);
            labelName.setText(price.getSize());
            labelName.setVisibility(View.VISIBLE);
            if(i >= 0 && i < size - 1) {
                TextView labelPipe = (TextView) cardapioPriceView.findViewById(R.id.label_pipe);
                labelPipe.setVisibility(View.VISIBLE);
            }
        } else {
            labelPrice.setTextColor(resources.getColor(R.color.orange));
        }
        lytPrice.addView(cardapioPriceView);
    }

}
