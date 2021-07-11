package com.SuperCook.utilities;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.widget.ProgressBar;

public class MyProgressDialog {
    private ProgressDialog dialog;

    /**
     * To show the loading spinner on screen in case of network request
     *
     * @param context contains the activity on which the spinner needs to be displayed
     */
    public void showDialog(Context context) {
        dialog = ProgressDialog.show(context, null, null);
        ProgressBar spinner = new android.widget.ProgressBar(context, null, android.R.attr.progressBarStyle);
        spinner.getIndeterminateDrawable().setColorFilter(Color.parseColor("#000000"), android.graphics.PorterDuff.Mode.SRC_IN);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(spinner);
        dialog.setCancelable(false);
        dialog.show();
    }

    /**
     * TO remove the dialog from screen
     */
    public void dismissDialog() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }
}
