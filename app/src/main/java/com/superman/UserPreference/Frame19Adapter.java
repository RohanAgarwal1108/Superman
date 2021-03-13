package com.superman.UserPreference;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.superman.R;
import com.superman.Utilities.CustomItemClickListener;

import java.util.ArrayList;

public class Frame19Adapter extends RecyclerView.Adapter<Frame19Adapter.ViewHolder> {
    private final ArrayList<Lang_FoodPOJO> mDataset;
    private final CustomItemClickListener mOnClickListener;
    private final Context context;

    public Frame19Adapter(Context context, ArrayList<Lang_FoodPOJO> dataset, CustomItemClickListener listener) {
        mDataset = dataset;
        mOnClickListener = listener;
        this.context = context;
    }

    @NonNull
    @Override
    public Frame19Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cuisine_item, parent, false);
        Frame19Adapter.ViewHolder viewHolder = new Frame19Adapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull Frame19Adapter.ViewHolder holder, int position) {
        Lang_FoodPOJO lang_foodPOJO = mDataset.get(position);
        holder.cuisinetxt.setText(lang_foodPOJO.getLanguage());
        if (lang_foodPOJO.isSelected()) {
            holder.cuisinecard.setCardBackgroundColor(context.getColor(R.color.yellow));
        } else {
            holder.cuisinecard.setCardBackgroundColor(context.getColor(R.color.white));
        }
        holder.cuisineimg.setImageResource(R.drawable.ic_launcher_background);
    }

    @Override
    public int getItemCount() {
        if (mDataset == null) {
            return 0;
        } else {
            return mDataset.size();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final View view;
        private final CardView cuisinecard;
        private final TextView cuisinetxt;
        private final ImageView cuisineimg;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            cuisinecard = view.findViewById(R.id.cuisinecard);
            cuisineimg = view.findViewById(R.id.cuisineimg);
            cuisinetxt = view.findViewById(R.id.cuisinetxt);
            cuisinecard.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onCustomItemClick(clickedPosition);
        }
    }
}
