package com.superman.UserPreference;

import java.util.List;

public class CookDetails {
    private final List<String> booked;
    private String city;
    private String cookPic;
    private String cookGender;
    private int rating;
    private String bio;
    private List<String> cuisine;
    private List<String> canSpeak;
    private String mealtype;
    private String charges;
    private String background;
    private String name;
    private String from;
    private List<String> foodPictureURL;
    private String cookID;

    public CookDetails(String city, String cookPic, String cookGender, int rating, String bio, List<String> cuisine, List<String> canSpeak, String mealtype, String charges, String background,
                       String name, String from, List<String> foodPictureURL, String cookID, List<String> booked) {
        this.background = background;
        this.bio = bio;
        this.canSpeak = canSpeak;
        this.charges = charges;
        this.city = city;
        this.cookGender = cookGender;
        this.cookPic = cookPic;
        this.cuisine = cuisine;
        this.foodPictureURL = foodPictureURL;
        this.from = from;
        this.mealtype = mealtype;
        this.rating = rating;
        this.name = name;
        this.cookID = cookID;
        this.booked = booked;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public List<String> getCuisine() {
        return cuisine;
    }

    public void setCuisine(List<String> cuisine) {
        this.cuisine = cuisine;
    }

    public String getMealtype() {
        return mealtype;
    }

    public void setMealtype(String mealtype) {
        this.mealtype = mealtype;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public List<String> getCanSpeak() {
        return canSpeak;
    }

    public void setCanSpeak(List<String> canSpeak) {
        this.canSpeak = canSpeak;
    }

    public String getCookGender() {
        return cookGender;
    }

    public void setCookGender(String cookGender) {
        this.cookGender = cookGender;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCookPic() {
        return cookPic;
    }

    public void setCookPic(String cookPic) {
        this.cookPic = cookPic;
    }

    public String getCharges() {
        return charges;
    }

    public void setCharges(String charges) {
        this.charges = charges;
    }

    public List<String> getFoodPictureURL() {
        return foodPictureURL;
    }

    public void setFoodPictureURL(List<String> foodPictureURL) {
        this.foodPictureURL = foodPictureURL;
    }

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getCookID() {
        return cookID;
    }

    public void setCookID(String cookID) {
        this.cookID = cookID;
    }

    public List<String> getBooked() {
        return booked;
    }
}
