package orm;

import android.annotation.TargetApi;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class Configure implements ISession {

    static String dataBaseName;
    private static DataBaseHelper myDbHelper;
    private static boolean reloadBase = false;

    private SQLiteDatabase sqLiteDatabaseForReadable = null;
    private SQLiteDatabase sqLiteDatabaseForWritable = null;

    private Configure() {
        sqLiteDatabaseForReadable = GetSqLiteDatabaseForReadable();
        sqLiteDatabaseForWritable = GetSqLiteDatabaseForWritable();
    }

    public Configure(String dataBaseName, Context context, boolean reloadBase) {
        Configure.reloadBase = reloadBase;
        Configure.dataBaseName = dataBaseName;

        myDbHelper = new DataBaseHelper(context, Configure.dataBaseName);

        if (reloadBase) {
            myDbHelper.getReadableDatabase();
            try {
                myDbHelper.copyDataBase();
            } catch (IOException e) {
                throw new Error("MError copying database -" + e.getMessage());
            }
        } else {
            if (!myDbHelper.checkDataBase()) {
                myDbHelper.createDataBase();
            }
        }
    }

    //////////////////////////////////////////////////// bulk
    public static <T> void bulk(Class<T> tClass, List<T> tList, ISession ses) {

        List<List<T>> sd = partition(tList, 500);
        for (List<T> ts : sd) {
            Configure.InsertBulk s = Configure.getInsertBulk(tClass);
            for (T t : ts) {
                s.add(t);
            }
            String sql = s.getSql();
            if (sql != null) {
                try {
                    ses.execSQL(sql);
                } catch (Exception ex) {
                    //int i = 0;
                }
            }
        }
    }
    /////////////////////////////////////////////////////////////////////

    private static <T> List<List<T>> partition(Collection<T> members, int maxSize) {
        List<List<T>> res = new ArrayList<>();

        List<T> internal = new ArrayList<>();

        for (T member : members) {
            internal.add(member);

            if (internal.size() == maxSize) {
                res.add(internal);
                internal = new ArrayList<>();
            }
        }
        if (!internal.isEmpty()) {
            res.add(internal);
        }
        return res;
    }

    public static boolean isLive() {
        return dataBaseName != null && myDbHelper != null;
    }

    public static String getBaseName() {
        return dataBaseName;
    }

    public static Configure getSession() {
        return new Configure();
    }

    private static SQLiteDatabase GetSqLiteDatabaseForReadable() throws SQLException {
        return myDbHelper.openDataBaseForReadable();
    }

    private static SQLiteDatabase GetSqLiteDatabaseForWritable() throws SQLException {

        return myDbHelper.openDataBaseForWritable();
    }

    public static void createBase(String path) {
        File f = new File(path);
        if (f.exists()) return;
        f.getParentFile().mkdirs();
        try {
            f.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(" orm create base :" + e.getMessage());
        }
    }

    private static String pizdaticusKey(ItemField field) {
        if (field.type == double.class || field.type == float.class || field.type == Double.class || field.type == Float.class) {
            return " REAL ";
        }
        if (field.type == int.class || field.type == long.class || field.type == short.class || field.type == byte.class || field.type == Integer.class ||
                field.type == Long.class || field.type == Short.class || field.type == Byte.class) {
            return " INTEGER ";
        }
        if (field.type == String.class) {
            return " TEXT ";
        }
        if (field.type == boolean.class) {
            return " BOOL ";
        }
        return "";
    }

    private static String pizdaticusField(ItemField field) {
        if (field.type == double.class ||
                field.type == float.class ||
                field.type == Double.class ||
                field.type == Float.class) {
            return " REAL DEFAULT 0, ";
        }
        if (field.type == int.class ||
                field.type == Enum.class ||
                field.type == long.class ||
                field.type == short.class ||
                field.type == byte.class ||
                field.type == Integer.class ||
                field.type == Long.class ||
                field.type == Short.class) {
            return " INTEGER DEFAULT 0, ";
        }
        if (field.type == String.class) {
            return " TEXT, ";
        }
        if (field.type == boolean.class || field.type == Boolean.class) {
            return " BOOL DEFAULT 0, ";
        }

        if (field.type == byte[].class) {
            return " BLOB, ";
        }
        return "";
    }

    public static void createTable(Class<?> aClass) {
        cacheMetaDate date = CacheDictionary.getCacheMetaDate(aClass);
        StringBuilder sb = new StringBuilder("CREATE TABLE " + date.tableName + " (");
        sb.append(date.keyColumn.columnName).append(" ");
        sb.append(pizdaticusKey(date.keyColumn));
        sb.append("PRIMARY KEY, ");
        for (Object f : date.listColumn) {
            ItemField ff = (ItemField) f;
            sb.append(ff.columnName);
            sb.append(pizdaticusField(ff));
        }
        String s = sb.toString().trim();
        String ss = s.substring(0, s.length() - 1);
        String sql = ss + ")";
        Configure.getSession().execSQL(sql);
    }

    // пакетная вставка
    private static InsertBulk getInsertBulk(Class aClass) {
        return new InsertBulk(aClass);
    }

    //@Override
    public static synchronized void close() {
        if (myDbHelper != null) {
            myDbHelper.close();
        }
    }

    @Override
    public <T> int update(T item) {
        SQLiteDatabase con = sqLiteDatabaseForWritable;
        cacheMetaDate d = CacheDictionary.getCacheMetaDate(item.getClass());
        ContentValues values = null;
        try {
            values = getContentValues(item, d);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e.getMessage());
        }
        Object key = null;
        try {
            Field field = d.keyColumn.field;
            field.setAccessible(true);
            key = field.get(item);
        } catch (Exception e) {
            Loger.LogE(e.getMessage());
            throw new RuntimeException("Config Update:" + e.getMessage());
        }
        assert key != null;
        if (d.isIAction()) {
            ((IActionOrm) item).actionBeforeUpdate(item);
        }
        int i = con.update(d.tableName, values, d.keyColumn.columnName + " = ?", new String[]{key.toString()});

        if (i == -1) {
            throw new RuntimeException("ORM simple update -  res=-1");
        } else {
            if (d.isIAction()) {
                ((IActionOrm) item).actionAfterUpdate(item);
            }
        }
        return i;
    }

    @Override
    public <T> int updateWhere(T item, String whereSql) {
        SQLiteDatabase con = sqLiteDatabaseForWritable;
        cacheMetaDate d = CacheDictionary.getCacheMetaDate(item.getClass());
        ContentValues values = null;
        try {
            values = getContentValues(item, d);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e.getMessage());
        }
        Object key = null;
        try {
            Field field = d.keyColumn.field;
            field.setAccessible(true);
            key = field.get(item);
        } catch (Exception e) {
            Loger.LogE(e.getMessage());
            throw new RuntimeException("Config Update:" + e.getMessage());
        }
        assert key != null;
        if (d.isIAction()) {
            ((IActionOrm) item).actionBeforeUpdate(item);
        }
        int i = con.update(d.tableName, values, d.keyColumn.columnName + " = ? and " + whereSql, new String[]{key.toString()});

        if (i == -1) {
            // throw new RuntimeException("ORM simple update -  res=-1");
        } else {
            if (d.isIAction()) {
                ((IActionOrm) item).actionAfterUpdate(item);
            }
        }
        return i;
    }

    @Override
    public <T> int insert(T item) {

        SQLiteDatabase con = sqLiteDatabaseForWritable;
        cacheMetaDate d = CacheDictionary.getCacheMetaDate(item.getClass());
        ContentValues values = null;
        try {
            values = getContentValues(item, d);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e.getMessage());
        }
        if (d.isIAction()) {
            ((IActionOrm) item).actionBeforeInsert(item);
        }
        int i = (int) con.insert(d.tableName, null, values);

        if (i == -1) {
            throw new RuntimeException(" no insert record");
        } else {
            if (d.isIAction()) {
                ((IActionOrm) item).actionAfterInsert(item);
            }
        }
        try {
            d.keyColumn.field.setAccessible(true);
            d.keyColumn.field.set(item, i);
        } catch (Exception e) {
            new RuntimeException("ORM insert ---" + e.getMessage());
        }
        Loger.LogI("INSERT:" + d.tableName + " VALUES:" + String.valueOf(values));
        return i;
    }

    private <T> ContentValues getContentValues(T item, cacheMetaDate<?> d) throws NoSuchFieldException {
        ContentValues values = new ContentValues();
        try {
            for (ItemField str : d.listColumn) {
                Field field = str.field;//item.getClass().getDeclaredField(str.fieldName);
                field.setAccessible(true);

                if (str.isUserType) {
                    Object date = str.aClassUserType.newInstance();
                    String json = ((IUserType) date).getString(field.get(item));
                    values.put(str.columnName, json);
                } else {
                    if (str.type == String.class)
                        values.put(str.columnName, (String) field.get(item));
                    if (str.type == int.class)
                        values.put(str.columnName, (int) field.get(item));
                    if (str.type == long.class)
                        values.put(str.columnName, (long) field.get(item));
                    if (str.type == short.class)
                        values.put(str.columnName, (short) field.get(item));
                    if (str.type == byte.class)
                        values.put(str.columnName, (byte) field.get(item));
                    if (str.type == Short.class)
                        values.put(str.columnName, (Short) field.get(item));
                    if (str.type == Long.class)
                        values.put(str.columnName, (Long) field.get(item));
                    if (str.type == Integer.class)
                        values.put(str.columnName, (Integer) field.get(item));
                    if (str.type == Double.class)
                        values.put(str.columnName, (Double) field.get(item));
                    if (str.type == Float.class)
                        values.put(str.columnName, (Float) field.get(item));
                    if (str.type == byte[].class)
                        values.put(str.columnName, (byte[]) field.get(item));
                    if (str.type == double.class)
                        values.put(str.columnName, (double) field.get(item));
                    if (str.type == boolean.class) {
                        boolean val = (boolean) field.get(item);
                        if (val) {
                            values.put(str.columnName, 1);
                        } else {
                            values.put(str.columnName, 0);
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        return values;
    }

    @Override
    public <T> int delete(T item) {
        SQLiteDatabase con = sqLiteDatabaseForWritable;
        cacheMetaDate d = CacheDictionary.getCacheMetaDate(item.getClass());

        Object key = null;
        try {
            Field field = d.keyColumn.field;
            field.setAccessible(true);
            key = field.get(item);
        } catch (Exception e) {
            throw new RuntimeException("ORM simple delete - " + e.getMessage());
        }
        if (d.isIAction()) {
            ((IActionOrm) item).actionBeforeDelete(item);
        }
        int res = con.delete(d.tableName, d.keyColumn.columnName + "=?", new String[]{key.toString()});
        if (res != 0) {
            if (d.isIAction()) {
                ((IActionOrm) item).actionAfterDelete(item);
            }
        } else {
            throw new RuntimeException(" orm not deleted:" + item.getClass().getName() + " object key:" + String.valueOf(key));
        }
        return res;
    }

    private String wherower(String where, cacheMetaDate cacheMetaDate) {
        if (cacheMetaDate.where != null) {
            where = " " + cacheMetaDate.where.trim() + (where == null ? "" : " and " + where) + " ";
        }
        return where;
    }

    @Override
    public SQLiteDatabase getSqLiteDatabase() {
        return sqLiteDatabaseForReadable;
    }

    @Override
    public <T> List<T> getList(Class<T> tClass, String where, Object... objects) throws SQLException {
        List<T> list = null;
        SQLiteDatabase con;
        try {
            con = sqLiteDatabaseForReadable;
            cacheMetaDate d = CacheDictionary.getCacheMetaDate(tClass);

            //////////////////////// add where
            where = wherower(where, d);
            ////////////////////////////////

            Cursor c = null;
            String[] sdd = d.getStringSelect();
            if (where == null && objects == null || where == null && objects.length == 0) {

                c = con.query(d.tableName, sdd, null, null, null, null, null, null);
            } else if (where != null && objects == null || where != null && objects.length == 0) {
                c = con.query(d.tableName, sdd, where, null, null, null, null, null);
            }


            if (where != null && objects != null) {
                String[] str = new String[objects.length];
                for (int i = 0; i < objects.length; i++) {
                    str[i] = String.valueOf(objects[i]);
                }
                c = con.query(d.tableName, sdd, where, str, null, null, null, null);
            }

            if (c != null) {
                list = new ArrayList<>(c.getCount());
                Loger.printSql(c);
                try {
                    if (c.moveToFirst()) {
                        do {
                            Object sd = tClass.newInstance();
                            Companaund(d.listColumn, d.keyColumn, c, sd);
                            list.add((T) sd);
                        } while (c.moveToNext());
                    }
                } finally {
                    c.close();
                }
            }
        } catch (Exception e) {
            new RuntimeException("ORM getList ---" + e.getMessage());
        }
        return list;
    }

    private void Companaund(List<ItemField> listIf, ItemField key, Cursor c, Object o) throws NoSuchFieldException, IllegalAccessException {
        for (ItemField str : listIf) {
            int i = c.getColumnIndex(str.columnName);
            Field res = str.field;
            res.setAccessible(true);


            if (str.isUserType) {
                IUserType data = null;
                try {
                    data = (IUserType) str.aClassUserType.newInstance();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                }
                Object sdd = data.getObject(c.getString(i));
                res.set(o, sdd);
            } else {
                if (str.type == int.class) {
                    res.setInt(o, c.getInt(i));
                } else if (str.type == String.class) {
                    res.set(o, c.getString(i));
                } else if (str.type == double.class) {
                    res.setDouble(o, c.getDouble(i));
                } else if (str.type == float.class) {
                    res.setFloat(o, c.getFloat(i));
                } else if (str.type == long.class) {
                    res.setLong(o, c.getLong(i));
                } else if (str.type == short.class) {
                    res.setShort(o, c.getShort(i));
                } else if (str.type == byte[].class) {
                    res.set(o, c.getBlob(i));
                } else if (str.type == byte.class) {
                    res.setByte(o, (byte) c.getLong(i));
                } else if (str.type == Integer.class) {
                    if (c.isNull(i)) {
                        res.set(o, null);
                    } else {
                        Integer ii = c.getInt(i);
                        res.set(o, ii);
                    }
                } else if (str.type == Double.class) {
                    if (c.isNull(i)) {
                        res.set(o, null);
                    } else {
                        Double d = c.getDouble(i);
                        res.set(o, d);
                    }
                } else if (str.type == Float.class) {
                    if (c.isNull(i)) {
                        res.set(o, null);
                    } else {
                        Float f = c.getFloat(i);
                        res.set(o, f);
                    }
                } else if (str.type == Long.class) {
                    if (c.isNull(i)) {
                        res.set(o, null);
                    } else {
                        Long l = c.getLong(i);
                        res.set(o, l);
                    }
                } else if (str.type == Short.class) {
                    if (c.isNull(i)) {
                        res.set(o, null);
                    } else {
                        Short sh = c.getShort(i);
                        res.set(o, sh);
                    }
                } else if (str.type == boolean.class) {
                    boolean val;
                    val = c.getInt(i) != 0;
                    res.setBoolean(o, val);
                } else if (str.type == Boolean.class) {
                    if (c.isNull(i)) {
                        res.set(o, null);
                    } else {
                        boolean val;
                        val = c.getInt(i) != 0;
                        res.setBoolean(o, val);
                    }
                }else {
                    new RuntimeException("Error orm set values");
                }
            }
        }
        try {
            Field field = key.field;
            field.setAccessible(true);
            field.set(o, c.getInt(c.getColumnIndex(key.columnName)));
        } catch (Exception e) {
            throw new RuntimeException("orm get id" + e.getMessage());
        }
    }

    private boolean containInterface(Class<?>[] classes) {
        for (Class<?> aClass : classes) {
            if (aClass == IUsingGuidId.class) {
                return true;
            }
        }
        return false;
    }

    @Override
    public <T> T get(Class<T> tClass, Object id) {
        cacheMetaDate d = CacheDictionary.getCacheMetaDate(tClass);
        List<T> res;
        if (containInterface(tClass.getInterfaces()) && id instanceof String) {
            if (id == null || id.toString().trim().length() == 0) {
                return null;
            }
            String where = wherower("idu = ?", d);

            res = getList(tClass, where, id.toString());
        } else {
            res = getList(tClass, d.keyColumn.columnName + "=?", id);
        }

        if (res.size() == 0) return null;


        if (res.size() > 1) {
            throw new RuntimeException("orm (get) more than one");
        }
        return res.get(0);
    }

    @Override
    public Object executeScalar(String sql, Object... objects) {
        List<String> arrayList = new ArrayList<>();
        String[] array = null;
        if (objects != null && objects.length > 0) {
            for (Object object : objects) {
                arrayList.add(String.valueOf(object));
            }
            array = new String[arrayList.size()];
            arrayList.toArray(array);
        }
        Loger.LogI(sql);
        return InnerListExe(sql, array);
    }

    @Override
    public void execSQL(String sql, Object... objects) {

        List<String> arrayList = new ArrayList<>();
        String[] array;
        if (objects != null && objects.length > 0) {
            for (Object object : objects) {
                arrayList.add(String.valueOf(object));
            }
            array = new String[arrayList.size()];
            arrayList.toArray(array);
            sqLiteDatabaseForWritable.execSQL(sql, array);

        } else {
            sqLiteDatabaseForWritable.execSQL(sql);
        }
        Loger.LogI(sql);
    }

    @Override
    public int deleteTable(String tableName) {

        int i = sqLiteDatabaseForWritable.delete(tableName, null, null);
        Loger.LogI("DELETE FROM " + tableName + "; RES=" + String.valueOf(i));
        return i;
    }

    @Override
    public int deleteTable(String tableName, String where, Object... objects) {
        if (tableName == null || tableName.trim().length() == 0) return 0;
        String[] par = null;
        if (objects != null) {
            par = new String[objects.length];
            for (int i = 0; i < objects.length; i++) {
                par[i] = objects[i].toString();
            }
        }
        int i = 0;
        if (where == null) {
            i = sqLiteDatabaseForWritable.delete(tableName, null, null);
        }
        if (where != null && par == null) {
            i = sqLiteDatabaseForWritable.delete(tableName, where, null);
        }
        if (where != null && par != null) {
            i = sqLiteDatabaseForWritable.delete(tableName, where, par);
        }
        Loger.LogI("DELETE FROM " + tableName + "; RES=" + String.valueOf(i));
        return i;
    }

    @Override
    public void beginTransaction() {
        myDbHelper.getWritableDatabase().beginTransaction();
    }

    @Override
    public void commitTransaction() {
        myDbHelper.getWritableDatabase().setTransactionSuccessful();
    }

    @Override
    public void endTransaction() {
        myDbHelper.getWritableDatabase().endTransaction();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private Object InnerListExe(String sql, String[] strings) {
        Cursor c;
        if (strings == null) {
            c = sqLiteDatabaseForReadable.rawQuery(sql, null);
        } else {
            c = sqLiteDatabaseForReadable.rawQuery(sql, strings);
        }
        if (c != null) {
            try {
                if (c.moveToFirst()) {
                    do {
                        int i = c.getType(0);
                        if (i == 0) {
                            return null;
                        }
                        if (i == 1) {
                            return c.getInt(0);
                        }
                        if (i == 2) {
                            return c.getFloat(0);
                        }
                        if (i == 3) {
                            return c.getString(0);
                        }
                        if (i == 4) {
                            return c.getBlob(0);
                        }
                    } while (c.moveToNext());
                }
            } finally {
                c.close();
            }
        }
        return null;
    }

    private static class InsertBulk<F> {
        final private StringBuilder sql = new StringBuilder();
        private int it = 0;
        private cacheMetaDate metaDate = null;
        private Class<F> aClass;

        InsertBulk(Class<F> aClass) {
            metaDate = CacheDictionary.getCacheMetaDate(aClass);
            sql.append(" INSERT INTO ");
            sql.append(metaDate.tableName).append(" (");
            for (int i = 0; i < metaDate.listColumn.size(); i++) {
                ItemField f = (ItemField) metaDate.listColumn.get(i);
                sql.append(f.columnName);
                if (i < metaDate.listColumn.size() - 1) {
                    sql.append(", ");
                } else {
                    sql.append(") VALUES ");
                }
            }

        }

        private static <T> List<List<T>> partition(Collection<T> members, int maxSize) {
            List<List<T>> res = new ArrayList<>();
            List<T> internal = new ArrayList<>();
            for (T member : members) {
                internal.add(member);
                if (internal.size() == maxSize) {
                    res.add(internal);
                    internal = new ArrayList<>();
                }
            }
            if (!internal.isEmpty()) {
                res.add(internal);
            }
            return res;
        }

        public static <T> void bulk(Class<T> tClass, List<T> tList, ISession ses) {

            List<List<T>> sd = partition(tList, 500);
            for (List<T> ts : sd) {
                Configure.InsertBulk s = Configure.getInsertBulk(tClass);
                for (T t : ts) {
                    s.add(t);
                }
                String sql = s.getSql();
                if (sql != null) {
                    try {
                        ses.execSQL(sql);
                    } catch (Exception ex) {
                        int i = 0;
                    }
                }
            }
        }

        public void add(F o) {
            it++;
            sql.append("(");
            for (int i = 0; i < metaDate.listColumn.size(); i++) {
                ItemField f = (ItemField) metaDate.listColumn.get(i);
                try {
                    Object value = f.field.get(o);
                    sql.append(getString(value, f.field.getType()));
                    if (i < metaDate.listColumn.size() - 1) {
                        sql.append(", ");
                    } else {
                    }

                } catch (IllegalAccessException e) {
                    throw new RuntimeException("InsertBulk:" + e.getMessage());
                }
            }
            sql.append(") ,");
        }

        String getSql() {
            if (it == 0) {
                return null;
            } else {
                return sql.toString().substring(0, sql.toString().lastIndexOf(",")).trim();
            }

        }

        private String getString(Object o, Class fClass) {

            if (fClass == String.class) {
                if (o == null) {
                    return "null";
                } else {
                    return "'" + String.valueOf(o).replace("'", " ") + "'";
                }
            } else if (fClass == boolean.class) {

                if (o == null) {
                    return "0";
                } else {
                    if ((Boolean) o) {
                        return "1";
                    } else {
                        return "0";
                    }
                }
            } else if (fClass == Boolean.class) {

                if (o == null) {
                    return "null";
                } else {
                    if ((Boolean) o) {
                        return "1";
                    } else {
                        return "0";
                    }
                }
            } else if (fClass == int.class ||
                    fClass == long.class ||
                    fClass == float.class ||
                    fClass == double.class ||
                    fClass == short.class) {
                if (o == null) {
                    return "0";
                } else {
                    return String.valueOf(o);
                }
            } else if (fClass == Integer.class ||
                    fClass == Float.class ||
                    fClass == Double.class ||
                    fClass == Long.class ||
                    fClass == Short.class) {
                if (o == null) {
                    return "null";
                } else {
                    return String.valueOf(o);
                }
            } else {
                throw new RuntimeException("InsertBulk:не могу определить тип");
            }
        }
    }
}
