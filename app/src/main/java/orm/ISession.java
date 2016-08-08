package orm;

import android.database.sqlite.SQLiteDatabase;

import java.util.List;

public interface ISession {

    SQLiteDatabase getSqLiteDatabase();
    <T> int update(T item);

    <T> int insert(T item);

    <T> int delete(T item);

    <T> List<T> getList(Class<T> tClass, String where, Object... objects);

    <T> T get(Class<T> tClass, Object id);

    Object executeScalar(String sql, Object... objects);

    void execSQL(String sql, Object... objects);

    //  void execSQL(String sql);

    void beginTransaction();

    void commitTransaction();

    void endTransaction();

    void close();

    void deleteTable(String tableName);


}



