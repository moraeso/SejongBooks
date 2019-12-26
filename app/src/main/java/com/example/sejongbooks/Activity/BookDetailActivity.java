package com.example.sejongbooks.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sejongbooks.Popup.ConfirmDialog;
import com.example.sejongbooks.Popup.FullImagePopup;
import com.example.sejongbooks.R;
import com.example.sejongbooks.Singleton.BookManager;
import com.example.sejongbooks.VO.BookVO;

public class BookDetailActivity extends AppCompatActivity {

    private BookVO m_book;

    private ConfirmDialog errDialog;

    private ScrollView mainScrollView;
    private ImageView transparentImageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_book_detail);

        initActivity();
    }

    public void initActivity() {
        m_book = BookManager.getInstance().getBookDataFromID
                (Integer.parseInt(getIntent().getStringExtra("BookID")));

        BookManager.getInstance().setSelectedBookID(m_book.getID());

        //Log.d("mmee:initActivityWidget","Thumbnail : " + m_book.getThumbnail());
        ImageView m_iv_bookThumbnail = (ImageView) this.findViewById(R.id.iv_bookThumbnail);
        m_iv_bookThumbnail.setImageBitmap(m_book.getImage());

        m_iv_bookThumbnail.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                FullImagePopup fullImagePopup = new FullImagePopup(BookDetailActivity.this, m_book.getImage());
                fullImagePopup.show();
                return false;
            }
        });

        TextView m_tv_bookName = (TextView) this.findViewById(R.id.tv_bookName);
        m_tv_bookName.setText(m_book.getName());

        TextView m_tv_bookHeight = (TextView) this.findViewById(R.id.tv_bookHeight);
        m_tv_bookHeight.setText(Integer.toString(m_book.getID()) + "m");

        TextView m_tv_bookDistance = (TextView) this.findViewById(R.id.tv_bookDistance);
        float distance = m_book.getGrade();
        if (distance < 1.0f) {
            m_tv_bookDistance.setText(Integer.toString((int)(distance * 1000)) + "m");
        } else {
            m_tv_bookDistance.setText(Float.toString(Math.round(distance * 10) / 10.0f) + "km");
        }
        TextView m_tv_bookGrade = (TextView) this.findViewById(R.id.txt_book_grade_map);
        m_tv_bookGrade.setText(Float.toString(m_book.getGrade()));

        TextView m_tv_bookAddress = (TextView) this.findViewById(R.id.tv_bookAddress);
        m_tv_bookAddress.setText(m_book.getAuthor());

        TextView m_tv_bookIntro = (TextView) this.findViewById(R.id.tv_bookIntro);
        m_tv_bookIntro.setText(m_book.getIntro());

        ImageView m_iv_isClimbed = (ImageView) this.findViewById(R.id.img_isClimbed);

        if (! m_book.isRead())
            m_iv_isClimbed.setVisibility(View.INVISIBLE);

        // rattingBar
        RatingBar rb_bookGrade = (RatingBar) this.findViewById(R.id.rb_book_grade_map);
        rb_bookGrade.setRating(m_book.getGrade());

        // close 버튼
        ImageButton closeButton = (ImageButton) this.findViewById(R.id.btn_close);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // TODO:
                // This function closes Activity Two
                // Hint: use Context's finish() method
                finish();
            }
        });

        errDialog = new ConfirmDialog(this);

        Button reviewWriteButton = (Button) this.findViewById(R.id.btn_writeReview);
        reviewWriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (m_book.isRead()) {
                    Intent intent = new Intent(view.getContext(), ReviewWriteActivity.class);
                    Log.d("bookID", "" + m_book.getID());
                    intent.putExtra("bookID", "" + m_book.getID());
                    startActivity(intent);
                } else {
                    errDialog.setErrorMessage("산을 등반한 후에\n리뷰를 작성해주세요.");
                    errDialog.show();
                }
            }
        });

        Button reviewEnterButton = (Button) this.findViewById(R.id.btn_enterReview);
        reviewEnterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(),ReviewActivity.class);

                Log.d("bookID",""+m_book.getID());
                intent.putExtra("bookID",""+m_book.getID());
                startActivity(intent);
                overridePendingTransition(R.anim.anim_slide_in_bottom,R.anim.anim_slide_out_top);
            }
        });

        mainScrollView = (ScrollView) findViewById(R.id.main_scrollview);
        transparentImageView = (ImageView) findViewById(R.id.transparent_image);
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
}
