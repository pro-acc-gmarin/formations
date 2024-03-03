package gateway.cache.model;

import java.util.Optional;

/*
* https://medium.com/@marcellogpassos/creating-a-simple-and-generic-cache-manager-in-java-e62e4204a10e
 */
public interface CacheManager<K, V> {
    void clean();
    void clear();
    boolean containsKey(K key);
    Optional<V> get(K key);
    void put(K key, V value);
    void remove(K key);
}
