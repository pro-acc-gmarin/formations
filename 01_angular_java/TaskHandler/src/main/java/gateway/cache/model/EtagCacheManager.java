package gateway.cache.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class EtagCacheManager<K, V> implements CacheManager<K, V>{

    public static final Long DEFAULT_CACHE_TIMEOUT = 60000L;

    protected Map<K, V> cacheMap = new HashMap<>();
    protected final Long cacheTimeout;

    public EtagCacheManager(){
        this(DEFAULT_CACHE_TIMEOUT);
    }

    public EtagCacheManager(final Long cacheTimeout){
        this.cacheTimeout = cacheTimeout;
        this.clear();
    }

    @Override
    public void clean() {}

    @Override
    public void clear() {
        this.cacheMap = new HashMap<>();
    }

    @Override
    public boolean containsKey(final K key) {
        return this.cacheMap.containsKey(key);
    }

    @Override
    public Optional<V> get(final K key) {
        if (this.cacheMap.containsKey(key)) {
            return Optional.of(this.cacheMap.get(key));
        }
        return Optional.empty();
    }

    @Override
    public void put(final K key, final V value) {
        this.cacheMap.put(key, value);
    }

    @Override
    public void remove(final K key) {
        this.cacheMap.remove(key);
    }

    public V getETag(final K key) {
        if (this.cacheMap.containsKey(key)) {
            return this.cacheMap.get(key);
        }
        return null;
    }
}
