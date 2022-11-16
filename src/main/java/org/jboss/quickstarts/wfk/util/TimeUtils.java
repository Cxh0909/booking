package org.jboss.quickstarts.wfk.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatterBuilder;


public class TimeUtils {
    private static final String PATTERN = "yyyy-MM-dd HH:mm:ss";

    public static LocalDate parse(String date) {
        try {
            return LocalDate.parse(date, new DateTimeFormatterBuilder().appendPattern(PATTERN).parseStrict().toFormatter());
        } catch (Exception e) {
            return null;
        }
    }
}
