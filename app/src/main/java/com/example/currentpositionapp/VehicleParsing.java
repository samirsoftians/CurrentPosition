package com.example.currentpositionapp;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Simple container object for contact data
 *
 * Created by mgod on 9/12/13.
 * @author mgod
 */
class VehicleParsing implements Parcelable {

        public String getVehicle() {
            return vehicle;
        }

        public void setVehicle(String vehicle) {
            this.vehicle = vehicle;
        }

        private String vehicle;


        VehicleParsing(String n) {
            vehicle = n;

        }

        @Override
        public String toString() { return vehicle; }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.vehicle);
        }

        private VehicleParsing(Parcel in) {
            this.vehicle = in.readString();

        }

        public static final Creator<VehicleParsing> CREATOR = new Creator<VehicleParsing>() {
            @Override
            public VehicleParsing createFromParcel(Parcel source) {
                return new VehicleParsing(source);
            }

            @Override
            public VehicleParsing[] newArray(int size) {
                return new VehicleParsing[size];
            }
        };
    }