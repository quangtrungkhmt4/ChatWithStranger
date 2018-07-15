package com.example.quang.chatwithstranger.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.quang.chatwithstranger.R;
import com.example.quang.chatwithstranger.consts.Constants;
import com.example.quang.chatwithstranger.model.Message;
import com.example.quang.chatwithstranger.model.Prefs;
import com.example.quang.chatwithstranger.model.User;

import java.util.ArrayList;

public class ListMessageAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private Context context;
    private ArrayList<Message> arrItem;
    private int idUser;

    public ListMessageAdapter(Context context, ArrayList<Message> arrItem,int idUser) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.arrItem = arrItem;
        this.idUser = idUser;
    }

    private class ViewHolder{
        TextView tvText;
        de.hdodenhof.circleimageview.CircleImageView imAvatar;
        TextView tvTime;
        ImageView imPhoto;
    }

    @Override
    public int getCount() {
        return arrItem.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Prefs prefs = new Prefs(context);
        User user = prefs.getUser(context,Constants.KEY_USER_LOGIN);

        Message message = arrItem.get(i);


        ViewHolder viewHolder;
        if (view == null)
        {
            viewHolder = new ViewHolder();
            if (message.getIdUser() == idUser)
            {
                view = inflater.inflate(R.layout.item_chat_user,viewGroup,false);
            }
            else
            {
                view = inflater.inflate(R.layout.item_chat_guest,viewGroup,false);
            }
            viewHolder.tvText = view.findViewById(R.id.tvTextItemMessage);
            viewHolder.tvTime = view.findViewById(R.id.tvTimeItemMessase);
            viewHolder.imAvatar = view.findViewById(R.id.imAvatarItemMessage);
            viewHolder.imPhoto = view.findViewById(R.id.imPhotoItemMessage);
            view.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) view.getTag();
        }
        if (message.getIdUser() == idUser)
        {
            Glide.with(context).load(Constants.PORT+user.getImage()).into(viewHolder.imAvatar);
        }
        else if (message.getIdUser() != idUser)
        {
            Glide.with(context).load(Constants.PORT+message.getAvatar()).into(viewHolder.imAvatar);
        }
        viewHolder.tvTime.setText(message.getTime());

        if (!message.getText().equalsIgnoreCase("")){
            viewHolder.tvText.setText(message.getText());
        }
//        else if (!message.getEmotion().equalsIgnoreCase("")){
//            viewHolder.tvText.setText(getEmojiByUnicode(Integer.parseInt(message.getEmotion().substring(2), 16)));
//        }
        else if (!message.getPhoto().equalsIgnoreCase("")){
            Glide.with(context).load(Constants.PORT+message.getPhoto()).into(viewHolder.imPhoto);
            viewHolder.tvText.setBackgroundColor(Color.WHITE);
        }else {
            viewHolder.imPhoto.setVisibility(View.GONE);
        }

        return view;
    }

    public String getEmojiByUnicode(int unicode){
        return new String(Character.toChars(unicode));
    }



}


