package com.application.arenda.UI.Components.SideBar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.application.arenda.MainWorkspace.Fragments.FragmentAllAnnouncements;
import com.application.arenda.MainWorkspace.Fragments.FragmentUserAnnouncements;
import com.application.arenda.MainWorkspace.Fragments.FragmentUserProposals;
import com.application.arenda.MainWorkspace.Fragments.FragmentUserStatistics;
import com.application.arenda.R;
import com.application.arenda.UI.Components.ComponentManager;
import com.application.arenda.UI.Components.ContainerFragments.ContainerFragments;
import com.google.android.material.navigation.NavigationView;

import butterknife.ButterKnife;
import timber.log.Timber;

public final class ContainerDrawerLayout implements SideBar,
        NavigationView.OnNavigationItemSelectedListener,
        DrawerLayout.DrawerListener, ComponentManager.Observer {

    @SuppressLint("StaticFieldLeak")
    private static ContainerDrawerLayout containerDrawerLayout;

    //    @Nullable
//    @BindView(R.id.containerDrawerLayout)
    DrawerLayout drawerLayout;
    //    @Nullable
//    @BindView(R.id.sbNavigationView)
    NavigationView navigationView;
    //    @Nullable
//    @BindView(R.id.itemUserAccount)
    ImageView itemUserAccount;

    private ContainerDrawerLayout(Activity activity) {
        ButterKnife.bind(activity);

        initComponents(activity);

        initListeners();
    }

    public static ContainerDrawerLayout getInstance(Activity activity) {
        if (containerDrawerLayout == null)
            containerDrawerLayout = new ContainerDrawerLayout(activity);

        return containerDrawerLayout;
    }

    private void initComponents(Activity activity) {
        drawerLayout = activity.findViewById(R.id.containerDrawerLayout);
        navigationView = activity.findViewById(R.id.sbNavigationView);
        itemUserAccount = activity.findViewById(R.id.itemUserAccount);
    }

    private void initListeners() {
        drawerLayout.addDrawerListener(this);

        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void update(@NonNull Object object) {
        close();
        if (object instanceof AdapterSideBar) {
            ((AdapterSideBar) object).setSideBar(this);
        }
    }

    @Override
    public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

    }

    @Override
    public void onDrawerOpened(@NonNull View drawerView) {

    }

    @Override
    public void onDrawerClosed(@NonNull View drawerView) {

    }

    @Override
    public void onDrawerStateChanged(int newState) {

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
    public void open() {
        try {
            if (drawerLayout != null)
                drawerLayout.openDrawer(GravityCompat.START);
        } catch (Throwable e) {
            Timber.d(e);
        }
    }

    @Override
    public void close() {
        try {
            if (drawerLayout != null)
                drawerLayout.closeDrawer(GravityCompat.START);
        } catch (Throwable e) {
            Timber.d(e);
        }
    }
}