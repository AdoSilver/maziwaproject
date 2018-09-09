package com.lastmilesale.android.mobileapps.lastmile.Activities.Authentication;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.lastmilesale.android.mobileapps.lastmile.Functions.ConnectionHandler;
import com.lastmilesale.android.mobileapps.lastmile.R;
import com.onesignal.OSPermissionSubscriptionState;
import com.onesignal.OneSignal;

public class SignUp extends Activity {

    String name,phone,password,shopName,location,tinNumber,player_id;
    EditText nameEdit,phoneEdit,passwordEdit,shopNameEdit,tinNumberEdit,locationEdit;
    ImageView visibilityOn,visibilityOff;
    Button signUpBtn,signUpLoading,inactiveSignUpBtn;
    TextView loginBtn;
    View rootView;
    ConnectionHandler connectionHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        connectionHandler = new ConnectionHandler(SignUp.this);

        //Establishing views
        rootView = findViewById(R.id.signup_layout);
        loginBtn = (TextView) findViewById(R.id.signup_signin_btn);
        signUpBtn = (Button) findViewById(R.id.signup_btn);
        signUpLoading = (Button) findViewById(R.id.signup_loading_btn);
        inactiveSignUpBtn = (Button) findViewById(R.id.signup_btn_inactive);
        visibilityOff = (ImageView) findViewById(R.id.signup_visibility_off);
        visibilityOn = (ImageView) findViewById(R.id.signup_visibility);
        nameEdit = (EditText) findViewById(R.id.signup_nameEdit);
        phoneEdit = (EditText) findViewById(R.id.signup_phoneEdit);
        passwordEdit = (EditText) findViewById(R.id.signup_passwordEdit);
        shopNameEdit = (EditText) findViewById(R.id.signup_shop_name);
        tinNumberEdit = (EditText) findViewById(R.id.signup_tin);
        locationEdit = (EditText) findViewById(R.id.signup_location);

        //Collecting player id
        OSPermissionSubscriptionState status = OneSignal.getPermissionSubscriptionState();
        status.getPermissionStatus().getEnabled();

        status.getSubscriptionStatus().getSubscribed();
        status.getSubscriptionStatus().getUserSubscriptionSetting();
        player_id = status.getSubscriptionStatus().getUserId();
        status.getSubscriptionStatus().getPushToken();

        visibilityOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                visibilityOn.setVisibility(View.INVISIBLE);
                visibilityOff.setVisibility(View.VISIBLE);
                passwordEdit.setInputType(InputType.TYPE_CLASS_TEXT);
                passwordEdit.setSelection(passwordEdit.getText().length());
            }
        });

        visibilityOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                visibilityOn.setVisibility(View.VISIBLE);
                visibilityOff.setVisibility(View.INVISIBLE);
                passwordEdit.setInputType(InputType.TYPE_CLASS_TEXT |
                        InputType.TYPE_TEXT_VARIATION_PASSWORD);
                passwordEdit.setSelection(passwordEdit.getText().length());
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        nameEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if((nameEdit.getText().toString().length() > 0) && (phoneEdit.getText().toString().length() > 0) && (passwordEdit.getText().toString().length() > 0) && (shopNameEdit.getText().toString().length() > 0)){
                    signUpBtn.setVisibility(View.VISIBLE);
                    inactiveSignUpBtn.setVisibility(View.INVISIBLE);
                }
            }
        });

        phoneEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if((nameEdit.getText().toString().length() > 0) && (phoneEdit.getText().toString().length() > 0) && (passwordEdit.getText().toString().length() > 0) && (shopNameEdit.getText().toString().length() > 0)){
                    signUpBtn.setVisibility(View.VISIBLE);
                    inactiveSignUpBtn.setVisibility(View.INVISIBLE);
                }
            }
        });

        passwordEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if((nameEdit.getText().toString().length() > 0) && (phoneEdit.getText().toString().length() > 0) && (passwordEdit.getText().toString().length() > 0) && (shopNameEdit.getText().toString().length() > 0)){
                    signUpBtn.setVisibility(View.VISIBLE);
                    inactiveSignUpBtn.setVisibility(View.INVISIBLE);
                }
            }
        });

        shopNameEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if((nameEdit.getText().toString().length() > 0) && (phoneEdit.getText().toString().length() > 0) && (passwordEdit.getText().toString().length() > 0) && (shopNameEdit.getText().toString().length() > 0)){
                    signUpBtn.setVisibility(View.VISIBLE);
                    inactiveSignUpBtn.setVisibility(View.INVISIBLE);
                }
            }
        });

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(connectionHandler.isConnected()) {
                    name = nameEdit.getText().toString();
                    phone = phoneEdit.getText().toString();
                    password = passwordEdit.getText().toString();
                    shopName = shopNameEdit.getText().toString();
                    location = locationEdit.getText().toString();
                    tinNumber = tinNumberEdit.getText().toString();
                    signUpLoading.setVisibility(View.VISIBLE);
                    signUpBtn.setVisibility(View.INVISIBLE);
                    new SignUpService(SignUp.this,rootView,name,phone,password,shopName,location,tinNumber,player_id).execute();
                }else{
                    //if internet is no there, run the following codes
                    String status_msg = "No Internet connection";
                    Snackbar snack = Snackbar.make(findViewById(R.id.signup_fab), status_msg, Snackbar.LENGTH_LONG);
                    View gview = snack.getView();
                    FrameLayout.LayoutParams params =(FrameLayout.LayoutParams)gview.getLayoutParams();
                    params.gravity = Gravity.TOP;
                    gview.setLayoutParams(params);
                    snack.show();
                }
            }
        });

    }
}
