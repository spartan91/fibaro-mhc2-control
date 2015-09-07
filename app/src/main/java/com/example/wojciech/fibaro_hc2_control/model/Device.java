package com.example.wojciech.fibaro_hc2_control.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Wojciech on 2015-09-07.
 */
public class Device implements Parcelable {
    public int id;
    public String name;
    public int roomID;
    public String type;

    public Properties properties;
    public Actions actions;

    public int sortOrder;


    public Device() {
        super();
    }
    public Device(Parcel in) {
        super();
        this.id=in.readInt();
        this.name=in.readString();
        this.roomID=in.readInt();
        this.type=in.readString();
        this.properties=in.readParcelable(getClass().getClassLoader());
        this.actions=in.readParcelable(getClass().getClassLoader());
        //in.readArray(this.properties, Properties.CREATOR);
        //this.properties=in.createTypedArray(Properties.CREATOR);
        //in.readTypedList(this.actions,Actions.CREATOR);
        this.sortOrder=in.readInt();
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeInt(roomID);
        dest.writeString(type);
        dest.writeParcelable(properties,0);
        dest.writeParcelable(actions,1);
        dest.writeInt(sortOrder);

    }
    public static final Parcelable.Creator<Device> CREATOR = new Parcelable.Creator<Device>() {

        @Override
        public Device createFromParcel(Parcel source) {
            return new Device(source);
        }

        @Override
        public Device[] newArray(int size) {
            return new Device[size];
        }
    };
}
