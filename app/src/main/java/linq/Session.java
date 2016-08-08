package linq;

import java.util.Collection;
import java.util.List;
import java.util.Map;


public interface Session<D> extends Collection<D> {
    /**
     * Возвращает последовательность элементов, где
     * какждый элемент удовлеторяет предикату
     *
     * @param iCompare
     * @return
     */
    Session<D> where(Predicate<D> iCompare);


    /**
     * Вытаскивает последовательность из базовой последовательности
     *
     * @param start началао  вытаскивания из базовой
     * @param count количество вытащенных элементов
     * @return
     */
    Session<D> limit(int start, int count);

    Session<D> limit(int count);

    <TRes> Session<TRes> select(Function<D, TRes> function);

    <TRes> Session<TRes> selectAsSet(Function<D, TRes> function);

    <TRes> Session<D> orderBy(final Function<D, TRes> function);

    <TRes> Session<D> thenBy(final Function<D, TRes> function);

    Collection<D> getCoreCollection();

    Session<D> removeIf(Predicate<D> iCompare);

    List<D> toList();

    D firstOrDefault(Predicate<D> dPredicate);

    boolean any(Predicate<D> dPredicate);

    boolean anyAll(Predicate<D> dPredicate);

    <T, Tkey> Map<Tkey, T> toMap(Function<D, Tkey> key, Function<D, T> value);

    <F> Session<F> castTo(Class<F> t);

    <Tkey> Map<Tkey, List<D>> groupBy(Function<D, Tkey> func);

    Number max(Function<D, Number> func);

    Number min(Function<D, Number> func);

    Number sum(Function<D, Number> func);

    D get(int index);

    boolean compare(Collection<D> collection, PredicateCopmare<D> iCompare);

    Session<D> forEach(Action<D> dAction);

    <TKey> Map<TKey, Collection<D>> croupBy(Function<D, TKey> key);

    int Count(Predicate<D> dPredicate);

    boolean Any(Predicate<D> dPredicate);

    boolean AnyAll(Predicate<D> dPredicate);

    Session<D> reverce();


}
