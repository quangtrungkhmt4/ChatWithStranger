package com.example.quang.chatwithstranger;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.quang.chatwithstranger.consts.Constants;
import com.example.quang.chatwithstranger.fragments.ChatFragment;
import com.example.quang.chatwithstranger.fragments.ListFriendFragment;
import com.example.quang.chatwithstranger.fragments.ProfileFragment;
import com.example.quang.chatwithstranger.model.Conversation;
import com.example.quang.chatwithstranger.model.JsonConversation;
import com.example.quang.chatwithstranger.model.Prefs;
import com.example.quang.chatwithstranger.model.User;
import com.example.quang.chatwithstranger.singleton.Singleton;
import com.github.nkzawa.emitter.Emitter;

public class MainActivity extends AppCompatActivity {

    Emitter.Listener onResultLogout;
    {
        onResultLogout = new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                resultLogout(args[0]);
            }
        };
    }

    private void resultLogout(final Object arg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String result = (String) arg;
                if (result.equalsIgnoreCase("true")){
                    Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                    startActivity(intent);
                    // trong anim ta tạo hai file rồi gọi ở đây để tạo animation khi chuyển activity
                    overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                    finish();
                }
            }
        });
    }

    private BottomNavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findID();
        initSockets();
        initViews();
    }

    private void initSockets() {
        Singleton.Instance().getmSocket().on(Constants.SERVER_SEND_RESULT_LOGOUT,onResultLogout);
    }

    private void findID() {
        navigationView = findViewById(R.id.navigation);

    }

    private void initViews() {
        navigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        loadFragment(new ProfileFragment());
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.action_info:
                    fragment = new ProfileFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.action_friend:
                    fragment = new ListFriendFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.action_chat:
                    fragment = new ChatFragment();
                    loadFragment(fragment);
                    return true;
            }
            return false;
        }
    };

    private void loadFragment(Fragment fragment) {
        // load fragment
        if (getSupportFragmentManager().getBackStackEntryCount() > 0){
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction trans = manager.beginTransaction();
            trans.remove(manager.getFragments().get(manager.getFragments().size() - 1));
            trans.commit();
            manager.popBackStack();
        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frameGroup, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void logout(int id){
        Singleton.Instance().getmSocket().emit(Constants.LOGOUT,id);
        Prefs prefs = new Prefs(this);
        prefs.deleteUser(this,Constants.KEY_USER_LOGIN);
    }

    public Context getAppContext(){
        return getApplicationContext();
    }

    public void switchMessActivity(Conversation conversation){
        Intent intent = new Intent(this,MessageActivity.class);
        intent.putExtra("conversation",conversation);
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        startActivity(intent);
    }
}
