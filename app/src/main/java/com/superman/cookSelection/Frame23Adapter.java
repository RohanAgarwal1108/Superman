package com.superman.cookSelection;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.superman.R;
import com.superman.utilities.ExtraUtils;

import java.util.List;

public class Frame23Adapter extends RecyclerView.Adapter<Frame23Adapter.ViewHolder> {
    private final List<String> mDataset;
    private final Context context;

    public Frame23Adapter(Context context, List<String> dataset) {
        mDataset = dataset;
        this.context = context;
    }

    @NonNull
    @Override
    public Frame23Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.language_item1, parent, false);
        Frame23Adapter.ViewHolder viewHolder = new Frame23Adapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull Frame23Adapter.ViewHolder holder, int position) {
        String language = mDataset.get(position);
        //lang
        int id = context.getResources().getIdentifier("drawable/" + ExtraUtils.nameFormatter(language), null, context.getPackageName());
        holder.langsrc.setImageResource(id == 0 ? R.drawable.langdef : id);
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
        private final ImageView langsrc;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            langsrc = view.findViewById(R.id.langsrc);
        }
    }
}
