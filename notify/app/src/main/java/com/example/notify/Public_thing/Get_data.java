package com.example.notify.Public_thing;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class Get_data {

    static public String url="https://www.recycle11.top/";

    public static String touchHtml(String path) throws Exception {
        //System.out.println(path);
        URL url = new URL(path);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(5000);
        conn.setRequestMethod("GET");
        if (conn.getResponseCode() == 200) {
            InputStream inputStream = conn.getInputStream();
            byte[] data = StreamTool.read(inputStream);
            String text = new String(data);
            //System.out.println("1111111111111111111111111111111111111");

            return text;
        }
        return "no";
    }

    public static String postMethod(String url,String param){
        // 结果值
        StringBuffer rest=new StringBuffer();

        HttpURLConnection conn=null;
        OutputStream out=null;
        BufferedReader br=null;

        try {
            // 创建 URL
            URL restUrl = new URL(url);
            // 打开连接
            conn= (HttpURLConnection) restUrl.openConnection();
            // 设置请求方式为 POST
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection","keep-Alive");
            // 设置接收文件类型
            conn.setRequestProperty("Accept","application/json");
            //设置发送文件类型
            /**
             这里注意  传递JSON数据的话 就要设置
             普通参数的话 就要注释掉
             */
            conn.setRequestProperty("Content-Type","application/json");
            // 输入 输出 都打开
            conn.setDoOutput(true);
            conn.setDoInput(true);
            //开始连接
            conn.connect();

            // 传递参数  流的方式
            out=conn.getOutputStream();
            out.write(param.getBytes());
            out.flush();

            // 读取数据
            br=new BufferedReader(new InputStreamReader(conn.getInputStream(),"utf-8"));
            String line=null;
            while (null != (line=br.readLine())){
                rest.append(line);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            // 关闭所有通道
            try {
                if (br!=null) {
                    br.close();
                }
                if (out!=null) {
                    out.close();
                }
                if (conn!=null) {
                    conn.disconnect();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return rest.toString();
    }


    public static ArrayList<Cloud_rule_repositories> get_cloud(int mode) throws Exception {
        ArrayList<Cloud_rule_repositories> list=new ArrayList<>();
        String path=null;
        if(mode==1){
            path = url+"notify/get_cloud.php";
        }
        else if(mode==2){
            path = url+"notify/get_cloud_over.php";
        }
        else if(mode==3){
            path = url+"notify/get_cloud_new.php";
        }
        System.out.println(path);
        HttpURLConnection conn = (HttpURLConnection) new URL(path).openConnection();
        InputStream json = conn.getInputStream();
        byte[] data = StreamTool.read(json);
        String json_str = new String(data);
        JSONArray jsonArray = new JSONArray(json_str);
        for(int i = 0; i < jsonArray.length() ; i++){
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            String tel=jsonObject.getString("tel");
            String storage_name=jsonObject.getString("storage_name");
            String description=jsonObject.getString("description");
            int fork=jsonObject.getInt("fork");
            String datetime=jsonObject.getString("datetime");
            list.add(new Cloud_rule_repositories(tel,storage_name,description,fork,datetime));
        }
        return list;
    }

    public static ArrayList<Cloud_rule_repositories> get_my_cloud(String c_tel,int mode) throws Exception {
        ArrayList<Cloud_rule_repositories> list=new ArrayList<>();
        String path=null;
        if(mode==1){
            path = url+"notify/get_my_cloud.php?tel="+c_tel;
        }
        else if(mode==2){
            path = url+"notify/get_my_cloud_over.php?tel="+c_tel;
        }
        System.out.println(path);
        HttpURLConnection conn = (HttpURLConnection) new URL(path).openConnection();
        InputStream json = conn.getInputStream();
        byte[] data = StreamTool.read(json);
        String json_str = new String(data);
        JSONArray jsonArray = new JSONArray(json_str);
        for(int i = 0; i < jsonArray.length() ; i++){
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            String tel=jsonObject.getString("tel");
            String storage_name=jsonObject.getString("storage_name");
            String description=jsonObject.getString("description");
            int fork=jsonObject.getInt("fork");
            String datetime=jsonObject.getString("datetime");
            list.add(new Cloud_rule_repositories(tel,storage_name,description,fork,datetime));
        }
        return list;
    }

    public static User get_user(String c_tel) throws Exception{
        ArrayList<Cloud_rule_repositories> list=new ArrayList<>();
        String path=url+"notify/get_user.php?tel="+c_tel;
        System.out.println(path);
        HttpURLConnection conn = (HttpURLConnection) new URL(path).openConnection();
        InputStream json = conn.getInputStream();
        byte[] data = StreamTool.read(json);
        String json_str = new String(data);
        JSONArray jsonArray = new JSONArray(json_str);
        for(int i = 0; i < jsonArray.length() ; i++){
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            String tel=jsonObject.getString("tel");
            String name=jsonObject.getString("name");
            String gender=jsonObject.getString("gender");
            return new User(tel,name,gender);
        }
        return null;
    }

    public static ArrayList<Rule> get_cloud_rule(String c_tel,String c_storage_name) throws Exception {
        ArrayList<Rule> list=new ArrayList<>();
        String path = url+"notify/get_cloud_rule.php?tel="+c_tel+"&storage_name="+c_storage_name;
        System.out.println(path);
        HttpURLConnection conn = (HttpURLConnection) new URL(path).openConnection();
        InputStream json = conn.getInputStream();
        byte[] data = StreamTool.read(json);
        String json_str = new String(data);
        JSONArray jsonArray = new JSONArray(json_str);
        for(int i = 0; i < jsonArray.length() ; i++){
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            int _id=jsonObject.getInt("_id");
            String tel=jsonObject.getString("tel");
            String storage_name=jsonObject.getString("storage_name");
            String packagename=jsonObject.getString("packagename");
            String keyword=jsonObject.getString("keyword");
            int mode=jsonObject.getInt("mode");
            int action=jsonObject.getInt("action_name");
            int sound=jsonObject.getInt("sound");
            int vibrate=jsonObject.getInt("vibrate");
            int match=jsonObject.getInt("match_name");
            list.add(new Rule(_id,tel,storage_name,packagename,keyword,mode,action,sound,vibrate,match));
        }
        return list;
    }

    public static ArrayList<Collection> get_collection(String c_tel,int mode) throws Exception {
        ArrayList<Collection> list=new ArrayList<>();
        String path=null;
        if(mode==1){
            path = url+"notify/get_collection.php?tel="+c_tel;
        }
        else if(mode==2){
            path = url+"notify/get_collection_over.php?tel="+c_tel;
        }
        System.out.println(path);
        HttpURLConnection conn = (HttpURLConnection) new URL(path).openConnection();
        InputStream json = conn.getInputStream();
        byte[] data = StreamTool.read(json);
        String json_str = new String(data);
        JSONArray jsonArray = new JSONArray(json_str);
        for(int i = 0; i < jsonArray.length() ; i++){
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            String tel=jsonObject.getString("tel");
            String name=jsonObject.getString("name");
            String title=jsonObject.getString("title");
            String text=jsonObject.getString("text");
            String image=jsonObject.getString("image");
            String datetime=jsonObject.getString("datetime");
            list.add(new Collection(tel,name,title,text,image,datetime));
        }
        return list;
    }

//    public static user_information get_user(String school_id,String school) throws Exception{
//        String path = "https://www.recycle11.top/cloud_classroom/get_user.php?school_id="+school_id+"&school="+school;
//        System.out.println(path);
//        HttpURLConnection conn = (HttpURLConnection) new URL(path).openConnection();
//        conn.setConnectTimeout(5000);
//        conn.setRequestMethod("GET");
//        if(conn.getResponseCode() == 200){
//            InputStream json = conn.getInputStream();
//            byte[] data = StreamTool.read(json);
//            String json_str = new String(data);
//            JSONArray jsonArray = new JSONArray(json_str);
//            JSONObject jsonObject = jsonArray.getJSONObject(0);
//            String user_id=jsonObject.getString("user_id");
//            String user_name=jsonObject.getString("user_name");
//            String identity=jsonObject.getString("identity");
//            String gender=jsonObject.getString("gender");
//            return new user_information(user_id,user_name,identity,gender,school_id,school);
//        }
//        return null;
//    }

//    public static String get_user_used(String tel) throws Exception{
//        String path = url+"notify/get_user_used.php?tel="+tel;
//        System.out.println(path);
//        HttpURLConnection conn = (HttpURLConnection) new URL(path).openConnection();
//        conn.setConnectTimeout(5000);
//        conn.setRequestMethod("GET");
//        if(conn.getResponseCode() == 200){
//            InputStream json = conn.getInputStream();
//            byte[] data = StreamTool.read(json);
//            String json_str = new String(data);
//            return json_str;
//        }
//        return null;
//    }

    public static String get_rule_repositories_used(String tel,String storage_name) throws Exception{
        String path = url+"notify/get_rule_repositories_used.php?tel="+tel;
        System.out.println(path);
        HttpURLConnection conn = (HttpURLConnection) new URL(path).openConnection();
        conn.setConnectTimeout(5000);
        conn.setRequestMethod("GET");
        if(conn.getResponseCode() == 200){
            InputStream json = conn.getInputStream();
            byte[] data = StreamTool.read(json);
            String json_str = new String(data);
            return json_str;
        }
        return null;
    }



//    public static int get_num(String class_id) throws Exception{
//        String path = "https://www.recycle11.top/cloud_classroom/get_num.php?class_id="+class_id;
//        HttpURLConnection conn = (HttpURLConnection) new URL(path).openConnection();
//        conn.setConnectTimeout(5000);
//        conn.setRequestMethod("GET");
//        if(conn.getResponseCode() == 200){
//            InputStream json = conn.getInputStream();
//            byte[] data = StreamTool.read(json);
//            String json_str = new String(data);
//            JSONArray jsonArray = new JSONArray(json_str);
//            JSONObject jsonObject = jsonArray.getJSONObject(0);
//            int num=jsonObject.getInt("count(*)");
//            return num;
//        }
//        return 0;
//    }
//
//    public static long get_time(String user_id) throws Exception{
//        String path = "https://www.recycle11.top/cloud_classroom/get_time.php?user_id="+user_id;
//        HttpURLConnection conn = (HttpURLConnection) new URL(path).openConnection();
//        conn.setConnectTimeout(5000);
//        conn.setRequestMethod("GET");
//        long time=0;
//        if(conn.getResponseCode() == 200){
//            InputStream json = conn.getInputStream();
//            byte[] data = StreamTool.read(json);
//            String json_str = new String(data);
//            JSONArray jsonArray = new JSONArray(json_str);
//            for(int i = 0; i < jsonArray.length() ; i++){
//                JSONObject jsonObject = jsonArray.getJSONObject(i);
//                time+=jsonObject.getLong("time");
//            }
//            return time;
//        }
//        return 0;
//    }
//
//    public static ArrayList<String> get_ad() throws Exception{
//        ArrayList<String> list = new ArrayList<>();
//        String path = "https://www.recycle11.top/cloud_classroom/get_ad.php";
//        HttpURLConnection conn = (HttpURLConnection) new URL(path).openConnection();
//        conn.setConnectTimeout(5000);
//        conn.setRequestMethod("GET");
//        if(conn.getResponseCode() == 200){
//            InputStream json = conn.getInputStream();
//            byte[] data = StreamTool.read(json);
//            String json_str = new String(data);
//            JSONArray jsonArray = new JSONArray(json_str);
//            for(int i = 0; i < jsonArray.length() ; i++){
//                JSONObject jsonObject = jsonArray.getJSONObject(i);
//                String class_id=jsonObject.getString("class_id");
//                list.add(class_id);
//            }
//            return list;
//        }
//        return null;
//    }
//
//    public static ArrayList<notify_information> get_notify(String user_id) throws Exception{
//        ArrayList<notify_information> list = new ArrayList<>();
//        String path = "https://www.recycle11.top/cloud_classroom/get_notify.php?user_id="+user_id;
//        HttpURLConnection conn = (HttpURLConnection) new URL(path).openConnection();
//        conn.setConnectTimeout(5000);
//        conn.setRequestMethod("GET");
//        if(conn.getResponseCode() == 200){
//            InputStream json = conn.getInputStream();
//            byte[] data = StreamTool.read(json);
//            String json_str = new String(data);
//            JSONArray jsonArray = new JSONArray(json_str);
//            for(int i = 0; i < jsonArray.length() ; i++){
//                JSONObject jsonObject = jsonArray.getJSONObject(i);
//                String user_name=jsonObject.getString("user_name");
//                String time=jsonObject.getString("time");
//                String title=jsonObject.getString("title");
//                String text=jsonObject.getString("text");
//                list.add(new notify_information(user_name,time,title,text));
//            }
//            return list;
//        }
//        return null;
//    }
//
//    public static ArrayList<sign_information> get_sign(String user_id) throws Exception{
//        ArrayList<sign_information> list = new ArrayList<>();
//        String path = "https://www.recycle11.top/cloud_classroom/get_sign.php?user_id="+user_id;
//        HttpURLConnection conn = (HttpURLConnection) new URL(path).openConnection();
//        conn.setConnectTimeout(5000);
//        conn.setRequestMethod("GET");
//        if(conn.getResponseCode() == 200){
//            InputStream json = conn.getInputStream();
//            byte[] data = StreamTool.read(json);
//            String json_str = new String(data);
//            JSONArray jsonArray = new JSONArray(json_str);
//            for(int i = 0; i < jsonArray.length() ; i++){
//                JSONObject jsonObject = jsonArray.getJSONObject(i);
//                String class_id=jsonObject.getString("class_id");
//                String class_name=jsonObject.getString("class_name");
//                String time=jsonObject.getString("sign_time");
//                String duration=jsonObject.getString("duration");
//                String people=jsonObject.getString("people");
//                list.add(new sign_information(class_id,class_name,time,duration,people));
//            }
//            return list;
//        }
//        return null;
//    }
//
//    public static String login(String school,String school_id,String password) throws Exception{
//        String path = "https://www.recycle11.top/cloud_classroom/login.php?school_id="+school_id+"&password="+password+"&school="+school;
//        HttpURLConnection conn = (HttpURLConnection) new URL(path).openConnection();
//        conn.setConnectTimeout(5000);
//        conn.setRequestMethod("GET");
//        if(conn.getResponseCode() == 200){
//            InputStream json = conn.getInputStream();
//            byte[] data = StreamTool.read(json);
//            String json_str = new String(data);
//            System.out.println(json_str);
//            return json_str;
//        }
//        return null;
//    }
//
//    public static ArrayList<leave_information> get_leave(String user_id,int identity) throws Exception{
//        ArrayList<leave_information> list = new ArrayList<>();
//        String path="";
//        if(identity==0){
//            path = "https://www.recycle11.top/cloud_classroom/get_leave.php?user_id="+user_id;
//        }
//        else{
//            path = "https://www.recycle11.top/cloud_classroom/get_teacher_leave.php?teacher_id="+user_id;
//        }
//        System.out.println(path);
//        HttpURLConnection conn = (HttpURLConnection) new URL(path).openConnection();
//        conn.setConnectTimeout(5000);
//        conn.setRequestMethod("GET");
//        if(conn.getResponseCode() == 200){
//            InputStream json = conn.getInputStream();
//            byte[] data = StreamTool.read(json);
//            String json_str = new String(data);
//            JSONArray jsonArray = new JSONArray(json_str);
//            for(int i = 0; i < jsonArray.length() ; i++){
//                JSONObject jsonObject = jsonArray.getJSONObject(i);
//                String leave_id=jsonObject.getString("leave_id");
//                String student_name=jsonObject.getString("user_name");
//                String type=jsonObject.getString("type");
//                String start_time=jsonObject.getString("start_time");
//                String end_time=jsonObject.getString("end_time");
//                String need=jsonObject.getString("need");
//                String reason=jsonObject.getString("reason");
//                String photo=jsonObject.getString("photo");
//                String teacher_id=jsonObject.getString("teacher_id");
//                int status=jsonObject.getInt("status");
//                list.add(new leave_information(leave_id,student_name,type,start_time,end_time,need,reason,photo,teacher_id,status));
//            }
//            return list;
//        }
//        return null;
//    }
//
//    public static int get_sc_sum(String class_id) throws Exception{
//        String path = "https://www.recycle11.top/cloud_classroom/get_sc_sum.php?class_id="+class_id;
//        HttpURLConnection conn = (HttpURLConnection) new URL(path).openConnection();
//        conn.setConnectTimeout(5000);
//        conn.setRequestMethod("GET");
//        if(conn.getResponseCode() == 200){
//            InputStream json = conn.getInputStream();
//            byte[] data = StreamTool.read(json);
//            String json_str = new String(data);
//            JSONArray jsonArray = new JSONArray(json_str);
//            JSONObject jsonObject = jsonArray.getJSONObject(0);
//            int num=jsonObject.getInt("count(*)");
//            return num;
//        }
//        return 0;
//    }
//
//    public static ArrayList<user_information> get_teacher(String user_id) throws Exception{
//        ArrayList<user_information> list = new ArrayList<>();
//        String path = "https://www.recycle11.top/cloud_classroom/get_teacher.php?user_id="+user_id;
//        HttpURLConnection conn = (HttpURLConnection) new URL(path).openConnection();
//        conn.setConnectTimeout(5000);
//        conn.setRequestMethod("GET");
//        if(conn.getResponseCode() == 200){
//            InputStream json = conn.getInputStream();
//            byte[] data = StreamTool.read(json);
//            String json_str = new String(data);
//            JSONArray jsonArray = new JSONArray(json_str);
//            for(int i = 0; i < jsonArray.length() ; i++){
//                JSONObject jsonObject = jsonArray.getJSONObject(i);
//                String teacher_id=jsonObject.getString("user_id");
//                String teacher_name=jsonObject.getString("user_name");
//                list.add(new user_information(teacher_id,teacher_name));
//            }
//            return list;
//        }
//        return null;
//    }
//
//    public static ArrayList<chat_information> get_chat(String class_id,int num) throws Exception{
//        ArrayList<chat_information> list = new ArrayList<>();
//        String path = "https://www.recycle11.top/cloud_classroom/get_chat.php?class_id="+class_id+"&num="+num;
//        HttpURLConnection conn = (HttpURLConnection) new URL(path).openConnection();
//        conn.setConnectTimeout(5000);
//        conn.setRequestMethod("GET");
//        if(conn.getResponseCode() == 200){
//            InputStream json = conn.getInputStream();
//            byte[] data = StreamTool.read(json);
//            String json_str = new String(data);
//            JSONArray jsonArray = new JSONArray(json_str);
//            for(int i = 0; i < jsonArray.length() ; i++){
//                JSONObject jsonObject = jsonArray.getJSONObject(i);
//                String name=jsonObject.getString("user_name");
//                String text=jsonObject.getString("text");
//                list.add(new chat_information(name,text));
//            }
//            return list;
//        }
//        return null;
//    }
//
//    public static ArrayList<class_information> get_sc(String user_id) throws Exception{
//        ArrayList<class_information> list=new ArrayList<>();
//        String path = "https://www.recycle11.top/cloud_classroom/get_sc.php?user_id="+user_id;
//        HttpURLConnection conn = (HttpURLConnection) new URL(path).openConnection();
//        conn.setConnectTimeout(5000);
//        conn.setRequestMethod("GET");
//        if(conn.getResponseCode() == 200){
//            InputStream json = conn.getInputStream();
//            byte[] data = StreamTool.read(json);
//            String json_str = new String(data);
//            JSONArray jsonArray = new JSONArray(json_str);
//            for(int i = 0; i < jsonArray.length() ; i++){
//                JSONObject jsonObject = jsonArray.getJSONObject(i);
//                String class_id=jsonObject.getString("class_id");
//                String class_name=jsonObject.getString("class_name");
//                String school=jsonObject.getString("school");
//                String picture=jsonObject.getString("picture");
//                list.add(new class_information(class_id,class_name,school,picture));
//            }
//            return list;
//        }
//        return null;
//    }

//    private static ArrayList<class_information> parseJSON(InputStream jsonStream) throws Exception{
//        ArrayList<class_information> list = new ArrayList<>();
//        byte[] data = StreamTool.read(jsonStream);
//        String json = new String(data);
//        JSONArray jsonArray = new JSONArray(json);
//        for(int i = 0; i < jsonArray.length() ; i++){
//            JSONObject jsonObject = jsonArray.getJSONObject(i);
//            String class_id=jsonObject.getString("class_id");
//            String class_name=jsonObject.getString("class_name");
//            String school=jsonObject.getString("school");
//            String picture=jsonObject.getString("picture");
//            list.add(new class_information(class_id,class_name,school,picture));
//        }
//        return list;
//    }

    public static class StreamTool {
        //从流中读取数据
        public static byte[] read(InputStream inStream) throws Exception{
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            while((len = inStream.read(buffer)) != -1)
            {
                outStream.write(buffer,0,len);
            }
            inStream.close();
            return outStream.toByteArray();
        }
    }

}
