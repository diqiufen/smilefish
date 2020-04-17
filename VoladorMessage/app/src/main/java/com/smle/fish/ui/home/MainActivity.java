package com.smle.fish.ui.home;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.smle.fish.R;
import com.smle.fish.db.FishDatabase;
import com.smle.fish.interfaces.InjectView;
import com.smle.fish.interfaces.OnListFragmentInteractionListener;
import com.smle.fish.model.db.FishUser;
import com.smle.fish.smilelibrary.util.InitTool;
import com.smle.fish.ui.BaseActivity;
import com.smle.fish.ui.home.home.ConversationFragment;
import com.smle.fish.ui.home.home.dummy.DummyContent;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends BaseActivity implements OnListFragmentInteractionListener {

    FishDatabase fishDatabase ;
    @InjectView(viewId = R.id.nav_view)
    BottomNavigationView navView;

    @Override
    public int getContentViewId() {
        return R.layout.activity_main;
    }

    @Override
    public void init() {
        fishDatabase = FishDatabase.getInstance(this);
//        fishDatabase.init();
//        navView = findViewById(R.id.nav_view);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
        navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.navigation_home) {
                    insertData();
                } else if (item.getItemId() == R.id.navigation_dashboard) {
//                    fishDatabase.deleteList(fishUserList, new Observer<Integer>() {
//                        @Override
//                        public void onChanged(Integer integer) {
//                            Log.d(TAG, "delete 状态=" + integer);
//                        }
//                    });
                    update();
                } else {

//                    update();
                    query();
                }
                return false;
            }
        });
    }

    List<FishUser> fishUserList = new ArrayList<>();

    @Override
    public void getData() {
        fishUserList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            FishUser fishUser = new FishUser();
            fishUser.setName("name" + i);
            fishUser.setAge((i + 1) * 10);
            fishUser.setSex(i % 2);
            fishUser.setNickName("nickName" + i);
            fishUser.setPassWord("123456" + i);
            fishUserList.add(fishUser);
        }

//        insertData();
    }

    private void insertData() {

        fishDatabase.insertData(fishUserList, new Observer<Long>() {
            @Override
            public void onChanged(Long integer) {
                Toast.makeText(MainActivity.this, "返回值=" + integer, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void query() {
        try {
            fishDatabase.query(new FishUser(), new Observer<List<FishUser>>() {
                @Override
                public void onChanged(List<FishUser> fishUserList) {
                    MainActivity.this.fishUserList = fishUserList;
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

    private void update() {
        fishUserList.get(5).setName("我是被改了的那个对象的NAME");
        fishUserList.get(5).setPassWord("12345678");
        fishDatabase.update(fishUserList.get(5), new Observer<Long>() {
            @Override
            public void onChanged(Long integer) {
                Log.d(TAG, "更新 状态=" + integer);
            }
        });
    }

    @Override
    public void onListFragmentInteraction(Object o, int index) {

    }
}
