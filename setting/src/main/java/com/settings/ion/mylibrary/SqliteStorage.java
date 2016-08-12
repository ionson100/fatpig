package com.settings.ion.mylibrary;


import android.annotation.TargetApi;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;


import com.google.gson.Gson;

import java.util.HashSet;
import java.util.Set;

public class SqliteStorage {

    private static final Set<String> stringSet=new HashSet<>();

    private static SQLiteDatabase myDatabase;

    private static String getTableName(Class aClass){
        return aClass.getName().replace('.','_');
    }

    private static SQLiteDatabase getMyDatabase(){
        if(myDatabase==null){
            myDatabase= Reanimator.getContext().openOrCreateDatabase(Reanimator.filePath+"/ion100.sqlite", Context.MODE_PRIVATE, null);
        }
        return myDatabase;
    }

    private static   boolean checkExostTable(Class aClass){//;
        Cursor c=null;
        try{
            String name=getTableName(aClass);
            c= getMyDatabase().rawQuery("SELECT * FROM sqlite_master WHERE name ='" + name + "' and type='table'", null);
           // Log.i("REANIMATOR","SELECT * FROM sqlite_master");
            return   c.getCount()==1;
        }catch (Exception ex){
            throw new RuntimeException( "reanimator select  as exist: "+ex.getMessage());
        }finally {
            if(c!=null)
                c.close();
        }
    }


    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static Object getObject(Class aClass){
        Object resO=null;
        if(!stringSet.contains(getTableName(aClass))&&!checkExostTable(aClass)){

                   getMyDatabase().beginTransaction();
                   try{
                       String sql="create table " + getTableName(aClass) + " ( _id integer primary key autoincrement, ass text not null);";
                       getMyDatabase().execSQL(sql);
                       resO= aClass.newInstance();
                       Gson sd3 = new Gson();
                       String str = sd3.toJson(resO);
                       getMyDatabase().execSQL("INSERT INTO "+getTableName(aClass)+" (ass) VALUES ('"+str+"');");
                       getMyDatabase().setTransactionSuccessful();
                       getMyDatabase().endTransaction();
                     //  Log.i("REANIMATOR",sql);
                   }catch (Exception ex){
                       getMyDatabase().endTransaction();
                       throw new RuntimeException( "reanimator create-insert: "+ex.getMessage());
                   }
        }else{
            try{

                Cursor c= getMyDatabase().rawQuery("select ass from " + getTableName(aClass) + " where _id=1", null);
                if (c.moveToFirst()) {

                    String str= c.getString(0);
                    Gson ss = new Gson();
                    resO=  ss.fromJson(str,aClass);
                }
                c.close();
                stringSet.add(getTableName(aClass));
            }catch (Exception ex){
                throw new RuntimeException( "reanimator select  as get object: "+ex.getMessage());
            }

        }
        return resO;
    }
    public static void saveObject(Object o,Class aClass){

        Gson sd3 = new Gson();
        String str=  sd3.toJson(o);

        getMyDatabase().beginTransaction();
        try{
            getMyDatabase().execSQL("UPDATE " + getTableName(aClass)+ " SET ass = '"+str+"' WHERE _id = 1");
            getMyDatabase().setTransactionSuccessful();
            getMyDatabase().endTransaction();
        }catch (Exception ex){
            getMyDatabase().endTransaction();
            throw new RuntimeException( "reanimator save update: "+ex.getMessage());
        }
    }
}
