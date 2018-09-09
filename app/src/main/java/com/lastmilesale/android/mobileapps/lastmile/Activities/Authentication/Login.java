package com.lastmilesale.android.mobileapps.lastmile.Activities.Authentication;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
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

public class Login extends Activity {

    EditText phoneEdit,passwordEdit;
    ImageView visibilityOn,visibilityOff;
    Button loginBtn,inactiveLoginBtn,loadingLoginBtn;
    TextView forgotPassBtn,signupBtn;
    View rootView;
    String phoneNumber, password, player_id;
    ConnectionHandler connectionHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        connectionHandler = new ConnectionHandler(Login.this);

        //Establishing views
        rootView = findViewById(R.id.signin_layout);
        phoneEdit = (EditText) findViewById(R.id.signin_phoneEdit);
        passwordEdit = (EditText) findViewById(R.id.signin_passwordEdit);
        visibilityOn = (ImageView) findViewById(R.id.signin_visibility);
        visibilityOff = (ImageView) findViewById(R.id.signin_visibility_off);
        loginBtn = (Button) findViewById(R.id.signin_btn);
        inactiveLoginBtn = (Button) findViewById(R.id.signin_btn_inactive);
        loadingLoginBtn = (Button) findViewById(R.id.signin_loading_btn);
        forgotPassBtn = (TextView) findViewById(R.id.signin_forgotPasswd_btn);
        signupBtn = (TextView) findViewById(R.id.signin_signup_btn);


        //Collecting player id
        OSPermissionSubscriptionState status = OneSignal.getPermissionSubscriptionState();
        status.getPermissionStatus().getEnabled();

        status.getSubscriptionStatus().getSubscribed();
        status.getSubscriptionStatus().getUserSubscriptionSetting();
        player_id = status.getSubscriptionStatus().getUserId();
        status.getSubscriptionStatus().getPushToken();

        phoneEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if((phoneEdit.getText().toString().length() > 0) && (passwordEdit.getText().toString().length() > 0)){
                    loginBtn.setVisibility(View.VISIBLE);
                    inactiveLoginBtn.setVisibility(View.INVISIBLE);
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
                if((phoneEdit.getText().toString().length() > 0) && (passwordEdit.getText().toString().length() > 0)){
                    loginBtn.setVisibility(View.VISIBLE);
                    inactiveLoginBtn.setVisibility(View.INVISIBLE);
                }
            }
        });

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

        forgotPassBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "http://lastmiletoafrica.com/";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this,SignUp.class);
                startActivity(intent);
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(connectionHandler.isConnected()) {
                    phoneNumber = phoneEdit.getText().toString();
                    password = passwordEdit.getText().toString();
                    loadingLoginBtn.setVisibility(View.VISIBLE);
                    loginBtn.setVisibility(View.INVISIBLE);
                    new LoginService(Login.this,rootView,phoneNumber,password, player_id).execute();
                }else{
                    //if internet is no there, run the following codes
                    String status_msg = "No Internet connection";
                    Snackbar snack = Snackbar.make(findViewById(R.id.signin_fab), status_msg, Snackbar.LENGTH_LONG);
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
