package br.com.mobiclub.quantoprima.ui.adapter;

import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.kevinsawicki.wishlist.SingleTypeAdapter;

import java.util.Date;
import java.util.List;

import br.com.mobiclub.quantoprima.R;
import br.com.mobiclub.quantoprima.core.service.api.entity.Image;
import br.com.mobiclub.quantoprima.database.table.MobiTable;
import br.com.mobiclub.quantoprima.domain.Mobi;
import br.com.mobiclub.quantoprima.ui.view.ImageLoader;
import br.com.mobiclub.quantoprima.util.Util;
import butterknife.ButterKnife;

/**
 *
 */
public class MobisOfflineListAdapter extends SingleTypeAdapter<Mobi> {

    private Resources resources;

    public MobisOfflineListAdapter(LayoutInflater layoutInflater, Resources resources,
                                   List<Mobi> items) {
        super(layoutInflater, R.layout.list_item_offline_mobi);
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
            R.id.image_logo, R.id.label_location, R.id.label_status,
                R.id.label_time, R.id.lyt_score
        };
    }

    private void loadImage(Image image, ImageView imageItem) {
        ImageLoader.getInstance().loadImage(imageItem, image,
                ImageLoader.Placeholder.ESTABLISHMENT_LIST);
    }

    @Override
    protected void update(final int position, final Mobi mobi) {
        String logo = mobi.getLogo();
        if(logo != null) {
            loadImage(new Image(logo), imageView(0));
        } else {
            imageView(0).setImageResource(R.drawable.img001_mobiclub);
        }

        String locationName = mobi.getLocationName();
        if(locationName != null) {
            setText(1, locationName);
        } else {
            setText(1, resources.getString(R.string.mobis_offline_reference_default));
        }

        TextView labelStatus = getView(2, TextView.class);

        getView(3, TextView.class).setVisibility(View.VISIBLE);
        getView(4, RelativeLayout.class).setVisibility(View.INVISIBLE);

        String status = resources.getString(R.string.unknown);
        int mobiStatus = mobi.getStatus();
        if(mobiStatus == MobiTable.STATUS_NOT_SYNC_TYPE) {
            labelStatus.setTextColor(resources.getColor(R.color.sync_status_error));
            status = resources.getString(R.string.offline_mobi_not_sync);
        } else if(mobiStatus == MobiTable.STATUS_SYNCED_ERROR_TYPE_EXPIRED) {
            getView(3, TextView.class).setVisibility(View.GONE);
            labelStatus.setTextColor(resources.getColor(R.color.sync_status_error));
            status = resources.getString(R.string.offline_mobi_sync_error_expired);
            setText(2, status);
            return;
        } else if(mobiStatus == MobiTable.STATUS_SYNCED_TYPE) {
            labelStatus.setTextColor(resources.getColor(R.color.green));
            status = resources.getString(R.string.offline_mobi_synced);
            getView(4, RelativeLayout.class).setVisibility(View.VISIBLE);
        } else if(mobiStatus == MobiTable.STATUS_SYNCING_TYPE) {
            labelStatus.setTextColor(resources.getColor(R.color.black_light));
            status = resources.getString(R.string.offline_mobi_syncing);
        }

        setText(2, status);

        Date timeUpdate = mobi.getTimeUpdate();
        String timeUpdateString = Util.getSQLDateString(timeUpdate);
        if(timeUpdateString != null) {
            timeUpdateString = resources.getString(R.string.offline_mobi_label_time, timeUpdateString);
            setText(3, timeUpdateString);
        } else {
            setText(3, "");
        }
    }

}
