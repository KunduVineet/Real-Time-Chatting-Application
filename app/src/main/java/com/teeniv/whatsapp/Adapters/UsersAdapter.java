package com.teeniv.whatsapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.teeniv.whatsapp.ChatDetailActivity;
import com.teeniv.whatsapp.Models.Users;
import com.teeniv.whatsapp.R;

import java.util.ArrayList;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.ViewHolder> {

    ArrayList<Users> list;
    Context context;

    public UsersAdapter(ArrayList<Users> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Ensure that the parent ViewGroup has a valid Context
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the layout using the valid Context
        View userView = inflater.inflate(R.layout.sample_show_user, parent, false);

        // Return a new ViewHolder instance
        return new ViewHolder(userView);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Users users = list.get(position);
        Picasso.get().load(users.getProfilepic()).placeholder(R.drawable.avatar).into(holder.image);
        holder.username.setText(users.getUsername());

        FirebaseDatabase.getInstance().getReference().child("Chats")
                .child(FirebaseAuth.getInstance().getUid() + users.getUserid())
                .orderByChild("timestamp")
                .limitToLast(1)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.hasChildren()) {
                            for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                                holder.lastMessage.setText(snapshot1.child("message").getValue().toString());
                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, ChatDetailActivity.class);
            intent.putExtra("UserId", users.getUserid());
            intent.putExtra("ProfilePic", users.getProfilepic());
            intent.putExtra("Username", users.getUsername());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView username, lastMessage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.profile_image);
            username = itemView.findViewById(R.id.username);
            lastMessage = itemView.findViewById(R.id.Last_Message);
        }
    }
}
