package com.ber.cache.caffeine;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import javax.jws.Oneway;
import java.util.concurrent.TimeUnit;

/**
 * @Author 鳄鱼儿
 * @Description Caffeine 使用示例
 * @date 2022/9/26 17:40
 * @Version 1.0
 * Caffeine是基于java8实现的新一代缓存工具，缓存性能接近理论最优。
 * 可以看作是Guava Cache的增强版，功能上两者类似
 * 不同的是Caffeine采用了一种结合LRU、LFU优点的算法：W-TinyLFU，在性能上有明显的优越性。
 */

public class CaffeineCacheTest {

    public static void main(String[] args) throws Exception {
        //创建guava cache
        Cache<String, String> loadingCache = Caffeine.newBuilder()
                //cache的初始容量
                .initialCapacity(5)
                //cache最大缓存数
                .maximumSize(10)
                //设置写缓存后n秒钟过期
                .expireAfterWrite(17, TimeUnit.SECONDS)
                //设置读写缓存后n秒钟过期,实际很少用到,类似于expireAfterWrite
                //.expireAfterAccess(17, TimeUnit.SECONDS)
                .build();
        String key = "key";
        // 往缓存写数据
        loadingCache.put(key, "value");

        // 获取value的值，如果key不存在，获取value后再返回
        String value = loadingCache.get(key, CaffeineCacheTest::getValueFromDB);

        System.out.println(value);

        // 删除key
        loadingCache.invalidate(key);
    }

    /**
     * 从数据源获取缓存数据
     *
     * @param key
     * @return
     */
    private static String getValueFromDB(String key) {
        return "获取新value";
    }
}