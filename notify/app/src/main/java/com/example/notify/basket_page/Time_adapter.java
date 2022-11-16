package com.example.notify.basket_page;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notify.Public_thing.Get_data;
import com.example.notify.Public_thing.ImageUtils;
import com.example.notify.Public_thing.Notify;
import com.example.notify.Public_thing.Public_data;
import com.example.notify.Public_thing.Public_database_method;
import com.example.notify.Public_thing.Rule;
import com.example.notify.Public_thing.Upload;
import com.example.notify.R;
import com.example.notify.interest_page.CloudDetailActivity;
import com.example.notify.interest_page.CustomDialog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

public class Time_adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private OnItemClickListener listener;
    private OnItemClickListener1 listener1;
    public ArrayList<Notify> notifyArrayList;
    public String text_foot="没有更多了";
    //int width;
    String photo_name=Public_data.tel+"_"+Public_data.getUniqueKey();
    private Handler handler=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message message) {
            switch (message.what){
                case 0:
                    Toast.makeText(context, "已收藏过", Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    Toast.makeText(context, "收藏失败", Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    Toast.makeText(context, "收藏成功", Toast.LENGTH_SHORT).show();
                    break;
            }
            return false;
        }
    });

    public Time_adapter(Context context, OnItemClickListener listener,OnItemClickListener1 listener1){
        //this.width=width;
        this.context=context;
        this.listener=listener;
        this.listener1=listener1;
        notifyArrayList=new ArrayList<>();
    }

    public void setNotifyArrayList(ArrayList<Notify> notifyArrayList) {
        this.notifyArrayList = notifyArrayList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType==0){
            return new myholder(LayoutInflater.from(context).inflate(R.layout.layout_time_item,parent,false));
        }
        else{
            return new footholder(LayoutInflater.from(context).inflate(R.layout.layout_footer,parent,false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        if(getItemViewType(position)==0){
            PackageManager packageManager=context.getPackageManager();
            Notify notify=notifyArrayList.get(position);
            try {
                ((myholder)holder).imageView.setBackground(packageManager.getApplicationIcon(notify.packagename));
            } catch (PackageManager.NameNotFoundException e) {
                ((myholder)holder).imageView.setBackground(context.getDrawable(R.drawable.null_app));
                e.printStackTrace();
            }
            ((myholder)holder).tv_datetime.setText(notify.datetime);
            ((myholder)holder).tv_title.setText(Public_data.change_string(notify.title,15));
            ((myholder)holder).tv_text.setText(Public_data.change_string(notify.text,20));
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                listener.onClick(position);
//            }
//        });
            ((myholder)holder).ll_content.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    boolean flag=true;
                    for(int i=0;i<Public_data.pendingIntents.size();i++){
                        if(Public_data.pendingIntents.get(i)._id==notify._id){
                            try {
                                Public_data.pendingIntents.get(i).pendingIntent.send();
                                flag=false;
                            } catch (PendingIntent.CanceledException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    if(flag){
                        launch(notify.packagename);
                    }
                }
            });
            ((myholder)holder).ll_content.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    InfoDialog infoDialog=new InfoDialog(context);
                    infoDialog.setTitle(notify.title).setMessage(notify.text);
                    infoDialog.show();
                    return false;
                }
            });

            //leftParams.width = width;
            ((myholder)holder).tv_datetime.setText((notify.datetime.split("日")[1]).trim().substring(0,5));
            //ViewGroup.LayoutParams leftParams =  ((myholder)holder).ll_1.getLayoutParams();
            //((myholder)holder).ll_1.setLayoutParams(leftParams);
            ((myholder)holder).rl_1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(Public_data.login(context)){
                        CustomDialog customDialog=new CustomDialog(context);
                        customDialog.setTitle("提示").setMessage("是否收藏")
                                .setCancel("取消", new CustomDialog.IOnCancelListener() {
                                    @Override
                                    public void onCancel(CustomDialog dialog) {

                                    }
                                }).setConfirm("确定", new CustomDialog.IOnConfirmListener() {
                                    @Override
                                    public void onConfirm(CustomDialog dialog) {
                                        new Thread(new Runnable() {
                                            @Override
                                            public void run() {
                                                try {
                                                    PackageInfo info = packageManager.getPackageInfo(notify.packagename,PackageManager.GET_ACTIVITIES);
                                                    if(Get_data.touchHtml(Get_data.url+"notify/get_collection_used.php?tel="+Public_data.tel+"&name="+info.applicationInfo.loadLabel(packageManager).toString()+"&title="+notify.title+"&text="+notify.text).equals("有")){
                                                        handler.sendEmptyMessage(0);
                                                    }
                                                    else{
                                                        if(Get_data.touchHtml(Get_data.url+"notify/insert_collection.php?tel="+Public_data.tel+"&name="+info.applicationInfo.loadLabel(packageManager).toString()+"&image="+Get_data.url+"notify/collection_image/"+photo_name+".png"+"&title="+notify.title+"&text="+notify.text+"&datetime="+notify.datetime).equals("上传成功")){
                                                            System.out.println(packageManager.getApplicationIcon(notify.packagename).toString());
                                                            Bitmap bm=getBitmapFromDrawable(packageManager.getApplicationIcon(notify.packagename));
                                                            uploadPic(bm);
                                                        }
                                                        else{
                                                            handler.sendEmptyMessage(1);
                                                        }
                                                    }
                                                } catch (Exception e) {
                                                    try {
                                                        if(Get_data.touchHtml(Get_data.url+"notify/get_collection_used.php?tel="+Public_data.tel+"&name=未知应用&title="+notify.title+"&text="+notify.text).equals("有")){
                                                            handler.sendEmptyMessage(0);
                                                        }
                                                        else{
                                                            if(Get_data.touchHtml(Get_data.url+"notify/insert_collection.php?tel="+Public_data.tel+"&name=未知应用&image="+Get_data.url+"notify/collection_image/"+photo_name+".png"+"&title="+notify.title+"&text="+notify.text+"&datetime="+notify.datetime).equals("上传成功")){
                                                                Bitmap bm=getBitmapFromDrawable(context.getDrawable(R.drawable.null_app));
                                                                uploadPic(bm);
                                                            }
                                                            else{
                                                                handler.sendEmptyMessage(1);
                                                            }
                                                        }
                                                    } catch (Exception ex) {
                                                        handler.sendEmptyMessage(0);
                                                        ex.printStackTrace();
                                                    }
                                                    e.printStackTrace();
                                                }
                                            }
                                        }).start();
                                    }
                                }).show();
                    }
                }
            });
            ((myholder)holder).rl_2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener1.onClick(position);
                }
            });
        }
        else{
            ((footholder)holder).tv_foot.setText(text_foot);
        }

    }

    @Override
    public int getItemViewType(int position) {
        if(position==notifyArrayList.size()){
            return 1;
        }
        return 0;
    }

    @Override
    public int getItemCount() {
        return notifyArrayList.size()+1;
    }

    class myholder extends RecyclerView.ViewHolder{

        private ImageView imageView;
        private TextView tv_title;
        private TextView tv_text;
        private TextView tv_datetime;
        private LinearLayout ll_content;
        private LinearLayout ll_1;
        private RelativeLayout rl_1;
        private RelativeLayout rl_2;

        public myholder(View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.imageview);
            tv_title=itemView.findViewById(R.id.tv_title);
            tv_text=itemView.findViewById(R.id.tv_text);
            tv_datetime=itemView.findViewById(R.id.tv_datetime);
            ll_content=itemView.findViewById(R.id.ll_content);
            ll_1=itemView.findViewById(R.id.ll_1);
            rl_1=itemView.findViewById(R.id.rl_1);
            rl_2=itemView.findViewById(R.id.rl_2);
        }
    }

    class footholder extends RecyclerView.ViewHolder{

        TextView tv_foot;

        public footholder(View itemView) {
            super(itemView);
            tv_foot=itemView.findViewById(R.id.tv_foot);
        }
    }

    public interface OnItemClickListener{
        void onClick(int position);
    }

    public interface OnItemClickListener1{
        void onClick(int position);
    }

    private Bitmap getBitmapFromDrawable(@NonNull Drawable drawable) {
        final Bitmap bmp = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(bmp);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bmp;
    }

    private void uploadPic(Bitmap bitmap) {
        // 上传至服务器
        // ... 可以在这里把Bitmap转换成file，然后得到file的url，做文件上传操作
        // 注意这里得到的图片已经是圆形图片了
        // bitmap是没有做个圆形处理的，但已经被裁剪了
        String imagePath = ImageUtils.savePhoto(bitmap, Environment
                .getExternalStorageDirectory().getAbsolutePath(), String
                .valueOf(System.currentTimeMillis()));
        Log.e("imagePath", imagePath+"");
        if(imagePath != null){
            // 使用imagePath上传
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Upload upload=new Upload(imagePath, Get_data.url+"notify/upload_collection_image.php",photo_name);
                    handler.sendEmptyMessage(2);
                }
            }).start();


        }
    }

    private void launch(String packagename) {
        Intent intent = context.getPackageManager().getLaunchIntentForPackage(packagename);
        // 这里如果intent为空，就说名没有安装要跳转的应用嘛
        if (intent != null) {
            context.startActivity(intent);
        } else {
            // 没有安装要跳转的app应用，提醒一下
            Toast.makeText(context, "暂无此APP", Toast.LENGTH_LONG).show();
        }
    }

}

