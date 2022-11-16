package com.example.notify.my_page;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.notify.Public_thing.Cloud_rule_repositories;
import com.example.notify.Public_thing.Collection;
import com.example.notify.Public_thing.Get_data;
import com.example.notify.Public_thing.Public_data;
import com.example.notify.R;
import com.example.notify.basket_page.InfoDialog;
import com.example.notify.interest_page.RuleDetailDialog;

import java.util.ArrayList;

public class Collection_adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private OnItemClickListener listener;
    private OnItemClickListener1 listener1;
    public ArrayList<Collection> collection_ArrayList;
    int mode=0;

    public Collection_adapter(Context context, OnItemClickListener listener,OnItemClickListener1 listener1,int mode){
        this.context=context;
        this.listener=listener;
        this.listener1=listener1;
        collection_ArrayList=new ArrayList<>();
        this.mode=mode;
    }

    public void setCollection_ArrayList(ArrayList<Collection> collection_ArrayList) {
        this.collection_ArrayList = collection_ArrayList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(mode==0){
            return new myholder(LayoutInflater.from(context).inflate(R.layout.layout_collection_item,parent,false));
        }
        else{
            return new myholder(LayoutInflater.from(context).inflate(R.layout.layout_collection_item_small,parent,false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Collection collection=collection_ArrayList.get(position);
        Glide.with(context).load(collection.image).into(((myholder)holder).imageView);
        ((myholder)holder).tv_title.setText(Public_data.change_string(collection.title,15));
        ((myholder)holder).tv_text.setText(collection.text);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(position);
            }
        });
        ((myholder)holder).ll_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InfoDialog infoDialog=new InfoDialog(context);
                infoDialog.setTitle(collection.title).setMessage(collection.text);
                infoDialog.show();
            }
        });
        ((myholder)holder).rl_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener1.onClick(collection.name,collection.title,collection.text);
            }
        });
    }

    @Override
    public int getItemCount() {
        return collection_ArrayList.size();
    }

    class myholder extends RecyclerView.ViewHolder{

        private ImageView imageView;
        private TextView tv_title;
        private TextView tv_text;
        private LinearLayout ll_1;
        private RelativeLayout rl_2;

        public myholder(View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.imageview);
            tv_title=itemView.findViewById(R.id.tv_title);
            tv_text=itemView.findViewById(R.id.tv_text);
            ll_1=itemView.findViewById(R.id.ll_1);
            rl_2=itemView.findViewById(R.id.rl_2);
        }
    }

    public interface OnItemClickListener{
        void onClick(int position);
    }

    public interface OnItemClickListener1{
        void onClick(String name,String title,String text);
    }
}

