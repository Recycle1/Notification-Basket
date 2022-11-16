package com.example.notify.my_page;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.notify.Public_thing.Get_data;
import com.example.notify.Public_thing.Public_data;
import com.example.notify.R;

public class LoginActivity extends AppCompatActivity {

    private Button btn_register,btn_login;
    private EditText et_tel,et_password;
    private Handler handler=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message message) {
            switch (message.what){
                case 0:
                    Toast.makeText(LoginActivity.this, "电话或密码错误", Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                    Public_data.tel=(((String)message.obj).split("tel=")[1]).split("name")[0];
                    Public_data.name=((String)message.obj).split("name=")[1];
                    setResult(3);
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
        setContentView(R.layout.activity_login);
        btn_register=findViewById(R.id.btn_register);
        btn_login=findViewById(R.id.btn_login);
        et_tel=findViewById(R.id.et_tel);
        et_password=findViewById(R.id.et_password);
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String tel=et_tel.getText().toString();
                        String password=et_password.getText().toString();
                        try {
                            String feedback=Get_data.touchHtml(Get_data.url+"notify/login.php?tel="+tel+"&password="+password);
                            if(feedback.equals("错误")){
                                handler.sendEmptyMessage(0);
                            }else{
                                Message message=new Message();
                                message.what=1;
                                message.obj=feedback;
                                handler.sendMessage(message);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });
    }
}