package com.example.sejongbooks.ServerConnect;

import android.content.ContentValues;
import android.os.AsyncTask;

import com.example.sejongbooks.Helper.Constant;
import com.example.sejongbooks.Listener.AsyncCallback;
import com.example.sejongbooks.Singleton.BookManager;
import com.example.sejongbooks.VO.BookVO;

public class ReadBookTask extends AsyncTask<Void, Void, Void> {
    private AsyncCallback m_callback;
    private Exception m_exception;
    private String m_url;
    private ContentValues m_values;

    public ReadBookTask(String url, ContentValues values, AsyncCallback callback) {
        this.m_callback = callback;
        this.m_url = url;
        this.m_values = values;
    }

    @Override
    protected Void doInBackground(Void... voids) {

        String result;

        try {
            PostHttpURLConnection requestHttpURLConnection = new PostHttpURLConnection();
            result = requestHttpURLConnection.request(m_url, m_values);

            int bookID = m_values.getAsInteger("bookID");

            for (BookVO item : BookManager.getInstance().getItems()) {
                if (item.getID() == bookID) {
                    if (m_values.getAsInteger("isInsert") == 1) {
                        item.setCount(item.getCount() + 1);
                    } else {
                        item.setCount(item.getCount() - 1);

                    }
                    break;
                }
            }

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
}
