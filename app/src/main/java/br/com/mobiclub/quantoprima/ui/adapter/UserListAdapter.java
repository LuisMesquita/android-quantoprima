package br.com.mobiclub.quantoprima.ui.adapter;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.widget.ImageView;

import com.github.kevinsawicki.wishlist.SingleTypeAdapter;

import java.text.SimpleDateFormat;
import java.util.List;

import br.com.mobiclub.quantoprima.R;
import br.com.mobiclub.quantoprima.core.service.api.entity.Image;
import br.com.mobiclub.quantoprima.core.service.api.entity.User;
import br.com.mobiclub.quantoprima.ui.view.ImageLoader;

/**
 * Adapter to display a list of traffic items
 */
public class UserListAdapter extends SingleTypeAdapter<User> {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MMMM dd");

    /**
     * @param inflater
     * @param items
     */
    public UserListAdapter(final LayoutInflater inflater, final List<User> items) {
        super(inflater, R.layout.user_list_item);

        setItems(items);
    }

    /**
     * @param inflater
     */
    public UserListAdapter(final LayoutInflater inflater) {
        this(inflater, null);

    }

    @Override
    public long getItemId(final int position) {
        final String id = String.valueOf(getItem(position).getId());
        return !TextUtils.isEmpty(id) ? id.hashCode() : super
                .getItemId(position);
    }

    @Override
    protected int[] getChildViewIds() {
        return new int[]{R.id.iv_avatar, R.id.tv_name};
    }


    private void loadImage(Image image, ImageView imageItem) {
        ImageLoader.getInstance().loadImage(imageItem, image,
                ImageLoader.Placeholder.USER_PHOTO);
    }

    @Override
    protected void update(final int position, final User user) {
        setText(1, String.format("%1$s %2$s", user.getName(), user.getLastName()));
    }

}
