package com.example.notify.interest_page;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.notify.Public_thing.Public_data;
import com.example.notify.Public_thing.Public_database_method;
import com.example.notify.Public_thing.Rule;
import com.example.notify.R;

public class RuleActivity extends AppCompatActivity {

    private RecyclerView rv;
    private TextView tv_storage_name;
    private Rule_adapter adapter;
    private Button button,btn_back;
    String storage_name;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rule);
        rv=findViewById(R.id.rv);
        button=findViewById(R.id.btn);
        tv_storage_name=findViewById(R.id.tv_storage_name);
        btn_back=findViewById(R.id.btn_back);
        rv.setLayoutManager(new LinearLayoutManager(this));
        storage_name=getIntent().getStringExtra("storage_name");
        tv_storage_name.setText(storage_name);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        adapter=new Rule_adapter(this, new Rule_adapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {

            }
        }, new Rule_adapter.OnItemClickListener1() {
            @Override
            public void onClick(Intent intent) {
                startActivityForResult(intent, 0);
            }
        }, new Rule_adapter.OnItemClickListener2() {
            @Override
            public void onClick(Rule rule) {
                CustomDialog customDialog=new CustomDialog(RuleActivity.this);
                customDialog.setTitle("提示").setMessage("是否删除")
                        .setCancel("取消", new CustomDialog.IOnCancelListener() {
                            @Override
                            public void onCancel(CustomDialog dialog) {

                            }
                        }).setConfirm("确定", new CustomDialog.IOnConfirmListener() {
                            @Override
                            public void onConfirm(CustomDialog dialog) {
                                if(Public_database_method.delete_rule(RuleActivity.this,rule.storage_name,rule._id)){
                                    Toast.makeText(RuleActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                                    adapter.setRuleArrayList(Public_database_method.get_rule_list(RuleActivity.this, storage_name));
                                    adapter.notifyDataSetChanged();
                                }
                            }
                        }).show();
            }
        },0);
        adapter.setRuleArrayList(Public_database_method.get_rule_list(this,storage_name));
        rv.setAdapter(adapter);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(RuleActivity.this,AddRuleActivity.class);
                intent.putExtra("storage_name",storage_name);
                startActivityForResult(intent,0);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==1){
            adapter.setRuleArrayList(Public_database_method.get_rule_list(this, storage_name));
            adapter.notifyDataSetChanged();
        }
    }
}