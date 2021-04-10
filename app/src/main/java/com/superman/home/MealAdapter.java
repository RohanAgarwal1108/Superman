package com.superman.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.superman.R;

import java.util.HashMap;

public class MealAdapter extends RecyclerView.Adapter<MealAdapter.ViewHolder> {
    int mealtime;
    Context context;

    public MealAdapter(int i, Context context) {
        mealtime = i;
        this.context = context;
    }

    @NonNull
    @Override
    public MealAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.default_menu_item, parent, false);
        MealAdapter.ViewHolder viewHolder = new MealAdapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MealAdapter.ViewHolder holder, int position) {
        String bld;
        if (mealtime == 0) {
            bld = "Breakfast";
        } else if (mealtime == 1) {
            bld = "Lunch";
        } else {
            bld = "Dinner";
        }
        String meals = ((HashMap<String, String>) Frame101.defaultmenu.get(Frame101.day)).get(bld);
        String[] mealsarray = meals.split(",");
        holder.mealtxt.setText(mealsarray[position]);
    }

    @Override
    public int getItemCount() {
        if (Frame101.day == null || Frame101.defaultmenu == null) {
            return 0;
        } else if (Frame101.defaultmenu.containsKey(Frame101.day)) {
            HashMap<String, String> mealdetails = (HashMap<String, String>) Frame101.defaultmenu.get(Frame101.day);
            if (mealtime == 0 && mealdetails.containsKey("Breakfast") && mealdetails.get("Breakfast").length() != 0) {
                char comma = ',';
                int count = 0;
                for (int j = 0; j < mealdetails.get("Breakfast").length(); j++) {
                    if (mealdetails.get("Breakfast").charAt(j) == comma) {
                        count++;
                    }
                }
                return count + 1;
            } else if (mealtime == 1 && mealdetails.containsKey("Lunch") && mealdetails.get("Lunch").length() != 0) {
                char comma = ',';
                int count = 0;
                for (int j = 0; j < mealdetails.get("Lunch").length(); j++) {
                    if (mealdetails.get("Lunch").charAt(j) == comma) {
                        count++;
                    }
                }
                return count + 1;
            } else if (mealtime == 2 && mealdetails.containsKey("Dinner") && mealdetails.get("Dinner").length() != 0) {
                char comma = ',';
                int count = 0;
                for (int j = 0; j < mealdetails.get("Dinner").length(); j++) {
                    if (mealdetails.get("Dinner").charAt(j) == comma) {
                        count++;
                    }
                }
                return count + 1;
            } else {
                return 0;
            }
        } else {
            return 0;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View view;
        private final ImageView mealimg;
        private final TextView mealtxt;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            mealimg = view.findViewById(R.id.mealimg);
            mealtxt = view.findViewById(R.id.mealtxt);
        }
    }
}
