package com.superman.authentication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.superman.R;
import com.superman.Utilities.KeyboardUtil;
import com.superman.Utilities.LogoutDailog;
import com.superman.databinding.ActivityFrame39Binding;

public class Frame39 extends AppCompatActivity implements TextWatcher, View.OnClickListener {
    private ActivityFrame39Binding binding;
    private Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFrame39Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.phoneedit.addTextChangedListener(this);
        binding.next39.setOnClickListener(v -> {
            if (binding.next39.getCardBackgroundColor() == getColorStateList(R.color.black) && isNumberCorrect()) {
                Intent intent = new Intent(Frame39.this, Frame38.class);
                intent.putExtra("phoneno", binding.phoneedit.getText().toString().trim());
                startActivity(intent);
            }
        });

        binding.pp.setOnClickListener(this);
        binding.tac.setOnClickListener(this);
        binding.nh.setOnClickListener(this);
        findViewById(android.R.id.content).setFocusableInTouchMode(true);
    }

    private boolean isNumberCorrect() {
        String number = binding.phoneedit.getText().toString().trim();
        if (number.length() == 10 && (number.charAt(0) >= '5' && number.charAt(0) <= '9')) {
            return true;
        } else {
            showToast();
            return false;
        }
    }

    private void showToast() {
        if (toast != null) {
            toast.cancel();
        }
        toast = Toast.makeText(Frame39.this, "Please enter a valid mobile number!", Toast.LENGTH_SHORT);
        toast.show();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (s.toString().length() == 0) {
            disableNext();
        } else {
            enableNext();
        }
    }

    private void enableNext() {
        binding.next39.setCardBackgroundColor(getColor(R.color.black));
    }

    private void disableNext() {
        binding.next39.setCardBackgroundColor(getColor(R.color.disabledbutton));
    }

    @Override
    public void onClick(View v) {
        if (v == binding.tac) {
            Intent intent = new Intent(Frame39.this, Webview.class);
            intent.putExtra("url", "www.tandc.eule.in");
            startActivity(intent);
        } else if (v == binding.pp) {
            Intent intent = new Intent(Frame39.this, Webview.class);
            intent.putExtra("url", "www.privacypolicies.eule.in");
            startActivity(intent);
        } else if (v == binding.nh) {
            String url = "https://api.whatsapp.com/send?phone=+917972803790&text=Hey Supercook! I need help!";
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
    }

    @Override
    public void onBackPressed() {
        openDialog();
    }

    private void openDialog() {
        LogoutDailog logoutDialog = new LogoutDailog();
        logoutDialog.show(getSupportFragmentManager(), "Logout dialog");
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        KeyboardUtil keyboardUtil = new KeyboardUtil(this, ev);
        keyboardUtil.touchEvent();
        return super.dispatchTouchEvent(ev);
    }
}