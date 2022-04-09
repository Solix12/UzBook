package com.salikh.uzboock.activity;

import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.salikh.uzboock.R;
import com.salikh.uzboock.databinding.ActivityMainBinding;
import com.salikh.uzboock.fragment.AddPostFragment;
import com.salikh.uzboock.fragment.HomeFragment;
import com.salikh.uzboock.fragment.Notification2Fragment;
import com.salikh.uzboock.fragment.ProfileFragment;
import com.salikh.uzboock.fragment.SearchFragment;

import me.ibrahimsn.lib.OnItemSelectedListener;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        setNavBar();
        setBars();
        setFragmentMain();
    }

    private void setNavBar() {

        binding.bottomBar.setItemIconSize(70);
        binding.bottomBar.setItemIconTint(Color.parseColor("#000000"));
        binding.bottomBar.setItemIconTintActive(Color.parseColor("#000000"));
        binding.bottomBar.setBarIndicatorColor(Color.parseColor("#FFDADADA"));
        binding.bottomBar.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public boolean onItemSelect(int i) {

                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();


                switch (i) {
                    case 0: {
                        transaction.replace(R.id.container, new HomeFragment());
                        break;
                    }
                    case 1: {
                        transaction.replace(R.id.container, new Notification2Fragment());
                        break;
                    }
                    case 2: {
                        transaction.replace(R.id.container, new AddPostFragment());
                        break;
                    }
                    case 3: {
                        transaction.replace(R.id.container, new SearchFragment());
                        break;
                    }
                    case 4: {
                        transaction.replace(R.id.container, new ProfileFragment());
                        break;
                    }
                }
                transaction.commit();
                return true;
            }
        });


    }

    private void setFragmentMain() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, new HomeFragment());
        transaction.commit();
    }

    private void setBars() {
        getWindow().setStatusBarColor(getResources().getColor(R.color.white));
        getWindow().setNavigationBarColor(getResources().getColor(R.color.white));
    }
}