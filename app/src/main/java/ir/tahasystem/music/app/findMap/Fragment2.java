package ir.tahasystem.music.app.findMap;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gc.materialdesign.views.ProgressBarCircularIndeterminate;
import com.github.johnpersano.supertoasts.SuperActivityToast;
import com.github.johnpersano.supertoasts.SuperToast;
import com.github.johnpersano.supertoasts.util.Style;
import com.google.gson.Gson;

import org.ksoap2.serialization.SoapPrimitive;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

import ir.onlinefood.app.factory.R;
import ir.tahasystem.music.app.MainActivity;
import ir.tahasystem.music.app.Model.Company;
import ir.tahasystem.music.app.Model.LoginHolder;
import ir.tahasystem.music.app.Model.LoginModel;
import ir.tahasystem.music.app.Model.MapModel;
import ir.tahasystem.music.app.MyApplication;
import ir.tahasystem.music.app.ThreadPool.ConnectionThreadPool;
import ir.tahasystem.music.app.Values;
import ir.tahasystem.music.app.connection.ConnectionPool;
import ir.tahasystem.music.app.custom.EndlessRecyclerOnScrollListener;
import ir.tahasystem.music.app.utils.FontUtils;
import ir.tahasystem.music.app.utils.NetworkUtil;
import ir.tahasystem.music.app.utils.Serialize;
import ir.tahasystem.music.app.utils.SharedPref;
import ir.tahasystem.music.app.utils.Utils;
import it.gmariotti.recyclerview.adapter.ScaleInAnimatorAdapter;


public class Fragment2 extends Fragment implements LocationListener {

    public final static String ITEMS_COUNT_KEY = "PartThreeFragment$ItemsCount";
    FrameLayout aLayout;
    private Fragment2 context;
    private com.gc.materialdesign.views.ProgressBarIndeterminate aProgress;

    RecyclerView aRecyclerView;


    private boolean userIsInteracting;
    public int cateId;
    public int subCateId;
    private String searchTxt;

    RelativeLayout cardHolder;
    CardView cardView;
    LinearLayout searchBox;

    EditText sEdt;

    RelativeLayout noLoc;

    public boolean isGetLoc = false;

    TextView textView, cardHolderTxt;

    EditText yourEditText;

    ProgressBarCircularIndeterminate aProgressBar;

    public static Fragment2 createInstance(int itemsCount) {
        Fragment2 partThreeFragment = new Fragment2();
        Bundle bundle = new Bundle();
        bundle.putInt(ITEMS_COUNT_KEY, itemsCount);
        partThreeFragment.setArguments(bundle);
        return partThreeFragment;

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        this.context = this;
        isGetLoc = false;

        LoginHolder.getInstance().init(MyApplication.getContext());


        View aView = (View) inflater.inflate(R.layout.fragment_map_2, container, false);

        aProgressBar = (ProgressBarCircularIndeterminate) aView.findViewById(R.id.progress);

        locationManager = (LocationManager) aView.getContext().getSystemService(Context.LOCATION_SERVICE);

        aProgress = (com.gc.materialdesign.views.ProgressBarIndeterminate) aView.findViewById(R.id.list_load);

        aRecyclerView = (RecyclerView) aView.findViewById(R.id.recyclerView);

        noLoc = (RelativeLayout) aView.findViewById(R.id.no_loc);

        setupRecyclerView(aView, aRecyclerView);

        sEdt = (EditText) aView.findViewById(R.id.search_edt);

        cardHolderTxt = (TextView) aView.findViewById(R.id.card_holder_txt);

        aView.findViewById(R.id.search_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                searchTxt = sEdt.getText().toString();

                if (recyclerAdapter != null) recyclerAdapter.clearItem();
                count = 0;
                isFill = false;
                loading = true;

                hideKeyboardFrom(context.getActivity(), sEdt);

                getData(0, 10);

            }
        });

        sEdt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                    searchTxt = sEdt.getText().toString();

                    if (recyclerAdapter != null) recyclerAdapter.clearItem();
                    count = 0;
                    isFill = false;
                    loading = true;

                    hideKeyboardFrom(context.getActivity(), sEdt);

                    getData(0, 10);

                    return true;
                }
                return false;
            }
        });

        cardHolder = (RelativeLayout) aView.findViewById(R.id.card_holder);
        cardView = (CardView) aView.findViewById(R.id.card);
        searchBox = (LinearLayout) aView.findViewById(R.id.search_box);

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                swip();

            }
        });


        aView.findViewById(R.id.search_by_auto).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                isGetLoc = true;

                isRun = false;

                Values.lat = 0;
                Values.lng = 0;

                HideShow(View.VISIBLE, View.GONE);

                getMyLocation();

                // Intent intent = new Intent(Fragment2.this.getActivity(), MainActivity.class);
                // startActivity(intent);

                // FindMapActivity.context.finish();


            }
        });

        textView = (TextView) aView.findViewById(R.id.no_loc_txt);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        textView.setLinkTextColor(Color.BLUE);

        final String lats = SharedPref.read(context.getActivity(), "lats");
        final String lngs = SharedPref.read(context.getActivity(), "lngs");

        if (lats != null && lngs != null && lats.length() != 0 && lngs.length() != 0) {

            aView.findViewById(R.id.search_by_auto_last_loc_layout).setVisibility(View.VISIBLE);

        } else {
            aView.findViewById(R.id.search_by_auto_last_loc_layout).setVisibility(View.GONE);
        }

        aView.findViewById(R.id.search_by_auto_last_loc).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                isGetLoc = true;

                Values.lat = 0;
                Values.lng = 0;

                getData(Double.parseDouble(lats), Double.parseDouble(lngs));

                // HideShow(View.VISIBLE, View.GONE);


                // Intent intent = new Intent(Fragment2.this.getActivity(), MainActivity.class);
                // startActivity(intent);

                // FindMapActivity.context.finish();


            }
        });

        HideShow(View.GONE, View.VISIBLE);


        yourEditText = (EditText) aView.findViewById(R.id.search_edt);

        if (!Values.hasGetLoc && lats != null && lngs != null && lats.length() != 0 && lngs.length() != 0) {

            aProgressBar.setVisibility(View.VISIBLE);

            aView.findViewById(R.id.layout_search).setVisibility(View.GONE);
            cardHolder.setVisibility(View.GONE);
            cardView.setVisibility(View.GONE);
            cardHolderTxt.setVisibility(View.GONE);

            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
            getData(Double.parseDouble(lats), Double.parseDouble(lngs));


        } else {

            searchBox.setVisibility(View.VISIBLE);
            cardHolder.setVisibility(View.GONE);
            cardView.setVisibility(View.GONE);
            cardHolderTxt.setVisibility(View.GONE);
            cardView.setOnClickListener(null);

            yourEditText.requestFocus();
            InputMethodManager imm = (InputMethodManager) Fragment2.this.getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(yourEditText, InputMethodManager.SHOW_IMPLICIT);


        }


        return aView;
    }


    public void onResume() {
        super.onResume();

    }

    public boolean isInit = false;

    public void init() {

        if (isInit || context == null)
            return;

        isInit = true;


    }

    public void swip() {


        AnimationSet animationSet = new AnimationSet(true);

        DisplayMetrics metrics = new DisplayMetrics();
        this.getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);

        int height = metrics.heightPixels;
        int width = metrics.widthPixels;


        TranslateAnimation trans = new TranslateAnimation(0, 0, 0, (-height / 2) + PxToDpi(48) + PxToDpi(30));
        trans.setDuration(1000);
        trans.setFillAfter(true);


        final AlphaAnimation fin = new AlphaAnimation(0f, 1f);
        fin.setDuration(100);
        fin.setFillBefore(true);
        fin.setFillAfter(true);

        final AlphaAnimation fout = new AlphaAnimation(1f, 0f);
        fout.setDuration(100);
        fout.setFillAfter(true);

        final AlphaAnimation fout2 = new AlphaAnimation(1f, 0f);
        fout2.setDuration(200);
        fout2.setStartOffset(700);
        fout2.setFillAfter(true);


        animationSet.addAnimation(trans);
        animationSet.addAnimation(fout2);

        // RelativeLayout cardHolder ;
        //  CardView cardView ;
        // LinearLayout searchBox ;

        animationSet.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationEnd(Animation arg0) {
                // start animation1 when animation2 ends (repeat)
                searchBox.startAnimation(fin);
                cardHolder.startAnimation(fout);

                yourEditText = (EditText) context.getView().findViewById(R.id.search_edt);
                yourEditText.requestFocus();
                InputMethodManager imm = (InputMethodManager) Fragment2.this.getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(yourEditText, InputMethodManager.SHOW_IMPLICIT);

            }

            @Override
            public void onAnimationRepeat(Animation arg0) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationStart(Animation arg0) {
                // TODO Auto-generated method stub

            }

        });

        fin.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationEnd(Animation arg0) {
                // start animation1 when animation2 ends (repeat)
                searchBox.setVisibility(View.VISIBLE);
                cardHolder.setVisibility(View.GONE);
                cardView.setVisibility(View.GONE);
                cardHolderTxt.setVisibility(View.GONE);
                cardView.setOnClickListener(null);
            }

            @Override
            public void onAnimationRepeat(Animation arg0) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationStart(Animation arg0) {

                searchBox.setVisibility(View.VISIBLE);
                cardHolder.setVisibility(View.VISIBLE);

            }

        });

        cardView.startAnimation(animationSet);


    }

    public int PxToDpi(float dipValue) {
        Resources r = context.getResources();
        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, r.getDisplayMetrics());
        return px;

    }


   /* private OnCompleteListener mListener;

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            this.mListener = (OnCompleteListener) activity;
        } catch (final ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnCompleteListener");
        }
    }


    public void onResume() {
        super.onResume();

        mListener.onComplete();
    }*/


    public void onStart() {
        super.onStart();

        // if (aListKala == null || aListKala.size() == 0)
        // getData(0, 10);
    }

    private boolean loading = true;
    private int count = 0;
    RecyclerAdapter2 recyclerAdapter;

    LinearLayoutManager mLayoutManager;


    private void setupRecyclerView(final View aView, final RecyclerView recyclerView) {

        int paddingTop = Utils.getToolbarHeight(this.getActivity()) + Utils.getTabsHeight(this.getActivity());
        recyclerView.setPadding(recyclerView.getPaddingLeft(), 0, recyclerView.getPaddingRight(),
                recyclerView.getPaddingBottom());

        mLayoutManager = new LinearLayoutManager(getActivity());

        recyclerView.setLayoutManager(mLayoutManager);
        recyclerAdapter = new RecyclerAdapter2(this, new ArrayList<Address>());

        //recyclerView.setItemAnimator(new ScaleInAnimator());
        //ScaleInAnimationAdapter alphaAdapter = new ScaleInAnimationAdapter(recyclerAdapter);
        //alphaAdapter.setFirstOnly(false);
        //alphaAdapter.setDuration(1000);
        //alphaAdapter.setInterpolator(new OvershootInterpolator(.10f));
        //recyclerView.setAdapter(recyclerAdapter);

        ScaleInAnimatorAdapter animatorAdapter = new ScaleInAnimatorAdapter(recyclerAdapter, recyclerView);
        recyclerView.setAdapter(animatorAdapter);

        // int aVisibleItemCount = mLayoutManager.getChildCount();
        recyclerView.scrollToPosition(count - 10);

        recyclerView.setOnScrollListener(new EndlessRecyclerOnScrollListener(mLayoutManager) {
            @Override
            public void onLoadMore(int current_page) {

                // count = count + 10;

                // getData(count, count + 10);
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int topRowVerticalPosition = (recyclerView == null || recyclerView.getChildCount() == 0) ? 0
                        : recyclerView.getChildAt(0).getTop();
                // refreshLayout.setEnabled(topRowVerticalPosition >= 0);

                if (dy > 0) // check for scroll down
                {
                    int visibleItemCount = mLayoutManager.getChildCount();
                    int totalItemCount = mLayoutManager.getItemCount();
                    int pastVisiblesItems = mLayoutManager.findFirstVisibleItemPosition();

                    if (loading) {
                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {

                            loading = false;
                            count = count + 10;
                            getData(count, count + 10);

                        }
                    }
                }

            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

            }
        });


        // boolean pauseOnScroll = true; // or true
        // boolean pauseOnFling = true; // or false
        // NewPauseOnScrollListener listener = new NewPauseOnScrollListener(ImageLoader.getInstance(), pauseOnScroll, pauseOnFling);
        //recyclerView.addOnScrollListener(listener);

    }

    private void HideShow(final int pro, final int fab) {

        if (context == null || context.getActivity() == null || context.getView() == null || !isAdded())
            return;

        final View aView = context.getView();

        context.getActivity().runOnUiThread(new Runnable() {

            @Override
            public void run() {

                if (aProgress != null) aProgress.setVisibility(pro);

                if (pro == View.VISIBLE)
                    noLoc.setVisibility(View.GONE);


            }
        });
    }

    boolean isFill = false;
    private String idz;


    private void getData(final int page, final int limit) {

        HideShow(View.VISIBLE, View.GONE);

        ConnectionThreadPool.getInstance().AddTask(new Runnable() {

            @Override
            public void run() {


                List<Address> cListKala = (List<Address>) new Serialize().readFromFile(context.getActivity(),
                        "aListKalaAddress" + searchTxt);

                if (cListKala != null) {
                    // aListKala.add(cListKala);
                    isFill = true;
                    setupView(cListKala, cListKala.size());

                } else {
                    isFill = false;
                    if (NetworkUtil.getConnectivityStatusString(context.getActivity()) == null)
                        noServerResponse();

                }

                try {

                   /* ConnectionPool aConnectionPool = new ConnectionPool();

                    SoapObject aSoapObject = null;
                    if (NetworkUtil.getConnectivityStatusString(MainActivity.context) != null)
                        aSoapObject = aConnectionPool.ConnectGetCates(Values.companyId);


                    if (aSoapObject == null) {
                        noServerResponse();
                        return;
                    }*/


                    List<Address> bListKala = new ArrayList<Address>();

                    Locale lfa = new Locale("fa");
                    Geocoder geocoder = new Geocoder(FindMapActivity.context, lfa);
                    bListKala = geocoder.getFromLocationName(searchTxt, 100);


                    loading = true;

                    if (bListKala.size() != 0) {


                        if (!isFill) {
                            // aListKala.add(bListKala);
                            setupView(bListKala, bListKala.size());

                        } else {
                            HideShow(View.GONE, View.VISIBLE);
                        }

                        new Serialize().saveToFile(context.getActivity(), bListKala,
                                "aListKalaAddress" + searchTxt);

                    } else {
                        HideShow(View.VISIBLE, View.GONE);
                    }

                } catch (Exception e) {
                    noServerResponse();
                    e.printStackTrace();
                }

            }
        });

    }

    public void getData(final double lat, final double lng) {

        HideShow(View.VISIBLE, View.GONE);

        ConnectionThreadPool.getInstance().AddTask(new Runnable() {

            @Override
            public void run() {

                try {


                    //###

                    ConnectionPool aConnectionPool = new ConnectionPool();
                    SoapPrimitive aSoapObject = null;


                    if (NetworkUtil.getConnectivityStatusString(MainActivity.context) != null)
                        aSoapObject = aConnectionPool.ConnectGetCompanyInfo(Values.companyId);
                    else
                        noServerResponse();


                    if (aSoapObject == null || aSoapObject.toString() == null) {
                        noServerResponse();
                        return;
                    }

                    final Company aCompany = new Gson().fromJson(aSoapObject.toString(), Company.class);

                    Values.company=aCompany;

                    Values.phone = aCompany.phone;
                    Values.companyName=aCompany.companyName;





                    LoginModel aLoginModel = LoginHolder.getInstance().getLogin();

                    if (aLoginModel == null)
                        aLoginModel = new LoginModel();

                    Values.takCatId=aCompany.takCatId;

                    aLoginModel.showPrice = aCompany.showPrice;
                    aLoginModel.hasRole = aCompany.IsManufacture;
                    aLoginModel.accessType = aCompany.AccessType;

                    LoginHolder.getInstance().setLogin(aLoginModel);


                    System.out.println("hasRole-ACCESS F->" + aLoginModel.hasRole + "-" + aLoginModel.fullAccess);

                    if (aCompany.companyId == -1) {

                        if (context == null || context.getActivity() == null || context.getView() == null || !isAdded())
                            return;

                        context.getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {


                                AlertDialog.Builder builder = new AlertDialog.Builder(context.getActivity(), R.style.MyAlertDialogStyle);

                                Typeface typeface = Typeface.createFromAsset(context.getActivity().getAssets(), "irfontnumbold.ttf");

                                builder.setTitle(FontUtils.typeface(typeface, context.getString(R.string.app_name)));
                                builder.setMessage(FontUtils.typeface(typeface, aCompany.companyName));
                                builder.setCancelable(false);

                                builder.setPositiveButton(context.getString(R.string.close), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        context.getActivity().finish();
                                    }
                                });


                                AlertDialog alertDialog = builder.create();
                                alertDialog.show();
                            }
                        });

                        return;
                    }


                    //###

                    SoapPrimitive aSoapPrimitive;

                    if (NetworkUtil.getConnectivityStatusString(context.getActivity()) == null) {
                        noServerResponse();
                        return;
                    }

                    aConnectionPool = new ConnectionPool();
                    aSoapPrimitive = aConnectionPool.ConnectGetShippingCostOfDistance(Values.companyId,
                            lat,
                            lng);

                    String[] distanceCost = aSoapPrimitive.toString().split(Pattern.quote("_"));

                    if (distanceCost[1] != null && Integer.parseInt(distanceCost[1]) < 0) {
                        notAvalible(distanceCost[0], lat, lng);

                        SharedPref.write(context.getActivity(), "lats", "");
                        SharedPref.write(context.getActivity(), "lngs", "");

                    } else
                        setupViewFinal(lat, lng);

                } catch (Exception e) {
                    noServerResponse();
                    e.printStackTrace();
                }

            }
        });

    }

    public void setupViewFinal(final double lat, final double lng) {

        if (context == null || context.getActivity() == null || context.getView() == null || !isAdded())
            return;

        ((Activity) context.getActivity()).runOnUiThread(new Runnable() {

            @Override
            public void run() {

                HideShow(View.VISIBLE, View.GONE);
                aProgressBar.setVisibility(View.GONE);


                SharedPref.write(context.getActivity(), "lats", String.valueOf(lat));
                SharedPref.write(context.getActivity(), "lngs", String.valueOf(lng));


                MapModel mapModel = new MapModel();

                mapModel.latHome = lat;
                mapModel.lngHome = lng;

                Values.lat = lat;
                Values.lng = lng;

                if (context == null || context.getActivity() == null || context.getView() == null || !isAdded())
                    return;

                new Serialize().saveToFile(context.getActivity(), mapModel,
                        "aListKalaMapUser");

                if (context == null || context.getActivity() == null || context.getView() == null || !isAdded())
                    return;

                context.getActivity().runOnUiThread(new Runnable() {

                    @Override
                    public void run() {

                        hideKeyboardFrom(context.getActivity(), yourEditText);
                    }
                });

                if (FindMapActivity.context == null)
                    return;

                LoginHolder.getInstance().init(FindMapActivity.context);

                if (LoginHolder.getInstance().getLogin() != null && LoginHolder.getInstance().getLogin().uid!=null) {
                    // getDataAllTime();

                    startActivity(new Intent(context.getActivity(), MainActivity.class));
                    FindMapActivity.context.finish();
                } else if (FindMapActivity.viewPager != null) {
                    //FindMapActivity.viewPager.setCurrentItem(1);
                    startActivity(new Intent(context.getActivity(), MainActivity.class));
                    FindMapActivity.context.finish();
                }
            }
        });

    }

    public void msg(final String msg) {

        if (context == null || context.getActivity() == null || context.getView() == null || !isAdded())
            return;

        ((Activity) context.getActivity()).runOnUiThread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub

                final Style customStyle = new Style();
                customStyle.animations = SuperToast.Animations.SCALE;
                customStyle.background = SuperToast.Background.RED;
                customStyle.textColor = Color.parseColor("#ffffff");
                customStyle.buttonTextColor = Color.WHITE;
                customStyle.dividerColor = Color.WHITE;

                SuperActivityToast superActivityToast = new SuperActivityToast(context.getActivity(), customStyle);
                superActivityToast.setDuration(SuperToast.Duration.MEDIUM);
                superActivityToast.setText(msg);
                superActivityToast.show();

            }
        });

    }

    public void notAvalible(final String msg, final double lat, final double lng) {

        if (context == null || context.getActivity() == null || context.getView() == null || !isAdded())
            return;

        ((Activity) context.getActivity()).runOnUiThread(new Runnable() {

            @Override
            public void run() {


                AlertDialog.Builder builder = new AlertDialog.Builder(context.getActivity(), R.style.MyAlertDialogStyle);

                Typeface typeface = Typeface.createFromAsset(context.getActivity().getAssets(), "irfontnumbold.ttf");

                builder.setTitle(FontUtils.typeface(typeface, context.getString(R.string.app_name)));
                builder.setMessage(FontUtils.typeface(typeface, msg));
                builder.setCancelable(true);

                builder.setPositiveButton(context.getString(R.string.close), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        context.getActivity().finish();

                    }
                });

                builder.setNegativeButton(context.getString(R.string.goon), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        setupViewFinal(lat, lng);

                    }
                });


                AlertDialog alertDialog = builder.create();
                alertDialog.show();

            }
        });

    }

    private void getDataAllTime() {

       /* ConnectionThreadPool.getInstance().

                AddTask(new Runnable() {

                            @Override
                            public void run() {

                                try {

                                    ConnectionPool aConnectionPool = new ConnectionPool();


                                    LoginModel aLoginModel = LoginHolder.getInstance().getLogin();
                                    //aLoginModel.uid = mobile;


                                    SoapPrimitive bSoapObject = null;
                                    if (NetworkUtil.getConnectivityStatusString(context.getActivity()) != null)
                                        bSoapObject = aConnectionPool.ConnectIsManufacture(aLoginModel.uid);


                                    if (bSoapObject == null) {
                                        startActivity(new Intent(context.getActivity(), MainActivity.class));
                                        FindMapActivity.context.finish();
                                        return;
                                    }

                                    IsManu isManu = new Gson().fromJson(bSoapObject.toString(), IsManu.class);

                                    // aLoginModel.hasRole = isManu.IsManufacture;
                                    aLoginModel.fullAccess = isManu.FullAccess;


                                    System.out.println("hasRole-ACCESS->" + aLoginModel.hasRole + "-" + aLoginModel.fullAccess);

                                    LoginHolder.getInstance().setLogin(aLoginModel);
                                    context.getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {

                                            //initLogin();

                                            // fragmentHomeSmall.isInit = false;
                                            //fragmentHomeSmall.init();

                                            // fragmentHomeBig.isInit = false;
                                            // fragmentHomeBig.init();

                                            // getSupportFragmentManager().beginTransaction().remove(fragmentCates).commit();
                                            //fragmentCates = FragmentCates.createInstance(11);
                                            //addFragment(fragmentCates, fragmentCates.getClass().getName());
                                        }
                                    });


                                    // noServerResponse(context.getString(R.string.is_reg));


                                    startActivity(new Intent(context.getActivity(), MainActivity.class));
                                    FindMapActivity.context.finish();


                                    //initService();


                                } catch (Exception e) {
                                    e.printStackTrace();
                                }


                            }
                        }

                );*/

    }


    public void setupViewNoLoc(final String msg) {

        if (context == null || context.getActivity() == null || context.getView() == null || !isAdded())
            return;

        context.getActivity().runOnUiThread(new Runnable() {

            @Override
            public void run() {

                noLoc.setVisibility(View.VISIBLE);
                HideShow(View.GONE, View.VISIBLE);

                recyclerAdapter.clearItem();

                textView.setText(Html.fromHtml(msg +
                        "<br>" + getString(R.string.no_loc_goto_bazzar) + "<br>" +
                        "<a href=\"https://cafebazaar.ir/developer/alireza7200/?l=fa\">لینک ما در کافه بازار</a> "));

                textView.setMovementMethod(LinkMovementMethod.getInstance());
                textView.setLinkTextColor(Color.BLUE);


            }
        });
    }


    public void setupView(final List<Address> aList, final int length) {

        if (context == null || context.getActivity() == null || context.getView() == null || !isAdded())
            return;


        context.getActivity().runOnUiThread(new Runnable() {

            @Override
            public void run() {

                // if (aListKala == null)
                // return;

                if (length > 0) {

                    recyclerAdapter.addItem(aList, count);
                } else {

                    noLoc.setVisibility(View.VISIBLE);
                }

                loading = true;

                HideShow(View.GONE, View.VISIBLE);
                // aLayout.setVisibility(View.VISIBLE);
            }
        });
    }


    public void load() {

        if (recyclerAdapter != null) recyclerAdapter.clearItem();
        // aListKala.clear();
        count = 0;
        isFill = false;
        loading = true;

        // setupRecyclerView(context.getView(), (RecyclerView)
        // context.getView().findViewById(R.id.recyclerView));

        getData(0, 10);

    }

    Snackbar snackbar;

    public void noServerResponse() {

        HideShow(View.GONE, View.VISIBLE);

        if (context == null || context.getActivity() == null || context.getView() == null || !isAdded())
            return;

        ((Activity) context.getActivity()).runOnUiThread(new Runnable() {

            @Override
            public void run() {

                aProgressBar.setVisibility(View.GONE);

                snackbar = Snackbar
                        .make(context.getView().findViewById(R.id.layout), getString(R.string.server_not_response), Snackbar.LENGTH_INDEFINITE)
                        .setAction(getString(R.string.retry), new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                snackbar.dismiss();

                                //MainActivity.context.mToolbarContainer.setExpanded(true);
                                // HideShow(View.VISIBLE, View.VISIBLE);
                                // getData(count, count + 10);


                            }
                        });

                if (recyclerAdapter.getItemCount() != 0)
                    snackbar.show();
                else
                    context.getView().findViewById(R.id.no_server_response).setVisibility(View.VISIBLE);


                // MainActivity.context.mToolbarContainer.setExpanded(true);

                context.getView().findViewById(R.id.no_server_response_retry).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        context.getView().findViewById(R.id.no_server_response).setVisibility(View.GONE);
                        //MainActivity.context.mToolbarContainer.setExpanded(true);
                        // HideShow(View.VISIBLE, View.GONE);
                        // getData(count, count + 10);

                        if (!Values.hasGetLoc) {

                            final String lats = SharedPref.read(context.getActivity(), "lats");
                            final String lngs = SharedPref.read(context.getActivity(), "lngs");

                            if (lats != null && lngs != null && lats.length() != 0 && lngs.length() != 0) {
                                getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
                                getData(Double.parseDouble(lats), Double.parseDouble(lngs));
                            }

                        }

                    }
                });

            }
        });

    }

    public static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    public void clear() {

        if (recyclerAdapter != null) recyclerAdapter.clearItem();
        count = 0;
        isFill = false;
        loading = true;

    }

    private boolean isGPSEnabled, isNetworkEnabled;
    private boolean canGetLocation;
    LocationManager locationManager;

    private static final long MIN_TIME_BW_UPDATES = 10000;
    private static final float MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;


    Location location;

    public void getMyLocation() {
        try {


            //  System.out.println("GPS-->"+location);


            // getting GPS status
            isGPSEnabled = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);

            // getting network status
            isNetworkEnabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            System.out.println("GPS->" + isGPSEnabled + "||" + isNetworkEnabled);


            if (!isGPSEnabled) {

                GpsBox();

            } else {

                if (alertDialog != null && alertDialog.isShowing())
                    alertDialog.dismiss();

                this.canGetLocation = true;

                if (context == null || context.getActivity() == null || context.getView() == null || !isAdded())
                    return;

                if (ActivityCompat.checkSelfPermission(context.getActivity(),
                        Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(context.getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                }


                if (isNetworkEnabled) {

                    locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, context);

                    if (locationManager != null)
                        onLocationChanged(locationManager
                                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER));

                }
                synchronized (this) {
                    if (isGPSEnabled) {
                        if (location == null) {
                            locationManager.requestLocationUpdates(
                                    LocationManager.GPS_PROVIDER,
                                    MIN_TIME_BW_UPDATES,
                                    MIN_DISTANCE_CHANGE_FOR_UPDATES, context);

                            if (locationManager != null)
                                onLocationChanged(locationManager
                                        .getLastKnownLocation(LocationManager.GPS_PROVIDER));

                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isRun = false;

    @Override
    public void onLocationChanged(final Location location) {


        synchronized (this) {


            if (location != null && !isRun) {

                isRun = true;

                getData(location.getLatitude(), location.getLongitude());

            }

        }


    }

    private String getCompleteAddressString(Address returnedAddress) {

        StringBuilder strReturnedAddress = new StringBuilder("");

        for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
            strReturnedAddress.append(returnedAddress.getAddressLine(i)).append(" ");
        }

        return strReturnedAddress.toString();

    }


    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }


    AlertDialog alertDialog;

    public void GpsBox() {

        if (context == null || context.getActivity() == null || context.getView() == null || !isAdded())
            return;

        AlertDialog.Builder builder = new AlertDialog.Builder(context.getActivity(), R.style.MyAlertDialogStyle);

        Typeface typeface = Typeface.createFromAsset(context.getActivity().getAssets(), "irfontnumbold.ttf");

        builder.setTitle(FontUtils.typeface(typeface, context.getString(R.string.gps)));
        builder.setMessage(FontUtils.typeface(typeface, context.getString(R.string.gps_not_enabled)));
        builder.setCancelable(true);

        builder.setPositiveButton(context.getString(R.string.enable), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                alertDialog.dismiss();

                Intent gpsOptionsIntent = new Intent(
                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(gpsOptionsIntent, 1000);

            }
        });


        if (alertDialog != null && alertDialog.isShowing())
            return;

        alertDialog = builder.create();
        alertDialog.show();
    }

}
