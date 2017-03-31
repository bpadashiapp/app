package ir.tahasystem.music.app;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentManager.OnBackStackChangedListener;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gc.materialdesign.views.ProgressBarCircularIndeterminate;
import com.github.johnpersano.supertoasts.SuperActivityToast;
import com.github.johnpersano.supertoasts.SuperToast;
import com.github.johnpersano.supertoasts.util.Style;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import ir.onlinefood.app.factory.R;
import ir.tahasystem.music.app.DialogBox.LoadBox;
import ir.tahasystem.music.app.Model.IsManu;
import ir.tahasystem.music.app.Model.Kala;
import ir.tahasystem.music.app.Model.LoginHolder;
import ir.tahasystem.music.app.Model.LoginManager;
import ir.tahasystem.music.app.Model.LoginModel;
import ir.tahasystem.music.app.Model.ModelHolder;
import ir.tahasystem.music.app.Model.ModelHolderService;
import ir.tahasystem.music.app.Model.NotifyModel;
import ir.tahasystem.music.app.Model.Remember;
import ir.tahasystem.music.app.Model.VersionModel;
import ir.tahasystem.music.app.ThreadPool.ConnectionThreadPool;
import ir.tahasystem.music.app.connection.ConnectionPool;
import ir.tahasystem.music.app.custom.CustomViewPager;
import ir.tahasystem.music.app.fragment.FragmentCates;
import ir.tahasystem.music.app.fragment.FragmentHome;
import ir.tahasystem.music.app.fragment.FragmentHomeSmall;
import ir.tahasystem.music.app.fragment.FragmentSearch;
import ir.tahasystem.music.app.fragment.FragmentSmsContactList;
import ir.tahasystem.music.app.fragment.FragmentSmsInfo;
import ir.tahasystem.music.app.services.ServiceGPS;
import ir.tahasystem.music.app.services.Services;
import ir.tahasystem.music.app.services.ServicesSocket;
import ir.tahasystem.music.app.services.ServicesSocketManager;
import ir.tahasystem.music.app.services.ServicesSocketMap;
import ir.tahasystem.music.app.update.AppDownloadManagerManual;
import ir.tahasystem.music.app.update.UpdateAlert;
import ir.tahasystem.music.app.update.UpdatePanel;
import ir.tahasystem.music.app.utils.FontUtils;
import ir.tahasystem.music.app.utils.NetworkUtil;
import ir.tahasystem.music.app.utils.Serialize;


public class MainActivity extends AppCompatActivity
        implements OnBackStackChangedListener,
        NavigationView.OnNavigationItemSelectedListener {


    public Toolbar mToolbar;
    public static MainActivity context;
    public NavigationView navigationView;
    public static DrawerLayout drawerLayout;

    public LoadBox aLoadBox;

    public CustomViewPager viewPager;

    public TextView subTitle;

    TabLayout aTabLayout;

    private static final int ASK_MULTIPLE_PERMISSION_REQUEST_CODE = 112;
    private Bundle savedInstanceState;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        context = this;
        setTheme(R.style.AppThemeBlueMain);

        LoginHolder.getInstance().init(this);

        super.onCreate(savedInstanceState);
        this.savedInstanceState = savedInstanceState;
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);


        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mToolbar.setTitle(null);
        mToolbar.setSubtitle(null);

        mToolbar.setTitleTextColor(getResources().getColor(android.R.color.white));

        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                System.out.println("1");


                if (item != null && item.getItemId() == R.id.action_menuz) {
                    if (drawerLayout.isDrawerOpen(Gravity.RIGHT)) {
                        drawerLayout.closeDrawer(Gravity.RIGHT);
                    } else {
                        drawerLayout.openDrawer(Gravity.RIGHT);
                    }
                    return true;
                }
                return false;

            }
        });

        //    <uses-permission android:name="android.permission.READ_PHONE_STATE" />
        //<uses-permission android:name="android.permission.CALL_PHONE" />
        //<uses-permission android:name="android.permission.PROCESS_INCOMING_CALLS" />

        viewPager = (CustomViewPager) findViewById(R.id.viewPager);
        aTabLayout = (TabLayout) findViewById(R.id.tabs);


        init();

        getDataCompanyInfo();

        if (getIntent() != null && getIntent().getBooleanExtra("manager", false)) {

            if (LoginHolder.getInstance().getLogin().user == null)
                LoginManager();
            else
                startActivity(new Intent(context, OrderActivity.class));
        }


    }

    public void init() {


        initToolbarMain();
        initDrawar();
        if (Values.isJustOmade)
            initViewPagerAndTabsIsJustOmade();
        else if (Values.isJustSmall)
            initViewPagerAndTabsIsJustSmall();
        else
            initViewPagerAndTabs();
        initLogin();

        if (Values.lat == 0 || Values.lng == 0)


            aLoadBox = new LoadBox();

        // #check for Update

        NotifyModel aNotifyModel = (NotifyModel) new Serialize().readFromFile(context,
                "aNotifyModelNew");

        if (aNotifyModel == null || aNotifyModel.getVer() == null)
            return;

        UpdatePanel updatePanel = new UpdatePanel(context, aNotifyModel.getVer());
        updatePanel.update();


    }


    @Override
    protected void onNewIntent(final Intent intent) {
        super.onNewIntent(intent);

    }

    View mCustomViewMain;
    public TextView aTextViewBasket;
    TextView aTextViewNotify;

    TextView aTextViewMsg;

    View countBasket;

    View actionMsg;

    private void initToolbarMain() {

        //BASKET

        LayoutInflater mInflater = LayoutInflater.from(context);

        if (mCustomViewMain == null)
            mCustomViewMain = mInflater.inflate(R.layout.action_all_layout, null);
        mToolbar.addView(mCustomViewMain);

        subTitle = (TextView) mCustomViewMain.findViewById(R.id.toolbar_sub_title);
        if (Values.appId == 0)
            subTitle.setText(getString(R.string.bigSell));
        else
            subTitle.setText(getString(R.string.home));


        countBasket = mCustomViewMain.findViewById(R.id.action_buy);


        countBasket.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                if(!Values.company.basket){
                    UpgradeMsg();
                    return;
                }

                // if (LoginHolder.getInstance().getLogin() == null || !LoginHolder.getInstance().getLogin().hasAccess()) {

                //ManagerCall();
//
                //} else {
                startActivityForResult(new Intent(MainActivity.this, BasketActivity.class), 1);
                // }

            }
        });
        aTextViewBasket = (TextView) countBasket.findViewById(R.id.action_buy_txt);

        if (aTextViewBasket != null)
            if (ModelHolder.getInstance().getBasketCount() > 0) {
                aTextViewBasket.setVisibility(View.VISIBLE);
                aTextViewBasket.setText(String.valueOf(ModelHolder.getInstance().getBasketCount()));
            } else {
                aTextViewBasket.setVisibility(View.INVISIBLE);
            }


        //NOTIFY


        View count = mCustomViewMain.findViewById(R.id.action_notify);
        count.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                if(!Values.company.notification){
                    UpgradeMsg();
                    return;
                }

                //if (LoginHolder.getInstance().getLogin() == null) {

                // Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                //  intent.putExtra("id", "notify");
                //  startActivity(intent);

                //return;
                //}

                //LoadBox.ShowLoad(context, context.getString(R.string.plz_wait));
                startActivity(new Intent(MainActivity.this, NotifyActivity.class));

            }
        });

        aTextViewNotify = (TextView) count.findViewById(R.id.action_notify_txt);

        int countNotify = getNotViewedNotifyCount();

        updateNotfiyView(countNotify);

        actionMsg = mCustomViewMain.findViewById(R.id.action_msg);
        actionMsg.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                //if (LoginHolder.getInstance().getLogin() == null) {

                // Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                //  intent.putExtra("id", "notify");
                //  startActivity(intent);

                //return;
                //}

                //LoadBox.ShowLoad(context, context.getString(R.string.plz_wait));

                if(!Values.company.chat){
                    UpgradeMsg();
                    return;
                }


                startActivity(new Intent(MainActivity.this, MsgActivity.class));

            }
        });

        aTextViewMsg = (TextView) actionMsg.findViewById(R.id.action_msg_txt);
        int countMsg = getNotViewedMsgCount();
        updateMsgView(countMsg);


    }

    LoginModel aLoginModel;

    public void initLogin() {

        aLoginModel = LoginHolder.getInstance().getLogin();

        System.out.println("-->##>>" + (LoginModel) new Serialize().readFromFile(context,
                "aLoginModel"));

        if (aLoginModel != null) {


            if (aLoginModel.hasRole && aLoginModel.user != null) {
                navigationView.getMenu().setGroupVisible(R.id.menu_login, false);
                navigationView.getMenu().setGroupVisible(R.id.menu_panel, true);
            } else if (aLoginModel.hasRole && aLoginModel.user == null) {
                navigationView.getMenu().setGroupVisible(R.id.menu_login, true);
                navigationView.getMenu().setGroupVisible(R.id.menu_panel, false);
            }

            //######### TEST
            //navigationView.getMenu().setGroupVisible(R.id.menu_panel, true);


            if (LoginHolder.getInstance().getLogin() == null || !LoginHolder.getInstance().getLogin().showPrice)
                navigationView.getMenu().setGroupVisible(R.id.menu_access, true);
            else
                navigationView.getMenu().setGroupVisible(R.id.menu_access, false);

            if (Values.appId != 0)
                navigationView.getMenu().setGroupVisible(R.id.menu_access, false);

            if (LoginHolder.getInstance().getLogin() == null || LoginHolder.getInstance().getLogin().accessType == 1) {
                navigationView.getMenu().findItem(R.id.contactus).setVisible(false);
                navigationView.getMenu().findItem(R.id.map).setVisible(false);
                navigationView.getMenu().findItem(R.id.aboutus).setVisible(false);
            } else if (LoginHolder.getInstance().getLogin() == null || LoginHolder.getInstance().getLogin().accessType == 2) {
                navigationView.getMenu().findItem(R.id.buy_history).setVisible(false);
                navigationView.getMenu().findItem(R.id.about_creator).setVisible(false);

            } else if (LoginHolder.getInstance().getLogin() == null || LoginHolder.getInstance().getLogin().accessType == 12) {

                navigationView.getMenu().findItem(R.id.buy_history).setVisible(false);
                navigationView.getMenu().findItem(R.id.contactus).setVisible(false);
                navigationView.getMenu().findItem(R.id.about_creator).setVisible(false);

                navigationView.getMenu().findItem(R.id.contactus).setVisible(false);
                navigationView.getMenu().findItem(R.id.map).setVisible(false);
                navigationView.getMenu().findItem(R.id.aboutus).setVisible(false);

            }

            //navigationView.getMenu().setGroupVisible(R.id.menu_login, true);


            header.findViewById(R.id.login).setVisibility(View.GONE);
            header.findViewById(R.id.login_info).setVisibility(View.VISIBLE);
            // navigationView.getMenu().setGroupVisible(R.id.menu_user, true);

            TextView name = (TextView) header.findViewById(R.id.name);
            TextView email = (TextView) header.findViewById(R.id.email);

            TextView versionAndTitle = (TextView) header.findViewById(R.id.version_and_title);

            versionAndTitle.setText(getString(R.string.version) + " " + getVersionName());

            ImageView avatar = (ImageView) header.findViewById(R.id.avatar);

            name.setText(aLoginModel.uid);
            //email.setText(aLoginModel.email);

            if (aLoginModel.avatar != null && aLoginModel.avatar.length() > 0)
                Picasso.with(context)
                        .load(aLoginModel.avatar)

                        .into(avatar);

            System.out.println("ROLE->" + aLoginModel.role);

        } else {
            Login();
        }
    }


    public String getVersionName() {
        PackageInfo pInfo = null;
        try {
            pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return pInfo.versionName;
    }

    public boolean isShowCopon;

    public void onResume() {
        super.onResume();

        if (aTextViewBasket != null)
            if (ModelHolder.getInstance().getBasketCount() > 0) {
                aTextViewBasket.setVisibility(View.VISIBLE);
                aTextViewBasket.setText(String.valueOf(ModelHolder.getInstance().getBasketCount()));
            } else {
                aTextViewBasket.setVisibility(View.INVISIBLE);
            }


        if (isShowCopon) {
            isShowCopon = false;
        }

        if (LoginHolder.getInstance().getLogin() != null && aDialogFragment != null && aDialogFragment.isVisible())
            try {
                aDialogFragment.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }


        System.out.println("->CHECK GPSZ");

        System.out.println("->RECHECK GPS");

        initService();
    }


    View header;

    private void initDrawar() {

        // Initializing NavigationView
        navigationView = (NavigationView) findViewById(R.id.navigation_view);

        header = navigationView.getHeaderView(0);

        LinearLayout aLinearLayout = (LinearLayout) header.findViewById(R.id.login);
        aLinearLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                if (aLoginModel == null)
                    Login();

            }
        });


        // Setting Navigation View Item Selected Listener to handle the item
        // click of the navigation menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                switch (menuItem.getItemId()) {

                    case R.id.share:
                        shareInfo();
                        /*DFragmentShare alertdFragment = new DFragmentShare();
                        alertdFragment.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
                        alertdFragment.show(getSupportFragmentManager(), "");*/
                        break;

                    case R.id.access:
                        AccessManager();
                        break;

                    case R.id.login_m:
                        LoginManager();
                        break;

                    case R.id.gallery:
                        if(!Values.company.gallery){
                            UpgradeMsg();
                            break;
                        }
                        startActivity(new Intent(context, GalleryActivity.class));
                        break;

                    case R.id.about_creator:
                        if(!Values.company.tamasbaSazandeh){
                            UpgradeMsg();
                            break;
                        }
                        startActivity(new Intent(context, AboutCreatorActivity.class));
                        break;


                    case R.id.aboutus:
                        if(!Values.company.darbareMa){
                            UpgradeMsg();
                            break;
                        }
                        startActivity(new Intent(context, AboutusActivity.class));
                        break;

                    case R.id.contactus:
                        if(!Values.company.tamasbaMa){
                            UpgradeMsg();
                            break;
                        }
                        startActivity(new Intent(context, ContactusActivity.class));
                        break;

                    case R.id.orders:
                        startActivity(new Intent(context, OrderActivity.class));
                        break;

                    case R.id.logout_m:
                        LoginModel aLoginModel = LoginHolder.getInstance().getLogin();
                        aLoginModel.user = null;
                        LoginHolder.getInstance().setLogin(aLoginModel);

                        initLogin();

                        break;

                    case R.id.map:
                        if(!Values.company.mogheyat){
                            UpgradeMsg();
                            break;
                        }
                        startActivity(new Intent(context, MapActivity.class));
                        break;

                    case R.id.buy_history:
                        if(!Values.company.savabegh){
                            UpgradeMsg();
                            break;
                        }
                        startActivity(new Intent(context, CustomerOrderActivityTotal.class));
                        break;

                    case R.id.update:
                        VersionModel aVersionModel = new VersionModel();

                        aVersionModel.apkDir = "/CAPK/";
                        aVersionModel.apkName = Values.companyId + ".apk";
                        aVersionModel.apkFile = Values.companyId + ".apk";

                        AppDownloadManagerManual.getInstance().init(context, aVersionModel);
                        break;

                    case R.id.setting:
                        startActivity(new Intent(context, SettingActivity.class));
                        break;
                }


                return false;
            }
        });


        // Initializing Drawer Layout and ActionBarToggle
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.drawer_open, R.string.drawer_close) {

            @Override
            public boolean onOptionsItemSelected(MenuItem item) {
                if (item != null && item.getItemId() == R.id.action_menuz) {
                    if (drawerLayout.isDrawerOpen(Gravity.RIGHT)) {
                        drawerLayout.closeDrawer(Gravity.RIGHT);
                    } else {
                        drawerLayout.openDrawer(Gravity.RIGHT);
                    }
                    return true;
                }
                return false;
            }
        };

        // Setting the actionbarToggle to drawer layout
        drawerLayout.setDrawerListener(actionBarDrawerToggle);

        // calling sync state is necessay or else your hamburger icon wont show
        // up
        actionBarDrawerToggle.syncState();

    }

    public static String getApkName(Context context) {
        String packageName = context.getPackageName();
        PackageManager pm = context.getPackageManager();
        try {
            ApplicationInfo ai = pm.getApplicationInfo(packageName, 0);
            String apk = ai.publicSourceDir;
            return apk;
        } catch (Throwable x) {
        }
        return null;
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        // mDrawerToggle.syncState();

    }

    @Override
    public void onBackStackChanged() {

        // if (drawerLayout.isDrawerVisible(GravityCompat.START)) {
        // drawerLayout.closeDrawer(GravityCompat.START);
        // }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {


        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.drawar_item, menu);

        return super.onCreateOptionsMenu(menu);
    }


    public void onDestroy() {

        super.onDestroy();


        ConnectionThreadPool.getInstance().ShutDown();

        context = null;

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);


    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {

        System.out.println("1");


        if (menuItem.getItemId() == R.id.action_menuz) {
            if (drawerLayout.isDrawerOpen(Gravity.RIGHT)) {
                drawerLayout.closeDrawer(Gravity.RIGHT);
                System.out.println("2");
            } else {
                drawerLayout.openDrawer(Gravity.RIGHT);
            }
        }
        return super.onOptionsItemSelected(menuItem);
    }

    boolean isExit;

    public void onBackPressed() {

        if (aDialogFragment != null && aDialogFragment.isVisible()) {
            moveTaskToBack(true);
            System.out.println("GOBACK--->");
            return;
        }

        if (drawerLayout.isDrawerOpen(Gravity.RIGHT)) {
            MainActivity.drawerLayout.closeDrawer(Gravity.RIGHT);
        } else if (!isExit) {

            if (context != null)
                msg(context.getString(R.string.press_twice_to_exit));
            isExit = true;
            ExitThread();
        } else {

            finish();
        }

    }


    public void ExitThread() {
        ConnectionThreadPool.getInstance().AddTask(new Runnable() {
            private String command;

            @Override
            public void run() {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                isExit = false;

            }
        });
    }


    public void msg(final String msg) {

        ((Activity) context).runOnUiThread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub

                final Style customStyle = new Style();
                customStyle.animations = SuperToast.Animations.SCALE;
                customStyle.background = SuperToast.Background.RED;
                customStyle.textColor = Color.parseColor("#ffffff");
                customStyle.buttonTextColor = Color.WHITE;
                customStyle.dividerColor = Color.WHITE;

                SuperActivityToast superActivityToast = new SuperActivityToast(MainActivity.this, customStyle);
                superActivityToast.setDuration(SuperToast.Duration.MEDIUM);
                superActivityToast.setText(msg);
                superActivityToast.show();

            }
        });

    }

    @Override
    public boolean onNavigationItemSelected(MenuItem arg0) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        super.onActivityResult(requestCode, resultCode, data);

    }


    // #

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


    public FragmentHome fragmentHomeBig;
    public FragmentHomeSmall fragmentHomeSmall;
    public FragmentCates fragmentCates;
    public FragmentSearch fragmentSearch;

    private void initViewPagerAndTabs() {

        //aTabLayout.addTab(aTabLayout.newTab().setIcon(R.drawable.search));
        //aTabLayout.addTab(aTabLayout.newTab().setIcon(R.drawable.cates));
        //aTabLayout.addTab(aTabLayout.newTab().setIcon(R.drawable.home));

        fragmentHomeBig = FragmentHome.createInstance(33);
        fragmentHomeSmall = FragmentHomeSmall.createInstance(33);
        fragmentCates = FragmentCates.createInstance(34);
        fragmentSearch = FragmentSearch.createInstance(35);


        PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager());

        aTabLayout.addTab(aTabLayout.newTab().setText(getString(R.string.search)));
        pagerAdapter.addFragment(fragmentSearch, getString(R.string.search));

        if (Values.appId == 0) {


            aTabLayout.addTab(aTabLayout.newTab().setText(getString(R.string.smallSell)));
            pagerAdapter.addFragment(fragmentHomeSmall, getString(R.string.smallSell));

        }


        aTabLayout.addTab(aTabLayout.newTab().setText(getString(R.string.home)));
        pagerAdapter.addFragment(fragmentCates, getString(R.string.home));

        if (Values.appId == 0) {


            aTabLayout.addTab(aTabLayout.newTab().setText(getString(R.string.bigSell)));
            pagerAdapter.addFragment(fragmentHomeBig, getString(R.string.bigSell));
        }


        //Setting adapter


        viewPager.setPagingEnabled(true);
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(aTabLayout));
        viewPager.setAdapter(pagerAdapter);

        if (Values.appId == 0) {
            aTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
            aTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        } else {
            //aTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
            aTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        }


        viewPager.setOffscreenPageLimit(5);
        viewPager.setPagingEnabled(true);

        //aTabLayout.setupWithViewPager(viewPager);

        viewPager.setCurrentItem(3);


        aTabLayout.setOnTabSelectedListener(
                new TabLayout.ViewPagerOnTabSelectedListener(viewPager) {
                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        super.onTabSelected(tab);


                        switch (tab.getPosition()) {
                            case 0:
                                MainActivity.context.subTitle.setText(getString(R.string.search));
                                break;
                            case 1:
                                if (Values.appId != 0)
                                    subTitle.setText(getString(R.string.home));
                                else
                                    MainActivity.context.subTitle.setText(getString(R.string.smallSell));
                                //fragmentCates.init();
                                break;
                            case 2:
                                MainActivity.context.subTitle.setText(getString(R.string.home));
                                // fragmentHomeBig.init();
                                break;
                            case 3:
                                MainActivity.context.subTitle.setText(getString(R.string.bigSell));
                                break;
                        }
                    }


                });
    }

    private void initViewPagerAndTabsIsJustOmade() {

        //aTabLayout.addTab(aTabLayout.newTab().setIcon(R.drawable.search));
        //aTabLayout.addTab(aTabLayout.newTab().setIcon(R.drawable.cates));
        //aTabLayout.addTab(aTabLayout.newTab().setIcon(R.drawable.home));

        fragmentHomeBig = FragmentHome.createInstance(33);
        fragmentHomeSmall = FragmentHomeSmall.createInstance(33);
        fragmentCates = FragmentCates.createInstance(34);
        fragmentSearch = FragmentSearch.createInstance(35);


        PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager());

        aTabLayout.addTab(aTabLayout.newTab().setText(getString(R.string.search)));
        pagerAdapter.addFragment(fragmentSearch, getString(R.string.search));


        aTabLayout.addTab(aTabLayout.newTab().setText(getString(R.string.home)));
        pagerAdapter.addFragment(fragmentCates, getString(R.string.home));


        aTabLayout.addTab(aTabLayout.newTab().setText(getString(R.string.sell)));
        pagerAdapter.addFragment(fragmentHomeBig, getString(R.string.sell));


        //Setting adapter


        viewPager.setPagingEnabled(true);
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(aTabLayout));
        viewPager.setAdapter(pagerAdapter);


        aTabLayout.setTabMode(TabLayout.MODE_FIXED);
        aTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);


        viewPager.setOffscreenPageLimit(5);
        viewPager.setPagingEnabled(true);

        //aTabLayout.setupWithViewPager(viewPager);

        viewPager.setCurrentItem(3);


        aTabLayout.setOnTabSelectedListener(
                new TabLayout.ViewPagerOnTabSelectedListener(viewPager) {
                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        super.onTabSelected(tab);


                        switch (tab.getPosition()) {
                            case 0:
                                MainActivity.context.subTitle.setText(getString(R.string.search));
                                break;
                            case 1:
                                MainActivity.context.subTitle.setText(getString(R.string.home));
                                // fragmentHomeBig.init();
                                break;
                            case 2:
                                MainActivity.context.subTitle.setText(getString(R.string.sell));
                                break;
                        }
                    }


                });
    }

    private void initViewPagerAndTabsIsJustSmall() {

        //aTabLayout.addTab(aTabLayout.newTab().setIcon(R.drawable.search));
        //aTabLayout.addTab(aTabLayout.newTab().setIcon(R.drawable.cates));
        //aTabLayout.addTab(aTabLayout.newTab().setIcon(R.drawable.home));

        fragmentHomeBig = FragmentHome.createInstance(33);
        fragmentHomeSmall = FragmentHomeSmall.createInstance(33);
        fragmentCates = FragmentCates.createInstance(34);
        fragmentSearch = FragmentSearch.createInstance(35);


        PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager());

        aTabLayout.addTab(aTabLayout.newTab().setText(getString(R.string.search)));
        pagerAdapter.addFragment(fragmentSearch, getString(R.string.search));

        aTabLayout.addTab(aTabLayout.newTab().setText(getString(R.string.smallSell)));
        pagerAdapter.addFragment(fragmentHomeSmall, getString(R.string.smallSell));


        aTabLayout.addTab(aTabLayout.newTab().setText(getString(R.string.home)));
        pagerAdapter.addFragment(fragmentCates, getString(R.string.home));





        //Setting adapter


        viewPager.setPagingEnabled(true);
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(aTabLayout));
        viewPager.setAdapter(pagerAdapter);


        aTabLayout.setTabMode(TabLayout.MODE_FIXED);
        aTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);


        viewPager.setOffscreenPageLimit(5);
        viewPager.setPagingEnabled(true);

        //aTabLayout.setupWithViewPager(viewPager);

        viewPager.setCurrentItem(3);


        aTabLayout.setOnTabSelectedListener(
                new TabLayout.ViewPagerOnTabSelectedListener(viewPager) {
                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        super.onTabSelected(tab);


                        switch (tab.getPosition()) {
                            case 0:
                                MainActivity.context.subTitle.setText(getString(R.string.search));
                                break;
                            case 1:
                                MainActivity.context.subTitle.setText(getString(R.string.smallSell));
                                // fragmentHomeBig.init();
                                break;
                            case 2:
                                MainActivity.context.subTitle.setText(getString(R.string.home));
                                break;
                        }
                    }


                });
    }


    private synchronized int getNotViewedNotifyCount() {

        int count = 0;

        List<Kala> oldNotifyModelList = ModelHolderService.getInstance().getKalaList(context);
        if (oldNotifyModelList == null)
            return 0;

        for (Kala aNotifyModel : oldNotifyModelList)
            if (!aNotifyModel.isView)
                count++;

        return count;

    }

    private synchronized int getNotViewedMsgCount() {

        int count = 0;

        List<Kala> oldNotifyModelList = ModelHolderService.getInstance().getKalaListChat(context);
        if (oldNotifyModelList == null)
            return 0;

        for (Kala aNotifyModel : oldNotifyModelList)
            if (!aNotifyModel.isView)
                count++;

        return count;

    }


    public void updateNotify() {

        final int countNotify = getNotViewedNotifyCount();
        updateNotfiyView(countNotify);

    }

    public void updateMsg() {

        final int countMsg = getNotViewedMsgCount();
        updateMsgView(countMsg);

    }

    public void updateMsgView(final int countMsg) {

        if (context == null)
            return;

        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if (aTextViewMsg == null || mCustomViewMain == null)
                    return;

                System.out.println(countMsg + "--->countMsg");

                if (countMsg > 0) {
                    aTextViewMsg.setVisibility(View.VISIBLE);
                    aTextViewMsg.setText(String.valueOf(countMsg));
                } else {
                    aTextViewMsg.setVisibility(View.INVISIBLE);
                }

                aTextViewMsg.invalidate();
                aTextViewMsg.postInvalidate();

                mCustomViewMain.invalidate();
                mCustomViewMain.postInvalidate();


            }

        });


    }


    public void updateNotfiyView(final int countNotify) {

        if (context == null)
            return;

        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if (aTextViewNotify == null || mCustomViewMain == null)
                    return;

                if (countNotify > 0) {
                    aTextViewNotify.setVisibility(View.VISIBLE);
                    aTextViewNotify.setText(String.valueOf(countNotify));
                } else {
                    aTextViewNotify.setVisibility(View.INVISIBLE);
                }

                aTextViewNotify.invalidate();
                aTextViewNotify.postInvalidate();

                mCustomViewMain.invalidate();
                mCustomViewMain.postInvalidate();


            }

        });


    }

    boolean isNotifyed = false;

    public void notifyAvaliableUpdate(final VersionModel versionModel) {

        if (context == null && isNotifyed)
            return;

        isNotifyed = true;

        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                UpdateAlert aDialogFragmentUpdate = new UpdateAlert(context, versionModel);
                aDialogFragmentUpdate.show();
            }
        });
    }

    public void Login() {

        DFragmentPhone alertdFragment = new DFragmentPhone();
        alertdFragment.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
        alertdFragment.show(getSupportFragmentManager(), "");

    }

    public void LoginManager() {

        DFragmentLogin alertdFragment = new DFragmentLogin();
        alertdFragment.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
        alertdFragment.show(getSupportFragmentManager(), "");

    }

    public void AccessManager() {

        DFragmentAccess alertdFragment = new DFragmentAccess();
        alertdFragment.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
        alertdFragment.show(getSupportFragmentManager(), "");

    }


    EditText edtTelMoaref;
    TextView noResTextView;
    ProgressBarCircularIndeterminate aProgressBar;
    LinearLayout reg, reg2, reg3;
    String vcode;
    DFragmentPhone aDialogFragment;
    private static final String FORMAT = "%02d : %02d : %02d";

    class DFragmentPhone extends DialogFragment {

        Button aButtonCall, bButton, cButton;
        public EditText edtTel, edtCode;

        public DFragmentPhone() {
            aDialogFragment = this;
        }

        public void getDataF() {
            getData2(edtTel.getText().toString(), edtTelMoaref.getText().toString());
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

            getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

            final View dialog = inflater.inflate(R.layout.dialogz_reg_insert_num, container, false);

            setTitle(getString(R.string.app_name));

            aDialogFragment.setCancelable(false);

            reg = (LinearLayout) dialog.findViewById(R.id.reg);
            reg2 = (LinearLayout) dialog.findViewById(R.id.reg2);
            reg3 = (LinearLayout) dialog.findViewById(R.id.reg3);

            edtCode = (EditText) dialog.findViewById(R.id.value2);
            bButton = (Button) dialog.findViewById(R.id.btn2);

            edtTel = (EditText) dialog.findViewById(R.id.value);
            edtTelMoaref = (EditText) dialog.findViewById(R.id.valueMoaref);
            aButtonCall = (Button) dialog.findViewById(R.id.btnCall);

            final TextView text1 = (TextView) dialog.findViewById(R.id.timer);

            noResTextView = (TextView) dialog.findViewById(R.id.no_res);
            aProgressBar = (ProgressBarCircularIndeterminate) dialog.findViewById(R.id.progress);

            cButton = (Button) dialog.findViewById(R.id.btn3);

            aButtonCall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (edtTel.getText().toString().trim().length() == 11) {

                        hideKeyboardFrom(context, edtTel);

                        edtTel.setError(null);


                        reg.setVisibility(View.INVISIBLE);
                        aProgressBar.setVisibility(View.VISIBLE);

                        getData(edtTel.getText().toString(), 0);
                        getData(edtTel.getText().toString(), 1);


                        context.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                reg.setVisibility(View.GONE);
                                reg2.setVisibility(View.VISIBLE);
                                aProgressBar.setVisibility(View.GONE);
                            }
                        });


                        new CountDownTimer(90000, 1000) { // adjust the milli seconds here

                            public void onTick(long millisUntilFinished) {

                                if (context == null || aDialogFragment == null || !aDialogFragment.isVisible())
                                    cancel();

                                text1.setText("" + String.format(FORMAT,
                                        TimeUnit.MILLISECONDS.toHours(millisUntilFinished),
                                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(
                                                TimeUnit.MILLISECONDS.toHours(millisUntilFinished)),
                                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(
                                                TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
                            }

                            public void onFinish() {
                                if (context != null && aDialogFragment != null && aDialogFragment.isVisible())
                                    noServerResponse(getString(R.string.retry));

                            }
                        }.start();


                    } else {
                        edtTel.setError(dialog.getContext().getString(R.string.mob_error));
                    }

                }
            });

            bButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (edtCode.getText().toString().trim().equals(vcode)) {

                        hideKeyboardFrom(context, edtCode);

                        edtCode.setError(null);

                        reg.setVisibility(View.INVISIBLE);
                        reg2.setVisibility(View.GONE);
                        aProgressBar.setVisibility(View.VISIBLE);

                        getData2(edtTel.getText().toString(), edtTelMoaref.getText().toString());

                    } else {

                        edtCode.setError(dialog.getContext().getString(R.string.code_error));
                    }

                }
            });

            cButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    reg.setVisibility(View.VISIBLE);
                    reg2.setVisibility(View.GONE);
                    reg3.setVisibility(View.INVISIBLE);
                }
            });


            return dialog;
        }
    }


    EditText info;
    DFragmentLogin aDFragmentLogin;

    public class DFragmentLogin extends DialogFragment {


        public DFragmentLogin() {

        }


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


            getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            // setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar_Fullscreen);

            final View dialog = inflater.inflate(R.layout.dialogz_login, container, false);

            aDFragmentLogin = this;

            reg = (LinearLayout) dialog.findViewById(R.id.reg);
            reg2 = (LinearLayout) dialog.findViewById(R.id.reg2);
            aProgressBar = (ProgressBarCircularIndeterminate) dialog.findViewById(R.id.progress);

            noResTextView = (TextView) dialog.findViewById(R.id.no_res);


            final EditText user = (EditText) dialog.findViewById(R.id.user);
            final EditText pass = (EditText) dialog.findViewById(R.id.pass);

            dialog.findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (user.getText().toString().trim().length() == 0) {
                        user.setError(context.getString(R.string.plz_fill));

                        return;
                    } else {
                        user.setError(null);
                    }

                    if (pass.getVisibility() != View.GONE)
                        if (pass.getText().toString().trim().length() == 0) {
                            pass.setError(context.getString(R.string.plz_fill));
                            return;
                        } else {
                            pass.setError(null);
                        }


                    hideKeyboardFrom(context, pass);
                    hideKeyboardFrom(context, user);

                    DFragmentLogin.this.setCancelable(false);

                    if (pass.getVisibility() != View.GONE)
                        getData3(user.getText().toString().trim(), pass.getText().toString().trim());
                    else
                        getDataRem(user.getText().toString());

                    reg.setVisibility(View.INVISIBLE);
                    reg2.setVisibility(View.GONE);
                    aProgressBar.setVisibility(View.VISIBLE);

                }
            });

            dialog.findViewById(R.id.btn2).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    reg.setVisibility(View.VISIBLE);
                    reg2.setVisibility(View.GONE);
                    aProgressBar.setVisibility(View.GONE);

                }
            });

            dialog.findViewById(R.id.btn_rem).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    pass.setVisibility(View.GONE);
                    dialog.findViewById(R.id.btn_rem).setVisibility(View.GONE);

                    ((Button) dialog.findViewById(R.id.btn)).setText(getString(R.string.remind_password));


                    hideKeyboardFrom(context, pass);
                    hideKeyboardFrom(context, user);


                }
            });


            return dialog;
        }
    }

    public static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void getData(final String mobile, final int activeBy) {


        ConnectionThreadPool.getInstance().AddTask(new Runnable() {

            @Override
            public void run() {

                try {

                    ConnectionPool aConnectionPool = new ConnectionPool();

                    SoapObject aSoapObject = null;
                    if (NetworkUtil.getConnectivityStatusString(MainActivity.context) != null)
                        aSoapObject = aConnectionPool.ConnectVerifyMobile(mobile, activeBy);


                    if (aSoapObject == null) {
                        noServerResponse(getString(R.string.server_not_response));
                        return;
                    }


                    System.out.println(aSoapObject.getPropertyCount());


                    System.out.println(aSoapObject.getProperty("id").toString());
                    System.out.println(aSoapObject.getProperty("description").toString());

                    float id = Float.parseFloat(aSoapObject.getProperty("id").toString());
                    if (id < 0) {
                        noServerResponse(aSoapObject.getProperty("description").toString());
                        return;
                    }

                    vcode = aSoapObject.getProperty("id").toString();


                } catch (Exception e) {
                    noServerResponse(getString(R.string.server_not_response));
                    e.printStackTrace();
                }


            }
        });

    }

    private void getData2(final String mobile, final String moaref) {

        if (context != null)
            context.runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    reg.setVisibility(View.INVISIBLE);
                    reg2.setVisibility(View.GONE);
                    aProgressBar.setVisibility(View.VISIBLE);
                }
            });

        ConnectionThreadPool.getInstance().

                AddTask(new Runnable() {

                            @Override
                            public void run() {

                                try {

                                    ConnectionPool aConnectionPool = new ConnectionPool();

                                    SoapPrimitive aSoapObject = null;
                                    if (NetworkUtil.getConnectivityStatusString(MainActivity.context) != null)
                                        aSoapObject = aConnectionPool.ConnectRegisterByMob(mobile, moaref);


                                    if (aSoapObject == null) {
                                        noServerResponse(getString(R.string.server_not_response));
                                        return;
                                    }


                                    List<Kala> bListKala = new ArrayList<Kala>();

                                    //SoapObject bSoapObject = (SoapObject) aSoapObject.getProperty("GetCategoriesResult");

                                    System.out.println(aSoapObject);


                                    if (!Boolean.parseBoolean(aSoapObject.toString())) {
                                        noServerResponse(context.getString(R.string.not_reg));
                                    } else {
                                        LoginModel aLoginModel = new LoginModel();
                                        aLoginModel.uid = mobile;


                                        SoapPrimitive bSoapObject = null;
                                        if (NetworkUtil.getConnectivityStatusString(MainActivity.context) != null)
                                            bSoapObject = aConnectionPool.ConnectIsManufacture(mobile);


                                        if (bSoapObject == null) {
                                            noServerResponse(getString(R.string.server_not_response));
                                            return;
                                        }

                                        IsManu isManu = new Gson().fromJson(bSoapObject.toString(), IsManu.class);

                                        aLoginModel.hasRole = isManu.IsManufacture;
                                        aLoginModel.fullAccess = isManu.FullAccess;


                                        System.out.println("hasRole-ACCESS->" + aLoginModel.hasRole + "-" + aLoginModel.fullAccess);

                                        LoginHolder.getInstance().setLogin(aLoginModel);
                                        context.runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {

                                                initLogin();

                                                // fragmentHomeSmall.isInit = false;
                                                //fragmentHomeSmall.init();

                                                // fragmentHomeBig.isInit = false;
                                                // fragmentHomeBig.init();

                                                fragmentHomeBig.isInit = false;
                                                fragmentHomeBig.init();
                                                fragmentSearch.clear();


                                            }
                                        });


                                        // noServerResponse(context.getString(R.string.is_reg));
                                    }


                                    context.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                aDialogFragment.dismiss();
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }

                                        }
                                    });

                                    // initService();


                                } catch (Exception e) {
                                    noServerResponse(getString(R.string.server_not_response));
                                    e.printStackTrace();
                                }


                            }
                        }

                );

    }

    private void noServerResponse(final String msg) {

        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                reg.setVisibility(View.GONE);
                reg2.setVisibility(View.GONE);
                reg3.setVisibility(View.VISIBLE);
                aDialogFragment.setCancelable(false);
                noResTextView.setVisibility(View.VISIBLE);
                noResTextView.setText(msg);
                aProgressBar.setVisibility(View.GONE);
            }
        });
    }

    private void noServerResponse2(final String msg) {

        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {


                aDFragmentLogin.setCancelable(true);

                reg.setVisibility(View.GONE);
                reg2.setVisibility(View.VISIBLE);
                noResTextView.setVisibility(View.VISIBLE);
                noResTextView.setText(msg);
                aProgressBar.setVisibility(View.GONE);
            }
        });
    }

    private void noServerResponse3(final String msg) {

        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {


                aDFragmentAccess.setCancelable(true);

                reg.setVisibility(View.GONE);
                reg2.setVisibility(View.VISIBLE);
                noResTextView.setVisibility(View.VISIBLE);
                noResTextView.setText(msg);
                aProgressBar.setVisibility(View.GONE);
            }
        });
    }


    private void getData3(final String user, final String pass) {


        ConnectionThreadPool.getInstance().AddTask(new Runnable() {

            @Override
            public void run() {

                try {

                    ConnectionPool aConnectionPool = new ConnectionPool();

                    SoapPrimitive aSoapObject = null;
                    if (NetworkUtil.getConnectivityStatusString(MainActivity.context) != null)
                        aSoapObject = aConnectionPool.ConnectLogin(user, pass);


                    if (aSoapObject == null) {
                        noServerResponse2(getString(R.string.server_not_response));
                        return;
                    }

                    LoginManager aLoginManager = new Gson().fromJson(aSoapObject.toString(), LoginManager.class);

                    if (aLoginManager == null) {
                        noServerResponse2(getString(R.string.server_not_response));
                        return;
                    }


                    if (aLoginManager.status.equals("0")) {
                        noServerResponse2(getString(R.string.wrong_user_pass));
                        return;
                    }

                    System.out.println(aSoapObject);

                    LoginModel aLoginModel = LoginHolder.getInstance().getLogin();
                    aLoginModel.user = user;
                    aLoginModel.pass = pass;
                    LoginHolder.getInstance().setLogin(aLoginModel);

                    context.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            aDFragmentLogin.dismiss();
                            initLogin();
                        }
                    });


                } catch (Exception e) {
                    noServerResponse2(getString(R.string.server_not_response));
                    e.printStackTrace();
                }


            }
        });

    }

    DFragmentAccess aDFragmentAccess;

    class DFragmentAccess extends DialogFragment {


        public DFragmentAccess() {

        }


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

            getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

            final View dialog = inflater.inflate(R.layout.dialogz_access, container, false);

            aDFragmentAccess = this;

            reg = (LinearLayout) dialog.findViewById(R.id.reg);
            reg2 = (LinearLayout) dialog.findViewById(R.id.reg2);
            aProgressBar = (ProgressBarCircularIndeterminate) dialog.findViewById(R.id.progress);

            noResTextView = (TextView) dialog.findViewById(R.id.no_res);


            final EditText user = (EditText) dialog.findViewById(R.id.code);

            dialog.findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (user.getText().toString().trim().length() == 0) {
                        user.setError(context.getString(R.string.plz_fill));

                        return;
                    } else {
                        user.setError(null);
                    }


                    hideKeyboardFrom(context, user);

                    DFragmentAccess.this.setCancelable(false);

                    getData4(user.getText().toString().trim());

                    reg.setVisibility(View.INVISIBLE);
                    reg2.setVisibility(View.GONE);
                    aProgressBar.setVisibility(View.VISIBLE);

                }
            });

            dialog.findViewById(R.id.btn2).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    reg.setVisibility(View.VISIBLE);
                    reg2.setVisibility(View.GONE);
                    aProgressBar.setVisibility(View.GONE);

                }
            });


            return dialog;
        }
    }

    private void getData4(final String code) {


        ConnectionThreadPool.getInstance().AddTask(new Runnable() {

            @Override
            public void run() {

                try {

                    ConnectionPool aConnectionPool = new ConnectionPool();

                    SoapPrimitive aSoapObject = null;
                    if (NetworkUtil.getConnectivityStatusString(MainActivity.context) != null)
                        aSoapObject = aConnectionPool.ConnectAccess(code);


                    if (aSoapObject == null) {
                        noServerResponse3(getString(R.string.server_not_response));
                        return;
                    }

                    if (aSoapObject.toString().toLowerCase().equals("false")) {
                        noServerResponse3(getString(R.string.wrong_code));
                        return;
                    }

                    System.out.println(aSoapObject);

                    LoginModel aLoginModel = LoginHolder.getInstance().getLogin();
                    aLoginModel.hasAccess = Boolean.parseBoolean(aSoapObject.toString());
                    LoginHolder.getInstance().setLogin(aLoginModel);

                    context.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            aDFragmentAccess.dismiss();
                            initLogin();

                            // fragmentHomeSmall.isInit = false;
                            // fragmentHomeSmall.init();

                            fragmentHomeBig.isInit = false;
                            fragmentHomeBig.init();
                            fragmentSearch.clear();

                            // if (Build.VERSION.SDK_INT >= 11) {
                            //   recreate();
                            //  } else {
                            // Intent intent = getIntent();
                            // finish();
                            //  startActivity(intent);
                            // }
                        }
                    });


                } catch (Exception e) {
                    noServerResponse3(getString(R.string.server_not_response));
                    e.printStackTrace();
                }


            }
        });

    }

    public void ManagerCall() {

        DFragmentCall alertdFragment = new DFragmentCall();
        alertdFragment.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
        alertdFragment.show(getSupportFragmentManager(), "");

    }


    class DFragmentCall extends DialogFragment {


        public DFragmentCall() {

        }


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

            getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            final View dialog = inflater.inflate(R.layout.dialogz_call, container, false);

            dialog.findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    DFragmentCall.this.dismiss();

                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + Values.phone));
                    startActivity(intent);

                }
            });


            return dialog;
        }
    }

    private void getDataCompanyInfo() {


        ConnectionThreadPool.getInstance().AddTask(new Runnable() {

            @Override
            public void run() {

                try {


                } catch (Exception e) {

                    e.printStackTrace();
                }


            }
        });

    }


    private void initService() {

        startService(new Intent(this, Services.class));
        startService(new Intent(this, ServicesSocket.class));
        startService(new Intent(this, ServicesSocketManager.class));
        if (Values.appId == 3 && Values.isService) {
            startService(new Intent(this, ServicesSocketMap.class));
            startService(new Intent(this, ServiceGPS.class));
        }

    }

    public static final int PICK_CONTACT = 100;

    public class DFragmentShare extends DialogFragment {


        public DFragmentShare() {

        }


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            final View dialog = inflater.inflate(R.layout.dialogz_share, container, false);

            setTitle(getString(R.string.share));

            setTitleColor(getResources().getColor(R.color.color_primary));

            dialog.findViewById(R.id.share_apk).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    DFragmentShare.this.dismiss();

                    shareApk();

                }
            });

            dialog.findViewById(R.id.share_info).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    DFragmentShare.this.dismiss();

                    shareInfo();

                }
            });

            dialog.findViewById(R.id.share_apk_btn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    DFragmentShare.this.dismiss();

                    shareApk();

                }
            });

            dialog.findViewById(R.id.share_info_btn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    DFragmentShare.this.dismiss();

                    shareInfo();

                }
            });


            return dialog;
        }
    }

    private void shareApk() {

        /*Intent intent2 = new Intent();
        intent2.setAction(Intent.ACTION_SEND);
        intent2.setType("application/vnd.android.package-archive");
        intent2.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(getApkName(MainActivity.this))));
        startActivity(intent2);*/

        FragmentSmsContactList.isSelect = true;
        FragmentSmsInfo.shareType = 0;
        startActivity(new Intent(this, SmsActivity.class));
    }

    public static final int REQUEST_CODE_PICK_CONTACT = 1;
    public static final int MAX_PICK_CONTACT = 10;

    private void shareInfo() {

        FragmentSmsContactList.isSelect = false;
        FragmentSmsInfo.shareType = 1;
        startActivity(new Intent(this, SmsActivity.class));


    }


    private void getDataRem(final String user) {


        ConnectionThreadPool.getInstance().AddTask(new Runnable() {

            @Override
            public void run() {

                try {

                    ConnectionPool aConnectionPool = new ConnectionPool();

                    SoapPrimitive aSoapObject = null;
                    if (NetworkUtil.getConnectivityStatusString(MainActivity.context) != null)
                        aSoapObject = aConnectionPool.ConnectBazyabi(user);


                    if (aSoapObject == null) {
                        noServerResponseRem(getString(R.string.server_not_response));
                        return;
                    }

                    final Remember remember = new Gson().fromJson(aSoapObject.toString(), Remember.class);

                    if (remember == null) {
                        noServerResponseRem(getString(R.string.server_not_response));
                        return;
                    }

                    context.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            aDFragmentLogin.dismiss();


                            AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.MyAlertDialogStyle);

                            Typeface typeface = Typeface.createFromAsset(context.getAssets(), "irfontnumbold.ttf");

                            builder.setTitle(FontUtils.typeface(typeface, context.getString(R.string.remind_password)));
                            builder.setMessage(FontUtils.typeface(typeface, remember.msg));
                            builder.setCancelable(true);

                            builder.setPositiveButton(context.getString(R.string.close), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {


                                }
                            });


                            AlertDialog alertDialog = builder.create();
                            alertDialog.show();
                        }
                    });


                } catch (Exception e) {
                    noServerResponseRem(getString(R.string.server_not_response));
                    e.printStackTrace();
                }


            }
        });
    }

    private void noServerResponseRem(final String msg) {

        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {


                aDFragmentLogin.setCancelable(true);

                reg.setVisibility(View.GONE);
                reg2.setVisibility(View.VISIBLE);
                noResTextView.setVisibility(View.VISIBLE);
                noResTextView.setText(msg);
                aProgressBar.setVisibility(View.GONE);
            }
        });
    }





    public static void UpgradeMsg() {


        if(context==null)
            return;


        ((Activity) context).runOnUiThread(new Runnable() {

            @Override
            public void run() {


                AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.MyAlertDialogStyle);

                Typeface typeface = Typeface.createFromAsset(context.getAssets(), "irfontnumbold.ttf");

                builder.setTitle(FontUtils.typeface(typeface, context.getString(R.string.app_name)));
                builder.setMessage(FontUtils.typeface(typeface,Values.company.msg));
                builder.setCancelable(true);

                builder.setPositiveButton(context.getString(R.string.close), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                    }
                });


                AlertDialog alertDialog = builder.create();
                alertDialog.show();


            }
        });


    }



















}
