package com.example.wojciech.fibaro_hc2_control.service;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;

import com.example.wojciech.fibaro_hc2_control.MainActivity;
import com.example.wojciech.fibaro_hc2_control.model.Device;
import com.example.wojciech.fibaro_hc2_control.model.HC2;
import com.example.wojciech.fibaro_hc2_control.model.RefreshStates;
import com.example.wojciech.fibaro_hc2_control.model.Room;
import com.example.wojciech.fibaro_hc2_control.model.Section;
import com.example.wojciech.fibaro_hc2_control.utils.ConnectivityUtil;
import com.example.wojciech.fibaro_hc2_control.utils.DataParserUtil;
import com.example.wojciech.fibaro_hc2_control.utils.HC2Actions;

import java.io.IOException;
import java.io.InputStream;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

/**
 * Created by Wojciech on 2015-09-07.
 */
public class ServiceHC2 extends IntentService {
    public static final String TAG = ServiceHC2.class.getSimpleName();
    public static final String EXTRA_REQUESTED_ACTION = "action";
    public static final String EXTRA_REQUESTED_OBJECT = "object";
    public static final String EXTRA_REQUESTED_VALUE = "value";

    public static final String USER = "admin";
    public static final String PASS = "admin";

    public static final String RESULT_CODE = "result_code";
    public static final String RESULT_ACTION = "result_action";
    public static final String RESULT_PARCEL = "result_parcel";


    public enum ServiceHC2Action {
        Update(0), GetSection(1), GetSections(2), GetRoom(3), GetRooms(4), GetDevice(5), GetDevices(6), CallActionSwitch(7), CallActionDimm(8), RefreshStates(9), GetInfo(10);

        private final int value;

        ServiceHC2Action(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public static ServiceHC2Action fromInteger(int x) {
            switch (x) {
                case 0:
                    return Update;
                case 1:
                    return GetSection;
                case 2:
                    return GetSections;
                case 3:
                    return GetRoom;
                case 4:
                    return GetRooms;
                case 5:
                    return GetDevice;
                case 6:
                    return GetDevices;
                case 7:
                    return CallActionSwitch;
                case 8:
                    return CallActionDimm;
                case 9:
                    return RefreshStates;
                case 10:
                    return GetInfo;
                default:
                    return Update;
            }
        }

    }

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public ServiceHC2(String name) {
        super(name);
    }

    public ServiceHC2() {
        super(TAG);
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG, "Service running " + System.currentTimeMillis());
        if (ConnectivityUtil.isNetworkAvailable(this)) {
            int req = intent.getIntExtra(EXTRA_REQUESTED_ACTION, 0);
            ServiceHC2Action action = ServiceHC2Action.fromInteger(req);
            try {
                switch (action) {
                    case Update:
                        getDevices(true);
                        break;
                    case GetSection://Need to get rooms for that
                        getSection(intent);
                        break;
                    case GetSections:
                        getSections();
                        break;
                    case GetRoom://Need to get devices for that
                        getRoom(intent);
                        break;
                    case GetRooms:
                        getRooms();
                        break;
                    case GetDevice:
                        break;
                    case GetDevices:
                        getDevices(false);
                        break;
                    case CallActionSwitch:
                        getActionCallSwitch(intent);
                        break;
                    case CallActionDimm:
                        getCallActionDimm(intent);
                        break;
                    case GetInfo:
                        getInfo();
                        break;
                    default:
                        break;
                }
                update();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void updateUiDevices(ArrayList<Device> arrayList) {
        Intent in = new Intent(MainActivity.ACTION);
        in.putExtra(RESULT_CODE, Activity.RESULT_OK);
        in.putExtra(RESULT_ACTION, ServiceHC2Action.GetDevice.getValue());
        in.putExtra(RESULT_PARCEL, arrayList);
        LocalBroadcastManager.getInstance(ServiceHC2.this).sendBroadcast(in);
    }


    private void updateDevice(int deviceID) {
        Log.d(TAG, "updateDevice id: " + deviceID);
        String result = null;
        try {
            result = getUrl(HC2Actions.getDevice(deviceID), 15000);
            if (!TextUtils.isEmpty(result)) {
                Device dev = DataParserUtil.parseToDevice(result);
                ArrayList<Device> arrayList = new ArrayList<>();
                arrayList.add(dev);
                updateUiDevices(arrayList);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void update() {
        runRefresh();
    }

    static boolean isRunning = false;

    private void runRefresh() {
        Log.d(TAG, "runRefresh " + isRunning);
        if (!isRunning)
            Executors.newSingleThreadExecutor().execute(new Runnable() {
                @Override
                public void run() {
                    isRunning = true;
                    Log.d(TAG, "runRefresh run");
                    String result;
                    try {
                        result = getUrl(HC2Actions.getRefreshStatestes(), 31000);
                        if (!TextUtils.isEmpty(result)) {
                            RefreshStates hc2 = DataParserUtil.parseToRefreshStates(result);
                            if (hc2 != null) {
                                Log.d(TAG, "runRefresh state last: " + hc2.last);

                                result = getUrl(HC2Actions.getRefreshStatestes(hc2.last), 31000);
                                hc2 = DataParserUtil.parseToRefreshStates(result);
                                if (hc2 != null && hc2.changes.size() > 0) {
                                    Log.d(TAG, "runRefresh state ID: " + hc2.changes.get(0).id + " val: " + hc2.changes.get(0).value);
                                    updateDevice(hc2.changes.get(0).id);
                                }

                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    update();//may perform update from this thread
                    isRunning = false;
                }
            });
    }

    private void getInfo() throws IOException {
        String result;
        result = getUrl(HC2Actions.getSettingsInfo(), 20000);
        if (!TextUtils.isEmpty(result)) {
            HC2 hc2 = DataParserUtil.parseToHC2(result);
            if (hc2 != null) {
                Intent in = new Intent(MainActivity.ACTION);
                in.putExtra(RESULT_CODE, Activity.RESULT_OK);
                in.putExtra(RESULT_ACTION, ServiceHC2Action.GetInfo.getValue());
                in.putExtra(RESULT_PARCEL, hc2);
                LocalBroadcastManager.getInstance(this).sendBroadcast(in);
            }
        }
    }

    private void getCallActionDimm(Intent intent) {
        String result;
        try {
            Device d = intent.getParcelableExtra(EXTRA_REQUESTED_OBJECT);
            int progress = intent.getIntExtra(EXTRA_REQUESTED_VALUE, 50);
            if (d != null) {
                result = getUrl(HC2Actions.getCallActionDimm(d.id, progress), 20000);

                if (!TextUtils.isEmpty(result)) {
                    Log.d(TAG, result);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void getActionCallSwitch(Intent intent) {
        String result;
        try {
            Device d = intent.getParcelableExtra(EXTRA_REQUESTED_OBJECT);
            boolean val = intent.getBooleanExtra(EXTRA_REQUESTED_VALUE, false);
            if (d != null) {
                result = getUrl(HC2Actions.getCallActionSwitch(d.id, val), 20000);

                if (!TextUtils.isEmpty(result)) {
                    Log.d(TAG, result);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void getDevices(boolean withUpdate) throws IOException {
        String result;
        result = getUrl(HC2Actions.getDevices(), 20000);
        if (!TextUtils.isEmpty(result)) {
            ArrayList<Device> list = new ArrayList<>();
            List<Device> device = DataParserUtil.parseToDeviceList(result);
            if (device != null) {
                list.addAll(device);
                if (list.size() > 0) {
                    Intent in = new Intent(MainActivity.ACTION);
                    in.putExtra(RESULT_CODE, Activity.RESULT_OK);
                    if(withUpdate) in.putExtra(RESULT_ACTION, ServiceHC2Action.GetDevice.getValue());
                    else in.putExtra(RESULT_ACTION, ServiceHC2Action.GetDevices.getValue());
                    in.putParcelableArrayListExtra(RESULT_PARCEL, list);
                    LocalBroadcastManager.getInstance(this).sendBroadcast(in);
                }
            }
        }
    }

    private void getRooms() throws IOException {
        String result;
        result = getUrl(HC2Actions.getRooms(), 20000);
        if (!TextUtils.isEmpty(result)) {
            ArrayList<Room> list = new ArrayList<>();
            List<Room> rooms = DataParserUtil.parseToRoomList(result);
            if (rooms != null) {
                list.addAll(rooms);
                if (list.size() > 0) {
                    Intent in = new Intent(MainActivity.ACTION);
                    in.putExtra(RESULT_CODE, Activity.RESULT_OK);
                    in.putExtra(RESULT_ACTION, ServiceHC2Action.GetRooms.getValue());
                    in.putParcelableArrayListExtra(RESULT_PARCEL, list);
                    LocalBroadcastManager.getInstance(this).sendBroadcast(in);
                }
            }
        }
    }

    private void getRoom(Intent intent) throws IOException {
        String result;
        Room r = intent.getParcelableExtra(EXTRA_REQUESTED_OBJECT);
        result = getUrl(HC2Actions.getDevices(), 20000);
        if (!TextUtils.isEmpty(result)) {
            List<Device> devices = DataParserUtil.parseToDeviceList(result);
            if (devices != null && devices.size() > 0) {
                ArrayList<Device> devicesToShow = new ArrayList<>();
                for (Device d : devices) {
                    if (d.roomID == r.id) {
                        devicesToShow.add(d);
                    }
                }
                if (devicesToShow.size() > 0) {
                    Intent in = new Intent(MainActivity.ACTION);
                    in.putExtra(RESULT_CODE, Activity.RESULT_OK);
                    in.putExtra(RESULT_ACTION, ServiceHC2Action.GetRoom.getValue());
                    in.putParcelableArrayListExtra(RESULT_PARCEL, devicesToShow);
                    LocalBroadcastManager.getInstance(this).sendBroadcast(in);

                }
            }
        }
    }

    private void getSections() throws IOException {
        String result;
        result = getUrl(HC2Actions.getSections(), 20000);
        if (!TextUtils.isEmpty(result)) {
            ArrayList<Section> list = new ArrayList<>();
            List<Section> sectionList = DataParserUtil.parseToSectionList(result);
            if (sectionList != null) {
                list.addAll(sectionList);
                if (sectionList.size() > 0) {
                    Intent in = new Intent(MainActivity.ACTION);
                    in.putExtra(RESULT_CODE, Activity.RESULT_OK);
                    in.putExtra(RESULT_ACTION, ServiceHC2Action.GetSections.getValue());
                    in.putParcelableArrayListExtra(RESULT_PARCEL, list);
                    LocalBroadcastManager.getInstance(this).sendBroadcast(in);
                }
            }
        }
    }

    private void getSection(Intent intent) throws IOException {
        String result;
        Section s = intent.getParcelableExtra(EXTRA_REQUESTED_OBJECT);
        result = getUrl(HC2Actions.getRooms(), 20000);
        if (!TextUtils.isEmpty(result)) {
            List<Room> rooms = DataParserUtil.parseToRoomList(result);
            if (rooms != null && rooms.size() > 0) {
                ArrayList<Room> roomsToShow = new ArrayList<>();
                for (Room r : rooms) {
                    if (r.sectionID == s.id) {
                        roomsToShow.add(r);
                    }
                }
                if (roomsToShow.size() > 0) {
                    Intent in = new Intent(MainActivity.ACTION);
                    in.putExtra(RESULT_CODE, Activity.RESULT_OK);
                    in.putExtra(RESULT_ACTION, ServiceHC2Action.GetSection.getValue());
                    in.putParcelableArrayListExtra(RESULT_PARCEL, roomsToShow);
                    LocalBroadcastManager.getInstance(this).sendBroadcast(in);

                }
            }
        }
    }

    private String getUrl(String uRl, int connTimeout) throws IOException {
        InputStream is = null;
        Authenticator.setDefault(new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(USER, PASS.toCharArray());
            }
        });
        try {
            URL url = new URL(uRl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(connTimeout /* milliseconds */);
            conn.setConnectTimeout(connTimeout /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.connect();
            int response = conn.getResponseCode();
            Log.d(TAG, "The response is: " + response);
            is = conn.getInputStream();

            if (is != null)
                return DataParserUtil.parseInputStreamToString(is);
            return null;

        } finally {
            if (is != null) {
                is.close();
            }
        }
    }


}
