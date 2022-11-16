package com.example.notify.basket_page;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notify.Public_thing.Public_database_method;
import com.example.notify.R;

import java.util.ArrayList;

public class Fragment_basket_APP extends Fragment {

    private RecyclerView rv;
    private Grid_adapter adapter;
    Fragment_basket_APP_detail fragment_basket_app_detail;

    public static Fragment_basket_APP newInstance() {
        Fragment_basket_APP fragment = new Fragment_basket_APP();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_basket_app, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rv=view.findViewById(R.id.rv);
        fragment_basket_app_detail=Fragment_basket_APP_detail.newInstance();
        rv.setLayoutManager(new GridLayoutManager(getActivity(),2));
        adapter=new Grid_adapter(getActivity(), new Grid_adapter.OnItemClickListener() {
            @Override
            public void onClick(int pos) {
                setFragment(fragment_basket_app_detail);
            }
        },1);
        adapter.setList(Public_database_method.get_app_list(getActivity()));
        rv.setAdapter(adapter);
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

