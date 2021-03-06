package com.SuperCook.common;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.SuperCook.databinding.ActivityReconnectBinding;

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

        binding.backreconn.setOnClickListener(v -> {
            if (getIntent().getExtras() != null && getIntent().getExtras().containsKey("refresh")) {
                Intent intent = new Intent();
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
            onBackPressed();
        });
    }
}