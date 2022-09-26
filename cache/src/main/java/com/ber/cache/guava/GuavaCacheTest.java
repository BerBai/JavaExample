package com.ber.cache.guava;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * @Author 鳄鱼儿
 * @Description Guava Cache使用示例
 * @date 2022/9/26 17:27
 * @Version 1.0
 * Guava是Google团队开源的一款 Java 核心增强库，包含集合、并发原语、缓存、IO、反射等工具箱，性能和稳定性上都有保障，应用十分广泛。
 * Guava Cache支持很多特性：
 * 支持最大容量限制
 * 支持两种过期删除策略（插入时间和访问时间）
 * 支持简单的统计功能
 * 基于LRU算法实现
 */

public class GuavaCacheTest {

    public static void main(String[] args) throws Exception {
        //创建guava cache
        Cache<String, String> loadingCache = CacheBuilder.newBuilder()
                //cache的初始容量
                .initialCapacity(5)
                //cache最大缓存数
                .maximumSize(10)
                //设置写缓存后n秒钟过期
                .expireAfterWrite(17, TimeUnit.SECONDS)
                //设置读写缓存后n秒钟过期,实际很少用到,类似于expireAfterWrite
                //.expireAfterAccess(17, TimeUnit.SECONDS)
                .build();
        // 缓存的key值
        String key = "key";
        // 往缓存写数据
//        loadingCache.put(key, "value");

        // 获取value的值，如果key不存在，调用collable方法获取value值加载到key中再返回
        // 方法一：匿名内部类
        /*String value = loadingCache.get(key, new Callable<String>() {
            @Override
            public String call() throws Exception {
                return getValueFromDB(key);
            }
        });*/

        // 方法二：Lambda表达式实现函数式接口
        String value = loadingCache.get(key, () -> getValueFromDB(key));

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