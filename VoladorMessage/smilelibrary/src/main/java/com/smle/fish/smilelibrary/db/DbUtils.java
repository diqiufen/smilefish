package com.smle.fish.smilelibrary.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @PACKAGE_NAME：com.smle.fish.db
 * @user：yj
 * @date：2020/4/8
 * @版本：
 * @功能描述：
 */
public class DbUtils {

    private String TAG = DbUtils.class.getCanonicalName();
    private final String CREATE_TABLE = "CREATE TABLE";
    private final String DELETE_TABLE = "DROP TABLE";
    private final String ALTER_TABLE = "ALTER TABLE";//修改表名
    private String key = "id";//主键
    //复制的临时表 用于 在表数据 大更改时更改原表名（为了存储历史数据）  再重新创建表
    public static final String tempTable = "copyTable";
    //当查询的列名不存在时  是否插入列
    private boolean isInsertLine = true;

    public void initTable(Class c) {

    }

    public String createTable(Class c) {
        return getCreateTableSql(c);
    }

    public String deleteTable(Class c) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(DELETE_TABLE);
        stringBuffer.append(" ");
        stringBuffer.append(c.getSimpleName());
        Log.d(TAG, "sql=" + stringBuffer.toString());
        return stringBuffer.toString();
    }

    /**
     * 更新表名
     *
     * @param oldClass
     * @param newClass
     * @return
     */
    public String updateTableName(String oldClass, String newClass) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(ALTER_TABLE);
        stringBuffer.append(" ");
        stringBuffer.append(oldClass);
        stringBuffer.append(" ");
        stringBuffer.append("RENAME TO");
        stringBuffer.append(" ");
        stringBuffer.append(newClass);
        Log.d(TAG, "sql=" + stringBuffer.toString());
        return stringBuffer.toString();
    }

    /**
     * 给表新增列
     *
     * @param oldClass
     * @param columnName
     * @return
     */
    public String addTableColumn(Class oldClass, String columnName) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(ALTER_TABLE);
        stringBuffer.append(" ");
        stringBuffer.append(oldClass.getSimpleName());
        stringBuffer.append(" ");
        stringBuffer.append("ADD COLUMN");
        stringBuffer.append(" ");
        stringBuffer.append(columnName);
        Log.d(TAG, "sql=" + stringBuffer.toString());
        return stringBuffer.toString();
    }


    /**
     * create table user(name varchar(20) ,age integer default 0 ,)
     *
     * @param c
     */
    public String getCreateTableSql(Class c) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(CREATE_TABLE);
        stringBuffer.append(" ");
        stringBuffer.append(c.getSimpleName());
        stringBuffer.append("(");
        setKey(stringBuffer);
        getTableField(stringBuffer, c);
        stringBuffer.append(")");
        Log.d(TAG, "sql=" + stringBuffer.toString());
        return stringBuffer.toString();
    }

    //设置主键 自动增长
    private void setKey(StringBuffer stringBuffer) {
        stringBuffer.append(key);
        stringBuffer.append(" ");
        stringBuffer.append("INTEGER");
        stringBuffer.append(" ");
        stringBuffer.append("PRIMARY");
        stringBuffer.append(" ");
        stringBuffer.append("KEY");
        stringBuffer.append(" ");
        stringBuffer.append("AUTOINCREMENT");
        stringBuffer.append(" ");
        stringBuffer.append("UNIQUE");
        stringBuffer.append(" ");
        stringBuffer.append(",");
    }

    public String getQueryTableSql(String tableName) {
        String sql = "SELECT * FROM " + tableName + " ORDER BY " + key + " ASC";
        return sql;
    }

    /**
     * @param stringBuffer
     * @param objectModule c
     */
    private void getTableField(StringBuffer stringBuffer, Class c) {
        Field[] fields = c.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            String name = fields[i].getName();
            if (name.equals(c.getCanonicalName())) {
                continue;
            }
            stringBuffer.append(name);
            stringBuffer.append(" ");
            stringBuffer.append(getType(fields[i].getType()));
            Object object = getFieldValueByName(fields[i].getName(), c);
            if (object != null && !c.getCanonicalName().equals(object)) {
                stringBuffer.append(" ");
                stringBuffer.append("default");
                stringBuffer.append(" ");
                stringBuffer.append(object);
                stringBuffer.append(" ");
            }
            if (i != fields.length - 1) {
                stringBuffer.append(",");
            }
        }
    }

    /**
     * 获取对象的值
     *
     * @param fieldName
     * @param objectModule
     * @return
     */
    private Object getFieldValueByName(String fieldName, Object objectModule) {
        Method method = null;
        Object value = null;
        try {
            Class c = objectModule.getClass();
            String firstLetter = fieldName.substring(0, 1).toUpperCase();
            String getter = "get" + firstLetter + fieldName.substring(1);
            method = c.getMethod(getter, new Class[]{});
            value = method.invoke(objectModule, null);
//            Log.i(TAG, "value=" + value);
            return value;
        } catch (Exception e) {
//            try {
//                if (e.equals("Wrong number of arguments; expected 0, got 1")) {
//                    value = method.invoke(objectModule, new Object[]{});
//                }
//                return value;
//            } catch (Exception e1) {
//            }
            return null;
        }
    }

    private Object getTypeDefaultValue(Object type) {

        if (type instanceof Integer) {
            return 0;
        } else if (type instanceof Double) {
            return 0;
        } else if (type instanceof Float) {
            return 0;
        } else if (type instanceof String) {
            return "";
        } else if (type instanceof Long) {
            return 0;
        }
        return null;
    }

    private Object getType(Object type) {

        if (type instanceof Integer) {
            return SqliteType.INTEGER.toString();
        } else if (type instanceof Float) {
            return SqliteType.INTEGER.toString();
        } else if (type instanceof String) {
            return SqliteType.TEXT.toString();
        } else if (type instanceof Long) {
            return SqliteType.INTEGER.toString();
        } else if (type.toString().equals("int")) {
            return SqliteType.INTEGER.toString();
        } else {
            return SqliteType.TEXT.toString();
        }
    }

    public String getTableName(Class c) {
        return c.getSimpleName();
    }

    /**
     * 根据类对象键值 存入 ContentValues对象的 name value contentValues.put(name, value);
     *
     * @param objectModule
     */
    public ContentValues getFieldContentValues(Object objectModule) {
        ContentValues contentValues = new ContentValues();
        Class c = objectModule.getClass();
        Field[] fields = c.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            String name = fields[i].getName();
            //id dbBaseModel id
            if (name.equals(c.getCanonicalName()) || name.equals("id")) {
                continue;
            }
            Object object = getFieldValueByName(fields[i].getName(), objectModule);
            getMatchingTypeValue(object, name, contentValues);
        }
        return contentValues;
    }

    private void getMatchingTypeValue(Object type, String name, ContentValues contentValues) {
        if (type == null) {
            contentValues.put(name, "");
        } else if (type instanceof Integer) {
            contentValues.put(name, Integer.parseInt(type.toString()));
        } else if (type instanceof Float) {
            contentValues.put(name, Float.parseFloat(type.toString()));
        } else if (type instanceof String) {
            contentValues.put(name, type.toString());
        } else if (type instanceof Long) {
            contentValues.put(name, Long.parseLong(type.toString()));
        } else if (type.toString().equals("int")) {
            contentValues.put(name, Integer.parseInt(type.toString()));
        } else {
            contentValues.put(name, type.toString());
        }
    }

    /**
     * 获取删除条件
     *
     * @return
     */
    public Map<String, Object> getDeleteCondition(Object objectModule, String whereClause, String whereArgs) {
        Map<String, Object> deleteConditionValue = new HashMap<>();
        Class aClass = objectModule.getClass();
        StringBuffer stringBuffer = new StringBuffer();
        Field[] fields = aClass.getDeclaredFields();
        List<String> whereArgsValue = new ArrayList<>();
        for (int i = 0; i < fields.length; i++) {
            String name = fields[i].getName();
            if (name.equals(aClass.getCanonicalName())) {
                continue;
            }
            Object object = getFieldValueByName(fields[i].getName(), objectModule);

            if (object != null && !object.equals("")) {
                if (stringBuffer.length() > 0) {
                    stringBuffer.append(" and ");
                }
                stringBuffer.append(name);
                stringBuffer.append("=?");
                whereArgsValue.add(object.toString());
            }
        }
        String[] strings = new String[whereArgsValue.size()];
        whereArgsValue.toArray(strings);
        deleteConditionValue.put(whereClause, stringBuffer.toString());
        deleteConditionValue.put(whereArgs, strings);
        return deleteConditionValue;
    }

    /**
     * 获取条件
     *
     * @param objectModule
     * @param accordingField 条件字段
     * @param whereClause
     * @param whereArgs
     * @return
     */
    public Map<String, Object> getAccordingValue(Object objectModule, String accordingField, String whereClause, String whereArgs) {
        Map<String, Object> deleteConditionValue = new HashMap<>();
        Class aClass = objectModule.getClass();
        StringBuffer stringBuffer = new StringBuffer();
        Field[] fields = aClass.getDeclaredFields();
        List<String> whereArgsValue = new ArrayList<>();
        for (int i = 0; i < fields.length; i++) {
            String name = fields[i].getName();
            if (name.equals(aClass.getCanonicalName())) {
                continue;
            }
            if (accordingField.equals(name)) {
                Object object = getFieldValueByName(name, objectModule);
                if (object != null) {
                    stringBuffer.append(name);
                    stringBuffer.append("=?");
                    whereArgsValue.add(object.toString());
                }
                break;
            }
        }
        String[] strings = new String[whereArgsValue.size()];
        whereArgsValue.toArray(strings);
        deleteConditionValue.put(whereClause, stringBuffer.toString());
        deleteConditionValue.put(whereArgs, strings);
        return deleteConditionValue;
    }

    /**
     * @param objectModule
     * @param accordingField 条件字段
     * @param whereClause
     * @param whereArgs
     * @return
     */
    public Map<String, Object> getAccordingValue(Object objectModule, String[] accordingField, String whereClause, String whereArgs) {
        Map<String, Object> deleteConditionValue = new HashMap<>();
        Class aClass = objectModule.getClass();
        StringBuffer stringBuffer = new StringBuffer();
        Field[] fields = aClass.getDeclaredFields();
        List<String> whereArgsValue = new ArrayList<>();
        for (int i = 0; i < fields.length; i++) {
            String name = fields[i].getName();
            if (name.equals(aClass.getCanonicalName())) {
                continue;
            }
            for (int j = 0; j < accordingField.length; j++) {
                if (accordingField[j].equals(name)) {
                    Object object = getFieldValueByName(name, objectModule);
                    if (object != null) {
                        if (stringBuffer.length() > 0) {
                            stringBuffer.append(" and ");
                        }
                        stringBuffer.append(name);
                        stringBuffer.append("=?");
                        whereArgsValue.add(object.toString());
                    }
                    break;
                }
            }
            if (accordingField.length == whereArgsValue.size()) {
                break;
            }
        }
        String[] strings = new String[whereArgsValue.size()];
        whereArgsValue.toArray(strings);
        deleteConditionValue.put(whereClause, stringBuffer.toString());
        deleteConditionValue.put(whereArgs, strings);
        return deleteConditionValue;
    }

    /**
     * 根据模型 获取查到的数据
     *
     * @param c
     * @param cursor
     * @param <T>
     * @return
     */
    public <T> T getModule(Class c, Cursor cursor) {
        Object object = null;
        try {
            object = c.newInstance();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        } catch (InstantiationException e) {
            e.printStackTrace();
            return null;
        }
        try {
            Field field = c.getField(key);
            setBaseValue(c, field, cursor, object);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        Field[] fields = c.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
//            String name = fields[i].getName();
//            if (name.equals(c.getCanonicalName())) {
//                continue;
//            }
            setValue(c, fields[i], cursor, object);
//            Object value = getTypeValue(cursor, fields[i].getType(), name);
//            if (value != null) {
//                try {
//                    Field declaredField = c.getDeclaredField(name);
//                    declaredField.setAccessible(true);
//                    declaredField.set(object, value);
//                } catch (NoSuchFieldException | IllegalAccessException e) {
//                    continue;
//                }
//
//            }
        }
        return (T) object;
    }

    private void setValue(Class c, Field field, Cursor cursor, Object object) {
        try {
            String fieldName = field.getName();
            if (fieldName.equals(c.getCanonicalName())) {
                return;
            }
            Object value = getTypeValue(cursor, field.getType(), fieldName);
            if (value != null) {
                Field declaredField = c.getDeclaredField(fieldName);
                declaredField.setAccessible(true);
                declaredField.set(object, value);
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private void setBaseValue(Class c, Field field, Cursor cursor, Object object) {
        try {
            String fieldName = field.getName();
            if (fieldName.equals(c.getCanonicalName())) {
                return;
            }
            Object value = getTypeValue(cursor, field.getType(), fieldName);
            if (value != null) {
                Field declaredField = c.getField(fieldName);
                declaredField.setAccessible(true);
                declaredField.set(object, value);
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private Object getTypeValue(Cursor cursor, Object type, String fieldName) {
        Object value = null;
        try {
            if (type instanceof Integer) {
                value = cursor.getInt(cursor.getColumnIndex(fieldName));
            } else if (type instanceof Float) {
                value = cursor.getFloat(cursor.getColumnIndex(fieldName));
            } else if (type instanceof String) {
                value = cursor.getString(cursor.getColumnIndex(fieldName));
            } else if (type instanceof Long) {
                value = cursor.getLong(cursor.getColumnIndex(fieldName));
            } else if (type.toString().equals("int")) {
                value = cursor.getInt(cursor.getColumnIndex(fieldName));
            } else {
                value = cursor.getString(cursor.getColumnIndex(fieldName));
            }
        } catch (IllegalStateException e) {
            e.printStackTrace();
            value = null;
        }
        return value;
    }


    /**
     * 导入表数据的sql
     *
     * @param newTableName 新表名
     * @param columnNames  就表列
     * @return
     */
    public String importTableDataSql(String newTableName, String[] columnNames) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("INSERT INTO");
        stringBuffer.append(" ");
        stringBuffer.append(newTableName);
        stringBuffer.append("(");
        StringBuffer child = new StringBuffer();
        for (String columu : columnNames) {
            if (child.length() > 0) {
                child.append(",");
            }
            child.append(columu);
        }
        stringBuffer.append(child.toString());
        stringBuffer.append(")");
        stringBuffer.append(" ");
        stringBuffer.append("SELECT");
        stringBuffer.append(" ");
        stringBuffer.append(child.toString());
        stringBuffer.append(" ");
        stringBuffer.append("FROM");
        stringBuffer.append(" ");
        stringBuffer.append(tempTable);
        return stringBuffer.toString();
    }

    /**
     * 删除表
     *
     * @param simpleName
     * @return
     */
    public String getDeleteTable(String simpleName) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("DROP");
        stringBuffer.append(" ");
        stringBuffer.append("table");
        stringBuffer.append(" ");
        stringBuffer.append(simpleName);
        return stringBuffer.toString();
    }
}
