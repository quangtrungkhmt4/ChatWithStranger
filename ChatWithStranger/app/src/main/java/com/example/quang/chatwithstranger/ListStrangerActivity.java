package com.example.quang.chatwithstranger;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quang.chatwithstranger.adapter.ListFriendsAdapter;
import com.example.quang.chatwithstranger.consts.Constants;
import com.example.quang.chatwithstranger.model.JsonConversation;
import com.example.quang.chatwithstranger.model.ListFriend;
import com.example.quang.chatwithstranger.model.Prefs;
import com.example.quang.chatwithstranger.model.User;
import com.example.quang.chatwithstranger.singleton.Singleton;
import com.github.nkzawa.emitter.Emitter;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

public class ListStrangerActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    Emitter.Listener onResultAllUserOnline;
    {
        onResultAllUserOnline = new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                resultAllUserOnline(args[0]);
            }
        };

    }

    private void resultAllUserOnline(final Object arg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                arrStranger.clear();
                arrAllUser.clear();
                JSONArray data = (JSONArray) arg;
                for (int i=0; i<data.length(); i++){
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
                        arrAllUser.add(u);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                arrStranger.addAll(filterStranger(arrAllUser,arrFriend));
                adapter = new ListFriendsAdapter(ListStrangerActivity.this
                        ,R.layout.item_listview_friends,arrStranger);
                lvStranger.setAdapter(adapter);
                tvCountStranger.setText("("+arrStranger.size()+")");
            }
        });
    }

    private Toolbar toolbar;
    private TextView tvCountStranger;
    private ListFriendsAdapter adapter;
    private ListView lvStranger;
    private User user;
    private ArrayList<User> arrAllUser;
    private ArrayList<User> arrFriend;
    private ArrayList<User> arrStranger;
    private boolean checkRefesh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_stranger);

        findId();
        loadData();
        initSockets();
        initViews();

    }

    private void findId() {
        toolbar = findViewById(R.id.toolbarListStranger);
        tvCountStranger = findViewById(R.id.tvCountStranger);
        lvStranger = findViewById(R.id.lvStranger);
    }

    private void initSockets() {
        Singleton.Instance().getmSocket().on(Constants.SERVER_SEND_RESULT_USER_ONLINE,onResultAllUserOnline);
    }

    private void loadData() {
        Intent intent = getIntent();
        ListFriend lf = (ListFriend) intent.getSerializableExtra("listFriend");
        arrAllUser = new ArrayList<>();
        arrFriend = lf.getArrUser();
        arrStranger = new ArrayList<>();

        Prefs prefs = new Prefs(this);
        user = prefs.getUser(this,Constants.KEY_USER_LOGIN);

        Singleton.Instance().getmSocket().emit(Constants.CLIENT_GET_ALL_USER_ONLINE,user.getId());
    }


    private void initViews() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.list_stranger);

        arrStranger.addAll(filterStranger(arrAllUser,arrFriend));
        adapter = new ListFriendsAdapter(ListStrangerActivity.this
                ,R.layout.item_listview_friends,arrStranger);
        lvStranger.setAdapter(adapter);
        lvStranger.setOnItemClickListener(this);
        lvStranger.setOnItemLongClickListener(this);
        checkRefesh = true;
    }

    private ArrayList<User> filterStranger(ArrayList<User> allUser, ArrayList<User> friends){
        ArrayList<User> arrStranger = new ArrayList<>();
        for (User u : allUser){
            boolean check = false;
            for (User f : friends){
                if (u.getId() == f.getId()){
                    check = true;
                    continue;
                }
            }
            if (!check){
                arrStranger.add(u);
            }
        }
        return arrStranger;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
            case R.id.action_refesh:
                if (checkRefesh){
                    Toast.makeText(this, "refesh", Toast.LENGTH_SHORT).show();
                    Singleton.Instance().getmSocket().emit(Constants.CLIENT_GET_ALL_USER_ONLINE,user.getId());
                    checkRefesh = false;
                    new CountDownTimer(5000,1000){

                        @Override
                        public void onTick(long l) {

                        }

                        @Override
                        public void onFinish() {
                            checkRefesh = true;
                        }
                    }.start();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_refesh, menu);
        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
        JsonConversation jc = new JsonConversation(user.getName()+","+arrStranger.get(i).getName()
            ,user.getId(),arrStranger.get(i).getId(), Calendar.getInstance().getTime().toString());

        Gson gson = new Gson();
        JSONObject obj = null;
        try {
            obj = new JSONObject(gson.toJson(jc));
            Singleton.Instance().getmSocket().emit(Constants.CLIENT_CREATE_CONVERSATION,obj);
            new CountDownTimer(2000, 1000) {
                @Override
                public void onTick(long l) {

                }

                @Override
                public void onFinish() {
                    Intent intent = new Intent(ListStrangerActivity.this,MessageActivity.class);
                    intent.putExtra("friend",arrStranger.get(i));
                    startActivity(intent);
                    Log.e("stranger","----------stranger");
                    // trong anim ta tạo hai file rồi gọi ở đây để tạo animation khi chuyển activity
                    overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                }
            }.start();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int i, long l) {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(this);
        }
        builder.setTitle(R.string.add)
                .setMessage(R.string.question)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Singleton.Instance().getmSocket().emit(Constants.CILENT_SEND_REQUEST_ADD_FRIEND,user.getId()+"-"+arrStranger.get(i).getId());
                        dialog.dismiss();
                    }
                })
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
        return true;
    }
}
