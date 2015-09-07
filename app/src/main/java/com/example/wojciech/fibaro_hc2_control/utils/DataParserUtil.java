package com.example.wojciech.fibaro_hc2_control.utils;

import android.text.TextUtils;

import com.example.wojciech.fibaro_hc2_control.model.Device;
import com.example.wojciech.fibaro_hc2_control.model.HC2;
import com.example.wojciech.fibaro_hc2_control.model.RefreshStates;
import com.example.wojciech.fibaro_hc2_control.model.Room;
import com.example.wojciech.fibaro_hc2_control.model.Section;
import com.example.wojciech.fibaro_hc2_control.service.ServiceHC2;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Wojciech on 2015-09-04.
 */
public class DataParserUtil {

    public static List<Section> parseToSectionList(String json) {
        if(TextUtils.isEmpty(json)) return null;
        List<Section> sectionList2 = toList(json,Section.class);
        return sectionList2;
        //inline may be used
        // new Gson().fromJson(json, new TypeToken<ArrayList<Section>>() { }.getType());
    }

    public static Section parseToSection(String json) {
        if(TextUtils.isEmpty(json)) return null;
        return new Gson().fromJson(json, Section.class);
    }

    public static List<Room> parseToRoomList(String json) {
        if(TextUtils.isEmpty(json)) return null;
        List<Room> list = toList(json, Room.class);
        return list;
    }

    public static Room parseToRoom(String json) {
        if(TextUtils.isEmpty(json)) return null;
        return new Gson().fromJson(json, Room.class);
    }

    public static List<Device> parseToDeviceList(String json) {
        if(TextUtils.isEmpty(json)) return null;
        List<Device> list = new Gson().fromJson(json, new TypeToken<ArrayList<Device>>() {}.getType());
        return list;
    }

    public static Device parseToDevice(String json) {
        if(TextUtils.isEmpty(json)) return null;
        return new Gson().fromJson(json, Device.class);
    }


    public static HC2 parseToHC2(String json) {
        if(TextUtils.isEmpty(json)) return null;
        return new Gson().fromJson(json, HC2.class);
    }
    public static RefreshStates parseToRefreshStates(String json) {
        if(TextUtils.isEmpty(json)) return null;
        return new Gson().fromJson(json, RefreshStates.class);
    }


    public static String parseInputStreamToString(InputStream stream) throws IOException, UnsupportedEncodingException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(
                stream, "UTF-8"), 8);
        StringBuilder sb = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        stream.close();
        return sb.toString();
    }







    public static <T> List<T> toList(String json, Class<T> typeClass)
    {
        return new Gson().fromJson(json, new ListOfJson<T>(typeClass));
    }
    public static class ListOfJson<T> implements ParameterizedType
    {
        private Class<?> wrapped;

        public ListOfJson(Class<T> wrapper)
        {
            this.wrapped = wrapper;
        }

        @Override
        public Type[] getActualTypeArguments()
        {
            return new Type[] { wrapped };
        }

        @Override
        public Type getRawType()
        {
            return List.class;
        }

        @Override
        public Type getOwnerType()
        {
            return null;
        }
    }
}
