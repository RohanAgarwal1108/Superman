package com.SuperCook.home;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.SuperCook.R;
import com.SuperCook.UserPreference.Lang_FoodPOJO;
import com.SuperCook.common.MainActivity;
import com.SuperCook.common.Reconnect;
import com.SuperCook.cookSelection.SelectedDishes;
import com.SuperCook.databinding.ActivityTailoredMealBinding;
import com.SuperCook.utilities.CustomItemClickListener;
import com.SuperCook.utilities.CustomItemClickListener2;
import com.SuperCook.utilities.CustomItemClickListener3;
import com.SuperCook.utilities.ExtraUtils;
import com.SuperCook.utilities.MyProgressDialog;
import com.SuperCook.utilities.ScreenUtils;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.SuperCook.home.Frame101.getDayOfWeek;

public class TailoredMeal extends AppCompatActivity implements CustomItemClickListener2, CustomItemClickListener, View.OnClickListener, CustomItemClickListener3 {
    public static HashMap<String, ArrayList<Lang_FoodPOJO>> breakfastdishes;
    public static HashMap<String, ArrayList<Lang_FoodPOJO>> lunchdishes;
    public static HashMap<String, ArrayList<Lang_FoodPOJO>> dinnerdishes;
    public static String[] keys;
    private static String[] dishes;
    private ArrayList<String> dates;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private SliderLayoutManager sliderLayoutManager;
    private ActivityTailoredMealBinding binding;
    private ArrayList<String> dow;
    private String day;
    private RecyclerView breakrecycler;
    private RecyclerView.Adapter mAdapter1;
    private RecyclerView.LayoutManager layoutManager1;
    private RecyclerView lunchrecycler;
    private RecyclerView.Adapter mAdapter2;
    private RecyclerView.LayoutManager layoutManager2;
    private RecyclerView dinnerrecycler;
    private RecyclerView.Adapter mAdapter3;
    private RecyclerView.LayoutManager layoutManager3;
    private HashMap<String, Object> defaultmenu;
    private MyProgressDialog myProgressDialog;

    public static void makeAllDishes(int i) {
        if (i == 0) {
            breakfastdishes = new HashMap<>();
        } else if (i == 1) {
            lunchdishes = new HashMap<>();
        } else {
            dinnerdishes = new HashMap<>();
        }

        dishes = new String[]{"Aloo Gobi", "Bhindi", "Aloo Paratha", "Rajma Chawal", "Malai Kofta", "Dum Aloo", "Chole", "Dal",
                "Dal Makhani", "Pav Bhaji", "Kadhai Panner", "Paneer Tikka", "Fried Chicken", "Chole Bhature"};
        addEntries("North Indian", i);

        dishes = new String[]{"Idli", "Dosa", "Sambar Rice", "Vada Sambar", "Dahi Vada", "Biryani", "Upma", "Uttapam"};
        addEntries("South Indian", i);

        dishes = new String[]{"Khandvi", "Dhokla", "Thepla", "Dal Dhokli", "Handvo", "Shrikhand"};
        addEntries("North Eastern", i);

        dishes = new String[]{"Vada pav", "Misal Pav", "Pooran Poli", "Pav Bhaji", "Sabudana Khichdi", "Poha", "Basundi", "Poori Bhaji", "Veg Kolhapuri"};
        addEntries("Maharashtrian", i);

        dishes = new String[]{"Chow mein", "Fried Rice", "Chilli Potato", "Chilli Paneer"};
        addEntries("Chinese", i);

        dishes = new String[]{"Pasta", "Pizza"};
        addEntries("Italian", i);

        dishes = new String[]{"Burger", "Sandwich", "French Loaf", "Pancake"};
        addEntries("American", i);

        keys = TailoredMeal.breakfastdishes.keySet().toArray(new String[0]);
    }

    public static void addEntries(String cuisine, int i) {
        ArrayList<Lang_FoodPOJO> arrayList = new ArrayList<>();
        for (String dish : dishes) {
            arrayList.add(new Lang_FoodPOJO(dish, false));
        }
        (i == 0 ? breakfastdishes : i == 1 ? lunchdishes : dinnerdishes).put(cuisine, arrayList);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTailoredMealBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setupListeners();
        setupWeekRecycler();
        makeMeals();
    }

    private void setupListeners() {
        binding.backtailored.setOnClickListener(this);
        binding.confirm.setOnClickListener(this);
        binding.confirmation.setOnClickListener(this);
    }

    private void makeMeals() {
        defaultmenu = new HashMap<>();
        int breakcounter = 0;
        int lunchcounter = 0;
        int dinnercounter = 0;
        List<SelectedDishes>[] mealSelectedDishes = SelectedDishes.mealSelectedDishes;
        for (int i = 1; i <= 7; i++) {
            HashMap<String, String> daymeal = new HashMap<>();
            if (breakcounter == mealSelectedDishes[0].size()) {
                breakcounter = 0;
            }
            daymeal.put("Breakfast", mealSelectedDishes[0].get(breakcounter).getName());
            daymeal.put("BreakfastQuantity", "1");

            if (lunchcounter == mealSelectedDishes[1].size()) {
                lunchcounter = 0;
            }
            daymeal.put("Lunch", mealSelectedDishes[1].get(lunchcounter).getName());
            daymeal.put("LunchQuantity", "1");
            if (dinnercounter == mealSelectedDishes[2].size()) {
                dinnercounter = 0;
            }
            daymeal.put("Dinner", mealSelectedDishes[2].get(dinnercounter).getName());
            daymeal.put("DinnerQuantity", "1");
            breakcounter++;
            lunchcounter++;
            dinnercounter++;
            daymeal.put("time", "8:00, 12:00, 19:00");
            defaultmenu.put(Frame101.getDayOfWeek(i), daymeal);
        }
        setUpMealsRecyclers();
    }

    private void setupWeekRecycler() {
        recyclerView = findViewById(R.id.dow);
        sliderLayoutManager = new SliderLayoutManager(this, LinearLayoutManager.HORIZONTAL, false, this);
        recyclerView.setLayoutManager(sliderLayoutManager);
        createDates();
        mAdapter = new Frame101DOWAdapter(this, dates, "i");
        recyclerView.setAdapter(mAdapter);

        int padding = ScreenUtils.Companion.getScreenWidth(this);
        binding.dow.setPadding(ScreenUtils.Companion.dpToPx(this, 84), 0, padding - ScreenUtils.Companion.dpToPx(this, 196), 0);
        new LinearSnapHelper().attachToRecyclerView(binding.dow);
    }

    private void createDates() {
        dates = new ArrayList<>();
        dow = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        day = getDayOfWeek(calendar.get(Calendar.DAY_OF_WEEK));
        for (int i = 0; i < 7; i++) {
            Date date = calendar.getTime();
            String day = new SimpleDateFormat("dd").format(date);
            String month = new SimpleDateFormat("E").format(date);
            int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
            dates.add(day + month);
            dow.add(getDayOfWeek(dayOfWeek));
            calendar.add(Calendar.DATE, 1);
        }
    }

    @Override
    public void onClick(View v) {
        if (v == binding.confirm) {
            myProgressDialog = new MyProgressDialog();
            myProgressDialog.showDialog(this);
            try {
                setDefaultMeals()
                        .addOnCompleteListener(task -> {
                            myProgressDialog.dismissDialog();
                            if (!task.isSuccessful()) {
                                Intent intent = new Intent(TailoredMeal.this, Reconnect.class);
                                startActivity(intent);
                            } else {
                                binding.confirmation.setVisibility(View.VISIBLE);
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        Intent intent = new Intent(TailoredMeal.this, Frame101.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                    }
                                }, 3000);
                            }
                        });
            } catch (GeneralSecurityException | IOException e) {
                e.printStackTrace();
                myProgressDialog.dismissDialog();
                ExtraUtils.makeToast(TailoredMeal.this, "An Error occurred! Please try later.");
            }
        } else if (v == binding.backtailored) {
            this.onBackPressed();
        }
    }

    private void add(int i) {
        Intent intent = new Intent(TailoredMeal.this, Frame116.class);
        startActivityForResult(intent, i);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 0 || requestCode == 1 || requestCode == 2) {
                String mealtime = requestCode == 0 ? "Breakfast" : requestCode == 1 ? "Lunch" : "Dinner";

                String tc = ((HashMap<String, String>) defaultmenu.get(day)).get(mealtime);

                String mealtoadd = data.getStringExtra("result");

                if (!tc.contains(mealtoadd)) {
                    String comma;
                    if (tc.isEmpty()) {
                        comma = "";
                    } else {
                        comma = ",";
                    }
                    ((HashMap<String, String>) defaultmenu.get(day))
                            .put(mealtime, tc + comma + mealtoadd);

                    ((HashMap<String, String>) defaultmenu.get(day))
                            .put(mealtime + "Quantity", ((HashMap<String, String>) defaultmenu.get(day))
                                    .get(mealtime + "Quantity") + comma + "1");

                    (requestCode == 0 ? mAdapter1 : requestCode == 1 ? mAdapter2 : mAdapter3).notifyDataSetChanged();
                }
            }
        }
    }

    private Task<HashMap<String, Object>> setDefaultMeals() throws GeneralSecurityException, IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("uid", MainActivity.getValue(getApplicationContext(), MainActivity.ALIAS_UID));
        for (int i = 1; i <= 7; i++) {
            String day = Frame101.getDayOfWeek(i);
            data.put(day, defaultmenu.get(day));
        }
        return MainActivity.mFunctions
                .getHttpsCallable("setDefaultMeals")
                .call(data)
                .continueWith(task -> (HashMap<String, Object>) task.getResult().getData());
    }

    private void setUpMealsRecyclers() {
        breakrecycler = binding.breakrecycler;
        layoutManager1 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        breakrecycler.setLayoutManager(layoutManager1);
        mAdapter1 = new MealAdapter(0, this, defaultmenu, day, this);
        breakrecycler.setAdapter(mAdapter1);

        lunchrecycler = binding.lunchrecycler;
        layoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        lunchrecycler.setLayoutManager(layoutManager2);
        mAdapter2 = new MealAdapter(1, this, defaultmenu, day, this);
        lunchrecycler.setAdapter(mAdapter2);

        dinnerrecycler = binding.dinnerrecycler;
        layoutManager3 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        dinnerrecycler.setLayoutManager(layoutManager3);
        mAdapter3 = new MealAdapter(2, this, defaultmenu, day, this);
        dinnerrecycler.setAdapter(mAdapter3);

        mAdapter1.notifyDataSetChanged();
        mAdapter2.notifyDataSetChanged();
        mAdapter3.notifyDataSetChanged();
    }

    @Override
    public void onCustomItemClick(int index) {
        if (!day.equals(dow.get(index))) {
            day = dow.get(index);
            ((MealAdapter) mAdapter1).dayChanger(day);
            ((MealAdapter) mAdapter2).dayChanger(day);
            ((MealAdapter) mAdapter3).dayChanger(day);
            mAdapter1.notifyDataSetChanged();
            mAdapter2.notifyDataSetChanged();
            mAdapter3.notifyDataSetChanged();
        }
    }

    @Override
    public void onCustomItemClick(View view) {
        int pos = binding.dow.getChildLayoutPosition(view);
        binding.dow.smoothScrollToPosition(pos);
    }

    @Override
    public void onCustomItemClick(int index, int i, boolean add) {
        if (index != -1) {
            String whichmeal = i == 0 ? "Breakfast" : i == 1 ? "Lunch" : "Dinner";
            String whichQuantity = whichmeal + "Quantity";
            String quants = ((HashMap<String, String>) defaultmenu.get(day)).get(whichQuantity);
            String[] quantsarray = quants.split(",");
            int quantity = Integer.parseInt(quantsarray[index].trim());
            if (add) {
                if (quantity < 5) {
                    quantity++;
                    quantsarray[index] = String.valueOf(quantity);
                    ((HashMap<String, String>) defaultmenu.get(day)).put(whichQuantity, join(quantsarray));
                    (i == 0 ? mAdapter1 : i == 1 ? mAdapter2 : mAdapter3).notifyDataSetChanged();
                }
            } else {
                if (quantity != 1) {
                    quantity--;
                    quantsarray[index] = String.valueOf(quantity);
                    ((HashMap<String, String>) defaultmenu.get(day)).put(whichQuantity, join(quantsarray));
                } else {
                    String meals = ((HashMap<String, String>) defaultmenu.get(day)).get(whichmeal);
                    String[] mealarrays = meals.split(",");
                    mealarrays = remove(mealarrays, index);
                    ((HashMap<String, String>) defaultmenu.get(day)).put(whichmeal, join(mealarrays));
                    ((HashMap<String, String>) defaultmenu.get(day)).put(whichQuantity, getQuantity(quantsarray, index));
                }
                (i == 0 ? mAdapter1 : i == 1 ? mAdapter2 : mAdapter3).notifyDataSetChanged();
            }
        } else {
            add(i);
        }
    }

    private String[] remove(String[] mealarrays, int index) {
        String[] str = new String[mealarrays.length - 1];
        int counter = 0;
        for (int i = 0; i < mealarrays.length; i++) {
            if (i == index) {
                continue;
            }
            str[counter] = mealarrays[i];
            counter++;
        }
        return str;
    }

    private String join(String[] array) {
        String str = "";
        for (int i = 0; i < array.length; i++) {
            str += array[i] + ",";
        }
        if (str.isEmpty()) {
            return "";
        }
        return str.substring(0, str.length() - 1);
    }

    private String getQuantity(String[] quantsarray, int index) {
        String str = "";
        for (int i = 0; i < quantsarray.length; i++) {
            if (i == index) {
                continue;
            }
            str += quantsarray[i] + ",";
        }
        if (str.isEmpty()) {
            return "";
        }
        return str.substring(0, str.length() - 1);
    }
}