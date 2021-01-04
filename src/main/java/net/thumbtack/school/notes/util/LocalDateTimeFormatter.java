package net.thumbtack.school.notes.util;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class LocalDateTimeFormatter {
    public static String format(LocalDateTime dateTime) {
        return dateTime == null?
                "'null'" :
                dateTime.format(DateTimeFormatter.ofPattern("''yyyy.MM.dd HH:mm:ss''"));
    }
}
