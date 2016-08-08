package com.settings.ion.mylibrary;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.Nullable;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Reanimator {

    private static final Map<Class, Object> map = new HashMap<>();
    protected static iListenerСhanges mIListener;
    private static String filePath = Environment.getExternalStorageDirectory().toString() + "/omsksettings";
    private static ReadWriteLock rwlock = new ReentrantReadWriteLock();

    /**
     * Регистрация пути, где будут храниться все  файлы настроек
     * по умолчанию: Environment.getExternalStorageDirectory().toString()+"/omsksettings";
     *
     * @param path
     */
    public static void setPathHostSettings(String path) {
        filePath = path;
    }

    /**
     * Регистрация слушателя обработчика изменеия файла настроек
     *
     * @param iListener
     */
    public static void onSetListenerСhanges(iListenerСhanges iListener) {
        mIListener = iListener;
    }

    /**
     * отказ от регистации слушателя обработчика изменеия файла настроек
     */
    public static void unOnSetListenerchanges() {
        mIListener = null;
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

    /**
     * Получение объекта настроек
     *
     * @param aClass тип объекта настроек
     * @return объект настроек
     */

    public static synchronized Object get(Class aClass) {
        if (map.containsKey(aClass)) {
            return map.get(aClass);
        } else {
            return getObject(aClass);
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private static synchronized Object getObject(Class aClass) {
        Object ob = null;
        try {
            ob = deserializer(aClass);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("serialise object.Reanimator");
        }
        if (ob != null) {
            map.put(aClass, ob);
            return ob;
        } else {
            Object oeb = null;
            try {
                oeb = aClass.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                throw new RuntimeException("could not create object.Reanimator");
            }
            serializer(oeb, aClass);
            map.put(aClass, oeb);
            return oeb;
        }
    }

    @Nullable
    private static Object deserializer(Class aClass) throws ClassNotFoundException {
        File f = new File(filePath + "/" + aClass.getName() + ".txt");
        if (f.exists()) {
            Object e;
            FileInputStream fileIn = null;
            ObjectInputStream in = null;
            try {

                try {
                    fileIn = new FileInputStream(filePath + "/" + aClass.getName() + ".txt");
                } catch (FileNotFoundException e1) {
                    throw  new RuntimeException("Setting FileNotFoundException:"+e1.getMessage());
                }
                in = new ObjectInputStream(fileIn);
                e = in.readObject();

                return e;
            } catch (IOException i) {
                i.printStackTrace();
                return null;
            } finally {
                if (fileIn != null) {
                    try {
                        fileIn.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        }
        return null;
    }

    private static synchronized void serializer(Object ob, Class aClass) {
        FileOutputStream fileOut = null;
        ObjectOutputStream out = null;
        File f = new File(filePath);

        if (!f.exists()) {
            boolean d = f.mkdirs();
            if (!d) {
                throw new RuntimeException(" reanimator:I can not create a directory settings-" + aClass.getName());
            }
        }
        try {
            fileOut = new FileOutputStream(filePath + "/" + aClass.getName() + ".txt");
            out = new ObjectOutputStream(fileOut);
            out.writeObject(ob);
        } catch (IOException e) {
           throw  new RuntimeException("reanimator:"+e.getMessage());
        } finally {
            if (fileOut != null) {
                try {
                    fileOut.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    /**
     * Сохранение объекта настроек на диск в файл (сериализация)
     *
     * @param aClass
     */
    public static synchronized void save(Class aClass) {
        Object o = map.get(aClass);
        if (o == null) {
            new RuntimeException("save reanimator settings: object not exists  from map");
        }
        if (filePath == null) {
            new RuntimeException("save reanimator settings:filePath null");
        }
        serializer(o, aClass);
    }

    public static String getHostPath() {
        return filePath;
    }
}
