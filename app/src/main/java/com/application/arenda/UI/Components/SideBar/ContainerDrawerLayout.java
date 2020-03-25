package com.application.arenda.UI.Components.SideBar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.drawerlayout.widget.DrawerLayout;

import com.application.arenda.Entities.Utils.Utils;
import com.application.arenda.MainWorkspace.Fragments.FragmentAllAnnouncements;
import com.application.arenda.MainWorkspace.Fragments.FragmentCustomerService;
import com.application.arenda.MainWorkspace.Fragments.FragmentProhibited;
import com.application.arenda.MainWorkspace.Fragments.FragmentRegulations;
import com.application.arenda.MainWorkspace.Fragments.FragmentServices;
import com.application.arenda.MainWorkspace.Fragments.FragmentUserAnnouncements;
import com.application.arenda.MainWorkspace.Fragments.FragmentUserFavorites;
import com.application.arenda.MainWorkspace.Fragments.FragmentUserStatistics;
import com.application.arenda.MainWorkspace.Fragments.FragmentUserWallet;
import com.application.arenda.MainWorkspace.Fragments.Proposals.FragmentUserProposals;
import com.application.arenda.R;
import com.application.arenda.UI.Components.ComponentManager;
import com.application.arenda.UI.Components.ContainerFragments.ContainerFragments;
import com.google.android.material.navigation.NavigationView;

import timber.log.Timber;

public final class ContainerDrawerLayout implements SideBar,
        NavigationView.OnNavigationItemSelectedListener, ComponentManager.Observer {

    @SuppressLint("StaticFieldLeak")
    private static ContainerDrawerLayout containerDrawerLayout;

    private DrawerLayout drawerLayout;

    private NavigationView leftMenu;

    private FrameLayout rightMenu;

    private ImageButton itemUserAccount, itemLogout;

    private ContainerDrawerLayout(Activity activity) {
        initComponents(activity);

        initListeners();
    }

    public static ContainerDrawerLayout getInstance(Activity activity) {
        if (containerDrawerLayout == null)
            containerDrawerLayout = new ContainerDrawerLayout(activity);

        return containerDrawerLayout;
    }

    private void initComponents(Activity activity) {
        leftMenu = activity.findViewById(R.id.sbLeftMenu);
        drawerLayout = activity.findViewById(R.id.sbDrawerMenu);
        rightMenu = activity.findViewById(R.id.sbRightMenuLayout);

        itemLogout = activity.findViewById(R.id.itemLogout);
        itemUserAccount = activity.findViewById(R.id.itemUserAccount);

        drawerLayout.setScrimColor(Color.TRANSPARENT);
    }

    private void initListeners() {
        leftMenu.setNavigationItemSelectedListener(this);
    }

    private void setCheckedItem(ItemSideBar item) {
        if (item instanceof FragmentAllAnnouncements) {
            leftMenu.getMenu().getItem(0).setChecked(true);
        } else if (item instanceof FragmentUserAnnouncements) {
            leftMenu.getMenu().getItem(1).setChecked(true);
        } else if (item instanceof FragmentUserProposals) {
            leftMenu.getMenu().getItem(2).setChecked(true);
        } else if (item instanceof FragmentUserStatistics) {
            leftMenu.getMenu().getItem(3).setChecked(true);
        }
    }

    @Override
    public void update(@NonNull Object object) {
        closeLeftMenu();
        if (object instanceof ItemSideBar) {
            ((ItemSideBar) object).setSideBar(this);

            setCheckedItem((ItemSideBar) object);

            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        } else {
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {

            case R.id.item_all_announcements:
                ContainerFragments.getInstance().replaceFragmentInContainer(FragmentAllAnnouncements.getInstance());
                return true;
            case R.id.item_user_announcements:
                ContainerFragments.getInstance().replaceFragmentInContainer(FragmentUserAnnouncements.getInstance());
                return true;
            case R.id.item_proposals:
                ContainerFragments.getInstance().replaceFragmentInContainer(FragmentUserProposals.getInstance());
                return true;
            case R.id.item_statistics:
                ContainerFragments.getInstance().replaceFragmentInContainer(FragmentUserStatistics.getInstance());
                return true;
            case R.id.item_favorites:
                ContainerFragments.getInstance().replaceFragmentInContainer(new FragmentUserFavorites());
                return true;
            case R.id.item_wallet:
                ContainerFragments.getInstance().replaceFragmentInContainer(new FragmentUserWallet());
                return true;
            case R.id.item_services:
                ContainerFragments.getInstance().replaceFragmentInContainer(new FragmentServices());
                return true;
            case R.id.item_regulations:
                ContainerFragments.getInstance().replaceFragmentInContainer(new FragmentRegulations());
                return true;
            case R.id.item_customer_service:
                ContainerFragments.getInstance().replaceFragmentInContainer(new FragmentCustomerService());
                return true;
            case R.id.item_prohibited:
                ContainerFragments.getInstance().replaceFragmentInContainer(new FragmentProhibited());
                return true;
        }

        return false;
    }

    @Override
    public void openRightMenu() {
        try {
            if (drawerLayout != null && !drawerLayout.isDrawerOpen(leftMenu))
                Utils.getHandler().post(() -> drawerLayout.openDrawer(rightMenu));
        } catch (Throwable e) {
            Timber.d(e);
        }
    }

    @Override
    public void closeRightMenu() {
        try {
            if (drawerLayout != null && !drawerLayout.isDrawerOpen(leftMenu))
                Utils.getHandler().post(() -> drawerLayout.closeDrawer(rightMenu));
        } catch (Throwable e) {
            Timber.d(e);
        }
    }

    @Override
    public void openLeftMenu() {
        try {
            if (drawerLayout != null && !drawerLayout.isDrawerOpen(rightMenu))
                Utils.getHandler().post(() -> drawerLayout.openDrawer(leftMenu));
        } catch (Throwable e) {
            Timber.d(e);
        }
    }

    @Override
    public void closeLeftMenu() {
        try {
            if (drawerLayout != null && !drawerLayout.isDrawerOpen(rightMenu))
                Utils.getHandler().post(() -> drawerLayout.closeDrawer(leftMenu));
        } catch (Throwable e) {
            Timber.d(e);
        }
    }
}