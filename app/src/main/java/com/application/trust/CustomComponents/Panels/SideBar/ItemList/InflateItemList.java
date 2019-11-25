package com.application.trust.CustomComponents.Panels.SideBar.ItemList;

import com.application.trust.MainWorkspace.Fragments.FragmentAllAnnouncements;
import com.application.trust.MainWorkspace.Fragments.FragmentCustomerService;
import com.application.trust.MainWorkspace.Fragments.FragmentProhibited;
import com.application.trust.MainWorkspace.Fragments.FragmentRegulations;
import com.application.trust.MainWorkspace.Fragments.FragmentServices;
import com.application.trust.MainWorkspace.Fragments.FragmentUserAnnouncements;
import com.application.trust.MainWorkspace.Fragments.FragmentUserFavorites;
import com.application.trust.MainWorkspace.Fragments.FragmentUserProposals;
import com.application.trust.MainWorkspace.Fragments.FragmentUserStatistics;
import com.application.trust.MainWorkspace.Fragments.FragmentUserWallet;
import com.application.trust.R;

import java.util.ArrayList;
import java.util.List;

public final class InflateItemList {
    public static List<ContentItemList> getItemListData(PanelItemList panelItemList){
        List<ContentItemList> contentItemLists = new ArrayList<>();
        contentItemLists.add(new ContentItemList(new FragmentAllAnnouncements(),R.string.itemList_all_announcements, R.drawable.sb_item_search_announcements, panelItemList));
        contentItemLists.add(new ContentItemList(new FragmentUserAnnouncements(), R.string.itemList_user_announcements, R.drawable.sb_item_user_announcements, panelItemList));
        contentItemLists.add(new ContentItemList(new FragmentUserProposals(), R.string.itemList_proposals, R.drawable.sb_item_proposals, panelItemList));
        contentItemLists.add(new ContentItemList(new FragmentUserStatistics(), R.string.itemList_statistics, R.drawable.sb_item_statistics, panelItemList));
        contentItemLists.add(new ContentItemList(new FragmentUserFavorites(), R.string.itemList_favorites, R.drawable.sb_item_favorites, panelItemList));
        contentItemLists.add(new ContentItemList(new FragmentUserWallet(), R.string.itemList_wallet, R.drawable.sb_item_wallet, panelItemList));
        contentItemLists.add(new ContentItemList(new FragmentServices(), R.string.itemList_services, R.drawable.sb_item_services, panelItemList));
        contentItemLists.add(new ContentItemList(new FragmentRegulations(), R.string.itemList_regulations, R.drawable.sb_item_regulations, panelItemList));
        contentItemLists.add(new ContentItemList(new FragmentCustomerService(), R.string.itemList_customer_service, R.drawable.sb_item_customer_service, panelItemList));
        contentItemLists.add(new ContentItemList(new FragmentProhibited(), R.string.itemList_prohibited, R.drawable.sb_item_prohibited, panelItemList));
        return contentItemLists;
    }
}