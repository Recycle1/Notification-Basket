package com.example.notify.Public_thing;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

import java.io.File;

public class Public_database extends SQLiteOpenHelper {
    public Public_database(Context context) {
        super(context, Environment.getExternalStorageDirectory()+"/notify_basket/basket_database", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table info (_id integer primary key autoincrement,datetime varchar(25),title varchar(30), context varchar(75), packagename varchar(35))");
        db.execSQL("create table rule (_id integer primary key autoincrement,storage_name varchar(25),packagename varchar(25),keyword varchar(25), mode int,action_name int, sound int,vibrate int,match_name int)");
        db.execSQL("create table rule_repositories (storage_name varchar(25),description varchar(50),active int,datetime varchar(25))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

