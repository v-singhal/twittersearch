package com.vbstudio.twittersearch.network;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RequestQueue.RequestFilter;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.vbstudio.twittersearch.Constants.ConstanKeys;
import com.vbstudio.twittersearch.fragment.BaseFragment;
import com.vbstudio.twittersearch.preferences.HaptikAssignmentPreferences;

import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import twitter4j.BASE64Encoder;

public class NetworkManager implements RequestFilter {

    private static final int SOCKET_TIMEOUT_MS = 10000; //10 seconds

    private static RequestQueue requestQueue;
    private static String BASE_URL;

    public static String getBaseSnapdealUrl() {
        return BASE_URL;
    }

    private NetworkManager(Context context, String savedBaseUrl) {
        // TODO Auto-generated constructor stub
        if (requestQueue == null) {
            BASE_URL = savedBaseUrl;
            requestQueue = Volley.newRequestQueue(context, null, Volley.DEFAULT_CACHE_DIR);
            requestQueue.start();
        }
    }

    public static NetworkManager newInstance(Context context, String baseUrl) {
        NetworkManager instance = new NetworkManager(context, baseUrl);
        return instance;
    }

    @Override
    public boolean apply(Request<?> request) {
        return request.getTag() == this;
    }

    public Request<?> jsonRequest(JsonObjectRequest request, int identifier, boolean shouldCache) {
        request.setIdentifier(identifier);
        request.setShouldCache(shouldCache);
        request.setTag(this);
        request.setRetryPolicy(new DefaultRetryPolicy(SOCKET_TIMEOUT_MS, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        try {
            Log.i(BaseFragment.LOG_TAG, "-------------------------");
            Log.i(BaseFragment.LOG_TAG, "REQUEST HEADERS: " + request.getHeaders().toString());
            Log.i(BaseFragment.LOG_TAG, "REQUEST URL: " + request.getUrl().toString());
            Log.i(BaseFragment.LOG_TAG, "REQUEST PARAMS: " + request.getBodyString());
            Log.i(BaseFragment.LOG_TAG, "-------------------------");
        } catch (AuthFailureError authFailureError) {
            authFailureError.printStackTrace();
        }

        addRequest(request);
        return request;
    }

    public void addRequest(Request<?> request) {
        requestQueue.add(request);
    }

    public Request<?> jsonRequest(final Context context, int identifier, String url,
                                  Map<String, String> requestMap, Listener<JSONObject> listener,
                                  ErrorListener errorListener, boolean shouldCache) {

        String urlToHit = BASE_URL + url + getTwitterOAuthString(context, BASE_URL + url);
        JsonObjectRequest request = new JsonObjectRequest(urlToHit, new JSONObject(requestMap), listener, errorListener) {
            /*@Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<String, String>();
                params.putAll(getHeadersForRequest(context));
                return params;
            }*/
        };

        return jsonRequest(request, identifier, shouldCache);
    }


    public Request<?> jsonRequest(final Context context, int identifier, String url,
                                  JSONObject object, Listener<JSONObject> listener,
                                  ErrorListener errorListener, boolean shouldCache) {

        String urlToHit = BASE_URL + url + getTwitterOAuthString(context, BASE_URL + url);
        JsonObjectRequest request = new JsonObjectRequest(urlToHit, object, listener, errorListener) {
            /*@Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<String, String>();
                params.putAll(getHeadersForRequest(context));
                return params;
            }*/
        };
        ;
        return jsonRequest(request, identifier, shouldCache);
    }

    public Request<?> jsonGetRequest(int identifier, String url,
                                     Listener<String> listener,
                                     ErrorListener errorListener, boolean shouldCache) {
        StringRequest myReq = new StringRequest(
                Request.Method.GET,
                url,
                listener,
                errorListener);
        addRequest(myReq);

        return myReq;
    }

    public void cancel() {
        requestQueue.cancelAll(this);
    }

    public boolean hasRunningRequests() {
        return requestQueue.hasRequestsWithTag(this);
    }

    private Map<? extends String, ? extends String> getHeadersForRequest(Context context) {
        HashMap<String, String> oauthMap = new HashMap<>();

        oauthMap.put("oauth_consumer_key", ConstanKeys.TWITTER_CONSUMER_KEY);
        oauthMap.put("oauth_nonce", "");
        oauthMap.put("oauth_signature", "");
        oauthMap.put("oauth_signature_method", "HMAC-SHA1");
        oauthMap.put("oauth_timestamp", String.valueOf(Calendar.getInstance().getTimeInMillis()));
        oauthMap.put("oauth_token", HaptikAssignmentPreferences.getTwitterToken(context));
        oauthMap.put("oauth_version", "1.0");

        return oauthMap;
    }

    private String getTwitterOAuthString(Context context, String url) {
        String oauthString = "";

        oauthString += "&" +"oauth_consumer_key=" + ConstanKeys.TWITTER_CONSUMER_KEY;
        oauthString += "&" +"oauth_nonce=" + generateNOnce();
        oauthString += "&" +"oauth_signature=" + generateSignature(context, url);
        oauthString += "&" +"oauth_signature_method=" + "HMAC-SHA1";
        oauthString += "&" +"oauth_timestamp=" + String.valueOf(Calendar.getInstance().getTimeInMillis());
        oauthString += "&" +"oauth_token=" + HaptikAssignmentPreferences.getTwitterToken(context);
        oauthString += "&" +"oauth_version=" + "1.0";

        return oauthString;
    }

    private String generateNOnce() {
        String nOnce = "";

        while (nOnce.length() < 32) {
            nOnce += String.valueOf(Math.round(Math.random()));
        }
        return nOnce.substring(0, 32);
    }

    private String generateSignature(Context context, String signatueBaseStr) {
        String oAuthConsumerSecret = ConstanKeys.TWITTER_CONSUMER_SECRET;
        String oAuthTokenSecret = HaptikAssignmentPreferences.getTwitterTokenSecret(context);
        byte[] byteHMAC = null;
        try {
            Mac mac = Mac.getInstance("HmacSHA1");
            SecretKeySpec spec;
            if (null == oAuthTokenSecret) {
                String signingKey = encode(oAuthConsumerSecret) + '&';
                spec = new SecretKeySpec(signingKey.getBytes(), "HmacSHA1");
            } else {
                String signingKey = encode(oAuthConsumerSecret) + '&' + encode(oAuthTokenSecret);
                spec = new SecretKeySpec(signingKey.getBytes(), "HmacSHA1");
            }
            mac.init(spec);
            byteHMAC = mac.doFinal(signatueBaseStr.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return BASE64Encoder.encode(byteHMAC);
    }

    private String encode(String value) {
        String encoded = "";
        try {
            encoded = URLEncoder.encode(value, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        String stringToBeReturned = "";
        char focus;
        for (int i = 0; i < encoded.length(); i++) {
            focus = encoded.charAt(i);
            if (focus == '*') {
                stringToBeReturned += "%2A";
            } else if (focus == '+') {
                stringToBeReturned += "%20";
            } else if (focus == '%' && i + 1 < encoded.length() && encoded.charAt(i + 1) == '7' && encoded.charAt(i + 2) == 'E') {
                stringToBeReturned += '~';
                i += 2;
            } else {
                stringToBeReturned += focus;
            }
        }
        return stringToBeReturned.toString();
    }

}
