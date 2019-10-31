package com.application.trust.PreviewApp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;

import com.application.trust.R;

public class AdapterPreview extends PagerAdapter {

    private Context context;
    private LayoutInflater layoutInflater;

    public AdapterPreview(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return getArgumentsDescription().length;
    }

    private String[] getArgumentsDescription(){
        return context.getResources().getStringArray(R.array.previewDescription);
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (ConstraintLayout)object;
    }

    private int gerImage(){
        return R.id.logoPreview;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.preview_pattern, container, false);

        ImageView logo = view.findViewById(R.id.logoPreview);
        TextView description = view.findViewById(R.id.descriptionPreview);

        logo.setImageResource(R.drawable.ic_logo);
        description.setText(getArgumentsDescription()[position]);

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((ConstraintLayout)object);
    }
}