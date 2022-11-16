package com.example.notify.main_page;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.notify.Public_thing.APP_info;
import com.example.notify.Public_thing.Collection;
import com.example.notify.Public_thing.Public_data;
import com.example.notify.Public_thing.Rule;
import com.example.notify.R;
import com.example.notify.basket_page.Time_adapter;
import com.example.notify.my_page.Collection_adapter;

import java.util.ArrayList;

public class Info_adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private Context context;
    public ArrayList<APP_info> appList;
    long sum;

    public Info_adapter(Context context,long sum){
        this.context=context;
        appList=new ArrayList<>();
        this.sum=sum;
    }

    public void setAppList(ArrayList<APP_info> appList) {
        this.appList = appList;
    }

    public void setAppList(ArrayList<APP_info> appList,long sum) {
        this.appList = appList;
        this.sum=sum;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new myholder(LayoutInflater.from(context).inflate(R.layout.layout_app_info_item,parent,false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        APP_info app_info=appList.get(position);
        PackageManager packageManager=context.getPackageManager();
        PackageInfo info = null;
        try {
            info = packageManager.getPackageInfo(app_info.packagename,PackageManager.GET_ACTIVITIES);
            ((myholder)holder).iv_icon.setBackground(packageManager.getApplicationIcon(app_info.packagename));
            ((myholder)holder).tv_name.setText(Public_data.change_string(info.applicationInfo.loadLabel(packageManager).toString(),5));
            ((myholder)holder).tv_number.setText(String.valueOf(app_info.number));
        } catch (PackageManager.NameNotFoundException e) {
            ((myholder)holder).iv_icon.setBackground(context.getDrawable(R.drawable.null_app));
            ((myholder)holder).tv_name.setText("未知应用");
            ((myholder)holder).tv_number.setText(String.valueOf(app_info.number));
            e.printStackTrace();
        }
        ViewGroup.LayoutParams f_Params =  ((myholder)holder).fl_1.getLayoutParams();
        ViewGroup.LayoutParams v_Params =  ((myholder)holder).v_c.getLayoutParams();
        v_Params.width=Long.valueOf(app_info.number).intValue()*f_Params.width/Long.valueOf(sum).intValue();
        ((myholder)holder).v_c.setLayoutParams(v_Params);
    }

    @Override
    public int getItemCount() {
        return appList.size();
    }

    class myholder extends RecyclerView.ViewHolder{

        private ImageView iv_icon;
        private TextView tv_name,tv_number;
        private FrameLayout fl_1;
        private View v_c;

        public myholder(View itemView) {
            super(itemView);
            iv_icon=itemView.findViewById(R.id.iv_icon);
            tv_name=itemView.findViewById(R.id.tv_name);
            tv_number=itemView.findViewById(R.id.tv_number);
            fl_1=itemView.findViewById(R.id.fl_1);
            v_c=itemView.findViewById(R.id.v_c);
        }
    }

}
