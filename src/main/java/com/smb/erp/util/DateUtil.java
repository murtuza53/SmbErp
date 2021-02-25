package com.smb.erp.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedList;

public class DateUtil {

    public static SimpleDateFormat FORMATTER = new SimpleDateFormat("dd/MM/yyyy");
    public static SimpleDateFormat MONTH_YEAR_FORMAT = new SimpleDateFormat("MMM-yy");

    private static Calendar CALENDAR = Calendar.getInstance();

    /**
     * Returns the date with current time set
     *
     * @param date Date to calculate end of day from
     * @return <code>date</code>
     */
    public static Date setCurrentTime(Date date) {
        Calendar cal = Calendar.getInstance();
        int hr = cal.get(Calendar.HOUR_OF_DAY);
        int min = cal.get(Calendar.MINUTE);
        int sec = cal.get(Calendar.SECOND);
        cal.set(DateUtil.getYear(date), DateUtil.getMonth(date), DateUtil.getDay(date), hr, min, sec);
        return cal.getTime();
    }
    
    /**
     * Returns the last millisecond of the specified date.
     *
     * @param date Date to calculate end of day from
     * @return Last millisecond of <code>date</code>
     */
    public static Date endOfDay(Date date) {
        Calendar calendar = CALENDAR;
        if (date == null) {
            date = new Date();
        }
        synchronized (calendar) {
            calendar.setTime(date);
            calendar.set(Calendar.HOUR_OF_DAY, 23);
            calendar.set(Calendar.MILLISECOND, 999);
            calendar.set(Calendar.SECOND, 59);
            calendar.set(Calendar.MINUTE, 59);
            return calendar.getTime();
        }
    }

    /**
     * Returns a new Date with the hours, milliseconds, seconds and minutes set
     * to 0.
     *
     * @param date Date used in calculating start of day
     * @return Start of <code>date</code>
     */
    public static Date startOfDay(Date date) {
        if (date == null) {
            date = new Date();
        }
        Calendar calendar = CALENDAR;
        synchronized (calendar) {
            calendar.setTime(date);
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MINUTE, 0);
            return calendar.getTime();
        }
    }

    public static Date startOfMonth(Date date) {
        return new Date(getStartOfMonth(date.getTime()));
    }

    public static Date endOfMonth(Date date) {
        return new Date(getEndOfMonth(date.getTime()));
    }

    /**
     * Returns day in millis with the hours, milliseconds, seconds and minutes
     * set to 0.
     *
     * @param date long used in calculating start of day
     * @return Start of <code>date</code>
     */
    public static long startOfDayInMillis(long date) {
        Calendar calendar = CALENDAR;
        synchronized (calendar) {
            calendar.setTimeInMillis(date);
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MINUTE, 0);
            return calendar.getTimeInMillis();
        }
    }

    /**
     * Returns the last millisecond of the specified date.
     *
     * @param date long to calculate end of day from
     * @return Last millisecond of <code>date</code>
     */
    public static long endOfDayInMillis(long date) {
        Calendar calendar = CALENDAR;
        synchronized (calendar) {
            calendar.setTimeInMillis(date);
            calendar.set(Calendar.HOUR_OF_DAY, 23);
            calendar.set(Calendar.MILLISECOND, 999);
            calendar.set(Calendar.SECOND, 59);
            calendar.set(Calendar.MINUTE, 59);
            return calendar.getTimeInMillis();
        }
    }

    /**
     * Returns the day after <code>date</code>.
     *
     * @param date Date used in calculating next day
     * @return Day after <code>date</code>.
     */
    public static Date nextDay(Date date) {
        return new Date(addDays(date.getTime(), 1));
    }

    /**
     * Adds <code>amount</code> days to <code>time</code> and returns the
     * resulting time.
     *
     * @param time Base time
     * @param amount Amount of increment.
     */
    public static long addDays(long time, int amount) {
        Calendar calendar = CALENDAR;
        synchronized (calendar) {
            calendar.setTimeInMillis(time);
            calendar.add(Calendar.DAY_OF_MONTH, amount);
            return calendar.getTimeInMillis();
        }
    }

    /**
     * Adds <code>amount</code> days to <code>time</code> and returns the
     * resulting time.
     *
     * @param date Base time
     * @param days Amount of increment.
     */
    public static Date addDays(Date date, int days) {
        Calendar calendar = CALENDAR;
        synchronized (calendar) {
            calendar.setTimeInMillis(date.getTime());
            calendar.add(Calendar.DAY_OF_MONTH, days);
            return calendar.getTime();
        }
    }

    /**
     * Adds <code>amount</code> days to <code>time</code> and returns the
     * resulting time.
     *
     * @param date Base time
     * @param months Amount of increment.
     */
    public static Date addMonths(Date date, int months) {
        Calendar calendar = CALENDAR;
        synchronized (calendar) {
            calendar.setTimeInMillis(date.getTime());
            calendar.add(Calendar.MONTH, months);
            return calendar.getTime();
        }
    }

    /**
     * Returns the day after <code>date</code>.
     *
     * @param date Date used in calculating next day
     * @return Day after <code>date</code>.
     */
    public static long nextDay(long date) {
        return addDays(date, 1);
    }

    /**
     * Returns the week after <code>date</code>.
     *
     * @param date Date used in calculating next week
     * @return week after <code>date</code>.
     */
    public static long nextWeek(long date) {
        return addDays(date, 7);
    }

    /**
     * Returns the number of days difference between <code>t1</code> and
     * <code>t2</code>.
     *
     * @param t1 Time 1
     * @param t2 Time 2
     * @param checkOverflow indicates whether to check for overflow
     * @return Number of days between <code>start</code> and <code>end</code>
     */
    public static int getDaysDiff(long t1, long t2, boolean checkOverflow) {
        if (t1 > t2) {
            long tmp = t1;
            t1 = t2;
            t2 = tmp;
        }
        Calendar calendar = CALENDAR;
        synchronized (calendar) {
            calendar.setTimeInMillis(t1);
            int delta = 0;
            while (calendar.getTimeInMillis() < t2) {
                calendar.add(Calendar.DAY_OF_MONTH, 1);
                delta++;
            }
            if (checkOverflow && (calendar.getTimeInMillis() > t2)) {
                delta--;
            }
            return delta;
        }
    }

    /**
     * Returns the number of days difference between <code>t1</code> and
     * <code>t2</code>.
     *
     * @param t1 Time 1
     * @param t2 Time 2
     * @return Number of days between <code>start</code> and <code>end</code>
     */
    public static int getDaysDiff(long t1, long t2) {
        return getDaysDiff(t1, t2, true);
    }

    /*
     * This methods returns true if the date passed in is the first day of the
     * year.
     */
    public static boolean isFirstOfYear(long date) {
        boolean ret = false;
        Calendar calendar = CALENDAR;
        synchronized (calendar) {
            calendar.setTimeInMillis(date);
            int currentYear = calendar.get(Calendar.YEAR);
            // Check yesterday
            calendar.add(Calendar.DATE, -1);
            int yesterdayYear = calendar.get(Calendar.YEAR);
            ret = (currentYear != yesterdayYear);
        }
        return ret;
    }

    /*
     * This methods returns true if the date passed in is the first day of the
     * month.
     */
    public static boolean isFirstOfMonth(long date) {
        boolean ret = false;
        Calendar calendar = CALENDAR;
        synchronized (calendar) {
            calendar.setTimeInMillis(date);
            int currentMonth = calendar.get(Calendar.MONTH);
            // Check yesterday
            calendar.add(Calendar.DATE, -1);
            int yesterdayMonth = calendar.get(Calendar.MONTH);
            ret = (currentMonth != yesterdayMonth);
        }
        return ret;
    }

    /**
     * Returns the day before <code>date</code>.
     *
     * @param date Date used in calculating previous day
     * @return Day before <code>date</code>.
     */
    public static long previousDay(long date) {
        return addDays(date, -1);
    }

    /**
     * Returns the week before <code>date</code>.
     *
     * @param date Date used in calculating previous week
     * @return week before <code>date</code>.
     */
    public static long previousWeek(long date) {
        return addDays(date, -7);
    }

    /**
     * Returns the first day before <code>date</code> that has the day of week
     * matching <code>startOfWeek</code>. For example, if you want to find the
     * previous monday relative to <code>date</code> you would call
     * <code>getPreviousDay(date, Calendar.MONDAY)</code>.
     *
     * @param date Base date
     * @param startOfWeek Calendar constant correspoding to start of week.
     * @return start of week, return value will have 0 hours, 0 minutes, 0
     * seconds and 0 ms.
     *
     */
    public static long getPreviousDay(long date, int startOfWeek) {
        return getDay(date, startOfWeek, -1);
    }

    /**
     * Returns the first day after <code>date</code> that has the day of week
     * matching <code>startOfWeek</code>. For example, if you want to find the
     * next monday relative to <code>date</code> you would call
     * <code>getPreviousDay(date, Calendar.MONDAY)</code>.
     *
     * @param date Base date
     * @param startOfWeek Calendar constant correspoding to start of week.
     * @return start of week, return value will have 0 hours, 0 minutes, 0
     * seconds and 0 ms.
     *
     */
    public static long getNextDay(long date, int startOfWeek) {
        return getDay(date, startOfWeek, 1);
    }

    private static long getDay(long date, int startOfWeek, int increment) {
        Calendar calendar = CALENDAR;
        synchronized (calendar) {
            calendar.setTimeInMillis(date);
            int day = calendar.get(Calendar.DAY_OF_WEEK);
            // Normalize the view starting date to a week starting day
            while (day != startOfWeek) {
                calendar.add(Calendar.DATE, increment);
                day = calendar.get(Calendar.DAY_OF_WEEK);
            }
            return startOfDayInMillis(calendar.getTimeInMillis());
        }
    }

    /**
     * Returns the previous month.
     *
     * @param date Base date
     * @return previous month
     */
    public static long getPreviousMonth(long date) {
        return incrementMonth(date, -1);
    }

    /**
     * Returns the next month.
     *
     * @param date Base date
     * @return next month
     */
    public static long getNextMonth(long date) {
        return incrementMonth(date, 1);
    }

    private static long incrementMonth(long date, int increment) {
        Calendar calendar = CALENDAR;
        synchronized (calendar) {
            calendar.setTimeInMillis(date);
            calendar.add(Calendar.MONTH, increment);
            return calendar.getTimeInMillis();
        }
    }

    /**
     * Returns the date corresponding to the start of the month.
     *
     * @param date Base date
     * @return Start of month.
     */
    public static long getStartOfMonth(long date) {
        return getMonth(date, -1);
    }

    /**
     * Returns the date corresponding to the end of the month.
     *
     * @param date Base date
     * @return End of month.
     */
    public static long getEndOfMonth(long date) {
        return getMonth(date, 1);
    }

    private static long getMonth(long date, int increment) {
        Calendar calendar = CALENDAR;
        synchronized (calendar) {
            calendar.setTimeInMillis(date);
            if (increment == -1) {
                calendar.set(Calendar.DAY_OF_MONTH, 1);
                return startOfDayInMillis(calendar.getTimeInMillis());
            } else {
                calendar.add(Calendar.MONTH, 1);
                calendar.set(Calendar.DAY_OF_MONTH, 1);
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.MILLISECOND, 0);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.add(Calendar.MILLISECOND, -1);
                return calendar.getTimeInMillis();
            }
        }
    }

    /**
     * Returns the day of the week.
     *
     * @param date date
     * @return day of week.
     */
    public static int getDayOfWeek(long date) {
        Calendar calendar = CALENDAR;
        synchronized (calendar) {
            calendar.setTimeInMillis(date);
            return (calendar.get(Calendar.DAY_OF_WEEK));
        }
    }

    public static Date startOfYear(Date date) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(date);
        return new GregorianCalendar(cal.get(Calendar.YEAR), Calendar.JANUARY,
                1).getTime();
    }

    public static Date endOfYear(Date date) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(date);
        return new GregorianCalendar(cal.get(Calendar.YEAR), Calendar.DECEMBER,
                31).getTime();
    }

    public static String formatDate(Date date) {
        return formatDate(date, FORMATTER.toPattern());
    }

    public static String formatDate(Date date, String pattern) {
        return new SimpleDateFormat(pattern).format(date);
    }

    public static String formatMonthYear(Date date){
        return MONTH_YEAR_FORMAT.format(date);
    }
    
    public static boolean isToday(Date date) {
        if (getDaysDiff(date.getTime(), new Date().getTime()) == 0) {
            return true;
        }
        return false;
    }

    public static boolean isThisMonth(Date date) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(date);
        int month = cal.get(Calendar.MONTH);
        int year = cal.get(Calendar.YEAR);

        cal.setTime(new Date());
        if (month == cal.get(Calendar.MONTH) && year == cal.get(Calendar.YEAR)) {
            return true;
        }
        return false;
    }

    public static boolean isThisYear(Date date) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(date);
        int month = cal.get(Calendar.YEAR);

        cal.setTime(new Date());
        if (month == cal.get(Calendar.YEAR)) {
            return true;
        }
        return false;
    }

    public static boolean isPast(Date date) {
        return (date.getTime() - new Date().getTime()) < 0 ? true : false;
    }

    // return start of the month (that days starting)
    public static Date startOfMonth(int month) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(new Date());
        Date start = startOfDay(new GregorianCalendar(cal.get(Calendar.YEAR),
                month, 1).getTime());

        return start;
    }

    // return end of the month (that days starting)
    public static Date endOfMonth(int month) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(new Date());
        Date end = endOfDay(new Date(getEndOfMonth(new GregorianCalendar(cal.get(Calendar.YEAR), month, 1).getTimeInMillis())));

        return end;
    }

    public static Date startOfMonth(int month, int year) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(new Date());
        Date start = startOfDay(new GregorianCalendar(year, month, 1).getTime());

        return start;
    }

    // return end of the month (that days starting)
    public static Date endOfMonth(int month, int year) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(new Date());
        Date end = endOfDay(new Date(getEndOfMonth(new GregorianCalendar(
                year, month, 1).getTimeInMillis())));

        return end;
    }

    /**
     * Returns date with early time of the day
     */
    public static Date startOfDate(int day, int month, int year) {
        return startOfDay(new GregorianCalendar(year, month, day).getTime());
    }

    /**
     * Returns date with mid day time
     */
    public static Date middleOfDate(int day, int month, int year) {
        Date date = startOfDay(new GregorianCalendar(year, month, 1).getTime());
        Calendar calendar = CALENDAR;
        synchronized (calendar) {
            calendar.setTime(date);
            calendar.set(Calendar.HOUR_OF_DAY, 12);
            calendar.set(Calendar.MILLISECOND, 500);
            calendar.set(Calendar.SECOND, 30);
            calendar.set(Calendar.MINUTE, 30);
            return calendar.getTime();
        }
    }

    /**
     * Returns date with end of the day time month: 0-11 for Jan-Dec
     */
    public static Date endOfDate(int day, int month, int year) {
        return endOfDay(new GregorianCalendar(year, month, day).getTime());
    }

    /**
     * Returns date with specified time of the day month: 0-11 for Jan-Dec
     */
    public static Date createDate(int day, int month, int year, int hour,
            int min, int sec, int ms) {
        Calendar calendar = CALENDAR;
        synchronized (calendar) {
            calendar.setTime(createDate(day, month, year));
            calendar.set(Calendar.HOUR_OF_DAY, hour);
            calendar.set(Calendar.MILLISECOND, ms);
            calendar.set(Calendar.SECOND, sec);
            calendar.set(Calendar.MINUTE, min);
            return calendar.getTime();
        }
    }

    /**
     * Returns date with current time of the day
     */
    public static Date createDate(int day, int month, int year) {
        Calendar cal = CALENDAR;
        cal.set(cal.YEAR, year);
        cal.set(cal.MONTH, month);
        cal.set(cal.DATE, day);

        return cal.getTime();
    }

    public static String spellMonth(Date date) {
        Calendar calendar = CALENDAR;
        synchronized (calendar) {
            calendar.setTimeInMillis(date.getTime());
            return spellMonth((calendar.get(Calendar.MONTH)));
        }
    }

    public static String spellMonthWithYear(Date date) {
        String month = "";
        Calendar calendar = CALENDAR;
        synchronized (calendar) {
            calendar.setTimeInMillis(date.getTime());
            month = spellMonthShort((calendar.get(Calendar.MONTH)));
        }

        return month + " " + (calendar.get(Calendar.YEAR) + "").substring(2);
    }

    public static String spellMonth(int month) {
        switch (month) {
            case Calendar.JANUARY:
                return "January";
            case Calendar.FEBRUARY:
                return "February";
            case Calendar.MARCH:
                return "March";
            case Calendar.APRIL:
                return "April";
            case Calendar.MAY:
                return "May";
            case Calendar.JUNE:
                return "June";
            case Calendar.JULY:
                return "July";
            case Calendar.AUGUST:
                return "August";
            case Calendar.SEPTEMBER:
                return "September";
            case Calendar.OCTOBER:
                return "October";
            case Calendar.NOVEMBER:
                return "November";
            default:
                return "December";
        }
    }

    public static String spellMonthShort(int month) {
        switch (month) {
            case Calendar.JANUARY:
                return "Jan";
            case Calendar.FEBRUARY:
                return "Feb";
            case Calendar.MARCH:
                return "Mar";
            case Calendar.APRIL:
                return "Apr";
            case Calendar.MAY:
                return "May";
            case Calendar.JUNE:
                return "Jun";
            case Calendar.JULY:
                return "Jul";
            case Calendar.AUGUST:
                return "Aug";
            case Calendar.SEPTEMBER:
                return "Sep";
            case Calendar.OCTOBER:
                return "Oct";
            case Calendar.NOVEMBER:
                return "Nov";
            default:
                return "Dec";
        }
    }

    public static int getMonthFromSpelling(String month) {
        if (month.equalsIgnoreCase("January") || month.equalsIgnoreCase("Jan")) {
            return Calendar.JANUARY;
        } else if (month.equalsIgnoreCase("February") || month.equalsIgnoreCase("Feb")) {
            return Calendar.FEBRUARY;
        } else if (month.equalsIgnoreCase("March") || month.equalsIgnoreCase("Mar")) {
            return Calendar.MARCH;
        } else if (month.equalsIgnoreCase("April") || month.equalsIgnoreCase("Apr")) {
            return Calendar.APRIL;
        } else if (month.equalsIgnoreCase("May")) {
            return Calendar.MAY;
        } else if (month.equalsIgnoreCase("June") || month.equalsIgnoreCase("Jun")) {
            return Calendar.JUNE;
        } else if (month.equalsIgnoreCase("JULY") || month.equalsIgnoreCase("JUL")) {
            return Calendar.JULY;
        } else if (month.equalsIgnoreCase("August") || month.equalsIgnoreCase("Aug")) {
            return Calendar.AUGUST;
        } else if (month.equalsIgnoreCase("September") || month.equalsIgnoreCase("Sep")) {
            return Calendar.SEPTEMBER;
        } else if (month.equalsIgnoreCase("October") || month.equalsIgnoreCase("Oct")) {
            return Calendar.OCTOBER;
        } else if (month.equalsIgnoreCase("November") || month.equalsIgnoreCase("Nov")) {
            return Calendar.NOVEMBER;
        } else {
            return Calendar.DECEMBER;
        }
    }

    public static Collection<String> getMonthArrayShortName() {
        Collection<String> months = new LinkedList<String>();
        for (int i = 0; i < 12; i++) {
            months.add(spellMonthShort(i));
        }
        return months;
    }

    public static Collection<String> getMonthArrayFullName() {
        Collection<String> months = new LinkedList<String>();
        for (int i = 0; i < 12; i++) {
            months.add(spellMonth(i));
        }
        return months;
    }

    public static Date cloneDate(Date date) {
        if (date == null) {
            date = new Date();
        }
        Calendar calendar = CALENDAR;
        synchronized (calendar) {
            calendar.setTimeInMillis(date.getTime());
        }
        return createDate(calendar.get(GregorianCalendar.DATE), calendar.get(GregorianCalendar.MONTH), calendar.get(GregorianCalendar.YEAR), calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE),
                calendar.get(Calendar.SECOND), calendar.get(Calendar.MILLISECOND));
    }

    public static int getMonth(Date date) {
        if (date == null) {
            date = new Date();
        }
        Calendar calendar = CALENDAR;
        synchronized (calendar) {
            calendar.setTimeInMillis(date.getTime());
        }
        return calendar.get(GregorianCalendar.MONTH);
    }

    public static int getDay(Date date) {
        if (date == null) {
            date = new Date();
        }
        Calendar calendar = CALENDAR;
        synchronized (calendar) {
            calendar.setTimeInMillis(date.getTime());
        }
        return calendar.get(GregorianCalendar.DATE);
    }

    public static int getYear(Date date) {
        if (date == null) {
            date = new Date();
        }
        Calendar calendar = CALENDAR;
        synchronized (calendar) {
            calendar.setTimeInMillis(date.getTime());
        }
        return calendar.get(GregorianCalendar.YEAR);
    }

    public static boolean isSameDay(Date date1, Date date2) {
        return getDay(date1) == getDay(date2);
    }

    public static boolean isSameMonth(Date date1, Date date2) {
        return getMonth(date1) == getMonth(date2);
    }

    public static boolean isSameYear(Date date1, Date date2) {
        return getYear(date1) == getYear(date2);
    }

    public static boolean isPast(Date baseDate, Date comparedDate) {
        int result = baseDate.compareTo(comparedDate);
        return result == 1;
    }

    public static boolean isFuture(Date baseDate, Date comparedDate) {
        return !isPast(baseDate, comparedDate);
    }

    public static int getMonthDiff(Date fromDate, Date toDate) {
        if (fromDate.after(toDate)) {
            Date temp = fromDate;
            fromDate = toDate;
            toDate = temp;
        }
        int years = getYear(toDate) - getYear(fromDate);
        int month1 = 12 - getMonth(fromDate);
        int month2 = getMonth(toDate);
        if (years == 0) {
            month1 = month2 - month1;
            month2 = 0;
        } else {
            years = years - 1;
        }

        int months = month1 + (years * 12) + month2;
        //if((getDay(toDate)-getDay(fromDate))<0)
        //	months = months - 1;
        return months;
    }

    public static Date getFirstDayInYear(int year, int weekDay) {
        Calendar c = new GregorianCalendar(year, Calendar.JANUARY, 1);
        c.setMinimalDaysInFirstWeek(7);
        c.setFirstDayOfWeek(weekDay);
        c.set(Calendar.DAY_OF_WEEK, weekDay);
        //System.out.println("C1: " + new Date(c.getTimeInMillis()));
        c.set(Calendar.WEEK_OF_YEAR, 1);
        //System.out.println("C2: " + new Date(c.getTimeInMillis()));
        //if(year>getYear(c.getTime())){
        //    c.set(Calendar.WEEK_OF_YEAR, 2);    
        //}
        return new Date(c.getTimeInMillis());
    }

    public static Date getLastDayInYear(int year, int weekDay) {
        Date date = getFirstDayInYear(year + 1, weekDay);
        return createDate(getDay(date) - 7, getMonth(date), year + 1);
    }

    public static Date addHours(Date date, int hours) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR_OF_DAY, hours);
        return calendar.getTime();
    }

    public static Date addHours(Date date, int hours, int min) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR_OF_DAY, hours);
        calendar.add(Calendar.MINUTE, min);
        return calendar.getTime();
    }

    public static int getHours(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    public static int getMins(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.MINUTE);
    }

    public static int getSecs(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.SECOND);
    }
    
    public static Date transferTime(Date fromDate, Date toDate){
        int hr = getHours(fromDate);
        int min = getMins(fromDate);
        
        toDate = startOfDay(toDate);
        return addHours(toDate, hr, min);
    }
    
    public static int findAge(Date sourceDate, Date compareDate){
        if(compareDate!=null){
            return DateUtil.getDaysDiff(compareDate.getTime(), sourceDate.getTime());
        }
        return 0;
    }
        
    public static void main(String args[]) {
        // System.out.println(createDate(10, Calendar.DECEMBER, 2006));
    }
}
