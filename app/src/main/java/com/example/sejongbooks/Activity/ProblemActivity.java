package com.example.sejongbooks.Activity;

import android.content.ContentValues;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sejongbooks.Listener.AsyncCallback;
import com.example.sejongbooks.R;
import com.example.sejongbooks.ServerConnect.ProblemTask;
import com.example.sejongbooks.Singleton.BookManager;
import com.example.sejongbooks.VO.BookVO;
import com.example.sejongbooks.VO.ProblemVO;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class ProblemActivity extends AppCompatActivity implements View.OnClickListener {

    private ArrayList<ProblemVO> problemList;
    private BookVO m_book;

    public ProblemActivity() {}

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_problem);

        m_book = BookManager.getInstance().getBookDataFromID
                (Integer.parseInt(getIntent().getStringExtra("BookID")));

        problemList = new ArrayList<>();

        loadProblems();
    }

    private void loadProblems() {

        ContentValues values = new ContentValues();
        values.put("bookID", m_book.getID());

        ProblemTask problemTask = new ProblemTask(values, new AsyncCallback() {
            @Override
            public void onSuccess(Object object) {
                problemList = (ArrayList<ProblemVO>)object;
                int a = 1;
            }

            @Override
            public void onFailure(Exception e) {
                e.printStackTrace();

            }
        });
        problemTask.execute();
    }

    @Override
    public void onClick(View v) {

    }
}
