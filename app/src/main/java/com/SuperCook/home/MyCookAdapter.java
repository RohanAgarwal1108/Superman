package com.SuperCook.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.SuperCook.R;
import com.SuperCook.cookSelection.CookDetails;
import com.SuperCook.utilities.CookItemClickListener;
import com.SuperCook.utilities.DateFormatter;
import com.sinaseyfi.advancedcardview.AdvancedCardView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;

public class MyCookAdapter extends RecyclerView.Adapter<MyCookAdapter.ViewHolder> {
    private final ArrayList<CookDetails> cookDetails;
    private final CookItemClickListener listener;

    public MyCookAdapter(ArrayList<CookDetails> cookDetails, CookItemClickListener listener) {
        this.cookDetails = cookDetails;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyCookAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.previouscook_item, parent, false);
        MyCookAdapter.ViewHolder viewHolder = new MyCookAdapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyCookAdapter.ViewHolder holder, int position) {
        CookDetails cookDetail = cookDetails.get(position);
        holder.name.setText(cookDetail.getName());
        String duration = "";
        long hire = cookDetail.getHiredate();
        long fire = cookDetail.getFiredate();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(hire);
        duration += DateFormatter.formateDateFromstring("EEE MMM dd HH:mm:ss z yyyy", "MMM dd", calendar.getTime().toString());
        calendar.setTimeInMillis(fire);
        duration += " - ";
        duration += DateFormatter.formateDateFromstring("EEE MMM dd HH:mm:ss z yyyy", "MMM dd", calendar.getTime().toString());
        holder.time.setText(duration);
        Picasso.get().load(cookDetail.getCookPic()).placeholder(R.drawable.ic_cook_def).into(holder.previmg);
        ViewCompat.setTransitionName(holder.previmg, cookDetail.getCookID());

        holder.prevcook.setOnClickListener(v -> listener.onCookItemClick(holder.getAdapterPosition(), cookDetail, holder.previmg));
    }

    @Override
    public int getItemCount() {
        if (cookDetails == null) {
            return 0;
        } else {
            return cookDetails.size();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View view;
        public final ImageView previmg;
        public final TextView name;
        public final TextView time;
        public final AdvancedCardView prevcook;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            prevcook = view.findViewById(R.id.prevcook);
            previmg = view.findViewById(R.id.previmg);
            name = view.findViewById(R.id.cookname);
            time = view.findViewById(R.id.time);
        }
    }
}
