package br.com.mobiclub.quantoprima.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import br.com.mobiclub.quantoprima.MobiClubApplication;
import br.com.mobiclub.quantoprima.R;
import br.com.mobiclub.quantoprima.util.Ln;

public class TutorialActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);

        ViewPager pager = (ViewPager) findViewById(R.id.tutorial_pages);
        pager.setAdapter(new TutorialPagerAdapter(getSupportFragmentManager()));
    }

    public void onSkipTutorial(View view) {
        Ln.d("skipTutorial");
        onStart(view);
    }

    public void onStart(View view) {
        Ln.d("onStart");
        MobiClubApplication.getInstance().setFirstTime(false);
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    public class TutorialPagerAdapter extends FragmentPagerAdapter {

        public TutorialPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new TutorialPageOneFragment();
                case 1:
                    return new TutorialPageTowFragment();
                case 2:
                    return new TutorialPageThreeFragment();
                case 3:
                    return new TutorialPageFourFragment();
                default:
                    return new TutorialPageOneFragment();
            }
        }

        @Override
        public int getCount() {
            return 4;
        }
    }

    public static class TutorialPageOneFragment extends Fragment {

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_tutorial_page_one, container, false);
            return view;
        }

    }

    public static class TutorialPageTowFragment extends Fragment {

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_tutorial_page_two, container, false);
            return view;
        }

    }

    public static class TutorialPageThreeFragment extends Fragment {

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_tutorial_page_three, container, false);
            return view;
        }

    }

    public static class TutorialPageFourFragment extends Fragment {

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_tutorial_page_four, container, false);
            return view;
        }

    }
}
