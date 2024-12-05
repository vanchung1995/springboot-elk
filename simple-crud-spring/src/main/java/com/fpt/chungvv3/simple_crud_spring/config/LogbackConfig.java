package com.fpt.chungvv3.simple_crud_spring.config;

import ch.qos.logback.classic.AsyncAppender;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.logstash.logback.appender.LogstashTcpSocketAppender;
import net.logstash.logback.encoder.LogstashEncoder;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class LogbackConfig {

    @Value("${logstash.url}")
    private String logstashUrl;

    @Value("${spring.application.name}")
    private String application;

    @Value("${spring.profiles.active}")
    private String environment;

    @Bean
    public Logger configureLogstashAppender() throws JsonProcessingException {
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        Logger rootLogger = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);

        // add new custom field to log
        Map<String, Object> customFieldsMap = new HashMap<>();
        customFieldsMap.put("application", application);
        customFieldsMap.put("environment", environment);
        ObjectMapper objectMapper = new ObjectMapper();
        String customFieldsJson = objectMapper.writeValueAsString(customFieldsMap);

        // Logstash encoder
        LogstashEncoder logstashEncoder = new LogstashEncoder();
        logstashEncoder.setContext(context);
        logstashEncoder.setCustomFields(customFieldsJson);
        logstashEncoder.start();

        // Logstash TCP appender: use tcp to send message to logstash (tcp is faster than http)
        LogstashTcpSocketAppender logstashAppender = new LogstashTcpSocketAppender();
        logstashAppender.setContext(context);
        logstashAppender.addDestination(logstashUrl); // Địa chỉ Logstash
        logstashAppender.setEncoder(logstashEncoder); // Gắn encoder vào appender
        logstashAppender.start();

        // Wrap logstashAppender with AsyncAppender: send log by batch, can set queue size to send
        AsyncAppender asyncAppender = new AsyncAppender();
        asyncAppender.setContext(context);
        asyncAppender.addAppender(logstashAppender);
        asyncAppender.start();

        // appender to logger root
        rootLogger.addAppender(logstashAppender);
        return rootLogger;
    }
}
