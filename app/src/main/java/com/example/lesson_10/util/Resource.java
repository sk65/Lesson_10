package com.example.lesson_10.util;

public abstract class Resource<T> {
    private T data;
    private String massage;

    public Resource(T data, String massage) {
        this.data = data;
        this.massage = massage;
    }

    public Resource(T data) {
        this.data = data;
    }

    public Resource() {
    }

    public T getData() {
        return data;
    }

    public String getMassage() {
        return massage;
    }

    public static class Success<T> extends Resource<T> {
        public Success(T data) {
            super(data);
        }
    }

    public static class Error<T> extends Resource<T> {

        public Error(T data, String massage) {
            super(data, massage);
        }


    }

    public static class Loading<T> extends Resource<T> {

    }

}
