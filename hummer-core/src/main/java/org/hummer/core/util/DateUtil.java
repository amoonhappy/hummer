/**
 * <p>Open Source Architecture Project -- Hummer            </p>
 * <p>Class Description                                     </p>
 * <p>                                                      </p>
 * <p>                                                      </p>
 * <p>Change History                                        </p>
 * <p>Author    Date      Description                       </p>
 * <p>                                                      </p>
 * <p>                                                      </p>
 *
 * @author <a href="mailto:jeff_myth@yahoo.com.cn">Jeff Zhou</a> Date: 2005-12-7
 * @version 1.0
 */
package org.hummer.core.util;

import org.hummer.core.constants.Constant;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class DateUtil {
    public final static int YEAR = 0;
    public final static int MONTH = 1;
    public final static int DAY = 2;
    private static final int ONE_MINUTE = 60 * 1000;
    private static final int ONE_HOUR = 60 * ONE_MINUTE;
    private static final int ONE_DAY = 24 * ONE_HOUR;
    public static String[] monthAbbr = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov",
            "Dec"};
    private static SimpleDateFormat dataFormat = new SimpleDateFormat(Constant.DATE_FORMAT_DEFAULT);
    public static final String SDF = "yyyyMMddHHmmss" ;
    public static final String SDF_CN = "yyyy-MM-dd HH:mm:ss" ;
    public static final String SDF_CN_DATE = "yyyy-MM-dd" ;
    private static final Object lock = new Object();

    private static Map<String, ThreadLocal<SimpleDateFormat>> SDF_MAP = new HashMap<>();

    /**
     * 返回一个ThreadLocal的SimpleDateFormat,每个线程只会new一次SimpleDateFormat
     *
     * @param pattern 时间格式
     * @return SimpleDateFormat
     */
    private static SimpleDateFormat getFormat(final String pattern) {
        ThreadLocal<SimpleDateFormat> t = SDF_MAP.get(pattern);
        if (t == null) {
            synchronized (lock) {
                t = SDF_MAP.get(pattern);
                if (t == null) {
                    t = new ThreadLocal<SimpleDateFormat>() {
                        @Override
                        protected SimpleDateFormat initialValue() {
                            return new SimpleDateFormat(pattern);
                        }
                    };
                    SDF_MAP.put(pattern, t);
                }
            }
        }
        return t.get();
    }

    public static String format(Date date, String pattern) {
        return getFormat(pattern).format(date);
    }

    public static Date parse(String dateStr, String pattern) {
        try {
            return getFormat(pattern).parse(dateStr);
        } catch (ParseException e) {
            throw new RuntimeException("时间格式转换错误。" );
        }
    }

    /**
     * Long型时间转换为Date
     *
     * @param dateFormat
     * @param millSec
     * @return
     * @Title: formatLongToDate
     * @Description: TODO
     * @return: String
     */
    public static String formatLongToDate(String dateFormat, Long millSec) {
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        Date date = new Date(millSec);
        return sdf.format(date);
    }

    public static String formatDate(Long Id, String compart) {
        String ret = "" ;

        if (null != Id) {
            String ds = Id.toString();

            for (int i = 0; i < (8 - ds.length()); i++) {
                ds = "0" + ds;
            }

            ret = ds.substring(0, 4) + compart + ds.substring(4, 6) + compart + ds.substring(6);
        }

        return ret;
    }

    /**
     * get the abbreviation of month
     *
     * @param month
     * @return String
     */
    public static String getMonthAbbr(int month) {
        return monthAbbr[month - 1];
    }

    /**
     * return ture if the two dates are the same
     *
     * @param date1
     * @param date2
     * @return boolean
     */
    public static boolean isEqual(Date date1, Date date2) {
        return ((date1.getTime() - date2.getTime()) == 0);
    }

    /**
     * convert date to String
     *
     * @param date
     * @param pattern
     * @param local
     * @return String
     */
    public static String date2String(Date date, String pattern, Locale local) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern, local);

        return sdf.format(date);
    }

    /**
     * convert date to String
     *
     * @param date
     * @param pattern
     * @return String
     */
    public static String date2String(Date date, String pattern) {
        return date2String(date, pattern, Constant.LOCALE_DEFAULT);
    }

    /**
     * convert date to String the default date format is yyyy/MM/dd
     *
     * @param date
     * @return String
     */
    public static String date2String(Date date) {
        return date2String(date, Constant.DATE_FORMAT_DEFAULT);
    }

    /**
     * convert String to date default date format is yyyy/MM/dd
     *
     * @param sDate
     * @return Date
     */
    public static Date getDate(String sDate) {
        return getDate(sDate, Constant.DATE_FORMAT_DEFAULT);
    }

    /**
     * convert String to Date
     *
     * @param sDate
     * @param pattern
     * @return Date
     */
    public static Date getDate(String sDate, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        Date date = null;

        try {
            date = util2Sql(sdf.parse(sDate));
        } catch (Exception e) {
            // TODO Add Log here
            e.printStackTrace();
        }

        return date;
    }

    /**
     * change the Date format
     *
     * @param sDate
     * @param pattern
     * @return Date
     */
    public static Date getDate(Date sDate, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        Date date = null;

        try {
            date = util2Sql(sdf.parse(sdf.format(sDate)));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return date;
    }

    /**
     * convert int to Date
     *
     * @param year
     * @param month
     * @param day
     * @return Date
     */
    public static Date getDate(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(year, month - 1, day);

        return new Date(calendar.getTime().getTime());
    }

    /**
     * get current date
     *
     * @return Date
     */
    public static Date getDate() {
        return new Date(new java.util.Date().getTime());
    }

    /**
     * convert Util Date to SQL Date
     *
     * @param date
     * @return Date
     */
    public static Date util2Sql(java.util.Date date) {
        return new Date(date.getTime());
    }

    /**
     * get the year number of Date
     *
     * @param date
     * @return int
     */
    public static int getYear(java.util.Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        return calendar.get(Calendar.YEAR);
    }

    /**
     * get the max day of a month
     *
     * @param date
     * @return int
     */
    public static int getMaximumDayOfMonth(java.util.Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    /**
     * get the month number
     *
     * @param date
     * @return int
     */
    public static int getMonth(java.util.Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        return calendar.get(Calendar.MONTH) + 1;
    }

    /**
     * get the day number
     *
     * @param date
     * @return int
     */
    public static int getDay(java.util.Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        return calendar.get(Calendar.DATE);
    }

    /**
     * increase year
     *
     * @param date
     * @param iYear
     * @return Date
     */
    public static Date addYear(Date date, int iYear) {
        return add(date, iYear, DateUtil.YEAR);
    }

    /**
     * increase month
     *
     * @param date
     * @param iMonth
     * @return Date
     */
    public static Date addMonth(Date date, int iMonth) {
        return add(date, iMonth, DateUtil.MONTH);
    }

    /**
     * increase day
     *
     * @param date
     * @param iDay
     * @return Date
     */
    public static Date addDay(Date date, int iDay) {
        return add(date, iDay, DateUtil.DAY);
    }

    /**
     * return year difference between date1 and date2
     *
     * @param date1
     * @param date2
     * @return int
     */
    public static int diffYear(Date date1, Date date2) {
        int diff = 0;
        Calendar calendar1 = Calendar.getInstance();
        Calendar calendar2 = Calendar.getInstance();

        calendar1.setTime(date1);
        calendar2.setTime(date2);

        diff = calendar1.get(Calendar.YEAR) - calendar2.get(Calendar.YEAR);

        return diff;
    }

    /**
     * return month difference between date1 and date2
     *
     * @param date1
     * @param date2
     * @return int
     */
    public static int diffMonth(Date date1, Date date2) {
        int diff = 0;
        Calendar calendar1 = Calendar.getInstance();
        Calendar calendar2 = Calendar.getInstance();

        calendar1.setTime(date1);
        calendar2.setTime(date2);

        diff = calendar1.get(Calendar.YEAR) - calendar2.get(Calendar.YEAR);
        diff = ((diff * 12) + calendar1.get(Calendar.MONTH)) - calendar2.get(Calendar.MONTH);

        return diff;
    }

    /**
     * return day difference between date1 and date2
     *
     * @param date1
     * @param date2
     * @return int
     */
    public static int diffDay(Date date1, Date date2) {
        long diff = date1.getTime() - date2.getTime();

        return (int) (diff / (long) (24 * 60 * 60 * 1000));
    }

    private static Date add(Date date, int iCount, int iField) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        switch (iField) {
            case YEAR:
                calendar.add(Calendar.YEAR, iCount);

                break;

            case MONTH:
                calendar.add(Calendar.MONTH, iCount);

                break;

            case DAY:
                calendar.add(Calendar.DATE, iCount);

                break;

            default:
                break;
        }

        return new Date(calendar.getTime().getTime());
    }

    /**
     * get the first day of month
     *
     * @param date
     * @return Date
     */
    public static Date getFirstDateOfMonth(Date date) {
        int year = getYear(date);
        int month = getMonth(date);

        return getDate(year, month, 1);
    }

    /**
     * get the last of month
     *
     * @param date
     * @return Date
     */
    public static Date getLastDateOfMonth(Date date) {
        int year = getYear(date);
        int month = getMonth(date);
        int day = getMaximumDayOfMonth(date);

        return getDate(year, month, day);
    }

    /**
     * get the list of period between startDate and endDate
     *
     * @param startDate
     * @param endDate
     * @return ArrayList
     */
    public static ArrayList<Date> getEveryMonthOfPeriod(Date startDate, Date endDate) {
        ArrayList<Date> returnVal = new ArrayList<Date>();
        int cnt = diffMonth(endDate, startDate);
        Date tempDate = getDate(getYear(startDate), getMonth(startDate), 1);

        returnVal.add(startDate);

        for (int i = 2; i <= cnt; i++) {
            tempDate = addMonth(tempDate, 1);
            returnVal.add(tempDate);
        }

        returnVal.add(endDate);

        return returnVal;
    }

    /**
     * get current date
     *
     * @return String
     */
    public static String getCurrentDate() {
        return dataFormat.format(Calendar.getInstance().getTime());
    }

    /**
     * get date by default format: yyyy-MM-dd
     *
     * @param sDate
     * @return String
     */
    public static Date getDateYMD(String sDate) {
        return getDate(sDate, Constant.DATE_FORMAT_DEFAULT);
    }

    /**
     * get current date by format: yyyy-MM-dd
     *
     * @return Date
     */
    public static Date getCurrentDateYMD() {
        return getDate(new Date(), Constant.DATE_FORMAT_DEFAULT);
    }

    /**
     * get the month ratio of start date
     *
     * @param date
     * @return double
     */
    public static double getMonthRatioOfStartDate(Date date) {
        int day = getDay(date);
        int maxDay = getMaximumDayOfMonth(date);
        int numeratorDay = (maxDay + 1) - day;

        return (double) ((double) numeratorDay / (double) maxDay);
    }

    /**
     * get the month ratio of end date
     *
     * @param date
     * @return double
     */
    public static double getMonthRatioOfEndDate(Date date) {
        int day = getDay(date);
        int maxDay = getMaximumDayOfMonth(date);

        return (double) ((double) day / (double) maxDay);
    }

    /**
     * Get the every year in the specified date arrange
     *
     * @param startDate --
     *                  The start date
     * @param endDate   --
     *                  The end date
     * @return ArrayList -- The year list array
     */
    public static ArrayList<HashMap<Object, Date>> getEveryYearOfPeriod(Date startDate, Date endDate) {
        ArrayList<HashMap<Object, Date>> returnVal = new ArrayList<HashMap<Object, Date>>();
        Date tempStartDate = startDate;
        Date tempEndDate = getDate(getYear(tempStartDate), 3, 31);

        if (tempStartDate.compareTo(tempEndDate) > 0) {
            tempEndDate = getDate(getYear(tempStartDate) + 1, 3, 31);
        }

        while (endDate.compareTo(tempEndDate) > 0) {
            HashMap<Object, Date> hm = new HashMap<Object, Date>();
            hm.put("startDate", tempStartDate);
            hm.put("endDate", tempEndDate);
            tempStartDate = addDay(tempEndDate, 1);
            tempEndDate = getDate(getYear(tempStartDate) + 1, 3, 31);
            returnVal.add(hm);
        }

        HashMap<Object, Date> hm2 = new HashMap<Object, Date>();
        hm2.put("startDate", tempStartDate);
        hm2.put("endDate", endDate);

        returnVal.add(hm2);

        return returnVal;
    }

    /**
     * get the number between start and end
     *
     * @param start
     * @param end
     * @return int
     */
    public static int getBetweenDays(Date start, Date end) {
        long startTime = start.getTime();
        long endTime = end.getTime();

        /*
         * Calendar calendar = GregorianCalendar.getInstance();
         * calendar.setTime(start); System.out.println("YEAR: " +
         * calendar.get(Calendar.YEAR)); System.out.println("MONTH: " +
         * calendar.get(Calendar.MONTH)); System.out.println("WEEK_OF_YEAR: " +
         * calendar.get(Calendar.WEEK_OF_YEAR));
         * System.out.println("WEEK_OF_MONTH: " +
         * calendar.get(Calendar.WEEK_OF_MONTH)); System.out.println("DATE: " +
         * calendar.get(Calendar.DATE)); System.out.println("HOUR: " +
         * calendar.get(Calendar.HOUR_OF_DAY)); System.out.println("MINUTES: " +
         * calendar.get(Calendar.MINUTE)); System.out.println("SECOND: " +
         * calendar.get(Calendar.SECOND)); calendar.setTime(end);
         * System.out.println("YEAR: " + calendar.get(Calendar.YEAR));
         * System.out.println("MONTH: " + calendar.get(Calendar.MONTH));
         * System.out.println("WEEK_OF_YEAR: " +
         * calendar.get(Calendar.WEEK_OF_YEAR));
         * System.out.println("WEEK_OF_MONTH: " +
         * calendar.get(Calendar.WEEK_OF_MONTH)); System.out.println("DATE: " +
         * calendar.get(Calendar.DATE)); System.out.println("HOUR: " +
         * calendar.get(Calendar.HOUR_OF_DAY)); System.out.println("MINUTES: " +
         * calendar.get(Calendar.MINUTE)); System.out.println("SECOND: " +
         * calendar.get(Calendar.SECOND));
         */
        return (int) ((endTime - startTime) / ONE_DAY);
    }

    /**
     * get the number between start and end
     *
     * @param start
     * @param end
     * @return int
     */
    public static int getBetweenDays(Calendar start, Calendar end) {
        return (getBetweenDays(start.getTime(), end.getTime()));
    }

    /**
     * validate the date String
     *
     * @param value
     * @param datePattern
     * @param strict      true if you want to validate the length of String and pattern
     * @return boolean
     */
    public static boolean isDate(String value, String datePattern, boolean strict) {
        if (value == null) {
            return false;
        } else {
            value = value.trim();
        }

        if (!value.matches("[0-9-/]+" )) {
            return false;
        }

        // return GenericValidator.isDate(value,datePattern,strict);
        if ((value == null) || (datePattern == null) || (datePattern.length() <= 0)) {
            return false;
        }

        SimpleDateFormat formatter = new SimpleDateFormat(datePattern);
        formatter.setLenient(false);

        try {
            formatter.parse(value);
        } catch (ParseException e) {
            return false;
        }

        if (strict && (datePattern.length() != value.length())) {
            return false;
        }

        return true;
    }

    /**
     * validate date String
     *
     * @param value
     * @param locale
     * @return boolean
     */
    public static boolean isDate(String value, Locale locale) {
        if (value == null) {
            return false;
        } else {
            value = value.trim();
        }

        if (!value.matches("[0-9-/]+" )) {
            return false;
        }

        // return GenericValidator.isDate(value,locale);
        if (value == null) {
            return false;
        }

        DateFormat formatter = null;

        if (locale != null) {
            formatter = DateFormat.getDateInstance(DateFormat.SHORT, locale);
        } else {
            formatter = DateFormat.getDateInstance(DateFormat.SHORT, Constant.LOCALE_DEFAULT);
        }

        formatter.setLenient(false);

        try {
            formatter.parse(value);
        } catch (ParseException e) {
            return false;
        }

        return true;
    }

    public static java.sql.Timestamp getTimestampBeforeToday(int days) {
        return new java.sql.Timestamp(System.currentTimeMillis() - (Long.valueOf(ONE_DAY).longValue() * days));
    }

    public static java.sql.Timestamp getTimestamp(String sDate, String pattern) {
        if ((pattern == null) || pattern.trim().equals("" )) {
            pattern = "yyyy-MM-dd" ;
        }

        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        java.sql.Timestamp date = null;

        try {
            date = util2Timestamp(sdf.parse(sDate));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return date;
    }

    public static java.sql.Date getDateBeforeToday(int days) {
        return new java.sql.Date(System.currentTimeMillis() - (Long.valueOf(ONE_DAY).longValue() * days));
    }

    public static java.sql.Timestamp util2Timestamp(java.util.Date date) {
        return new java.sql.Timestamp(date.getTime());
    }
}
