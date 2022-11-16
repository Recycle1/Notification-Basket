package com.example.notify.basket_page;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.notify.R;

public class InfoDialog extends Dialog implements View.OnClickListener{
    private TextView mTvTitle,mTvMessage,mTvCancel;
    private String title,message,cancel;
    private IOnCancelListener cancelListener;

    public InfoDialog(@NonNull Context context) {
        super(context);
    }

    public InfoDialog(@NonNull Context context, int themeID) {
        super(context,themeID);
    }

    public InfoDialog setTitle(String title) {
        this.title = title;
        return this;
    }

    public InfoDialog setMessage(String message) {
        this.message = message;
        return this;
    }


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_info_dialog);

        //设置宽度
        WindowManager m=getWindow().getWindowManager();
        Display d=m.getDefaultDisplay();
        WindowManager.LayoutParams p=getWindow().getAttributes();
        Point size=new Point();
        d.getSize(size);
        p.width=(int)(size.x*0.95);//设置dialog的宽度为当前手机屏幕的宽度*0.95
        getWindow().setAttributes(p);

        mTvTitle=findViewById(R.id.tv_title);
        mTvMessage=findViewById(R.id.tv_message);
        mTvCancel=findViewById(R.id.tv_cancel);
        if(!TextUtils.isEmpty(title)){
            mTvTitle.setText(title);
        }
        if(!TextUtils.isEmpty(message)){
            mTvMessage.setText(message);
        }
        mTvCancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_cancel:
                if(cancelListener!=null){
                    cancelListener.onCancel(this);
                }
                dismiss();
                break;
        }
    }

    public interface IOnCancelListener{
        void onCancel(InfoDialog dialog);
    }

}
