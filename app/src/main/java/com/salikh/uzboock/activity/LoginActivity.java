package com.salikh.uzboock.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.salikh.uzboock.R;
import com.salikh.uzboock.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private FirebaseAuth auth;
    private String email, pass;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setBars();

        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();

        setListener();
    }

    private void setListener() {
        binding.goSing.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, SingUpActivity.class);
            startActivity(intent);
            finish();
        });


        binding.button.setOnClickListener(view -> {
            email = binding.inputEmail.getText().toString();
            pass = binding.inputPassword.getText().toString();

            binding.button.setVisibility(View.INVISIBLE);
            binding.progressBar.setVisibility(View.VISIBLE);

            if (!email.isEmpty() && !pass.isEmpty()) {
                auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            binding.button.setVisibility(View.VISIBLE);
                            binding.progressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(LoginActivity.this, "Invalid email or password", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            } else {
                binding.button.setVisibility(View.VISIBLE);
                binding.progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(this, "Reference is not enough", Toast.LENGTH_SHORT).show();
            }

        });
    }

    private void setBars() {
        getWindow().setStatusBarColor(getResources().getColor(R.color.white));
        getWindow().setNavigationBarColor(getResources().getColor(R.color.white));
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (currentUser != null) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
}