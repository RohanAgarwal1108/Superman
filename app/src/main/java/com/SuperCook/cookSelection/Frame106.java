package com.SuperCook.cookSelection;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.SuperCook.databinding.ActivityFrame106Binding;
import com.archit.calendardaterangepicker.customviews.CalendarListener;

import java.util.Calendar;

public class Frame106 extends AppCompatActivity {
    private ActivityFrame106Binding binding;
    private Calendar startDateSelectable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFrame106Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.next106.setAlpha(0.5f);
        setUpCalendarTime();

        binding.next106.setOnClickListener(v -> {
            if (binding.next106.getAlpha() == 1) {
                Intent intent = new Intent();
                intent.putExtra("newdate", getUserTime());
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });

        binding.back106.setOnClickListener(v -> onBackPressed());
    }

    private String getUserTime() {
        Calendar calendar = binding.calendar.getStartDate();
        calendar.set(Calendar.HOUR_OF_DAY, binding.hourPicker.getValue());
        calendar.set(Calendar.MINUTE, binding.minutePicker.getValue());
        return calendar.getTime().toString();
    }

    private void setUpCalendarTime() {
        //font
        Typeface nb = Typeface.createFromAsset(getAssets(), "fonts/nb.otf");
        binding.calendar.setFonts(nb);
        binding.hourPicker.setTypeface(nb);
        binding.hourPicker.setSelectedTypeface(nb);
        binding.minutePicker.setSelectedTypeface(nb);
        binding.minutePicker.setTypeface(nb);
        startDateSelectable = Calendar.getInstance();
        startDateSelectable.add(Calendar.HOUR_OF_DAY, 2);
        Calendar calendar1 = Calendar.getInstance();
        calendar1.add(Calendar.DATE, 7);
        binding.calendar.setSelectableDateRange(startDateSelectable, calendar1);
        binding.calendar.setCalendarListener(new CalendarListener() {
            @Override
            public void onFirstDateSelected(Calendar calendar) {

            }

            @Override
            public void onDateRangeSelected(Calendar calendar2, Calendar calendar3) {
                binding.next106.setAlpha(1);
                if (calendar2.get(Calendar.YEAR) == startDateSelectable.get(Calendar.YEAR)
                        && calendar2.get(Calendar.MONTH) == startDateSelectable.get(Calendar.MONTH)
                        && calendar2.get(Calendar.DAY_OF_MONTH) == startDateSelectable.get(Calendar.DAY_OF_MONTH)) {
                    binding.hourPicker.setMinValue(startDateSelectable.get(Calendar.HOUR_OF_DAY));
                    minuteChanger(binding.hourPicker.getValue());
                } else {
                    binding.hourPicker.setMinValue(0);
                    binding.minutePicker.setMinValue(0);
                }
            }
        });
    }

    /**
     * To set the min and max value for minute slider
     */
    private void minuteChanger(int value) {
        if (value > startDateSelectable.get(Calendar.HOUR_OF_DAY)) {
            binding.minutePicker.setMinValue(0);
        } else {
            binding.minutePicker.setMinValue(startDateSelectable.get(Calendar.MINUTE));
        }
        Calendar calendar = binding.calendar.getStartDate();
        if (calendar.get(Calendar.YEAR) == startDateSelectable.get(Calendar.YEAR)
                && calendar.get(Calendar.MONTH) == startDateSelectable.get(Calendar.MONTH)
                && calendar.get(Calendar.DAY_OF_MONTH) == startDateSelectable.get(Calendar.DAY_OF_MONTH)) {

        } else {
            binding.minutePicker.setMinValue(0);
        }
    }
}