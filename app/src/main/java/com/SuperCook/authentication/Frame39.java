package com.SuperCook.authentication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.SuperCook.R;
import com.SuperCook.common.MainActivity;
import com.SuperCook.databinding.ActivityFrame39Binding;
import com.SuperCook.utilities.ExitDailog;
import com.SuperCook.utilities.ExtraUtils;
import com.SuperCook.utilities.KeyboardUtil;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class Frame39 extends AppCompatActivity implements TextWatcher, View.OnClickListener {
    private ActivityFrame39Binding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFrame39Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setListeners();
        findViewById(android.R.id.content).setFocusableInTouchMode(true);
        removeOldUserKeys();
    }

    /**
     * To set the click listeners for all clickables on screen
     */
    private void setListeners() {
        binding.phoneeditext.addTextChangedListener(this);
        binding.next39.setOnClickListener(this);
        binding.privatepolicy.setOnClickListener(this);
        binding.termsandcondition.setOnClickListener(this);
        binding.needhelp.setOnClickListener(this);
    }

    /**
     * To remove old key value pairs from Encrypted Shared Preferences to avoid confusion
     */
    private void removeOldUserKeys() {
        try {
            MainActivity.removeValue(Frame39.this, new String[]{MainActivity.ALIAS_PHONENUMBER, MainActivity.ALIAS_STATUS, MainActivity.ALIAS_NAME});
        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
            finishAffinity();
        }
    }

    /**
     * Check if the phone number meets Indian standards
     *
     * @return true and false based on correctness of number
     */
    private boolean isNumberCorrect() {
        String number = binding.phoneeditext.getText().toString().trim();
        if (number.length() == 10 && (number.charAt(0) >= '5' && number.charAt(0) <= '9')) {
            return true;
        } else {
            ExtraUtils.makeToast(Frame39.this, "Please enter a valid mobile number!");
            return false;
        }
    }


    /**
     * Overriding TextWatcher to disable/enable next button when somethong is added to phone number text
     */
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

    /**
     * To enable next button
     */
    private void enableNext() {
        binding.next39.setCardBackgroundColor(getColor(R.color.black));
    }

    /**
     * To disable next button
     */
    private void disableNext() {
        binding.next39.setCardBackgroundColor(getColor(R.color.disabledbutton));
    }

    /**
     * To handle clicks on views
     *
     * @param v to get the view clicked on
     */
    @Override
    public void onClick(View v) {
        if (v == binding.termsandcondition) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http:/tandc.eule.in"));
            startActivity(browserIntent);
            /*
           todo
            Intent intent = new Intent(Frame39.this, Webview.class);
            intent.putExtra("url", "");
            startActivity(intent);*/
        } else if (v == binding.privatepolicy) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://privacypolicies.eule.in"));
            startActivity(browserIntent);
            /*
            Intent intent = new Intent(Frame39.this, Webview.class);
            intent.putExtra("url", "http://privacypolicies.eule.in");
            startActivity(intent);*/
        } else if (v == binding.needhelp) {
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
        } else if (v == binding.next39) {
            if (binding.next39.getCardBackgroundColor() == getColorStateList(R.color.black) && isNumberCorrect()) {
                Intent intent = new Intent(Frame39.this, Frame38.class);
                intent.putExtra("phoneno", binding.phoneeditext.getText().toString().trim());
                startActivity(intent);
            }
        }
    }

    /**
     * Overriding back functionality to ask user if the user wants to exit
     */
    @Override
    public void onBackPressed() {
        openDialog();
    }

    /**
     * To open confirmation dialog
     */
    private void openDialog() {
        ExitDailog exitDailog = new ExitDailog();
        exitDailog.show(getSupportFragmentManager(), "Exit dialog");
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        KeyboardUtil keyboardUtil = new KeyboardUtil(this, ev);
        keyboardUtil.touchEvent();
        return super.dispatchTouchEvent(ev);
    }
}