package com.example.sejongbooks.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sejongbooks.Activity.BookDetailActivity;
import com.example.sejongbooks.R;
import com.example.sejongbooks.VO.BookVO;

import java.util.ArrayList;

public class BookReadListRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context m_context;
    private ArrayList<BookVO> m_bookItems;

    private OnLoadMoreListener onLoadMoreListener;

    private boolean isMoreLoading = true;

    public interface OnLoadMoreListener {
        void onLoadMore();
    }

    public BookReadListRecyclerViewAdapter(Context context, OnLoadMoreListener onLoadMoreListener) {
        this.m_context = context;
        this.onLoadMoreListener = onLoadMoreListener;
        m_bookItems = new ArrayList();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BookListViewHolder(LayoutInflater.from(m_context).inflate(R.layout.item_book_read, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final BookVO bookVO = m_bookItems.get(position);

        ((BookListViewHolder) holder).iv_bookImage.setImageBitmap(bookVO.getImage());
        ((BookListViewHolder) holder).tv_bookName.setText(bookVO.getName());
        ((BookListViewHolder) holder).tv_bookAuthor.setText(bookVO.getAuthor());
        ((BookListViewHolder) holder).tv_bookPublisher.setText(bookVO.getPublisher());
        ((BookListViewHolder) holder).tv_bookGrade.setText(Float.toString(bookVO.getGrade()));

        ((BookListViewHolder) holder).rb_bookGrade.setRating(bookVO.getGrade());

        ((BookListViewHolder) holder).layout_bookPanel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(m_context, BookDetailActivity.class);
                intent.putExtra("BookID", Integer.toString(bookVO.getID()));
                m_context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return m_bookItems.size();
    }

    public ArrayList<BookVO> getItems() {
        return m_bookItems;
    }

    public void showLoading() {
        if (isMoreLoading && m_bookItems != null && onLoadMoreListener != null) {
            Log.d("mee:BookListAdapter", "showLoading");
            isMoreLoading = false;
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    m_bookItems.add(null); // 무슨 의미? 결국 하나 추가했다 지워주는데 나중에 이해해보자
                    notifyItemInserted(m_bookItems.size() - 1); // -1?
                    onLoadMoreListener.onLoadMore();
                }
            });
        }
    }

    public void addAll(ArrayList<BookVO> items) {
        m_bookItems.clear();
        m_bookItems.addAll(items);
        notifyDataSetChanged();
    }

    public void dismissLoading() {
        if (m_bookItems != null && m_bookItems.size() > 0) {
            m_bookItems.remove(m_bookItems.size() - 1);
            notifyItemRemoved(m_bookItems.size());
        }
    }

    public void addItemMore(ArrayList<BookVO> items) {
        int sizeInit = m_bookItems.size();
        m_bookItems.addAll(items);
        notifyItemRangeChanged(sizeInit, m_bookItems.size());
    }

    public void setMore(boolean isMore) { this.isMoreLoading = isMore; }

    public void filterList(ArrayList<BookVO> filteredItems) {
        m_bookItems.clear();
        m_bookItems = filteredItems;
        notifyDataSetChanged();
    }

    public static class BookListViewHolder extends RecyclerView.ViewHolder {

        private CardView layout_bookPanel;
        private ImageView iv_bookImage;
        private TextView tv_bookName;
        private TextView tv_bookAuthor;
        private TextView tv_bookPublisher;
        private TextView tv_bookGrade;
        private ImageView iv_isRead;
        private RatingBar rb_bookGrade;
        //private boolean isClimbed;

        public BookListViewHolder(View convertView) {
            super(convertView);

            layout_bookPanel = (CardView) convertView.findViewById(R.id.layout_bookPanel);
            iv_bookImage = (ImageView) convertView.findViewById(R.id.iv_bookImage);
            tv_bookName = (TextView) convertView.findViewById(R.id.tv_bookName);
            tv_bookAuthor = (TextView) convertView.findViewById(R.id.tv_bookAuthor);
            tv_bookPublisher = (TextView) convertView.findViewById(R.id.tv_bookPublisher);
            tv_bookGrade = (TextView) convertView.findViewById(R.id.txt_book_grade);
            iv_isRead = (ImageView) convertView.findViewById(R.id.iv_book_read);
            rb_bookGrade = (RatingBar) convertView.findViewById(R.id.rb_book_grade);
            /*
            rb_bookGrade.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                @Override
                public void onRatingChanged(RatingBar ratingBar, float rating,
                                            boolean fromUser) {
                    tv_bookGrade.setText(rating + "");
                }
            });*/
        }
    }

    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar pBar;

        public ProgressViewHolder(View convertView) {
            super(convertView);
            pBar = (ProgressBar) convertView.findViewById(R.id.pBar);
        }
    }
}
