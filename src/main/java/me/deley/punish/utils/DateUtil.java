package me.deley.punish.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DateUtil {
    public static String toMillis(double d) {
        String string = String.valueOf(d);
        StringBuilder sb = new StringBuilder();
        boolean stop = false;
        char[] var5;
        int var6 = (var5 = string.toCharArray()).length;

        for (int var7 = 0; var7 < var6; ++var7) {
            char c = var5[var7];
            if (stop) {
                return sb.append(c).toString();
            }

            if (c == '.') {
                stop = true;
            }

            sb.append(c);
        }

        return sb.toString();
    }

    private static String fromLong(long lenth) {
        int days = (int) TimeUnit.SECONDS.toDays(lenth);
        long hours = TimeUnit.SECONDS.toHours(lenth) - (long) (days * 24);
        long minutes = TimeUnit.SECONDS.toMinutes(lenth) - TimeUnit.SECONDS.toHours(lenth) * 60L;
        long seconds = TimeUnit.SECONDS.toSeconds(lenth) - TimeUnit.SECONDS.toMinutes(lenth) * 60L;
        String totalDay = days + (days == 1 ? " dia " : " dias ");
        String totalHours = hours + (hours == 1L ? " hora " : " horas ");
        String totalMinutes = minutes + (minutes == 1L ? " minuto " : " minutos ");
        String totalSeconds = seconds + (seconds == 1L ? " segundo" : " segundos");
        if (days == 0) {
            totalDay = "";
        }

        if (hours == 0L) {
            totalHours = "";
        }

        if (minutes == 0L) {
            totalMinutes = "";
        }

        if (seconds == 0L) {
            totalSeconds = "";
        }

        String restingTime = totalDay + totalHours + totalMinutes + totalSeconds;
        restingTime = restingTime.trim();
        if (restingTime.equals("")) {
            restingTime = "0 segundos";
        }

        return restingTime;
    }

    public static String getDifferenceFormat(long time) {
        if (time <= 0L) {
            return "";
        } else {
            long day = TimeUnit.SECONDS.toDays(time);
            long hours = TimeUnit.SECONDS.toHours(time) - day * 24L;
            long minutes = TimeUnit.SECONDS.toMinutes(time) - TimeUnit.SECONDS.toHours(time) * 60L;
            long seconds = TimeUnit.SECONDS.toSeconds(time) - TimeUnit.SECONDS.toMinutes(time) * 60L;
            StringBuilder sb = new StringBuilder();
            if (day > 0L) {
                sb.append(day).append(" ").append("dia" + (day > 1L ? "s" : "")).append(" ");
            }

            if (hours > 0L) {
                sb.append(hours).append(" ").append("hora" + (hours > 1L ? "s" : "")).append(" ");
            }

            if (minutes > 0L) {
                sb.append(minutes).append(" ").append("minuto" + (minutes > 1L ? "s" : "")).append(" ");
            }

            if (seconds > 0L) {
                sb.append(seconds).append(" ").append("segundo" + (seconds > 1L ? "s" : ""));
            }

            return sb.toString();
        }
    }

    public static String formatDifference(long time) {
        long timeLefting = time - System.currentTimeMillis();
        long seconds = timeLefting / 1000L;
        return fromLong(seconds);
    }

    public static long parseDateDiff(String time) throws Exception {
        Pattern timePattern = Pattern.compile("(?:([0-9]+)\\s*y[a-z]*[,\\s]*)?" + "(?:([0-9]+)\\s*mo[a-z]*[,\\s]*)?"
                + "(?:([0-9]+)\\s*w[a-z]*[,\\s]*)?" + "(?:([0-9]+)\\s*d[a-z]*[,\\s]*)?"
                + "(?:([0-9]+)\\s*h[a-z]*[,\\s]*)?" + "(?:([0-9]+)\\s*m[a-z]*[,\\s]*)?"
                + "(?:([0-9]+)\\s*(?:s[a-z]*)?)?", Pattern.CASE_INSENSITIVE);
        Matcher m = timePattern.matcher(time);
        int years = 0;
        int months = 0;
        int weeks = 0;
        int days = 0;
        int hours = 0;
        int minutes = 0;
        int seconds = 0;
        boolean found = false;
        while (m.find()) {
            if (m.group() == null || m.group().isEmpty()) {
                continue;
            }
            for (int i = 0; i < m.groupCount(); i++) {
                if (m.group(i) != null && !m.group(i).isEmpty()) {
                    found = true;
                    break;
                }
            }
            if (found) {
                if (m.group(1) != null && !m.group(1).isEmpty()) {
                    years = Integer.parseInt(m.group(1));
                }
                if (m.group(2) != null && !m.group(2).isEmpty()) {
                    months = Integer.parseInt(m.group(2));
                }
                if (m.group(3) != null && !m.group(3).isEmpty()) {
                    weeks = Integer.parseInt(m.group(3));
                }
                if (m.group(4) != null && !m.group(4).isEmpty()) {
                    days = Integer.parseInt(m.group(4));
                }
                if (m.group(5) != null && !m.group(5).isEmpty()) {
                    hours = Integer.parseInt(m.group(5));
                }
                if (m.group(6) != null && !m.group(6).isEmpty()) {
                    minutes = Integer.parseInt(m.group(6));
                }
                if (m.group(7) != null && !m.group(7).isEmpty()) {
                    seconds = Integer.parseInt(m.group(7));
                }
                break;
            }
        }
        if (!found) {
            throw new Exception("Illegal Date");
        }

        if (years > 20) {
            throw new Exception("Illegal Date");
        }

        Calendar c = new GregorianCalendar();
        if (years > 0) {
            c.add(Calendar.YEAR, years * 1);
        }
        if (months > 0) {
            c.add(Calendar.MONTH, months * 1);
        }
        if (weeks > 0) {
            c.add(Calendar.WEEK_OF_YEAR, weeks * 1);
        }
        if (days > 0) {
            c.add(Calendar.DAY_OF_MONTH, days * 1);
        }
        if (hours > 0) {
            c.add(Calendar.HOUR_OF_DAY, hours * 1);
        }
        if (minutes > 0) {
            c.add(Calendar.MINUTE, minutes * 1);
        }
        if (seconds > 0) {
            c.add(Calendar.SECOND, seconds * 1);
        }
        return c.getTimeInMillis();
    }

}
