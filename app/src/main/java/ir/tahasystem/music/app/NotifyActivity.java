package ir.tahasystem.music.app;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
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
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

import ir.onlinefood.app.factory.R;
import ir.tahasystem.music.app.Model.BasketModel;
import ir.tahasystem.music.app.Model.Kala;
import ir.tahasystem.music.app.Model.ModelHolder;
import ir.tahasystem.music.app.Model.ModelHolderService;
import ir.tahasystem.music.app.fragment.FragmentNotify;


public class NotifyActivity extends AppCompatActivity {

    Toolbar mToolbar;

    public static NotifyActivity context;
    public FragmentNotify fragmentNotify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        context = this;

        setLang();
        setTheme(R.style.AppThemeBlue);
        super.onCreate(savedInstanceState);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_gallery);



        initToolbar();

        fragmentNotify = FragmentNotify.createInstance(11);
        addFragment(fragmentNotify);

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

    private void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        mToolbar.setTitleTextColor(getResources().getColor(android.R.color.white));

        LayoutInflater mInflater = LayoutInflater.from(context);
        View mCustomView = mInflater.inflate(R.layout.action_delete, null);
        mToolbar.addView(mCustomView, 0);


        final View count = mCustomView.findViewById(R.id.action_delete);
        count.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                if(fragmentNotify.recyclerAdapter!=null) fragmentNotify.recyclerAdapter.clearItem();

                List<Kala> eListKala= ModelHolderService.getInstance().getKalaList(context);

                for(Kala eKala:eListKala)
                eKala.isDelete=true;

                ModelHolderService.getInstance().setKalaList(context, eListKala);

            }
        });

        final View countr = mCustomView.findViewById(R.id.action_refresh);
        countr.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                if (fragmentNotify.recyclerAdapter != null)
                    fragmentNotify.recyclerAdapter.clearItem();

                fragmentNotify.getDataNotify();


            }
        });

        setTitle("");
        TextView atxt = (TextView) mToolbar.findViewById(R.id.toolbar_title);
        atxt.setText(getString(R.string.msg_and_notify));

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

}
