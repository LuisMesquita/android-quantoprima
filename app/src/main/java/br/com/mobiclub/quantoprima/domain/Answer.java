package br.com.mobiclub.quantoprima.domain;

/**
 *
 */
public class Answer {

    private Integer IdToAnswer;

    private Integer nota;

    public Answer(Integer idToAnswer, Integer nota) {
        IdToAnswer = idToAnswer;
        this.nota = nota;
    }

    public Integer getIdToAnswer() {
        return IdToAnswer;
    }

    public void setIdToAnswer(Integer idToAnswer) {
        IdToAnswer = idToAnswer;
    }

    public Integer getNota() {
        return nota;
    }

    public void setNota(Integer nota) {
        this.nota = nota;
    }
}
