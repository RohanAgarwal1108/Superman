package com.SuperCook.common;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.SuperCook.R;

public class PageFragment extends Fragment {
    int position = -1;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        position = getArguments().getInt("POSITION");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        int layoutId = (position == 1 ? R.layout.onboarding1 : position == 2 ? R.layout.onboarding2 : R.layout.onboarding3);
        return inflater.inflate(layoutId, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.textView2).setOnClickListener(v -> OnBoarding.skip());
        if (position == 3) {
            view.findViewById(R.id.next).setOnClickListener(v -> OnBoarding.skip());
        }
    }
}