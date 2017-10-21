package br.com.mobiclub.quantoprima.domain;

import java.io.Serializable;
import java.util.Date;

import br.com.mobiclub.quantoprima.util.Util;

public class Mobi implements Serializable, Comparable<Mobi> {

    public static final int QR_CODE_LENGTH = 40;

    //TODO: ver o tipo certo
    public static final int MOBI_OFFLINE_TYPE = 0;
    public static final java.lang.String SHOT_MOBI_CODE = "mobiclubshot";
    public static final int MOBI_ONLINE_TYPE = 1;
    public static final Integer MOBIS_GAIN_MOBI_TYPE = 0;
    public static final Integer MOBIS_BUY_REWARD_TYPE = 1;

    private Integer id;

    private String code;
    private int timeMinute;
    private int status;
    private int userId;
    private String locationName;
    private String logo;
    private Date timeUpdate;

    public Mobi() {

    }

    public Mobi(String code, int timeMinute) {
        this.code = code;
        this.timeMinute = timeMinute;
    }

    public Mobi(int id, String code, int timeMinute) {
        this.id = id;
        this.code = code;
        this.timeMinute = timeMinute;
    }

    @Override
    public String toString() {
        return String.format("id=%s, status=%s, timeMinute=%d, location=%s",
                id, status, timeMinute, locationName);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getTimeMinute() {
        return timeMinute;
    }

    public void setTimeMinute(int timeMinute) {
        this.timeMinute = timeMinute;
    }

    public boolean isShotMobi() {
        return Util.isShotMobi(code);
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getUserId() {
        return userId;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getLogo() {
        return logo;
    }

    public void setTimeUpdate(Date timeUpdate) {
        this.timeUpdate = timeUpdate;
    }

    public Date getTimeUpdate() {
        return timeUpdate;
    }

    @Override
    public int compareTo(Mobi another) {
        if(another.getId() > this.getId()) {
            return 1;
        } else if(another.getId() < this.getId()) {
            return -1;
        }
        return 0;
    }
}
