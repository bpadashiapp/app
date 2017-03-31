package ir.tahasystem.music.app.fragment;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.List;

import ir.onlinefood.app.factory.R;
import ir.tahasystem.music.app.Model.Kala;
import ir.tahasystem.music.app.ThreadPool.ConnectionThreadPool;
import ir.tahasystem.music.app.Values;
import ir.tahasystem.music.app.connection.ConnectionPool;
import ir.tahasystem.music.app.custom.EndlessRecyclerOnScrollListener;
import ir.tahasystem.music.app.utils.NetworkUtil;
import ir.tahasystem.music.app.utils.Serialize;
import ir.tahasystem.music.app.utils.Utils;
import it.gmariotti.recyclerview.adapter.ScaleInAnimatorAdapter;


public class FragmentChange extends Fragment {

    public final static String ITEMS_COUNT_KEY = "PartThreeFragment$ItemsCount";

    FrameLayout aLayout;
    public static  FragmentChange context;
    private com.gc.materialdesign.views.ProgressBarIndeterminate aProgress;

    RecyclerView aRecyclerView;
    FloatingActionButton aFabUp;


    private boolean userIsInteracting;
    public static int cateId;
    public int subCateId;

    private List<Kala> listToFilter;

    Button showBtn;
    Button discountBtn;
    Button emptyBtn;
    public boolean isList=true;

    public static FragmentChange createInstance(int itemsCount) {
        FragmentChange partThreeFragment = new FragmentChange();
        Bundle bundle = new Bundle();
        bundle.putInt(ITEMS_COUNT_KEY, itemsCount);
        partThreeFragment.setArguments(bundle);
        return partThreeFragment;

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        this.context = this;

        View aView = (View) inflater.inflate(R.layout.fragment_kala_change, container, false);

        final SwipeRefreshLayout refreshLayout = (SwipeRefreshLayout) aView.findViewById(R.id.swipe_layout);
        refreshLayout.setColorSchemeColors(Color.BLUE);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                if (NetworkUtil.getConnectivityStatusString(context.getActivity()) == null) {
                    refreshLayout.setRefreshing(false);
                    return;
                }
                refreshLayout.setRefreshing(false);
                if(recyclerAdapter!=null) recyclerAdapter.clearItem();
                count = 0;
                isFill = false;
                loading = true;

                getData(0, 10);

            }
        });

        aProgress = (com.gc.materialdesign.views.ProgressBarIndeterminate) aView.findViewById(R.id.list_load);


        aRecyclerView = (RecyclerView) aView.findViewById(R.id.recyclerView);

        aFabUp = (FloatingActionButton) aView.findViewById(R.id.fab_up);aFabUp.setVisibility(View.VISIBLE);
        if(isList){
            aFabUp.setImageResource(R.drawable.fab_list);
        }else{
            aFabUp.setImageResource(R.drawable.fab_image);
        }
        aFabUp.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if(isList){
                    aFabUp.setImageResource(R.drawable.fab_image);
                    isList=false;

                    if(recyclerAdapter!=null) recyclerAdapter.clearItem();
                    count = 0;
                    isFill = false;
                    loading = true;

                    getData(0, 10);



                }else{
                    aFabUp.setImageResource(R.drawable.fab_list);
                    isList=true;

                    if(recyclerAdapter!=null) recyclerAdapter.clearItem();
                    count = 0;
                    isFill = false;
                    loading = true;

                    getData(0, 10);


                }



            }
        });


        setupRecyclerView(aView, aRecyclerView);

        showBtn = (Button) aView.findViewById(R.id.change_show_all);
        discountBtn = (Button) aView.findViewById(R.id.change_show_discount);
        emptyBtn = (Button) aView.findViewById(R.id.change_show_empty);

        showBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (listToFilter == null)
                    return;

                showBtn.setTextColor(Color.WHITE);
                showBtn.setBackgroundColor(getResources().getColor(R.color.color_primary));

                discountBtn.setTextColor(getResources().getColor(R.color.color_primary));
                discountBtn.setBackgroundColor(Color.WHITE);

                emptyBtn.setTextColor(getResources().getColor(R.color.color_primary));
                emptyBtn.setBackgroundColor(Color.WHITE);

                if (recyclerAdapter!= null) recyclerAdapter.clearItem();


                List<Kala> eListKala = new ArrayList<Kala>();

                for (Kala kala : listToFilter) {
                    eListKala.add(kala);
                }

                setupView(eListKala, eListKala.size());


            }
        });

        discountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (listToFilter == null)
                    return;

                discountBtn.setTextColor(Color.WHITE);
                discountBtn.setBackgroundColor(getResources().getColor(R.color.color_primary));

                showBtn.setTextColor(getResources().getColor(R.color.color_primary));
                showBtn.setBackgroundColor(Color.WHITE);

                emptyBtn.setTextColor(getResources().getColor(R.color.color_primary));
                emptyBtn.setBackgroundColor(Color.WHITE);

                if (recyclerAdapter != null) recyclerAdapter.clearItem();


                List<Kala> eListKala = new ArrayList<Kala>();

                for (Kala kala : listToFilter) {
                    if (kala.discount > 0)
                        eListKala.add(kala);
                }

                setupView(eListKala, eListKala.size());


            }
        });

        emptyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (listToFilter == null)
                    return;

                emptyBtn.setTextColor(Color.WHITE);
                emptyBtn.setBackgroundColor(getResources().getColor(R.color.color_primary));

                discountBtn.setTextColor(getResources().getColor(R.color.color_primary));
                discountBtn.setBackgroundColor(Color.WHITE);

                showBtn.setTextColor(getResources().getColor(R.color.color_primary));
                showBtn.setBackgroundColor(Color.WHITE);

                if (recyclerAdapter != null) recyclerAdapter.clearItem();


                List<Kala> eListKala = new ArrayList<Kala>();

                for (Kala kala : listToFilter) {
                    if (kala.unitsInStock<=0)
                        eListKala.add(kala);
                }

                setupView(eListKala, eListKala.size());


            }
        });

        return aView;
    }

    public boolean isInit = false;

    public void init() {

        if (isInit || context == null)
            return;

        //isInit = true;

        load();

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

    private boolean loading = true;
    private int count = 0;
    public RecyclerAdapterChange recyclerAdapter;

    LinearLayoutManager mLayoutManager;


    private void setupRecyclerView(final View aView, final RecyclerView recyclerView) {

        int paddingTop = Utils.getToolbarHeight(this.getActivity()) + Utils.getTabsHeight(this.getActivity());
        recyclerView.setPadding(recyclerView.getPaddingLeft(), 0, recyclerView.getPaddingRight(),
                recyclerView.getPaddingBottom());

        mLayoutManager = new LinearLayoutManager(getActivity());

        recyclerView.setLayoutManager(mLayoutManager);
        recyclerAdapter = new RecyclerAdapterChange(this, new ArrayList<Kala>());

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

                    /*if (loading) {
                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount ) {

                            loading = false;
                            count = count + 10;
                            getData(count/10,10);

                        }
                    }*/
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

                if(aProgress!=null) aProgress.setVisibility(pro);


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

                List<Kala> cListKala = null;
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

                    ConnectionPool aConnectionPool = new ConnectionPool();

                    SoapObject aSoapObject = null;
                    if (NetworkUtil.getConnectivityStatusString(context.getActivity()) != null)
                        aSoapObject = aConnectionPool.ConnectGetProduct(Values.companyId,
                                cateId);

                    if (aSoapObject == null) {
                        noServerResponse();
                        return;
                    }


                    List<Kala> bListKala = new ArrayList<Kala>();

                    //SoapObject bSoapObject = (SoapObject) aSoapObject.getProperty("GetCategoriesResult");

                    System.out.println(aSoapObject.getPropertyCount());

                    for (int i = 0; i < aSoapObject.getPropertyCount(); i++) {



                        SoapObject cSoapObject = (SoapObject) aSoapObject.getProperty(i);
                        Kala aKala = new Kala();

                        System.out.println(cSoapObject.getProperty("id").toString());

                        aKala.id = Integer.parseInt( cSoapObject.getProperty("id").toString());
                        aKala.name = (String) cSoapObject.getProperty("name").toString();
                        aKala.description =  cSoapObject.getProperty("description").toString();
                        aKala.price = Long.parseLong( cSoapObject.getProperty("price").toString());
                        aKala.priceByDiscount = Long.parseLong( cSoapObject.getProperty("discount_price").toString());
                        aKala.minOrder = Integer.parseInt( cSoapObject.getProperty("minOrder").toString());
                        aKala.rank = Integer.parseInt( cSoapObject.getProperty("rank").toString());
                        aKala.unitsInStock = Integer.parseInt(cSoapObject.getProperty("unitsInStock").toString());
                        aKala.saleType = cSoapObject.getProperty("saleType").toString();
                        aKala.image=(String) cSoapObject.getProperty("img").toString();
                        aKala.discount=Float.parseFloat( cSoapObject.getProperty("discount").toString());

                        bListKala.add(aKala);

                        System.out.println(aKala.description+cSoapObject.getProperty("description"));

                    }


                    loading = true;

                    if (bListKala.size() != 0) {


                        if (!isFill) {
                            // aListKala.add(bListKala);
                            setupView(bListKala, bListKala.size());

                            listToFilter=bListKala;

                        } else {
                            HideShow(View.GONE, View.VISIBLE);
                        }

                        new Serialize().saveToFile(context.getActivity(), bListKala,
                                "aListKalaHomeProductsChange" + Values.companyId + page);

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


    public void setupView(final List<Kala> aList, final int length) {


        if (context == null || context.getActivity() == null || context.getView() == null || !isAdded())
            return;

        final View aView = context.getView();

        context.getActivity().runOnUiThread(new Runnable() {

            @Override
            public void run() {

                // if (aListKala == null)
                // return;

                if (length > 0) {

                    recyclerAdapter.addItem(aList, count);
                }

                loading = true;

                HideShow(View.GONE, View.VISIBLE);
                // aLayout.setVisibility(View.VISIBLE);
            }
        });
    }

    public void load() {

        if(recyclerAdapter!=null) recyclerAdapter.clearItem();
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

                snackbar = Snackbar
                        .make(context.getView().findViewById(R.id.layout), getString(R.string.server_not_response), Snackbar.LENGTH_INDEFINITE)
                        .setAction(getString(R.string.retry), new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                snackbar.dismiss();

                                //MainActivity.context.mToolbarContainer.setExpanded(true);
                                HideShow(View.VISIBLE, View.VISIBLE);
                                getData(count, count + 10);


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
                        HideShow(View.VISIBLE, View.GONE);
                        getData(count, count + 10);

                    }
                });

            }
        });

    }

}
