package com.SuperCook.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.SuperCook.R;
import com.SuperCook.cookSelection.Frame96Adapter;
import com.SuperCook.cookSelection.Frame96Adapter1;
import com.SuperCook.cookSelection.SelectedDishes;
import com.SuperCook.databinding.ActivityFrame112Binding;
import com.SuperCook.utilities.CustomItemClickListener;
import com.SuperCook.utilities.CustomItemClickListener1;

import java.util.ArrayList;

public class Frame112 extends AppCompatActivity implements CustomItemClickListener, CustomItemClickListener1, View.OnClickListener {
    public static int index = 0;
    String[] keys;
    private ActivityFrame112Binding binding;
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
        binding = ActivityFrame112Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        TailoredMeal.makeAllDishes(2);
        keys = TailoredMeal.dinnerdishes.keySet().toArray(new String[0]);

        setupRecyclers();
        binding.next110.setOnClickListener(this);
        binding.back112.setOnClickListener(this);
    }

    private void setupRecyclers() {
        recyclerView = binding.cuisinelist;
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new Frame96Adapter(this, this, TailoredMeal.dinnerdishes, 3);
        recyclerView.setAdapter(mAdapter);

        recyclerView1 = findViewById(R.id.dishes);
        layoutManager1 = new GridLayoutManager(this, 2);
        recyclerView1.setLayoutManager(layoutManager1);
        mAdapter1 = new Frame96Adapter1(this, TailoredMeal.dinnerdishes.get(TailoredMeal.keys[index]), this, 7, 3);
        recyclerView1.setAdapter(mAdapter1);
    }

    @Override
    public void onClick(View v) {
        if (v == binding.next110) {
            if (SelectedDishes.mealSelectedDishes[2].size() == 0) {
                makeToast(2);
            } else {
                Intent intent = new Intent(Frame112.this, TailoredMeal.class);
                startActivity(intent);
            }
        } else if (v == binding.back112) {
            this.onBackPressed();
        }
    }

    @Override
    public void onCustomItemClick(int i) {
        index = i;
        mAdapter.notifyDataSetChanged();
        ((Frame96Adapter1) mAdapter1).datachanger(TailoredMeal.dinnerdishes.get(TailoredMeal.keys[index]));
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
            toast = Toast.makeText(Frame112.this, "Cannot choose more than 7 items!", Toast.LENGTH_SHORT);
            toast.show();
        } else if (i == 2) {
            toast = Toast.makeText(Frame112.this, "Please choose at least one dish!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        SelectedDishes.mealSelectedDishes[2] = new ArrayList<>();
    }
}