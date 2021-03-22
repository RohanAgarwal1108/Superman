package com.superman.common;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.superman.databinding.ActivityReconnectBinding;

public class Reconnect extends AppCompatActivity {
    private ActivityReconnectBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityReconnectBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.reconn.setOnClickListener(v -> {
            Intent intent = new Intent();
            setResult(Activity.RESULT_OK, intent);
            finish();
        });

        binding.backreconn.setOnClickListener(v -> onBackPressed());
    }
}