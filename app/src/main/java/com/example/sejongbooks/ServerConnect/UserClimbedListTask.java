package com.example.sejongbooks.ServerConnect;

import android.os.AsyncTask;
import android.util.Log;

import com.example.sejongbooks.Helper.Constant;
import com.example.sejongbooks.Listener.AsyncCallback;
import com.example.sejongbooks.Singleton.BookManager;
import com.example.sejongbooks.VO.BookVO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class UserClimbedListTask extends AsyncTask<Void, Void, Void> {

    AsyncCallback m_callback;
    Exception m_exception;
    String m_url;

    public UserClimbedListTask(String url, AsyncCallback callback) {
        m_callback = callback;
        m_url = url;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(Void... voids) {
        String UserClimbedList_json_str;

        Log.d("mmee:ClimbedTask","try");
        try {
            PostHttpURLConnection requestHttpURLConnection = new PostHttpURLConnection();
            UserClimbedList_json_str = requestHttpURLConnection.
                    request(m_url, null);
            // 나중에 Constant.ADMIN_ID -> MyInfo.getInstance().getUserID() 로 수정

            initUserClimbed(UserClimbedList_json_str);
        } catch(Exception e) {
            this.m_exception = e;
            return null;
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        if (m_callback != null && m_exception == null) {
            m_callback.onSuccess(true);
        } else {
            m_callback.onFailure(m_exception);
        }
    }

    private void initUserClimbed(String json_str) {

        ArrayList<BookVO> bookList = BookManager.getInstance().getItems();

        try {
            JSONArray jsonArray = new JSONArray(json_str);

            // user 등반 기록 false로 초기화
            for (BookVO book : bookList) {
                book.setRead(false);
            }

            // 순회하며 등반 기록 적용
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObj = jsonArray.getJSONObject(i);

                int mntID = jsonObj.getInt("mntID");
                for (BookVO book : bookList) {
                    if (book.getID() == mntID) {
                        book.setRead(true);
                       break;
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
