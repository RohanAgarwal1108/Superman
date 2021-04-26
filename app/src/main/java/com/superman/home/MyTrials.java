package com.superman.home;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.superman.databinding.ActivityMyTrialsBinding;

public class MyTrials extends AppCompatActivity {
    private ActivityMyTrialsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMyTrialsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}