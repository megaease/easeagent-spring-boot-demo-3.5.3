package com.megaease.easeagent.demo.gateway.config;

import com.megaease.easeagent.demo.jdkserver.JdkHttpServerPool;
import org.springframework.cloud.client.DefaultServiceInstance;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import reactor.core.publisher.Flux;

import java.util.Arrays;
import java.util.List;

public class DemoServiceInstanceListSuppler implements ServiceInstanceListSupplier {

    private final String serviceId;

    DemoServiceInstanceListSuppler(String serviceId) {
        this.serviceId = serviceId;
    }

    @Override
    public String getServiceId() {
        return serviceId;
    }

    @Override
    public Flux<List<ServiceInstance>> get() {
        int[] ports = JdkHttpServerPool.ports();
        return Flux.just(Arrays
                .asList(new DefaultServiceInstance(serviceId + "1", serviceId, "localhost", ports[0], false),
//						new DefaultServiceInstance(serviceId + "2", serviceId, "localhost", 9095, false),
                        new DefaultServiceInstance(serviceId + "3", serviceId, "localhost", ports[1], false)));
    }
}