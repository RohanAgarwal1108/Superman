package com.superman.authentication;

import java.util.List;

public class User {
    public static User user;
    private String number;
    private boolean isRegisteredUser;
    private String uid;
    private String name;
    private String city;
    private String mealtype;
    private String cookgender;
    private List<String> languages;

    public static void initUser() {
        user = new User();
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCookgender() {
        return cookgender;
    }

    public void setCookgender(String cookgender) {
        this.cookgender = cookgender;
    }

    public String getMealtype() {
        return mealtype;
    }

    public void setMealtype(String mealtype) {
        this.mealtype = mealtype;
    }

    public List<String> getLanguages() {
        return languages;
    }

    public void setLanguages(List<String> languages) {
        this.languages = languages;
    }

    public boolean isRegisteredUser() {
        return isRegisteredUser;
    }

    public void setRegisteredUser(boolean registeredUser) {
        isRegisteredUser = registeredUser;
    }
}
