package com.lastmilesale.android.mobileapps.lastmile.Activities.Main;


import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.lastmilesale.android.mobileapps.lastmile.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SwitchAllCartsToOrders extends AsyncTask<Void, Integer, Integer> {
    private String user_id,order_id,API;
    private Context context;
    private Response.Listener listener;
    String player_id;
    List<String> orderList;

    public SwitchAllCartsToOrders(Context context, List<String> orderList, String player_id){
        this.context = context;
        this.player_id = player_id;
        this.orderList = orderList;
        this.API = "http://lastmilesale.com/api/checkout-all-orders";
        this.listener = initializeResponseListener();
        user_id = context.getSharedPreferences(context.getResources().getString(R.string.app_file),0).getString(context.getResources().getString(R.string.active_user_id),null);
    }

    @Override
    protected Integer doInBackground(Void... params) {
        SwitchAllCartsToOrdersRequest addProductToCartRequest = new SwitchAllCartsToOrdersRequest(API,orderList,player_id,listener);
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(addProductToCartRequest);
        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Integer integer) {
        super.onPostExecute(integer);
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
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");
                    if(success){
                        Toast.makeText(context,"Added",Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(context,"Failed",Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException em) {
//                    em.printStackTrace();
//                    String b_msg = "Temporary error occured, try again later";
//                    Snackbar snack = Snackbar.make(rootView.findViewById(R.id.cart_fab), b_msg, Snackbar.LENGTH_LONG);
//                    View view = snack.getView();
//                    FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) view.getLayoutParams();
//                    params.gravity = Gravity.TOP;
//                    view.setLayoutParams(params);
//                    snack.show();
                }
            }
        };
        return localResponseListener;
    }

}


class SwitchAllCartsToOrdersRequest extends StringRequest {
    private Map<String, String> params;

    public SwitchAllCartsToOrdersRequest(String API,List<String> orders, String player_id, Response.Listener<String> listener){
        super(Method.POST, API, listener, null);
        params = new HashMap<>();
        JSONArray jsonArray = new JSONArray();
        for(int i=0; i < orders.size(); i++){
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("id",orders.get(i));
                jsonArray.put(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        params.put("orders",jsonArray.toString());
        params.put("player_id",player_id);
    }


    public Map<String, String> getParams(){
        return params;
    }
}
