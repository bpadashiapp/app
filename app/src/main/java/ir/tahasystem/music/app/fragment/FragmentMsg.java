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
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.github.johnpersano.supertoasts.SuperActivityToast;
import com.github.johnpersano.supertoasts.SuperToast;
import com.github.johnpersano.supertoasts.util.Style;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import ir.onlinefood.app.factory.R;
import ir.tahasystem.music.app.MainActivity;
import ir.tahasystem.music.app.Model.Kala;
import ir.tahasystem.music.app.Model.LoginHolder;
import ir.tahasystem.music.app.Model.ModelHolderService;
import ir.tahasystem.music.app.ThreadPool.ConnectionThreadPool;
import ir.tahasystem.music.app.Values;
import ir.tahasystem.music.app.custom.EndlessRecyclerOnScrollListener;
import ir.tahasystem.music.app.services.ServicesSocket;
import ir.tahasystem.music.app.utils.NetworkUtil;
import ir.tahasystem.music.app.utils.Utils;
import it.gmariotti.recyclerview.adapter.ScaleInAnimatorAdapter;


public class FragmentMsg extends Fragment {

    public final static String ITEMS_COUNT_KEY = "PartThreeFragment$ItemsCount";
    FrameLayout aLayout;
    public FragmentMsg context;
    private com.gc.materialdesign.views.ProgressBarIndeterminate aProgress;

    RecyclerView aRecyclerView, aRecyclerViewH;


    private boolean userIsInteracting;
    public int cateId;
    public int subCateId;

    public static FragmentMsg createInstance(int itemsCount) {
        FragmentMsg partThreeFragment = new FragmentMsg();
        Bundle bundle = new Bundle();
        bundle.putInt(ITEMS_COUNT_KEY, itemsCount);
        partThreeFragment.setArguments(bundle);
        return partThreeFragment;

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        this.context = this;

        View aView = (View) inflater.inflate(R.layout.fragment_msg, container, false);


        final EditText editText = (EditText) aView.findViewById(R.id.msg_edt);

        aView.findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (NetworkUtil.getConnectivityStatusString(context.getActivity()) == null){
                    HideShow(View.GONE, View.VISIBLE);
                    msg(getString(R.string.server_not_response));
                    return;
                }

                if(ServicesSocket.mWebSocketClient==null ||
                        !ServicesSocket.mWebSocketClient.isOpen()) {
                    msg(getString(R.string.server_not_response));
                    return;
                }



                String msg = editText.getText().toString();

                if (msg.trim().length() != 0) {

                    hideKeyboardFrom(FragmentMsg.this.getActivity(), editText);

                    Kala aModel = new Kala();
                    aModel.uid = UUID.randomUUID().toString();
                    aModel.sender = LoginHolder.getInstance().getLogin().uid + "-" + Values.companyId;
                    aModel.receiver = RecyclerAdapterMsg2H.reciever;
                    SimpleDateFormat df = new SimpleDateFormat("MMMM dd hh:mm");
                    aModel.date = df.format(new Date());
                    aModel.text = msg;
                    aModel.command = "get";
                    aModel.isRec = 0;
                    aModel.isServer = 1;
                    aModel.numServer = RecyclerAdapterMsg2H.selected;
                    aModel.name = getString(R.string.app_name);
                    aModel.companyId = Values.companyId;


                    if (ServicesSocket.mWebSocketClient != null && ServicesSocket.mWebSocketClient.isOpen()) {
                        ServicesSocket.mWebSocketClient.send(new Gson().toJson(aModel).toString());
                        System.out.println(new Gson().toJson(aModel).toString());
                        msg(getString(R.string.msg_sended));
                        editText.setText("");

                        ModelHolderService.getInstance().setKalaListChat(context.getActivity(), aModel);

                        load();
                    } else {
                        msg(getString(R.string.server_not_response));
                    }
                }

            }
        });

        final SwipeRefreshLayout refreshLayout = (SwipeRefreshLayout) aView.findViewById(R.id.swipe_layout);
        refreshLayout.setColorSchemeColors(Color.BLUE);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                if (NetworkUtil.getConnectivityStatusString(context.getActivity()) == null) {
                    refreshLayout.setRefreshing(false);
                    msg(getString(R.string.server_not_response));
                    return;
                }

               /* if (recyclerAdapter != null) recyclerAdapter.clearItem();
                count = 0;
                isFill = false;
                loading = true;*/

                //if (recyclerAdapterH != null)
                // recyclerAdapterH.clearItem();

                if(ServicesSocket.mWebSocketClient==null ||
                        !ServicesSocket.mWebSocketClient.isOpen()) {
                    msg(getString(R.string.server_not_response));
                    return;
                }


                if (recyclerAdapterH != null) recyclerAdapterH.clearItem();

                Kala aModel = new Kala();
                aModel.uid = UUID.randomUUID().toString();
                aModel.sender = LoginHolder.getInstance().getLogin().uid + "-" + Values.companyId;
                aModel.receiver = String.valueOf(Values.companyId);
                aModel.date = "";
                aModel.text = "";
                aModel.command = "list";
                aModel.isRec = 0;
                aModel.name = "";
                aModel.isServer = 1;
                aModel.companyId = Values.companyId;

                ServicesSocket.mWebSocketClient.send(new Gson().toJson(aModel));


                refreshLayout.setRefreshing(false);


            }
        });

        aProgress = (com.gc.materialdesign.views.ProgressBarIndeterminate) aView.findViewById(R.id.list_load);


        aRecyclerView = (RecyclerView) aView.findViewById(R.id.recyclerView);
        setupRecyclerView(aView, aRecyclerView);

        aRecyclerViewH = (RecyclerView) aView.findViewById(R.id.recyclerViewH);


        setupRecyclerViewH(aView, aRecyclerViewH);


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

    public static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


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
    public RecyclerAdapterMsg recyclerAdapter;

    LinearLayoutManager mLayoutManager;
    RecyclerView recyclerView, recyclerViewH;


    private void setupRecyclerView(final View aView, final RecyclerView recyclerView) {

        this.recyclerView = recyclerView;

        int paddingTop = Utils.getToolbarHeight(this.getActivity()) + Utils.getTabsHeight(this.getActivity());
        recyclerView.setPadding(recyclerView.getPaddingLeft(), 0, recyclerView.getPaddingRight(),
                recyclerView.getPaddingBottom());

        mLayoutManager = new LinearLayoutManager(getActivity());

        recyclerView.setLayoutManager(mLayoutManager);
        recyclerAdapter = new RecyclerAdapterMsg(this, new ArrayList<Kala>());

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

    public RecyclerAdapterMsg2H recyclerAdapterH;
    LinearLayoutManager mLayoutManagerH;

    private void setupRecyclerViewH(final View aView, final RecyclerView recyclerView) {

        int paddingTop = Utils.getToolbarHeight(this.getActivity()) + Utils.getTabsHeight(this.getActivity());
        recyclerView.setPadding(recyclerView.getPaddingLeft(), 0, recyclerView.getPaddingRight(),
                recyclerView.getPaddingBottom());

        mLayoutManagerH = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);

        recyclerView.setLayoutManager(mLayoutManagerH);
        recyclerAdapterH = new RecyclerAdapterMsg2H(this, new ArrayList<Kala>());

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

        if (NetworkUtil.getConnectivityStatusString(context.getActivity()) == null){
            HideShow(View.GONE, View.VISIBLE);
            return;
        }


        ConnectionThreadPool.getInstance().AddTask(new Runnable() {

            @Override
            public void run() {

                try {


                    //if (recyclerAdapterH != null)
                    //  recyclerAdapterH.clearItem();

                    Kala aModel = new Kala();
                    aModel.uid = UUID.randomUUID().toString();
                    aModel.sender = LoginHolder.getInstance().getLogin().uid + "-" + Values.companyId;
                    aModel.receiver = String.valueOf(Values.companyId);
                    aModel.date = "";
                    aModel.text = "";
                    aModel.command = "list";
                    aModel.isRec = 0;
                    aModel.name = "";
                    aModel.isServer = 1;
                    aModel.companyId = Values.companyId;

                    ServicesSocket.mWebSocketClient.send(new Gson().toJson(aModel));


                    List<Kala> oldKalaList = ModelHolderService.getInstance().getKalaListChat(context.getActivity());


                    if (oldKalaList == null) {
                        HideShow(View.GONE, View.GONE);
                        //showNotFound(View.VISIBLE);
                        return;
                    } else {
                        //showNotFound(View.GONE);
                    }


                    List<Kala> aListKala = new ArrayList<Kala>();
                    for (Kala oldKala : oldKalaList) {

                        System.out.println(oldKala.text+" "+oldKala.isView);

                        if (!oldKala.isView) {
                            cancelNotification(context.getActivity(), oldKala.id);
                            oldKala.isView = true;
                        }
                        oldKala.isOrderSeen = true;
                        aListKala.add(oldKala);

                    }
                    ModelHolderService.getInstance().setKalaListChat(context.getActivity(), aListKala);


                    //Collections.reverse(aListKala);

                    List<Kala> eListKala = new ArrayList<Kala>();
                    for (Kala aKala : aListKala)
                        if (!aKala.isDelete)
                            eListKala.add(aKala);


                    setupView(eListKala);

                    if (MainActivity.context != null)
                        MainActivity.context.updateMsg();


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

                if (recyclerAdapter != null) recyclerAdapter.clearItem();


                recyclerAdapter.addItem(aList, count);


                loading = true;

                recyclerView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        recyclerView.scrollToPosition(recyclerView.getAdapter().getItemCount() - 1);
                    }
                }, 1000);



                // aLayout.setVisibility(View.VISIBLE);
            }
        });
    }

    public void setupViewH(final Kala aKala) {


        if (context == null || context.getActivity() == null || context.getView() == null || !isAdded())
            return;


        context.getActivity().runOnUiThread(new Runnable() {

            @Override
            public void run() {

                boolean toAdd = true;

                for (Kala fKala : recyclerAdapterH.mItemList) {

                    if (fKala.sender.equals(aKala.sender) || fKala.numServer != aKala.numServer||aKala.isServer != 2) {
                        toAdd = false;
                    }

                }
                if (aKala.isServer == 2 && toAdd) {
                    recyclerAdapterH.addItem(aKala, 0);

                    RecyclerAdapterMsg2H.selected = aKala.numServer;
                }

                HideShow(View.GONE, View.VISIBLE);

            }
        });

    }


    public void load() {

        if (recyclerAdapter != null) recyclerAdapter.clearItem();
        // aListKala.clear();
        count = 0;
        isFill = false;
        loading = true;

        if (recyclerAdapterH != null) recyclerAdapterH.clearItem();

        // setupRecyclerView(context.getView(), (RecyclerView)
        // context.getView().findViewById(R.id.recyclerView));

        getData();

    }

    Snackbar snackbar;

    public void noServerResponse() {

        HideShow(View.GONE, View.VISIBLE);

        /*if (context == null || context.getActivity() == null || context.getView() == null || !isAdded())
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
        });*/

    }

    public void getNotify() {
        getData();
    }


    public void getServer() {


    }
}
