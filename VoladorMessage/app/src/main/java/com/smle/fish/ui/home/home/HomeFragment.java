package com.smle.fish.ui.home.home;

import android.widget.TextView;

import com.smle.fish.R;
import com.smle.fish.ui.BaseFragment;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;


public class HomeFragment extends BaseFragment {

    private HomeViewModel homeViewModel;

    @Override
    public int getContentViewId() {
        return R.layout.fragment_home;
    }

    @Override
    public void init() {
        homeViewModel = getDefaultViewModelProviderFactory().create(HomeViewModel.class);
        final TextView textView = rootView.findViewById(R.id.text_home);
        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
    }

    @Override
    public void getData() {
        homeViewModel.initData();
    }
}
