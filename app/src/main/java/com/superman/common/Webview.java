package com.superman.common;

import android.os.Bundle;
import android.webkit.WebView;

import androidx.appcompat.app.AppCompatActivity;

import com.superman.R;
import com.superman.databinding.ActivityWebviewBinding;

public class Webview extends AppCompatActivity {
    Bundle extras;
    ActivityWebviewBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWebviewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        extras = getIntent().getExtras();

        String url = extras.getString("url");
        WebView webView = findViewById(R.id.webview);

        if (extras.containsKey("title")) {
            binding.title.setText(extras.getString("title"));
        }

        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(url);

        binding.backweb.setOnClickListener(v -> onBackPressed());
    }
}