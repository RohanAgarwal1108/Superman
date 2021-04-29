package com.SuperCook.utilities;

import android.widget.ImageView;

import com.SuperCook.cookSelection.CookDetails;

public interface CookItemClickListener1 {
    void onCookItemClick(int position, CookDetails cookDetails, ImageView cookpic, boolean isOngoing);
}

