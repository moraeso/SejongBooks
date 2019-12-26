package com.example.sejongbooks.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sejongbooks.Helper.Constant;
import com.example.sejongbooks.Listener.AsyncCallback;
import com.example.sejongbooks.R;
import com.example.sejongbooks.ServerConnect.BookTask;
import com.example.sejongbooks.ServerConnect.StarTask;
import com.example.sejongbooks.ServerConnect.WriteImageTask;
import com.example.sejongbooks.ServerConnect.WriteTask;
import com.example.sejongbooks.Singleton.BookManager;
import com.example.sejongbooks.Singleton.MyInfo;
import com.example.sejongbooks.VO.BookVO;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class ReviewWriteActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageButton btn_close;
    private Button btn_submit;

    private RatingBar ratingBar_review;
    private EditText editText_review;
    private TextView tv_review_length;
    //view part
    private int m_bookID;
    private String m_reviewSentURL;
    private String m_reviewStarURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_write);

        initData();
        initView();
        initListener();
    }

    void initData() {
        m_reviewSentURL = "http://15011066.iptime.org:8888/api/review";
        m_reviewStarURL = "http://15011066.iptime.org:8888/api/star";

        Intent intent = getIntent();
        m_bookID = Integer.parseInt(intent.getStringExtra("bookID"));
    }

    void initView() {
        btn_close = findViewById(R.id.btn_review_close);
        btn_submit = findViewById(R.id.btn_review_submit);

        ratingBar_review = findViewById(R.id.ratingBar_reivew);
        editText_review = findViewById(R.id.editText_review);

        //글자수 제한
        tv_review_length = findViewById(R.id.tv_review_length);
        int max = 200;
        InputFilter[] fArray = new InputFilter[1];
        fArray[0] = new InputFilter.LengthFilter(max);
        editText_review.setFilters(fArray);
    }

    void initListener() {
        btn_close.setOnClickListener(this);
        btn_submit.setOnClickListener(this);

        ratingBar_review.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                Log.d("rating", "" + v);
                ratingBar.setRating(v);
                ratingBar_review.setRating(v);
            }
        });
        editText_review.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String input = editText_review.getText().toString();
                tv_review_length.setText(input.length() + " / 200");
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_review_close:
                Log.d("smh:button", "close");
                finish();
                break;
            case R.id.btn_review_submit:
                Log.d("smh:button", "submit");
                pushSubmitButton();
                break;
        }
    }

    public void pushSubmitButton() {
        if (editText_review.getText().length() == 0) {
            Toast.makeText(this, "리뷰를 작성해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }
        ContentValues values = new ContentValues();

        values.put("reviewUserID", MyInfo.getInstance().getUser().getID());
        values.put("reviewMntID", m_bookID);
        values.put("reviewString", editText_review.getText().toString());
        values.put("reviewStar", ratingBar_review.getRating());

        btn_submit.setEnabled(false);

        WriteTask writeTask = new WriteTask(m_reviewSentURL, values, new AsyncCallback() {
            @Override
            public void onSuccess(Object object) {
                String result = object.toString();
                String reviewID = null;
                try {
                    JSONObject jsonObj = new JSONObject(result);
                    reviewID = jsonObj.getString("reviewID");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                ContentValues values = new ContentValues();
                values.put("reviewMntID", m_bookID);

                StarTask starTask = new StarTask(m_reviewStarURL, values, new AsyncCallback() {
                    @Override
                    public void onSuccess(Object object) {
                        String url = Constant.URL + "/api/mntall";
                        BookTask bookTask = new BookTask(Constant.UPDATE_ITEMS, url, null, new AsyncCallback() {
                            @Override
                            public void onSuccess(Object object) {
                                finish();
                                Message msgProfile = handlerToast.obtainMessage();
                                handlerToast.sendMessage(msgProfile);
                            }

                            @Override
                            public void onFailure(Exception e) {

                            }
                        });
                        bookTask.execute();
                    }

                    @Override
                    public void onFailure(Exception e) {

                    }
                });
                starTask.execute();
            }

            @Override
            public void onFailure(Exception e) {
            }
        });
        writeTask.execute();
    }

    final Handler handlerToast = new Handler() {
        public void handleMessage(Message msg) {
            Toast.makeText(ReviewWriteActivity.this, "리뷰를 등록하였습니다", Toast.LENGTH_SHORT).show();
        }

    };
}

