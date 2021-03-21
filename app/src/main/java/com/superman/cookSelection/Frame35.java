package com.superman.cookSelection;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;
import com.superman.Utilities.DateFormatter;
import com.superman.databinding.ActivityFrame35Binding;

public class Frame35 extends AppCompatActivity {
    private ActivityFrame35Binding binding;
    private Bundle extras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = com.superman.databinding.ActivityFrame35Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        extras = getIntent().getExtras();
        setupUI();
    }

    private void setupUI() {
        CookDetails cookDetail = frame21.cookDetails.get(extras.getInt("index"));
        Picasso.get().load(cookDetail.getCookPic()).into(binding.photo);
        binding.cookname.setText(cookDetail.getName());
        binding.superCode.setText(extras.getString("superCode"));
        binding.food.setText(getFood());
        binding.time.setText(getFormattedTime());
        binding.address.setText(extras.getString("address"));
        binding.ttd.setText(extras.getString("details"));
    }

    private String getFormattedTime() {
        String unformattedTime = extras.getString("time");
        String res = DateFormatter.formateDateFromstring("EEE MMM dd HH:mm:ss z yyyy", "dd MMMM hh : mm ", unformattedTime);
        res += DateFormatter.formateDateFromstring("EEE MMM dd HH:mm:ss z yyyy", "a", unformattedTime).toUpperCase();
        return res;
    }

    private String getFood() {
        String str = "";
        for (int i = 0; i < SelectedDishes.selectedDishes.size(); i++) {
            SelectedDishes selectedDish = SelectedDishes.selectedDishes.get(i);
            if (i != 0 || i != SelectedDishes.selectedDishes.size() - 1) {
                str += "  " + selectedDish.getName() + " x " + selectedDish.getQuantity();
            } else {
                str += "  " + selectedDish.getName() + " x " + selectedDish.getQuantity() + "\n";
            }
        }
        return str;
    }
}