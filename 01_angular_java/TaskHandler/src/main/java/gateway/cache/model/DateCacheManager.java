package gateway.cache.model;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class DateCacheManager<K, V> implements CacheManager<K, V>{

    public static final Long DEFAULT_CACHE_TIMEOUT = 60000L;

    protected Map<K, DateCacheValue<V>> cacheMap = new HashMap<>();
    protected final Long cacheTimeout;

    public DateCacheManager(){
        this(DEFAULT_CACHE_TIMEOUT);
    }

    public DateCacheManager(final Long cacheTimeout){
        this.cacheTimeout = cacheTimeout;
        this.clear();
    }

    @Override
    public void clean() {
        for(final K key : this.getExpiredKeys()){
            this.remove(key);
        }
    }

    private Set<K> getExpiredKeys(){
        return this.cacheMap.keySet().parallelStream()
                .filter(this::isExpired)
                .collect(Collectors.toSet());
    }

    private boolean isExpired(final K key) {
        final long expirationDateTime = this.cacheMap.get(key).getCreatedAt() + DEFAULT_CACHE_TIMEOUT;
        return Instant.now().getEpochSecond() > (expirationDateTime);
    }
    @Override
    public void clear() {
        this.cacheMap.clear();
    }

    @Override
    public boolean containsKey(final K key) {
        return this.cacheMap.containsKey(key);
    }

    @Override
    public Optional<V> get(final K key) {
        // On nettoie la map avant de récupérer une valeur.
        this.clean();
        return Optional.ofNullable(this.cacheMap.get(key)).map(DateCacheValue::getValue);
    }

    @Override
    public void put(K key, V value) {
        this.cacheMap.put(key, this.createCacheValue(value));
    }

    public Long getCreatedAt(final K key) {
        if (this.cacheMap.containsKey(key)) {
            return this.cacheMap.get(key).getCreatedAt();
        }
        return null;
    }

    protected DateCacheValue<V> createCacheValue(V value) {
        long now = Instant.now().getEpochSecond();
        return new DateCacheValue<V>() {
            @Override
            public V getValue() {
                return value;
            }

            @Override
            public Long getCreatedAt() {
                return now;
            }
        };
    }

    @Override
    public void remove(K key) {
        this.cacheMap.remove(key);
    }

    protected interface DateCacheValue<V> {
        V getValue();
        Long getCreatedAt();
    }
}
