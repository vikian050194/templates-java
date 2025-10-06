package vanilla.app.config;

import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LoggerConfig {

    private static final String APP_LOGGER_NAME = "app";
    private static final String DEBUG_LOGGER_NAME = "debug";

    private static final String SUB_LOGGER_FORMAT = "%s.%s";

    private static void cleanGlobalLogger(Logger logger) {
        Handler[] handlers = logger.getHandlers();
        for (Handler handler : handlers) {
            logger.removeHandler(handler);
        }
    }

    private static void setupGlobalConsoleLogger(Logger logger, Formatter formatter) {
        var consoleHandler = new ConsoleHandler();
        consoleHandler.setFilter(f -> true);
        consoleHandler.setFormatter(formatter);
        logger.addHandler(consoleHandler);

    }

    private static void setupGlobalFileLogger(Logger logger, Formatter formatter) {
        try {
            // TODO get app name from ...
            var fileHandler = new FileHandler("vanilla.log", true);
            fileHandler.setFilter(f -> true);
            fileHandler.setFormatter(formatter);
            logger.addHandler(fileHandler);
        } catch (IOException ex) {
            logger.severe(ex.toString());
        }
    }

    private static void setGlobalDebuggerLoggerLevel() {
        if (RuntimeConfig.getInstance().runMode() == RuntimeConfig.RunMode.PROD) {
            getDebugLogger().setLevel(Level.SEVERE);
        } else {
            getDebugLogger().setLevel(Level.FINE);
        }
    }

    public static void setupGlobalLogger() {
        // TODO what is proper way to get global logger instance?
        var globalLogger = Logger.getLogger("");
        globalLogger.setLevel(Level.INFO);

        cleanGlobalLogger(globalLogger);

        var formatter = new CustomFormatter();

        setupGlobalConsoleLogger(globalLogger, formatter);
        setupGlobalFileLogger(globalLogger, formatter);

        setGlobalDebuggerLoggerLevel();
    }

    public static Logger getApplicationLogger() {
        return Logger.getLogger(APP_LOGGER_NAME);
    }

    public static Logger getApplicationLogger(String subLogger) {
        return Logger.getLogger(String.format(SUB_LOGGER_FORMAT, APP_LOGGER_NAME, subLogger));
    }

    public static Logger getDebugLogger() {
        return Logger.getLogger(DEBUG_LOGGER_NAME);
    }

    public static Logger getDebugLogger(String subLogger) {
        return Logger.getLogger(String.format(SUB_LOGGER_FORMAT, DEBUG_LOGGER_NAME, subLogger));
    }
}
