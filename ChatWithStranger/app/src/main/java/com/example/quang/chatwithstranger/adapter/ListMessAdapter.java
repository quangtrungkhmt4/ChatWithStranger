package com.example.quang.chatwithstranger.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.quang.chatwithstranger.R;
import com.example.quang.chatwithstranger.consts.Constants;
import com.example.quang.chatwithstranger.model.Message;
import com.example.quang.chatwithstranger.utils.Utils;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ListMessAdapter extends RecyclerView.Adapter {
    private static final int VIEW_TYPE_MESSAGE_SENT = 1;
    private static final int VIEW_TYPE_MESSAGE_RECEIVED = 2;

    private Context mContext;
    private List<Message> mMessageList;
    private int currentUser;
    private static ClickListener clickListener;

    public ListMessAdapter(Context context, List<Message> messageList, int currentUser) {
        mContext = context;
        mMessageList = messageList;
        this.currentUser = currentUser;
    }

    @Override
    public int getItemCount() {
        return mMessageList.size();
    }

    // Determines the appropriate ViewType according to the sender of the message.
    @Override
    public int getItemViewType(int position) {
        Message message = mMessageList.get(position);

        if (message.getIdUser() == currentUser) {
            // If the current user is the sender of the message
            return VIEW_TYPE_MESSAGE_SENT;
        } else {
            // If some other user sent the message
            return VIEW_TYPE_MESSAGE_RECEIVED;
        }
    }

    // Inflates the appropriate layout according to the ViewType.
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;

        if (viewType == VIEW_TYPE_MESSAGE_SENT) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_chat_user, parent, false);
            return new SentMessageHolder(view);
        } else if (viewType == VIEW_TYPE_MESSAGE_RECEIVED) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_chat_guest, parent, false);
            return new ReceivedMessageHolder(view);
        }

        return null;
    }

    // Passes the message object to a ViewHolder so that the contents can be bound to UI.
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Message message =  mMessageList.get(position);

        switch (holder.getItemViewType()) {
            case VIEW_TYPE_MESSAGE_SENT:
                ((SentMessageHolder) holder).bind(message);
                break;
            case VIEW_TYPE_MESSAGE_RECEIVED:
                ((ReceivedMessageHolder) holder).bind(message);
        }
    }

    private class SentMessageHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView messageText, timeText;
        CircleImageView avatar;
        ImageView imageView;

        SentMessageHolder(View itemView) {
            super(itemView);

            messageText = (TextView) itemView.findViewById(R.id.tvTextItemMessage);
            timeText = (TextView) itemView.findViewById(R.id.tvTimeItemMessase);
            avatar = itemView.findViewById(R.id.imAvatarItemMessage);
            imageView = itemView.findViewById(R.id.imPhotoItemMessage);
            itemView.setOnClickListener(this);
        }

        void bind(Message message) {

            // Format the stored timestamp into a readable String using method.
            timeText.setText(message.getTime());
            Glide.with(mContext).load(Constants.PORT+message.getAvatar()).into(avatar);

            if (!message.getPhoto().equalsIgnoreCase("")){
                imageView.setVisibility(View.VISIBLE);
                Glide.with(mContext).load(Constants.PORT+message.getPhoto()).into(imageView);
                messageText.setVisibility(View.GONE);
            }else{
                String txt = "";
                if (message.getText().contains("0x1F")){
                    String[] arr = message.getText().split(" ");
                    for (int i=0; i<arr.length;i++){
                        if (arr[i].contains("0x1F")){
                            int u = Integer.parseInt(arr[i].substring(2), 16);
                            txt = txt + " " + getEmojiByUnicode(u);
                        }else {
                            txt = txt + " " + arr[i];
                        }
                    }
                    messageText.setText(txt);
                }else {
                    messageText.setText(message.getText());
                }
                messageText.setVisibility(View.VISIBLE);
                imageView.setVisibility(View.GONE);
            }
        }

        public String getEmojiByUnicode(int unicode){
            return new String(Character.toChars(unicode));
        }

        @Override
        public void onClick(View v) {
            clickListener.onItemClick(getAdapterPosition(), v);
        }
    }

    private class ReceivedMessageHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView messageText, timeText;
        CircleImageView profileImage;
        ImageView imPhoto;

        ReceivedMessageHolder(View itemView) {
            super(itemView);

            messageText = (TextView) itemView.findViewById(R.id.tvTextItemMessage);
            timeText = (TextView) itemView.findViewById(R.id.tvTimeItemMessase);
            profileImage =  itemView.findViewById(R.id.imAvatarItemMessage);
            imPhoto =  itemView.findViewById(R.id.imPhotoItemMessage);
            itemView.setOnClickListener(this);
        }

        void bind(Message message) {


            // Format the stored timestamp into a readable String using method.
            timeText.setText(message.getTime());

            // Insert the profile image from the URL into the ImageView.
            Glide.with(mContext).load(Constants.PORT+message.getAvatar()).into(profileImage);
            if (!message.getPhoto().equalsIgnoreCase("")){
                imPhoto.setVisibility(View.VISIBLE);
                Glide.with(mContext).load(Constants.PORT+message.getPhoto()).into(imPhoto);
                messageText.setVisibility(View.GONE);
            }else{
                String txt = "";
                if (message.getText().contains("0x1F")){
                    String[] arr = message.getText().split(" ");
                    for (int i=0; i<arr.length;i++){
                        if (arr[i].contains("0x1F")){
                            int u = Integer.parseInt(arr[i].substring(2), 16);
                            txt = txt + " " + getEmojiByUnicode(u);
                        }else {
                            txt = txt + " " + arr[i];
                        }
                    }
                    messageText.setText(txt);
                }else {
                    messageText.setText(message.getText());
                }
                messageText.setVisibility(View.VISIBLE);
                imPhoto.setVisibility(View.GONE);
            }
        }

        public String getEmojiByUnicode(int unicode){
            return new String(Character.toChars(unicode));
        }

        @Override
        public void onClick(View v) {
            clickListener.onItemClick(getAdapterPosition(), v);
        }
    }

    public void setOnItemClickListener(ClickListener clickListener) {
        ListMessAdapter.clickListener = clickListener;
    }


    public interface ClickListener {
        void onItemClick(int position, View v);
        void onItemLongClick(int position, View v);
    }
}