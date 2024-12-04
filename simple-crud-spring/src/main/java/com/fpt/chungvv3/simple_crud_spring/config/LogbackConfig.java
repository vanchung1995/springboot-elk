package com.fpt.chungvv3.simple_crud_spring.config;

import ch.qos.logback.classic.AsyncAppender;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import net.logstash.logback.appender.LogstashTcpSocketAppender;
import net.logstash.logback.encoder.LogstashEncoder;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LogbackConfig {

    @Value("${logstash.url}")
    private String logstashUrl;

    @Bean
    public Logger configureLogstashAppender() {
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        Logger rootLogger = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);

        // Logstash encoder
        LogstashEncoder logstashEncoder = new LogstashEncoder();
        logstashEncoder.setContext(context);
        logstashEncoder.start();

        // Logstash TCP appender
        LogstashTcpSocketAppender logstashAppender = new LogstashTcpSocketAppender();
        logstashAppender.setContext(context);
        logstashAppender.addDestination(logstashUrl); // Địa chỉ Logstash
        logstashAppender.setEncoder(logstashEncoder); // Gắn encoder vào appender
        logstashAppender.start();

        // Wrap logstashAppender with AsyncAppender
        AsyncAppender asyncAppender = new AsyncAppender();
        asyncAppender.setContext(context);
        asyncAppender.addAppender(logstashAppender);
        asyncAppender.start();

        // Gắn appender vào logger root
        rootLogger.addAppender(logstashAppender);
        return rootLogger;
    }
}


