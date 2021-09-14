package com.hoppinzq.service.config;

import com.hoppinzq.service.plugin.CacheFilter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;

@ConditionalOnWebApplication
public class MyFilter {

    @Bean
    public CacheFilter cacheFilter() {
        return new CacheFilter();
    }
}
