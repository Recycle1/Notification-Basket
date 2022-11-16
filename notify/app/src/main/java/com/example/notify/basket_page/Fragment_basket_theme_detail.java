package com.example.notify.basket_page;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.util.JsonReader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notify.Public_thing.Get_data;
import com.example.notify.Public_thing.Notify;
import com.example.notify.Public_thing.Public_data;
import com.example.notify.Public_thing.Public_database_method;
import com.example.notify.R;
import com.example.notify.interest_page.CustomDialog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

public class Fragment_basket_theme_detail extends Fragment{

    private RecyclerView rv;
    private View foot_view;
    Time_adapter adapter;
    ArrayList<Notify> notifies;
    String result;
    int fresh=0;
    boolean end_flag=false;
    ArrayList<Notify> list=new ArrayList<>();
    private Handler handler=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message message) {
            switch (message.what){
                case 0:
                    Toast.makeText(getActivity(), "删除成功", Toast.LENGTH_SHORT).show();
                    adapter.setNotifyArrayList(Public_database_method.get_importance(getActivity(), Public_data.importance_mode));
                    adapter.notifyDataSetChanged();
                    break;
                case 1:
                    adapter.setNotifyArrayList(list);
                    rv.setAdapter(adapter);
                    break;
                case 2:
                    adapter.setNotifyArrayList(list);
                    adapter.notifyDataSetChanged();
                    if(end_flag==true){
                        adapter.text_foot="没有更多了";
                        adapter.notifyItemChanged(list.size()+1);
                    }
            }
            return false;
        }
    });

    public static Fragment_basket_theme_detail newInstance() {
        Fragment_basket_theme_detail fragment = new Fragment_basket_theme_detail();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_basket_theme_detail, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rv=view.findViewById(R.id.rv);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        notifies=Public_database_method.get(getActivity());
//        DisplayMetrics metric = new DisplayMetrics();
//        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metric);
//        int windowsWidth = metric.widthPixels;
        adapter=new Time_adapter(getActivity(), new Time_adapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {

            }
        }, new Time_adapter.OnItemClickListener1() {
            @Override
            public void onClick(int position) {
                CustomDialog customDialog=new CustomDialog(getActivity());
                customDialog.setTitle("提示").setMessage("确定删除")
                        .setCancel("取消", new CustomDialog.IOnCancelListener() {
                            @Override
                            public void onCancel(CustomDialog dialog) {

                            }
                        }).setConfirm("确定", new CustomDialog.IOnConfirmListener() {
                            @Override
                            public void onConfirm(CustomDialog dialog) {
                                if(Public_database_method.delete_notify(getActivity(),adapter.notifyArrayList.get(position)._id)){
                                    handler.sendEmptyMessage(0);
                                }
                            }
                        }).show();
            }
        });
        adapter.text_foot="加载更多";

        //添加滑动监听
        rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                //得到当前显示的最后一个item的view
                View lastChildView = recyclerView.getLayoutManager().getChildAt(recyclerView.getLayoutManager().getChildCount()-1);
                //得到lastChildView的bottom坐标值
                int lastChildBottom = lastChildView.getBottom();
                //得到Recyclerview的底部坐标减去底部padding值，也就是显示内容最底部的坐标
                int recyclerBottom =  recyclerView.getBottom()-recyclerView.getPaddingBottom();
                //通过这个lastChildView得到这个view当前的position值
                int lastPosition  = recyclerView.getLayoutManager().getPosition(lastChildView);

                //判断lastChildView的bottom值跟recyclerBottom
                //判断lastPosition是不是最后一个position
                //如果两个条件都满足则说明是真正的滑动到了底部
                //两种方法都可以
                //if(!rv.canScrollVertically(-1))
                if(lastPosition == recyclerView.getLayoutManager().getItemCount()-1 ){
                    if(end_flag){
                        //Toast.makeText(getActivity(), "没有更多了", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        //Toast.makeText(getActivity(), "加载更多", Toast.LENGTH_SHORT).show();
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    String text="";
                                    if(fresh+100>notifies.size()){
                                        for(int i=fresh;i<notifies.size();i++){
                                            if(i+1==fresh+100){
                                                text+=notifies.get(i).title+"."+notifies.get(i).text;
                                            }
                                            else{
                                                text+=notifies.get(i).title+"."+notifies.get(i).text+";tzl;";
                                            }
                                            end_flag=true;
                                        }
                                    }else{
                                        for(int i=fresh;i<fresh+100;i++){
                                            if(i+1==fresh+100){
                                                text+=notifies.get(i).title+"."+notifies.get(i).text;
                                            }
                                            else{
                                                text+=notifies.get(i).title+"."+notifies.get(i).text+";tzl;";
                                            }


                                        }
                                        fresh+=100;
                                    }
                                    //System.out.println(text);
                                    result=Get_data.postMethod(Get_data.url.substring(0,Get_data.url.length()-1)+":9443/thuctc?text=",text);
                                    String [] strs=result.split(";tzl;");
                                    for(int i=0;i< strs.length;i++){
                                        if(strs[i].equals(String.valueOf(Public_data.theme_mode))){
                                            list.add(notifies.get(i));
                                        }
                                    }
                                    handler.sendEmptyMessage(2);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                    }

                }
            }

        });


        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String text="";
                    if(fresh+100>notifies.size()){
                        for(int i=fresh;i<notifies.size();i++){
                            if(i+1==fresh+100){
                                text+=notifies.get(i).title+"."+notifies.get(i).text;
                            }
                            else{
                                text+=notifies.get(i).title+"."+notifies.get(i).text+";tzl;";
                            }
                            end_flag=true;
                        }
                    }else{
                        for(int i=fresh;i<fresh+100;i++){
                            if(i+1==fresh+100){
                                text+=notifies.get(i).title+"."+notifies.get(i).text;
                            }
                            else{
                                text+=notifies.get(i).title+"."+notifies.get(i).text+";tzl;";
                            }


                        }
                        fresh+=100;
                    }
                    //System.out.println(text);
                    result=Get_data.postMethod("http://123.57.153.73:8080/thuctc",text);
                    System.out.println("121212");
                    System.out.println(result);
                    String [] strs=result.split(";tzl;");
                    for(int i=0;i< strs.length;i++){
                        if(strs[i].equals(String.valueOf(Public_data.theme_mode))){
                            list.add(notifies.get(i));
                        }
                    }
                    handler.sendEmptyMessage(1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
//        rv.setAdapter(adapter);
//        adapter.setNotifyArrayList(Public_database_method.get_importance(getActivity(), Public_data.importance_mode));
    }
}
