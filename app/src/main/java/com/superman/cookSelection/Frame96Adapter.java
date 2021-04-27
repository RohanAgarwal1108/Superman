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
import com.superman.home.Frame110;
import com.superman.home.Frame111;
import com.superman.home.Frame112;
import com.superman.home.Frame116;
import com.superman.utilities.CustomItemClickListener;
import com.superman.utilities.ExtraUtils;

import java.util.ArrayList;
import java.util.HashMap;

public class Frame96Adapter extends RecyclerView.Adapter<Frame96Adapter.ViewHolder> {
    private final String[] dataset;
    private final CustomItemClickListener listener;
    private final Context mContext;
    private final int tracker;

    public Frame96Adapter(Context context, CustomItemClickListener listener, HashMap<String, ArrayList<Lang_FoodPOJO>> alldishes, int tracker) {
        dataset = alldishes.keySet().toArray(new String[0]);
        this.listener = listener;
        mContext = context;
        this.tracker = tracker;
    }

    @NonNull
    @Override
    public Frame96Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cuisine96_item, parent, false);
        Frame96Adapter.ViewHolder viewHolder = new Frame96Adapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull Frame96Adapter.ViewHolder holder, int position) {
        String cuisine = dataset[position];
        if ((tracker == 0 ? Frame96.index : tracker == 1 ? Frame110.index : tracker == 2 ? Frame111.index : tracker == 4 ? Frame116.index : Frame112.index) == position) {
            holder.background.setCardBackgroundColor(mContext.getColor(R.color.yellow));
        } else {
            holder.background.setCardBackgroundColor(mContext.getColor(R.color.white));
        }
        holder.cuisinetxt.setText(cuisine);
        int id = mContext.getResources().getIdentifier("drawable/" + ExtraUtils.nameFormatter(cuisine), null, mContext.getPackageName());
        holder.cuisineimg.setImageResource(id == 0 ? R.drawable.ic_dishplaceholder : id);
    }

    @Override
    public int getItemCount() {
        if (dataset == null) {
            return 0;
        }
        return dataset.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final View view;
        private final ImageView cuisineimg;
        private final TextView cuisinetxt;
        private final CardView background;
        private final RelativeLayout cuisine;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            cuisineimg = view.findViewById(R.id.cuisineimg);
            cuisinetxt = view.findViewById(R.id.cuisinetxt);
            background = view.findViewById(R.id.background);
            cuisine = view.findViewById(R.id.cuisine);
            cuisine.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            listener.onCustomItemClick(clickedPosition);
        }
    }
}
