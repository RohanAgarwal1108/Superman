package com.superman.cookSelection;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sinaseyfi.advancedcardview.AdvancedCardView;
import com.superman.R;
import com.superman.Utilities.CustomItemClickListener;
import com.superman.Utilities.CustomItemClickListener1;
import com.superman.databinding.ActivityFrame96Binding;

public class Frame96 extends AppCompatActivity implements CustomItemClickListener, CustomItemClickListener1 {
    public static int index = 0;
    String[] keys;
    private ActivityFrame96Binding binding;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView recyclerView1;
    private RecyclerView.Adapter mAdapter1;
    private RecyclerView recyclerView2;
    private RecyclerView.Adapter mAdapter2;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.LayoutManager layoutManager1;
    private RecyclerView.LayoutManager layoutManager2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFrame96Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        keys = Frame34.alldishes.keySet().toArray(new String[0]);

        setupRecyclers();
        toggleBottomSheet();
    }

    private void toggleBottomSheet() {
        AdvancedCardView bottomSheet = findViewById(R.id.bottom_sheet);
        if (SelectedDishes.selectedDishes == null || SelectedDishes.selectedDishes.size() == 0) {
            bottomSheet.setVisibility(View.GONE);
            binding.buttonlin.setVisibility(View.GONE);
        } else {
            bottomSheet.setVisibility(View.VISIBLE);
            binding.buttonlin.setVisibility(View.VISIBLE);
        }
    }

    private void setupRecyclers() {
        recyclerView = binding.cuisinelist;
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new Frame96Adapter(this, this, Frame34.alldishes);
        recyclerView.setAdapter(mAdapter);

        recyclerView1 = findViewById(R.id.dishes);
        layoutManager1 = new GridLayoutManager(this, 2);
        recyclerView1.setLayoutManager(layoutManager1);
        mAdapter1 = new Frame96Adapter1(this, Frame34.alldishes.get(keys[index]), this);
        recyclerView1.setAdapter(mAdapter1);

        recyclerView2 = findViewById(R.id.quantityrecycler);
        layoutManager2 = new LinearLayoutManager(this);
        recyclerView2.setLayoutManager(layoutManager2);
        mAdapter2 = new Frame96Adapter2(this);
        recyclerView2.setAdapter(mAdapter2);
    }

    @Override
    public void onCustomItemClick(int i) {
        index = i;
        mAdapter.notifyDataSetChanged();
        ((Frame96Adapter1) mAdapter1).datachanger(Frame34.alldishes.get(keys[index]));
    }

    @Override
    public void onCustomItemClick1(int i, int ij) {
        if (ij == 0) {
            mAdapter1.notifyDataSetChanged();
            toggleBottomSheet();
            mAdapter2.notifyDataSetChanged();
        } else {
            makeToast();
        }
    }

    private void makeToast() {
        Toast.makeText(Frame96.this, "Cannot choose more than 3 items!", Toast.LENGTH_SHORT).show();
    }
}