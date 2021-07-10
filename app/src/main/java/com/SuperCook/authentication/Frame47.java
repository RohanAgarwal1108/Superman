package com.SuperCook.authentication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.SuperCook.UserPreference.Frame28;
import com.SuperCook.common.MainActivity;
import com.SuperCook.common.Reconnect;
import com.SuperCook.databinding.ActivityFrame47Binding;
import com.SuperCook.utilities.ExtraUtils;
import com.SuperCook.utilities.KeyboardUtil;
import com.SuperCook.utilities.MyProgressDialog;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.functions.FirebaseFunctionsException;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.Map;

public class Frame47 extends AppCompatActivity implements View.OnClickListener {
    private static final int RC_PHOTO_PICKER = 1;
    private final String[] states = {
            "Andhra Pradesh             ", "Arunachal Pradesh          ", "Assam                      ",
            "Bihar                      ", "Chandigarh                 ", "Chhattisgarh               ", "Daman & Diu                ",
            "Delhi                      ", "Goa                        ", "Gujarat                    ", "Haryana                    ",
            "Himachal Pradesh           ", "Jammu & Kashmir            ", "Jharkhand                  ", "Karnataka                  ",
            "Kerala                     ", "Ladakh                     ", "Lakshadweep                ", "Madhya Pradesh             ",
            "Maharashtra                ", "Manipur                    ", "Meghalaya                  ", "Mizoram                    ",
            "Nagaland                   ", "Odisha                     ", "Puducherry                 ", "Punjab                     ",
            "Rajasthan                  ", "Sikkim                     ", "Tamil Nadu                 ", "Telangana                  ",
            "Tripura                    ", "Uttarakhand                ", "Uttar Pradesh              ", "West Bengal                "};
    Uri selectedImageUri;
    StorageReference ppRef;
    byte[] imagebytes;
    String photourl;
    Toast toast;
    private ActivityFrame47Binding binding;
    private Bitmap bitmap;
    private MyProgressDialog myProgressDialog;
    private String fcmToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFrame47Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ppRef = FirebaseStorage.getInstance().getReference().child(getRefforImage());
        setListeners();
        setSpinner();
        binding.state.setText("");
    }

    /**
     * To get the root reference of storage where image will be stored
     *
     * @return
     */
    private String getRefforImage() {
        String str = null;
        try {
            str = MainActivity.getValue(getApplicationContext(), MainActivity.ALIAS_UID);
        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
            ExtraUtils.makeToast(Frame47.this, "An Error occurred! Please try again later.");
            finishAffinity();
        }
        return "images/ProfilePic_" + str + ".jpeg";
    }

    /**
     * To set the state choose spinner
     */
    private void setSpinner() {
        ArrayAdapter ad = new ArrayAdapter(this, android.R.layout.simple_spinner_item, states) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                Typeface externalFont = Typeface.createFromAsset(getAssets(), "fonts/nb.otf");
                ((TextView) v).setTypeface(externalFont);
                return v;
            }

            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View v = super.getDropDownView(position, convertView, parent);
                Typeface externalFont = Typeface.createFromAsset(getAssets(), "fonts/nb.otf");
                ((TextView) v).setTypeface(externalFont);
                return v;
            }
        };
        ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinner.setAdapter(ad);
        binding.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                binding.state.setText(states[position].trim());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    /**
     * To set listeners for clickables on screen
     */
    private void setListeners() {
        binding.camcard.setOnClickListener(this);
        binding.photocard.setOnClickListener(this);
        binding.wherecard.setOnClickListener(this);
        binding.next47.setOnClickListener(this);
        binding.back47.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == binding.photocard || v == binding.camcard) {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_PICK);
            startActivityForResult(Intent.createChooser(intent, "Complete action using"), RC_PHOTO_PICKER);
        } else if (v == binding.wherecard) {
            binding.spinner.performClick();
        } else if (isDetailFilled() && v == binding.next47) {
            postUserImage();
        } else if (v == binding.back47) {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(Frame47.this, Frame39.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }

    /**
     * To check if details are correctly filled in fields
     *
     * @return
     */
    private boolean isDetailFilled() {
        if (binding.nameedit.getText().toString().isEmpty()) {
            ExtraUtils.makeToast(Frame47.this, "Please enter your name!");
            return false;
        } else if (binding.city.getText().toString().isEmpty()) {
            ExtraUtils.makeToast(Frame47.this, "Please enter you city!");
            return false;
        } else if (selectedImageUri == null) {
            ExtraUtils.makeToast(Frame47.this, "Image is required. You can cat fish!");
            return false;
        }
        return true;
    }

    /**
     * To post user image to firebase
     */
    private void postUserImage() {
        myProgressDialog = new MyProgressDialog();
        myProgressDialog.showDialog(this);
        UploadTask uploadTask = ppRef.putBytes(imagebytes);
        uploadTask.addOnFailureListener(exception -> {
            myProgressDialog.dismissDialog();
            Intent intent = new Intent(Frame47.this, Reconnect.class);
            startActivityForResult(intent, 2);
        }).addOnSuccessListener(taskSnapshot -> ppRef.getDownloadUrl()
                .addOnSuccessListener(uri -> {
                    photourl = uri.toString();
                    getFcmToken();
                }));
    }

    /**
     * To get firebase cloud messaging token for user
     */
    private void getFcmToken() {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        myProgressDialog.dismissDialog();
                        ExtraUtils.makeToast(Frame47.this, "Please check your internet connection and try again later!");
                        return;
                    }
                    fcmToken = task.getResult();
                    createUser();
                });
    }

    /**
     * To create user in firebase
     */
    private void createUser() {
        try {
            registerUser()
                    .addOnCompleteListener(task -> {
                        myProgressDialog.dismissDialog();
                        if (!task.isSuccessful()) {
                            Exception e = task.getException();
                            if (e instanceof FirebaseFunctionsException) {
                                FirebaseFunctionsException ffe = (FirebaseFunctionsException) e;
                                FirebaseFunctionsException.Code code = ffe.getCode();
                            }
                            Intent intent = new Intent(Frame47.this, Reconnect.class);
                            startActivityForResult(intent, 3);
                        } else {
                            try {
                                MainActivity.removeValue(getApplicationContext(), new String[]{MainActivity.ALIAS_PHONENUMBER});
                                MainActivity.putValues(MainActivity.ALIAS_STATUS, "midpref", getApplicationContext());
                                MainActivity.putValues(MainActivity.ALIAS_NAME, binding.nameedit.getText().toString().trim(), getApplicationContext());
                            } catch (GeneralSecurityException | IOException e) {
                                e.printStackTrace();
                            }
                            Intent intent = new Intent(Frame47.this, Frame28.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }
                    });
        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
            myProgressDialog.dismissDialog();
            ExtraUtils.makeToast(Frame47.this, "An Error occurred! Please try later.");
        }
    }

    /**
     * To call the cloud function to save data in firebase
     *
     * @return
     * @throws GeneralSecurityException
     * @throws IOException
     */
    private Task<HashMap<String, Object>> registerUser() throws GeneralSecurityException, IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("pictureUrl", photourl);
        data.put("name", binding.nameedit.getText().toString());
        data.put("phoneNo", MainActivity.getValue(getApplicationContext(), MainActivity.ALIAS_PHONENUMBER));
        data.put("from", binding.state.getText().toString());
        data.put("fcmToken", fcmToken);
        data.put("uid", MainActivity.getValue(getApplicationContext(), MainActivity.ALIAS_UID));
        data.put("preferences", false);
        data.put("city", binding.city.getText().toString());
        return MainActivity.mFunctions
                .getHttpsCallable("registerUser")
                .call(data)
                .continueWith(task -> (HashMap<String, Object>) task.getResult().getData());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_PHOTO_PICKER && resultCode == RESULT_OK) {
            selectedImageUri = data.getData();
            bitmap = getBitmap();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 20, out);
            imagebytes = out.toByteArray();
            bitmap = BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));
            binding.dp.setImageBitmap(bitmap);
        } else if (requestCode == 2 && requestCode == RESULT_OK) {
            postUserImage();
        } else if (requestCode == 3 && resultCode == RESULT_OK) {
            myProgressDialog.showDialog(this);
            createUser();
        }
    }

    /**
     * To get the bitmap from Uri provided by image picker intent
     */
    private Bitmap getBitmap() {
        try {
            return MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        KeyboardUtil keyboardUtil = new KeyboardUtil(this, ev);
        keyboardUtil.touchEvent();
        return super.dispatchTouchEvent(ev);
    }
}