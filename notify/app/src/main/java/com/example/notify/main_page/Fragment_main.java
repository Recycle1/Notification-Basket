package com.example.notify.main_page;

import android.Manifest;
import android.app.Fragment;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Outline;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.notify.Public_thing.Cloud_rule_repositories;
import com.example.notify.Public_thing.Get_data;
import com.example.notify.Public_thing.Public_database;
import com.example.notify.Public_thing.Public_database_method;
import com.example.notify.R;
import com.example.notify.interest_page.CloudActivity;
import com.example.notify.interest_page.CloudDetailActivity;
import com.example.notify.interest_page.Cloud_adapter;
import com.youth.banner.Banner;
import com.youth.banner.adapter.BannerAdapter;
import com.youth.banner.indicator.RectangleIndicator;
import com.youth.banner.transformer.DepthPageTransformer;

import java.util.ArrayList;
import java.util.List;

public class Fragment_main extends Fragment {

    private RecyclerView rv;
    private Banner banner;
    private TextView tv_n;
    private RelativeLayout rl_1;
    private Cloud_adapter adapter;
    private ArrayList<Cloud_rule_repositories> list;
    private Handler handler=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message message) {
            switch (message.what){
                case 0:
                    adapter.setCloud_rule_repositories_ArrayList(list);
                    rv.setAdapter(adapter);
                    break;
                case 1:
                    break;
            }
            return false;
        }
    });

    public static Fragment_main newInstance() {
        Fragment_main fragment = new Fragment_main();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        banner=view.findViewById(R.id.banner);
        tv_n=view.findViewById(R.id.tv_n);
        rv=view.findViewById(R.id.rv);
        rl_1=view.findViewById(R.id.rl_1);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        Intent intent=new Intent(getActivity(), CloudDetailActivity.class);
        adapter=new Cloud_adapter(getActivity(), new Cloud_adapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                Intent intent=new Intent(getActivity(), CloudDetailActivity.class);
                intent.putExtra("tel",list.get(position).tel);
                intent.putExtra("storage_name",list.get(position).storage_name);
                intent.putExtra("description",list.get(position).description);
                intent.putExtra("fork",list.get(position).fork);
                startActivityForResult(intent,0);
            }
        },1);
        rl_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(),InformationActivity.class);
                startActivity(intent);
            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    list=Get_data.get_cloud(3);
                    handler.sendEmptyMessage(0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
        tv_n.setText(String.valueOf(Public_database_method.getsum(getActivity())));
        initBanner();
    }

    void initBanner(){
        ArrayList list=new ArrayList();
        list.add("https://www.recycle11.top/notify/ad/1.png");
        list.add("https://www.recycle11.top/notify/ad/2.png");
        list.add("https://www.recycle11.top/notify/ad/3.png");
//        banner.setImageLoader(new GlideImageLoader());
//        banner.setImages(list);
        banner.setDatas(list);
         banner.setAdapter(new ImageAdapter(list));
         banner.setPageTransformer(new DepthPageTransformer());
         banner.setIndicator(new RectangleIndicator(getActivity()));
        banner.start();
    }

    public class ImageAdapter extends BannerAdapter<String, ImageAdapter.BannerViewHolder> {

        public ImageAdapter(List<String> mDatas) {
            //设置数据，也可以调用banner提供的方法,或者自己在adapter中实现
            super(mDatas);
        }

        //创建ViewHolder，可以用viewType这个字段来区分不同的ViewHolder
        @Override
        public BannerViewHolder onCreateHolder(ViewGroup parent, int viewType) {
            ImageView imageView = new ImageView(parent.getContext());
            //注意，必须设置为match_parent，这个是viewpager2强制要求的
            imageView.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            return new BannerViewHolder(imageView);
        }

        @Override
        public void onBindView(BannerViewHolder holder, String data, int position, int size) {
//            holder.imageView.setImageResource(data.imageRes);
            Glide.with(getActivity()).load(mDatas.get(position)).into(holder.imageView);
            holder.imageView.setOutlineProvider(new ViewOutlineProvider() {
                @Override
                public void getOutline(View view, Outline outline) {
                    outline.setRoundRect(80, 0, view.getWidth()-80, view.getHeight(), 40);
                }
            });
            holder.imageView.setClipToOutline(true);
            holder.imageView.setPadding(80,0,80,0);
        }

        class BannerViewHolder extends RecyclerView.ViewHolder {
            ImageView imageView;

            public BannerViewHolder(@NonNull ImageView view) {
                super(view);
                this.imageView = view;
            }
        }
    }

}


