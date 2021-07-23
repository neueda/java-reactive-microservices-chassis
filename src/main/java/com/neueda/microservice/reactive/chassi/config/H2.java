package com.neueda.microservice.reactive.chassi.config;

import org.h2.tools.Server;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class H2 {

    private Server webServer;

    private Server tcpServer;

    @EventListener(org.springframework.context.event.ContextRefreshedEvent.class)
    public void start() throws java.sql.SQLException {
        this.webServer = Server.createWebServer("-webPort", "8082", "-tcpAllowOthers").start();
        this.tcpServer = Server.createTcpServer("-tcpPort", "9092", "-tcpAllowOthers").start();
    }

    @EventListener(org.springframework.context.event.ContextClosedEvent.class)
    public void stop() {
        this.tcpServer.stop();
        this.webServer.stop();
    }

}
