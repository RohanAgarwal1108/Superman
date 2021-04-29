package com.SuperCook.home;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.SuperCook.common.MainActivity;
import com.SuperCook.common.Reconnect;
import com.SuperCook.cookSelection.CookDetails;
import com.SuperCook.cookSelection.Frame23;
import com.SuperCook.cookSelection.frame21;
import com.SuperCook.databinding.ActivityMyTrialsBinding;
import com.SuperCook.utilities.CookItemClickListener1;
import com.SuperCook.utilities.ExtraUtils;
import com.SuperCook.utilities.MyProgressDialog;
import com.google.android.gms.tasks.Task;
import com.google.firebase.functions.FirebaseFunctionsException;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyTrials extends AppCompatActivity implements CookItemClickListener1 {
    private ActivityMyTrialsBinding binding;
    public static final String EXTRA_COOK_IMAGE_TRANSITION_NAME = "transition";
    public static ArrayList<CookDetails> cookDetails;
    public static ArrayList<CookDetails> currentTrials;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView recyclerView1;
    private RecyclerView.Adapter mAdapter1;
    private RecyclerView.LayoutManager layoutManager1;
    private MyProgressDialog myProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMyTrialsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setUpRecylers();
        getMyTrials();

        binding.book.setOnClickListener(v -> {
            Intent intent = new Intent(MyTrials.this, frame21.class);
            intent.putExtra("Source", "Mycooks");
            startActivity(intent);
        });

        binding.help.setOnClickListener(v -> {
            String url = "https://api.whatsapp.com/send?phone=+917972803790&text=Hey Supercook! I am facing trouble booking trials.";
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            i.setPackage("com.whatsapp");
            if (i.resolveActivity(getPackageManager()) != null) {
                startActivity(i);
            } else {
                i.setPackage(null);
            }
            startActivity(i);
        });

        binding.backtrials.setOnClickListener(v -> onBackPressed());
    }

    private void setUpRecylers() {
        cookDetails = new ArrayList<>();
        recyclerView = binding.prevrecycler;
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new MyTrialAdapter(cookDetails, this, false, this);
        recyclerView.setAdapter(mAdapter);

        currentTrials = new ArrayList<>();
        recyclerView1 = binding.ongoingrecycler;
        recyclerView1.setHasFixedSize(true);
        layoutManager1 = new LinearLayoutManager(this);
        recyclerView1.setLayoutManager(layoutManager1);
        mAdapter1 = new MyTrialAdapter(currentTrials, this, true, this);
        recyclerView1.setAdapter(mAdapter1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            getMyTrials();
        } else {
            this.onBackPressed();
        }
    }

    private void getMyTrials() {
        myProgressDialog = new MyProgressDialog();
        myProgressDialog.showDialog(MyTrials.this);
        try {
            getTrials().addOnCompleteListener(task -> {
                myProgressDialog.dismissDialog();
                if (!task.isSuccessful()) {
                    Exception e = task.getException();
                    if (e instanceof FirebaseFunctionsException) {
                        FirebaseFunctionsException ffe = (FirebaseFunctionsException) e;
                        FirebaseFunctionsException.Code code = ffe.getCode();
                        if (code == FirebaseFunctionsException.Code.NOT_FOUND) {
                            binding.booknow.setVisibility(View.VISIBLE);
                            binding.bookedtrials.setVisibility(View.GONE);
                        } else {
                            Intent intent = new Intent(MyTrials.this, Reconnect.class);
                            startActivityForResult(intent, 1);
                        }
                    } else {
                        Intent intent = new Intent(MyTrials.this, Reconnect.class);
                        startActivityForResult(intent, 1);
                    }
                } else {
                    binding.booknow.setVisibility(View.GONE);
                    binding.bookedtrials.setVisibility(View.VISIBLE);
                    HashMap<String, Object> res = task.getResult();

                    List<HashMap<String, Object>> results = (List<HashMap<String, Object>>) res.get("cookDetails");
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
                        booked.addAll((List<String>) result.get("trailsBooked"));
                        List<String> foodPictureURL = (List<String>) result.get("foodPictureURL");
                        cookDetails.add(new CookDetails(city, cookPic, cookGender, rating, bio, cuisine, canSpeak, mealtype, charges, background,
                                name, from, foodPictureURL, cookID, booked, false));
                    }

                    List<HashMap<String, Object>> trials = (List<HashMap<String, Object>>) res.get("trailDetails");
                    for (int i = 0; i < trials.size(); i++) {
                        HashMap<String, Object> trial = trials.get(i);
                        String id = (String) trial.get("cookID");
                        for (int j = 0; j < cookDetails.size(); j++) {
                            if (id.equals(cookDetails.get(j).getCookID())) {
                                if (cookDetails.get(j).getSlotbooked() != null) {
                                    continue;
                                }
                                cookDetails.get(j).setSlotbooked((String) trial.get("slotBooked"));
                                cookDetails.get(j).setSuperCode((String) trial.get("superCode"));
                                cookDetails.get(j).setMeal((String) trial.get("meal"));
                                cookDetails.get(j).setQuantity((String) trial.get("quantity"));
                                cookDetails.get(j).setAddress((String) trial.get("address"));
                                if (trial.get("status").equals("ongoing")) {
                                    currentTrials.add(cookDetails.get(j));
                                    cookDetails.remove(j);
                                }
                                break;
                            }
                        }
                    }
                    toggleRecyclers();
                    mAdapter1.notifyDataSetChanged();
                    mAdapter.notifyDataSetChanged();
                }
            });
        } catch (GeneralSecurityException | IOException e) {
            myProgressDialog.dismissDialog();
            ExtraUtils.makeToast(MyTrials.this, "An error occured! Try again later.");
            this.onBackPressed();
        }
    }

    private void toggleRecyclers() {
        if (currentTrials.size() == 0) {
            binding.ongoingrecycler.setVisibility(View.GONE);
            binding.ongoingrel.setVisibility(View.GONE);
        }
        if (cookDetails.size() == 0) {
            binding.card.setVisibility(View.GONE);
            binding.previouscook.setVisibility(View.GONE);
        }
    }

    private Task<HashMap<String, Object>> getTrials() throws GeneralSecurityException, IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("uid", MainActivity.getValue(getApplicationContext(), MainActivity.ALIAS4));
        return MainActivity.mFunctions
                .getHttpsCallable("getMyTrails")
                .call(data)
                .continueWith(task -> (HashMap<String, Object>) task.getResult().getData());
    }

    @Override
    public void onCookItemClick(int position, CookDetails cookDetails, ImageView cookpic, boolean isOngoing) {
        Intent intent = new Intent(MyTrials.this, Frame23.class);
        intent.putExtra("index", position);
        intent.putExtra("MyTrials", String.valueOf(isOngoing));
        intent.putExtra(EXTRA_COOK_IMAGE_TRANSITION_NAME, ViewCompat.getTransitionName(cookpic));
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                this,
                cookpic,
                ViewCompat.getTransitionName(cookpic));
        startActivity(intent, options.toBundle());
    }
}