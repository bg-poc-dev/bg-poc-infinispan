package com.bg.poc.bgpocinfinispan.cache;

import com.bg.poc.bgpocinfinispan.BgPocInfinispanApplication;
import com.bg.poc.bgpocinfinispan.domain.Data;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class DataCache {

    @Cacheable(value = BgPocInfinispanApplication.CACHE_NAME)
    public Data findData(String key) {
        return new Data(key, String.valueOf(System.currentTimeMillis()));
    }


}
