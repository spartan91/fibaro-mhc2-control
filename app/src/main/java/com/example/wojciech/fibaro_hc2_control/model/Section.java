package com.example.wojciech.fibaro_hc2_control.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Wojciech on 2015-09-07.
 */
public class Section implements Parcelable {
    public int id;
    public String name;
    public int sortOrder;


    public Section() {
        super();
    }
    public Section(Parcel in) {
        super();
        this.id=in.readInt();
        this.name=in.readString();
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
        dest.writeInt(sortOrder);

    }
    public static final Parcelable.Creator<Section> CREATOR = new Parcelable.Creator<Section>() {

        @Override
        public Section createFromParcel(Parcel source) {
            return new Section(source);
        }

        @Override
        public Section[] newArray(int size) {
            return new Section[size];
        }
    };
}
