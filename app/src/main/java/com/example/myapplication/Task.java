package com.example.myapplication;

import android.os.Parcelable;
import android.os.Parcel;
public class Task implements Parcelable {
    private long id;
    private String title;
    private String description;
    private boolean completed;

    public Task(String title, String description, boolean completed,long id) {
        this.title = title;
        this.description = description;
        this.completed = completed;
        this.id=id;
    }
    protected Task(Parcel in) {
        id = in.readLong();
        title = in.readString();
        description = in.readString();
        completed = in.readByte() != 0;
    }
    public static final Creator<Task> CREATOR = new Creator<Task>() {
        @Override
        public Task createFromParcel(Parcel in) {
            return new Task(in);
        }

        @Override
        public Task[] newArray(int size) {
            return new Task[size];
        }
    };
    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public long getId() {
        return id;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(title);
        dest.writeString(description);
        dest.writeByte((byte) (completed ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
