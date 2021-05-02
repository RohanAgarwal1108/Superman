package com.SuperCook.home;

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
import com.SuperCook.cookSelection.CookDetails;
import com.SuperCook.utilities.Animations;
import com.SuperCook.utilities.CookItemClickListener1;
import com.sinaseyfi.advancedcardview.AdvancedCardView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MyTrialAdapter1 extends RecyclerView.Adapter<MyTrialAdapter1.ViewHolder> {
    private final ArrayList<CookDetails> cookDetails;
    private final CookItemClickListener1 listener1;
    private final boolean isOngoing;
    private final Context context;

    public MyTrialAdapter1(ArrayList<CookDetails> cookDetails, CookItemClickListener1 listener, boolean isOngoing, Context context) {
        this.cookDetails = cookDetails;
        this.listener1 = listener;
        this.context = context;
        this.isOngoing = isOngoing;
    }

    @NonNull
    @Override
    public MyTrialAdapter1.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.mytrial_item1, parent, false);
        MyTrialAdapter1.ViewHolder viewHolder = new MyTrialAdapter1.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyTrialAdapter1.ViewHolder holder, int position) {
        CookDetails cookDetail = cookDetails.get(position);
        holder.cookname.setText(cookDetail.getName());
        String str = getFormattedMeal(cookDetail);
        if (str.equals("No meals selected.")) {
            holder.meal.setVisibility(View.GONE);
        } else {
            holder.meal.setVisibility(View.VISIBLE);
        }
        holder.meals.setText(str);
        holder.slotbooked.setText(getSlot(cookDetail));
        holder.address.setText(cookDetail.getAddress());
        Picasso.get().load(cookDetail.getCookPic()).placeholder(R.drawable.ic_cook_def).into(holder.trialimg);
        holder.view.setOnClickListener(v -> {
            boolean show = toggleLayout(!cookDetail.isExpanded(), v, holder.layoutExpand, holder.circle1, holder.circle2);
            cookDetails.get(position).setExpanded(show);
        });
        holder.hire.setOnClickListener(v -> listener1.onCookItemClick(holder.getAdapterPosition(), cookDetail, holder.trialimg, isOngoing));
    }

    private boolean toggleLayout(boolean isExpanded, View v, RelativeLayout layoutExpand, AdvancedCardView card1, AdvancedCardView card2) {
        Animations.toggleArrow(v, isExpanded);
        if (isExpanded) {
            Animations.expand(layoutExpand);
            card1.setVisibility(View.VISIBLE);
            card2.setVisibility(View.VISIBLE);
        } else {
            Animations.collapse(layoutExpand);
            card1.setVisibility(View.GONE);
            card2.setVisibility(View.GONE);
        }
        return isExpanded;
    }

    private String getSlot(CookDetails cookDetail) {
        String[] str = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        String unformatted = cookDetail.getSlotbooked();
        String[] time = unformatted.split("/");
        String formatted = time[0];
        formatted = formatted + " " + str[Integer.parseInt(time[1]) - 1] + " " + time[3];
        return formatted;
    }

    private String getFormattedMeal(CookDetails cookDetail) {
        if (cookDetail.getQuantity() == null || cookDetail.getQuantity().trim().isEmpty()) {
            return "No meals selected.";
        }
        String[] meals = cookDetail.getMeal().split(",");
        String[] quantity = cookDetail.getQuantity().split(",");
        String formattedmeal = "";
        for (int i = 0; i < quantity.length; i++) {
            formattedmeal = formattedmeal + meals[i] + " x " + quantity[i];
            if (i != quantity.length - 1) {
                formattedmeal += "\n";
            }
        }
        return formattedmeal;
    }

    @Override
    public int getItemCount() {
        if (cookDetails == null) {
            return 0;
        }
        return cookDetails.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View view;
        public final ImageView trialimg;
        public final TextView cookname;
        public final TextView meals;
        public final TextView meal;
        public final TextView slotbooked;
        public final TextView address;
        public final View line1;
        public final View line2;
        public final AdvancedCardView circle1;
        public final AdvancedCardView circle2;
        public final AdvancedCardView circle3;
        public final AdvancedCardView circle4;
        public final RelativeLayout layoutExpand;
        public final TextView hire;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            trialimg = view.findViewById(R.id.trialimg);
            cookname = view.findViewById(R.id.cookname);
            meals = view.findViewById(R.id.meal);
            meal = view.findViewById(R.id.meals);
            slotbooked = view.findViewById(R.id.slotbooked);
            address = view.findViewById(R.id.address);
            layoutExpand = view.findViewById(R.id.expandedets);
            line1 = view.findViewById(R.id.line1);
            line2 = view.findViewById(R.id.line2);
            circle1 = view.findViewById(R.id.circle1);
            circle2 = view.findViewById(R.id.circle2);
            circle3 = view.findViewById(R.id.circle3);
            circle4 = view.findViewById(R.id.circle4);
            hire = view.findViewById(R.id.hire);
            circle1.setBackground_Color(context.getColor(R.color.secondaryblue));
            circle2.setBackground_Color(context.getColor(R.color.secondaryblue));
            circle3.setBackground_Color(context.getColor(R.color.secondaryblue));
            circle4.setBackground_Color(context.getColor(R.color.secondaryblue));
            line1.setBackgroundColor(context.getColor(R.color.secondaryblue));
            line2.setBackgroundColor(context.getColor(R.color.secondaryblue));
        }
    }
}
