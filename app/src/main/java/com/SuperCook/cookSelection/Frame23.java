package com.SuperCook.cookSelection;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.SuperCook.databinding.ActivityFrame23Binding;
import com.SuperCook.home.MyCooks;
import com.SuperCook.home.MyTrials;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class Frame23 extends AppCompatActivity implements View.OnClickListener {
    private ActivityFrame23Binding binding;
    private Bundle extras;
    private CookDetails cookDetails;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView recyclerView1;
    private RecyclerView.Adapter mAdapter1;
    private RecyclerView recyclerView2;
    private RecyclerView.Adapter mAdapter2;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.LayoutManager layoutManager1;
    private RecyclerView.LayoutManager layoutManager2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFrame23Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        extras = getIntent().getExtras();
        getDetailsFromPrevActivity();
        if (extras != null && extras.containsKey("normal")) {
            activityOpenNormally();
        } else {
            specialActivityOpen();
        }
        setupUI();
        binding.cross.setOnClickListener(this);
    }

    /**
     * To get cook details from previous activity
     */
    private void getDetailsFromPrevActivity() {
        if (extras != null && extras.containsKey("Mycooks")) {
            if (extras.getString("Mycooks").equals("true")) {
                cookDetails = MyCooks.cookDetails.get(extras.getInt("index"));
            } else {
                cookDetails = MyCooks.currentCook;
            }
        } else if (extras != null && extras.containsKey("MyTrials")) {
            if (extras.getString("MyTrials").equals("true")) {
                cookDetails = MyTrials.currentTrials.get(extras.getInt("index"));
            } else {
                cookDetails = MyTrials.cookDetails.get(extras.getInt("index"));
            }
        } else {
            cookDetails = frame21.cookDetails.get(extras.getInt("index"));
        }
    }

    /**
     * To set the recyclerview in the activity
     */
    private void setupUI() {
        String name = cookDetails.getName();
        binding.name.setText(name.indexOf(' ') == -1 ? name : name.substring(0, name.indexOf(' ')));
        binding.rate.setText(String.valueOf(cookDetails.getRating()));
        binding.background.setText(cookDetails.getBackground());

        recyclerView = binding.canspeak;
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new Frame23Adapter(this, cookDetails.getCanSpeak());
        recyclerView.setAdapter(mAdapter);

        recyclerView1 = binding.can1recycler;
        recyclerView1.setHasFixedSize(true);
        layoutManager1 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView1.setLayoutManager(layoutManager1);
        mAdapter1 = new Frame23Adapter1(this, cookDetails.getCuisine());
        recyclerView1.setAdapter(mAdapter1);

        recyclerView2 = binding.photos;
        recyclerView2.setHasFixedSize(true);
        layoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView2.setLayoutManager(layoutManager2);
        mAdapter2 = new Frame23Adapter2(this, cookDetails.getFoodPictureURL());
        recyclerView2.setAdapter(mAdapter2);
    }

    /**
     * To open activity without transition
     */
    private void activityOpenNormally() {
        ImageView imageView = binding.cookpic23;
        String imageUrl = cookDetails.getCookPic();
        Picasso.get()
                .load(imageUrl)
                .noFade()
                .into(imageView);
    }

    /**
     * To open activity with transition
     */
    private void specialActivityOpen() {
        supportPostponeEnterTransition();

        ImageView imageView = binding.cookpic23;

        String imageUrl = cookDetails.getCookPic();
        String imageTransitionName = extras.getString(frame21.EXTRA_ANIMAL_IMAGE_TRANSITION_NAME);
        imageView.setTransitionName(imageTransitionName);

        Picasso.get()
                .load(imageUrl)
                .noFade()
                .into(imageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        supportStartPostponedEnterTransition();
                    }

                    @Override
                    public void onError(Exception e) {
                        supportStartPostponedEnterTransition();
                    }
                });
    }

    @Override
    public void onClick(View v) {
        if (v == binding.cross) {
            this.onBackPressed();
        }
    }
}