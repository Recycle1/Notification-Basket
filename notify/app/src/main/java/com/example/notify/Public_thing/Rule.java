package com.example.notify.Public_thing;

public class Rule {

    public int _id;
    public String tel;
    public String storage_name;
    public String packagename;
    public String keyword;
    public int mode;
    public int action;
    public int sound;
    public int vibrate;
    public int match;

    public Rule(int _id,String tel,String storage_name, String packagename, String keyword, int mode, int action, int sound, int vibrate, int match) {
        this._id=_id;
        this.tel=tel;
        this.storage_name = storage_name;
        this.packagename = packagename;
        this.keyword = keyword;
        this.mode = mode;
        this.action = action;
        this.sound = sound;
        this.vibrate = vibrate;
        this.match = match;
    }

    public Rule(int _id,String storage_name, String packagename, String keyword, int mode, int action, int sound, int vibrate, int match) {
        this._id=_id;
        this.storage_name = storage_name;
        this.packagename = packagename;
        this.keyword = keyword;
        this.mode = mode;
        this.action = action;
        this.sound = sound;
        this.vibrate = vibrate;
        this.match = match;
    }

}
