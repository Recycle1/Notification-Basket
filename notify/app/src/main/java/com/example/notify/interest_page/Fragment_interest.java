package com.example.notify.interest_page;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.notify.Public_thing.Cloud_rule_repositories;
import com.example.notify.Public_thing.Get_data;
import com.example.notify.Public_thing.Public_data;
import com.example.notify.Public_thing.Public_database;
import com.example.notify.Public_thing.Public_database_method;
import com.example.notify.R;

import java.util.ArrayList;

public class Fragment_interest extends Fragment {

    private RecyclerView rv;
    private RelativeLayout rl_2,rl_p1,rl_p2,rl_p3,rl_p4,rl_p5;
    private TextView tv_storage_name;
    private Switch switch1;
    private LinearLayout ll_cloud;
    private ArrayList<Cloud_rule_repositories> list;
    private ImageView iv_p1,iv_p2,iv_p3;
    private View view1;
    private TextView tv_p1,tv_p2,tv_p3,tv_p4,tv_p5,tv_p1_fork,tv_p2_fork,tv_p3_fork,tv_p4_fork,tv_p5_fork;
    private Handler handler=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message message) {
            switch(message.what){
                case 0:
                    if(list.size()==5){
                        Glide.with(view1).load(Get_data.url+"notify/user_image/user_"+list.get(0).tel+".png").into(iv_p1);
                        Glide.with(view1).load(Get_data.url+"notify/user_image/user_"+list.get(1).tel+".png").into(iv_p2);
                        Glide.with(view1).load(Get_data.url+"notify/user_image/user_"+list.get(2).tel+".png").into(iv_p3);
                        tv_p1.setText("TOP1."+ Public_data.change_string(list.get(0).storage_name,7));
                        tv_p2.setText("TOP2."+Public_data.change_string(list.get(1).storage_name,7));
                        tv_p3.setText("TOP3."+ Public_data.change_string(list.get(2).storage_name,7));
                        tv_p4.setText("TOP4."+ Public_data.change_string(list.get(3).storage_name,7));
                        tv_p5.setText("TOP5."+ Public_data.change_string(list.get(4).storage_name,7));
                        tv_p1_fork.setText(String.valueOf(list.get(0).fork));
                        tv_p2_fork.setText(String.valueOf(list.get(1).fork));
                        tv_p3_fork.setText(String.valueOf(list.get(2).fork));
                        tv_p4_fork.setText(String.valueOf(list.get(3).fork));
                        tv_p5_fork.setText(String.valueOf(list.get(4).fork));
                        rl_p1.setOnClickListener(new Onclick());
                        rl_p2.setOnClickListener(new Onclick());
                        rl_p3.setOnClickListener(new Onclick());
                        rl_p4.setOnClickListener(new Onclick());
                        rl_p5.setOnClickListener(new Onclick());
                    }
                    break;
            }
            return false;
        }
    });

    public static Fragment_interest newInstance() {
        Fragment_interest fragment = new Fragment_interest();
        return fragment;
    }

    class Onclick implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            Intent intent=new Intent(getActivity(),CloudDetailActivity.class);
            String tel=null;
            String storage_name=null;
            String description=null;
            int fork=0;
            switch (view.getId()){
                case R.id.rl_p1:
                    tel=list.get(0).tel;
                    storage_name=list.get(0).storage_name;
                    description=list.get(0).description;
                    fork=list.get(0).fork;
                    break;
                case R.id.rl_p2:
                    tel=list.get(1).tel;
                    storage_name=list.get(1).storage_name;
                    description=list.get(1).description;
                    fork=list.get(1).fork;
                    break;
                case R.id.rl_p3:
                    tel=list.get(2).tel;
                    storage_name=list.get(2).storage_name;
                    description=list.get(2).description;
                    fork=list.get(2).fork;
                    break;
                case R.id.rl_p4:
                    tel=list.get(3).tel;
                    storage_name=list.get(3).storage_name;
                    description=list.get(3).description;
                    fork=list.get(3).fork;
                    break;
                case R.id.rl_p5:
                    tel=list.get(4).tel;
                    storage_name=list.get(4).storage_name;
                    description=list.get(4).description;
                    fork=list.get(4).fork;
                    break;
            }
            intent.putExtra("tel",tel);
            intent.putExtra("storage_name",storage_name);
            intent.putExtra("description",description);
            intent.putExtra("fork",fork);
            startActivity(intent);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_interest, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rl_2=view.findViewById(R.id.rl_2);
        ll_cloud=view.findViewById(R.id.ll_cloud);
        tv_storage_name=view.findViewById(R.id.tv_storage_name);
        switch1=view.findViewById(R.id.switch1);

        iv_p1=view.findViewById(R.id.iv_p1);
        iv_p2=view.findViewById(R.id.iv_p2);
        iv_p3=view.findViewById(R.id.iv_p3);
        tv_p1=view.findViewById(R.id.tv_p1);
        tv_p2=view.findViewById(R.id.tv_p2);
        tv_p3=view.findViewById(R.id.tv_p3);
        tv_p4=view.findViewById(R.id.tv_p4);
        tv_p5=view.findViewById(R.id.tv_p5);
        tv_p1_fork=view.findViewById(R.id.tv_p1_fork);
        tv_p2_fork=view.findViewById(R.id.tv_p2_fork);
        tv_p3_fork=view.findViewById(R.id.tv_p3_fork);
        tv_p4_fork=view.findViewById(R.id.tv_p4_fork);
        tv_p5_fork=view.findViewById(R.id.tv_p5_fork);
        rl_p1=view.findViewById(R.id.rl_p1);
        rl_p2=view.findViewById(R.id.rl_p2);
        rl_p3=view.findViewById(R.id.rl_p3);
        rl_p4=view.findViewById(R.id.rl_p4);
        rl_p5=view.findViewById(R.id.rl_p5);

        view1=view;



        rl_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), RepositoriesActivity.class);
                startActivityForResult(intent,0);
            }
        });
        ll_cloud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(), CloudActivity.class);
                startActivityForResult(intent,0);
            }
        });
//        Public_database helper=new Public_database(getActivity());
//        helper.getWritableDatabase();
        init_my_rule();

        update_list();

    }

    void init_my_rule(){
        switch1.setEnabled(false);
        if(Public_database_method.get_active(getActivity())==null){
            tv_storage_name.setText("暂无应用规则");
            switch1.setChecked(false);
        }
        else{
            tv_storage_name.setText(Public_database_method.get_active(getActivity()).storage_name);
            switch1.setChecked(true);
        }
    }

    void update_list(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    list= Get_data.get_cloud(2);
                    handler.sendEmptyMessage(0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //更新本地规则
        if(resultCode==1){
            init_my_rule();
        }
        //更新云端规则
        else if(resultCode==2){
            update_list();
        }
    }
}
