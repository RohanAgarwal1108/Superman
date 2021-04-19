package com.superman.utilities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.widget.Toast;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.superman.common.MainActivity;
import com.superman.home.Frame101;
import com.superman.home.UserProfile;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class ExtraUtils {
    private static Toast toast;
    public static Bitmap profilepic;
    private static StorageReference ppRef;

    public static void makeToast(Context context, String str) {
        if (toast != null) {
            toast.cancel();
        }
        toast = Toast.makeText(context, str, Toast.LENGTH_SHORT);
        toast.show();
    }

    public static void getProfilePic(Context context) {
        try {
            ppRef = FirebaseStorage.getInstance().getReference().child(getRefforImage(context));
            ppRef.getDownloadUrl().addOnSuccessListener(uri -> downloadProfilePic(uri.toString()));
        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
        }
    }

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
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        });
    }

    private static String getRefforImage(Context context) throws GeneralSecurityException, IOException {
        String str = MainActivity.getValue(context, MainActivity.ALIAS4);
        return "images/ProfilePic_" + str + ".jpeg";
    }


}
