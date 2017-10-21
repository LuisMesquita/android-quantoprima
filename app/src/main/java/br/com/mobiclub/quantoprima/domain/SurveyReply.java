package br.com.mobiclub.quantoprima.domain;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import br.com.mobiclub.quantoprima.core.service.api.entity.AppreciationResponse;
import br.com.mobiclub.quantoprima.core.service.api.entity.EstablishmentLocation;
import br.com.mobiclub.quantoprima.core.service.api.entity.ScoreAnswerResponse;
import br.com.mobiclub.quantoprima.core.service.api.entity.SurveySelectedResponse;
import br.com.mobiclub.quantoprima.core.service.api.entity.builder.AppreciationResponseBuilder;
import br.com.mobiclub.quantoprima.facade.Facade;

public class SurveyReply {

    private String comment;
    private MobiClubUser user;

    private int nps;
    private int nsi;

    private EstablishmentLocation location;

    private List<Answer> answers = new ArrayList<Answer>();

    public SurveyReply(MobiClubUser user, EstablishmentLocation location) {
        this.user = user;
        this.location = location;
    }

    public int getNps() {
        return nps;
    }

    public void setNps(int nps) {
        this.nps = nps;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setUser(MobiClubUser user) {
        this.user = user;
    }

    public MobiClubUser getUser() {
        return user;
    }

    public void setLocation(EstablishmentLocation location) {
        this.location = location;
    }

    public EstablishmentLocation getLocation() {
        return location;
    }

    public void setNSI(int nsi) {
        this.nsi = nsi;
    }

    public int getNSI() {
        return nsi;
    }

    public void addAnswer(Answer answer) {
        answers.add(answer);
    }

    public String getAnswersJSON() {
        List<ScoreAnswerResponse> scoreAnswers = new ArrayList<ScoreAnswerResponse>();

        for(int i = 0; i < answers.size();i++) {
            Answer answer = answers.get(i);
            ScoreAnswerResponse sar = new ScoreAnswerResponse();
            sar.setScoreAnswer(answer.getNota());
            sar.setSelectedResponse(new SurveySelectedResponse(answer.getIdToAnswer()));
            scoreAnswers.add(sar);
        }
        Gson gson = Facade.getInstance().getGSON();
        return gson.toJson(scoreAnswers);
    }

    public String getAppreciationsJSON() {
        AppreciationResponseBuilder builder = new AppreciationResponseBuilder();
        AppreciationResponse appreciationResponse = builder.setDisplayInApp(1)
                .setLocationId(location.getId()).setNps(nps).setNsi(nsi).setComment(comment).getResult();

        Gson gson = Facade.getInstance().getGSON();

        return gson.toJson(appreciationResponse);
    }

    public AnsweredQuestions getAnsweredQuestionsResult() {
        AnsweredQuestions result = new AnsweredQuestions();
        int answered = 0;
        Integer totalAnswered = 0;
        for (int i = 0; i < answers.size(); i++) {
            Integer nota = answers.get(i).getNota();
            if(nota != 0) {
                answered++;
                totalAnswered += nota;
            }
        }
        result.quantAnswered = answered;
        result.totalAnswered = totalAnswered;
        return result;
    }

    public class AnsweredQuestions {
        public int quantAnswered;
        public Integer totalAnswered;
    }
}
