package com.SuperCook.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.SuperCook.R;
import com.SuperCook.utilities.CustomItemClickListener3;
import com.SuperCook.utilities.ExtraUtils;

import java.util.HashMap;

public class MealAdapter extends RecyclerView.Adapter<MealAdapter.ViewHolder> {
    int mealtime;
    Context context;
    private final CustomItemClickListener3 customItemClickListener3;
    HashMap<String, Object> mymenu;
    private String day;
    private int size;

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
        if (size != 1) {
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

            if (position == mealsarray.length) {
                holder.addmeal.setVisibility(View.VISIBLE);
                if (position == 6) {
                    holder.addmeal.setAlpha(0.5f);
                } else {
                    holder.addmeal.setAlpha(1);
                }
                holder.item.setVisibility(View.GONE);
            } else {
                int id = context.getResources().getIdentifier("drawable/" + ExtraUtils.nameFormatter(mealsarray[position].trim()), null, context.getPackageName());
                holder.mealimg.setImageResource(id == 0 ? R.drawable.ic_dishplaceholder : id);
                holder.addmeal.setVisibility(View.GONE);
                holder.item.setVisibility(View.VISIBLE);
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
        } else {
            holder.addmeal.setVisibility(View.VISIBLE);
            holder.addmeal.setAlpha(1);
            holder.item.setVisibility(View.GONE);
        }
    }

    public void dataChanger(HashMap<String, Object> map) {
        mymenu = map;
        notifyDataSetChanged();
    }

    public void dayChanger(String myday) {
        day = myday;
    }

    @Override
    public int getItemCount() {
        if (day == null || mymenu == null) {
            size = 1;
            return 1;
        } else if (mymenu.containsKey(day)) {
            HashMap<String, String> mealdetails = (HashMap<String, String>) mymenu.get(day);
            String[] times = {"Breakfast", "Lunch", "Dinner"};
            if (mealtime >= 0 && mealtime <= 2 && mealdetails.containsKey(times[mealtime]) && mealdetails.get(times[mealtime]).length() != 0) {
                String str = mealdetails.get(times[mealtime]);
                String[] str1 = str.split(",");
                size = str1.length + 1;
                return str1.length + 1;
            } else {
                size = 1;
                return 1;
            }
        } else {
            size = 1;
            return 1;
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
        private final FrameLayout addmeal;
        private final RelativeLayout item;

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
            addmeal = view.findViewById(R.id.addmeal);
            item = view.findViewById(R.id.item);
            minusframe.setOnClickListener(this);
            trash.setOnClickListener(this);
            add.setOnClickListener(this);
            addmeal.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            if (v == minusframe || v == trash || v == add) {
                customItemClickListener3.onCustomItemClick(clickedPosition, mealtime, v == add);
            } else if (v == addmeal && clickedPosition != 6) {
                customItemClickListener3.onCustomItemClick(-1, mealtime, true);
            }
        }
    }
}
