package com.application.trust.StructureProject.ButtonsNavigation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.application.trust.R;

import java.util.Objects;

import static android.graphics.Paint.Style.FILL;

public class FragmentButtonsNavigation extends Fragment {
    ImageView buttonsNavigation;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_buttons_navigation, container, false);

        buttonsNavigation = view.findViewById(R.id.buttonsNavigation);

        buttonsNavigation.setImageDrawable
                (new Panels(Objects.requireNonNull(getActivity()), R.color.colorWhite, FILL));

        return view;
    }
}