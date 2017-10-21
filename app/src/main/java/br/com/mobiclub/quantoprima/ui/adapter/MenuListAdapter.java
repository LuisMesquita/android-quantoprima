package br.com.mobiclub.quantoprima.ui.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.kevinsawicki.wishlist.SingleTypeAdapter;

import java.util.List;

import br.com.mobiclub.quantoprima.R;
import br.com.mobiclub.quantoprima.core.service.api.entity.Image;
import br.com.mobiclub.quantoprima.domain.MobiClubUser;
import br.com.mobiclub.quantoprima.facade.Facade;
import br.com.mobiclub.quantoprima.ui.view.ImageLoader;
import br.com.mobiclub.quantoprima.domain.Constants;

/**
 *
 */
public class MenuListAdapter extends SingleTypeAdapter<MenuItem> {

    private Facade facade;

    public MenuListAdapter(Activity activity, List<MenuItem> items) {
        super(activity, R.layout.list_item_menu);
        setItems(items);
        facade = Facade.getInstance();
    }

    @Override
    protected int[] getChildViewIds() {
        return new int[] {
                R.id.image_profile, R.id.image_icon, R.id.label_user_name,
                R.id.label_item, R.id.item_profile, R.id.item_menu, R.id.frame_notifications,
                R.id.menu_notifications_count
        };
    }

    private void loadImage(Image image, ImageView imageItem) {
        ImageLoader.getInstance().loadImage(imageItem, image,
                ImageLoader.Placeholder.USER_PHOTO);
    }

    @Override
    protected void update(int position, MenuItem menuItem) {
        if(position != 0) {
            LinearLayout itemProfile = getView(4, LinearLayout.class);
            itemProfile.setVisibility(View.GONE);
            LinearLayout itemMenu = getView(5, LinearLayout.class);
            itemMenu.setVisibility(View.VISIBLE);
        } else {
            //item perfil
            MobiClubUser loggedUser = facade.getLoggedUser();
            if(loggedUser != null) {
                String profilePicture = loggedUser.getProfilePicture();
                loadImage(new Image(profilePicture), imageView(0));
            }
            LinearLayout itemMenu = getView(5, LinearLayout.class);
            itemMenu.setVisibility(View.GONE);
            LinearLayout itemProfile = getView(4, LinearLayout.class);
            itemProfile.setVisibility(View.VISIBLE);
            setText(2, menuItem.getTitle());
            return;
        }
        if(menuItem.getItemId() == R.id.menu_notification) {
            Intent intent = menuItem.getIntent();
            if(intent != null) {
                int notifications = intent.getIntExtra(Constants.Extra.NOTIFICATION_COUNT, 0);
                if(notifications > 0) {
                    getView(6, FrameLayout.class).setVisibility(View.VISIBLE);
                    getView(7, TextView.class).setText(String.valueOf(notifications));
                } else {
                    getView(6, FrameLayout.class).setVisibility(View.GONE);
                }
            }
        }
        if(menuItem.getIcon() != null) {
            ImageView icon = getView(1, ImageView.class);
            icon.setImageDrawable(menuItem.getIcon());
        }
        if(menuItem.getTitle() != null) {
            setText(3, menuItem.getTitle());
        }
    }

}
