package com.smle.fish.ui.home.dashboard;

import android.widget.TextView;

import com.smle.fish.R;
import com.smle.fish.ui.BaseFragment;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;


public class DashboardFragment extends BaseFragment {

    private DashboardViewModel dashboardViewModel;


    @Override
    public int getContentViewId() {
        return R.layout.fragment_dashboard;
    }

    @Override
    public void init() {
        dashboardViewModel =
                ViewModelProviders.of(this).get(DashboardViewModel.class);
        final TextView textView = rootView.findViewById(R.id.text_dashboard);
        dashboardViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
    }

    @Override
    public void getData() {

    }
}
