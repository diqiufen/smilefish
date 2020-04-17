package com.smle.fish.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.smle.fish.R;
import com.smle.fish.interfaces.InjectView;
import com.smle.fish.interfaces.OnListFragmentInteractionListener;
import com.smle.fish.interfaces.WindowInterface;
import com.smle.fish.smilelibrary.util.InitTool;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * @PACKAGE_NAME：com.smle.fish.ui
 * @user：yj
 * @date：2020/4/8
 * @版本：
 * @功能描述：
 */
public abstract class BaseFragment extends Fragment implements WindowInterface {

    protected View rootView;
    protected OnListFragmentInteractionListener mListener;
    protected String TAG = this.getClass().getCanonicalName();
    private Context context;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(getContentViewId(), container, false);
        initView();
        init();
        getData();
        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    protected void initView() {
        context = rootView.getContext();
        try {
            InitTool.init(this, InjectView.class, new InitTool.InitCallBack<InjectView>() {
                @Override
                public Object onCallBack(InjectView annotationObject) {
                    return rootView.findViewById(annotationObject.viewId());
                }
            });
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
