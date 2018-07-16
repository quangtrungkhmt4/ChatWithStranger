package com.example.quang.chatwithstranger;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ListView;

import com.example.quang.chatwithstranger.adapter.ListRequestAdapter;
import com.example.quang.chatwithstranger.consts.Constants;
import com.example.quang.chatwithstranger.model.Prefs;
import com.example.quang.chatwithstranger.model.RequestFriend;
import com.example.quang.chatwithstranger.model.User;
import com.example.quang.chatwithstranger.singleton.Singleton;
import com.github.nkzawa.emitter.Emitter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class RequestFriendActivity extends AppCompatActivity {

    Emitter.Listener onResultAllRequest;
    {
        onResultAllRequest = new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                resultAllRequest(args[0]);
            }
        };
    }

    private void resultAllRequest(final Object arg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                JSONArray data = (JSONArray) arg;
                for (int i=0; i<data.length();i++){
                    try {
                        JSONObject obj = data.getJSONObject(i);
                        int idRe = obj.getInt("IDREQUESTFRIEND");
                        int id = obj.getInt("IDUSER");
                        String name = obj.getString("FULLNAME");
                        String image = obj.getString("IMAGE");

                        RequestFriend request = new RequestFriend(idRe,id,name,image);
                        arrRequest.add(request);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                adapter = new ListRequestAdapter(RequestFriendActivity.this,R.layout.item_request_friend
                        ,arrRequest);
                lvRequest.setAdapter(adapter);
            }
        });
    }

    private Toolbar toolbar;
    private ListView lvRequest;
    private ListRequestAdapter adapter;
    private User user;
    private ArrayList<RequestFriend> arrRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_friend);

        findID();
        initSockets();
        loadData();
        initViews();
    }

    private void findID() {
        toolbar = findViewById(R.id.toolbarRequestFriend);
        lvRequest = findViewById(R.id.lvRequestFriend);
    }

    private void initSockets() {
        Singleton.Instance().getmSocket().on(Constants.SERVER_SEND_ALL_REQUEST_FRIEND,onResultAllRequest);
    }

    private void loadData() {
        arrRequest = new ArrayList<>();
        Prefs prefs = new Prefs(this);
        user = prefs.getUser(this, Constants.KEY_USER_LOGIN);

        Singleton.Instance().getmSocket().emit(Constants.CLIENT_GET_ALL_REQUEST_FRIEND,user.getId());
    }

    private void initViews() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.list_request);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                Intent intent = new Intent(RequestFriendActivity.this,MainActivity.class);
                startActivity(intent);
                // trong anim ta tạo hai file rồi gọi ở đây để tạo animation khi chuyển activity
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
