package linq;

import java.util.Collection;
import java.util.Map;


public class Linq {
    public static <D> Session<D> toStream(Collection<D> collection) {
        return new Stream<>(collection);
    }

    public static <Tkey, TValue> SessionMap<Tkey, TValue> toStream(Map<Tkey, TValue> map) {
        return new StreamMap<>(map);
    }
}

