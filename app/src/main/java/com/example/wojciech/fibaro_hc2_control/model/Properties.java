package com.example.wojciech.fibaro_hc2_control.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Wojciech on 2015-09-07.
 */
public class Properties implements Parcelable{
    public String dead;
    public String disabled;
    public String value;


    public Properties() {
        super();
    }
    public Properties(Parcel in) {
        super();
        this.dead=in.readString();
        this.disabled=in.readString();
        this.value=in.readString();
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(dead);
        dest.writeString(disabled);
        dest.writeString(value);

    }
    public static final Parcelable.Creator<Properties> CREATOR = new Parcelable.Creator<Properties>() {

        @Override
        public Properties createFromParcel(Parcel source) {
            return new Properties(source);
        }

        @Override
        public Properties[] newArray(int size) {
            return new Properties[size];
        }
    };
}
