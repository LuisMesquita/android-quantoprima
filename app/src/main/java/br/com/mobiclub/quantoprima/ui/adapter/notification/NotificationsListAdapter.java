package br.com.mobiclub.quantoprima.ui.adapter.notification;

import android.app.Activity;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.kevinsawicki.wishlist.SingleTypeAdapter;

import java.util.Date;
import java.util.List;

import br.com.mobiclub.quantoprima.R;
import br.com.mobiclub.quantoprima.core.service.api.entity.Notification;
import br.com.mobiclub.quantoprima.util.NotificacaoCallBack;
import br.com.mobiclub.quantoprima.util.Util;
import butterknife.ButterKnife;

/**
 *
 */
public class NotificationsListAdapter extends SingleTypeAdapter<Notification> {

    private final Activity activity;
    private final LayoutInflater layoutInflater;
    private Resources resources;
    public static NotificacaoCallBack callBack;

    public NotificationsListAdapter(Activity activity, LayoutInflater layoutInflater, Resources resources,
                                    List<Notification> items) {
        super(layoutInflater, R.layout.list_item_notification);
        this.activity = activity;
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
            R.id.image_bullet, R.id.label_reference, R.id.label_time,
                R.id.label_title, R.id.label_content, R.id.imgBtExcluir
        };
    }

    @Override
    protected void update(final int position, final Notification notification) {
        String reference = notification.getReference();
        String name = notification.getName();
        String finalName = name;
        if(finalName == null) {
            finalName = reference;
        }
        if(finalName != null)
            setText(1, finalName);

        if(notification.getRead()) {
            getView(0, ImageView.class).setImageResource(R.drawable.t14_mensagem_lida_marcador);
            getView(1, TextView.class).setTextColor(resources.getColor(R.color.grey8));
            getView(2, TextView.class).setTextColor(resources.getColor(R.color.grey8));
            getView(3, TextView.class).setTextColor(resources.getColor(R.color.grey8));
            getView(4, TextView.class).setTextColor(resources.getColor(R.color.grey8));
            getView(5, ImageButton.class).setVisibility(View.VISIBLE);
        } else {
            getView(0, ImageView.class).setImageResource(R.drawable.t14_mensagem_naolida_marcador);
            getView(1, TextView.class).setTextAppearance(activity, R.style.Notification_Title);
            getView(2, TextView.class).setTextAppearance(activity, R.style.Notification_Time);
            getView(3, TextView.class).setTextColor(resources.getColor(R.color.black));
            getView(4, TextView.class).setTextColor(resources.getColor(R.color.grey8));
            getView(5, ImageButton.class).setVisibility(View.GONE);
        }

        Date date = notification.getSendAt();
        if(date != null) {
            String formatDate = Util.formatNotificationDate(date);
            setText(2, formatDate);
        }
        getView(5, ImageButton.class).setTag(name);
        getView(5, ImageButton.class).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBack.removerNotificacao(notification);
            }
        });
        getView(1, TextView.class).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBack.openNotificacao(notification);
            }
        });

        getView(3, TextView.class).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBack.openNotificacao(notification);
            }
        });

        String title = notification.getTitle();
        if(title != null)
            setText(3, title);
        String text = notification.getText();
        if(text != null)
            setText(4, text);
    }

}
