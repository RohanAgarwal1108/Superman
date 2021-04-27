package com.superman.cookSelection;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.superman.R;
import com.superman.utilities.ExtraUtils;

public class Frame96Adapter2 extends RecyclerView.Adapter<Frame96Adapter2.ViewHolder> {
    private final Context mContext;

    public Frame96Adapter2(Context context) {
        mContext = context;
    }

    @NonNull
    @Override
    public Frame96Adapter2.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.quantity96_item, parent, false);
        Frame96Adapter2.ViewHolder viewHolder = new Frame96Adapter2.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull Frame96Adapter2.ViewHolder holder, int position) {
        SelectedDishes selectedDish = SelectedDishes.selectedDishes.get(position);
        holder.quanttxt.setText(selectedDish.getName());
        holder.quantity.setText(String.valueOf(selectedDish.getQuantity()));

        holder.minus.setOnClickListener(v -> {
            if (SelectedDishes.selectedDishes.get(position).getQuantity() > 1) {
                SelectedDishes.selectedDishes.get(position).setQuantity(SelectedDishes.selectedDishes.get(position).getQuantity() - 1);
                notifyDataSetChanged();
            }
        });

        holder.plus.setOnClickListener(v -> {
            if (SelectedDishes.selectedDishes.get(position).getQuantity() < 7) {
                SelectedDishes.selectedDishes.get(position).setQuantity(SelectedDishes.selectedDishes.get(position).getQuantity() + 1);
                notifyDataSetChanged();
            }
        });

        int id = mContext.getResources().getIdentifier("drawable/" + ExtraUtils.nameFormatter(selectedDish.getName()), null, mContext.getPackageName());
        holder.quantimg.setImageResource(id == 0 ? R.drawable.ic_dishplaceholder : id);
    }

    @Override
    public int getItemCount() {
        if (SelectedDishes.selectedDishes == null) {
            return 0;
        }
        return SelectedDishes.selectedDishes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View view;
        private final ImageView quantimg;
        private final TextView quanttxt;
        private final TextView quantity;
        private final TextView plus;
        private final TextView minus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            quantimg = view.findViewById(R.id.quantimg);
            quanttxt = view.findViewById(R.id.quanttxt);
            plus = view.findViewById(R.id.plus);
            quantity = view.findViewById(R.id.quantity);
            minus = view.findViewById(R.id.minus);
        }
    }
}
