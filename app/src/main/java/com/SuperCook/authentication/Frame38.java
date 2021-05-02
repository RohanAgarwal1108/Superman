package com.SuperCook.authentication;

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
    private PhoneAuthCredential credential;
    private String fcmToken;

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
        credential = PhoneAuthProvider.getCredential(mVerificationId, etOtp);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = task.getResult().getUser();
                        Uid = user.getUid();
                        getFcmToken();
                    } else {
                        myProgressDialog.dismissDialog();
                        if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                            makeToast("Invalid credentials!", Toast.LENGTH_SHORT);
                        }
                    }
                });
    }

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
                                try {
                                    MainActivity.putValues(MainActivity.ALIAS2, "details", getApplicationContext());
                                    MainActivity.putValues(MainActivity.ALIAS1, phno, getApplicationContext());
                                    MainActivity.putValues(MainActivity.ALIAS4, Uid, getApplicationContext());
                                } catch (GeneralSecurityException | IOException generalSecurityException) {
                                    generalSecurityException.printStackTrace();
                                    return;
                                }
                                initializeSegment();
                                Analytics.with(Frame38.this).identify(new Traits().putValue("User ID", Uid));
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
                        initializeSegment();
                        HashMap<String, Object> result = task.getResult();
                        HashMap<String, Object> data = (HashMap<String, Object>) result.get("data");
                        String uid = (String) data.get("uid");
                        String city = (String) data.get("city");
                        String name = (String) data.get("name");
                        Analytics.with(Frame38.this).identify(new Traits().putValue("User ID", uid));
                        try {
                            MainActivity.putValues(MainActivity.ALIAS4, uid, getApplicationContext());
                            MainActivity.putValues(MainActivity.ALIAS3, name, getApplicationContext());
                            MainActivity.putValues(MainActivity.ALIAS1, phno, getApplicationContext());
                        } catch (GeneralSecurityException | IOException e) {
                            e.printStackTrace();
                            makeToast("An Error occurred! Please try again.", Toast.LENGTH_SHORT);
                            return;
                        }
                        boolean preferences = (boolean) data.get("preferences");
                        if (preferences) {
                            try {
                                MainActivity.putValues(MainActivity.ALIAS2, "preferences", getApplicationContext());
                            } catch (GeneralSecurityException | IOException e) {
                                e.printStackTrace();
                                makeToast("An Error occurred! Please try again.", Toast.LENGTH_SHORT);
                                return;
                            }
                            Intent intent = new Intent(this, Frame101.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(this, Frame28.class);
                            startActivity(intent);
                        }
                    }
                });
    }

    private void initializeSegment() {
        Analytics analytics = new Analytics.Builder(getApplicationContext(), "U727ZBegynSOCrtZiQeyCqhgw5HhqJeZ")
                .trackApplicationLifecycleEvents()
                .recordScreenViews()
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