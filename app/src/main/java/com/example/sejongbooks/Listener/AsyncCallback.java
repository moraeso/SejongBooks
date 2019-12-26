package com.example.sejongbooks.Listener;

public interface AsyncCallback<T> {
    void onSuccess(T object);
    void onFailure(Exception e);
}
