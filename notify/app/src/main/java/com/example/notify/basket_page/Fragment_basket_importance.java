package com.example.notify.basket_page;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notify.Public_thing.Public_data;
import com.example.notify.Public_thing.Public_touch_method;
import com.example.notify.R;

public class Fragment_basket_importance extends Fragment {

    private Fragment_basket_importance_detail fragment_basket_importance_detail;
    private RelativeLayout rl_red;
    private RelativeLayout rl_blue;
    private RelativeLayout rl_yellow;
    private Fragment fragment;

    public static Fragment_basket_importance newInstance() {
        Fragment_basket_importance fragment = new Fragment_basket_importance();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_basket_importance, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rl_red=view.findViewById(R.id.rl_red);
        rl_blue=view.findViewById(R.id.rl_blue);
        rl_yellow=view.findViewById(R.id.rl_yellow);
        rl_red.setOnTouchListener(new Public_touch_method());
        fragment_basket_importance_detail=Fragment_basket_importance_detail.newInstance();
        rl_red.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Public_data.importance_mode=1;
                setFragment(fragment_basket_importance_detail);
            }
        });
        rl_blue.setOnTouchListener(new Public_touch_method());
        rl_blue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Public_data.importance_mode=2;
                setFragment(fragment_basket_importance_detail);
            }
        });
        rl_yellow.setOnTouchListener(new Public_touch_method());
        rl_yellow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Public_data.importance_mode=3;
                setFragment(fragment_basket_importance_detail);
            }
        });
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

