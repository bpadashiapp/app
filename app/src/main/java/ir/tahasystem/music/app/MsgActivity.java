package ir.tahasystem.music.app;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
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
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Locale;
import java.util.UUID;

import ir.onlinefood.app.factory.R;
import ir.tahasystem.music.app.Model.BasketModel;
import ir.tahasystem.music.app.Model.Kala;
import ir.tahasystem.music.app.Model.LoginHolder;
import ir.tahasystem.music.app.Model.ModelHolder;
import ir.tahasystem.music.app.Model.ModelHolderService;
import ir.tahasystem.music.app.fragment.FragmentMsg;
import ir.tahasystem.music.app.services.ServicesSocket;


public class MsgActivity extends AppCompatActivity {

    Toolbar mToolbar;

    public static MsgActivity context;
    public FragmentMsg fragmentMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        context = this;

        setLang();
        setTheme(R.style.AppThemeBlue);
        super.onCreate(savedInstanceState);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_msg);


        initToolbar();

        fragmentMsg = FragmentMsg.createInstance(11);
        addFragment(fragmentMsg);

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
        View mCustomView = mInflater.inflate(R.layout.action_msg, null);
        mToolbar.addView(mCustomView, 0);


        final View count = mCustomView.findViewById(R.id.action_delete);
        count.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                if (fragmentMsg.recyclerAdapter != null) fragmentMsg.recyclerAdapter.clearItem();

                ModelHolderService.getInstance().setKalaListChat(context, new ArrayList<Kala>());

            }
        });

        final View countr = mCustomView.findViewById(R.id.action_send_msg);
        countr.setVisibility(View.GONE);
        countr.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                DFragmentCall alertdFragment = new DFragmentCall();
                alertdFragment.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
                alertdFragment.show(getSupportFragmentManager(), "");


            }
        });

        setTitle("");
        TextView atxt = (TextView) mToolbar.findViewById(R.id.toolbar_title);
        atxt.setText(getString(R.string.online_chat));

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
            MsgActivity.this.finish();
        } else if (menuItem.getItemId() == R.id.action_back) {
            MsgActivity.this. finish();
        }

        /*if(MainActivity.context==null) {
            Intent intent = new Intent(this,MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }*/

        return super.onOptionsItemSelected(menuItem);
    }

    public void onDestroy() {
        super.onDestroy();
        for (BasketModel aBasketModel : ModelHolder.getInstance().getBasket())
            aBasketModel.count = 0;
    }

    public class DFragmentCall extends DialogFragment {


        public DFragmentCall() {

        }


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

            getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            final View dialog = inflater.inflate(R.layout.dialogz_msg, container, false);

            final EditText editText = (EditText) dialog.findViewById(R.id.msg_edt);

            dialog.findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {



                    String msg = editText.getText().toString();

                    if (msg.trim().length() != 0) {

                        DFragmentCall.this.dismiss();

                        Kala aModel = new Kala();
                        aModel.uid = UUID.randomUUID().toString();
                        aModel.sender = LoginHolder.getInstance().getLogin().uid;
                        aModel.receiver = String.valueOf(Values.companyId);
                        aModel.date = "";
                        aModel.text = msg;
                        aModel.command = "get";
                        aModel.isRec = 0;
                        aModel.name = getString(R.string.app_name);


                        if (ServicesSocket.mWebSocketClient != null && ServicesSocket.mWebSocketClient.isOpen())
                            ServicesSocket.mWebSocketClient.send(new Gson().toJson(aModel).toString());
                    }

                }
            });


            return dialog;
        }
    }

}
