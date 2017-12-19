package com.svlada;

import com.svlada.common.utils.DateUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

import static com.svlada.common.utils.DateUtils.FULL_DATE_FORMAT;

public class BCryptPasswordEncoderTest {


    /*public static void main(String[] args) {
        LocalDateTime dateTime = LocalDateTime.now();
        System.out.println("dateTime = " + dateTime);
    }*/
    public static void main(String[] args) {
        Date arithmetic = DateUtils.arithmetic(new Date(), Calendar.MONTH, -1);
        System.out.println("arithmetic = " + arithmetic);
        Date endTimeOfMonth = DateUtils.getEndTimeOfMonth();
        System.out.println("endTimeOfMonth = " + endTimeOfMonth);
        Date startTimeOfMonth = DateUtils.getStartTimeOfMonth();
        System.out.println("startTimeOfMonth = " + startTimeOfMonth);
        Date endTimeOfToday = DateUtils.getEndTimeOfToday();
        System.out.println("endTimeOfToday = " + endTimeOfToday);
        Date startTimeOfToday = DateUtils.getStartTimeOfToday();
        System.out.println("startTimeOfToday = " + startTimeOfToday);
//        SimpleDateFormat format = new SimpleDateFormat(FULL_DATE_FORMAT);
        Calendar ca = Calendar.getInstance();
        ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));
        ca.set(Calendar.HOUR_OF_DAY, 0);
        ca.set(Calendar.MINUTE, 0);
        ca.set(Calendar.SECOND, 0);
        System.out.println("ca.getTime() = " + ca.getTime());
//        String last = format.format(ca.getTime());
//        Date time = ca.getTime();
//        System.out.println("time = " + time);
//        System.out.println("===============last:"+last);
        /*
        // Convert java.time.LocalDate to java.util.Date and back to
        // java.time.LocalDate
        LocalDate localDate = LocalDate.now();
        System.out.println("LocalDate = " + localDate);

        Date date1 = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        System.out.println("Date      = " + date1);

        localDate = date1.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        System.out.println("LocalDate = " + localDate);
        System.out.println();

        // Convert java.time.LocalDateTime to java.util.Date and back to
        // java.time.LocalDateTime
        LocalDateTime localDateTime = LocalDateTime.now();
        System.out.println("LocalDateTime = " + localDateTime);

        Date date2 = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
        System.out.println("Date          = " + date2);

        localDateTime = date2.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        System.out.println("LocalDateTime = " + localDateTime);*/
    }

    public static void testEncoder(){
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String clientSecret = bCryptPasswordEncoder.encode("clientSecret");
        boolean matches = bCryptPasswordEncoder.matches("clientSecret", clientSecret);
        System.out.println("matches = " + matches);
        System.out.println("clientSecret = " + clientSecret);
    }
}
