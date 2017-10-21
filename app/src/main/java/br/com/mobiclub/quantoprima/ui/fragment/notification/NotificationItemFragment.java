package br.com.mobiclub.quantoprima.ui.fragment.notification;


import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Date;

import br.com.mobiclub.quantoprima.R;
import br.com.mobiclub.quantoprima.core.service.api.entity.Notification;
import br.com.mobiclub.quantoprima.domain.Constants;
import br.com.mobiclub.quantoprima.util.Util;
import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * A simple {@link Fragment} subclass.
 *
 */
public class NotificationItemFragment extends Fragment {

    @InjectView(R.id.label_reference)
    TextView labelReference;

    @InjectView(R.id.label_time)
    TextView labelTime;

    @InjectView(R.id.label_title)
    TextView labelTitle;

    @InjectView(R.id.label_message_content)
    TextView labelMessageContent;

    @InjectView(R.id.image_bullet)
    ImageView imageBullet;

    private Notification notification;

    public NotificationItemFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent arguments = getActivity().getIntent();
        if(arguments != null) {
            notification = (Notification) arguments.getSerializableExtra(Constants.Extra.NOTIFICATION);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notification_item, container, false);

        ButterKnife.inject(this, view);

        if(notification == null) {
            return view;
        }

        if(notification.getRead()) {
            imageBullet.setImageResource(R.drawable.t14_mensagem_lida_marcador);
            Resources resources = getActivity().getResources();
            labelReference.setTextColor(resources.getColor(R.color.grey8));
            //labelTime.setTextColor(resources.getColor(R.color.grey8));
            //labelTitle.setTextColor(resources.getColor(R.color.grey8));
        } else {
            imageBullet.setImageResource(R.drawable.t14_mensagem_naolida_marcador);
            //labelReference.setTextAppearance(activity, R.style.Notification_Title);
            //labelTime.setTextAppearance(activity, R.style.Notification_Time);
            //labelTitle.setTextColor(resources.getColor(R.color.black));
        }

        String reference = notification.getReference();
        String name = notification.getName();
        String finalName = name;
        if(finalName == null) {
            finalName = reference;
        }
        if(finalName != null)
            labelReference.setText(finalName);

        Date date = notification.getSendAt();
        String formatDate = Util.formatNotificationDate(date);
        if(formatDate != null) {
            labelTime.setText(formatDate);
        }
        String title = notification.getTitle();
        if(title != null)
            labelTitle.setText(title);
        String text = notification.getText();
        if(text != null)
            labelMessageContent.setText(text);

        return view;
    }

}
