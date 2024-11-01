package com.cinema.cin.services.config;

import java.util.Calendar;
import java.util.logging.Level;

public class Configuration {
    public static final String DAO_IMPL = "MySQLJDBCImpl";
    public static final String DATABASE_DRIVER = "org.postgresql.Driver";
    public static final String SERVER_TIMEZONE = Calendar.getInstance().getTimeZone().getID();
    public static final String DATABASE_URL;
    public static final String COOKIE_IMPL = "CookieImpl";
    public static final String GLOBAL_LOGGER_NAME = "Cinema_log";
    public static final String GLOBAL_LOGGER_FILE = "/Users/marce/Documents/log/Cinema_log.%g.%u.txt";
    public static final Level GLOBAL_LOGGER_LEVEL;
    public Configuration() {
    }

    static {

        DATABASE_URL = "jdbc:postgresql://localhost/Cinema_web?user=postgres&password=145810&allowPublicKeyRetrieval=true&useSSL=false&ServerTimezone="+SERVER_TIMEZONE;
        GLOBAL_LOGGER_LEVEL = Level.ALL;
    }
}
