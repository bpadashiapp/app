package ir.tahasystem.music.app;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import ir.onlinefood.app.factory.R;


import android.app.Activity;
import android.app.FragmentManager.OnBackStackChangedListener;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TabLayout.OnTabSelectedListener;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import ir.tahasystem.music.app.custom.CustomTabLayout;
import ir.tahasystem.music.app.fragment.FragmentLogin;
import ir.tahasystem.music.app.fragment.FragmentRegister;


public class LoginActivity extends AppCompatActivity implements OnBackStackChangedListener,
        OnPageChangeListener, OnTabSelectedListener {

    private ActionBarDrawerToggle mDrawerToggle;
    Toolbar mToolbar;

    public static LoginActivity context;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    public static ViewPager viewPager;
    private String launchId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setLang();
        setTheme(R.style.AppThemeBlue);
        super.onCreate(savedInstanceState);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        setContentView(R.layout.activity_login);

        context = this;

        // //initImageLoader();
        initToolbar();
        //initBottomBar(savedInstanceState);
        // initDrawar();

        initViewPagerAndTabs();

        Intent intent = getIntent();

        if (intent != null && intent.getStringExtra("id") != null)
            launchId = intent.getStringExtra("id");


    }


    public void setLang() {

        Locale locale = new Locale("en");
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());

    }

    private void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        setTitle("");
        mToolbar.setTitleTextColor(getResources().getColor(android.R.color.white));

        setTitle("");
        TextView atxt = (TextView) mToolbar.findViewById(R.id.toolbar_title);
        atxt.setText(getString(R.string.reg_or_sign));


        // disableScroll();

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        // mDrawerToggle.syncState();

    }

    @Override
    public void onBackStackChanged() {

        if (drawerLayout.isDrawerVisible(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    private void initViewPagerAndTabs() {

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager());
        pagerAdapter.addFragment(FragmentLogin.createInstance(4), getString(R.string.login));
        pagerAdapter.addFragment(FragmentRegister.createInstance(20), getString(R.string.register));

        // pagerAdapter.addFragment(FragmentKhad.createInstance(4),
        // getString(R.string.khad));
        // pagerAdapter.addFragment(FragmentMost.createInstance(4),
        // getString(R.string.most));
        // pagerAdapter.addFragment(FragmentLast.createInstance(4),
        // getString(R.string.last));
        // pagerAdapter.addFragment(FragmentShegeft.createInstance(4),
        // getString(R.string.shegeft));
        // pagerAdapter.addFragment(FragmentKamyab.createInstance(4),
        // getString(R.string.kamyab));
        viewPager.setAdapter(pagerAdapter);
        CustomTabLayout tabLayout = (CustomTabLayout) findViewById(R.id.tabLayout);
        // tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setupWithViewPager(viewPager);

        viewPager.setOffscreenPageLimit(10);

        viewPager.setOnPageChangeListener(this);
        tabLayout.setOnTabSelectedListener((OnTabSelectedListener) this);

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

    LinearLayout li;
    private boolean isLoadBefore = false;


    public int PxToDpi(float dipValue) {
        Resources r = context.getResources();
        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, r.getDisplayMetrics());
        return px;

    }

    public void onConfigurationChanged(Configuration newConfig) {

        super.onConfigurationChanged(newConfig);

		/*
         * Display display = getWindowManager().getDefaultDisplay(); int width =
		 * display.getWidth(); int height = display.getHeight();
		 * super.onConfigurationChanged(newConfig);
		 * 
		 * // mDrawerToggle.onConfigurationChanged(newConfig);
		 * 
		 * FrameLayout.LayoutParams lp;
		 * 
		 * lp = new FrameLayout.LayoutParams(PxToDpi(width), PxToDpi(40));
		 * 
		 * lp.setMargins(0, PxToDpi(10), 0, PxToDpi(10));
		 * li.setLayoutParams(lp);
		 */

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (isLoadBefore) {
            outState.putBoolean("isLoadBefore", true);

        }

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState.containsKey("isLoadBefore")) {
            isLoadBefore = savedInstanceState.getBoolean("isLoadBefore");
        }

    }

    public synchronized void ani(final View v) {

        Animation fadeIn = new AlphaAnimation(0.0f, 1.0f);
        fadeIn.setInterpolator(new DecelerateInterpolator());

        fadeIn.setDuration(1000);

        v.startAnimation(fadeIn);

    }

    public void setTranslation(View v) {

        // create translation animation
        TranslateAnimation trans = new TranslateAnimation(Animation.RELATIVE_TO_SELF * 2, Animation.RELATIVE_TO_SELF,
                Animation.RELATIVE_TO_SELF, Animation.RELATIVE_TO_SELF);
        // trans.setDuration(1);
        trans.setFillAfter(true);

        // add new animations to the set

        // start our animation
        v.startAnimation(trans);
    }



	/*
     * public void enableScroll() { AppBarLayout.LayoutParams params =
	 * (AppBarLayout.LayoutParams) mToolbar.getLayoutParams();
	 * params.setScrollFlags( AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL |
	 * AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS); }
	 * 
	 * public void disableScroll() { AppBarLayout.LayoutParams params =
	 * (AppBarLayout.LayoutParams) mToolbar.getLayoutParams();
	 * params.setScrollFlags(0); }
	 */

    @Override
    public void onPageScrollStateChanged(int arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onPageSelected(int pos) {

        // if(pos==0)
        // disableScroll();
        // else
        // enableScroll();

    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        viewPager.setCurrentItem(tab.getPosition(), true);

        // if(tab.getPosition()==0)
        // disableScroll();
        // else
        // enableScroll();

    }

    @Override
    public void onTabUnselected(TabLayout.Tab arg0) {
        // TODOonTabReselected Auto-generated method stub

    }

    @Override
    public void onTabReselected(TabLayout.Tab arg0) {
        // TODO Auto-generated method stub

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
            MainActivity.drawerLayout.closeDrawers();
            finish();
        } else if (menuItem.getItemId() == R.id.action_back) {
            MainActivity.drawerLayout.closeDrawers();
            finish();
        }
        return super.onOptionsItemSelected(menuItem);
    }


    public void onBackPressed() {
        finish();
    }

    public void onDestroy() {
        super.onDestroy();

    }

    public static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


}
