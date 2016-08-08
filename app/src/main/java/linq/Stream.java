package linq;


import android.annotation.TargetApi;
import android.os.Build;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;


class Stream<T> implements Session<T> {

    private List<Compare> dComparators = new ArrayList<>();
    private Collection<T> mCollection;

    public Stream(Collection<T> tList) {
        if (tList == null) {
            throw new IllegalArgumentException(" method ctor. Stream, argument tList = null");
        }
        this.mCollection = tList;
    }

    @Override
    public boolean add(T object) {
        if (object == null) {
            throw new IllegalArgumentException(" method add, argument object = null");
        }
        return mCollection.add(object);
    }

    @Override
    public boolean addAll(@NonNull Collection<? extends T> collection) {

        return mCollection.addAll(collection);
    }

    @Override
    public void clear() {
        mCollection.clear();
    }

    @Override
    public boolean contains(Object object) {
        return mCollection.contains(object);
    }

    @Override
    public boolean containsAll(@NonNull Collection<?> collection) {
        return mCollection.containsAll(collection);
    }

    @Override
    public boolean isEmpty() {
        return mCollection.isEmpty();
    }


    @NonNull
    @Override
    public Iterator<T> iterator() {
        return mCollection.iterator();
    }

    @Override
    public boolean remove(Object object) {
        if (object == null) {
            throw new IllegalArgumentException(" method remove, argument object = null");
        }
        return mCollection.remove(object);
    }

    @Override
    public boolean removeAll(@NonNull Collection<?> collection) {
        if (collection == null) {
            throw new IllegalArgumentException(" method removeAll, argument collection = null");
        }
        return mCollection.removeAll(collection);
    }

    @Override
    public boolean retainAll(@NonNull Collection<?> collection) {
        return false;
    }

    @Override
    public int size() {
        return mCollection.size();
    }


    @NonNull
    @Override
    public Object[] toArray() {
        return mCollection.toArray();
    }


    @Override
    public <F> F[] toArray(F[] array) {
        return mCollection.toArray(array);
    }

    @Override
    public Stream<T> where(Predicate<T> iCompare) {
        Collection<T> collection = getFactoryCollections();
        for (T t : mCollection) {
            boolean sd = iCompare.apply(t);
            if (sd) {
                collection.add(t);
            }
        }
        return new Stream<>(collection);
    }

    @Override
    public Session<T> limit(int start, int count) {
        if (start < 0 || count < 0) {
            throw new IllegalArgumentException(" method limit, argument start <0 or count <0");
        }

        Collection<T> collection = getFactoryCollections();
        int i = 0 - 1;
        for (T t : mCollection) {
            i++;
            if (i < start) continue;
            if (i < start + count) {
                collection.add(t);
            } else {
                break;
            }
        }
        return new Stream<>(collection);
    }

    @Override
    public Session<T> limit(int count) {
        if (count < 0) {
            throw new IllegalArgumentException(" method limit, argument count < 0");
        }
        Collection<T> collection = getFactoryCollections();
        int i = 0 - 1;
        for (T t : mCollection) {
            i++;
            if (i < count) {
                collection.add(t);
            } else {
                break;
            }
        }
        return new Stream<>(collection);
    }

    @Override
    public <TRes> Stream<TRes> select(Function<T, TRes> function) {
        Collection<TRes> list = new ArrayList<>();
        for (T t : mCollection) {
            list.add(function.foo(t));
        }
        return new Stream<>(list);
    }

    @Override
    public <Tres> Stream<Tres> selectAsSet(Function<T, Tres> function) {
        Collection<Tres> list = new HashSet<>();
        for (T t : mCollection) {
            list.add(function.foo(t));
        }
        return new Stream<>(list);
    }

    @Override
    public <TRes> Stream<T> orderBy(final Function<T, TRes> function) {
        dComparators = new ArrayList<>();
        Compare compare = new Compare((Function<T, Object>) function);
        dComparators.add(compare);
        final List<T> list = new ArrayList<>(mCollection);
        Collections.sort(list, compare);
        Stream<T> f = new Stream<>(list);
        f.dComparators = dComparators;
        return f;
    }

    @Override
    public <TRes> Stream<T> thenBy(Function<T, TRes> function) {
        if (dComparators.size() == 0) {
            throw new RuntimeException(" not exist pre orderBy");
        }
        final Compare compare = new Compare((Function<T, Object>) function);
        dComparators.add(compare);
        final List<T> list = new ArrayList<>(mCollection);
        Collections.sort(list, new Comparator<T>() {
            @Override
            public int compare(T lhs, T rhs) {
                return recursionCompare(dComparators, 0, lhs, rhs, dComparators.size());
            }

        });

        Stream<T> f = new Stream<>(list);
        f.dComparators = dComparators;
        return f;

    }

    @Override
    public Collection<T> getCoreCollection() {
        return mCollection;
    }

    @Override
    public Stream<T> removeIf(Predicate<T> iCompare) {
        List<T> delete = new ArrayList<>();
        for (T d : mCollection) {
            if (iCompare.apply(d)) {
                delete.add(d);
            }
        }
        mCollection.removeAll(delete);
        return new Stream<>(mCollection);
    }

    private int recursionCompare(List<Compare> c, int i, T d1, T d2, int listSize) {
        int sComp = c.get(i).compare(d1, d2);

        if (sComp != 0) {
            return sComp;
        } else {
            int ii = i;
            int ii1 = listSize - 1;
            if (ii < ii1) {
                i = i + 1;
                return recursionCompare(c, i, d1, d2, listSize);
            } else {
                return c.get(i).compare(d1, d2);
            }
        }
    }

    public List<T> toList() {
        return new ArrayList<>(mCollection);
    }

    public Stream<T> reverse() {
        Collection<T> collection = getFactoryCollections();
        List<T> list = new ArrayList<>(mCollection);
        Collections.reverse(list);
        collection.addAll(list);
        return new Stream<>(collection);
    }

    public T firstOrDefault(Predicate<T> dPredicate) {
        T res = null;
        for (T d : this) {
            if (dPredicate.apply(d)) {
                res = d;
                break;
            }
        }
        return res;
    }

    public boolean any(Predicate<T> dPredicate) {
        for (T d : mCollection) {
            if (dPredicate.apply(d)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean anyAll(Predicate<T> predicate) {
        boolean res = true;
        for (T d : mCollection) {
            if (!predicate.apply(d)) {
                res = false;
                break;
            }
        }
        return res;
    }

    @Override
    public <TValue, TKey> Map<TKey, TValue> toMap(Function<T, TKey> key, Function<T, TValue> value) {
        Map<TKey, TValue> map = new HashMap<>();
        for (T d : mCollection) {
            map.put(key.foo(d), value.foo(d));
        }
        return map;
    }

    @Override
    public <F> Stream<F> castTo(Class<F> t) {
        List<F> list = new ArrayList<>(mCollection.size());
        for (T d : mCollection) {
            if (d == null) continue;
            list.add((F) d);
        }
        return new Stream<>(list);
    }

    @Override
    public <TRes> Map<TRes, List<T>> groupBy(Function<T, TRes> func) {
        Map<TRes, List<T>> hashMap = new HashMap<>();
        for (T d : mCollection) {
            TRes o = func.foo(d);
            if (!hashMap.containsKey(o)) {
                List<T> list = new ArrayList<>();
                list.add(d);
                hashMap.put(o, list);
            } else {
                hashMap.get(o).add(d);
            }
        }
        return hashMap;
    }

    @Override
    public Number max(Function<T, Number> func) {
        Number acum = null;
        for (T d : mCollection) {
            acum = Utils.maxNumbers(acum, func.foo(d));
        }
        return acum;
    }

    @Override
    public Number min(Function<T, Number> func) {
        Number acum = null;
        for (T d : mCollection) {
            acum = Utils.minNumbers(acum, func.foo(d));
        }
        return acum;
    }

    @Override
    public Number sum(Function<T, Number> func) {
        Number acum = null;
        for (T d : mCollection) {
            Number sd = func.foo(d);
            acum = Utils.addNumbers(acum, sd);
        }
        return acum;
    }

    @Override
    public T get(int index) {

        if (mCollection instanceof List<?>) {
            return ((List<T>) mCollection).get(index);
        }
        throw new RuntimeException("ksajkdajsdkdasjdksjadkasdj");
    }

    @Override
    public boolean compare(Collection<T> collection, PredicateCopmare<T> iCompare) {
        if (collection == null || this.size() != collection.size()) {
            return false;
        }
        int i = 0;
        for (T t : collection) {
            if (!iCompare.apply(this.get(i), t)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public Session<T> forEach(Action<T> tAction) {
        for (T t : this) {
            tAction.action(t);
        }
        return this;
    }

    @Override
    public <TKey> Map<TKey, Collection<T>> croupBy(Function<T, TKey> key) {
        Map<TKey, Collection<T>> tMap = new HashMap<>();
        for (T t : this) {
            TKey tKey = key.foo(t);
            if (tMap.containsKey(tKey)) {
                Collection<T> ts = tMap.get(tKey);
                ts.add(t);
            } else {
                Collection<T> ts = getFactoryCollections();
                ts.add(t);
                tMap.put(tKey, ts);
            }
        }
        return tMap;
    }

    @Override
    public int Count(Predicate<T> tPredicate) {
        int i = 0;
        for (T t : this) {
            if (tPredicate.apply(t)) {
                i += 1;
            }
        }
        return i;
    }

    @Override
    public boolean Any(Predicate<T> tPredicate) {
        for (T t : this) {
            if (tPredicate.apply(t)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean AnyAll(Predicate<T> tPredicate) {

        int i = 0;
        for (T t : this) {
            if (tPredicate.apply(t)) {
                i += 1;
            }
        }
        return this.size() == i;
    }

    @Override
    public Session<T> reverce() {

        Collection<T> ts = getFactoryCollections();
        for (int i = this.size() - 1; i >= 0; i--) {
            T t = this.get(i);
            ts.add(t);
        }
        mCollection = ts;
        return this;

    }

    private Collection<T> getFactoryCollections() {
        if (mCollection instanceof Set) {
            return new HashSet<>();
        }
        if (mCollection instanceof Queue) {
            return new LinkedList<>();
        }
        if (mCollection instanceof List) {
            return new ArrayList<>();
        }

        throw new RuntimeException("not determine type collection");
    }

    private class Compare implements Comparator<T> {

        private final Function<T, Object> action;

        public Compare(Function<T, Object> action) {
            this.action = action;
        }

        @TargetApi(Build.VERSION_CODES.KITKAT)
        @Override
        public int compare(T lhs, T rhs) {
            Object oo1 = action.foo(lhs);
            Object oo2 = action.foo(rhs);

            if (oo1 == oo2) {
                return 0;
            }
            if (oo1 == null) {
                return -1;
            }
            if (oo2 == null) {
                return 1;
            }


            if (oo1 instanceof String) return oo1.toString().compareTo(oo2.toString());
            else if (oo1 instanceof Integer) return Integer.compare((Integer) oo1, (Integer) oo2);
            else if (oo1 instanceof Float) return Float.compare((Float) oo1, (Float) oo2);
            else if (oo1 instanceof Long) return Long.compare((Long) oo1, (Long) oo2);
            else if (oo1 instanceof Double) return Double.compare((Double) oo1, (Double) oo2);
            else if (oo1 instanceof Boolean) return Boolean.compare((Boolean) oo1, (Boolean) oo2);
            else if (oo1 instanceof Short) return Short.compare((Short) oo1, (Short) oo2);
            else if (oo1 instanceof Byte) return Byte.compare((Byte) oo1, (Byte) oo2);
            else throw new RuntimeException("this write go");
        }
    }

}


