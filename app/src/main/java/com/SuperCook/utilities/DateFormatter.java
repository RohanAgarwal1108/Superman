package com.SuperCook.utilities;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateFormatter {
    /**
     * Format date to a particular format
     *
     * @param inputFormat  to get the format of the date to be processed
     * @param outputFormat to get the resulting format of the date
     * @param inputDate    to get the date to be processed
     */
    public static String formateDateFromstring(String inputFormat, String outputFormat, String inputDate) {

        Date parsed = null;
        String outputDate = "";

        SimpleDateFormat df_input = new SimpleDateFormat(inputFormat, Locale.getDefault());
        SimpleDateFormat df_output = new SimpleDateFormat(outputFormat, Locale.getDefault());

        try {
            parsed = df_input.parse(inputDate);
            outputDate = df_output.format(parsed);

        } catch (ParseException e) {
        }

        return outputDate;

    }

    /**
     * TO get the calendar object from a string of date
     */
    public static Calendar getCal(String string) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
        try {
            cal.setTime(sdf.parse(string));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return cal;
    }
}