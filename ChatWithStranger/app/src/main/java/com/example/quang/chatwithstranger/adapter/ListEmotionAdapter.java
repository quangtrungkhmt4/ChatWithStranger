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

public class ListEmotionAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private String[] arrItem;

    public ListEmotionAdapter(Context context, int layout, String[] arrItem) {
        this.context = context;
        this.layout = layout;
        this.arrItem = arrItem;
    }

    private class ViewHolder{
        TextView icon;
    }

    @Override
    public int getCount() {
        return arrItem.length;
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
                holder.icon = viewRow.findViewById(R.id.tvEmotion);
            }

            if (viewRow != null) {
                viewRow.setTag(holder);
            }
        }
        String item = arrItem[i];
        ViewHolder holder = null;
        if (viewRow != null) {
            holder = (ViewHolder) viewRow.getTag();
        }

        holder.icon.setText(getEmojiByUnicode(Integer.parseInt(item.substring(2), 16)));


        return viewRow;
    }

    public String getEmojiByUnicode(int unicode){
        return new String(Character.toChars(unicode));
    }
}
