package com.example.notify.Public_thing;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Public_database_method {
    static public int add(Context context1,String datetime,String title,String context, String packagename){
        Public_database helper=new Public_database(context1);
        SQLiteDatabase db=helper.getWritableDatabase();
        int max=0;
        db.execSQL("insert into info(datetime,title, context, packagename)values(?,?,?,?);",new Object[]{datetime,title,context,packagename});
        Cursor cursor=db.rawQuery("select max(_id) from info;",null);
        if(cursor!=null&&cursor.getCount()>0){
            while (cursor.moveToNext()){
                max=cursor.getInt(0);
                break;
            }
        }
        db.close();
        return max;
    }
    static public void add_rule(Context context,String storage_name,String packagename,String keyword,int mode,int action,int sound,int vibrate,int match){
        Public_database helper=new Public_database(context);
        SQLiteDatabase db=helper.getWritableDatabase();
        db.execSQL("insert into rule(storage_name,packagename, keyword, mode,action_name,sound,vibrate,match_name)values(?,?,?,?,?,?,?,?);",new Object[]{storage_name,packagename,keyword,mode,action,sound,vibrate,match});
        db.close();
    }
    static public void add_repositories(Context context,String storage_name,String description,int active,String datetime){
        Public_database helper=new Public_database(context);
        SQLiteDatabase db=helper.getWritableDatabase();
        db.execSQL("insert into rule_repositories(storage_name,description,active,datetime)values(?,?,?,?);",new Object[]{storage_name,description,active,datetime});
        db.close();
    }
    static public void update_rule(Context context,int _id,String packagename,String keyword,int mode,int action,int sound,int vibrate,int match){
        Public_database helper=new Public_database(context);
        SQLiteDatabase db=helper.getWritableDatabase();
        db.execSQL("update rule set packagename=?,keyword=?,mode=?,action_name=?,sound=?,vibrate=?,match_name=? where _id=?",new Object[]{packagename,keyword,mode,action,sound,vibrate,match,_id});
        db.close();
    }
    static public void delete(Context context, String name){
        Public_database helper=new Public_database(context);
        SQLiteDatabase db=helper.getWritableDatabase();
        db.execSQL("delete from info where name=?;",new Object[]{name});
        db.close();
    }
    static public void add_active(Context context,String storage_name){
        Public_database helper=new Public_database(context);
        SQLiteDatabase db=helper.getWritableDatabase();
        db.execSQL("update rule_repositories set active=0");
        db.execSQL("update rule_repositories set active=1 where storage_name=?",new Object[]{storage_name});
        db.close();
    }
    static public void cancel_active(Context context,String storage_name){
        Public_database helper=new Public_database(context);
        SQLiteDatabase db=helper.getWritableDatabase();
        db.execSQL("update rule_repositories set active=0 where storage_name=?",new Object[]{storage_name});
        db.close();
    }
    static public String get_active_status(Context context){
        Public_database helper=new Public_database(context);
        SQLiteDatabase db=helper.getWritableDatabase();
        String storage_name="";
        Cursor cursor=db.rawQuery("select storage_name from rule_repositories where active=1",null);
        if(cursor!=null&&cursor.getCount()>0){
            while (cursor.moveToNext()){
                storage_name=cursor.getString(0);
                break;
            }
        }
        cursor.close();
        db.close();
        return storage_name;
    }
    static public ArrayList<Notify> get(Context context){
        Public_database helper=new Public_database(context);
        SQLiteDatabase db=helper.getWritableDatabase();
        Cursor cursor=db.rawQuery("select * from info order by _id DESC;",null);
        ArrayList<Notify> notifies=new ArrayList<>();
        if(cursor!=null&&cursor.getCount()>0){
            while (cursor.moveToNext()){
                String datetime=cursor.getString(1);
                String title=cursor.getString(2);
                String text=cursor.getString(3);
                String packagename=cursor.getString(4);
                notifies.add(new Notify(datetime,packagename,title,text));
            }
        }
        cursor.close();
        db.close();
        return notifies;
    }
    //获取全部
    static public long get_sum(Context context) {
        Public_database helper=new Public_database(context);
        SQLiteDatabase db=helper.getWritableDatabase();
        Cursor cursor=db.rawQuery("select count(*) from info;",null);
        long sum=0;
        if(cursor!=null&&cursor.getCount()>0) {
            while (cursor.moveToNext()) {
                sum=cursor.getLong(0);
                break;
            }
        }
        cursor.close();
        db.close();
        System.out.println(sum);
        return sum;
    }
    static public boolean get_different(Context context) {
        Public_database helper=new Public_database(context);
        SQLiteDatabase db=helper.getWritableDatabase();
        Cursor cursor=db.rawQuery("select * from info;",null);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd");
        Date date = new Date(System.currentTimeMillis());
        String datetime=simpleDateFormat.format(date);
        if(cursor!=null&&cursor.getCount()>0) {
            if(cursor.getCount()==1){
                return false;
            }
            else{
                cursor.moveToNext();
                String str=cursor.getString(1).split("日")[0];
                if(!str.equals(datetime)){
                    return true;
                }
                return false;
            }
        }
        cursor.close();
        db.close();
        return false;
    }
    static public ArrayList<APP_info> get_app_info(Context context) {
        Public_database helper=new Public_database(context);
        SQLiteDatabase db=helper.getWritableDatabase();
        ArrayList<APP_info> list=new ArrayList<>();
        Cursor cursor=db.rawQuery("select count(*),packagename from info group by packagename order by count(*) DESC;",null);
        if(cursor!=null&&cursor.getCount()>0) {
            while (cursor.moveToNext()) {
                long number=cursor.getLong(0);
                String packagename=cursor.getString(1);
                list.add(new APP_info(packagename,number));
            }
        }
        cursor.close();
        db.close();
        return list;
    }
    static public ArrayList<APP_info> get_app_info_today(Context context) {
        Public_database helper=new Public_database(context);
        SQLiteDatabase db=helper.getWritableDatabase();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日");
        Date date = new Date(System.currentTimeMillis());
        String datetime=simpleDateFormat.format(date);
        ArrayList<APP_info> list=new ArrayList<>();
        Cursor cursor=db.rawQuery("select count(*),packagename from info where datetime like '"+datetime+"%' group by packagename order by count(*) DESC;",null);
        if(cursor!=null&&cursor.getCount()>0) {
            while (cursor.moveToNext()) {
                long number=cursor.getLong(0);
                String packagename=cursor.getString(1);
                list.add(new APP_info(packagename,number));
            }
        }
        cursor.close();
        db.close();
        return list;
    }
    static public boolean get_rule_repositories_used(Context context,String storage_name) {
        Public_database helper=new Public_database(context);
        SQLiteDatabase db=helper.getWritableDatabase();
        Cursor cursor=db.rawQuery("select * from rule_repositories where storage_name='"+storage_name+"'",null);
        if(cursor!=null&&cursor.getCount()>0) {
            return true;
        }
        cursor.close();
        db.close();
        return false;
    }
    static public ArrayList<Rule> get_rule(Context context){
        Public_database helper=new Public_database(context);
        SQLiteDatabase db=helper.getWritableDatabase();
        String c_storage_name=get_active_status(context);
        System.out.println(c_storage_name);
        if(c_storage_name==null){
            return null;
        }else{
            Cursor cursor=db.rawQuery("select * from rule where storage_name='"+c_storage_name+"'",null);
            ArrayList<Rule> rules=new ArrayList<>();
            if(cursor!=null&&cursor.getCount()>0){
                while (cursor.moveToNext()){
                    int _id=cursor.getInt(0);
                    String storage_name=cursor.getString(1);
                    String packagename=cursor.getString(2);
                    String keyword=cursor.getString(3);
                    int mode=cursor.getInt(4);
                    int action=cursor.getInt(5);
                    int sound=cursor.getInt(6);
                    int vibrate=cursor.getInt(7);
                    int match=cursor.getInt(8);
                    rules.add(new Rule(_id,storage_name,packagename,keyword,mode,action,sound,vibrate,match));
                    //Toast.makeText(this, "number:"+number+"\nname:"+name+"\nphone:"+phone, Toast.LENGTH_SHORT).show();
                }
            }
            cursor.close();
            db.close();
            return rules;
        }
    }
    static public ArrayList<Rule> get_rule_list(Context context,String c_storage_name){
        Public_database helper=new Public_database(context);
        SQLiteDatabase db=helper.getWritableDatabase();
        Cursor cursor=db.rawQuery("select * from rule where storage_name='"+c_storage_name+"';",null);
        ArrayList<Rule> rules=new ArrayList<>();
        if(cursor!=null&&cursor.getCount()>0){
            while (cursor.moveToNext()){
                int _id=cursor.getInt(0);
                String storage_name=cursor.getString(1);
                String packagename=cursor.getString(2);
                String keyword=cursor.getString(3);
                int mode=cursor.getInt(4);
                int action=cursor.getInt(5);
                int sound=cursor.getInt(6);
                int vibrate=cursor.getInt(7);
                int match=cursor.getInt(8);
                rules.add(new Rule(_id,storage_name,packagename,keyword,mode,action,sound,vibrate,match));
                //Toast.makeText(this, "number:"+number+"\nname:"+name+"\nphone:"+phone, Toast.LENGTH_SHORT).show();
            }
        }
        cursor.close();
        db.close();
        return rules;
    }
    //获取今日
    static public long getsum(Context context){
        Public_database helper=new Public_database(context);
        SQLiteDatabase db=helper.getWritableDatabase();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日");
        Date date = new Date(System.currentTimeMillis());
        String datetime=simpleDateFormat.format(date);
        Cursor cursor=db.rawQuery("select count(*) from info where datetime like '"+datetime+"%'",null);
        long count=0;
        if(cursor!=null&&cursor.getCount()>0){
            while (cursor.moveToNext()){
                count=cursor.getLong(0);
            }
        }
        cursor.close();
        db.close();
        return count;
    }
    static public ArrayList<Notify> getdate(Context context, String date){
        Public_database helper=new Public_database(context);
        SQLiteDatabase db=helper.getWritableDatabase();
        Cursor cursor=db.rawQuery("select * from info order by _id DESC;",null);
        ArrayList<Notify> notifies=new ArrayList<>();
        if(cursor!=null&&cursor.getCount()>0){
            while (cursor.moveToNext()){
                int _id=cursor.getInt(0);
                String datetime=cursor.getString(1);
                String title=cursor.getString(2);
                String text=cursor.getString(3);
                String packagename=cursor.getString(4);
                if(datetime.split("日")[0].equals(date)){
                    notifies.add(new Notify(_id,datetime,packagename,title,text));
                }
                //Toast.makeText(this, "number:"+number+"\nname:"+name+"\nphone:"+phone, Toast.LENGTH_SHORT).show();
            }
        }
        cursor.close();
        db.close();
        return notifies;
    }
    static public ArrayList<Notify> get_importance(Context context,int mode){
        Public_database helper=new Public_database(context);
        SQLiteDatabase db=helper.getWritableDatabase();
        Cursor cursor=db.rawQuery("select * from info order by _id DESC;",null);
        ArrayList<Notify> notifies=new ArrayList<>();
        if(cursor!=null&&cursor.getCount()>0){
            while (cursor.moveToNext()){
                int _id=cursor.getInt(0);
                String datetime=cursor.getString(1);
                String title=cursor.getString(2);
                String text=cursor.getString(3);
                String packagename=cursor.getString(4);

                if(mode==1){
                    if(packagename.equals("com.tencent.mobileqq")||packagename.equals("com.tencent.mm")){
                        for(String str:Public_data.imp_1){
                            if(title!=null&&title.contains(str)||text!=null&&text.contains(str)){
                                notifies.add(new Notify(_id,datetime,packagename,title,text));
                                break;
                            }
                        }
                    }
                }
                else if(mode==2){
                    boolean flag=false;
                    for(String str:Public_data.imp_1){
                        if(title!=null&&title.contains(str)||text!=null&&text.contains(str)){
                            flag=true;
                            break;
                        }
                    }
                    for(String str:Public_data.imp_3){
                        if(title!=null&&title.contains(str)||text!=null&&text.contains(str)){
                            flag=true;
                            break;
                        }
                    }
                    if(flag==false){
                        notifies.add(new Notify(_id,datetime,packagename,title,text));
                    }
                }
                else if(mode==3){
                    for(String str:Public_data.imp_3){
                        if(title!=null&&title.contains(str)||text!=null&&text.contains(str)){
                            notifies.add(new Notify(_id,datetime,packagename,title,text));
                            break;
                        }
                    }
                }

                //Toast.makeText(this, "number:"+number+"\nname:"+name+"\nphone:"+phone, Toast.LENGTH_SHORT).show();
            }
        }
        cursor.close();
        db.close();
        return notifies;
    }
    static public ArrayList<Notify> get_app(Context context, String c_packagename){
        Public_database helper=new Public_database(context);
        SQLiteDatabase db=helper.getWritableDatabase();
        String sql="select * from info where packagename= '"+c_packagename+"' order by _id DESC;";
        Cursor cursor=db.rawQuery(sql,null);
        ArrayList<Notify> notifies=new ArrayList<>();
        if(cursor!=null&&cursor.getCount()>0){
            while (cursor.moveToNext()){
                int _id=cursor.getInt(0);
                String datetime=cursor.getString(1);
                String title=cursor.getString(2);
                String text=cursor.getString(3);
                String packagename=cursor.getString(4);
                notifies.add(new Notify(_id,datetime,packagename,title,text));
                //Toast.makeText(this, "number:"+number+"\nname:"+name+"\nphone:"+phone, Toast.LENGTH_SHORT).show();
            }
        }
        cursor.close();
        db.close();
        return notifies;
    }
    static public ArrayList<String> get_app_list(Context context){
        Public_database helper=new Public_database(context);
        SQLiteDatabase db=helper.getWritableDatabase();
        ArrayList <String> list=new ArrayList<>();
        Cursor cursor=db.rawQuery("select packagename from info group by packagename",null);
        if(cursor!=null&&cursor.getCount()>0){
            while (cursor.moveToNext()){
                list.add(cursor.getString(0));
            }
        }
        cursor.close();
        db.close();
        return list;
    }
    static public String getstart(Context context){
        String re="";
        Public_database helper=new Public_database(context);
        SQLiteDatabase db=helper.getWritableDatabase();
        Cursor cursor=db.rawQuery("select datetime from info where _id=1;",null);
        if(cursor!=null&&cursor.getCount()>0){
            while (cursor.moveToNext()){
                String datetime=cursor.getString(0);

                return datetime;
            }
        }
        cursor.close();
        db.close();
        return re;
    }
    static public Rule_repositories get_active(Context context){
        Public_database helper=new Public_database(context);
        SQLiteDatabase db=helper.getWritableDatabase();
        Cursor cursor=db.rawQuery("select * from rule_repositories where active=1;",null);
        if(cursor!=null&&cursor.getCount()>0){
            while (cursor.moveToNext()){
                String storage_name=cursor.getString(0);
                String description=cursor.getString(1);
                int active=cursor.getInt(2);
                String datetime=cursor.getString(3);
                return new Rule_repositories(storage_name,description,active,datetime);
            }
        }
        cursor.close();
        db.close();
        return null;
    }
    static public ArrayList<Rule_repositories> get_repositories(Context context){
        Public_database helper=new Public_database(context);
        SQLiteDatabase db=helper.getWritableDatabase();
        ArrayList<Rule_repositories> rule_repositories=new ArrayList<>();
        Cursor cursor=db.rawQuery("select * from rule_repositories order by datetime DESC;",null);
        if(cursor!=null&&cursor.getCount()>0){
            while (cursor.moveToNext()){
                String storage_name=cursor.getString(0);
                String description=cursor.getString(1);
                int active=cursor.getInt(2);
                String datetime=cursor.getString(3);
                rule_repositories.add(new Rule_repositories(storage_name,description,active,datetime));
            }
        }
        cursor.close();
        db.close();
        return rule_repositories;
    }
    static public boolean delete_rule_repositories(Context context,String storage_name){
        Public_database helper=new Public_database(context);
        SQLiteDatabase db=helper.getWritableDatabase();
        db.execSQL("delete from rule_repositories where storage_name=?;",new Object[]{storage_name});
        db.close();
        return true;
    }
    static public boolean delete_rule(Context context, String storage_name,int _id){
        Public_database helper=new Public_database(context);
        SQLiteDatabase db=helper.getWritableDatabase();
        db.execSQL("delete from rule where storage_name=? and _id=?;",new Object[]{storage_name,_id});
        db.close();
        return true;
    }
    static public boolean delete_notify(Context context,int _id){
        Public_database helper=new Public_database(context);
        SQLiteDatabase db=helper.getWritableDatabase();
        db.execSQL("delete from info where _id=?;",new Object[]{_id});
        db.close();
        return true;
    }
}
