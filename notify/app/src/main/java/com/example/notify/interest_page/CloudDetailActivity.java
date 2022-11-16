package com.example.notify.interest_page;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.notify.Public_thing.Get_data;
import com.example.notify.Public_thing.Public_data;
import com.example.notify.Public_thing.Public_database;
import com.example.notify.Public_thing.Public_database_method;
import com.example.notify.Public_thing.Rule;
import com.example.notify.Public_thing.User;
import com.example.notify.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class CloudDetailActivity extends AppCompatActivity {

    private RecyclerView rv;
    private ImageView iv_icon;
    private TextView tv_storage_name,tv_name,tv_description,tv_fork,tv_delete;
    private Rule_adapter adapter;
    private ArrayList<Rule> list;
    private Button btn_back,btn_fork;
    private User user;

    //回传更新界面
    Boolean flag=false;

    String tel;
    String name;
    String storage_name;
    String description;
    int fork;

    private Handler handler=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message message) {
            switch (message.what){
                case 0:
                    adapter.setRuleArrayList(list);
                    rv.setAdapter(adapter);
                    break;
                case 1:
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
                    Date date = new Date(System.currentTimeMillis());
                    String datetime=simpleDateFormat.format(date);
                    Public_database_method.add_repositories(CloudDetailActivity.this,name,description,0,datetime);


                    PackageManager packageManager=getPackageManager();
                    List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
                    //存包名转名称
//                    PackageInfo info = null;
//                    try {
//                        info = packageManager.getPackageInfo(rule.packagename,PackageManager.GET_ACTIVITIES);
//
//                    } catch (PackageManager.NameNotFoundException e) {
//                        e.printStackTrace();
//                    }
//                    info.applicationInfo.loadLabel(packageManager).toString()
                    for(int i=0;i<list.size();i++){
                        boolean flag=false;
                        if(packageInfos != null){
                            for(int j = 0; j < packageInfos.size(); j++){
                                if(list.get(i).packagename.equals(packageInfos.get(j).applicationInfo.loadLabel(packageManager).toString())){
                                    flag=true;
                                    break;
                                }
                            }
                        }
                        if(flag||list.get(i).packagename.equals("所有APP")){
                            Public_database_method.add_rule(CloudDetailActivity.this, name,list.get(i).packagename,list.get(i).keyword,list.get(i).mode,list.get(i).action,list.get(i).sound,list.get(i).vibrate,list.get(i).match);
                        }
                    }
                    Toast.makeText(CloudDetailActivity.this, "下载成功", Toast.LENGTH_SHORT).show();
                    flag=true;
                    tv_fork.setText(String.valueOf(fork+1));
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Get_data.touchHtml(Get_data.url+"notify/update_fork.php?tel="+tel+"&storage_name="+storage_name);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                    break;
                case 2:
                    tv_name.setText(Public_data.change_string("贡献者："+user.name,11));
                    break;
                case 3:
                    Toast.makeText(CloudDetailActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                    setResult(1);
                    finish();
                    break;
            }
            return false;
        }
    });

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cloud_detail);
        rv=findViewById(R.id.rv);
        btn_fork=findViewById(R.id.btn_fork);
        iv_icon=findViewById(R.id.iv_icon);
        tv_storage_name=findViewById(R.id.tv_storage_name);
        tv_name=findViewById(R.id.tv_user_name);
        tv_description=findViewById(R.id.tv_description);
        tv_fork=findViewById(R.id.tv_fork);
        tv_delete=findViewById(R.id.tv_delete);
        btn_back=findViewById(R.id.btn_back);
        tel=getIntent().getStringExtra("tel");
        storage_name=getIntent().getStringExtra("storage_name");
        description=getIntent().getStringExtra("description");
        fork=getIntent().getIntExtra("fork",0);
        tv_storage_name.setText(storage_name);
        tv_description.setText(Public_data.change_string(description,25));
        tv_fork.setText(String.valueOf(fork));
        Glide.with(this).load(Get_data.url+"notify/user_image/user_"+tel+".png").into(iv_icon);
        if(Public_data.tel!=null&&Public_data.tel.equals(tel)){
            tv_delete.setVisibility(View.VISIBLE);
            tv_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CustomDialog customDialog=new CustomDialog(CloudDetailActivity.this);
                    customDialog.setTitle("提示").setMessage("是否删除")
                            .setCancel("取消", new CustomDialog.IOnCancelListener() {
                                @Override
                                public void onCancel(CustomDialog dialog) {

                                }
                            })
                            .setConfirm("确定", new CustomDialog.IOnConfirmListener() {
                                @Override
                                public void onConfirm(CustomDialog dialog) {
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                if(Get_data.touchHtml(Get_data.url+"notify/delete_cloud.php?tel="+tel+"&storage_name="+storage_name).equals("删除成功")){
                                                    handler.sendEmptyMessage(3);
                                                }
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }).start();
                                }
                            }).show();
                }
            });
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    user=Get_data.get_user(tel);
                    handler.sendEmptyMessage(2);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(flag){
                    setResult(1);
                }
                finish();
            }
        });
        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter=new Rule_adapter(this, new Rule_adapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {

            }
        }, new Rule_adapter.OnItemClickListener1() {
            @Override
            public void onClick(Intent intent) {

            }
        }, new Rule_adapter.OnItemClickListener2() {
            @Override
            public void onClick(Rule rule) {

            }
        },1);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    list=Get_data.get_cloud_rule(tel,storage_name);
                    handler.sendEmptyMessage(0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
        iv_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(CloudDetailActivity.this,BigImageActivity.class);
                intent.putExtra("url",Get_data.url+"notify/user_image/user_"+tel+".png");
                startActivity(intent);
            }
        });
        btn_fork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name=storage_name;
                if(Public_database_method.get_rule_repositories_used(CloudDetailActivity.this,storage_name)){
                    CustomDialog customDialog=new CustomDialog(CloudDetailActivity.this);
                    customDialog.setTitle("提示").setMessage("发现同名仓库，是否覆盖")
                            .setCancel("创建新的", new CustomDialog.IOnCancelListener() {
                                @Override
                                public void onCancel(CustomDialog dialog) {
                                    do{
                                        Random random=new Random();
                                        name+=Public_data.postfix[random.nextInt(11)];
                                    }while(Public_database_method.get_rule_repositories_used(CloudDetailActivity.this,name));
                                    handler.sendEmptyMessage(1);
                                }
                            }).setConfirm("覆盖", new CustomDialog.IOnConfirmListener() {
                                @Override
                                public void onConfirm(CustomDialog dialog) {
                                    if(Public_database_method.delete_rule_repositories(CloudDetailActivity.this,name)){
                                        ArrayList<Rule> rules=Public_database_method.get_rule_list(CloudDetailActivity.this,name);
                                        for(int i=0;i<rules.size();i++){
                                            Public_database_method.delete_rule(CloudDetailActivity.this,name,rules.get(i)._id);
                                        }
                                        handler.sendEmptyMessage(1);
                                    }
                                }
                            }).show();
                }
                else{
                    handler.sendEmptyMessage(1);
                }
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            if(flag){
                setResult(1);
                finish();
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}