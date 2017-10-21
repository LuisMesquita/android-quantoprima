package br.com.mobiclub.quantoprima.ui.fragment.store;

import android.accounts.OperationCanceledException;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.List;

import javax.inject.Inject;

import br.com.mobiclub.quantoprima.R;
import br.com.mobiclub.quantoprima.config.Injector;
import br.com.mobiclub.quantoprima.core.service.MobiClubServiceApi;
import br.com.mobiclub.quantoprima.core.service.MobiClubServiceProvider;
import br.com.mobiclub.quantoprima.core.service.api.entity.Cardapio;
import br.com.mobiclub.quantoprima.core.service.api.entity.Establishment;
import br.com.mobiclub.quantoprima.core.service.api.entity.EstablishmentLocation;
import br.com.mobiclub.quantoprima.core.service.api.entity.LocationDetails;
import br.com.mobiclub.quantoprima.core.service.api.entity.Phone;
import br.com.mobiclub.quantoprima.core.service.api.entity.Survey;
import br.com.mobiclub.quantoprima.core.service.api.entity.SurveyQuestion;
import br.com.mobiclub.quantoprima.core.service.api.exception.AppBlockedException;
import br.com.mobiclub.quantoprima.core.service.api.exception.AppOutdatedException;
import br.com.mobiclub.quantoprima.core.service.api.exception.UserBlockedException;
import br.com.mobiclub.quantoprima.domain.Language;
import br.com.mobiclub.quantoprima.facade.Facade;
import br.com.mobiclub.quantoprima.ui.activity.AppInactiveActivity;
import br.com.mobiclub.quantoprima.ui.activity.MapActivity;
import br.com.mobiclub.quantoprima.ui.activity.WebViewActivity;
import br.com.mobiclub.quantoprima.ui.activity.account.AccountBlockedActivity;
import br.com.mobiclub.quantoprima.ui.activity.launch.OutdatedActivity;
import br.com.mobiclub.quantoprima.ui.activity.store.CardapioActivity;
import br.com.mobiclub.quantoprima.ui.activity.store.survey.SurveyActivity;
import br.com.mobiclub.quantoprima.ui.factory.AlertDialogFactory;
import br.com.mobiclub.quantoprima.ui.helper.LocationDetailsHelper;
import br.com.mobiclub.quantoprima.ui.view.ThrowableLoader;
import br.com.mobiclub.quantoprima.domain.Constants;
import br.com.mobiclub.quantoprima.util.Ln;
import br.com.mobiclub.quantoprima.util.NetworkUtil;
import br.com.mobiclub.quantoprima.util.Util;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.InjectViews;
import butterknife.OnClick;

public class StoreInformationFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<LocationDetails>,
        GainMobiFragmentListener {

    private static final int TELEPHONY_BUTTON_ID = 0;
    private static final int SITE_BUTTON_ID = 1;
    private static final int MENU_BUTTON_ID = 2;
    private static final int MAP_BUTTON_ID = 3;
    private static final int SURVEY_BUTTON_ID = 4;
    private int[][] btnDrawables;

    private static final int CARDAPIO_REQUEST_CODE = 0;
    @Inject protected MobiClubServiceProvider serviceProvider;

    @InjectView(R.id.label_store_distance)
    TextView labelStoreDistance;

    @InjectView(R.id.lyt_how_to_win)
    LinearLayout lytHowToWin;

    @InjectView(R.id.lyt_how_to_win_info)
    LinearLayout howToWinInfo;

    @InjectViews( {R.id.button_telephony, R.id.button_site, R.id.button_menu,
            R.id.button_map, R.id.button_evaluate})
    List<ImageButton> buttons;

    @InjectView(R.id.scrollview)
    ScrollView scrollView;

    private Establishment establishment;
    private EstablishmentLocation establishmentLocation;
    private int locationId;

    private LocationDetails location;
    private LocationDetailsHelper helper;
    private boolean hasMenu = false;
    private boolean hasSurvey = false;
    private ProgressBar progressBar;
    private Language[] languages;
    private Establishment mapLocations;
    private boolean hasMap;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Injector.inject(this);

        establishment = Facade.getInstance().getEstablishment();
        establishmentLocation = Facade.getInstance().getLocation();
        locationId = establishmentLocation.getId();

        btnDrawables = getButtonDrawables();

    }

    private int[][] getButtonDrawables() {
        int[][] d = new int[][] {
                { R.drawable.bg_btn_telefone},
                { R.drawable.bg_btn_site},
                { R.drawable.bg_btn_cardapio},
                { R.drawable.bg_btn_map},
                { R.drawable.bg_btn_evaluate},
        };

        return d;
    }

    private Drawable getDrawable(int id) {
        return getResources().getDrawable(id);
    }

    @Override
    public void onActivityCreated(final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ActionBarActivity actionBarActivity = getActionBarActivity();

        if(!isValid()) {
            //TODO: show error
            Ln.e("Dados nao sao validos");
            return;
        }

        getLoaderManager().initLoader(Constants.Loader.STORE_INFORMATION, null, this);
        Ln.d("initLoader");
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {

            @Override
            public void onScrollChanged() {

            }
        });

        hide(view);
    }

    private void hide(View view) {
        view.findViewById(R.id.frame_location).setVisibility(View.GONE);
        view.findViewById(R.id.frame_interaction).setVisibility(View.GONE);
        view.findViewById(R.id.frame_distance).setVisibility(View.GONE);
        view.findViewById(R.id.frame_extract).setVisibility(View.GONE);
        view.findViewById(R.id.frame_location).setVisibility(View.GONE);
        view.findViewById(R.id.lyt_how_to_win).setVisibility(View.GONE);
    }

    @OnClick(R.id.button_telephony)
    public void onPhone() {
        Ln.d("onPhone");
        if(location != null && containsPhone(location.getPhones())) {
            List<Phone> phones = location.getPhones();
            final String[] phonesData = new String[phones.size()];
            for (int i = 0;i < phones.size();i++) {
                Phone phone = phones.get(i);
                String phoneNumber = phone.getPhoneNumber();
                if(phoneNumber == null) continue;
                phoneNumber.replace("-", "");
                phonesData[i] = phone.getDDD() + phoneNumber;
            }
            AlertDialog.Builder builder = AlertDialogFactory.createListAlertDialog(getActivity(),
                    R.string.call_to_phone, phonesData);
            builder.setItems(phonesData, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    if(which == -1)
                        which = 0;
                    callPhone(phonesData[which]);
                }
            });
            builder.setNegativeButton(R.string.alert_cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).setPositiveButton(R.string.button_call, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if(which == -1)
                        which = 0;
                    callPhone(phonesData[which]);
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
    }

    private boolean containsPhone(List<Phone> phones) {
        if(phones == null || (phones != null && phones.isEmpty())) {
            return false;
        }
        for (int i = 0; i < phones.size(); i++) {
            if(phones.get(i).getPhoneNumber() != null) {
                return true;
            }
        }
        return false;
    }

    private void callPhoneAlert(final String phone) {
        if(phone == null)
            return;
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.dialog_call_phone_message).
                setPositiveButton(R.string.button_yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        callPhone(phone);
                    }
                }).setNegativeButton(R.string.button_no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).setCancelable(true);
        builder.create().show();
    }

    private void callPhone(String phone) {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + phone));
        startActivity(callIntent);
    }

    @OnClick(R.id.button_site)
    public void onSite() {
        Ln.d("onSite");
        if(location.getSite() != null) {
            FragmentActivity activity = getActivity();
            if(!NetworkUtil.isConnected(activity)) {
                AlertDialog defaultError = AlertDialogFactory.createDefaultError(activity, R.string.alert_title_attention,
                        R.string.alert_title_webview_no_connection);
                defaultError.setCancelable(true);
                defaultError.show();
                return;
            }
            Intent browserIntent = new Intent(activity, WebViewActivity.class);
            Ln.d("Abrindo site %s", location.getSite());
            browserIntent.putExtra(Constants.Extra.WEB_VIEW_URL, location.getSite());
            Ln.d("Titulo site %s", location.getReference());
            browserIntent.putExtra(Constants.Extra.WEB_VIEW_TITLE, location.getReference());
            startActivity(browserIntent);
        }
    }

    @OnClick(R.id.button_menu)
    public void onCardapio() {
        Ln.d("onMenu");
        if(hasMenu) {
            Facade.getInstance().setLocation(establishmentLocation);
            Facade.getInstance().setEstablishment(establishment);
            onStoreInteraction(CardapioActivity.class);
        }
    }

    @OnClick(R.id.button_map)
    public void onMap() {
        Ln.d("onMap");
        Facade.getInstance().setLocation(establishmentLocation);
        Facade.getInstance().setEstablishment(establishment);

        onStoreInteraction(MapActivity.class);
    }

    @OnClick(R.id.button_evaluate)
    public void onEvaluate() {
        Ln.d("onEvaluate");
        Facade.getInstance().setLocation(establishmentLocation);
        Facade.getInstance().setEstablishment(establishment);
        onStoreInteraction(SurveyActivity.class);
    }

    private void onStoreInteraction(Class<? extends Activity> activity) {
        if(activity == null) {
            return;
        }
        Intent intent = new Intent(getActivity(), activity);

        Facade.getInstance().setLocation(establishmentLocation);
        Facade.getInstance().setEstablishment(establishment);

        intent.putExtra(Constants.Extra.LOCATIONS_MAPS, mapLocations);

        intent.putExtra(Constants.Extra.CARDAPIO_LANGUAGES, new LanguageWrapper(languages));
        startActivityForResult(intent, CARDAPIO_REQUEST_CODE);
    }

    private boolean isValid() {
        return establishmentLocation != null && establishment != null &&
                establishmentLocation.getId() > 0;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_store_information, container, false);

        ButterKnife.inject(this, view);

        progressBar = (ProgressBar) view.findViewById(R.id.pb_loading);
        final AnimationDrawable anim = (AnimationDrawable) progressBar.getBackground();
        if (anim != null) anim.start();

        if(establishmentLocation != null) {
            for(int j = 0; j < btnDrawables.length;j++) {
                buttons.get(j).setImageResource(btnDrawables[j][0]);
            }
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        establishment = Facade.getInstance().getEstablishment();
        establishmentLocation = Facade.getInstance().getLocation();
        LocationDetailsHelper helper = new LocationDetailsHelper(getView(),
                establishmentLocation, establishment);
        helper.updateLocationMobis(establishmentLocation);
    }

    @Override
    public Loader<LocationDetails> onCreateLoader(int id, Bundle args) {
        final LocationDetails initialLocation = location;
        return new ThrowableLoader<LocationDetails>(getActivity(), location) {
            @Override
            public LocationDetails loadData() throws Exception {
                try {
                    LocationDetails latest = null;

                    if (getActivity() != null) {
                        MobiClubServiceApi service = serviceProvider.getService(getActivity());
                        latest = service.getLocation(locationId);
                        Cardapio menu = service.getCardapioByLocation(locationId);
                        hasMenu = false;
                        if(menu.getCategories() != null &&
                                !menu.getCategories().isEmpty()) {
                            hasMenu = true;
                            List<Language> langs = menu.getLanguages();
                            if(!langs.isEmpty()) {
                                languages = new Language[langs.size()];
                                languages = langs.toArray(languages);
                            }
                        }
                        List<Establishment> establishments = service.getLocationsPositions();
                        if(establishments != null && !establishments.isEmpty()) {
                            Establishment establishment = establishments.get(0);
                            if(!establishment.getLocations().isEmpty()) {
                                List<EstablishmentLocation> locations = establishment.getLocations();
                                for (int i = 0; i < locations.size(); i++) {
                                    EstablishmentLocation el = locations.get(i);
                                    if(el.getReference().equals(establishmentLocation.getReference())) {
                                        establishment.setLocation(el);
                                    }
                                }
                                mapLocations = establishment;
                                hasMap = true;
                            } else {
                                hasMap = false;
                            }
                        }
                        Survey survey = service.getSurveyByLocation(locationId);
                        List<SurveyQuestion> questions = survey.getSurveyQuestions();
                        hasSurvey = false;
                        if(questions != null && !questions.isEmpty()) {
                            hasSurvey = true;
                        }
                    }
                    if (latest != null) {
                        return latest;
                    } else {
                        return null;
                    }
                } catch (final OperationCanceledException e) {
                    final Activity activity = getActivity();
                    if (activity != null) {
                        activity.finish();
                    }
                    return initialLocation;
                }
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<LocationDetails> loader, LocationDetails location) {
        progressBar.setVisibility(View.GONE);

        final Exception exception = getException(loader);
        //TODO: colocar frame para recarregar
        if (location == null) {
            showError(getErrorLoadingMessage(exception));
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
                getActivity().finish();
            }
            return;
        }
        if (exception != null) {
            showError(getErrorMessage(exception));
            return;
        }
        this.location = location;
        Ln.d("Showing store information");

        showView();

        helper = new LocationDetailsHelper(getView(),
                establishmentLocation, establishment);
        helper.show();

        LayoutInflater li = getActivity().getLayoutInflater();

        List<String> descriptions = location.getDescriptions();
        if(descriptions != null) {
            if(!descriptions.isEmpty()) {
                for (int i = 0;i < descriptions.size();i++) {
                    String description = descriptions.get(i);
                    View item = li.inflate(R.layout.lyt_item_how_to_win, null);
                    TextView labelHowToWinInfo = (TextView) item.findViewById(R.id.label_how_to_win_info);
                    labelHowToWinInfo.setText(description);
                    howToWinInfo.addView(item);
                }
                howToWinInfo.requestLayout();
            } else {
                lytHowToWin.setVisibility(View.INVISIBLE);
            }
        } else {
            lytHowToWin.setVisibility(View.INVISIBLE);
        }

        if(!containsPhone(location.getPhones())) {
            //TODO: fazer isso com setenabled = false
            ImageButton button = buttons.get(TELEPHONY_BUTTON_ID);
            button.setImageResource(R.drawable.t24_loja_btn_telefone_disabled);
            button.setClickable(false);
        }

        if(!hasMenu) {
            ImageButton button = buttons.get(MENU_BUTTON_ID);
            button.setImageResource(R.drawable.t24_loja_btn_cardapio_disabled);
            button.setClickable(false);
        }

        if(!hasMap) {
            ImageButton button = buttons.get(MAP_BUTTON_ID);
            button.setImageResource(R.drawable.t24_loja_btn_mapa_disabled);
            button.setClickable(false);
        }
        if(!hasSurvey) {
            ImageButton button = buttons.get(SURVEY_BUTTON_ID);
            button.setImageResource(R.drawable.t24_loja_btn_avalie_disabled);
            button.setClickable(false);
        }

        String site = location.getSite();
        if(site == null || (site != null && site.isEmpty())) {
            //TODO: fazer isso com setenabled = false
            ImageButton button = buttons.get(SITE_BUTTON_ID);
            button.setImageResource(R.drawable.t24_loja_btn_site_disabled);
            button.setClickable(false);
        }
        Double distance = establishmentLocation.getDistance();
        String storeDistance = Util.getDistanceString(distance);
        String distanceValue = getString(R.string.store_information_label_position, storeDistance);
        labelStoreDistance.setText(distanceValue);

    }

    private void showView() {
        View view = getView();
        view.findViewById(R.id.frame_location).setVisibility(View.VISIBLE);
        view.findViewById(R.id.frame_interaction).setVisibility(View.VISIBLE);
        view.findViewById(R.id.frame_distance).setVisibility(View.VISIBLE);
        view.findViewById(R.id.frame_extract).setVisibility(View.VISIBLE);
        view.findViewById(R.id.frame_location).setVisibility(View.VISIBLE);
        view.findViewById(R.id.lyt_how_to_win).setVisibility(View.VISIBLE);
    }

    @Override
    public void onLoaderReset(Loader<LocationDetails> loader) {

    }

    private ActionBarActivity getActionBarActivity() {
        return ((ActionBarActivity) getActivity());
    }

    /**
     * Show exception in a Toast
     *
     * @param message
     */
    protected void showError(final int message) {
        AlertDialogFactory.createDefaultError(getActivity(), message).show();
    }

    /**
     * Get exception from loader if it provides one by being a
     * {@link br.com.mobiclub.fastfood.ui.view.ThrowableLoader}
     *
     * @param loader
     * @return exception or null if none provided
     */
    protected Exception getException(final Loader<LocationDetails> loader) {
        if (loader instanceof ThrowableLoader) {
            return ((ThrowableLoader<LocationDetails>) loader).clearException();
        } else {
            return null;
        }
    }

    protected int getErrorLoadingMessage(final Exception exception) {
        return R.string.error_loading_store;
    }

    protected int getErrorMessage(final Exception exception) {
        return R.string.error_loading_store;
    }

    @Override
    public void updateLocationMobis(EstablishmentLocation location,
                                    Integer mobisType,
                                    Integer buyValue) {
        establishment = Facade.getInstance().getEstablishment();
        establishmentLocation = Facade.getInstance().getLocation();
    }
}
