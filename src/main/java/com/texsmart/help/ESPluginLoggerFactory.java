package com.texsmart.help;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.spi.ExtendedLogger;

/**
 * @project: elasticsearch-analysis-texsmart
 * @description: logger
 * @author: wei_liu
 * @create: 2020-09-09 15:10
 */
public class ESPluginLoggerFactory {

    private ESPluginLoggerFactory() {
    }

    static public Logger getLogger(String name) {
        return getLogger("", LogManager.getLogger(name));
    }

    static public Logger getLogger(String prefix, String name) {
        return getLogger(prefix, LogManager.getLogger(name));
    }

    static public Logger getLogger(String prefix, Class<?> clazz) {
        return getLogger(prefix, LogManager.getLogger(clazz.getName()));
    }

    static public Logger getLogger(String prefix, Logger logger) {
        return (Logger)(prefix != null && prefix.length() != 0 ? new PrefixPluginLogger((ExtendedLogger)logger, logger.getName(), prefix) : logger);
    }
}

