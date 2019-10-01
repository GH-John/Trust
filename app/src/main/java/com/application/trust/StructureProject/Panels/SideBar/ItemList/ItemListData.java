package com.application.trust.StructureProject.Panels.SideBar.ItemList;

import com.application.trust.R;

import java.util.ArrayList;
import java.util.List;

public final class ItemListData {
    public static List<ItemList> getitemListData(){
        List<ItemList> itemLists = new ArrayList<>();
        itemLists.add(new ItemList(R.string.itemList_1, R.drawable.item_all_announcement, R.drawable.style_item_list));
        itemLists.add(new ItemList(R.string.itemList_2, R.drawable.item_user_announcement, R.drawable.style_item_list));
        itemLists.add(new ItemList(R.string.itemList_3, R.drawable.item_statistics, R.drawable.style_item_list));
        itemLists.add(new ItemList(R.string.itemList_4, R.drawable.item_favorites, R.drawable.style_item_list));
        itemLists.add(new ItemList(R.string.itemList_5, R.drawable.item_message, R.drawable.style_item_list));
        itemLists.add(new ItemList(R.string.itemList_6, R.drawable.item_proposals, R.drawable.style_item_list));
        itemLists.add(new ItemList(R.string.itemList_7, R.drawable.item_wallet, R.drawable.style_item_list));
        itemLists.add(new ItemList(R.string.itemList_8, R.drawable.item_services, R.drawable.style_item_list));
        itemLists.add(new ItemList(R.string.itemList_9, R.drawable.item_regulations, R.drawable.style_item_list));
        itemLists.add(new ItemList(R.string.itemList_10, R.drawable.item_customer_service, R.drawable.style_item_list));
        itemLists.add(new ItemList(R.string.itemList_11, R.drawable.item_prohibited, R.drawable.style_item_list));
        return itemLists;
    }
}