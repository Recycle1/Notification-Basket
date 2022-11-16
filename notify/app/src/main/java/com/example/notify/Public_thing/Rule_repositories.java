package com.example.notify.Public_thing;

public class Rule_repositories {

    //public String tel;
    public String storage_name;
    public String description;
    public int active;
    public String datetime;

    public Rule_repositories(String storage_name, String description, int active,String datetime) {
        //this.tel = tel;
        this.storage_name = storage_name;
        this.description = description;
        this.active = active;
        this.datetime=datetime;
    }
}
