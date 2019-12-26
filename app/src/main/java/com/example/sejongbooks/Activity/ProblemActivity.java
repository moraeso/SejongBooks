package com.example.sejongbooks.Activity;

import android.content.ContentValues;
import android.content.Context;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.IdRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.sejongbooks.Listener.AsyncCallback;
import com.example.sejongbooks.R;
import com.example.sejongbooks.ServerConnect.ProblemTask;
import com.example.sejongbooks.Singleton.BookManager;
import com.example.sejongbooks.VO.BookVO;
import com.example.sejongbooks.VO.ProblemVO;

import java.util.ArrayList;

public class ProblemActivity extends AppCompatActivity implements View.OnClickListener {

    private ArrayList<ProblemVO> problemList;
    private BookVO m_book;

    private ImageView iv_problem;
    private TextView tv_page;

    private RadioGroup radioGroup;

    private Button prevBtn, nextBtn;
    private int page;
    private int selected;

    private int[] aryAnswer;

    private Context context;

    public ProblemActivity() {
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_problem);

        context = this;

        m_book = BookManager.getInstance().getBookDataFromID
                (Integer.parseInt(getIntent().getStringExtra("BookID")));

        problemList = new ArrayList<>();

        page = 1;
        selected = -1;

        tv_page = (TextView) findViewById(R.id.tv_page);
        tv_page.setText("1");

        iv_problem = (ImageView) findViewById(R.id.iv_problem);

        prevBtn = (Button) findViewById(R.id.btn_prev);
        nextBtn = (Button) findViewById(R.id.btn_next);

        prevBtn.setOnClickListener(this);
        nextBtn.setOnClickListener(this);

        aryAnswer = new int[20];
        for (int i=0; i<20; i++) {
            aryAnswer[i] = -1;
        }

        //라디오 그룹 클릭 리스너
        RadioGroup.OnCheckedChangeListener radioGroupButtonChangeListener =
                new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                        if (i == R.id.radio1) {
                            selected = 1;
                        } else if (i == R.id.radio2) {
                            selected = 2;
                        } else if (i == R.id.radio3) {
                            selected = 3;
                        } else if (i == R.id.radio4) {
                            selected = 4;
                        } else if (i == R.id.radio5) {
                            selected = 5;
                        }
                    }
                };

        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(radioGroupButtonChangeListener);

        loadProblems();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_prev:
                if (page > 0 && selected != -1) {
                    aryAnswer[page - 1] = selected;
                    if (aryAnswer[page - 2] != -1) {
                        RadioButton btn = (RadioButton) radioGroup.getChildAt(aryAnswer[page - 2] - 1);
                        btn.toggle();
                        selected = aryAnswer[page - 2];
                    } else {
                        radioGroup.check(-1);
                        selected = -1;
                    }
                    page--;
                    tv_page.setText("" + page);
                    Glide.with(this).load(problemList.get(page - 1).getProblemImageString()).into(iv_problem);
                }
                break;


            case R.id.btn_next:
                if (page < 20 && selected != -1) {
                    aryAnswer[page - 1] = selected;
                    if (aryAnswer[page] != -1) {
                        RadioButton btn = (RadioButton) radioGroup.getChildAt(aryAnswer[page] - 1);
                        btn.toggle();
                        selected = aryAnswer[page];
                    } else {
                        radioGroup.check(-1);
                        selected = -1;
                    }
                    page++;
                    tv_page.setText("" + page);
                    Glide.with(this).load(problemList.get(page - 1).getProblemImageString()).into(iv_problem);
                }
                break;
        }
    }

    private void loadProblems() {

        ContentValues values = new ContentValues();
        values.put("bookID", m_book.getID());

        ProblemTask problemTask = new ProblemTask(values, new AsyncCallback() {
            @Override
            public void onSuccess(Object object) {
                problemList = (ArrayList<ProblemVO>) object;
                Glide.with(context).load(problemList.get(page - 1).getProblemImageString()).into(iv_problem);
            }

            @Override
            public void onFailure(Exception e) {
                e.printStackTrace();

            }
        });
        problemTask.execute();
    }


}
