package org.zywx.wbpalmstar.plugin.uexsidemenu.vo;

import java.io.Serializable;
import java.util.List;

public class MenuDataVO implements Serializable{
    private static final long serialVersionUID = -6407758894124150915L;
    private double left = 0;
    private double top = 0;
    private double width = -1;
    private double height = -1;
    private List<MenuItemDataVO> menuItems;

    public int getLeft() {
        return (int)left;
    }

    public void setLeft(double left) {
        this.left = left;
    }

    public int getTop() {
        return (int)top;
    }

    public void setTop(double top) {
        this.top = top;
    }

    public int getWidth() {
        return (int)width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public int getHeight() {
        return (int)height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public List<MenuItemDataVO> getMenuItems() {
        return menuItems;
    }

    public void setMenuItems(List<MenuItemDataVO> menuItems) {
        this.menuItems = menuItems;
    }
}
