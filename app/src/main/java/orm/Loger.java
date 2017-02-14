package orm;

import android.database.Cursor;
import android.database.sqlite.SQLiteQuery;
import android.util.Log;


import com.omsk.bitnic.fatpig.BuildConfig;

import java.lang.reflect.Field;


public class Loger {
    private final static boolean isWrite = BuildConfig.DEBUG;

    public static void LogE(String msg) {
        if (isWrite) {
            Log.e("____ORM____", msg);
        }

    }

    public static void LogI(String msg) {
        if (isWrite) {
            Log.i("____ORM____", msg);
        }

    }

    public static void printSql(Cursor cursor) {
        if (isWrite) {
            try {

                Field ddd = cursor.getClass().getDeclaredField("mQuery");
                ddd.setAccessible(true);
                SQLiteQuery v = (SQLiteQuery) ddd.get(cursor);
                Loger.LogI(v.toString());
            } catch (Exception ignored) {

            }
        }
    }
}
