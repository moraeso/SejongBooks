package com.example.sejongbooks.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.example.sejongbooks.Helper.BackPressCloseHandler;
import com.example.sejongbooks.Helper.Constant;
import com.example.sejongbooks.Listener.AsyncCallback;
import com.example.sejongbooks.R;
import com.example.sejongbooks.ServerConnect.BookTask;
import com.example.sejongbooks.Singleton.BookManager;

public class IntroActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private TextView tv_loadPercent;
    private BackPressCloseHandler backPressCloseHandler;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_intro);

        progressBar = (ProgressBar) findViewById(R.id.progressBar_intro);
        tv_loadPercent = (TextView) findViewById(R.id.tv_loadPercent);

        initBookList();
        startLoading();
    }

    private void initBookList() {
        BookManager.getInstance().setLoadPercent(0);
        BookManager.getInstance().getItems().clear();
        loadBookData();
    }

    private void startLoading() {
        handler = new Handler();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (BookManager.getInstance().getLoadPercent() < 100) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setProgress(BookManager.getInstance().getLoadPercent());
                            tv_loadPercent.setText(BookManager.getInstance().getLoadPercent() + " %");
                        }
                    });
                    try {
                        // Sleep for 100 milliseconds to show the progress slowly.
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        thread.start();
    }

    @Override
    public void onBackPressed() {
        backPressCloseHandler.onBackPressed();
    }

    private void loadBookData() {
        // 산 URL 설정
        String url = Constant.URL + "/api/mntall";

        // execute, 산 리스트 생성 및 저장
        BookTask bookTask = new BookTask(Constant.GET_NEW, url, null, new AsyncCallback() {
            @Override
            public void onSuccess(Object object) {
                Log.d("mmee:bookTask", "get book resource success!");
                finish();
            }

            @Override
            public void onFailure(Exception e) {
                //e.printStackTrace();
            }
        });
        bookTask.execute();
    }

}
