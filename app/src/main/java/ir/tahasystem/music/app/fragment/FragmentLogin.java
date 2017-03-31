package ir.tahasystem.music.app.fragment;


import com.github.johnpersano.supertoasts.SuperActivityToast;
import com.github.johnpersano.supertoasts.SuperToast;
import com.github.johnpersano.supertoasts.util.Style;


import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;

import ir.tahasystem.music.app.MainActivity;
import ir.onlinefood.app.factory.R;
import ir.tahasystem.music.app.ThreadPool.ConnectionThreadPool;
import ir.tahasystem.music.app.connection.ConnectionPool;
import ir.tahasystem.music.app.utils.SharedPref;


public class FragmentLogin extends Fragment {

    public final static String ITEMS_COUNT_KEY = "PartThreeFragment$ItemsCount";

    public static FragmentLogin context;
    private com.gc.materialdesign.views.ProgressBarIndeterminate aProgress;

    private LinearLayout aLayout;

    CheckBox aCheckBox;

    EditText aEditUsername;
    EditText aEditPassword;

    Button aRip;

    InputMethodManager inputManager;

    public static FragmentLogin createInstance(int itemsCount) {
        FragmentLogin partThreeFragment = new FragmentLogin();
        Bundle bundle = new Bundle();
        bundle.putInt(ITEMS_COUNT_KEY, itemsCount);
        partThreeFragment.setArguments(bundle);
        return partThreeFragment;

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        this.context = this;

        View aView = (View) inflater.inflate(R.layout.fragment_login, container, false);
        aProgress = (com.gc.materialdesign.views.ProgressBarIndeterminate) aView.findViewById(R.id.list_load);
        aLayout = (LinearLayout) aView.findViewById(R.id.layout);

        aEditUsername = (EditText) aView.findViewById(R.id.login_email);
        aEditPassword = (EditText) aView.findViewById(R.id.login_password);


        aCheckBox = (CheckBox) aView.findViewById(R.id.save_pass);

        if (SharedPref.readBoolTrue(this.getActivity(), "checked")) {
            aEditUsername.setText(SharedPref.read(this.getActivity(), "username"));
            aEditPassword.setText(SharedPref.read(this.getActivity(), "password"));
            aCheckBox.setChecked(true);
        } else {

            aEditUsername.setText("");
            aEditPassword.setText("");
            aCheckBox.setChecked(false);

        }

        aCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (!isChecked) {

                    SharedPref.write(context.getActivity(), "username", "");
                    SharedPref.write(context.getActivity(), "password", "");

                }

                SharedPref.writeBool(context.getActivity(), "checked", isChecked);
                System.out.println(SharedPref.readBoolTrue(context.getActivity(), "checked"));
            }
        });

        aView.findViewById(R.id.remember).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DFragment alertdFragment = new DFragment();
                alertdFragment.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
                alertdFragment.show(getChildFragmentManager(), "");

            }
        });

        aRip = (Button) aView.findViewById(R.id.login);
        aRip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!aEditUsername.getText().toString().trim().equals("")
                        && !aEditPassword.getText().toString().trim().equals("")) {

                    hideKeyboardFrom(context.getActivity(), aEditUsername);

                    connect(aEditUsername.getText().toString().trim(), aEditPassword.getText().toString().trim());

                } else {

                    if (aEditUsername.getText().toString().trim().length() == 0) {
                        aEditUsername.setError(v.getContext().getString(R.string.fill));
                    }

                    if (aEditPassword.getText().toString().trim().length() == 0) {
                        aEditPassword.setError(v.getContext().getString(R.string.fill));
                    }

                    msg(getString(R.string.enter_user_pass));

                }

            }
        });


        aLayout.setVisibility(View.VISIBLE);
        aProgress.setVisibility(View.GONE);
        aRip.setVisibility(View.VISIBLE);

        aEditPassword.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    if (!aEditUsername.getText().toString().trim().equals("")
                            && !aEditPassword.getText().toString().trim().equals("")) {

                        hideKeyboardFrom(context.getActivity(), aEditUsername);

                        connect(aEditUsername.getText().toString().trim(), aEditPassword.getText().toString().trim());

                    } else {

                        if (aEditUsername.getText().toString().trim().length() == 0) {
                            aEditUsername.setError(v.getContext().getString(R.string.fill));
                        }

                        if (aEditPassword.getText().toString().trim().length() == 0) {
                            aEditPassword.setError(v.getContext().getString(R.string.fill));
                        }

                        msg(getString(R.string.enter_user_pass));

                    }
                    return true;
                }
                return false;
            }
        });

        return aView;
    }

    public void onStart() {
        super.onStart();

    }


    private void HideShow(final int pro) {

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

    private void connect(final String user, final String pass) {

        aLayout.setVisibility(View.INVISIBLE);
        aProgress.setVisibility(View.VISIBLE);
        aRip.setVisibility(View.INVISIBLE);

        SharedPref.write(context.getActivity(), "username", aEditUsername.getText().toString());
        SharedPref.write(context.getActivity(), "password", aEditPassword.getText().toString());

        ConnectionThreadPool.getInstance().AddTask(new Runnable() {

            @Override
            public void run() {

                // (string name, string family, string mob, string password,
                // DateTime birth, string nation, int insurance, string card)

                String param = "user=" + aEditUsername.getText().toString() + "&" + "pass="
                        + aEditPassword.getText().toString();

                StringBuilder aStringBuilder = new StringBuilder();

                StringBuilder res = null;
                ConnectionPool aConnectionPool = new ConnectionPool();
              /*  try {
                    if (NetworkUtil.getConnectivityStatusString(ir.tahasystem.music.app.MainActivity.context) != null)
                        res = aConnectionPool.ConnectAndPost(URLConnectionz.ApiLogin,
                                "?username=" + user + "&password=" + pass);
                    else {
                        msg(getString(R.string.no_internet));
                        noServerResponse();
                        return;
                    }

                    if (res == null) {
                        msg(getString(R.string.wrong_user_pass));
                        HideShow(View.GONE, View.VISIBLE);
                        return;
                    }

                    Mapper aMapper = new Mapper();

                    LoginModel[] aLoginModel = null;
                    try {
                        aLoginModel = aMapper.mapJsonToBeanLoginModel(res);
                    } catch (Exception e) {
                        noServerResponse();
                        e.printStackTrace();
                    }

                    if (aLoginModel == null || aLoginModel.length == 0 || !aLoginModel[0].status.equals("1")) {

                        HideShow(View.GONE, View.VISIBLE);
                        msg(getString(R.string.wrong_user_pass));
                        return;

                    } else {

                        LoginHolder.getInstance().setLogin(aLoginModel[0]);

                        if (context == null || context.getActivity() == null)
                            return;


                        Logined();

                        //Intent intent = new Intent(context.getActivity(), MainActivity.class);
                        //intent.putExtra("login", aLoginModel[0]);
                        //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        // startActivity(intent);

                        LoginActivity.context.finish();

                    }

                } catch (IOException e) {
                    // TODO Auto-generated catch block

                    noServerResponse();

                    e.printStackTrace();
                }*/

            }
        });

    }

    public void Logined() {

        if (context == null || context.getActivity() == null || context.getView() == null || !isAdded())
            return;

        ((Activity) context.getActivity()).runOnUiThread(new Runnable() {

            @Override
            public void run() {

                if (MainActivity.context != null)
                    MainActivity.context.initLogin();


            }
        });

    }

    public void HideShow(final int pro, final int layout) {
        if (context == null || context.getActivity() == null || context.getView() == null || !isAdded())
            return;

        ((Activity) context.getActivity()).runOnUiThread(new Runnable() {

            @Override
            public void run() {
                if(aProgress!=null) aProgress.setVisibility(pro);
                aLayout.setVisibility(layout);
                aRip.setVisibility(layout);
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

    public void msgOk(final String msg) {

        if (context == null || context.getActivity() == null || context.getView() == null || !isAdded())
            return;

        ((Activity) context.getActivity()).runOnUiThread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub

                final Style customStyle = new Style();
                customStyle.animations = SuperToast.Animations.SCALE;
                customStyle.background = SuperToast.Background.BLUE;
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


    class DFragment extends DialogFragment {


        public DFragment() {

        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View dialog = inflater.inflate(R.layout.dialogz_remember, container, false);

            final EditText aEdt = (EditText) dialog.findViewById(R.id.value);

            Button aRip = (Button) dialog.findViewById(R.id.buy_r);

            aRip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (aEdt.getText().toString().trim().length() > 0) {

                        dismiss();

                        hideKeyboardFrom(FragmentLogin.context.getActivity(), aEdt);
                    }


                }
            });


            return dialog;
        }
    }

    String remember;

    public void noServerResponse() {

        HideShow(View.GONE, View.VISIBLE);

        if (context == null || context.getActivity() == null || context.getView() == null || !isAdded())
            return;

        ((Activity) context.getActivity()).runOnUiThread(new Runnable() {

            @Override
            public void run() {

                context.getView().findViewById(R.id.no_server_response).setVisibility(View.VISIBLE);
                context.getView().findViewById(R.id.no_server_response_retry).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        HideShow(View.VISIBLE, View.GONE);
                        context.getView().findViewById(R.id.no_server_response).setVisibility(View.GONE);
                        connect(aEditUsername.getText().toString().trim(), aEditPassword.getText().toString().trim());
                    }
                });

            }
        });

    }

    public void noServerResponse2() {

        HideShow(View.GONE, View.VISIBLE);

        if (context == null || context.getActivity() == null || context.getView() == null || !isAdded())
            return;

        ((Activity) context.getActivity()).runOnUiThread(new Runnable() {

            @Override
            public void run() {


                context.getView().findViewById(R.id.no_server_response).setVisibility(View.VISIBLE);
                context.getView().findViewById(R.id.no_server_response_retry).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        HideShow(View.VISIBLE, View.GONE);
                        context.getView().findViewById(R.id.no_server_response).setVisibility(View.GONE);


                    }
                });

            }
        });

    }

}
