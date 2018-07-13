package com.example.quang.chatwithstranger.adapter;

import android.content.Context;
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
    private Context context;
    private int layout_left;
    private int layout_right;
    private ArrayList<Message> arrItem;
    private String avatarGuest;

    public ListMessageAdapter(Context context, int layout, int layout_right, ArrayList<Message> arrItem, String avatarGuest) {
        this.context = context;
        this.layout_left = layout;
        this.layout_right = layout_right;
        this.arrItem = arrItem;
        this.avatarGuest = avatarGuest;
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
        View viewRow = view;
        Prefs prefs = new Prefs(context);
        User user = prefs.getUser(context,Constants.KEY_USER_LOGIN);

        Message message = arrItem.get(i);

        if (message.getIdUser() == user.getId()){

            if(viewRow == null)
            {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                if (inflater != null) {
                    viewRow = inflater.inflate(layout_right,viewGroup,false);
                }

                ViewHolder holder = new ViewHolder();
                if (viewRow != null) {
                    holder.tvText = viewRow.findViewById(R.id.tvTextItemMessage);
                }
                if (viewRow != null) {
                    holder.imAvatar = viewRow.findViewById(R.id.imAvatarItemMessage);
                }
                if (viewRow != null) {
                    holder.tvTime = viewRow.findViewById(R.id.tvTimeItemMessase);
                }
                if (viewRow != null) {
                    holder.imPhoto = viewRow.findViewById(R.id.imPhotoItemMessage);
                }

                if (viewRow != null) {
                    viewRow.setTag(holder);
                }
            }
            ViewHolder holder = null;
            if (viewRow != null) {
                holder = (ViewHolder) viewRow.getTag();
            }

            holder.tvText.setText(message.getText());
            holder.tvTime.setText(message.getTime());

            if (!message.getPhoto().equalsIgnoreCase("")){
                Glide.with(context).load(Constants.PORT+message.getPhoto()).into(holder.imPhoto);
            }
            if (holder != null) {
                Glide.with(context).load(Constants.PORT+user.getImage()).into(holder.imAvatar);
            }

        }else {
            if(viewRow == null)
            {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                if (inflater != null) {
                    viewRow = inflater.inflate(layout_left,viewGroup,false);
                }

                ViewHolder holder = new ViewHolder();
                if (viewRow != null) {
                    holder.tvText = viewRow.findViewById(R.id.tvTextItemMessage);
                }
                if (viewRow != null) {
                    holder.imAvatar = viewRow.findViewById(R.id.imAvatarItemMessage);
                }
                if (viewRow != null) {
                    holder.tvTime = viewRow.findViewById(R.id.tvTimeItemMessase);
                }
                if (viewRow != null) {
                    holder.imPhoto = viewRow.findViewById(R.id.imPhotoItemMessage);
                }

                if (viewRow != null) {
                    viewRow.setTag(holder);
                }
            }
            ViewHolder holder = null;
            if (viewRow != null) {
                holder = (ViewHolder) viewRow.getTag();
            }

            holder.tvText.setText(message.getText());
            holder.tvTime.setText(message.getTime());

            if (!message.getPhoto().equalsIgnoreCase("")){
                Glide.with(context).load(Constants.PORT+message.getPhoto()).into(holder.imPhoto);
            }
            if (holder != null) {
                Glide.with(context).load(Constants.PORT+avatarGuest).into(holder.imAvatar);
            }
        }



        return viewRow;
    }
}
