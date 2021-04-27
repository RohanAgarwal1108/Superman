package com.superman.utilities;

import android.widget.ImageView;

import com.superman.cookSelection.CookDetails;

public interface CookItemClickListener1 {
    void onCookItemClick(int position, CookDetails cookDetails, ImageView cookpic, boolean isOngoing, boolean isClickDetail);
}

