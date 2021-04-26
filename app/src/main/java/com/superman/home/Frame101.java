package com.superman.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.github.islamkhsh.CardSliderViewPager;
import com.google.android.gms.tasks.Task;
import com.google.firebase.functions.FirebaseFunctionsException;
import com.superman.R;
import com.superman.authentication.User;
import com.superman.common.MainActivity;
import com.superman.common.Reconnect;
import com.superman.databinding.ActivityFrame101Binding;
import com.superman.utilities.CustomItemClickListener;
import com.superman.utilities.CustomItemClickListener2;
import com.superman.utilities.CustomItemClickListener3;
import com.superman.utilities.ExtraUtils;
import com.superman.utilities.LogoutDailog;
import com.superman.utilities.MyProgressDialog;
import com.superman.utilities.ScreenUtils;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class Frame101 extends AppCompatActivity implements CustomItemClickListener2, CustomItemClickListener, View.OnClickListener, CustomItemClickListener3 {
    public static ActivityFrame101Binding binding;
    private ArrayList<CardsPOJO> cardsPOJOS;
    private Frame101Adapter adapter;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private SliderLayoutManager sliderLayoutManager;
    private HashMap<String, Object> defaultmenu;
    Timer timer;
    private String day;
    private RecyclerView breakrecycler;
    private RecyclerView.Adapter mAdapter1;
    private RecyclerView.LayoutManager layoutManager1;
    private RecyclerView lunchrecycler;
    private RecyclerView.Adapter mAdapter2;
    private RecyclerView.LayoutManager layoutManager2;
    private RecyclerView dinnerrecycler;
    private ArrayList<String> dates;
    private RecyclerView.Adapter mAdapter3;
    private RecyclerView.LayoutManager layoutManager3;
    private ArrayList<String> dow;
    private MyProgressDialog myProgressDialog;
    private HashMap<String, Object> temp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFrame101Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupMainCard();
        setupWeekRecycler();
        makeHome();
        setListeners();
        ExtraUtils.getProfilePic(getApplicationContext());
    }

    private void makeHome() {
        try {
            myProgressDialog = new MyProgressDialog();
            myProgressDialog.showDialog(this);
            setHome();
        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
            finishAffinity();
        }
    }

    private void setListeners() {
        binding.startnow.setOnClickListener(this);
        binding.ppcard.setOnClickListener(this);
        binding.noclick.setOnClickListener(this);
    }

    private void setupWeekRecycler() {
        recyclerView = findViewById(R.id.dow);
        sliderLayoutManager = new SliderLayoutManager(this, LinearLayoutManager.HORIZONTAL, false, this);
        recyclerView.setLayoutManager(sliderLayoutManager);
        createDates();
        mAdapter = new Frame101DOWAdapter(this, dates);
        recyclerView.setAdapter(mAdapter);

        int padding = ScreenUtils.Companion.getScreenWidth(this);
        binding.dow.setPadding(ScreenUtils.Companion.dpToPx(this, 84), 0, padding - ScreenUtils.Companion.dpToPx(this, 196), 0);
        new LinearSnapHelper().attachToRecyclerView(binding.dow);
    }

    public static String getDayOfWeek(int i) {
        switch (i) {
            case 1:
                return "Sunday";
            case 2:
                return "Monday";
            case 3:
                return "Tuesday";
            case 4:
                return "Wednesday";
            case 5:
                return "Thursday";
            case 6:
                return "Friday";
            default:
                return "Saturday";
        }
    }

    private void setupMainCard() {
        CardSliderViewPager cardSliderViewPager = binding.cards;
        cardsPOJOS = new ArrayList<>();
        adapter = new Frame101Adapter(cardsPOJOS, this);
        cardSliderViewPager.setAdapter(adapter);
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

    private void setHome() throws GeneralSecurityException, IOException {
        defaultmenu = new HashMap<>();

        getHome()
                .addOnCompleteListener(task -> {
                    myProgressDialog.dismissDialog();
                    if (!task.isSuccessful()) {
                        Exception e = task.getException();
                        if (e instanceof FirebaseFunctionsException) {
                            FirebaseFunctionsException ffe = (FirebaseFunctionsException) e;
                            FirebaseFunctionsException.Code code = ffe.getCode();
                        }
                        Intent intent = new Intent(Frame101.this, Reconnect.class);
                        intent.putExtra("refresh", "refresh");
                        startActivityForResult(intent, 3);
                    } else {
                        HashMap<String, Object> result = task.getResult();
                        ArrayList<HashMap<String, Object>> cards = (ArrayList<HashMap<String, Object>>) result.get("cards");
                        User.initUser();
                        User.user.setName((String) result.get("userName"));
                        User.user.setLocation(result.get("city") + ", " + result.get("state"));
                        for (int i = 0; i < cards.size(); i++) {
                            HashMap<String, Object> card = cards.get(i);
                            CardsPOJO cardPOJO = new CardsPOJO((int) card.get("cta"), (String) card.get("color"), (String) card.get("details"), (String) card.get("title"));
                            cardsPOJOS.add(cardPOJO);
                        }
                        try {
                            defaultmenu = (HashMap<String, Object>) result.get("defaultMeals");
                            if (defaultmenu == null) {
                                throw new Exception("Exception");
                            }
                        } catch (Exception e) {
                            defaultmenu = null;
                            binding.noclick.setVisibility(View.VISIBLE);
                        }
                        /*if(defaultMeals!=null || defaultMeals.size()!=0){
                            for(int i=0;i<7;i++) {
                                if (defaultMeals.containsKey(getDayOfWeek(i))) {
                                    HashMap<String, String> mealdetails= (HashMap<String, String>) defaultMeals.get("Monday");
                                    String breakfast=null;
                                    String lunch=null;
                                    String dinner=null;

                                    if (mealdetails.containsKey("Breakfast")) {
                                        breakfast = mealdetails.get("Breakfast");
                                    }
                                    if (mealdetails.containsKey("Lunch")) {
                                        lunch = mealdetails.get("Lunch");
                                    }
                                    if (mealdetails.containsKey("Dinner")) {
                                        dinner = mealdetails.get("Dinner");
                                    }

                                    ArrayList<String> daymenu=new ArrayList<>();
                                    daymenu.add(breakfast);
                                    daymenu.add(lunch);
                                    daymenu.add(dinner);
                                    daymenu.add(time);
                                    defaultmenu.add(i, daymenu);
                                }
                                else{
                                    defaultmenu.add(i, null);
                                }
                            }
                        }*/
                        adapter.notifyDataSetChanged();
                        setUpMealsRecyclers();
                    }
                });
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

    private Task<HashMap<String, Object>> getHome() throws GeneralSecurityException, IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("uid", MainActivity.getValue(getApplicationContext(), MainActivity.ALIAS4));
        return MainActivity.mFunctions
                .getHttpsCallable("getHome")
                .call(data)
                .continueWith(task -> (HashMap<String, Object>) task.getResult().getData());
    }

    @Override
    public void onCustomItemClick(View view) {
        int pos = binding.dow.getChildLayoutPosition(view);
        binding.dow.smoothScrollToPosition(pos);
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
            setTime();
        }
    }

    private void setTime() {
    }

    @Override
    public void onClick(View v) {
        if (v == binding.ppcard) {
            Intent intent = new Intent(Frame101.this, UserProfile.class);
            startActivity(intent);
        } else if (v == binding.startnow) {
            Intent intent = new Intent(Frame101.this, Frame110.class);
            startActivity(intent);
        }
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
                    makeDeepCopy();
                    ((HashMap<String, String>) defaultmenu.get(day)).put(whichQuantity, join(quantsarray));
                    sendRequest();
                    (i == 0 ? mAdapter1 : i == 1 ? mAdapter2 : mAdapter3).notifyDataSetChanged();
                }
            } else {
                if (quantity != 1) {
                    quantity--;
                    quantsarray[index] = String.valueOf(quantity);
                    makeDeepCopy();
                    ((HashMap<String, String>) defaultmenu.get(day)).put(whichQuantity, join(quantsarray));
                    sendRequest();
                } else {
                    String meals = ((HashMap<String, String>) defaultmenu.get(day)).get(whichmeal);
                    String[] mealarrays = meals.split(",");
                    mealarrays = remove(mealarrays, index);
                    makeDeepCopy();
                    ((HashMap<String, String>) defaultmenu.get(day)).put(whichmeal, join(mealarrays));
                    ((HashMap<String, String>) defaultmenu.get(day)).put(whichQuantity, getQuantity(quantsarray, index));
                    sendRequest();
                }
                (i == 0 ? mAdapter1 : i == 1 ? mAdapter2 : mAdapter3).notifyDataSetChanged();
            }
        } else {
            add(i);
        }
    }

    private void sendRequest() {
        if (timer != null) {
            timer.cancel();
            timer.purge();
            timer = null;
        }
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    setDefaultMeals()
                            .addOnCompleteListener(task -> {
                                Log.e("hih", "jij");
                                if (!task.isSuccessful()) {
                                    defaultmenu = DeepClone.deepClone(temp);
                                    ((MealAdapter) mAdapter1).dataChanger(defaultmenu);
                                    ((MealAdapter) mAdapter2).dataChanger(defaultmenu);
                                    ((MealAdapter) mAdapter3).dataChanger(defaultmenu);
                                }
                                temp = null;
                            });
                } catch (GeneralSecurityException | IOException e) {
                    e.printStackTrace();
                }
            }
        }, 3000);
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

    private void makeDeepCopy() {
        if (temp == null) {
            temp = new HashMap<>();
            temp = DeepClone.deepClone(defaultmenu);
        }
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

    private void add(int i) {
        Intent intent = new Intent(Frame101.this, Frame116.class);
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
                    makeDeepCopy();
                    ((HashMap<String, String>) defaultmenu.get(day))
                            .put(mealtime, tc + comma + mealtoadd);

                    ((HashMap<String, String>) defaultmenu.get(day))
                            .put(mealtime + "Quantity", ((HashMap<String, String>) defaultmenu.get(day))
                                    .get(mealtime + "Quantity") + comma + "1");

                    sendRequest();

                    (requestCode == 0 ? mAdapter1 : requestCode == 1 ? mAdapter2 : mAdapter3).notifyDataSetChanged();
                }
            }
        }
        if (requestCode == 3) {
            makeHome();
        }
    }

    @Override
    public void onBackPressed() {
        LogoutDailog logoutDialog = new LogoutDailog();
        logoutDialog.show(getSupportFragmentManager(), "Logout dialog");
    }
}