package com.superman.UserPreference;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.superman.R;
import com.superman.authentication.User;
import com.superman.databinding.ActivityFrame28Binding;
import com.superman.utilities.CustomItemClickListener;
import com.superman.utilities.LogoutDailog;

import java.util.ArrayList;
import java.util.List;

public class Frame28 extends AppCompatActivity implements View.OnClickListener, CustomItemClickListener {
    private ActivityFrame28Binding binding;
    private int mealtype = -1;
    private int cookgender = -1;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Lang_FoodPOJO> languages;

    /**
     * for later use
     */
    /*
    public static Location location = null;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private int i = 0;
    private Typeface nb;
    private Typeface nl;
    private int tracker=0;*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFrame28Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setUI();
        setListeners();
        setUpRecyler();
    }

    private void setUpRecyler() {
        makeArrayList();
        recyclerView = findViewById(R.id.speakrecycler);
        layoutManager = new GridLayoutManager(this, 4);
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new Frame28Adapter(this, languages, this);
        recyclerView.setAdapter(mAdapter);
    }

    private void makeArrayList() {
        languages = new ArrayList<>();
        languages.add(new Lang_FoodPOJO("Assamese", false));
        languages.add(new Lang_FoodPOJO("Bengali", false));
        languages.add(new Lang_FoodPOJO("Bodo", false));
        languages.add(new Lang_FoodPOJO("Dogri", false));
        languages.add(new Lang_FoodPOJO("Gujarati", false));
        languages.add(new Lang_FoodPOJO("Hindi", false));
        languages.add(new Lang_FoodPOJO("Kannada", false));
        languages.add(new Lang_FoodPOJO("Kashmiri", false));
        languages.add(new Lang_FoodPOJO("Konkani", false));
        languages.add(new Lang_FoodPOJO("Maithili", false));
        languages.add(new Lang_FoodPOJO("Malayalam", false));
        languages.add(new Lang_FoodPOJO("Manipuri", false));
        languages.add(new Lang_FoodPOJO("Marathi", false));
        languages.add(new Lang_FoodPOJO("Nepali", false));
        languages.add(new Lang_FoodPOJO("Oriya", false));
        languages.add(new Lang_FoodPOJO("Punjabi", false));
        languages.add(new Lang_FoodPOJO("Sanskrit", false));
        languages.add(new Lang_FoodPOJO("Santhali", false));
        languages.add(new Lang_FoodPOJO("Sindhi", false));
        languages.add(new Lang_FoodPOJO("Tamil", false));
        languages.add(new Lang_FoodPOJO("Telugu", false));
        languages.add(new Lang_FoodPOJO("Urdu", false));
    }

    private void setListeners() {
        for (int i = 0; i < 3; i++) {
            binding.type.getChildAt(i).setOnClickListener(this);
            binding.whatlin.getChildAt(i).setOnClickListener(this);
        }
        binding.next28.setOnClickListener(this);
    }

    private void setUI() {
        //for later use
        //nb = Typeface.createFromAsset(getAssets(), "fonts/nb.otf");
        //nl = Typeface.createFromAsset(getAssets(), "fonts/nl.otf");
        String fullname = User.user.getName();
        int i = fullname.indexOf(' ');
        String name = i == -1 ? fullname : fullname.substring(0, i);
        String str = "Hi " + name + ", so need a supercook that";
        binding.hello.setText(str);
    }

    @Override
    public void onClick(View v) {
        if (v == binding.type.getChildAt(0)) {
            setMealType(0);
        } else if (v == binding.type.getChildAt(1)) {
            setMealType(1);
        } else if (v == binding.type.getChildAt(2)) {
            setMealType(2);
        } else if (v == binding.whatlin.getChildAt(0)) {
            setCookGender(0);
        } else if (v == binding.whatlin.getChildAt(1)) {
            setCookGender(1);
        } else if (v == binding.whatlin.getChildAt(2)) {
            setCookGender(2);
        } else if (v == binding.next28 && binding.next28.getCardBackgroundColor() == getColorStateList(R.color.black)) {
            if (mealtype == 0) {
                User.user.setMealtype("Veg");
            } else if (mealtype == 1) {
                User.user.setMealtype("Non-Veg");
            } else {
                User.user.setMealtype("Any");
            }
            if (cookgender == 0) {
                User.user.setCookgender("Male");
            } else if (cookgender == 1) {
                User.user.setCookgender("Female");
            } else {
                User.user.setCookgender("Any");
            }
            List<String> al = new ArrayList<String>();
            for (Lang_FoodPOJO language : languages) {
                if (language.isSelected()) {
                    al.add(language.getLanguage());
                }
            }
            User.user.setLanguages(al);
            Intent intent = new Intent(Frame28.this, Frame19.class);
            startActivity(intent);
        }
    }

    private void setCookGender(int i) {
        if (cookgender != -1 && cookgender != i) {
            ((CardView) binding.whatlin.getChildAt(cookgender)).setCardBackgroundColor(getColor(R.color.white));
        }
        ((CardView) binding.whatlin.getChildAt(i)).setCardBackgroundColor(getColor(R.color.yellow));
        cookgender = i;
        toggleNext();
    }

    private void toggleNext() {
        if (mealtype == -1 || cookgender == -1) {
            disableNext();
            return;
        } else {
            for (Lang_FoodPOJO language : languages) {
                if (language.isSelected()) {
                    enableNext();
                    return;
                }
            }
            disableNext();
        }
    }

    private void setMealType(int i) {
        if (mealtype != -1 && mealtype != i) {
            ((CardView) binding.type.getChildAt(mealtype)).setCardBackgroundColor(getColor(R.color.white));
        }
        ((CardView) binding.type.getChildAt(i)).setCardBackgroundColor(getColor(R.color.yellow));
        mealtype = i;
        toggleNext();
    }

    @Override
    public void onCustomItemClick(int index) {
        languages.get(index).setSelected(!languages.get(index).isSelected());
        mAdapter.notifyDataSetChanged();
        toggleNext();
    }

    private void disableNext() {
        binding.next28.setCardBackgroundColor(getColor(R.color.disabledbutton));
    }

    private void enableNext() {

        binding.next28.setCardBackgroundColor(getColor(R.color.black));
    }

    @Override
    public void onBackPressed() {
        openDialog();
    }

    private void openDialog() {
        LogoutDailog logoutDialog = new LogoutDailog();
        logoutDialog.show(getSupportFragmentManager(), "Logout dialog");
    }

    /*for later use*/
/*
    private void getLocationPermission() {
        if (ContextCompat.checkSelfPermission(Frame28.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        } else {
            getLocation();
        }
    }

    @SuppressLint("MissingPermission")
    private void getLocation() {
        if (isLocationEnabled()) {
            fusedLocationProviderClient.getLastLocation().addOnCompleteListener(task -> {
                location = task.getResult();
                if (location == null) {
                    requestNewLocationData();
                } else {
                    //todo
                    //getNearbyAirport(this);
                }
            });
        } else if (i == 0) {
            i = 1;
            DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        tracker=1;
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(intent);
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        dialog.dismiss();
                        break;
                }
            };

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Do you wish to enable GPS for location based services?")
                    .setPositiveButton("Yes", dialogClickListener)
                    .setNegativeButton("No", dialogClickListener);
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(this, R.color.black));
            alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(this, R.color.black));
            TextView textView = alertDialog.getWindow().findViewById(android.R.id.message);
            Button button1 = alertDialog.getWindow().findViewById(android.R.id.button1);
            Button button2 = alertDialog.getWindow().findViewById(android.R.id.button2);
            textView.setTypeface(nb);
            button1.setTypeface(nb);
            button2.setTypeface(nb);
        }
    }

    @SuppressLint("MissingPermission")
    private void requestNewLocationData() {

        // Initializing LocationRequest
        // object with appropriate methods
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(5);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        // setting LocationRequest
        // on FusedLocationClient
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        fusedLocationProviderClient.requestLocationUpdates(mLocationRequest, new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                location = locationResult.getLastLocation();
                //todo
                //getNearbyAirport(Activity18.this);
            }
        }, Looper.myLooper());
    }

    // method to check
    // if location is enabled
    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
            getLocation();
        }
    }

    private boolean checkPermissions() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (checkPermissions() && tracker==1) {
            getLocation();
            tracker=0;
        }
    }*/
}