package com.SuperCook.home;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.SuperCook.authentication.User;
import com.SuperCook.common.MainActivity;
import com.SuperCook.common.Reconnect;
import com.SuperCook.cookSelection.CookDetails;
import com.SuperCook.databinding.ActivityFrame102Binding;
import com.SuperCook.utilities.ExtraUtils;
import com.SuperCook.utilities.MyProgressDialog;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.Map;

public class Frame102 extends AppCompatActivity implements View.OnClickListener {
    ActivityFrame102Binding binding;
    CookDetails cookDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFrame102Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (getIntent().getExtras() != null && getIntent().getExtras().containsKey("index")) {
            int i = getIntent().getIntExtra("index", 0);
            cookDetail = MyTrials.cookDetails.get(i);
        }

        setUI();
    }

    private void setUI() {
        binding.explore.setOnClickListener(this);
        binding.back102.setOnClickListener(this);
        binding.next102.setOnClickListener(this);
        binding.cooked.setText("cooked food by " + cookDetail.getName());
        binding.dialogtext.setText("Welcome to SuperFam, " + User.user.getName() + "!\nYou just took the right step towards SORTED Life!\nEnjoy \uD83E\uDD42");
    }

    @Override
    public void onClick(View v) {
        if (v == binding.explore || v == binding.back102) {
            this.onBackPressed();
        } else if (v == binding.next102) {
            try {
                hireMyCook();
            } catch (GeneralSecurityException | IOException e) {
                e.printStackTrace();
                ExtraUtils.makeToast(Frame102.this, "An error occurred! Please try later.");
            }
        }
    }

    private void hireMyCook() throws GeneralSecurityException, IOException {
        MyProgressDialog myProgressDialog = new MyProgressDialog();
        myProgressDialog.showDialog(Frame102.this);
        hireCook().addOnCompleteListener(task -> {
            myProgressDialog.dismissDialog();
            if (task.isSuccessful()) {
                binding.confirmation.setVisibility(View.VISIBLE);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(Frame102.this, Frame101.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                }, 3000);
            } else {
                Intent intent = new Intent(Frame102.this, Reconnect.class);
                startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            binding.next102.performClick();
        } else {
            this.onBackPressed();
        }
    }

    private Task<HashMap<String, Object>> hireCook() throws GeneralSecurityException, IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("uid", MainActivity.getValue(getApplicationContext(), MainActivity.ALIAS_UID));
        data.put("paymentStatus", false);
        data.put("hiringDate", System.currentTimeMillis());
        data.put("firingDate", 0);
        data.put("cookID", cookDetail.getCookID());
        data.put("plan", 2);
        return MainActivity.mFunctions
                .getHttpsCallable("hireCook")
                .call(data)
                .continueWith(task -> (HashMap<String, Object>) task.getResult().getData());
    }
}