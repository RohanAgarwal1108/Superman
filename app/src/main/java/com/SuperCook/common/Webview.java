package com.SuperCook.common;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

import com.SuperCook.R;
import com.SuperCook.databinding.ActivityWebviewBinding;

public class Webview extends AppCompatActivity {
    Bundle extras;
    ActivityWebviewBinding binding;

    @SuppressLint("SetJavaScriptEnabled")
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
        webView.setWebViewClient(new MyWebViewClient());
        webView.loadUrl(url);

        binding.backweb.setOnClickListener(v -> onBackPressed());
    }

    private static class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return false;
        }
    }
}