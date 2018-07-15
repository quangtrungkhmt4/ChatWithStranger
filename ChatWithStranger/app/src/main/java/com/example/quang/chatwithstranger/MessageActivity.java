package com.example.quang.chatwithstranger;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.quang.chatwithstranger.adapter.ListMessAdapter;
import com.example.quang.chatwithstranger.adapter.ListMessageAdapter;
import com.example.quang.chatwithstranger.consts.Constants;
import com.example.quang.chatwithstranger.model.JsonPhotoMessage;
import com.example.quang.chatwithstranger.model.JsonTextMessage;
import com.example.quang.chatwithstranger.model.Message;
import com.example.quang.chatwithstranger.model.Prefs;
import com.example.quang.chatwithstranger.model.User;
import com.example.quang.chatwithstranger.singleton.Singleton;
import com.github.nkzawa.emitter.Emitter;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;

public class MessageActivity extends AppCompatActivity implements View.OnClickListener, ListMessAdapter.ClickListener {

    Emitter.Listener onResultMessages;
    Emitter.Listener onResultUpdateMess;
    {
        onResultMessages = new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                ResultMessages(args[0]);
            }
        };

        onResultUpdateMess = new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                resultUpdateMess(args[0]);
            }
        };
    }

    private void resultUpdateMess(final Object arg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                JSONObject obj = (JSONObject) arg;
                try {
                    int idCon = obj.getInt("idCon");
                    if (idCon == idConversation){
                        JSONArray data = (JSONArray) obj.getJSONArray("arr");
                        for (int i=0; i<data.length();i++){
                            try {
                                JSONObject object = data.getJSONObject(i);
                                int id = object.getInt("IDMESSAGES");
                                int idConversation = object.getInt("IDCONVERSATION");
                                int iduser = object.getInt("IDUSER");
                                String text = object.getString("TEXT");
                                String photo = object.getString("PHOTO");
                                String createdAt = object.getString("CREATED_AT");
                                String avatar = object.getString("IMAGE");
                                Message message = new Message(id,idConversation,iduser,text,photo,createdAt,avatar);
                                arrMess.add(message);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
//                        messAdapter = new ListMessAdapter(MessageActivity.this, arrMess,user.getId());
//                        recyclerView.setLayoutManager(new LinearLayoutManager(MessageActivity.this));
//                        recyclerView.setAdapter(messAdapter);
                        messAdapter.notifyDataSetChanged();
                        recyclerView.scrollToPosition(arrMess.size() - 1);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void ResultMessages(final Object arg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (!arg.equals("false")){
                    arrMess.clear();
                    JSONObject obj = (JSONObject) arg;
                    try {
                        int idCon = obj.getInt("idCon");
                        idConversation = idCon;
                            JSONArray data = (JSONArray) obj.getJSONArray("arr");
                            for (int i=0; i<data.length();i++){
                                try {
                                    JSONObject object = data.getJSONObject(i);
                                    int id = object.getInt("IDMESSAGES");
                                    int idConversation = object.getInt("IDCONVERSATION");
                                    int iduser = object.getInt("IDUSER");
                                    String text = object.getString("TEXT");
                                    String photo = object.getString("PHOTO");
                                    String createdAt = object.getString("CREATED_AT");
                                    String avatar = object.getString("IMAGE");
                                    Message message = new Message(id,idConversation,iduser,text,photo,createdAt,avatar);
                                    arrMess.add(message);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }


                        messAdapter = new ListMessAdapter(MessageActivity.this, arrMess,user.getId());
                        recyclerView.setLayoutManager(new LinearLayoutManager(MessageActivity.this));
                        recyclerView.setAdapter(messAdapter);
                        recyclerView.scrollToPosition(arrMess.size() - 1);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        });
    }

    private User friend;
    private User user;

    private Toolbar toolbar;
    private ImageView btnMore;
    private ImageView btnEmotion;
    private ImageView btnPhoto;
    private ImageView btnDraw;
    private ImageView btnSend;
    private EditText edtMessage;
    private ListView lvMessage;
    private int idConversation;
    private ArrayList<Message> arrMess;
    private ListMessageAdapter adapter;
    private GridView gvEmotion;
    private CardView cardEmotion;
//    String[] arrEmotion = new String[]{"0x1F601","0x1F602","0x1F603","0x1F604","0x1F605"
//            ,"0x1F606","0x1F609","0x1F60A","0x1F60B","0x1F60C","0x1F60D","0x1F60F","0x1F612"
//            ,"0x1F613","0x1F614","0x1F616","0x1F618","0x1F61A","0x1F61C","0x1F61D","0x1F61E"
//            ,"0x1F620","0x1F621","0x1F622","0x1F623","0x1F624","0x1F625","0x1F628","0x1F629"
//            ,"0x1F62A","0x1F62B","0x1F62D","0x1F630","0x1F631","0x1F632","0x1F633","0x1F635","0x1F637"};
//    private ListEmotionAdapter emotionAdapter;
//
    private boolean checkMore;
//    private Conversation conversation;

//    private boolean checkOpenEmotion;

    private RecyclerView recyclerView;
    private ListMessAdapter messAdapter;

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
        //lvMessage = findViewById(R.id.lvMessage);
        gvEmotion = findViewById(R.id.gvEmotion);
        cardEmotion = findViewById(R.id.cardEmotion);

        recyclerView = (RecyclerView) findViewById(R.id.reyclerview_message_list);
    }

    private void loadData() {
        arrMess = new ArrayList<>();
        Intent intent = getIntent();
        friend = (User) intent.getSerializableExtra("friend");
        Prefs prefs = new Prefs(this);
        user = prefs.getUser(this,Constants.KEY_USER_LOGIN);



//        conversation = (Conversation) intent.getSerializableExtra("conversation");


        Singleton.Instance().getmSocket().emit(Constants.CLIENT_GET_MESSAGES,friend.getId()+"-"+user.getId());
    }

    private void initSockets(){
        Singleton.Instance().getmSocket().on(Constants.SERVER_SEND_RESULT_MESSAGES,onResultMessages);
        Singleton.Instance().getmSocket().on(Constants.SERVER_SEND_UPDATE_MESSAGES,onResultUpdateMess);
    }

    private void initViews() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(friend.getName());

        checkMore = false;
        btnMore.setOnClickListener(this);
        edtMessage.setOnClickListener(this);
        btnSend.setOnClickListener(this);
        btnPhoto.setOnClickListener(this);
        btnEmotion.setOnClickListener(this);

        messAdapter = new ListMessAdapter(MessageActivity.this, arrMess,user.getId());
        recyclerView.setLayoutManager(new LinearLayoutManager(MessageActivity.this));
        recyclerView.setAdapter(messAdapter);
        messAdapter.setOnItemClickListener(this);


//        emotionAdapter = new ListEmotionAdapter(this,R.layout.item_emotion,arrEmotion);
//        gvEmotion.setAdapter(emotionAdapter);
//        checkOpenEmotion = false;
//
//        gvEmotion.setOnItemClickListener(this);


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
            case R.id.btnSendMessage:
                String text = edtMessage.getText().toString();
                if (text.isEmpty()){
                    return;
                }
                int lastIndex = 0;
                if(arrMess.size() == 0)
                    lastIndex = 0;
                else
                    lastIndex = arrMess.get(arrMess.size() - 1).getId();
                JsonTextMessage mess = new JsonTextMessage(idConversation,user.getId(),text
                        , Calendar.getInstance().getTime().toString(),lastIndex);
                Gson gson = new Gson();
                try {
                    JSONObject obj = new JSONObject(gson.toJson(mess));
                    Singleton.Instance().getmSocket().emit(Constants.CLIENT_SEND_TEXT_MESSAGE,obj);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                edtMessage.setText("");
                break;
            case R.id.btnInsertPhoto:
                Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto , 111);//one can be replaced with any action code
                break;
            case R.id.btnInsertEmotion:
//                if (checkOpenEmotion){
//                    cardEmotion.setVisibility(View.GONE);
//                    checkOpenEmotion = false;
//                }else {
//                    cardEmotion.setVisibility(View.VISIBLE);
//                    checkOpenEmotion = true;
//                }

                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case 111:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = data.getData();
                    InputStream iStream = null;
                    try {
                        iStream = getContentResolver().openInputStream(selectedImage);
                        byte[] bytes = getBytes(iStream);

                        int lastIndex = 0;
                        if(arrMess.size() == 0)
                            lastIndex = 0;
                        else
                            lastIndex = arrMess.get(arrMess.size() - 1).getId();
                        JsonPhotoMessage photoMessage = new JsonPhotoMessage(idConversation,user.getId()
                            ,bytes,Calendar.getInstance().getTime().toString(),lastIndex);
                        Gson gson = new Gson();
                        JSONObject obj = new JSONObject(gson.toJson(photoMessage));

                        Singleton.Instance().getmSocket().emit(Constants.CLIENT_SEND_PHOTO_MESSAGE,obj);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onItemClick(int position, View v) {
        Toast.makeText(this, arrMess.get(position).getText()+"", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemLongClick(int position, View v) {

    }


//    @Override
//    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//        switch (adapterView.getId()){
//            case R.id.gvEmotion:
//                JsonEmotion jsonEmotion = new JsonEmotion(conversation.getId(),user.getId()
//                        ,arrEmotion[i],Calendar.getInstance().getTime().toString());
//                Gson gson = new Gson();
//                JSONObject obj = null;
//                try {
//                    obj = new JSONObject(gson.toJson(jsonEmotion));
//                    Singleton.Instance().getmSocket().emit(Constants.CLIENT_SEND_EMOTION_MESSAGE,obj);
//                } catch (JSONException e) {
//
//
//                }
//
//                break;
//        }
//    }
}
