package com.example.tfoodsapi.projectpackage.model;

public class Callback<T> {
    private Exception error;
    private T data;

    public Callback(Exception error, T data) {
        this.error = error;
        this.data = data;
    }

    public T getData() {
        return data;
    }

    public boolean isError() {
        return (error != null);
    }

    public String getError() {
        return error.getMessage();
    }
}
