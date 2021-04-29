package com.SuperCook.home;

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

import com.SuperCook.R;
import com.SuperCook.common.MainActivity;
import com.SuperCook.common.Reconnect;
import com.SuperCook.cookSelection.CookDetails;
import com.SuperCook.cookSelection.Frame23;
import com.SuperCook.cookSelection.frame21;
import com.SuperCook.databinding.ActivityMyCooksBinding;
import com.SuperCook.utilities.CookItemClickListener;
import com.SuperCook.utilities.DateFormatter;
import com.SuperCook.utilities.ExtraUtils;
import com.SuperCook.utilities.MyProgressDialog;
import com.google.android.gms.tasks.Task;
import com.google.firebase.functions.FirebaseFunctionsException;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyCooks extends AppCompatActivity implements CookItemClickListener {
    public static final String EXTRA_COOK_IMAGE_TRANSITION_NAME = "transition";
    public static ArrayList<CookDetails> cookDetails;
    public static CookDetails currentCook;
    private ActivityMyCooksBinding binding;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private MyProgressDialog myProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMyCooksBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setUpRecyler();
        getMyCooks();

        binding.backcooks.setOnClickListener(v -> onBackPressed());

        binding.bookcook.setOnClickListener(v -> {
            Intent intent = new Intent(MyCooks.this, frame21.class);
            intent.putExtra("Source", "Mycooks");
            startActivity(intent);
        });

        binding.book.setOnClickListener(v -> {
            Intent intent = new Intent(MyCooks.this, frame21.class);
            intent.putExtra("Source", "Mycooks");
            startActivity(intent);
        });
    }

    private void setUpRecyler() {
        cookDetails = new ArrayList<>();
        recyclerView = binding.prevrecycler;
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new MyCookAdapter(cookDetails, this);
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            getMyCooks();
        } else {
            this.onBackPressed();
        }
    }

    private void getMyCooks() {
        myProgressDialog = new MyProgressDialog();
        myProgressDialog.showDialog(MyCooks.this);
        try {
            getCooks().addOnCompleteListener(task -> {
                myProgressDialog.dismissDialog();
                if (!task.isSuccessful()) {
                    Exception e = task.getException();
                    if (e instanceof FirebaseFunctionsException) {
                        FirebaseFunctionsException ffe = (FirebaseFunctionsException) e;
                        FirebaseFunctionsException.Code code = ffe.getCode();
                        if (code == FirebaseFunctionsException.Code.NOT_FOUND) {
                            binding.booknow.setVisibility(View.VISIBLE);
                            binding.bookedcooks.setVisibility(View.GONE);
                        } else {
                            Intent intent = new Intent(MyCooks.this, Reconnect.class);
                            startActivityForResult(intent, 1);
                        }
                    } else {
                        Intent intent = new Intent(MyCooks.this, Reconnect.class);
                        startActivityForResult(intent, 1);
                    }
                } else {
                    HashMap<String, Object> res = task.getResult();
                    List<HashMap<String, Object>> results = (List<HashMap<String, Object>>) res.get("details");
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
                    List<HashMap<String, Object>> dates = (List<HashMap<String, Object>>) res.get("dates");
                    for (int i = 0; i < dates.size(); i++) {
                        HashMap<String, Object> date = dates.get(i);
                        String id = (String) date.get("cookID");
                        for (int j = 0; j < cookDetails.size(); j++) {
                            if (id.equals(cookDetails.get(j).getCookID())) {
                                cookDetails.get(j).setHiredate((Long) date.get("hiringDate"));
                                if (date.get("firingDate") instanceof Integer) {
                                    currentCook = cookDetails.get(j);
                                    cookDetails.remove(j);
                                } else {
                                    cookDetails.get(j).setFiredate((Long) date.get("firingDate"));
                                }
                                break;
                            }
                        }
                    }
                    setCurrentCook();
                    mAdapter.notifyDataSetChanged();
                }
            });
        } catch (GeneralSecurityException | IOException e) {
            myProgressDialog.dismissDialog();
            ExtraUtils.makeToast(MyCooks.this, "An error occured! Try again later.");
            this.onBackPressed();
        }
    }

    private void setCurrentCook() {
        if (currentCook == null) {
            binding.presentcook.setVisibility(View.GONE);
            return;
        }
        binding.currentname.setText(currentCook.getName());
        Picasso.get().load(currentCook.getCookPic()).placeholder(R.drawable.ic_cook_def).into(binding.currentimg);
        ViewCompat.setTransitionName(binding.currentimg, currentCook.getCookID());
        binding.currentid.setText("#" + currentCook.getCookID());
        String duration = "";
        long hire = currentCook.getHiredate();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(hire);
        duration += DateFormatter.formateDateFromstring("EEE MMM dd HH:mm:ss z yyyy", "MMM dd", calendar.getTime().toString());
        duration += " - Present";
        binding.currentduration.setText(duration);
        binding.presentcook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyCooks.this, Frame23.class);
                intent.putExtra("index", -1);
                intent.putExtra("Mycooks", "false");
                intent.putExtra(EXTRA_COOK_IMAGE_TRANSITION_NAME, ViewCompat.getTransitionName(binding.currentimg));
                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        MyCooks.this,
                        binding.currentimg,
                        ViewCompat.getTransitionName(binding.currentimg));
                startActivity(intent, options.toBundle());
            }
        });
    }

    private Task<HashMap<String, Object>> getCooks() throws GeneralSecurityException, IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("uid", MainActivity.getValue(getApplicationContext(), MainActivity.ALIAS4));
        return MainActivity.mFunctions
                .getHttpsCallable("getMyCooks")
                .call(data)
                .continueWith(task -> (HashMap<String, Object>) task.getResult().getData());
    }

    @Override
    public void onCookItemClick(int position, CookDetails cookDetails, ImageView cookpic) {
        Intent intent = new Intent(MyCooks.this, Frame23.class);
        intent.putExtra("index", position);
        intent.putExtra("Mycooks", "true");
        intent.putExtra(EXTRA_COOK_IMAGE_TRANSITION_NAME, ViewCompat.getTransitionName(cookpic));
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                this,
                cookpic,
                ViewCompat.getTransitionName(cookpic));
        startActivity(intent, options.toBundle());
    }
}