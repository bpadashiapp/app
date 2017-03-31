package ir.tahasystem.music.app.findMap;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import ir.onlinefood.app.factory.R;
import ir.tahasystem.music.app.MainActivity;
import ir.tahasystem.music.app.Model.BasketModel;
import ir.tahasystem.music.app.Model.LoginHolder;
import ir.tahasystem.music.app.Model.ModelHolder;
import ir.tahasystem.music.app.custom.CustomViewPager;


public class FindMapActivity extends AppCompatActivity {

    Toolbar mToolbar;

    public static FindMapActivity context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        context = this;

        setLang();
        setTheme(R.style.AppThemeBlue);
        super.onCreate(savedInstanceState);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_basket);

        viewPager = (CustomViewPager) findViewById(R.id.viewPager);

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
        atxt.setText(getString(R.string.app_name));


    }


    public void onResume() {
        super.onResume();

        if (fragment2.isGetLoc)
            fragment2.getMyLocation();

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

    public static CustomViewPager viewPager;

    public Fragment1 fragment1;
    public Fragment2 fragment2;


    private void initViewPagerAndTabs() {


        fragment1 = Fragment1.createInstance(34);
        fragment2 = Fragment2.createInstance(35);


        PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager());


        LoginHolder.getInstance().init(FindMapActivity.context);
        if (LoginHolder.getInstance().getLogin() == null || LoginHolder.getInstance().getLogin().uid==null) {

            pagerAdapter.addFragment(fragment1, getString(R.string.last));
        }


        pagerAdapter.addFragment(fragment2, getString(R.string.last));



        viewPager.setAdapter(pagerAdapter);
        viewPager.setOffscreenPageLimit(3);
        viewPager.setPagingEnabled(false);

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int state) {
            }

            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        //  atxt.setText(getString(R.string.purches));
                        break;
                    case 1:
                        // atxt.setText(getString(R.string.choose_date_time));
                        //fragmentBasket2.init();
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

    public void onBackPressed() {


        //if (viewPager.getCurrentItem() > 0)
            moveTaskToBack(true);
        //else
            //super.onBackPressed();

    }

}
