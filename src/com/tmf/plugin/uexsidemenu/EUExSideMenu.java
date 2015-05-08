package com.tmf.plugin.uexsidemenu;

import java.io.InputStream;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.zywx.wbpalmstar.base.BUtility;
import org.zywx.wbpalmstar.engine.EBrowserView;

import org.zywx.wbpalmstar.engine.universalex.EUExBase;
import org.zywx.wbpalmstar.engine.universalex.EUExCallback;


import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
/*
1. open(params)
var params = {
	x:0,
    y:0,
};
2. close()
	3. setItems(params) //闁荤姳绀佹晶浠嬫偪閸℃稑鏋侀柣妤�墕缁侊箑顪冮妶蹇斿
var params = {
	
	menuItems:[
		item:{
			buttonImg:闂佺偨鍎查崡鐮畇://img_n.png闂佺偨鍎婚幏锟�/闂佹悶鍎辨晶鑺ユ櫠閿燂拷			bgImg:闂佺偨鍎查崡鐮畇://img_b.png闂佺偨鍎婚幏锟�/闂佹悶鍎辨晶鑺ユ櫠閿燂拷		}, 
闂佺偨鍎婚幏锟�闂佺绻戝﹢鍦垝椤掑嫬鏋侀柣妤�墕缁侊箑顪冮妶蹇斿
	]
};
4.onItemClick(data) //閻庢鍠氶幊鎾诲吹椤撱垺鍤曟繝濠傚暙缁�顪冮妶鍥舵敯妞ゎ偓绠撻幃娆撴偡閺夋寧鐦栭梺姹囧妼鐎氼喖煤閸愵喖瑙︽い鎴ｆ硶閻ㄦ垵霉閻欏懏瀚�{
	index:0
}


*/
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

public class EUExSideMenu extends EUExBase {
	private SideMenu mSideMenu;
	private static final String function_onItemClick="uexSideMenu.onItemClick";
	private  Object lock=new Object();
    private int density;
	public EUExSideMenu(Context arg0, EBrowserView arg1) {
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
		mSideMenu=new SideMenu(arg0, this);
		density=BUtility.getDeviceDesity((Activity) arg0);
		  
	}

	@Override
	protected boolean clean() {
		// TODO Auto-generated method stub
		
		synchronized (lock) {
			((Activity)mContext).runOnUiThread(new Runnable() {
				
			    @Override
				public void run() {
					// TODO Auto-generated method stub
			    	if (null != mSideMenu&&mSideMenu.isInitView()) {
						removeViewFromCurrentWindow(mSideMenu.getSideMenu());
						mSideMenu.clean();
					}
					mSideMenu=null;
				}
			});
			    
			
			
		}
		
		return true;
	}
	
	public void open(String[] params){
		 if (params.length < 2) {
				return;
			}
			String inX = params[0];
			String inY = params[1];
			

			int x = 0;
			int y = 0;
			
			try {
				x = Integer.parseInt(inX);
				y = Integer.parseInt(inY);
				
			} catch (Exception e) {
				e.printStackTrace();
				return;
			}
			
			 final RelativeLayout.LayoutParams lparm = new RelativeLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
			      lparm.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
			     	lparm.leftMargin = x;
			 		lparm.topMargin = y;
			 		
				
				synchronized (lock) {
					((Activity)mContext).runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							if (mSideMenu == null) {
								mSideMenu = new SideMenu(mContext, EUExSideMenu.this);
								mSideMenu.initView();
								}
							if(!mSideMenu.isInitView()){
								mSideMenu.initView();
							}
							if(mSideMenu.getSideMenu().getParent()!=null){
								
								removeViewFromCurrentWindow(mSideMenu.getSideMenu());
							}
						
							addViewToCurrentWindow(mSideMenu.getSideMenu(), lparm);
						    mSideMenu.openMenu();
						}
					});
					
				}
				
		
	}
	public void close(String[] params){
		synchronized (lock) {
			((Activity)mContext).runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					if (mSideMenu != null&&mSideMenu.isInitView()) {
		                if(mSideMenu.isOpen){
		                	mSideMenu.close();
		                }
						//removeViewFromCurrentWindow(mSideMenu.getSideMenu());
						
					}
				}
				}
			);
		}
			
		
		
	}

	public void setItems(String[] params){
		if (params.length < 1) {
			return;
		}
		String jsonParam = params[0];
		/*
		 *var params = {
	
	menuItems:[
		item:{
			buttonImg:闂佺偨鍎查崡鐮畇://img_n.png闂佺偨鍎婚幏锟�/闂佹悶鍎辨晶鑺ユ櫠閿燂拷			bgImg:闂佺偨鍎查崡鐮畇://img_b.png闂佺偨鍎婚幏锟�/闂佹悶鍎辨晶鑺ユ櫠閿燂拷		}, 
闂佺偨鍎婚幏锟�闂佺绻戝﹢鍦垝椤掑嫬鏋侀柣妤�墕缁侊箑顪冮妶蹇斿
	]
		 */
		try {
			final ArrayList<SideMenuItem> sideBeanList=new ArrayList<SideMenuItem>();
			JSONObject tabItems = new JSONObject(jsonParam);
			JSONArray items = tabItems.getJSONArray("menuItems");
			for(int i=0;i<items.length();i++){
				JSONObject item=(JSONObject) items.get(i);
				JSONObject itemdata=item.getJSONObject("item");
				SideMenuItem	sideitembean=new SideMenuItem();
				String imgpath=itemdata.getString("buttonImg");
				String bgimgpath=itemdata.getString("bgImg");
				InputStream in = BUtility.getInputStreamByResPath(mContext,
								imgpath);

				InputStream in1 = BUtility.getInputStreamByResPath(mContext,
								  bgimgpath);
				sideitembean.img=new BitmapDrawable(mContext.getResources(), in);
				sideitembean.img.setTargetDensity(density);
				
				sideitembean.bgimg=new BitmapDrawable(mContext.getResources(),in1);
				sideitembean.bgimg.setTargetDensity(density);	
				
				sideBeanList.add(sideitembean);
				
			}
			synchronized (lock) {
			
		    ((Activity)mContext).runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						
							if (mSideMenu == null) {
								mSideMenu = new SideMenu(mContext, EUExSideMenu.this);
							}
							mSideMenu.setItems(sideBeanList);
					
				}
			
			});
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	public void onItemClick(int index){
		
		JSONObject data=new JSONObject();
		try {
			data.put("index", index);
			jsCallback(function_onItemClick, 0, EUExCallback.F_C_JSON, data.toString());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	

}
