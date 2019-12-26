package com.example.sejongbooks.Singleton;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.example.sejongbooks.R;
import com.example.sejongbooks.VO.BookVO;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class BookManager {

    private int loadPercent;
    private String currentSort;
    private int selectedBookID;

    private BookManager() {
        m_items = new ArrayList();
    }

    private static class BookManagerHolder {
        public static final BookManager instance = new BookManager();
    }

    public static BookManager getInstance() {
        return BookManagerHolder.instance;
    }

    private ArrayList<BookVO> m_items;

    public ArrayList<BookVO> getItems() { return m_items; }

    public Bitmap getBookBitmapFromURL(String url, String srcName) {
        InputStream is;
        Drawable book_drawable = null;
        Bitmap book_bitmap = null;

        try {
            is = (InputStream) new URL(url).getContent();
            book_drawable = Drawable.createFromStream(is, srcName);
            book_bitmap = ((BitmapDrawable)book_drawable).getBitmap();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return book_bitmap;
    }

    public BookVO getBookDataFromID(int bookID) {
        BookVO book = new BookVO();
        for (BookVO item : BookManager.getInstance().getItems()) {
            if (item.getID() == bookID) {
                book = item;
                break;
            }
        }
        return book;
    }

    public void setLoadPercent(int percent) {
        loadPercent = percent;
    }

    public int getLoadPercent() {
        return loadPercent;
    }

    public void setSelectedBookID(int id) { selectedBookID = id; }

    public int getSelectedBookID() { return selectedBookID; }

    public void setCurrentSort(String sort) {
        currentSort = sort;
    }

    public String getCurrentSort() {
        return currentSort;
    }

    public void sortBookList(String str) {
        Log.d("mmee:BookListFragment", "spinner changed : " + str);

        if (str.equals("별점 순")) {
            Collections.sort(BookManager.getInstance().getItems(), new Comparator<BookVO>() {
                @Override
                public int compare(BookVO o1, BookVO o2) {
                    if (o1.getGrade() < o2.getGrade()) {
                        return 1;
                    } else if (o1.getGrade() > o2.getGrade()) {
                        return -1;
                    } else {
                        return 0;
                    }
                }
            });
        } else if (str.equals("페이지 ↑")) {
            Collections.sort(BookManager.getInstance().getItems(), new Comparator<BookVO>() {
                @Override
                public int compare(BookVO o1, BookVO o2) {
                    if (o1.getPage() < o2.getPage()) {
                        return 1;
                    } else if (o1.getPage() > o2.getPage()) {
                        return -1;
                    } else {
                        return 0;
                    }
                }
            });
        } else if (str.equals("페이지 ↓")) {
            Collections.sort(BookManager.getInstance().getItems(), new Comparator<BookVO>() {
                @Override
                public int compare(BookVO o1, BookVO o2) {
                    if (o1.getPage() > o2.getPage()) {
                        return 1;
                    } else if (o1.getPage() < o2.getPage()) {
                        return -1;
                    } else {
                        return 0;
                    }
                }
            });
        } else if (str.equals("가나다 순")) {
            Collections.sort(BookManager.getInstance().getItems(), new Comparator<BookVO>() {
                @Override
                public int compare(BookVO o1, BookVO o2) {
                    if (o1.getName().toString().
                            compareTo(o2.getName().toString()) > 0) {
                        return 1;
                    } else if (o1.getName().toString().
                            compareTo(o2.getName().toString()) < 0) {
                        return -1;
                    } else {
                        return 0;
                    }
                }
            });
        } else if (str.equals("인증 순")) {
            Collections.sort(BookManager.getInstance().getItems(), new Comparator<BookVO>() {
                @Override
                public int compare(BookVO o1, BookVO o2) {
                   return 0;
                }
            });
        }
    }
}