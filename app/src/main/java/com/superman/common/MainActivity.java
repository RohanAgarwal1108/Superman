package com.superman.common;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.functions.FirebaseFunctions;
import com.segment.analytics.Analytics;
import com.superman.R;
import com.superman.UserPreference.Frame28;
import com.superman.authentication.Frame39;
import com.superman.authentication.Frame47;
import com.superman.home.Frame101;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class MainActivity extends AppCompatActivity {
    public static FirebaseFunctions mFunctions = FirebaseFunctions.getInstance("us-central1");
    public static String ALIAS3 = "name";
    public static String ALIAS4 = "Uid";
    public static String ALIAS1 = "phonenumber";
    public static String ALIAS2 = "status";
    public static String ALIAS5 = "onboarding";
    static String masterKeyAlias = null;
    private static SharedPreferences sharedPreferences = null;

    private static void createPrefInstance(Context context) throws GeneralSecurityException, IOException {
        masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC);
        sharedPreferences = EncryptedSharedPreferences.create(
                "supercook_shared_prefs",
                masterKeyAlias,
                context,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        );
    }

    public static void putValues(String Alias, String value, Context context) throws GeneralSecurityException, IOException {
        if (masterKeyAlias == null || sharedPreferences == null) {
            createPrefInstance(context);
        }
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Alias, value);
        editor.apply();
    }

    public static String getValue(Context context, String Alias) throws GeneralSecurityException, IOException {
        if (masterKeyAlias == null || sharedPreferences == null) {
            createPrefInstance(context);
        }
        if (sharedPreferences.contains(Alias)) {
            return sharedPreferences.getString(Alias, null);
        } else {
            return null;
        }
    }

    public static void removeValue(Context context, String[] alias) throws GeneralSecurityException, IOException {
        if (masterKeyAlias == null || sharedPreferences == null) {
            createPrefInstance(context);
        }
        SharedPreferences.Editor editor = sharedPreferences.edit();
        for (int i = 0; i < alias.length; i++) {
            if (sharedPreferences.contains(alias[i])) {
                editor.remove(alias[i]);
            }
        }
        editor.apply();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(ContextCompat.getColor(MainActivity.this, R.color.mattblack));
        setContentView(R.layout.activity_main);
        initializeSegment();
        new Handler().postDelayed(this::checkOnboarding, 3 * 1000);
    }

    private void initializeSegment() {
        Analytics analytics = new Analytics.Builder(MainActivity.this, "U727ZBegynSOCrtZiQeyCqhgw5HhqJeZ")
                .trackApplicationLifecycleEvents()
                .recordScreenViews()
                .build();
        Analytics.setSingletonInstance(analytics);
    }

    private void checkOnboarding() {
        try {
            String string = getValue(MainActivity.this, ALIAS5);
            if (string == null) {
                putValues(ALIAS5, "true", MainActivity.this);
                Intent intent = new Intent(MainActivity.this, OnBoarding.class);
                startActivity(intent);
            } else {
                checkSignedInUser();
            }
        } catch (GeneralSecurityException | IOException e) {
            checkSignedInUser();
        }
    }

    private void checkSignedInUser() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            try {
                String str = getValue(MainActivity.this, ALIAS2);
                if (str == null) {
                    Intent intent = new Intent(MainActivity.this, Frame47.class);
                    startActivity(intent);
                } else if (str.equals("preferences")) {
                    Intent intent = new Intent(MainActivity.this, Frame101.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                } else if (str.equals("midpref")) {
                    Intent intent = new Intent(MainActivity.this, Frame28.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(MainActivity.this, Frame47.class);
                    startActivity(intent);
                }
            } catch (GeneralSecurityException | IOException e) {
                e.printStackTrace();
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(MainActivity.this, Frame39.class);
                startActivity(intent);
            }
        } else {
            Intent intent = new Intent(MainActivity.this, Frame39.class);
            startActivity(intent);
        }
        finish();
    }
}