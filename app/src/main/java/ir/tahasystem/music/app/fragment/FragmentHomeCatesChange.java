package ir.tahasystem.music.app.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import ir.onlinefood.app.factory.R;
import ir.tahasystem.music.app.CatesChangeActivity;
import ir.tahasystem.music.app.custom.CustomViewPager;

/**
 * Created by BabakPadashi on 4/26/2016.
 */
public class FragmentHomeCatesChange extends Fragment {

    public final static String ITEMS_COUNT_KEY = "PartThreeFragment$ItemsCount";
    public static final String TAG = FragmentHomeCatesChange.class.getCanonicalName();


    public static FragmentHomeCatesChange context;

    public static CustomViewPager viewPager;

    public static FragmentHomeCatesChange createInstance(int itemsCount) {

        FragmentHomeCatesChange f = new FragmentHomeCatesChange();

        Bundle bdl = new Bundle(1);
        bdl.putString("EXTRA_MESSAGE", TAG);
        f.setArguments(bdl);

        return f;

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        this.context = this;

        View view = (View) inflater.inflate(R.layout.fragment_home, container, false);

        viewPager = (CustomViewPager) view.findViewById(R.id.viewPager);

        return view;
    }

    public void onResume() {
        super.onResume();

        init();

    }

    boolean isInit;

    public void init() {

        if (isInit)
            return;
        isInit = true;

        initViewPagerAndTabs();

    }

    public FragmentSubCates fragmentSubCates;
    public FragmentChange fragmentProduct;

    private void initViewPagerAndTabs() {


        fragmentSubCates = FragmentSubCates.createInstance(35);
        fragmentProduct = FragmentChange.createInstance(36);

        PagerAdapter pagerAdapter = new PagerAdapter(getChildFragmentManager());

        pagerAdapter.addFragment(fragmentSubCates, getString(R.string.last));
        pagerAdapter.addFragment(fragmentProduct, getString(R.string.last));

        viewPager.setAdapter(pagerAdapter);
        viewPager.setOffscreenPageLimit(3);
        viewPager.setPagingEnabled(true);

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int state) {
            }

            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            public void onPageSelected(int position) {



                switch (position) {
                    case 0:
                       if(CatesChangeActivity.context!=null)
                        CatesChangeActivity.subTitle.setText(getString(R.string.subcates));
                        break;
                    case 1:
                        if (CatesChangeActivity.context != null)
                        CatesChangeActivity.subTitle.setText(getString(R.string.products));
                        fragmentProduct.isInit=false;
                        fragmentProduct.init();
                        break;
                }
            }
        });

        viewPager.setPagingEnabled(false);
       // fragmentCates.init();

    }

    static class PagerAdapter extends FragmentPagerAdapter {

        private final List<Fragment> fragmentList = new ArrayList();
        private final List<String> fragmentTitleList = new ArrayList();

        public PagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        public void addFragment(Fragment fragment, String title) {
            fragmentList.add(fragment);
            fragmentTitleList.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentTitleList.get(position);
        }
    }


}
