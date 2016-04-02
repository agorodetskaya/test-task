package cachemap;

/**
 * A generic cache. Works just like a Map, except that entries automatically "disappear"
 * when they expire. <p>
 * <p>
 * For example, if you have a bunch of persons in a database or external system,
 * and you have a corresponding Person class with id of type Long, you can create a cache
 * with 60-second expiry like this: <p>
 * <pre>
 * CacheMap&lt;Long, Person&gt; cache = new CacheMapImpl&lt;Long, Person&gt;();
 * cache.setTimeToLive(60000);
 * </pre>
 * <p>
 * Your code for fetching a person by id would look something like this: <p>
 * <p>
 * <pre>
 * Person person = cache.get(personId);
 * if (person == null) {
 *   person = slowSystemThatShouldntBeUsedTooOften.getPerson(personId);
 *   cache.put(personId, person);
 * }
 * return person;
 * </pre>
 * <p>
 * Additional notes: <br>
 * <ul>
 * <li>
 * Implementations do not have to be thread-safe.
 * </li>
 * <li>
 * No methods should ever return or count any entries that have expired.
 * </li>
 * <li>
 * Implementations do not have to clear expired entries automatically
 * as soon as they expire.
 * It is OK to clean up expired entries in conjunction with method calls instead.
 * From the outside, however, it should look like entries disappear as soon as they
 * get expired.
 * </li>
 * <li>
 * For unit-testing purposes, this class should get the current time using
 * the Clock class, not System.currentTimeMillis(). That way unit tests can override the
 * time.
 * </li>
 * </ul>
 *
 * @see Clock
 */
public interface CacheMap<KeyType, ValueType> {

    /**
     * Sets how long new entries are kept in the cache. Until this method is called,
     * some kind of default value should apply.
     */
    void setTimeToLive(long timeToLive);

    long getTimeToLive();

    /**
     * Caches the given value under the given key.
     * <p>
     * If there already is an item under the given key, it will be replaced by the new value. <p>
     *
     * @param key  may not be null
     * @param value may be null, in which case the cache entry will be removed (if it existed).
     * @return the previous value, or null if none
     */
    ValueType put(KeyType key, ValueType value);

    /**
     * Clears all expired entries.
     * This is called automatically in conjuction with most operations,
     * but for memory optimization reasons you may call this explicitely at any time.
     */
    void clearExpired();

    /**
     * Removes all entries.
     */
    void clear();

    /**
     * Checks if the given key is included in this cache map.
     */
    boolean containsKey(KeyType key);

    /**
     * Checks if the given value is included in this cache map.
     */
    boolean containsValue(ValueType value);

    /**
     * Returns the value for the given key. Null if there is no value,
     * or if it has expired.
     */
    ValueType get(KeyType key);

    /**
     * True if this cache is empty.
     */
    boolean isEmpty();

    /**
     * Removes the given key.
     *
     * @param key the value of which must be removed
     * @return the previous value, if there was any
     */
    ValueType remove(KeyType key);

    /**
     * How many entries this cache map contains.
     */
    int size();

}
