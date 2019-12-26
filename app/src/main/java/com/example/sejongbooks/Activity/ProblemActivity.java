package com.example.sejongbooks.Activity;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sejongbooks.R;

public class ProblemActivity extends AppCompatActivity implements View.OnClickListener {

    public ProblemActivity() {}

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_problem);
    }

    @Override
    public void onClick(View v) {

    }
}
