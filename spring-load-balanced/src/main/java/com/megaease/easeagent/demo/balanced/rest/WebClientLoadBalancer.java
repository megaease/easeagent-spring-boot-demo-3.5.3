package com.megaease.easeagent.demo.balanced.rest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.reactive.LoadBalancedExchangeFilterFunction;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RestController
@Slf4j
public class WebClientLoadBalancer {
    @Autowired
    WebClient.Builder loadBalancedWebClientBuilder;
    @Autowired
    LoadBalancedExchangeFilterFunction lbFunction;

    @RequestMapping("/hi")
    public Mono<String> hi(@RequestParam(value = "name", defaultValue = "Mary") String name) {
        return loadBalancedWebClientBuilder.build().get().uri("http://say-hello/example")
                .retrieve().bodyToMono(String.class)
                .map(greeting -> String.format("%s, %s!", greeting, name));
    }

    @RequestMapping("/hello")
    public Mono<String> helloRlb(@RequestParam(value = "name", defaultValue = "John") String name) {
        log.info("lb name: {}", lbFunction.getClass().getName());
        return WebClient.builder()
                .filter(lbFunction)
                .build().get().uri("http://say-hello/example")
                .retrieve().bodyToMono(String.class)
                .map(greeting -> String.format("%s, %s!", greeting, name));
    }

    @RequestMapping("/hello_code")
    public Mono<String> helloCode(@RequestParam(value = "name", defaultValue = "John") String name) {
        log.info("lb name: {}", lbFunction.getClass().getName());
        return WebClient.builder()
                .filter(lbFunction)
                .build().get().uri("http://say-hello/example?code=500")
                .retrieve().bodyToMono(String.class)
                .map(greeting -> String.format("%s, %s!", greeting, name));
    }
}
