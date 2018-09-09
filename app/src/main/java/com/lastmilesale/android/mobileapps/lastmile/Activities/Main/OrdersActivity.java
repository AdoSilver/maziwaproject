package com.lastmilesale.android.mobileapps.lastmile.Activities.Main;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.lastmilesale.android.mobileapps.lastmile.Functions.ConnectionHandler;
import com.lastmilesale.android.mobileapps.lastmile.Objects.Order;
import com.lastmilesale.android.mobileapps.lastmile.R;

import java.util.List;

public class OrdersActivity extends AppCompatActivity {

    ConnectionHandler connectionHandler;
    List<Order> orderList;
    String user_id;
    public RecyclerView.Adapter ordersRecyclerAdapter;
    View rootView;
    Toolbar toolbar;
    ImageView backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        connectionHandler = new ConnectionHandler(OrdersActivity.this);
        rootView = findViewById(R.id.orders_activity_layout);
        backBtn = (ImageView) findViewById(R.id.orders_back_button);
        toolbar = (Toolbar) findViewById(R.id.orders_toolbar);
        user_id = getSharedPreferences(getResources().getString(R.string.app_file),0).getString(getResources().getString(R.string.active_user_id),"null");
        //setting up toolbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        if(connectionHandler.isConnected()){
            new GetOrdersTask(OrdersActivity.this,rootView,user_id).execute();
        }else{
            String msg = "No Internet connection";
            Snackbar snack = Snackbar.make(findViewById(R.id.orders_fab), msg, Snackbar.LENGTH_LONG);
            View view = snack.getView();
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) view.getLayoutParams();
            params.gravity = Gravity.TOP;
            view.setLayoutParams(params);
            snack.show();
        }

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }
}
