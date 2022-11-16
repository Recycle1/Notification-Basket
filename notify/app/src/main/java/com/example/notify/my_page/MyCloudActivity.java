package com.example.notify.my_page;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import com.example.notify.Public_thing.Cloud_rule_repositories;
import com.example.notify.Public_thing.Get_data;
import com.example.notify.Public_thing.Public_data;
import com.example.notify.R;
import com.example.notify.interest_page.CloudActivity;
import com.example.notify.interest_page.CloudDetailActivity;
import com.example.notify.interest_page.Cloud_adapter;

import java.util.ArrayList;

public class MyCloudActivity extends AppCompatActivity {

    private RecyclerView rv;
    private Button btn_back;
    private Cloud_adapter adapter;
    private ArrayList<Cloud_rule_repositories> list;

    //回传更新的东西
    boolean flag=false;

    private Handler handler=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message message) {
            switch (message.what){
                case 0:
                    adapter.setCloud_rule_repositories_ArrayList(list);
                    rv.setAdapter(adapter);
                    break;
                case 1:
                    break;
            }
            return false;
        }
    });

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_cloud);
        btn_back=findViewById(R.id.btn_back);
        rv=findViewById(R.id.rv);
        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter=new Cloud_adapter(this, new Cloud_adapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                Intent intent=new Intent(MyCloudActivity.this, CloudDetailActivity.class);
                intent.putExtra("tel",list.get(position).tel);
                intent.putExtra("storage_name",list.get(position).storage_name);
                intent.putExtra("description",list.get(position).description);
                intent.putExtra("fork",list.get(position).fork);
                startActivityForResult(intent,0);
            }
        },0);
        update_my_cloud();
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(flag){
                    setResult(1);
                }
                finish();
            }
        });
    }

    void update_my_cloud(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    list= Get_data.get_my_cloud(Public_data.tel,1);
                    handler.sendEmptyMessage(0);
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
                setResult(1);
            }
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==1){
            update_my_cloud();
            flag=true;
        }
    }
}