package com.example.notify.interest_page;

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

public class RuleDetailDialog  extends Dialog implements View.OnClickListener{
    private TextView tv_packagename,tv_keyword,tv_mode,tv_action,tv_vibrate,tv_sound,tv_match,tv_cancel;;
    private String title,message;
    private String packagename,keyword,mode,action,vibrate,sound,match;
    private IOnCancelListener cancelListener;

    public RuleDetailDialog(@NonNull Context context) {
        super(context);
    }

    public RuleDetailDialog(@NonNull Context context, int themeID) {
        super(context,themeID);
    }

    public RuleDetailDialog setTitle(String title) {
        this.title = title;
        return this;
    }

    public void setPackagename(String packagename) {
        this.packagename = packagename;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public void setVibrate(String vibrate) {
        this.vibrate = vibrate;
    }

    public void setSound(String sound) {
        this.sound = sound;
    }

    public void setMatch(String match) {
        this.match = match;
    }

    public RuleDetailDialog setMessage(String message) {
        this.message = message;
        return this;
    }

    public RuleDetailDialog setCancel(IOnCancelListener listener) {
        this.cancelListener=listener;
        return this;
    }



    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_rule_dialog);

        //设置宽度
        WindowManager m=getWindow().getWindowManager();
        Display d=m.getDefaultDisplay();
        WindowManager.LayoutParams p=getWindow().getAttributes();
        Point size=new Point();
        d.getSize(size);
        p.width=(int)(size.x*0.8);//设置dialog的宽度为当前手机屏幕的宽度*0.8
        getWindow().setAttributes(p);
        tv_packagename=findViewById(R.id.tv_packagename);
        tv_keyword=findViewById(R.id.tv_keyword);
        tv_mode=findViewById(R.id.tv_mode);
        tv_action=findViewById(R.id.tv_action);
        tv_vibrate=findViewById(R.id.tv_vibrate);
        tv_sound=findViewById(R.id.tv_sound);
        tv_match=findViewById(R.id.tv_match);
        tv_cancel=findViewById(R.id.tv_cancel);
        tv_packagename.setText(packagename);
        tv_keyword.setText(keyword);
        tv_mode.setText(mode);
        tv_action.setText(action);
        if(!TextUtils.isEmpty(vibrate)){
            tv_vibrate.setVisibility(View.VISIBLE);
            tv_vibrate.setText(vibrate);
        }
        if(!TextUtils.isEmpty(sound)){
            tv_sound.setVisibility(View.VISIBLE);
            tv_sound.setText(sound);
        }
        tv_match.setText(match);
        tv_cancel.setOnClickListener(this);
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
        void onCancel(RuleDetailDialog dialog);
    }
}
