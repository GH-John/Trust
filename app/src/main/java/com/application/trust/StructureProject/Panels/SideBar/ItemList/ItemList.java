package com.application.trust.StructureProject.Panels.SideBar.ItemList;

class ItemList {
    private int idNameItemList;
    private int idIconItemList;
    private int idStyleItemList;

    public ItemList(int idNameItemList, int idIconItemList, int idStyleItemList) {
        this.idNameItemList = idNameItemList;
        this.idIconItemList = idIconItemList;
        this.idStyleItemList = idStyleItemList;
    }

    public int getIdIconItemList() {
        return idIconItemList;
    }

    public int getIdStyleItemList() {
        return idStyleItemList;
    }

    public int getIdNameItemList() {
        return idNameItemList;
    }
}