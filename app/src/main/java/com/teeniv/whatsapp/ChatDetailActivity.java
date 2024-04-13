package com.teeniv.whatsapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.teeniv.whatsapp.Adapters.ChatAdapter;
import com.teeniv.whatsapp.Models.Messages;
import com.teeniv.whatsapp.databinding.ActivityChatDetailBinding;

import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class ChatDetailActivity extends AppCompatActivity {

    ActivityChatDetailBinding binding;
    FirebaseDatabase database;
    FirebaseAuth auth;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityChatDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Objects.requireNonNull(getSupportActionBar()).hide();
        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        final String senderId = auth.getUid();
        String receiveId = getIntent().getStringExtra("UserId");
        String username = getIntent().getStringExtra("Username");
        String ProfliePic = getIntent().getStringExtra("ProfilePic");

        binding.username.setText(username);
        Picasso.get().load(ProfliePic).placeholder(R.drawable.avatar).into(binding.profileImage);

        binding.backArrow.setOnClickListener(view -> {
            Intent intent = new Intent(ChatDetailActivity.this, MainActivity.class);
            startActivity(intent);
        });

        final ArrayList<Messages> messages = new ArrayList<>();
        final ChatAdapter chatAdapter = new ChatAdapter(messages, this, receiveId);
        binding.chatRecyclerView.setAdapter(chatAdapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.chatRecyclerView.setLayoutManager(layoutManager);

        final String senderRoom = senderId + receiveId;
        final String receiverRoom = receiveId + senderId;

        database.getReference().child("Chats")
                .child(senderRoom)
                .addValueEventListener(new ValueEventListener() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        messages.clear();
                        for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                            Messages messages1 = snapshot1.getValue(Messages.class);
                            assert messages1 != null;
                            messages1.setMessageId(snapshot1.getKey());
                            messages.add(messages1);
                        }
                        chatAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        binding.send.setOnClickListener(view -> {
            String messag = binding.etMessage.getText().toString();
            final Messages messages1 = new Messages(senderId, messag);
            messages1.setTimestamp(new Date().getTime());
            binding.etMessage.setText(" ");

            if(binding.etMessage.getText().toString().isEmpty())
            {
                binding.etMessage.setError("Enter Text");
                return;
            }
            database.getReference().child("Chats")
                    .child(senderRoom)
                    .push()
                    .setValue(messages1).addOnSuccessListener(unused -> database.getReference().child("Chats")
                            .child(receiverRoom)
                            .push()
                            .setValue(messages1).addOnSuccessListener(unused1 -> {

                            }));
        });
    }
}