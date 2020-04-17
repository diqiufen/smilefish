package com.smle.fish.db;

import android.content.Context;

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
public class ContactsDatabase {

    private final String databaseName = "smileFish";
    private int version = 1;
    private Context context;


//    public ContactsDatabase(Context context) {
//        this.context = context;
//    }
//
//    public void init() {
//        List<Class> classList = new ArrayList<>();
//        classList.add(FishUser.class);
//        classList.add(LatelyContacts.class);
//        DatabaseControl.getInstance(context).initDatabase(databaseName, version, classList);
//    }
//
//    public void insertData(List<FishUser> fishUserList, Observer<Long> observer) {
//        DatabaseControl.getInstance(context).insert(fishUserList, observer);
////        DatabaseControl.getInstance(context).insertData(fishUserList,observer);
//    }
//
//    public void deleteTable(FishUser c, Observer<Long> observer) {
//        DatabaseControl.getInstance(context).delete(c, observer);
//    }
//
//    public void delete(FishUser c, Observer<Long> observer) {
//        DatabaseControl.getInstance(context).delete(c, observer);
//    }
//
//    public void deleteList(List<FishUser> c, Observer<Long> observer) {
//        DatabaseControl.getInstance(context).delete(c, observer);
//    }
//
//    public void update(FishUser fishUser, Observer<Long> observer) {
//        DatabaseControl.getInstance(context).update(fishUser, observer);
//    }
//
//    public void query(FishUser c, Observer<List<FishUser>> observer) throws Exception {
//        DatabaseControl.getInstance(context).query(c, observer);
//    }
//
//    public <T extends DbBaseModel> void query(T module, Observer<List<FishUser>> observer) throws Exception {
//        DatabaseControl.getInstance(context).query(module, observer);
//    }

}
