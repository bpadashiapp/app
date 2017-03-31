package ir.tahasystem.music.app.fragment;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.google.gson.Gson;

import org.ksoap2.serialization.SoapPrimitive;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ir.onlinefood.app.factory.R;
import ir.tahasystem.music.app.MainActivity;
import ir.tahasystem.music.app.Model.Kala;
import ir.tahasystem.music.app.Model.LoginHolder;
import ir.tahasystem.music.app.Model.ModelHolderService;
import ir.tahasystem.music.app.Model.NotifyModel;
import ir.tahasystem.music.app.Model.NotifyModelNew;
import ir.tahasystem.music.app.Model.VersionModel;
import ir.tahasystem.music.app.ThreadPool.ConnectionThreadPool;
import ir.tahasystem.music.app.Values;
import ir.tahasystem.music.app.connection.ConnectionPool;
import ir.tahasystem.music.app.custom.EndlessRecyclerOnScrollListener;
import ir.tahasystem.music.app.update.UpdatePanel;
import ir.tahasystem.music.app.utils.NetworkUtil;
import ir.tahasystem.music.app.utils.Serialize;
import ir.tahasystem.music.app.utils.Utils;
import it.gmariotti.recyclerview.adapter.ScaleInAnimatorAdapter;


public class FragmentNotify extends Fragment {

    public final static String ITEMS_COUNT_KEY = "PartThreeFragment$ItemsCount";
    FrameLayout aLayout;
    public FragmentNotify context;
    private com.gc.materialdesign.views.ProgressBarIndeterminate aProgress;

    RecyclerView aRecyclerView;


    private boolean userIsInteracting;
    public int cateId;
    public int subCateId;

    public static FragmentNotify createInstance(int itemsCount) {
        FragmentNotify partThreeFragment = new FragmentNotify();
        Bundle bundle = new Bundle();
        bundle.putInt(ITEMS_COUNT_KEY, itemsCount);
        partThreeFragment.setArguments(bundle);
        return partThreeFragment;

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        this.context = this;

        View aView = (View) inflater.inflate(R.layout.fragment_kala, container, false);

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
                if (recyclerAdapter != null) recyclerAdapter.clearItem();
                count = 0;
                isFill = false;
                loading = true;

                getDataNotify();

            }
        });

        aProgress = (com.gc.materialdesign.views.ProgressBarIndeterminate) aView.findViewById(R.id.list_load);


        aRecyclerView = (RecyclerView) aView.findViewById(R.id.recyclerView);


        setupRecyclerView(aView, aRecyclerView);


        return aView;
    }

    public boolean isInit = false;

    public void init() {

        if (isInit || context == null)
            return;

        isInit = true;

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
    public RecyclerAdapterNotify recyclerAdapter;

    LinearLayoutManager mLayoutManager;


    private void setupRecyclerView(final View aView, final RecyclerView recyclerView) {

        int paddingTop = Utils.getToolbarHeight(this.getActivity()) + Utils.getTabsHeight(this.getActivity());
        recyclerView.setPadding(recyclerView.getPaddingLeft(), 0, recyclerView.getPaddingRight(),
                recyclerView.getPaddingBottom());

        mLayoutManager = new LinearLayoutManager(getActivity());

        recyclerView.setLayoutManager(mLayoutManager);
        recyclerAdapter = new RecyclerAdapterNotify(this, new ArrayList<Kala>());

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

                            //loading = false;
                            //count = count + 10;
                            //getData(count / 10, 10);

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
                //if(aFabUp!=null) aFabUp.setVisibility(fab);

            }
        });
    }

    boolean isFill = false;
    private String idz;


    private void getData() {

        HideShow(View.VISIBLE, View.GONE);

        ConnectionThreadPool.getInstance().AddTask(new Runnable() {

            @Override
            public void run() {

                try {


                    Kala bkala = ModelHolderService.getInstance().getKalaOrder(context.getActivity());
                    if (bkala != null) {
                        bkala.isOrderSeen = true;
                        ModelHolderService.getInstance().setKalaOrder(context.getActivity(), bkala);
                    }


                    List<Kala> oldKalaList = ModelHolderService.getInstance().getKalaList(context.getActivity());


                    if (oldKalaList == null) {
                        HideShow(View.GONE, View.GONE);
                        //showNotFound(View.VISIBLE);
                        return;
                    } else {
                        //showNotFound(View.GONE);
                    }


                    List<Kala> aListKala = new ArrayList<Kala>();
                    for (Kala oldKala : oldKalaList) {

                        if (!oldKala.isView) {
                            cancelNotification(context.getActivity(), oldKala.id);
                            oldKala.isView = true;
                        }
                        oldKala.isOrderSeen = true;
                        aListKala.add(oldKala);

                    }
                    ModelHolderService.getInstance().setKalaList(context.getActivity(), aListKala);

                    if (bkala != null)
                        aListKala.add(bkala);

                    Collections.reverse(aListKala);

                    List<Kala> eListKala = new ArrayList<Kala>();
                    for (Kala aKala : aListKala)
                        if (!aKala.isDelete)
                            eListKala.add(aKala);


                    setupView(eListKala);

                    if (MainActivity.context != null)
                        MainActivity.context.updateNotify();


                } catch (Exception e) {
                    noServerResponse();
                    e.printStackTrace();
                }


            }
        });

    }

    public static synchronized void cancelNotification(Context ctx, int notifyId) {
        String ns = Context.NOTIFICATION_SERVICE;
        NotificationManager nMgr = (NotificationManager) ctx.getSystemService(ns);
        nMgr.cancel(notifyId);
    }


    public void setupView(final List<Kala> aList) {

        if (context == null || context.getActivity() == null || context.getView() == null || !isAdded())
            return;

        final View aView = context.getView();

        context.getActivity().runOnUiThread(new Runnable() {

            @Override
            public void run() {

                // if (aListKala == null)
                // return;


                recyclerAdapter.addItem(aList, count);


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

        getData();

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
                                getData();


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
                        getData();

                    }
                });

            }
        });

    }

    public void getNotify() {
        getData();
    }

    public synchronized void getDataNotify() {

        HideShow(View.VISIBLE, View.GONE);

        if (NetworkUtil.getConnectivityStatusString(context.getActivity()) != null && LoginHolder.getInstance().getLogin() != null)
            new Thread(new Runnable() {

                @Override
                public void run() {

                    try {

                        ModelHolderService.getInstance().setKalaList(context.getActivity(),null);

                        System.out.println("NOTIFY CHECK");

                        ConnectionPool aConnectionPool = new ConnectionPool();

                        SoapPrimitive aSoapObject = null;
                        if (NetworkUtil.getConnectivityStatusString(context.getActivity()) != null)
                            aSoapObject = aConnectionPool.ConnectGetNotifications();


                        if (aSoapObject == null || aSoapObject.toString() == null) {

                            return;
                        }

                        if (context == null || context.getActivity() == null || context.getView() == null || !isAdded())
                            return;

                        NotifyModelNew aNotifyModelNew;

                        try {

                            aNotifyModelNew = new Gson().fromJson(aSoapObject.toString(), NotifyModelNew.class);

                        }catch (Exception e){
                            e.printStackTrace();
                            return;
                        }


                        if (aNotifyModelNew.neworder > 0 && LoginHolder.getInstance().getLogin().hasRole) {

                            Kala aKala = new Kala();
                            aKala.isOrder = true;
                            aKala.isOrderSeen = false;
                            aKala.id = 0;
                            aKala.neworder = aNotifyModelNew.neworder;
                            aKala.name = context.getString(R.string.new_orders);
                            aKala.description = context.getString(R.string.you_have) + " " + aNotifyModelNew.neworder + " " + context.getString(R.string.for_submit);

                            Kala bKala = ModelHolderService.getInstance().getKalaOrder(context.getActivity());
                            // if (bKala==null ||aNotifyModelNew.neworder != bKala.neworder || !bKala.isOrderSeen)
                            // Notify(aKala, 10000);


                            ModelHolderService.getInstance().setKalaOrder(context.getActivity(), aKala);
                        }




                        NotifyModel aNotifyModel = new NotifyModel();

                        VersionModel aVersionModel = new VersionModel();
                        aVersionModel.apkVer = aNotifyModelNew.version;
                        aVersionModel.apkName = Values.companyId + ".apk";
                        aVersionModel.apkFile = Values.companyId + ".apk";
                        aVersionModel.apkDir = "/CAPK/";
                        aVersionModel.apkIsCan = false;
                        aNotifyModel.ver = aVersionModel;


                        aNotifyModel.notify = aNotifyModelNew.news;


                        if (aNotifyModel == null)
                            return;

                        new Serialize().saveToFile(context.getActivity(), aNotifyModel, "aNotifyModelNew");

                        List<Kala> newKalaList = aNotifyModel.getNotify();
                        if (newKalaList == null)
                            return;

                        List<Kala> oldKalaList = ModelHolderService.getInstance().getKalaList(context.getActivity());

                        List<Kala> tmpKalaList = new ArrayList<Kala>();

                        if (oldKalaList == null) {

                            oldKalaList = new ArrayList<Kala>();
                            tmpKalaList = newKalaList;

                        } else {

                            for (Kala newKala : newKalaList) {
                                Kala hasModel = newKala;
                                for (Kala oldKala : oldKalaList) {
                                    if (oldKala.id == newKala.id)
                                        hasModel = null;
                                }
                                if (hasModel != null)
                                    tmpKalaList.add(hasModel);
                            }
                        }


                        // for (Kala tmpKala : tmpKalaList)
                        //  Notify(tmpKala);


                        oldKalaList.addAll(tmpKalaList);

                        ModelHolderService.getInstance().setKalaList(context.getActivity(), oldKalaList);


                        System.out.println("getNotify()-->");
                        getNotify();


                        // #check for Update
                        UpdatePanel updatePanel = new UpdatePanel(context.getActivity(), aNotifyModel.getVer());
                        updatePanel.update();


                        HideShow(View.GONE ,View.VISIBLE);


                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (XmlPullParserException e) {
                        e.printStackTrace();
                    } finally {

                    }

                }
            }).start();

    }
}
