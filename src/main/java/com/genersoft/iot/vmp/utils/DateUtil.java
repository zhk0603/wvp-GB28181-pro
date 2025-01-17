package com.genersoft.iot.vmp.utils;


import org.apache.commons.lang3.ObjectUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAccessor;
import java.util.Locale;

/**    
 * 全局时间工具类
 * @author lin
 */
public class DateUtil {
    private final static Logger logger = LoggerFactory.getLogger(DateUtil.class);

    /**
     * 兼容不规范的iso8601时间格式
     */
	private static final String ISO8601_COMPATIBLE_PATTERN = "yyyy-M-d'T'H:m:s";

    /**
     * 用以输出标准的iso8601时间格式
     */
	private static final String ISO8601_PATTERN = "yyyy-MM-dd'T'HH:mm:ss";

    /**
     * wvp内部统一时间格式
     */
    public static final String PATTERN = "yyyy-MM-dd HH:mm:ss";

    /**
     * wvp内部统一时间格式
     */
    public static final String URL_PATTERN = "yyyyMMddHHmmss";

    public static final String zoneStr = "Asia/Shanghai";

    public static final DateTimeFormatter formatterCompatibleISO8601 = DateTimeFormatter.ofPattern(ISO8601_COMPATIBLE_PATTERN, Locale.getDefault()).withZone(ZoneId.of(zoneStr));
    public static final DateTimeFormatter formatterISO8601 = DateTimeFormatter.ofPattern(ISO8601_PATTERN, Locale.getDefault()).withZone(ZoneId.of(zoneStr));
    public static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(PATTERN, Locale.getDefault()).withZone(ZoneId.of(zoneStr));
    public static final DateTimeFormatter urlFormatter = DateTimeFormatter.ofPattern(URL_PATTERN, Locale.getDefault()).withZone(ZoneId.of(zoneStr));

	public static String yyyy_MM_dd_HH_mm_ssToISO8601(String formatTime) {

        return formatterISO8601.format(formatter.parse(formatTime));
    }
	
	public static String ISO8601Toyyyy_MM_dd_HH_mm_ss(String formatTime) {
        try {
            TemporalAccessor temporalAccessor = formatterCompatibleISO8601.parse(formatTime);
            return formatter.format(temporalAccessor);
        }
        catch (Exception e) {
            logger.warn("ISO8601Toyyyy_MM_dd_HH_mm_ss >>>> ", e);
            return formatter.format(tryCorrectingDate(formatTime));
        }
    }

    // 尝试修正错误的时间格式（2023-10-09T25:02:32），失败时返回当前时间
    @NotNull
    private static LocalDateTime tryCorrectingDate(String formatTime) {
        try {
            String[] ts = formatTime.split("T");
            LocalDate date = LocalDate.parse(ts[0]);
            String[] timeArr = ts[1].split(":");
            long hours = getArrayValue(timeArr, 0, 0);
            long minutes = getArrayValue(timeArr, 1, 0);
            long seconds = getArrayValue(timeArr, 2, 0);
            LocalDateTime dateTime = LocalDateTime.of(date, LocalTime.MIN);
            dateTime = dateTime.plusHours(hours).plusMinutes(minutes).plusSeconds(seconds);
            return dateTime;
        } catch (Exception e) {
            return LocalDateTime.now();
        }
    }

    private static long getArrayValue(String[] array, int index, long defaultValue) {
        if (index >= 0 && index < array.length) {
            return Long.parseLong(array[index]);
        }

        return defaultValue;
    }

    public static String urlToyyyy_MM_dd_HH_mm_ss(String formatTime) {
        return formatter.format(urlFormatter.parse(formatTime));
    }

    /**
     * yyyy_MM_dd_HH_mm_ss 转时间戳
     * @param formatTime
     * @return
     */
	public static long yyyy_MM_dd_HH_mm_ssToTimestamp(String formatTime) {
        TemporalAccessor temporalAccessor = formatter.parse(formatTime);
        Instant instant = Instant.from(temporalAccessor);
        return instant.getEpochSecond();
	}

    /**
     * 获取当前时间
     * @return
     */
    public static String getNow() {
        LocalDateTime nowDateTime = LocalDateTime.now();
        return formatter.format(nowDateTime);
    }

    /**
     * 获取当前时间
     * @return
     */
    public static String getNowForUrl() {
        LocalDateTime nowDateTime = LocalDateTime.now();
        return urlFormatter.format(nowDateTime);
    }


    /**
     * 格式校验
     * @param timeStr 时间字符串
     * @param dateTimeFormatter 待校验的格式
     * @return
     */
    public static boolean verification(String timeStr, DateTimeFormatter dateTimeFormatter) {
        try {
            LocalDate.parse(timeStr, dateTimeFormatter);
            return true;
        }catch (DateTimeParseException exception) {
            return false;
        }
    }

    public static String getNowForISO8601() {
        LocalDateTime nowDateTime = LocalDateTime.now();
        return formatterISO8601.format(nowDateTime);
    }

    public static long getDifferenceForNow(String keepaliveTime) {
        if (ObjectUtils.isEmpty(keepaliveTime)) {
            return 0;
        }
        Instant beforeInstant = Instant.from(formatter.parse(keepaliveTime));
        return ChronoUnit.MILLIS.between(beforeInstant, Instant.now());
    }


    public static void main(String[] args) throws ParseException {
        String timeStr = "2023-10-09T25:02:32";
        LocalDateTime dateTime = tryCorrectingDate(timeStr);
    }
}
