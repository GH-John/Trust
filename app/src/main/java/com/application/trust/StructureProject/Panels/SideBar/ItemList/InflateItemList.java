package com.application.trust.StructureProject.Panels.SideBar.ItemList;

import com.application.trust.Fragments.FragmentAllAnnouncements;
import com.application.trust.Fragments.FragmentCustomService;
import com.application.trust.Fragments.FragmentProhibited;
import com.application.trust.Fragments.FragmentRegulations;
import com.application.trust.Fragments.FragmentServices;
import com.application.trust.Fragments.FragmentUserAnnouncements;
import com.application.trust.Fragments.FragmentUserFavorites;
import com.application.trust.Fragments.FragmentUserMessages;
import com.application.trust.Fragments.FragmentUserProposals;
import com.application.trust.Fragments.FragmentUserStatistics;
import com.application.trust.Fragments.FragmentUserWallet;
import com.application.trust.R;

import java.util.ArrayList;
import java.util.List;

public final class InflateItemList {
    public static List<ContentItemList> getitemListData(PanelItemList panelItemList){
        List<ContentItemList> contentItemLists = new ArrayList<>();
        contentItemLists.add(new ContentItemList(new FragmentAllAnnouncements(),R.string.itemList_1, R.drawable.sb_item_all_announcement, panelItemList));
        contentItemLists.add(new ContentItemList(new FragmentUserAnnouncements(), R.string.itemList_2, R.drawable.sb_item_user_announcement, panelItemList));
        contentItemLists.add(new ContentItemList(new FragmentUserStatistics(), R.string.itemList_3, R.drawable.sb_item_statistics, panelItemList));
        contentItemLists.add(new ContentItemList(new FragmentUserFavorites(), R.string.itemList_4, R.drawable.sb_item_favorites, panelItemList));
        contentItemLists.add(new ContentItemList(new FragmentUserMessages(), R.string.itemList_5, R.drawable.sb_item_message, panelItemList));
        contentItemLists.add(new ContentItemList(new FragmentUserProposals(), R.string.itemList_6, R.drawable.sb_item_proposals, panelItemList));
        contentItemLists.add(new ContentItemList(new FragmentUserWallet(), R.string.itemList_7, R.drawable.sb_item_wallet, panelItemList));
        contentItemLists.add(new ContentItemList(new FragmentServices(), R.string.itemList_8, R.drawable.sb_item_services, panelItemList));
        contentItemLists.add(new ContentItemList(new FragmentRegulations(), R.string.itemList_9, R.drawable.sb_item_regulations, panelItemList));
        contentItemLists.add(new ContentItemList(new FragmentCustomService(), R.string.itemList_10, R.drawable.sb_item_customer_service, panelItemList));
        contentItemLists.add(new ContentItemList(new FragmentProhibited(), R.string.itemList_11, R.drawable.sb_item_prohibited, panelItemList));
        return contentItemLists;
    }
}