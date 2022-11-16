package com.example.notify.Public_thing;

import android.app.PendingIntent;

public class Pending_storage {
    public int _id;
    public PendingIntent pendingIntent;

    public Pending_storage(int _id, PendingIntent pendingIntent) {
        this._id = _id;
        this.pendingIntent = pendingIntent;
    }

}
