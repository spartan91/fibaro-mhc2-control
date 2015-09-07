package com.example.wojciech.fibaro_hc2_control.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Wojciech on 2015-09-07.
 */
public class HC2 implements Parcelable {
    public String serialNumber;
    public String mac;
    public String softVersion;
    public boolean beta;
    public String zwaveVersion;
    public int serverStatus;
    public String defaultLanguage;
    public String sunsetHour;
    public String sunriseHour;
    public boolean hotelMode;
    public boolean updateStableAvailable;
    public boolean updateBetaAvailable;
    public boolean batteryLowNotification;

    public HC2() {
        super();
    }
    public HC2(Parcel in) {
        super();
        this.serialNumber=in.readString();
        this.mac=in.readString();
        this.softVersion=in.readString();
        this.beta=in.readByte() != 0;
        this.zwaveVersion=in.readString();
        this.serverStatus=in.readInt();
        this.defaultLanguage=in.readString();
        this.sunsetHour=in.readString();
        this.sunriseHour=in.readString();
        this.hotelMode=in.readByte() != 0;
        this.updateStableAvailable=in.readByte() != 0;
        this.updateBetaAvailable=in.readByte() != 0;
        this.batteryLowNotification=in.readByte() != 0;
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(serialNumber);
        dest.writeString(mac);
        dest.writeString(softVersion);
        dest.writeByte((byte) (beta ? 1 : 0));
        dest.writeString(zwaveVersion);
        dest.writeInt(serverStatus);
        dest.writeString(defaultLanguage);
        dest.writeString(sunsetHour);
        dest.writeString(sunriseHour);
        dest.writeByte((byte) (hotelMode ? 1 : 0));
        dest.writeByte((byte) (updateStableAvailable ? 1 : 0));
        dest.writeByte((byte) (updateBetaAvailable ? 1 : 0));
        dest.writeByte((byte) (batteryLowNotification ? 1 : 0));

    }
    public static final Parcelable.Creator<HC2> CREATOR = new Parcelable.Creator<HC2>() {

        @Override
        public HC2 createFromParcel(Parcel source) {
            return new HC2(source);
        }

        @Override
        public HC2[] newArray(int size) {
            return new HC2[size];
        }
    };
}
