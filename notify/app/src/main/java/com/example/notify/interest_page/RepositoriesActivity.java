package com.example.notify.interest_page;

import android.app.usage.UsageEvents;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.DragEvent;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notify.Public_thing.Public_data;
import com.example.notify.Public_thing.Public_database;
import com.example.notify.Public_thing.Public_database_method;
import com.example.notify.Public_thing.Rule;
import com.example.notify.R;
import com.example.notify.basket_page.SlideRecyclerView;

import java.util.ArrayList;
import java.util.Random;

public class RepositoriesActivity extends AppCompatActivity {

    private SlideRecyclerView rv;
    private Rule_repositories_adapter adapter;
    private Button button,btn_back;
    float moveX =0;
    float moveY=0;
    private Handler handler=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message message) {
            switch (message.what){
                case 0:
                    adapter.setRepositoriesArrayList(Public_database_method.get_repositories(RepositoriesActivity.this));
                    adapter.notifyDataSetChanged();
                    break;
            }
            return false;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repositories);
        rv=findViewById(R.id.rv);
        button=findViewById(R.id.btn);
        btn_back=findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter=new Rule_repositories_adapter(this, new Rule_repositories_adapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                Intent intent = new Intent(RepositoriesActivity.this, RuleActivity.class);
                intent.putExtra("storage_name", adapter.repositoriesArrayList.get(position).storage_name);
                startActivity(intent);
            }
        }, new Rule_repositories_adapter.OnItemClickListener1() {
            @Override
            public void onClick(int position) {
                CustomDialog customDialog=new CustomDialog(RepositoriesActivity.this);
                customDialog.setTitle("提示").setMessage("是否删除")
                        .setCancel("取消", new CustomDialog.IOnCancelListener() {
                            @Override
                            public void onCancel(CustomDialog dialog) {

                            }
                        }).setConfirm("确定", new CustomDialog.IOnConfirmListener() {
                            @Override
                            public void onConfirm(CustomDialog dialog) {
                                String name=adapter.repositoriesArrayList.get(position).storage_name;
                                if(Public_database_method.delete_rule_repositories(RepositoriesActivity.this, name)){
                                    ArrayList<Rule> rules=Public_database_method.get_rule_list(RepositoriesActivity.this,name);
                                    for(int i=0;i<rules.size();i++){
                                        Public_database_method.delete_rule(RepositoriesActivity.this,name,rules.get(i)._id);
                                    }
                                    Toast.makeText(RepositoriesActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                                    handler.sendEmptyMessage(0);
                                }
                            }
                        }).show();
            }
        } , new Rule_repositories_adapter.OnItemChangeListener() {
            @Override
            public void onClick(int position) {
                handler.sendEmptyMessage(0);
            }
        });
        adapter.setRepositoriesArrayList(Public_database_method.get_repositories(this));
        rv.setAdapter(adapter);
//        button.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//                boolean touchFlag = false;
//                switch (motionEvent.getAction()) {
//                    case MotionEvent.ACTION_DOWN:
//                        touchFlag=false;
//                        moveX = motionEvent.getX();
//                        moveY = motionEvent.getY();
//                        break;
//                    case MotionEvent.ACTION_MOVE:
//                        touchFlag=true;
//                        view.setTranslationX(view.getX() + (motionEvent.getX() - moveX));
//                        view.setTranslationY(view.getY() + (motionEvent.getY() - moveY));
//                        break;
//                    case MotionEvent.ACTION_UP:
//                        if (touchFlag)return true;
//                        break;
//                    case MotionEvent.ACTION_CANCEL:
//                        break;
//                }
//                return false;
//            }
//        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(RepositoriesActivity.this,AddRepositoriesActivity.class);
                startActivityForResult(intent,1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1){
            adapter.setRepositoriesArrayList(Public_database_method.get_repositories(this));
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            setResult(1);
        }
        return super.onKeyDown(keyCode, event);
    }
}