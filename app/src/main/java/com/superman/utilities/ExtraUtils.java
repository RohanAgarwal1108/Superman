package com.superman.utilities;

import android.content.Context;
import android.widget.Toast;

public class ExtraUtils {
    private static Toast toast;

    public static void makeToast(Context context, String str) {
        if (toast != null) {
            toast.cancel();
        }
        toast = Toast.makeText(context, str, Toast.LENGTH_SHORT);
        toast.show();
    }
}
