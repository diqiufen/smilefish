package com.smle.fish.ui.home;

import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.smle.fish.R;
import com.smle.fish.db.FishDatabase;
import com.smle.fish.interfaces.OnListFragmentInteractionListener;
import com.smle.fish.model.db.FishUser;
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

    FishDatabase fishDatabase = new FishDatabase(this);

    @Override
    public int getContentViewId() {
        return R.layout.activity_main;
    }

    @Override
    public void init() {
        fishDatabase.init();
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
        navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.isChecked() && item.getItemId() == R.id.navigation_home) {
                    getData();
                }else {
                    update();
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
            fishUser.setNickname("nickName" + i);
            fishUser.setPassWord("123456" + i);
            fishUserList.add(fishUser);
        }
        fishDatabase.deleteList(fishUserList, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                Log.d(TAG, "delete 状态=" + integer);
            }
        });
        fishDatabase.insertData(fishUserList, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                Toast.makeText(MainActivity.this, "返回值=" + integer, Toast.LENGTH_LONG).show();
            }
        });

    }

    private void update() {
        fishDatabase.update(fishUserList.get(5), new String[]{"Nickname"}, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {

            }
        });
    }

    @Override
    public void onListFragmentInteraction(Object o, int index) {

    }
}
