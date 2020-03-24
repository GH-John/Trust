package com.application.arenda.MainWorkspace.Fragments.Proposals;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.application.arenda.Entities.Announcements.ProposalsAnnouncement.PagerItem;
import com.application.arenda.R;

public class FragmentOutgoingProposals extends Fragment implements PagerItem {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_outgoing_proposals, container, false);
    }

    @Override
    public Integer getResourceTitle() {
        return R.string.title_pager_outgoing_proposals;
    }

    @Override
    public Fragment getFragment() {
        return this;
    }
}