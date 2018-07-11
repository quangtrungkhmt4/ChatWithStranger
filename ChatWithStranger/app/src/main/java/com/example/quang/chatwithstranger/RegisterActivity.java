package com.example.quang.chatwithstranger;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.quang.chatwithstranger.consts.Constants;
import com.example.quang.chatwithstranger.singleton.Singleton;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.github.ybq.android.spinkit.SpinKitView;

import org.json.JSONArray;

import java.io.ByteArrayOutputStream;
import java.net.URISyntaxException;
import java.util.Calendar;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {

//    Emitter.Listener onResultUserExists;
//    Emitter.Listener onResultImage;
    Emitter.Listener onResultRegister;
    {
//        onResultUserExists = new Emitter.Listener() {
//            @Override
//            public void call(Object... args) {
//                resultUserExists(args[0]);
//            }
//        };
//
//        onResultImage = new Emitter.Listener() {
//            @Override
//            public void call(Object... args) {
//                resultImage(args[0]);
//            }
//        };

        onResultRegister = new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                resultRegister(args[0]);
            }
        };
    }


    private EditText edtName;
    private EditText edtUsername;
    private EditText edtPassword;
    private RadioGroup groupGender;
    private RadioButton radMale;
    private RadioButton radFemale;
    private Button btnRegister;
    private Button btnBack;
    private SpinKitView loading;

    private int gender;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        findID();
        initSockets();
        initViews();
    }

    private void findID() {
        edtName = findViewById(R.id.edtFullNameRe);
        edtUsername = findViewById(R.id.edtUsernameRe);
        edtPassword = findViewById(R.id.edtPasswordRe);
        groupGender = findViewById(R.id.groupSex);
        radMale = findViewById(R.id.radMale);
        radFemale = findViewById(R.id.radFemale);
        btnRegister = findViewById(R.id.btnRegisterRe);
        btnBack = findViewById(R.id.btnBackRe);
        loading = findViewById(R.id.loading_Register);
    }

    private void initSockets() {
//        Singleton.Instance().getmSocket().on(Constants.SERVER_SEND_RESULT_CHECK_EXISTS,onResultUserExists);
//        Singleton.Instance().getmSocket().on(Constants.SERVER_SEND_RESULT_AVATAR,onResultImage);
        Singleton.Instance().getmSocket().on(Constants.SERVER_SEND_RESULT_REGISTER,onResultRegister);
    }

    private void initViews() {
        loading.setVisibility(View.INVISIBLE);
        //imPhoto.setImageResource(R.drawable.ic_add_a_photo_white_100dp);

        gender = 0;

        btnBack.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
        groupGender.setOnCheckedChangeListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnBackRe:
                Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(intent);
                // trong anim ta tạo hai file rồi gọi ở đây để tạo animation khi chuyển activity
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                finish();
                break;
//            case R.id.btnChooseImageRe:
//                Intent pickPhoto = new Intent(Intent.ACTION_PICK,
//                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                startActivityForResult(pickPhoto , 1);//one can be replaced with any action code
//                break;
            case R.id.btnRegisterRe:
                loading.setVisibility(View.VISIBLE);
                String name = edtName.getText().toString();
                String user = edtUsername.getText().toString();
                String pass = edtPassword.getText().toString();

                if (name.isEmpty() || user.isEmpty() || pass.isEmpty()){
                    Snackbar snackbar = Snackbar
                            .make(edtUsername, R.string.insert_info, Snackbar.LENGTH_SHORT);
                    snackbar.setActionTextColor(Color.WHITE);
                    View snackbarView = snackbar.getView();
                    snackbarView.setBackgroundColor(Color.DKGRAY);
                    snackbar.show();
                    return;
                }

//                Singleton.Instance().getmSocket().emit(Constants.CLIENT_SEND_USER_CHECK_EXISTS,user);
//                Log.e("--------","checkExists");

                String url = "";
                if (gender == 1){
                    url = "/images/avatar/defaultMale.png";
                }else if (gender == 0){
                    url = "/images/avatar/defaultFemale.png";
                }
                String currentTime = Calendar.getInstance().getTime().toString();
//                String query = "INSERT INTO `tblusers` VALUES (null,'"+edtUsername.getText().toString()
//                        +"','"+edtPassword.getText().toString()+"','','"+edtName.getText().toString()
//                        +"','',"+gender+",0,'"+currentTime+"','"+url+"')";

                String data = edtUsername.getText().toString()+"-"+edtPassword.getText().toString()+"-"+edtName.getText().toString()+"-"+String.valueOf(gender)+"-"+currentTime+"-"+url;
                Singleton.Instance().getmSocket().emit(Constants.CLIENT_SEND_REQUEST_REGISTER,data);
                break;
        }
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        switch(requestCode) {
//            case 1:
//                if(resultCode == RESULT_OK){
//                    Uri selectedImage = data.getData();
//                    imPhoto.setImageURI(selectedImage);
//                }
//                break;
//        }
//    }

    public byte[] ImageView_To_Byte(ImageView imgv){

        Bitmap bmp = null;
        try {
            BitmapDrawable drawable = (BitmapDrawable) imgv.getDrawable();
            bmp = drawable.getBitmap();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();
            return byteArray;
        }catch (Exception e){
            return null;
        }
    }

    private void resultRegister(final Object arg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.e("--------","checkRegister");

                String result = (String) arg;
                if (result.equalsIgnoreCase("true")){
                    Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                    startActivity(intent);
                    // trong anim ta tạo hai file rồi gọi ở đây để tạo animation khi chuyển activity
                    overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                    finish();
                }else if (result.equalsIgnoreCase("false")){
                    Snackbar snackbar = Snackbar
                            .make(edtPassword, R.string.exists_user, Snackbar.LENGTH_SHORT);
                    snackbar.setActionTextColor(Color.WHITE);
                    View snackbarView = snackbar.getView();
                    snackbarView.setBackgroundColor(Color.DKGRAY);
                    snackbar.show();
                    loading.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

//    private void resultImage(final Object arg) {
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                Log.e("--------","checkImage: "+arg);
//                String url = (String) arg;
//                if (gender == 1){
//                    if (url.equalsIgnoreCase("null"))
//                        url = "/images/avatar/defaultMale.png";
//                }else if (gender == 0){
//                    if (url.equalsIgnoreCase("null"))
//                        url = "/images/avatar/defaultFemale.png";
//                }
//                String currentTime = Calendar.getInstance().getTime().toString();
////                String query = "INSERT INTO `tblusers` VALUES (null,'"+edtUsername.getText().toString()
////                        +"','"+edtPassword.getText().toString()+"','','"+edtName.getText().toString()
////                        +"','',"+gender+",0,'"+currentTime+"','"+url+"')";
//
//                String data = edtUsername.getText().toString()+"-"+edtPassword.getText().toString()+"-"+edtName.getText().toString()+"-"+String.valueOf(gender)+"-"+currentTime+"-"+url;
//                Singleton.Instance().getmSocket().emit(Constants.CLIENT_SEND_REQUEST_REGISTER,data);
//            }
//        });
//
//    }
//
//    private void resultUserExists(final Object arg) {
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                JSONArray data = (JSONArray) arg;
//                if (data.length() != 0) {
//                    Snackbar snackbar = Snackbar
//                            .make(edtPassword, R.string.exists_user, Snackbar.LENGTH_SHORT);
//                    snackbar.setActionTextColor(Color.WHITE);
//                    View snackbarView = snackbar.getView();
//                    snackbarView.setBackgroundColor(Color.DKGRAY);
//                    snackbar.show();
//                    return;
//                }else if (data.length() == 0){
//                    Log.e("++++++++","push");
//                    Singleton.Instance().getmSocket().emit(Constants.CLIENT_SEND_AVATAR,ImageView_To_Byte(imPhoto));
//                }
//            }
//        });
//    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        switch (radioGroup.getId()){
            case R.id.groupSex:
                if (i == R.id.radMale){
                    gender = 1;
                }else if (i == R.id.radFemale){
                    gender = 0;
                }
                break;
        }
    }

}
