package com.zhenyu.zhenyu.Database;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class StringListConverter {
    @TypeConverter
    public static String fromStringList(List<String> strings){
        Gson gson = new Gson();
        String json = gson.toJson(strings);
        return json;
    }

    @TypeConverter
    public List<String> fromJson(String value){
        Gson gson = new Gson();
        Type listType = new TypeToken<List<String>>() {}.getType();
        return gson.fromJson(value, listType);
    }
}
