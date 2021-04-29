package com.SuperCook.cookSelection;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.SuperCook.R;
import com.SuperCook.utilities.ExtraUtils;

import java.util.List;

public class Frame23Adapter1 extends RecyclerView.Adapter<Frame23Adapter1.ViewHolder> {
    private final List<String> mDataset;
    private final Context context;

    public Frame23Adapter1(Context context, List<String> dataset) {
        mDataset = dataset;
        this.context = context;
    }

    @NonNull
    @Override
    public Frame23Adapter1.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cancook_item, parent, false);
        Frame23Adapter1.ViewHolder viewHolder = new Frame23Adapter1.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull Frame23Adapter1.ViewHolder holder, int position) {
        String cancook = mDataset.get(position);
        holder.cuisinetxt.setText(cancook);
        int id = context.getResources().getIdentifier("drawable/" + ExtraUtils.nameFormatter(cancook), null, context.getPackageName());
        holder.foodimg.setImageResource(id == 0 ? R.drawable.ic_dishplaceholder : id);
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
        private final TextView cuisinetxt;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            foodimg = view.findViewById(R.id.foodimg);
            cuisinetxt = view.findViewById(R.id.cuisinetxt);
        }
    }
}
