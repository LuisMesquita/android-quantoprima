package br.com.mobiclub.quantoprima.core.service.api.entity;

import com.google.gson.annotations.SerializedName;

/**
 */
public class GainMobiResponse {

    @SerializedName("HttpStatus")
    private Integer httpStatus=0;

    @SerializedName("MessageHead")
    private String messageHead="";

    @SerializedName("MessageBody")
    private String messageBody;

    @SerializedName("TitleHead")
    private String titleHead="";

    @SerializedName("TitleBody")
    private String titleBody="";

    @SerializedName("Message")
    private String message;


    @SerializedName("PlusScore")
    private int plusScore;

    @SerializedName("Establishment")
    private Establishment establishment = null;

    private boolean shotMobi;

    public Integer getHttpStatus() {
        if (httpStatus==null){
            httpStatus = 0;
        }
        return httpStatus;
    }

    public void setHttpStatus(Integer httpStatus) {
        this.httpStatus = httpStatus;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Establishment getEstablishment() {

        return establishment;
    }

    public void setEstablishment(Establishment establishment) {
        this.establishment = establishment;
    }

    public boolean isSuccess() {
        return httpStatus != null && httpStatus == 200;
    }

    public void setShotMobi(boolean shotMobi) {
        this.shotMobi = shotMobi;
    }

    public boolean isShotMobi() {
        return shotMobi;
    }

    public String getMessageHead() {
        return messageHead;
    }

    public void setMessageHead(String messageHead) {
        this.messageHead = messageHead;
    }

    public String getMessageBody() {
        return messageBody;
    }

    public void setMessageBody(String messageBody) {
        this.messageBody = messageBody;
    }

    public String getTitleHead() {
        return titleHead;
    }

    public void setTitleHead(String titleHead) {
        this.titleHead = titleHead;
    }

    public String getTitleBody() {
        return titleBody;
    }

    public void setTitleBody(String titleBody) {
        this.titleBody = titleBody;
    }

    public int getPlusScore() {
        return plusScore;
    }

    public void setPlusScore(int plusScore) {
        this.plusScore = plusScore;
    }
}
