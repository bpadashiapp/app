package ir.tahasystem.music.app.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.ksoap2.serialization.SoapPrimitive;

import java.lang.reflect.Type;
import java.util.ArrayList;

import ir.onlinefood.app.factory.R;
import ir.tahasystem.music.app.MainActivity;
import ir.tahasystem.music.app.Model.Company;
import ir.tahasystem.music.app.ThreadPool.ConnectionThreadPool;
import ir.tahasystem.music.app.Values;
import ir.tahasystem.music.app.connection.ConnectionPool;
import ir.tahasystem.music.app.utils.NetworkUtil;
import ir.tahasystem.music.app.utils.Serialize;


public class FragmentSmsInfo extends Fragment {

    public final static String ITEMS_COUNT_KEY = "PartThreeFragment$ItemsCount";
    LinearLayout aLayout;
    public static FragmentSmsInfo context;
    private com.gc.materialdesign.views.ProgressBarIndeterminate aProgress;

    RecyclerView aRecyclerView;
    // FloatingActionButton aFabUp;

    public static int cateId;

    private EditText edt;

    public static int shareType = 0;
    private String shareText;

    public static FragmentSmsInfo createInstance(int itemsCount) {
        FragmentSmsInfo partThreeFragment = new FragmentSmsInfo();
        Bundle bundle = new Bundle();
        bundle.putInt(ITEMS_COUNT_KEY, itemsCount);
        partThreeFragment.setArguments(bundle);
        return partThreeFragment;

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        this.context = this;

        View aView = (View) inflater.inflate(R.layout.fragment_sms_info, container, false);

        aView.findViewById(R.id.sms_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //if (shareType == 0)
                   // SmsActivity.context.viewPager.setCurrentItem(1);
               // else
                    share();


            }
        });

        aLayout = (LinearLayout) aView.findViewById(R.id.layout);
        aProgress = (com.gc.materialdesign.views.ProgressBarIndeterminate) aView.findViewById(R.id.list_load);


        edt = (EditText) aView.findViewById(R.id.sms_edt_text);

        TextView textView = (TextView) aView.findViewById(R.id.sms_txt);
        textView.setText(Html.fromHtml(getString(R.string.by_intra) +
                " " + getString(R.string.app_name) + " " + getString(R.string.by_intra_des) + " "
                + getString(R.string.by_intra_shavid) + "<br>" + getString(R.string.app_link) + "<br>" +
                "<a href=\"http://onlinefood.ir/apk\">"+ getString(R.string.download)+"</a> "));

        textView.setMovementMethod(LinkMovementMethod.getInstance());
        textView.setLinkTextColor(Color.BLUE);


        return aView;
    }

    public void share() {
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.app_name));
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, edt.getText().toString());
        startActivity(Intent.createChooser(sharingIntent, getResources().getString(R.string.share)));
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

        init();
    }

    private boolean loading = true;
    private int count = 0;
    RecyclerAdapterProduct recyclerAdapter;

    LinearLayoutManager mLayoutManager;


    private void HideShow(final int pro, final int fab) {

        if (context == null || context.getActivity() == null || context.getView() == null || !isAdded())
            return;

        final View aView = context.getView();

        context.getActivity().runOnUiThread(new Runnable() {

            @Override
            public void run() {

                if (aProgress != null) aProgress.setVisibility(pro);
                //

                aLayout.setVisibility(fab);

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

                Company company = (Company) new Serialize().readFromFile(context.getActivity(),
                        "aListKalaSmsi");

                if (company != null) {
                    // aListKala.add(cListKala);
                    isFill = true;
                    setupView(company);

                } else {
                    isFill = false;
                    if (NetworkUtil.getConnectivityStatusString(MainActivity.context) == null)
                        noServerResponse();

                }

                try {

                    ConnectionPool aConnectionPool = new ConnectionPool();

                    SoapPrimitive aSoapObject = null;
                    if (NetworkUtil.getConnectivityStatusString(MainActivity.context) != null)
                        aSoapObject = aConnectionPool.ConnectGetCompanyInfo(Values.companyId);


                    if (aSoapObject == null && aSoapObject.toString() == null) {
                        noServerResponse();
                        return;
                    }

                    System.out.println(aSoapObject.toString());

                    Type listType = new TypeToken<ArrayList<Company>>() {
                    }.getType();

                    Company aCompany = new Gson().fromJson(aSoapObject.toString(), Company.class);


                    if (aCompany != null) {


                        if (!isFill) {
                            setupView(aCompany);

                        } else {
                            HideShow(View.GONE, View.VISIBLE);
                        }

                        new Serialize().saveToFile(context.getActivity(), aCompany,
                                "aListKalaSmsi");

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

    public void setupView(final Company company) {

        if (context == null || context.getActivity() == null || context.getView() == null || !isAdded())
            return;

        final View aView = context.getView();

        context.getActivity().runOnUiThread(new Runnable() {

            @Override
            public void run() {


                edt.setText(company.messageDay);

                shareText = company.messageDay;

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

