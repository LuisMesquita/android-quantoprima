package br.com.mobiclub.quantoprima.ui.fragment.notification;


import android.accounts.OperationCanceledException;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.Loader;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.github.kevinsawicki.wishlist.TypeAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import br.com.mobiclub.quantoprima.R;
import br.com.mobiclub.quantoprima.config.Injector;
import br.com.mobiclub.quantoprima.core.service.MobiClubServiceApi;
import br.com.mobiclub.quantoprima.core.service.MobiClubServiceProvider;
import br.com.mobiclub.quantoprima.core.service.api.entity.ApiResult;
import br.com.mobiclub.quantoprima.core.service.api.entity.Notification;
import br.com.mobiclub.quantoprima.domain.MobiClubUser;
import br.com.mobiclub.quantoprima.facade.Facade;
import br.com.mobiclub.quantoprima.service.authenticator.LogoutService;
import br.com.mobiclub.quantoprima.ui.activity.notification.NotificationItemActivity;
import br.com.mobiclub.quantoprima.ui.adapter.notification.NotificationsListAdapter;
import br.com.mobiclub.quantoprima.ui.fragment.ItemListFragment;
import br.com.mobiclub.quantoprima.ui.view.ThrowableLoader;
import br.com.mobiclub.quantoprima.domain.Constants;
import br.com.mobiclub.quantoprima.util.Ln;
import br.com.mobiclub.quantoprima.util.NotificacaoCallBack;
import br.com.mobiclub.quantoprima.util.SafeAsyncTask;

/**
 * A simple {@link Fragment} subclass.
 *
 */
public class NotificationListFragment extends ItemListFragment<Notification> implements NotificacaoCallBack {

    @Inject
    MobiClubServiceProvider serviceProvider;
    private int page = 0;
    private Button btnLoadMore;
    private Facade facade;
    private boolean created;
    private Notification notification;
    private Intent starterIntent;
    private MobiClubServiceApi service;


    public NotificationListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        facade = Facade.getInstance();
        Injector.inject(this);
        starterIntent = getActivity().getIntent();
        created = true;
    }

    @Override
    public void onPause() {
        super.onPause();
        created = false;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(!created) {
            Ln.d("Updating...");
            //getLoaderManager().restartLoader(0, getLoaderParams(), this);
            startActivity(starterIntent);
            getActivity().finish();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    protected LogoutService getLogoutService() {
        return null;
    }

    @Override
    protected int getErrorMessage(Exception exception) {
        return R.string.no_notifications;
    }

    @Override
    protected TypeAdapter createAdapter(List<Notification> items) {
        return new NotificationsListAdapter(getActivity(), getActivity().getLayoutInflater(),
                getResources(), items);
    }

    @Override
    protected void configureList(Activity activity, ListView listView) {
        // Creating a button - Load More
//        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
//        ViewGroup root = new LinearLayout(activity);
//        View view = layoutInflater.inflate(R.layout.button_load_more,
//                root , true);
//        Button btnLoadMore = (Button) view.findViewById(R.id.btn_load_more);
//        AbsListView.LayoutParams params = new AbsListView.LayoutParams(AbsListView.LayoutParams.WRAP_CONTENT, AbsListView.LayoutParams.FILL_PARENT);
//        btnLoadMore.setLayoutParams(params);

        btnLoadMore = new Button(activity);
        btnLoadMore.setText(R.string.notification_list_btn_load_more);
        btnLoadMore.setTextSize(16);
        btnLoadMore.setTextColor(activity.getResources().getColor(R.color.text_black));
        btnLoadMore.setBackgroundResource(R.drawable.t11_btn_conectar);

        /**
         * Listening to Load More button click event
         * */
        btnLoadMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Starting a new async task
                loadNewPage();
            }
        });

        // Adding button to listview at footer
        listView.addFooterView(btnLoadMore);

        super.configureList(activity, listView);

        listView.setDivider(this.getResources().getDrawable(R.drawable.transparent_drawable));
        listView.setDividerHeight(0);
        NotificationsListAdapter.callBack = this;
    }

    @Override
    protected Bundle getLoaderParams() {
        Bundle args = new Bundle();
        args.putInt(Constants.Extra.NOTIFICATION_PAGE, page);
        return args;
    }

    private void loadNewPage() {
        page++;
        getLoaderManager().restartLoader(0, getLoaderParams(), this);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        MobiClubUser loggedUser = facade.getLoggedUser();
        if(loggedUser != null) {
            loggedUser.readNotification();
        }
        notification = items.get(position);
        if(!notification.getRead()) {
            Ln.d("Nao esta lida");
            readNotification(notification);
            notification.setRead(true);
        } else {
            Ln.d("Esta lida");
        }
        Intent intent = new Intent(getActivity(), NotificationItemActivity.class);
        intent.putExtra(Constants.Extra.NOTIFICATION, notification);
        startActivity(intent);
    }

    private void readNotification(final Notification notification) {
        new SafeAsyncTask<Boolean>() {

            @Override
            public Boolean call() throws Exception {
                MobiClubServiceApi service = serviceProvider.getService(getActivity());
                ApiResult result = service.readNotifyMessage(notification.getId());
                return result.isSuccess();
            }

            @Override
            protected void onException(Exception e) throws RuntimeException {
                super.onException(e);
                Ln.e("Error ao ler mensagem: %s", notification.getId());
            }

            @Override
            protected void onSuccess(Boolean aBoolean) throws Exception {
                super.onSuccess(aBoolean);
                if(aBoolean) {
                    Ln.d("Sucesso ao ler mensagem: %s", notification.getId());
                } else {
                    Ln.e("Error ao ler mensagem: %s", notification.getId());
                }
            }

        }.execute();
    }

    @Override
    public Loader<List<Notification>> onCreateLoader(final int id, Bundle args) {
        final List<Notification> initialItems = items;
        return new ThrowableLoader<List<Notification>>(getActivity(), items) {
            @Override
            public List<Notification> loadData() throws Exception {
                try {
                    List<Notification> result = new ArrayList<Notification>();
                    result.addAll(initialItems);

                    if (getActivity() != null && serviceProvider != null) {
                        MobiClubServiceApi service = serviceProvider.getService(getActivity());
                        Ln.d("Carregando pagina %d", page);
                        List<Notification> latest = service.getNotifications(page + 1);
                        Collections.sort(latest);
                        if(latest != null)
                            result.addAll(latest);
                    } else {
                        Ln.e("Fragmento em estado invalido");
                    }

                    return result;
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
    public void onLoadFinished(Loader<List<Notification>> loader, List<Notification> newItems) {
        Ln.d("newItems size %s", newItems.size());
        if(items.isEmpty() && newItems.isEmpty()) {
            showNoContent();
            listView.removeFooterView(btnLoadMore);
            return;
        }
        //se permaneceu na mesma quantidade
        if(newItems.size() == items.size()) {
            listView.removeFooterView(btnLoadMore);
            return;
        }
        if((items.size() + newItems.size()) % 15 == 0) {
            listView.addFooterView(btnLoadMore);
        } else {
            listView.removeFooterView(btnLoadMore);
        }
        super.onLoadFinished(loader, newItems);
    }

    private void removeNotification(final Notification notification) {
        new SafeAsyncTask<Boolean>() {

            @Override
            public Boolean call() throws Exception {
                service = serviceProvider.getService(getActivity());
                ApiResult result = service.removeNotifyMessage(notification.getId());
                return result.isSuccess();
            }

            @Override
            protected void onException(Exception e) throws RuntimeException {
                super.onException(e);
                Ln.e("Error ao ler mensagem: %s", notification.getId());
            }

            @Override
            protected void onSuccess(Boolean aBoolean) throws Exception {
                super.onSuccess(aBoolean);
                if(aBoolean) {
                    Ln.d("Sucesso ao ler mensagem: %s", notification.getId());

                    startActivity(starterIntent);
                    getActivity().finish();
                } else {
                    Ln.e("Error ao ler mensagem: %s", notification.getId());
                }
            }

        }.execute();
    }

    private void showNoContent() {
        super.showNoContentFragment(R.layout.no_content_notifications);
    }

    @Override
    public void removerNotificacao(Notification notification) {
        removeNotification(notification);
    }

    @Override
    public void openNotificacao(Notification notification) {
        MobiClubUser loggedUser = facade.getLoggedUser();
        if(loggedUser != null) {
            loggedUser.readNotification();
        }
        if(!notification.getRead()) {
            Ln.d("Nao esta lida");
            readNotification(notification);
            notification.setRead(true);
        } else {
            Ln.d("Esta lida");
        }
        Intent intent = new Intent(getActivity(), NotificationItemActivity.class);
        intent.putExtra(Constants.Extra.NOTIFICATION, notification);
        startActivity(intent);
    }
}
