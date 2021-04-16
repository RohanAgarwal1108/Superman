package com.superman.common;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.functions.FirebaseFunctions;
import com.superman.R;

public class MainActivity extends AppCompatActivity {
    public static FirebaseFunctions mFunctions = FirebaseFunctions.getInstance("us-central1");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}