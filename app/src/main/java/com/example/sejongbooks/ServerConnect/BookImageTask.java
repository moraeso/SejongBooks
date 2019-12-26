package com.example.sejongbooks.ServerConnect;

import android.os.AsyncTask;
import android.util.Log;

import com.example.sejongbooks.Helper.Constant;
import com.example.sejongbooks.Listener.AsyncCallback;
import com.example.sejongbooks.Singleton.BookManager;
import com.example.sejongbooks.VO.BookVO;
import java.util.ArrayList;

public class BookImageTask extends AsyncTask<Void, Void, Void> {
    AsyncCallback m_callback;
    Exception m_exception;
    int taskType;

    public BookImageTask(final int TASK_TYPE, AsyncCallback callback) {
        this.m_callback = callback;
        this.taskType = TASK_TYPE;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        //progress bar를 보여주는 등등의 행위
    }

    @Override
    protected Void doInBackground(Void... params) {
        ArrayList<BookVO> bookList = BookManager.getInstance().getItems();

        try {
            switch(taskType) {
                case Constant.FIRST_TEN:
                    for (int i = 0; i < 10; i++) {
                        if (bookList.get(i).getThumbnail() == null) {
                            int id = bookList.get(i).getID();
                            String url_img = Constant.URL + "/basicImages/" + id + ".jpg";
                            bookList.get(i).setThumbnail(BookManager.getInstance().getBookBitmapFromURL(url_img, "book" + id));
                            Log.d("mmee:BookImageTask", "get book resource " + id);
                        }
                    }
                    break;

                case Constant.CLIMBED:
                    for (BookVO book : BookManager.getInstance().getItems()) {
                        if (book.isClimbed() && book.getThumbnail() == null) {
                            int id = book.getID();
                            String url_img = Constant.URL + "/basicImages/" + id + ".jpg";
                            book.setThumbnail(BookManager.getInstance().getBookBitmapFromURL(url_img, "book" + id));
                            Log.d("mmee:BookImageTask", "get book resource " + id);
                        }
                    }
                    break;
            }
        } catch(Exception e) {
            this.m_exception = e;
            return null;
        }
        return null; // 결과가 여기에 담깁니다. 아래 onPostExecute()의 파라미터로 전달됩니다.
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