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
import com.google.firebase.database.FirebaseDatabase;
import com.salikh.uzboock.R;
import com.salikh.uzboock.databinding.ActivitySingUpBinding;
import com.salikh.uzboock.model.User;

public class SingUpActivity extends AppCompatActivity {

    private ActivitySingUpBinding binding;
    private FirebaseAuth auth;
    private FirebaseDatabase database;
    private String profess, pass, name, email;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySingUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setBars();

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        setListener();
    }

    private void setListener() {
        binding.goLog.setOnClickListener(view -> {
            Intent intent = new Intent(SingUpActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });

        binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                email = binding.inputEmail.getText().toString();
                pass = binding.inputPassword.getText().toString();
                name = binding.inputName.getText().toString();
                profess = binding.inputProfession.getText().toString();

                binding.button.setVisibility(View.INVISIBLE);
                binding.progressBar.setVisibility(View.VISIBLE);

                if (!email.isEmpty() && !pass.isEmpty() && !name.isEmpty() && !profess.isEmpty()) {
                    auth.createUserWithEmailAndPassword(email, pass)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        User user = new User(name, profess, email, pass);
                                        String id = task.getResult().getUser().getUid();
                                        database.getReference().child("Users").child(id).setValue(user);
                                        Toast.makeText(SingUpActivity.this, "User Data saved", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(SingUpActivity.this, MainActivity.class);
                                        startActivity(intent);
                                        finish();

                                    } else {
                                        Toast.makeText(SingUpActivity.this, "Error", Toast.LENGTH_SHORT).show();
                                        binding.button.setVisibility(View.VISIBLE);
                                        binding.progressBar.setVisibility(View.INVISIBLE);
                                    }

                                }
                            });
                } else {
                    binding.button.setVisibility(View.VISIBLE);
                    binding.progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(SingUpActivity.this, "Reference is not enough", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }


    private void setBars() {
        getWindow().setStatusBarColor(getResources().getColor(R.color.white));
        getWindow().setNavigationBarColor(getResources().getColor(R.color.white));
    }
}