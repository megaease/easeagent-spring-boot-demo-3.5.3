/*
 * Copyright (c) 2017, MegaEase
 * All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.megaease.easeagent.demo.jdkserver;

import com.sun.net.httpserver.*;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.BiFunction;
import java.util.function.Consumer;

public class JdkHttpServer {
    private final int port;
    private final Consumer<HttpExchange> beforeF;
    private final BiFunction<HttpExchange, String, String> afterF;
    private final HttpServer server;

    private static final AtomicLong atomicLong = new AtomicLong();

    public JdkHttpServer(Consumer<HttpExchange> beforeF, BiFunction<HttpExchange, String, String> afterF, int port) throws IOException {
        this.beforeF = beforeF;
        this.afterF = afterF;
        this.server = HttpServer.create(new InetSocketAddress(port), 0);
        this.port = port;
    }


    public void startHttpServer() throws IOException {
        HttpContext context = server.createContext("/example");
        context.setHandler(JdkHttpServer.this::handleRequest);
        server.start();
    }

    public void stop() {
        server.stop(0);
    }

    public void handleRequest(HttpExchange exchange) throws IOException {
        beforeF.accept(exchange);
        URI requestURI = exchange.getRequestURI();
        printRequestInfo(exchange);
        String response = String.format("This is the response at %s port: %s", requestURI, port);
        exchange.sendResponseHeaders(getCode(requestURI), response.getBytes().length);
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
        afterF.apply(exchange, response);
        exchange.close();
    }

    public int getCode(URI requestURI) {
        String query = requestURI.getQuery();
        if (query == null) {
            return 200;
        }
        String[] queryItems = query.split("&");
        for (String queryItem : queryItems) {
            String[] keyValue = queryItem.split("=");
            if (keyValue.length < 2) {
                continue;
            }
            if (keyValue[0].equals("code") && keyValue[1].matches("\\d+") && atomicLong.incrementAndGet() % 2 == 0) {
                return Integer.parseInt(keyValue[1]);
            }
        }
        return 200;
    }


    public void printRequestInfo(HttpExchange exchange) {
        System.out.println(String.format("--------- server port:%s ---------------", port));
        System.out.println("-- headers --");
        Headers requestHeaders = exchange.getRequestHeaders();
        requestHeaders.entrySet().forEach(System.out::println);

        System.out.println("-- principle --");
        HttpPrincipal principal = exchange.getPrincipal();
        System.out.println(principal);

        System.out.println("-- HTTP method --");
        String requestMethod = exchange.getRequestMethod();
        System.out.println(requestMethod);

        System.out.println("-- query --");
        URI requestURI = exchange.getRequestURI();
        String query = requestURI.getQuery();
        System.out.println(query);
    }

    public static void main(String[] args) throws IOException {
        new JdkHttpServer((a) -> {
            a.getResponseHeaders().add("X-EG-Circuit-Breaker", "bbbb");
        }, (a, b) -> "", 8500).startHttpServer();
    }
}
