package com.teeniv.whatsapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.FirebaseDatabase;
import com.teeniv.whatsapp.Models.Users;
import com.teeniv.whatsapp.databinding.ActivitySignInBinding;

public class SignIn_Activity extends AppCompatActivity {
    ActivitySignInBinding binding;
    ProgressDialog progressDialog;
    FirebaseAuth auth;
    GoogleSignInClient mGoogleSignInClient;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Enable edge-to-edge layout
        EdgeToEdge.enable(this);

        // Set padding to handle system bars
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Hide action bar
        getSupportActionBar().hide();

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance();

        // Initialize Firebase Database
        database = FirebaseDatabase.getInstance();

        // Initialize progress dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Login");
        progressDialog.setMessage("Logging in to your account");

        // Configure Google Sign-In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // Set click listeners
        binding.btnSignIn.setOnClickListener(view -> signInWithEmail());
        binding.btnGoogle.setOnClickListener(view -> signInWithGoogle());
        binding.txtSignup.setOnClickListener(view -> navigateToSignUp());

        // Automatically sign in if user is already authenticated
        if (auth.getCurrentUser() != null) {
            navigateToMain();
        }
    }

    private void signInWithEmail() {
        if(binding.edtEmail.getText().toString().isEmpty())
        {
            binding.edtEmail.setError("Enter your email");
            return;
        }

        if(binding.edtPassword.getText().toString().isEmpty())
        {
            binding.edtPassword.setError("Enter your password");
            return;
        }


        String email = binding.edtEmail.getText().toString();
        String password = binding.edtPassword.getText().toString();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.show();
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    progressDialog.dismiss();
                    if (task.isSuccessful()) {
                        navigateToMain();
                    } else {
                        Toast.makeText(SignIn_Activity.this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void signInWithGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void navigateToMain() {
        Intent intent = new Intent(SignIn_Activity.this, MainActivity.class);
        startActivity(intent);
        finish(); // Finish this activity to prevent going back to sign-in screen
    }

    private void navigateToSignUp() {
        Intent intent = new Intent(SignIn_Activity.this, SignUp_Activity.class);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                Log.w(TAG, "Google sign in failed", e);
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = auth.getCurrentUser();

                        Users users = new Users();
                        users.setUserid(user.getUid());
                        users.setUsername(user.getDisplayName());
                        users.setProfilepic(user.getPhotoUrl().toString());
                        database.getReference().child("Users").child(user.getUid()).setValue(users);

                        navigateToMain();

                        Toast.makeText(this, "Sign in with Google", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(SignIn_Activity.this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private static final int RC_SIGN_IN = 123;
    private static final String TAG = "SignIn_Activity";
}
