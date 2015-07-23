package org.zywx.wbpalmstar.plugin.uexsidemenu;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Transformation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.ScrollView;

import org.zywx.wbpalmstar.base.BUtility;
import org.zywx.wbpalmstar.engine.universalex.EUExUtil;
import org.zywx.wbpalmstar.plugin.uexsidemenu.EUExSideMenu.CallbackListener;
import org.zywx.wbpalmstar.plugin.uexsidemenu.vo.MenuDataVO;
import org.zywx.wbpalmstar.plugin.uexsidemenu.vo.MenuItemDataVO;

import java.util.ArrayList;
import java.util.List;

public class SideMenu implements View.OnClickListener {
    
    private Context mContext;
    private ScrollView mSideMenu;
    private RelativeLayout mRelativeLayout;
    private LinearLayout mLinearLayout;
    private List<SideMenuItem> dataList=new ArrayList<SideMenuItem>();
    private ArrayList<RelativeLayout> itemsList=new ArrayList<RelativeLayout>();
    private int itemCount;
    private boolean isInit;
    public boolean isOpen=false;
    private LayoutInflater lf;
    private int layoutId=0;
    private int width=0;
    private CallbackListener mCallBack;
    private int mWidth;
    private int mHeight;
    
    public SideMenu(Context ctx, MenuDataVO data, CallbackListener callback){
        this.mContext=ctx;
        this.mCallBack=callback;
        this.dataList = getDataList(data);
        if (data.getWidth() < 0 || data.getHeight() < 0){
            this.mWidth = dataList.get(0).bgimg.getIntrinsicWidth();
            this.mHeight = dataList.get(0).bgimg.getIntrinsicHeight();
        }else{
            this.mWidth = data.getWidth();
            this.mHeight = data.getHeight();
        }

    }

    private List<SideMenuItem> getDataList(MenuDataVO data) {
        int density = BUtility.getDeviceDesity((Activity) mContext);
        List<SideMenuItem> list = new ArrayList<SideMenuItem>();
        for (int i = 0; i < data.getMenuItems().size(); i++){
            MenuItemDataVO menuItemDataVO = data.getMenuItems().get(i);
            SideMenuItem item = new SideMenuItem();
            item.bgimg = getBitmapFromPath(menuItemDataVO.getBgImg());
            item.bgimg.setTargetDensity(density);
            item.img = getBitmapFromPath(menuItemDataVO.getButtonImg());
            item.img.setTargetDensity(density);
            list.add(item);
        }
        return list;
    }

    private BitmapDrawable getBitmapFromPath(String bgImg) {
        return SideMenuUtils.getLocalImgBitmap(mContext, bgImg);
    }

    public boolean isInitView() {
        // TODO Auto-generated method stub
        return isInit;
    }
    public View getSideMenu() {
        // TODO Auto-generated method stub
        return mSideMenu;
    }

    public void clean() {
        // TODO Auto-generated method stub
        mSideMenu=null;
        isInit=false;
        mLinearLayout.removeAllViews();
        itemsList.clear();
        dataList.clear();
        itemCount = 0;
    }
    @SuppressLint("NewApi")
    public void initView() {
        // TODO Auto-generated method stub
        mSideMenu=new ScrollView(mContext);
        mRelativeLayout=new RelativeLayout(mContext);
        mLinearLayout=new LinearLayout(mContext);
        LayoutParams lp=new LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT,android.view.ViewGroup.LayoutParams.MATCH_PARENT);

        mSideMenu.addView(mRelativeLayout, lp);
        lp=new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        lp.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);

        mRelativeLayout.addView(mLinearLayout, lp);
        if(dataList!=null&&dataList.size()>0){
            itemCount=dataList.size();
        }else{
            return;
        }

        mLinearLayout.setOrientation(LinearLayout.VERTICAL);

        lf=LayoutInflater.from(mContext);
        layoutId=EUExUtil.getResLayoutID("plugin_uexside_item");
        width = mWidth;

        for(int i=0;i<itemCount;i++){
            RelativeLayout rlay=(RelativeLayout) lf.inflate(layoutId,null);
            ImageView ivbg=(ImageView) rlay.findViewWithTag("bg_img");
            ivbg.getLayoutParams().width = mWidth;
            ivbg.getLayoutParams().height = mHeight;
            if(Build.VERSION.SDK_INT<16){
                ivbg.setBackgroundDrawable(dataList.get(i).img);
            }else{
                ivbg.setBackground(dataList.get(i).bgimg);
            }

            ImageView iv=(ImageView) rlay.findViewWithTag("img");
            RelativeLayout.LayoutParams ivP = new RelativeLayout.LayoutParams(
                    mHeight * 3 / 4, mHeight * 3 / 4);
            ivP.setMargins(0,0,mHeight / 8,0);
            ivP.addRule(RelativeLayout.CENTER_VERTICAL);
            ivP.addRule(RelativeLayout.ALIGN_RIGHT, EUExUtil.getResIdID("plugin_side_itembg"));
            iv.setLayoutParams(ivP);
            if(Build.VERSION.SDK_INT<16){
                iv.setBackgroundDrawable(dataList.get(i).img);
            }else{
                iv.setBackground(dataList.get(i).img);
            }
            LinearLayout.LayoutParams llp=new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT, 1);
            llp.bottomMargin=20;

            mLinearLayout.addView(rlay,llp);
            rlay.setOnClickListener(this);
            itemsList.add(rlay);
        }
        mSideMenu.setVisibility(View.INVISIBLE);
           isInit=true;
    }
    
    @Override
    public void onClick(View arg0) {
        // TODO Auto-generated method stub
        for(int i=0;i<itemCount;i++){
            
            if(arg0.equals(itemsList.get(i))){
                mCallBack.onItemClick(i);
                break;
            }
        }
        
    }
    
    public void openMenu(){
        isOpen=true;
        mSideMenu.setVisibility(View.VISIBLE);
        startOpenAnimation();
    }
    public void close() {
        startCloseAnimation();
        isOpen=false;
    }
    private void startOpenAnimation(){
    
        int duration=300;
        int time=0;
        int dtime=duration/itemCount;
        for(int i=0;i<itemCount;i++){
            MyAimation ta=new MyAimation(-width, 0, 0.2f);
            ta.setAnimationListener(new MyOpenAnimationListener(itemsList.get(i)));
            ta.setDuration(duration);
            ta.setFillEnabled(true);
            ta.setFillAfter(true);
            ta.setStartOffset(time);
            itemsList.get(i).startAnimation(ta);
            time+=dtime;
            
        }
        
        
    }
    private void startCloseAnimation(){
        
        int duration=300;
        int time=0;
        int dtime=duration/itemCount;
        for(int i=0;i<itemCount;i++){
            TranslateAnimation ta=new TranslateAnimation(0,-width,0, 0);
            ta.setAnimationListener(new MyCloseAnimationListener(itemsList.get(i)));
            ta.setInterpolator(new DecelerateInterpolator());
            ta.setDuration(duration);
           ta.setFillEnabled(true);
            ta.setFillAfter(true);
            ta.setStartOffset(time);
            itemsList.get(i).startAnimation(ta);
            time+=dtime;
            
        }
    }
     
    
    class MyOpenAnimationListener implements AnimationListener{
        private View v;
        public MyOpenAnimationListener(View v){
            this.v=v;
        }
        @Override
        public void onAnimationEnd(Animation arg0) {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void onAnimationRepeat(Animation arg0) {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void onAnimationStart(Animation arg0) {
            // TODO Auto-generated method stub
            v.setVisibility(View.VISIBLE);
        }
        
    }
    class MyCloseAnimationListener implements AnimationListener{
        private View v;
        public MyCloseAnimationListener(View v){
            this.v=v;
        }

        @Override
        public void onAnimationEnd(Animation arg0) {
            // TODO Auto-generated method stub
            v.setVisibility(View.INVISIBLE);
            
            if(v.equals(itemsList.get(itemCount-1))){
                mCallBack.removeViewFromLayout(mSideMenu);
            }
        }

        @Override
        public void onAnimationRepeat(Animation arg0) {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void onAnimationStart(Animation arg0) {
            // TODO Auto-generated method stub
            
        }
        
    }
    
    class MyAimation extends Animation{
        private float actor=0f;
        private float fXD=0f;
        private float tXD=0f;
        
        
        private float[] keyT=new float[2];
        
      
        public MyAimation(float fromXDelta, float toXDelta,float actor) {
             this.fXD=fromXDelta;
             this.tXD=toXDelta;
             if(actor>=1){
                 actor=0.2f;
             }
             this.actor=actor;
            
             
          
        }


    


        @Override
        protected void applyTransformation(float interpolatedTime,
                Transformation t) {
            // TODO Auto-generated method stub
             
            float dT=0f;
            if(interpolatedTime<keyT[0]){
                dT=interpolatedTime; 
                
            }else if(interpolatedTime<keyT[1]){
                
                dT=(2*keyT[0]-interpolatedTime); 
            }else{
                dT=keyT[0]+interpolatedTime-1;
            }
            float dx = fXD + ((tXD-fXD)*(1+2*actor))* dT; 
            
            t.getMatrix().setTranslate(dx, 0); 

            
        }

        @Override
        public void initialize(int width, int height, int parentWidth,
                int parentHeight) {
            // TODO Auto-generated method stub
        
            super.initialize(width, height, parentWidth, parentHeight);
            keyT[0]=1/(1+2*actor);
            keyT[1]=(1+actor)/(1+2*actor);
            
            setFillEnabled(true);
            setFillAfter(true);
            setInterpolator(new DecelerateInterpolator());
            
        }
        
        
        
    }
    
}
