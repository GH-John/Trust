package com.application.arenda.entities.announcements.proposalsAnnouncement.pager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class ProposalsPagerAdapter extends FragmentStatePagerAdapter {

    private List<PagerItem> items = new ArrayList<>();

    public ProposalsPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    public void setPagerItem(Fragment fragment, String title) {
        items.add(new PagerItem() {
            @Override
            public String getTitle() {
                return title;
            }

            @Override
            public Fragment getFragment() {
                return fragment;
            }
        });
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return items.get(position).getFragment();
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return items.get(position).getTitle();
    }

    public interface PagerItem {
        String getTitle();

        Fragment getFragment();
    }
}