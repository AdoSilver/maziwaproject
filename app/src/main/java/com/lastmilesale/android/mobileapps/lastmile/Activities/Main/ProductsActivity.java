package com.lastmilesale.android.mobileapps.lastmile.Activities.Main;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
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
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lastmilesale.android.mobileapps.lastmile.Functions.ConnectionHandler;
import com.lastmilesale.android.mobileapps.lastmile.Objects.Product;
import com.lastmilesale.android.mobileapps.lastmile.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ProductsActivity extends AppCompatActivity {

    public ConnectionHandler connectionHandler;
    public String user_id,supplier_id;
    public RecyclerView.Adapter productRecyclerAdapter;
    public View rootView;
    public Toolbar toolbar;
    public ImageView backBtn,cartBtn,productImage;
    public Product clickedProduct;
    public TextView cartCountBadge;
    public SharedPreferences appFile;
    public SharedPreferences.Editor appFileEditor;
    public int cartCount;
    public SwipeRefreshLayout swipeRefreshLayout;
    public RelativeLayout noProductsLayout;
    public TextView noProductsRefreshView,productName,productPrice,productDetails;
    public List<Product> productList;
    public BottomSheetBehavior bottomSheetBehavior;
    public BottomSheetDialog bottomSheetDialog;
    public EditText piecesEdit,dozensEdit,cartonsEdit;
    public Button addToCartBtn;
    public String noOfPieces = "0",noOfDozens = "0",noOfCartons="0";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);

        connectionHandler = new ConnectionHandler(ProductsActivity.this);
        rootView = findViewById(R.id.products_activity_layout);
        toolbar = (Toolbar) findViewById(R.id.products_toolbar);
        backBtn = (ImageView) findViewById(R.id.products_toolbar_back_btn);
        cartBtn = (ImageView) findViewById(R.id.products_cart_btn);
        cartCountBadge = (TextView) findViewById(R.id.m_sc_badge);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.products_swipe_refresh_layout);
        noProductsLayout = (RelativeLayout) findViewById(R.id.no_products_layout);
        noProductsRefreshView = (TextView) findViewById(R.id.no_products_refresh);
        user_id = getIntent().getExtras().getString("user_id");
        supplier_id = getIntent().getExtras().getString("supplier_id");
        this.appFile = getSharedPreferences(getResources().getString(R.string.app_file),0);
        this.appFileEditor = appFile.edit();
        productList = new ArrayList<>();

        //Bottom sheet codes
        bottomSheetDialog = new BottomSheetDialog(ProductsActivity.this);
        View cartDialog = getLayoutInflater().inflate(R.layout.products_bottom_sheet,null);
        bottomSheetDialog.setContentView(cartDialog);
        bottomSheetBehavior = BottomSheetBehavior.from((View)cartDialog.getParent());
        bottomSheetBehavior.setPeekHeight((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,300,getResources().getDisplayMetrics()));
        piecesEdit = (EditText) cartDialog.findViewById(R.id.no_of_pieces);
        dozensEdit = (EditText) cartDialog.findViewById(R.id.no_of_dozens);
        cartonsEdit = (EditText) cartDialog.findViewById(R.id.no_of_cartons);
        addToCartBtn = (Button) cartDialog.findViewById(R.id.add_to_cart_btn);
        productImage = (ImageView) cartDialog.findViewById(R.id.product_image);
        productName = (TextView) cartDialog.findViewById(R.id.product_name);
        productDetails = (TextView) cartDialog.findViewById(R.id.product_content);
        productPrice = (TextView)cartDialog.findViewById(R.id.product_price);




        //setting up toolbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        //Handling cart and it's badge
        cartCount = Integer.parseInt(appFile.getString(getResources().getString(R.string.user_cart_count),"0"));
        if(cartCount == 0){
            cartCountBadge.setVisibility(View.INVISIBLE);
        }else{
            cartCountBadge.setVisibility(View.VISIBLE);
            if(cartCount < 100){
                cartCountBadge.setText(Integer.toString(cartCount));
            }else{
                cartCountBadge.setText("99+");
            }
        }
        cartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ci = new Intent(ProductsActivity.this,CartActivity.class);
                startActivity(ci);
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                new GetProductsTask(ProductsActivity.this,rootView,user_id,supplier_id,true).execute();
            }
        });

        noProductsRefreshView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                swipeRefreshLayout.setRefreshing(true);
                noProductsLayout.setVisibility(View.INVISIBLE);
                new GetProductsTask(ProductsActivity.this,rootView,user_id,supplier_id,true).execute();
            }
        });


        addToCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(piecesEdit.getText().toString().length() != 0) {
                    noOfPieces = piecesEdit.getText().toString();
                }
                if(dozensEdit.getText().toString().length() != 0) {
                    noOfDozens = dozensEdit.getText().toString();
                }
                if(cartonsEdit.getText().toString().length() != 0) {
                    noOfCartons = cartonsEdit.getText().toString();
                }
                new AddProductToCart(ProductsActivity.this,clickedProduct.getId(),noOfPieces,noOfDozens,noOfCartons,"cart").execute();
                bottomSheetDialog.dismiss();

//                cartCount ++;
//                cartCountBadge.setVisibility(View.VISIBLE);
//                cartCountBadge.setText(cartCount+"");
//                appFileEditor.putString(getResources().getString(R.string.user_cart_count),cartCount+"").commit();
            }
        });

        bottomSheetDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                productName.setText(clickedProduct.getName());
                productPrice.setText(clickedProduct.getPrice());
                productDetails.setText(clickedProduct.getQuantity());
                Picasso.with(ProductsActivity.this).load(clickedProduct.getProduct_image())
                        .config(Bitmap.Config.ARGB_8888)
                        .error(R.drawable.lastmile_logo_vector)
                        .into(productImage);
            }
        });


        if(connectionHandler.isConnected()){
            new GetProductsTask(ProductsActivity.this,rootView,user_id,supplier_id,false).execute();
        }else{
            noProductsLayout.setVisibility(View.VISIBLE);
            String msg = "No Internet connection";
            Snackbar snack = Snackbar.make(findViewById(R.id.products_fab), msg, Snackbar.LENGTH_LONG);
            View view = snack.getView();
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) view.getLayoutParams();
            params.gravity = Gravity.TOP;
            view.setLayoutParams(params);
            snack.show();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        //Handling cart shortcut
        cartCount = Integer.parseInt(appFile.getString(getResources().getString(R.string.user_cart_count),"0"));
        if(cartCount == 0){
            cartCountBadge.setVisibility(View.INVISIBLE);
        }else{
            cartCountBadge.setVisibility(View.VISIBLE);
            if(cartCount < 100){
                cartCountBadge.setText(Integer.toString(cartCount));
            }else{
                cartCountBadge.setText("99+");
            }
        }
    }
}
