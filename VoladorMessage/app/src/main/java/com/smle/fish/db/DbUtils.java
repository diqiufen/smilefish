package com.smle.fish.db;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
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
    private final String CREATE_TABLE = "create table";

    public void initTable(Class c) {

    }

    public void createTable(SQLiteDatabase db, Class c) {
        db.execSQL(getSql(c));
    }

    /**
     * create table user(name varchar(20) ,age integer default 0 ,)
     *
     * @param c
     */
    public String getSql(Class c) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(CREATE_TABLE);
        stringBuffer.append(" ");
        stringBuffer.append(c.getSimpleName());
        stringBuffer.append("(");
        getTableField(stringBuffer, c);
        stringBuffer.append(")");
        Log.d(TAG, "sql=" + stringBuffer.toString());
        return stringBuffer.toString();
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
            if (object != null) {
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
        try {
            Class c = objectModule.getClass();
            String firstLetter = fieldName.substring(0, 1).toUpperCase();
            String getter = "get" + firstLetter + fieldName.substring(1);
            Method method = c.getMethod(getter, new Class[]{});
            Object value = method.invoke(objectModule, new Object[]{});
//            Log.i(TAG, "value=" + value);
            return value;
        } catch (Exception e) {
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
            if (name.equals(c.getCanonicalName())) {
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
                    Object object = getFieldValueByName(fields[i].getName(), objectModule);
                    if (stringBuffer.length() > 0) {
                        stringBuffer.append(" and ");
                    }
                    stringBuffer.append(name);
                    stringBuffer.append("=?");
                    if (object == null) {
                        whereArgsValue.add(null);
                    } else {
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
}
