package com.example.notify.interest_page;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.notify.Public_thing.Package_info;
import com.example.notify.Public_thing.Public_data;
import com.example.notify.Public_thing.Public_database_method;
import com.example.notify.R;

import java.util.ArrayList;
import java.util.List;

public class UpdateRuleActivity extends AppCompatActivity {

    Button btn_back;
    TextView btn_right;
    Spinner sp_APPname, sp_action;
    CheckBox cb_action_shake,cb_action_sound;
    EditText editText;
    RadioButton rb_true,rb_false,rb_positive,rb_negative;
    RadioGroup rg_1,rg_range;
    RelativeLayout rl_6;

    String packagename;
    String keyword;
    int mode;
    int action;
    int sound=0;
    int vibrate=0;
    int match;

    @SuppressLint({"MissingInflatedId", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_rule);
        sp_APPname = findViewById(R.id.sp_APPname);
        sp_action = findViewById(R.id.sp_action);
        cb_action_sound = findViewById(R.id.cb_action_sound);
        cb_action_shake = findViewById(R.id.cb_action_shake);
        rg_1 = findViewById(R.id.rg_1);
        rg_range = findViewById(R.id.rg_range);
        rb_false = findViewById(R.id.rb_false);
        rb_true = findViewById(R.id.rb_true);
        rb_positive = findViewById(R.id.rb_positive);
        rb_negative = findViewById(R.id.rb_negative);
        btn_right = findViewById(R.id.btn_right);
        rl_6 = findViewById(R.id.rl_6);
        editText = findViewById(R.id.et_keyword);
        btn_back = findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        int _id=getIntent().getIntExtra("_id",0);
        String storage_name=getIntent().getStringExtra("storage_name");
        String y_packagename=getIntent().getStringExtra("APP");
        String y_keyword=getIntent().getStringExtra("keyword");
        int y_mode=getIntent().getIntExtra("mode",0);
        int y_action=getIntent().getIntExtra("action",0);
        int y_sound=getIntent().getIntExtra("sound",0);
        int y_vibrate=getIntent().getIntExtra("vibrate",0);
        int y_match=getIntent().getIntExtra("match",0);
        ArrayList<Package_info> package_infos = getAppProcessName(this);
        String[] mItems = new String[package_infos.size() + 1];
        mItems[0] = "所有APP";
        int pos=0;
        for (int i = 0; i < package_infos.size(); i++) {
            mItems[i + 1] = package_infos.get(i).name;
            if(y_packagename.equals(package_infos.get(i).name)){
                pos=i+1;
            }
        }
        editText.setText(y_keyword);
        sp_action.setSelection(y_action);
        if(y_action!=0){
            if(y_vibrate==1){
                cb_action_shake.setChecked(true);
            }
            if(y_sound==1){
                cb_action_sound.setChecked(true);
            }
        }
        if(y_mode==0){
            rb_positive.setChecked(true);
        }
        else{
            rb_negative.setChecked(true);
        }
        if(y_match==0){
            rb_true.setChecked(true);
        }
        else{
            rb_false.setChecked(true);
        }
        sp_action.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                action = position;
                if (position > 0) {
                    rl_6.setVisibility(View.VISIBLE);
                } else {
                    rl_6.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        sp_action.setDropDownWidth(Public_data.dip2px(this, 85));
        rg_1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton checkedButton = findViewById(checkedId);
                String str = checkedButton.getText().toString();
                if (str.equals("正选")) {
                    mode = 0;
                } else if (str.equals("反选")) {
                    mode = 1;
                }
            }
        });
        rg_range.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton checkedButton = findViewById(checkedId);
                String str = checkedButton.getText().toString();
                if (str.equals("模糊匹配")) {
                    match = 0;
                } else if (str.equals("精准匹配")) {
                    match = 1;
                }
            }
        });

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, mItems);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_APPname.setAdapter(adapter);
        sp_APPname.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {

                if (pos == 0) {
                    packagename = "所有APP";
                } else {
                    packagename = package_infos.get(pos - 1).package_name;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });
        sp_APPname.setSelection(pos);
        sp_APPname.setDropDownWidth(Public_data.dip2px(this, 125));
        sp_APPname.setDropDownVerticalOffset(Public_data.dip2px(this, 35));

        btn_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keyword = editText.getText().toString().trim();
                if (keyword.length() == 0) {
                    Toast.makeText(UpdateRuleActivity.this, "请输入关键词", Toast.LENGTH_SHORT).show();
                } else if (action == 2 && !cb_action_sound.isChecked() && !cb_action_shake.isChecked()) {
                    Toast.makeText(UpdateRuleActivity.this, "升级请设置震动或声音", Toast.LENGTH_SHORT).show();
                } else {
                    if (cb_action_sound.isChecked()) {
                        sound = 1;
                    }
                    if (cb_action_shake.isChecked()) {
                        vibrate = 1;
                    }
                    Public_database_method.update_rule(UpdateRuleActivity.this, _id,packagename,keyword, mode, action, sound, vibrate, match);
                    Toast.makeText(UpdateRuleActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                    setResult(1);
                    finish();
                }
            }
        });
    }

    public ArrayList<Package_info> getAppProcessName(Context context) {
        //当前应用pid
        final PackageManager packageManager = getPackageManager();
        final Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        // get all apps
        ArrayList<Package_info> package_infos=new ArrayList<>();
        final List<ResolveInfo> apps = packageManager.queryIntentActivities(mainIntent, 0);
        for (int i = 0; i <apps.size() ; i++) {
            String name = apps.get(i).activityInfo.packageName;
            if(!name.contains("android")){
                package_infos.add(new Package_info(name,apps.get(i).loadLabel(packageManager).toString()));
            }
        }
        return package_infos;
    }

}