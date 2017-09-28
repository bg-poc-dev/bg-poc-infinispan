package com.bg.poc.bgpocinfinispan;

import infinispan.autoconfigure.InfinispanCacheConfigurer;
import infinispan.autoconfigure.InfinispanGlobalConfigurer;
import lombok.extern.java.Log;
import org.infinispan.Cache;
import org.infinispan.configuration.cache.CacheMode;
import org.infinispan.configuration.cache.Configuration;
import org.infinispan.configuration.cache.ConfigurationBuilder;
import org.infinispan.configuration.global.GlobalConfigurationBuilder;
import org.infinispan.manager.EmbeddedCacheManager;
import org.infinispan.spring.provider.SpringEmbeddedCacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;


@SpringBootApplication
@EnableCaching
@Log
public class BgPocInfinispanApplication {

    public static final String CACHE_NAME = "data";

    @Value("${cache.expiration.time}")
    private long expirationTime;

    @Value("${cache.storage.location}")
    private String storageLocation;

    @Value("${cache.cluster.name}")
    private String clusterName;

    @Bean
    public InfinispanGlobalConfigurer globalConfiguration() {
        log.info("Defining Global Configuration");
        return () -> GlobalConfigurationBuilder
                .defaultClusteredBuilder()
                .transport().clusterName(clusterName)
                .nodeName(System.currentTimeMillis() + "_node")
                .build();
    }

    @Bean
    public InfinispanCacheConfigurer cacheConfigurer() {
        log.info(String.format("Defining %s configuration", CACHE_NAME));
        return manager -> {
            Configuration ispnConfig = new ConfigurationBuilder()
                    .persistence()
                    .addSingleFileStore().location(storageLocation)
                    .clustering().cacheMode(CacheMode.REPL_ASYNC)
                    .expiration().lifespan(expirationTime)
                    .build();

            manager.defineConfiguration(CACHE_NAME, ispnConfig);
        };
    }

    @Bean
    public CacheManager cacheManager(@Autowired EmbeddedCacheManager embeddedCacheManager) {
        return new SpringEmbeddedCacheManager(embeddedCacheManager);
    }

    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(BgPocInfinispanApplication.class, args);


        EmbeddedCacheManager cacheManager = ctx.getBean(EmbeddedCacheManager.class);
        Cache<Long, String> cache = cacheManager.getCache(CACHE_NAME);
        cache.put(System.currentTimeMillis(), "Infinispan");

        log.info(String.format("Values from Cache: %s", cache.entrySet()));
    }
}
