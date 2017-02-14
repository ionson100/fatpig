package com.settings.ion.mylibrary;


import android.annotation.TargetApi;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;


class SqliteStorage {

    private static DataBaseHelper helper=new DataBaseHelper(Reanimator.getContext(), Reanimator.filePath+"/ion100.sqlite");;

    private static SQLiteDatabase read(){
       return helper.getReadableDatabase();
    }

    private static SQLiteDatabase write(){
        return helper.getWritableDatabase();
    }



    private static String getTableName(Class aClass){
        return aClass.getName().replace('.','_');
    }

    private  static   boolean existTable(Class aClass){//;
        Cursor c=null;
        SQLiteDatabase database=read();
        String name=getTableName(aClass);
        List<String> masters=new ArrayList<>();
        c= database.rawQuery("SELECT name FROM sqlite_master", null);
        if (c != null) {
            try {
                if (c.moveToFirst()) {
                    do {
                        masters.add(c.getString(0));
                    } while (c.moveToNext());
                }
            } finally {
                c.close();
            }
        }
       // database.close();
        return   masters.contains(name);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    static  Object getObject(Class aClass){//synchronized
        Object resO=null;
        if(!existTable(aClass)){

            SQLiteDatabase ses=write();
                   ses.beginTransaction();
                   try{
                       String sql="create table " + getTableName(aClass) + " ( _id integer primary key autoincrement, ass text not null);";
                       ses.execSQL(sql);
                       resO= aClass.newInstance();
                       Gson sd3 = new Gson();
                       String str = sd3.toJson(resO);
                       ses.execSQL("INSERT INTO "+getTableName(aClass)+" (ass) VALUES ('"+str+"');");
                       ses.setTransactionSuccessful();
                       ses.endTransaction();
                   }catch (Exception ex){
                       ses.endTransaction();
                       throw new RuntimeException( "reanimator create-insert: "+ex.getMessage());
                   }finally {
                      // ses.close();
                   }
        }else{
            SQLiteDatabase ses=read();
            Cursor c=null;
            try{


                c= ses.rawQuery("select ass from " + getTableName(aClass) + " where _id=1", null);
                if (c.moveToFirst()) {

                    String str= c.getString(0);
                    if(str==null){
                        resO = aClass.newInstance();
                    }else {
                        Gson ss = new Gson();
                        resO=  ss.fromJson(str,aClass);
                    }
                }

            }catch (Exception ex){
                throw new RuntimeException( "reanimator select  as get object: "+ex.getMessage());
            }finally {
//                if(ses!=null){
//                    ses.close();
//                }
                if(c!=null){
                    c.close();
                }
            }
        }
        return resO;
    }

    static  void saveObject(Object o, Class aClass){//synchronized

        Gson sd3 = new Gson();
        String str=  sd3.toJson(o);
        SQLiteDatabase ses=write();

        try {
            ses.beginTransaction();
            ses.execSQL("UPDATE " + getTableName(aClass)+ " SET ass = '"+str+"' WHERE _id = 1");
            ses.setTransactionSuccessful();
            ses.endTransaction();
        }catch (Exception ex){
            ses.endTransaction();
        }finally {
//            if(ses!=null){
//                ses.close();
//            }
        }
    }

//    public static void start() {
//        if(helper!=null){
//            helper.close();
//        }
//
//    }

    public  static void close() {
            helper.close();
    }//synchronized
}


