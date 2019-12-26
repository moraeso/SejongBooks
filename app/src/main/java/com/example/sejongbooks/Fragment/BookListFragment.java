package com.example.sejongbooks.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.view.inputmethod.InputMethod;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.sejongbooks.Adapter.BookListRecyclerViewAdapter;
import com.example.sejongbooks.Helper.Calculator;
import com.example.sejongbooks.Helper.Constant;
import com.example.sejongbooks.Helper.BookListRecyclerViewDecoration;
import com.example.sejongbooks.Listener.AsyncCallback;
import com.example.sejongbooks.R;
import com.example.sejongbooks.ServerConnect.BookImageTask;
import com.example.sejongbooks.ServerConnect.BookTask;
import com.example.sejongbooks.ServerConnect.UserClimbedListTask;
import com.example.sejongbooks.Singleton.BookManager;
import com.example.sejongbooks.VO.BookVO;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class BookListFragment extends Fragment implements BookListRecyclerViewAdapter.OnLoadMoreListener,
        SwipeRefreshLayout.OnRefreshListener {

    private RecyclerView m_bookRecycleView;
    private RecyclerView.LayoutManager m_layoutManager;
    private BookListRecyclerViewAdapter m_adapter;
    private SwipeRefreshLayout m_swipeRefresh;
    private Spinner m_sortSpinner;
    private EditText m_et_bookSearch;

    private ArrayList<BookVO> m_bufferItems; // 버퍼로 사용할 리스트
    private TextView txtCurrentAddress;

    private InputMethodManager imm;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_book_list, container, false);

        txtCurrentAddress = view.findViewById(R.id.tv_myAddress);
        txtCurrentAddress.setText(Constant.CURRENT_ADDRESS);

        m_bufferItems = new ArrayList();

        // RecycleView 생성 및 사이즈 고정
        m_bookRecycleView = (RecyclerView) view.findViewById(R.id.rv_bookList);
        m_bookRecycleView.setHasFixedSize(true);

        // Grid 레이아웃 적용
        m_layoutManager = new GridLayoutManager(getContext(), 2);
        m_bookRecycleView.setLayoutManager(m_layoutManager);
        m_bookRecycleView.addItemDecoration(new BookListRecyclerViewDecoration(getActivity()));

        // 어뎁터 연결
        m_adapter = new BookListRecyclerViewAdapter(getContext(), this);
        m_bookRecycleView.setAdapter(m_adapter);

        // 새로고침
        m_swipeRefresh = view.findViewById(R.id.swipeRefresh_bookList);
        m_swipeRefresh.setOnRefreshListener(this);

        // 화면 끝까지 스크롤 했을 때 추가 로딩 리스너
        m_bookRecycleView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                // 마지막 체크, 왜 -2 인지?
                GridLayoutManager gridLayoutManager = (GridLayoutManager) m_bookRecycleView.getLayoutManager();
                boolean isEditTextEmpty = m_et_bookSearch.getText().toString().equals("");
                if (isEditTextEmpty && dy > 0 &&
                        gridLayoutManager.findLastCompletelyVisibleItemPosition() > (m_adapter.getItemCount() - 2)) {
                    m_adapter.showLoading();
                }
            }
        });

        /*
        ArrayList<BookVO> bookList = BookManager.getInstance().getItems();
        for(BookVO book : bookList){
            if (Constant.X != 0.0) {
                book.setDistance(
                        Calculator.calculateDistance(
                                book.getLocX(),
                                book.getLocY()
                        )
                );
            } else {
                book.setDistance(0);
            }
        }*/

        imm = (InputMethodManager)super.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        // EditText 필터
        m_et_bookSearch = (EditText) view.findViewById(R.id.et_bookSearch);
        m_et_bookSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    imm.hideSoftInputFromWindow(m_et_bookSearch.getWindowToken(), 0 );
                }
                return true;
            }
        });
        m_et_bookSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                bookFilter(editable.toString());
            }
        });


        m_sortSpinner = (Spinner) view.findViewById(R.id.spinner_bookSort);

        String[] spinnerArray = getResources().getStringArray(R.array.book_sort);
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                BookListFragment.super.getContext(), R.layout.spinner_item, spinnerArray);
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        m_sortSpinner.setAdapter(spinnerArrayAdapter);

        m_sortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // 왜 자꾸 자동으로 실행되는지
                Log.d("mmee:BookListFragment", "BookList 정렬");
                BookManager.getInstance().sortBookList(adapterView.getItemAtPosition(i).toString());
                loadFirstData();
                m_bookRecycleView.smoothScrollToPosition(0);
                //m_adapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        //sortBookList(m_sortSpinner.getSelectedItem().toString());
/*
        // User 등반 리스트 갱신
        String url_userClimbedList = Constant.URL + "/book/list";
        UserClimbedListTask userClimbedListTask = new UserClimbedListTask(url_userClimbedList, new AsyncCallback() {
            @Override
            public void onSuccess(Object object) {
                // 정렬 스피너
                m_sortSpinner = (Spinner) BookListFragment.super.getView().findViewById(R.id.spinner_bookSort);

                String[] spinnerArray = getResources().getStringArray(R.array.book_sort);
                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                        BookListFragment.super.getContext(), R.layout.spinner_item, spinnerArray);
                spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                m_sortSpinner.setAdapter(spinnerArrayAdapter);

                m_sortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        // 왜 자꾸 자동으로 실행되는지
                        Log.d("mmee:BookListFragment", "BookList 정렬");
                        BookManager.getInstance().sortBookList(adapterView.getItemAtPosition(i).toString());
                        loadFirstData();
                        m_bookRecycleView.smoothScrollToPosition(0);
                        //m_adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
                //sortBookList(m_sortSpinner.getSelectedItem().toString());
            }

            @Override
            public void onFailure(Exception e) {

            }
        });
        userClimbedListTask.execute();*/


        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("mmee:BookListFragment", "onStart");
    }

    @Override
    public void onRefresh() {
        Log.d("mmee:BookListFragment", "onRefresh");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                m_swipeRefresh.setRefreshing(false);
                //m_bookItems.clear();

                //loadFirstData();

                m_et_bookSearch.setText("");
                BookManager.getInstance().sortBookList(m_sortSpinner.getSelectedItem().toString());

                loadFirstData();
                m_bookRecycleView.smoothScrollToPosition(0);
                //m_adapter.notifyDataSetChanged();
            }
        }, 1000);
    }

    @Override
    public void onLoadMore() {
        Log.d("mmee:BookListFragment", "onLoadMore");
        new AsyncTask<Void, Void, ArrayList<BookVO>>() {
            @Override
            protected ArrayList<BookVO> doInBackground(Void... voids) {
                ArrayList<BookVO> bookList = BookManager.getInstance().getItems();

                // 목록 10개 추가
                int start = m_adapter.getItemCount() - 1;
                int end = start + 10;
                if (end > bookList.size()) {
                    end = bookList.size();
                }

                m_bufferItems.clear();
                for (int i = start; i < end; i++) {
                    if (bookList.get(i).getImage() == null) {
                        int id = bookList.get(i).getID();
                        String url_img = Constant.URL + "/basicImages/" + id + ".jpg";
                        bookList.get(i).setImage(BookManager.getInstance().getBookBitmapFromURL(url_img, "book" + id));
                        Log.d("mmee:loadMore", "get book resource " + id);
                    }
                    m_bufferItems.add(bookList.get(i));
                }
                // 1초 sleep
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                return m_bufferItems;
            }

            @Override
            protected void onPostExecute(ArrayList<BookVO> items) {
                super.onPostExecute(items);

                m_adapter.dismissLoading();     // 하나 삭제해주는데 왜?
                m_adapter.addItemMore(items);   // 로딩될 아이템들 add
                m_adapter.setMore(true);
            }
        }.execute();
    }

    private void loadFirstData() {
        Log.d("mmee:BookListFragment", "LoadFirstData");
        BookImageTask bookImageTask = new BookImageTask(Constant.FIRST_TEN, new AsyncCallback() {
            @Override
            public void onSuccess(Object object) {

                // 이미지 10개 view 출력
                m_bufferItems.clear();
                for (int i = 0; i < 10; i++) {
                    m_bufferItems.add(BookManager.getInstance().getItems().get(i));
                }
                m_adapter.addAll(m_bufferItems);
            }

            @Override
            public void onFailure(Exception e) {

            }
        });
        bookImageTask.execute();
    }

    public void sortBookList(String str, boolean isRefresh) {
        /*
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
        } else if (str.equals("가까운 순")) {
            Collections.sort(BookManager.getInstance().getItems(), new Comparator<BookVO>() {
                @Override
                public int compare(BookVO o1, BookVO o2) {
                    if (o1.getDistance() > o2.getDistance()) {
                        return 1;
                    } else if (o1.getDistance() < o2.getDistance()) {
                        return -1;
                    } else {
                        return 0;
                    }
                }
            });
        } else if (str.equals("높은 순")) {
            Collections.sort(BookManager.getInstance().getItems(), new Comparator<BookVO>() {
                @Override
                public int compare(BookVO o1, BookVO o2) {
                    if (o1.getHeight() < o2.getHeight()) {
                        return 1;
                    } else if (o1.getHeight() > o2.getHeight()) {
                        return -1;
                    } else {
                        return 0;
                    }
                }
            });
        } else if (str.equals("낮은 순")) {
            Collections.sort(BookManager.getInstance().getItems(), new Comparator<BookVO>() {
                @Override
                public int compare(BookVO o1, BookVO o2) {
                    if (o1.getHeight() > o2.getHeight()) {
                        return 1;
                    } else if (o1.getHeight() < o2.getHeight()) {
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
        }*/

        if (isRefresh) {
            loadFirstData();
            m_bookRecycleView.smoothScrollToPosition(0);
            m_adapter.notifyDataSetChanged();
        }
    }


    private void bookFilter(String text) {
        ArrayList<BookVO> filterItems = new ArrayList();

        for (BookVO item : BookManager.getInstance().getItems()) {
            if (item.getName().toLowerCase().contains(text.toLowerCase())) {
                filterItems.add(item);
            }
        }
        m_adapter.filterList(filterItems);
    }

}
