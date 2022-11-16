package com.example.notify.interest_page;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.notify.Public_thing.Cloud_rule_repositories;
import com.example.notify.Public_thing.Get_data;
import com.example.notify.Public_thing.Notify;
import com.example.notify.R;

import java.util.ArrayList;

public class Cloud_adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private OnItemClickListener listener;
    public ArrayList<Cloud_rule_repositories> cloud_rule_repositories_ArrayList;
    int mode=0;

    public Cloud_adapter(Context context, OnItemClickListener listener,int mode){
        this.context=context;
        this.listener=listener;
        cloud_rule_repositories_ArrayList=new ArrayList<>();
        this.mode=mode;
    }

    public void setCloud_rule_repositories_ArrayList(ArrayList<Cloud_rule_repositories> cloud_rule_repositories_ArrayList) {
        this.cloud_rule_repositories_ArrayList = cloud_rule_repositories_ArrayList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(mode==0){
            return new myholder(LayoutInflater.from(context).inflate(R.layout.layout_cloud_item,parent,false));
        }
        else{
            return new myholder(LayoutInflater.from(context).inflate(R.layout.layout_cloud_item_small,parent,false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Cloud_rule_repositories cloud_rule_repositories=cloud_rule_repositories_ArrayList.get(position);
        Glide.with(context).load(Get_data.url+"notify/user_image/user_"+cloud_rule_repositories.tel+".png").into(((myholder)holder).imageView);
        ((myholder)holder).tv_storage_name.setText(cloud_rule_repositories.storage_name);
        ((myholder)holder).tv_fork.setText(String.valueOf(cloud_rule_repositories.fork));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return cloud_rule_repositories_ArrayList.size();
    }

    class myholder extends RecyclerView.ViewHolder{

        private ImageView imageView;
        private TextView tv_storage_name;
        private TextView tv_fork;

        public myholder(View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.imageview);
            tv_storage_name=itemView.findViewById(R.id.tv_storage_name);
            tv_fork=itemView.findViewById(R.id.tv_fork);
        }
    }

    public interface OnItemClickListener{
        void onClick(int position);
    }
}

