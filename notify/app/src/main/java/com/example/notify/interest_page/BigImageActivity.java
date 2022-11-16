package com.example.notify.interest_page;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.example.notify.R;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutionException;

public class BigImageActivity extends AppCompatActivity {

    private ImageView mImgzoom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_big_image);
        mImgzoom = (ImageView) findViewById(R.id.imgzoom);
        String url=getIntent().getStringExtra("url");
        Glide.with(this).load(url).into(mImgzoom);
    }

}