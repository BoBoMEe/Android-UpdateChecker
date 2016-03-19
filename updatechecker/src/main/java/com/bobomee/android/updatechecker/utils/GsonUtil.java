package com.bobomee.android.updatechecker.utils;


import com.google.gson.Gson;

public class GsonUtil {

    private static class SingletonHolder {
        private static Gson _instance = new Gson();
    }

    private GsonUtil() {
    }


    public static <T> T jsonToBean(String json, Class<T> clazz) {
        return SingletonHolder._instance.fromJson(json, clazz);
    }

    public static <T> String beanToJson(T t) {
        return SingletonHolder._instance.toJson(t);
    }
}
