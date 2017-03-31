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
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.johnpersano.supertoasts.SuperActivityToast;
import com.github.johnpersano.supertoasts.SuperToast;
import com.github.johnpersano.supertoasts.util.Style;
import com.google.gson.Gson;

import org.ksoap2.serialization.SoapPrimitive;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import ir.onlinefood.app.factory.R;
import ir.tahasystem.music.app.MainActivity;
import ir.tahasystem.music.app.Model.Kala;
import ir.tahasystem.music.app.Model.LoginHolder;
import ir.tahasystem.music.app.Model.LoginModel;
import ir.tahasystem.music.app.Model.Order;
import ir.tahasystem.music.app.Model.OrderDetails;
import ir.tahasystem.music.app.Model.OrderModel;
import ir.tahasystem.music.app.ThreadPool.ConnectionThreadPool;
import ir.tahasystem.music.app.connection.ConnectionPool;
import ir.tahasystem.music.app.custom.EndlessRecyclerOnScrollListener;
import ir.tahasystem.music.app.utils.NetworkUtil;
import ir.tahasystem.music.app.utils.Utils;
import it.gmariotti.recyclerview.adapter.ScaleInAnimatorAdapter;


public class FragmentOrderRequest extends Fragment {

    public final static String ITEMS_COUNT_KEY = "PartThreeFragment$ItemsCount";
    LinearLayout aLayout;
    private FragmentOrderRequest context;
    private com.gc.materialdesign.views.ProgressBarIndeterminate aProgress;


    private boolean userIsInteracting;
    public int cateId;
    public int subCateId;

    public static List<OrderDetails> aOrderDetails;
    private boolean isFill;

    public static FragmentOrderRequest createInstance(int itemsCount) {
        FragmentOrderRequest partThreeFragment = new FragmentOrderRequest();
        Bundle bundle = new Bundle();
        bundle.putInt(ITEMS_COUNT_KEY, itemsCount);
        partThreeFragment.setArguments(bundle);
        return partThreeFragment;

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        this.context = this;

        View aView = (View) inflater.inflate(R.layout.fragment_order_request, container, false);


        aProgress = (com.gc.materialdesign.views.ProgressBarIndeterminate) aView.findViewById(R.id.list_load);
        aLayout = (LinearLayout) aView.findViewById(R.id.layout);

        //setupRecyclerView(aView, aRecyclerView);
        final EditText edt = (EditText) aView.findViewById(R.id.edt);

        edt.setText(getString(R.string.company) + " " +
                getString(R.string.app_name) + " " +
                getString(R.string.wants_refund));


        aView.findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (edt.getText().toString().trim().length() > 0) {
                    setData(edt.getText().toString());
                } else {
                    edt.setError(getString(R.string.fill));
                }


            }
        });


        return aView;
    }

    public boolean isInit = false;

    public void init() {

        if (isInit || context == null)
            return;

        isInit = true;

        getData();


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

    }


    private void HideShow(final int pro, final int fab) {

        if (context == null || context.getActivity() == null || context.getView() == null || !isAdded())
            return;

        final View aView = context.getView();

        context.getActivity().runOnUiThread(new Runnable() {

            @Override
            public void run() {

                if(aProgress!=null) aProgress.setVisibility(pro);
                aLayout.setVisibility(fab);

            }
        });
    }

    private void setData(final String body) {

        HideShow(View.VISIBLE, View.GONE);

        ConnectionThreadPool.getInstance().AddTask(new Runnable() {

            @Override
            public void run() {


                if (NetworkUtil.getConnectivityStatusString(context.getActivity()) == null)
                    noServerResponse();

                try {

                    ConnectionPool aConnectionPool = new ConnectionPool();

                    LoginModel aLoginHolder = LoginHolder.getInstance().getLogin();

                    SoapPrimitive aSoapObject = null;
                    if (NetworkUtil.getConnectivityStatusString(context.getActivity()) != null)
                        aSoapObject = aConnectionPool.ConnectGetSendMail(aLoginHolder.user,
                                context.getString(R.string.refund_request), body);


                    if (aSoapObject == null) {
                        noServerResponse();
                        return;
                    }

                    if (aSoapObject.toString().toLowerCase().equals("true")) {

                        msg(getString(R.string.done));
                    } else {

                        msg(getString(R.string.not_done));

                    }

                    HideShow(View.GONE, View.VISIBLE);


                } catch (Exception e) {
                    noServerResponse();
                    e.printStackTrace();
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
                customStyle.background = SuperToast.Background.GREEN;
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

    private void getData() {

        HideShow(View.VISIBLE, View.GONE);

        ConnectionThreadPool.getInstance().AddTask(new Runnable() {

            @Override
            public void run() {


                if (NetworkUtil.getConnectivityStatusString(context.getActivity()) == null)
                    noServerResponse();

                try {

                    ConnectionPool aConnectionPool = new ConnectionPool();

                    LoginModel aLoginHolder = LoginHolder.getInstance().getLogin();

                    SoapPrimitive aSoapObject = null;
                    if (NetworkUtil.getConnectivityStatusString(context.getActivity()) != null)
                        aSoapObject = aConnectionPool.ConnectGetBill(aLoginHolder.user, aLoginHolder.pass);


                    if (aSoapObject == null) {
                        noServerResponse();
                        return;
                    }


                    OrderModel aOrderModel = new Gson().fromJson(aSoapObject.toString(), OrderModel.class);


                    if (aOrderModel == null)
                        noServerResponse();


                    setupView(aOrderModel);

                } catch (Exception e) {
                    noServerResponse();
                    e.printStackTrace();
                }


            }
        });

    }

    public void setupView(final OrderModel aOrderModel) {

        if (context == null || context.getActivity() == null || context.getView() == null || !isAdded())
            return;

        final View aView = context.getView();

        context.getActivity().runOnUiThread(new Runnable() {

            @Override
            public void run() {

                DecimalFormat df = new DecimalFormat("###,###,###,###,###,###,###");


                ((TextView) context.getView().findViewById(R.id.AllOrders)).setText(df.format(aOrderModel.AllOrders) + " " + getString(R.string.toman));
                ((TextView) context.getView().findViewById(R.id.AllPayment)).setText(df.format(aOrderModel.AllPayment) + " " + getString(R.string.toman));
                ((TextView) context.getView().findViewById(R.id.Bill)).setText(df.format(aOrderModel.Bill) + " " + getString(R.string.toman));

                HideShow(View.GONE, View.VISIBLE);
                // aLayout.setVisibility(View.VISIBLE);
            }
        });
    }


    public void noServerResponse() {

        HideShow(View.GONE, View.VISIBLE);

        if (context == null || context.getActivity() == null || context.getView() == null || !isAdded())
            return;

        ((Activity) context.getActivity()).runOnUiThread(new Runnable() {

            @Override
            public void run() {

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

}
