package com.ber.cache.encache;

import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;

/**
 * @Author 鳄鱼儿
 * @Description Encache 使用示例
 * @date 2022/9/26 17:49
 * @Version 1.0
 * Encache是一个纯Java的进程内缓存框架，具有快速、精干等特点，是Hibernate中默认的CacheProvider。
 * 同Caffeine和Guava Cache相比，Encache的功能更加丰富，扩展性更强：
 * 支持多种缓存淘汰算法，包括LRU、LFU和FIFO
 * 缓存支持堆内存储、堆外存储、磁盘存储（支持持久化）三种
 * 支持多种集群方案，解决数据共享问题
 */

public class EncacheTest {

    public static void main(String[] args) throws Exception {
        // 声明一个cacheBuilder
        CacheManager cacheManager = CacheManagerBuilder.newCacheManagerBuilder()
                .withCache("encacheInstance", CacheConfigurationBuilder
                        //声明一个容量为20的堆内缓存
                        .newCacheConfigurationBuilder(String.class, String.class, ResourcePoolsBuilder.heap(20)))
                .build(true);
        // 获取Cache实例
        Cache<String, String> myCache = cacheManager.getCache("encacheInstance", String.class, String.class);
        // 写缓存
        myCache.put("key", "v");
        // 读缓存
        String value = myCache.get("key");

        System.out.println(value);
        // 移除缓存
        cacheManager.removeCache("myCache");
        cacheManager.close();
    }
}

