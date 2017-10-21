package com.uservoice.uservoicesdk.custom;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.uservoice.uservoicesdk.R;

/**
 * <em>Copy from Android source to enable {@link android.support.v4.app.Fragment} support.</em>
 *
 * @see android.app.ListActivity
 */
public abstract class FragmentList extends Fragment {

    // changed to private as original suggested
    private ListAdapter mAdapter;
    // changed to private as original suggested
    private ListView mList;

    private Handler mHandler = new Handler();
    private boolean mFinishedStart = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private Runnable mRequestFocus = new Runnable() {
        public void run() {
            mList.focusableViewAvailable(mList);
        }
    };

    /**
     * This method will be called when an item in the list is selected.
     * Subclasses should override. Subclasses can call
     * getListView().getItemAtPosition(position) if they need to access the data
     * associated with the selected item.
     *
     * @param l        The ListView where the click happened
     * @param v        The view that was clicked within the ListView
     * @param position The position of the view in the list
     * @param id       The row id of the item that was clicked
     */
    protected void onListItemClick(ListView l, View v, int position, long id) {
    }

    /**
     * Ensures the list view has been created before Activity restores all of
     * the view states.
     *
     * @see android.app.Activity#onRestoreInstanceState(android.os.Bundle)
     */
//    @Override
//    protected void onRestoreInstanceState(Bundle state) {
//        ensureList();
//        super.onRestoreInstanceState(state);
//    }

    /**
     * Updates the screen state (current list and other views) when the content
     * changes.
     *
     * @see android.app.Activity#onContentChanged()
     */
    public void onContentChanged() {
        // changed references from com.android.internal.R to android.R.*
        //View emptyView = getView().findViewById(android.R.id.empty);
        mList = (ListView) getView().findViewById(android.R.id.list);

        if (mList == null) {
            throw new RuntimeException("Your content must have a ListView whose id attribute is " + "'android.R.id.list'");
        }
        //if (emptyView != null) {
        //    mList.setEmptyView(emptyView);
        //}
        //mList.setOnItemClickListener(mOnClickListener);
        //if (mFinishedStart) {
        //    setListAdapter(mAdapter);
        //}
        //mHandler.post(mRequestFocus);
        mFinishedStart = true;
    }

    /**
     * Provide the cursor for the list view.
     */
    public void setListAdapter(ListAdapter adapter) {
        synchronized (this) {
            ensureList();
            mAdapter = adapter;
            mList.setAdapter(adapter);
        }
    }

    /**
     * Set the currently selected list item to the specified position with the
     * adapter's data
     *
     * @param position
     */
    public void setSelection(int position) {
        mList.setSelection(position);
    }

    /**
     * Get the position of the currently selected list item.
     */
    public int getSelectedItemPosition() {
        return mList.getSelectedItemPosition();
    }

    /**
     * Get the cursor row ID of the currently selected list item.
     */
    public long getSelectedItemId() {
        return mList.getSelectedItemId();
    }

    /**
     * Get the activity's list view widget.
     */
    public ListView getListView() {
        ensureList();
        return mList;
    }

    /**
     * Get the ListAdapter associated with this activity's ListView.
     */
    public ListAdapter getListAdapter() {
        return mAdapter;
    }

    private void ensureList() {
        if (mList != null) {
            return;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.uv_list_content, container, false);
        return view;
    }

    private AdapterView.OnItemClickListener mOnClickListener = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
            onListItemClick((ListView) parent, v, position, id);
        }
    };
}