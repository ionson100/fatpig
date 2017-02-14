package com.settings.ion.mylibrary;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.ContextWrapper;
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

public class ReanimatorFile extends ContextWrapper {


    private static final Map<Class, Object> map = new HashMap<>();
   // public static Context context;

    public static String filePath = Environment.getExternalStorageDirectory().toString() + "/omsksettings";
    private static ReadWriteLock rwlock = new ReentrantReadWriteLock();

    public ReanimatorFile(Context base) {
        super(base);
    }

    private static ReanimatorFile reanimator;
    public static void intContext(Context context){
        reanimator=new ReanimatorFile(context);
    }
    public static Context getContext(){
      return   reanimator.getApplicationContext();
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
