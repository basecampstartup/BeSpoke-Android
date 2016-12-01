package com.bespoke.servercommunication;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.bespoke.BeSpokeApplication;
import com.bespoke.callback.APIRequestCallback;
import com.bespoke.commons.Commons;
import com.bespoke.sprefs.AppSPrefs;
import com.bespoke.utils.Logger;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.utils.URLEncodedUtils;
import cz.msebera.android.httpclient.message.BasicNameValuePair;

/**
 * Created by admin on 11/15/2016.
 */
public class Communicator {
    private final String TAG = getClass().getSimpleName();
    private Context mContext;
    private HashMap<String, String> params;
    private String methodName;
    private String tag_json_obj = "json_obj_req";
    private HashMap<String, String> header;

    public Communicator(Context mContext,int methodType, String methodName, HashMap<String, String> params) {
        this.mContext = mContext;
        this.params = params;
        this.methodName = methodName;

        //network call for api.
        callAPIMethod(methodType);
    }

    public void callAPIMethod(int method){
        switch (method){
        case Request.Method.GET:
            callGetAPI();
        break;
        case Request.Method.POST:
            makeJsonObjReq();
        break;
        }
    }

    public void requestString() {
        Logger.i(TAG, "Request url: " + url
                + "\nmethodName: " + methodName
                + "\nkeys: " + params.keySet()
                + "\nvalues: " + params.values());

    }


    /**
     * This method will be used to call server api and redirect the response to respective screen.
     */
    public void callGetAPI() {
        //Log request parameters
        toString();
        String url=APIUtils.BASE_URL+APIUtils.METHOD_NAME+"="+APIUtils.METHOD_LOGIN;
        List<NameValuePair> list = new ArrayList<NameValuePair>(params.size());
        for (Map.Entry<String, String> entry : params.entrySet()) {
            list.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }
        String urlString = URLEncodedUtils.format(list, "utf-8");
        url=url+urlString;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(APIUtils.METHOD_LOGIN.equalsIgnoreCase(methodName)
                                || APIUtils.METHOD_REGISTER_USER.equalsIgnoreCase(methodName)){
                            AppSPrefs.setString(Commons.USER_ID, params.get(Commons.USER_ID));
                            AppSPrefs.setString(Commons.PASSWORD, params.get(Commons.PASSWORD));
                        }
                        ((APIRequestCallback) mContext).onSuccess(methodName, response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        ((APIRequestCallback) mContext).onFailure(methodName, error);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Logger.i(TAG, "params: " + params);
                return params;
            }

        };
        BeSpokeApplication.getInstance().addToRequestQueue(stringRequest, tag_json_obj);
        //For managing timeout of request.
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }


    private void makeJsonObjReq() {
        String url=APIUtils.BASE_URL+APIUtils.METHOD_NAME+"="+methodName;
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, url, prepareRequestObject(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());
                        if(APIUtils.METHOD_LOGIN.equalsIgnoreCase(methodName)
                                || APIUtils.METHOD_REGISTER_USER.equalsIgnoreCase(methodName)){
                            AppSPrefs.setString(Commons.USER_ID, params.get(Commons.USER_ID));
                            AppSPrefs.setString(Commons.PASSWORD, params.get(Commons.PASSWORD));
                        }
                        ((APIRequestCallback) mContext).onSuccess(methodName, response);
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
            }
        }) {
            /**
             * Passing some request headers
             * */
          /*  @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return prepareHeader();
            }*/
        };
        //  requestString();
        // Adding request to request queue
        BeSpokeApplication.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
        // For managing timeout of request.
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
         // Cancelling request
        // ApplicationController.getInstance().getRequestQueue().cancelAll(tag_json_obj);
    }

    public HashMap<String,String> prepareHeader()
    {   header=new HashMap<>();
        header.put("Content-Type","application/json");
        //header.put("API-Key","QmVTcG9rZUFwaUtleQ");
        return header;
    }

    /**
     * Prepares the request json object for given operation
     * @return
     */
   public JSONObject prepareRequestObject()
   {
      JSONObject jsonObject = null;
      if(methodName.equalsIgnoreCase(APIUtils.METHOD_LOGIN)) {

          try {
              jsonObject = new JSONObject(params);
          } catch (Exception e) {
              e.printStackTrace();
          }
          return  jsonObject;
      }
       return null;
   }


    /**
     * This method will be used to call server api and redirect the response to respective screen.
     */
    String url="";
    public void callPostAPI() {
        //Log request parameters

        url=APIUtils.BASE_URL + APIUtils.METHOD_NAME+"="+APIUtils.METHOD_LOGIN;
        requestString();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        /*if(APIUtils.CMD_SIGN_IN.equalsIgnoreCase(methodName)
                                || APIUtils.CMD_SIGN_UP.equalsIgnoreCase(methodName)){
                            AppSPrefs.setString(Commons.USER_ID, params.get(Commons.USER_ID));
                            AppSPrefs.setString(Commons.PASSWORD, params.get(Commons.PASSWORD));
                        }*/
                        ((APIRequestCallback) mContext).onSuccess(methodName, response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        ((APIRequestCallback) mContext).onFailure(methodName, error);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Logger.i(TAG, "params: " + params);
                return params;
            }
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                header=new HashMap<>();
                header.put("Content-Type","application/json");
                return header;
            }

        };
        BeSpokeApplication.getInstance().addToRequestQueue(stringRequest, tag_json_obj);
        //For managing timeout of request.
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

}
