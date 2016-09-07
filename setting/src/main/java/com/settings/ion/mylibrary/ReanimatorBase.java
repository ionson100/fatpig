package com.settings.ion.mylibrary;

import android.content.Context;
import android.content.ContextWrapper;

import java.util.HashMap;
import java.util.Map;


public class ReanimatorBase extends ContextWrapper {

    private static ReanimatorBase reanimator;

    private static final Map<Class, Object> map = new HashMap<>();

    public static Object get(Class aClass){
        return innerGetSave(aClass, ReanimatorBase.ActionBase.get);
    }


    public static void save(Class aClass){
        innerGetSave(aClass, ReanimatorBase.ActionBase.save);
    }

    private static synchronized Object innerGetSave(Class aClass,ReanimatorBase.ActionBase actionBase){

        Object res=null;
        if(actionBase== ReanimatorBase.ActionBase.get){
            if (map.containsKey(aClass)) {
                return map.get(aClass);
            } else {
                Object dd=  SqliteStorage.getObject(aClass);
                map.put(aClass,dd);
                res= dd;
            }
        }
        if(actionBase== ReanimatorBase.ActionBase.save){
            Object o = map.get(aClass);
            SqliteStorage.saveObject(o,aClass);
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

    private enum ActionBase{
        get,save
    }
}
