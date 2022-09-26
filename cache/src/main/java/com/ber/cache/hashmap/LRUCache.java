package com.ber.cache.hashmap;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @Author 鳄鱼儿
 * @Description LinkedHashMap维持了一个链表结构，用来存储节点的插入顺序或者访问顺序
 * 并且内部封装了一些业务逻辑，只需要覆盖removeEldestEntry方法，便可以实现缓存的LRU淘汰策略。
 * 利用读写锁，保障缓存的并发安全性。
 * 不支持过期时间淘汰
 * @date 2022/9/26 17:02
 * @Version 1.0
 */

public class LRUCache extends LinkedHashMap {

    /**
     * 可重入读写锁，保证并发读写安全性
     */
    private ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    private Lock readLock = readWriteLock.readLock();
    private Lock writeLock = readWriteLock.writeLock();

    /**
     * 缓存大小限制
     */
    private int maxSize;

    public LRUCache(int maxSize) {
        // 参数：initialCapacity - 初始容量； loadFactor – 负载因子； accessOrder – 排序模式（访问顺序为true ，插入顺序为false）
        super(maxSize + 1, 1.0f, true);
        this.maxSize = maxSize;
    }

    @Override
    public Object get(Object key) {
        readLock.lock();
        try {
            return super.get(key);
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public Object put(Object key, Object value) {
        writeLock.lock();
        try {
            return super.put(key, value);
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry eldest) {
        return this.size() > maxSize;
    }
}

