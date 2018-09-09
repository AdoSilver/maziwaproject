package com.lastmilesale.android.mobileapps.lastmile.Activities.Profile;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
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

public class EditProfile extends AppCompatActivity {

    Toolbar toolbar;
    ImageView backBtn,fowardBtn;
    EditText fullNameEdit,emailEdit,phoneNumberEdit,locationEdit,shopNameEdit,tinNumberEdit;
    String fullName,email,phoneNumber,location,shopName,tinNumber;
    SharedPreferences appFile;
    String user_id;
    Response.Listener<String> listener;
    String UPDATE_USER_LINK = "http://lastmilesale.com/api/update/user";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        toolbar = (Toolbar) findViewById(R.id.edit_profile_toolbar);
        backBtn = (ImageView) findViewById(R.id.edit_profile_toolbar_back_btn);
        fowardBtn = (ImageView) findViewById(R.id.edit_profile_toolbar_save_btn);
        fullNameEdit = (EditText) findViewById(R.id.ep_emailEdit);
        emailEdit = (EditText) findViewById(R.id.ep_fullnameEdit);
        phoneNumberEdit = (EditText) findViewById(R.id.ep_genderSpinner);
        locationEdit = (EditText) findViewById(R.id.ep_addressEdit);
        shopNameEdit = (EditText) findViewById(R.id.ep_ageEdit);
        tinNumberEdit = (EditText) findViewById(R.id.ep_pregnancyAgeEdit);
        appFile = getSharedPreferences(getResources().getString(R.string.app_file),0);
        user_id = appFile.getString(getResources().getString(R.string.active_user_id),"");


        //rendering data to views
        getData();


        //setting up toolbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        fowardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fullName = fullNameEdit.getText().toString();
                phoneNumber = phoneNumberEdit.getText().toString();
                location = locationEdit.getText().toString();
                shopName = shopNameEdit.getText().toString();
                tinNumber = tinNumberEdit.getText().toString();
                UpdateUserProfile updateUserProfile = new UpdateUserProfile(UPDATE_USER_LINK,user_id,fullName,phoneNumber,location,shopName,tinNumber,initializeSenderResponseListener());
                RequestQueue queue = Volley.newRequestQueue(EditProfile.this);
                queue.add(updateUserProfile);
            }
        });
    }

    public void getData(){
        String API = "http://lastmilesale.com/api/user/"+user_id;
        listener = initializeReceiverResponseListener();
        GetUserProfile getUserProfile = new GetUserProfile(API,listener);
        RequestQueue queue = Volley.newRequestQueue(EditProfile.this);
        queue.add(getUserProfile);

    }


    protected Response.Listener<String> initializeReceiverResponseListener(){
        Response.Listener<String> localResponseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    fullName = jsonObject.getString("name");
                    phoneNumber = jsonObject.getString("phone_number");
                    location = jsonObject.getString("location");
                    shopName = jsonObject.getString("name_of_firm");
                    tinNumber = jsonObject.getString("tin_number");

                    //rendering data
                    fullNameEdit.setText(fullName);
                    phoneNumberEdit.setText(phoneNumber);
                    locationEdit.setText(location);
                    shopNameEdit.setText(shopName);
                    tinNumberEdit.setText(tinNumber);

                } catch (JSONException em) {}
            }
        };
        return localResponseListener;
    }

    protected Response.Listener<String> initializeSenderResponseListener(){
        Response.Listener<String> localResponseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = false;
                    boolean is_valid_phone = false;
                    String user_id = "";
                    success = jsonObject.getBoolean("success");
                    is_valid_phone = jsonObject.getBoolean("is_valid_phone_number");
                    user_id = jsonObject.getString("user_id");
                    if(success){
                        Toast.makeText(EditProfile.this,"Information saved",Toast.LENGTH_SHORT).show();
                    }else{
                        if(!is_valid_phone){
                            Toast.makeText(EditProfile.this,"Invalid phone number",Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(EditProfile.this,"Error occured",Toast.LENGTH_SHORT).show();
                        }
                    }

                } catch (JSONException em) {
                    Toast.makeText(EditProfile.this,"Server busy",Toast.LENGTH_SHORT).show();
                }
            }
        };
        return localResponseListener;
    }

}


class GetUserProfile extends StringRequest {
    private Map<String, String> params;

    public GetUserProfile(String API, Response.Listener<String> listener){
        super(Method.GET, API, listener, null);
        params = new HashMap<>();
    }


    public Map<String, String> getParams(){
        return params;
    }
}


class UpdateUserProfile extends StringRequest {
    private Map<String, String> params;

    public UpdateUserProfile(String API,String user_id, String fullName,String phoneNo,String location,String shopName, String tinNumber, Response.Listener<String> listener){
        super(Method.POST, API, listener, null);
        params = new HashMap<>();
        params.put("user_id",user_id);
        params.put("name",fullName);
        params.put("phone_number",phoneNo);
        params.put("location",location);
        params.put("name_of_firm",shopName);
        params.put("tin_number",tinNumber);
    }


    public Map<String, String> getParams(){
        return params;
    }
}