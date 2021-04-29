package com.SuperCook.home;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.SuperCook.R;
import com.SuperCook.utilities.CustomItemClickListener4;
import com.github.islamkhsh.CardSliderAdapter;
import com.sinaseyfi.advancedcardview.AdvancedCardView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Frame101Adapter extends CardSliderAdapter<Frame101Adapter.ViewHolder> {
    private final ArrayList<CardsPOJO> cardsPOJOS;
    private final Context context;
    private final CustomItemClickListener4 listener4;

    public Frame101Adapter(ArrayList<CardsPOJO> cardsPOJOS, Context context, CustomItemClickListener4 listener4) {
        this.cardsPOJOS = cardsPOJOS;
        this.context = context;
        this.listener4 = listener4;
    }

    @Override
    public void bindVH(Frame101Adapter.ViewHolder viewHolder, int i) {
        CardsPOJO cardPOJO = cardsPOJOS.get(i);
        viewHolder.title.setText(cardPOJO.getTitle());
        viewHolder.card.setCardBackgroundColor(Color.parseColor(cardPOJO.getCardColor()));
        viewHolder.details.setText(cardPOJO.getDetails());
        viewHolder.buttontxt.setTextColor(Color.parseColor(cardPOJO.getCardColor()));
        Picasso.get().load(cardPOJO.getIcon()).into(viewHolder.icon);
        if (cardPOJO.getCta() == 6) {
            viewHolder.buttonsupercard.setVisibility(View.GONE);
            viewHolder.buttoncard.setVisibility(View.GONE);
        } else {
            viewHolder.buttonsupercard.setVisibility(View.VISIBLE);
            viewHolder.buttoncard.setVisibility(View.VISIBLE);
        }

        if (cardPOJO.getCta() == 1) {
            viewHolder.buttontxt.setText("go");
        } else if (cardPOJO.getCta() == 2) {
            viewHolder.buttontxt.setText("more");
        } else if (cardPOJO.getCta() == 3) {
            viewHolder.buttontxt.setText("chat");
        } else if (cardPOJO.getCta() == 4) {
            viewHolder.buttontxt.setText("check out");
        } else {
            viewHolder.buttontxt.setText("order now");
        }
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

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final View view;
        private final TextView title;
        private final TextView details;
        private final CardView card;
        private final ImageView icon;
        private final AdvancedCardView buttonsupercard;
        private final TextView buttontxt;
        private final CardView buttoncard;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            title = view.findViewById(R.id.title);
            details = view.findViewById(R.id.details);
            card = view.findViewById(R.id.card);
            icon = view.findViewById(R.id.icon);
            buttonsupercard = view.findViewById(R.id.buttonsupercard);
            buttontxt = view.findViewById(R.id.buttontxt);
            buttoncard = view.findViewById(R.id.buttoncard);
            buttonsupercard.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            listener4.onCustomItemClick4(getAdapterPosition());
        }
    }

}
