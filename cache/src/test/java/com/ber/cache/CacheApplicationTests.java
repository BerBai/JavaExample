package com.ber.cache;

import com.ber.cache.hashmap.LRUCache;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Iterator;
import java.util.Map;

@SpringBootTest
class CacheApplicationTests {

    @Test
    void LRUCacheTest() {
        // 最近最少使用淘汰策略，初始容量为10
        LRUCache cache = new LRUCache(10);
        for (int i = 0; i < 11; i++) {
            cache.put(i, i);
            Iterator<Map.Entry<Object, Object>> it = cache.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<Object, Object> next = it.next();
                System.out.print(" " + next.getValue());
            }
            System.out.println();
        }
        cache.get(1);
        cache.put(11,11);
        Iterator<Map.Entry<Object,Object>> it = cache.entrySet().iterator();
        while(it.hasNext()) {
            Map.Entry<Object, Object> next = it.next();
            System.out.print(" " + next.getValue());
        }
    }

}
