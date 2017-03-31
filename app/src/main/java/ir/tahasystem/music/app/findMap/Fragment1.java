package ir.tahasystem.music.app.findMap;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gc.materialdesign.views.ProgressBarCircularIndeterminate;
import com.google.gson.Gson;

import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import ir.onlinefood.app.factory.R;
import ir.tahasystem.music.app.MainActivity;
import ir.tahasystem.music.app.Model.IsManu;
import ir.tahasystem.music.app.Model.Kala;
import ir.tahasystem.music.app.Model.LoginHolder;
import ir.tahasystem.music.app.Model.LoginModel;
import ir.tahasystem.music.app.ThreadPool.ConnectionThreadPool;
import ir.tahasystem.music.app.connection.ConnectionPool;
import ir.tahasystem.music.app.utils.NetworkUtil;


public class Fragment1 extends Fragment {

    public final static String ITEMS_COUNT_KEY = "PartThreeFragment$ItemsCount";
    public static Fragment1 context;
    private com.gc.materialdesign.views.ProgressBarIndeterminate aProgress;


    EditText edtTelMoaref;
    TextView noResTextView;
    ProgressBarCircularIndeterminate aProgressBar;
    LinearLayout reg, reg2, reg3;
    String vcode;
    private static final String FORMAT = "%02d : %02d";

    public Button aButtonCall;
    Button bButton;
    Button cButton;
    public EditText edtTel, edtCode;

    CountDownTimer aCountDownTimer;


    public static Fragment1 createInstance(int itemsCount) {
        Fragment1 partThreeFragment = new Fragment1();
        Bundle bundle = new Bundle();
        bundle.putInt(ITEMS_COUNT_KEY, itemsCount);
        partThreeFragment.setArguments(bundle);
        return partThreeFragment;

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {

        this.context = this;

        View aView = (View) inflater.inflate(R.layout.fragment_map_1, container, false);

        reg = (LinearLayout) aView.findViewById(R.id.reg);
        reg2 = (LinearLayout) aView.findViewById(R.id.reg2);
        reg3 = (LinearLayout) aView.findViewById(R.id.reg3);


        bButton = (Button) aView.findViewById(R.id.btn2);
        aButtonCall = (Button) aView.findViewById(R.id.btnCall);

        edtCode = (EditText) aView.findViewById(R.id.value2);
        edtTel = (EditText) aView.findViewById(R.id.value);
        edtTelMoaref = (EditText) aView.findViewById(R.id.valueMoaref);


        edtCode.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {

                edtCode.requestLayout();
                FindMapActivity.context.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

                return false;
            }
        });

        edtTel.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {

                edtTel.setFocusableInTouchMode(true);
                edtTel.clearFocus();
                edtTel.requestFocus();

                edtTel.requestLayout();
                FindMapActivity.context.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

                return false;
            }
        });

        edtTelMoaref.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                edtTelMoaref.requestLayout();
                FindMapActivity.context.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

                return false;
            }
        });

        final TextView text1 = (TextView) aView.findViewById(R.id.timer);

        noResTextView = (TextView) aView.findViewById(R.id.no_res);
        aProgressBar = (ProgressBarCircularIndeterminate) aView.findViewById(R.id.progress);

        cButton = (Button) aView.findViewById(R.id.btn3);

        aButtonCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (edtTel.getText().toString().trim().length() == 11) {

                    hideKeyboardFrom(context.getActivity(), edtTel);

                    edtTel.setError(null);


                    reg.setVisibility(View.INVISIBLE);
                    aProgressBar.setVisibility(View.VISIBLE);

                    getData(edtTel.getText().toString(), 0);
                    getData(edtTel.getText().toString(), 1);


                    context.getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            reg.setVisibility(View.GONE);
                            reg2.setVisibility(View.VISIBLE);
                            aProgressBar.setVisibility(View.GONE);
                        }
                    });


                    aCountDownTimer = new CountDownTimer(4 * 60 * 1000, 1000) { // adjust the milli seconds here

                        public void onTick(long millisUntilFinished) {

                            if (context == null || context.getActivity() == null || context.getView() == null || !isAdded()) {
                                cancel();
                                return;
                            }

                            text1.setText(getString(R.string.we) + " " + String.format(FORMAT,

                                    TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(
                                            TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)),
                                    TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(
                                            TimeUnit.MILLISECONDS.toHours(millisUntilFinished))) + " " + getString(R.string.call_you_in));
                        }

                        public void onFinish() {
                            if (context != null && context.getActivity() != null && context.getView() != null)
                                noServerResponse(getString(R.string.retry));

                        }
                    }.start();


                } else {
                    edtTel.setError(context.getActivity().getString(R.string.mob_error));
                }

            }
        });

        bButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (edtCode.getText().toString().trim().equals(vcode)) {

                    hideKeyboardFrom(context.getActivity(), edtCode);

                    edtCode.setError(null);

                    reg.setVisibility(View.INVISIBLE);
                    reg2.setVisibility(View.GONE);
                    aProgressBar.setVisibility(View.VISIBLE);

                    getData2(edtTel.getText().toString(), edtTelMoaref.getText().toString());

                } else {

                    edtCode.setError(context.getActivity().getString(R.string.code_error));
                }

            }
        });

        cButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                reg.setVisibility(View.VISIBLE);
                reg2.setVisibility(View.GONE);
                reg3.setVisibility(View.INVISIBLE);
            }
        });


        return aView;
    }

    public void onResume() {
        super.onResume();

        edtCode.clearFocus();
        edtTel.clearFocus();
        edtTelMoaref.clearFocus();


        aButtonCall.setFocusableInTouchMode(true);
        aButtonCall.requestFocus();
        synchronized (this) {
            if (Tel != null)
                getData2(Tel, Moaref);
        }
    }

    String Tel;
    String Moaref;

    public void getDataF() {
        synchronized (this) {
            Tel = edtTel.getText().toString();
            Moaref = edtTelMoaref.getText().toString();

            if (context != null && context.getActivity() != null && context.getView() != null && isAdded()) {

                Tel = null;

                getData2(edtTel.getText().toString(), edtTelMoaref.getText().toString());

            }
        }
    }

    public static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    private void noServerResponse(final String msg) {

        if (context != null && context.getActivity() != null && context.getView() != null && isAdded())

            context.getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    reg.setVisibility(View.GONE);
                    reg2.setVisibility(View.GONE);
                    reg3.setVisibility(View.VISIBLE);
                    noResTextView.setVisibility(View.VISIBLE);
                    noResTextView.setText(msg);
                    aProgressBar.setVisibility(View.GONE);
                    if (aCountDownTimer != null)
                        aCountDownTimer.cancel();
                }
            });
    }


    private void getData(final String mobile, final int activeBy) {


        ConnectionThreadPool.getInstance().AddTask(new Runnable() {

            @Override
            public void run() {

                try {

                    ConnectionPool aConnectionPool = new ConnectionPool();

                    SoapObject aSoapObject = null;
                    if (NetworkUtil.getConnectivityStatusString(context.getActivity()) != null)
                        aSoapObject = aConnectionPool.ConnectVerifyMobile(mobile, activeBy);


                    if (aSoapObject == null) {
                        noServerResponse(getString(R.string.server_not_response));
                        return;
                    }


                    System.out.println(aSoapObject.getPropertyCount());


                    System.out.println(aSoapObject.getProperty("id").toString());
                    System.out.println(aSoapObject.getProperty("description").toString());

                    float id = Float.parseFloat(aSoapObject.getProperty("id").toString());
                    if (id < 0) {
                        noServerResponse(aSoapObject.getProperty("description").toString());
                        return;
                    }

                    vcode = aSoapObject.getProperty("id").toString();


                } catch (Exception e) {
                    noServerResponse(getString(R.string.server_not_response));
                    e.printStackTrace();
                }


            }
        });

    }

    private void getData2(final String mobile, final String moaref) {


        if (context != null && context.getActivity() != null && context.getView() != null && isAdded())
            context.getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    reg.setVisibility(View.INVISIBLE);
                    reg2.setVisibility(View.GONE);
                    aProgressBar.setVisibility(View.VISIBLE);
                }
            });

        ConnectionThreadPool.getInstance().

                AddTask(new Runnable() {

                            @Override
                            public void run() {

                                try {

                                    ConnectionPool aConnectionPool = new ConnectionPool();

                                    SoapPrimitive aSoapObject = null;
                                    if (NetworkUtil.getConnectivityStatusString(context.getActivity()) != null)
                                        aSoapObject = aConnectionPool.ConnectRegisterByMob(mobile, moaref);


                                    if (aSoapObject == null) {
                                        noServerResponse(getString(R.string.server_not_response));
                                        return;
                                    }


                                    List<Kala> bListKala = new ArrayList<Kala>();

                                    //SoapObject bSoapObject = (SoapObject) aSoapObject.getProperty("GetCategoriesResult");

                                    System.out.println(aSoapObject);


                                    if (!Boolean.parseBoolean(aSoapObject.toString())) {
                                        noServerResponse(context.getString(R.string.not_reg));
                                    } else {
                                        LoginModel aLoginModel = LoginHolder.getInstance().getLogin();

                                        if (aLoginModel == null)
                                            aLoginModel = new LoginModel();

                                        aLoginModel.uid = mobile;


                                        SoapPrimitive bSoapObject = null;
                                        if (NetworkUtil.getConnectivityStatusString(context.getActivity()) != null)
                                            bSoapObject = aConnectionPool.ConnectIsManufacture(mobile);


                                        if (bSoapObject == null) {
                                            noServerResponse(getString(R.string.server_not_response));
                                            return;
                                        }

                                        IsManu isManu = new Gson().fromJson(bSoapObject.toString(), IsManu.class);

                                        aLoginModel.hasRole = isManu.IsManufacture;
                                        aLoginModel.fullAccess = isManu.FullAccess;
                                        aLoginModel.accessType = isManu.AccessType;


                                        System.out.println("hasRole-ACCESS S->" + aLoginModel.hasRole + "-" + aLoginModel.fullAccess);
                                        LoginHolder.getInstance().setLogin(aLoginModel);

                                    }

                                    if (context == null || context.getActivity() == null || context.getView() == null || !isAdded())
                                        return;

                                    //startActivity(new Intent(context.getActivity(), MainActivity.class));
                                    //FindMapActivity.context.finish();
                                    context.getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {

                                            FindMapActivity.viewPager.setCurrentItem(1);


                                        }
                                    });



                                    //initService();


                                } catch (Exception e) {
                                    if (context != null)
                                        noServerResponse(getString(R.string.server_not_response));
                                    e.printStackTrace();
                                }


                            }
                        }

                );

    }

}
