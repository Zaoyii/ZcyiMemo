package com.zcyi.rorschach;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.zcyi.rorschach.Pager.Fragment.AlarmMePagerFragment;
import com.zcyi.rorschach.Pager.Fragment.MemoPagerFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener  {

    private ViewPager viewPager;
    boolean isExit = false;

    Handler mHandler = new Handler(message -> {
        isExit = false;
        return false;
    });
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }
    private void init() {

        List<Fragment> list = new ArrayList<>();
        list.add(new MemoPagerFragment());
        list.add(new AlarmMePagerFragment());

        BottomNavigationView navigationView = findViewById(R.id.MainBNV);
        viewPager = findViewById(R.id.MainPager);
        navigationView.setOnNavigationItemSelectedListener(this);
        navigationView.setSelectedItemId(R.id.memo);
        myViewpagerFragment fragment = new myViewpagerFragment(getSupportFragmentManager(), 0, list);
        viewPager.setAdapter(fragment);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @SuppressLint("NonConstantResourceId")
            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        navigationView.setSelectedItemId(R.id.memo);
                        break;
                    case 1:
                        navigationView.setSelectedItemId(R.id.alarmClock);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.memo:
                viewPager.setCurrentItem(0);
                return true;
            case R.id.alarmClock:
                viewPager.setCurrentItem(1);
                return true;

        }
        return true;
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void exit() {
        if (!isExit) {
            isExit = true;
            Toast.makeText(getApplicationContext(), "再按就退出了~~~", Toast.LENGTH_SHORT).show();
            mHandler.sendEmptyMessageDelayed(0, 2000);
        } else {
            finish();
            System.exit(0);
        }
    }

    public static class myViewpagerFragment extends FragmentPagerAdapter {
        List<Fragment> list;

        public myViewpagerFragment(@NonNull FragmentManager fm, int behavior, List<Fragment> list) {
            super(fm, behavior);
            this.list = list;
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return list.get(position);
        }

        @Override
        public int getCount() {
            return list.size();
        }
    }
}