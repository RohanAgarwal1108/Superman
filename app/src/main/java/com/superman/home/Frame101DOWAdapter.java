package com.superman.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.superman.R;
import com.superman.utilities.CustomItemClickListener2;

import java.util.ArrayList;

public class Frame101DOWAdapter extends RecyclerView.Adapter<Frame101DOWAdapter.ViewHolder> {
    private final ArrayList<String> dates;
    private final CustomItemClickListener2 customItemClickListener;

    public Frame101DOWAdapter(CustomItemClickListener2 listener, ArrayList<String> dates) {
        customItemClickListener = listener;
        this.dates = dates;
    }

    @NonNull
    @Override
    public Frame101DOWAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.day_item, parent, false);
        Frame101DOWAdapter.ViewHolder viewHolder = new Frame101DOWAdapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull Frame101DOWAdapter.ViewHolder holder, int position) {
        String date = dates.get(position);
        holder.date.setText(date.substring(0, 2));
        holder.day.setText(date.substring(2));
    }

    @Override
    public int getItemCount() {
        if (dates == null) {
            return 0;
        } else {
            return dates.size();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final View view;
        private final TextView day;
        private final TextView date;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            day = view.findViewById(R.id.day);
            date = view.findViewById(R.id.date);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            customItemClickListener.onCustomItemClick(v);
        }
    }
}
