package com.example.notify.interest_page;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.notify.Public_thing.Public_data;
import com.example.notify.Public_thing.Public_database;
import com.example.notify.Public_thing.Public_database_method;
import com.example.notify.Public_thing.Rule;
import com.example.notify.Public_thing.Rule_repositories;
import com.example.notify.R;

import java.util.ArrayList;
import java.util.List;

public class Rule_adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private OnItemClickListener listener;
    private OnItemClickListener1 listener1;
    private OnItemClickListener2 listener2;
    public ArrayList<Rule> ruleArrayList;
    int mode=0;

    public Rule_adapter(Context context, OnItemClickListener listener,OnItemClickListener1 listener1,OnItemClickListener2 listener2,int mode){
        this.context=context;
        this.listener=listener;
        this.listener1=listener1;
        this.listener2=listener2;
        ruleArrayList=new ArrayList<>();
        this.mode=mode;
    }

    public void setRuleArrayList(ArrayList<Rule> ruleArrayList) {
        this.ruleArrayList = ruleArrayList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new myholder(LayoutInflater.from(context).inflate(R.layout.layout_rule_item,parent,false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Rule rule=ruleArrayList.get(position);
        ((myholder)holder).tv_title.setText("规则"+(position+1));
        ((myholder)holder).tv_APP.setText(rule.packagename);

        ((myholder)holder).tv_keyword.setText(rule.keyword);
        ((myholder)holder).tv_action.setText(Public_data.action_list[rule.action]);
        if(mode==0){
            ((myholder)holder).btn_update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(context,UpdateRuleActivity.class);
                    intent.putExtra("_id",rule._id);
                    intent.putExtra("storage_name",rule.storage_name);
                    intent.putExtra("APP",rule.packagename);
                    intent.putExtra("keyword",rule.keyword);
                    intent.putExtra("mode",rule.mode);
                    intent.putExtra("action",rule.action);
                    intent.putExtra("sound",rule.sound);
                    intent.putExtra("vibrate",rule.vibrate);
                    intent.putExtra("match",rule.match);
                    listener1.onClick(intent);
                }
            });
            ((myholder)holder).btn_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener2.onClick(rule);
                }
            });
        }
        else{
            ((myholder)holder).btn_update.setVisibility(View.GONE);
            ((myholder)holder).btn_delete.setVisibility(View.GONE);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PackageManager packageManager=context.getPackageManager();
                PackageInfo info = null;
                try {
                    info = packageManager.getPackageInfo(rule.packagename,PackageManager.GET_ACTIVITIES);

                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
                RuleDetailDialog ruleDetailDialog=new RuleDetailDialog(context);
                ruleDetailDialog.setPackagename(rule.packagename);
                ruleDetailDialog.setKeyword(rule.keyword);
                ruleDetailDialog.setMode(Public_data.mode_list[rule.mode]);
                ruleDetailDialog.setAction(Public_data.action_list[rule.action]);
                if(rule.action!=0){
                    ruleDetailDialog.setVibrate(Public_data.vibrate_list[rule.vibrate]);
                    ruleDetailDialog.setSound(Public_data.sound_list[rule.sound]);
                }
                ruleDetailDialog.setMatch(Public_data.match_list[rule.match]);
                ruleDetailDialog.show();
                listener.onClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return ruleArrayList.size();
    }

    class myholder extends RecyclerView.ViewHolder{

        private TextView tv_title;
        private TextView tv_APP;
        private TextView tv_keyword;
        private TextView tv_action;
        private Button btn_update;
        private Button btn_delete;

        public myholder(View itemView) {
            super(itemView);
            tv_title=itemView.findViewById(R.id.tv_title);
            tv_APP=itemView.findViewById(R.id.tv_app);
            tv_keyword=itemView.findViewById(R.id.tv_keyword);
            tv_action=itemView.findViewById(R.id.tv_action);
            btn_update=itemView.findViewById(R.id.btn_update);
            btn_delete=itemView.findViewById(R.id.btn_delete);
        }
    }

    public interface OnItemClickListener{
        void onClick(int position);
    }

    public interface OnItemClickListener1{
        void onClick(Intent intent);
    }

    public interface OnItemClickListener2{
        void onClick(Rule rule);
    }

}
