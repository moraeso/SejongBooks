package com.example.sejongbooks.Fragment;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.sejongbooks.Activity.LikeReviewActivity;
import com.example.sejongbooks.Activity.MyReviewActivity;
import com.example.sejongbooks.Helper.Constant;
import com.example.sejongbooks.Listener.AsyncCallback;
import com.example.sejongbooks.Popup.FullImagePopup;
import com.example.sejongbooks.R;
import com.example.sejongbooks.Adapter.BookReadListRecyclerViewAdapter;

import com.example.sejongbooks.Helper.BookListRecyclerViewDecoration;

import com.example.sejongbooks.ServerConnect.BookImageTask;
import com.example.sejongbooks.ServerConnect.BookTask;
import com.example.sejongbooks.ServerConnect.PostHttpURLConnection;
import com.example.sejongbooks.ServerConnect.UserClimbedListTask;
import com.example.sejongbooks.Singleton.BookManager;
import com.example.sejongbooks.Singleton.MyInfo;
import com.example.sejongbooks.VO.BookVO;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

public class UserFragment extends Fragment implements BookReadListRecyclerViewAdapter.OnLoadMoreListener,
        SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {

    private RecyclerView m_bookRecycleView;
    private RecyclerView.LayoutManager m_layoutManager;
    private BookReadListRecyclerViewAdapter m_adapter;
    private ArrayList<BookVO> m_bufferItems; // 버퍼로 사용할 리스트

    private SwipeRefreshLayout m_swipeRefresh;

    private TextView tv_type1, tv_type2, tv_type3, tv_type4, tv_bookCount;
    private TextView tv_type1_pass, tv_type2_pass, tv_type3_pass, tv_type4_pass;

    private int m_count=0;
    private int m_totalPages=0;

    private Button btnMyReview, btnLikeReview;

    private TextView txtID, txtLevel;
    private ImageView imgProfile;

    private View vExp;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, container, false);

        m_bufferItems = new ArrayList();

        // RecycleView 생성 및 사이즈 고정
        m_bookRecycleView = (RecyclerView) view.findViewById(R.id.rv_bookList);
        m_bookRecycleView.setHasFixedSize(true);

        // Grid 레이아웃 적용
        m_layoutManager= new LinearLayoutManager(getContext());
        ((LinearLayoutManager) m_layoutManager).setOrientation(LinearLayoutManager.HORIZONTAL);

        m_bookRecycleView.setLayoutManager(m_layoutManager);
        m_bookRecycleView.addItemDecoration(new BookListRecyclerViewDecoration(getActivity()));

        // 어뎁터 연결
        m_adapter = new BookReadListRecyclerViewAdapter(getContext(), this);
        m_bookRecycleView.setAdapter(m_adapter);

        tv_bookCount = view.findViewById(R.id.tv_bookCount);
        tv_type1 = view.findViewById(R.id.tv_type1);
        tv_type2 = view.findViewById(R.id.tv_type2);
        tv_type3 = view.findViewById(R.id.tv_type3);
        tv_type4 = view.findViewById(R.id.tv_type4);
        tv_type1_pass = view.findViewById(R.id.tv_type1_pass);
        tv_type2_pass = view.findViewById(R.id.tv_type2_pass);
        tv_type3_pass = view.findViewById(R.id.tv_type3_pass);
        tv_type4_pass = view.findViewById(R.id.tv_type4_pass);

        btnMyReview = view.findViewById(R.id.btn_user_my_review);
        btnLikeReview = view.findViewById(R.id.btn_user_like_review);

        btnMyReview.setOnClickListener(this);
        btnLikeReview.setOnClickListener(this);

        imgProfile = view.findViewById(R.id.img_profile_user);
        //vExp = view.findViewById(R.id.view_exp_user);

        // 새로고침
        m_swipeRefresh = view.findViewById(R.id.swipeRefresh_bookList);
        m_swipeRefresh.setOnRefreshListener(this);

        loadUserReadData();

        Glide.with(view)
                .load("http://15011066.iptime.org:7000/user/image/"+ MyInfo.getInstance().getUser().getID() + ".jpg")
                .apply(new RequestOptions().circleCrop().centerCrop())
                .error(R.drawable.ic_user_main)
                .into(imgProfile);

        txtID.setText(MyInfo.getInstance().getUser().getID());

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadUserReadData();
    }


    /*

    final Handler handlerImg = new Handler()
    {
        public void handleMessage(Message msg)
        {
            if(userDrawable!=null) {
                imgProfile.setImageBitmap(((BitmapDrawable) userDrawable).getBitmap());
                MyInfo.getInstance().getUser().setProfile(((BitmapDrawable) userDrawable).getBitmap());
                imgProfile.setBackground(new ShapeDrawable(new OvalShape()));
                if(Build.VERSION.SDK_INT >= 21) {
                    imgProfile.setClipToOutline(true);
                }

                imgProfile.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        FullImagePopup fullImagePopup =
                                new FullImagePopup(UserFragment.super.getContext(), ((BitmapDrawable) userDrawable).getBitmap());
                        fullImagePopup.show();
                        return false;
                    }
                });

            }
        }

    };

    final Handler handlerUser = new Handler()
    {
        public void handleMessage(Message msg)
        {
            txtID.setText(userID);

            String strExp = String.valueOf((exp/1000) + 1);
            txtLevel.setText(strExp);

            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) vExp.getLayoutParams();
            int nExp = exp%1000;
            lp.width = (int)((double)Constant.WIDTH/1000 * nExp);
            vExp.setLayoutParams(lp);

            MyInfo.getInstance().getUser().setID(userID);
            MyInfo.getInstance().getUser().setPassword(userPW);
            MyInfo.getInstance().getUser().setExperience(exp);
            MyInfo.getInstance().getUser().setLevel((exp/1000) + 1);
            MyInfo.getInstance().getUser().setTotalPages(m_totalPages);
        }

    };*/

    public void loadUserReadData() {
        int totalCount = 0;
        int countType1 = 0;
        int countType2 = 0;
        int countType3 = 0;
        int countType4 = 0;
        m_totalPages = 0;

        ArrayList<BookVO> bookList = BookManager.getInstance().getItems();
        m_bufferItems.clear();
        for (int i = 0; i < bookList.size(); i++) {
            if(bookList.get(i).isRead()) {
                m_bufferItems.add(BookManager.getInstance().getItems().get(i));
                m_totalPages+=BookManager.getInstance().getItems().get(i).getPage();

                if (bookList.get(i).getType().equals("서양의 역사와 사상")) {
                    countType1++;
                } else if (bookList.get(i).getType().equals("동양의 역사와 사상")) {
                    countType2++;
                } else if (bookList.get(i).getType().equals("동서양의 문학")) {
                    countType3++;
                } else if (bookList.get(i).getType().equals("과학 사상")) {
                    countType4++;
                }
                totalCount++;
            }
        }
        m_adapter.addAll(m_bufferItems);

        tv_type1.setText(String.valueOf(countType1)+"권 / 4권");
        tv_type2.setText(String.valueOf(countType2)+"권 / 3권");
        tv_type3.setText(String.valueOf(countType3)+"권 / 2권");
        tv_type4.setText(String.valueOf(countType4)+"권 / 1권");

        if (countType1 < 4) {
            tv_type1_pass.setText("불");
            tv_type1_pass.setTextColor(ContextCompat.getColor(getContext(), R.color.colorMain));
        }
        else { tv_type1_pass.setText("합");
            tv_type1_pass.setTextColor(ContextCompat.getColor(getContext(), R.color.colorGreen)); }

        if (countType2 < 3) { tv_type2_pass.setText("불");
            tv_type2_pass.setTextColor(ContextCompat.getColor(getContext(), R.color.colorMain)); }
        else { tv_type2_pass.setText("합");
            tv_type2_pass.setTextColor(ContextCompat.getColor(getContext(), R.color.colorGreen)); }

        if (countType3 < 2) { tv_type3_pass.setText("불");
            tv_type3_pass.setTextColor(ContextCompat.getColor(getContext(), R.color.colorMain)); }
        else { tv_type3_pass.setText("합");
            tv_type3_pass.setTextColor(ContextCompat.getColor(getContext(), R.color.colorGreen)); }

        if (countType4 < 1) { tv_type4_pass.setText("불");
            tv_type4_pass.setTextColor(ContextCompat.getColor(getContext(), R.color.colorMain)); }
        else { tv_type4_pass.setText("합");
            tv_type4_pass.setTextColor(ContextCompat.getColor(getContext(), R.color.colorGreen)); }


        tv_bookCount.setText(String.valueOf(totalCount));
    }


    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                m_swipeRefresh.setRefreshing(false);

                loadUserReadData();
            }
        }, 1000);
    }

    @Override
    public void onLoadMore() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_user_my_review:
                Intent intent = new Intent(getContext(), MyReviewActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.anim_slide_in_bottom,R.anim.anim_slide_out_top);
                break;
            case R.id.btn_user_like_review:
                intent = new Intent(getContext(), LikeReviewActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.anim_slide_in_bottom,R.anim.anim_slide_out_top);
                break;
        }
    }
}
