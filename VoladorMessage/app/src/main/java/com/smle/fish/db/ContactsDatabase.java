package com.smle.fish.db;

import android.content.Context;

import com.smle.fish.smilelibrary.db.DbBaseModel;
import com.smle.fish.model.db.FishUser;
import com.smle.fish.model.db.LatelyContacts;
import com.smle.fish.smilelibrary.db.DatabaseControl;

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

    public void update(FishUser fishUser, Observer<Integer> observer) {
        DatabaseControl.getInstance(context).getTableModel().update(fishUser, observer);
    }

    public void query(Class c, Observer<List<FishUser>> observer) throws Exception {
        DatabaseControl.getInstance(context).getTableModel().query(c, observer);
    }

    public <T extends DbBaseModel> void query(T module, Observer<List<FishUser>> observer) throws Exception {
        DatabaseControl.getInstance(context).getTableModel().query(module, observer);
    }

}
