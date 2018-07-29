package com.br.natanfelipe.marvelapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class Comics implements Parcelable{

    String collectionURI;
    List<ComicItem> items;

    protected Comics(Parcel in) {
        //collectionURI = in.readString();
        this.items = new ArrayList<ComicItem>();
        in.readList(this.items, ComicItem.class.getClassLoader());
    }

    public static final Creator<Comics> CREATOR = new Creator<Comics>() {
        @Override
        public Comics createFromParcel(Parcel in) {
            return new Comics(in);
        }

        @Override
        public Comics[] newArray(int size) {
            return new Comics[size];
        }
    };

    public String getCollectionURI() {
        return collectionURI;
    }

    public List<ComicItem> getItems() {
        return items;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(items);
    }
}
