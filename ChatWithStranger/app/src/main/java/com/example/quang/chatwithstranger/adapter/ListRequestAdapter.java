package com.example.quang.chatwithstranger.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.quang.chatwithstranger.MainActivity;
import com.example.quang.chatwithstranger.R;
import com.example.quang.chatwithstranger.RequestFriendActivity;
import com.example.quang.chatwithstranger.consts.Constants;
import com.example.quang.chatwithstranger.model.Prefs;
import com.example.quang.chatwithstranger.model.RequestFriend;
import com.example.quang.chatwithstranger.model.User;
import com.example.quang.chatwithstranger.singleton.Singleton;

import java.util.ArrayList;

public class ListRequestAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private ArrayList<RequestFriend> arrItem;

    public ListRequestAdapter(Context context, int layout, ArrayList<RequestFriend> arrItem) {
        this.context = context;
        this.layout = layout;
        this.arrItem = arrItem;
    }

    private class ViewHolder{
        TextView tvName;
        de.hdodenhof.circleimageview.CircleImageView imAvatar;
        Button btnYes;
        Button btnNo;
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
                holder.tvName = viewRow.findViewById(R.id.tvNameRequest);
            }
            if (viewRow != null) {
                holder.imAvatar = viewRow.findViewById(R.id.imAvatarRequest);
            }
            if (viewRow != null) {
                holder.btnYes = viewRow.findViewById(R.id.btnYesRequest);
            }
            if (viewRow != null) {
                holder.btnNo = viewRow.findViewById(R.id.btnNoRequest);
            }

            if (viewRow != null) {
                viewRow.setTag(holder);
            }
        }
        final RequestFriend item = arrItem.get(i);

        Prefs prefs = new Prefs(context);
        final User user = prefs.getUser(context,Constants.KEY_USER_LOGIN);
        ViewHolder holder = null;
        if (viewRow != null) {
            holder = (ViewHolder) viewRow.getTag();
        }

        holder.tvName.setText(item.getName());

        if (holder != null) {
            Glide.with(context).load(Constants.PORT+item.getImage()).into(holder.imAvatar);
        }

        holder.btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Singleton.Instance().getmSocket().emit(Constants.CLIENT_ACCEPT_REQUEST_FRIEND,item.getIdRequest()+"-"+item.getIdRequester()+"-"+user.getId());
                Intent intent = new Intent(((Activity)context),MainActivity.class);
                ((Activity)context).startActivity(intent);
                // trong anim ta tạo hai file rồi gọi ở đây để tạo animation khi chuyển activity
                ((Activity)context).overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                ((Activity)context).finish();
            }
        });

        holder.btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Singleton.Instance().getmSocket().emit(Constants.CLIENT_DELETE_REQUEST_FRIEND,item.getIdRequest()+"-"+item.getIdRequester()+"-"+user.getId());
                Intent intent = new Intent(((Activity)context),MainActivity.class);
                ((Activity)context).startActivity(intent);
                // trong anim ta tạo hai file rồi gọi ở đây để tạo animation khi chuyển activity
                ((Activity)context).overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                ((Activity)context).finish();
            }
        });

        return viewRow;
    }
}
