package com.teeniv.whatsapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import com.teeniv.whatsapp.Models.Users;
import com.teeniv.whatsapp.databinding.ActivitySettingsBinding;

import java.util.HashMap;
import java.util.Objects;

public class SettingsActivity extends AppCompatActivity {
    ActivitySettingsBinding binding;
    FirebaseAuth auth;
    FirebaseDatabase database;
    FirebaseStorage storage;
    ActivityResultLauncher<String> gallerylauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySettingsBinding.inflate(getLayoutInflater());
        storage = FirebaseStorage.getInstance();
        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();

        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());

        Objects.requireNonNull(getSupportActionBar()).hide();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        binding.backArrow.setOnClickListener(view -> {
            Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
            startActivity(intent);

        });

        binding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String status = binding.about.getText().toString();
                String username = binding.edtUsername.getText().toString();

                HashMap<String, Object> obj = new HashMap<>();
                obj.put("username", username);
                obj.put("status", status);

                database.getReference().child("Users").child(FirebaseAuth.getInstance().getUid())
                        .updateChildren(obj);

                Toast.makeText(SettingsActivity.this, "Profile Updated", Toast.LENGTH_SHORT).show();
            }
        });

        database.getReference().child("Users").child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()))
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Users users = snapshot.getValue(Users.class);
                        assert users != null;
                        Picasso.get()
                                .load(users.getProfilepic())
                                .placeholder(R.drawable.users_avatar_svgrepo_com)
                                .into(binding.profileImage);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
        gallerylauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> binding.profileImage.setImageURI(uri));

        database.getReference().child("Users").child(FirebaseAuth.getInstance().getUid())
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                Users users = snapshot.getValue(Users.class);
                                assert users != null;
                                Picasso.get()
                                        .load(users.getProfilepic())
                                        .placeholder(R.drawable.users_avatar_svgrepo_com)
                                        .into(binding.profileImage);

                                binding.about.setText(users.getStatus());
                                binding.edtUsername.setText(users.getUsername());
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
        binding.plus.setOnClickListener(view -> gallerylauncher.launch("image/*"));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        assert data != null;
        if (data.getData() != null) {
            Uri sfile = data.getData();
            binding.profileImage.setImageURI(sfile);

            final StorageReference reference = storage.getReference().child("Profile Pictures")
                    .child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()));

            reference.putFile(sfile)
                    .addOnSuccessListener(taskSnapshot -> reference.getDownloadUrl().addOnSuccessListener(uri -> {
                        database.getReference().child("Users").child(FirebaseAuth.getInstance().getUid())
                                .child("Profile Pic").setValue(uri.toString());
                        Toast.makeText(SettingsActivity.this, "Profile Pic Updated", Toast.LENGTH_LONG).show();

                    }));
        }
    }
}


