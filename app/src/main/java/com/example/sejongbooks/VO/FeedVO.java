package com.example.sejongbooks.VO;

import android.graphics.Bitmap;

public class FeedVO {
    private int m_feedID;
    private String m_userID; // 리뷰 작성한 유저
    private String m_cotent; // 내용
    private Bitmap m_Image; // image
    private Bitmap m_userImage;
    private int m_like;
    private boolean m_Pic;

    private String m_ImageName;

    public FeedVO(){ }

    public FeedVO(int m_feedID, String m_userID, String m_cotent, Bitmap m_Image, int m_like, boolean m_myLike) {
        this.m_feedID =m_feedID;
        this.m_userID = m_userID;
        this.m_cotent = m_cotent;
        this.m_Image = m_Image;
        this.m_like = m_like;
        this.m_Pic = m_myLike;
    }

    public FeedVO(int m_feedID, String m_userID, String m_cotent, boolean m_Pic) {
        this.m_feedID = m_feedID;
        this.m_userID = m_userID;
        this.m_cotent = m_cotent;
        this.m_Image = null;
        this.m_like = 0;
        this.m_Pic = m_Pic;
    }

    public int getFeedID() { return m_feedID; }
    public void setFeedID(int m_reivewID) { this.m_feedID = m_reivewID; }

    public String getUserId() { return m_userID; }
    public void setUserId(String m_userId) { this.m_userID = m_userId; }


    public String getCotent() { return m_cotent; }
    public void setCotent(String m_cotent) { this.m_cotent = m_cotent; }

    public Bitmap getImage() { return m_Image; }
    public void setImage(Bitmap m_mainImage) { this.m_Image = m_mainImage; }

    public int getLike() { return m_like; }
    public void setLike(int m_like) { this.m_like = m_like; }

    public boolean isPic() { return m_Pic; }
    public void setPic(boolean m_Pic) { this.m_Pic = m_Pic; }

    public Bitmap getUserImage() { return m_userImage; }
    public void setUserImage(Bitmap m_userImage) { this.m_userImage = m_userImage; }

    public String getImageName(){ return m_ImageName;}
    public void setM_ImageName(String imageName ){ m_ImageName = imageName; }

    public void setFeed(int m_feedID, String m_userID,String m_cotent, String imageName,int m_like,boolean m_Pic){
        this.m_feedID = m_feedID;
        this.m_userID = m_userID;
        this.m_cotent = m_cotent;
        this.m_ImageName = imageName;
        this.m_like = m_like;
        this.m_Pic = m_Pic;
    }
}
