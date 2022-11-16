package com.example.notify.my_page;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.notify.Public_thing.Cloud_rule_repositories;
import com.example.notify.Public_thing.Collection;
import com.example.notify.Public_thing.Get_data;
import com.example.notify.Public_thing.Public_data;
import com.example.notify.R;
import com.example.notify.interest_page.CloudDetailActivity;
import com.example.notify.interest_page.Cloud_adapter;

import java.util.ArrayList;

public class Fragment_my extends Fragment {

    private RecyclerView rv_cloud;
    private RecyclerView rv_collection;
    private TextView tv_name;
    private RelativeLayout rl_my_cloud;
    private RelativeLayout rl_collection;
    private ImageView imageView;

    private RelativeLayout rl_exit_login;

    public Cloud_adapter cloud_adapter;
    ArrayList<Cloud_rule_repositories> rule_repositories;
    public Collection_adapter collection_adapter;
    ArrayList<Collection> collections;

    private Handler handler=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message message) {
            switch (message.what){
                case 0:
                    cloud_adapter.setCloud_rule_repositories_ArrayList(rule_repositories);
                    rv_cloud.setAdapter(cloud_adapter);
                    break;
                case 1:
                    collection_adapter.setCollection_ArrayList(collections);
                    rv_collection.setAdapter(collection_adapter);
                    break;
            }
            return false;
        }
    });

    public static Fragment_my newInstance() {
        Fragment_my fragment = new Fragment_my();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tv_name=view.findViewById(R.id.tv_name);
        rv_cloud=view.findViewById(R.id.rv_cloud);
        rv_collection=view.findViewById(R.id.rv_collection);
        rl_collection=view.findViewById(R.id.rl_collection);
        rl_my_cloud=view.findViewById(R.id.rl_my_cloud);
        rl_exit_login=view.findViewById(R.id.rl_exit_login);
        imageView=view.findViewById(R.id.iv);
        rv_cloud.setLayoutManager(new LinearLayoutManager(getActivity()));
        rl_my_cloud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(),MyCloudActivity.class);
                startActivityForResult(intent,0);
            }
        });
        if(Public_data.tel!=null){
            Glide.with(this).load(Get_data.url+"notify/user_image/user_"+Public_data.tel+".png").into(imageView);
        }
        rv_collection.setLayoutManager(new LinearLayoutManager(getActivity()));
        rl_collection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(),CollectionActivity.class);
                startActivityForResult(intent,0);
            }
        });
        collection_adapter=new Collection_adapter(getActivity(), new Collection_adapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
            }
        }, new Collection_adapter.OnItemClickListener1() {
            @Override
            public void onClick(String name, String title, String text) {
            }
        },1);
        rl_exit_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Public_data.tel=null;
                Public_data.name=null;
                update_my_cloud();
                update_collection();
                tv_name.setText("登录/注册");
                tv_name.setClickable(true);
                tv_name.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(getActivity(),LoginActivity.class);
                        startActivityForResult(intent,3);
                    }
                });
                imageView.setImageDrawable(getActivity().getDrawable(R.drawable.null_pic));
            }
        });

        cloud_adapter=new Cloud_adapter(getActivity(), new Cloud_adapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                Intent intent=new Intent(getActivity(), CloudDetailActivity.class);
                intent.putExtra("tel",rule_repositories.get(position).tel);
                intent.putExtra("storage_name",rule_repositories.get(position).storage_name);
                intent.putExtra("description",rule_repositories.get(position).description);
                intent.putExtra("fork",rule_repositories.get(position).fork);
                startActivityForResult(intent,0);
            }
        },1);
        update_my_cloud();
        update_collection();
        if(Public_data.name!=null){
            tv_name.setText(Public_data.name);
        }
        else{
            tv_name.setClickable(true);
            tv_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(getActivity(),LoginActivity.class);
                    startActivityForResult(intent,3);
                }
            });
        }
    }

    void update_my_cloud(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    rule_repositories= Get_data.get_my_cloud(Public_data.tel,2);
                    handler.sendEmptyMessage(0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    void update_collection(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    collections= Get_data.get_collection(Public_data.tel,2);
                    handler.sendEmptyMessage(1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==1){
            update_my_cloud();
        }
        else if(resultCode==2){
            update_collection();
        }
        else if(resultCode==3){
            update_my_cloud();
            update_collection();
            Glide.with(this).load(Get_data.url+"notify/user_image/user_"+Public_data.tel+".png").into(imageView);
            tv_name.setText(Public_data.name);
            tv_name.setClickable(false);
        }
    }
}