/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package appointmentscheduler;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 *
 * @author ADMIN
 */
public class TimeConversionMethods {
    
    /**
     * This method is used to convert date time inputted in appointment field to UTC time zone before storing in database
     * @param datetime This parameter refers to the date time which needs to converted to UTC
     * @return string date time in UTC version
     */
    public static String toUTC(String datetime) {
        Timestamp ts = Timestamp.valueOf(datetime);
        LocalDateTime ldt = ts.toLocalDateTime();
        ZonedDateTime zdt = ldt.atZone(ZoneId.of(ZoneId.systemDefault().toString()));
        ZonedDateTime utczdt = zdt.withZoneSameInstant(ZoneId.of("UTC"));

        return utczdt.toString();
    }
     /**
     * This method is used to convert date time from database to Local time zone of user before displaying it
     * @param datetime This parameter refers to the date time which needs to converted to Local time
     * @return string date time in Local zone version
     */
    public static String toLocal(String datetime) {
        Timestamp ts = Timestamp.valueOf(datetime);
        LocalDateTime ldt = ts.toLocalDateTime();
        ZonedDateTime zdt = ldt.atZone(ZoneId.of("UTC"));
        ZonedDateTime utczdt = zdt.withZoneSameInstant(ZoneId.of(ZoneId.systemDefault().toString()));
        return utczdt.toString();

    }
}
