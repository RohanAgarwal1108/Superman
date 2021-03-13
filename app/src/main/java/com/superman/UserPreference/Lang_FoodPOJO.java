package com.superman.UserPreference;

public class Lang_FoodPOJO {
    private String language;
    private boolean isSelected;

    public Lang_FoodPOJO(String language, boolean isSelected) {
        this.language = language;
        this.isSelected = isSelected;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
