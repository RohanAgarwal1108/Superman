package com.superman.common;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class Adapter extends FragmentPagerAdapter {
    private final int total = 3;
    private final ArrayList<PageFragment> data = new ArrayList<>(total);

    public Adapter(FragmentManager fragmentManager) {
        super(fragmentManager);
        for (int i = 0; i < total; i++) {
            PageFragment fragment = new PageFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("POSITION", i + 1);
            fragment.setArguments(bundle);
            data.add(fragment);
        }
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return data.get(position);
    }

    @Override
    public int getCount() {
        return total;
    }
}