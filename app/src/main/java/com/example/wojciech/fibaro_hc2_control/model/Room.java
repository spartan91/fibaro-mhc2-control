package com.example.wojciech.fibaro_hc2_control.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Wojciech on 2015-09-07.
 */
public class Room implements Parcelable {
    public int id;
    public String name;
    public int sectionID;
    public int sortOrder;


    public Room() {
        super();
    }
    public Room(Parcel in) {
        super();
        this.id=in.readInt();
        this.name=in.readString();
        this.sectionID=in.readInt();
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
        dest.writeInt(sectionID);
        dest.writeInt(sortOrder);

    }
    public static final Parcelable.Creator<Room> CREATOR = new Parcelable.Creator<Room>() {

        @Override
        public Room createFromParcel(Parcel source) {
            return new Room(source);
        }

        @Override
        public Room[] newArray(int size) {
            return new Room[size];
        }
    };
}
