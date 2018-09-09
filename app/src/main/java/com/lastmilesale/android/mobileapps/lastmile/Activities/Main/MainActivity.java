package com.lastmilesale.android.mobileapps.lastmile.Activities.Main;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.ParseError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.lastmilesale.android.mobileapps.lastmile.Activities.Authentication.Login;
import com.lastmilesale.android.mobileapps.lastmile.Activities.Profile.EditProfile;
import com.lastmilesale.android.mobileapps.lastmile.Functions.ConnectionHandler;
import com.lastmilesale.android.mobileapps.lastmile.Interfaces.ItemClickListener;
import com.lastmilesale.android.mobileapps.lastmile.Objects.Supplier;
import com.lastmilesale.android.mobileapps.lastmile.R;
import com.lastmilesale.android.mobileapps.lastmile.Services.CartItemsCountTask;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    RecyclerView suppliersRecyclerView;
    RecyclerView.LayoutManager recyclerViewlayoutManager;
    RecyclerView.Adapter suppliersViewAdapter;
    Toolbar toolbar;
    ConnectionHandler connectionHandler;
    String  GET_JSON_DATA_HTTP_URL;
    JsonArrayRequest jsonArrayRequest;
    RequestQueue requestQueue;
    List<Supplier> supplierList;
    String user_id;
    SwipeRefreshLayout swipeRefreshLayout;
    DrawerLayout mainDrawerLayout;
    View navigation_header;
    ImageView menuBtn, cartBtn;
    RelativeLayout noSuppliersLayout;
    TextView noSuppliersRefreshView;//,notificationBadge;
    Response.Listener<String> responseListener;
    Response.ErrorListener errorListener;
    TextView fullNameView,shopNameView,cartBadge;
    SharedPreferences appFile;
    SharedPreferences.Editor appFileEditor;
    Button logOutBtn;
    int cartCount = 0;


    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        connectionHandler = new ConnectionHandler(MainActivity.this);
        appFile = getSharedPreferences(getResources().getString(R.string.app_file),0);
        appFileEditor = appFile.edit();
        supplierList = new ArrayList<>();
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.suppliers_swipe_refresh_layout);
        mainDrawerLayout = (DrawerLayout) findViewById(R.id.main_drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.main_navigation_view);
        drawerLayout = (DrawerLayout) findViewById(R.id.main_drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.main_navigation_view);
        menuBtn = (ImageView) findViewById(R.id.main_drawer_menu_btn);
        noSuppliersLayout = (RelativeLayout) findViewById(R.id.no_supplier_layout);
        noSuppliersRefreshView = (TextView) findViewById(R.id.no_suppliers_refresh);
        navigation_header = navigationView.getHeaderView(0);
        logOutBtn = (Button) findViewById(R.id.nav_logout_btn);
        fullNameView = (TextView) navigation_header.findViewById(R.id.nav_username);
        shopNameView = (TextView) navigation_header.findViewById(R.id.nav_fullname);
        cartBtn = (ImageView) findViewById(R.id.products_cart_btn);
        cartBadge = (TextView) findViewById(R.id.m_sc_badge);
        actionBarDrawerToggle = new ActionBarDrawerToggle(MainActivity.this, drawerLayout,R.string.main_open, R.string.main_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();


        user_id = appFile.getString(getResources().getString(R.string.active_user_id),"");

        fullNameView.setText(appFile.getString(getResources().getString(R.string.user_fullname),""));
        shopNameView.setText(appFile.getString(getResources().getString(R.string.user_shop_name),""));

        //Getting updates about cart count
        new CartItemsCountTask(MainActivity.this).execute();

        //initializing response listeners
        responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    JSON_PARSE_DATA_AFTER_WEBCALL(jsonArray);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        //Handling cart shortcut
        cartCount = Integer.parseInt(appFile.getString(getResources().getString(R.string.user_cart_count),"0"));
        if(cartCount == 0){
            cartBadge.setVisibility(View.INVISIBLE);
        }else{
            cartBadge.setVisibility(View.VISIBLE);
            if(cartCount < 100){
                cartBadge.setText(Integer.toString(cartCount));
            }else{
                cartBadge.setText("99+");
            }
        }
        cartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,CartActivity.class);
                startActivity(i);
            }
        });

        //Setting up logging out method
        logOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appFileEditor.clear().commit();
                Intent i = new Intent(MainActivity.this, Login.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
            }
        });

        errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                noSuppliersLayout.setVisibility(View.VISIBLE);
                if(error instanceof TimeoutError){
                    //SETTING UP MESSAGE
                    String msg = getResources().getString(R.string.timeout_error);
                    Snackbar snack = Snackbar.make(findViewById(R.id.main_fab), msg, Snackbar.LENGTH_LONG);
                    View view = snack.getView();
                    CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) view.getLayoutParams();
                    params.gravity = Gravity.TOP;
                    view.setLayoutParams(params);
                    snack.show();

                }else if (error instanceof AuthFailureError){
                    //SETTING UP MESSAGE
                    String msg = getResources().getString(R.string.authentication_error);
                    Snackbar snack = Snackbar.make(findViewById(R.id.main_fab), msg, Snackbar.LENGTH_LONG);
                    View view = snack.getView();
                    CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) view.getLayoutParams();
                    params.gravity = Gravity.TOP;
                    view.setLayoutParams(params);
                    snack.show();

                }else if(error instanceof NetworkError){
                    //SETTING UP MESSAGE
                    String msg = getResources().getString(R.string.network_error);
                    Snackbar snack = Snackbar.make(findViewById(R.id.main_fab), msg, Snackbar.LENGTH_LONG);
                    View view = snack.getView();
                    CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) view.getLayoutParams();
                    params.gravity = Gravity.TOP;
                    view.setLayoutParams(params);
                    snack.show();

                }else if(error instanceof ServerError){
                    //SETTING UP MESSAGE
                    String msg = getResources().getString(R.string.serverbusy_error);
                    Snackbar snack = Snackbar.make(findViewById(R.id.main_fab), msg, Snackbar.LENGTH_LONG);
                    View view = snack.getView();
                    CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) view.getLayoutParams();
                    params.gravity = Gravity.TOP;
                    view.setLayoutParams(params);
                    snack.show();

                }else if(error instanceof ParseError){
                    //SETTING UP MESSAGE
                    String msg = getResources().getString(R.string.parse_error);
                    Snackbar snack = Snackbar.make(findViewById(R.id.main_fab), msg, Snackbar.LENGTH_LONG);
                    View view = snack.getView();
                    CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) view.getLayoutParams();
                    params.gravity = Gravity.TOP;
                    view.setLayoutParams(params);
                    snack.show();
                }
            }
        };

        menuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        if (navigationView != null) {
            setupDrawerContent(navigationView);
        }

        //Getting views
        toolbar = (Toolbar) findViewById(R.id.suppliers_toolbar);

        //setting up toolbar
        setSupportActionBar(toolbar);
        try {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        } catch (NullPointerException ie) {
            ie.printStackTrace();
        }

        suppliersRecyclerView = (RecyclerView) findViewById(R.id.main_suppliers_recycler_view);
        suppliersRecyclerView.setHasFixedSize(true);
        recyclerViewlayoutManager = new LinearLayoutManager(this);
        suppliersRecyclerView.setLayoutManager(recyclerViewlayoutManager);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                supplierList.clear();
                getData();
                swipeRefreshLayout.setRefreshing(false);
            }
        });


        noSuppliersRefreshView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                noSuppliersLayout.setVisibility(View.INVISIBLE);
                supplierList.clear();
                getData();
            }
        });

        getData();

    }


    private void getData(){
        if (connectionHandler.isConnected()) {
            new getSuppliers().execute();
        } else {
            //Show no network error
            noSuppliersLayout.setVisibility(View.VISIBLE);
            Toast.makeText(MainActivity.this,"No internet",Toast.LENGTH_SHORT).show();
        }
    }


   private class getSuppliers extends AsyncTask<Void, Integer, Integer> {
        @Override
        protected Integer doInBackground(Void... args0) {
            for (int i = 0; i <= 80; i++) {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException ie) {
                    ie.printStackTrace();
                }
            }
            showAllSuppliers();
            return null;
        }


        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            swipeRefreshLayout.setRefreshing(false);

        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            swipeRefreshLayout.setRefreshing(true);
        }
    }

    private void showAllSuppliers() {
        GET_JSON_DATA_HTTP_URL = "http://lastmilesale.com/api/suppliers";
        SuppliersRequest suppliersRequest = new SuppliersRequest(GET_JSON_DATA_HTTP_URL,responseListener,errorListener);
        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(suppliersRequest);
    }

    private void JSON_PARSE_DATA_AFTER_WEBCALL(JSONArray response) {
        for (int i = 0; i < response.length(); i++) {
            Supplier supplier;
            JSONObject jsonObject = null;
            try {
                jsonObject = response.getJSONObject(i);
                supplier = new Supplier(jsonObject.getString("id"), jsonObject.getString("name"), jsonObject.getString("profile_image"),jsonObject.getString("status"));
                supplierList.add(supplier);
            } catch (JSONException e) {
                e.printStackTrace();
                //CATCHING_DATA_PARSE_ERROR
                String status_msg = getResources().getString(R.string.parse_error);
                Snackbar.make(findViewById(R.id.main_fab), status_msg, Snackbar.LENGTH_LONG).show();
            }
        }
        if(supplierList.size() != 0) {
            suppliersViewAdapter = new SuppliersRecyclerViewAdapter(MainActivity.this, supplierList);
            suppliersRecyclerView.setAdapter(suppliersViewAdapter);
        }else{
            noSuppliersLayout.setVisibility(View.VISIBLE);
        }
    }


    class SuppliersRecyclerViewAdapter extends RecyclerView.Adapter<SuppliersRecyclerViewAdapter.ViewHolder> {
        private Context context;
        private List<Supplier> supplierList;

        public SuppliersRecyclerViewAdapter(Context context, List<Supplier> supplierList){
            super();
            this.supplierList = supplierList;
            this.context = context;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.supplier_row_layout, parent, false);
            ViewHolder viewHolder = new ViewHolder(v);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {

            final  Supplier supplier =  supplierList.get(position);

            holder.name.setText(supplier.getName());

           holder.status.setText(supplier.getStatus());

            Picasso.with(context.getApplicationContext()).load(supplier.getLogo())
                    .config(Bitmap.Config.ARGB_8888)
                    .error(R.drawable.lastmile_logo_vector)
                    .into(holder.logoPhoto);

            holder.viewButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //SOME CODES GO HERE
                    Intent intent = new Intent(MainActivity.this,ProductsActivity.class);
                    intent.putExtra("user_id",user_id);
                    intent.putExtra("supplier_id",supplier.getId());
                    intent.putExtra("supplier_name",supplier.getName());
                    startActivity(intent);
                }
            });

            //Section on-click handler
            holder.setItemClickListener(new ItemClickListener() {
                @Override
                public void onItemClick(View v, int pos) {
                    holder.viewButton.performClick();
                }
            });
        }

        @Override
        public int getItemCount() {
            return supplierList.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
            TextView name,status;
            ImageView logoPhoto;
            Button viewButton;
            private ItemClickListener itemClickListener;
            public ViewHolder(View itemView) {
                super(itemView);
                name = (TextView) itemView.findViewById(R.id.supplier_name);
                status = (TextView) itemView.findViewById(R.id.suppliers_details);
                logoPhoto = (ImageView) itemView.findViewById(R.id.supplier_image);
                viewButton = (Button) itemView.findViewById(R.id.supplier_view_btn);
                itemView.setOnClickListener(this);
            }
            @Override
            public void onClick(View v) {
                this.itemClickListener.onItemClick(v,getLayoutPosition());
            }
            public void setItemClickListener(ItemClickListener ic){
                this.itemClickListener = ic;
            }
        }
    }


    private void setupDrawerContent(final NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                int id = item.getItemId();
                switch (id) {
                    case R.id.nav_cart: {
                        Intent au_intent = new Intent(MainActivity.this, CartActivity.class);
                        startActivity(au_intent);
                        drawerLayout.closeDrawer(Gravity.LEFT);
                        break;
                    }
                    case R.id.nav_orders: {
                        Intent t_intent = new Intent(MainActivity.this, OrdersActivity.class);
                        startActivity(t_intent);
                        drawerLayout.closeDrawer(Gravity.LEFT);
                        break;
                    }
                    case R.id.nav_profile: {
                        Intent pI = new Intent(MainActivity.this, EditProfile.class);
                        startActivity(pI);
                        drawerLayout.closeDrawer(Gravity.LEFT);
                        break;
                    }

                    case R.id.nav_about_us:
                        String url = "http://lastmiletoafrica.com/about-us";
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url));
                        startActivity(i);
                        break;

                    case R.id.nav_terms:
                        String turl = "http://lastmiletoafrica.com/terms-and-conditions";
                        Intent ti = new Intent(Intent.ACTION_VIEW);
                        ti.setData(Uri.parse(turl));
                        startActivity(ti);
                        break;

                    case R.id.nav_privacy:
                        String purl = "http://lastmiletoafrica.com/privacy-policy";
                        Intent pi = new Intent(Intent.ACTION_VIEW);
                        pi.setData(Uri.parse(purl));
                        startActivity(pi);
                        break;

                }
                return false;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    class SuppliersRequest extends StringRequest {

        private Map<String, String> params;

        public SuppliersRequest(String API, Response.Listener<String> listener,Response.ErrorListener errorListener){
            super(Method.GET,API,listener, errorListener);
            params = new HashMap<>();
        }

        public Map<String, String> getParams(){
            return params;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Handling cart shortcut
        cartCount = Integer.parseInt(appFile.getString(getResources().getString(R.string.user_cart_count),"0"));
        if(cartCount == 0){
            cartBadge.setVisibility(View.INVISIBLE);
        }else{
            cartBadge.setVisibility(View.VISIBLE);
            if(cartCount < 100){
                cartBadge.setText(Integer.toString(cartCount));
            }else{
                cartBadge.setText("99+");
            }
        }
    }






}
