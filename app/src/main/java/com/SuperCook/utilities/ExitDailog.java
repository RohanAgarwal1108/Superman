package com.SuperCook.utilities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.SuperCook.R;

public class ExitDailog extends DialogFragment {

    /**
     * To confirm if user wants to exit app
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.MyAlertDialogTheme);
        builder.setMessage("Are you sure you want to exit the app?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    getActivity().finishAffinity();
                })
                .setNegativeButton("No", ((dialog, which) -> {
                    dialog.dismiss();
                }));
        return builder.create();
    }

}
