package com.superman.home;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.Task;
import com.superman.R;
import com.superman.UserPreference.Lang_FoodPOJO;
import com.superman.common.MainActivity;
import com.superman.common.Reconnect;
import com.superman.cookSelection.SelectedDishes;
import com.superman.databinding.ActivityTailoredMealBinding;
import com.superman.utilities.CustomItemClickListener;
import com.superman.utilities.CustomItemClickListener2;
import com.superman.utilities.CustomItemClickListener3;
import com.superman.utilities.ExtraUtils;
import com.superman.utilities.MyProgressDialog;
import com.superman.utilities.ScreenUtils;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.superman.home.Frame101.getDayOfWeek;

public class TailoredMeal extends AppCompatActivity implements CustomItemClickListener2, CustomItemClickListener, View.OnClickListener, CustomItemClickListener3 {
    public static HashMap<String, ArrayList<Lang_FoodPOJO>> breakfastdishes;
    public static HashMap<String, ArrayList<Lang_FoodPOJO>> lunchdishes;
    public static HashMap<String, ArrayList<Lang_FoodPOJO>> dinnerdishes;
    public static String[] keys;
    private static String[] dishes;
    Toast toast;
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
        dishes = new String[]{"Aalo Gobi", "Bhindi Masala", "Aalo Paratha", "Rajma Masal Curry", "Maila Kofta", "Dum Aalo", "Chole", "Daal",
                "Daal Makhani", "Pav Bhaji", "Kadhai Panner", "Matar Paneer", "Fried Chicken", "Chole Bhatoore"};
        addEntries("North Indian", i);

        dishes = new String[]{"Idli Sambhar", "Dosa Sambhar", "Sambhar Rice", "Sambhar Vada", "Dahi Vada", "Veg Kurma", "Biryani", "Upma", "Uthapam"};
        addEntries("South Indian", i);

        dishes = new String[]{"Khandvi", "Dhokla", "Thepla", "Dal Dhokli", "Handvi", "Fafda", "Shrikhand"};
        addEntries("North Eastern", i);

        dishes = new String[]{"Vada pav", "Misal Pav", "Puran Poli", "Pav Bhaji", "Sabudana Khichdi", "Poha", "Basundi", "Bharli Bhindi", "Poori Bhaji", "Veg Kolhapuri"};
        addEntries("Maharashtrian", i);

        dishes = new String[]{"Chow Mein", "Fried Rice", "Chilli potato", "Chilli Paneer"};
        addEntries("Chinese", i);

        dishes = new String[]{"Pasta", "Pizza"};
        addEntries("Italian", i);

        dishes = new String[]{"Burger", "Sandwich", "French Toast", "Pancakes"};
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
        binding.add1.setOnClickListener(this);
        binding.add2.setOnClickListener(this);
        binding.add3.setOnClickListener(this);
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
        } else if (v == binding.add1) {
            if (binding.add1.getAlpha() == 1) {
                add(0);
            }
        } else if (v == binding.add2) {
            if (binding.add2.getAlpha() == 1) {
                add(1);
            }
        } else if (v == binding.add3) {
            if (binding.add3.getAlpha() == 1) {
                add(2);
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
                    ((HashMap<String, String>) defaultmenu.get(day))
                            .put(mealtime, tc + "," + mealtoadd);

                    ((HashMap<String, String>) defaultmenu.get(day))
                            .put(mealtime + "Quantity", ((HashMap<String, String>) defaultmenu.get(day))
                                    .get(mealtime + "Quantity") + ",1");

                    (requestCode == 0 ? mAdapter1 : requestCode == 1 ? mAdapter2 : mAdapter3).notifyDataSetChanged();

                    toggleAdd(requestCode, mealtime);
                }
            }
        }
    }

    private void toggleAdd(int requestCode, String mealtime) {
        String[] number = ((HashMap<String, String>) defaultmenu.get(day)).get(mealtime + "Quantity").split(",");
        if (requestCode < 0) {
            if (number.length < 5) {
                (requestCode == -1 ? binding.add1 : requestCode == -2 ? binding.add2 : binding.add3).setAlpha(1);
            }
            return;
        }
        if (number.length == 5) {
            (requestCode == 0 ? binding.add1 : requestCode == 1 ? binding.add2 : binding.add3).setAlpha(0.5f);
        }
    }

    private Task<HashMap<String, Object>> setDefaultMeals() throws GeneralSecurityException, IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("uid", MainActivity.getValue(getApplicationContext(), MainActivity.ALIAS4));
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
        breakrecycler.setHasFixedSize(true);
        layoutManager1 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        breakrecycler.setLayoutManager(layoutManager1);
        mAdapter1 = new MealAdapter(0, this, defaultmenu, day, this);
        breakrecycler.setAdapter(mAdapter1);

        lunchrecycler = binding.lunchrecycler;
        lunchrecycler.setHasFixedSize(true);
        layoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        lunchrecycler.setLayoutManager(layoutManager2);
        mAdapter2 = new MealAdapter(1, this, defaultmenu, day, this);
        lunchrecycler.setAdapter(mAdapter2);

        dinnerrecycler = binding.dinnerrecycler;
        dinnerrecycler.setHasFixedSize(true);
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
                (i == 0 ? mAdapter1 : i == 1 ? mAdapter2 : mAdapter3).notifyDataSetChanged();
            } else {
                String meals = ((HashMap<String, String>) defaultmenu.get(day)).get(whichmeal);
                String[] mealarrays = meals.split(",");
                if (mealarrays.length == 1) {
                    makeToast("At least one dish is required!");
                } else {
                    mealarrays = remove(mealarrays, index);
                    ((HashMap<String, String>) defaultmenu.get(day)).put(whichmeal, join(mealarrays));
                    ((HashMap<String, String>) defaultmenu.get(day)).put(whichQuantity, getQuantity(quantsarray, index));
                    (i == 0 ? mAdapter1 : i == 1 ? mAdapter2 : mAdapter3).notifyDataSetChanged();
                }
            }
            toggleAdd((i * -1) - 1, whichmeal);
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
        return str.substring(0, str.length() - 1);
    }

    private void makeToast(String str) {
        if (toast != null) {
            toast.cancel();
        }
        toast = Toast.makeText(TailoredMeal.this, str, Toast.LENGTH_SHORT);
        toast.show();
    }
}