package com.bg.poc.bgpocinfinispan.cache;

import com.bg.poc.bgpocinfinispan.BgPocInfinispanApplication;
import com.bg.poc.bgpocinfinispan.domain.Data;
import com.bg.poc.bgpocinfinispan.domain.Quoter;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class DataCache {

    @Cacheable(value = BgPocInfinispanApplication.CACHE_NAME)
    public Data findData(String key) {
        return new Data(key, String.valueOf(System.currentTimeMillis()));
    }

    @Cacheable(value = BgPocInfinispanApplication.CACHE_NAME)
    public Quoter quoter(String key) {
        return null;
    }

    @CachePut(value = BgPocInfinispanApplication.CACHE_NAME, key = "#key")
    public Quoter quoterPut(String key, Quoter quoter) {
        return quoter;
    }
}