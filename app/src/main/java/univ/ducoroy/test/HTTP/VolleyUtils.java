package univ.ducoroy.test.HTTP;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static android.provider.ContactsContract.CommonDataKinds.Website.URL;

public class VolleyUtils {

    public static void GET_METHOD(Context context, String url, final VolleyResponseListener listener) {

        // Initialize a new StringRequest
        StringRequest stringRequest = new StringRequest(
                Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        listener.onResponse(response);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        listener.onError(error.toString());

                    }
                }) {


        };

        // Access the RequestQueue through singleton class.
        RequestSingleton.getInstance(context).addToRequestQueue(stringRequest);
    }

    public static void POST_METHOD(Context context, String url, final Map<String, String> getParams, final VolleyResponseListener listener) {
        // Initialize a new StringRequest
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        listener.onResponse(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        listener.onError(error.toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                return getParams;
            }
        };

        // Access the RequestQueue through singleton class.
        RequestSingleton.getInstance(context).addToRequestQueue(stringRequest);
    }
}