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
import com.example.quang.chatwithstranger.model.User;

import java.util.ArrayList;

public class ListFriendsAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private ArrayList<User> arrItem;

    public ListFriendsAdapter(Context context, int layout, ArrayList<User> arrItem) {
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
        User item = arrItem.get(i);
        ViewHolder holder = null;
        if (viewRow != null) {
            holder = (ViewHolder) viewRow.getTag();
        }

        holder.tvName.setText(item.getName());

        if (holder != null) {
            if (item.getIsActive() == 1){
                holder.imIsActive.setImageResource(R.drawable.ic_on_20dp);
            }else {
                holder.imIsActive.setImageResource(R.drawable.ic_off_20dp);
            }
        }
        if (holder != null) {
            Glide.with(context).load(Constants.PORT+item.getImage()).into(holder.imAvatar);
        }

        return viewRow;
    }
}
