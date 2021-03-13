package com.superman.cookSelection;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.superman.UserPreference.Lang_FoodPOJO;
import com.superman.databinding.ActivityFrame34Binding;

import java.util.ArrayList;
import java.util.HashMap;

public class Frame34 extends AppCompatActivity implements View.OnClickListener {
    public static HashMap<String, ArrayList<Lang_FoodPOJO>> alldishes;
    private ActivityFrame34Binding binding;
    private Bundle extras;
    private int index;
    private String[] dishes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFrame34Binding.inflate(getLayoutInflater());

        extras = getIntent().getExtras();
        index = extras.getInt("index");

        setListeners();
        makeAllDishes();
    }

    private void makeAllDishes() {
        alldishes = new HashMap<>();
        dishes = new String[]{"Butter Chicken", "Paneer do Pyaza"};
        addEntries("Chinese");
        dishes = new String[]{"Butter Chicken", "Paneer do Pyaza"};
        addEntries("South Indian");
        dishes = new String[]{"Butter Chicken", "Paneer do Pyaza"};
        addEntries("North Indian");
        dishes = new String[]{"Butter Chicken", "Paneer do Pyaza"};
        addEntries("Maharashtrian");
    }

    private void addEntries(String cuisine) {
        ArrayList<Lang_FoodPOJO> arrayList = new ArrayList<>();
        for (String dish : dishes) {
            arrayList.add(new Lang_FoodPOJO(dish, false));
        }
        alldishes.put(cuisine, arrayList);
    }

    private void setListeners() {
        binding.back34.setOnClickListener(this);
        binding.choose.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == binding.back34) {
            this.onBackPressed();
        } else if (v == binding.choose) {
            Intent intent = new Intent(Frame34.this, Frame96.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //todo
    }
}