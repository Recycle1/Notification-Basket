package com.example.notify.my_page;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.notify.Public_thing.Collection;
import com.example.notify.Public_thing.Get_data;
import com.example.notify.Public_thing.Public_data;
import com.example.notify.R;
import com.example.notify.basket_page.SlideRecyclerView;
import com.example.notify.interest_page.CustomDialog;

import java.util.ArrayList;

public class CollectionActivity extends AppCompatActivity {

    private SlideRecyclerView rv;
    private Button btn_back;
    private Collection_adapter adapter;
    ArrayList<Collection> collection;

    //回传更新东西
    boolean flag=false;

    private Handler handler=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message message) {
            switch (message.what){
                case 0:
                    adapter.setCollection_ArrayList(collection);
                    rv.setAdapter(adapter);
                    break;
                case 1:
                    Toast.makeText(CollectionActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                    get_collection(2);
                    flag=true;
                    break;
                case 2:
                    adapter.setCollection_ArrayList(collection);
                    adapter.notifyDataSetChanged();
                    break;
            }
            return false;
        }
    });

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);
        rv=findViewById(R.id.rv);
        btn_back=findViewById(R.id.btn_back);
        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter=new Collection_adapter(this, new Collection_adapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {

            }
        }, new Collection_adapter.OnItemClickListener1() {
            @Override
            public void onClick(String name, String title, String text) {
                CustomDialog customDialog=new CustomDialog(CollectionActivity.this);
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
                                            if(Get_data.touchHtml(Get_data.url+"notify/delete_collection.php?tel="+Public_data.tel+"&name="+name+"&title="+title+"&text="+text).equals("删除成功")){
                                                handler.sendEmptyMessage(1);
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }).start();
                            }
                        }).show();
            }
        },0);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(flag){
                    setResult(2);
                }
                finish();
            }
        });
        get_collection(1);
    }

    void get_collection(int mode){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if(Public_data.tel!=null){
                        collection=Get_data.get_collection(Public_data.tel,1);
                        if(mode==1){
                            handler.sendEmptyMessage(0);
                        }
                        else if(mode==2){
                            handler.sendEmptyMessage(2);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            if(flag){
                setResult(2);
            }
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}