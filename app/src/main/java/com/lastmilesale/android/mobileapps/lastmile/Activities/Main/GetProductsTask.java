package com.lastmilesale.android.mobileapps.lastmile.Activities.Main;

import android.content.Context;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.ParseError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.lastmilesale.android.mobileapps.lastmile.Objects.Product;
import com.lastmilesale.android.mobileapps.lastmile.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class GetProductsTask extends AsyncTask<Void, Integer, Integer> {

    private String user_id,supplier_id,API;
    private View rootView;
    private Context context;
    private RecyclerView productsRecyclerView;
    private RecyclerView.LayoutManager recyclerViewlayoutManager;
    private Response.ErrorListener errorListener;
    private Response.Listener listener;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RelativeLayout noProductsLayout;

    private boolean isRefreshing = false;

    //RecyclerView.Adapter homeSchedulesFeedRecyclerAdapter;

    public GetProductsTask(Context context,View rootView, String user_id, String supplier_id,boolean isRefreshing){
        this.user_id = user_id;
        this.supplier_id = supplier_id;
        this.context = context;
        this.rootView = rootView;
        this.isRefreshing = isRefreshing;
        this.API = "http://lastmilesale.com/api/products/supplier/"+supplier_id;
        this.productsRecyclerView = (RecyclerView) rootView.findViewById(R.id.products_recycler_view);
        this.swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.products_swipe_refresh_layout);
        this.noProductsLayout = (RelativeLayout) rootView.findViewById(R.id.no_products_layout);
    }

    @Override
    protected Integer doInBackground(Void... params) {
        if(isRefreshing){
            ((ProductsActivity)context).productList.clear();
        }
        GetProductsRequest getProductsRequest = new GetProductsRequest(API,supplier_id,listener,errorListener);
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(getProductsRequest);
        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        swipeRefreshLayout.setRefreshing(true);
        productsRecyclerView.setHasFixedSize(true);
        recyclerViewlayoutManager = new LinearLayoutManager(context);
        productsRecyclerView.setLayoutManager(recyclerViewlayoutManager);
        errorListener = initializeErrorListener();
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


    protected Response.ErrorListener initializeErrorListener(){
        Response.ErrorListener localErrorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                swipeRefreshLayout.setRefreshing(false);
                noProductsLayout.setVisibility(View.VISIBLE);
                if(error instanceof TimeoutError){
                    //SETTING UP MESSAGE
                    String msg = context.getResources().getString(R.string.timeout_error);
                    Snackbar snack = Snackbar.make(rootView.findViewById(R.id.products_fab), msg, Snackbar.LENGTH_LONG);
                    View view = snack.getView();
                    FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) view.getLayoutParams();
                    params.gravity = Gravity.TOP;
                    view.setLayoutParams(params);
                    snack.show();

                }else if (error instanceof AuthFailureError){
                    //SETTING UP MESSAGE
                    String msg = context.getResources().getString(R.string.authentication_error);
                    Snackbar snack = Snackbar.make(rootView.findViewById(R.id.products_fab), msg, Snackbar.LENGTH_LONG);
                    View view = snack.getView();
                    FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) view.getLayoutParams();
                    params.gravity = Gravity.TOP;
                    view.setLayoutParams(params);
                    snack.show();


                }else if(error instanceof NetworkError){
                    //SETTING UP MESSAGE
                    String msg = context.getResources().getString(R.string.network_error);
                    Snackbar snack = Snackbar.make(rootView.findViewById(R.id.products_fab), msg, Snackbar.LENGTH_LONG);
                    View view = snack.getView();
                    FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) view.getLayoutParams();
                    params.gravity = Gravity.TOP;
                    view.setLayoutParams(params);
                    snack.show();

                }else if(error instanceof ServerError){
                    //SETTING UP MESSAGE
                    String msg = context.getResources().getString(R.string.serverbusy_error);
                    Snackbar snack = Snackbar.make(rootView.findViewById(R.id.products_fab), msg, Snackbar.LENGTH_LONG);
                    View view = snack.getView();
                    FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) view.getLayoutParams();
                    params.gravity = Gravity.TOP;
                    view.setLayoutParams(params);
                    snack.show();

                }else if(error instanceof ParseError){
                    //SETTING UP MESSAGE
                    String msg = context.getResources().getString(R.string.parse_error);
                    Snackbar snack = Snackbar.make(rootView.findViewById(R.id.products_fab), msg, Snackbar.LENGTH_LONG);
                    View view = snack.getView();
                    FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) view.getLayoutParams();
                    params.gravity = Gravity.TOP;
                    view.setLayoutParams(params);
                    snack.show();
                }
            }
        };
        return localErrorListener;
    }

    protected Response.Listener<String> initializeResponseListener(){
        Response.Listener<String> localResponseListener = new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    //JSON_PARSE_DATA_AFTER_WEBCALL(jsonArray);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = null;
                        Product product = null;
                        try {
                            jsonObject = jsonArray.getJSONObject(i);
                            product = new Product(jsonObject.getString("id"),jsonObject.getString("supplier_id"),jsonObject.getString("sku"),jsonObject.getString("name"),jsonObject.getString("quantity"),jsonObject.getString("dozen"),jsonObject.getString("caton"),jsonObject.getString("product_image"),jsonObject.getString("price"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(context,e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                        ((ProductsActivity)context).productList.add(product);
                    }

                    if(swipeRefreshLayout.isRefreshing()){
                        swipeRefreshLayout.setRefreshing(false);
                    }
                    if(((ProductsActivity)context).productList.size() != 0){
                        ((ProductsActivity)context).productRecyclerAdapter = new ProductsRecyclerAdapter(context,user_id,((ProductsActivity)context).productList);
                        productsRecyclerView.setAdapter(((ProductsActivity)context).productRecyclerAdapter);
                    }else{
                        noProductsLayout.setVisibility(View.VISIBLE);
                    }


                } catch (JSONException em) {
                    swipeRefreshLayout.setRefreshing(false);
                    noProductsLayout.setVisibility(View.VISIBLE);
                    em.printStackTrace();
                    String b_msg = "Temporary error occured, try again later";
                    Snackbar snack = Snackbar.make(rootView.findViewById(R.id.products_fab), b_msg, Snackbar.LENGTH_LONG);
                    View view = snack.getView();
                    FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) view.getLayoutParams();
                    params.gravity = Gravity.TOP;
                    view.setLayoutParams(params);
                    snack.show();
                }
            }
        };

        return localResponseListener;
    }

}

class GetProductsRequest extends StringRequest{
    private Map<String, String> params;

    public GetProductsRequest(String API, String supplier_id, Response.Listener<String> listener, Response.ErrorListener errorListener){
        super(Method.GET, API, listener, errorListener);
        params = new HashMap<>();
        //params.put("supplier_id",supplier_id);
    }


    public Map<String, String> getParams(){
        return params;
    }
}
