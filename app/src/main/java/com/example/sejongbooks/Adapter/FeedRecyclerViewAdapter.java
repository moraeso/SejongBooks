package com.example.sejongbooks.Adapter;

import android.content.ContentValues;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sejongbooks.Popup.FullImagePopup;
import com.example.sejongbooks.R;
import com.example.sejongbooks.ServerConnect.PostHttpURLConnection;
import com.example.sejongbooks.Singleton.MyInfo;
import com.example.sejongbooks.VO.FeedVO;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FeedRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private ArrayList<FeedVO> m_pidItems;

    private Context m_context;

    private boolean isLoading;
    private int lastVisibleItem, totalItemCount;
    private int visibleThreshold = 2;

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;

    private String m_url;

    private OnLoadMoreListener onLoadMoreListener;

    public int getItemViewType(int position) { //null값인 경우 로딩타입
        return m_pidItems.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    public FeedRecyclerViewAdapter(Context context, RecyclerView recyclerView, ArrayList<FeedVO> pidItems, OnLoadMoreListener onLoadMoreListener1) {
        this.m_context = context;
        this.m_pidItems = pidItems;
        this.onLoadMoreListener = onLoadMoreListener1;

        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                totalItemCount = linearLayoutManager.getItemCount();
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                //로딩중이아니고 , 전체아이템수 <= 마지막에 보이는 아이템인덱스 + 화면에보이는개수(리사이클러뷰에 아이템이 5개씩 보이므로 5로 설정함
                if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                    if (onLoadMoreListener != null) {
                        Log.d("smh:scrolled","LoadMore");

                        onLoadMoreListener.onLoadMore();
                    }
                    isLoading = true;
                }
            }
        });
    }//생산자

    @Override
    public int getItemCount() {
        return m_pidItems.size();
    }

    public ArrayList<FeedVO> getItems() {
        return m_pidItems;
    }

    public void clear() {
        m_pidItems.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<FeedVO> list) {
        m_pidItems.addAll(list);
        notifyDataSetChanged();
    }
    public void setLoaded() {
        isLoading = false;
    }

    public interface OnLoadMoreListener {
        void onLoadMore();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pid, parent, false);
            return new ItemViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_loading, parent, false);
            return new LoadingViewHolder(view);
        }
    }//아이템 뷰를 위한 뷰홀더 객체 생성

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {
            final FeedVO item = m_pidItems.get(position);

            ((ItemViewHolder) holder).m_imageButton_like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int like;
                    if(item.isPic() == true){

                    }
                    else{
                        item.setPic(true);
                        item.setLike(item.getLike()+1);
                        ((ItemViewHolder) holder).m_textView_like.setText(String.valueOf(item.getLike()));
                        ((ItemViewHolder) holder).m_imageButton_like.setImageResource(R.drawable.heart);
                        Log.d("like",""+item.getLike());
                        connectNetworkLike("http://15011066.iptime.org:7000/feed/like/",item);
                    }
                }
            });

            ((ItemViewHolder) holder).m_imageView_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Drawable drawable = ((ItemViewHolder) holder).m_imageView_image.getDrawable();
                    FullImagePopup fullImagePopup = new FullImagePopup(m_context, ((BitmapDrawable)drawable).getBitmap());
                    fullImagePopup.show();
                }
            });

            ((ItemViewHolder) holder).m_imageView_user_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Drawable drawable = ((ItemViewHolder) holder).m_imageView_user_image.getDrawable();
                    FullImagePopup fullImagePopup = new FullImagePopup(m_context, ((BitmapDrawable)drawable).getBitmap());
                    fullImagePopup.show();
                }
            });

            ((ItemViewHolder) holder).setItem(item);
        } else if (holder instanceof LoadingViewHolder) {
            ((LoadingViewHolder) holder).progressBar.setIndeterminate(true);
        }
    }//position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시


    private void connectNetworkLike(String url, FeedVO item){
        m_url = url;

        ContentValues contentValues = new ContentValues();
        contentValues.put("feedID",item.getFeedID());
        contentValues.put("userID", MyInfo.getInstance().getUser().getID());

        NetworkTask networkTask = new NetworkTask(m_url,contentValues);
        networkTask.execute();
    }


    public class NetworkTask extends AsyncTask<Void, Void, String> {

        private String url;
        private ContentValues values;

        public NetworkTask(String url, ContentValues values) {
            this.url = url;
            this.values = values;
        }

        @Override
        protected String doInBackground(Void... params) {

            String result; // 요청 결과를 저장할 변수.
            PostHttpURLConnection postHttpURLConnection = new PostHttpURLConnection();
            result = postHttpURLConnection.request(url, values); // 해당 URL로 부터 결과물을 얻어온다.
            Log.d("review like result",result);

            receiveResult(result);

            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }

    public void receiveResult(String result){
        try {
            JSONObject jsonObj = new JSONObject(result);

            int code = jsonObj.getInt("code");

            Log.v("Code", code+"");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView m_textView_user_id; // user id
        TextView m_textView_coment; // review coment
        TextView m_textView_like; // review how many people like review
        ImageView m_imageView_user_image;
        ImageView m_imageView_image; // review main image
        ImageButton m_imageButton_like;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            m_textView_user_id = itemView.findViewById(R.id.tv_user_id);
            m_textView_coment = itemView.findViewById(R.id.tv_review_content);
            m_textView_like = itemView.findViewById(R.id.tv_review_like);
            m_imageView_user_image = itemView.findViewById(R.id.iv_review_user_profile);
            m_imageView_image = itemView.findViewById(R.id.iv_review_image);
            m_imageButton_like = itemView.findViewById(R.id.btn_review_heart_button);
        }

        public void setItem(final FeedVO item) {
            m_textView_user_id.setText(item.getUserId());
            m_textView_coment.setText(item.getCotent());
            m_textView_like.setText(String.valueOf(item.getLike()));

            //좋아요 버튼 클릭시
            if (item.isPic() == true) {
                m_imageButton_like.setImageResource(R.drawable.heart);
            } else {
                m_imageButton_like.setImageResource(R.drawable.heart_uncheck);
            }

            //m_imageView_image.setImageResource(R.drawable.heart);

            if(item.getImage()==null) {
                m_imageView_image.getLayoutParams().height=0;
            }
            else {
                m_imageView_image.setImageBitmap(item.getImage());
            }


            if(m_imageView_user_image != null) {
                m_imageView_user_image.setBackground(new ShapeDrawable(new OvalShape()));
                if (Build.VERSION.SDK_INT >= 21) {
                    m_imageView_user_image.setClipToOutline(true);
                }
            }
            m_imageView_user_image.setImageBitmap(item.getUserImage());
        }
    }

    private class LoadingViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public LoadingViewHolder(View view) {
            super(view);
            progressBar = (ProgressBar) view.findViewById(R.id.progressBar_review);
        }
    }
}