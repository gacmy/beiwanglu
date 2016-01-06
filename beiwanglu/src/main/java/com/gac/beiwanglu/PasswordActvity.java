package com.gac.beiwanglu;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.gac.beiwanglu.bean.Notes;
import com.gac.beiwanglu.utils.Utils;

/**
 * Created by Administrator on 2016/1/6.
 */
public class PasswordActvity  extends Activity implements View.OnClickListener{
    private Button bt_postive;
    private Button bt_negative;
    private EditText et_pwd;
    private String pwd="";
    private String state="";
    private Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pwd);
        initView();
        initEvent();
        init();
    }

    private void init(){
         intent = getIntent();
        if(intent.getStringExtra(Utils.STATE) == null ){
            state = "null";
            return;
        }

        if(intent.getStringExtra(Utils.STATE).equals(Utils.STATE_OPENNOTE)){//打开编辑界面的状态
            //Bundle bundle = intent.getBundleExtra(Utils.STATE_DATA);
            state = Utils.STATE_OPENNOTE;
        }else if(intent.getStringExtra(Utils.STATE).equals(Utils.STATE_SETPWD)){
            state = Utils.STATE_SETPWD;//设置密码的状态
        }else if(intent.getStringExtra(Utils.STATE).equals(Utils.STATE_UNLOCK)){
            state = Utils.STATE_UNLOCK;
        }

    }
    private void initView(){
        bt_postive = (Button)findViewById(R.id.button_positive);
        bt_negative = (Button)findViewById(R.id.button_negative);
        et_pwd = (EditText)findViewById(R.id.id_et_pwd);
    }

    private void initEvent(){
        bt_negative.setOnClickListener(this);
        bt_postive.setOnClickListener(this);
    }

    //返回键
    @Override
    public void onBackPressed() {
       finishActivity();
    }

    private void finishActivity(){
        if(state.equals(Utils.STATE_UNLOCK)){
            setResult(RESULT_CANCELED);
            finish();
        }else {
            finish();
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_negative:
              finishActivity();

                break;
            case R.id.button_positive:
                savePwd();
                break;
        }
    }

    private void savePwd(){
        if(TextUtils.isEmpty(et_pwd.getText().toString().trim())){
            Utils.print(this,"密码不能为空");
            return;
        }
        SharedPreferences sp = this.getSharedPreferences("gac",MODE_PRIVATE);
        String pwd = et_pwd.getText().toString();
        String str = sp.getString("pwd","");
        if(str.equals("")){
            sp.edit().putString("pwd",pwd).commit();
            Utils.print(this,"设置密码成功");
            finish();
        }else{
            if(str.equals(pwd)){
                Utils.print(this,"密码正确");

                if(state.equals(Utils.STATE_OPENNOTE)){//打开笔记的状态
                    Bundle bundle = intent.getBundleExtra(Utils.STATE_DATA);
                    Intent i = new Intent(this,MainActivity.class);
                    i.putExtra(Utils.STATE, Utils.STATE_EDIT);
                    i.putExtra(Utils.STATE_DATA, bundle);
                    startActivity(i);
                    finish();
                    return;
                }else if(state.equals(Utils.STATE_UNLOCK)){
                    setResult(RESULT_OK);
                    finish();
                }


            }else{
                Utils.print(this,"密码错误");
            }
        }
    }
}
