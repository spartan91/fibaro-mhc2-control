package com.example.wojciech.fibaro_hc2_control.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Wojciech on 2015-09-07.
 */
public class Actions implements Parcelable{
    public int setValue;
    public int turnOff;
    public int turnOn;

    public Actions() {
        super();
    }
    public Actions(Parcel in) {
        super();
        this.setValue=in.readInt();
        this.turnOff=in.readInt();
        this.turnOn=in.readInt();
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(setValue);
        dest.writeInt(turnOff);
        dest.writeInt(turnOn);

    }
    public static final Parcelable.Creator<Actions> CREATOR = new Parcelable.Creator<Actions>() {

        @Override
        public Actions createFromParcel(Parcel source) {
            return new Actions(source);
        }

        @Override
        public Actions[] newArray(int size) {
            return new Actions[size];
        }
    };
}
