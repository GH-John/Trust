package com.application.arenda.mainWorkspace.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.application.arenda.databinding.FragmentUserChatBinding;

public class FragmentUserChat extends Fragment {
    private FragmentUserChatBinding bind;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        bind = FragmentUserChatBinding.inflate(inflater);

        return bind.getRoot();
    }
}