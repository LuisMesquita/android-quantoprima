package br.com.mobiclub.quantoprima.ui.fragment.store;

import android.accounts.OperationCanceledException;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
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
import br.com.mobiclub.quantoprima.core.service.api.entity.Survey;
import br.com.mobiclub.quantoprima.core.service.api.entity.SurveyQuestion;
import br.com.mobiclub.quantoprima.core.service.api.exception.AppBlockedException;
import br.com.mobiclub.quantoprima.core.service.api.exception.AppOutdatedException;
import br.com.mobiclub.quantoprima.core.service.api.exception.UserBlockedException;
import br.com.mobiclub.quantoprima.domain.Answer;
import br.com.mobiclub.quantoprima.domain.MobiClubUser;
import br.com.mobiclub.quantoprima.domain.SurveyReply;
import br.com.mobiclub.quantoprima.events.NetworkErrorEvent;
import br.com.mobiclub.quantoprima.events.RestErrorEvent;
import br.com.mobiclub.quantoprima.facade.Facade;
import br.com.mobiclub.quantoprima.ui.activity.AppInactiveActivity;
import br.com.mobiclub.quantoprima.ui.activity.account.AccountBlockedActivity;
import br.com.mobiclub.quantoprima.ui.activity.account.CadastrarCpfToStoreActivity;
import br.com.mobiclub.quantoprima.ui.activity.launch.OutdatedActivity;
import br.com.mobiclub.quantoprima.ui.adapter.DialogResultAdapter;
import br.com.mobiclub.quantoprima.ui.adapter.store.survey.SurveyAdapter;
import br.com.mobiclub.quantoprima.ui.factory.AlertDialogFactory;
import br.com.mobiclub.quantoprima.ui.factory.DialogResultFactory;
import br.com.mobiclub.quantoprima.ui.factory.ProgressDialogFactory;
import br.com.mobiclub.quantoprima.ui.helper.LocationDetailsHelper;
import br.com.mobiclub.quantoprima.ui.view.ResultDialog;
import br.com.mobiclub.quantoprima.ui.view.ThrowableLoader;
import br.com.mobiclub.quantoprima.domain.Constants;
import br.com.mobiclub.quantoprima.util.Ln;
import br.com.mobiclub.quantoprima.util.SafeAsyncTask;
import retrofit.RetrofitError;

public class SurveyFragment extends Fragment
        implements OnClickListener, OnRatingBarChangeListener,
        LoaderManager.LoaderCallbacks<Survey> {

    private SurveyReply surveyReply;

    private Survey survey;

    private Integer surveyHttpStatus;


    private ListView questionList;
    private RelativeLayout noContent;

    private LinearLayout answerLoading;

    private LinearLayout linearNPS; //linearNPS

    private LinearLayout linearAnswer;
    private LinearLayout linearComment;
    private LinearLayout layoutQuestionComment;
    private LinearLayout linearSurveyEstablishment;
    private TextView[] numerosNPS;
    private TextView[] numerosNSI;

    private LinearLayout btnNextStep1;
    private LinearLayout btnNextStep3;
    private LinearLayout btnNextStep4;

    private LinearLayout btnBackStep3;
    private LinearLayout btnBackStep4;

    private RatingBar NPS;
    private RatingBar NSI;

    private List<SurveyQuestion> questions;

    private SurveyAdapter surveyAdapter;
    private ListView list;
    private EstablishmentLocation location;

    private EditText text;

    @Inject
    MobiClubServiceProvider serviceProvider;

    @Inject
    Bus bus;

    private Establishment establishment;
    private TextView txtQuestionTitleComment;
    private TextView labelNoContentQuestion;

    private EditText txtQuestionComment;
    private ProgressBar progressBar;
    private ProgressDialog progress;

    private boolean sendReply = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle arguments = getArguments();
        this.location = Facade.getInstance().getLocation();
        this.establishment = Facade.getInstance().getEstablishment();
        Ln.d("Avaliando location %s.", location.getReference());

        //TODO: tratar location == null

        Injector.inject(this);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getLoaderManager().initLoader(Constants.Loader.SURVEY_FRAGMENT, null, this);
    }

    @Override
    public void onPause() {
        super.onPause();
        bus.unregister(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        bus.register(this);
    }

    @Override
    public Loader<Survey> onCreateLoader(int id, Bundle args) {
        final Survey initialItem = survey;
        answerLoading.setVisibility(View.VISIBLE);
        return new ThrowableLoader<Survey>(getActivity(), survey) {
            @Override
            public Survey loadData() throws Exception {
                try {
                    Survey latest = null;
                    if (getActivity() != null && location != null) {
                        MobiClubServiceApi service = serviceProvider.getService(getActivity());
                        latest = service.getSurveyByLocation(location.getId());
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
                    return initialItem;
                }
            }
        };
    }

    /**
     * Get exception from loader if it provides one by being a
     *
     * @param loader
     * @return exception or null if none provided
     */
    protected Exception getException(final Loader<Survey> loader) {
        if (loader instanceof ThrowableLoader) {
            return ((ThrowableLoader<Survey>) loader).clearException();
        } else {
            return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Survey> loader, Survey survey) {
        this.survey = survey;

        final Exception exception = getException(loader);
        if (exception != null) {
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
            if(exception instanceof RetrofitError) {
                survey = null;
            }
            if(intent != null) {
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                getActivity().finish();
                return;
            }
        }

        answerLoading.setVisibility(View.GONE);
        if(survey == null || (survey != null &&
                survey.getHttpStatus() != RestErrorEvent.HTTP_OK)) {
            noContent.setVisibility(View.VISIBLE);
            return;
        }
        if(survey.getHasAppreciation()) {
            linearNPS.setVisibility(View.VISIBLE);
        } else {
            btnNextStep4 = (LinearLayout) linearAnswer.findViewById(R.id.btnQuestionNextStep3Final);
            btnNextStep4.setOnClickListener(this);
            linearAnswer.findViewById(R.id.bullets).setVisibility(View.GONE);
            linearComment.setVisibility(View.GONE);
            btnNextStep3.setVisibility(View.GONE);
            btnNextStep4.setVisibility(View.VISIBLE);
            btnBackStep3.setVisibility(View.GONE);
            linearNPS.setVisibility(View.GONE);
            linearAnswer.setVisibility(View.VISIBLE);
        }

        List<SurveyQuestion> surveyQuestions = survey.getSurveyQuestions();
        if(surveyQuestions.isEmpty()) {
            questionList.setVisibility(View.GONE);
            labelNoContentQuestion.setVisibility(View.VISIBLE);
            return;
        }
        addQuestions(surveyQuestions);
    }

    private void addQuestions(List<SurveyQuestion> surveyQuestions) {
        surveyAdapter.clear();
        if (surveyQuestions != null) {
            for(SurveyQuestion sq : surveyQuestions) {
                surveyAdapter.add(sq);
            }
        }
        surveyAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<Survey> loader) {

    }

    private void sendReply() {
        //answerLoading.setVisibility(View.VISIBLE);

        progress = ProgressDialogFactory.createProgress(getActivity(), R.string.send_reply);
        progress.show();

        progress.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                btnNextStep4.setClickable(true);
                btnNextStep3.setClickable(true);
            }
        });

        progress.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                btnNextStep4.setClickable(true);
                btnNextStep3.setClickable(true);
            }
        });

        if(getActivity() != null && serviceProvider != null) {
            new SafeAsyncTask<Boolean>() {

                @Override
                public Boolean call() throws Exception {
                    SurveyFragment.this.sendReply = true;
                    MobiClubServiceApi service = serviceProvider.getService(getActivity());
                    ApiResult result = service.answerSurvey(surveyReply);
                    surveyHttpStatus = result.getHttpStatus();
                    return result.isSuccess();
                }

                @Override
                protected void onException(Exception e) throws RuntimeException {
                    super.onException(e);
                    Intent intent = null;
                    if(e instanceof AppBlockedException) {
                        Ln.d("AppBlockedException");
                        intent = new Intent(getActivity(), AppInactiveActivity.class);
                    } else if(e instanceof AppOutdatedException) {
                        Ln.d("AppOutdatedException");
                        intent = new Intent(getActivity(), OutdatedActivity.class);
                    } else if(e instanceof UserBlockedException) {
                        Ln.d("UserBlockedException");
                        intent = new Intent(getActivity(), AccountBlockedActivity.class);
                    }
                    if(intent != null) {
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra(Constants.Extra.REASON_USER_BLOCKED, e.getMessage());
                        startActivity(intent);
                        getActivity().finish();
                        return;
                    }
                    progress.dismiss();
                    if(!(e instanceof RetrofitError)) {
                        Ln.e(e);
                        surveyError(getString(R.string.error_on_app));
                    }
                }

                @Override
                protected void onSuccess(Boolean success) throws Exception {
                    super.onSuccess(success);
                    if(success) {
                        surveySuccess();
                    } else {
                        if (surveyHttpStatus == 412) {
                            surveyErrorNeedCPF();
                        } else {
                            surveyError(getString(R.string.send_reply_error));
                        }
                    }
                    progress.dismiss();
                }

                @Override
                protected void onFinally() throws RuntimeException {
                    super.onFinally();
                    SurveyFragment.this.sendReply = false;
                    btnNextStep4.setClickable(true);
                    btnNextStep3.setClickable(true);
                }
            }.execute();
        }
    }

    private void surveySuccess() {
        answerLoading.setVisibility(View.GONE);
        int layout = R.layout.dialog_survey_success_positive;
        if(survey.getHasAppreciation()) {
            int nps = surveyReply.getNps();
            int nsi = surveyReply.getNSI();
            float result = (nps + nsi) / 2;
            if(result < 7.0) {
                layout = R.layout.dialog_survey_success_negative;
            } else if(result >= 7.0 && result <= 8.0) {
                layout = R.layout.dialog_survey_success_neutral;
            } else if(result > 8.0) {
                layout = R.layout.dialog_survey_success_positive;
            }
        } else {
            SurveyReply.AnsweredQuestions answeredQuestions = surveyReply.getAnsweredQuestionsResult();
            int quantAnswered = answeredQuestions.quantAnswered;
            Integer totalAnswered = answeredQuestions.totalAnswered;
            if(quantAnswered != 0) {
                float i = totalAnswered / quantAnswered;
                if(i < 3.0) {
                    layout = R.layout.dialog_survey_success_negative;
                } else if(i >= 3.0 && i <= 4.0) {
                    layout = R.layout.dialog_survey_success_neutral;
                } else if(i >= 4.0) {
                    layout = R.layout.dialog_survey_success_positive;
                }
            }
        }
        DialogResultAdapter dialogResultAdapter = DialogResultFactory.
                createSurveySuccess(layout);
        showDialogResult(dialogResultAdapter);
    }

    private void surveyError(String message) {
        answerLoading.setVisibility(View.GONE);
        DialogResultAdapter dialogResultAdapter = DialogResultFactory.
                createErrorDefault();
        showDialogResult(dialogResultAdapter);
        btnNextStep4.setClickable(true);
    }

    private void surveyErrorNeedCPF() {
        progress.dismiss();
        Intent intent = new Intent(getActivity(), CadastrarCpfToStoreActivity.class);
        getActivity().startActivityForResult(intent, 20000);
    }

    private void showDialogResult(DialogResultAdapter result) {
        //TODO: make a factory ou builder
        ResultDialog dialog = new ResultDialog();
        Bundle args = new Bundle();
        args.putSerializable(Constants.Extra.DIALOG_RESULTER, result);
        dialog.setArguments(args);
        dialog.show(getFragmentManager(), "resultDialog");
    }

    @Subscribe
    public void onNetworkErrorEvent(NetworkErrorEvent e) {
        //nesse caso tenta offline
        AlertDialogFactory.createDefaultError(getActivity(),
                R.string.service_comunication_error).show();
        btnBackStep3.setClickable(true);
        btnBackStep4.setClickable(true);
        SurveyFragment.this.sendReply = false;
        if(progress != null)
            progress.dismiss();
    }

    /**
     * Quando ocorre erro na chamada do servico
     *
     * @param e
     */
    @Subscribe
    public void onRestErrorEvent(RestErrorEvent e) {
        ApiError apiError = e.getApiError();
        String message = null;
        if (apiError != null)
            message = apiError.getMessage();
        surveyError(message);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnQuestionNextStep1:
                this.btnQuestionNextStep1_Click();
                break;
            case R.id.btnQuestionNextStep3:
                this.btnQuestionNextStep3_Click();
                break;

            case R.id.btnQuestionNextStep4:
                this.btnQuestionNextStep4_Click();
                break;
            case R.id.btnQuestionNextStep3Final:
                this.btnQuestionNextStep4_Click();
                break;

            case R.id.btnQuestionBackStep3:
                this.btnQuestionBackStep3_Click();
                break;
            case R.id.btnQuestionBackStep4:
                this.btnQuestionBackStep4_Click();
                break;
            default:
                break;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_survey, container, false);

        MobiClubUser loggedUser = Facade.getInstance().getLoggedUser();
        surveyReply = new SurveyReply(loggedUser, this.location);
        surveyReply.setComment("");

        progressBar = (ProgressBar) view.findViewById(R.id.pb_loading);
        final AnimationDrawable anim = (AnimationDrawable) progressBar.getBackground();
        if (anim != null) anim.start();

        LocationDetailsHelper helper = new LocationDetailsHelper(view,
                location, establishment, false);
        helper.show();

        createView(view);

        return view;
    }

    private void initSurveyList(View view) {
        this.questions = new ArrayList<SurveyQuestion>();
        this.list = (ListView) view.findViewById(R.id.listQuestion);
        this.surveyAdapter = new SurveyAdapter(getActivity(), questions) {
            @Override
            public void changeRattingScore(RatingBar ratingBar, float rating, int position, boolean fromUser) {
                super.changeRattingScore(ratingBar, rating, position, fromUser);
                if (fromUser) {
                    changeScore(position, (int) rating);
                }
            }
        };
        this.list.setAdapter(this.surveyAdapter);
    }

    private void changeScore(int position, int rating) {
        questions.get(position).setNota(rating);
    }

    @Override
    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
        if (ratingBar.getId() == R.id.ratBarNPS) {
            this.surveyReply.setNps((int) rating - 1);
            if (rating > 0) {
                activateNextStep1(true);
            } else {
                activateNextStep1(false);
            }
            for (int i = 1; i < 12; i++) {
                if (i <= rating) {
                    numerosNPS[i - 1].setTextColor(getResources().getColor(R.color.gold));
                } else {
                    numerosNPS[i - 1].setTextColor(getResources().getColor(R.color.black));
                }
            }
        } else if (ratingBar.getId() == R.id.ratBarNSI) {
            this.surveyReply.setNSI((int) rating - 1);
            if (rating > 0) {
                //btnNextStep2.setBackgroundResource(R.drawable.proximo_on);
            } else {
                //btnNextStep2.setBackgroundResource(R.drawable.proximo_off);
            }
            for (int i = 1; i < 12; i++) {
                if (i <= rating) {
                    numerosNSI[i - 1].setTextColor(getResources().getColor(R.color.gold));
                } else {
                    numerosNSI[i - 1].setTextColor(getResources().getColor(R.color.black));
                }
            }
        }
    }

    private void activateNextStep1(boolean enable) {
        btnNextStep1.setClickable(enable);
    }

    private void btnQuestionBackStep3_Click() {
        linearAnswer.setVisibility(View.GONE);
        linearNPS.setVisibility(View.VISIBLE);
        linearComment.setVisibility(View.GONE);
    }

    private void btnQuestionBackStep4_Click() {
        FragmentActivity activity = getActivity();
        if(txtQuestionComment != null && activity != null) {
            InputMethodManager imm = (InputMethodManager)activity.getSystemService(
                    Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(txtQuestionComment.getWindowToken(), 0);
        }

        linearAnswer.setVisibility(View.VISIBLE);
        linearNPS.setVisibility(View.GONE);
        linearComment.setVisibility(View.GONE);
    }

    private void btnQuestionNextStep1_Click() {
        if (this.NPS.getRating() > 0 && this.NSI.getRating() > 0) {
            linearAnswer.setVisibility(View.VISIBLE);
            linearAnswer.requestLayout();
            linearNPS.setVisibility(View.GONE);
        } else {
            Toast toast = Toast.makeText(getActivity(), R.string.pesquisa_required_question, Toast.LENGTH_LONG);
            toast.show();
        }
    }

    private void btnQuestionNextStep3_Click() {
        linearAnswer.setVisibility(View.GONE);
        linearComment.setVisibility(View.VISIBLE);
        linearComment.requestLayout();
    }

    private void btnQuestionNextStep4_Click() {
        if(survey.getHasAppreciation()) {
            //linearNPS.setVisibility(View.GONE);
            //linearAnswer.setVisibility(View.GONE);
            this.surveyReply.setComment(this.text.getText().toString());
        } else {
            this.surveyReply.setComment("");
        }

        for (int i = 0; i < questions.size(); i++) {
            SurveyQuestion surveyQuestion = questions.get(i);
            this.surveyReply.addAnswer(new Answer(surveyQuestion.getIdToAnswer(), surveyQuestion.getNota()));
        }

        layoutQuestionComment.setVisibility(View.VISIBLE);
        linearComment.requestLayout();

        if(!sendReply) {
            btnNextStep4.setClickable(false);
            btnNextStep3.setClickable(false);
            sendReply();
        } else {
            AlertDialogFactory.createAlertDialog(getActivity(), R.string.alert_title_attention, R.string.wait_survey_reply).show();
        }
    }

    private void createView(View view) {
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        LinearLayout ll = (LinearLayout) view.findViewById(R.id.survey);

        layoutInflater.inflate(R.layout.fragment_survey_page_appreciation, ll, true);
        linearNPS = (LinearLayout) view.findViewById(R.id.linearNPS);
        linearNPS.setVisibility(View.GONE);

        layoutInflater.inflate(R.layout.fragment_survey_page_answer, ll, true);
        linearAnswer = (LinearLayout) view.findViewById(R.id.linearAnswers);
        linearAnswer.setVisibility(View.GONE);

        questionList = (ListView) view.findViewById(R.id.listQuestion);
        labelNoContentQuestion = (TextView) view.findViewById(R.id.label_question_no_content);


        layoutInflater.inflate(R.layout.fragment_survey_page_comment, ll);
        linearComment = (LinearLayout) view.findViewById(R.id.linearComment);
        linearComment.setVisibility(View.GONE);

        answerLoading = (LinearLayout) view.findViewById(R.id.answerLoading);
        answerLoading.setVisibility(View.VISIBLE);

        layoutInflater.inflate(R.layout.fragment_survey_page_no_content, ll);
        noContent = (RelativeLayout) view.findViewById(R.id.no_content);
        noContent.setVisibility(View.GONE);

        btnNextStep1 = (LinearLayout) view.findViewById(R.id.btnQuestionNextStep1);
        btnNextStep1.setOnClickListener(this);

        btnNextStep3 = (LinearLayout) view.findViewById(R.id.btnQuestionNextStep3);
        btnNextStep3.setOnClickListener(this);

        btnNextStep4 = (LinearLayout) view.findViewById(R.id.btnQuestionNextStep4);
        btnNextStep4.setOnClickListener(this);

        btnBackStep3 = (LinearLayout) view.findViewById(R.id.btnQuestionBackStep3);
        btnBackStep3.setOnClickListener(this);

        btnBackStep4 = (LinearLayout) view.findViewById(R.id.btnQuestionBackStep4);
        btnBackStep4.setOnClickListener(this);

        layoutQuestionComment = (LinearLayout) view.findViewById(R.id.layoutQuestionComment);

        linearSurveyEstablishment = (LinearLayout) view.findViewById(R.id.linearSurveyEstablishment);

        NPS = (RatingBar) view.findViewById(R.id.ratBarNPS);
        NPS.setOnRatingBarChangeListener(this);

        NSI = (RatingBar) view.findViewById(R.id.ratBarNSI);
        NSI.setOnRatingBarChangeListener(this);

        text = (EditText) view.findViewById(R.id.txtQuestionComment);

        numerosNPS = new TextView[11];
        numerosNSI = new TextView[11];

        numerosNPS[0] = (TextView) view.findViewById(R.id.txtNum0_nps);
        numerosNPS[0].setTextColor(getResources().getColor(R.color.black));
        numerosNSI[0] = (TextView) view.findViewById(R.id.txtNum0_nsi);
        numerosNSI[0].setTextColor(getResources().getColor(R.color.black));

        numerosNPS[1] = (TextView) view.findViewById(R.id.txtNum1_nps);
        numerosNPS[1].setTextColor(getResources().getColor(R.color.black));
        numerosNSI[1] = (TextView) view.findViewById(R.id.txtNum1_nsi);
        numerosNSI[1].setTextColor(getResources().getColor(R.color.black));

        numerosNPS[2] = (TextView) view.findViewById(R.id.txtNum2_nps);
        numerosNPS[2].setTextColor(getResources().getColor(R.color.black));
        numerosNSI[2] = (TextView) view.findViewById(R.id.txtNum2_nsi);
        numerosNSI[2].setTextColor(getResources().getColor(R.color.black));

        numerosNPS[3] = (TextView) view.findViewById(R.id.txtNum3_nps);
        numerosNPS[3].setTextColor(getResources().getColor(R.color.black));
        numerosNSI[3] = (TextView) view.findViewById(R.id.txtNum3_nsi);
        numerosNSI[3].setTextColor(getResources().getColor(R.color.black));

        numerosNPS[4] = (TextView) view.findViewById(R.id.txtNum4_nps);
        numerosNPS[4].setTextColor(getResources().getColor(R.color.black));
        numerosNSI[4] = (TextView) view.findViewById(R.id.txtNum4_nsi);
        numerosNSI[4].setTextColor(getResources().getColor(R.color.black));

        numerosNPS[5] = (TextView) view.findViewById(R.id.txtNum5_nps);
        numerosNPS[5].setTextColor(getResources().getColor(R.color.black));
        numerosNSI[5] = (TextView) view.findViewById(R.id.txtNum5_nsi);
        numerosNSI[5].setTextColor(getResources().getColor(R.color.black));

        numerosNPS[6] = (TextView) view.findViewById(R.id.txtNum6_nps);
        numerosNPS[6].setTextColor(getResources().getColor(R.color.black));
        numerosNSI[6] = (TextView) view.findViewById(R.id.txtNum6_nsi);
        numerosNSI[6].setTextColor(getResources().getColor(R.color.black));

        numerosNPS[7] = (TextView) view.findViewById(R.id.txtNum7_nps);
        numerosNPS[7].setTextColor(getResources().getColor(R.color.black));
        numerosNSI[7] = (TextView) view.findViewById(R.id.txtNum7_nsi);
        numerosNSI[7].setTextColor(getResources().getColor(R.color.black));

        numerosNPS[8] = (TextView) view.findViewById(R.id.txtNum8_nps);
        numerosNPS[8].setTextColor(getResources().getColor(R.color.black));
        numerosNSI[8] = (TextView) view.findViewById(R.id.txtNum8_nsi);
        numerosNSI[8].setTextColor(getResources().getColor(R.color.black));

        numerosNPS[9] = (TextView) view.findViewById(R.id.txtNum9_nps);
        numerosNPS[9].setTextColor(getResources().getColor(R.color.black));
        numerosNSI[9] = (TextView) view.findViewById(R.id.txtNum9_nsi);
        numerosNSI[9].setTextColor(getResources().getColor(R.color.black));

        numerosNPS[10] = (TextView) view.findViewById(R.id.txtNum10_nps);
        numerosNPS[10].setTextColor(getResources().getColor(R.color.black));
        numerosNSI[10] = (TextView) view.findViewById(R.id.txtNum10_nsi);
        numerosNSI[10].setTextColor(getResources().getColor(R.color.black));

        txtQuestionTitleComment = (TextView) view.findViewById(R.id.txtQuestionTitleComment);

        txtQuestionComment = (EditText) view.findViewById(R.id.txtQuestionComment);

        initSurveyList(view);
    }

}
