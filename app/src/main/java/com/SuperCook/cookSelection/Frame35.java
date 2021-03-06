package com.SuperCook.cookSelection;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.SuperCook.databinding.ActivityFrame35Binding;
import com.SuperCook.home.Frame101;
import com.SuperCook.utilities.DateFormatter;
import com.SuperCook.utilities.ExitDailog;
import com.segment.analytics.Analytics;
import com.squareup.picasso.Picasso;

public class Frame35 extends AppCompatActivity implements View.OnClickListener {
    private ActivityFrame35Binding binding;
    private Bundle extras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = com.SuperCook.databinding.ActivityFrame35Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        extras = getIntent().getExtras();
        setupUI();
        setAnalytics();
    }

    private void setAnalytics() {
        Analytics.with(Frame35.this).screen("Trial Booking", "Trial Booked Screen", null, null);
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

        setupListeners();
    }

    private void setupListeners() {
        binding.help.setOnClickListener(this);
        binding.next35.setOnClickListener(this);
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
            if (i == 0) {
                str += "Meal\n\n";
            }
            if (i != 0 || i != SelectedDishes.selectedDishes.size() - 1) {
                str += "  " + selectedDish.getName() + " x " + selectedDish.getQuantity() + "\n";
            } else {
                str += "  " + selectedDish.getName() + " x " + selectedDish.getQuantity();
            }
        }
        return str;
    }

    @Override
    public void onBackPressed() {
        openDialog();
    }

    private void openDialog() {
        ExitDailog exitDailog = new ExitDailog();
        exitDailog.show(getSupportFragmentManager(), "Exit dialog");
    }

    @Override
    public void onClick(View v) {
        if (v == binding.help) {
            String url = "https://api.whatsapp.com/send?phone=+917972803790&text=Hey Supercook! I need help!";
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            i.setPackage("com.whatsapp");
            if (i.resolveActivity(getPackageManager()) != null) {
                startActivity(i);
            } else {
                i.setPackage(null);
            }
            startActivity(i);
        } else if (v == binding.next35) {
            Intent intent = new Intent(Frame35.this, Frame101.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }
}