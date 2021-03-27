package com.superman.home;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.github.islamkhsh.CardSliderViewPager;
import com.google.android.gms.tasks.Task;
import com.google.firebase.functions.FirebaseFunctionsException;
import com.superman.R;
import com.superman.Utilities.CustomItemClickListener;
import com.superman.Utilities.CustomItemClickListener2;
import com.superman.Utilities.ScreenUtils;
import com.superman.common.MainActivity;
import com.superman.databinding.ActivityFrame101Binding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Frame101 extends AppCompatActivity implements CustomItemClickListener2, CustomItemClickListener {
    private ActivityFrame101Binding binding;
    private ArrayList<CardsPOJO> cardsPOJOS;
    private Frame101Adapter adapter;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private SliderLayoutManager sliderLayoutManager;
    private ArrayList<String> dates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFrame101Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupMainCard();
        setupWeekRecycler();
        setHome();
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

    private void createDates() {
        dates = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        for (int i = 0; i < 7; i++) {
            Date date = calendar.getTime();
            String day = new SimpleDateFormat("dd").format(date);
            String month = new SimpleDateFormat("E").format(date);
            dates.add(day + month);
            calendar.add(Calendar.DATE, 1);
        }
    }

    private void setupMainCard() {
        CardSliderViewPager cardSliderViewPager = binding.cards;
        cardsPOJOS = new ArrayList<>();
        adapter = new Frame101Adapter(cardsPOJOS, this);
        cardSliderViewPager.setAdapter(adapter);
    }

    private void setHome() {
        getHome()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Exception e = task.getException();
                        if (e instanceof FirebaseFunctionsException) {
                            FirebaseFunctionsException ffe = (FirebaseFunctionsException) e;
                            FirebaseFunctionsException.Code code = ffe.getCode();
                        }
                    } else {
                        HashMap<String, Object> result = task.getResult();
                        Log.e("jbhb", String.valueOf(result));
                        ArrayList<HashMap<String, Object>> cards = (ArrayList<HashMap<String, Object>>) result.get("cards");
                        for (int i = 0; i < cards.size(); i++) {
                            HashMap<String, Object> card = cards.get(i);
                            CardsPOJO cardPOJO = new CardsPOJO((int) card.get("cta"), (String) card.get("color"), (String) card.get("details"), (String) card.get("title"));
                            cardsPOJOS.add(cardPOJO);
                        }
                        //ArrayList<HashMap<String, Object>> defaultMeals= (ArrayList<HashMap<String, Object>>) result.get("defaultMeals");
                        /*if(defaultMeals==null){
                            //todo
                        }
                        else{
                            //todo
                        }*/
                        adapter.notifyDataSetChanged();
                    }
                });
    }

    private Task<HashMap<String, Object>> getHome() {
        Map<String, Object> data = new HashMap<>();
        data.put("uid", "Al1Ynrrtp4RDXX6f9fSMrzuCCob2");//User.user.getUid());int selectedLeft=ScreenUtils.Companion.dpToPx(context, 96);
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
        //todo
    }
}