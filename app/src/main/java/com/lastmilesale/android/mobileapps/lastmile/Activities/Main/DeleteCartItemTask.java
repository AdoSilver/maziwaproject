package com.lastmilesale.android.mobileapps.lastmile.Activities.Main;

import android.content.Context;
import android.os.AsyncTask;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


class DeleteCartItemTask extends AsyncTask<Void, Void, Void> {

    String id, API;
    Context context;
    Response.Listener<String> Listener;

    public DeleteCartItemTask(Context context, String id) {
        this.context = context;
        this.id = id;
        this.Listener = initializeResponseListener();
        this.API = "http://lastmilesale.com/api/delete-order/"+id;
    }

    @Override
    protected Void doInBackground(Void... params) {
        DeleteCartItemTaskRequest ar = new DeleteCartItemTaskRequest(API,id,Listener);
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(ar);
        return null;
    }


    protected Response.Listener<String> initializeResponseListener(){
        Response.Listener<String> localResponseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    boolean success = false;
                    //ACTIONS STARTS HERE
                    for(int i = 0; i<jsonArray.length(); i++) {
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = jsonArray.getJSONObject(i);
                            success = jsonObject.getBoolean("success");
                            if(success){
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    //ACTIONS ENDS HERE
                } catch (JSONException em) {
                    em.printStackTrace();
                }
            }
        };
        return localResponseListener;
    }

}


class DeleteCartItemTaskRequest extends StringRequest {

    private Map<String, String> params;

    public DeleteCartItemTaskRequest(String API,String order_id, Response.Listener<String> listener){
        super(Method.GET,API,listener, null);
        params = new HashMap<>();
        params.put("order_id",order_id);

    }

    public Map<String, String> getParams(){
        return params;
    }
}
