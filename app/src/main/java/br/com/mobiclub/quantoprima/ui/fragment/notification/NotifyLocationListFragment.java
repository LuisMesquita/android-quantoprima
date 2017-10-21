package br.com.mobiclub.quantoprima.ui.fragment.notification;

import android.accounts.OperationCanceledException;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.Loader;
import android.widget.ListView;

import com.github.kevinsawicki.wishlist.SingleTypeAdapter;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import br.com.mobiclub.quantoprima.R;
import br.com.mobiclub.quantoprima.config.Injector;
import br.com.mobiclub.quantoprima.core.service.MobiClubServiceApi;
import br.com.mobiclub.quantoprima.core.service.MobiClubServiceProvider;
import br.com.mobiclub.quantoprima.core.service.api.entity.ApiError;
import br.com.mobiclub.quantoprima.core.service.api.entity.ApiResult;
import br.com.mobiclub.quantoprima.core.service.api.entity.Establishment;
import br.com.mobiclub.quantoprima.core.service.api.entity.EstablishmentLocation;
import br.com.mobiclub.quantoprima.core.service.api.entity.NotifyLocationsWrapper;
import br.com.mobiclub.quantoprima.core.service.api.entity.Reason;
import br.com.mobiclub.quantoprima.core.service.api.exception.AppBlockedException;
import br.com.mobiclub.quantoprima.core.service.api.exception.AppOutdatedException;
import br.com.mobiclub.quantoprima.core.service.api.exception.UserBlockedException;
import br.com.mobiclub.quantoprima.events.NetworkErrorEvent;
import br.com.mobiclub.quantoprima.events.RestErrorEvent;
import br.com.mobiclub.quantoprima.service.authenticator.LogoutService;
import br.com.mobiclub.quantoprima.ui.activity.AppInactiveActivity;
import br.com.mobiclub.quantoprima.ui.activity.account.AccountBlockedActivity;
import br.com.mobiclub.quantoprima.ui.activity.launch.OutdatedActivity;
import br.com.mobiclub.quantoprima.ui.adapter.notification.NotifyLocationListAdapter;
import br.com.mobiclub.quantoprima.ui.adapter.notification.NotifyLocationListAdapterListener;
import br.com.mobiclub.quantoprima.ui.factory.AlertDialogFactory;
import br.com.mobiclub.quantoprima.ui.factory.ProgressDialogFactory;
import br.com.mobiclub.quantoprima.ui.fragment.ItemListFragment;
import br.com.mobiclub.quantoprima.ui.view.ThrowableLoader;
import br.com.mobiclub.quantoprima.domain.Constants;
import br.com.mobiclub.quantoprima.util.Ln;
import br.com.mobiclub.quantoprima.util.SafeAsyncTask;
import butterknife.ButterKnife;
import retrofit.RetrofitError;

public class NotifyLocationListFragment extends ItemListFragment<EstablishmentLocation>
        implements NotifyLocationListAdapterListener {

    private static final int DEFAULT_REASON_BLOCK = 1;
    @Inject MobiClubServiceProvider serviceProvider;

    @Inject
    Bus bus;

    private AlertDialog successAlert;
    private List<Reason> reasons;
    private Integer[] reasonsIds;
    private String[] reasonsString;
    private NotifyLocationListListener listener;
    private String messageError;
    private EstablishmentLocation location;
    private boolean errorOcurred = false;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Injector.inject(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        bus.register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        bus.unregister(this);
    }

    @Override
    protected LogoutService getLogoutService() {
        return null;
    }

    @Override
    public void onActivityCreated(final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setEmptyText(R.string.no_notify_locations);

        ButterKnife.inject(this, getView());

        try {
            listener = (NotifyLocationListListener) getParentFragment();
        } catch (Exception e) {
            throw new IllegalStateException(getParentFragment().toString() +
                    "deve implementar NotifyLocationListListener");
        }
    }

    @Override
    protected void configureList(final Activity activity, final ListView listView) {
        super.configureList(activity, listView);

        listView.setDividerHeight(1);
    }

    @Subscribe
    public void onNetworkErrorEvent(NetworkErrorEvent e) {
        String message = getString(R.string.service_comunication_error);
        notifySwitchError(message);
    }

    @Subscribe
    public void onRestErrorEvent(RestErrorEvent e) {
        ApiError apiError = e.getApiError();
        String message = null;
        if(apiError != null)
            message = apiError.getMessage();
        notifySwitchError( message);
    }

    @Override
    public void onSwitched(final EstablishmentLocation location,
                           final boolean isChecked) {
        Ln.d("onSwitched: " + location.getReference());
        this.location = location;

        if(location.getId() == this.location.getId() && errorOcurred) {
            errorOcurred = false;
            return;
        }

        if(getActivity() == null) {
            return;
        }

        if(getActivity() != null && reasons == null) {
            return;
        }

        if(isChecked) {
            onReasonChoose(location, null, isChecked);
        } else {
            final String[] reasons = reasonsString;
            AlertDialog.Builder builder = AlertDialogFactory.createListAlertDialog(getActivity(),
                    R.string.pick_reason, reasons);
            builder.setItems(reasons, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    try {
                        int reason = reasonsIds[which];
                        onReasonChoose(location, reason, isChecked);
                    } catch (Exception e) {
                        onReasonChoose(location, DEFAULT_REASON_BLOCK, isChecked);
                    }
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.setCancelable(false);
            alertDialog.show();
        }
    }

    private void cacheReasons() {
        reasonsIds = new Integer[reasons.size()];
        reasonsString = new String[reasons.size()];
        for (int i = 0; i < reasons.size(); i++) {
            Reason reason = reasons.get(i);
            reasonsIds[i] = reason.getId();
            reasonsString[i] = reason.getReasonText();
        }
    }

    private void onReasonChoose(final EstablishmentLocation location,
                                final Integer reason, final boolean isChecked) {
        final ProgressDialog progress;
        if(isChecked) {
            progress = ProgressDialogFactory.createProgress(getActivity(),
                    R.string.progress_dialog_notify_unblock_message);
        } else {
            progress = ProgressDialogFactory.createProgress(getActivity(),
                    R.string.progress_dialog_notify_block_message);
        }
        progress.show();
        new SafeAsyncTask<Boolean>() {

            @Override
            public Boolean call() throws Exception {
                FragmentActivity activity = getActivity();
                if(activity != null) {
                    MobiClubServiceApi service = serviceProvider.getService(activity);
                    if(service == null) {
                        return false;
                    }
                    ApiResult result;
                    if(isChecked) {
                        Ln.d("Chamando servico para desbloquear loja");
                        result = service.notifyUnBlock(location.getId());
                    } else {
                        Ln.d("Chamando servico para bloquear loja %s com razao %s",
                                location.getReference(), reasons.get(reason).getReasonText());
                        result = service.notifyBlock(location.getId(), reason);
                    }
                    messageError = result.getMessage();
                    return result.isSuccess();
                }
                return false;
            }

            @Override
            protected void onException(Exception exception) throws RuntimeException {
                super.onException(exception);
                progress.dismiss();
                Intent intent = null;
                if(exception instanceof AppBlockedException) {
                    Ln.d("AppBlockedException");
                    intent = new Intent(getActivity(), AppInactiveActivity.class);
                } else if(exception instanceof AppOutdatedException) {
                    Ln.d("AppOutdatedException");
                    intent = new Intent(getActivity(), OutdatedActivity.class);
                } else if(exception instanceof UserBlockedException) {
                    Ln.d("UserBlockedException");
                    intent = new Intent(getActivity(), AccountBlockedActivity.class);
                    intent.putExtra(Constants.Extra.REASON_USER_BLOCKED, exception.getMessage());
                }
                if(intent != null) {
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    //getActivity().finish();
                    return;
                }
                if(!(exception instanceof RetrofitError)) {
                    Ln.d(exception);
                    notifySwitchError(getString(R.string.notify_location_error));
                }
            }

            @Override
            protected void onSuccess(Boolean success) throws Exception {
                super.onSuccess(success);
                if(success) {
                    location.setBlocked(!isChecked);
                }
                errorOcurred = false;
                progress.dismiss();
                if(!success) {
                    Ln.e("Houve sucesso na requisicao mas o HttpStatus da mensagem " +
                            "não foi igual a 200");
                    String message = getString(R.string.notify_location_error);
                    if(messageError != null) {
                        message = messageError;
                    }
                    notifySwitchError(message);
                }
            }

        }.execute();
    }

    private void notifySwitchError(String message) {
        this.errorOcurred = true;
        AlertDialogFactory.createDefaultError(getActivity(), message).show();
        if(getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    NotifyLocationListAdapter adapter = (NotifyLocationListAdapter) getListView().getAdapter();
                    adapter.setItems(items);
                    adapter.notifyDataSetChanged();
                }
            });
        }
    }

    @Override
    public Loader<List<EstablishmentLocation>> onCreateLoader(final int id, final Bundle args) {
        final List<EstablishmentLocation> initialItems = items;
        return new ThrowableLoader<List<EstablishmentLocation>>(getActivity(), items) {
            @Override
            public List<EstablishmentLocation> loadData() throws Exception {
                try {
                    List<EstablishmentLocation> latest = null;
                    if (getActivity() != null) {
                        MobiClubServiceApi service = serviceProvider.getService(getActivity());
                        List<EstablishmentLocation> locations = new ArrayList<EstablishmentLocation>();
                        NotifyLocationsWrapper notifyLocations = service.getNotifyLocations();
                        List<Establishment> establishments = notifyLocations.getEstablishments();
                        reasons = notifyLocations.getReasons();
                        cacheReasons();
                        for(Establishment e : establishments) {
                            if(e.isGroup()) {
                                for(EstablishmentLocation el : e.getLocations()) {
                                    el.setEstablishment(e);
                                    locations.add(el);
                                }
                            } else {
                                EstablishmentLocation location = e.getLocation();
                                location.setEstablishment(e);
                                locations.add(location);
                            }
                        }
                        latest = locations;
                    }

                    if (latest != null) {
                        return latest;
                    } else {
                        return Collections.emptyList();
                    }
                } catch (final OperationCanceledException e) {
                    final Activity activity = getActivity();
                    if (activity != null) {
                        activity.finish();
                    }
                    return initialItems;
                }
            }
        };
    }

    @Override
    public void onLoadFinished(final Loader<List<EstablishmentLocation>> loader,
                               final List<EstablishmentLocation> items) {
        if(items.isEmpty()) {
            //nenhum resultado
            Ln.d("nocontent notify");
            showNoContentFragment(R.layout.no_content_location_notifications);
        }
        super.onLoadFinished(loader, items);
    }

    @Override
    protected int getErrorMessage(final Exception exception) {
        return R.string.server_connection_error;
    }

    @Override
    protected SingleTypeAdapter<EstablishmentLocation> createAdapter(final List<EstablishmentLocation> items) {
        return new NotifyLocationListAdapter(this, getActivity().getLayoutInflater(),
                getResources(), items);
    }
}
