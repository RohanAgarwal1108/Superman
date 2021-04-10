package com.superman.UserPreference;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.Task;
import com.google.firebase.functions.FirebaseFunctionsException;
import com.superman.R;
import com.superman.authentication.User;
import com.superman.common.MainActivity;
import com.superman.common.Reconnect;
import com.superman.cookSelection.frame21;
import com.superman.databinding.ActivityFrame19Binding;
import com.superman.utilities.CustomItemClickListener;
import com.superman.utilities.MyProgressDialog;

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

    private void setUpRecyler() {
        makeArrayList();
        recyclerView = findViewById(R.id.cuisinerecycler);
        layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new Frame19Adapter(this, cuisines, this);
        recyclerView.setAdapter(mAdapter);
    }

    private void makeArrayList() {
        cuisines = new ArrayList<>();
        String[] cuisinesarray = {"North Indian", "South Indian", "North Eastern", "Gujarati", "Maharashtrian", "Chinese", "Italian", "American", "Naga", "Bengali"};
        for (String cuisine : cuisinesarray) {
            cuisines.add(new Lang_FoodPOJO(cuisine, false));
        }
    }

    private void setListeners() {
        binding.back19.setOnClickListener(this);
        binding.next19.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == binding.next19 && binding.next19.getCardBackgroundColor() == getColorStateList(R.color.black)) {
            preferences();
        } else if (v == binding.back19) {
            this.onBackPressed();
        }
    }

    private void preferences() {
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
                        Intent intent = new Intent(Frame19.this, frame21.class);
                        startActivityForResult(intent, 1);
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            finish();
        }
    }

    private Task<HashMap<String, Object>> setPreferences() {
        Map<String, Object> data = new HashMap<>();
        data.put("uid", User.user.getUid());
        data.put("currentCity", "Chennai");
        data.put("cuisine", getCuisines());
        data.put("sex", User.user.getCookgender());
        data.put("cooks", User.user.getMealtype());
        data.put("canSpeak", User.user.getLanguages());
        return MainActivity.mFunctions
                .getHttpsCallable("setPreferences")
                .call(data)
                .continueWith(task -> (HashMap<String, Object>) task.getResult().getData());
    }

    private List<String> getCuisines() {
        List<String> al = new ArrayList<String>();
        for (Lang_FoodPOJO cuisine : cuisines) {
            if (cuisine.isSelected()) {
                al.add(cuisine.getLanguage());
            }
        }
        return al;
    }

    @Override
    public void onCustomItemClick(int index) {
        cuisines.get(index).setSelected(!cuisines.get(index).isSelected());
        mAdapter.notifyDataSetChanged();
        toggleNext();
    }

    private void toggleNext() {
        for (Lang_FoodPOJO cuisine : cuisines) {
            if (cuisine.isSelected()) {
                enableNext();
                return;
            }
        }
        disableNext();
    }

    private void disableNext() {
        binding.next19.setCardBackgroundColor(getColor(R.color.disabledbutton));
    }

    private void enableNext() {
        binding.next19.setCardBackgroundColor(getColor(R.color.black));
    }
}