package com.zoom.happiestplaces.intro;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.zoom.happiestplaces.MainActivity;
import com.zoom.happiestplaces.R;
import com.zoom.happiestplaces.databinding.FragmentIntroViewPagerBinding;
import com.zoom.happiestplaces.util.AppConstants;


public class IntroViewPagerFragment extends Fragment {
    FragmentIntroViewPagerBinding mBinding;
    private int[] layouts;
    ViewPagerAdapter pagerAdapter;
    private ViewPagerAdapter viewPagerAdapter;
    private TextView[] dots;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();
        ( (MainActivity)getActivity()).getSupportActionBar().hide();
    }

    @Override
    public void onStop() {
        super.onStop();
        ( (MainActivity)getActivity()).getSupportActionBar().show();
        if(Build.VERSION.SDK_INT>=21)
        {
            getActivity().getWindow().getDecorView()
                    .setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
            getActivity().getWindow().setStatusBarColor(getResources().getColor(R.color.primaryDarkColor));

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentIntroViewPagerBinding.inflate(inflater, container, false);
        if (Build.VERSION.SDK_INT >= 21) {

            //getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }


        layouts = new int[]{R.layout.intro_slide1, R.layout.intro_slide2, R.layout.intro_slide3};
        addBottomDots(0);
        changeStatusBarColor();
        viewPagerAdapter = new ViewPagerAdapter();
        mBinding.viewPager.setAdapter(viewPagerAdapter);
        mBinding.viewPager.addOnPageChangeListener(viewListener);

        //For the next and previous buttons
/*        mBinding.btnSkip.setOnClickListener(view -> {
            // Intent i = new Intent(MainActivity.this,Home.class);
            //startActivity(i);
            // finish();
        });
*/
        mBinding.btnNext.setOnClickListener(view -> {
            getActivity().onBackPressed();

        });
        return mBinding.getRoot();
    }


    //Giving the dots functionality
    private void addBottomDots(int position) {

        dots = new TextView[layouts.length];
        int[] colorActive = getResources().getIntArray(R.array.dot_active);
        int[] colorInactive = getResources().getIntArray(R.array.dot_inactive);
        mBinding.layoutDots.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(getContext());
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(colorInactive[position]);
            mBinding.layoutDots.addView(dots[i]);
        }
        if (dots.length > 0)
            dots[position].setTextColor(colorActive[position]);
    }

    private int getItem(int i) {
        return mBinding.viewPager.getCurrentItem() + i;
    }

    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {

            addBottomDots(position);
            if (position == layouts.length - 1) {
                mBinding.btnNext.setText("Next");

            } else {
                mBinding.btnNext.setText("Skip");

            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getActivity().getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    //PagerAdapter class which will inflate our sliders in our ViewPager
    public class ViewPagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;

        @Override
        public Object instantiateItem(ViewGroup myContainer, int mPosition) {
            layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = layoutInflater.inflate(layouts[mPosition], myContainer, false);
            myContainer.addView(v);
            return v;
        }

        @Override
        public int getCount() {
            return layouts.length;
        }

        @Override
        public boolean isViewFromObject(View mView, Object mObject) {
            return mView == mObject;
        }

        @Override
        public void destroyItem(ViewGroup mContainer, int mPosition, Object mObject) {
            View v = (View) mObject;
            mContainer.removeView(v);
        }
    }
}