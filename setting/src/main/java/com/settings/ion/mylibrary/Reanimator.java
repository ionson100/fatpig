package com.settings.ion.mylibrary;

import android.content.Context;
import android.os.Environment;


public class Reanimator {

    protected static iListenerСhanges mIListener;

    private final static boolean isBase = true;

    public static final String filePath = Environment.getExternalStorageDirectory().toString() + "/omsksettings";

    public static Object get(Class aClass) {

        if (isBase) {
            return ReanimatorBase.get(aClass);
        } else {
            return ReanimatorFile.get(aClass);
        }
    }

    public static void save(Class aClass) {
        if (isBase) {
            ReanimatorBase.save(aClass);
        } else {
            ReanimatorFile.save(aClass);
        }
    }

    public static void onSetListenerСhanges(iListenerСhanges iListener) {
        mIListener = iListener;
    }


    public static String getHostPath() {
        return filePath;
    }

    /**
     * Генерация события измеения объекта настроек
     *
     * @param thisObject объект в котором произошла генерация
     * @param fieldName  название поля, которое изменилось
     * @param oldValue   старое значение поля
     * @param newValue   новое значение поля
     */
     public static void notify(Object thisObject, String fieldName, Object oldValue, Object newValue) {
         if (mIListener != null) {
             mIListener.OnCallListen(thisObject.getClass(), fieldName, oldValue, newValue);
         }
    }

    /**
     * Генерация события измения состояния объекта настроек
     *
     * @param aClass тип объекта настроек
     */
    public static void notify(Class aClass) {
        if (mIListener != null) {
            mIListener.OnCallListen(aClass, null, null, null);
        }
    }

    public static void intContext(Context context) {
        if (isBase) {
            ReanimatorBase.intContext(context);
        } else {
            ReanimatorFile.intContext(context);
        }
    }

    public static Context getContext() {

        if (isBase) {
           return ReanimatorBase.getContext();
        } else {
           return ReanimatorFile.getContext();
        }
    }
}






