package br.com.mobiclub.quantoprima.core.service.api.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

public class User implements Serializable {

    @SerializedName("HttpStatus")
    private Integer httpStatus;

    @SerializedName("Message")
    private String message;

    private static final long serialVersionUID = -7495897652017488896L;

    @SerializedName("Id")
    private Integer id;

    @SerializedName("Name")
    protected String name;
    protected String lastName;

    @SerializedName("Email")
    private String email;

    @SerializedName("Gender")
    private String gender;

    @SerializedName("CPF")
    private String cpf;

    @SerializedName("Birthday")
    private Date birth;

    @SerializedName("FacebookId")
    private Long facebookId;

    @SerializedName("facebookAccessExpires")
    private Long facebookAccessExpires;

    @SerializedName("FacebookAccessToken")
    private String facebookAccessToken;

    public User(int id) {
        this.id = id;
    }

    public User(String name, String lastName, String email, Date birth, String gender) {
        this.name = name;
        this.lastName = lastName;
        this.email = email;
        this.birth = birth;
        this.gender = gender;
    }

    public User(String name) {
        this.name = name;
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

    public void setName(final String name) {
        this.name = name;
    }

    public String getSessionToken() {
        return String.valueOf(getId());
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(final String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getGender() {
        return gender;
    }

    public Date getBirth() {
        return birth;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setBirth(Date birth) {
        this.birth = birth;
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

    public Integer getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(Integer httpStatus) {
        this.httpStatus = httpStatus;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean isSuccess() {
        return httpStatus == 200;
    }

    public Boolean is412() {
        return httpStatus == 412;
    }

}
