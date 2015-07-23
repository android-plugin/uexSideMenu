package org.zywx.wbpalmstar.plugin.uexsidemenu.vo;

import java.io.Serializable;

public class MenuItemDataVO implements Serializable{
    private static final long serialVersionUID = -1641482931071504941L;
    private String buttonImg;
    private String bgImg;

    public String getButtonImg() {
        return buttonImg;
    }

    public void setButtonImg(String buttonImg) {
        this.buttonImg = buttonImg;
    }

    public String getBgImg() {
        return bgImg;
    }

    public void setBgImg(String bgImg) {
        this.bgImg = bgImg;
    }
}
