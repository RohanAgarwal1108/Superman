package com.SuperCook.common;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.SuperCook.R;
import com.SuperCook.authentication.Frame39;
import com.SuperCook.utilities.ExitDailog;
import com.cuberto.liquid_swipe.LiquidPager;

public class OnBoarding extends AppCompatActivity {
    static Context context;

    /**
     * to go to authentication directly
     */
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

    @Override
    public void onBackPressed() {
        openDialog();
    }

    /**
     * Open confirmation dialog if user wants to leave the app
     */
    private void openDialog() {
        ExitDailog exitDailog = new ExitDailog();
        exitDailog.show(getSupportFragmentManager(), "Exit dialog");
    }
}