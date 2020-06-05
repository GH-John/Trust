package com.application.arenda.ui.widgets.sideBar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.drawerlayout.widget.DrawerLayout;

import com.application.arenda.R;
import com.application.arenda.entities.models.ModelUser;
import com.application.arenda.entities.room.LocalCacheManager;
import com.application.arenda.entities.utils.Utils;
import com.application.arenda.entities.utils.glide.GlideUtils;
import com.application.arenda.mainWorkspace.activities.ActivityAuthorization;
import com.application.arenda.mainWorkspace.fragments.FragmentAllAnnouncements;
import com.application.arenda.mainWorkspace.fragments.FragmentUserAnnouncements;
import com.application.arenda.mainWorkspace.fragments.FragmentUserChats;
import com.application.arenda.mainWorkspace.fragments.FragmentUserFavoriteAnnouncements;
import com.application.arenda.mainWorkspace.fragments.FragmentUserProfile;
import com.application.arenda.mainWorkspace.fragments.FragmentUserStatistics;
import com.application.arenda.mainWorkspace.fragments.proposals.FragmentUserProposals;
import com.application.arenda.ui.widgets.ComponentManager;
import com.application.arenda.ui.widgets.containerFragments.ContainerFragments;
import com.google.android.material.navigation.NavigationView;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.ResourceCompletableObserver;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public final class ContainerDrawerLayout implements SideBar,
        NavigationView.OnNavigationItemSelectedListener, ComponentManager.Observer {

    @SuppressLint("StaticFieldLeak")
    private static ContainerDrawerLayout containerDrawerLayout;

    private DrawerLayout drawerLayout;

    private NavigationView leftMenu;

    private View headerNavigation;

    private TextView itemUserName, itemUserLogin;

    private FrameLayout rightMenu;

    private Button itemLogout, itemSignIn;

    private ImageButton itemSettings;

    private ImageView itemUserAvatar;

    private ContainerFragments containerFragments;

    private ContainerDrawerLayout(Activity activity) {
        initComponents(activity);

        inflateComponents(activity);

        initListeners();
    }

    public static ContainerDrawerLayout getInstance(Activity activity) {
        if (containerDrawerLayout == null)
            containerDrawerLayout = new ContainerDrawerLayout(activity);

        return containerDrawerLayout;
    }

    private void initComponents(Activity activity) {
        leftMenu = activity.findViewById(R.id.sbLeftMenu);
        headerNavigation = leftMenu.getHeaderView(0);

        drawerLayout = activity.findViewById(R.id.sbDrawerMenu);
        rightMenu = activity.findViewById(R.id.sbRightMenuLayout);

        itemLogout = headerNavigation.findViewById(R.id.itemLogout);
        itemSignIn = headerNavigation.findViewById(R.id.itemSignIn);
        itemSettings = headerNavigation.findViewById(R.id.itemBtnSettings);
        itemUserName = headerNavigation.findViewById(R.id.itemUserName);
        itemUserLogin = headerNavigation.findViewById(R.id.itemUserLogin);
        itemUserAvatar = headerNavigation.findViewById(R.id.itemUserAvatar);

        containerFragments = ContainerFragments.getInstance(activity);

        drawerLayout.setScrimColor(activity.getResources().getColor(R.color.shadowColor));
    }

    @SuppressLint({"SetTextI18n", "CheckResult"})
    private void inflateComponents(Context context) {
        LocalCacheManager.getInstance(context)
                .users()
                .getActiveUser()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(modelUsers -> {
                    if (modelUsers.size() > 0) {
                        ModelUser user = modelUsers.get(0);

                        itemUserName.setText(user.getLastName() + " " + user.getName());
                        itemUserLogin.setText(user.getLogin());
                        GlideUtils.loadAvatar(context, user.getAvatar(), itemUserAvatar, 200, 200);

                        itemSignIn.setVisibility(View.GONE);

                        itemLogout.setVisibility(View.VISIBLE);
                        itemUserName.setVisibility(View.VISIBLE);
                        itemUserLogin.setVisibility(View.VISIBLE);
                        itemUserAvatar.setVisibility(View.VISIBLE);
                    } else {
                        itemSignIn.setVisibility(View.VISIBLE);

                        itemLogout.setVisibility(View.GONE);
                        itemUserName.setVisibility(View.GONE);
                        itemUserLogin.setVisibility(View.GONE);
                        itemUserAvatar.setVisibility(View.GONE);
                    }
                });
    }

    private void initListeners() {
        leftMenu.setNavigationItemSelectedListener(this);

        itemSignIn.setOnClickListener(this::openActivityAuthorization);

        itemLogout.setOnClickListener(this::logoutUser);

        itemUserAvatar.setOnClickListener(v -> openProfile());
    }

    private void openProfile() {
        containerFragments.open(FragmentUserProfile.Companion.getInstance());
    }

    private void openActivityAuthorization(View view) {
        view.getContext().startActivity(new Intent(view.getContext(), ActivityAuthorization.class));
    }

    @SuppressLint("CheckResult")
    private void logoutUser(View view) {
        LocalCacheManager.getInstance(view.getContext())
                .users()
                .logoutCurrentUser()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResourceCompletableObserver() {
                    @Override
                    public void onComplete() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e);
                    }
                });
    }

    private void setCheckedItem(ItemSideBar item) {
        if (item instanceof FragmentAllAnnouncements) {
            leftMenu.getMenu().getItem(0).setChecked(true);
        } else if (item instanceof FragmentUserAnnouncements) {
            leftMenu.getMenu().getItem(1).setChecked(true);
        } else if (item instanceof FragmentUserProposals) {
            leftMenu.getMenu().getItem(2).setChecked(true);
        } else if (item instanceof FragmentUserChats) {
            leftMenu.getMenu().getItem(3).setChecked(true);
        } else if (item instanceof FragmentUserStatistics) {
            leftMenu.getMenu().getItem(4).setChecked(true);
        } else if (item instanceof FragmentUserFavoriteAnnouncements) {
            leftMenu.getMenu().getItem(5).setChecked(true);
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
                containerFragments.open(FragmentAllAnnouncements.getInstance());
                return true;
            case R.id.item_user_announcements:
                containerFragments.open(FragmentUserAnnouncements.getInstance());
                return true;
            case R.id.item_proposals:
                containerFragments.open(FragmentUserProposals.Companion.getInstance());
                return true;
            case R.id.item_messages:
                containerFragments.open(FragmentUserChats.Companion.getInstance());
                return true;
            case R.id.item_statistics:
                containerFragments.open(FragmentUserStatistics.getInstance());
                return true;
            case R.id.item_favorites:
                containerFragments.open(FragmentUserFavoriteAnnouncements.Companion.getInstance());
                return true;
//            case R.id.item_wallet:
//                containerFragments.open(new FragmentUserWallet());
//                return true;
//            case R.id.item_services:
//                containerFragments.open(new FragmentServices());
//                return true;
//            case R.id.item_regulations:
//                containerFragments.open(new FragmentRegulations());
//                return true;
//            case R.id.item_customer_service:
//                containerFragments.open(new FragmentCustomerService());
//                return true;
//            case R.id.item_prohibited:
//                containerFragments.open(new FragmentProhibited());
//                return true;
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