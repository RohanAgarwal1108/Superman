package com.SuperCook.UserPreference;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.SuperCook.R;
import com.SuperCook.utilities.CustomItemClickListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Frame28Adapter extends RecyclerView.Adapter<Frame28Adapter.ViewHolder> {
    private final ArrayList<Lang_FoodPOJO> mDataset;
    private final CustomItemClickListener mOnClickListener;
    private final Context context;

    public Frame28Adapter(Context context, ArrayList<Lang_FoodPOJO> dataset, CustomItemClickListener listener) {
        mDataset = dataset;
        mOnClickListener = listener;
        this.context = context;
    }

    @NonNull
    @Override
    public Frame28Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.language_item, parent, false);
        Frame28Adapter.ViewHolder viewHolder = new Frame28Adapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull Frame28Adapter.ViewHolder holder, int position) {
        Lang_FoodPOJO langFoodPOJO = mDataset.get(position);
        if (langFoodPOJO.isSelected()) {
            holder.langcard.setCardBackgroundColor(context.getColor(R.color.yellow));
        } else {
            holder.langcard.setCardBackgroundColor(context.getColor(R.color.white));
        }
        Picasso.get().load(langFoodPOJO.getUrl()).placeholder(R.drawable.langdef).into(holder.langsrc);
        holder.lang.setText(langFoodPOJO.getLanguage());
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
        private final CardView langcard;
        private final ImageView langsrc;
        private final TextView lang;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            langcard = view.findViewById(R.id.langcard);
            langsrc = view.findViewById(R.id.langsrc);
            langcard.setOnClickListener(this);
            lang = view.findViewById(R.id.lang);
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onCustomItemClick(clickedPosition);
        }
    }
}
