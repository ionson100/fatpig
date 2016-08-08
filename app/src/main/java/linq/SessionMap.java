package linq;


import java.util.List;
import java.util.Map;


public interface SessionMap<Tkey, TValue> extends Map<Tkey, TValue> {

    SessionMap<Tkey, TValue> whereKey(Predicate<Tkey> key);

    SessionMap<Tkey, TValue> whereValue(Predicate<TValue> value);

    Pair<Tkey, TValue> firstOrDefault(Predicate<TValue> valuePredicate);

    Number max(Function<TValue, Number> func);

    Number min(Function<TValue, Number> func);

    Number sum(Function<TValue, Number> func);

    List<Pair<Tkey, TValue>> toList();

    boolean any(Predicate<TValue> predicate);

    boolean anyAll(Predicate<TValue> predicate);

    Map<Tkey, TValue> toMap();


}

