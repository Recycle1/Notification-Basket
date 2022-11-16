package com.example.notify.basket_page;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.transition.Slide;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notify.Public_thing.Get_data;
import com.example.notify.Public_thing.Notify;
import com.example.notify.Public_thing.Public_data;
import com.example.notify.Public_thing.Public_database;
import com.example.notify.Public_thing.Public_database_method;
import com.example.notify.Public_thing.SharedPreferencesUtils;
import com.example.notify.R;
import com.example.notify.interest_page.CustomDialog;

import java.util.ArrayList;
import java.util.Calendar;

public class Fragment_basket_time extends Fragment  implements View.OnClickListener,CustomDatePickerDialogFragment.OnSelectedDateListener {

    private SlideRecyclerView rv;
    private Time_adapter time_adapter;
    private Spinner spinner;
    private Button button;
    private TextView tv_date;
    //Public_database helper;
    int c_year;
    int c_month;
    int c_day;
    private Handler handler=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message message) {
            switch (message.what){
                case 0:
                    Toast.makeText(getActivity(), "删除成功", Toast.LENGTH_SHORT).show();
                    time_adapter.setNotifyArrayList(Public_database_method.getdate(getActivity(),c_year + "年" + totwo(c_month) + "月" + totwo(c_day)));
                    time_adapter.notifyDataSetChanged();
                    break;
            }
            return false;
        }
    });

    public static Fragment_basket_time newInstance() {
        Fragment_basket_time fragment = new Fragment_basket_time();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_basket_time, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rv=view.findViewById(R.id.rv);
        spinner = view.findViewById(R.id.spinner);
        button = view.findViewById(R.id.btn);
        tv_date = view.findViewById(R.id.tv_date);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
//        DisplayMetrics metric = new DisplayMetrics();
//        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metric);
//        int windowsWidth = metric.widthPixels;
        time_adapter=new Time_adapter(getActivity(), new Time_adapter.OnItemClickListener() {
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
                                if(Public_database_method.delete_notify(getActivity(),time_adapter.notifyArrayList.get(position)._id)){
                                    handler.sendEmptyMessage(0);
                                }
                            }
                        }).show();
            }
        });

        SharedPreferences aDate = getActivity().getSharedPreferences("date", 0);
        if(aDate!=null){
            Calendar calendar=Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            c_year=calendar.get(Calendar.YEAR);
            c_month=calendar.get(Calendar.MONTH)+1;
            c_day=calendar.get(Calendar.DAY_OF_MONTH);
            String date=aDate.getString("date_info",c_year+"年"+c_month+"月"+c_day+"日");
            c_year=Integer.valueOf(date.split("年")[0]);
            c_month=Integer.valueOf((date.split("年")[1]).split("月")[0]);
            c_day=Integer.valueOf((date.split("月")[1]).split("日")[0]);
        }
        tv_date.setText(c_year+"年"+c_month+"月"+c_day+"日");
        ArrayList<Notify> notifies=new ArrayList<>();
//        helper=new Public_database(getActivity());
//        helper.getWritableDatabase();
        notifies= Public_database_method.getdate(getActivity(),c_year + "年" + totwo(c_month) + "月" + totwo(c_day));
        time_adapter.setNotifyArrayList(notifies);
        rv.setAdapter(time_adapter);
//        String[] mItems = getResources().getStringArray(R.array.languages);
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, mItems);
//        adapter.setDropDownViewResource(R.layout.layout_spinner_item);
//        spinner.setAdapter(adapter);
//        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @SuppressLint("WrongConstant")
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
//                switch (pos) {
//                    case 0:
//
//                        break;
//                    case 1:
//
//                        break;
//                    case 2:
//
//                        break;
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//                // Another interface callback
//            }
//        });
//        spinner.setDropDownWidth(dip2px(getActivity(), 125));
//        spinner.setDropDownHorizontalOffset(dip2px(getActivity(), -9));
//        spinner.setDropDownVerticalOffset(dip2px(getActivity(), 7));
        tv_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Public_database_method.get_sum(getActivity())!=0&&Public_database_method.get_different(getActivity())){
                    showDatePickDialog();
                }
            }
        });
    }

    @Override
    public void onSelectedDate(int year, int monthOfYear, int dayOfMonth) {
        //Toast.makeText(getActivity(), year + "年" + (monthOfYear + 1) + "月" + dayOfMonth + "日", Toast.LENGTH_SHORT).show();
        c_year=year;
        c_month=monthOfYear+1;
        c_day=dayOfMonth;
        tv_date.setText(c_year+"年"+c_month+"月"+c_day+"日");
        ArrayList<Notify> notifies1=new ArrayList<>();
        notifies1=Public_database_method.getdate(getActivity(),year + "年" + totwo(monthOfYear + 1) + "月" + totwo(dayOfMonth));
        time_adapter.setNotifyArrayList(notifies1);
        time_adapter.notifyDataSetChanged();
        SharedPreferencesUtils.saveDate(tv_date.getText().toString(),getActivity());
    }

    long day = 24 * 60 * 60 * 1000;

    private void showDatePickDialog() {
        CustomDatePickerDialogFragment fragment = new CustomDatePickerDialogFragment();
        fragment.setOnSelectedDateListener(this);
        Bundle bundle = new Bundle();
        Calendar currentDate = Calendar.getInstance();
        //currentDate.setTimeInMillis(System.currentTimeMillis());
        currentDate.set(c_year,(c_month-1),c_day);
        currentDate.set(Calendar.HOUR_OF_DAY, 0);
        currentDate.set(Calendar.MINUTE, 0);
        currentDate.set(Calendar.SECOND, 0);
        currentDate.set(Calendar.MILLISECOND, 0);
        bundle.putSerializable(CustomDatePickerDialogFragment.CURRENT_DATE, currentDate);


        Calendar s_Date=Calendar.getInstance();
        String start1=Public_database_method.getstart(getActivity());
        if(start1!=null){
            int y=Integer.valueOf(start1.split("年")[0]);
            int m=Integer.valueOf((start1.split("年")[1]).split("月")[0]);
            int d=Integer.valueOf((start1.split("月")[1]).split("日")[0]);
            s_Date.set(y,(m-1),d);
        }
        else{
            s_Date.setTimeInMillis(System.currentTimeMillis());
        }
        s_Date.set(Calendar.HOUR_OF_DAY, 0);
        s_Date.set(Calendar.MINUTE, 0);
        s_Date.set(Calendar.SECOND, 0);
        s_Date.set(Calendar.MILLISECOND, 0);

        long start = currentDate.getTimeInMillis() - day * 2;
        Calendar startDate = Calendar.getInstance();
        startDate.setTimeInMillis(start);
        Calendar endDate = Calendar.getInstance();
        endDate.setTimeInMillis(System.currentTimeMillis());
        bundle.putSerializable(CustomDatePickerDialogFragment.START_DATE, s_Date);
        bundle.putSerializable(CustomDatePickerDialogFragment.END_DATE, endDate);

        fragment.setArguments(bundle);

        fragment.show(getFragmentManager(), CustomDatePickerDialogFragment.class.getSimpleName());

    }

    @Override
    public void onClick(View v) {

    }

    public String totwo(int number){
        if(number<10){
            return "0"+number;
        }
        else{
            return ""+number;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString("date",tv_date.getText().toString());
        super.onSaveInstanceState(outState);
    }
}
