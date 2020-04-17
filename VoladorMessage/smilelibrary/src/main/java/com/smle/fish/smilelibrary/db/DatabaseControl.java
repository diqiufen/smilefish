package com.smle.fish.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursorDriver;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQuery;
import android.os.AsyncTask;
import android.util.Log;

import com.smle.fish.data.Result;
import com.smle.fish.model.db.DbBaseModel;
import com.smle.fish.model.db.FishUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;

/**
 * @PACKAGE_NAME：com.smle.fish.db
 * @user：yj
 * @date：2020/4/9
 * @版本：
 * @功能描述：
 */
public class DatabaseControl implements SQLiteDatabase.CursorFactory, Database.CreateDatabaseListeners {
    private String TAG = this.getClass().getCanonicalName();
    private Database database;
    private SQLiteDatabase databaseCreateTable;
    //读
    private SQLiteDatabase databaseRead;
    //写
    private SQLiteDatabase databaseWrite;
    private DbUtils dbUtils;
    private String databaseName = "";
    private static DatabaseControl databaseControl = null;
    private Context context;
    private int version = 1;
    private List<Class> tableList = new ArrayList<>();
    public final static int SUCCEED = 0;
    public final static int FAILED = -1;

    private DatabaseControl(Context context) {
        this.context = context;
        dbUtils = new DbUtils();
    }

    public static synchronized DatabaseControl getInstance(Context context) {
        if (databaseControl == null) {
            synchronized (DatabaseControl.class) {
                if (databaseControl == null) {
                    databaseControl = new DatabaseControl(context);
                }
            }
        }
        return databaseControl;
    }

    public void initDatabase(String databaseName, int version, List<Class> tableList) {
        this.tableList = tableList;
        database = new Database(context, databaseName, null, version, this);
        databaseRead = database.getReadableDatabase();
        databaseWrite = database.getWritableDatabase();
    }


    public void clear() {
        database.close();
        databaseWrite.close();
        databaseRead.close();
        databaseControl = null;
    }

    /**
     * 查询 根据需求整理返回的数据结构
     *
     * @param db
     * @param masterQuery
     * @param editTable
     * @param query
     * @return
     */
    @Override
    public Cursor newCursor(SQLiteDatabase db, SQLiteCursorDriver masterQuery, String editTable, SQLiteQuery query) {
        Log.d(TAG, "newCursor  " + query.toString());
        return null;
    }


    @Override
    public void onCreateDatabaseListener(SQLiteDatabase db) {
        this.databaseCreateTable = db;
        createTab();
    }

    /**
     * 创建表
     */
    public void createTab() {
        if (databaseCreateTable != null) {
            for (Class c : tableList) {
                Log.d(TAG, "createTab " + c.getSimpleName() + "成功");
                dbUtils.createTable(databaseCreateTable, c);
            }
        }
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public synchronized TableModel getTableModel() {
        return new TableModel();
    }


    public class TableModel implements Runnable {
        public final String whereClause = "whereClause";
        public final String whereArgs = "whereArgs";

        public TableModel() {

        }

        public <T extends DbBaseModel>long insertData(T module) {
            String tableName = getTableName(module);
            ContentValues contentValues = dbUtils.getFieldContentValues(module);
            long res = databaseWrite.insert(tableName, null, contentValues);
            return res;
        }

        public <T extends DbBaseModel> void insert(List<T> fishUserList, Observer<Integer> observer) {
            if (databaseWrite != null) {
                for (T module : fishUserList) {
                    long res = insertData(module);
                    if (res == FAILED) {
                        if (observer != null) {
                            observer.onChanged(FAILED);
                        }
                        return;
                    }
                }
                if (observer != null) {
                    observer.onChanged(SUCCEED);
                }
                Log.d(TAG, "插入成功");
            }
        }

        public  <T extends DbBaseModel> void insert(T module, Observer observer) {
            long res = insertData(module);
            if (observer != null) {
                observer.onChanged(res);
            }
        }

        public  <T extends DbBaseModel> void delete(T module, @NonNull Observer observer) {
            if (databaseWrite != null) {
                String tableName = dbUtils.getTableName(module.getClass());
                Map<String, Object> whereValue = dbUtils.getDeleteCondition(module, whereClause, whereArgs);
                Log.d(TAG, whereValue.get(whereClause).toString());
//                String whereClause = dbUtils.getWhereClause(module);
//                String[] whereArgs=dbUtils.getWhereArgs();
                Log.d(TAG, "delete FROM where " + whereClause);
                //这里将值拼接到whereClause里 所以whereArgs就不用传值了
                int res = databaseWrite.delete(tableName, whereValue.get(whereClause).toString(), (String[]) whereValue.get(whereArgs));
                observer.onChanged(res);
            } else {
                observer.onChanged(FAILED);
            }
        }

        public <T extends DbBaseModel> void deleteList(List<T> moduleList, @NonNull Observer observer) {
            if (databaseWrite != null) {
                for (T module : moduleList) {
                    String tableName = dbUtils.getTableName(module.getClass());
                    //注意 module 和module.getClass() 的区别
                    Map<String, Object> whereValue = dbUtils.getDeleteCondition(module, whereClause, whereArgs);
                    Log.d(TAG, whereValue.get(whereClause).toString());
                    //这里将值拼接到whereClause里 所以whereArgs就不用传值了
                    int res = databaseWrite.delete(tableName, whereValue.get(whereClause).toString(), (String[]) whereValue.get(whereArgs));
                    if (res == FAILED) {
                        observer.onChanged(FAILED);
                        break;
                    }
                }
                observer.onChanged(SUCCEED);
            } else {
                observer.onChanged(FAILED);
            }
        }

        /**
         * 更新某条数据的值
         *
         * @param module
         * @param observer
         */
        public <T extends DbBaseModel> void update(T module, @NonNull Observer observer) {
            if (databaseWrite != null) {
//                Map<String, Object> whereValue = dbUtils.getDeleteCondition(module, whereClause, whereArgs);
//                Map<String, Object> whereValue = dbUtils.getAccordingValue(module, whereClause, whereArgs);
                DbBaseModel dbBaseModel = module;
                String whereClauseString = "id=?";
                String whereArgsString = dbBaseModel.getId() + "";
                int res = databaseWrite.update(getTableName(module), getContentValues(module), whereClauseString, new String[]{whereArgsString});
                if (res == FAILED) {
                    observer.onChanged(FAILED);
                } else {
                    observer.onChanged(SUCCEED);
                }
            } else {
                observer.onChanged(FAILED);
            }
        }


        /**
         * 根据模型查询表
         *
         * @param module
         * @param observer
         */
        public void query(Class module, @NonNull Observer observer) throws Exception {
            if (databaseWrite != null) {
                String tableName = module.getSimpleName();
                String[] columns = null;
                //条件
                String selection = null;
                //条件值 没有传null
                String[] selectionArgs = null;
                String groupBy = null;
                String having = null;
                String orderBy = null;
                String sql = dbUtils.getQueryTableSql(tableName);
                //asc 升序 desc降序
                Cursor cursor = databaseWrite.rawQuery(sql, null);
//                Cursor cursor = databaseRead.query(tableName, columns, selection, selectionArgs, groupBy, having, orderBy);
                getCursorData(module, cursor, observer);
            }
        }

        /**
         * 根据模型查询表
         *
         * @param module
         * @param observer
         */
        public <T extends DbBaseModel> void query(T module, @NonNull Observer observer) throws Exception {
            if (databaseWrite != null) {
                String tableName = module.getClass().getSimpleName();
                String[] columns = null;
                //条件
                String selection = null;
                //条件值 没有传null
                String[] selectionArgs = null;
                String groupBy = null;
                String having = null;
                String orderBy = null;
                String sql = dbUtils.getQueryTableSql(tableName);
                //asc 升序 desc降序
                Cursor cursor = databaseWrite.rawQuery(sql, null);
//                Cursor cursor = databaseRead.query(tableName, columns, selection, selectionArgs, groupBy, having, orderBy);
                getCursorData(module.getClass(), cursor, observer);
            }
        }

        private <M extends DbBaseModel> void getCursorData(Class module, Cursor cursor, Observer observer) throws Exception {
            List<M> listValue = new ArrayList<>();
            if (cursor == null) {
                throw new Exception("查询错误");
            }
//            cursor.moveToFirst();
            Log.d(TAG, "getCount=" + cursor.getCount());
            while (cursor.moveToNext()) {
                listValue.add((M) dbUtils.getModule(module, cursor));
            }
            cursor.close();
            if (observer != null) {
                observer.onChanged(listValue);
            }
        }


        public void deleteTable(@NonNull Observer observer) {

        }

        public void query() {
            if (databaseRead != null) {
                Log.d(TAG, "createTab 成功");
            }
        }

        public <T extends DbBaseModel> String getTableName(T module) {
            String tableName = dbUtils.getTableName(module.getClass());
            return tableName;
        }

        @Override
        public void run() {

        }

    }

    /**
     * 根据对象 获取 ContentValues 数据
     *
     * @param module
     * @return
     */
    private ContentValues getContentValues(Object module) {
        ContentValues contentValues = dbUtils.getFieldContentValues(module);
        return contentValues;
    }

    class Abc extends AsyncTask {

        public Abc() {

        }

        @Override
        protected Object doInBackground(Object[] objects) {
            return null;
        }
    }

}
