package ir.tahasystem.music.app;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import ir.onlinefood.app.factory.R;
import ir.tahasystem.music.app.Model.BasketModel;
import ir.tahasystem.music.app.Model.ModelHolder;
import ir.tahasystem.music.app.custom.CustomViewPager;
import ir.tahasystem.music.app.fragment.FragmentBasket;
import ir.tahasystem.music.app.fragment.FragmentBasket2;
import ir.tahasystem.music.app.fragment.FragmentBasket3;
import ir.tahasystem.music.app.fragment.FragmentBasket4;
import ir.tahasystem.music.app.utils.TabLayoutUtils;


public class BasketActivity extends AppCompatActivity {

    Toolbar mToolbar;

    public static BasketActivity context;
    TabLayout aTabLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        context = this;

        setLang();
        setTheme(R.style.AppThemeBlue);
        super.onCreate(savedInstanceState);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.basket_buy);

        viewPager = (CustomViewPager) findViewById(R.id.viewPager);

        aTabLayout = (TabLayout) findViewById(R.id.tabs);

        initToolbar();


            initViewPagerAndTabs();

    }


    public void setLang() {

        Locale locale = new Locale("en");
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());

    }

    TextView atxt;

    private void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        mToolbar.setTitleTextColor(getResources().getColor(android.R.color.white));

        setTitle("");
        atxt = (TextView) mToolbar.findViewById(R.id.toolbar_title);
        atxt.setText(getString(R.string.purches));




/*

        View count = mCustomView.findViewById(R.id.action_delete);
        count.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {


                List<BasketModel> aList = new ArrayList<BasketModel>();

                for (BasketModel aBasketModel : ModelHolder.getInstance().getBasket())
                    if (aBasketModel.aCheckBoxName.isChecked()) {
                        aList.add(aBasketModel);
                    }

                for (BasketModel aBasketModel : aList)
                    ModelHolder.getInstance().removeBasket(aBasketModel);

                FragmentBasket.context.itemHolder.removeAllViews();
                FragmentBasket.context.initBasketItem();
                FragmentBasket.context.aCheckBoxAll.setChecked(false);
                FragmentBasket.context.aCheckBoxAll
                        .setText(getString(R.string.select_all) + " (" + ModelHolder.getInstance().getBasketCount() + ") ");
                FragmentBasket.context.sumFinal();

            }
        });*/


    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        // mDrawerToggle.syncState();

    }


    public void onConfigurationChanged(Configuration newConfig) {

        super.onConfigurationChanged(newConfig);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.back_item, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {


        if (menuItem.getItemId() == android.R.id.home) {
            finish();
        } else if (menuItem.getItemId() == R.id.action_back) {
            finish();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    public void onDestroy() {
        super.onDestroy();
        for (BasketModel aBasketModel : ModelHolder.getInstance().getBasket())
            aBasketModel.count = 0;

    }

    public CustomViewPager viewPager;

    public FragmentBasket fragmentBasket;
    public FragmentBasket2 fragmentBasket2;
    public FragmentBasket3 fragmentBasket3;
    public FragmentBasket4 fragmentBasket4;

    private void initViewPagerAndTabs() {


        fragmentBasket = FragmentBasket.createInstance(34);
        fragmentBasket2 = FragmentBasket2.createInstance(35);
        fragmentBasket3 = FragmentBasket3.createInstance(36);
        fragmentBasket4 = FragmentBasket4.createInstance(36);

        PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager());


        aTabLayout.addTab(aTabLayout.newTab().setText(getString(R.string.purches)));
        aTabLayout.addTab(aTabLayout.newTab().setText(getString(R.string.choose_date_time)));
        aTabLayout.addTab(aTabLayout.newTab().setText(getString(R.string.buyer_info)));
        aTabLayout.addTab(aTabLayout.newTab().setText(getString(R.string.submit_and_send)));


        pagerAdapter.addFragment(fragmentBasket, getString(R.string.last));
        pagerAdapter.addFragment(fragmentBasket2, getString(R.string.last));
        pagerAdapter.addFragment(fragmentBasket3, getString(R.string.last));
        pagerAdapter.addFragment(fragmentBasket4, getString(R.string.last));


        aTabLayout.getTabAt(0).setCustomView
                (getCustomView(getString(R.string.purches), getString(R.string.basket_buys)));

        aTabLayout.getTabAt(1).setCustomView
                (getCustomView(getString(R.string.choose_date_time), getString(R.string.basket_dates)));

        aTabLayout.getTabAt(2).setCustomView
                (getCustomView(getString(R.string.buyer_info), getString(R.string.basket_pre_submit)));

        aTabLayout.getTabAt(3).setCustomView
                (getCustomView(getString(R.string.submit_and_send), getString(R.string.basket_submit)));


        viewPager.setAdapter(pagerAdapter);

        aTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        aTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        TabLayoutUtils.enableTabs(aTabLayout, false);

        // aTabLayout.setupWithViewPager(viewPager);

        viewPager.setOffscreenPageLimit(7);
        viewPager.setPagingEnabled(true);

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int state) {
            }

            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            public void onPageSelected(int position) {

                aTabLayout.getTabAt(position).select();


                switch (position) {
                    case 0:
                        atxt.setText(getString(R.string.purches));
                        break;
                    case 1:
                        atxt.setText(getString(R.string.choose_date_time));
                        fragmentBasket2.init();
                        break;
                    case 2:
                        atxt.setText(getString(R.string.buyer_info));
                        fragmentBasket3.init();
                        break;
                    case 3:
                        ModelHolder.getInstance().paymentTypeId = 5;
                        atxt.setText(getString(R.string.submit_and_send));
                        fragmentBasket4.onReCheck();
                        break;
                }
            }
        });

        viewPager.setPagingEnabled(false);
        // fragmentCates.init();

    }

    private void initViewPagerAndTabsService() {

        for (BasketModel aBasketModel : ModelHolder.getInstance().getBasket())
            aBasketModel.count = 1;


        fragmentBasket2 = FragmentBasket2.createInstance(35);
        fragmentBasket3 = FragmentBasket3.createInstance(36);
        fragmentBasket4 = FragmentBasket4.createInstance(36);

        PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager());


        aTabLayout.addTab(aTabLayout.newTab().setText(getString(R.string.choose_date_time)));
        aTabLayout.addTab(aTabLayout.newTab().setText(getString(R.string.buyer_info)));
        aTabLayout.addTab(aTabLayout.newTab().setText(getString(R.string.submit_and_send)));


        pagerAdapter.addFragment(fragmentBasket2, getString(R.string.last));
        pagerAdapter.addFragment(fragmentBasket3, getString(R.string.last));
        pagerAdapter.addFragment(fragmentBasket4, getString(R.string.last));


        aTabLayout.getTabAt(0).setCustomView
                (getCustomView(getString(R.string.choose_date_time), "\uf073"));

        aTabLayout.getTabAt(1).setCustomView
                (getCustomView(getString(R.string.buyer_info), "\uf2bb"));

        aTabLayout.getTabAt(2).setCustomView
                (getCustomView(getString(R.string.submit_and_send), "\uf07a"));


        viewPager.setAdapter(pagerAdapter);

        aTabLayout.setTabMode(TabLayout.MODE_FIXED);
        aTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        TabLayoutUtils.enableTabs(aTabLayout, false);

        // aTabLayout.setupWithViewPager(viewPager);

        viewPager.setOffscreenPageLimit(7);
        viewPager.setPagingEnabled(true);

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int state) {
            }

            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            public void onPageSelected(int position) {

                aTabLayout.getTabAt(position).select();


                switch (position) {
                    case 0:
                        atxt.setText(getString(R.string.choose_date_time));
                        fragmentBasket2.init();
                        break;
                    case 1:
                        atxt.setText(getString(R.string.buyer_info));
                        fragmentBasket3.init();
                        break;
                    case 2:
                        ModelHolder.getInstance().paymentTypeId = 5;
                        atxt.setText(getString(R.string.submit_and_send));
                        fragmentBasket4.onReCheck();
                        break;
                }

            }
        });

        viewPager.setPagingEnabled(false);
        // fragmentCates.init();

    }

    private View getCustomView(String title, String icon) {

        View aView = getLayoutInflater().inflate(R.layout.custom_tab, null);

        ((TextView) aView.findViewById(R.id.img)).setText(icon);
        ((TextView) aView.findViewById(R.id.text)).setText(title);

        return aView;
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

    public void onBackPressed() {


        if (viewPager.getCurrentItem() > 0)
            viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
        else
            super.onBackPressed();

    }

}
