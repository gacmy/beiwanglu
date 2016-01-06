package com.gac.beiwanglu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.gac.beiwanglu.bean.Notes;
import com.gac.beiwanglu.db.DbOp;
import com.gac.beiwanglu.utils.Utils;
import com.gac.beiwanglu.view.ContentListView;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/1/4.
 */
public class ContentActivity extends Activity implements View.OnClickListener,ContentListView.DelListener{
    private ContentListView lv;
    private ArrayList<Notes> mDatas;
    private TextView tv_notesCount;
    private ImageView iv_add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);//在setContentView 之前调用
        setContentView(R.layout.activity_content);
        //设置titlebar布局文件为标题栏
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.titlebar_content);  //titlebar为自己标题栏的布局
        initView();
        initEvents();

       // startActivity(new Intent(this,PasswordActvity.class));
       // initData();
    }

    private void initView(){
        lv = (ContentListView)findViewById(R.id.id_lv_content);
        tv_notesCount =(TextView)findViewById(R.id.tv_note_count);
        iv_add = (ImageView)findViewById(R.id.iv_add_notes);
    }


    @Override
    protected void onStart() {
        Utils.log("onstart");
        initData();
        super.onStart();
    }



    private void initEvents(){
        lv.setDelListener(this);
        iv_add.setOnClickListener(this);
        lv.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        mDatas = lv.getDatas();
                        Notes note = mDatas.get(position);
                        Utils.log("点击条目的内容:"+mDatas.get(position).content);
                        if(mDatas.get(position).lock == Utils.LOCK){
                            //启动密码界面的情况1 打开密码界面验证 是否可以进入 编辑界面
                            Intent intent = new Intent(ContentActivity.this, PasswordActvity.class);
                            intent.putExtra(Utils.STATE, Utils.STATE_OPENNOTE);
                            Bundle bundle = new Bundle();

                            bundle.putString(Utils.FIELD_NAME_CONTENT, note.content);
                            bundle.putString(Utils.FIELD_NAME_DATE, note.date);
                            bundle.putString(Utils.FIELD_NAME_IDS, note.ids);
                            bundle.putString(Utils.FIELD_NAME_PWD, note.pwd);
                            bundle.putInt(Utils.FIELD_NAME_COLOR, note.color);
                            bundle.putInt(Utils.FIELD_NAME_LOCK, note.lock);
                            intent.putExtra(Utils.STATE_DATA, bundle);
                            startActivity(intent);
                        }else{
                            Intent intent = new Intent(ContentActivity.this, MainActivity.class);
                            intent.putExtra(Utils.STATE, Utils.STATE_EDIT);
                            Bundle bundle = new Bundle();

                            bundle.putString(Utils.FIELD_NAME_CONTENT, note.content);
                            bundle.putString(Utils.FIELD_NAME_DATE, note.date);
                            bundle.putString(Utils.FIELD_NAME_IDS, note.ids);
                            bundle.putString(Utils.FIELD_NAME_PWD, note.pwd);
                            bundle.putInt(Utils.FIELD_NAME_COLOR, note.color);
                            bundle.putInt(Utils.FIELD_NAME_LOCK, note.lock);
                            intent.putExtra(Utils.STATE_DATA, bundle);
                            startActivity(intent);
                        }

                    }
                }
        );
    }
    private void initData(){
        mDatas = new DbOp(this).query();

        if(mDatas.size() == 0){
            TextView empty = new TextView(this);
            empty.setText("当前没有任何内容");
            lv.setEmptyView(empty);
            tv_notesCount.setText("备忘录("+0+")");
        }else{
            tv_notesCount.setText("备忘录("+mDatas.size()+")");
        }
        lv.updateData(mDatas);

    }




    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_add_notes:
                Intent intent = new Intent(this,MainActivity.class);
                intent.putExtra(Utils.STATE,Utils.STATE_NEW);
                startActivity(intent);
                break;

        }
    }

    @Override
    public void update(int count) {
        tv_notesCount.setText("备忘录("+count+")");
    }
}
