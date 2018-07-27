package com.nepxion.discovery.plugin.strategy.extension.configuration;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.netflix.ribbon.RibbonClientConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.nepxion.discovery.plugin.strategy.extension.aop.StrategyAutoScanProxy;
import com.nepxion.discovery.plugin.strategy.extension.aop.StrategyInterceptor;
import com.nepxion.discovery.plugin.strategy.extension.constant.StrategyConstant;
import com.nepxion.discovery.plugin.strategy.extension.enable.DiscoveryEnabledAdapter;
import com.nepxion.discovery.plugin.strategy.extension.enable.DiscoveryEnabledPredicate;
import com.nepxion.discovery.plugin.strategy.extension.enable.DiscoveryEnabledRule;

@Configuration
@AutoConfigureBefore(RibbonClientConfiguration.class)
@ConditionalOnProperty(value = StrategyConstant.SPRING_APPLICATION_STRATEGY_CONTROL_ENABLED, matchIfMissing = true)
public class StrategyAutoConfiguration {
    @Value("${" + StrategyConstant.SPRING_APPLICATION_STRATEGY_SCAN_PACKAGES + ":}")
    private String scanPackages;

    @Autowired
    private DiscoveryEnabledAdapter discoveryEnabledAdapter;

    @Bean
    @ConditionalOnMissingBean
    // @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public DiscoveryEnabledRule discoveryEnabledRule() {
        DiscoveryEnabledRule discoveryEnabledRule = new DiscoveryEnabledRule();
        DiscoveryEnabledPredicate discoveryEnabledPredicate = discoveryEnabledRule.getDiscoveryEnabledPredicate();
        discoveryEnabledPredicate.setDiscoveryEnabledAdapter(discoveryEnabledAdapter);

        return discoveryEnabledRule;
    }

    @Bean
    @ConditionalOnProperty(value = StrategyConstant.SPRING_APPLICATION_STRATEGY_BUSINESS_CONTEXT_CONTROL_ENABLED, matchIfMissing = true)
    public StrategyAutoScanProxy strategyAutoScanProxy() {
        return new StrategyAutoScanProxy(scanPackages);
    }

    @Bean
    @ConditionalOnProperty(value = StrategyConstant.SPRING_APPLICATION_STRATEGY_BUSINESS_CONTEXT_CONTROL_ENABLED, matchIfMissing = true)
    public StrategyInterceptor strategyInterceptor() {
        return new StrategyInterceptor();
    }
}