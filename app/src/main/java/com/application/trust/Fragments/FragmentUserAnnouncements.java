package com.application.trust.Fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.application.trust.CustomComponents.Container.PluginFragment;
import com.application.trust.R;

public class FragmentUserAnnouncements extends Fragment implements PluginFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_announcements, container, false);
        return view;
    }

    @Override
    public void update() {

    }
}