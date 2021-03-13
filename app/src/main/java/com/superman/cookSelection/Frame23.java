package com.superman.cookSelection;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.superman.UserPreference.CookDetails;
import com.superman.databinding.ActivityFrame23Binding;

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
        cookDetails = frame21.cookDetails.get(extras.getInt("index"));
        if (extras.containsKey("normal")) {
            activityOpenNormally();
        } else {
            specialActivityOpen();
        }
        setupUI();

        binding.cross.setOnClickListener(this);
        binding.next23.setOnClickListener(this);
    }

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

    private void activityOpenNormally() {
        ImageView imageView = binding.cookpic23;
        String imageUrl = cookDetails.getCookPic();
        Picasso.get()
                .load(imageUrl)
                .noFade()
                .into(imageView);
    }

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
        if (v == binding.cross || v == binding.next23) {
            this.onBackPressed();
        }
    }
}