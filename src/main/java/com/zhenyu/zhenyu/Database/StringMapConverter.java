package com.zhenyu.zhenyu.Database;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;

public class StringMapConverter {
    @TypeConverter
    public static HashMap<String, Double> fromJson(String value) {
        Type mapType = new TypeToken<HashMap<String, Double>>() {}.getType();
        return new Gson().fromJson(value, mapType);
    }

    @TypeConverter
    public static String fromStringDoubleMap(HashMap<String, Double> map) {
        Gson gson = new Gson();
        return gson.toJson(map);
    }
}