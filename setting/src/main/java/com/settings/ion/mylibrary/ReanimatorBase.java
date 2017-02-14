package com.settings.ion.mylibrary;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Build;

import java.util.HashMap;
import java.util.Map;


public class ReanimatorBase extends ContextWrapper {

    private static ReanimatorBase reanimator;

    private static final Map<Class, Object> map = new HashMap<>();

    public static Object get(Class aClass){
        return innerGetSave(aClass, ReanimatorBase.ActionBase.get);
    }


    public  static void save(Class aClass){
        innerGetSave(aClass, ReanimatorBase.ActionBase.save);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)//
    private static   Object innerGetSave(Class aClass, ReanimatorBase.ActionBase actionBase){//synchronized

        Object res=null;
        if(actionBase== ReanimatorBase.ActionBase.get){
            if (map.containsKey(aClass)) {
                Object o = map.get(aClass);
                if(o ==null){
                    try {
                        o =aClass.newInstance();
                    } catch (InstantiationException |IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
                res= o;
            } else  {
                Object o =  SqliteStorage.getObject(aClass);
                if(o ==null){
                    try {
                        o =aClass.newInstance();
                    } catch (InstantiationException |IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
                map.put(aClass, o);
                res= o;
            }
        }
        if(actionBase== ReanimatorBase.ActionBase.save){

                Object o = map.get(aClass);
                if(o==null){
                    try {
                        o=aClass.newInstance();
                    } catch (InstantiationException |IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
                SqliteStorage.saveObject(o,aClass);
        }

        if(actionBase== ActionBase.close){
            SqliteStorage.close();
        }
        return  res;
    }

    public static String getHostPath() {
        return Reanimator.filePath;
    }

    public ReanimatorBase(Context base) {
        super(base);
    }

    public static void intContext(Context context){
        reanimator=new ReanimatorBase(context);
    }

    public static Context getContext(){
        return   reanimator.getApplicationContext();
    }

    public static void close() {
        innerGetSave(null, ActionBase.close);
    }

    private enum ActionBase{
        get, close, save
    }
}
