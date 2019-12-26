package com.example.sejongbooks.Activity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.UserManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sejongbooks.Helper.Constant;
import com.example.sejongbooks.Listener.AsyncCallback;
import com.example.sejongbooks.Popup.ConfirmDialog;
import com.example.sejongbooks.Popup.FullImagePopup;
import com.example.sejongbooks.R;
import com.example.sejongbooks.ServerConnect.ReadBookTask;
import com.example.sejongbooks.Singleton.BookManager;
import com.example.sejongbooks.Singleton.MyInfo;
import com.example.sejongbooks.VO.BookVO;

public class BookDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private View view;

    private BookVO m_book;

    private ConfirmDialog errDialog;

    private ScrollView mainScrollView;
    private ImageView transparentImageView;

    private ImageView m_iv_isRead;

    private Button readBtn;
    private Boolean isRead;

    private Button problemBtn;

    private ImageView m_iv_bookThumbnail;
    private ImageButton closeButton;

    private Button reviewWriteButton;
    private Button reviewEnterButton;

    public static final int PRESSED_ON = 1;
    public static final int PRESSED_OFF = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_book_detail);

        initActivity();
    }

    public void initActivity() {
        m_book = BookManager.getInstance().getBookDataFromID
                (Integer.parseInt(getIntent().getStringExtra("BookID")));

        initView();
        initListener();

        transparentImageView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        // Disallow ScrollView to intercept touch events.
                        mainScrollView.requestDisallowInterceptTouchEvent(true);
                        // Disable touch on transparent view
                        return false;

                    case MotionEvent.ACTION_UP:
                        // Allow ScrollView to intercept touch events.
                        mainScrollView.requestDisallowInterceptTouchEvent(false);
                        return true;

                    case MotionEvent.ACTION_MOVE:
                        mainScrollView.requestDisallowInterceptTouchEvent(true);
                        return false;

                    default:
                        return true;
                }
            }
        });
    }

    private void initView() {
        //Log.d("mmee:initActivityWidget","Thumbnail : " + m_book.getThumbnail());
        m_iv_bookThumbnail = (ImageView) this.findViewById(R.id.iv_bookThumbnail);
        m_iv_bookThumbnail.setImageBitmap(m_book.getImage());

        TextView m_tv_bookName = (TextView) this.findViewById(R.id.tv_bookName);
        m_tv_bookName.setText(m_book.getName());

        TextView m_tv_bookAuthor = (TextView) this.findViewById(R.id.tv_bookAuthor);
        m_tv_bookAuthor.setText(m_book.getAuthor());

        TextView m_tv_bookDistance = (TextView) this.findViewById(R.id.tv_bookPublisher);
        m_tv_bookDistance.setText(m_book.getPublisher());

        TextView m_tv_bookType = (TextView) this.findViewById(R.id.tv_bookType);
        m_tv_bookType.setText(m_book.getType());

        TextView m_tv_bookPage = (TextView) this.findViewById(R.id.tv_bookPage);
        m_tv_bookPage.setText(Integer.toString(m_book.getPage()) + "쪽");

        TextView m_tv_bookGrade = (TextView) this.findViewById(R.id.txt_book_grade);
        m_tv_bookGrade.setText(Float.toString(m_book.getGrade()));

        TextView m_tv_bookIntro = (TextView) this.findViewById(R.id.tv_bookIntro);
        m_tv_bookIntro.setText(m_book.getIntro());

        m_iv_isRead = (ImageView) this.findViewById(R.id.img_isRead);

        // rattingBar
        RatingBar rb_bookGrade = (RatingBar) this.findViewById(R.id.rb_book_grade);
        rb_bookGrade.setRating(m_book.getGrade());

        // close 버튼
        closeButton = (ImageButton) this.findViewById(R.id.btn_close);

        errDialog = new ConfirmDialog(this);

        readBtn = (Button) this.findViewById(R.id.btn_read);
        if (!m_book.isRead()) {
            m_iv_isRead.setVisibility(View.INVISIBLE);
            isRead = false;
            readBtn.setBackground(getResources().getDrawable(R.drawable.box_round_main));
        } else {
            m_iv_isRead.setVisibility(View.VISIBLE);
            isRead = true;
            readBtn.setBackground(getResources().getDrawable(R.drawable.box_round_main_full));
        }
        readBtn.setText("인증 : " + m_book.getCount());

        problemBtn = (Button) this.findViewById(R.id.btn_problem);
        if (m_book.getProblem() == 0) {
            problemBtn.setEnabled(false);
            problemBtn.setBackground(getResources().getDrawable(R.drawable.box_round_main));
        } else {
            problemBtn.setBackground(getResources().getDrawable(R.drawable.box_round_main_full));
        }

        reviewWriteButton = (Button) this.findViewById(R.id.btn_writeReview);
        reviewEnterButton = (Button) this.findViewById(R.id.btn_enterReview);

        mainScrollView = (ScrollView) findViewById(R.id.main_scrollview);
        transparentImageView = (ImageView) findViewById(R.id.transparent_image);
    }

    private void initListener() {
        readBtn.setOnClickListener(this);
        problemBtn.setOnClickListener(this);
        m_iv_bookThumbnail.setOnClickListener(this);
        closeButton.setOnClickListener(this);
        reviewWriteButton.setOnClickListener(this);
        reviewEnterButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_read:
                if (isRead) {
                    m_iv_isRead.setVisibility(View.INVISIBLE);
                    readBtn.setBackground(getResources().getDrawable(R.drawable.box_round_main));
                    isRead = false;
                    pushReadBtnTask(PRESSED_OFF);
                    readBtn.setEnabled(false);
                } else {
                    m_iv_isRead.setVisibility(View.VISIBLE);
                    readBtn.setBackground(getResources().getDrawable(R.drawable.box_round_main_full));
                    isRead = true;
                    pushReadBtnTask(PRESSED_ON);
                    readBtn.setEnabled(false);
                }
                break;

            case R.id.btn_problem:
                Intent intent = new Intent(v.getContext(), ProblemActivity.class);
                intent.putExtra("BookID", Integer.toString(m_book.getID()));

                startActivity(intent);
                break;

            case R.id.iv_bookThumbnail:
                FullImagePopup fullImagePopup = new FullImagePopup(BookDetailActivity.this, m_book.getImage());
                fullImagePopup.show();
                break;

            case R.id.btn_close:
                finish();
                break;

            case R.id.btn_writeReview:
                Intent intent2 = new Intent(v.getContext(), ReviewWriteActivity.class);
                Log.d("bookID", "" + m_book.getID());
                intent2.putExtra("bookID", "" + m_book.getID());
                startActivity(intent2);
                break;
            case R.id.btn_enterReview:
                Intent intent3 = new Intent(v.getContext(),ReviewActivity.class);
                Log.d("bookID",""+m_book.getID());
                intent3.putExtra("bookID",""+m_book.getID());
                startActivity(intent3);
                overridePendingTransition(R.anim.anim_slide_in_bottom,R.anim.anim_slide_out_top);
                break;
        }
    }

    private void pushReadBtnTask(int pushFlag) {

        ContentValues values = new ContentValues();
        values.put("userID", MyInfo.getInstance().getUser().getID());
        values.put("bookID", m_book.getID());
        values.put("isInsert", pushFlag);

        // 로그인 URL 설정
        String url = Constant.URL + "/user/read";

        ReadBookTask readBookTask = new ReadBookTask(url, values, new AsyncCallback() {
            @Override
            public void onSuccess(Object object) {
                readBtn.setEnabled(true);
                readBtn.setText("인증 : " + m_book.getCount());
            }

            @Override
            public void onFailure(Exception e) {
                readBtn.setEnabled(true);
                readBtn.setText("인증 : " + m_book.getCount());
            }
        });
        readBookTask.execute();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();
    }
}
