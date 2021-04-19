package com.superman.home;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.superman.R;
import com.superman.authentication.Frame39;
import com.superman.common.MainActivity;
import com.superman.common.Webview;
import com.superman.databinding.ActivityUserProfileBinding;
import com.superman.utilities.ExtraUtils;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class UserProfile extends AppCompatActivity implements View.OnClickListener {
    public static ActivityUserProfileBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.hc.setOnClickListener(this);
        binding.swf.setOnClickListener(this);
        binding.faq.setOnClickListener(this);
        binding.au.setOnClickListener(this);
        binding.tc.setOnClickListener(this);
        binding.pp.setOnClickListener(this);
        binding.logout.setOnClickListener(this);
        binding.backuserprof.setOnClickListener(this);

        try {
            binding.name.setText(MainActivity.getValue(UserProfile.this, MainActivity.ALIAS3));
        } catch (GeneralSecurityException | IOException e) {
            binding.name.setText("Guest");
        }

        if (ExtraUtils.profilepic != null) {
            binding.userimg.setImageBitmap(ExtraUtils.profilepic);
        }
    }

    @Override
    public void onClick(View v) {
        if (v == binding.faq) {
            startWebView("faq.eule.in", "FAQs");
        } else if (v == binding.au) {
            startWebView("about.eule.in", "About Us");
        } else if (v == binding.tc) {
            startWebView("tc.eule.in", "Terms and Conditions");
        } else if (v == binding.pp) {
            startWebView("privacypolicy.eule.in", "Privacy Policy" +
                    "");
        } else if (v == binding.hc) {
            startWhatsapp("Hey I am facing trouble with <please put here>");
        } else if (v == binding.swf) {
            //todo
            startWhatsapp("todo");
        } else if (v == binding.logout) {
            openDialog();
        } else if (v == binding.backuserprof) {
            this.onBackPressed();
        }
    }

    private void openDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(UserProfile.this, R.style.MyAlertDialogTheme);
        builder.setMessage("Are you sure you want to sign-out?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    FirebaseAuth.getInstance().signOut();
                    Intent intent = new Intent(UserProfile.this, Frame39.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                })
                .setNegativeButton("No", ((dialog, which) -> {
                    dialog.dismiss();
                }));
        AlertDialog alert = builder.create();
        alert.setTitle("");
        alert.show();
    }

    private void startWhatsapp(String str) {
        String url = "https://api.whatsapp.com/send?phone=+917972803790&text=" + str;
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        i.setPackage("com.whatsapp");
        if (i.resolveActivity(getPackageManager()) != null) {
            startActivity(i);
        } else {
            i.setPackage(null);
        }
        startActivity(i);
    }

    private void startWebView(String str, String title) {
        Intent intent = new Intent(UserProfile.this, Webview.class);
        intent.putExtra("url", str);

        intent.putExtra("title", title);
        startActivity(intent);
    }
}