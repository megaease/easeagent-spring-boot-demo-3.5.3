package com.megaease.easeagent.demo.balanced.config;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.*;
import org.springframework.cloud.client.loadbalancer.reactive.ReactiveLoadBalancer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Configuration
public class RestTemplateConfig {
    @Bean
    @LoadBalanced  // 关键注解：启用负载均衡
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate;
    }


//    @Bean
//    @LoadBalanced  // 启用负载均衡
//    public RestTemplate retryRestTemplate(LoadBalancerClient loadBalancer,
//                                          LoadBalancerRequestFactory requestFactory,
//                                          LoadBalancedRetryFactory lbRetryFactory,
//                                          ReactiveLoadBalancer.Factory<ServiceInstance> loadBalancerFactory) {
//        RestTemplate restTemplate = new RestTemplate();
//        RetryLoadBalancerInterceptor retryLoadBalancerInterceptor = new RetryLoadBalancerInterceptor(loadBalancer, requestFactory, lbRetryFactory, loadBalancerFactory);
//        restTemplate.setInterceptors(Collections.singletonList(retryLoadBalancerInterceptor));
//        return restTemplate;
//    }

}
