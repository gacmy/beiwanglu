package com.gac.beiwanglu.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.gac.beiwanglu.MainActivity;
import com.gac.beiwanglu.PasswordActvity;
import com.gac.beiwanglu.R;
import com.gac.beiwanglu.bean.Notes;
import com.gac.beiwanglu.db.DbOp;
import com.gac.beiwanglu.utils.Utils;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/1/4.
 */
public class ContentListView extends ListView {
    private ArrayList<Notes> mDatas = new ArrayList<Notes>();
    private Context mContext;
    private ContentAdapter mAdapter;
    private static int LOCK_VIEW = 0;
    private static int UNLOCK_VIEW =  1;
    private int curIndex = 0;//当前选中的条目
    private DelListener listener;
    //监听删除条目时候 数量的变化接口
    public interface DelListener{
       public void update(int count);
    }
    //menu

    public void setDelListener(DelListener delListener){
        this.listener = delListener;
    }
    private String[] color={"绿色","灰色","蓝色","黄色","紫色"};

    private int[] resIds = {R.id.id_iv_open,R.id.id_iv_edit,R.id.id_iv_lock,R.id.id_iv_color,R.id.id_iv_delete};
    public ContentListView(Context context) {
       this(context, null);
    }
    public ContentListView(Context context,AttributeSet attrs){
        super(context,attrs);
        this.mContext = context;
        mAdapter = new ContentAdapter();
        this.setAdapter(mAdapter);
        setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);
    }


    /*
    *
    * */

    public void updateData(ArrayList<Notes> datas){
        this.mDatas = datas;
        mAdapter.notifyDataSetChanged();
    }

    public ArrayList<Notes> getDatas(){
        return  mDatas;
    }
    //adapter
    private class ContentAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return mDatas.size();
        }

        @Override
        public Object getItem(int position) {
            return mDatas.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getItemViewType(int position) {
            return mDatas.get(position).lock;
        }

        @Override
        public int getViewTypeCount() {
            return 2;
        }

        @Override
        public View getView(final int position, View convertView, final ViewGroup parent) {
            LockViewHolder holder = null;
            UnlockViewHolder unLockHolder= null;
            Utils.log("getView position:"+position);
            if(getItemViewType(position) == Utils.UNLOCK){

                if(convertView == null){
                    unLockHolder = new UnlockViewHolder();
                    convertView = Utils.getView(mContext,R.layout.content_item);
                    unLockHolder.et_content =(TextView)convertView.findViewById(R.id.id_et_unlock);
                    unLockHolder.tv_unlockDate = (TextView)convertView.findViewById(R.id.id_tv_date_unlock);
                    unLockHolder.position = position;
                    init(unLockHolder,convertView);
                    convertView.setTag(unLockHolder);
                }else{
                    unLockHolder = (UnlockViewHolder)convertView.getTag();
                }

                    int len = mDatas.get(position).content.length();
                    unLockHolder.et_content.setText(mDatas.get(position).content.subSequence(0, Math.min(len, 30)));
                    unLockHolder.tv_unlockDate.setText(mDatas.get(position).date);
                    Utils.log("color:" + mDatas.get(position).color);
                    unLockHolder.et_content.setBackgroundResource(Utils.colorIds[mDatas.get(position).color]);
                    unLockHolder.position = position;
                   initMenu(unLockHolder);//每次更新视图的时候把菜单更新到关闭状态



            }else{


                if(convertView == null){
                    convertView = Utils.getView(mContext, R.layout.content_item_lock);
                    holder =new LockViewHolder();
                    holder.tv_date = (TextView)convertView.findViewById(R.id.tv_item_date);
                    //当前条目是edittext可用
                    convertView.setTag(holder);
                }else{
                    holder = (LockViewHolder)convertView.getTag();
                }

                holder.tv_date.setText(mDatas.get(position).date);
                holder.tv_date.setBackgroundResource(Utils.colorIds[mDatas.get(position).color]);

            }

            return convertView;
        }
        private class LockViewHolder{
            public ImageView iv;
            public TextView tv_date;
        }

        private class UnlockViewHolder{
            public  int position;
            public ArrayList<ImageView> ivList;
            public int[] resIds = {R.id.id_iv_open,R.id.id_iv_edit,R.id.id_iv_lock,R.id.id_iv_color,R.id.id_iv_delete};
            public TextView tv_unlockDate;
            public TextView et_content;


        }


        private void init(final UnlockViewHolder holder,View view){
            holder.ivList = new ArrayList<ImageView>();

            for(int i = 0; i < resIds.length;i++){
                ImageView imageView = (ImageView)view.findViewById(resIds[i]);
                if(i != 0){
                    imageView.setVisibility(View.GONE);
                }

                imageView.setOnClickListener(new View.OnClickListener(){

                    @Override
                    public void onClick(View v) {
                        switch (v.getId()) {
                            case R.id.id_iv_open:
                                if (mDatas.get(holder.position).openflag) {
                                    holder.ivList.get(0).setImageResource(R.drawable.ic_open);
                                    closeAnimaion(holder);
                                    //holder.openflag = false;
                                    mDatas.get(holder.position).openflag = false;//将内容里面的数据做改变 菜单打开false
                                } else {
                                    holder.ivList.get(0).setImageResource(R.drawable.ic_close);
                                    startAnimation(holder);
                                    //holder.openflag = true;
                                    mDatas.get(holder.position).openflag =true;

                                }
                                break;

                            case R.id.id_iv_color:
                                mDatas.get(holder.position).openflag = false;
                                colorDialog(holder.position);
                                break;
                            case R.id.id_iv_lock:
                                mDatas.get(holder.position).openflag = false;
                                Utils.log("click position:" + holder.position);
                                if(Utils.getPwd(mContext).equals("")){
                                    mContext.startActivity(new Intent(mContext, PasswordActvity.class));
                                }else{

                                    mDatas .get(holder.position).lock = 1;
                                    Utils.log("加密时的内容:"+mDatas.get(holder.position).content);
                                    new DbOp(mContext).updatePwd(mDatas.get(holder.position));
                                    notifyDataSetChanged();
                                }

                                break;
                            case R.id.id_iv_delete:
                                mDatas.get(holder.position).openflag = false;
                                new DbOp(mContext).delete(mDatas.get(holder.position));
                                mDatas.remove(holder.position);
                                listener.update(mDatas.size());
                                notifyDataSetChanged();
                               initMenu(holder);//由于view的复用所以需要初始化菜单 否则 菜单处于打开状态
                                break;
                            case R.id.id_iv_edit:
                                mDatas.get(holder.position).openflag = false;
                                Intent intent = new Intent(mContext, MainActivity.class);
                                intent.putExtra(Utils.STATE, Utils.STATE_EDIT);
                                Bundle bundle = new Bundle();
                                Utils.log("点击编辑的位置:"+holder.position);
                                Utils.log("点击编辑的内容:"+mDatas.get(holder.position).content);
                                bundle.putString(Utils.FIELD_NAME_CONTENT, mDatas.get(holder.position).content);
                                bundle.putString(Utils.FIELD_NAME_DATE, mDatas.get(holder.position).date);
                                bundle.putString(Utils.FIELD_NAME_IDS, mDatas.get(holder.position).ids);
                                bundle.putString(Utils.FIELD_NAME_PWD, mDatas.get(holder.position).pwd);
                                bundle.putInt(Utils.FIELD_NAME_COLOR, mDatas.get(holder.position).color);
                                bundle.putInt(Utils.FIELD_NAME_LOCK, mDatas.get(holder.position).lock);
                                intent.putExtra(Utils.STATE_DATA, bundle);
                                initMenu(holder);//由于view的复用所以需要初始化菜单 否则 菜单处于打开状态
                                mContext.startActivity(intent);
                                break;
                        }
                    }
                });
                holder.ivList.add(imageView);

            }
            //holder里的ivList 复用会产生错误

        }

        private void initMenu(UnlockViewHolder holder){
            for(int i = 1; i <holder.ivList.size();i++) {
                holder.ivList.get(i).setVisibility(View.GONE);
                holder.ivList.get(0).setImageResource(R.drawable.ic_open);
              // mDatas.get(holder.position).openflag = false;//下一次就是菜单打开状态
            }
        }
        private void startAnimation(final UnlockViewHolder holder){

            for(int i = 1;i < resIds.length;i++){
                final int cur = i;
                ObjectAnimator animator = ObjectAnimator.ofFloat(holder.ivList.get(i),"Rotation",0,360);
                BounceInterpolator b = new BounceInterpolator();
                animator.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        holder.ivList.get(cur).setVisibility(View.VISIBLE);
                    }
                });
                animator.setStartDelay(i * 200);
                animator.setInterpolator(b);
                animator.setDuration(100);

                animator.start();
            }

        }

        private void colorDialog( final int position){
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setTitle("选择背景")
                    .setItems(color, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            mDatas.get(position).color = which;
                            new DbOp(mContext).update(mDatas.get(position));
                            notifyDataSetChanged();
                        }
                    });
            AlertDialog dialog= builder.create();
            dialog.show();
        }
        private void closeAnimaion(final UnlockViewHolder holder){

            for( int i = resIds.length-1; i >= 1; i--){
                final int cur = i;
                ObjectAnimator animator = ObjectAnimator.ofFloat(holder.ivList.get(i),"Rotation",0,360);
                BounceInterpolator b = new BounceInterpolator();
                animator.setInterpolator(b);
                animator.setDuration(100);
                animator.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        holder.ivList.get(cur).setVisibility(View.GONE);
                    }
                });
                animator.setStartDelay(i * 200);
                animator.start();
            }
        }
    }
}
