package com.superman.home;

public class CardsPOJO {
    private final int cta;
    private final String cardColor;
    private final String details;
    private final String title;

    public CardsPOJO(int cta, String cardColor, String details, String title) {
        this.cta = cta;
        this.cardColor = cardColor;
        this.details = details;
        this.title = title;
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

    public String getTitle() {
        return title;
    }
}
