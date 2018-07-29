package com.br.natanfelipe.marvelapp.model;

import android.os.Parcel;
import android.os.Parcelable;

public class ComicItem implements Parcelable {
    String resourceUri;
    String name;

    protected ComicItem(Parcel in) {
        resourceUri = in.readString();
        name = in.readString();
    }

    public static final Creator<ComicItem> CREATOR = new Creator<ComicItem>() {
        @Override
        public ComicItem createFromParcel(Parcel in) {
            return new ComicItem(in);
        }

        @Override
        public ComicItem[] newArray(int size) {
            return new ComicItem[size];
        }
    };

    public String getResourceUri() {
        return resourceUri;
    }

    public String getName() {
        return name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.resourceUri);
    }
}
