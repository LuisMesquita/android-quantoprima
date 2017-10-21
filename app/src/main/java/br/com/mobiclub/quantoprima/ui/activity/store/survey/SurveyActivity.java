package br.com.mobiclub.quantoprima.ui.activity.store.survey;

import android.support.v4.app.Fragment;

import com.squareup.otto.Subscribe;

import br.com.mobiclub.quantoprima.events.NavItemSelectedEvent;
import br.com.mobiclub.quantoprima.ui.activity.MenuActivity;
import br.com.mobiclub.quantoprima.ui.fragment.store.SurveyFragment;
import br.com.mobiclub.quantoprima.ui.view.DialogListener;

public class SurveyActivity extends MenuActivity implements DialogListener {

    @Subscribe
    @Override
    public void onNavigationItemSelected(NavItemSelectedEvent event) {
        super.onNavigationItemSelectedImpl(event);
    }

    @Override
    protected Fragment createFragment() {
        SurveyFragment surveyFragment = new SurveyFragment();
        return surveyFragment;
    }

    @Override
    public void onCloseResult(int result) {
        if(result == DialogListener.RESULT_OK) {
            super.finishOnDialog();
        }
    }

}
