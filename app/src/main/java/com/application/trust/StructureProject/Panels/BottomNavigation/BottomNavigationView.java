package com.application.trust.StructureProject.Panels.BottomNavigation;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Outline;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.application.trust.Fragments.FragmentAddAnnouncement;
import com.application.trust.Fragments.FragmentAllAnnouncements;
import com.application.trust.Fragments.FragmentUserAnnouncements;
import com.application.trust.Fragments.FragmentUserProposals;
import com.application.trust.Fragments.FragmentUserStatistics;
import com.application.trust.R;
import com.application.trust.StructureProject.Panels.DrawPanel;

public class BottomNavigationView extends ConstraintLayout implements IBottomNavigation {
    private ImageView panelBottomNavigation,
                      itemAddAnnouncement,
                      itemAllAnnouncements,
                      itemUserAnnouncements,
                      itemUserProposals,
                      itemUserStatistics;

    private Fragment containerContentDisplay;

    public BottomNavigationView(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflateBottomNavigation(context, attrs);
        stylePanelBottomNavigation
                (context, new PanelBottomNavigation(context,
                        R.color.colorWhite, new float[8], R.color.shadowColor, 10f,0f,0f));
        styleItemBottomNavigation(context);
    }

    private void inflateBottomNavigation(Context context, AttributeSet attrs){
        inflate(context, R.layout.bottom_navigation, this);
        panelBottomNavigation = findViewById(R.id.panelBottomNavigation);
        itemAddAnnouncement = findViewById(R.id.itemAddAnnouncement);
        itemAllAnnouncements = findViewById(R.id.itemAllAnnouncement);
        itemUserAnnouncements = findViewById(R.id.itemUserAnnouncement);
        itemUserProposals = findViewById(R.id.itemUserProposals);
        itemUserStatistics = findViewById(R.id.itemUserStatistics);
    }

    @SuppressLint({"NewApi", "ResourceAsColor"})
    @Override
    public void stylePanelBottomNavigation(Context context, DrawPanel drawPanel) {
        this.panelBottomNavigation.setImageDrawable((Drawable)drawPanel);
    }

    @Override
    public void startListenerBottomNavigation(final AppCompatActivity activity, final int idContainerContent) {
        itemAddAnnouncement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!(activity.getSupportFragmentManager().findFragmentById(idContainerContent)
                        instanceof FragmentAddAnnouncement))
                    changeFragmentContainer(activity, new FragmentAddAnnouncement(), idContainerContent);
            }
        });

        itemAllAnnouncements.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!(activity.getSupportFragmentManager().findFragmentById(idContainerContent)
                        instanceof FragmentAllAnnouncements))
                    changeFragmentContainer(activity, new FragmentAllAnnouncements(), idContainerContent);
            }
        });

        itemUserAnnouncements.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!(activity.getSupportFragmentManager().findFragmentById(idContainerContent)
                        instanceof FragmentUserAnnouncements))
                    changeFragmentContainer(activity, new FragmentUserAnnouncements(), idContainerContent);
            }
        });

        itemUserProposals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!(activity.getSupportFragmentManager().findFragmentById(idContainerContent)
                        instanceof FragmentUserProposals))
                    changeFragmentContainer(activity, new FragmentUserProposals(), idContainerContent);
            }
        });

        itemUserStatistics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!(activity.getSupportFragmentManager().findFragmentById(idContainerContent)
                        instanceof FragmentUserStatistics))
                    changeFragmentContainer(activity, new FragmentUserStatistics(), idContainerContent);
            }
        });
    }

    private void changeFragmentContainer(AppCompatActivity activity, Fragment fragment, int idContainerContent){
        activity.getSupportFragmentManager()
                .beginTransaction()
                .replace(idContainerContent, fragment)
                .addToBackStack(String.valueOf(fragment.getClass()))
                .commit();
    }

    @SuppressLint({"NewApi", "ResourceAsColor"})
    @Override
    public void styleItemBottomNavigation(Context context) {
        ViewOutlineProvider viewOutlineProvider = new ViewOutlineProvider() {
            @Override
            public void getOutline(View view, Outline outline) {
                outline.setRoundRect(0,0, view.getWidth(), view.getHeight(), 90);
            }
        };

        itemAddAnnouncement.setOutlineProvider(viewOutlineProvider);
        itemAddAnnouncement.setClipToOutline(true);
    }

    @Override
    public void styleHideBottomNavigation(Context context) {

    }
}