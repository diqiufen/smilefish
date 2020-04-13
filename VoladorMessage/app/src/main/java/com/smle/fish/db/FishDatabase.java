package com.smle.fish.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.smle.fish.contrlo.FishIntent;
import com.smle.fish.model.db.FishUser;
import com.smle.fish.model.db.LatelyContacts;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;

/**
 * @PACKAGE_NAME：com.smle.fish.db
 * @user：yj
 * @date：2020/4/8
 * @版本：
 * @功能描述：SQLiteOpenHelper
 */
public class FishDatabase {

    private final String databaseName = "smileFish";
    private int version = 1;
    private Context context;


    public FishDatabase(Context context) {
        this.context = context;
    }

    public void init() {
        List<Class> classList = new ArrayList<>();
        classList.add(FishUser.class);
        classList.add(LatelyContacts.class);
        DatabaseControl.getInstance(context).initDatabase(databaseName, version, classList);
    }

    public void insertData(List<FishUser> fishUserList, Observer<Integer> observer) {
        DatabaseControl.getInstance(context).getTableModel().insert(fishUserList, observer);
//        DatabaseControl.getInstance(context).insertData(fishUserList,observer);
    }

    public void deleteTable(FishUser c, Observer<Integer> observer) {
        DatabaseControl.getInstance(context).getTableModel().delete(c, observer);
    }

    public void delete(FishUser c, Observer<Integer> observer) {
        DatabaseControl.getInstance(context).getTableModel().delete(c, observer);
    }

    public void deleteList(List<FishUser> c, Observer<Integer> observer) {
        DatabaseControl.getInstance(context).getTableModel().deleteList(c, observer);
    }

    public void update(FishUser fishUser, String[] accordingField, Observer<Integer> observer) {
        DatabaseControl.getInstance(context).getTableModel().update(fishUser, accordingField, observer);
    }
}
