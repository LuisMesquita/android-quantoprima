package com.uservoice.uservoicesdk.custom;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;

import com.uservoice.uservoicesdk.flow.InitManager;
import com.uservoice.uservoicesdk.ui.InstantAnswersAdapter;

public abstract class InstantAnswersFragment extends FragmentList {

    private InstantAnswersAdapter adapter;

    public InstantAnswersFragment() {
        super();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        onContentChanged();

        getListView().setDivider(null);

        adapter = createAdapter();
        setListAdapter(adapter);
        getListView().setOnHierarchyChangeListener(adapter);
        getListView().setOnItemClickListener(adapter);
        getListView().setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);
        new InitManager(getActivity(), new Runnable() {
            @Override
            public void run() {
                onInitialize();
            }
        }).init();
    }

    protected void onInitialize() {
        adapter.notifyDataSetChanged();
    }

    protected abstract InstantAnswersAdapter createAdapter();

    protected abstract int getDiscardDialogMessage();

}
