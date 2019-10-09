package com.application.trust.CustomComponents.Container;

import android.content.Context;
import android.util.AttributeSet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.application.trust.Patterns.Observable;
import com.application.trust.Patterns.ObserverManager;
import com.application.trust.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CustomContainer extends ConstraintLayout implements Observable {
    private Fragment containerContentDisplay;
    private List<Fragment> fragmentList;

    private ObserverManager observerManager;

    public CustomContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflateContainer(context, attrs);
    }

    private void inflateContainer(Context context, AttributeSet attrs){
        inflate(context, R.layout.custom_container, this);
        containerContentDisplay = ((AppCompatActivity)context).getSupportFragmentManager()
                .findFragmentById(R.id.containerContentDisplay);
        fragmentList = new ArrayList<>();
    }

    public void writeInConteinerFragment(int index){


        notifyObservers();
    }

    public void addFragments(Fragment... fragments){
        fragmentList.addAll(Arrays.asList(fragments));
    }

    @Override
    public void setManager(ObserverManager manager) {
        observerManager = manager;
    }

    @Override
    public void notifyObservers() {
        observerManager.notifyObservers(this);
    }
}