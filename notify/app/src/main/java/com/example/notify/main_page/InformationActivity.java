package com.example.notify.main_page;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.notify.Public_thing.Public_database_method;
import com.example.notify.R;

import org.w3c.dom.Text;

public class InformationActivity extends AppCompatActivity {

    private RecyclerView rv;
    private Info_adapter adapter;
    private TextView tv_sum,tv_today,tv_number,tv_s;
    private Button btn_back;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);
        rv=findViewById(R.id.rv);
        tv_sum=findViewById(R.id.tv_sum);
        tv_today=findViewById(R.id.tv_today);
        tv_number=findViewById(R.id.tv_number);
        tv_s=findViewById(R.id.tv_s);
        btn_back=findViewById(R.id.btn_back);
        tv_number.setText(String.valueOf(Public_database_method.getsum(this)));
        tv_sum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long sum= Public_database_method.get_sum(InformationActivity.this);
                adapter.setAppList(Public_database_method.get_app_info(InformationActivity.this),sum);
                adapter.notifyDataSetChanged();
                tv_number.setText(String.valueOf(sum));
                tv_s.setText("总共获得消息");
                tv_today.setBackground(getDrawable(R.drawable.button));
                tv_sum.setBackground(getDrawable(R.drawable.button2));
            }
        });
        tv_today.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long sum=Public_database_method.getsum(InformationActivity.this);
                adapter.setAppList(Public_database_method.get_app_info_today(InformationActivity.this),sum);
                adapter.notifyDataSetChanged();
                tv_number.setText(String.valueOf(sum));
                tv_s.setText("今日共获得消息");
                tv_today.setBackground(getDrawable(R.drawable.button2));
                tv_sum.setBackground(getDrawable(R.drawable.button));
            }
        });
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter=new Info_adapter(this, Public_database_method.getsum(this));
        adapter.setAppList(Public_database_method.get_app_info_today(this));
        rv.setAdapter(adapter);
    }
}