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


public class SignUpService extends AsyncTask<Void,Integer,Integer> {

    private Context context;
    private View rootView;
    private String name,phone,password,shopName,location,tinNumber,player_id;
    private Response.Listener<String> responseListener;
    private Response.ErrorListener errorListener;
    private Button signupBtn,signupLoadingBtn;
    private SharedPreferences appFile;
    private SharedPreferences.Editor appFileEditor;

    public SignUpService(Context context, View rootView, String name,String phone, String password,String shopName, String location,String tinNumber, String player_id){
        this.context = context;
        this.rootView = rootView;
        this.name = name;
        this.phone = phone;
        this.password = password;
        this.shopName = shopName;
        this.location = location;
        this.tinNumber = tinNumber;
        this.player_id = player_id;
        this.signupBtn = (Button) rootView.findViewById(R.id.signup_btn);
        this.signupLoadingBtn = (Button) rootView.findViewById(R.id.signup_loading_btn);
        this.errorListener = initializeErrorListener();
        this.responseListener = initializeResponseListener();
        appFile = context.getSharedPreferences(context.getResources().getString(R.string.app_file),0);
        appFileEditor = appFile.edit();
    }

    @Override
    protected Integer doInBackground(Void... voids) {
        //execute the response listener with volley
        String API = "http://lastmilesale.com/api/register";
        SignUpRequest signUpRequest = new SignUpRequest(API,name,phone,password,shopName,location,tinNumber,player_id,responseListener,errorListener);
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(signUpRequest);
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
        Snackbar snack = Snackbar.make(rootView.findViewById(R.id.signup_fab), b_msg, Snackbar.LENGTH_LONG);
        View view = snack.getView();
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) view.getLayoutParams();
        params.gravity = Gravity.TOP;
        view.setLayoutParams(params);
        snack.show();
        signupBtn.setVisibility(View.VISIBLE);
        signupLoadingBtn.setVisibility(View.INVISIBLE);
    }

    protected Response.ErrorListener initializeErrorListener(){
        Response.ErrorListener localErrorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(error instanceof TimeoutError){
                    signupBtn.setVisibility(View.VISIBLE);
                    signupLoadingBtn.setVisibility(View.INVISIBLE);
                    //SETTING UP MESSAGE
                    String msg = context.getResources().getString(R.string.timeout_error);
                    Snackbar snack = Snackbar.make(rootView.findViewById(R.id.signup_fab), msg, Snackbar.LENGTH_LONG);
                    View view = snack.getView();
                    FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) view.getLayoutParams();
                    params.gravity = Gravity.TOP;
                    view.setLayoutParams(params);
                    snack.show();

                }else if (error instanceof AuthFailureError){
                    signupBtn.setVisibility(View.VISIBLE);
                    signupLoadingBtn.setVisibility(View.INVISIBLE);
                    //SETTING UP MESSAGE
                    String msg = context.getResources().getString(R.string.authentication_error);
                    Snackbar snack = Snackbar.make(rootView.findViewById(R.id.signup_fab), msg, Snackbar.LENGTH_LONG);
                    View view = snack.getView();
                    FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) view.getLayoutParams();
                    params.gravity = Gravity.TOP;
                    view.setLayoutParams(params);
                    snack.show();


                }else if(error instanceof NetworkError){
                    signupBtn.setVisibility(View.VISIBLE);
                    signupLoadingBtn.setVisibility(View.INVISIBLE);
                    //SETTING UP MESSAGE
                    String msg = context.getResources().getString(R.string.network_error);
                    Snackbar snack = Snackbar.make(rootView.findViewById(R.id.signup_fab), msg, Snackbar.LENGTH_LONG);
                    View view = snack.getView();
                    FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) view.getLayoutParams();
                    params.gravity = Gravity.TOP;
                    view.setLayoutParams(params);
                    snack.show();

                }else if(error instanceof ServerError){
                    signupBtn.setVisibility(View.VISIBLE);
                    signupLoadingBtn.setVisibility(View.INVISIBLE);
                    //SETTING UP MESSAGE
                    String msg = context.getResources().getString(R.string.serverbusy_error);
                    Snackbar snack = Snackbar.make(rootView.findViewById(R.id.signup_fab), msg, Snackbar.LENGTH_LONG);
                    View view = snack.getView();
                    FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) view.getLayoutParams();
                    params.gravity = Gravity.TOP;
                    view.setLayoutParams(params);
                    snack.show();

                }else if(error instanceof ParseError){
                    signupBtn.setVisibility(View.VISIBLE);
                    signupLoadingBtn.setVisibility(View.INVISIBLE);
                    //SETTING UP MESSAGE
                    String msg = context.getResources().getString(R.string.parse_error);
                    Snackbar snack = Snackbar.make(rootView.findViewById(R.id.signup_fab), msg, Snackbar.LENGTH_LONG);
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
                    boolean is_valid_phone = false;
                    String user_id = "";
                        try {
                            success = jsonObject.getBoolean("success");
                            is_valid_phone = jsonObject.getBoolean("is_valid_phone_number");
                            user_id = jsonObject.getString("user_id");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (success) {
                            appFileEditor.putString(context.getResources().getString(R.string.active_user_id),user_id).commit();
                            appFileEditor.putString(context.getResources().getString(R.string.user_fullname),name).commit();
                            appFileEditor.putString(context.getResources().getString(R.string.user_shop_name),shopName).commit();
                            appFileEditor.putString(context.getResources().getString(R.string.user_phone_no),phone).commit();
                            Intent main_intent = new Intent(context,MainActivity.class);
                            main_intent.putExtra("user_id",user_id);
                            main_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            context.startActivity(main_intent);
                        } else {
                            //Returning views
                            signupBtn.setVisibility(View.VISIBLE);
                            signupLoadingBtn.setVisibility(View.INVISIBLE);
                            if(!is_valid_phone){
                                String f_msg = "Phone number already used";
                                ((EditText)rootView.findViewById(R.id.signup_phoneEdit)).setTextColor(Color.RED);
                                //removing loading progress bar
                                //setting the message
                                Snackbar snack = Snackbar.make(rootView.findViewById(R.id.signup_fab), f_msg, Snackbar.LENGTH_LONG);
                                View view = snack.getView();
                                FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) view.getLayoutParams();
                                params.gravity = Gravity.TOP;
                                view.setLayoutParams(params);
                                snack.show();
                            }else{
                                String f_msg = "Something went wrong";
                                //setting the message
                                Snackbar snack = Snackbar.make(rootView.findViewById(R.id.signup_fab), f_msg, Snackbar.LENGTH_LONG);
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
                    Snackbar snack = Snackbar.make(rootView.findViewById(R.id.signup_fab), b_msg, Snackbar.LENGTH_LONG);
                    View view = snack.getView();
                    FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) view.getLayoutParams();
                    params.gravity = Gravity.TOP;
                    view.setLayoutParams(params);
                    snack.show();
                    signupBtn.setVisibility(View.VISIBLE);
                    signupLoadingBtn.setVisibility(View.INVISIBLE);
                }

            }
        };

        return localResponseListener;
    }
}

class SignUpRequest extends StringRequest {
    private Map<String, String> params;
    public SignUpRequest(String API,String name,String phone, String password,String shopName, String location,String tinNumber,String player_id, Response.Listener<String> listener, Response.ErrorListener errorListener){
        super(Request.Method.POST,API,listener, errorListener);
        params = new HashMap<>();
        params.put("name",name);
        params.put("phone_number",phone);
        params.put("password",password);
        params.put("name_of_firm",shopName);
        params.put("location",location);
        params.put("tin_number",tinNumber);
        params.put("player_id",player_id);
    }
    public Map<String, String> getParams(){
        return params;
    }
}



