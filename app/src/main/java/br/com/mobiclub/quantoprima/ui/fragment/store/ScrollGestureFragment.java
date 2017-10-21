package br.com.mobiclub.quantoprima.ui.fragment.store;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

import br.com.mobiclub.quantoprima.ui.fragment.ItemListFragment;
import br.com.mobiclub.quantoprima.ui.fragment.common.ListScrollListener;

/**
 */
public abstract class ScrollGestureFragment<E> extends ItemListFragment<E> {

    private ListScrollListener listener;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            listener = (ListScrollListener) activity;
        } catch (ClassCastException e) {
            throw e;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    @Override
    protected void configureList(Activity activity, ListView listView) {
        super.configureList(activity, listView);

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if(scrollState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    onScrollStarted();
                } else if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    onScrollStopped();
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                //Ln.v("onScroll");
                View childAt = view.getChildAt(0);
                if (childAt != null && childAt.getTop() == 0) {
                    swipeLayout.setEnabled(true);
                }
                else {
                    swipeLayout.setEnabled(false);
                }
                int i = (visibleItemCount + firstVisibleItem) - totalItemCount;
                if(visibleItemCount != 0 && i >=0 && i <= 4) {
                    listener.reachListEnd(true);
                } else {
                    listener.reachListEnd(false);
                }
            }
        });
    }

    private void onScrollStopped() {
        if(listener != null)
            listener.showButtonGainMobis(true);
    }

    private void onScrollStarted() {
        if(listener != null)
            listener.showButtonGainMobis(false);
    }

}
