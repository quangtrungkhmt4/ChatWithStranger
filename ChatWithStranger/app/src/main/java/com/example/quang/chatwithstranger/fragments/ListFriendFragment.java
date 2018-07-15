package com.example.quang.chatwithstranger.fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.quang.chatwithstranger.MainActivity;
import com.example.quang.chatwithstranger.MessageActivity;
import com.example.quang.chatwithstranger.R;
import com.example.quang.chatwithstranger.adapter.ListFriendsAdapter;
import com.example.quang.chatwithstranger.consts.Constants;
import com.example.quang.chatwithstranger.model.Conversation;
import com.example.quang.chatwithstranger.model.JsonConversation;
import com.example.quang.chatwithstranger.model.Prefs;
import com.example.quang.chatwithstranger.model.User;
import com.example.quang.chatwithstranger.singleton.Singleton;
import com.example.quang.chatwithstranger.views.CustomListView;
import com.github.nkzawa.emitter.Emitter;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class ListFriendFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener {

    Emitter.Listener onResultOnOff;
    Emitter.Listener onResultFriends;
    Emitter.Listener onResultBlocks;
    Emitter.Listener onResultCreateConversation;
    {
        onResultOnOff = new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                resultOnOff(args[0]);
            }
        };

        onResultFriends = new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                resultFriends(args[0]);
            }
        };

        onResultBlocks = new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                resultBlocks(args[0]);
            }
        };

        onResultCreateConversation = new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                resultCreateConversation(args[0]);
            }
        };
    }

    private void resultOnOff(final Object arg) {
        mainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                JSONArray data = (JSONArray) arg;
                try {
                    JSONObject obj = data.getJSONObject(0);
                    int id = obj.getInt("IDUSER");
                    int is_active = obj.getInt("IS_ACTIVE");
                    for (User user:arrFriend){
                        if (user.getId() == id){
                            user.setIsActive(is_active);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                adapterFriends.notifyDataSetChanged();
            }
        });
    }

    private void resultCreateConversation(final Object arg) {
        Log.e("result-----------","result");
        mainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.e("result-----------","result1");
                JSONArray data = (JSONArray) arg;
                if(data.length() == 1){
                    try {
                        JSONObject object = data.getJSONObject(0);
                        int id = object.getInt("IDCONVERSATION");
                        String title = object.getString("TITLE");
                        String created_at = object.getString("CREATED_AT");
                        int u = object.getInt("USER");
                        int guest = object.getInt("GUEST");

                        conversation = new Conversation(id,title,created_at,u,guest);
                        Log.e("result-----------","result2");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        Log.e("result-----------","result3");
    }


    private void resultBlocks(final Object arg) {
        mainActivity.runOnUiThread(new Runnable() {
            @SuppressLint("SetTextI18n")
            @Override
            public void run() {
                arrBlocks.clear();
                JSONArray data = (JSONArray) arg;
                for (int i=0; i<data.length();i++){
                    try {
                        JSONObject object = data.getJSONObject(i);
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
                        arrBlocks.add(u);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                tvCountBlocks.setText("("+arrBlocks.size()+")");
                adapterBlocks.notifyDataSetChanged();
            }
        });
    }

    private void resultFriends(final Object arg) {
        mainActivity.runOnUiThread(new Runnable() {
            @SuppressLint("SetTextI18n")
            @Override
            public void run() {
                arrFriend.clear();
                JSONArray data = (JSONArray) arg;
                for (int i=0; i<data.length();i++){
                    try {
                        JSONObject object = data.getJSONObject(i);
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
                        arrFriend.add(u);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                tvCountFriends.setText("("+arrFriend.size()+")");
                adapterFriends.notifyDataSetChanged();
            }
        });
    }

    private Toolbar toolbar;
    private MainActivity mainActivity;
    private CardView cardShowFriends;
    private CardView cardShowBlocks;
    private CustomListView lvFriends;
    private CustomListView lvBlocks;
    private FloatingActionButton btnRandom;
    private ListFriendsAdapter adapterFriends;
    private ListFriendsAdapter adapterBlocks;
    private TextView tvCountFriends;
    private TextView tvCountBlocks;

    private ArrayList<User> arrFriend;
    private ArrayList<User> arrBlocks;

    private boolean checkOpenFriends;
    private boolean checkOpenBlocks;
    private User user;

    private Dialog dialogInfo;


    private Conversation conversation;

    private boolean checkClick = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list_friend,container,false);
    }

    @Override
    public void onStart() {
        super.onStart();

        findID();
        loadData();
        initSockets();
        initViews();

    }

    private void initDialogInfo(final User friend) {

//        JsonConversation jsonConversation = new JsonConversation(name+","+user.getName()
//                ,user.getId(),id,Calendar.getInstance().getTime().toString());
//        Gson gson = new Gson();
//        try {
//            JSONObject obj = new JSONObject(gson.toJson(jsonConversation));
//            Singleton.Instance().getmSocket().emit(Constants.CLIENT_CREATE_CONVERSATION,obj);
//            Log.e("click-----------","click");
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }



        dialogInfo = new Dialog(getActivity(),android.R.style.Theme_Holo_Light_Dialog_NoActionBar_MinWidth);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            dialogInfo.getWindow().setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY);
        else {
            dialogInfo.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        }
        dialogInfo.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogInfo.setContentView(R.layout.dialog_info_friend);
        dialogInfo.setCancelable(false);

        ImageView btnDimiss = dialogInfo.findViewById(R.id.btnDimissDialog);
        CircleImageView imPhoto = dialogInfo.findViewById(R.id.imDialogProfile);
        TextView tvName = dialogInfo.findViewById(R.id.tvNameDialogProfile);
        TextView tvEmail = dialogInfo.findViewById(R.id.tvEmailDialogProfile);
        TextView tvPhone = dialogInfo.findViewById(R.id.tvPhoneDialogProfile);
        TextView tvGender = dialogInfo.findViewById(R.id.tvGenderDialogProfile);
        Button btnChat = dialogInfo.findViewById(R.id.btnChatDialogProfile);

        Glide.with(mainActivity).load(Constants.PORT+friend.getImage()).into(imPhoto);
        tvName.setText(friend.getName());
        tvEmail.setText(friend.getEmail());
        tvPhone.setText(friend.getPhone());
        if (friend.getGender() == 1){
            tvGender.setText(R.string.male);
        }else {
            tvGender.setText(R.string.female);
        }

        btnDimiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogInfo.dismiss();
                checkClick = false;
                conversation = null;
            }
        });

        btnChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mainActivity,MessageActivity.class);
                intent.putExtra("friend",friend);
                mainActivity.overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                startActivity(intent);
                dialogInfo.dismiss();
                Log.e("switch","-------------Switch");
            }
        });

        dialogInfo.show();

    }

    @SuppressLint("NewApi")
    private void findID() {
        toolbar = Objects.requireNonNull(getActivity()).findViewById(R.id.toolbarListFriend);
        cardShowFriends = getActivity().findViewById(R.id.cardShowFriends);
        cardShowBlocks = getActivity().findViewById(R.id.cardShowBlocks);
        lvFriends = getActivity().findViewById(R.id.lvFriends);
        lvBlocks = getActivity().findViewById(R.id.lvBlocks);
        btnRandom = getActivity().findViewById(R.id.btnRandon);
        tvCountFriends = getActivity().findViewById(R.id.tvCountFriends);
        tvCountBlocks = getActivity().findViewById(R.id.tvCountBlocks);
    }

    private void loadData(){
        arrFriend = new ArrayList<>();
        arrBlocks = new ArrayList<>();
        mainActivity = (MainActivity) getActivity();
        Prefs prefs = new Prefs(mainActivity);
        user = prefs.getUser(mainActivity,Constants.KEY_USER_LOGIN);
        Singleton.Instance().getmSocket().emit(Constants.CLIENT_GET_FRIENDS,user.getId());

        adapterFriends = new ListFriendsAdapter(mainActivity,R.layout.item_listview_friends,arrFriend);
        lvFriends.setAdapter(adapterFriends);

        Singleton.Instance().getmSocket().emit(Constants.CLIENT_GET_BLOCKS,user.getId());

        adapterBlocks = new ListFriendsAdapter(mainActivity,R.layout.item_listview_friends,arrBlocks);
        lvBlocks.setAdapter(adapterBlocks);


    }

    private void initSockets() {
        Singleton.Instance().getmSocket().on(Constants.SERVER_SEND_STATE_ON_OFF,onResultOnOff);
        Singleton.Instance().getmSocket().on(Constants.SERVER_SEND_RESULT_FRIENDS,onResultFriends);
        Singleton.Instance().getmSocket().on(Constants.SERVER_SEND_RESULT_BLOCKS,onResultBlocks);
        Singleton.Instance().getmSocket().on(Constants.SERVER_SEND_RESULT_CREATE_CONVERSATION,onResultCreateConversation);
    }

    @SuppressLint("NewApi")
    private void initViews() {
        mainActivity.setSupportActionBar(toolbar);
        Objects.requireNonNull(mainActivity.getSupportActionBar()).setTitle(R.string.friends);

        checkOpenFriends = true;
        checkOpenBlocks = false;

        cardShowFriends.setOnClickListener(this);
        cardShowBlocks.setOnClickListener(this);


        lvFriends.setOnItemClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.cardShowFriends:
                if (!checkOpenFriends){
                    lvFriends.setVisibility(View.VISIBLE);
                    checkOpenFriends = true;
                }else {
                    lvFriends.setVisibility(View.GONE);
                    checkOpenFriends = false;
                }
                break;
            case  R.id.cardShowBlocks:
                if (!checkOpenBlocks){
                    lvBlocks.setVisibility(View.VISIBLE);
                    checkOpenBlocks = true;
                }else {
                    lvBlocks.setVisibility(View.GONE);
                    checkOpenBlocks = false;
                }
                break;
        }
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        switch (adapterView.getId()){
            case R.id.lvFriends:
                if (!checkClick){

                    User friend = arrFriend.get(i);
                    initDialogInfo(friend);

                    checkClick = true;
                }
                break;
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("resume-----------","resume");
        checkClick = false;
    }
}
