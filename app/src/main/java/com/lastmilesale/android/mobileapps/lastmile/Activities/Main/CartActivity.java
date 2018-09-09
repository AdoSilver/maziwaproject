package com.lastmilesale.android.mobileapps.lastmile.Activities.Main;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lastmilesale.android.mobileapps.lastmile.Functions.ConnectionHandler;
import com.lastmilesale.android.mobileapps.lastmile.Objects.Order;
import com.lastmilesale.android.mobileapps.lastmile.R;
import com.onesignal.OSPermissionSubscriptionState;
import com.onesignal.OneSignal;

import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity {

    public ConnectionHandler connectionHandler;
    public String user_id;
    public RecyclerView.Adapter cartRecyclerAdapter;
    public View rootView;
    public Toolbar toolbar;
    public ImageView backBtn,ordersBtn,menuBtn;
    public TextView ordersBadge,noCartRefresh, checkOutAllBtn;
    public int bagdeCount = 0;
    public RelativeLayout noCartLayout;
    public SwipeRefreshLayout swipeRefreshLayout;
    public BottomSheetBehavior bottomSheetBehavior;
    public BottomSheetDialog bottomSheetDialog;
    public TextView orderPrice,orderDetails,productPrice;
    public ProgressBar progressBar;
    public int checkedOutPosition;
    public Button confirmBtn;
    public Order selectedOrder;
    public String player_id;
    public SharedPreferences appFile;
    public SharedPreferences.Editor appFileEditor;
    public boolean isCheckOutAll = false;
    public List<Order> orderList;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        connectionHandler = new ConnectionHandler(CartActivity.this);
        appFile = getSharedPreferences(getResources().getString(R.string.app_file),0);
        appFileEditor = appFile.edit();
        orderList = new ArrayList<>();
        bottomSheetDialog = new BottomSheetDialog(CartActivity.this);
        View confirmCartDialog = getLayoutInflater().inflate(R.layout.cart_bottom_sheet,null);
        bottomSheetDialog.setContentView(confirmCartDialog);
        bottomSheetBehavior = BottomSheetBehavior.from((View)confirmCartDialog.getParent());
        bottomSheetBehavior.setPeekHeight((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,300,getResources().getDisplayMetrics()));

        rootView = findViewById(R.id.cart_activity_layout);
        toolbar = (Toolbar) findViewById(R.id.cart_toolbar);
        backBtn = (ImageView) findViewById(R.id.cart_back_button);
        ordersBadge = (TextView) findViewById(R.id.m_cart_orders_badge);
        checkOutAllBtn = (TextView) findViewById(R.id.checkout_all);
        ordersBtn = (ImageView) findViewById(R.id.cart_orders_btn);
        noCartLayout = (RelativeLayout) findViewById(R.id.no_cart_layout);
        noCartRefresh = (TextView) findViewById(R.id.no_cart_refresh);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.cart_swipe_refresh_layout);

        orderDetails = (TextView) confirmCartDialog.findViewById(R.id.order_details);
        orderPrice = (TextView) confirmCartDialog.findViewById(R.id.order_price);
        progressBar = (ProgressBar) confirmCartDialog.findViewById(R.id.cart_progress_bar);
        confirmBtn = (Button) confirmCartDialog.findViewById(R.id.checkout_btn);
        productPrice = (TextView) confirmCartDialog.findViewById(R.id.product_price);

        //setting up toolbar
        user_id = getSharedPreferences(getResources().getString(R.string.app_file),0).getString(getResources().getString(R.string.active_user_id),"null");
        //Log.d("kisangaaaa",user_id);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        //Collecting player id
        try {
            OSPermissionSubscriptionState status = OneSignal.getPermissionSubscriptionState();
            status.getPermissionStatus().getEnabled();

            status.getSubscriptionStatus().getSubscribed();
            status.getSubscriptionStatus().getUserSubscriptionSetting();
            player_id = status.getSubscriptionStatus().getUserId();
            status.getSubscriptionStatus().getPushToken();
        }catch (NullPointerException ie){ie.printStackTrace();}

        //saving player id
        appFileEditor.putString(getResources().getString(R.string.player_id),player_id).commit();


        //Directions to Orders
        ordersBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CartActivity.this,OrdersActivity.class);
                startActivity(i);
            }
        });

        //Orders Badge
        ordersBadge.setVisibility(View.INVISIBLE);

        //get Initial data
        getData(false);

        //swipe refresh layout implementation
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                getData(true);
            }
        });

        //refresh Layout implementation
        noCartRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                noCartLayout.setVisibility(View.INVISIBLE);
                swipeRefreshLayout.setRefreshing(true);
                getData(true);
            }
        });

        //handling checkout all btn
        checkOutAllBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isCheckOutAll = true;
                bottomSheetDialog.show();
            }
        });

        //Bottom dialog
        bottomSheetDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                if(isCheckOutAll){
                    orderDetails.setText("Total Orders: " + orderList.size()+" Orders");
                    productPrice.setText(getTotalPrice(orderList));
                    try {
                        // int totalPrice = Integer.parseInt(selectedOrder.getQuantity())*Integer.parseInt(selectedOrder.getPrice());
                        orderPrice.setText("Quantity: " + getTotalCartons(orderList) + " Carton(s), " + getTotalDozens(orderList) + " Dozen(s), & " + getPiecesInInt(orderList) + " Piece(s)");
                    } catch (NumberFormatException ie) {
                        ie.printStackTrace();
                    }
                }else {
                    orderDetails.setText("Order " + selectedOrder.getId() + ": " + selectedOrder.getSupplier_name() + ":" + selectedOrder.getProduct_name());
                    productPrice.setText(selectedOrder.getPrice());
                    try {
                        // int totalPrice = Integer.parseInt(selectedOrder.getQuantity())*Integer.parseInt(selectedOrder.getPrice());
                        orderPrice.setText("Quantity: " + selectedOrder.getNoOfCartons() + " Carton(s), " + selectedOrder.getNoOfDozens() + " Dozen(s), & " + selectedOrder.getNoOfPieces() + " Piece(s)");
                    } catch (NumberFormatException ie) {
                        ie.printStackTrace();
                    }
                }
            }
        });

        //Bottom view management
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orderPrice.setVisibility(View.INVISIBLE);
                orderDetails.setVisibility(View.INVISIBLE);
                productPrice.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.VISIBLE);

                if(isCheckOutAll){
                    //checkout all
                    try {
                        int cartCount = Integer.parseInt(appFile.getString(getResources().getString(R.string.user_cart_count), "0"));
                        if (cartCount > 0) {
                            cartCount = 0;
                            appFileEditor.putString(getResources().getString(R.string.user_cart_count), cartCount + "").commit();
                        } else {
                            appFileEditor.putString(getResources().getString(R.string.user_cart_count), "0").commit();
                        }
                    } catch (Exception ie) {
                        ie.printStackTrace();
                    }

                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // Do something after 5s = 5000ms
                            new SwitchAllCartsToOrders(CartActivity.this, getOrdersListInString(orderList), player_id).execute();
//                            for(int i = 0; i < orderList.size(); i++) {
//                                new SwitchCartToOrder(CartActivity.this, orderList.get(i).getId(), player_id).execute();
//                            }
                            orderPrice.setVisibility(View.VISIBLE);
                            orderDetails.setVisibility(View.VISIBLE);
                            productPrice.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.INVISIBLE);
                            bottomSheetDialog.dismiss();
                            bagdeCount = bagdeCount + orderList.size();
                            ordersBadge.setVisibility(View.VISIBLE);
                            ordersBadge.setText(bagdeCount + "");
                            ((CartRecyclerAdapter) cartRecyclerAdapter).removeAllItems();
                        }
                    }, 5000);


                }else {
                    //Checkout one by one
                    try {
                        int cartCount = Integer.parseInt(appFile.getString(getResources().getString(R.string.user_cart_count), "0"));
                        if (cartCount > 0) {
                            cartCount--;
                            appFileEditor.putString(getResources().getString(R.string.user_cart_count), cartCount + "").commit();
                        } else {
                            appFileEditor.putString(getResources().getString(R.string.user_cart_count), "0").commit();
                        }
                    } catch (Exception ie) {
                        ie.printStackTrace();
                    }

                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // Do something after 5s = 5000ms
                            new SwitchCartToOrder(CartActivity.this, selectedOrder.getId(), player_id).execute();
                            orderPrice.setVisibility(View.VISIBLE);
                            orderDetails.setVisibility(View.VISIBLE);
                            productPrice.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.INVISIBLE);
                            bottomSheetDialog.dismiss();
                            bagdeCount++;
                            ordersBadge.setVisibility(View.VISIBLE);
                            ordersBadge.setText(bagdeCount + "");
                            ((CartRecyclerAdapter) cartRecyclerAdapter).removeCheckedOutItem(checkedOutPosition);
                        }
                    }, 3000);
                }

            }
        });


        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public String getTotalPrice(List<Order> orderList){
        int totalPrice = 0;
        for(int i = 0; i < orderList.size(); i++){
            totalPrice = totalPrice + orderList.get(i).getPriceInInt();
        }
        return "Total: Tsh "+totalPrice+"/-";
    }

    public String getTotalDozens(List<Order> orderList){
        int total = 0;
        for(int i = 0; i < orderList.size(); i++){
            total = total + orderList.get(i).getDozensInInt();
        }
        return total+"";
    }

    public String getTotalCartons(List<Order> orderList){
        int total = 0;
        for(int i = 0; i < orderList.size(); i++){
            total = total + orderList.get(i).getCartonsInInt();
        }
        return total+"";
    }

    public String getPiecesInInt(List<Order> orderList){
        int total = 0;
        for(int i = 0; i < orderList.size(); i++){
            total = total + orderList.get(i).getPiecesInInt();
        }
        return total+"";
    }

    public List<String> getOrdersListInString(List<Order> orderList){
        List<String> orders = new ArrayList<>();
        for(int i = 0; i < orderList.size(); i++){
            orders.add(orderList.get(i).getId());
        }
        return orders;
    }

    public void getData(boolean isRefresh){
        if(connectionHandler.isConnected()){
            new GetCartTask(CartActivity.this,rootView,user_id,"cart",isRefresh).execute();

        }else{
            noCartLayout.setVisibility(View.VISIBLE);
            String msg = "No Internet connection";
            Snackbar snack = Snackbar.make(findViewById(R.id.cart_fab), msg, Snackbar.LENGTH_LONG);
            View view = snack.getView();
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) view.getLayoutParams();
            params.gravity = Gravity.TOP;
            view.setLayoutParams(params);
            snack.show();
        }
    }
}
