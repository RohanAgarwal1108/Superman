package com.SuperCook.authentication;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.SuperCook.BuildConfig;
import com.SuperCook.R;
import com.SuperCook.UserPreference.Frame28;
import com.SuperCook.common.MainActivity;
import com.SuperCook.common.Reconnect;
import com.SuperCook.databinding.ActivityFrame38Binding;
import com.SuperCook.home.Frame101;
import com.SuperCook.utilities.ExtraUtils;
import com.SuperCook.utilities.GenericKeyEvent;
import com.SuperCook.utilities.GenericTextWatcher;
import com.SuperCook.utilities.KeyboardUtil;
import com.SuperCook.utilities.MyProgressDialog;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.functions.FirebaseFunctionsException;
import com.google.firebase.messaging.FirebaseMessaging;
import com.segment.analytics.Analytics;
import com.segment.analytics.Traits;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class Frame38 extends AppCompatActivity implements View.OnClickListener, TextWatcher {
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
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
    private String fcmToken;
    private static final String SEGMENT_KEY = BuildConfig.SEGMENT_KEY;

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

        sendOTP();
        initFireBaseCallbacks();
    }

    /**
     * Custom text change listeneres for OTP boxes
     */
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

    /**
     * To send OTP through Firebase
     */
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
    private void firebaseOTPCheck(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = task.getResult().getUser();
                        Uid = user.getUid();
                        getFcmToken();
                    } else {
                        myProgressDialog.dismissDialog();
                        if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                            ExtraUtils.makeToast(Frame38.this, "Invalid credentials!");
                        }
                    }
                });
    }

    /**
     * To get firebase cloud messaging token for the user
     */
    private void getFcmToken() {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        myProgressDialog.dismissDialog();
                        ExtraUtils.makeToast(Frame38.this, "Please check your internet connection and try again later!");
                        return;
                    }
                    fcmToken = task.getResult();
                    getUserbyPhone();
                });
    }

    /**
     * check if the user exists with SuperCook or not
     */
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
                                saveDetailsLocally(Uid, phno, null, false);

                                initializeSegment();
                                Analytics.with(Frame38.this).identify(new Traits().putValue("User ID", Uid));

                                Intent intent = new Intent(Frame38.this, Frame47.class);
                                startActivity(intent);
                            } else {
                                gotoReconnectScreen();
                            }
                        } else {
                            gotoReconnectScreen();
                        }
                    } else {
                        initializeSegment();
                        userFound(task);
                    }
                });
    }

    /**
     * To change the screen if user is found in database
     *
     * @param task contains details about the user found
     */
    private void userFound(Task<HashMap<String, Object>> task) {
        HashMap<String, Object> result = task.getResult();
        HashMap<String, Object> data = (HashMap<String, Object>) result.get("data");

        String uid = (String) data.get("uid");
        //String city = (String) data.get("city");
        String name = (String) data.get("name");
        boolean preferences = (boolean) data.get("preferences");

        Analytics.with(Frame38.this).identify(new Traits().putValue("User ID", uid));

        int valuesSavedStatus = saveDetailsLocally(uid, phno, name, preferences);
        if (valuesSavedStatus == -1) {
            ExtraUtils.makeToast(Frame38.this, "An Error occurred! Please try again.");
            return;
        } else if (valuesSavedStatus == 2) {
            Intent intent = new Intent(this, Frame101.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        } else {
            Intent intent = new Intent(this, Frame28.class);
            startActivity(intent);
        }
    }

    /**
     * TO go to the reconnect screen in case of error
     */
    private void gotoReconnectScreen() {
        Intent intent = new Intent(Frame38.this, Reconnect.class);
        startActivity(intent);
    }

    /**
     * Saving the user details locally in Encrypted Shared Preferences
     *
     * @param Uid         contains the user id
     * @param phonenumber contains phone number of user
     * @param name        contains name of user
     * @param preferences checks if the user has set preferences or not
     * @return
     */
    private int saveDetailsLocally(String Uid, String phonenumber, String name, boolean preferences) {
        try {
            if (name == null) {
                MainActivity.putValues(MainActivity.ALIAS_STATUS, "details", getApplicationContext());
            } else {
                MainActivity.putValues(MainActivity.ALIAS_NAME, name, getApplicationContext());
            }
            MainActivity.putValues(MainActivity.ALIAS_PHONENUMBER, phonenumber, getApplicationContext());
            MainActivity.putValues(MainActivity.ALIAS_UID, Uid, getApplicationContext());
            if (preferences) {
                MainActivity.putValues(MainActivity.ALIAS_STATUS, "preferences", getApplicationContext());
                return 2;
            }
            return 1;
        } catch (GeneralSecurityException | IOException generalSecurityException) {
            generalSecurityException.printStackTrace();
            return -1;
        }
    }

    /**
     * To initialize segment to track user
     */
    private void initializeSegment() {
        Analytics analytics = new Analytics.Builder(getApplicationContext(), SEGMENT_KEY)
                .trackApplicationLifecycleEvents()
                .build();
        Analytics.setSingletonInstance(analytics);
    }

    /**
     * Callbacks for checking otp
     */
    void initFireBaseCallbacks() {
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                firebaseOTPCheck(credential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                flag = 2;
                myProgressDialog.dismissDialog();
                ExtraUtils.makeToast(Frame38.this, "OTP couldn't be sent. Please try again!");
            }

            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {
                myProgressDialog.dismissDialog();
                flag = 1;
                ExtraUtils.makeToast(Frame38.this, "Code Sent");
                mVerificationId = verificationId;
                mResendToken = token;
                changeResend();
            }
        };
    }

    /**
     * TO change the UI for showing 60 sec counter on OTP send on screen
     */
    private void changeResend() {
        cTimer = new CountDownTimer(60000, 1000) {
            public void onTick(long millisUntilFinished) {
                binding.didnt.setText("Resend code in ");
                long i = millisUntilFinished / 1000;
                binding.resend.setText((i >= 0 && i <= 9 ? "0:0" : "0:") + i);
            }

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

    /**
     * Setting listeners on all clickables on screen
     */
    private void setListeners() {
        binding.edit.setOnClickListener(this);
        binding.back38.setOnClickListener(this);
        binding.resend.setOnClickListener(this);
        binding.next38.setOnClickListener(this);
        binding.needhelp.setOnClickListener(this);
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
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, etOtp);
                    firebaseOTPCheck(credential);
                } else if (flag == 2) {
                    ExtraUtils.makeToast(Frame38.this, "OTP couldn't be sent. Please try again!");
                }
            }
        } else if (v == binding.needhelp) {
            ExtraUtils.openWhatsapp("Hey Supercook! I need help!", Frame38.this);
        }
    }

    /**
     * to disable/enable next button if otp is filled or not
     */
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

    /**
     * to enable next button
     */
    private void enableNext() {
        binding.next38.setCardBackgroundColor(getColor(R.color.black));
    }

    /**
     * to disable next button
     */
    private void disableNext() {
        binding.next38.setCardBackgroundColor(getColor(R.color.disabledbutton));
    }

    /**
     * Send request to server to find user by phone number
     *
     * @param phnumber to get the phone number to be checked
     * @return
     */
    private Task<HashMap<String, Object>> checkPhone(String phnumber) {
        Map<String, Object> data = new HashMap<>();
        data.put("phoneNo", phnumber);
        data.put("fcm", fcmToken);
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