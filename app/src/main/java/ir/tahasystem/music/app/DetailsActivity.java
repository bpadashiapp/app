package ir.tahasystem.music.app;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.johnpersano.supertoasts.SuperActivityToast;
import com.github.johnpersano.supertoasts.SuperToast;
import com.github.johnpersano.supertoasts.util.Style;
import com.squareup.picasso.Picasso;

import ir.onlinefood.app.factory.R;
import ir.tahasystem.music.app.DialogBox.LoadBox;
import ir.tahasystem.music.app.Model.BasketModel;
import ir.tahasystem.music.app.Model.Kala;
import ir.tahasystem.music.app.Model.LoginHolder;
import ir.tahasystem.music.app.Model.ModelHolder;
import ir.tahasystem.music.app.custom.CustomViewPager;
import ir.tahasystem.music.app.fragment.FragmentHome;
import ir.tahasystem.music.app.fragment.FragmentSubCates;


public class DetailsActivity extends AppCompatActivity {

    public static ProgressDialog aProgressDialog;
    public static Kala aKala;
    public Toolbar mToolbar;
    public static DetailsActivity context;
    public NavigationView navigationView;
    public static DrawerLayout drawerLayout;

    public static String path;
    public static String pathCache;
    public LoadBox aLoadBox;

    FragmentHome home;
    public CustomViewPager viewPager;

    public TextView subTitle;

    TabLayout aTabLayout;

    private static final int ASK_MULTIPLE_PERMISSION_REQUEST_CODE = 112;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        context = this;
        setTheme(R.style.AppThemeBlueMain);


        super.onCreate(savedInstanceState);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setContentView(R.layout.activity_details);

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

        ((TextView) this.findViewById(R.id.de_name)).setText(aKala.getName());
        ((TextView) this.findViewById(R.id.de_price)).setText(aKala.getPrice() + " " + getString(R.string.toman));
        ((TextView) this.findViewById(R.id.de_price)).setPaintFlags(((TextView) this.findViewById(R.id.de_price)).getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        ((TextView) this.findViewById(R.id.de_dprice)).setText(aKala.getPriceByDiscount() + " " + getString(R.string.toman));

        if (LoginHolder.getInstance().getLogin()!=null &&
                LoginHolder.getInstance().getLogin().showPrice && aKala.priceDetail != null)
            ((TextView) this.findViewById(R.id.de_description)).setText(Html.fromHtml(aKala.priceDetail));

        ((TextView) this.findViewById(R.id.de_description)).append(Html.fromHtml(aKala.description));
        // this.findViewById(R.id.price_detail_card).setVisibility(View.GONE);

        aKala.initprice=aKala.price;
        aKala.initpriceByDiscount=aKala.priceByDiscount;

        if (aKala.price == aKala.priceByDiscount || aKala.price==0)
            ((TextView) this.findViewById(R.id.de_price)).setVisibility(View.GONE);

        ImageView aImageView = (ImageView) this.findViewById(R.id.image);

        if (aKala.image != null) {

            Picasso.with(context)
                    .load(aKala.image).skipMemoryCache()
                    .into(aImageView);

            //ImageLoader.getInstance().displayImage(url, holder.aImageView, options);
        } else {
            aImageView.setImageResource(R.drawable.ic_launcher);
        }

        initToolbar();


        Button aButtonUp = (Button) findViewById(R.id.up);
        Button aButtonDown = (Button) findViewById(R.id.down);
        final TextView counterTxt = (TextView) findViewById(R.id.counter);

        counterTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DFragment alertdFragment = new DFragment(counterTxt);
                alertdFragment.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
                alertdFragment.show(getSupportFragmentManager(), "");
            }
        });

          if(aKala.count<aKala.minOrder)
            aKala.count=aKala.minOrder;;


        counterTxt.setText(String.valueOf(aKala.count) + " " + aKala.saleType);

        aButtonUp.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (aKala.count > aKala.unitsInStock) {
                    msg(getString(R.string.not_allowed));
                    return;
                }

                aKala.count = aKala.count + 1;
                counterTxt.setText(String.valueOf(aKala.count) + " " + aKala.saleType);


            }
        });

        aButtonDown.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (aKala.count <= aKala.minOrder) {
                    msg(getString(R.string.not_allowed));
                    return;
                } else if (aKala.unitsInStock < aKala.minOrder) {
                    msg(getString(R.string.no_store));
                    return;
                }

                aKala.count = aKala.count - 1;
                counterTxt.setText(String.valueOf(aKala.count) + " " + aKala.saleType);

            }
        });

        if (getIntent().getBooleanExtra("access", false))
            return;


        if (LoginHolder.getInstance().getLogin()==null ||
                !LoginHolder.getInstance().getLogin().showPrice) {
            this.findViewById(R.id.fab_layout).setVisibility(View.GONE);
            ((TextView) this.findViewById(R.id.de_price)).setVisibility(View.GONE);
            ((TextView) this.findViewById(R.id.de_dprice)).setVisibility(View.GONE);
        }

        if (FragmentSubCates.cateId==Values.takCatId) {
            this.findViewById(R.id.fab_layout).setVisibility(View.VISIBLE);
            ((TextView) this.findViewById(R.id.de_price)).setVisibility(View.VISIBLE);
            ((TextView) this.findViewById(R.id.de_dprice)).setVisibility(View.VISIBLE);
        }

        if(aKala.unitsInStock==0){

            this.findViewById(R.id.fab_layout).setVisibility(View.GONE);
            ((TextView) this.findViewById(R.id.de_price)).setVisibility(View.GONE);
            ((TextView) this.findViewById(R.id.de_dprice)).setVisibility(View.GONE);

        }


    }


    TextView aTextViewBasket;
    View mCustomViewMain;

    private void initToolbar() {

        if (context == null)
            return;


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

                startActivityForResult(new Intent(context, BasketActivity.class), 1);

            }
        });

        aTextViewBasket = (TextView) count.findViewById(R.id.action_buy_txt);

        if (aTextViewBasket != null)
            if (ModelHolder.getInstance().getBasketCount() > 0) {
                aTextViewBasket.setVisibility(View.VISIBLE);
                aTextViewBasket.setText(String.valueOf(ModelHolder.getInstance().getBasketCount()));
            } else {
                aTextViewBasket.setVisibility(View.INVISIBLE);
            }

        //##########BUY

        findViewById(R.id.fab_add_basket_new).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // if (ModelHolder.getInstance().getBasketCount()==0) {

                //  ManagerCall();
                // return;

                //}

                ani(v);

                for (BasketModel aBasketModel : ModelHolder.getInstance().getBasket())
                    if (aBasketModel.aKala.id == aKala.id) {
                        msg(getString(R.string.added_before));
                        return;
                    }

                if (aKala.unitsInStock < aKala.minOrder) {
                    context.msg(context.getString(R.string.no_store));
                    return;
                }

                ModelHolder.getInstance().addBasket(aKala);
                if (aTextViewBasket != null)
                    if (ModelHolder.getInstance().getBasketCount() > 0) {
                        aTextViewBasket.setVisibility(View.VISIBLE);
                        aTextViewBasket.setText(String.valueOf(ModelHolder.getInstance().getBasketCount()));
                        ani(aTextViewBasket);

                    } else {
                        aTextViewBasket.setVisibility(View.INVISIBLE);
                    }


            }
        });


    }

    public synchronized void ani(final View v) {

        Animation fadeIn = new AlphaAnimation(0.0f, 1.0f);
        fadeIn.setInterpolator(new DecelerateInterpolator());

        fadeIn.setDuration(1000);

        v.startAnimation(fadeIn);

    }

    public void msg(final String msg) {

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


    public void onDestroy() {
        super.onDestroy();
    }

    class DFragment extends DialogFragment {

        boolean enableChangeListener = true;
        TextView aTextViewCounter;


        public DFragment(final TextView aTextViewCounter) {

            this.aTextViewCounter = aTextViewCounter;

        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View dialog = inflater.inflate(R.layout.dialogz_count, container, false);

            final EditText aEdt = (EditText) dialog.findViewById(R.id.value);

            dialog.findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {



                    if (aEdt.getText().toString().trim() != null && aEdt.getText().toString().trim().length()>0&& Long.parseLong(aEdt.getText().toString().trim()) >= aKala.minOrder && aEdt.getText().toString().trim().length() > 0&& Long.parseLong(aEdt.getText().toString().trim()) < aKala.unitsInStock) {

                        dismiss();

                        long value = Long.parseLong(aEdt.getText().toString());
                        float frac = (float) value / (float) aKala.minOrder;

                        System.out.println(frac + "==" + Math.round(frac));

                        //  if(frac!=Math.round(frac)){
                        //  aEdt.setError(getString(R.string.not_accpet_by_this_value));
                        //  return;
                        // }

                        aTextViewCounter.setText(aEdt.getText().toString()+ " " + aKala.saleType);
                        aKala.count = Long.parseLong(aEdt.getText().toString());
                        aEdt.setError(null);



                    } else {
                        aEdt.setError(getString(R.string.not_accpet));
                    }

                }
            });


            return dialog;
        }
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
