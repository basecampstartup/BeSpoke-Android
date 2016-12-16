//===============================================================================
// (c) 2016 Basecamp Startups Pvt. Ltd.  All rights reserved.
// Original Author: Ankur Sharma
// Original Date: 28/11/2016
//===============================================================================
package com.bespoke.context;

import android.content.Context;

public class ContextHelper {

    private static Context context;

    /**
     * Get context
     * @return Context initialized
     * @throws Exception
     */
    public static Context getContext() {
        return context;
    }

    /**
     * Set context
     * @param context for initialization
     */
    public static void setContext(Context context) {
        ContextHelper.context = context;
    }
}
