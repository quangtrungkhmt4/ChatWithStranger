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
import com.example.quang.chatwithstranger.model.Conversation;
import com.example.quang.chatwithstranger.model.Prefs;
import com.example.quang.chatwithstranger.model.User;

import java.util.ArrayList;

public class ListConversationAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private ArrayList<Conversation> arrItem;

    public ListConversationAdapter(Context context, int layout, ArrayList<Conversation> arrItem) {
        this.context = context;
        this.layout = layout;
        this.arrItem = arrItem;
    }

    private class ViewHolder{
        TextView tvName;
        de.hdodenhof.circleimageview.CircleImageView imAvatar;
        ImageView imIsActive;
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
        if(viewRow == null)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (inflater != null) {
                viewRow = inflater.inflate(layout,viewGroup,false);
            }

            ViewHolder holder = new ViewHolder();
            if (viewRow != null) {
                holder.tvName = viewRow.findViewById(R.id.tvNameItem);
            }
            if (viewRow != null) {
                holder.imAvatar = viewRow.findViewById(R.id.imAvatarItem);
            }
            if (viewRow != null) {
                holder.imIsActive = viewRow.findViewById(R.id.imIsActiveItem);
            }

            if (viewRow != null) {
                viewRow.setTag(holder);
            }
        }
        Conversation item = arrItem.get(i);
        ViewHolder holder = null;
        if (viewRow != null) {
            holder = (ViewHolder) viewRow.getTag();
        }

        Prefs prefs = new Prefs(context);
        User user = prefs.getUser(context,Constants.KEY_USER_LOGIN);

        String[] arrName = item.getTitle().split(",");
        if (user.getName().equalsIgnoreCase(arrName[0])){
            holder.tvName.setText(arrName[1]);
        }else {
            holder.tvName.setText(arrName[0]);
        }

        if (holder != null) {
            holder.imIsActive.setVisibility(View.INVISIBLE);
        }
        if (holder != null) {
            holder.imAvatar.setImageResource(R.drawable.ic_action_group);
        }

        return viewRow;
    }
}
