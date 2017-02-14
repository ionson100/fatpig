package com.settings.ion.mylibrary;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


class DataBaseHelper extends SQLiteOpenHelper {


   // private static final SQLiteDatabase myDataBase = null;

    DataBaseHelper(Context context, String databasePath) {
        super(context, databasePath, null, 1);
    }

    public SQLiteDatabase openDataBaseForReadable() throws SQLException {

        return this.getReadableDatabase();
    }

    public SQLiteDatabase openDataBaseForWritable() throws SQLException {
        return this.getWritableDatabase();
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
