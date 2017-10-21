package br.com.mobiclub.quantoprima.ui.adapter.notification;

import android.content.res.Resources;
import android.os.Build;
import android.view.LayoutInflater;
import android.widget.CompoundButton;
import android.widget.ImageView;

import com.github.kevinsawicki.wishlist.SingleTypeAdapter;

import java.util.List;

import br.com.mobiclub.quantoprima.R;
import br.com.mobiclub.quantoprima.core.service.api.entity.Establishment;
import br.com.mobiclub.quantoprima.core.service.api.entity.EstablishmentLocation;
import br.com.mobiclub.quantoprima.core.service.api.entity.Image;
import br.com.mobiclub.quantoprima.ui.view.ImageLoader;
import br.com.mobiclub.quantoprima.util.Ln;

/**
 *
 */
public class NotifyLocationListAdapter extends SingleTypeAdapter<EstablishmentLocation> {

    private final NotifyLocationListAdapterListener listener;

    private Resources resources;
    private EstablishmentLocation lastLocation;

    public NotifyLocationListAdapter(NotifyLocationListAdapterListener listener,
                                     LayoutInflater layoutInflater, Resources resources,
                                     List<EstablishmentLocation> items) {
        super(layoutInflater, R.layout.list_item_notify_location);
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
    protected int[] getChildViewIds() {
        return new int[] {
                R.id.image_logo, R.id.label_location_name,
                R.id.label_location_reference, R.id.btn_switch
        };
    }

    private void loadImage(Image image, ImageView imageItem) {
        ImageLoader.getInstance().loadImage(imageItem, image,
                ImageLoader.Placeholder.NOTIFY_LOCATION);
    }

    @Override
    protected void update(final int position, final EstablishmentLocation location) {
        Establishment establishment = location.getEstablishment();
        if(establishment == null) {
            Ln.e("establishment == null");
            return;
        }
        loadImage(new Image(establishment.getLogoUrl()), imageView(0));

        if(establishment.isGroup()) {
            showItem(true);
            //TODO: mostrar item com estilo apropriado
            showLocation(establishment, location, position);
        } else if(!establishment.getLocations().isEmpty()) {
            showLocation(establishment, location, position);
        } else {
            //estabelecimento sem locations
            showItem(true);
            setText(1, String.format("%s", establishment.getName()));
            setText(2, String.format("%s", resources.getString(R.string.unknown)));
        }
    }

    private void showLocation(Establishment establishment,
                              EstablishmentLocation location, int position) {
        showItem(false);
        String name = establishment.getName();
        if(name != null)
            setText(1, String.format("%s", name));
        String reference = location.getReference();
        if(reference != null)
            setText(2, String.format("%s", reference));
        if(location.getBlocked() != null) {
            int currentapiVersion = android.os.Build.VERSION.SDK_INT;
            CompoundButton btnSwitchLocation;
            if (currentapiVersion >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                btnSwitchLocation = getView(3, CompoundButton.class);
            } else {
                btnSwitchLocation = getView(4, CompoundButton.class);
                //((ToggleButton)btnSwitchLocation).setButtonDrawable(R.drawable.bg_btn_switch);
            }
            btnSwitchLocation.setOnCheckedChangeListener(null);
            if (location.getBlocked()) {
                btnSwitchLocation.setChecked(false);
            } else {
                btnSwitchLocation.setChecked(true);
            }
            setListener(btnSwitchLocation, position);
        }
    }

    private void setListener(final CompoundButton btnSwitchToggle, int position) {
        btnSwitchToggle.setTag(position);
        btnSwitchToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int position = (int) btnSwitchToggle.getTag();
                EstablishmentLocation location = getItem(position);
                NotifyLocationListAdapter.this.lastLocation = location;
                listener.onSwitched(location, isChecked);
            }
        });
    }

    private void showItem(boolean locations) {

    }

}
