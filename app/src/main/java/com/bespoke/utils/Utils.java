package com.bespoke.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.support.v7.app.AlertDialog;

import com.bespoke.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

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

}