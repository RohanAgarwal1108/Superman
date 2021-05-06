package com.SuperCook.home;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.SuperCook.R;
import com.SuperCook.authentication.User;
import com.SuperCook.common.MainActivity;
import com.SuperCook.common.Reconnect;
import com.SuperCook.common.Webview;
import com.SuperCook.cookSelection.frame21;
import com.SuperCook.databinding.ActivityFrame101Binding;
import com.SuperCook.utilities.CustomItemClickListener;
import com.SuperCook.utilities.CustomItemClickListener2;
import com.SuperCook.utilities.CustomItemClickListener3;
import com.SuperCook.utilities.CustomItemClickListener4;
import com.SuperCook.utilities.ExtraUtils;
import com.SuperCook.utilities.LogoutDailog;
import com.SuperCook.utilities.MyProgressDialog;
import com.SuperCook.utilities.ScreenUtils;
import com.github.islamkhsh.CardSliderViewPager;
import com.google.android.gms.tasks.Task;
import com.google.firebase.functions.FirebaseFunctionsException;
import com.segment.analytics.Analytics;

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

public class Frame101 extends AppCompatActivity implements CustomItemClickListener2, CustomItemClickListener, View.OnClickListener, CustomItemClickListener3, CustomItemClickListener4 {
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

        setAnalytics();
    }

    private void setAnalytics() {
        Analytics.with(Frame101.this).screen("Home", "Home", null, null);
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
        binding.hey.setOnClickListener(this);
        binding.noclick.setOnClickListener(this);
        binding.breaktime.setOnClickListener(this);
        binding.lunchtime.setOnClickListener(this);
        binding.dinnertime.setOnClickListener(this);
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
        adapter = new Frame101Adapter(cardsPOJOS, this, this);
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
                        HashMap<String, Object> cards1 = (HashMap<String, Object>) result.get("cards");
                        ArrayList<HashMap<String, Object>> cards = (ArrayList<HashMap<String, Object>>) cards1.get("cardsArray");
                        User.initUser();
                        User.user.setName((String) result.get("userName"));
                        User.user.setCooks((ArrayList<String>) result.get("cooks"));
                        int k = User.user.getName().indexOf(" ") == -1 ? User.user.getName().length() - 1 : User.user.getName().indexOf(" ");
                        binding.hey.setText("Hey, " + User.user.getName().substring(0, k) + " \uD83C\uDF7F");
                        User.user.setLocation(result.get("city") + ", " + result.get("state"));
                        for (int i = 0; i < cards.size(); i++) {
                            HashMap<String, Object> card = cards.get(i);
                            CardsPOJO cardPOJO = new CardsPOJO(Integer.parseInt((String) card.get("cta")), (String) card.get("color"), (String) card.get("details"), (String) card.get("title"), (String) card.get("icon"), (String) card.get("url"));
                            cardsPOJOS.add(cardPOJO);
                        }
                        try {
                            defaultmenu = (HashMap<String, Object>) result.get("defaultMeals");
                            setTime();
                            if (defaultmenu == null) {
                                throw new Exception("Exception");
                            }
                        } catch (Exception e) {
                            defaultmenu = null;
                            binding.noclick.setVisibility(View.VISIBLE);
                        }
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
        String str = ((HashMap<String, String>) defaultmenu.get(day)).get("time");
        String[] timearr = str.split(",");
        binding.breaktime.setText(timearr[0]);
        binding.lunchtime.setText(timearr[1]);
        binding.dinnertime.setText(timearr[2]);
    }

    @Override
    public void onClick(View v) {
        if (v == binding.ppcard || v == binding.hey) {
            Intent intent = new Intent(Frame101.this, UserProfile.class);
            startActivity(intent);
        } else if (v == binding.startnow) {
            Intent intent = new Intent(Frame101.this, Frame110.class);
            startActivity(intent);
        } else if (v == binding.breaktime) {
            TimePickerDialog timePickerDialog = new TimePickerDialog(this, R.style.TimePickerTheme,
                    (view, hourOfDay, minute) -> {
                        String hr = String.valueOf(hourOfDay);
                        String min = String.valueOf(minute);
                        makeDeepCopy();
                        binding.breaktime.setText(hr + ":" + min);
                        String time = hr + ":" + min + "," + binding.lunchtime.getText().toString() + "," + binding.dinnertime.getText().toString();
                        ((HashMap<String, String>) defaultmenu.get(day)).put("time", time);
                        sendRequest();
                    }, 12, 0, true);
            timePickerDialog.show();
        } else if (v == binding.lunchtime) {
            TimePickerDialog timePickerDialog = new TimePickerDialog(this, R.style.TimePickerTheme,
                    (view, hourOfDay, minute) -> {
                        String hr = String.valueOf(hourOfDay);
                        String min = String.valueOf(minute);
                        makeDeepCopy();
                        binding.lunchtime.setText(hr + ":" + min);
                        String time = binding.breaktime.getText().toString() + "," + hr + ":" + min + "," + binding.dinnertime.getText().toString();
                        ((HashMap<String, String>) defaultmenu.get(day)).put("time", time);
                        sendRequest();
                    }, 12, 0, true);
            timePickerDialog.show();
        } else if (v == binding.dinnertime) {
            TimePickerDialog timePickerDialog = new TimePickerDialog(this, R.style.TimePickerTheme,
                    (view, hourOfDay, minute) -> {
                        String hr = String.valueOf(hourOfDay);
                        String min = String.valueOf(minute);
                        makeDeepCopy();
                        binding.dinnertime.setText(hr + ":" + min);
                        String time = binding.breaktime.getText().toString() + "," + binding.lunchtime.getText().toString() + "," + hr + ":" + min;
                        ((HashMap<String, String>) defaultmenu.get(day)).put("time", time);
                        sendRequest();
                    }, 12, 0, true);
            timePickerDialog.show();
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
                                if (!task.isSuccessful()) {
                                    defaultmenu = DeepClone.deepClone(temp);
                                    ((MealAdapter) mAdapter1).dataChanger(defaultmenu);
                                    ((MealAdapter) mAdapter2).dataChanger(defaultmenu);
                                    ((MealAdapter) mAdapter3).dataChanger(defaultmenu);
                                    setTime();
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

    @Override
    public void onCustomItemClick4(int index) {
        CardsPOJO cardsPOJO = cardsPOJOS.get(index);
        int i = cardsPOJO.getCta();
        if (i == 1) {
            Intent intent = new Intent(Frame101.this, frame21.class);
            intent.putExtra("Source", "Mycooks");
            startActivity(intent);
        } else if (i == 2) {
            Intent intent = new Intent(Frame101.this, MyTrials.class);
            startActivity(intent);
        } else if (i == 3) {
            String url = "https://api.whatsapp.com/send?phone=+917972803790&text=Hey, Supercook " + cardsPOJO.getUrl();
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            intent.setPackage("com.whatsapp");
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            } else {
                intent.setPackage(null);
            }
            startActivity(intent);
        } else {
            Intent intent = new Intent(Frame101.this, Webview.class);
            intent.putExtra("url", cardsPOJO.getUrl());
            startActivity(intent);
        }
    }
}