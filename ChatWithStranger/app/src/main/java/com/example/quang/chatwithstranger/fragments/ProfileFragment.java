package com.example.quang.chatwithstranger.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.quang.chatwithstranger.MainActivity;
import com.example.quang.chatwithstranger.R;
import com.example.quang.chatwithstranger.consts.Constants;
import com.example.quang.chatwithstranger.model.JsonImage;
import com.example.quang.chatwithstranger.model.Prefs;
import com.example.quang.chatwithstranger.model.User;
import com.example.quang.chatwithstranger.singleton.Singleton;
import com.github.nkzawa.emitter.Emitter;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;


public class ProfileFragment extends Fragment implements View.OnClickListener {

    Emitter.Listener onResultChangeName;
    Emitter.Listener onResultChangePass;
    Emitter.Listener onResultChangeEmail;
    Emitter.Listener onResultChangePhone;
    Emitter.Listener onResultChangeGender;
    Emitter.Listener onResultImage;
    {
        onResultChangeName = new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                resultChangeName(args[0]);
            }
        };

        onResultChangePass = new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                resultChangePass(args[0]);
            }
        };

        onResultChangeEmail = new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                resultChangeEmail(args[0]);
            }

        };

        onResultChangePhone = new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                resultChangePhone(args[0]);
            }

        };

        onResultChangeGender = new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                resultChangeGender(args[0]);
            }

        };

        onResultImage = new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                resultImage(args[0]);
            }
        };
    }

    private CircleImageView imProfile;
    private TextView tvName;
    private TextView tvUser;
    private TextView tvPass;
    private TextView tvEmail;
    private TextView tvPhone;
    private TextView tvGender;

    private ImageView imEditPass;
    private ImageView imEditPhone;
    private ImageView imEditEmail;
    private ImageView imEditGender;
    private Button btnLogOut;

    private User user;

    private MainActivity mainActivity;

    private Dialog dialogChangeName;
    private Dialog dialogChangePass;
    private Dialog dialogChangeEmail;
    private Dialog dialogChangePhone;
    private Dialog dialogChangeGender;
    private Prefs prefs;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile
                ,container,false);
    }



    @Override
    public void onStart() {
        super.onStart();
        findID();
        loadData();
        initSockets();
        initViews();
        initDialogChangeName();
        initDialogChangePass();
        initDialogChangeEmail();
        initDialogChangePhone();
        initDialogChangeGender();
    }

    private void initDialogChangeGender() {
        dialogChangeGender = new Dialog(getActivity(),android.R.style.Theme_Holo_Light_Dialog_NoActionBar_MinWidth);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            dialogChangeGender.getWindow().setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY);
        else {
            dialogChangeGender.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        }
        dialogChangeGender.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogChangeGender.setContentView(R.layout.dialog_change_gender);
        dialogChangeGender.setCancelable(false);

        final RadioButton radMale = dialogChangeGender.findViewById(R.id.radMaleChangerGender);
        final RadioButton radFemale = dialogChangeGender.findViewById(R.id.radFemaleChangeGender);
        TextView tvCancle = dialogChangeGender.findViewById(R.id.tvCancleNewGender);
        TextView tvDone = dialogChangeGender.findViewById(R.id.tvDoneNewGender);

        tvCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogChangeGender.dismiss();
            }
        });

        tvDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (radMale.isChecked()){
                    Singleton.Instance().getmSocket().emit(Constants.CLIENT_SEND_NEW_GENDER,1+"-"+user.getId());
                }else  if (radFemale.isChecked()){
                    Singleton.Instance().getmSocket().emit(Constants.CLIENT_SEND_NEW_GENDER,0+"-"+user.getId());

                }
                dialogChangeGender.dismiss();
            }
        });
    }

    private void initDialogChangePhone() {
        dialogChangePhone = new Dialog(getActivity(),android.R.style.Theme_Holo_Light_Dialog_NoActionBar_MinWidth);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            dialogChangePhone.getWindow().setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY);
        else {
            dialogChangePhone.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        }
        dialogChangePhone.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogChangePhone.setContentView(R.layout.dialog_change_phone);
        dialogChangePhone.setCancelable(false);

        final EditText edtNewPhone = dialogChangePhone.findViewById(R.id.edtNewPhone);
        TextView tvCancle = dialogChangePhone.findViewById(R.id.tvCancleNewPhone);
        TextView tvDone = dialogChangePhone.findViewById(R.id.tvDoneNewPhone);

        tvCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogChangePhone.dismiss();
            }
        });

        tvDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edtNewPhone.getText().toString().isEmpty()){
                    return;
                }

                Singleton.Instance().getmSocket().emit(Constants.CLIENT_SEND_NEW_PHONE,edtNewPhone.getText().toString()+"-"+user.getId());
                dialogChangePhone.dismiss();
            }
        });
    }

    private void initDialogChangeEmail() {
        dialogChangeEmail = new Dialog(getActivity(),android.R.style.Theme_Holo_Light_Dialog_NoActionBar_MinWidth);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            dialogChangeEmail.getWindow().setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY);
        else {
            dialogChangeEmail.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        }
        dialogChangeEmail.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogChangeEmail.setContentView(R.layout.dialog_change_email);
        dialogChangeEmail.setCancelable(false);

        final EditText edtNewEmail = dialogChangeEmail.findViewById(R.id.edtNewEmail);
        TextView tvCancle = dialogChangeEmail.findViewById(R.id.tvCancleNewEmail);
        TextView tvDone = dialogChangeEmail.findViewById(R.id.tvDoneNewEmail);

        tvCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogChangeEmail.dismiss();
            }
        });

        tvDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edtNewEmail.getText().toString().isEmpty()){
                    return;
                }

                Singleton.Instance().getmSocket().emit(Constants.CLIENT_SEND_NEW_EMAIL,edtNewEmail.getText().toString()+"-"+user.getId());
                dialogChangeEmail.dismiss();
            }
        });
    }

    private void initDialogChangePass() {
        dialogChangePass = new Dialog(getActivity(),android.R.style.Theme_Holo_Light_Dialog_NoActionBar_MinWidth);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            dialogChangePass.getWindow().setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY);
        else {
            dialogChangePass.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        }
        dialogChangePass.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogChangePass.setContentView(R.layout.dialog_change_pass);
        dialogChangePass.setCancelable(false);

        final EditText edtNewPass = dialogChangePass.findViewById(R.id.edtNewPass);
        final EditText edtOldPass = dialogChangePass.findViewById(R.id.edtOldPass);
        TextView tvCancle = dialogChangePass.findViewById(R.id.tvCancleNewPass);
        TextView tvDone = dialogChangePass.findViewById(R.id.tvDoneNewPass);


        tvCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogChangePass.dismiss();
            }
        });

        tvDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edtNewPass.getText().toString().isEmpty()||edtOldPass.getText().toString().isEmpty()){
                    return;
                }

                Singleton.Instance().getmSocket().emit(Constants.CLIENT_SEND_NEW_PASS
                        ,edtNewPass.getText().toString()+"-"+edtOldPass.getText().toString()+"-"+user.getId());
                dialogChangePass.dismiss();
            }
        });
    }

    private void initDialogChangeName() {
        dialogChangeName = new Dialog(getActivity(),android.R.style.Theme_Holo_Light_Dialog_NoActionBar_MinWidth);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            dialogChangeName.getWindow().setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY);
        else {
            dialogChangeName.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        }
        dialogChangeName.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogChangeName.setContentView(R.layout.dialog_change_name);
        dialogChangeName.setCancelable(false);

        final EditText edtNewname = dialogChangeName.findViewById(R.id.edtNewName);
        TextView tvCancle = dialogChangeName.findViewById(R.id.tvCancleNewName);
        TextView tvDone = dialogChangeName.findViewById(R.id.tvDoneNewName);

        tvCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogChangeName.dismiss();
            }
        });

        tvDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edtNewname.getText().toString().isEmpty()){
                    return;
                }

                Singleton.Instance().getmSocket().emit(Constants.CLIENT_SEND_NEW_NAME,edtNewname.getText().toString()+"-"+user.getId());
                dialogChangeName.dismiss();
            }
        });
    }

    private void findID() {
        imProfile = getActivity().findViewById(R.id.imProfile);
        tvName = getActivity().findViewById(R.id.tvNameProfile);
        tvUser = getActivity().findViewById(R.id.tvUserProfile);
        tvPass = getActivity().findViewById(R.id.tvPassProfile);
        tvEmail = getActivity().findViewById(R.id.tvEmailProfile);
        tvPhone = getActivity().findViewById(R.id.tvPhoneProfile);
        tvGender = getActivity().findViewById(R.id.tvGenderProfile);
        imEditPass = getActivity().findViewById(R.id.imEditPass);
        imEditEmail = getActivity().findViewById(R.id.imEditEmail);
        imEditPhone = getActivity().findViewById(R.id.imEditPhone);
        imEditGender = getActivity().findViewById(R.id.imEditGender);
        btnLogOut = getActivity().findViewById(R.id.btnLogout);
    }

    private void initSockets() {
        Singleton.Instance().getmSocket().on(Constants.SERVER_SEND_RESULT_CHANGE_NAME,onResultChangeName);
        Singleton.Instance().getmSocket().on(Constants.SERVER_SEND_RESULT_CHANGE_PASS,onResultChangePass);
        Singleton.Instance().getmSocket().on(Constants.SERVER_SEND_RESULT_CHANGE_EMAIL,onResultChangeEmail);
        Singleton.Instance().getmSocket().on(Constants.SERVER_SEND_RESULT_CHANGE_PHONE,onResultChangePhone);
        Singleton.Instance().getmSocket().on(Constants.SERVER_SEND_RESULT_CHANGE_GENDER,onResultChangeGender);
        Singleton.Instance().getmSocket().on(Constants.SERVER_SEND_RESULT_AVATAR,onResultImage);
    }

    private void loadData() {
        mainActivity = (MainActivity) getActivity();

        prefs = new Prefs(mainActivity.getAppContext());
        user = prefs.getUser(mainActivity,Constants.KEY_USER_LOGIN);

        Glide.with(this).load(Constants.PORT+user.getImage()).into(imProfile);
        tvName.setText(user.getName());
        tvUser.setText(user.getUser());
        tvPass.setText(user.getPass());
        tvPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
        tvEmail.setText(user.getEmail());
        tvPhone.setText(user.getPhone());
        if (user.getGender() == 1){
            tvGender.setText(R.string.male);
        }else if (user.getGender() == 0){
            tvGender.setText(R.string.female);
        }
    }

    private void initViews() {
        imProfile.setOnClickListener(this);
        tvName.setOnClickListener(this);
        imEditPass.setOnClickListener(this);
        imEditEmail.setOnClickListener(this);
        imEditPhone.setOnClickListener(this);
        imEditGender.setOnClickListener(this);
        btnLogOut.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.imProfile:
                Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto , 111);//one can be replaced with any action code
                break;
            case R.id.tvNameProfile:
                dialogChangeName.show();
                break;
            case R.id.imEditPass:
                dialogChangePass.show();
                break;
            case R.id.imEditEmail:
                dialogChangeEmail.show();
                break;
            case R.id.imEditPhone:
                dialogChangePhone.show();
                break;
            case R.id.imEditGender:
                dialogChangeGender.show();
                break;
            case R.id.btnLogout:
                mainActivity.logout(user.getId());
                break;
        }
    }

    private void resultChangeName(final Object arg) {
        mainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String result = (String) arg;
                if (result.equalsIgnoreCase("false")){
                    Snackbar snackbar = Snackbar
                            .make(imEditEmail, R.string.change_name_fail, Snackbar.LENGTH_SHORT);
                    snackbar.setActionTextColor(Color.WHITE);
                    View snackbarView = snackbar.getView();
                    snackbarView.setBackgroundColor(Color.DKGRAY);
                    snackbar.show();
                }else {
                    tvName.setText(result);
                    user.setName(result);
                    Prefs p = new Prefs(getContext(),Constants.KEY_USER_LOGIN,user);
                    p.comitUser();
                }
            }
        });
    }

    private void resultChangePass(final Object arg) {
        mainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String result = (String) arg;
                if (result.equalsIgnoreCase("false")){
                    Snackbar snackbar = Snackbar
                            .make(imEditEmail, R.string.change_pass_fail, Snackbar.LENGTH_SHORT);
                    snackbar.setActionTextColor(Color.WHITE);
                    View snackbarView = snackbar.getView();
                    snackbarView.setBackgroundColor(Color.DKGRAY);
                    snackbar.show();
                }else {
                    tvPass.setText(result);
                    user.setPass(result);
                    Prefs p = new Prefs(getContext(),Constants.KEY_USER_LOGIN,user);
                    p.comitUser();
                }
            }
        });
    }

    private void resultChangeEmail(final Object arg) {
        mainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String result = (String) arg;
                if (result.equalsIgnoreCase("false")){
                    Snackbar snackbar = Snackbar
                            .make(imEditEmail, R.string.change_email_fail, Snackbar.LENGTH_SHORT);
                    snackbar.setActionTextColor(Color.WHITE);
                    View snackbarView = snackbar.getView();
                    snackbarView.setBackgroundColor(Color.DKGRAY);
                    snackbar.show();
                }else {
                    tvEmail.setText(result);
                    user.setEmail(result);
                    Prefs p = new Prefs(getContext(),Constants.KEY_USER_LOGIN,user);
                    p.comitUser();
                }
            }
        });
    }

    private void resultChangePhone(final Object arg) {
        mainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String result = (String) arg;
                if (result.equalsIgnoreCase("false")){
                    Snackbar snackbar = Snackbar
                            .make(imEditEmail, R.string.change_phone_fail, Snackbar.LENGTH_SHORT);
                    snackbar.setActionTextColor(Color.WHITE);
                    View snackbarView = snackbar.getView();
                    snackbarView.setBackgroundColor(Color.DKGRAY);
                    snackbar.show();
                }else {
                    tvPhone.setText(result);
                    user.setPhone(result);
                    Prefs p = new Prefs(getContext(),Constants.KEY_USER_LOGIN,user);
                    p.comitUser();
                }
            }
        });
    }

    private void resultChangeGender(final Object arg) {
        mainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                int result = (int) arg;
                if (result == -1){
                    Snackbar snackbar = Snackbar
                            .make(imEditEmail, R.string.change_gender_fail, Snackbar.LENGTH_SHORT);
                    snackbar.setActionTextColor(Color.WHITE);
                    View snackbarView = snackbar.getView();
                    snackbarView.setBackgroundColor(Color.DKGRAY);
                    snackbar.show();
                }else {
                    if (result == 1){
                        tvGender.setText(R.string.male);
                        user.setGender(1);
                        Prefs p = new Prefs(getContext(),Constants.KEY_USER_LOGIN,user);
                        p.comitUser();
                    }else if (result == 0){
                        tvGender.setText(R.string.female);
                        user.setGender(0);
                        Prefs p = new Prefs(getContext(),Constants.KEY_USER_LOGIN,user);
                        p.comitUser();
                    }
                }
            }
        });
    }

    private void resultImage(final Object arg) {
        mainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String url = (String) arg;
                if (url.equalsIgnoreCase("null")){
                    Snackbar snackbar = Snackbar
                            .make(imEditEmail, R.string.upimage_fail, Snackbar.LENGTH_SHORT);
                    snackbar.setActionTextColor(Color.WHITE);
                    View snackbarView = snackbar.getView();
                    snackbarView.setBackgroundColor(Color.DKGRAY);
                    snackbar.show();
                }else {
                    Glide.with(mainActivity.getAppContext()).load(Constants.PORT+url).into(imProfile);
                    user.setImage(url);
                    Prefs p = new Prefs(mainActivity.getAppContext(),Constants.KEY_USER_LOGIN,user);
                    p.comitUser();
                }
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {
            case 111:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = data.getData();
                    try {
                        InputStream iStream =   getActivity().getContentResolver().openInputStream(selectedImage);
                        byte[] bytes = getBytes(iStream);

                        JsonImage jsonImage = new JsonImage(user.getId(),bytes);
                        Gson gson = new Gson();
                        try {
                            JSONObject obj = new JSONObject(gson.toJson(jsonImage));
                            Singleton.Instance().getmSocket().emit(Constants.CLIENT_SEND_AVATAR,obj);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }

    public byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }
}
