package com.application.arenda.mainWorkspace.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.application.arenda.databinding.FragmentUserMessagesBinding;

public class FragmentUserMessages extends Fragment {
    private FragmentUserMessagesBinding bind;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        bind = FragmentUserMessagesBinding.inflate(inflater);

        return bind.getRoot();
    }
}