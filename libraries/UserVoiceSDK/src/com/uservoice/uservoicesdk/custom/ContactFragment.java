package com.uservoice.uservoicesdk.custom;

import android.os.Bundle;

import com.uservoice.uservoicesdk.R;
import com.uservoice.uservoicesdk.ui.ContactAdapter;
import com.uservoice.uservoicesdk.ui.InstantAnswersAdapter;

public class ContactFragment extends InstantAnswersFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected InstantAnswersAdapter createAdapter() {
        return new ContactAdapter(getActivity());
    }

    @Override
    protected int getDiscardDialogMessage() {
        return R.string.uv_msg_confirm_discard_message;
    }
}
