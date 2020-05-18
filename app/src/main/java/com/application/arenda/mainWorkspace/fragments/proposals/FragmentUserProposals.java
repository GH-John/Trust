package com.application.arenda.mainWorkspace.fragments.proposals;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.application.arenda.entities.announcements.proposalsAnnouncement.PagerItem;
import com.application.arenda.entities.announcements.proposalsAnnouncement.ProposalsPagerAdapter;
import com.application.arenda.R;
import com.application.arenda.ui.widgets.actionBar.AdapterActionBar;
import com.application.arenda.ui.widgets.sideBar.ItemSideBar;
import com.application.arenda.ui.widgets.sideBar.SideBar;
import com.gigamole.navigationtabstrip.NavigationTabStrip;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public final class FragmentUserProposals extends Fragment implements AdapterActionBar, ItemSideBar {
    @SuppressLint("StaticFieldLeak")
    private static FragmentUserProposals fragmentUserProposals;

    @Nullable
    @BindView(R.id.pagerProposals)
    ViewPager viewPager;

    @Nullable
    @BindView(R.id.pagerTabLayout)
    NavigationTabStrip tabStrip;

    private Unbinder unbinder;
    private PagerAdapter pagerAdapter;

    private SideBar sideBar;
    private ImageView itemBurgerMenu;

    public static FragmentUserProposals getInstance() {
        if (fragmentUserProposals == null)
            fragmentUserProposals = new FragmentUserProposals();

        return fragmentUserProposals;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_proposals, container, false);

        unbinder = ButterKnife.bind(this, view);

        initComponents();

        return view;
    }

    private void initComponents() {
        List<PagerItem> pagerItems = new ArrayList<>();

        pagerItems.add(new FragmentIncomingProposals());
        pagerItems.add(new FragmentOutgoingProposals());

        pagerAdapter = new ProposalsPagerAdapter(getContext(), getActivity().getSupportFragmentManager(), pagerItems);
        viewPager.setAdapter(pagerAdapter);

        tabStrip.setTitles(getString(pagerItems.get(0).getResourceTitle()), getString(pagerItems.get(1).getResourceTitle()));
        tabStrip.setViewPager(viewPager, 0);
    }

    @Override
    public int getIdPatternResource() {
        return R.layout.ab_pattern_user_proposals;
    }

    @Override
    public void initComponentsActionBar(ViewGroup viewGroup) {
        itemBurgerMenu = viewGroup.findViewById(R.id.itemBurgerMenu);
    }

    @Override
    public void initListenersActionBar(ViewGroup viewGroup) {
        itemBurgerMenu.setOnClickListener(v -> sideBar.openLeftMenu());
    }

    @Override
    public void setSideBar(SideBar sideBar) {
        this.sideBar = sideBar;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}