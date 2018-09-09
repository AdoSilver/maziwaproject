package com.lastmilesale.android.mobileapps.lastmile.Activities.GetStarted;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lastmilesale.android.mobileapps.lastmile.Activities.Authentication.Login;
import com.lastmilesale.android.mobileapps.lastmile.R;

import java.util.ArrayList;

import me.relex.circleindicator.CircleIndicator;


public class SliderViewPagerAdapter extends PagerAdapter {
    private ArrayList<Integer> images;
    private ArrayList<String> categories;
    private ArrayList<String> descriptions;
    private LayoutInflater inflater;
    private Context context;
    View rootView;

    public SliderViewPagerAdapter(Context context,View rootView, ArrayList<Integer> images, ArrayList<String> categories,ArrayList<String> descriptions) {
        this.context = context;
        this.rootView = rootView;
        this.images=images;
        this.categories = categories;
        this.descriptions = descriptions;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {

        try {
            View frameLayout = inflater.inflate(R.layout.pitch_slider_layout, view, false);

            //CALLING OF SLIDER FRAME & ITS COMPONENTS
            RelativeLayout normalSlider = (RelativeLayout) frameLayout.findViewById(R.id.normal_slider);
            final Button continueBtn = (Button) frameLayout.findViewById(R.id.slider_continue_btn);
            Button skipBtn = (Button) frameLayout.findViewById(R.id.slider_skip_btn);
            ImageView myImage = (ImageView) frameLayout.findViewById(R.id.slider_welcome_image);
            TextView categoryText = (TextView) frameLayout.findViewById(R.id.sliderCategoryTextView);
            TextView descriptionText = (TextView) frameLayout.findViewById(R.id.sliderDescriptionTextView);

            CircleIndicator indicator = (CircleIndicator) rootView.findViewById(R.id.slider_circle_indicator);

            //FEEDING DATA TO SLIDER FRAME COMPONENTS
            categoryText.setText(categories.get(position));
            descriptionText.setText(descriptions.get(position));
            //some vital image processing methods for memory optimization
            myImage.setImageBitmap(
                    decodeSampledBitmapFromResource(context.getResources(), images.get(position), 384, 384));

            if(position == 2){
                continueBtn.setVisibility(View.VISIBLE);
                skipBtn.setVisibility(View.INVISIBLE);
            }else{
                continueBtn.setVisibility(View.INVISIBLE);
                skipBtn.setVisibility(View.VISIBLE);
            }



            continueBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, Login.class);
                    context.startActivity(intent);
                }
            });

            skipBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    continueBtn.performClick();
                }
            });

            view.addView(frameLayout, 0);
            return frameLayout;

        }catch (OutOfMemoryError ie){
            ie.printStackTrace();
            Intent intent = new Intent(context,Login.class);
            context.startActivity(intent);
            return  null;
        }
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // INITIALING THE DIMENSIONS OF THE IMAGE IN PROCESS
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1 ;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // CALCULATING LARGEST inSampleSize VALUE THAT IS OF POWER OF 2 AND KEEPING
            // HEIGHT AND WIDTH LARGER THAN THE REQUESTED_HEIGHT AND REQUESTED_WIDTH
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                         int reqWidth, int reqHeight) {

        // DECODING IMAGE WITH inJustDecodeBounds=true TO CHECK THE DIMENSIONS OF THE IMAGE
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // CALCULATING inSampleSize, FOR IMAGE RESIZING
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // DECODING BITMAP WITH CALCULATED inSampleSize SET
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }
}
