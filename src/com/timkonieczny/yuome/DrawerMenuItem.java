package com.timkonieczny.yuome;

public class DrawerMenuItem {
    protected int icon;
    protected String menuItemName;
    protected DrawerMenuItem(){
        super();
    }

    public DrawerMenuItem(int icon, String menuItemName) {
        super();
        this.icon = icon;
        this.menuItemName = menuItemName;
    }
}