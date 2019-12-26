package com.example.sejongbooks.Model;

import android.graphics.Bitmap;

public class User {

    String ID;
    String password;
    Bitmap profile;
    int totalPages;
    int level;
    int experience;

    public User() {
        ID = null;
        password = null;
        profile = null;
        totalPages = 0;
        level = 1;
        experience = 0;
    }

    public User(String ID, String password, Bitmap profile, int totalPages, int level, int experience) {
        this.ID = ID;
        this.password = password;
        this.profile = profile;
        this.totalPages = totalPages;
        this.level = level;
        this.experience = experience;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Bitmap getProfile() {
        return profile;
    }

    public void setProfile(Bitmap profile) {
        this.profile = profile;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }
}
