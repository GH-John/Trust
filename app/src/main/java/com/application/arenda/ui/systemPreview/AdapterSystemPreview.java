package com.application.arenda.ui.systemPreview;

import android.content.Context;
import android.graphics.Outline;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;

import com.application.arenda.R;

public class AdapterSystemPreview extends PagerAdapter {

    private Context context;
    private ImageView[] dots;
    private LayoutInflater layoutInflater;

    public AdapterSystemPreview(Context context, LinearLayout dotsLayout, Drawable dotUnselected, Drawable dotSelected) {
        this.context = context;
        createArrayDotsIndicator(getCount(), dotsLayout, dotUnselected);
        changeIndicator(0, dotUnselected, dotSelected);
    }

    @Override
    public int getCount() {
        return getArgumentsDescription().length;
    }

    private String[] getArgumentsDescription() {
        return context.getResources().getStringArray(R.array.previewDescription);
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.vp_preview_pattern, container, false);

        ImageView logo = view.findViewById(R.id.logoPreview);

        ViewOutlineProvider viewOutlineProvider = new ViewOutlineProvider() {
            @Override
            public void getOutline(View view, Outline outline) {
                outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), 100);
            }
        };
        logo.setOutlineProvider(viewOutlineProvider);
        logo.setClipToOutline(true);

        TextView description = view.findViewById(R.id.descriptionPreview);

        logo.setImageResource(R.drawable.ic_logo);
        description.setText(getArgumentsDescription()[position]);

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((ConstraintLayout) object);
    }

    public void createArrayDotsIndicator(int setDotCount, LinearLayout dotsLayout, Drawable drawable) {
        LinearLayout.LayoutParams layoutParams;
        dots = new ImageView[setDotCount];

        for (int i = 0; i < dots.length; i++) {
            dots[i] = new ImageView(context);
            dots[i].setImageDrawable(drawable);

            dotsLayout.addView(dots[i]);

            layoutParams = new LinearLayout.LayoutParams(dots[i].getLayoutParams());
            layoutParams.leftMargin = 20;

            dots[i].setLayoutParams(layoutParams);
        }
    }

    public void changeIndicator(int position, Drawable dotUnselected, Drawable dotSelected) {
        if (dots.length > 0) {
            if (position > 0) {
                dots[position - 1].setImageDrawable(dotUnselected);
                dots[position].setImageDrawable(dotSelected);

                if (position < dots.length - 1) {
                    dots[position + 1].setImageDrawable(dotUnselected);
                }
            } else if (position == 0) {
                dots[position].setImageDrawable(dotSelected);
                dots[position + 1].setImageDrawable(dotUnselected);
            }
        }
    }
}