package com.example.quang.chatwithstranger;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.example.quang.chatwithstranger.consts.Constants;
import com.example.quang.chatwithstranger.model.Prefs;
import com.example.quang.chatwithstranger.model.User;
import com.example.quang.chatwithstranger.singleton.Singleton;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.github.ybq.android.spinkit.SpinKitView;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

//    Socket mSocket;
    Emitter.Listener onResultLogin;{

//        try {
//            mSocket = IO.socket(Constants.PORT);
//        } catch (URISyntaxException e) {
//            e.printStackTrace();
//        }

        onResultLogin = new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                resultLogin(args[0]);
            }
        };
    }

    private EditText edtUsername;
    private EditText edtPassword;
    private Button btnLogin;
    private Button btnRegister;
    private SpinKitView loading;
    private CheckBox cbRememberPass;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        findID();
        loadData();
        initSockets();
        initViews();
    }

    private void findID() {
        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);
        loading = findViewById(R.id.loading_login);
        cbRememberPass = findViewById(R.id.checkBoxRememberPass);
    }

    private void loadData(){
        SharedPreferences pre=getSharedPreferences("chatwithstranger", MODE_PRIVATE);
        edtUsername.setText(pre.getString("user",""));
        edtPassword.setText(pre.getString("pass",""));
        if (edtUsername.getText().toString().isEmpty()){
            cbRememberPass.setChecked(false);
        }else {
            cbRememberPass.setChecked(true);
        }
    }

    private void initSockets() {
        //Singleton.Instance().setmSocket(mSocket);
        Singleton.Instance().getmSocket().connect();
        Singleton.Instance().getmSocket().on(Constants.SERVER_SEND_RESULT_LOGIN,onResultLogin);
    }

    private void initViews() {
        loading.setVisibility(View.INVISIBLE);

        btnLogin.setOnClickListener(this);
        btnRegister.setOnClickListener(this);

        cbRememberPass.setOnCheckedChangeListener(this);
    }

    private void resultLogin(final Object arg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                JSONArray data = (JSONArray) arg;
                if (data.length() == 0) {
                    Snackbar snackbar = Snackbar
                            .make(edtPassword, R.string.login_fail, Snackbar.LENGTH_SHORT);
                    snackbar.setActionTextColor(Color.WHITE);
                    View snackbarView = snackbar.getView();
                    snackbarView.setBackgroundColor(Color.DKGRAY);
                    snackbar.show();
                }else {
                    JSONObject object;
                    try {
                        object = data.getJSONObject(0);
                        int id = object.getInt("IDUSER");
                        String user = object.getString("USER");
                        String pass = object.getString("PASSWORD");
                        String email = object.getString("EMAIL");
                        String name = object.getString("FULLNAME");
                        String phone = object.getString("PHONE");
                        int gender = object.getInt("GENDER");
                        int isActive = object.getInt("IS_ACTIVE");
                        String createdAt = object.getString("CREATED_AT");
                        String image = object.getString("IMAGE");

                        User u = new User(id,user,pass,email,name,phone,gender,isActive,createdAt,image);

                        Prefs prefs = new Prefs(LoginActivity.this,Constants.KEY_USER_LOGIN,u);
                        prefs.comitUser();

                        Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                        startActivity(intent);
                        // trong anim ta tạo hai file rồi gọi ở đây để tạo animation khi chuyển activity
                        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                        finish();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnLogin:
                loading.setVisibility(View.VISIBLE);
                String user = edtUsername.getText().toString();
                String pass = edtPassword.getText().toString();
                if (user.isEmpty() || pass.isEmpty()){
                    Snackbar snackbar = Snackbar
                            .make(edtUsername, R.string.insert_info, Snackbar.LENGTH_SHORT);
                    snackbar.setActionTextColor(Color.WHITE);
                    View snackbarView = snackbar.getView();
                    snackbarView.setBackgroundColor(Color.DKGRAY);
                    snackbar.show();
                    return;
                }

                if (cbRememberPass.isChecked()){
                    SharedPreferences pre=getSharedPreferences("chatwithstranger", MODE_PRIVATE);
                    SharedPreferences.Editor edit=pre.edit();
                    edit.putString("user",edtUsername.getText().toString());
                    edit.putString("pass",edtPassword.getText().toString());
                    edit.apply();
                }else {
                    SharedPreferences pre=getSharedPreferences("chatwithstranger", MODE_PRIVATE);
                    SharedPreferences.Editor edit=pre.edit();
                    edit.putString("user","");
                    edit.putString("pass","");
                    edit.apply();
                }
                Singleton.Instance().getmSocket().emit(Constants.CLIENT_SEND_USER_PASS_LOGIN
                        ,user+"-"+pass);
                break;
            case R.id.btnRegister:
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
                // trong anim ta tạo hai file rồi gọi ở đây để tạo animation khi chuyển activity
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                finish();
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        switch (compoundButton.getId()){
            case R.id.checkBoxRememberPass:
                if (b){
                    SharedPreferences pre=getSharedPreferences("chatwithstranger", MODE_PRIVATE);
                    SharedPreferences.Editor edit=pre.edit();
                    edit.putString("user",edtUsername.getText().toString());
                    edit.putString("pass",edtPassword.getText().toString());
                    edit.apply();
                }else {
                    SharedPreferences pre=getSharedPreferences("chatwithstranger", MODE_PRIVATE);
                    SharedPreferences.Editor edit=pre.edit();
                    edit.putString("user","");
                    edit.putString("pass","");
                    edit.apply();
                }
                break;
        }
    }

}
