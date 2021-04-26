package com.superman.authentication;

import java.util.List;

public class User {
    public static User user;
    private String mealtype;
    private String cookgender;
    private List<String> languages;
    private String name;
    private String location;

    public static void initUser() {
        user = new User();
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
