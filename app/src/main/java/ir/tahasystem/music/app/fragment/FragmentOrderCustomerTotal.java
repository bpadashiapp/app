package ir.tahasystem.music.app.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import org.ksoap2.serialization.SoapPrimitive;

import ir.onlinefood.app.factory.R;
import ir.tahasystem.music.app.CustomerOrderActivity;
import ir.tahasystem.music.app.Model.CustomerTotalModel;
import ir.tahasystem.music.app.Model.LoginHolder;
import ir.tahasystem.music.app.Model.LoginModel;
import ir.tahasystem.music.app.ThreadPool.ConnectionThreadPool;
import ir.tahasystem.music.app.connection.ConnectionPool;
import ir.tahasystem.music.app.custom.OnCompleteListener;
import ir.tahasystem.music.app.utils.NetworkUtil;


public class FragmentOrderCustomerTotal extends Fragment {

    public final static String ITEMS_COUNT_KEY = "PartThreeFragment$ItemsCount";
    LinearLayout aLayout;
    private FragmentOrderCustomerTotal context;
    private com.gc.materialdesign.views.ProgressBarIndeterminate aProgress;

    RecyclerView aRecyclerView;


    private boolean userIsInteracting;
    public int cateId;
    public int subCateId;

    public static FragmentOrderCustomerTotal createInstance(int itemsCount) {
        FragmentOrderCustomerTotal partThreeFragment = new FragmentOrderCustomerTotal();
        Bundle bundle = new Bundle();
        bundle.putInt(ITEMS_COUNT_KEY, itemsCount);
        partThreeFragment.setArguments(bundle);
        return partThreeFragment;

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        this.context = this;

        View aView = (View) inflater.inflate(R.layout.fragment_customet_order_total, container, false);


        aProgress = (com.gc.materialdesign.views.ProgressBarIndeterminate) aView.findViewById(R.id.list_load);
        aLayout= (LinearLayout) aView.findViewById(R.id.layout);

        aView.findViewById(R.id.buy_details).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(FragmentOrderCustomerTotal.this.getActivity(),
                        CustomerOrderActivity.class));

            }
        });

        return aView;
    }

    public boolean isInit = false;

    public void init() {

        if (isInit || context == null)
            return;

        isInit = true;

        if (context == null || context.getActivity() == null || context.getView() == null || !isAdded())
            return;

        context.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {

                load();

            }
        });


    }


    private OnCompleteListener mListener;




    public void onResume() {
        super.onResume();

        init();

    }


    public void onStart() {
        super.onStart();

        // if (aListKala == null || aListKala.size() == 0)
        // getData(1, 10);
    }


    private void HideShow(final int pro, final int fab) {

        if (context == null || context.getActivity() == null || context.getView() == null || !isAdded())
            return;

        final View aView = context.getView();

        context.getActivity().runOnUiThread(new Runnable() {

            @Override
            public void run() {

                if (aProgress != null) aProgress.setVisibility(pro);
                if (aLayout!= null) aLayout.setVisibility(fab);

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

                    ConnectionPool aConnectionPool = new ConnectionPool();

                    LoginModel aLoginHolder = LoginHolder.getInstance().getLogin();

                    SoapPrimitive aSoapObject = null;
                    if (NetworkUtil.getConnectivityStatusString(context.getActivity()) != null)
                        aSoapObject = aConnectionPool.ConnectGetStatusConsumerByCIdJson(aLoginHolder.uid);


                    if (aSoapObject == null) {
                        noServerResponse();
                        return;
                    }


                    CustomerTotalModel aCustomerTotalModel = new Gson().fromJson(aSoapObject.toString(), CustomerTotalModel.class);


                    if (aCustomerTotalModel == null)
                        noServerResponse();


                    setupView(aCustomerTotalModel);


                } catch (Exception e) {
                    noServerResponse();
                    e.printStackTrace();
                }


            }
        });

    }


    public void setupView(final CustomerTotalModel aCustomerTotalModel) {

        if (context == null || context.getActivity() == null || context.getView() == null || !isAdded())
            return;

        final View aView = context.getView();

        context.getActivity().runOnUiThread(new Runnable() {

            @Override
            public void run() {



                ((TextView) context.getView().findViewById(R.id.lastDateOrder)).
                        setText(aCustomerTotalModel.lastDateOrder);

                ((TextView) context.getView().findViewById(R.id.lastOrderPrice)).
                        setText(aCustomerTotalModel.lastOrderPrice + " " + getString(R.string.toman));


                ((TextView) context.getView().findViewById(R.id.sumOrderPriceLastMounth)).
                        setText(aCustomerTotalModel.sumOrderPricelastMounth + " " + getString(R.string.toman));

                ((TextView) context.getView().findViewById(R.id.sumOrderPriceUpToNow)).
                        setText(aCustomerTotalModel.sumOrderPriceUpToNow+ " " + getString(R.string.toman));

                ((TextView) context.getView().findViewById(R.id.systemSharjAmount)).
                        setText(aCustomerTotalModel.systemSharjAmount + " " + getString(R.string.toman));

                ((TextView) context.getView().findViewById(R.id.introduceCount)).
                        setText(String.valueOf(aCustomerTotalModel.IntroduceCount));


                HideShow(View.GONE, View.VISIBLE);

            }
        });
    }

    public void load() {


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

}
