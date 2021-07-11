package com.SuperCook.utilities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.widget.Toast;

import com.SuperCook.common.MainActivity;
import com.SuperCook.common.Reconnect;
import com.SuperCook.home.Frame101;
import com.SuperCook.home.UserProfile;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class ExtraUtils {
    private static Toast toast;
    public static Bitmap profilepic;
    private static int flag = 0;

    /**
     * To make toast in the app
     *
     * @param str the message to be displayed in toast
     */
    public static void makeToast(Context context, String str) {
        if (toast != null) {
            toast.cancel();
        }
        toast = Toast.makeText(context, str, Toast.LENGTH_SHORT);
        toast.show();
    }

    /**
     * To get the user profile picture download URL from firebase
     */
    public static void getProfilePic(Context context) {
        if (flag == 0) {
            try {
                flag = 1;
                StorageReference ppRef = FirebaseStorage.getInstance().getReference().child(getRefforImage(context));
                ppRef.getDownloadUrl().addOnSuccessListener(uri -> downloadProfilePic(uri.toString()));
            } catch (GeneralSecurityException | IOException e) {
                flag = 0;
                e.printStackTrace();
            }
        }
    }

    /**
     * To download the profile picture from URL
     *
     * @param url gives the url from which picture os downloaded
     */
    private static void downloadProfilePic(String url) {
        Picasso.get().load(url).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                profilepic = bitmap;
                if (Frame101.binding != null) {
                    Frame101.binding.ppimg.setImageBitmap(profilepic);
                }
                if (UserProfile.binding != null) {
                    UserProfile.binding.userimg.setImageBitmap(profilepic);
                }
                flag=0;
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                flag = 0;
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        });
    }

    /**
     * To get the reference of profile picture in firebase storage
     */
    private static String getRefforImage(Context context) throws GeneralSecurityException, IOException {
        String str = MainActivity.getValue(context, MainActivity.ALIAS_UID);
        return "images/ProfilePic_" + str + ".jpeg";
    }

    /**
     * Format the name of resource to find easily in resources folder
     *
     * @param str contains the string to be formatted
     * @return the formatted resource name
     */
    public static String nameFormatter(String str) {
        str = str.toLowerCase();
        str = str.replaceAll("\\s", "");
        return str;
    }

    /**
     * To redirect to whatsapp
     *
     * @param str contains the text that should be pre-written on opening whatsapp
     */
    public static void openWhatsapp(String str, Context context) {
        String url = "https://api.whatsapp.com/send?phone=+917972803790&text=" + str;
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        i.setPackage("com.whatsapp");
        if (i.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(i);
        } else {
            i.setPackage(null);
        }
        context.startActivity(i);
    }

    /**
     * To redirect to reconnect screen in case of error
     *
     * @param context contains the context of activity from which user will be redirected
     */
    public static void gotoReconnectScreen(Context context) {
        Intent intent = new Intent(context, Reconnect.class);
        context.startActivity(intent);
    }
}
