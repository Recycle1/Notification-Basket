package com.example.notify.basket_page;

import android.app.Application;
import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notify.Public_thing.Public_data;
import com.example.notify.Public_thing.Public_database_method;
import com.example.notify.Public_thing.Public_touch_method;
import com.example.notify.R;
import com.example.notify.interest_page.CustomDialog;

public class Fragment_basket_importance_detail extends Fragment {

    private SlideRecyclerView rv;
    Time_adapter adapter;
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
                    break;
            }
            return false;
        }
    });

    public static Fragment_basket_importance_detail newInstance() {
        Fragment_basket_importance_detail fragment = new Fragment_basket_importance_detail();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_basket_importance_detail, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rv=view.findViewById(R.id.rv);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
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
        rv.setAdapter(adapter);
        adapter.setNotifyArrayList(Public_database_method.get_importance(getActivity(), Public_data.importance_mode));
    }

}

