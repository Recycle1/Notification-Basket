package com.example.notify.interest_page;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notify.Public_thing.Get_data;
import com.example.notify.Public_thing.Public_data;
import com.example.notify.Public_thing.Public_database_method;
import com.example.notify.Public_thing.Rule;
import com.example.notify.Public_thing.Rule_repositories;
import com.example.notify.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Rule_repositories_adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private OnItemClickListener listener;
    private OnItemClickListener1 listener1;
    private OnItemChangeListener change_listener;
    public ArrayList<Rule_repositories> repositoriesArrayList;
    private Handler handler=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message message) {
            switch (message.what){
                case 0:
                    Toast.makeText(context, "已上传过", Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    Toast.makeText(context, "上传成功", Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    Toast.makeText(context, "删除成功", Toast.LENGTH_SHORT).show();
                    break;
            }
            return false;
        }
    });

    public Rule_repositories_adapter(Context context, OnItemClickListener listener,OnItemClickListener1 listener1,OnItemChangeListener change_listener){
        this.context=context;
        this.listener=listener;
        this.listener1=listener1;
        this.change_listener=change_listener;
        repositoriesArrayList=new ArrayList<>();
    }

    public void setRepositoriesArrayList(ArrayList<Rule_repositories> repositoriesArrayList) {
        this.repositoriesArrayList = repositoriesArrayList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new myholder(LayoutInflater.from(context).inflate(R.layout.layout_repositories_item,parent,false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Rule_repositories repositories=repositoriesArrayList.get(position);
        ((myholder)holder).tv_storage_name.setText(repositories.storage_name);
        if(repositories.active==0){
            ((myholder)holder).switch1.setChecked(false);
        }
        else{
            ((myholder)holder).switch1.setChecked(true);
        }
        ((myholder)holder).btn_cloud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Public_data.login(context)){
                    CustomDialog customDialog=new CustomDialog(context);
                    customDialog.setTitle("提示").setMessage("是否上传")
                            .setCancel("取消", new CustomDialog.IOnCancelListener() {
                                @Override
                                public void onCancel(CustomDialog dialog) {

                                }
                            })
                            .setConfirm("确定", new CustomDialog.IOnConfirmListener() {
                                @Override
                                public void onConfirm(CustomDialog dialog) {
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                if(Get_data.touchHtml(Get_data.url+"notify/get_rule_repositories_used.php?tel="+Public_data.tel+"&storage_name="+repositories.storage_name).equals("有")){
                                                    handler.sendEmptyMessage(0);
                                                }
                                                else{
                                                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
                                                    Date date = new Date(System.currentTimeMillis());
                                                    String datetime=simpleDateFormat.format(date);
                                                    if(Get_data.touchHtml(Get_data.url+"notify/insert_rule_repositories.php?tel="+Public_data.tel+"&storage_name="+repositories.storage_name+"&description="+repositories.description+"&datetime="+datetime).equals("成功")){
                                                        ArrayList<Rule> rules=Public_database_method.get_rule_list(context,repositories.storage_name);
                                                        for(int i=0;i<rules.size();i++){
                                                            Get_data.touchHtml(Get_data.url+"notify/insert_rule.php?_id="+rules.get(i)._id+"&tel="+Public_data.tel+"&packagename="+rules.get(i).packagename+"&storage_name="+rules.get(i).storage_name+"&keyword="+rules.get(i).keyword+"&mode="+
                                                                    rules.get(i).mode+"&action_name="+rules.get(i).action+"&sound="+rules.get(i).sound+"&vibrate="+rules.get(i).vibrate+"&match_name="+rules.get(i).match);
                                                        }
                                                        handler.sendEmptyMessage(1);
                                                    }
                                                }
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }).start();
                                }
                            }).show();
                }

            }
        });
        ((myholder)holder).switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    Public_database_method.add_active(context,repositories.storage_name);
                }
                else{
                    Public_database_method.cancel_active(context,repositories.storage_name);
                }
                change_listener.onClick(position);
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(position);
            }
        });
        ((myholder)holder).btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener1.onClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return repositoriesArrayList.size();
    }

    class myholder extends RecyclerView.ViewHolder{

        private TextView tv_storage_name;
        private Switch switch1;
        private RelativeLayout btn_cloud,btn_delete;

        public myholder(View itemView) {
            super(itemView);
            tv_storage_name=itemView.findViewById(R.id.tv_storage_name);
            switch1=itemView.findViewById(R.id.switch1);
            btn_cloud=itemView.findViewById(R.id.rl_1);
            btn_delete=itemView.findViewById(R.id.rl_2);
        }
    }

    public interface OnItemClickListener{
        void onClick(int position);
    }

    public interface OnItemClickListener1{
        void onClick(int position);
    }

    public interface OnItemChangeListener{
        void onClick(int position);
    }
}

