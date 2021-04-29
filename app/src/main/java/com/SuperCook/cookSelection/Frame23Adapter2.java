package com.SuperCook.cookSelection;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.SuperCook.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class Frame23Adapter2 extends RecyclerView.Adapter<Frame23Adapter2.ViewHolder> {
    private final List<String> mDataset;
    private final Context context;

    public Frame23Adapter2(Context context, List<String> dataset) {
        mDataset = dataset;
        this.context = context;
    }

    @NonNull
    @Override
    public Frame23Adapter2.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cookfood_item, parent, false);
        Frame23Adapter2.ViewHolder viewHolder = new Frame23Adapter2.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull Frame23Adapter2.ViewHolder holder, int position) {
        String cookfood = mDataset.get(position);
        Picasso.get().load(cookfood).placeholder(R.drawable.ic_cookfood_placeholder).into(holder.foodimg);
    }

    @Override
    public int getItemCount() {
        if (mDataset == null) {
            return 0;
        } else {
            return mDataset.size();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View view;
        private final ImageView foodimg;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            foodimg = view.findViewById(R.id.foodimg);
        }
    }
}
