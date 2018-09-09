package com.lastmilesale.android.mobileapps.lastmile.Activities.Authentication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.lastmilesale.android.mobileapps.lastmile.Activities.Main.MainActivity;
import com.lastmilesale.android.mobileapps.lastmile.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginService extends AsyncTask<Void,Integer,Integer> {

    private Context context;
    private View rootView;
    private String phone,password, player_id;
    private Response.Listener<String> responseListener;
    private Response.ErrorListener errorListener;
    private Button signinBtn,signinLoadingBtn;
    private SharedPreferences appFile;
    private SharedPreferences.Editor appFileEditor;

    public LoginService(Context context, View rootView, String phone, String password, String player_id){
        this.context = context;
        this.rootView = rootView;
        this.phone = phone;
        this.password = password;
        this.player_id = player_id;
        signinBtn = (Button) rootView.findViewById(R.id.signin_btn);
        signinLoadingBtn = (Button) rootView.findViewById(R.id.signin_loading_btn);
        this.errorListener = initializeErrorListener();
        this.responseListener = initializeResponseListener();
        appFile = context.getSharedPreferences(context.getResources().getString(R.string.app_file),0);
        appFileEditor = appFile.edit();
    }

    @Override
    protected Integer doInBackground(Void... voids) {
        //execute the response listener with volley
        String API = "http://lastmilesale.com/api/login";
        LoginRequest loginRequest = new LoginRequest(API,phone,password,player_id,responseListener,errorListener);
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(loginRequest);
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
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        String b_msg = "Request cancelled";
        Snackbar snack = Snackbar.make(rootView.findViewById(R.id.signin_fab), b_msg, Snackbar.LENGTH_LONG);
        View view = snack.getView();
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) view.getLayoutParams();
        params.gravity = Gravity.TOP;
        view.setLayoutParams(params);
        snack.show();
        signinBtn.setVisibility(View.VISIBLE);
        signinLoadingBtn.setVisibility(View.INVISIBLE);
    }

    protected Response.ErrorListener initializeErrorListener(){
        Response.ErrorListener localErrorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(error instanceof TimeoutError){
                    signinBtn.setVisibility(View.VISIBLE);
                    signinLoadingBtn.setVisibility(View.INVISIBLE);
                    //SETTING UP MESSAGE
                    String msg = context.getResources().getString(R.string.timeout_error);
                    Snackbar snack = Snackbar.make(rootView.findViewById(R.id.signin_fab), msg, Snackbar.LENGTH_LONG);
                    View view = snack.getView();
                    FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) view.getLayoutParams();
                    params.gravity = Gravity.TOP;
                    view.setLayoutParams(params);
                    snack.show();

                }else if (error instanceof AuthFailureError){
                    signinBtn.setVisibility(View.VISIBLE);
                    signinLoadingBtn.setVisibility(View.INVISIBLE);
                    //SETTING UP MESSAGE
                    String msg = context.getResources().getString(R.string.authentication_error);
                    Snackbar snack = Snackbar.make(rootView.findViewById(R.id.signin_fab), msg, Snackbar.LENGTH_LONG);
                    View view = snack.getView();
                    FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) view.getLayoutParams();
                    params.gravity = Gravity.TOP;
                    view.setLayoutParams(params);
                    snack.show();


                }else if(error instanceof NetworkError){
                    signinBtn.setVisibility(View.VISIBLE);
                    signinLoadingBtn.setVisibility(View.INVISIBLE);
                    //SETTING UP MESSAGE
                    String msg = context.getResources().getString(R.string.network_error);
                    Snackbar snack = Snackbar.make(rootView.findViewById(R.id.signin_fab), msg, Snackbar.LENGTH_LONG);
                    View view = snack.getView();
                    FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) view.getLayoutParams();
                    params.gravity = Gravity.TOP;
                    view.setLayoutParams(params);
                    snack.show();

                }else if(error instanceof ServerError){
                    signinBtn.setVisibility(View.VISIBLE);
                    signinLoadingBtn.setVisibility(View.INVISIBLE);
                    //SETTING UP MESSAGE
                    String msg = context.getResources().getString(R.string.serverbusy_error);
                    Snackbar snack = Snackbar.make(rootView.findViewById(R.id.signin_fab), msg, Snackbar.LENGTH_LONG);
                    View view = snack.getView();
                    FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) view.getLayoutParams();
                    params.gravity = Gravity.TOP;
                    view.setLayoutParams(params);
                    snack.show();

                }else if(error instanceof ParseError){
                    signinBtn.setVisibility(View.VISIBLE);
                    signinLoadingBtn.setVisibility(View.INVISIBLE);
                    //SETTING UP MESSAGE
                    String msg = context.getResources().getString(R.string.parse_error);
                    Snackbar snack = Snackbar.make(rootView.findViewById(R.id.signin_fab), msg, Snackbar.LENGTH_LONG);
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
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = false;
                    String shopName = "",fullName="";
                    boolean is_valid_password = false,is_valid_phone = false;
                    String user_id = "";
                        try {
                            success = jsonObject.getBoolean("success");
                            shopName = jsonObject.getString("shop_name");
                            fullName = jsonObject.getString("full_name");
                            is_valid_password = jsonObject.getBoolean("is_valid_password");
                            is_valid_phone = jsonObject.getBoolean("is_valid_phone");
                            user_id = jsonObject.getString("user_id");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (success) {
                            appFileEditor.putString(context.getResources().getString(R.string.user_phone_no),phone).commit();
                            appFileEditor.putString(context.getResources().getString(R.string.active_user_id),user_id).commit();
                            appFileEditor.putString(context.getResources().getString(R.string.user_fullname),fullName).commit();
                            appFileEditor.putString(context.getResources().getString(R.string.user_shop_name),shopName).commit();
                            Intent main_intent = new Intent(context,MainActivity.class);
                            main_intent.putExtra("user_id",user_id);
                            main_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            context.startActivity(main_intent);
                        } else {
                            //Returning views
                            signinBtn.setVisibility(View.VISIBLE);
                            signinLoadingBtn.setVisibility(View.INVISIBLE);
                            if(!is_valid_phone){
                                String f_msg = "Invalid phone number";
                                ((EditText)rootView.findViewById(R.id.signin_phoneEdit)).setTextColor(Color.RED);
                                //removing loading progress bar
                                //setting the message
                                Snackbar snack = Snackbar.make(rootView.findViewById(R.id.signin_fab), f_msg, Snackbar.LENGTH_LONG);
                                View view = snack.getView();
                                FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) view.getLayoutParams();
                                params.gravity = Gravity.TOP;
                                view.setLayoutParams(params);
                                snack.show();
                            }else if(!is_valid_password){
                                String f_msg = "Invalid password";
                                ((EditText)rootView.findViewById(R.id.signin_phoneEdit)).setTextColor(Color.RED);
                                //removing loading progress bar
                                //setting the message
                                Snackbar snack = Snackbar.make(rootView.findViewById(R.id.signin_fab), f_msg, Snackbar.LENGTH_LONG);
                                View view = snack.getView();
                                FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) view.getLayoutParams();
                                params.gravity = Gravity.TOP;
                                view.setLayoutParams(params);
                                snack.show();
                            }
                        }

                } catch (JSONException em) {
                    em.printStackTrace();
                    String b_msg = "Temporary server failure";
                    Snackbar snack = Snackbar.make(rootView.findViewById(R.id.signin_fab), b_msg, Snackbar.LENGTH_LONG);
                    View view = snack.getView();
                    FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) view.getLayoutParams();
                    params.gravity = Gravity.TOP;
                    view.setLayoutParams(params);
                    snack.show();

                    signinBtn.setVisibility(View.VISIBLE);
                    signinLoadingBtn.setVisibility(View.INVISIBLE);
                }

            }
        };

        return localResponseListener;
    }
}

class LoginRequest extends StringRequest {
    private Map<String, String> params;
    public LoginRequest(String API,String phone, String password,String player_id, Response.Listener<String> listener, Response.ErrorListener errorListener){
        super(Request.Method.POST,API,listener, errorListener);
        params = new HashMap<>();
        params.put("phone_number",phone);
        params.put("password",password);
        params.put("player_id",player_id);
    }
    public Map<String, String> getParams(){
        return params;
    }
}

