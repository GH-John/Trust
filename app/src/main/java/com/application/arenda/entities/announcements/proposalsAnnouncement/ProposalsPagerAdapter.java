package com.application.arenda.entities.announcements.proposalsAnnouncement;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class ProposalsPagerAdapter extends SmartFragmentStatePagerAdapter {

    private Context context;
    private List<PagerItem> items = new ArrayList<>();

    public ProposalsPagerAdapter(Context context, @NonNull FragmentManager fm, List<PagerItem> items) {
        super(fm, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);

        this.context = context;
        this.items = items;
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
        return context.getResources().getString(items.get(position).getResourceTitle());
    }
}