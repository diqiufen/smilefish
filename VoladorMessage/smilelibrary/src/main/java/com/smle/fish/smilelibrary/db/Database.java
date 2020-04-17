package com.smle.fish.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

/**
 * @PACKAGE_NAME：com.smle.fish.db
 * @user：yj
 * @date：2020/4/8
 * @版本：
 * @功能描述：SQLiteOpenHelper
 */
public class Database extends SQLiteOpenHelper {

    private CreateDatabaseListeners createDatabaseListeners;

    /**
     *
     * @param context
     * @param name
     * @param factory
     * @param version
     * @param createDatabaseListeners
     */
    Database(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version,CreateDatabaseListeners createDatabaseListeners) {
        super(context, name, factory, version);
        this.createDatabaseListeners=createDatabaseListeners;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        if(createDatabaseListeners!=null){
            createDatabaseListeners.onCreateDatabaseListener(db);
        }
//        dbUtils.createTable(db,);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


    public interface CreateDatabaseListeners{
        void onCreateDatabaseListener(SQLiteDatabase db);
    }
}
