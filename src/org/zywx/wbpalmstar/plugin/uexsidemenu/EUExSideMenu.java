package org.zywx.wbpalmstar.plugin.uexsidemenu;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import org.json.JSONException;
import org.json.JSONObject;
import org.zywx.wbpalmstar.base.BUtility;
import org.zywx.wbpalmstar.engine.DataHelper;
import org.zywx.wbpalmstar.engine.EBrowserView;
import org.zywx.wbpalmstar.engine.universalex.EUExBase;
import org.zywx.wbpalmstar.plugin.uexsidemenu.vo.MenuDataVO;
import org.zywx.wbpalmstar.plugin.uexsidemenu.vo.MenuItemDataVO;

import java.util.List;

public class EUExSideMenu extends EUExBase {

    private static final String BUNDLE_DATA = "data";
    private static final int MSG_OPEN = 1;
    private static final int MSG_CLOSE = 2;
    private SideMenu mSideMenu;

    public EUExSideMenu(Context context, EBrowserView eBrowserView) {
        super(context, eBrowserView);
    }

    @Override
    protected boolean clean() {
        return false;
    }


    public void open(String[] params) {
        if (params == null || params.length < 1) {
            errorCallback(0, 0, "error params!");
            return;
        }
        Message msg = new Message();
        msg.obj = this;
        msg.what = MSG_OPEN;
        Bundle bd = new Bundle();
        bd.putStringArray(BUNDLE_DATA, params);
        msg.setData(bd);
        mHandler.sendMessage(msg);
    }

    private void openMsg(String[] params) {
        String json = params[0];
        MenuDataVO dataVO = DataHelper.gson.fromJson(json, MenuDataVO.class);
        List<MenuItemDataVO> items =  dataVO.getMenuItems();
        if (items == null || items.size() < 1){
            errorCallback(0, 0, "error params!");
            return;
        }
        for (int i = 0; i < items.size(); i++){
            MenuItemDataVO itemDataVO = items.get(i);
            String btnPath = itemDataVO.getButtonImg();
            if (!TextUtils.isEmpty(btnPath)){
                String desPath = getRealPath(btnPath);
                itemDataVO.setButtonImg(desPath);
            }
            String bgPath = itemDataVO.getBgImg();
            if (!TextUtils.isEmpty(bgPath)){
                String desPath = getRealPath(bgPath);
                itemDataVO.setBgImg(desPath);
            }
        }
        RelativeLayout.LayoutParams lparm = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lparm.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        lparm.leftMargin = dataVO.getLeft();
        lparm.topMargin = dataVO.getTop();

        if (mSideMenu == null) {
            mSideMenu = new SideMenu(mContext, dataVO, mListener);
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

    public void close(String[] params) {
        Message msg = new Message();
        msg.obj = this;
        msg.what = MSG_CLOSE;
        Bundle bd = new Bundle();
        bd.putStringArray(BUNDLE_DATA, params);
        msg.setData(bd);
        mHandler.sendMessage(msg);
    }

    private void closeMsg() {
        if (mSideMenu != null&&mSideMenu.isInitView()) {
            if(mSideMenu.isOpen){
                mSideMenu.close();
            }
        }
    }

    @Override
    public void onHandleMessage(Message message) {
        if(message == null){
            return;
        }
        Bundle bundle=message.getData();
        switch (message.what) {

            case MSG_OPEN:
                openMsg(bundle.getStringArray(BUNDLE_DATA));
                break;
            case MSG_CLOSE:
                closeMsg();
                break;
            default:
                super.onHandleMessage(message);
        }
    }

    private void callBackPluginJs(String methodName, String jsonData){
        String js = SCRIPT_HEADER + "if(" + methodName + "){"
                + methodName + "('" + jsonData + "');}";
        onCallback(js);
    }

    private String getRealPath(String srcPath){
        String imgPath = BUtility.makeRealPath(
                BUtility.makeUrl(mBrwView.getCurrentUrl(), srcPath),
                mBrwView.getCurrentWidget().m_widgetPath,
                mBrwView.getCurrentWidget().m_wgtType);
        return imgPath;
    }

    private CallbackListener mListener = new CallbackListener() {
        @Override
        public void onItemClick(int index) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put(JsConst.CALLBACK_INDEX, index);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            callBackPluginJs(JsConst.ON_ITEM_CLICK, jsonObject.toString());
        }

        @Override
        public void removeViewFromLayout(View view) {
            removeViewFromCurrentWindow(view);
        }
    };

    public interface CallbackListener{
        public void onItemClick(int index);
        public void removeViewFromLayout(View view);
    }

}
