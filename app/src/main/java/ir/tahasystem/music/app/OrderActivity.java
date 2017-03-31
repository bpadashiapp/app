package ir.tahasystem.music.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gc.materialdesign.views.ProgressBarCircularIndeterminate;
import com.google.gson.Gson;

import org.ksoap2.serialization.SoapPrimitive;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import ir.onlinefood.app.factory.R;
import ir.tahasystem.music.app.Model.Access;
import ir.tahasystem.music.app.Model.BasketModel;
import ir.tahasystem.music.app.Model.Kala;
import ir.tahasystem.music.app.Model.ModelHolder;
import ir.tahasystem.music.app.Model.ModelHolderService;
import ir.tahasystem.music.app.ThreadPool.ConnectionThreadPool;
import ir.tahasystem.music.app.connection.ConnectionPool;
import ir.tahasystem.music.app.custom.CustomViewPager;
import ir.tahasystem.music.app.fragment.FragmentCates;
import ir.tahasystem.music.app.fragment.FragmentChangeFilter;
import ir.tahasystem.music.app.fragment.FragmentOrderNew;
import ir.tahasystem.music.app.fragment.FragmentOrderReject;
import ir.tahasystem.music.app.fragment.FragmentOrderRequest;
import ir.tahasystem.music.app.fragment.FragmentOrderSubmit;
import ir.tahasystem.music.app.utils.NetworkUtil;


public class OrderActivity extends AppCompatActivity {

    Toolbar mToolbar;

    public static OrderActivity context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        context = this;
        FragmentCates.isChange2 = true;


        setLang();
        setTheme(R.style.AppThemeBlue);
        super.onCreate(savedInstanceState);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_order);

        aTabLayout = (TabLayout) findViewById(R.id.tabs);
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

    View actionMsg;
    TextView aTextViewMsg;
    View mCustomViewMain;

    private void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        mToolbar.setTitleTextColor(getResources().getColor(android.R.color.white));

        setTitle("");
        TextView atxt = (TextView) mToolbar.findViewById(R.id.toolbar_title);
        atxt.setText(getString(R.string.orders));

        LayoutInflater mInflater = LayoutInflater.from(context);
        View mCustomView = mInflater.inflate(R.layout.action_submit, null);
        mToolbar.addView(mCustomView, 0);

        View count = mCustomView.findViewById(R.id.action_submit);

        if (Values.appId != 0)
            count.setVisibility(View.GONE);

        count.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                DFragmentAccess alertdFragment = new DFragmentAccess();
                alertdFragment.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
                alertdFragment.show(getSupportFragmentManager(), "");


            }
        });


        mCustomViewMain = mCustomView.findViewById(R.id.action_msg);


        actionMsg = mCustomViewMain.findViewById(R.id.action_msg);
        actionMsg.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                startActivity(new Intent(OrderActivity.this, MsgActivityOrder.class));

            }
        });

        aTextViewMsg = (TextView) actionMsg.findViewById(R.id.action_msg_txt);
        int countMsg = getNotViewedMsgCount();
        updateMsgView(countMsg);


    }
    private synchronized int getNotViewedMsgCount() {

        int count = 0;

        List<Kala> oldNotifyModelList = ModelHolderService.getInstance().getKalaListChatManager(context);
        if (oldNotifyModelList == null)
            return 0;

        for (Kala aNotifyModel : oldNotifyModelList)
            if (!aNotifyModel.isView)
                count++;

        return count;

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

        FragmentCates.isChange2 = false;
    }

    public CustomViewPager viewPager;
    TabLayout aTabLayout;

    public FragmentChangeFilter fragmentChangeFilter;
    public FragmentCates fragmentOrderChange;
    public FragmentOrderRequest fragmentOrderRequest;
    public FragmentOrderReject fragmentOrderReject;
    public FragmentOrderSubmit fragmentOrderSubmit;
    public FragmentOrderNew fragmentOrderNew;


    private void initViewPagerAndTabs() {


        fragmentChangeFilter = FragmentChangeFilter.createInstance(3310);
        fragmentOrderChange = FragmentCates.createInstance(3310);
        fragmentOrderRequest = FragmentOrderRequest.createInstance(331);
        fragmentOrderReject = FragmentOrderReject.createInstance(332);
        fragmentOrderSubmit = FragmentOrderSubmit.createInstance(333);
        fragmentOrderNew = FragmentOrderNew.createInstance(334);

        PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager());

        aTabLayout.addTab(aTabLayout.newTab().setText(getString(R.string.payment_order)));
        aTabLayout.addTab(aTabLayout.newTab().setText(getString(R.string.change_details_with_filter)));
        aTabLayout.addTab(aTabLayout.newTab().setText(getString(R.string.change_details)));
        aTabLayout.addTab(aTabLayout.newTab().setText(getString(R.string.cancel_order)));
        aTabLayout.addTab(aTabLayout.newTab().setText(getString(R.string.submit_order)));
        aTabLayout.addTab(aTabLayout.newTab().setText(getString(R.string.new_order)));

        pagerAdapter.addFragment(fragmentOrderRequest, getString(R.string.payment_order));
        pagerAdapter.addFragment(fragmentChangeFilter, getString(R.string.change_details_with_filter));
        pagerAdapter.addFragment(fragmentOrderChange, getString(R.string.change_details));
        pagerAdapter.addFragment(fragmentOrderReject, getString(R.string.cancel_order));
        pagerAdapter.addFragment(fragmentOrderSubmit, getString(R.string.submit_order));
        pagerAdapter.addFragment(fragmentOrderNew, getString(R.string.new_order));


        //Setting adapter

        viewPager.setAdapter(pagerAdapter);

        aTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        aTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        //aTabLayout.setupWithViewPager(viewPager);

        viewPager.setPagingEnabled(true);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(aTabLayout));

        viewPager.setOffscreenPageLimit(6);
        viewPager.setPagingEnabled(true);

        viewPager.setCurrentItem(5);


        aTabLayout.setOnTabSelectedListener(
                new TabLayout.ViewPagerOnTabSelectedListener(viewPager) {
                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        super.onTabSelected(tab);


                        switch (tab.getPosition()) {
                            case 5:
                                fragmentOrderNew.isInit=false;
                                fragmentOrderNew.init();
                                break;
                            case 4:
                                fragmentOrderSubmit.init();
                                break;
                            case 3:
                                fragmentOrderReject.init();
                                break;

                            case 2:
                                fragmentOrderChange.init();
                                break;
                            case 1:
                                fragmentChangeFilter.init();
                                break;

                            case 0:
                                fragmentOrderRequest.init();
                                break;
                        }

                    }
                });
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


        // if (viewPager.getCurrentItem() > 0)
        //  viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
        // else
        super.onBackPressed();

    }

    DFragmentAccess aDFragmentAccess;

    TextView noResTextView;
    ProgressBarCircularIndeterminate aProgressBar;
    LinearLayout reg, reg2;

    Access aAccess;


    class DFragmentAccess extends DialogFragment {


        public DFragmentAccess() {

        }


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            final View dialog = inflater.inflate(R.layout.dialogz_access_manager, container, false);

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
                    aDFragmentAccess.dismiss();

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
                    if (NetworkUtil.getConnectivityStatusString(context) != null)
                        aSoapObject = aConnectionPool.ConnectGetCodeForShowPrice(code);


                    if (aSoapObject == null) {
                        noServerResponse2(getString(R.string.server_not_response));
                        return;
                    }


                    System.out.println(aSoapObject);

                    final Access aAccess = new Gson().fromJson(aSoapObject.toString(), Access.class);

                    context.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            noServerResponse2(aAccess.description + "\n" + aAccess.code);
                        }
                    });


                } catch (Exception e) {
                    noServerResponse2(getString(R.string.server_not_response));
                    e.printStackTrace();
                }


            }
        });

    }

    private void noServerResponse2(final String msg) {

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


    public static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}
