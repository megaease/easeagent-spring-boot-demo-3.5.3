/*
 * Copyright (c) 2021, MegaEase
 * All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.megaease.easeagent.demo.balanced.rest;

import com.megaease.easeagent.demo.balanced.model.Greeting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.atomic.AtomicLong;

@RestController
public class RestTemplateController {
    @Autowired
    RestTemplate restTemplate;
    private final AtomicLong counter = new AtomicLong();
    private static final Logger LOGGER = LoggerFactory.getLogger(RestTemplateController.class.getName());

    @Value("${server.port}")
    int port;


    @GetMapping("/rest_template")
    public Greeting greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
        LOGGER.info("get rest_template name: {}", name);
        LOGGER.info("rest load balancer Interceptor class:");
        for (ClientHttpRequestInterceptor interceptor : restTemplate.getInterceptors()) {
            LOGGER.info(interceptor.getClass().getName());
        }
        System.out.println("Starting BLOCKING Controller!");

        String response = restTemplate.getForObject(
                "http://say-hello/example",
                String.class
        );

        Greeting result = new Greeting(counter.incrementAndGet(), response + " name: " + name);
        System.out.println(result);
        System.out.println("Exiting BLOCKING Controller!");
        return result;
    }

    @GetMapping("/rest_template_code")
    public Greeting deleteGreeting(@RequestParam(value = "name", defaultValue = "World") String name) {
        LOGGER.info("code rest_template name: {}", name);
        LOGGER.info("rest load balancer Interceptor class:");
        for (ClientHttpRequestInterceptor interceptor : restTemplate.getInterceptors()) {
            LOGGER.info(interceptor.getClass().getName());
        }
        System.out.println("Starting BLOCKING Controller!");


        String response = restTemplate.getForObject(
                "http://say-hello/example?code=500",
                String.class
        );

        Greeting result = new Greeting(counter.incrementAndGet(), response + " name: " + name);
        System.out.println(result);
        System.out.println("Exiting BLOCKING Controller!");
        return result;
    }
}
