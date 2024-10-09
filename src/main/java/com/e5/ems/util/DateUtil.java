package com.e5.ems.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * <p>
 * Implementation to handle all Date utility methods
 * </p>
 */

public class DateUtil {
    /**
     * <p>
     * Calculate the years between two different dates.
     * </p>
     *
     * @param startDate The earlier date (must not be null).
     * @param endDate   The later date (must not be null).
     * @return The difference in years between the two dates.
     */
    public static int findDifferenceOfDates(Date startDate, Date endDate) {
        if (startDate == null || endDate == null) {
            return -1;
        }
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTime(startDate);
        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTime(endDate);
        // Calculate the difference in years
        int yearsDifference = endCalendar.get(Calendar.YEAR) - startCalendar.get(Calendar.YEAR);

        // Adjust for months and days
        if (endCalendar.get(Calendar.MONTH) < startCalendar.get(Calendar.MONTH) ||
                (endCalendar.get(Calendar.MONTH) == startCalendar.get(Calendar.MONTH) &&
                        endCalendar.get(Calendar.DAY_OF_MONTH) < startCalendar.get(Calendar.DAY_OF_MONTH))) {
            yearsDifference--;
        }
        return yearsDifference;
    }

    /**
     * <p>
     * Converts a date string in the format "yyyy-MM-dd" to a Date object.
     * </p>
     *
     * @param date it is used for convert string to date. Must be in the format "yyyy-MM-dd".
     * @return The corresponding Date object, or null if the input string is null or cannot be parsed.
     */
    public static Date strToDate(String date) throws ParseException {
        if (date == null) {
            return null;
        }
        // Parsing the date string to Date object and handle the parsing exception.
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            return sdf.parse(date);
        } catch (ParseException e) {
            throw new ParseException("Your Date format is wrong", e.getErrorOffset());
        }
    }

}