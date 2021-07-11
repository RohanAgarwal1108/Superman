package com.SuperCook.UserPreference;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.SuperCook.R;
import com.SuperCook.authentication.User;
import com.SuperCook.common.MainActivity;
import com.SuperCook.common.Reconnect;
import com.SuperCook.cookSelection.frame21;
import com.SuperCook.databinding.ActivityFrame19Binding;
import com.SuperCook.utilities.CustomItemClickListener;
import com.SuperCook.utilities.ExtraUtils;
import com.SuperCook.utilities.MyProgressDialog;
import com.google.android.gms.tasks.Task;
import com.google.firebase.functions.FirebaseFunctionsException;
import com.segment.analytics.Analytics;
import com.segment.analytics.Properties;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Frame19 extends AppCompatActivity implements View.OnClickListener, CustomItemClickListener {
    private ActivityFrame19Binding binding;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Lang_FoodPOJO> cuisines;
    private MyProgressDialog myProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFrame19Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setListeners();
        setUpRecyler();
    }

    /**
     * To set up recyclerview for types of cuisine
     */
    private void setUpRecyler() {
        makeArrayList();
        recyclerView = findViewById(R.id.cuisinerecycler);
        layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new Frame19Adapter(this, cuisines, this);
        recyclerView.setAdapter(mAdapter);
    }

    /**
     * To make a list of cuisines to display
     */
    private void makeArrayList() {
        cuisines = new ArrayList<>();
        String[] cuisinesarray = {"North Indian", "South Indian", "North Eastern", "Maharashtrian", "Chinese", "Italian", "American"};
        for (String cuisine : cuisinesarray) {
            cuisines.add(new Lang_FoodPOJO(cuisine, false));
        }
    }

    /**
     * To set up listeners for clickables on screeen
     */
    private void setListeners() {
        binding.back19.setOnClickListener(this);
        binding.next19.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == binding.next19 && binding.next19.getCardBackgroundColor() == getColorStateList(R.color.black)) {
            try {
                preferences();
            } catch (GeneralSecurityException | IOException e) {
                e.printStackTrace();
                ExtraUtils.makeToast(Frame19.this, "An Error occurred! Please try later");
            }
        } else if (v == binding.back19) {
            this.onBackPressed();
        }
    }

    /**
     * set preferences for user in backend
     *
     * @throws GeneralSecurityException
     * @throws IOException
     */
    private void preferences() throws GeneralSecurityException, IOException {
        myProgressDialog = new MyProgressDialog();
        myProgressDialog.showDialog(this);
        setPreferences()
                .addOnCompleteListener(task -> {
                    myProgressDialog.dismissDialog();
                    if (!task.isSuccessful()) {
                        Exception e = task.getException();
                        if (e instanceof FirebaseFunctionsException) {
                            FirebaseFunctionsException ffe = (FirebaseFunctionsException) e;
                            FirebaseFunctionsException.Code code = ffe.getCode();
                        }
                        Intent intent = new Intent(Frame19.this, Reconnect.class);
                        startActivity(intent);
                    } else {
                        sendAnalytics();
                        try {
                            MainActivity.putValues(MainActivity.ALIAS_STATUS, "preferences", getApplicationContext());
                        } catch (GeneralSecurityException | IOException e) {
                            e.printStackTrace();
                            ExtraUtils.makeToast(Frame19.this, "An Error occurred! Please try again.");
                            return;
                        }
                        if (getIntent().getExtras() != null && getIntent().getExtras().containsKey("state")) {
                            Intent returnIntent = new Intent();
                            setResult(Activity.RESULT_OK, returnIntent);
                            finish();
                        } else {
                            Intent intent = new Intent(Frame19.this, frame21.class);
                            startActivityForResult(intent, 1);
                        }
                    }
                });
    }

    /**
     * To send data to segment
     */
    private void sendAnalytics() {
        Analytics.with(Frame19.this).screen("User Preferences", "Food Preferences", getProperties(), null);
    }

    /**
     * To get properyies to send to segment
     *
     * @return
     */
    private Properties getProperties() {
        Properties properties = new Properties();
        try {
            properties.put("uid", MainActivity.getValue(getApplicationContext(), MainActivity.ALIAS_UID));
            properties.put("currentCity", User.user.getCity());
            properties.put("cuisine", getCuisines());
            properties.put("sex", User.user.getCookgender());
            properties.put("cooks", User.user.getMealtype());
            properties.put("canSpeak", User.user.getLanguages());
            return properties;
        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            finish();
        }
    }

    /**
     * To create request to cloud function to save preferences
     *
     * @return
     * @throws GeneralSecurityException
     * @throws IOException
     */
    private Task<HashMap<String, Object>> setPreferences() throws GeneralSecurityException, IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("uid", MainActivity.getValue(getApplicationContext(), MainActivity.ALIAS_UID));
        data.put("currentCity", User.user.getCity());
        data.put("cuisine", getCuisines());
        data.put("sex", User.user.getCookgender());
        data.put("cooks", User.user.getMealtype());
        data.put("canSpeak", User.user.getLanguages());
        return MainActivity.mFunctions
                .getHttpsCallable("setPreferences")
                .call(data)
                .continueWith(task -> (HashMap<String, Object>) task.getResult().getData());
    }

    /**
     * To get the list of selected cuisines
     *
     * @return
     */
    private List<String> getCuisines() {
        List<String> al = new ArrayList<>();
        for (Lang_FoodPOJO cuisine : cuisines) {
            if (cuisine.isSelected()) {
                al.add(cuisine.getLanguage());
            }
        }
        return al;
    }

    /**
     * handling clicks on cuisines
     *
     * @param index
     */
    @Override
    public void onCustomItemClick(int index) {
        cuisines.get(index).setSelected(!cuisines.get(index).isSelected());
        mAdapter.notifyDataSetChanged();
        toggleNext();
        changeSelected();
    }

    /**
     * To disable/enable next button on clicking cuisine item
     */
    private void toggleNext() {
        for (Lang_FoodPOJO cuisine : cuisines) {
            if (cuisine.isSelected()) {
                enableNext();
                return;
            }
        }
        disableNext();
    }

    /**
     * To disable next button
     */
    private void disableNext() {
        binding.next19.setCardBackgroundColor(getColor(R.color.disabledbutton));
    }

    /**
     * To enable next button
     */
    private void enableNext() {
        binding.next19.setCardBackgroundColor(getColor(R.color.black));
    }

    /**
     * To change the counter selected cuisines
     */
    private void changeSelected() {
        int counter = 0;
        int total = cuisines.size();
        for (Lang_FoodPOJO cuisine : cuisines) {
            if (cuisine.isSelected()) {
                counter++;
            }
        }
        binding.selected.setText("(" + counter + "/" + total + ") Selected");
    }
}