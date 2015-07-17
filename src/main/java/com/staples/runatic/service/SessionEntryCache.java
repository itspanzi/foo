package com.staples.runatic.service;

import com.staples.runatic.model.SessionEntry;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * This class represents a cache store for session entries
 */
public class SessionEntryCache {
    static final String STAPLES_ORDER_DATA_KEY = "staples_order_data_key";
    static final String EXTERNAL_ORDER_DATA_KEY = "external_order_data_key";

    private Map<String, Map<Long, SessionEntry>> cache = new ConcurrentHashMap<>();

    private Lock cacheLock = new ReentrantLock();

    public Map<Long, SessionEntry> get(String key, PopulateCache cachePopulator) {
        Map<Long, SessionEntry> cachedValue = cache.get(key);
        if (cachedValue == null) {
            try {
                obtainLock();
                // Another might have populated the cache. Check first.
                cachedValue = cache.get(key);
                if (cachedValue != null) {
                    return cachedValue;
                } else {
                    // Looks like no one has cached this yet. Lets cache it.
                    cachedValue = cachePopulator.populateCache();
                    cache.putIfAbsent(key, cachedValue);
                }
            } finally {
                cacheLock.unlock();
            }
        }
        return cachedValue;
    }

    private void obtainLock() {
        try {
            if (!cacheLock.tryLock(3, TimeUnit.SECONDS)) {
                throw new RuntimeException("Couldn't secure a lock. The server is busy. Please try again in sometime");
            }
        } catch (InterruptedException e) {
            throw new RuntimeException("Got interrupted during lock acquisition. Please try again in sometime");
        }
    }

    public interface PopulateCache {
        Map<Long, SessionEntry> populateCache();
    }
}
