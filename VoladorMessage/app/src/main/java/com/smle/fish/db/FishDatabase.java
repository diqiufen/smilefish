package com.smle.fish.db;

import android.content.Context;
import android.util.Log;

import com.smle.fish.model.TestModelA;
import com.smle.fish.model.db.FishUser;
import com.smle.fish.model.db.LatelyContacts;
import com.smle.fish.smilelibrary.db.DatabaseControl;
import com.smle.fish.smilelibrary.db.DbBaseModel;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.Observer;

/**
 * @PACKAGE_NAME：com.smle.fish.db
 * @user：yj
 * @date：2020/4/8
 * @版本：
 * @功能描述：SQLiteOpenHelper
 */
public class FishDatabase {

    private final String databaseName = "FishDatabase";
    private int version = 11;
    private Context context;
    private static FishDatabase fishDatabase = null;
    public DatabaseControl databaseControl;

    private FishDatabase(Context context) {
        this.context = context;
        databaseControl = new DatabaseControl(context, databaseName, version);
    }

    public static synchronized FishDatabase getInstance(Context context) {
        if (fishDatabase == null) {
            synchronized (DatabaseControl.class) {
                if (fishDatabase == null) {
                    fishDatabase = new FishDatabase(context);
                }
            }
        }
        return fishDatabase;
    }

    public void init() {
        List<Class> classList = new ArrayList<>();
        classList.add(FishUser.class);
        classList.add(LatelyContacts.class);
        List<Class> classListTwo = new ArrayList<>();
        classList.add(FishUser.class);
        classList.add(LatelyContacts.class);
//        databaseControl.setUpgradeTableList(classListTwo);
        databaseControl.initDatabase(classList);
        databaseControl.addTable(new TestModelA(), new Observer() {
            @Override
            public void onChanged(Object o) {
                Log.d("onChanged", "o=" + o);
            }
        });
    }

    public <T extends DbBaseModel> void insertData(List<T> fishUserList, Observer<Long> observer) {
        databaseControl.insert(fishUserList, observer);
//        DatabaseControl.getInstance(context).insertData(fishUserList,observer);
    }

    public <T extends DbBaseModel> void deleteTable(T t, Observer<Long> observer) {
        databaseControl.delete(t, observer);
    }

    public <T extends DbBaseModel> void delete(T t, Observer<Long> observer) {
        databaseControl.delete(t, observer);
    }

    public <T extends DbBaseModel> void deleteList(List<T> c, Observer<Long> observer) {
        databaseControl.delete(c, observer);
    }

    public <T extends DbBaseModel> void update(T t, Observer<Long> observer) {
        databaseControl.update(t, observer);
    }

    public <T extends DbBaseModel, M> void query(T c, Observer<List<M>> observer) {
        databaseControl.query(c, observer);
    }

}
