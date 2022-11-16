package com.example.notify.Public_thing;

import android.content.Context;
import android.content.Intent;

import com.example.notify.R;
import com.example.notify.my_page.LoginActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Public_data {
    static public String tel=null;
    static public String name=null;
    static public ArrayList<Pending_storage> pendingIntents=new ArrayList<>();
    static public String [] action_list={"消除","降级","增强"};
    static public String [] mode_list={"正选","反选"};
    static public String [] match_list={"模糊匹配","精确匹配"};
    static public String [] vibrate_list={"无震动","震动"};
    static public String [] sound_list={"无声音","声音"};
    static public String [] postfix={"1","2","3","4","5","6","7","8","9","10","n"};
    static public String [] imp_1={"紧急","通知","全体成员"};
    static public String [] imp_3={"天气","网红","网友"};
    static public String [] theme_list={"财经","教育","房产","星座","科技","时尚","彩票","体育","游戏","时政","股票","娱乐","社会","家居"};
    static public int [] folder={R.drawable.blue_info,R.drawable.yellow_info,R.drawable.red_info,R.drawable.green_info};
    static public int [] basket_background={R.drawable.info_basket_blue,R.drawable.info_basket_yellow,R.drawable.info_basket_red,R.drawable.info_basket_green};
    static public int importance_mode=1;
    static public int theme_mode=1;
    static public String app_packagename;
    static public boolean login(Context context){
        if(Public_data.tel==null){
            Intent intent=new Intent(context, LoginActivity.class);
            context.startActivity(intent);
            return false;
        }
        return true;
    }
    static public int dip2px(Context context, float dipValue) {
        float m = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * m + 0.5f);
    }
    static public String change_string(String str, int sum){
        String str1=str;
        if(str!=null&&str.length()>sum){
            str1=str.substring(0,sum)+"...";
        }
        return str1;
    }
    static public synchronized String  getUniqueKey(){
        Random random = new Random();
        Integer number = random.nextInt(900)+100;
        return String.valueOf(System.currentTimeMillis()).substring(7,13)+ String.valueOf(number);
    }
}
