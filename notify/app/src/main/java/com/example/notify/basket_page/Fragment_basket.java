package com.example.notify.basket_page;

import android.app.Fragment;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notify.Notify_service1;
import com.example.notify.Public_thing.SharedPreferencesUtils;
import com.example.notify.R;

public class Fragment_basket extends Fragment{

    private RecyclerView rv;
    private Switch switch1;
    private TextView btn_time;
    private TextView btn_theme;
    private TextView btn_importance;
    private TextView btn_APP;
    public ServiceConnection serviceConnection;

    private Fragment fragment;
    private Fragment_basket_time fragment_basket_time;
    private Fragment_basket_importance fragment_basket_importance;
    private Fragment_basket_theme fragment_basket_theme;
    private Fragment_basket_APP fragment_basket_app;

    public static Fragment_basket newInstance() {
        Fragment_basket fragment = new Fragment_basket();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_basket, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        switch1 = view.findViewById(R.id.switch1);
        btn_time=view.findViewById(R.id.time);
        btn_theme=view.findViewById(R.id.theme);
        btn_importance=view.findViewById(R.id.importance);
        btn_APP=view.findViewById(R.id.app);
        btn_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_time.setBackground(getResources().getDrawable(R.drawable.button));
                btn_theme.setBackground(getResources().getDrawable(R.color.white));
                btn_importance.setBackground(getResources().getDrawable(R.color.white));
                btn_APP.setBackground(getResources().getDrawable(R.color.white));
                setFragment(fragment_basket_time);
            }
        });
        btn_theme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_time.setBackground(getResources().getDrawable(R.color.white));
                btn_theme.setBackground(getResources().getDrawable(R.drawable.button));
                btn_importance.setBackground(getResources().getDrawable(R.color.white));
                btn_APP.setBackground(getResources().getDrawable(R.color.white));
                setFragment(fragment_basket_theme);
            }
        });
        btn_importance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_time.setBackground(getResources().getDrawable(R.color.white));
                btn_theme.setBackground(getResources().getDrawable(R.color.white));
                btn_importance.setBackground(getResources().getDrawable(R.drawable.button));
                btn_APP.setBackground(getResources().getDrawable(R.color.white));
                setFragment(fragment_basket_importance);
            }
        });
        btn_APP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_time.setBackground(getResources().getDrawable(R.color.white));
                btn_theme.setBackground(getResources().getDrawable(R.color.white));
                btn_importance.setBackground(getResources().getDrawable(R.color.white));
                btn_APP.setBackground(getResources().getDrawable(R.drawable.button));
                setFragment(fragment_basket_app);
            }
        });
        SharedPreferences aSwitch = getActivity().getSharedPreferences("switch", 0);
        if (aSwitch != null) {
            boolean flag = false;
            boolean state = aSwitch.getBoolean("state", flag);
            switch1.setChecked(state);
        }
        switch1.setEnabled(false);
        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked) {
//                    if (!isEnabled()) {
//                        System.out.println("11");
//                        startActivity(new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"));
//                    }
//                    else{
//                        toggleNotificationListenerService(getActivity());
//                        getActivity().startService(new Intent(getActivity(),Notify_service1.class));
//                        //Toast.makeText(getActivity(), "1", Toast.LENGTH_SHORT).show();
//                    }
//                } else {
//                    getActivity().stopService(new Intent(getActivity(), Notify_service1.class));
//                }
//                SharedPreferencesUtils.saveSwitch(isChecked,getActivity());
            }
        });
        fragment_basket_time = Fragment_basket_time.newInstance();
        fragment_basket_importance = Fragment_basket_importance.newInstance();
        fragment_basket_theme = Fragment_basket_theme.newInstance();
        fragment_basket_app=Fragment_basket_APP.newInstance();
        getFragmentManager().beginTransaction().add(R.id.fragment_basket, fragment_basket_time, "main").commitAllowingStateLoss();
    }

    private boolean isEnabled() {
        String pkgName = getActivity().getPackageName();
        final String flat = Settings.Secure.getString(getActivity().getContentResolver(), "enabled_notification_listeners");
        if (!TextUtils.isEmpty(flat)) {
            final String[] names = flat.split(":");
            for (int i = 0; i < names.length; i++) {
                final ComponentName cn = ComponentName.unflattenFromString(names[i]);
                if (cn != null) {
                    if (TextUtils.equals(pkgName, cn.getPackageName())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static void toggleNotificationListenerService(Context context) {
        PackageManager pm = context.getPackageManager();
        pm.setComponentEnabledSetting(
                new ComponentName(context, Notify_service1.class),
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);

        pm.setComponentEnabledSetting(
                new ComponentName(context, Notify_service1.class),
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
    }

    private void setFragment(Fragment target_fragment) {
        Fragment fragment = getFragmentManager().findFragmentByTag("basket");
        if (fragment != null) {
            getFragmentManager().beginTransaction().hide(fragment).replace(R.id.fragment_basket, target_fragment).commitAllowingStateLoss();
        } else {
            getFragmentManager().beginTransaction().replace(R.id.fragment_basket, target_fragment).commitAllowingStateLoss();
        }
    }

}

