package br.com.mobiclub.quantoprima.ui.fragment.notification;



import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import br.com.mobiclub.quantoprima.R;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.Optional;

/**
 * A simple {@link Fragment} subclass.
 *
 */
public class NotificationsConfigFragment extends Fragment
                                         implements NotifyLocationListListener {

    //TODO: colocar swicth comum a partir do 2.2
    @InjectView(R.id.btn_switch)
    @Optional
    CompoundButton btnSwitch;

    public NotificationsConfigFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notifications_config, container, false);

        ButterKnife.inject(this, view);

        if(btnSwitch != null) {
            btnSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        enableAll();
                    } else {
                        disableAll();
                    }
                }

            });
        }

        FragmentManager fm = getChildFragmentManager();
        NotifyLocationListFragment fragment = new NotifyLocationListFragment();
        fm.beginTransaction().replace(R.id.location_list, fragment).commit();

        return view;
    }

    private void disableAll() {
        //TODO: disable all
    }

    private void enableAll() {
        //TODO: enable all
    }

    @Override
    public void onSwitchAllError() {
        //TODO:
    }
}
