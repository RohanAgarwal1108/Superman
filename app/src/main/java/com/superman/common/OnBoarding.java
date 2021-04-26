package com.superman.common;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.cuberto.liquid_swipe.LiquidPager;
import com.superman.R;
import com.superman.authentication.Frame39;

public class OnBoarding extends AppCompatActivity {
    static Context context;

    public static void skip() {
        Intent intent = new Intent(context, Frame39.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_boarding);
        context = this;
        LiquidPager pager = findViewById(R.id.pager);
        pager.setAdapter(new Adapter(getSupportFragmentManager()));
    }
}