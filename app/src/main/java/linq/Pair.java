package linq;


public class Pair<TKey, TValue> {

    private final TKey key;
    private final TValue value;

    public Pair(TKey tKey, TValue tValue) {
        this.key = tKey;
        this.value = tValue;
    }

    public TValue getValue() {
        return value;
    }

    public TKey getKey() {
        return key;
    }
}
