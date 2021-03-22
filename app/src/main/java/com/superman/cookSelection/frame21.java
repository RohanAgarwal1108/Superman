package com.superman.cookSelection;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.Task;
import com.google.firebase.functions.FirebaseFunctionsException;
import com.superman.Utilities.CookItemClickListener;
import com.superman.Utilities.CustomItemClickListener1;
import com.superman.Utilities.MyProgressDialog;
import com.superman.authentication.User;
import com.superman.common.MainActivity;
import com.superman.common.Reconnect;
import com.superman.databinding.ActivityFrame21Binding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class frame21 extends AppCompatActivity implements CustomItemClickListener1, CookItemClickListener, View.OnClickListener {
    public static final String EXTRA_ANIMAL_IMAGE_TRANSITION_NAME = "transition";
    public static ArrayList<CookDetails> cookDetails;
    private ActivityFrame21Binding binding;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private MyProgressDialog myProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFrame21Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setListeners();
        setUpRecyler();
        queryCooks();
    }

    private void queryCooks() {
        myProgressDialog = new MyProgressDialog();
        myProgressDialog.showDialog(this);
        getCooks()
                .addOnCompleteListener(task -> {
                    myProgressDialog.dismissDialog();
                    if (!task.isSuccessful()) {
                        int flag = 0;
                        Exception e = task.getException();
                        if (e instanceof FirebaseFunctionsException) {
                            flag = 1;
                            FirebaseFunctionsException ffe = (FirebaseFunctionsException) e;
                            FirebaseFunctionsException.Code code = ffe.getCode();
                            if (code == FirebaseFunctionsException.Code.NOT_FOUND) {
                                //todo
                                //com.superman E/bijb: com.google.firebase.functions.FirebaseFunctionsException: No cooks found that matches your preference try changing something.
                            } else {
                                Intent intent = new Intent(frame21.this, Reconnect.class);
                                startActivityForResult(intent, 1);
                            }
                        }
                        if (flag == 0) {
                            Intent intent = new Intent(frame21.this, Reconnect.class);
                            startActivityForResult(intent, 1);
                        }
                    } else {
                        List<HashMap<String, Object>> results = task.getResult();
                        try {
                            binding.showing.setText("Showing " + results.size() + " SuperCooks");
                            for (int i = 0; i < results.size(); i++) {
                                HashMap<String, Object> result = results.get(i);
                                String city = (String) result.get("city");
                                String cookPic = (String) result.get("pictureURL");
                                String cookGender = (String) result.get("sex");
                                int rating = (int) (result.get("rating") == null ? 0 : result.get("rating"));
                                String bio = (String) result.get("bio");
                                List<String> cuisine = (List<String>) result.get("cuisine");
                                List<String> canSpeak = (List<String>) result.get("canSpeak");
                                String mealtype = (String) result.get("cooks");
                                String charges = (String) result.get("charges");
                                String background = (String) result.get("background");
                                String name = (String) result.get("name");
                                String from = (String) result.get("from");
                                String cookID = (String) result.get("cookID");
                                List<String> booked = new ArrayList<>();
                                if (result.get("slotBooked") != null) {
                                    booked.addAll((List<String>) result.get("slotBooked"));
                                }
                                if (result.get("trailsBooked") != null) {
                                    booked.addAll((List<String>) result.get("trailsBooked"));
                                }
                                List<String> foodPictureURL = (List<String>) result.get("foodPictureURL");
                                cookDetails.add(new CookDetails(city, cookPic, cookGender, rating, bio, cuisine, canSpeak, mealtype, charges, background,
                                        name, from, foodPictureURL, cookID, booked));
                            }
                            mAdapter.notifyDataSetChanged();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            queryCooks();
        } else if (requestCode == 1 && resultCode != RESULT_OK) {
            finish();
        }
    }

    private Task<List<HashMap<String, Object>>> getCooks() {
        Map<String, Object> data = new HashMap<>();
        data.put("uid", User.user.getUid());
        return MainActivity.mFunctions
                .getHttpsCallable("getCooks")
                .call(data)
                .continueWith(task -> (List<HashMap<String, Object>>) task.getResult().getData());
    }

    private void setUpRecyler() {
        cookDetails = new ArrayList<>();
        recyclerView = binding.cookrecycler;
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new Frame21Adapter(cookDetails, this, this);
        recyclerView.setAdapter(mAdapter);
    }

    private void setListeners() {
        binding.changepref.setOnClickListener(this);
        binding.skip.setOnClickListener(this);
    }

    @Override
    public void onCustomItemClick1(int index, int i) {
        if (i == 2) {
            Intent intent = new Intent(frame21.this, Frame34.class);
            intent.putExtra("index", index);
            startActivity(intent);
        }
    }

    @Override
    public void onCookItemClick(int position, CookDetails cookDetails, ImageView cookpic) {
        Intent intent = new Intent(frame21.this, Frame23.class);
        intent.putExtra("index", position);
        intent.putExtra(EXTRA_ANIMAL_IMAGE_TRANSITION_NAME, ViewCompat.getTransitionName(cookpic));
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                this,
                cookpic,
                ViewCompat.getTransitionName(cookpic));
        startActivity(intent, options.toBundle());
    }

    @Override
    public void onClick(View v) {
        if (v == binding.changepref) {
            Intent intent = new Intent();
            setResult(Activity.RESULT_OK, intent);
            finish();
        } else if (v == binding.skip) {
            //todo
        }
    }
}