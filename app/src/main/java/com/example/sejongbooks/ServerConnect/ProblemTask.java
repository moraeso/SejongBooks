package com.example.sejongbooks.ServerConnect;

import android.content.ContentValues;
import android.os.AsyncTask;

import com.example.sejongbooks.Listener.AsyncCallback;
import com.example.sejongbooks.VO.ProblemVO;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class ProblemTask extends AsyncTask<Void, Void, ArrayList<ProblemVO>> {
    private AsyncCallback m_callback;
    private Exception m_exception;
    String url;
    ContentValues values;

    public ProblemTask(ContentValues values, AsyncCallback callback) {
        this.m_callback = callback;
        this.url = "http://15011066.iptime.org:7000/quiz/answer";
        this.values = values;
    }

    @Override
    protected ArrayList<ProblemVO> doInBackground(Void... params) {
        String result;
        ArrayList<ProblemVO> problemVOS = new ArrayList<ProblemVO>();
        int answer;
        int problemNum;
        try {
            PostHttpURLConnection requestHttpURLConnection = new PostHttpURLConnection();
            result = requestHttpURLConnection.request(url, values); // post token
            // 토큰 설정
            JSONObject jsonObject = new JSONObject(result);
            problemNum = jsonObject.getInt("bookID");

            for(int i = 0; i < 20; i++){
                answer = jsonObject.getInt("A"+(i+1));

                ProblemVO problemVO = new ProblemVO(
                            "http://15011066.iptime.org:7000/quiz/exam/"
                                    + problemNum+"_"+"Q"+(i+1)+".png", answer);
                problemVOS.add(problemVO);
            }

            return problemVOS;
        } catch (Exception e) {
            e.printStackTrace();
            m_exception = e;
        }
        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<ProblemVO> result) {
        super.onPostExecute(result);
        if (m_callback != null && m_exception == null) {
            m_callback.onSuccess(result);
        } else {
            m_callback.onFailure(m_exception);
        }
    }
}
