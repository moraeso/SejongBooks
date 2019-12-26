package com.example.sejongbooks.VO;

import android.graphics.Bitmap;

public class BookVO {

    private int m_id;
    private Bitmap m_image;
    private String m_type;
    private String m_name;
    private String m_author;
    private String m_publisher;
    private int m_page;
    private int m_date;
    private float m_grade;
    private Boolean m_isRead;
    private String m_intro; // 산 소개

    public BookVO() {
    }

    public void setBook(int m_id, String m_type, String m_name, String m_author, String m_publisher, int date, int m_page, String m_intro, float m_grade) {
        this.m_id = m_id;
        this.m_type = m_type;
        this.m_name = m_name;
        this.m_author = m_author;
        this.m_publisher = m_publisher;
        this.m_page = m_page;
        this.m_intro = m_intro;
        this.m_grade = m_grade;
    }

    public int getID() {
        return m_id;
    }

    public void setID(int m_id) {
        this.m_id = m_id;
    }

    public Bitmap getImage() {
        return m_image;
    }

    public void setImage(Bitmap m_image) {
        this.m_image = m_image;
    }

    public String getType() {
        return m_type;
    }

    public void setType(String m_type) {
        this.m_type = m_type;
    }

    public String getName() {
        return m_name;
    }

    public void setName(String m_name) {
        this.m_name = m_name;
    }

    public String getAuthor() {
        return m_author;
    }

    public int getPage() {
        return m_page;
    }

    public int getDate() {
        return m_date;
    }

    public void setDate(int m_date) {
        this.m_date = m_date;
    }

    public void setPage(int m_page) {
        this.m_page = m_page;
    }

    public void setAuthor(String m_author) {
        this.m_author = m_author;
    }

    public String getPublisher() {
        return m_publisher;
    }

    public void setPublisher(String m_publisher) {
        this.m_publisher = m_publisher;
    }

    public float getGrade() {
        return m_grade;
    }

    public void setGrade(float m_grade) {
        this.m_grade = m_grade;
    }

    public String getIntro() {
        return m_intro;
    }

    public void setIntro(String m_intro) {
        this.m_intro = m_intro;
    }

    public boolean isRead() {
        return m_isRead;
    }
    public void setRead(Boolean m_isRead) {
        this.m_isRead = m_isRead;
    }
}
