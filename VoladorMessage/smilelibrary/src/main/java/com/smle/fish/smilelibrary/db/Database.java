package com.smle.fish.smilelibrary.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

/**
 * @PACKAGE_NAME：com.smle.fish.db
 * @user：yj
 * @date：2020/4/8
 * @版本：
 * @功能描述：SQLiteOpenHelper
 */
public class Database extends SQLiteOpenHelper {

    private String TAG = this.getClass().getCanonicalName();
    private CreateDatabaseListeners createDatabaseListeners;

    /**
     * @param context
     * @param name
     * @param factory
     * @param version
     * @param createDatabaseListeners
     */
    Database(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version, CreateDatabaseListeners createDatabaseListeners) {
        super(context, name, factory, version);
        this.createDatabaseListeners = createDatabaseListeners;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        if (createDatabaseListeners != null) {
            createDatabaseListeners.onCreateDatabaseListener(db);
        }
//        dbUtils.createTable(db,);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, "oldVersion=" + oldVersion + "  newVersion=" + newVersion);
        if (createDatabaseListeners != null) {
            createDatabaseListeners.onUpgradeDatabaseListener(db, oldVersion, newVersion);
        }
    }


    public interface CreateDatabaseListeners {
        void onCreateDatabaseListener(SQLiteDatabase db);

        void onUpgradeDatabaseListener(SQLiteDatabase db, int oldVersion, int newVersion);
    }


}
