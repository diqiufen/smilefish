package com.smle.fish.ui.home.home;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.smle.fish.R;
import com.smle.fish.db.FishDatabase;
import com.smle.fish.interfaces.InjectView;
import com.smle.fish.interfaces.OnListFragmentInteractionListener;
import com.smle.fish.model.db.FishUser;
import com.smle.fish.ui.BaseFragment;
import com.smle.fish.ui.home.MainActivity;
import com.smle.fish.ui.home.home.dummy.DummyContent;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class ConversationFragment extends BaseFragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    @InjectView(viewId = R.id.list)
    private RecyclerView recyclerView;
    private ConversationRecyclerViewAdapter conversationRecyclerViewAdapter;
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ConversationFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static ConversationFragment newInstance(int columnCount) {
        ConversationFragment fragment = new ConversationFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getContentViewId() {
        return R.layout.fragment_conversation_list;
    }

    @Override
    public void init() {
        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
        setAdapter();
    }

    private void setAdapter(){
        if (mColumnCount <= 1) {
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), mColumnCount));
        }
        conversationRecyclerViewAdapter =new ConversationRecyclerViewAdapter(fishUserList,mListener);
        recyclerView.setAdapter(conversationRecyclerViewAdapter);
    }

    @Override
    public void getData() {
        quer();
    }

    List<FishUser> fishUserList = new ArrayList<>();

    public void quer() {
        try {
            FishDatabase.getInstance(getActivity()).query(new FishUser(), new Observer<List<FishUser>>() {
                @Override
                public void onChanged(List<FishUser> fishUserList) {
                    ConversationFragment.this.fishUserList.clear();
                    ConversationFragment.this.fishUserList.addAll(fishUserList);
                    conversationRecyclerViewAdapter.notifyDataSetChanged();
                    int i = 0;
                    for (FishUser fishUser : fishUserList) {
                        i++;
                        Log.d(TAG, i + "=" + fishUser.toString());
                    }
                }
            });
        } catch (Exception e) {
            Log.d(TAG, "" + e.getMessage());
        }

    }

}
