package com.rutershok.daily.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Objects;

public class DateUtil {

    private static final String PATTERN = "yyyy-MM-dd";

    public static String getLocalFormatDate(String strDate) {
            try {
                    return DateFormat.getDateInstance().format(Objects.requireNonNull(new SimpleDateFormat(PATTERN, Locale.getDefault()).parse(strDate)));
            } catch (ParseException e) {
                e.printStackTrace();
            }

        return strDate;
    }

    public static String getWeekDayName(String strDate) {
        try {
                return new SimpleDateFormat("EEEE", Locale.getDefault()).format(Objects.requireNonNull(new SimpleDateFormat(PATTERN, Locale.getDefault()).parse(strDate)));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        return "";
    }
}
