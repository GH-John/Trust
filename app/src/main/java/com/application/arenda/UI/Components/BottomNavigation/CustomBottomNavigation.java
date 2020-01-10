package com.application.arenda.UI.Components.BottomNavigation;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Outline;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.ImageView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.application.arenda.MainWorkspace.Fragments.FragmentAllAnnouncements;
import com.application.arenda.MainWorkspace.Fragments.FragmentInsertAnnouncement;
import com.application.arenda.MainWorkspace.Fragments.FragmentUserAnnouncements;
import com.application.arenda.MainWorkspace.Fragments.FragmentUserProposals;
import com.application.arenda.MainWorkspace.Fragments.FragmentUserStatistics;
import com.application.arenda.R;
import com.application.arenda.UI.Components.ComponentManager;
import com.application.arenda.UI.Components.ContainerFragments.ContainerFragments;
import com.application.arenda.UI.DrawPanel;

public class CustomBottomNavigation extends ConstraintLayout implements BottomNavigation, ComponentManager.Observable {
    private ImageView
            itemUserAnnouncements,
            itemAllAnnouncements,
            itemInsertAnnouncement,
            itemUserStatistics,
            itemUserProposals,
            leftPanelBN,
            rightPanelBN;

    public CustomBottomNavigation(Context context, AttributeSet attrs) {
        super(context, attrs);
        initializationComponents(context, attrs);
        stylePanel(new PanelBottomNavigation(context,
                R.color.colorWhite, R.color.shadowColor,
                10f, 0f, 0f, 0f), leftPanelBN);

        styleItems();
        itemListener();
    }

    private void initializationComponents(Context context, AttributeSet attrs) {
        inflate(context, R.layout.bn_bottom_navigation, this);

        itemUserAnnouncements = findViewById(R.id.itemUserAnnouncement);
        leftPanelBN = findViewById(R.id.leftPanelBN);
        rightPanelBN = findViewById(R.id.rightPanelBN);
        itemAllAnnouncements = findViewById(R.id.itemAllAnnouncement);
        itemInsertAnnouncement = findViewById(R.id.itemAddAnnouncement);
        itemUserStatistics = findViewById(R.id.itemUserStatistics);
        itemUserProposals = findViewById(R.id.itemUserProposals);
    }

    @SuppressLint({"ResourceAsColor"})
    @Override
    public void stylePanel(DrawPanel drawPanel, View view) {
        ((ImageView) view).setImageDrawable((Drawable) drawPanel);
        rightPanelBN.setImageDrawable(((ImageView) view).getDrawable());
        rightPanelBN.setRotation(180f);
    }

    @SuppressLint({"ResourceAsColor"})
    @Override
    public void styleItems() {
        ViewOutlineProvider viewOutlineProvider = new ViewOutlineProvider() {
            @Override
            public void getOutline(View view, Outline outline) {
                outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), 90);
            }
        };

        itemInsertAnnouncement.setOutlineProvider(viewOutlineProvider);
        itemInsertAnnouncement.setClipToOutline(true);
    }

    @Override
    public void itemListener() {
        itemAllAnnouncements.setOnClickListener(v -> ContainerFragments.getInstance()
                .replaceFragmentInContainer(FragmentAllAnnouncements.getInstance()));

        itemUserAnnouncements.setOnClickListener(v -> ContainerFragments.getInstance()
                .replaceFragmentInContainer(FragmentUserAnnouncements.getInstance()));

        itemInsertAnnouncement.setOnClickListener(v -> ContainerFragments.getInstance()
                .replaceFragmentInContainer(FragmentInsertAnnouncement.getInstance()));

        itemUserProposals.setOnClickListener(v -> ContainerFragments.getInstance()
                .replaceFragmentInContainer(FragmentUserProposals.getInstance()));

        itemUserStatistics.setOnClickListener(v -> ContainerFragments.getInstance()
                .replaceFragmentInContainer(FragmentUserStatistics.getInstance()));
    }

    @Override
    public void notifyObservers(Object object) {
        ComponentManager.notifyObservers(this, (Fragment) object);
    }
}