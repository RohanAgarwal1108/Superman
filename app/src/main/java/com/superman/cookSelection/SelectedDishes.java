package com.superman.cookSelection;

import java.util.ArrayList;
import java.util.List;

public class SelectedDishes {
    public static List<SelectedDishes> selectedDishes;
    private int quantity;
    private String name;

    public SelectedDishes(String name, int quantity) {
        this.name = name;
        this.quantity = quantity;
    }

    public static void initSelectedDishes() {
        selectedDishes = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
