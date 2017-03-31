package ir.tahasystem.music.app;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.NavigationView;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.johnpersano.supertoasts.SuperActivityToast;
import com.github.johnpersano.supertoasts.SuperToast;
import com.github.johnpersano.supertoasts.util.Style;
import com.squareup.picasso.LruCache;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.Locale;

import ir.onlinefood.app.factory.R;
import ir.tahasystem.music.app.Model.LoginHolder;
import ir.tahasystem.music.app.database.DatabaseObj;
import ir.tahasystem.music.app.utils.SharedPref;


public class SettingActivity extends AppCompatActivity {


    Toolbar mToolbar;

    public static SettingActivity context;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private ViewPager viewPager;

    private Picasso picasso;
    private LruCache picassoLruCache;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        context = this;

        setLang();
        setTheme(R.style.AppThemeBlue);
        super.onCreate(savedInstanceState);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        setContentView(R.layout.activity_setting);

        final RadioButton aRadioButton = (RadioButton) findViewById(R.id.notify_full);
        aRadioButton.setChecked(SharedPref.readBoolTrue(context, "notifyfull"));
        aRadioButton.setTag("notifyfull");


        final RadioButton bRadioButton = (RadioButton) findViewById(R.id.notify_silent);
        bRadioButton.setChecked(SharedPref.readBool(context, "notifysilent"));
        aRadioButton.setTag("notifysilent");

        final RadioButton cRadioButton = (RadioButton) findViewById(R.id.notify_nothing);
        cRadioButton.setChecked(SharedPref.readBoolTrue(context, "notifynotihng"));
        aRadioButton.setTag("notifynotihng");


        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.rb);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                                                  public void onCheckedChanged(RadioGroup group, int checkedId) {

                                                      RadioButton checkedRadioButton = (RadioButton) group.findViewById(checkedId);


                                                      if (checkedId == R.id.notify_full) {
                                                          SharedPref.writeBool(context, "notifyfull", true);
                                                          SharedPref.writeBool(context, "notifysilent", false);
                                                          SharedPref.writeBool(context, "notifynotihng", false);
                                                      } else if (checkedId == R.id.notify_silent) {
                                                          SharedPref.writeBool(context, "notifyfull", false);
                                                          SharedPref.writeBool(context, "notifysilent", true);
                                                          SharedPref.writeBool(context, "notifynotihng", false);
                                                      } else if (checkedId == R.id.notify_nothing) {
                                                          SharedPref.writeBool(context, "notifyfull", false);
                                                          SharedPref.writeBool(context, "notifysilent", false);
                                                          SharedPref.writeBool(context, "notifynotihng", true);
                                                      }


                                                  }
                                              }

        );


        findViewById(R.id.reset)

                .

                        setOnClickListener(new View.OnClickListener() {
                                               @Override
                                               public void onClick(View v) {

                                                   DatabaseObj.getInstance(context).close();


                                                   File aFile = new File(
                                                           Environment.getExternalStorageDirectory() + File.separator + DatabaseObj.FILE_DIR + File.separator + context.getPackageName() + File.separator +
                                                                   DatabaseObj.DATABASE_NAME);
                                                   if (aFile.exists())
                                                       aFile.delete();

                                                   deleteDir(context.getCacheDir());


                                                   // msgOk(getString(R.string.done));


                                                   LoginHolder.getInstance().setLogin(null);

                                                   restartFirstActivity();


                                               }
                                           }

                        );


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, R.layout.notify_spinner, getResources().getStringArray(R.array.notify_array)) {
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                ((TextView) v).setGravity(Gravity.CENTER);
                return v;
            }

            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View v = super.getDropDownView(position, convertView, parent);
                ((TextView) v).setGravity(Gravity.CENTER);
                return v;
            }
        };
        final Spinner spinner = (Spinner) findViewById(R.id.spinner_notify);
        spinner.setAdapter(adapter);
        spinner.setSelection(SharedPref.readIntOne(context, "spinnernotify"));
        spinner.setOnTouchListener(new View.OnTouchListener()

                                   {
                                       @Override
                                       public boolean onTouch(View v, MotionEvent event) {
                                           spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                               @Override
                                               public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                                   SharedPref.write(context, "spinnernotify", position);

                                                   switch (position) {

                                                       case 0:
                                                           SharedPref.write(context, "notifytime", 5);
                                                           break;
                                                       case 1:
                                                           SharedPref.write(context, "notifytime", 15);
                                                           break;
                                                       case 2:
                                                           SharedPref.write(context, "notifytime", 60);
                                                           break;
                                                       case 3:
                                                           SharedPref.write(context, "notifytime", 1440);
                                                           break;
                                                       case 4:
                                                           SharedPref.write(context, "notifytime", Integer.MAX_VALUE);
                                                           break;
                                                       default:
                                                           SharedPref.write(context, "notifytime", 15);


                                                   }


                                               }

                                               @Override
                                               public void onNothingSelected(AdapterView<?> parent) {


                                               }
                                           });
                                           return false;
                                       }
                                   }

        );

        initToolbar();
        //initBottomBar(savedInstanceState);
    }

    private void restartFirstActivity() {

        finish();

        MainActivity.context.finish();


        Intent intent = new Intent(context, SplashActivity.class);
        finish();
        startActivity(intent);

    }

    public void msgOk(final String msg) {

        if (context == null)
            return;

        ((Activity) context).runOnUiThread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub

                final Style customStyle = new Style();
                customStyle.animations = SuperToast.Animations.SCALE;
                customStyle.background = SuperToast.Background.BLUE;
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

    public static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete();
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

        mToolbar.setTitleTextColor(getResources().getColor(android.R.color.white));

        setTitle("");
        TextView atxt = (TextView) mToolbar.findViewById(R.id.toolbar_title);
        atxt.setText(getString(R.string.setting));


        // disableScroll();

    }

    public void onResume() {
        super.onResume();

        //mBottomBar.setDefaultTabPosition(MainActivity.mBottomBarSelected);
    }


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

    public void onDestroy() {
        super.onDestroy();


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

   /* public void onComplete() {

        new Thread(new Runnable() {

            @Override
            public void run() {

                try {
                    Thread.sleep(4000);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                if (context != null)

                    context.runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            context.findViewById(R.id.intra).setVisibility(View.GONE);

                        }
                    });

            }
        }).start();

    }*/

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


}
