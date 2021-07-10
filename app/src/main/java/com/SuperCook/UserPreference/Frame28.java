package com.SuperCook.UserPreference;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.SuperCook.R;
import com.SuperCook.authentication.User;
import com.SuperCook.common.MainActivity;
import com.SuperCook.common.Reconnect;
import com.SuperCook.databinding.ActivityFrame28Binding;
import com.SuperCook.utilities.CustomItemClickListener;
import com.SuperCook.utilities.ExitDailog;
import com.SuperCook.utilities.MyProgressDialog;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.segment.analytics.Analytics;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Frame28 extends AppCompatActivity implements View.OnClickListener, CustomItemClickListener {
    private ActivityFrame28Binding binding;
    private int mealtype = -1;
    private int cookgender = -1;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Lang_FoodPOJO> languages;
    FirebaseFirestore db;
    int state = 0;
    private String[] cities;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFrame28Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        db = FirebaseFirestore.getInstance();

        if (getIntent().getExtras() != null && getIntent().getExtras().containsKey("state")) {
            state = 1;
        }

        setUI();
        setListeners();
        setUpRecycler();
        Analytics.with(Frame28.this).screen("User Preferences", "Cook Preferences", null, null);
    }

    /**
     * To set spinner to choose city of service
     */
    private void setSpinner() {
        ArrayAdapter ad = new ArrayAdapter(this, android.R.layout.simple_spinner_item, cities) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                Typeface externalFont = Typeface.createFromAsset(getAssets(), "fonts/nb.otf");
                ((TextView) v).setTypeface(externalFont);
                return v;
            }

            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View v = super.getDropDownView(position, convertView, parent);
                Typeface externalFont = Typeface.createFromAsset(getAssets(), "fonts/nb.otf");
                ((TextView) v).setTypeface(externalFont);
                return v;
            }
        };
        ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinner.setAdapter(ad);
        binding.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                binding.city.setText(cities[position].trim());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    /**
     * To set up recyclerview to select languages
     */
    private void setUpRecycler() {
        languages = new ArrayList<>();
        recyclerView = findViewById(R.id.speakrecycler);
        layoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new Frame28Adapter(this, languages, this);
        recyclerView.setAdapter(mAdapter);
        makeArrayList();
    }

    /**
     * To get the list of languages and cities to show
     */
    private void makeArrayList() {
        MyProgressDialog myProgressDialog = new MyProgressDialog();
        myProgressDialog.showDialog(Frame28.this);
        getLanguages()
                .addOnCompleteListener(task -> {
                    myProgressDialog.dismissDialog();
                    if (task.isSuccessful()) {
                        HashMap<String, String> language = (HashMap<String, String>) task.getResult().get("Language");

                        Iterator it = language.entrySet().iterator();
                        while (it.hasNext()) {
                            Map.Entry pair = (Map.Entry) it.next();
                            languages.add(new Lang_FoodPOJO((String) pair.getKey(), false, (String) pair.getValue()));
                            it.remove();
                        }
                        mAdapter.notifyDataSetChanged();
                        ArrayList<String> citiesArrayList = (ArrayList<String>) task.getResult().get("Cities");
                        assert citiesArrayList != null;
                        cities = citiesArrayList.toArray(new String[0]);
                        setSpinner();
                    } else {
                        Intent intent = new Intent(Frame28.this, Reconnect.class);
                        startActivityForResult(intent, 2);
                    }
                });
    }

    /**
     * To call the cloud function to get languages and cities from firestore
     *
     * @return
     */
    private Task<HashMap<String, Object>> getLanguages() {
        Map<String, Object> data = new HashMap<>();
        data.put("req", "Languages_and_Cities");
        return MainActivity.mFunctions
                .getHttpsCallable("getAppData")
                .call(data)
                .continueWith(task -> (HashMap<String, Object>) task.getResult().getData());
    }

    /**
     * To set up listeners for all clickables on screen
     */
    private void setListeners() {
        for (int i = 0; i < 3; i++) {
            binding.type.getChildAt(i).setOnClickListener(this);
            binding.whatlin.getChildAt(i).setOnClickListener(this);
        }
        binding.next28.setOnClickListener(this);
        binding.citycard.setOnClickListener(this);
    }

    /**
     * To set up basic UI of the screen
     */
    private void setUI() {
        //for later use
        //nb = Typeface.createFromAsset(getAssets(), "fonts/nb.otf");
        //nl = Typeface.createFromAsset(getAssets(), "fonts/nl.otf");
        String fullname;
        try {
            fullname = MainActivity.getValue(getApplicationContext(), MainActivity.ALIAS_NAME);
        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
            fullname = "Guest";
        }
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
            User.initUser();
            User.user.setCity(binding.city.getText().toString());
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
            List<String> al = new ArrayList<>();
            for (Lang_FoodPOJO language : languages) {
                if (language.isSelected()) {
                    al.add(language.getLanguage());
                }
            }
            User.user.setLanguages(al);
            Intent intent = new Intent(Frame28.this, Frame19.class);
            if (state == 1) {
                intent.putExtra("state", "1");
                startActivityForResult(intent, 1);
            } else {
                startActivity(intent);
            }
        } else if (v == binding.citycard) {
            binding.spinner.performClick();
        }
    }

    /**
     * To handle clicks on gender of cooks
     *
     * @param i to get the cook gender clicked on
     */
    private void setCookGender(int i) {
        if (cookgender != -1 && cookgender != i) {
            ((CardView) binding.whatlin.getChildAt(cookgender)).setCardBackgroundColor(getColor(R.color.white));
        }
        ((CardView) binding.whatlin.getChildAt(i)).setCardBackgroundColor(getColor(R.color.yellow));
        cookgender = i;
        toggleNext();
    }

    /**
     * To enable/disable next button if all items are selected
     */
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

    /**
     * To handle clicks on types of meals
     *
     * @param i to get the type of meal clicked on
     */
    private void setMealType(int i) {
        if (mealtype != -1 && mealtype != i) {
            ((CardView) binding.type.getChildAt(mealtype)).setCardBackgroundColor(getColor(R.color.white));
        }
        ((CardView) binding.type.getChildAt(i)).setCardBackgroundColor(getColor(R.color.yellow));
        mealtype = i;
        toggleNext();
    }

    /**
     * handle clicks on language section of screen
     *
     * @param index
     */
    @Override
    public void onCustomItemClick(int index) {
        languages.get(index).setSelected(!languages.get(index).isSelected());
        mAdapter.notifyDataSetChanged();
        toggleNext();
    }

    /**
     * To disable next button
     */
    private void disableNext() {
        binding.next28.setCardBackgroundColor(getColor(R.color.disabledbutton));
    }

    /**
     * To enable next button
     */
    private void enableNext() {
        binding.next28.setCardBackgroundColor(getColor(R.color.black));
    }

    @Override
    public void onBackPressed() {
        if (state == 1) {
            super.onBackPressed();
        } else {
            openDialog();
        }
    }

    /**
     * To open dialog to ask if the user wants to quit app or not
     */
    private void openDialog() {
        ExitDailog exitDailog = new ExitDailog();
        exitDailog.show(getSupportFragmentManager(), "Exit dialog");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            Intent returnIntent = new Intent();
            setResult(Activity.RESULT_OK, returnIntent);
            finish();
        }
        if (requestCode == 2) {
            if (resultCode == RESULT_OK) {
                makeArrayList();
            } else {
                this.onBackPressed();
            }
        }
    }

    /*for later use*/

    /*
    public static Location location = null;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private int i = 0;
    private Typeface nb;
    private Typeface nl;
    private int tracker=0;

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