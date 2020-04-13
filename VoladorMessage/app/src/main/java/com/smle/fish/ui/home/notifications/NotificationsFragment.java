package com.smle.fish.ui.home.notifications;

import android.widget.TextView;

import com.smle.fish.R;
import com.smle.fish.ui.BaseFragment;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;


public class NotificationsFragment extends BaseFragment {

    private NotificationsViewModel notificationsViewModel;


    @Override
    public int getContentViewId() {
        return R.layout.fragment_notifications;
    }

    @Override
    public void init() {
        notificationsViewModel =
                ViewModelProviders.of(this).get(NotificationsViewModel.class);
        final TextView textView = rootView.findViewById(R.id.text_notifications);
        notificationsViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
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
