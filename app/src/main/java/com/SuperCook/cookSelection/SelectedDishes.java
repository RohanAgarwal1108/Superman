package com.SuperCook.cookSelection;

import java.util.ArrayList;
import java.util.List;

public class SelectedDishes {
    public static List<SelectedDishes> selectedDishes;
    public static List<SelectedDishes>[] mealSelectedDishes;

    private int quantity;
    private String name;

    public SelectedDishes(String name, int quantity) {
        this.name = name;
        this.quantity = quantity;
    }

    public static void initSelectedDishes() {
        selectedDishes = new ArrayList<>();
    }

    public static void initMealSelectedDishes() {
        mealSelectedDishes = new List[]{new ArrayList<>(), new ArrayList<>(), new ArrayList<>()};
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
