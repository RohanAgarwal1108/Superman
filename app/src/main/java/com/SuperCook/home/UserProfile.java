package com.SuperCook.home;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ShareCompat;

import com.SuperCook.R;
import com.SuperCook.authentication.Frame39;
import com.SuperCook.authentication.User;
import com.SuperCook.common.Webview;
import com.SuperCook.databinding.ActivityUserProfileBinding;
import com.SuperCook.utilities.ExtraUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.segment.analytics.Analytics;

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
        binding.mc.setOnClickListener(this);
        binding.mt.setOnClickListener(this);

        try {
            binding.name.setText(User.user.getName());
            binding.location.setText(User.user.getLocation());
        } catch (Exception e) {
            binding.name.setText("Guest");
        }

        if (ExtraUtils.profilepic != null) {
            binding.userimg.setImageBitmap(ExtraUtils.profilepic);
        }
    }

    @Override
    public void onClick(View v) {
        if (v == binding.faq) {
            startWebView("https://www.notion.so/FAQ-f3e302236d504ce6b90cecbae073b0a8", "FAQs");
        } else if (v == binding.au) {
            startWebView("https://about.eule.in", "About Us");
        } else if (v == binding.tc) {
            startWebView("https://tc.eule.in", "Terms and Conditions");
        } else if (v == binding.pp) {
            startWebView("https://privacypolicy.eule.in", "Privacy Policy");
        } else if (v == binding.hc) {
            startWhatsapp("Hey Team SuperCook! I need help.");
        } else if (v == binding.swf) {
            ShareCompat.IntentBuilder.from(UserProfile.this)
                    .setType("text/plain")
                    .setChooserTitle("SuperCook")
                    .setText("Want delicious food served to you staright from your kitchen? Download SuperCook now- https://play.google.com/store/apps/details?id=com.SuperCook")
                    .startChooser();
        } else if (v == binding.logout) {
            openDialog();
        } else if (v == binding.backuserprof) {
            this.onBackPressed();
        } else if (v == binding.mc) {
            Intent intent = new Intent(UserProfile.this, MyCooks.class);
            startActivity(intent);
        } else if (v == binding.mt) {
            Intent intent = new Intent(UserProfile.this, MyTrials.class);
            startActivity(intent);
        }
    }

    private void openDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(UserProfile.this, R.style.MyAlertDialogTheme);
        builder.setMessage("Are you sure you want to sign-out?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    FirebaseAuth.getInstance().signOut();
                    Analytics.with(UserProfile.this).flush();
                    Analytics.with(UserProfile.this).reset();
                    Analytics.with(UserProfile.this).shutdown();
                    Intent intent = new Intent(UserProfile.this, Frame39.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                })
                .setNegativeButton("No", ((dialog, which) -> dialog.dismiss()));
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