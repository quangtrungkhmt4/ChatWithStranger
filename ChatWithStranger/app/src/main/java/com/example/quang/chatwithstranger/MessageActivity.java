package com.example.quang.chatwithstranger;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.quang.chatwithstranger.adapter.ListMessageAdapter;
import com.example.quang.chatwithstranger.consts.Constants;
import com.example.quang.chatwithstranger.model.Conversation;
import com.example.quang.chatwithstranger.model.Message;
import com.example.quang.chatwithstranger.model.Prefs;
import com.example.quang.chatwithstranger.model.User;
import com.example.quang.chatwithstranger.singleton.Singleton;
import com.github.nkzawa.emitter.Emitter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MessageActivity extends AppCompatActivity implements View.OnClickListener {

    Emitter.Listener onResultMessages;
    {
        onResultMessages = new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                ResultMessages(args[0]);
            }
        };
    }

    private void ResultMessages(final Object arg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (!arg.equals("false")){
                    JSONArray data = (JSONArray) arg;
                    for (int i=0; i<data.length();i++){
                        try {
                            JSONObject object = data.getJSONObject(i);
                            int id = object.getInt("IDMESSAGES");
                            int idConversation = object.getInt("IDCONVERSATION");
                            int iduser = object.getInt("IDUSER");
                            String text = object.getString("TEXT");
                            String photo = object.getString("PHOTO");
                            String video = object.getString("VIDEO");
                            String gif = object.getString("GIF");
                            String emotion = object.getString("EMOTION");
                            String createdAt = object.getString("CREATED_AT");

                            Message message = new Message(id,idConversation,iduser,text,photo,video,gif,emotion,createdAt);
                            arrMess.add(message);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

    private Toolbar toolbar;
    private ImageView btnMore;
    private ImageView btnEmotion;
    private ImageView btnPhoto;
    private ImageView btnDraw;
    private ImageView btnSend;
    private EditText edtMessage;
    private ListView lvMessage;
    private ArrayList<Message> arrMess;
    private ListMessageAdapter adapter;

    private boolean checkMore;
    private Conversation conversation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        findId();
        loadData();
        initSockets();
        initViews();
    }

    private void findId() {
        toolbar = findViewById(R.id.toolbarMessage);
        btnMore = findViewById(R.id.btnInsertMore);
        btnEmotion = findViewById(R.id.btnInsertEmotion);
        btnPhoto = findViewById(R.id.btnInsertPhoto);
        btnDraw = findViewById(R.id.btnInsertDraw);
        btnSend = findViewById(R.id.btnSendMessage);
        edtMessage = findViewById(R.id.edtTextMessage);
        lvMessage = findViewById(R.id.lvMessage);
    }

    private void loadData() {
        arrMess = new ArrayList<>();
        Intent intent = getIntent();
        conversation = (Conversation) intent.getSerializableExtra("conversation");

        Singleton.Instance().getmSocket().emit(Constants.CLIENT_GET_MESSAGES,conversation.getId());
    }

    private void initSockets(){
        Singleton.Instance().getmSocket().on(Constants.SERVER_SEND_RESULT_MESSAGES,onResultMessages);
    }

    private void initViews() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Prefs prefs = new Prefs(this);
        User user = prefs.getUser(this, Constants.KEY_USER_LOGIN);

        String[] arrName = conversation.getTitle().split(",");
        if (user.getName().equalsIgnoreCase(arrName[0])){
            getSupportActionBar().setTitle(arrName[1]);
        }else {
            getSupportActionBar().setTitle(arrName[0]);
        }

        adapter = new ListMessageAdapter(this,R.layout.item_chat_guest
                ,R.layout.item_chat_user,arrMess,user.getImage());
        lvMessage.setAdapter(adapter);

        checkMore = false;
        btnMore.setOnClickListener(this);
        edtMessage.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnInsertMore:
                if (!checkMore){
                    btnMore.setImageResource(R.drawable.ic_action_cancle_insert);
                    btnEmotion.setVisibility(View.VISIBLE);
                    btnPhoto.setVisibility(View.VISIBLE);
                    btnDraw.setVisibility(View.VISIBLE);
                    checkMore = true;
                }else {
                    btnMore.setImageResource(R.drawable.ic_action_more_insert);
                    btnEmotion.setVisibility(View.GONE);
                    btnPhoto.setVisibility(View.GONE);
                    btnDraw.setVisibility(View.GONE);
                    checkMore = false;
                }
                break;
            case R.id.edtTextMessage:
                if (!checkMore){

                }else {
                    btnMore.setImageResource(R.drawable.ic_action_more_insert);
                    btnEmotion.setVisibility(View.GONE);
                    btnPhoto.setVisibility(View.GONE);
                    btnDraw.setVisibility(View.GONE);
                    checkMore = false;
                }
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
