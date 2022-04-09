package com.salikh.uzboock.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.salikh.uzboock.R;
import com.salikh.uzboock.adapter.ViewPagerAdapter;

public class NotificationFragment extends Fragment {

    private ViewPager viewPager;
    private TabLayout tabLayout;

    public NotificationFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_notification, container, false);

        viewPager = view.findViewById(R.id.viePager);
        viewPager.setAdapter(new ViewPagerAdapter(getFragmentManager()));

        tabLayout = view.findViewById(R.id.tabLayaut);
        tabLayout.setupWithViewPager(viewPager);

        return view;
    }
}