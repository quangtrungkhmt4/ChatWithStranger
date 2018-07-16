package com.example.quang.chatwithstranger.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.quang.chatwithstranger.ListStrangerActivity;
import com.example.quang.chatwithstranger.MainActivity;
import com.example.quang.chatwithstranger.MessageActivity;
import com.example.quang.chatwithstranger.R;
import com.example.quang.chatwithstranger.adapter.ListConversationAdapter;
import com.example.quang.chatwithstranger.consts.Constants;
import com.example.quang.chatwithstranger.model.Conversation;
import com.example.quang.chatwithstranger.model.ListFriend;
import com.example.quang.chatwithstranger.model.Prefs;
import com.example.quang.chatwithstranger.model.User;
import com.example.quang.chatwithstranger.singleton.Singleton;
import com.github.nkzawa.emitter.Emitter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ChatFragment extends Fragment implements AdapterView.OnItemClickListener {

    Emitter.Listener onResultConversations;
    Emitter.Listener onResultGuestConversation;
    {
        onResultConversations = new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                resultConversations(args[0]);
            }
        };

        onResultGuestConversation = new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                resultGuestConversation(args[0]);
            }
        };
    }

    private void resultGuestConversation(final Object arg) {
        Handler handler = new Handler(mainActivity.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
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
                        arrUser.add(u);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void resultConversations(final Object arg) {
        mainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                arrConversation.clear();
                JSONArray data = (JSONArray) arg;
                for (int i=0; i<data.length();i++){
                    try {
                        JSONObject object = data.getJSONObject(i);
                        int id = object.getInt("IDCONVERSATION");
                        String title = object.getString("TITLE");
                        String created_at = object.getString("CREATED_AT");
                        int u = object.getInt("USER");
                        int guest = object.getInt("GUEST");

                        Conversation c = new Conversation(id,title,created_at,u,guest);
                        arrConversation.add(c);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                adapter.notifyDataSetChanged();

            }
        });
    }

    private Toolbar toolbar;
    private MainActivity mainActivity;
    private ListView lvConversation;
    private ArrayList<Conversation> arrConversation;
    private ListConversationAdapter adapter;
    private ArrayList<User> arrUser;

    private User user;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chat,container,false);
    }

    @Override
    public void onStart() {
        super.onStart();

        findID();
        initSockets();
        loadData();
        initViews();
    }

    private void findID() {
        toolbar = getActivity().findViewById(R.id.toolbarChat);
        lvConversation = getActivity().findViewById(R.id.lvConversation);

    }

    private void initSockets() {
        Singleton.Instance().getmSocket().on(Constants.SERVER_SEND_RESULT_CONVERSATIONS,onResultConversations);
        Singleton.Instance().getmSocket().on(Constants.SERVER_SEND_RESULT_GUEST_CONVERSATION,onResultGuestConversation);
    }

    private void loadData() {
        mainActivity = (MainActivity) getActivity();
        arrConversation = new ArrayList<>();
        arrUser = new ArrayList<>();

        Prefs prefs = new Prefs(mainActivity);
        user = prefs.getUser(mainActivity,Constants.KEY_USER_LOGIN);

        Singleton.Instance().getmSocket().emit(Constants.CLIENT_GET_CONVERSATIONS,user.getId());

        Singleton.Instance().getmSocket().emit(Constants.CLIENT_GET_GUEST_CONVERSATION,"");
    }

    private void initViews() {
        mainActivity.setSupportActionBar(toolbar);
        mainActivity.getSupportActionBar().setTitle(R.string.list_chat);

        adapter = new ListConversationAdapter(mainActivity,R.layout.item_listview_friends,arrConversation);
        lvConversation.setAdapter(adapter);

        lvConversation.setOnItemClickListener(this);
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        int idGuest = -1;
        if (arrConversation.get(i).getIdUser() == user.getId()){
            idGuest = arrConversation.get(i).getIdGuest();
        }else {
            idGuest = arrConversation.get(i).getIdUser();
        }

        User guest = null;
        for (User u : arrUser){
            if (u.getId() == idGuest){
                guest = u;
                break;
            }
        }
        Intent intent = new Intent(mainActivity, MessageActivity.class);
        intent.putExtra("friend",guest);
        mainActivity.overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        mainActivity.startActivity(intent);
    }
}
