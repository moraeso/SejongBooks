package com.example.sejongbooks.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.graphics.Point;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.ImageButton;

import com.example.sejongbooks.Fragment.BookListFragment;
import com.example.sejongbooks.Fragment.BookMapFragment;
import com.example.sejongbooks.Fragment.UserFragment;
import com.example.sejongbooks.Fragment.SettingFragment;
import com.example.sejongbooks.Helper.BackPressCloseHandler;
import com.example.sejongbooks.Helper.Constant;
import com.example.sejongbooks.Listener.AsyncCallback;
import com.example.sejongbooks.R;
import com.example.sejongbooks.ServerConnect.BookTask;
import com.example.sejongbooks.Singleton.MyInfo;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    FragmentManager fragmentManager;
    Fragment fragment;
    private int curFragment;

    ImageButton btnBookList, btnBookMap, btnUser, btnSetting;
    View selectedBookList, selectedBookMap, selectedUser, selectedSetting;

    private BackPressCloseHandler backPressCloseHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.v("text", "text");
        Constant.context = this;

        setContentView(R.layout.activity_main);

        getDisplaySize();
        initFragment();
        initView();
        initListener();

        backPressCloseHandler = new BackPressCloseHandler(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (curFragment == Constant.FRAGMENT_LIST) {
            ((BookListFragment) fragment).updateBookList();
        } else if (curFragment == Constant.FRAGMENT_USER) {
            //((UserFragment) fragment).loadUserReadData();
        }
    }

    @Override
    public void onBackPressed() {
        backPressCloseHandler.onBackPressed();
    }

    private void getDisplaySize() {
        DisplayMetrics dm = getApplicationContext().getResources().getDisplayMetrics();

        Constant.WIDTH = dm.widthPixels;
        Constant.HEIGHT = dm.heightPixels;

    }

    private void initFragment() {
        fragment = new BookListFragment();
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.main_fragment, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
        curFragment = Constant.FRAGMENT_LIST;
    }

    private void initView() {
        btnBookList = findViewById(R.id.btn_book_list);
        btnBookMap = findViewById(R.id.btn_book_map);
        btnUser = findViewById(R.id.btn_user);
        btnSetting = findViewById(R.id.btn_setting);

        selectedBookList = findViewById(R.id.view_selected_book_list);
        selectedBookMap = findViewById(R.id.view_selected_book_map);
        selectedUser = findViewById(R.id.view_selected_user);
        selectedSetting = findViewById(R.id.view_selected_setting);
    }

    private void initListener() {
        btnBookList.setOnClickListener(this);
        btnBookMap.setOnClickListener(this);
        btnUser.setOnClickListener(this);
        btnSetting.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_book_list:
                if (curFragment != Constant.FRAGMENT_LIST) {
                    curFragment = Constant.FRAGMENT_LIST;

                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.main_fragment, new BookListFragment())
                            .addToBackStack(null)
                            .detach(fragment)
                            .attach(fragment)
                            .commit();

                    selectedBookList.setVisibility(View.VISIBLE);
                    selectedBookMap.setVisibility(View.INVISIBLE);
                    selectedUser.setVisibility(View.INVISIBLE);
                    selectedSetting.setVisibility(View.INVISIBLE);
                    curFragment = Constant.FRAGMENT_LIST;
                }
                break;

            case R.id.btn_book_map:
                if (curFragment != Constant.FRAGMENT_MAP) {
                    curFragment = Constant.FRAGMENT_MAP;

                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.main_fragment, new BookMapFragment())
                            .addToBackStack(null)
                            .detach(fragment)
                            .attach(fragment)
                            .commit();

                    selectedBookList.setVisibility(View.INVISIBLE);
                    selectedBookMap.setVisibility(View.VISIBLE);
                    selectedUser.setVisibility(View.INVISIBLE);
                    selectedSetting.setVisibility(View.INVISIBLE);
                    curFragment = Constant.FRAGMENT_MAP;
                }
                break;
            case R.id.btn_user:
                if (curFragment != Constant.FRAGMENT_USER) {
                    curFragment = Constant.FRAGMENT_USER;

                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.main_fragment, new UserFragment())
                            .addToBackStack(null)
                            .detach(fragment)
                            .attach(fragment)
                            .commit();

                    selectedBookList.setVisibility(View.INVISIBLE);
                    selectedBookMap.setVisibility(View.INVISIBLE);
                    selectedUser.setVisibility(View.VISIBLE);
                    selectedSetting.setVisibility(View.INVISIBLE);
                    curFragment = Constant.FRAGMENT_USER;
                }
                break;
            case R.id.btn_setting:
                if (curFragment != Constant.FRAGMENT_SETTING) {
                    curFragment = Constant.FRAGMENT_SETTING;

                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.main_fragment, new SettingFragment())
                            .addToBackStack(null)
                            .detach(fragment)
                            .attach(fragment)
                            .commit();

                    selectedBookList.setVisibility(View.INVISIBLE);
                    selectedBookMap.setVisibility(View.INVISIBLE);
                    selectedUser.setVisibility(View.INVISIBLE);
                    selectedSetting.setVisibility(View.VISIBLE);
                    curFragment = Constant.FRAGMENT_SETTING;
                }
                break;
        }
    }
}