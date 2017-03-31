package ir.tahasystem.music.app.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.NumberPicker;


import org.ksoap2.serialization.SoapObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import ir.onlinefood.app.factory.R;
import ir.tahasystem.music.app.MainActivity;
import ir.tahasystem.music.app.Model.Kala;
import ir.tahasystem.music.app.Model.ModelHolder;
import ir.tahasystem.music.app.ThreadPool.ConnectionThreadPool;
import ir.tahasystem.music.app.Values;
import ir.tahasystem.music.app.connection.ConnectionPool;
import ir.tahasystem.music.app.custom.CustomLayoutManager;
import ir.tahasystem.music.app.custom.EndlessRecyclerOnScrollListener;
import ir.tahasystem.music.app.custom.OnCompleteListener;
import ir.tahasystem.music.app.utils.NetworkUtil;
import ir.tahasystem.music.app.utils.Utils;
import it.gmariotti.recyclerview.adapter.ScaleInAnimatorAdapter;


public class FragmentBasket2 extends Fragment {

    public final static String ITEMS_COUNT_KEY = "PartThreeFragment$ItemsCount";
    FrameLayout aLayout;
    private FragmentBasket2 context;
    private com.gc.materialdesign.views.ProgressBarIndeterminate aProgress;

    RecyclerView aRecyclerViewH, aRecyclerViewV;


    private boolean userIsInteracting;
    public int cateId;
    public int subCateId;
    private OnCompleteListener mListener;
    CardView cardView;

    public int vSelect,hSelect;

    public static FragmentBasket2 createInstance(int itemsCount) {
        FragmentBasket2 partThreeFragment = new FragmentBasket2();
        Bundle bundle = new Bundle();
        bundle.putInt(ITEMS_COUNT_KEY, itemsCount);
        partThreeFragment.setArguments(bundle);
        return partThreeFragment;

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        this.context = this;

        View aView = (View) inflater.inflate(R.layout.fragment_basket2, container, false);

        final SwipeRefreshLayout refreshLayout = (SwipeRefreshLayout) aView.findViewById(R.id.swipe_layout);
        refreshLayout.setColorSchemeColors(Color.BLUE);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                if (NetworkUtil.getConnectivityStatusString(MainActivity.context) == null) {
                    refreshLayout.setRefreshing(false);
                    return;
                }
                refreshLayout.setRefreshing(false);
                if (recyclerAdapterV != null) recyclerAdapterV.clearItem();

                getDataV();

            }
        });

        aView.findViewById(R.id.search_in_list).setVisibility(View.GONE);


        aProgress = (com.gc.materialdesign.views.ProgressBarIndeterminate) aView.findViewById(R.id.list_load);


        aRecyclerViewH = (RecyclerView) aView.findViewById(R.id.recyclerViewH);
        aRecyclerViewV = (RecyclerView) aView.findViewById(R.id.recyclerViewV);


        setupRecyclerViewH(aView, aRecyclerViewH);
        setupRecyclerViewV(aView, aRecyclerViewV);

         cardView = (CardView) aView.findViewById(R.id.card_picker);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogPicker();
            }
        });


        return aView;
    }

    public boolean isInit = false;

    public void init() {

        if (isInit || context == null)
            return;

        isInit = true;

        getDataH();

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
        // getData(1, 10);
    }



    public void onResume() {
        super.onResume();

        init();

    }

    RecyclerAdapterBasket2H recyclerAdapterH;



    CustomLayoutManager mLayoutManagerH;
    RecyclerView recyclerViewH;

    private void setupRecyclerViewH(final View aView, final RecyclerView recyclerView) {


        this.recyclerViewH=recyclerView;

        int paddingTop = Utils.getToolbarHeight(this.getActivity()) + Utils.getTabsHeight(this.getActivity());
        recyclerView.setPadding(recyclerView.getPaddingLeft(), 0, recyclerView.getPaddingRight(),
                recyclerView.getPaddingBottom());

         mLayoutManagerH = new CustomLayoutManager(getActivity());

        recyclerView.setLayoutManager(mLayoutManagerH);
        recyclerAdapterH = new RecyclerAdapterBasket2H(this, new ArrayList<Kala>());


        //recyclerView.setItemAnimator(new ScaleInAnimator());
        //ScaleInAnimationAdapter alphaAdapter = new ScaleInAnimationAdapter(recyclerAdapter);
        //alphaAdapter.setFirstOnly(false);
        //alphaAdapter.setDuration(1000);
        //alphaAdapter.setInterpolator(new OvershootInterpolator(.10f));
        //recyclerView.setAdapter(recyclerAdapterH);
        ScaleInAnimatorAdapter animatorAdapter = new ScaleInAnimatorAdapter(recyclerAdapterH, recyclerView);
        recyclerView.setAdapter(animatorAdapter);

        // int aVisibleItemCount = mLayoutManager.getChildCount();
        // recyclerView.scrollToPosition(count - 10);

        recyclerView.setOnScrollListener(new EndlessRecyclerOnScrollListener(mLayoutManagerH) {
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

            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

            }
        });


    }

    RecyclerAdapterBasket2V recyclerAdapterV;

    LinearLayoutManager mLayoutManagerV;

    private void setupRecyclerViewV(final View aView, final RecyclerView recyclerView) {


        int paddingTop = Utils.getToolbarHeight(this.getActivity()) + Utils.getTabsHeight(this.getActivity());
        recyclerView.setPadding(recyclerView.getPaddingLeft(), 0, recyclerView.getPaddingRight(),
                recyclerView.getPaddingBottom());
        mLayoutManagerV = new LinearLayoutManager(getActivity());

        recyclerView.setLayoutManager(mLayoutManagerV);
        recyclerAdapterV = new RecyclerAdapterBasket2V(this, new ArrayList<Kala>());

        //recyclerView.setItemAnimator(new ScaleInAnimator());
        //ScaleInAnimationAdapter alphaAdapter = new ScaleInAnimationAdapter(recyclerAdapter);
        //alphaAdapter.setFirstOnly(false);
        //alphaAdapter.setDuration(1000);
        //alphaAdapter.setInterpolator(new OvershootInterpolator(.10f));
        // recyclerView.setAdapter(recyclerAdapterV);

        ScaleInAnimatorAdapter animatorAdapter = new ScaleInAnimatorAdapter(recyclerAdapterV, recyclerView);
        recyclerView.setAdapter(animatorAdapter);

        // int aVisibleItemCount = mLayoutManager.getChildCount();
        // recyclerView.scrollToPosition(count - 10);

        recyclerView.setOnScrollListener(new EndlessRecyclerOnScrollListener(mLayoutManagerV) {
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

            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

            }
        });


    }

    private void HideShow(final int pro, final int fab) {

        if (context == null || context.getActivity() == null || context.getView() == null || !isAdded())
            return;

        final View aView = context.getView();

        context.getActivity().runOnUiThread(new Runnable() {

            @Override
            public void run() {

                if (aProgress != null) aProgress.setVisibility(pro);
                if (Values.appId == 3 && cardView!=null)
                    cardView.setVisibility(View.VISIBLE);

            }
        });
    }

    boolean isFill = false;
    private String idz;


    private void getDataH() {

        hSelect=-1;

        HideShow(View.VISIBLE, View.GONE);

        ConnectionThreadPool.getInstance().AddTask(new Runnable() {

            @Override
            public void run() {

                List<Kala> cListKala = null;

                try {

                    ConnectionPool aConnectionPool = new ConnectionPool();

                    System.out.println(Values.companyId + "-->>");

                    SoapObject aSoapObject = null;
                    if (NetworkUtil.getConnectivityStatusString(MainActivity.context) != null)
                        aSoapObject = aConnectionPool.ConnectGetShippedTimesWeeks(Values.companyId,year,month);


                    if (aSoapObject == null) {
                        // noServerResponse();
                        return;
                    }


                    List<Kala> bListKala = new ArrayList<Kala>();

                    //SoapObject bSoapObject = (SoapObject) aSoapObject.getProperty("GetCategoriesResult");

                    System.out.println(aSoapObject.getPropertyCount());

                    for (int i = 0; i < aSoapObject.getPropertyCount(); i++) {


                        SoapObject cSoapObject = (SoapObject) aSoapObject.getProperty(i);
                        Kala aKala = new Kala();

                        System.out.println(cSoapObject.getProperty("id").toString());

                        aKala.id = Integer.parseInt(cSoapObject.getProperty("id").toString());
                        aKala.name = (String) cSoapObject.getProperty("name").toString();
                        aKala.description = cSoapObject.getProperty("description").toString();
                        aKala.price = Integer.parseInt(cSoapObject.getProperty("price").toString());
                        aKala.priceByDiscount = Integer.parseInt(cSoapObject.getProperty("discount_price").toString());
                        aKala.minOrder = Integer.parseInt(cSoapObject.getProperty("minOrder").toString());
                        aKala.saleType = cSoapObject.getProperty("saleType").toString();
                        aKala.rank = Integer.parseInt(cSoapObject.getProperty("rank").toString());
                        aKala.unitsInStock = Integer.parseInt(cSoapObject.getProperty("unitsInStock").toString());

                        aKala.image = (String) cSoapObject.getProperty("img").toString();

                        aKala.tag=cSoapObject.getProperty("tag").toString();

                        bListKala.add(aKala);

                        System.out.println("aKala.tag->"+aKala.tag);

                    }


                    if (bListKala.size() != 0) {


                        if (!isFill) {
                            // aListKala.add(bListKala);
                            setupViewH(bListKala, bListKala.size());

                        } else {
                            HideShow(View.GONE, View.VISIBLE);
                        }


                    } else {
                        HideShow(View.GONE, View.VISIBLE);
                    }

                } catch (Exception e) {
                    noServerResponse();
                    e.printStackTrace();
                }


            }
        });

    }

    int id = 0;
    String tag;

    public void getDataV(final int id, final String tag) {

        vSelect=-1;

        this.id = id;
        this.tag=tag;

        HideShow(View.VISIBLE, View.GONE);

        ConnectionThreadPool.getInstance().AddTask(new Runnable() {

            @Override
            public void run() {

                List<Kala> cListKala = null;

                try {

                    ConnectionPool aConnectionPool = new ConnectionPool();

                    SoapObject aSoapObject = null;
                    if (NetworkUtil.getConnectivityStatusString(MainActivity.context) != null)
                        aSoapObject = aConnectionPool.ConnectGetShippedTimes(Values.companyId, id,tag);


                    if (aSoapObject == null) {
                        // noServerResponse();
                        return;
                    }


                    List<Kala> bListKala = new ArrayList<Kala>();

                    //SoapObject bSoapObject = (SoapObject) aSoapObject.getProperty("GetCategoriesResult");

                    System.out.println(aSoapObject.getPropertyCount());

                    for (int i = 0; i < aSoapObject.getPropertyCount(); i++) {


                        SoapObject cSoapObject = (SoapObject) aSoapObject.getProperty(i);
                        Kala aKala = new Kala();

                        System.out.println(cSoapObject.getProperty("id").toString());

                        aKala.id = Integer.parseInt(cSoapObject.getProperty("id").toString());
                        aKala.name = (String) cSoapObject.getProperty("name").toString();
                        aKala.description = cSoapObject.getProperty("description").toString();
                        aKala.price = Integer.parseInt(cSoapObject.getProperty("price").toString());
                        aKala.priceByDiscount = Integer.parseInt(cSoapObject.getProperty("discount_price").toString());
                        aKala.minOrder = Integer.parseInt(cSoapObject.getProperty("minOrder").toString());
                        aKala.rank = Integer.parseInt(cSoapObject.getProperty("rank").toString());
                        aKala.unitsInStock = Integer.parseInt(cSoapObject.getProperty("unitsInStock").toString());
                        aKala.saleType = cSoapObject.getProperty("saleType").toString();
                        aKala.image = (String) cSoapObject.getProperty("img").toString();

                        bListKala.add(aKala);

                        aKala.tag=cSoapObject.getProperty("tag").toString();

                        System.out.println(aKala.description + cSoapObject.getProperty("description"));

                    }


                    if (bListKala.size() != 0) {


                        if (!isFill) {
                            // aListKala.add(bListKala);
                            setupViewV(bListKala, bListKala.size());

                        } else {
                            HideShow(View.GONE, View.VISIBLE);
                        }


                    } else {
                        HideShow(View.GONE, View.VISIBLE);
                    }

                } catch (Exception e) {
                    noServerResponse();
                    e.printStackTrace();
                }


            }
        });

    }

    public void getDataV() {

        vSelect=-1;

        HideShow(View.VISIBLE, View.GONE);

        ConnectionThreadPool.getInstance().AddTask(new Runnable() {

            @Override
            public void run() {

                List<Kala> cListKala = null;

                try {

                    ConnectionPool aConnectionPool = new ConnectionPool();

                    SoapObject aSoapObject = null;
                    if (NetworkUtil.getConnectivityStatusString(MainActivity.context) != null)
                        aSoapObject = aConnectionPool.ConnectGetShippedTimes(Values.companyId, id,tag);


                    if (aSoapObject == null) {
                        // noServerResponse();
                        return;
                    }


                    List<Kala> bListKala = new ArrayList<Kala>();

                    //SoapObject bSoapObject = (SoapObject) aSoapObject.getProperty("GetCategoriesResult");

                    System.out.println(aSoapObject.getPropertyCount());

                    for (int i = 0; i < aSoapObject.getPropertyCount(); i++) {


                        SoapObject cSoapObject = (SoapObject) aSoapObject.getProperty(i);
                        Kala aKala = new Kala();

                        System.out.println(cSoapObject.getProperty("id").toString());

                        aKala.id = Integer.parseInt(cSoapObject.getProperty("id").toString());
                        aKala.name = (String) cSoapObject.getProperty("name").toString();
                        aKala.description = cSoapObject.getProperty("description").toString();
                        aKala.price = Integer.parseInt(cSoapObject.getProperty("price").toString());
                        aKala.priceByDiscount = Integer.parseInt(cSoapObject.getProperty("discount_price").toString());
                        aKala.minOrder = Integer.parseInt(cSoapObject.getProperty("minOrder").toString());
                        aKala.rank = Integer.parseInt(cSoapObject.getProperty("rank").toString());
                        aKala.unitsInStock = Integer.parseInt(cSoapObject.getProperty("unitsInStock").toString());
                        aKala.saleType = cSoapObject.getProperty("saleType").toString();
                        aKala.image = (String) cSoapObject.getProperty("img").toString();

                        bListKala.add(aKala);

                        System.out.println(aKala.description + cSoapObject.getProperty("description"));

                    }


                    if (bListKala.size() != 0) {


                        if (!isFill) {
                            // aListKala.add(bListKala);
                            setupViewV(bListKala, bListKala.size());

                        } else {
                            HideShow(View.GONE, View.VISIBLE);
                        }


                    } else {
                        HideShow(View.GONE, View.VISIBLE);
                    }

                } catch (Exception e) {
                    noServerResponse();
                    e.printStackTrace();
                }


            }
        });

    }


    public void setupViewH(final List<Kala> aList, final int length) {

        if (context == null || context.getActivity() == null || context.getView() == null || !isAdded())
            return;

        final View aView = context.getView();

        context.getActivity().runOnUiThread(new Runnable() {

            @Override
            public void run() {

                // if (aListKala == null)
                // return;

                recyclerAdapterH.clearItem();

                if (length > 0) {

                    recyclerAdapterH.addItem(aList, 0);

                }


                // aLayout.setVisibility(View.VISIBLE);
            }
        });

        getDataV(aList.get(0).id,aList.get(0).tag);
    }

    public void setupViewV(final List<Kala> aList, final int length) {

        if (context == null || context.getActivity() == null || context.getView() == null || !isAdded())
            return;

        final View aView = context.getView();

        context.getActivity().runOnUiThread(new Runnable() {

            @Override
            public void run() {

                // if (aListKala == null)
                // return;

                recyclerAdapterV.clearItem();

                if (length > 0) {

                    recyclerAdapterV.addItem(aList, 0);
                }


                HideShow(View.GONE, View.VISIBLE);
                // aLayout.setVisibility(View.VISIBLE);
            }
        });
    }


    Snackbar snackbar;

    public void noServerResponse() {

        HideShow(View.GONE, View.VISIBLE);

        if (context == null || context.getActivity() == null || context.getView() == null || !isAdded())
            return;

        ((Activity) context.getActivity()).runOnUiThread(new Runnable() {

            @Override
            public void run() {

                snackbar = Snackbar
                        .make(context.getView().findViewById(R.id.layout), getString(R.string.server_not_response), Snackbar.LENGTH_INDEFINITE)
                        .setAction(getString(R.string.retry), new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                snackbar.dismiss();

                                //MainActivity.context.mToolbarContainer.setExpanded(true);
                                HideShow(View.VISIBLE, View.VISIBLE);
                                getDataH();


                            }
                        });

                if (recyclerAdapterH.getItemCount() != 0 || recyclerAdapterV.getItemCount() != 0)
                    snackbar.show();
                else
                    context.getView().findViewById(R.id.no_server_response).setVisibility(View.VISIBLE);


                // MainActivity.context.mToolbarContainer.setExpanded(true);

                context.getView().findViewById(R.id.no_server_response_retry).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        context.getView().findViewById(R.id.no_server_response).setVisibility(View.GONE);
                        //MainActivity.context.mToolbarContainer.setExpanded(true);
                        HideShow(View.VISIBLE, View.GONE);
                        getDataH();


                    }
                });

            }
        });

    }


    public  int year;
    public  int month;

    public void showDialogPicker() {

         year=1395;
         month=1;


        final Dialog dialog = new Dialog(context.getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_picker);

        final NumberPicker pickYear = (NumberPicker) dialog.findViewById(R.id.pick_year);
        pickYear.setMinValue(1395);
        pickYear.setMaxValue(1397);
        pickYear.setDisplayedValues(getResources().getStringArray(R.array.year_array));
        pickYear.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {

            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                // TODO Auto-generated method stub


                year = newVal;
            }
        });

        final NumberPicker pickMonth = (NumberPicker) dialog.findViewById(R.id.pick_month);
        pickMonth.setMinValue(1);
        pickMonth.setMaxValue(12);
        pickMonth.setDisplayedValues(getResources().getStringArray(R.array.month_array));
        pickMonth.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {

            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                // TODO Auto-generated method stub


                month = newVal;
            }
        });



        dialog.findViewById(R.id.pick_btn_select).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

                if (recyclerAdapterH != null) recyclerAdapterH.clearItem();
                if (recyclerAdapterV != null) recyclerAdapterV.clearItem();

                cardView.setVisibility(View.GONE);

                getDataH();
            }
        });

        dialog.findViewById(R.id.pick_btn_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }

    public  void setNumberPicker(NumberPicker numberPicker)
    {
        final int count = numberPicker.getChildCount();
        for(int i = 0; i < count; i++){
            View child = numberPicker.getChildAt(i);
            if(child instanceof EditText){
                try{
                    Field selectorWheelPaintField = numberPicker.getClass()
                            .getDeclaredField("mSelectorWheelPaint");
                    selectorWheelPaintField.setAccessible(true);
                    ((Paint)selectorWheelPaintField.get(numberPicker)).setColor(getResources().getColor(R.color.color_primary));
                    ((EditText)child).setTextColor(getResources().getColor(R.color.color_primary));
                    Typeface aFont = Typeface.createFromAsset(context.getActivity().getAssets(),
                            "irfontnumlight.ttf");

                    ((EditText)child).setTypeface(aFont, Typeface.BOLD);
                    numberPicker.invalidate();
                }
                catch(NoSuchFieldException e){
                    Log.w("setNumberP", e);
                }
                catch(IllegalAccessException e){
                    Log.w("setNumberP", e);
                }
                catch(IllegalArgumentException e){
                    Log.w("setNumberP", e);
                }
            }
        }
    }

}
