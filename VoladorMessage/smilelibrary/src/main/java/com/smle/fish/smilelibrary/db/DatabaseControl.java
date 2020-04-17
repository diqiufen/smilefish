package com.smle.fish.smilelibrary.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursorDriver;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQuery;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;


import com.smle.fish.smilelibrary.util.ExecutorManage;

import java.util.ArrayList;
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
    public final static long SUCCEED = 0;
    public final static long FAILED = -1;
    private long timeMax = 10000;//更新数据库指令的等待最大时长
    private DbHandler dbHandler;

    public DatabaseControl(Context context, String databaseName, int version) {
        this.context = context;
        database = new Database(context, databaseName, null, version, this);
        dbUtils = new DbUtils();
        dbHandler = new DbHandler();
    }

//    public static synchronized DatabaseControl getInstance(Context context) {
//        if (databaseControl == null) {
//            synchronized (DatabaseControl.class) {
//                if (databaseControl == null) {
//                    databaseControl = new DatabaseControl(context);
//                }
//            }
//        }
//        return databaseControl;
//    }

    public void initDatabase(List<Class> tableList) {
        this.tableList = tableList;
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

    @Override
    public void onUpgradeDatabaseListener(SQLiteDatabase db, int oldVersion, int newVersion) {
        this.databaseCreateTable = db;
//        upgradeTable();
    }

    /**
     * 创建表
     */
    public void createTab() {
        if (databaseCreateTable != null) {
            for (Class c : tableList) {
                Log.d(TAG, "createTab " + c.getSimpleName() + "成功");
                databaseCreateTable.execSQL(dbUtils.createTable(c));
            }
        }
    }

//    public void setDeleteTableList(List<Class> deleteTableList) {
//        this.deleteTableList = deleteTableList;
//    }
//
//    public <T extends DbBaseModel> void deleteTable(List<Class> deleteTableList) {
//
//        if (databaseCreateTable != null) {
//            for (Class c : deleteTableList) {
//                databaseCreateTable.execSQL(dbUtils.deleteTable(c));
//            }
//            deleteTableList.clear();
//        }
//    }

//    public void upgradeVersion(int version) {
//        if (databaseCreateTable == null) {
//            database = new Database(context, databaseName, null, version, this);
//            databaseRead = database.getReadableDatabase();
//            databaseWrite = database.getWritableDatabase();
//        }
//    }

//    public void upgradeTable() {
//        if (databaseCreateTable != null) {
//            for (Class c : upgradeTableList) {
//                Log.d(TAG, "createTab " + c.getSimpleName() + "成功");
//                databaseCreateTable.execSQL(dbUtils.createTable(c));
//            }
//        }
//    }

    /**
     * 需要升级版本修改{@link DatabaseControl version} 否则无效
     * 新增表
     *
     * @param module
     * @param observer
     * @param <T>
     */
    public <T extends DbBaseModel> void addTable(T module, Observer observer) {
        ExecutorManage.singleTaskExecutor.submit(getAlterTable(Order.addTable, module, null, observer));
    }

    /**
     * 需要升级版本修改{@link DatabaseControl version} 否则无效
     * 新增表
     *
     * @param moduleList
     * @param observer
     * @param <T>
     */
    public <T extends DbBaseModel> void addTableList(List<T> moduleList, Observer observer) {
        ExecutorManage.singleTaskExecutor.submit(getAlterTable(Order.addTableList, null, moduleList, observer));

    }

    /**
     * 需要升级版本修改{@link DatabaseControl version} 否则无效
     * 删除表
     *
     * @param module
     * @param observer
     * @param <T>
     */
    public <T extends DbBaseModel> void deleteTable(T module, Observer observer) {
        ExecutorManage.singleTaskExecutor.submit(getAlterTable(Order.deleteTable, module, null, observer));
    }

    /**
     * 需要升级版本修改{@link DatabaseControl version} 否则无效
     * 删除表
     *
     * @param moduleList
     * @param observer
     * @param <T>
     */
    public <T extends DbBaseModel> void deleteTableList(List<T> moduleList, Observer observer) {
        ExecutorManage.singleTaskExecutor.submit(getAlterTable(Order.deleteTableList, null, moduleList, observer));
    }

    /**
     * 需要升级版本修改{@link DatabaseControl version} 否则无效
     * 在不更改原表数据的情况下修改表（目前只支持 新增对象属性 从而在表中新增字段）
     *
     * @param module
     * @param observer
     * @param <T>
     */
    public <T extends DbBaseModel> void alterTable(T module, Observer observer) {
        ExecutorManage.singleTaskExecutor.submit(getAlterTable(Order.alterTable, module, null, observer));
    }


//    public void setUpgradeTableList(List<Class> upgradeTableList) {
//        this.upgradeTableList = upgradeTableList;
//    }
//
//    public int getVersion() {
//        return version;
//    }
//
//    public void setVersion(int version) {
//        this.version = version;
//    }

    /**
     * 向 T 表中插入多条数据
     *
     * @param moduleList
     * @param observer
     * @param <T>
     */
    public <T extends DbBaseModel> void insert(List<T> moduleList, Observer<Long> observer) {
        ExecutorManage.singleTaskExecutor.submit(getTableModel(Order.insert, null, moduleList, observer));
    }

    /**
     * 向 T 表中插入单条数据
     *
     * @param module
     * @param observer
     * @param <T>
     */
    public <T extends DbBaseModel> void insert(T module, Observer<Long> observer) {
        ExecutorManage.singleTaskExecutor.submit(getTableModel(Order.insert, module, null, observer));
    }

    /**
     * 删除 T 表中的某条数据
     *
     * @param module   删除的数据对象 （根据 DbBaseModel 的id 删除 所以要删除的数据必须包含数据对象的id）
     * @param observer
     * @param <T>
     */
    public <T extends DbBaseModel> void delete(T module, Observer<Long> observer) {
        ExecutorManage.singleTaskExecutor.submit(getTableModel(Order.delete, module, null, observer));
    }

    /**
     * 删除 T 表中的多条数据
     *
     * @param moduleList 删除的数据对象 （根据 DbBaseModel 的id 删除 所以要删除的数据必须包含数据对象的id）
     * @param observer   返回状态值
     * @param <T>
     */
    public <T extends DbBaseModel> void delete(List<T> moduleList, Observer<Long> observer) {
        ExecutorManage.singleTaskExecutor.submit(getTableModel(Order.delete, null, moduleList, observer));
    }

    /**
     * 更新数据
     *
     * @param module   更新的对象 （根据 DbBaseModel 的id 更新 所以传入的对象必须存在id）
     * @param observer 返回状态值
     * @param <T>
     */
    public <T extends DbBaseModel> void update(T module, Observer<Long> observer) {
        ExecutorManage.singleTaskExecutor.submit(getTableModel(Order.update, module, null, observer));
    }

    /**
     * 同时更新多条数据
     *
     * @param moduleList 更新的对象 （根据 DbBaseModel 的id 更新 所以传入的对象必须存在id）
     * @param observer
     * @param <T>
     */
    public <T extends DbBaseModel> void update(List<T> moduleList, Observer<Long> observer) {
        ExecutorManage.singleTaskExecutor.submit(getTableModel(Order.update, null, moduleList, observer));
    }

    /**
     * 根据对象 查询相对于的数据表
     *
     * @param module
     * @param observer
     * @param <T>
     * @param <M>
     */
    public <T extends DbBaseModel, M> void query(T module, Observer<M> observer) {
        ExecutorManage.singleTaskExecutor.submit(getTableModel(Order.query, module, null, observer));
    }

    private synchronized <T extends DbBaseModel, M> TableModel getTableModel(Order order, T module, List<T> moduleList, Observer<M> observer) {
        // TODO: 2020/4/15
        return new TableModel(order, module, moduleList, observer);
    }

    private synchronized <T extends DbBaseModel, M> AlterTable getAlterTable(Order order, T module, List<T> moduleList, Observer<M> observer) {
        return new AlterTable(order, module, moduleList, observer);
    }


    class DbHandler<M> extends Handler {
        private Observer<M> observer;

        public void setValue(Observer<M> observer) {
            this.observer = observer;
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (observer != null) {
                observer.onChanged((M) msg.obj);
            }
        }
    }

    class TableModel<T extends DbBaseModel, M> implements Runnable {
        public final String whereClause = "whereClause";
        public final String whereArgs = "whereArgs";
        private Order order;//访问类型
        private T module;
        private List<T> moduleList;
        private Observer<M> observer;

//        private Handler handler = new Handler() {
//            @Override
//            public void handleMessage(@NonNull Message msg) {
//                super.handleMessage(msg);
//                if (observer != null) {
//                    switch (order) {
//                        case insert:
//                        case delete:
//                        case update:
//                            observer.onChanged((M) msg.obj);
//                            break;
//                        case query:
//                            observer.onChanged((M) msg.obj);
//                            break;
//                        default:
//                    }
//                }
//            }
//        };

        public TableModel(Order order, T module, List<T> moduleList, Observer<M> observer) {
            this.order = order;
            this.module = module;
            this.moduleList = moduleList;
            this.observer = observer;
        }

        @Override
        public void run() {
            switch (order) {
                case insert:
                    if (module != null) {
                        insert(module, (Observer<Long>) observer);
                    } else {
                        insert(moduleList, (Observer<Long>) observer);
                    }
                    break;
                case delete:
                    if (module != null) {
                        delete(module, observer);
                    } else {
                        deleteList(moduleList, observer);
                    }
                    break;
                case update:
                    if (module != null) {
                        update(module, observer);
                    } else {
                        deleteList(moduleList, observer);
                    }
                    break;
                case query:
                    try {
                        query(module, observer);
                    } catch (NullPointerException exception) {
                        exception.printStackTrace();
//                        //表示数据库中没有这列 这里新增列
//                        if (!exception.getMessage().equals("")) {
//                            addTableColumn(dbUtils.addTableColumn(module.getClass(), exception.getMessage()));
//                            try {
//                                databaseRead = database.getReadableDatabase();
//                                query(module, observer);
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                default:
            }
        }

        private <M> void sendMessage(Object object, Observer<M> observer) {
            Message message = new Message();
            message.obj = object;
            dbHandler.setValue(observer);
            dbHandler.sendMessage(message);
        }

        public <T extends DbBaseModel> long insertData(T module) {
            String tableName = getTableName(module);
            ContentValues contentValues = dbUtils.getFieldContentValues(module);
            long res = databaseWrite.insert(tableName, null, contentValues);
            return res;
        }

        public <T extends DbBaseModel> void insert(List<T> fishUserList, Observer<Long> observer) {
            if (databaseWrite != null) {
                for (T module : fishUserList) {
                    long res = insertData(module);
                    if (res == FAILED) {
                        sendMessage(FAILED, observer);
//                        if (observer != null) {
//                            observer.onChanged(FAILED);
//                        }
                        return;
                    }
                }
                sendMessage(SUCCEED, observer);
//                if (observer != null) {
//                    observer.onChanged(SUCCEED);
//                }
                Log.d(TAG, "插入成功");
            }
        }

        public <T extends DbBaseModel> void insert(T module, Observer<Long> observer) {
            long res = insertData(module);
            sendMessage(res, observer);
            if (observer != null) {
//                observer.onChanged(res);
            }
        }

        public <T extends DbBaseModel> void delete(T module, @NonNull Observer observer) {
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
         * 更新某条数据的值
         *
         * @param moduleList
         * @param observer
         */
        public <T extends DbBaseModel> void updateList(List<T> moduleList, @NonNull Observer observer) {
            if (databaseWrite != null) {
                for (T module : moduleList) {
                    DbBaseModel dbBaseModel = module;
                    String whereClauseString = "id=?";
                    String whereArgsString = dbBaseModel.getId() + "";
                    int res = databaseWrite.update(getTableName(module), getContentValues(module), whereClauseString, new String[]{whereArgsString});
                    if (res == FAILED) {
                        observer.onChanged(FAILED);
                        return;
                    } else {
                        observer.onChanged(SUCCEED);
                    }
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
                Cursor cursor = databaseRead.rawQuery(sql, null);
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
                Cursor cursor = databaseRead.rawQuery(sql, null);
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

        private <T extends DbBaseModel> String getTableName(T module) {
            String tableName = dbUtils.getTableName(module.getClass());
            return tableName;
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
    }

    /**
     * 更新表 需要升级数据库版本
     *
     * @param <T>
     */
    class AlterTable<T> implements Runnable {

        private Order order;//访问类型
        private T module;
        private List<T> moduleList;
        private Observer<Long> observer;
        //等待更新数据库 最晚10秒 十秒过后将停止任务等待
        private long timeMax = DatabaseControl.this.timeMax;
        private long runTime = 0;

        public AlterTable(Order order, T module, List<T> moduleList, Observer<Long> observer) {
            this.order = order;
            this.module = module;
            this.moduleList = moduleList;
            this.observer = observer;
        }

        @Override
        public void run() {
            while (true) {
                if (databaseCreateTable != null) {
                    switch (order) {
                        case addTable:
                            addTable(module);
                            break;
                        case alterTable:
                            alterTable(module);
                            break;
                        case deleteTable:
                            deleteTable(module);
                            break;
                        case addTableList:
                            addTableList(moduleList);
                            break;
                        case deleteTableList:
                            deleteTableList(moduleList);
                            break;
                    }
                    if (observer != null) {
                        observer.onChanged(SUCCEED);
                    }
                    break;
                } else {
                    if (timeMax <= runTime) {
                        if (observer != null) {
                            observer.onChanged(FAILED);
                        }
                        break;
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    runTime += 1000;
                }
            }
        }

        /**
         * 为包添加列 一次只能添加一列
         */
        public void addTable(T t) {
            if (databaseCreateTable != null) {
                Log.d(TAG,"addTable--sql="+dbUtils.createTable(t.getClass()));
                databaseCreateTable.execSQL(dbUtils.createTable(t.getClass()));
            }
        }

        public void addTableList(List<T> moduleList) {
            for (T t : moduleList) {
                addTable(t);
            }
        }

        /**
         * 删除表
         *
         * @param t
         */
        public void deleteTable(T t) {
            if (databaseCreateTable != null) {
                databaseCreateTable.execSQL(dbUtils.deleteTable(t.getClass()));
            }
        }

        /**
         * 删除多张表
         *
         * @param moduleList
         */
        public void deleteTableList(List<T> moduleList) {
            for (T t : moduleList) {
                deleteTable(t);
            }
        }

        /**
         * 表新增了列 后需要使用此方法
         *
         * @param t
         */
        public void alterTable(T t) {
            if (databaseCreateTable != null && databaseRead != null) {
                Class c = t.getClass();
                databaseCreateTable.execSQL(dbUtils.updateTableName(c.getSimpleName(), DbUtils.tempTable));
                databaseCreateTable.execSQL(dbUtils.createTable(c));
                String sql = dbUtils.getQueryTableSql(c.getSimpleName());
                Cursor cursor = databaseRead.rawQuery(sql, null);
                if (cursor != null) {
                    throw new NullPointerException("null cursor");
                }
                databaseWrite.execSQL(dbUtils.importTableDataSql(c.getSimpleName(), cursor.getColumnNames()));
                databaseCreateTable.execSQL(dbUtils.deleteTable(c));
            }
        }

    }

    public long getTimeMax() {
        return timeMax;
    }

    public void setTimeMax(long timeMax) {
        this.timeMax = timeMax;
    }
}
