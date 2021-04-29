package com.SuperCook.cookSelection;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.SuperCook.R;
import com.SuperCook.UserPreference.Lang_FoodPOJO;
import com.SuperCook.utilities.CustomItemClickListener1;
import com.SuperCook.utilities.ExtraUtils;
import com.sinaseyfi.advancedcardview.AdvancedCardView;

import java.util.ArrayList;
import java.util.List;

public class Frame96Adapter1 extends RecyclerView.Adapter<Frame96Adapter1.ViewHolder> {
    private final Context mContext;
    private final CustomItemClickListener1 listener1;
    private ArrayList<Lang_FoodPOJO> dishes;
    private final int selections;
    private final int i;

    public Frame96Adapter1(Context context, ArrayList<Lang_FoodPOJO> dishes, CustomItemClickListener1 customItemClickListener1, int selections, int i) {
        mContext = context;
        listener1 = customItemClickListener1;
        this.dishes = new ArrayList<>();
        this.dishes.addAll(dishes);
        this.selections = selections;
        this.i = i;
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
            holder.background.setBackground_Color(mContext.getColor(R.color.yellow));
        } else {
            holder.background.setBackground_Color(mContext.getColor(R.color.white));
        }
        holder.dishes1.setOnClickListener(v -> {
            int clickedPosition = position;
            List<SelectedDishes> myselected;
            if (i == 0) {
                myselected = SelectedDishes.selectedDishes;
            } else if (i == 10) {
                listener1.onCustomItemClick1(clickedPosition, 1);
                return;
            } else {
                myselected = SelectedDishes.mealSelectedDishes[i - 1];
            }

            if (dishes.get(clickedPosition).isSelected()) {
                for (int i = 0; i < myselected.size(); i++) {
                    SelectedDishes selectedDish = myselected.get(i);
                    if (selectedDish.getName().equals(dish.getLanguage())) {
                        myselected.remove(i);
                        break;
                    }
                }
                dishes.get(clickedPosition).setSelected(!dishes.get(clickedPosition).isSelected());
                listener1.onCustomItemClick1(clickedPosition, 0);
            } else {
                if (myselected.size() != selections) {
                    dishes.get(clickedPosition).setSelected(!dishes.get(clickedPosition).isSelected());
                    myselected.add(new SelectedDishes(dish.getLanguage(), 1));
                    listener1.onCustomItemClick1(clickedPosition, 0);
                } else {
                    listener1.onCustomItemClick1(clickedPosition, 1);
                }
            }
        });
        holder.dishtxt.setText(dish.getLanguage());
        int id = mContext.getResources().getIdentifier("drawable/" + ExtraUtils.nameFormatter(dish.getLanguage()), null, mContext.getPackageName());
        holder.dishimg.setImageResource(id == 0 ? R.drawable.ic_dishplaceholder : id);
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
        private final AdvancedCardView background;
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
