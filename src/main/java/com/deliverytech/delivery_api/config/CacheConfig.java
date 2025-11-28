package com.deliverytech.delivery_api.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CacheConfig {

    @Bean
    public CacheManager cacheManager() {
        // nomes de caches usados na aplicação
        return new ConcurrentMapCacheManager("restaurantes", "produtos", "pedidos", "clientes");
    }
}