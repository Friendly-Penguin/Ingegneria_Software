package com.cinema.cin.services.logservice;

import com.cinema.cin.services.config.Configuration;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class LogService {
    private static Logger applicationLogger;

    private LogService() {
    }

    public static Logger getApplicationLogger() {

        SimpleFormatter formatterTxt;
        Handler fileHandler;

        try {

            if (applicationLogger == null) {

                applicationLogger = Logger.getLogger(Configuration.GLOBAL_LOGGER_NAME);
                fileHandler = new FileHandler(Configuration.GLOBAL_LOGGER_FILE, true);
                formatterTxt = new SimpleFormatter();
                fileHandler.setFormatter(formatterTxt);
                applicationLogger.addHandler(fileHandler);
                applicationLogger.setLevel(Configuration.GLOBAL_LOGGER_LEVEL);
                applicationLogger.setUseParentHandlers(false);
                applicationLogger.log(Level.CONFIG, "Logger: {0} created.", applicationLogger.getName());

            }

        } catch (IOException e) {
            applicationLogger.log(Level.SEVERE, "Error occured in Logger creation", e);
            throw new RuntimeException(e);
        }
        return applicationLogger;

    }

}