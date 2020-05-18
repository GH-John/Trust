package com.application.arenda.entities.announcements.viewAnnouncement;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.application.arenda.R;
import com.github.piasy.biv.indicator.progresspie.ProgressPieIndicator;
import com.github.piasy.biv.view.BigImageView;

import java.util.List;

public class AdapterPagerBigImageView extends PagerAdapter {
    private List<Uri> uriList;

    private OnClickListener listener;

    public AdapterPagerBigImageView(List<Uri> uriList) {
        this.uriList = uriList;
    }

    public void setImageClickListener(OnClickListener listener) {
        this.listener = listener;
    }

    @Override
    public int getCount() {
        return uriList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.vp_big_image_views_pattern, container, false);

        BigImageView imgContainer = view.findViewById(R.id.vpBigImageView);
        imgContainer.setProgressIndicator(new ProgressPieIndicator());

        if (listener != null)
            imgContainer.setOnClickListener(listener);

        imgContainer.showImage(uriList.get(position));

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}