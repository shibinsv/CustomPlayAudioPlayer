package com.example.audioplayer.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.audioplayer.R;

public class AppUtils {

    private static AppUtils appUtils = null;


    public static AppUtils getInstance() {
        if (appUtils == null) {
            return new AppUtils();
        } else {
            return appUtils;
        }
    }

public void ImageAnimation(Context context, ImageView imageView, Bitmap bitmap){
    Animation animOut = AnimationUtils.loadAnimation(context,android.R.anim.fade_out);
    Animation animIn = AnimationUtils.loadAnimation(context,android.R.anim.fade_in);
    animOut.setAnimationListener(new Animation.AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            Glide.with(context).load(bitmap).into(imageView);
            animIn.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {

                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            imageView.startAnimation(animIn);

        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    });
    imageView.startAnimation(animOut);

}

}
