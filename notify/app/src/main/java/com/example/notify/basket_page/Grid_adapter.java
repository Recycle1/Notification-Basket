package com.example.notify.basket_page;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notify.Public_thing.Public_data;
import com.example.notify.Public_thing.Rule;
import com.example.notify.R;

import java.util.ArrayList;
import java.util.Arrays;

public class Grid_adapter extends RecyclerView.Adapter<Grid_adapter.linearViewHolder>{

    private Context mContext;
    private OnItemClickListener mlistener;
    public ArrayList<String> list;
    int mode;

    public Grid_adapter(Context context, OnItemClickListener listener,int mode){
        this.mContext=context;
        this.mlistener=listener;
        this.mode=mode;
    }

    public void setList(ArrayList<String> list) {
        this.list = list;
    }

    @Override
    public Grid_adapter.linearViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new linearViewHolder(LayoutInflater.from(mContext).inflate(R.layout.layout_grid_recyclerview_item,parent,false));
    }

    @Override
    public void onBindViewHolder(Grid_adapter.linearViewHolder holder, @SuppressLint("RecyclerView") int position) {
        if(mode==1){
            PackageManager packageManager=mContext.getPackageManager();
            PackageInfo info = null;
            try {
                info = packageManager.getPackageInfo(list.get(position), PackageManager.GET_ACTIVITIES);
                holder.tv_name.setText(Public_data.change_string(info.applicationInfo.loadLabel(packageManager).toString(),5));
                holder.iv_icon.setBackground(packageManager.getApplicationIcon(list.get(position)));
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Public_data.app_packagename=list.get(position);
                    mlistener.onClick(position);
                }
            });
        }
        else if(mode==2){
            holder.tv_name.setText(list.get(position));
            holder.iv_icon.setBackground(mContext.getDrawable(Public_data.folder[position%4]));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mlistener.onClick(position);
                }
            });
        }
        holder.ll_1.setBackground(mContext.getDrawable(Public_data.basket_background[position%4]));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class linearViewHolder extends RecyclerView.ViewHolder{

        private ImageView iv_icon;
        private TextView tv_name;
        private LinearLayout ll_1;

        public linearViewHolder(View itemView) {
            super(itemView);
            tv_name=itemView.findViewById(R.id.tv_name);
            iv_icon=itemView.findViewById(R.id.iv_icon);
            ll_1=itemView.findViewById(R.id.ll_1);
        }
    }

    public interface OnItemClickListener{
        void onClick(int pos);
    }

}

