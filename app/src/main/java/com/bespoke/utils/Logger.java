//===============================================================================
// (c) 2016 Basecamp Startups Pvt. Ltd.  All rights reserved.
// Original Author: Ankur Sharma
// Original Date: 18/11/2016.
//===============================================================================
package com.bespoke.utils;

import android.util.Log;

public class Logger {
    private static boolean loggerEnable = true;
    private static boolean errorLoggerEnable = true;

    public static void i(String tag, String msg){
        if(loggerEnable)
            Log.i(tag, msg);
    }

    public static void d(String tag, String msg){
        if(loggerEnable)
            Log.i(tag, msg);
    }

    public static void e(String tag, String msg){
        if(loggerEnable || errorLoggerEnable)
            Log.e(tag, msg);
    }
}
