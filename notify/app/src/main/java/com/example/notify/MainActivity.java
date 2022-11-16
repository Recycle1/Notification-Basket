package com.example.notify;

import static android.app.Notification.EXTRA_CHANNEL_ID;
import static android.provider.Settings.EXTRA_APP_PACKAGE;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.notify.Public_thing.Public_database;
import com.example.notify.basket_page.Fragment_basket;
import com.example.notify.interest_page.Fragment_interest;
import com.example.notify.main_page.Fragment_main;
import com.example.notify.my_page.Fragment_my;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity{

    private static final int REQUEST_CODE = 1024;

    private RelativeLayout rl_main;
    private RelativeLayout rl_basket;
    private RelativeLayout rl_interest;
    private RelativeLayout rl_my;

    private ImageView iv_main;
    private ImageView iv_basket;
    private ImageView iv_interest;
    private ImageView iv_my;

    private TextView tv_main;
    private TextView tv_basket;
    private TextView tv_interest;
    private TextView tv_my;

    private Fragment fragment;
    private Fragment_main fragment_main;
    private Fragment_basket fragment_basket;
    private Fragment_interest fragment_interest;
    private Fragment_my fragment_my;

    private Button button1;
    private Button button2;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rl_main=findViewById(R.id.rl_main);
        rl_basket=findViewById(R.id.rl_basket);
        rl_interest=findViewById(R.id.rl_interest);
        rl_my=findViewById(R.id.rl_my);
        iv_main=findViewById(R.id.iv_main);
        iv_basket=findViewById(R.id.iv_basket);
        iv_interest=findViewById(R.id.iv_interest);
        iv_my=findViewById(R.id.iv_my);
        tv_main=findViewById(R.id.tv_main);
        tv_basket=findViewById(R.id.tv_basket);
        tv_interest=findViewById(R.id.tv_interest);
        tv_my=findViewById(R.id.tv_my);

        button1=findViewById(R.id.btn1);
        button2=findViewById(R.id.btn2);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Notify_service1.class);
                toggleNotificationListenerService(MainActivity.this);
                startService(intent);
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Notify_service1.class);
                stopService(intent);
            }
        });

        requestPermission();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.VIBRATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.VIBRATE}, 1);
        }
        //ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 222);


        init_button();
        //init_fragment();
        checkNotify();

        createFileDir(new File(Environment.getExternalStorageDirectory() + "/notify_basket"));

        if (NotificationManagerCompat.getEnabledListenerPackages(this).contains(getPackageName())) {

            toggleNotificationListenerService(this);
//            startService(new Intent(this, Notify_service.class));

        }else {

// 去开启 监听通知权限

            startActivity(new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"));

        }

    }

    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // 先判断有没有权限
            if (Environment.isExternalStorageManager()) {
                init_fragment();
            } else {
                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                intent.setData(Uri.parse("package:" + this.getPackageName()));
                startActivityForResult(intent, REQUEST_CODE);
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // 先判断有没有权限
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                init_fragment();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);
            }
        } else {
            init_fragment();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (Environment.isExternalStorageManager()) {
                init_fragment();
            } else {
                Toast.makeText(this, "存储权限未获取", Toast.LENGTH_SHORT).show();
            }
        }
    }

    void init_button(){
        rl_main.setOnClickListener(new Onclick());
        rl_basket.setOnClickListener(new Onclick());
        rl_interest.setOnClickListener(new Onclick());
        rl_my.setOnClickListener(new Onclick());
    }

    void init_fragment(){
        fragment_main=Fragment_main.newInstance();
        fragment_basket=Fragment_basket.newInstance();
        fragment_interest=Fragment_interest.newInstance();
        fragment_my=Fragment_my.newInstance();
        getFragmentManager().beginTransaction().add(R.id.fragment_content,fragment_main,"content").commitAllowingStateLoss();
    }

    class Onclick implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.rl_main:
                    iv_main.setBackground(getDrawable(R.drawable.main_active));
                    iv_basket.setBackground(getDrawable(R.drawable.basket));
                    iv_interest.setBackground(getDrawable(R.drawable.interest));
                    iv_my.setBackground(getDrawable(R.drawable.my));
                    tv_main.setTextColor(Color.parseColor("#1296db"));
                    tv_basket.setTextColor(Color.parseColor("#515151"));
                    tv_interest.setTextColor(Color.parseColor("#515151"));
                    tv_my.setTextColor(Color.parseColor("#515151"));
                    setFragment(fragment,fragment_main);
                    break;
                case R.id.rl_basket:
                    iv_main.setBackground(getDrawable(R.drawable.main));
                    iv_basket.setBackground(getDrawable(R.drawable.basket_active));
                    iv_interest.setBackground(getDrawable(R.drawable.interest));
                    iv_my.setBackground(getDrawable(R.drawable.my));
                    tv_main.setTextColor(Color.parseColor("#515151"));
                    tv_basket.setTextColor(Color.parseColor("#1296db"));
                    tv_interest.setTextColor(Color.parseColor("#515151"));
                    tv_my.setTextColor(Color.parseColor("#515151"));
                    setFragment(fragment,fragment_basket);
                    break;
                case R.id.rl_interest:
                    iv_main.setBackground(getDrawable(R.drawable.main));
                    iv_basket.setBackground(getDrawable(R.drawable.basket));
                    iv_interest.setBackground(getDrawable(R.drawable.interest_active));
                    iv_my.setBackground(getDrawable(R.drawable.my));
                    tv_main.setTextColor(Color.parseColor("#515151"));
                    tv_basket.setTextColor(Color.parseColor("#515151"));
                    tv_interest.setTextColor(Color.parseColor("#1296db"));
                    tv_my.setTextColor(Color.parseColor("#515151"));
                    setFragment(fragment,fragment_interest);
                    break;
                case R.id.rl_my:
                    iv_main.setBackground(getDrawable(R.drawable.main));
                    iv_basket.setBackground(getDrawable(R.drawable.basket));
                    iv_interest.setBackground(getDrawable(R.drawable.interest));
                    iv_my.setBackground(getDrawable(R.drawable.my_active));
                    tv_main.setTextColor(Color.parseColor("#515151"));
                    tv_basket.setTextColor(Color.parseColor("#515151"));
                    tv_interest.setTextColor(Color.parseColor("#515151"));
                    tv_my.setTextColor(Color.parseColor("#1296db"));
                    setFragment(fragment,fragment_my);
                    break;
            }
        }
    }

    private void setFragment(Fragment fragment, Fragment target_fragment){
        fragment=getFragmentManager().findFragmentByTag("content");
        if(fragment!=null){
            getFragmentManager().beginTransaction().hide(fragment).replace(R.id.fragment_content,target_fragment).commitAllowingStateLoss();
        }else{
            getFragmentManager().beginTransaction().replace(R.id.fragment_content,target_fragment).commitAllowingStateLoss();
        }
    }

    //先关闭再启动
    public static void toggleNotificationListenerService(Context context) {
        PackageManager pm = context.getPackageManager();
        pm.setComponentEnabledSetting(
                new ComponentName(context, Notify_service1.class),
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);

        pm.setComponentEnabledSetting(
                new ComponentName(context, Notify_service1.class),
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    void notification() {
        //创建通知消息管理类
//        Vibrator vibrator = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);
//        long[] vibrationPattern = new long[]{0, 180, 80, 120};
//        // 第一个参数为开关开关的时间，第二个参数是重复次数，振动需要添加权限
//        vibrator.vibrate(vibrationPattern, -1);
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        Notification notification1=new Notification.Builder(MainActivity.this,"12345")
                .setContentTitle("12345")
                .setContentText("12345")
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.main)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.main))
                .build();

        @SuppressLint("WrongConstant") NotificationChannel channel=new NotificationChannel("12345","1235",NotificationManager.IMPORTANCE_MAX);
        channel.enableVibration(true);
        channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        channel.enableLights(true);
        channel.setLightColor(R.color.white);
        channel.setShowBadge(true);
        channel.setSound(null,null);
        channel.setVibrationPattern(new long[]{1000,500,2000});
        manager.createNotificationChannel(channel);
        manager.notify(12345,notification1);
    }

    private boolean isEnabled() {
        String pkgName = getPackageName();
        final String flat = Settings.Secure.getString(getContentResolver(), "enabled_notification_listeners");
        if (!TextUtils.isEmpty(flat)) {
            final String[] names = flat.split(":");
            for (int i = 0; i < names.length; i++) {
                final ComponentName cn = ComponentName.unflattenFromString(names[i]);
                if (cn != null) {
                    if (TextUtils.equals(pkgName, cn.getPackageName())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    //通知栏权限
    private void checkNotify(){
        if(!checkNotifySetting(MainActivity.this)){
            AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(this)
                    .setTitle("通知权限")
                    .setMessage("尚未开启通知权限，点击去开启")
                    .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            try {
                                Intent intent = new Intent();
                                intent.setAction(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
                                //这种方案适用于 API 26, 即8.0（含8.0）以上可以用
                                intent.putExtra(EXTRA_APP_PACKAGE, getPackageName());
                                intent.putExtra(EXTRA_CHANNEL_ID, getApplicationInfo().uid);

                                //这种方案适用于 API21——25，即 5.0——7.1 之间的版本可以使用
                                intent.putExtra("app_package", getPackageName());
                                intent.putExtra("app_uid", getApplicationInfo().uid);

                                // 小米6 -MIUI9.6-8.0.0系统，是个特例，通知设置界面只能控制"允许使用通知圆点"——然而这个玩意并没有卵用，我想对雷布斯说：I'm not ok!!!
                                //  if ("MI 6".equals(Build.MODEL)) {
                                //      intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                //      Uri uri = Uri.fromParts("package", getPackageName(), null);
                                //      intent.setData(uri);
                                //      // intent.setAction("com.android.settings/.SubSettings");
                                //  }
                                startActivity(intent);
                            } catch (Exception e) {
                                e.printStackTrace();
                                // 出现异常则跳转到应用设置界面：锤子坚果3——OC105 API25
                                Intent intent = new Intent();

                                //下面这种方案是直接跳转到当前应用的设置界面。
                                //https://blog.csdn.net/ysy950803/article/details/71910806
                                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package", getPackageName(), null);
                                intent.setData(uri);
                                startActivity(intent);
                            }
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
            myAlertDialog.show();
        }
    }

    private List<String> packageNames;
    //读取系统所有包名
    private void allPackage() {
        //获取PackageManager
        PackageManager packageManager = this.getPackageManager();
        //获取所有已安装程序的包信息
        List<PackageInfo> packageInfos = packageManager.getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES);
        //用于存储所有已安装程序的包名
        // List<String> packageNames = new ArrayList<>();
        packageNames = new ArrayList<>();

        //从pinfo中将包名字逐一取出，压入pName list中
        if (packageInfos != null) {
            for (int i = 0; i < packageInfos.size(); i++) {
                String packName = packageInfos.get(i).packageName;
                packageNames.add(packName);
                System.out.println(packName);
                //Log.e(TAG, "allPackage: ------------------------------------" + packName);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        if (!hasAllPermissionsGranted(grantResults)) {
//            return;
//        }
        switch (requestCode) {
            case 222:
                //Toast.makeText(getApplicationContext(), "已申请权限", Toast.LENGTH_SHORT).show();
            case REQUEST_CODE:
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                        ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    init_fragment();
                } else {
                    Toast.makeText(this, "存储权限未获取", Toast.LENGTH_SHORT).show();
                }
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
    public boolean checkNotifySetting(Context context) {
        NotificationManagerCompat manager = NotificationManagerCompat.from(context);
        // areNotificationsEnabled方法的有效性官方只最低支持到API 19，低于19的仍可调用此方法不过只会返回true，即默认为用户已经开启了通知。
        return manager.areNotificationsEnabled();
    }
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
}
