package com.example.sejongbooks.Fragment;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.sejongbooks.Adapter.FeedRecyclerViewAdapter;
import com.example.sejongbooks.Listener.AsyncCallback;
import com.example.sejongbooks.R;
import com.example.sejongbooks.ServerConnect.PostHttpURLConnection;
import com.example.sejongbooks.VO.FeedVO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;


public class BookMapFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{
    private ArrayList<FeedVO> m_reviewItems; //리뷰 리스트
    private ArrayList<FeedVO> m_bufferList;  //맨처음 모든 리뷰를 받아옴

    private RecyclerView m_recyclerView;
    private LinearLayoutManager m_layoutManager;
    private FeedRecyclerViewAdapter m_adapter;

    private SwipeRefreshLayout m_swipeRefreshLayout;

    private String m_url;

    private CheckTypesTask loading;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feed, container, false);

        initView(view);

        m_url = "http://15011066.iptime.org:7000/feed/all";

        NetworkTask networkTask = new NetworkTask(m_url , new AsyncCallback() {
            @Override
            public void onSuccess(Object object) {
                Log.d("mmee:ReviewActivity","get review success");
                getData();
            }

            @Override
            public void onFailure(Exception e) {
                Log.d("mmee:ReviewActivity","get review fail");
            }
        });
        networkTask.execute();

        return view;
    }

    private void initView(View view) {
        m_reviewItems = new ArrayList();
        m_bufferList = new ArrayList();

        m_recyclerView = view.findViewById(R.id.rc_feed_recyclerview);

        //레이아웃매니저 설정 설정
        m_layoutManager = new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false);
        m_recyclerView.setLayoutManager(m_layoutManager);

        //어탭더 설정
        m_adapter = new FeedRecyclerViewAdapter(getContext(),m_recyclerView,m_reviewItems,new FeedRecyclerViewAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (m_reviewItems.size() <= 100) {
                    Log.d("smh:loadmore","more");

                    m_reviewItems.add(null);
                    m_recyclerView.post(new Runnable() {
                        public void run() {
                            m_adapter.notifyItemInserted(m_reviewItems.size() - 1);
                        }
                    });
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            m_reviewItems.remove(m_reviewItems.size() - 1);
                            m_adapter.notifyItemRemoved(m_reviewItems.size());

                            //Generating more data
                            int index = m_reviewItems.size();
                            int end = index + 5;
                            if (end > m_bufferList.size() - 1) {
                                end = m_bufferList.size();
                            }

                            if(index == end){
                                Log.d("smh:reviewStart",""+index);
                                Log.d("smh:end",""+end);
                                return;
                            }

                            for (int i = index; i < end; i++) {
                                m_reviewItems.add(m_bufferList.get(i));
                            }
                            m_adapter.notifyDataSetChanged();
                            m_adapter.setLoaded();
                        }
                    }, 2000);
                } else {
                    Toast.makeText(getContext(), "Loading data completed", Toast.LENGTH_SHORT).show();
                }
            }
        });
        m_recyclerView.setAdapter(m_adapter);

        // 새로고침
        m_swipeRefreshLayout =  view.findViewById(R.id.swipeContainer);
        m_swipeRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    public void onResume() {

        super.onResume();
    }

    public class NetworkTask extends AsyncTask<Void, Void, String> {

        private AsyncCallback callback;
        private Exception exception;
        private String url;
        private ContentValues values;

        public NetworkTask(String url, AsyncCallback callback) {
            this.exception = null;
            this.callback = callback;
            this.url = url;
            this.values = values;

            Message msgProfile = handlerLoadingStart.obtainMessage();
            handlerLoadingStart.sendMessage(msgProfile);
        }

        @Override
        protected String doInBackground(Void... params) {

            String result; // 요청 결과를 저장할 변수.
            PostHttpURLConnection postHttpURLConnection = new PostHttpURLConnection();
            result = postHttpURLConnection.request(url, values); // 해당 URL로 부터 결과물을 얻어온다.
            Log.d("smh:result", result);

            receiveFeed(result);
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            Message msgProfile = handlerLoading.obtainMessage();
            handlerLoading.sendMessage(msgProfile);

            if (callback != null && exception == null) {
                callback.onSuccess(true);
            } else {
                callback.onFailure(exception);
            }
        }


    }

    final Handler handlerLoadingStart = new Handler() {
        public void handleMessage(Message msg) {
            loading = new CheckTypesTask();
            loading.execute();
        }
    };

    final Handler handlerLoading = new Handler() {
        public void handleMessage(Message msg) {
            loading.onPostExecute(null);
        }
    };

    public class CheckTypesTask extends AsyncTask<Void, Void, Void> {

        ProgressDialog asyncDialog = new ProgressDialog(getContext(), R.style.progress_bar_style);

        @Override
        protected void onPreExecute() {

            asyncDialog.setCancelable(false);
            asyncDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            asyncDialog.setMessage("피드를 불러오고 있습니다");

            // show dialog
            asyncDialog.show();

            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            try {
                for (int i = 0; i < 5; i++) {
                    asyncDialog.setProgress(i*30);
                    Thread.sleep(500);
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            asyncDialog.dismiss();
            super.onPostExecute(result);
        }

    }

    @Override
    public void onRefresh() {
        // 새로고침 코드
        m_adapter.clear();
        getData();
        // 새로고침 완료
        m_swipeRefreshLayout.setRefreshing(false);
    }

    private void getData(){
        Log.d("smh:get","data");
        m_reviewItems.clear();
        int end = 5;

        if(m_bufferList.size() < 5){
            end = m_bufferList.size();
        }
        Log.d("smh:first size",""+m_bufferList.size());


        for(int i =0;i<end;i++){
            m_reviewItems.add(m_bufferList.get(i));
        }

        m_adapter.notifyDataSetChanged();
    }

    public void getFeedImage(FeedVO newFeed) {
        InputStream is = null;
        try {
            String reviewImg_url = "http://15011066.iptime.org:7000/" + newFeed.getImageName();

            is = (InputStream) new URL(reviewImg_url).getContent();
        } catch (IOException e) {
            Drawable drawable = getResources().getDrawable(R.drawable.ic_book_ranking_main);
            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();

            e.printStackTrace();
            return;
        }

        Drawable feed_drawable = Drawable.createFromStream(is, "book" + newFeed.getFeedID());
        newFeed.setImage(((BitmapDrawable)feed_drawable).getBitmap());

        Log.d("mmee:ReviewActivity", "Get review image");
    }

    public void getUserImage(FeedVO newReview) {
        InputStream is = null;
        try {
            String userImg_url = "http://15011066.iptime.org:8888/userimages/" + newReview.getUserId() + ".jpg";
            is = (InputStream) new URL(userImg_url).getContent();

        } catch (IOException e) {
            Drawable drawable = getResources().getDrawable(R.drawable.ic_book_ranking_main);
            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
            newReview.setUserImage(bitmap);
            e.printStackTrace();
            return;
        }

        Drawable user_drawable = Drawable.createFromStream(is, "book" + newReview.getUserId());
        newReview.setUserImage(((BitmapDrawable) user_drawable).getBitmap());
        Log.d("mmee:ReviewActivity", "Get user image");

    }

    public void receiveFeed(String result) {
        try {
            JSONArray jsonArray = new JSONArray(result);
            Log.d("smh:length", "" + jsonArray.length());

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObj = jsonArray.getJSONObject(i);

                int feedID = jsonObj.getInt("feedID");
                String userID = jsonObj.getString("userID");
                String feedString = jsonObj.getString("feedString");
                String feedPIC = jsonObj.getString("feedPIC");
                int reviewLike = 1;
                int reviewIFLIKE = 1;

                final FeedVO newFeed = new FeedVO();
                if (reviewIFLIKE == 1) {
                    newFeed.setFeed(feedID, userID, feedString, feedPIC, reviewLike, true);
                } else {
                    newFeed.setFeed(feedID, userID, feedString, feedPIC, reviewLike, false);
                }

                getFeedImage(newFeed);
                getUserImage(newFeed);

                m_bufferList.add(newFeed);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}