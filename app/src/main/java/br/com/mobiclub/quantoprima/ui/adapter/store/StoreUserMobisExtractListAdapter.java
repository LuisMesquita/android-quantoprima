package br.com.mobiclub.quantoprima.ui.adapter.store;

import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.github.kevinsawicki.wishlist.SingleTypeAdapter;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import br.com.mobiclub.quantoprima.R;
import br.com.mobiclub.quantoprima.core.service.api.entity.ExtractDetails;

/**
 *
 */
public class StoreUserMobisExtractListAdapter extends SingleTypeAdapter<ExtractDetails> {

    private Resources resources;

    public StoreUserMobisExtractListAdapter(LayoutInflater layoutInflater, Resources resources,
                                  List<ExtractDetails> items) {
        super(layoutInflater, R.layout.list_item_store_user_mobis_extract);
        this.resources = resources;
        setItems(items);
    }

    @Override
    public long getItemId(final int position) {
        //TODO: check hash code
        final int id = getItem(position).getReference().hashCode();
        return id != 0 ? id : super.getItemId(position);
    }

    @Override
    protected int[] getChildViewIds() {
        return new int[] {
                R.id.label_month_reference, R.id.label_mobis_expired, R.id.label_mobis_used,
                R.id.label_mobis_win, R.id.frame_expired, R.id.frame_used, R.id.frame_win,
                R.id.lyt_extract, R.id.lyt_legend, R.id.item
        };
    }

    @Override
    protected void update(final int position, final ExtractDetails extractDetails) {
        if(position > 0) {
            LinearLayout lytExtract = getView(7, LinearLayout.class);
            lytExtract.setVisibility(View.GONE);
            LinearLayout lytLegend = getView(8, LinearLayout.class);
            lytLegend.setVisibility(View.GONE);
        } else {
            LinearLayout lytExtract = getView(7, LinearLayout.class);
            lytExtract.setVisibility(View.VISIBLE);
            LinearLayout lytLegend = getView(8, LinearLayout.class);
            lytLegend.setVisibility(View.VISIBLE);
        }

//        if(extractDetails.getWon() == 0 && extractDetails.getLose() == 0 &&
//                extractDetails.getUsed() == 0) {
//            getView(9, LinearLayout.class).setVisibility(View.GONE);
//            return;
//        } else {
//            getView(9, LinearLayout.class).setVisibility(View.VISIBLE);
//        }

        String referenceValue = getExtractDateReference(extractDetails.getReference());
        if(referenceValue != null)
            setText(0, referenceValue);

        LinearLayout frameExpired = getView(4, LinearLayout.class);
        LinearLayout frameUsed = getView(5, LinearLayout.class);
        LinearLayout frameWin = getView(6, LinearLayout.class);

        //expirado
        int lose = extractDetails.getLose();
        if(lose != 0) {
            frameExpired.setVisibility(View.VISIBLE);
            String expiredString = String.valueOf(lose);
            if(expiredString != null)
                setText(1, "-"+expiredString);
        } else {
            frameExpired.setVisibility(View.INVISIBLE);
        }

        //usado/gasto
        int used = extractDetails.getUsed();
        if(used != 0) {
            frameUsed.setVisibility(View.VISIBLE);
            String usedString = String.valueOf(used);
            if(usedString != null)
                setText(2, "-"+usedString);
        } else {
            frameUsed.setVisibility(View.INVISIBLE);
        }

        //ganhou
        int won = extractDetails.getWon();
        if(won != 0) {
            frameWin.setVisibility(View.VISIBLE);
            String winString = String.valueOf(won);
            if(winString != null)
                setText(3, "+"+winString);
        } else {
            frameWin.setVisibility(View.INVISIBLE);
        }

    }

    //TODO: move this for util
    public static String getExtractDateReference(Date reference) {
        Calendar date = Calendar.getInstance();
        date.setTime(reference);
        Locale locale = new Locale("pt", "BR");
        String month = date.getDisplayName(Calendar.MONTH, Calendar.LONG, locale);
        String result = String.format("%s/%s", month, date.get(Calendar.YEAR));
        return Character.toUpperCase(result.charAt(0)) + result.substring(1);
    }

}
