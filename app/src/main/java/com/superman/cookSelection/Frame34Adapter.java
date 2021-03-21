package com.superman.cookSelection;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.superman.R;
import com.superman.Utilities.CustomItemClickListener;

import java.util.ArrayList;

public class Frame34Adapter extends RecyclerView.Adapter<Frame34Adapter.ViewHolder> {
    private final ArrayList<String> mDataset;
    private final CustomItemClickListener customItemClickListener;

    public Frame34Adapter(ArrayList<String> finalizedItems, CustomItemClickListener listener) {
        mDataset = finalizedItems;
        customItemClickListener = listener;
    }

    @NonNull
    @Override
    public Frame34Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                //todo
                .inflate(R.layout.frame34_item, parent, false);
        Frame34Adapter.ViewHolder viewHolder = new Frame34Adapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull Frame34Adapter.ViewHolder holder, int position) {
        String finalizedstring = mDataset.get(position);
        holder.finalizedtxt.setText(finalizedstring);
        //todo
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
        private final ImageView finalizedimg;
        private final TextView finalizedtxt;
        private final LinearLayout finalizedItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            finalizedimg = view.findViewById(R.id.finalizedimg);
            finalizedtxt = view.findViewById(R.id.finalizedtxt);
            finalizedItem = view.findViewById(R.id.finalizeditem);
            finalizedItem.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            customItemClickListener.onCustomItemClick(0);
        }
    }
}
