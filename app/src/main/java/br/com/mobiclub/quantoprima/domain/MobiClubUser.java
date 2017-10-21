package br.com.mobiclub.quantoprima.domain;

import java.io.Serializable;
import java.util.Date;

import br.com.mobiclub.quantoprima.core.service.api.entity.Localization;
import br.com.mobiclub.quantoprima.util.Util;

@SuppressWarnings("serial")
public class MobiClubUser implements Serializable {

    public static final String FACEBOOK_PROFILE_PICTURE_URL = "https://graph.facebook.com/%s/picture?type=square";
    public static final String GENDER_MALE_TYPE = "male";
    public static final String GENDER_FEMALE_TYPE = "female";

    public static final String GENDER_MALE_STRING = "Masculino";
    public static final String GENDER_FEMALE_STRING = "Feminino";
    public static final String FACEBOOK_PROFILE_PHOTO = "facebook_profile_photo";

    private Integer id;
    private Integer userId;
    private String name;
    protected String lastName;
    private String email;
    private Date birthday;
    private String genderType;

    private Long facebookId;

    private Long facebookAccessExpires;

    private String facebookAccessToken;
    private String hometown;
    private String location;
    private String locale;
    private String profilePicture;
    private Localization localization;
    private Integer notifications = 0;
    private String facebookEmail;
    private String facebokEmail;
    private String cpf = "";
    private Integer httpStatus;
    public MobiClubUser(Integer userId) {
        this.userId = userId;
    }

    public MobiClubUser(long facebookId, String name,String facebookEmail) {
        this.facebookId = facebookId;
        this.facebookEmail = facebookEmail;
        this.name = name;
    }

    public MobiClubUser(String name, String lastName, String email, Date birthday,
                        String genderType, String cpf) {
        this.name = name;
        this.lastName = lastName;
        this.email = email;
        this.birthday = birthday;
        this.genderType = genderType;
        this.cpf = cpf;
    }

    @Override
    public String toString() {
        String string = "MobiClubUser[id=%d, userId=%s, name=%s, lastName=%s, email=%s, facebookEmail=%s, profilePicture=%s, birthday=%s, genderType=%s, facebookId=%s, facebookAccessExpires=%s, facebookAccessToken=%s, hometown=%s, location=%s, locale=%s, cpf=%s]";
        return String.format(string, id, userId, name, lastName, email, facebookEmail, profilePicture, getBirthdayString(), genderType, facebookId, facebookAccessExpires, facebookAccessToken, hometown, location, locale, cpf);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getGenderType() {
        return genderType;
    }

    public void setGenderType(String genderType) {
        this.genderType = genderType;
    }

    public Long getFacebookId() {
        return facebookId;
    }

    public void setFacebookId(Long facebookId) {
        this.facebookId = facebookId;
    }

    public Long getFacebookAccessExpires() {
        return facebookAccessExpires;
    }

    public void setFacebookAccessExpires(Long facebookAccessExpires) {
        this.facebookAccessExpires = facebookAccessExpires;
    }

    public String getFacebookAccessToken() {
        return facebookAccessToken;
    }

    public void setFacebookAccessToken(String facebookAccessToken) {
        this.facebookAccessToken = facebookAccessToken;
    }

    public void setHometown(String hometown) {
        this.hometown = hometown;
    }

    public String getHometown() {
        if(hometown == null)
            return "";
        return hometown;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLocation() {
        if(location == null)
            return "";
        return location;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public String getLocale() {
        if(locale == null)
            return "";
        return locale;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getFullName() {
        if(getLastName() != null) {
            return getName() + " " + getLastName();
        }
        return getName();
    }

    public boolean isConnectedToFacebook() {
        return getFacebookId() != null && getFacebookId() != 0 &&
                getFacebookAccessToken() != null &&
                !getFacebookAccessToken().isEmpty() ? true : false;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getProfilePicture() {
        if(facebookId == null || (facebookId != null && facebookId == 0))
            return null;
        return String.format(FACEBOOK_PROFILE_PICTURE_URL, getFacebookId());
    }

    public String getSignUptBirthdayString() {
        if(birthday != null) {
            return Util.getSignUpDateString(birthday);
        }
        return "";
    }

    public String getGenderString() {
        if(getGenderType() == null)
            return null;
        return getGenderType().equals(GENDER_MALE_TYPE) ? GENDER_MALE_STRING : GENDER_FEMALE_STRING;
    }

    public void setLocalization(Localization localization) {
        this.localization = localization;
    }

    public Localization getLocalization() {
        return localization;
    }

    public void setNotifications(Integer notifications) {
        this.notifications = notifications;
    }

    public Integer getNotifications() {
        return notifications;
    }

    public String getBirthdayString() {
        if(birthday != null) {
            return Util.getDateString(birthday);
        }
        return "";
    }

    public void readNotification() {
        notifications--;
    }

    public String getFacebookEmail() {
        return facebookEmail;
    }

    public void setFacebookEmail(String facebokEmail) {
        this.facebokEmail = facebokEmail;
    }

    public String getFacebokEmail() {
        return facebokEmail;
    }

    public String getCpf() {
        if(cpf==null){
            cpf = "";
        }
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public Integer getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(Integer httpStatus) {
        this.httpStatus = httpStatus;
    }
}
