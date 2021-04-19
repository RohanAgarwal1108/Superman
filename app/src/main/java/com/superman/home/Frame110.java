package com.superman.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.superman.R;
import com.superman.cookSelection.Frame96Adapter;
import com.superman.cookSelection.Frame96Adapter1;
import com.superman.cookSelection.SelectedDishes;
import com.superman.databinding.ActivityFrame110Binding;
import com.superman.utilities.CustomItemClickListener;
import com.superman.utilities.CustomItemClickListener1;

public class Frame110 extends AppCompatActivity implements CustomItemClickListener, CustomItemClickListener1, View.OnClickListener {
    public static int index = 0;
    String[] keys;
    private ActivityFrame110Binding binding;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView recyclerView1;
    private RecyclerView.Adapter mAdapter1;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.LayoutManager layoutManager1;
    private Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFrame110Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        TailoredMeal.makeAllDishes(0);
        SelectedDishes.initMealSelectedDishes();
        keys = TailoredMeal.breakfastdishes.keySet().toArray(new String[0]);

        setupRecyclers();
        binding.next110.setOnClickListener(this);
        binding.back110.setOnClickListener(this);
    }

    private void setupRecyclers() {
        recyclerView = binding.cuisinelist;
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new Frame96Adapter(this, this, TailoredMeal.breakfastdishes, 1);
        recyclerView.setAdapter(mAdapter);

        recyclerView1 = findViewById(R.id.dishes);
        layoutManager1 = new GridLayoutManager(this, 2);
        recyclerView1.setLayoutManager(layoutManager1);
        mAdapter1 = new Frame96Adapter1(this, TailoredMeal.breakfastdishes.get(TailoredMeal.keys[index]), this, 7, 1);
        recyclerView1.setAdapter(mAdapter1);
    }

    @Override
    public void onClick(View v) {
        if (v == binding.next110) {
            if (SelectedDishes.mealSelectedDishes[0].size() == 0) {
                makeToast(2);
            } else {
                Intent intent = new Intent(Frame110.this, Frame111.class);
                startActivity(intent);
            }
        } else if (binding.back110 == v) {
            this.onBackPressed();
        }
    }

    @Override
    public void onCustomItemClick(int i) {
        index = i;
        mAdapter.notifyDataSetChanged();
        ((Frame96Adapter1) mAdapter1).datachanger(TailoredMeal.breakfastdishes.get(TailoredMeal.keys[index]));
    }

    @Override
    public void onCustomItemClick1(int i, int ij) {
        if (ij == 0) {
            mAdapter1.notifyDataSetChanged();
        } else {
            makeToast(1);
        }
    }

    private void makeToast(int i) {
        if (toast != null) {
            toast.cancel();
        }
        if (i == 1) {
            toast = Toast.makeText(Frame110.this, "Cannot choose more than 7 items!", Toast.LENGTH_SHORT);
            toast.show();
        } else if (i == 2) {
            toast = Toast.makeText(Frame110.this, "Please choose at least one dish!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}