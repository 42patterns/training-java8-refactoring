package com.foo.dictionary;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.PatternLayout;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.FileAppender;
import ch.qos.logback.core.encoder.LayoutWrappingEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class LoggerConfiguration {

    static public void configureLogger() {
        // assume SLF4J is bound to logback in the current environment
        LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
        // reset existing configurations
        lc.reset();

        //file appender
        FileAppender<ILoggingEvent> fa = new FileAppender<ILoggingEvent>();
        fa.setFile(System.getProperty("java.io.tmpdir") + File.separator + "dictionary.log");
        fa.setContext(lc);
        fa.setName("file");

        //default layout
        PatternLayout layout = new PatternLayout();
        layout.setPattern("%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n");

        layout.setContext(lc);
        layout.start();

        LayoutWrappingEncoder<ILoggingEvent> encoder = new LayoutWrappingEncoder<ILoggingEvent>();
        encoder.setContext(lc);
        encoder.setLayout(layout);

        fa.setEncoder(encoder);
        fa.start();

        ch.qos.logback.classic.Logger rootLogger = lc.getLogger(Logger.ROOT_LOGGER_NAME);
        rootLogger.addAppender(fa);
    }
}
