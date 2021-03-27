package com.superman.home;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.github.islamkhsh.CardSliderAdapter;
import com.superman.R;

import java.util.ArrayList;

public class Frame101Adapter extends CardSliderAdapter<Frame101Adapter.ViewHolder> {
    private final ArrayList<CardsPOJO> cardsPOJOS;
    private final Context context;

    public Frame101Adapter(ArrayList<CardsPOJO> cardsPOJOS, Context context) {
        this.cardsPOJOS = cardsPOJOS;
        this.context = context;
    }

    @Override
    public void bindVH(Frame101Adapter.ViewHolder viewHolder, int i) {
        CardsPOJO cardPOJO = cardsPOJOS.get(i);
        viewHolder.title.setText(cardPOJO.getTitle());
        viewHolder.card.setCardBackgroundColor(Color.parseColor(cardPOJO.getCardColor()));
        viewHolder.details.setText(cardPOJO.getDetails());
    }

    @NonNull
    @Override
    public Frame101Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.frame101_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public int getItemCount() {
        if (cardsPOJOS == null) {
            return 0;
        } else {
            return cardsPOJOS.size();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View view;
        private final TextView title;
        private final TextView details;
        private final CardView card;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            title = view.findViewById(R.id.title);
            details = view.findViewById(R.id.details);
            card = view.findViewById(R.id.card);
        }
    }

}
