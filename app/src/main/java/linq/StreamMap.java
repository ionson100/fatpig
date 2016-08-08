package linq;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

class StreamMap<TKey, TValue> implements SessionMap<TKey, TValue> {

    private final Map<TKey, TValue> mMap;

    public StreamMap(Map<TKey, TValue> map) {
        this.mMap = map;//pair
    }

    @Override
    public SessionMap<TKey, TValue> whereKey(Predicate<TKey> key) {
        Map<TKey, TValue> map = new HashMap<>();
        for (TKey tkey : mMap.keySet()) {
            if (key.apply(tkey)) {
                map.put(tkey, map.get(tkey));
            }
        }
        return new StreamMap<>(map);
    }


    @Override
    public SessionMap<TKey, TValue> whereValue(Predicate<TValue> value) {
        Map<TKey, TValue> map = new HashMap<>();
        for (TKey tkey : mMap.keySet()) {
            TValue val = mMap.get(tkey);
            if (value.apply(val)) {
                map.put(tkey, val);
            }
        }
        return new StreamMap<>(map);
    }

    @Override
    public Pair<TKey, TValue> firstOrDefault(Predicate<TValue> valuePredicate) {
        for (TKey tkey : mMap.keySet()) {
            if (valuePredicate.apply(mMap.get(tkey))) {
                return new Pair<>(tkey, mMap.get(tkey));
            }
        }
        return null;
    }

    @Override
    public Number max(Function<TValue, Number> func) {
        Number acum = null;
        for (TValue d : mMap.values()) {
            acum = Utils.maxNumbers(acum, func.foo(d));
        }
        return acum;
    }

    @Override
    public Number min(Function<TValue, Number> func) {
        Number acum = null;
        for (TValue d : mMap.values()) {
            acum = Utils.minNumbers(acum, func.foo(d));
        }
        return acum;
    }

    @Override
    public Number sum(Function<TValue, Number> func) {
        Number acum = null;
        for (TValue d : mMap.values()) {
            Number sd = func.foo(d);
            acum = Utils.addNumbers(acum, sd);
        }
        return acum;
    }

    @Override
    public List<Pair<TKey, TValue>> toList() {
        List<Pair<TKey, TValue>> res = new ArrayList<>();
        for (TKey tkey : mMap.keySet()) {
            TValue d = mMap.get(tkey);
            res.add(new Pair<>(tkey, d));
        }
        return res;
    }

    @Override
    public boolean any(Predicate<TValue> predicate) {
        for (TValue d : mMap.values()) {
            if (predicate.apply(d)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean anyAll(Predicate<TValue> predicate) {
        boolean res = true;
        for (TValue d : mMap.values()) {
            if (!predicate.apply(d)) {
                res = false;
                break;
            }
        }
        return res;
    }

    @Override
    public Map<TKey, TValue> toMap() {
        return mMap;
    }

    @Override
    public int size() {
        return mMap.size();
    }

    @Override
    public boolean isEmpty() {
        return mMap.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return mMap.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return mMap.containsValue(value);
    }

    @Override
    public TValue get(Object key) {
        return mMap.get(key);
    }

    @Override
    public TValue put(TKey key, TValue value) {
        return mMap.put(key, value);
    }

    @Override
    public TValue remove(Object key) {
        return mMap.remove(key);
    }

    @Override
    public void putAll(@NonNull Map<? extends TKey, ? extends TValue> m) {
        mMap.putAll(m);
    }

    @Override
    public void clear() {
        mMap.clear();
    }

    @NonNull
    @Override
    public Set<TKey> keySet() {
        return mMap.keySet();
    }

    @NonNull
    @Override
    public Collection<TValue> values() {
        return mMap.values();
    }

    @NonNull
    @Override
    public Set<Entry<TKey, TValue>> entrySet() {
        return mMap.entrySet();
    }
}
