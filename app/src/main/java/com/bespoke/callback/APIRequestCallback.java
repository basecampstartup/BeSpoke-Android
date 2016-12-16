//===============================================================================
// (c) 2016 Basecamp Startups Pvt. Ltd.  All rights reserved.
// Original Author: Ankur Sharma
// Original Date: 28/11/2016
//===============================================================================
package com.bespoke.callback;

/**
 * call back handler for handling webservice responses
 */
public interface APIRequestCallback {

    /**
     * Called, When ajax response in successfully transformed into the desired object
     * @param name   string call name returned from ajax response on success
     * @param object object returned from ajax response on success
     */
    void onSuccess(String name, Object object);

    /**
     * Called, When there happens any kind of error, exception or failure in getting ajax response from the server
     *
     * @param name   string call name returned from ajax response on failure
     * @param object returned from ajax response on failure
     */
    void onFailure(String name, Object object);

}
