package br.com.mobiclub.quantoprima.ui.fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import br.com.mobiclub.quantoprima.R;
import br.com.mobiclub.quantoprima.ui.activity.WebViewActivity;
import br.com.mobiclub.quantoprima.ui.factory.AlertDialogFactory;
import br.com.mobiclub.quantoprima.domain.Constants;
import br.com.mobiclub.quantoprima.util.NetworkUtil;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;

/**
 */
public class FragmentNoContent extends Fragment {

    private int layout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle arguments = getArguments();
        if(arguments != null)
            layout = arguments.getInt(Constants.Extra.NO_CONTENT_LAYOUT);
    }

    @OnClick(R.id.label_how_to_reward)
    @Optional
    public void howToReward() {
        FragmentActivity activity = getActivity();
        if(activity == null)
            return;
        if(!NetworkUtil.isConnected(activity)) {
            AlertDialog defaultError = AlertDialogFactory.createDefaultError(activity, R.string.alert_title_attention,
                    R.string.alert_title_webview_no_connection);
            defaultError.setCancelable(true);
            defaultError.show();
            return;
        }
        Intent intent = new Intent(getActivity(), WebViewActivity.class);
        intent.putExtra(Constants.Extra.WEB_VIEW_URL, Constants.URL.COMO_GANHAR_RECOMPENSA);
        intent.putExtra(Constants.Extra.WEB_VIEW_TITLE, getString(R.string.web_view_how_to_gain_reward_title));
        startActivity(intent);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(layout, container, false);

        ButterKnife.inject(this, view);

        return view;
    }
}
