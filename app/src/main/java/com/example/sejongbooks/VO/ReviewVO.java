package com.example.sejongbooks.VO;

import android.graphics.Bitmap;

public class ReviewVO {
    private int m_reivewID;
    private String m_userID; // 리뷰 작성한 유저
    private int m_bookID;
    private String m_cotent; // 내용
    private Bitmap m_userImage;
    private int m_like;
    private double m_grade;

    private boolean m_Pic;

    private String m_ImageName;

    public ReviewVO(){ }

    public int getReivewID() { return m_reivewID; }
    public void setM_reivewID(int m_reivewID) { this.m_reivewID = m_reivewID; }

    public String getUserId() { return m_userID; }
    public void setUserId(String m_userId) { this.m_userID = m_userId; }

    public int getBookID() { return m_bookID; }
    public void setBookID(int m_bookID) { this.m_bookID = m_bookID; }

    public String getCotent() { return m_cotent; }
    public void setCotent(String m_cotent) { this.m_cotent = m_cotent; }

    public int getLike() { return m_like; }
    public void setLike(int m_like) { this.m_like = m_like; }

    public double getGrade() { return m_grade; }
    public void setGrade(double m_grade) { this.m_grade = m_grade; }

    public boolean isPic() { return m_Pic; }
    public void setPic(boolean m_Pic) { this.m_Pic = m_Pic; }

    public Bitmap getUserImage() { return m_userImage; }
    public void setUserImage(Bitmap m_userImage) { this.m_userImage = m_userImage; }

    public String getImageName(){ return m_ImageName;}
    public void setM_ImageName(String imageName ){ m_ImageName = imageName; }

    public void setReview(int m_reivewID, String m_userID, int m_bookID, String m_cotent, double m_grade,int m_like, boolean IFLIKE){
        this.m_reivewID = m_reivewID;
        this.m_userID = m_userID;
        this.m_bookID = m_bookID;
        this.m_cotent = m_cotent;
        this.m_grade = m_grade;
        this.m_like = m_like;
        this.m_Pic = IFLIKE;
    }
}