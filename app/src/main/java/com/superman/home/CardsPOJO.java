package com.superman.home;

public class CardsPOJO {
    private final int cta;
    private final String cardColor;
    private final String details;
    private final String title;
    private final String icon;
    private final String url;

    public CardsPOJO(int cta, String cardColor, String details, String title, String icon, String url) {
        this.cta = cta;
        this.cardColor = cardColor;
        this.details = details;
        this.title = title;
        this.icon = icon;
        this.url = url;
    }

    public int getCta() {
        return cta;
    }

    public String getCardColor() {
        return cardColor;
    }

    public String getDetails() {
        return details;
    }

    public String getUrl() {
        return url;
    }

    public String getIcon() {
        return icon;
    }

    public String getTitle() {
        return title;
    }
}
