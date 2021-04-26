package com.superman.common;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.superman.R;

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

/*
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        when (position) {
            1 -> return
            2 -> return
            3 -> view.setBackgroundColor(
                Color.rgb(
                    (0.3647058904 * 255).toInt(),
                    (0.06666667014 * 255).toInt(),
                    (0.9686274529 * 255).toInt()
                )
            )
            4 -> view.setBackgroundColor(
                Color.rgb(
                    255,
                    (0.3529352546 * 255).toInt(),
                    (0.2339158952 * 255).toInt()
                )
            )
            5 -> view.setBackgroundColor(
                Color.rgb(
                    (0.1215686277 * 255).toInt(),
                    (0.01176470611 * 255).toInt(),
                    (0.4235294163 * 255).toInt()
                )
            )
            6 -> view.setBackgroundColor(
                Color.rgb(
                    (0.3411764801 * 255).toInt(),
                    (0.6235294342 * 255).toInt(),
                    (0.1686274558 * 255).toInt()
                )
            )
            else -> view.setBackgroundColor(
                Color.rgb(
                    (Math.random() * 255).toInt(),
                    (Math.random() * 255).toInt(),
                    (Math.random() * 255).toInt()
                )
            )
        }

    }
}
 */
