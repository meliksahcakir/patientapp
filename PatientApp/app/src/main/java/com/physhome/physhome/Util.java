package com.physhome.physhome;

import android.app.ProgressDialog;
import android.text.Html;
import android.text.Spanned;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Meliksah on 10/12/2017.
 */

public class Util {
    @SuppressWarnings("deprecation")
    public static Spanned fromHtml(String html){
        Spanned result;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            result = Html.fromHtml(html,Html.FROM_HTML_MODE_LEGACY);
        } else {
            result = Html.fromHtml(html);
        }
        return result;
    }
    public static String ChangeDateFormat(String dateString, String oldFormat, String newFormat){
        try {
            SimpleDateFormat sdfOld = new SimpleDateFormat(oldFormat);
            SimpleDateFormat sdfNew = new SimpleDateFormat(newFormat);
            Date date = sdfOld.parse(dateString);
            return sdfNew.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return  dateString;
    }

    public static Date StringToDate(String dateString, String format){
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return sdf.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return  null;
    }
    public static String DateToString(Date date, String format){
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }
    public static String dateToString(Calendar c,String format){
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(c.getTime());
    }
    public static void dismissProgressDialog(ProgressDialog pd) {
        if (pd!= null && pd.isShowing()) {
            pd.dismiss();
        }
    }
}
