package com.application.arenda.UI.Components.SideBar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Handler;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.drawerlayout.widget.DrawerLayout;

import com.application.arenda.MainWorkspace.Fragments.FragmentAllAnnouncements;
import com.application.arenda.MainWorkspace.Fragments.FragmentUserAnnouncements;
import com.application.arenda.MainWorkspace.Fragments.FragmentUserProposals;
import com.application.arenda.MainWorkspace.Fragments.FragmentUserStatistics;
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

    private ImageView itemUserAccount;

    private Handler handler = new Handler();

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

        itemUserAccount = activity.findViewById(R.id.itemUserAccount);
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
                break;
            case R.id.item_wallet:
                break;
            case R.id.item_services:
                break;
            case R.id.item_regulations:
                break;
            case R.id.item_customer_service:
                break;
            case R.id.item_prohibited:
                break;
        }

        return false;
    }

    @Override
    public void openRightMenu() {
        try {
            if (drawerLayout != null && !drawerLayout.isDrawerOpen(leftMenu))
                handler.post(() -> drawerLayout.openDrawer(rightMenu));
        } catch (Throwable e) {
            Timber.d(e);
        }
    }

    @Override
    public void closeRightMenu() {
        try {
            if (drawerLayout != null && !drawerLayout.isDrawerOpen(leftMenu))
                handler.post(() -> drawerLayout.closeDrawer(rightMenu));
        } catch (Throwable e) {
            Timber.d(e);
        }
    }

    @Override
    public void openLeftMenu() {
        try {
            if (drawerLayout != null && !drawerLayout.isDrawerOpen(rightMenu))
                handler.post(() -> drawerLayout.openDrawer(leftMenu));
        } catch (Throwable e) {
            Timber.d(e);
        }
    }

    @Override
    public void closeLeftMenu() {
        try {
            if (drawerLayout != null && !drawerLayout.isDrawerOpen(rightMenu))
                handler.post(() -> drawerLayout.closeDrawer(leftMenu));
        } catch (Throwable e) {
            Timber.d(e);
        }
    }
}