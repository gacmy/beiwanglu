package com.gac.beiwanglu.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.gac.beiwanglu.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * Created by Administrator on 2016/1/4.
 */
public class Utils {

    /*
    *
    * <color name="green_pre">#D4D4</color>
    <color name="green">#093</color>
    <color name="gray">#d4d4d4</color>
    <color name="blue">#7db2c2</color>
    <color name="white">#ffff</color>
    <color name="yellow">#ffaa00</color>
    * */

    public static int COLOR_GREEN = 0;
    public static int COLOR_GRAY = 1;
    public static int COLOR_BLUE = 2;
    public static int COLOR_YELLOWA = 3;
    public static int COLOR_PIPLE = 4;

    public static int LOCK = 1;
    public static int UNLOCK = 0;
    public static int[] colorIds={R.color.green_pre,R.color.gray,R.color.blue,R.color.yellow,R.color.piple};

    public static String STATE="state";
    public static String STATE_DATA = "notes";
    public static String STATE_EDIT="edit";//编辑状态
    public static String STATE_NEW = "new";//新建备忘录状态
    public static String STATE_OPENNOTE="opennote";//密码界面 解锁成功打开编辑界面
    public static String STATE_SETPWD = "setpwd";//设置密码的状态
    public static String STATE_UNLOCK="unlock";//密码界面 取消加锁功能
    //属性名称
    public static String FIELD_NAME_IDS ="ids";
    public static String FIELD_NAME_CONTENT="content";
    public static String FIELD_NAME_COLOR="color";
    public static String FIELD_NAME_LOCK="lock";
    public static String FIELD_NAME_PWD="pwd";
    public static String FIELD_NAME_DATE="date";
    public static String FIELD_NAME_VIEWTYPE="viewType";
    public static View getView(Context context,int layoutId){
        LayoutInflater inflter =  LayoutInflater.from(context);
        return inflter.inflate(layoutId,null);
    }

    public static void loseFocus(EditText et){
        et.setFocusable(false);
    }
    public static void getFocus(EditText editText){

        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
        editText.requestFocusFromTouch();
    }

    public static int getScreenHeight(Activity context){
        DisplayMetrics metric = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(metric);
        return metric.widthPixels;
//        int width = metric.widthPixels;     // 屏幕宽度（像素）
//        int height = metric.heightPixels;   // 屏幕高度（像素）
//        float density = metric.density;      // 屏幕密度（0.75 / 1.0 / 1.5）
//        int densityDpi = metric.densityDpi;  // 屏幕密度DPI（120 / 160 / 240）
    }
    public static int getScreenWidth(Activity context){
        DisplayMetrics metric = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(metric);
        return metric.heightPixels;
//        int width = metric.widthPixels;     // 屏幕宽度（像素）
//        int height = metric.heightPixels;   // 屏幕高度（像素）
//        float density = metric.density;      // 屏幕密度（0.75 / 1.0 / 1.5）
//        int densityDpi = metric.densityDpi;  // 屏幕密度DPI（120 / 160 / 240）
    }

    public static void print(Context context,String content){
        Toast.makeText(context,content,Toast.LENGTH_SHORT).show();
    }

    public static String getDate(){
        Date d = new Date();
        System.out.println(d);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateNowStr = sdf.format(d);
        //System.out.println("格式化后的日期：" + dateNowStr);

//        String str = "2012-1-13 17:26:33";  //要跟上面sdf定义的格式一样
//        Date today = sdf.parse(str);
//        System.out.println("字符串转成日期：" + today);
        return dateNowStr;
    }

    public static  String getUid(){
        String uuid = UUID.randomUUID().toString();
        return uuid;
    }

    public static void log(String str){
        Log.e("gac",str);
    }

    public static String getPwd(Context context){
        SharedPreferences sp =context.getSharedPreferences("gac", context.MODE_PRIVATE);
       return sp.getString("pwd","");
    }
}
