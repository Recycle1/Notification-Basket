package com.example.notify.Public_thing;

import android.graphics.LightingColorFilter;
import android.view.MotionEvent;
import android.view.View;

public class Public_touch_method implements View.OnTouchListener {

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN)
            v.getBackground().setColorFilter(new LightingColorFilter(0xFF909090, 0xFF555555));
        else if (event.getAction() == MotionEvent.ACTION_UP)
            v.getBackground().clearColorFilter();
        return false;
    }
}
