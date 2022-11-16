package com.example.notify;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.os.Vibrator;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.bumptech.glide.load.engine.Resource;
import com.example.notify.Public_thing.Package_info;
import com.example.notify.Public_thing.Pending_storage;
import com.example.notify.Public_thing.Public_data;
import com.example.notify.Public_thing.Public_database_method;
import com.example.notify.Public_thing.Rule;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Notify_service1 extends NotificationListenerService {

    public final  static String TAG=Notify_service1.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();
        System.out.println("连接");
        //Toast.makeText(this, "1", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onListenerConnected() {
        super.onListenerConnected();
        Log.d(TAG,"onListenerConnected");
    }

    @Override
    public void onListenerDisconnected() {
        super.onListenerDisconnected();
        Log.d(TAG,"onListenerDisconnected");
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {

        super.onNotificationPosted(sbn);
        //Toast.makeText(this, "1235", Toast.LENGTH_SHORT).show();
        NotificationManager notificationManager= (NotificationManager) getApplication().getSystemService(NOTIFICATION_SERVICE);
        System.out.println(sbn.getNotification().extras.getString(Notification.EXTRA_TITLE));

        String packagename=sbn.getPackageName();
        System.out.println(packagename);
        String title=sbn.getNotification().extras.getString(Notification.EXTRA_TITLE);
        String text=sbn.getNotification().extras.getString(Notification.EXTRA_TEXT);
        sbn.getNotification().getChannelId();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());
        String datetime=simpleDateFormat.format(date);
        if(title!=null&&text!=null&&(!packagename.equals("android"))&&sbn.getNotification().bigContentView==null&&!text.contains("语音通话")&&(!sbn.getNotification().getChannelId().equals("notify_flag"))){
            int _id=Public_database_method.add(this,datetime,title,text,packagename);
            Public_data.pendingIntents.add(new Pending_storage(_id,sbn.getNotification().contentIntent));
        }

        PackageManager packageManager=getPackageManager();
        PackageInfo info = null;
        try {
            info = packageManager.getPackageInfo(packagename,PackageManager.GET_ACTIVITIES);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String name=info.applicationInfo.loadLabel(packageManager).toString();
        ArrayList<Rule> rules=Public_database_method.get_rule(this);
        if(rules!=null&&(!sbn.getNotification().getChannelId().equals("notify_flag"))){
            for(int i=0;i<rules.size();i++){
                System.out.println(rules.get(i).packagename);
                if(rules.get(i).packagename.equals(name)||rules.get(i).packagename.equals("所有APP")){
                    if(rules.get(i).match==0){
                        if(rules.get(i).mode==0){
                            if((title!=null&&title.contains(rules.get(i).keyword))||(text!=null&&text.contains(rules.get(i).keyword))){
                                notificationManager.deleteNotificationChannel(sbn.getNotification().getChannelId());
                                notificationManager.cancel(sbn.getTag(),sbn.getId());
                                notificationManager.cancel(sbn.getId());
                                cancelNotification(sbn.getKey());
                                if(rules.get(i).action!=0){
                                    notification(sbn,rules.get(i).action,rules.get(i).sound,rules.get(i).vibrate);
                                }
                                break;
                            }
                        }
                        else if(rules.get(i).mode==1){
                            if(title!=null&&!title.contains(rules.get(i).keyword)&&text!=null&&!text.contains(rules.get(i).keyword)){
                                notificationManager.deleteNotificationChannel(sbn.getNotification().getChannelId());
                                notificationManager.cancel(sbn.getTag(),sbn.getId());
                                notificationManager.cancel(sbn.getId());
                                cancelNotification(sbn.getKey());
                                if(rules.get(i).action!=0){
                                    notification(sbn,rules.get(i).action,rules.get(i).sound,rules.get(i).vibrate);
                                }
                                break;
                            }
                        }

                    }
                    else if(rules.get(i).match==1){
                        if(rules.get(i).mode==0){
                            if((title!=null&&title.equals(rules.get(i).keyword))||(text!=null&&text.equals(rules.get(i).keyword))){
                                notificationManager.deleteNotificationChannel(sbn.getNotification().getChannelId());
                                notificationManager.cancel(sbn.getTag(),sbn.getId());
                                notificationManager.cancel(sbn.getId());
                                cancelNotification(sbn.getKey());
                                if(rules.get(i).action!=0){
                                    notification(sbn,rules.get(i).action,rules.get(i).sound,rules.get(i).vibrate);
                                }
                                break;
                            }
                        }
                        else if(rules.get(i).mode==1){
                            if(title!=null&&!title.equals(rules.get(i).keyword)&&text!=null&&!text.equals(rules.get(i).keyword)){
                                notificationManager.deleteNotificationChannel(sbn.getNotification().getChannelId());
                                notificationManager.cancel(sbn.getTag(),sbn.getId());
                                notificationManager.cancel(sbn.getId());
                                cancelNotification(sbn.getKey());
                                if(rules.get(i).action!=0){
                                    notification(sbn,rules.get(i).action,rules.get(i).sound,rules.get(i).vibrate);
                                }
                                break;
                            }
                        }
                    }
                }
            }
        }

    }

    private Activity getActivity(Context context) {
        while (!(context instanceof Activity) && context instanceof ContextWrapper) {
            context = ((ContextWrapper) context).getBaseContext();
        }

        if (context instanceof Activity) {
            return (Activity) context;
        }
        return null;
    }

    /**
     * 创建文件夹---之所以要一层层创建，是因为一次性创建多层文件夹可能会失败！
     */
    public static boolean createFileDir(File dirFile) {
        if (dirFile == null) return true;
        if (dirFile.exists()) {
            return true;
        }
        File parentFile = dirFile.getParentFile();
        if (parentFile != null && !parentFile.exists()) {
            //父文件夹不存在，则先创建父文件夹，再创建自身文件夹
            return createFileDir(parentFile) && createFileDir(dirFile);
        } else {
            boolean mkdirs = dirFile.mkdirs();
            boolean isSuccess = mkdirs || dirFile.exists();
            if (!isSuccess) {
                System.out.println("createFileDir fail " + dirFile);
            }
            return isSuccess;
        }
    }

    public static File createFile(String dirPath, String fileName) {
        try {
            File dirFile = new File(dirPath);
            if (!dirFile.exists()) {
                if (!createFileDir(dirFile)) {
                    System.out.println("createFile dirFile.mkdirs fail");
                    return null;
                }
            } else if (!dirFile.isDirectory()) {
                boolean delete = dirFile.delete();
                if (delete) {
                    return createFile(dirPath, fileName);
                } else {
                    System.out.println("createFile dirFile !isDirectory and delete fail");
                    return null;
                }
            }
            File file = new File(dirPath, fileName);
            if (!file.exists()) {
                if (!file.createNewFile()) {
                    System.out.println("createFile createNewFile fail");
                    return null;
                }
            }
            return file;
        } catch (Exception e) {
            System.out.println("createFile fail :" + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }


    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        super.onNotificationRemoved(sbn);
        System.out.println("12");
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    void notification(StatusBarNotification sbn,int action,int sound,int vibrate) {

        //创建通知消息管理类
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Notification notification1=new Notification.Builder(getApplicationContext(),"notify_flag")
                .setContentTitle(sbn.getNotification().extras.getString(Notification.EXTRA_TITLE))
                .setContentText(sbn.getNotification().extras.getString(Notification.EXTRA_TEXT))
                .setWhen(0)
                .setSmallIcon(sbn.getNotification().getSmallIcon())
                .setLargeIcon(sbn.getNotification().getLargeIcon())
                .setContentIntent(sbn.getNotification().contentIntent)
                .build();

        NotificationChannel channel;
        if(action==1){
            channel=new NotificationChannel("notify_flag", "1235",NotificationManager.IMPORTANCE_MIN);
        }
        else{
            channel=new NotificationChannel("notify_flag", "1235", NotificationManager.IMPORTANCE_HIGH);
        }
        channel.enableLights(false);
        channel.setLightColor(R.color.white);

        if(sound==1){
            MediaPlayer mediaPlayer=MediaPlayer.create(getApplicationContext(),R.raw.notify);
            mediaPlayer.start();
        }

        if(vibrate==1){
            Vibrator vibrator = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);
            long[] vibrationPattern = new long[]{0, 180, 80, 120};
            // 第一个参数为开关开关的时间，第二个参数是重复次数，振动需要添加权限
            vibrator.vibrate(vibrationPattern, -1);
        }

        manager.createNotificationChannel(channel);
        manager.notify(sbn.getId(),notification1);
    }



}
