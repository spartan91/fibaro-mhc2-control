package com.example.wojciech.fibaro_hc2_control.utils;

/**
 * Created by Wojciech on 2015-09-07.
 */
public class HC2Actions {
    private final static String SERVER_ADDRESS = "http://217.168.133.115:8080";
    private final static String SETTINGS_INFO = "/api/settings/info";
    private final static String SECTIONS = "/api/sections";
    private final static String ROOMS = "/api/rooms";
    private final static String DEVICES = "/api/devices";
    private final static String DEVICE = "?id=";
    private final static String CALL_ACTION = "/api/callAction";
    private final static String CALL_ACTION_DEVICE_ID = "deviceID=";
    private final static String CALL_ACTION_NAME = "name=";
    private final static String CALL_ACTION_ARG1 = "arg1=";
    private final static String CALL_ACTION_ARG2 = "arg2=";
    private final static String CALL_ACTION_ARG3 = "arg3=";
    private final static String VALUE_CALL_ACTION_SET_VALUE = "setValue";
    private final static String VALUE_CALL_ACTION_TURN_ON = "turnOn";
    private final static String VALUE_CALL_ACTION_TURN_OFF = "turnOff";
    private final static String REFRESH_STATES = "/api/refreshStates";
    private final static String REFRESH_STATES_LAST = "last=";

    public static String getSettingsInfo() {
        return SERVER_ADDRESS + SETTINGS_INFO;
    }

    /*GET Lists*/
    public static String getSections() {
        return SERVER_ADDRESS + SECTIONS;
    }

    public static String getRooms() {
        return SERVER_ADDRESS + ROOMS;
    }

    public static String getDevices() {
        return SERVER_ADDRESS + DEVICES;
    }

    /*GET Lists*/
    public static String getSection(int id) {
        return SERVER_ADDRESS + SECTIONS + "/" + id;
    }

    public static String getRoom(int id) {
        return SERVER_ADDRESS + ROOMS + "/" + id;
    }

    public static String getDevice(int id) {
        return SERVER_ADDRESS + DEVICES
                + DEVICE+ id;
    }

    /*CallAction*/
    public static String getCallActionSwitch(int id, boolean action) {
        if (action)
            return SERVER_ADDRESS + CALL_ACTION
                    + "?" + CALL_ACTION_DEVICE_ID + id
                    + "&" + CALL_ACTION_NAME + VALUE_CALL_ACTION_TURN_ON;
        else
            return SERVER_ADDRESS + CALL_ACTION
                    + "?" + CALL_ACTION_DEVICE_ID + id
                    + "&" + CALL_ACTION_NAME + VALUE_CALL_ACTION_TURN_OFF;
    }

    public static String getCallActionDimm(int id, int value) {
        return SERVER_ADDRESS + CALL_ACTION
                + "?" + CALL_ACTION_DEVICE_ID + id
                + "&" + CALL_ACTION_NAME + VALUE_CALL_ACTION_SET_VALUE
                + "&" + CALL_ACTION_ARG1 + value;
    }

    /*CallAction*/
    public static String getRefreshStatestes() {
        return SERVER_ADDRESS + REFRESH_STATES;
    }
    public static String getRefreshStatestes(int lastId) {
        return SERVER_ADDRESS + REFRESH_STATES
                + "?" + REFRESH_STATES_LAST + lastId;
    }

}
