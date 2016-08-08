package orm;

import java.util.Dictionary;
import java.util.Hashtable;


class CacheDictionary {
    private static final Object lock = new Object();
    private static final Dictionary<String, cacheMetaDate> dic = new Hashtable();

    public static cacheMetaDate getCacheMetaDate(Class aClass) {
        if (dic.get(aClass.getName()) == null) {
            synchronized (lock) {
                if (dic.get(aClass.getName()) == null) {
                    dic.put(aClass.getName(), new cacheMetaDate<>(aClass));
                }
            }
        }
        return dic.get(aClass.getName());
    }
}
