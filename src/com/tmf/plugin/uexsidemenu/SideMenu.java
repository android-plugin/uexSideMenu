package com.tmf.plugin.uexsidemenu;

import java.util.ArrayList;


import org.zywx.wbpalmstar.engine.universalex.EUExUtil;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.BounceInterpolator;
import android.view.animation.CycleInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.Transformation;

import android.view.animation.AnticipateInterpolator;

import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.ScrollView;

public class SideMenu implements View.OnClickListener {
	
	private EUExSideMenu mcallBack;
	private Context mContext;
    private ScrollView mSideMenu;
    private RelativeLayout mRelativeLayout;
    private LinearLayout mLinearLayout;
    private ArrayList<SideMenuItem> dataList=new ArrayList<SideMenuItem>();
    private ArrayList<RelativeLayout> itemsList=new ArrayList<RelativeLayout>();
    private int itemcount;
    private boolean isInit;
	public boolean isOpen=false;
	private LayoutInflater lf;
	private int layoutid=0;
	private int width=0;
    public SideMenu(Context ctx,EUExSideMenu callback){
    	this.mContext=ctx;
    	this.mcallBack=callback;
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
		itemcount=0;
	}
	@SuppressLint("NewApi")
	public void initView() {
		// TODO Auto-generated method stub
		mSideMenu=new ScrollView(mContext);
		mRelativeLayout=new RelativeLayout(mContext);
		mLinearLayout=new LinearLayout(mContext);
		LayoutParams lp=new LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT,android.view.ViewGroup.LayoutParams.MATCH_PARENT);
	
		mSideMenu.addView(mRelativeLayout, lp);
		lp=new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.MATCH_PARENT);
		lp.addRule(RelativeLayout.CENTER_VERTICAL,RelativeLayout.TRUE);
		
		mRelativeLayout.addView(mLinearLayout,lp);
		if(dataList!=null&&dataList.size()>0){
			itemcount=dataList.size();
		}else{
			//set default items;
			getDefaultDataList();
		}
		
		mLinearLayout.setOrientation(LinearLayout.VERTICAL);
		
		 lf=LayoutInflater.from(mContext);
		 layoutid=EUExUtil.getResLayoutID("plugin_uexside_item");
		 width=dataList.get(0).bgimg.getIntrinsicWidth();
		for(int i=0;i<itemcount;i++){
			RelativeLayout rlay=(RelativeLayout) lf.inflate(layoutid,null);
			ImageView ivbg=(ImageView) rlay.findViewWithTag("bg_img");
			if(Build.VERSION.SDK_INT<16){
				ivbg.setBackgroundDrawable(dataList.get(i).img);
			}else{
				ivbg.setBackground(dataList.get(i).bgimg);
			}
			
			ImageView iv=(ImageView) rlay.findViewWithTag("img");
			if(Build.VERSION.SDK_INT<16){
				iv.setBackgroundDrawable(dataList.get(i).img);
			}else{
				iv.setBackground(dataList.get(i).img);
			}
			
			LinearLayout.LayoutParams llp=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1);
			llp.bottomMargin=20;
			
			mLinearLayout.addView(rlay,llp);
			rlay.setOnClickListener(this);
			itemsList.add(rlay);
			
		}
		mSideMenu.setVisibility(View.INVISIBLE);
	   	isInit=true;
		
		
	}
	
	private void getDefaultDataList() {
		// TODO Auto-generated method stub
		int[] imgIds=new int[5];
		int bgImgIds=0;
		imgIds[0]=EUExUtil.getResDrawableID("plugin_uexside_ite1");
		imgIds[1]=EUExUtil.getResDrawableID("plugin_uexside_ite2");
		imgIds[2]=EUExUtil.getResDrawableID("plugin_uexside_ite3");
		imgIds[3]=EUExUtil.getResDrawableID("plugin_uexside_ite4");
		imgIds[4]=EUExUtil.getResDrawableID("plugin_uexside_ite5");
	    bgImgIds=EUExUtil.getResDrawableID("plugin_uexside_bg");
	  
	    if(dataList==null){
	    	dataList=new ArrayList<SideMenuItem>();
	    }
	    if(dataList.size()>0){
	    	dataList.clear();
	    }
	    Drawable bg=mContext.getResources().getDrawable(bgImgIds);
	   
	    for(int i=0; i<5;i++){
	       	 SideMenuItem item=new SideMenuItem();
	    	 item.img=(BitmapDrawable) mContext.getResources().getDrawable(imgIds[i]);
	    	 item.bgimg=(BitmapDrawable) bg;
	    	 dataList.add(item);
	    }
	    itemcount=5;
	   
	}
	public void setItems(ArrayList<SideMenuItem> sideBeanList) {
		// TODO Auto-generated method stub
	   dataList=sideBeanList;
	  
	   notifyItemDataChanged();
		
	}
	@SuppressLint("NewApi")
	private void notifyItemDataChanged(){
		if(isInit){
			if(dataList!=null&&dataList.size()>0){
				width=dataList.get(0).bgimg.getIntrinsicWidth();
				int newCount=dataList.size();
				if(newCount>itemcount){
					for(int i=0;i<itemcount;i++){
						
						if(Build.VERSION.SDK_INT<16){
							itemsList.get(i).findViewWithTag("img").setBackgroundDrawable(dataList.get(i).img);
							itemsList.get(i).findViewWithTag("bg_img").setBackgroundDrawable(dataList.get(i).bgimg);
					
						}else{
							itemsList.get(i).findViewWithTag("img").setBackground(dataList.get(i).img);
							itemsList.get(i).findViewWithTag("bg_img").setBackground(dataList.get(i).bgimg);
					
						}
						
					}
					
					for(int i=itemcount;i<newCount;i++){
						RelativeLayout rlay=(RelativeLayout) lf.inflate(layoutid,null);
						ImageView ivbg=(ImageView) rlay.findViewWithTag("bg_img");
						if(Build.VERSION.SDK_INT<16){
							ivbg.setBackgroundDrawable(dataList.get(i).img);
						}else{
							
							ivbg.setBackground(dataList.get(i).bgimg);
						}
					
						ImageView iv=(ImageView) rlay.findViewWithTag("img");
						
						if(Build.VERSION.SDK_INT<16){
							iv.setBackgroundDrawable(dataList.get(i).img);
						}else{
							iv.setBackground(dataList.get(i).img);
						}
						
						LinearLayout.LayoutParams llp=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1);
						llp.bottomMargin=20;
						mLinearLayout.addView(rlay,llp);
						rlay.setOnClickListener(this);
						itemsList.add(rlay);
					}
					
				}else if(newCount<itemcount){
					for(int i=0;i<newCount;i++){
						if(Build.VERSION.SDK_INT<16){
							itemsList.get(i).findViewWithTag("img").setBackgroundDrawable(dataList.get(i).img);
							itemsList.get(i).findViewWithTag("bg_img").setBackgroundDrawable(dataList.get(i).bgimg);
					
						}else{
							itemsList.get(i).findViewWithTag("img").setBackground(dataList.get(i).img);
							itemsList.get(i).findViewWithTag("bg_img").setBackground(dataList.get(i).bgimg);
					
						}
						
				
					}
					for(int i=newCount;i<itemcount;i++){
						mLinearLayout.removeView(itemsList.get(i));
					}
				}else{
					for(int i=0;i<itemcount;i++){
						if(Build.VERSION.SDK_INT<16){
							itemsList.get(i).findViewWithTag("img").setBackgroundDrawable(dataList.get(i).img);
							itemsList.get(i).findViewWithTag("bg_img").setBackgroundDrawable(dataList.get(i).bgimg);
					
						}else{
							itemsList.get(i).findViewWithTag("img").setBackground(dataList.get(i).img);
							itemsList.get(i).findViewWithTag("bg_img").setBackground(dataList.get(i).bgimg);
					
						}
					}	
				}
				itemcount=newCount;
				
			}else{
				mLinearLayout.removeAllViews();
				itemsList.clear();
				itemcount=0;
			}
			
			
		}
		
	}
	
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		for(int i=0;i<itemcount;i++){
			
			if(arg0.equals(itemsList.get(i))){
				mcallBack.onItemClick(i);
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
		int dtime=duration/itemcount;
		for(int i=0;i<itemcount;i++){
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
		int dtime=duration/itemcount;
		for(int i=0;i<itemcount;i++){
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
			
			if(v.equals(itemsList.get(itemcount-1))){
				
				mcallBack.removeViewFromCurrentWindow(mSideMenu);
				
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
