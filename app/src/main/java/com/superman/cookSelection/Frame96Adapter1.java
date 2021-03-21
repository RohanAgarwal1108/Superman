package com.superman.cookSelection;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.superman.R;
import com.superman.UserPreference.Lang_FoodPOJO;
import com.superman.Utilities.CustomItemClickListener1;

import java.util.ArrayList;

public class Frame96Adapter1 extends RecyclerView.Adapter<Frame96Adapter1.ViewHolder> {
    private final Context mContext;
    private final CustomItemClickListener1 listener1;
    private ArrayList<Lang_FoodPOJO> dishes;

    public Frame96Adapter1(Context context, ArrayList<Lang_FoodPOJO> dishes, CustomItemClickListener1 customItemClickListener1) {
        mContext = context;
        listener1 = customItemClickListener1;
        this.dishes = new ArrayList<>();
        this.dishes.addAll(dishes);
    }

    @NonNull
    @Override
    public Frame96Adapter1.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.dish_list, parent, false);
        Frame96Adapter1.ViewHolder viewHolder = new Frame96Adapter1.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull Frame96Adapter1.ViewHolder holder, int position) {
        Lang_FoodPOJO dish = dishes.get(position);
        if (dish.isSelected()) {
            holder.background.setCardBackgroundColor(mContext.getColor(R.color.yellow));
        } else {
            holder.background.setCardBackgroundColor(mContext.getColor(R.color.white));
        }
        holder.dishes1.setOnClickListener(v -> {
            int clickedPosition = position;
            if (dishes.get(clickedPosition).isSelected()) {
                for (int i = 0; i < SelectedDishes.selectedDishes.size(); i++) {
                    SelectedDishes selectedDish = SelectedDishes.selectedDishes.get(i);
                    if (selectedDish.getName().equals(dish.getLanguage())) {
                        SelectedDishes.selectedDishes.remove(i);
                        break;
                    }
                }
                dishes.get(clickedPosition).setSelected(!dishes.get(clickedPosition).isSelected());
                listener1.onCustomItemClick1(clickedPosition, 0);
            } else {
                if (SelectedDishes.selectedDishes.size() != 3) {
                    dishes.get(clickedPosition).setSelected(!dishes.get(clickedPosition).isSelected());
                    SelectedDishes.selectedDishes.add(new SelectedDishes(dish.getLanguage(), 1));
                    listener1.onCustomItemClick1(clickedPosition, 0);
                } else {
                    listener1.onCustomItemClick1(clickedPosition, 1);
                }
            }
        });
        holder.dishtxt.setText(dish.getLanguage());
        //todo image
    }

    public void datachanger(ArrayList<Lang_FoodPOJO> newdishes) {
        dishes = new ArrayList<>();
        dishes.addAll(newdishes);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (dishes == null) {
            return 0;
        }
        return dishes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View view;
        private final ImageView dishimg;
        private final TextView dishtxt;
        private final CardView background;
        private final RelativeLayout dishes1;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            dishimg = view.findViewById(R.id.dishimg);
            dishtxt = view.findViewById(R.id.dishtxt);
            background = view.findViewById(R.id.background);
            dishes1 = view.findViewById(R.id.dishes);
        }
    }
}
