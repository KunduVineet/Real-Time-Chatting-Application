package com.teeniv.whatsapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.teeniv.whatsapp.Models.Users;
import com.teeniv.whatsapp.databinding.ActivitySignUpBinding;

public class SignUp_Activity extends AppCompatActivity {

    ActivitySignUpBinding binding;
    private FirebaseAuth auth;
    FirebaseDatabase database;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        // Initialize the binding object first
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Set insets listener
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        progressDialog = new ProgressDialog(SignUp_Activity.this);
        progressDialog.setTitle("Creating Account");
        progressDialog.setMessage("We're creating your account");


        // Hide the action bar
        getSupportActionBar().hide();

        // Initialize Firebase authentication and database
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        // Set click listener for sign up button
        binding.btnSignup.setOnClickListener(view -> {
            progressDialog.show();
            auth.createUserWithEmailAndPassword(binding.edtEmail.getText().toString(), binding.edtPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    progressDialog.dismiss();
                    if (task.isSuccessful()) {
                        Users user = new Users(binding.edtUsername.getText().toString(),binding.edtEmail.getText().toString(), binding.edtPassword.getText().toString());

                        String id = task.getResult().getUser().getUid();

                        database.getReference().child("Users").child(id).setValue(user);

                        Toast.makeText(SignUp_Activity.this, "User created successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(SignUp_Activity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        });
        binding.alreadyHaveAccount.setOnClickListener(view -> {
            Intent intent = new Intent(SignUp_Activity.this, SignIn_Activity.class);
            startActivity(intent);
        });

    }

}