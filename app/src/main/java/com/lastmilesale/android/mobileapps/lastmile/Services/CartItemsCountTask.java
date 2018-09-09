package com.lastmilesale.android.mobileapps.lastmile.Services;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.lastmilesale.android.mobileapps.lastmile.R;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

public class CartItemsCountTask extends AsyncTask<Void,Void,Void> {
    Context context;
    SharedPreferences appFile;
    SharedPreferences.Editor appFileEditor;
    String user_id,API;
    Response.Listener<String> listener;

    public CartItemsCountTask(Context context){
        this.context = context;
        this.appFile = context.getSharedPreferences(context.getResources().getString(R.string.app_file),0);
        this.appFileEditor = appFile.edit();
        this.user_id = appFile.getString(context.getResources().getString(R.string.active_user_id),"");
        this.API = "http://lastmilesale.com/api/orders/cart/"+user_id;
        this.listener = initializeResponseListener();
    }

    @Override
    protected Void doInBackground(Void... params) {
        CartItemsCountRequest cartItemsCountRequest = new CartItemsCountRequest(API,listener);
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(cartItemsCountRequest);
        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
    }

    protected Response.Listener<String> initializeResponseListener(){
        Response.Listener<String> localResponseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    appFileEditor.putString(context.getResources().getString(R.string.user_cart_count),jsonArray.length()+"").commit();
                } catch (JSONException em) {em.printStackTrace();}
            }
        };
        return localResponseListener;
    }
}


class CartItemsCountRequest extends StringRequest {

    private Map<String, String> params;

    public CartItemsCountRequest(String API,Response.Listener<String> listener){
        super(Request.Method.GET,API,listener, null);
        params = new HashMap<>();
    }

    public Map<String, String> getParams(){
        return params;
    }
}
