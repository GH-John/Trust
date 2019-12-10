package com.application.arenda.CustomComponents.Panels.SideBar.ItemList;

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
    public static List<ContentItemList> getItemListData(Drawable drawable){
        List<ContentItemList> contentItemLists = new ArrayList<>();
        contentItemLists.add(new ContentItemList(new FragmentAllAnnouncements(),R.string.itemList_all_announcements, R.drawable.sb_item_search_announcements, drawable));
        contentItemLists.add(new ContentItemList(new FragmentUserAnnouncements(), R.string.itemList_user_announcements, R.drawable.sb_item_user_announcements, drawable));
        contentItemLists.add(new ContentItemList(new FragmentUserProposals(), R.string.itemList_proposals, R.drawable.sb_item_proposals, drawable));
        contentItemLists.add(new ContentItemList(new FragmentUserStatistics(), R.string.itemList_statistics, R.drawable.sb_item_statistics, drawable));
        contentItemLists.add(new ContentItemList(new FragmentUserFavorites(), R.string.itemList_favorites, R.drawable.sb_item_favorites, drawable));
        contentItemLists.add(new ContentItemList(new FragmentUserWallet(), R.string.itemList_wallet, R.drawable.sb_item_wallet, drawable));
        contentItemLists.add(new ContentItemList(new FragmentServices(), R.string.itemList_services, R.drawable.sb_item_services, drawable));
        contentItemLists.add(new ContentItemList(new FragmentRegulations(), R.string.itemList_regulations, R.drawable.sb_item_regulations, drawable));
        contentItemLists.add(new ContentItemList(new FragmentCustomerService(), R.string.itemList_customer_service, R.drawable.sb_item_customer_service, drawable));
        contentItemLists.add(new ContentItemList(new FragmentProhibited(), R.string.itemList_prohibited, R.drawable.sb_item_prohibited, drawable));
        return contentItemLists;
    }
}