package com.application.arenda.UI.Components.BottomNavigation;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Outline;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.application.arenda.MainWorkspace.Fragments.FragmentAllAnnouncements;
import com.application.arenda.MainWorkspace.Fragments.FragmentInsertAnnouncement;
import com.application.arenda.MainWorkspace.Fragments.FragmentUserAnnouncements;
import com.application.arenda.MainWorkspace.Fragments.FragmentUserStatistics;
import com.application.arenda.MainWorkspace.Fragments.Proposals.FragmentUserProposals;
import com.application.arenda.R;
import com.application.arenda.UI.Components.ComponentManager;
import com.application.arenda.UI.Components.ContainerFragments.ContainerFragments;
import com.application.arenda.UI.DrawPanel;

public class CustomBottomNavigation extends ConstraintLayout implements BottomNavigation, ComponentManager.Observer {
    private ImageButton
            itemUserAnnouncements,
            itemAllAnnouncements,
            itemInsertAnnouncement,
            itemUserStatistics,
            itemUserProposals;

    private ImageView
            leftPanelBN,
            rightPanelBN;

    private ContainerFragments containerFragments;

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

        containerFragments = ContainerFragments.getInstance(context);
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
        itemAllAnnouncements.setOnClickListener(v -> containerFragments
                .add(FragmentAllAnnouncements.getInstance()));

        itemUserAnnouncements.setOnClickListener(v -> containerFragments
                .add(FragmentUserAnnouncements.getInstance()));

        itemInsertAnnouncement.setOnClickListener(v -> containerFragments
                .add(FragmentInsertAnnouncement.getInstance()));

        itemUserProposals.setOnClickListener(v -> containerFragments
                .add(FragmentUserProposals.getInstance()));

        itemUserStatistics.setOnClickListener(v -> containerFragments
                .add(FragmentUserStatistics.getInstance()));
    }

    @Override
    public void update(@NonNull Object object) {
        if (object instanceof FragmentAllAnnouncements) {

            changeImageInBtn(itemAllAnnouncements);
            setVisibility(VISIBLE);

        } else if (object instanceof FragmentUserAnnouncements) {

            changeImageInBtn(itemUserAnnouncements);
            setVisibility(VISIBLE);

        } else if (object instanceof FragmentInsertAnnouncement) {

            changeImageInBtn(itemInsertAnnouncement);
            setVisibility(VISIBLE);

        } else if (object instanceof FragmentUserProposals) {

            changeImageInBtn(itemUserProposals);
            setVisibility(VISIBLE);

        } else if (object instanceof FragmentUserStatistics) {

            changeImageInBtn(itemUserStatistics);
            setVisibility(VISIBLE);

        } else {
            setVisibility(INVISIBLE);
        }
    }

    private void changeImageInBtn(ImageButton button) {
        itemAllAnnouncements.setImageResource(R.drawable.bn_item_search_announcements_unselected);
        itemUserAnnouncements.setImageResource(R.drawable.bn_item_user_announcements_unselected);
        itemInsertAnnouncement.setImageResource(R.drawable.ic_plus_white);
        itemUserProposals.setImageResource(R.drawable.bn_item_proposals_unselected);
        itemUserStatistics.setImageResource(R.drawable.bn_item_statistics_unselected);

        if (itemUserAnnouncements.equals(button)) {
            itemUserAnnouncements.setImageResource(R.drawable.bn_item_user_announcements_selected);
        } else if (itemInsertAnnouncement.equals(button)) {
            itemInsertAnnouncement.setImageResource(R.drawable.ic_plus_white_bold);
        } else if (itemUserProposals.equals(button)) {
            itemUserProposals.setImageResource(R.drawable.bn_item_proposals_selected);
        } else if (itemUserStatistics.equals(button)) {
            itemUserStatistics.setImageResource(R.drawable.bn_item_statistics_selected);
        } else {
            itemAllAnnouncements.setImageResource(R.drawable.bn_item_search_announcements_selected);
        }
    }
}