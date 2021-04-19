package com.superman.home;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.superman.R;
import com.superman.UserPreference.Lang_FoodPOJO;
import com.superman.cookSelection.Frame96Adapter;
import com.superman.cookSelection.Frame96Adapter1;
import com.superman.databinding.ActivityFrame116Binding;
import com.superman.utilities.CustomItemClickListener;
import com.superman.utilities.CustomItemClickListener1;

import java.util.ArrayList;
import java.util.HashMap;

public class Frame116 extends AppCompatActivity implements CustomItemClickListener1, CustomItemClickListener {
    public static int index = 0;
    private static String[] dishes;
    private ActivityFrame116Binding binding;
    private HashMap<String, ArrayList<Lang_FoodPOJO>> mydishes;
    private String[] keys;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView recyclerView1;
    private RecyclerView.Adapter mAdapter1;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.LayoutManager layoutManager1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFrame116Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        makeAllDishes();
        setupRecyclers();
        binding.back116.setOnClickListener(v -> onBackPressed());
    }

    private void setupRecyclers() {
        recyclerView = binding.cuisinelist;
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new Frame96Adapter(this, this, TailoredMeal.breakfastdishes, 4);
        recyclerView.setAdapter(mAdapter);

        recyclerView1 = findViewById(R.id.dishes);
        layoutManager1 = new GridLayoutManager(this, 2);
        recyclerView1.setLayoutManager(layoutManager1);
        mAdapter1 = new Frame96Adapter1(this, mydishes.get(keys[index]), this, 7, 10);
        recyclerView1.setAdapter(mAdapter1);
    }

    private void makeAllDishes() {
        mydishes = new HashMap<>();
        dishes = new String[]{"Aalo Gobi", "Bhindi Masala", "Aalo Paratha", "Rajma Masal Curry", "Maila Kofta", "Dum Aalo", "Chole", "Daal",
                "Daal Makhani", "Pav Bhaji", "Kadhai Panner", "Matar Paneer", "Fried Chicken", "Chole Bhatoore"};
        addEntries("North Indian");

        dishes = new String[]{"Idli Sambhar", "Dosa Sambhar", "Sambhar Rice", "Sambhar Vada", "Dahi Vada", "Veg Kurma", "Biryani", "Upma", "Uthapam"};
        addEntries("South Indian");

        dishes = new String[]{"Khandvi", "Dhokla", "Thepla", "Dal Dhokli", "Handvi", "Fafda", "Shrikhand"};
        addEntries("North Eastern");

        dishes = new String[]{"Vada pav", "Misal Pav", "Puran Poli", "Pav Bhaji", "Sabudana Khichdi", "Poha", "Basundi", "Bharli Bhindi", "Poori Bhaji", "Veg Kolhapuri"};
        addEntries("Maharashtrian");

        dishes = new String[]{"Chow Mein", "Fried Rice", "Chilli potato", "Chilli Paneer"};
        addEntries("Chinese");

        dishes = new String[]{"Pasta", "Pizza"};
        addEntries("Italian");

        dishes = new String[]{"Burger", "Sandwich", "French Toast", "Pancakes"};
        addEntries("American");

        keys = TailoredMeal.breakfastdishes.keySet().toArray(new String[0]);
    }

    private void addEntries(String cuisine) {
        ArrayList<Lang_FoodPOJO> arrayList = new ArrayList<>();
        for (String dish : dishes) {
            arrayList.add(new Lang_FoodPOJO(dish, false));
        }
        mydishes.put(cuisine, arrayList);
    }

    @Override
    public void onCustomItemClick(int i) {
        index = i;
        mAdapter.notifyDataSetChanged();
        ((Frame96Adapter1) mAdapter1).datachanger(mydishes.get(keys[index]));
    }

    @Override
    public void onCustomItemClick1(int i, int ij) {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("result", mydishes.get(keys[index]).get(i).getLanguage());
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }
}