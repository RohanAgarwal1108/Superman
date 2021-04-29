package com.SuperCook.cookSelection;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.SuperCook.R;
import com.SuperCook.utilities.CookItemClickListener;
import com.SuperCook.utilities.CustomItemClickListener1;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class Frame21Adapter extends RecyclerView.Adapter<Frame21Adapter.ViewHolder> {
    private final ArrayList<CookDetails> mDataset;
    private final CustomItemClickListener1 customItemClickListener;
    private final CookItemClickListener cookItemClickListener;

    public Frame21Adapter(ArrayList<CookDetails> cookDetails, CustomItemClickListener1 listener, CookItemClickListener listener1) {
        mDataset = cookDetails;
        customItemClickListener = listener;
        cookItemClickListener = listener1;
    }

    @NonNull
    @Override
    public Frame21Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.frame21item, parent, false);
        Frame21Adapter.ViewHolder viewHolder = new Frame21Adapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull Frame21Adapter.ViewHolder holder, int position) {
        CookDetails cookDetails = mDataset.get(position);
        holder.name.setText(cookDetails.getName());
        holder.cost.setText(cookDetails.getCharges() + "/M");
        holder.oneliner.setText(cookDetails.getBio());
        holder.rate.setText(String.valueOf(cookDetails.getRating()));
        List<String> cuisines = cookDetails.getCuisine();
        if (cuisines == null || cuisines.size() == 0) {
            holder.cuisine.setVisibility(View.GONE);
        } else {
            holder.cuisine.setVisibility(View.VISIBLE);
            String str = "";
            for (int i = 0; i < cuisines.size(); i++) {
                if (i == cuisines.size() - 1) {
                    str += cuisines.get(i) + '!';
                } else {
                    str += cuisines.get(i) + ", ";
                }
            }
            holder.cuisine.setText(str);
        }
        holder.currentCity.setText(cookDetails.getCity());
        holder.from.setText("From " + cookDetails.getFrom());

        Picasso.get().load(cookDetails.getCookPic()).placeholder(R.drawable.ic_cook_def).into(holder.cookpic);
        ViewCompat.setTransitionName(holder.cookpic, cookDetails.getCookID());

        holder.morecard.setOnClickListener(v -> cookItemClickListener.onCookItemClick(holder.getAdapterPosition(), cookDetails, holder.cookpic));
    }

    @Override
    public int getItemCount() {
        if (mDataset == null) {
            return 0;
        }
        return mDataset.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final View view;
        private final TextView name;
        private final ImageView cookpic;
        private final TextView rate;
        private final TextView oneliner;
        private final TextView cuisine;
        private final TextView cost;
        private final CardView schedulecard;
        private final CardView morecard;
        private final CardView like;
        private final TextView from;
        private final TextView currentCity;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            name = view.findViewById(R.id.name);
            cookpic = view.findViewById(R.id.cookpic);
            rate = view.findViewById(R.id.rate);
            oneliner = view.findViewById(R.id.oneliner);
            cuisine = view.findViewById(R.id.cuisine);
            cost = view.findViewById(R.id.cost);
            schedulecard = view.findViewById(R.id.schedulecard);
            morecard = view.findViewById(R.id.morecard);
            like = view.findViewById(R.id.like);
            from = view.findViewById(R.id.from);
            currentCity = view.findViewById(R.id.currentCity);
            schedulecard.setOnClickListener(this);
            like.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            if (v == schedulecard) {
                customItemClickListener.onCustomItemClick1(clickedPosition, 2);
            } else if (v == like) {
                customItemClickListener.onCustomItemClick1(clickedPosition, 0);
            }
        }
    }
}
