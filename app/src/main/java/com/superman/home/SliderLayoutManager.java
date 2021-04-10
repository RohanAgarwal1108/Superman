package com.superman.home;

import android.content.Context;
import android.view.View;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.superman.R;
import com.superman.utilities.CustomItemClickListener;
import com.superman.utilities.ScreenUtils;

public class SliderLayoutManager extends LinearLayoutManager {

    private final CustomItemClickListener customItemClickListener;
    private final Context context;
    private RecyclerView recyclerView;

    public SliderLayoutManager(Context context, int orientation, boolean reverseLayout, CustomItemClickListener listener) {
        super(context, orientation, reverseLayout);
        customItemClickListener = listener;
        this.context = context;
    }

    @Override
    public void onAttachedToWindow(RecyclerView view) {
        super.onAttachedToWindow(view);
        recyclerView = view;
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        super.onLayoutChildren(recycler, state);
        colorChanger();
    }

    private void colorChanger() {
        int selectedLeft = ScreenUtils.Companion.dpToPx(context, 110);
        int selectedRight = ScreenUtils.Companion.dpToPx(context, 140);
        for (int i = 0; i < getChildCount(); i++) {
            // Calculating the distance of the child from the center
            View child = getChildAt(i);
            float childMid = (child.getLeft() + child.getRight()) / 2;

            if (childMid > selectedLeft && childMid < selectedRight) {
                ((CardView) child).setCardBackgroundColor(context.getColor(R.color.yellow));
            } else {
                ((CardView) child).setCardBackgroundColor(context.getColor(R.color.white));
            }
        }
    }

    @Override
    public int scrollHorizontallyBy(int dx, RecyclerView.Recycler recycler, RecyclerView.State state) {
        colorChanger();
        return super.scrollHorizontallyBy(dx, recycler, state);
    }

    @Override
    public void onScrollStateChanged(int state) {
        if (state == RecyclerView.SCROLL_STATE_IDLE) {
            int recyclerViewCenterX = getRecyclerViewCenterX();
            int minDistance = recyclerView.getWidth();
            int position = -1;
            for (int i = 0; i < recyclerView.getChildCount(); i++) {
                View child = recyclerView.getChildAt(i);
                int childCenterX = getDecoratedLeft(child) + (getDecoratedRight(child) - getDecoratedLeft(child)) / 2;
                int childDistanceFromCenter = Math.abs(childCenterX - recyclerViewCenterX);
                if (childDistanceFromCenter < minDistance) {
                    minDistance = childDistanceFromCenter;
                    position = recyclerView.getChildLayoutPosition(child);
                }
            }
            customItemClickListener.onCustomItemClick(position);
        }
        super.onScrollStateChanged(state);
    }

    private int getRecyclerViewCenterX() {
        return ScreenUtils.Companion.dpToPx(context, 126) + recyclerView.getLeft();
    }
}
