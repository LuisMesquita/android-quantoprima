package br.com.mobiclub.quantoprima.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;

import br.com.mobiclub.quantoprima.domain.Mobi;

/**
 *
 */
public class Util {

    private static DateFormat iso8601Format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    final static String pattern = "yyyy-MM-dd'T'hh:mm:ss";
    final static SimpleDateFormat sdf = new SimpleDateFormat(pattern);

    final static SimpleDateFormat formatDateTime = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    final static SimpleDateFormat formatDate = new SimpleDateFormat("dd/MM/yyyy");

    final static SimpleDateFormat formatSignUpDate = new SimpleDateFormat("MM/dd/yyyy");

    final static SimpleDateFormat facebookFormatDate = new SimpleDateFormat("MM/dd/yyyy");

    public static boolean isShotMobi(String code) {
        if(code.startsWith(Mobi.SHOT_MOBI_CODE)) {
            return true;
        }
        return false;
    }

    public static Date getDate(String stringDate) {
        if(stringDate == null)
            return null;
        try {
            return iso8601Format.parse(stringDate);
        } catch (ParseException e) {
            Ln.w(e);
        }
        return null;
    }

    public static String getSQLDateString(Date date) {
        if(date == null)
            return null;
        try {
            return iso8601Format.format(date);
        } catch (Exception e) {
        }
        return null;
    }

    public static Date paserTPatternDate(String date) {
        if(date == null)
            return null;
        try
        {
            Date d = sdf.parse(date);
            return d;
        }
        catch (ParseException e)
        {
        }
        return null;
    }

    public static String getDateTimeString(Date date) {
        if(date == null)
            return null;
        String stringDate = formatDateTime.format(date);
        return stringDate;
    }

    public static String getDateString(Date date) {
        if(date == null)
            return null;
        String stringDate = formatDate.format(date);
        return stringDate;
    }

    public static String getSignUpDateString(Date date) {
        if(date == null)
            return null;
        String stringDate = formatSignUpDate.format(date);
        return stringDate;
    }

    public static String getDistanceString(Double distance) {
        if(distance > 1.0) {
            return String.format("%.1fkm", distance);
        }
        return String.format("%dm", (int)(distance * 1000));
    }

    public static String formatNotificationDate(Date date) {
        if(date == null)
            return null;
        Calendar now = Calendar.getInstance();
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        String formatDate = Util.getDateString(date);
        //se for mesma semana
        if(now.get(Calendar.WEEK_OF_MONTH) == c.get(Calendar.WEEK_OF_MONTH) &&
                now.get(Calendar.DAY_OF_MONTH) - c.get(Calendar.DAY_OF_MONTH) <= 7) {
            formatDate = new SimpleDateFormat("EEEE").format(date);
        }
        //se for mesmo dia
        if(now.get(Calendar.DAY_OF_MONTH) == c.get(Calendar.DAY_OF_MONTH)
                && now.get(Calendar.MONTH) == c.get(Calendar.MONTH)) {
            int min = c.get(Calendar.MINUTE);
            String minString = String.valueOf(min);
            if(min >= 0 && min <= 9) {
                minString = "0"+String.valueOf(min);
            }
            formatDate = c.get(Calendar.HOUR_OF_DAY) + ":" + minString;
        }
        return formatDate;
    }

    public static Date parseFacebookDate(String birthday) {
        if(birthday == null)
            return null;
        try
        {
            Date d = facebookFormatDate.parse(birthday);
            return d;
        }
        catch (ParseException e)
        {
        }
        return null;
    }

    public static Date parseDate(String date) {
        if(date == null)
            return null;
        try
        {
            Date d = formatDate.parse(date);
            return d;
        }
        catch (ParseException e)
        {
        }
        return null;
    }

    public static boolean isValidarCpf(String cpf){
        Pattern sPattern = Pattern.compile("([0-9]{2}[\\.]?[0-9]{3}[\\.]?[0-9]{3}[\\/]?[0-9]{4}[-]?[0-9]{2})|([0-9]{3}[\\.]?[0-9]{3}[\\.]?[0-9]{3}[-]?[0-9]{2})");

        return sPattern.matcher(cpf).matches();
    }
}
