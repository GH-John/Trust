package com.application.arenda.UI.Components.SideBar.ItemList;

import android.graphics.drawable.Drawable;

import com.application.arenda.MainWorkspace.Fragments.FragmentAllAnnouncements;
import com.application.arenda.MainWorkspace.Fragments.FragmentCustomerService;
import com.application.arenda.MainWorkspace.Fragments.FragmentProhibited;
import com.application.arenda.MainWorkspace.Fragments.FragmentRegulations;
import com.application.arenda.MainWorkspace.Fragments.FragmentServices;
import com.application.arenda.MainWorkspace.Fragments.FragmentUserAnnouncements;
import com.application.arenda.MainWorkspace.Fragments.FragmentUserFavorites;
import com.application.arenda.MainWorkspace.Fragments.FragmentUserProposals;
import com.application.arenda.MainWorkspace.Fragments.FragmentUserStatistics;
import com.application.arenda.MainWorkspace.Fragments.FragmentUserWallet;
import com.application.arenda.R;

import java.util.ArrayList;
import java.util.List;

public final class InflateItemList {
    public static List<ModelItemList> getItemListData(Drawable drawable){
        List<ModelItemList> modelItemLists = new ArrayList<>();
        modelItemLists.add(new ModelItemList(FragmentAllAnnouncements.getInstance(),R.string.itemList_all_announcements, R.drawable.sb_item_search_announcements, drawable));
        modelItemLists.add(new ModelItemList(FragmentUserAnnouncements.getInstance(), R.string.itemList_user_announcements, R.drawable.sb_item_user_announcements, drawable));
        modelItemLists.add(new ModelItemList(FragmentUserProposals.getInstance(), R.string.itemList_proposals, R.drawable.sb_item_proposals, drawable));
        modelItemLists.add(new ModelItemList(new FragmentUserStatistics(), R.string.itemList_statistics, R.drawable.sb_item_statistics, drawable));
        modelItemLists.add(new ModelItemList(new FragmentUserFavorites(), R.string.itemList_favorites, R.drawable.sb_item_favorites, drawable));
        modelItemLists.add(new ModelItemList(new FragmentUserWallet(), R.string.itemList_wallet, R.drawable.sb_item_wallet, drawable));
        modelItemLists.add(new ModelItemList(new FragmentServices(), R.string.itemList_services, R.drawable.sb_item_services, drawable));
        modelItemLists.add(new ModelItemList(new FragmentRegulations(), R.string.itemList_regulations, R.drawable.sb_item_regulations, drawable));
        modelItemLists.add(new ModelItemList(new FragmentCustomerService(), R.string.itemList_customer_service, R.drawable.sb_item_customer_service, drawable));
        modelItemLists.add(new ModelItemList(new FragmentProhibited(), R.string.itemList_prohibited, R.drawable.sb_item_prohibited, drawable));
        return modelItemLists;
    }
}