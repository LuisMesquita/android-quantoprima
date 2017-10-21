package br.com.mobiclub.quantoprima.ui.fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.Loader;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.ListView;

import com.github.kevinsawicki.wishlist.SingleTypeAdapter;

import java.util.Collections;
import java.util.List;

import br.com.mobiclub.quantoprima.R;
import br.com.mobiclub.quantoprima.domain.Constants;
import br.com.mobiclub.quantoprima.domain.Mobi;
import br.com.mobiclub.quantoprima.facade.Facade;
import br.com.mobiclub.quantoprima.service.authenticator.LogoutService;
import br.com.mobiclub.quantoprima.ui.activity.mobi.MobiOfflineActivity;
import br.com.mobiclub.quantoprima.ui.adapter.MobisOfflineListAdapter;
import br.com.mobiclub.quantoprima.ui.factory.ProgressDialogFactory;
import br.com.mobiclub.quantoprima.ui.view.ThrowableLoader;
import br.com.mobiclub.quantoprima.util.Ln;

/**
 * A fragment representing a list of Items.
 *
 */
public class MobisOfflineFragment extends ItemListFragment<Mobi> {

    private LocalBroadcastManager broadcastManager;
    private MobiSyncReceiver mobiSyncReceiver;
    private Facade facade;
    private ProgressDialog progress;
    private boolean happenError = false;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public MobisOfflineFragment() {
    }

    private class MobiSyncReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle extras = intent.getExtras();

            Integer i = (Integer) extras.get(Constants.Extra.MOBI_SYNCED);
            Ln.d("Recebeu da sincronização %d mobis sincronizados", i);

            if(isAdded()) {
                if(i > 0) {
                    getLoaderManager().restartLoader(0, null, MobisOfflineFragment.this);
                    happenError = false;
                }
                else if(!happenError) {
                    happenError = true;
                    progress.dismiss();
                    //houve erro de conexao
                    showError();
                }
            }
        }

        private void showError() {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    FragmentActivity activity = getActivity();
                    if(activity != null && activity instanceof MobiOfflineActivity) {
                        MobiOfflineActivity a = (MobiOfflineActivity) activity;
                        a.onUpdateError();
                        a.onBackPressed();
                    }
                }
            });
            builder.setTitle(R.string.dialog_title_error);
            builder.setMessage(R.string.server_connection_error);
            builder.create().show();
        }

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        facade = Facade.getInstance();

        progress = ProgressDialogFactory.createProgress(getActivity(), R.string.loading_message);
        progress.show();

        IntentFilter mStatusIntentFilter = new IntentFilter(Constants.Extra.MOBI_SYNC_BROADCAST_ACTION);
        mobiSyncReceiver = new MobiSyncReceiver();
        broadcastManager = LocalBroadcastManager.getInstance(getActivity());
        broadcastManager.registerReceiver(
                mobiSyncReceiver,
                mStatusIntentFilter);
    }

    @Override
    public void onLoadFinished(Loader<List<Mobi>> loader, List<Mobi> items) {
        progress.dismiss();
        super.onLoadFinished(loader, items);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            broadcastManager.unregisterReceiver(mobiSyncReceiver);
        } catch (Exception e) {
        }
    }

    @Override
    protected LogoutService getLogoutService() {
        return null;
    }

    @Override
    protected int getErrorMessage(Exception exception) {
        return 0;
    }

    @Override
    protected SingleTypeAdapter<Mobi> createAdapter(List<Mobi> items) {
        return new MobisOfflineListAdapter(getActivity().getLayoutInflater(),
                getResources(), items);
    }


    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
    }

    @Override
    public Loader<List<Mobi>> onCreateLoader(int id, Bundle args) {
        final List<Mobi> initialItems = items;

        return new ThrowableLoader<List<Mobi>>(getActivity(), items) {
            @Override
            public List<Mobi> loadData() throws Exception {
                try {
                    List<Mobi> latest = null;
                    if (getActivity() != null) {
                        latest = facade.getAllMobis();
                        Collections.sort(latest);
                        Ln.d("Exibindo %s mobis offline", latest.size());
                    }
                    if(latest != null) {
                        return latest;
                    } else {
                        return Collections.emptyList();
                    }
                } catch (Exception e) {
                    Ln.d(e);
                    return initialItems;
                }
            }
        };
    }

}
