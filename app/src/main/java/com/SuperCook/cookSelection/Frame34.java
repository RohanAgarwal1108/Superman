package com.SuperCook.cookSelection;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.SuperCook.R;
import com.SuperCook.UserPreference.Lang_FoodPOJO;
import com.SuperCook.common.MainActivity;
import com.SuperCook.common.Reconnect;
import com.SuperCook.databinding.ActivityFrame34Binding;
import com.SuperCook.utilities.CustomItemClickListener;
import com.SuperCook.utilities.DateFormatter;
import com.SuperCook.utilities.ExtraUtils;
import com.SuperCook.utilities.KeyboardUtil;
import com.SuperCook.utilities.MyProgressDialog;
import com.google.android.gms.tasks.Task;
import com.segment.analytics.Analytics;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Frame34 extends AppCompatActivity implements View.OnClickListener, CustomItemClickListener, TextWatcher {
    public static HashMap<String, ArrayList<Lang_FoodPOJO>> alldishes;
    private ActivityFrame34Binding binding;
    private Bundle extras;
    private int index;
    private String[] dishes;
    private final ArrayList<String> finalizedItems = new ArrayList<>();
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private String[] keys;
    private String timeforslot;
    private MyProgressDialog myProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFrame34Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        extras = getIntent().getExtras();
        index = extras.getInt("index");

        setListeners();
        makeAllDishes();

        setupRecycler();
        SelectedDishes.initSelectedDishes();

        setTimeforUser(getTimeplus2());

        setAnalytics();
    }

    private void setAnalytics() {
        Analytics.with(Frame34.this).screen("Trial Booking", "Trial Details", null, null);
    }

    private String getTimeplus2() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR_OF_DAY, 2);
        String res = calendar.getTime().toString();
        timeforslot = res;
        return res;
    }

    private void setTimeforUser(String datetime) {
        binding.date.setText(DateFormatter.formateDateFromstring("EEE MMM dd HH:mm:ss z yyyy", "EEE, dd MMM yyyy", datetime));
        binding.time.setText(DateFormatter.formateDateFromstring("EEE MMM dd HH:mm:ss z yyyy", "hh:mm", datetime));
        binding.ampm.setText(DateFormatter.formateDateFromstring("EEE MMM dd HH:mm:ss z yyyy", "a", datetime).toUpperCase());
    }

    private void setupRecycler() {
        recyclerView = binding.finalizedrecycler;
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new Frame34Adapter(finalizedItems, this, this);
        recyclerView.setAdapter(mAdapter);
    }

    private void makeAllDishes() {
        alldishes = new HashMap<>();
        dishes = new String[]{"Aloo Gobi", "Bhindi", "Aloo Paratha", "Rajma Chawal", "Malai Kofta", "Dum Aloo", "Chole", "Dal",
                "Dal Makhani", "Pav Bhaji", "Kadhai Panner", "Paneer Tikka", "Fried Chicken", "Chole Bhature"};
        addEntries("North Indian");

        dishes = new String[]{"Idli", "Dosa", "Sambar Rice", "Vada Sambar", "Dahi Vada", "Biryani", "Upma", "Uttapam"};
        addEntries("South Indian");

        dishes = new String[]{"Khandvi", "Dhokla", "Thepla", "Dal Dhokli", "Handvo", "Shrikhand"};
        addEntries("North Eastern");

        dishes = new String[]{"Vada pav", "Misal Pav", "Pooran Poli", "Pav Bhaji", "Sabudana Khichdi", "Poha", "Basundi", "Poori Bhaji", "Veg Kolhapuri"};
        addEntries("Maharashtrian");

        dishes = new String[]{"Chow mein", "Fried Rice", "Chilli Potato", "Chilli Paneer"};
        addEntries("Chinese");

        dishes = new String[]{"Pasta", "Pizza"};
        addEntries("Italian");

        dishes = new String[]{"Burger", "Sandwich", "French Loaf", "Pancake"};
        addEntries("American");

        keys = Frame34.alldishes.keySet().toArray(new String[0]);
    }

    private void addEntries(String cuisine) {
        ArrayList<Lang_FoodPOJO> arrayList = new ArrayList<>();
        for (String dish : dishes) {
            arrayList.add(new Lang_FoodPOJO(dish, false));
        }
        alldishes.put(cuisine, arrayList);
    }

    private void setListeners() {
        binding.back34.setOnClickListener(this);
        binding.choose.setOnClickListener(this);
        binding.timedetails.setOnClickListener(this);
        binding.book34.setOnClickListener(this);
        binding.address.addTextChangedListener(this);
        binding.notescard.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == binding.back34) {
            this.onBackPressed();
        } else if (v == binding.choose) {
            Intent intent = new Intent(Frame34.this, Frame96.class);
            startActivity(intent);
        } else if (v == binding.timedetails) {
            Intent intent = new Intent(Frame34.this, Frame106.class);
            startActivityForResult(intent, 1);
        } else if (v == binding.book34) {
            if (binding.book34.getCardBackgroundColor() == getColorStateList(R.color.black)) {
                try {
                    bookTrial();
                } catch (GeneralSecurityException | IOException e) {
                    e.printStackTrace();
                    ExtraUtils.makeToast(Frame34.this, "An Error occurred! Please try again.");
                    myProgressDialog.dismissDialog();
                }
            }
        } else if (binding.notescard == v) {
            binding.notes.performClick();
        }
    }

    private void bookTrial() throws GeneralSecurityException, IOException {
        myProgressDialog = new MyProgressDialog();
        myProgressDialog.showDialog(this);
        scheduleTrial()
                .addOnCompleteListener(task -> {
                    myProgressDialog.dismissDialog();
                    if (!task.isSuccessful()) {
                        if (task.getException().toString().equals("Slot already booked")) {
                            ExtraUtils.makeToast(Frame34.this, "Slot already booked! Please re-schedule you trial.");
                        } else {
                            Intent intent = new Intent(Frame34.this, Reconnect.class);
                            startActivity(intent);
                        }
                    } else {
                        try {
                            HashMap<String, Object> data = task.getResult();
                            String res = (String) data.get("res");
                            if (res.equalsIgnoreCase("Scheduled")) {
                                String trailID = (String) data.get("trailID");
                                String details = (String) data.get("details");
                                String superCode = (String) data.get("superCode");
                                Intent intent = new Intent(Frame34.this, Frame35.class);
                                intent.putExtra("trailID", trailID);
                                intent.putExtra("superCode", superCode);
                                intent.putExtra("address", binding.address.getText().toString());
                                intent.putExtra("time", timeforslot);
                                intent.putExtra("index", index);
                                intent.putExtra("details", details);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private Task<HashMap<String, Object>> scheduleTrial() throws GeneralSecurityException, IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("cookID", frame21.cookDetails.get(index).getCookID());
        data.put("slot", getSlot());
        data.put("uid", MainActivity.getValue(getApplicationContext(), MainActivity.ALIAS4));
        data.put("address", binding.address.getText().toString());
        data.put("notes", binding.notes.getText().toString());
        data.put("meal", getMeal());
        data.put("quantity", getQuantitiy());
        data.put("status", "ongoing");
        return MainActivity.mFunctions
                .getHttpsCallable("scheduleTrail")
                .call(data)
                .continueWith(task -> (HashMap<String, Object>) task.getResult().getData());
    }

    private String getQuantitiy() {
        String res = "";
        if (SelectedDishes.selectedDishes != null) {
            for (SelectedDishes selectedDish : SelectedDishes.selectedDishes) {
                res += selectedDish.getQuantity() + ",";
            }
        }
        return res.substring(0, res.length() - 1 == -1 ? 0 : res.length() - 1);
    }

    private String getMeal() {
        String res = "";
        if (SelectedDishes.selectedDishes != null) {
            for (SelectedDishes selectedDish : SelectedDishes.selectedDishes) {
                res += selectedDish.getName() + ",";
            }
        }
        return res.substring(0, res.length() - 1 == -1 ? 0 : res.length() - 1);
    }

    private String getSlot() {
        String slotend = add1hr();
        String res = DateFormatter.formateDateFromstring("EEE MMM dd HH:mm:ss z yyyy", "dd/MM/yyyy/HH:mm", timeforslot);
        res += "-" + DateFormatter.formateDateFromstring("EEE MMM dd HH:mm:ss z yyyy", "HH:mm", slotend);
        return res;
    }

    private String add1hr() {
        Calendar calendar = DateFormatter.getCal(timeforslot);
        calendar.add(Calendar.HOUR_OF_DAY, 1);
        return calendar.getTime().toString();
    }

    @Override
    protected void onResume() {
        super.onResume();
        finalizedItems.clear();
        for (String key : keys) {
            ArrayList<Lang_FoodPOJO> dishes_in_cuisine = alldishes.get(key);
            for (Lang_FoodPOJO dish_in_cuisine : dishes_in_cuisine) {
                if (dish_in_cuisine.isSelected()) {
                    finalizedItems.add(dish_in_cuisine.getLanguage());
                }
            }
        }
        if (finalizedItems.size() == 0) {
            hideItems();
        } else {
            showItems();
        }
    }

    private void hideItems() {
        mAdapter.notifyDataSetChanged();
        binding.nofinalized.setVisibility(View.VISIBLE);
        binding.finalizedrecycler.setVisibility(View.GONE);
    }

    private void showItems() {
        mAdapter.notifyDataSetChanged();
        binding.nofinalized.setVisibility(View.GONE);
        binding.finalizedrecycler.setVisibility(View.VISIBLE);
    }

    @Override
    public void onCustomItemClick(int index) {
        Intent intent = new Intent(Frame34.this, Frame96.class);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            String str = data.getStringExtra("newdate");
            setTimeforUser(str);
            timeforslot = str;
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
        if (s.toString().isEmpty()) {
            disableBook();
        } else {
            enableBook();
        }
    }

    private void enableBook() {
        binding.book34.setCardBackgroundColor(getColor(R.color.black));
    }

    private void disableBook() {
        binding.book34.setCardBackgroundColor(getColor(R.color.disabledbutton));
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        KeyboardUtil keyboardUtil = new KeyboardUtil(this, ev);
        keyboardUtil.touchEvent();
        return super.dispatchTouchEvent(ev);
    }
}