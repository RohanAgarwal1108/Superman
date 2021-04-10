package com.superman.authentication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.functions.FirebaseFunctionsException;
import com.superman.R;
import com.superman.UserPreference.Frame28;
import com.superman.common.MainActivity;
import com.superman.common.Reconnect;
import com.superman.databinding.ActivityFrame38Binding;
import com.superman.utilities.GenericKeyEvent;
import com.superman.utilities.GenericTextWatcher;
import com.superman.utilities.KeyboardUtil;
import com.superman.utilities.MyProgressDialog;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class Frame38 extends AppCompatActivity implements View.OnClickListener, TextWatcher {
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    Toast toast;
    FirebaseAuth mAuth;
    String mVerificationId;
    PhoneAuthProvider.ForceResendingToken mResendToken;
    CountDownTimer cTimer;
    String etOtp;
    private ActivityFrame38Binding binding;
    private String phno;
    private String Uid;
    private int flag = 1;
    private MyProgressDialog myProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFrame38Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        phno = getIntent().getExtras().getString("phoneno");
        binding.number.setText(phno);
        mAuth = FirebaseAuth.getInstance();
        setListeners();
        setOTPListeners();
        initFireBaseCallbacks();
        sendOTP();
    }

    private void setOTPListeners() {
        binding.otp1.addTextChangedListener(new GenericTextWatcher(binding.otp2, binding.otp1, binding.otp1));
        binding.otp2.addTextChangedListener(new GenericTextWatcher(binding.otp3, binding.otp1, binding.otp2));
        binding.otp3.addTextChangedListener(new GenericTextWatcher(binding.otp4, binding.otp2, binding.otp3));
        binding.otp4.addTextChangedListener(new GenericTextWatcher(binding.otp5, binding.otp3, binding.otp3));
        binding.otp5.addTextChangedListener(new GenericTextWatcher(binding.otp6, binding.otp4, binding.otp5));
        binding.otp6.addTextChangedListener(new GenericTextWatcher(binding.otp6, binding.otp5, binding.otp6));
        binding.otp1.addTextChangedListener(this);
        binding.otp2.addTextChangedListener(this);
        binding.otp3.addTextChangedListener(this);
        binding.otp4.addTextChangedListener(this);
        binding.otp5.addTextChangedListener(this);
        binding.otp6.addTextChangedListener(this);
        binding.otp1.setOnKeyListener(new GenericKeyEvent(binding.otp1, null, binding.otp2));
        binding.otp2.setOnKeyListener(new GenericKeyEvent(binding.otp2, binding.otp1, binding.otp3));
        binding.otp3.setOnKeyListener(new GenericKeyEvent(binding.otp3, binding.otp2, binding.otp4));
        binding.otp4.setOnKeyListener(new GenericKeyEvent(binding.otp4, binding.otp3, binding.otp5));
        binding.otp5.setOnKeyListener(new GenericKeyEvent(binding.otp5, binding.otp4, binding.otp6));
        binding.otp6.setOnKeyListener(new GenericKeyEvent(binding.otp6, binding.otp5, null));
    }

    private void sendOTP() {
        myProgressDialog = new MyProgressDialog();
        myProgressDialog.showDialog(this);
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber("+91" + phno)
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(this)
                        .setCallbacks(mCallbacks)
                        .setForceResendingToken(mResendToken)
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    /**
     * Checking the otp with firebase
     */
    private void firebaseOTPCheck() {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, etOtp);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Uid = task.getResult().getUser().getUid();
                        getUserbyPhone();
                    } else {
                        myProgressDialog.dismissDialog();
                        if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                            makeToast("Invalid credentials!", Toast.LENGTH_SHORT);
                        }
                    }
                });
    }

    private void getUserbyPhone() {
        checkPhone(phno)
                .addOnCompleteListener(task -> {
                    myProgressDialog.dismissDialog();
                    if (!task.isSuccessful()) {
                        Exception e = task.getException();
                        if (e instanceof FirebaseFunctionsException) {
                            FirebaseFunctionsException ffe = (FirebaseFunctionsException) e;
                            FirebaseFunctionsException.Code code = ffe.getCode();
                            if (code == FirebaseFunctionsException.Code.NOT_FOUND) {
                                User.initUser();
                                User.user.setNumber(phno);
                                User.user.setRegisteredUser(false);
                                User.user.setUid(Uid);
                                Intent intent = new Intent(Frame38.this, Frame47.class);
                                startActivity(intent);
                            } else {
                                Intent intent = new Intent(Frame38.this, Reconnect.class);
                                startActivity(intent);
                            }
                        } else {
                            Intent intent = new Intent(Frame38.this, Reconnect.class);
                            startActivity(intent);
                        }
                    } else {
                        User.initUser();
                        User.user.setNumber(phno);
                        User.user.setRegisteredUser(true);
                        HashMap<String, Object> result = task.getResult();
                        HashMap<String, Object> data = (HashMap<String, Object>) result.get("data");
                        String uid = (String) data.get("uid");
                        String city = (String) data.get("city");
                        String name = (String) data.get("name");
                        boolean preferences = (boolean) data.get("preferences");
                        if (preferences) {

                        } else {
                            User.user.setNumber(phno);
                            User.user.setRegisteredUser(true);
                            User.user.setUid(uid);
                            User.user.setName(name);
                            Intent intent = new Intent(this, Frame28.class);
                            startActivity(intent);
                        }
                    }
                });
    }

    /**
     * Callbacks for checking otp
     */
    void initFireBaseCallbacks() {
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                myProgressDialog.dismissDialog();
                binding.next38.performClick();
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                flag = 2;
                myProgressDialog.dismissDialog();
                makeToast("OTP couldn't be sent. Please try again!", Toast.LENGTH_SHORT);
            }

            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {
                myProgressDialog.dismissDialog();
                flag = 1;
                makeToast("OTP Sent", Toast.LENGTH_SHORT);
                mVerificationId = verificationId;
                mResendToken = token;
                changeResend();
            }
        };
    }

    private void changeResend() {
        cTimer = new CountDownTimer(60000, 1000) {
            //when timer is started
            public void onTick(long millisUntilFinished) {
                binding.didnt.setText("Resend code in ");
                long i = millisUntilFinished / 1000;
                binding.resend.setText((i >= 0 && i <= 9 ? "0:0" : "0:") + i);
            }

            //when timer is finished
            public void onFinish() {
                binding.resend.setText("Resend");
                binding.didnt.setText("Didn't receive? ");
            }
        };
        cTimer.start();
    }

    /**
     * Cancelling the timer
     */
    void cancelTimer() {
        if (cTimer != null)
            cTimer.cancel();
    }

    /**
     * Cancelling the timer when app is closed to avoid memory leak
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        cancelTimer();
    }

    private void makeToast(String str, int lengthShort) {
        if (toast != null) {
            toast.cancel();
        }
        toast = Toast.makeText(Frame38.this, str, lengthShort);
        toast.show();
    }

    private void setListeners() {
        binding.edit.setOnClickListener(this);
        binding.back38.setOnClickListener(this);
        binding.resend.setOnClickListener(this);
        binding.next38.setOnClickListener(this);
        binding.nh.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == binding.edit || v == binding.back38) {
            onBackPressed();
        } else if (v == binding.resend) {
            if (binding.resend.getText().equals("Resend")) {
                sendOTP();
            }
        } else if (v == binding.next38) {
            if (binding.next38.getCardBackgroundColor() == getColorStateList(R.color.black)) {
                if (flag == 1) {
                    myProgressDialog.showDialog(this);
                    firebaseOTPCheck();
                } else if (flag == 2) {
                    makeToast("OTP could'nt be sent. Please try again!", Toast.LENGTH_SHORT);
                }
            }
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
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        etOtp = binding.otp1.getText().toString() + binding.otp2.getText().toString() +
                binding.otp3.getText().toString() + binding.otp4.getText().toString() +
                binding.otp5.getText().toString() + binding.otp6.getText().toString();
        if (etOtp.length() != 6) {
            disableNext();
        } else {
            enableNext();
        }
    }

    private void enableNext() {
        binding.next38.setCardBackgroundColor(getColor(R.color.black));
    }

    private void disableNext() {
        binding.next38.setCardBackgroundColor(getColor(R.color.disabledbutton));
    }

    private Task<HashMap<String, Object>> checkPhone(String text) {
        Map<String, Object> data = new HashMap<>();
        data.put("phoneNo", text);
        return MainActivity.mFunctions
                .getHttpsCallable("checkPhone")
                .call(data)
                .continueWith(task -> (HashMap<String, Object>) task.getResult().getData());
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        KeyboardUtil keyboardUtil = new KeyboardUtil(this, ev);
        keyboardUtil.touchEvent();
        return super.dispatchTouchEvent(ev);
    }
}