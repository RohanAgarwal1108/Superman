package com.SuperCook.UserPreference;

public class Lang_FoodPOJO {
    private final String language;
    private boolean isSelected;
    private String url;

    public Lang_FoodPOJO(String language, boolean isSelected) {
        this.language = language;
        this.isSelected = isSelected;
    }

    public Lang_FoodPOJO(String language, boolean isSelected, String url) {
        this.language = language;
        this.isSelected = isSelected;
        this.url = url;
    }

    public String getLanguage() {
        return language;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getUrl() {
        return url;
    }
}
