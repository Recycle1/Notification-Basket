package com.example.notify.Public_thing;

public class Notify {
    public int _id;
    public String datetime;
    public String packagename;
    public String title;
    public String text;

    public Notify(String datetime,String packagename, String title, String text) {
        this.datetime = datetime;
        this.packagename = packagename;
        this.title = title;
        this.text = text;
    }

    public Notify(int _id,String datetime,String packagename, String title, String text) {
        this._id=_id;
        this.datetime = datetime;
        this.packagename = packagename;
        this.title = title;
        this.text = text;
    }
}
