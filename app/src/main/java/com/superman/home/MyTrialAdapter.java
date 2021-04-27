package com.superman.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sinaseyfi.advancedcardview.AdvancedCardView;
import com.squareup.picasso.Picasso;
import com.superman.R;
import com.superman.cookSelection.CookDetails;
import com.superman.utilities.CookItemClickListener1;

import java.util.ArrayList;

public class MyTrialAdapter extends RecyclerView.Adapter<MyTrialAdapter.ViewHolder> {
    private final ArrayList<CookDetails> cookDetails;
    private final CookItemClickListener1 listener1;
    private final boolean isOngoing;
    private final Context context;

    public MyTrialAdapter(ArrayList<CookDetails> cookDetails, CookItemClickListener1 listener, boolean isOngoing, Context context) {
        this.cookDetails = cookDetails;
        this.listener1 = listener;
        this.context = context;
        this.isOngoing = isOngoing;
    }

    @NonNull
    @Override
    public MyTrialAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.mytrial_item, parent, false);
        MyTrialAdapter.ViewHolder viewHolder = new MyTrialAdapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyTrialAdapter.ViewHolder holder, int position) {
        CookDetails cookDetail = cookDetails.get(position);
        holder.cookname.setText(cookDetail.getName());
        holder.cooksupercode.setText(cookDetail.getSuperCode());
        holder.meals.setText(getFormattedMeal(cookDetail));
        holder.slotbooked.setText(getSlot(cookDetail));
        holder.address.setText(cookDetail.getAddress());
        Picasso.get().load(cookDetail.getCookPic()).placeholder(R.drawable.ic_cook_def).into(holder.trialimg);
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
            return "";
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
        public final TextView cooksupercode;
        public final TextView meals;
        public final TextView slotbooked;
        public final TextView address;
        public final View line1;
        public final View line2;
        public final AdvancedCardView circle1;
        public final AdvancedCardView circle2;
        public final AdvancedCardView circle3;
        public final AdvancedCardView circle4;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            trialimg = view.findViewById(R.id.trialimg);
            cookname = view.findViewById(R.id.cookname);
            cooksupercode = view.findViewById(R.id.cooksupercode);
            meals = view.findViewById(R.id.meal);
            slotbooked = view.findViewById(R.id.slotbooked);
            address = view.findViewById(R.id.address);
            line1 = view.findViewById(R.id.line1);
            line2 = view.findViewById(R.id.line2);
            circle1 = view.findViewById(R.id.circle1);
            circle2 = view.findViewById(R.id.circle2);
            circle3 = view.findViewById(R.id.circle3);
            circle4 = view.findViewById(R.id.circle4);
            if (isOngoing) {
                circle1.setBackground_Color(context.getColor(R.color.primaryblue));
                circle2.setBackground_Color(context.getColor(R.color.primaryblue));
                circle3.setBackground_Color(context.getColor(R.color.primaryblue));
                circle4.setBackground_Color(context.getColor(R.color.primaryblue));
                line1.setBackgroundColor(context.getColor(R.color.primaryblue));
                line2.setBackgroundColor(context.getColor(R.color.primaryblue));
            } else {
                circle1.setBackground_Color(context.getColor(R.color.secondaryblue));
                circle2.setBackground_Color(context.getColor(R.color.secondaryblue));
                circle3.setBackground_Color(context.getColor(R.color.secondaryblue));
                circle4.setBackground_Color(context.getColor(R.color.secondaryblue));
                line1.setBackgroundColor(context.getColor(R.color.secondaryblue));
                line2.setBackgroundColor(context.getColor(R.color.secondaryblue));
            }
        }
    }
}
