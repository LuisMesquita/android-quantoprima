package br.com.mobiclub.quantoprima.domain;

import br.com.mobiclub.quantoprima.core.service.api.entity.User;

public class MobiSurveySelected {

    private String question;
    private int selectedId;
    private int locationId;
    private int nota;
    private User user;

    public MobiSurveySelected() {
    }

    public MobiSurveySelected(String question, int selectedId, int locationId) {
        this.nota = 0;
        this.question = question;
        this.locationId = locationId;
        this.selectedId = selectedId;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public String getQuestion() {
        return this.question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public int getSelectedId() {
        return this.selectedId;
    }

    public void setSelectedId(int selectedId) {
        this.selectedId = selectedId;
    }

    public int getLocationId() {
        return this.locationId;
    }

    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }

    public int getNota() {
        return this.nota;
    }

    public void setNota(int nota) {
        this.nota = nota;
    }
}
