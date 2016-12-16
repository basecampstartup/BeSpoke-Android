//===============================================================================
// (c) 2016 Basecamp Startups Pvt. Ltd.  All rights reserved.
// Original Author: Ankur Sharma
// Original Date: 23/11/2016.
//===============================================================================
package com.bespoke.sprefs;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import com.bespoke.commons.Commons;
import com.bespoke.context.ContextHelper;

public class AppSPrefs {

    private static SharedPreferences sPrefs;
    private static SharedPreferences.Editor editor;
    /**
     *This method to get preference reference.
     * @return
     */
    private static SharedPreferences getSPrefsInstance() {
        if (sPrefs == null) {
            sPrefs = PreferenceManager.getDefaultSharedPreferences(ContextHelper.getContext());
        }
        return sPrefs;
    }

    public static void clearAppSPrefs(){
        editor = getSPrefsInstance().edit();
        editor.clear();
        editor.commit();
    }

    public static String getString(String key){
        return getSPrefsInstance().getString(key, "");
    }

    public static void setString(String key, String value){
        editor = getSPrefsInstance().edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static boolean isAlreadyLoggedIn(){
        return getSPrefsInstance().getBoolean(Commons.IS_ALREADY_LOGGED_IN, false);
    }

    public static void setAlreadyLoggedIn(boolean value){
        editor = getSPrefsInstance().edit();
        editor.putBoolean(Commons.IS_ALREADY_LOGGED_IN, value);
        editor.commit();
    }


}
