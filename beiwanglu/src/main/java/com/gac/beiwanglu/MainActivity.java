package com.gac.beiwanglu;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.animation.BounceInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.gac.beiwanglu.bean.Notes;
import com.gac.beiwanglu.db.DbOp;
import com.gac.beiwanglu.utils.Utils;
import com.gac.beiwanglu.view.CustomEditText;

import java.util.ArrayList;

public class MainActivity extends Activity implements View.OnClickListener{

    Button bt;
    EditText et1;
    private int[] resIds = {R.id.id_iv_open,R.id.id_iv_lock,R.id.id_iv_color,R.id.id_iv_delete};
    private String[] color={"绿色","灰色","蓝色","黄色","紫色"};
    private ArrayList<ImageView> ivList;
    boolean openflag = false;
    private TextView tv_save;//保存
    private CustomEditText et_content;
    private TextView tv_date;
    private TextView tv_negative;//取消
    private boolean isUpdate = false;//是新建 还是编辑数据
    private Notes notes;//保存当前笔记内容
    private boolean isLock = true;//下次点击 的状态默认是加密状态 是加密还是解密
    private ImageView iv_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);//在setContentView 之前调用
        setContentView(R.layout.activity_main);
        //设置titlebar布局文件为标题栏
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.titlebar_main);  //titlebar为自己标题栏的布局
        initView();
        initEvents();
        initData();
    }
    private void initData(){
        Intent intent = getIntent();
        notes = new Notes();
       if(intent.getStringExtra(Utils.STATE).equals(Utils.STATE_NEW)){
           tv_date.setText(Utils.getDate());
           notes.ids="";
           //初始化界面数据
           notes.date = tv_date.getText().toString();//日期显示
           et_content.setBackgroundResource(Utils.colorIds[notes.color]);//备忘录颜色
           isUpdate = false;//新建状态
       }else if(intent.getStringExtra(Utils.STATE).equals(Utils.STATE_EDIT)){
           isUpdate = true;//编辑状态
           Bundle bundle = intent.getBundleExtra(Utils.STATE_DATA);

           notes.date = bundle.getString(Utils.FIELD_NAME_DATE);
           notes.color = bundle.getInt(Utils.FIELD_NAME_COLOR);
           notes.pwd = bundle.getString(Utils.FIELD_NAME_PWD);
           notes.lock = bundle.getInt(Utils.FIELD_NAME_LOCK);
           notes.ids = bundle.getString(Utils.FIELD_NAME_IDS);
           notes.content = bundle.getString(Utils.FIELD_NAME_CONTENT);
           //初始化界面数据
           tv_date.setText(bundle.getString(Utils.FIELD_NAME_DATE));
           et_content.setText(bundle.getString(Utils.FIELD_NAME_CONTENT));
           et_content.setBackgroundResource(Utils.colorIds[notes.color]);
           if(notes.lock == Utils.LOCK){
               ivList.get(1).setImageResource(R.drawable.ic_unlock);
               isLock = false;//下次点击就是解密
           }else{
               ivList.get(1).setImageResource(R.drawable.ic_lock);
               isLock = true;//下次点击就是加密
           }


       }
    }
    private void initView(){

        tv_save = (TextView)findViewById(R.id.id_tv_save);
        et_content = (CustomEditText)findViewById(R.id.note);
        tv_date = (TextView)findViewById(R.id.id_tv_date);
        iv_back = (ImageView)findViewById(R.id.id_iv_back);
        tv_negative = (TextView)findViewById(R.id.id_tv_negative);
        init();
    }

    private void initEvents(){
        tv_save.setOnClickListener(this);
        tv_negative.setOnClickListener(this);
        iv_back.setOnClickListener(this);
    }

    private void init(){
        ivList = new ArrayList<ImageView>();
        for(int i = 0; i < resIds.length;i++){
            ImageView imageView = (ImageView)findViewById(resIds[i]);
            if(i != 0){
                imageView.setVisibility(View.GONE);
            }

            imageView.setOnClickListener(this);
            ivList.add(imageView);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void startAnimation(){
        int width = Utils.getScreenWidth(this);
        for(int i = 1;i < resIds.length;i++){
            final int cur = i;
            ObjectAnimator animator = ObjectAnimator.ofFloat(ivList.get(i),"Rotation",0,360);
            BounceInterpolator b = new BounceInterpolator();
            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animation) {
                    ivList.get(cur).setVisibility(View.VISIBLE);
                }
            });
            animator.setStartDelay(i * 200);
            animator.setInterpolator(b);
            animator.setDuration(100);

            animator.start();
        }

    }
    private void closeAnimaion(){

        for( int i = resIds.length-1; i >= 1; i--){
            final int cur = i;
            ObjectAnimator animator = ObjectAnimator.ofFloat(ivList.get(i),"Rotation",0,360);
            BounceInterpolator b = new BounceInterpolator();
            animator.setInterpolator(b);
            animator.setDuration(100);
            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    ivList.get(cur).setVisibility(View.GONE);
                }
            });
            animator.setStartDelay(i * 200);
            animator.start();
        }
    }
    //数据库中增加一条数据
    private void save(){

        if(TextUtils.isEmpty(et_content.getText().toString().trim())){
            Utils.print(this,"保存内容不能为空");
            return;
        }
        Utils.log("saveColor:"+notes.color);

        notes.pwd="";
        notes.ids=Utils.getUid();
        notes.content = et_content.getText().toString();


        new DbOp(this).insert(notes);
        finish();
    }
    //数据库中更新数据状态
    private void edit(){
        if(TextUtils.isEmpty(et_content.getText().toString().trim())){
            Utils.print(this,"保存内容不能为空");
            return;
        }



        notes.content = et_content.getText().toString();

        new DbOp(this).update(notes);
        finish();
    }


    private void colorDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("选择背景")
                .setItems(color, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // The 'which' argument contains the index position
                        // of the selected item
                        Utils.print(MainActivity.this, "which:" + which);
                        et_content.setBackgroundResource(Utils.colorIds[which]);
                        notes.color = which;
                    }
                });
        AlertDialog dialog= builder.create();
        dialog.show();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.id_iv_open:
                if(openflag){
                    ivList.get(0).setImageResource(R.drawable.ic_open);
                    closeAnimaion();
                    openflag = false;
                }else{
                    ivList.get(0).setImageResource(R.drawable.ic_close);
                     startAnimation();
                     openflag = true;

                }
                break;
            case R.id.id_tv_save:
                if(isUpdate){
                    edit();
                }else{
                    save();
                }


                break;
            case R.id.id_tv_negative:
                finish();
                break;
            case R.id.id_iv_color://改变背景颜色
                colorDialog();
                break;
            case R.id.id_iv_lock://加密功能实现 解密功能实现
                if(isLock){
                   lock();//加密
                }else{
                    unlock();//解密
                }
                break;
            case R.id.id_iv_delete://删除功能实现
                delete();
                break;
            case R.id.id_iv_back:
                finish();
                break;

        }
    }

    //删除功能的实现
    public void delete(){
        if(notes.ids.equals("")){
            finish();
        }else{
            new DbOp(this).delete(notes);
            finish();
        }

    }
    //加密功能的实现
    private void lock(){
       if(Utils.getPwd(this).equals("")){
           //设置密码
           Intent i = new Intent(this,PasswordActvity.class);
           i.putExtra(Utils.STATE,Utils.STATE_SETPWD);
           startActivity(i);

       }else{
           isLock = false;//下次状态就是解密状态
           ivList.get(1).setImageResource(R.drawable.ic_unlock);
           notes.lock = 1;
           new DbOp(this).updatePwd(notes);

       }
    }

    //解密功能的实现
    private void unlock(){
        Intent i = new Intent(this,PasswordActvity.class);
        i.putExtra(Utils.STATE,Utils.STATE_UNLOCK);
        startActivityForResult(i, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){
            if(resultCode == RESULT_OK){
                //解密成功
                notes.lock = 0;
                new DbOp(this).updatePwd(notes);
                ivList.get(1).setImageResource(R.drawable.ic_lock);
                isLock = true;//下次就是 加密状态
            }else {

            }
        }
    }
}
