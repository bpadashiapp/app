package ir.tahasystem.music.app;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.johnpersano.supertoasts.SuperActivityToast;
import com.github.johnpersano.supertoasts.SuperToast;
import com.github.johnpersano.supertoasts.util.Style;

import java.util.Locale;

import ir.tahasystem.music.app.Model.BasketModel;
import ir.tahasystem.music.app.Model.LoginHolder;
import ir.tahasystem.music.app.Model.ModelHolder;
import ir.tahasystem.music.app.fragment.FragmentHomeCates;
import ir.onlinefood.app.factory.R;


public class CatesActivity extends AppCompatActivity {

    Toolbar mToolbar;

    public static CatesActivity context;
    FragmentHomeCates fragmentHomeCates;
    public static TextView subTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        context = this;

        setLang();
        setTheme(R.style.AppThemeBlue);
        super.onCreate(savedInstanceState);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_cates);



        initToolbar();

        fragmentHomeCates =FragmentHomeCates.createInstance(11);
        addFragment(fragmentHomeCates);

    }

    public void addFragment(Fragment fragment) {

        FragmentManager fragMan = getSupportFragmentManager();
        FragmentTransaction fragTransaction = fragMan.beginTransaction();
        fragTransaction.add(R.id.fragment, fragment);
        fragTransaction.commit();

    }


    public void setLang() {

        Locale locale = new Locale("en");
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());

    }

    public TextView aTextViewBasket;
    View mCustomViewMain;

    private void initToolbar() {

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

                finish();

                return false;

            }
        });


        LayoutInflater mInflater = LayoutInflater.from(context);
        if (mCustomViewMain == null)
            mCustomViewMain = mInflater.inflate(R.layout.action_all_layout_des, null);
        mToolbar.addView(mCustomViewMain);

        View count = mCustomViewMain.findViewById(R.id.action_buy);
        count.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {


                if(!Values.company.basket){
                    MainActivity.UpgradeMsg();
                    return;
                }

                //if(LoginHolder.getInstance().getLogin()==null || !LoginHolder.getInstance().getLogin().hasAccess()){
                  //  ManagerCall();
               // }else {
                    startActivityForResult(new Intent(CatesActivity.this, BasketActivity.class), 1);
               // }


            }
        });

        subTitle = (TextView) mCustomViewMain.findViewById(R.id.toolbar_title);
        subTitle.setText(getString(R.string.subcates));

        aTextViewBasket = (TextView) count.findViewById(R.id.action_buy_txt);

        if (aTextViewBasket != null)
            if (ModelHolder.getInstance().getBasketCount() > 0) {
                aTextViewBasket.setVisibility(View.VISIBLE);
                aTextViewBasket.setText(String.valueOf(ModelHolder.getInstance().getBasketCount()));
            } else {
                aTextViewBasket.setVisibility(View.INVISIBLE);
            }


    }

    public void onResume() {
        super.onResume();

        if (aTextViewBasket != null)
            if (ModelHolder.getInstance().getBasketCount() > 0) {
                aTextViewBasket.setVisibility(View.VISIBLE);
                aTextViewBasket.setText(String.valueOf(ModelHolder.getInstance().getBasketCount()));
            } else {
                aTextViewBasket.setVisibility(View.INVISIBLE);
            }
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

                SuperActivityToast superActivityToast = new SuperActivityToast(context, customStyle);
                superActivityToast.setDuration(SuperToast.Duration.MEDIUM);
                superActivityToast.setText(msg);
                superActivityToast.show();

            }
        });

    }

    public void onBackPressed(){

        if(FragmentHomeCates.context!=null && FragmentHomeCates.context.viewPager.getCurrentItem()>0)
            FragmentHomeCates.context.viewPager.setCurrentItem(FragmentHomeCates.context.viewPager.getCurrentItem()-1);
        else
            super.onBackPressed();
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

}
