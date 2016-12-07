package com.bespoke.servercommunication;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.android.volley.Request;
import com.bespoke.callback.APIRequestCallback;
import com.bespoke.commons.Commons;
import com.bespoke.utils.Logger;
import com.bespoke.utils.Utils;

import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by admin on 11/15/2016.
 */
public class CommunicatorNew {
    private final String TAG = getClass().getSimpleName();
    private Context mContext;
    private HashMap<String, String> params;
    private String methodName;
    private String tag_json_obj = "json_obj_req";
    private HashMap<String, String> header;

    public CommunicatorNew(Context mContext, int methodType, String methodName, HashMap<String, String> params) {
        this.mContext = mContext;
        this.params = params;
        this.methodName = methodName;
        //network call for api.
        callAPIMethod(methodType, methodName);
    }

    public void callAPIMethod(int method, String methodName) {
        switch (method) {
            case Request.Method.GET:
               /* String paramString=prepareRequestObject().toString();
                if(TextUtils.isEmpty(paramString))paramString="";*/
                new NetworkAsyncTask(APIUtils.BASE_URL, Commons.GET, methodName,getParamsString(params)).
                        executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (APIRequestCallback) mContext);
                break;
            case Request.Method.POST:
               /* String paramStringnew=prepareRequestObject().toString();
                if(TextUtils.isEmpty(paramStringnew)){paramStringnew="";}*/
                new NetworkAsyncTask(APIUtils.BASE_URL, Commons.POST, methodName, prepareRequestObject().toString()).
                        executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (APIRequestCallback) mContext);
                break;
        }
    }

    /**
     * Prepares the request json object for given operation
     *
     * @return
     */
    public JSONObject prepareRequestObject() {
        JSONObject jsonObject = null;
        if (methodName.equalsIgnoreCase(APIUtils.METHOD_REGISTER_USER)) {
            try {
                jsonObject = new JSONObject(params);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return jsonObject;
        }
        if (methodName.equalsIgnoreCase(APIUtils.METHOD_LOGIN)) {
            try {
                jsonObject = new JSONObject(params);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return jsonObject;
        }
        if (methodName.equalsIgnoreCase(APIUtils.METHOD_FORGOT_PASSWORD)) {
            try {
                jsonObject = new JSONObject(params);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return jsonObject;
        }

        if (methodName.equalsIgnoreCase(APIUtils.METHOD_GET_USER_BY_TYPE)) {
            try {
                jsonObject = new JSONObject(params);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return jsonObject;
        }
        return null;
    }

    /*Common thread for background operation for GET, POST, PUT, DELETE, NONE and NONE WITH EMP ID*/
    class NetworkAsyncTask extends AsyncTask<APIRequestCallback, Void, Void> {

        private URL url = null;
        private String httpMethodType = "";
        private String methodName = "";
        private String jsonString = "";
        HttpURLConnection urlConn = null;

        public NetworkAsyncTask(String url, String httpMethodType, String methodName, String jsonString) {
            try {
                this.url = new URL(url);
            } catch (Exception e) {
                e.printStackTrace();
            }
            this.httpMethodType = httpMethodType;
            this.methodName = methodName;
            this.jsonString = jsonString;
        }

        @Override
        protected Void doInBackground(APIRequestCallback... params) {
            switch (httpMethodType) {
                //Get entities from server e.g. for requiedSetPassword method
                case Commons.GET:
                    callHttpMethodInBackground(Commons.GET, params);
                    break;
                //Add entities e.g. for add employees
                case Commons.POST:
                    callHttpMethodInBackground(Commons.POST, params);
                    break;
            }
            return null;
        }

        /*
        * Common call for Http GET, POST, PUT, DELETE and return callback in calling method
        * */
        private Void callHttpMethodInBackground(String i, APIRequestCallback... params) {
            DataOutputStream outputStream;
            DataInputStream input;
            try {

                switch (i) {
                    //For GET
                    case Commons.GET:
                        try {
                            String urlString=APIUtils.BASE_URL + APIUtils.METHOD_NAME + "=" + methodName + jsonString;
                            Log.i("URL",urlString);
                            url = new URL(urlString);
                            urlConn = (HttpURLConnection) url.openConnection();
                            urlConn.setRequestMethod("GET");
                            urlConn.setDoInput(true);
                            urlConn.setDoOutput(true);
                            urlConn.setUseCaches(false);
                            urlConn.setConnectTimeout(10000);
                            urlConn.setRequestProperty("Content-Type", "application/json");
                            urlConn.connect();
                        } catch (Exception e) {
                            params[Commons.ZERO].onFailure(methodName, e);
                            e.printStackTrace();
                            Log.e("Exception", e.toString());
                        }
                        break;
                    //For POST
                    case Commons.POST:

                        try {
                            url = new URL(APIUtils.BASE_URL + APIUtils.METHOD_NAME + "=" + methodName);
                            urlConn = (HttpURLConnection) url.openConnection();
                            // urlConn.
                            urlConn.setRequestMethod("POST");
                            urlConn.setDoInput(true);
                            urlConn.setDoOutput(true);
                            urlConn.setUseCaches(false);
                            urlConn.setConnectTimeout(10000);
                            urlConn.setRequestProperty("Content-Type", "application/json");
                            urlConn.connect();
                            outputStream = new DataOutputStream(urlConn.getOutputStream());
                            outputStream.writeBytes(jsonString);//URLEncoder.encode(
                            outputStream.flush();
                            outputStream.close();
                        } catch (Exception e) {
                            params[Commons.ZERO].onFailure(methodName, e);
                            e.printStackTrace();
                            Log.e("Exception", e.toString());
                        }
                        break;
                }
            } catch (Exception e) {
                 params[Commons.ZERO].onFailure(methodName, e);
                Log.e("Exception", e.toString());
                e.printStackTrace();
                return null;
            }
            // Set up the header types needed to properly transfer JSON
            // Execute POST
            try {
                switch (i) {
                    //For GET
                    case Commons.GET:
                        if (urlConn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                            Log.i("HARI1", "getResponseCode END");
                            String out = Utils.convertResponseToString(urlConn.getInputStream());
                            params[0].onSuccess(methodName, out);
                        } else {
                            params[Commons.ZERO].onFailure(methodName,urlConn);
                            //handle error
                        }
                        break;

                    //For POST
                    case Commons.POST:
                        if (urlConn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                            Log.i("HARI1", "getResponseCode END");
                            String out = Utils.convertResponseToString(urlConn.getInputStream());
                            params[0].onSuccess(methodName, out);
                        } else {
                            params[Commons.ZERO].onFailure(methodName,urlConn);
                            //handle error
                        }
                            break;
                }

            } catch (Exception e) {
                params[Commons.ZERO].onFailure(methodName, e);
                e.printStackTrace();
                Log.e("Exception", e.toString());
            }
            return null;
        }
    }

    private String getParamsString(HashMap<String, String> map) {
        StringBuilder sb = new StringBuilder();
        if(map.size()>0){
            sb.append("&");
            List items = new ArrayList(map.keySet());
            for (int i = 0; i < items.size(); i++) {
                sb.append(items.get(i));
                sb.append("=");
                sb.append(map.get(items.get(i)));
                if (i < (items.size() - 1)) {
                    sb.append("&");
                }
            }
        }
        Logger.i(TAG, "ParamsString: "+sb.toString());
        return sb.toString();
    }
}
