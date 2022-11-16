package com.example.notify.interest_page;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.notify.Public_thing.Public_data;
import com.example.notify.Public_thing.Public_database;
import com.example.notify.Public_thing.Public_database_method;
import com.example.notify.R;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AddRepositoriesActivity extends AppCompatActivity {

    private EditText et_name;
    private EditText et_description;
    private Button btn_back;
    private TextView tv_confirm;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_repositories);
        et_name=findViewById(R.id.et_name);
        et_description=findViewById(R.id.et_description);
        btn_back=findViewById(R.id.btn_back);
        tv_confirm=findViewById(R.id.tv_confirm);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        tv_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(et_name.getText().toString().trim().length()==0){
                    Toast.makeText(AddRepositoriesActivity.this, "请输入名称", Toast.LENGTH_SHORT).show();
                }
                else if(et_description.getText().toString().trim().length()==0){
                    Toast.makeText(AddRepositoriesActivity.this, "请输入描述", Toast.LENGTH_SHORT).show();
                }
                else if(Public_database_method.get_rule_repositories_used(AddRepositoriesActivity.this,et_name.getText().toString().trim())){
                    Toast.makeText(AddRepositoriesActivity.this, "已创建过同名仓库", Toast.LENGTH_SHORT).show();
                }
                else{
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
                    Date date = new Date(System.currentTimeMillis());
                    String datetime=simpleDateFormat.format(date);
                    Public_database_method.add_repositories(AddRepositoriesActivity.this, et_name.getText().toString().trim(),et_description.getText().toString().trim(),0,datetime);
                    Toast.makeText(AddRepositoriesActivity.this, "添加成功", Toast.LENGTH_SHORT).show();
                    setResult(1);
                    finish();
                }
            }
        });
    }
}