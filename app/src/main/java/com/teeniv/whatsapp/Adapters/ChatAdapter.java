package com.teeniv.whatsapp.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.teeniv.whatsapp.Models.Messages;
import com.teeniv.whatsapp.R;

import java.util.ArrayList;

public class ChatAdapter extends RecyclerView.Adapter{

    ArrayList<Messages> messages;
    Context context;
    int SENDER_VIEW_TYPE=1;
    int RECEIVER_VIEW_TYPE=2;
    String recId;

    public ChatAdapter(ArrayList<Messages> messages, Context context,String recId) {
        this.messages = messages;
        this.context = context;
        this.recId = recId;
    }

    public ChatAdapter(ArrayList<Messages> messages, Context context) {
        this.messages = messages;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == SENDER_VIEW_TYPE)
        {
            View view = LayoutInflater.from(context).inflate(R.layout.sample_sender,parent,false);
            return new SenderViewHolder(view);
        }   else {
            View view = LayoutInflater.from(context).inflate(R.layout.sample_receiver,parent,false);
            return new ReceiverViewHolder(view);
        }

    }

    @Override
    public int getItemViewType(int position) {
        if(messages.get(position).getuId().equals(FirebaseAuth.getInstance().getUid())) {
            return SENDER_VIEW_TYPE;
        }   else {
            return RECEIVER_VIEW_TYPE;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
    Messages messages1 = messages.get(position);

    holder.itemView.setOnLongClickListener(view -> {
        new AlertDialog.Builder(context)
                .setTitle("Delete")
                .setMessage("Are you sure you want to delete this Message")
                .setPositiveButton("Yes", (dialogInterface, i) -> {
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    String sender = FirebaseAuth.getInstance().getUid() + recId;
                    database.getReference().child("Chats").child(sender)
                            .child(messages1.getMessageId())
                            .setValue(null);

                }).setNegativeButton("No", (dialogInterface, i) -> dialogInterface.dismiss()).show();
        return false;
    });

    if(holder.getClass() == SenderViewHolder.class)
        {
            ((SenderViewHolder)holder).senderMsg.setText(messages1.getMessage());
        }   else {
            ((ReceiverViewHolder)holder).receiverMsg.setText(messages1.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public static class ReceiverViewHolder extends RecyclerView.ViewHolder {
        TextView receiverMsg,receiverTime;
        public ReceiverViewHolder(@NonNull View itemView) {
            super(itemView);

            receiverMsg = itemView.findViewById(R.id.receiver_text);
            receiverTime = itemView.findViewById(R.id.receiver_time);
        }
    }

    public static class SenderViewHolder extends RecyclerView.ViewHolder {
        TextView senderMsg,senderTime;
        public SenderViewHolder(@NonNull View itemView) {
            super(itemView);
            senderMsg = itemView.findViewById(R.id.SenderText);
            senderTime = itemView.findViewById(R.id.SenderTime);
        }
    }
}
