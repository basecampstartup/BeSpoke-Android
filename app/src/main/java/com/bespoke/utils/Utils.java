package com.bespoke.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.support.v7.app.AlertDialog;
import android.text.Spanned;

import com.bespoke.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by admin on 11/23/2016.
 */

public class Utils {


    /**
     * @param context context
     * @param fontId  fontid
     * @return Typeface
     * This method will return the font which apply on application text.
     */
    public static Typeface getTypeface(Context context, int fontId) {
        Typeface typeface = null;
        switch (fontId) {
            case 1:
                typeface = Typeface.createFromAsset(context.getAssets(), "avenir-roman.otf");
                break;
        }
        return typeface;
    }

    /**
     * @param response
     * @return
     * @throws Exception
     */
    public static String convertResponseToString(InputStream response) {
        InputStream in = null;
        BufferedReader reader = null;
        StringBuffer sb = new StringBuffer();
        try {
            in = response;
            reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            String serverResponse = null;
            while ((serverResponse = reader.readLine()) != null) {
                sb.append(serverResponse);
            }
        } catch (IllegalStateException e) {

        } catch (IOException e) {

        } finally {
            try {
                if (in != null) {
                    in.close();
                }
                if (reader != null) {
                    reader.close();
                }
            } catch (Exception e) {
            }
        }
        String str = sb.toString();
        return str;
    }

    public static void showErrorDialog(Context con,String message) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(con);
        // set dialog message
        alertDialogBuilder
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton(con.getResources().getString(R.string.CommonOK), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


    /**
     * @param context
     * @param title
     * @param message
     */
    public static void alertDialog(final Context context, String title, String message) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        // set title
        alertDialogBuilder.setTitle(title);

        // set dialog message
        alertDialogBuilder
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton(context.getResources().getString(R.string.CommonOK), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        //loading.setVisibility(View.INVISIBLE);
                    }
                });
        final AlertDialog alertDialog = alertDialogBuilder.create();
        try {
            // show it
            alertDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Method to return formatted date according to locale
     *
     * @param context
     * @param mDate
     * @param dateStyle
     * @return
     */
    public static String formatDate(Context context, Date mDate, DateStyleEnum.StyleType dateStyle) {
        switch (dateStyle) {
            case SHORT:
                DateFormat shortDateFormat = DateFormat.getDateInstance(DateFormat.SHORT, getSysLocale(context));
                return shortDateFormat.format(mDate);
            case MEDIUM:
                DateFormat mediumDateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM, getSysLocale(context));
                return mediumDateFormat.format(mDate);
            case LONG:
                DateFormat longDateFormat = DateFormat.getDateInstance(DateFormat.LONG, getSysLocale(context));
                return longDateFormat.format(mDate);
            case DAY_MONTH:
                return getDayMonthFormat(context, mDate);
            default:
                return "";
        }
    }
    /**
     * method to return system locale
     *
     * @return
     */
    private static Locale getSysLocale(Context context) {
        return context.getResources().getConfiguration().locale;
    }

    /**
     * method to return birth day
     *
     * @param mDate
     * @return
     */
    private static String getDayMonthFormat(Context context, Date mDate) {
        DateFormat dayMonthDateFormat = DateFormat.getDateInstance(DateFormat.LONG, getSysLocale(context));
        String bDate = dayMonthDateFormat.format(mDate);
        //extract year
        SimpleDateFormat formatYear = new SimpleDateFormat("yyyy");
        String currentYear = formatYear.format(mDate);
        String dateString = bDate.replace(currentYear, "");
        return dateString.replace(",", "");
    }
}
