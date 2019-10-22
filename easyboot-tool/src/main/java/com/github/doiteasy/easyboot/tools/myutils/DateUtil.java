package com.github.doiteasy.easyboot.tools.myutils;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Stream;

/**
 * 日期工具类
 *
 */
@Slf4j
public class DateUtil {
    public final static String YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm";
    public final static String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    public final static String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";
    public final static String YYYY_MM_DD = "yyyy-MM-dd";
    public final static String YYYYMMDD = "yyyyMMdd";
    public final static String HH_mm_ss = "HH:mm:ss";


    private DateUtil() {
    }


    /**
     * 时间戳转换成日期格式字符串
     * @param seconds 精确到秒的字符串
     * @param formatStr
     * @return
     */
    public static String timeStamp2Date(String seconds,String formatStr) {
        if(seconds == null || seconds.isEmpty() || seconds.equals("null")){
            return "";
        }
        if(formatStr == null || formatStr.isEmpty()){
            formatStr = YYYY_MM_DD_HH_MM_SS;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(formatStr);
        return sdf.format(new Date(Long.valueOf(seconds+"000")));
    }
    /**
     * 日期格式字符串转换成时间戳
     * @param date_str 字符串日期
     * @param format 如：yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static String date2TimeStamp(String date_str,String format){
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return String.valueOf(sdf.parse(date_str).getTime()/1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }


    /**
     * 一天的0秒
     */
    public static Date getStartTime(Date date) {
        if (date == null) {
            return null;
        }
        Calendar todayStart = Calendar.getInstance();
        todayStart.setTime(date);
        todayStart.set(Calendar.AM_PM, Calendar.AM);
        todayStart.set(Calendar.HOUR, 0);
        todayStart.set(Calendar.MINUTE, 0);
        todayStart.set(Calendar.SECOND, 0);
        todayStart.set(Calendar.MILLISECOND, 0);
        return todayStart.getTime();
    }

    /**
     * 一天的最后一秒
     */
    public static Date getEndTime(Date date) {
        if (date == null) {
            return null;
        }
        Calendar todayStart = Calendar.getInstance();
        todayStart.setTime(date);
        todayStart.set(Calendar.AM_PM, Calendar.AM);
        todayStart.set(Calendar.HOUR, 23);
        todayStart.set(Calendar.MINUTE, 59);
        todayStart.set(Calendar.SECOND, 59);
        todayStart.set(Calendar.MILLISECOND, 0);
        return todayStart.getTime();
    }

    /**
     * 当前时间格式化
     *
     * @return YYYY_MM_DD
     */
    public static String dateSimple(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return "";
        }
        return localDateTime.format(DateTimeFormatter.ofPattern(YYYY_MM_DD));
    }

    /**
     * 当前时间格式化
     *
     * @return YYYY_MM_DD
     */
    public static String dateSimple(LocalDateTime localDateTime,String format) {
        if (localDateTime == null) {
            return "";
        }
        return localDateTime.format(DateTimeFormatter.ofPattern(format));
    }


    /**
     * 获取两个时间相差天数
     *
     * @param minuendLocalDate 被减数
     * @param subtrahendLocalDate 减数
     * @return
     */
    public static Long getTimeDifference(LocalDate minuendLocalDate,LocalDate subtrahendLocalDate){
        if(minuendLocalDate != null && subtrahendLocalDate != null){
            return minuendLocalDate.toEpochDay() - subtrahendLocalDate.toEpochDay();
        }else {
            return null;
        }
    }

    /**
     * LocalDateTime转换为Date
     * @param localDateTime
     */
    public static Date localDateTime2Date( LocalDateTime localDateTime){
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime zdt = localDateTime.atZone(zoneId);
        Date date = Date.from(zdt.toInstant());
        return date;
    }

    /**
     * Date转换为LocalDateTime
     * @param date
     */
    public static LocalDateTime date2LocalDateTime(Date date){
        Instant instant = date.toInstant();
        ZoneId zoneId = ZoneId.systemDefault();
        LocalDateTime localDateTime = instant.atZone(zoneId).toLocalDateTime();
        return localDateTime;
    }

    // 获得某天最大时间 xxxx-xx-xx 23:59:59
    public static LocalDateTime getEndOfDay(LocalDateTime localDateTime) {
        Date endTime = getEndTime(localDateTime2Date(localDateTime));
        LocalDateTime endOfDay = date2LocalDateTime(endTime);
        return endOfDay;
    }

    /**
     * 获取两个日期间隔的所有日期
     * @param startDate 格式必须为'2018-01-25'
     * @param endDate 格式必须为'2018-01-25'
     * @return
     */
    public static List<String> getBetweenDate(LocalDate startDate, LocalDate endDate){
        List<String> list = new ArrayList<>();
        long distance = ChronoUnit.DAYS.between(startDate, endDate);
        if (distance < 1) {
            return list;
        }
        Stream.iterate(startDate, d -> {
            return d.plusDays(1);
        }).limit(distance + 1).forEach(f -> {
            list.add(f.toString());
        });
        return list;
    }


    /**
     * 当前时间格式化
     *
     * @return YYYY MM DD
     */
    public static String dateSimple(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(YYYY_MM_DD);
        return simpleDateFormat.format(date);
    }

    public static String dateFormatYYYYMMDDHHMMSS(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(YYYY_MM_DD_HH_MM_SS);
        return simpleDateFormat.format(date);
    }

    /**
     * 转时间
     *
     * @param f    格式化
     * @param date 时间
     * @return date类型
     */
    public static Date parse(String f, String date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(f);
        try {
            return dateFormat.parse(date);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 在指定时间的基础上增加 days 后的日期
     *
     * @param days
     * @return
     */
    public static Date dayAdd(int days, Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int day = calendar.get(Calendar.DAY_OF_YEAR);
        calendar.set(Calendar.DAY_OF_YEAR, day + days);
        return calendar.getTime();
    }

    /**
     * 在指定时间的基础上增加 days 后的日期
     *
     * @param days
     * @return
     */
    public static String dayAdd(int days, String date) {
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(new SimpleDateFormat(YYYY_MM_DD).parse(date));
        } catch (ParseException e) {
            log.error(e.getMessage(), e);
        }
        int day = calendar.get(Calendar.DAY_OF_YEAR);
        calendar.set(Calendar.DAY_OF_YEAR, day + days);
        return new SimpleDateFormat(YYYY_MM_DD).format(calendar.getTime());
    }

    /**
     * 获取当前日期前N天和后N天时间字符串
     *
     * @param beforOrAfterDate
     * @returnB
     */
    public static String getBeforeOrAfterTimeStr(Integer beforOrAfterDate, String format) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        calendar.add(Calendar.DATE, beforOrAfterDate);
        return sdf.format(calendar.getTime());
    }

    public static Date parse(String date) {
        try {
            return getDateFormat().parse(date);
        } catch (Exception e) {
            try {
                return getDateFormat(YYYY_MM_DD_HH_MM_SS).parse(date);
            } catch (Exception e1) {
                try {
                    return getDateFormat(YYYY_MM_DD_HH_MM).parse(date);
                } catch (ParseException e2) {
                    try {
                        return getDateFormat(YYYYMMDDHHMMSS).parse(date);
                    } catch (Exception e3) {
                        try {
                            return getDateFormat(YYYY_MM_DD).parse(date);
                        } catch (Exception e4) {
                            try {
                                return getDateFormat(HH_mm_ss).parse(date);
                            } catch (Exception e5) {
                                return null;
                            }
                        }
                    }
                }
            }
        }
    }

    public static DateFormat getDateFormat() {
        return new SimpleDateFormat(YYYY_MM_DD_HH_MM_SS);
    }

    public static DateFormat getDateFormat(String format) {
        return new SimpleDateFormat(format);
    }

    public static String format(String format, Date date) {
        return getDateFormat(format).format(date);
    }

    // 获得当天0点时间
    public static Date getTimesmorning() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }



    //获取某段时间内的所有日期
    public static List<Date> findDates(Date dStart, Date dEnd) {
        Calendar cStart = Calendar.getInstance();
        cStart.setTime(dStart);

        List dateList = new ArrayList();
        dateList.add(dStart);
        // 此日期是否在指定日期之后
        while (dEnd.after(cStart.getTime())) {
            // 根据日历的规则，为给定的日历字段添加或减去指定的时间量
            cStart.add(Calendar.DAY_OF_MONTH, 1);
            dateList.add(cStart.getTime());
        }
        return dateList;
    }


    //获取某段时间相差天数
    public static Integer getDayDiffer(String dStart, String dEnd) {
        SimpleDateFormat sdf = new SimpleDateFormat(YYYY_MM_DD);
        Date date = null;
        Date date1 = null;
        try {
            date = sdf.parse(dStart);
            date1 = sdf.parse(dEnd);
        } catch (ParseException e) {
            log.error(e.getMessage(), e);
        }
        List<Date> dates = findDates(date, date1);
        List<String> list = new ArrayList<>(dates.size());
        dates.forEach((date2) -> {
            list.add(sdf.format(date2));
        });
        return list.size() - 1;
    }

    //获取某段时间相差天数
    public static Integer getDayDiffer(Date dStart, Date dEnd) {
        SimpleDateFormat sdf = new SimpleDateFormat(YYYY_MM_DD);
        Integer dayDiffer = getDayDiffer(sdf.format(dStart), sdf.format(dEnd));
        return dayDiffer;
    }

    public static String getCurrentDate(String aFormat) {
        String tDate = new SimpleDateFormat(aFormat).format(new Date(
            System.currentTimeMillis()));
        return tDate;
    }


    public static String getCurrentDate() {
        String tDate = new SimpleDateFormat(YYYY_MM_DD_HH_MM_SS).format(new Date(
            System.currentTimeMillis()));
        return tDate;
    }


    public static String getDate(int i) {
        SimpleDateFormat sf = new SimpleDateFormat(YYYYMMDD);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        if (i == 1) {
            String str = sf.format(calendar.getTime());
            return str;
        } else {
            calendar.add(Calendar.DAY_OF_YEAR, -1);
            String str = sf.format(calendar.getTime());
            return str;
        }
    }

    /**
     * YYYYMMDD -> YYYY-MM-DD
     */
    public static String parseYYYYMMDD(int day) {
        SimpleDateFormat sdf = new SimpleDateFormat(YYYYMMDD);
        SimpleDateFormat sdf1 = new SimpleDateFormat(YYYY_MM_DD);
        try {
            return sdf1.format(sdf.parse(day + ""));
        } catch (ParseException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }



    /**
     * 本周一
     *
     * @return
     */
    public static LocalDateTime getThisWeekMonday() {
        return LocalDateTime.now().with(DayOfWeek.MONDAY);
    }


    /**
     * 上周一
     *
     * @return
     */
    public static LocalDateTime getLastWeekMonday() {
        return LocalDateTime.now().minusWeeks(1).with(DayOfWeek.MONDAY);
    }


    public static LinkedList<LocalDate> getDateRangeList(LocalDate startStatDate, LocalDate endStatDate) {
        LocalDate tempDate = startStatDate;
        LinkedList<LocalDate> rangeDates = Lists.newLinkedList();
        while(!tempDate.isAfter(endStatDate)) {
            rangeDates.add(tempDate);
            tempDate = tempDate.plusDays(1);
        }
        return rangeDates;
    }

    public static String plusDayByDayString(String date, int day) {
        LocalDate tempDate = LocalDate.parse(date, DateTimeFormatter.ISO_DATE);
        return tempDate.plusDays(day).toString();
    }

    public static LocalDate getRepayDate (LocalDate localDate, int period) {
        return localDate.plusDays(period - 1);
    }
}
