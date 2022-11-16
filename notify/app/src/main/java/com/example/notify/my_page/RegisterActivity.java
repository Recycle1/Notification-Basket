package com.example.notify.my_page;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.notify.Public_thing.Get_data;
import com.example.notify.Public_thing.ImageUtils;
import com.example.notify.Public_thing.Public_data;
import com.example.notify.Public_thing.Upload;
import com.example.notify.Public_thing.UriUtils;
import com.example.notify.R;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class RegisterActivity extends AppCompatActivity {

    private EditText et_tel,et_user_name,et_password,et_confirm;
    private RadioButton man,woman;
    private Button btn_cancel,btn_confirm,btn_image;
    private ImageView imageView;

    String tel;
    String user_name;
    String gender;
    String password;
    String confirm;

    int vis=0;
    Uri uri;

    protected static final int CHOOSE_PICTURE = 0;
    protected static final int TAKE_PICTURE = 1;
    private static final int CROP_SMALL_PICTURE = 2;
    protected static Uri tempUri;
    private Bitmap class_photo=null;
    String photo_name;
    private Handler handler=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message message) {
            switch (message.what){
                case 0:
                    Toast.makeText(RegisterActivity.this, "请输入电话号码", Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    Toast.makeText(RegisterActivity.this, "请输入正确电话号码", Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    Toast.makeText(RegisterActivity.this, "电话号码已注册", Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    Toast.makeText(RegisterActivity.this, "请输入用户名", Toast.LENGTH_SHORT).show();
                    break;
                case 4:
                    Toast.makeText(RegisterActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
                    break;
                case 5:
                    Toast.makeText(RegisterActivity.this, "请输入确认密码", Toast.LENGTH_SHORT).show();
                    break;
                case 6:
                    Toast.makeText(RegisterActivity.this, "密码输入不一致", Toast.LENGTH_SHORT).show();
                    break;
                case 7:
                    Toast.makeText(RegisterActivity.this, "请放置用户头像", Toast.LENGTH_SHORT).show();
                    break;
                case 8:
                    Toast.makeText(RegisterActivity.this, "注册失败", Toast.LENGTH_SHORT).show();
                    break;
                case 9:
                    Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                    finish();
                    break;
            }
            return false;
        }
    });

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        et_tel=findViewById(R.id.et_tel);
        et_user_name=findViewById(R.id.et_user_name);
        et_password=findViewById(R.id.et_password);
        et_confirm=findViewById(R.id.et_confirm);
        man=findViewById(R.id.man);
        woman=findViewById(R.id.woman);
        btn_cancel=findViewById(R.id.cancel);
        btn_confirm=findViewById(R.id.btn_confirm);
        btn_image=findViewById(R.id.btn_image);
        imageView=findViewById(R.id.imageview);
        char Vision[] ;
        Vision = Build.VERSION.RELEASE.toCharArray();
        vis=0;
        for (int i = 0, j = 1; (i < Vision.length) && (Vision[i] != '.'); i++, j = j * 10) {
            vis = vis * j + ((int) Vision[i] - 48);
        }
        btn_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showChoosePicDialog();
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tel=et_tel.getText().toString().trim();
                user_name=et_user_name.getText().toString().trim();
                if(man.isChecked()){
                    gender="男";
                }
                else if(woman.isChecked()){
                    gender="女";
                }
                password=et_password.getText().toString().trim();
                confirm=et_confirm.getText().toString().trim();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            if(tel.length()==0){
                                handler.sendEmptyMessage(0);
                            }
                            else if(tel.length()!=11){
                                handler.sendEmptyMessage(1);
                            }
                            else if(Get_data.touchHtml(Get_data.url+"notify/get_user_used.php?tel="+tel).equals("有")){
                                handler.sendEmptyMessage(2);
                            }
                            else if (user_name.length()==0){
                                handler.sendEmptyMessage(3);
                            }
                            else if(password.length()==0){
                                handler.sendEmptyMessage(4);
                            }
                            else if(confirm.length()==0){
                                handler.sendEmptyMessage(5);
                            }
                            else if(!password.equals(confirm)){
                                handler.sendEmptyMessage(6);
                            }
                            else if(class_photo==null&&uri==null){
                                handler.sendEmptyMessage(7);
                            }
                            else{
                                String user_image_name=Get_data.url+"notify/user_image/user_"+tel+".png";
                                if(Get_data.touchHtml(Get_data.url+"notify/insert_user.php?tel="+tel+"&name="+user_name+"&password="+password+"&gender="+gender+"&image="+user_image_name).equals("no")){
                                    handler.sendEmptyMessage(8);
                                }
                                else{
                                    if(vis>=11){
                                        uploadPic1(RegisterActivity.this,uri);
                                    }
                                    else{
                                        uploadPic(class_photo);
                                    }

                                    handler.sendEmptyMessage(9);
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });
    }

    /**
     * 显示修改头像的对话框
     */
    protected void showChoosePicDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("选择封面");
        String[] items = { "选择本地照片" };
        builder.setNegativeButton("取消", null);
        builder.setItems(items, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case CHOOSE_PICTURE: // 选择本地照片
                        Intent openAlbumIntent = new Intent(
                                Intent.ACTION_PICK);
                        openAlbumIntent.setType("image/*");
                        startActivityForResult(openAlbumIntent, CHOOSE_PICTURE);
                        break;
//                    case TAKE_PICTURE: // 拍照
//                        takePicture();
//                        break;
                }
            }
        });
        builder.create().show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println(resultCode);
        if (resultCode == RESULT_OK) { // 如果返回码是可以用的
            switch (requestCode) {
//                case TAKE_PICTURE:
//                    startPhotoZoom(tempUri); // 开始对图片进行裁剪处理
//                    break;
                case CHOOSE_PICTURE:
                    if(vis>=11){
                        imageView.setImageBitmap(ImageSizeCompress(data.getData()));
                        //class_photo=ImageSizeCompress(data.getData());
                        uri=data.getData();
                    }
                    else{
                        startPhotoZoom(data.getData()); // 开始对图片进行裁剪处理
                    }
                    break;
                case CROP_SMALL_PICTURE:
                    if (data != null) {
                        setImageToView(data); // 让刚才选择裁剪得到的图片显示在界面上
                    }
                    break;
            }
        }
    }

    //     保存裁剪之后的图片数据
    protected void setImageToView(Intent data) {
        Bundle extras = data.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
            //photo = ImageUtils.toRoundBitmap(photo); // 这个时候的图片已经被处理成圆形的了
            imageView.setImageBitmap(photo);
            class_photo=photo;

//            上传到服务器
            //uploadPic(photo);
        }
    }

    private Bitmap ImageSizeCompress(Uri uri){
        InputStream Stream = null;
        InputStream inputStream = null;
        try {
            //根据uri获取图片的流
            inputStream = getContentResolver().openInputStream(uri);
            BitmapFactory.Options options = new BitmapFactory.Options();
            //options的in系列的设置了，injustdecodebouond只解析图片的大小，而不加载到内存中去
            options.inJustDecodeBounds = true;
            //1.如果通过options.outHeight获取图片的宽高，就必须通过decodestream解析同options赋值
            //否则options.outheight获取不到宽高
            BitmapFactory.decodeStream(inputStream,null,options);
            //2.通过 btm.getHeight()获取图片的宽高就不需要1的解析，我这里采取第一张方式
//            Bitmap btm = BitmapFactory.decodeStream(inputStream);
            //以屏幕的宽高进行压缩
            DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
            int heightPixels = displayMetrics.heightPixels;
            int widthPixels = displayMetrics.widthPixels;
            //获取图片的宽高
            int outHeight = options.outHeight;
            int outWidth = options.outWidth;
            //heightPixels就是要压缩后的图片高度，宽度也一样
            int a = (int) Math.ceil((outHeight/(float)heightPixels));
            int b = (int) Math.ceil(outWidth/(float)widthPixels);
            //比例计算,一般是图片比较大的情况下进行压缩
            int max = Math.max(a, b);
            if(max > 1){
                options.inSampleSize = max;
            }
            //解析到内存中去
            options.inJustDecodeBounds = false;
//            根据uri重新获取流，inputstream在解析中发生改变了
            Stream = getContentResolver().openInputStream(uri);
            Bitmap bitmap = BitmapFactory.decodeStream(Stream, null, options);
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                if(inputStream != null) {
                    inputStream.close();
                }
                if(Stream != null){
                    Stream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return  null;
    }


    // 裁剪图片方法实现
    protected void startPhotoZoom(Uri uri) {
        System.out.println(uri.getPath());
        if (uri == null) {
            Log.i("tag", "The uri is not exist.");
        }
        tempUri = uri;
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        // 设置裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        intent.putExtra("scale", true);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, CROP_SMALL_PICTURE);
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
                    photo_name="user_"+tel;
                    Upload upload=new Upload(imagePath, Get_data.url+"notify/upload_user_image.php",photo_name);
                }
            }).start();


        }
    }

    private void uploadPic1(Context context, Uri uri) {
            // 使用imagePath上传
            new Thread(new Runnable() {
                @Override
                public void run() {
                    photo_name="user_"+tel;
                    Upload upload=new Upload(UriUtils.getFileAbsolutePath(context,uri), Get_data.url+"notify/upload_user_image.php",photo_name);
                }
            }).start();
    }

}