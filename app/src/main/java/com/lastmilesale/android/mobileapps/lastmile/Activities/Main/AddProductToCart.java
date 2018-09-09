package com.lastmilesale.android.mobileapps.lastmile.Activities.Main;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.lastmilesale.android.mobileapps.lastmile.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class AddProductToCart extends AsyncTask<Void, Integer, Integer> {

    private String user_id,product_id,noOfPieces,noOfDozens,noOfCartons,API;
    private Context context;
    private Response.Listener listener;
    String status;
    SharedPreferences appFile;
    SharedPreferences.Editor appFileEditor;
    int cartCount;

    //RecyclerView.Adapter homeSchedulesFeedRecyclerAdapter;

    public AddProductToCart(Context context,String product_id, String noOfPieces, String noOfDozens,String noOfCartons, String status){
        this.product_id = product_id;
        this.noOfPieces = noOfPieces;
        this.noOfDozens = noOfDozens;
        this.noOfCartons = noOfCartons;
        this.context = context;
        this.status = status;
        this.appFile = context.getSharedPreferences(context.getResources().getString(R.string.app_file),0);
        this.appFileEditor = appFile.edit();
        cartCount = Integer.parseInt(appFile.getString(context.getResources().getString(R.string.user_cart_count),"0"));
        this.API = "http://lastmilesale.com/api/order";
        user_id = context.getSharedPreferences(context.getResources().getString(R.string.app_file),0).getString(context.getResources().getString(R.string.active_user_id),null);
    }

    @Override
    protected Integer doInBackground(Void... params) {
        AddProductToCartRequest addProductToCartRequest = new AddProductToCartRequest(API,user_id,product_id,noOfPieces,noOfDozens,noOfCartons,status,listener);
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(addProductToCartRequest);
        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
//        errorListener = initializeErrorListener();
        listener = initializeResponseListener();
    }

    @Override
    protected void onPostExecute(Integer integer) {
        super.onPostExecute(integer);
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
    }
//    protected Response.ErrorListener initializeErrorListener(){
//        Response.ErrorListener localErrorListener = new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                if(error instanceof TimeoutError){
//                    //SETTING UP MESSAGE
//                    String msg = context.getResources().getString(R.string.timeout_error);
//                    Snackbar snack = Snackbar.make(rootView.findViewById(R.id.cart_fab), msg, Snackbar.LENGTH_LONG);
//                    View view = snack.getView();
//                    FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) view.getLayoutParams();
//                    params.gravity = Gravity.TOP;
//                    view.setLayoutParams(params);
//                    snack.show();
//
//                }else if (error instanceof AuthFailureError){
//                    //SETTING UP MESSAGE
//                    String msg = context.getResources().getString(R.string.authentication_error);
//                    Snackbar snack = Snackbar.make(rootView.findViewById(R.id.cart_fab), msg, Snackbar.LENGTH_LONG);
//                    View view = snack.getView();
//                    FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) view.getLayoutParams();
//                    params.gravity = Gravity.TOP;
//                    view.setLayoutParams(params);
//                    snack.show();
//
//
//                }else if(error instanceof NetworkError){
//                    //SETTING UP MESSAGE
//                    String msg = context.getResources().getString(R.string.network_error);
//                    Snackbar snack = Snackbar.make(rootView.findViewById(R.id.cart_fab), msg, Snackbar.LENGTH_LONG);
//                    View view = snack.getView();
//                    FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) view.getLayoutParams();
//                    params.gravity = Gravity.TOP;
//                    view.setLayoutParams(params);
//                    snack.show();
//
//                }else if(error instanceof ServerError){
//                    //SETTING UP MESSAGE
//                    String msg = context.getResources().getString(R.string.serverbusy_error);
//                    Snackbar snack = Snackbar.make(rootView.findViewById(R.id.cart_fab), msg, Snackbar.LENGTH_LONG);
//                    View view = snack.getView();
//                    FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) view.getLayoutParams();
//                    params.gravity = Gravity.TOP;
//                    view.setLayoutParams(params);
//                    snack.show();
//
//                }else if(error instanceof ParseError){
//                    //SETTING UP MESSAGE
//                    String msg = context.getResources().getString(R.string.parse_error);
//                    Snackbar snack = Snackbar.make(rootView.findViewById(R.id.cart_fab), msg, Snackbar.LENGTH_LONG);
//                    View view = snack.getView();
//                    FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) view.getLayoutParams();
//                    params.gravity = Gravity.TOP;
//                    view.setLayoutParams(params);
//                    snack.show();
//                }
//            }
//        };
//        return localErrorListener;
//    }

    protected Response.Listener<String> initializeResponseListener(){
        Response.Listener<String> localResponseListener = new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");
                    if(success){
                     Toast.makeText(context,"Added",Toast.LENGTH_SHORT).show();
                        cartCount ++;
                        ((ProductsActivity)context).cartCount ++;
                        appFileEditor.putString(context.getResources().getString(R.string.user_cart_count),cartCount+"").commit();
                        ((ProductsActivity)context).cartCountBadge.setVisibility(View.VISIBLE);
                        ((ProductsActivity)context).cartCountBadge.setText(cartCount+"");
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

class AddProductToCartRequest extends StringRequest{
    private Map<String, String> params;

    public AddProductToCartRequest(String API,String user_id, String product_id,String noOfPieces, String noOfDozens,String noOfCartons,String status, Response.Listener<String> listener){
        super(Method.POST, API, listener, null);
        params = new HashMap<>();
        params.put("user_id",user_id);
        params.put("product_id",product_id);
        params.put("no_of_pieces",noOfPieces);
        params.put("no_of_dozens",noOfDozens);
        params.put("no_of_cartons",noOfCartons);
        params.put("status",status);
    }


    public Map<String, String> getParams(){
        return params;
    }
}
