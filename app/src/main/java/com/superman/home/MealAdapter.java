package com.superman.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.superman.R;
import com.superman.utilities.CustomItemClickListener3;

import java.util.HashMap;

public class MealAdapter extends RecyclerView.Adapter<MealAdapter.ViewHolder> {
    int mealtime;
    Context context;
    private final CustomItemClickListener3 customItemClickListener3;
    HashMap<String, Object> mymenu;
    private String day;

    public MealAdapter(int i, Context context, HashMap<String, Object> mymenu, String day, CustomItemClickListener3 listener3) {
        mealtime = i;
        this.context = context;
        this.mymenu = mymenu;
        this.day = day;
        customItemClickListener3 = listener3;
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
        String meals = ((HashMap<String, String>) mymenu.get(day)).get(bld);
        String quantity = ((HashMap<String, String>) mymenu.get(day)).get(bld + "Quantity");
        String[] mealsarray = meals.split(",");
        String[] quantityarray = quantity.split(",");

        holder.mealtxt.setText(mealsarray[position].trim());
        holder.quantity.setText(quantityarray[position].trim());
        if (quantityarray[position].trim().equals("1")) {
            holder.trash.setVisibility(View.VISIBLE);
            holder.minus.setVisibility(View.GONE);
        } else {
            holder.trash.setVisibility(View.GONE);
            holder.minus.setVisibility(View.VISIBLE);
        }
    }

    public void dayChanger(String myday) {
        day = myday;
    }

    @Override
    public int getItemCount() {
        if (day == null || mymenu == null) {
            return 0;
        } else if (mymenu.containsKey(day)) {
            HashMap<String, String> mealdetails = (HashMap<String, String>) mymenu.get(day);
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

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final View view;
        private final ImageView mealimg;
        private final TextView mealtxt;
        private final TextView quantity;
        private final TextView minus;
        private final ImageView trash;
        private final TextView add;
        private final FrameLayout minusframe;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            quantity = view.findViewById(R.id.quantity);
            mealimg = view.findViewById(R.id.mealimg);
            mealtxt = view.findViewById(R.id.mealtxt);
            minus = view.findViewById(R.id.minus);
            trash = view.findViewById(R.id.trash);
            add = view.findViewById(R.id.add);
            minusframe = view.findViewById(R.id.minusframe);
            minusframe.setOnClickListener(this);
            trash.setOnClickListener(this);
            add.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            customItemClickListener3.onCustomItemClick(clickedPosition, mealtime, v == add);
        }
    }
}
