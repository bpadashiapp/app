package ir.tahasystem.music.app.fragment;


import com.github.johnpersano.supertoasts.SuperActivityToast;
import com.github.johnpersano.supertoasts.SuperToast;
import com.github.johnpersano.supertoasts.util.Style;


import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import ir.tahasystem.music.app.LoginActivity;
import ir.tahasystem.music.app.Model.LoginModel;
import ir.onlinefood.app.factory.R;
import ir.tahasystem.music.app.ThreadPool.ConnectionThreadPool;
import ir.tahasystem.music.app.connection.ConnectionPool;


public class FragmentRegister extends Fragment {

	public final static String ITEMS_COUNT_KEY = "PartThreeFragment$ItemsCount";

	private FragmentRegister context;

	LinearLayout aLayout;
	Button aRip;

	private com.gc.materialdesign.views.ProgressBarIndeterminate aProgress;
	public EditText edtPass, edtName, edtTel, edtEmail;

	private LoginModel aLoginModel;

	public static FragmentRegister createInstance(int itemsCount) {
		FragmentRegister partThreeFragment = new FragmentRegister();
		Bundle bundle = new Bundle();
		bundle.putInt(ITEMS_COUNT_KEY, itemsCount);
		partThreeFragment.setArguments(bundle);
		return partThreeFragment;

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		aLoginModel = new LoginModel();

		this.context = this;

		View aView = (View) inflater.inflate(R.layout.fragment_register, container, false);
		aProgress = (com.gc.materialdesign.views.ProgressBarIndeterminate) aView.findViewById(R.id.list_load);
		aLayout = (LinearLayout) aView.findViewById(R.id.layout);

		edtPass = (EditText) aView.findViewById(R.id.reg_pass);
		edtEmail = (EditText) aView.findViewById(R.id.reg_email);
		edtName = (EditText) aView.findViewById(R.id.reg_name);
		edtTel = (EditText) aView.findViewById(R.id.reg_mobile);


		aRip= (Button)  aView.findViewById(R.id.reg_btn);

		aRip.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				boolean check = true;

				if (edtPass.getText().toString().trim().length() != 0) {

					aLoginModel.password = edtPass.getText().toString().trim();
					edtPass.setError(null);
				} else {
					edtPass.setError(v.getContext().getString(R.string.fill));
					check = false;
				}

				if (edtEmail.getText().toString().trim().length() != 0) {
					edtEmail.setError(null);
				} else {
					edtEmail.setError(v.getContext().getString(R.string.fill));
					check = false;
				}

				if (isValidEmail(edtEmail.getText().toString().trim())) {

					aLoginModel.email = edtEmail.getText().toString().trim();
					edtEmail.setError(null);
				} else {
					edtEmail.setError(v.getContext().getString(R.string.invalid_email));
					check = false;
				}


				if (edtTel.getText().toString().trim().length() != 0) {

					edtTel.setError(null);
				} else {
					edtTel.setError(v.getContext().getString(R.string.fill));
					check = false;
				}

				if (edtTel.getText().toString().trim().length() == 11) {

					aLoginModel.mobile = edtTel.getText().toString().trim();
					aLoginModel.mobile = edtTel.getText().toString().trim();
					edtTel.setError(null);
				} else {
					edtTel.setError(v.getContext().getString(R.string.mob_error));
					check = false;
				}

				if (check)
					register();
				else
					msg(v.getContext().getString(R.string.plz_fill));

			}
		});

		edtPass.setOnKeyListener(new View.OnKeyListener() {
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {


					boolean check = true;

					if (edtPass.getText().toString().trim().length() != 0) {

						aLoginModel.password = edtPass.getText().toString().trim();
						edtPass.setError(null);
					} else {
						edtPass.setError(v.getContext().getString(R.string.fill));
						check = false;
					}

					if (edtEmail.getText().toString().trim().length() != 0) {
						edtEmail.setError(null);
					} else {
						edtEmail.setError(v.getContext().getString(R.string.fill));
						check = false;
					}

					if (isValidEmail(edtEmail.getText().toString().trim())) {

						aLoginModel.email = edtEmail.getText().toString().trim();
						edtEmail.setError(null);
					} else {
						edtEmail.setError(v.getContext().getString(R.string.invalid_email));
						check = false;
					}


					if (edtTel.getText().toString().trim().length() != 0) {

						edtTel.setError(null);
					} else {
						edtTel.setError(v.getContext().getString(R.string.fill));
						check = false;
					}

					if (edtTel.getText().toString().trim().length() == 11) {

						aLoginModel.mobile = edtTel.getText().toString().trim();
						aLoginModel.mobile = edtTel.getText().toString().trim();
						edtTel.setError(null);
					} else {
						edtTel.setError(v.getContext().getString(R.string.mob_error));
						check = false;
					}

					if (check)
						register();
					else
						msg(v.getContext().getString(R.string.plz_fill));


					return true;
				}
				return false;
			}
		});


		aLayout.setVisibility(View.VISIBLE);
		aProgress.setVisibility(View.GONE);
		aRip.setVisibility(View.VISIBLE);

		return aView;
	}

	public static void hideKeyboardFrom(Context context, View view) {
	    InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
	    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
	}

	public void onStart() {
		super.onStart();

	}

	protected void register() {



		aLayout.setVisibility(View.GONE);
		aProgress.setVisibility(View.VISIBLE);
		aRip.setVisibility(View.GONE);

		ConnectionThreadPool.getInstance().AddTask(new Runnable() {

            @Override
            public void run() {


                ConnectionPool aConnectionPool = new ConnectionPool();

                // name, mobile, email, password

                StringBuilder res = null;
                /*try {
                    if (NetworkUtil.getConnectivityStatusString(ir.shiraztakhfif.shop.MainActivity.context) != null)
                        res = aConnectionPool.ConnectAndPost(URLConnectionz.ApiRegister,
                                "?mobile=" + aLoginModel.mobile + "&email="
                                        + aLoginModel.email + "&password=" + aLoginModel.getPassword());

                    else {
                        msg(getString(R.string.no_internet));
                        noServerResponse();
                        return;
                    }

                    if (res == null) {
                        msg(getString(R.string.register_error));
                        noSetupUi();
                        return;
                    }
                    if (res.toString().contains("1")) {


                        SharedPref.write(context.getActivity(), "username", aLoginModel.email);
                        SharedPref.write(context.getActivity(), "password", aLoginModel.password);
                        SharedPref.writeBool(context.getActivity(), "checked", true);


                        SetupUi();

                        //LoginHolder.getInstance().setLogin(aLoginModel);

                        //Intent intent = new Intent(getActivity(), MainActivity.class);
                        //intent.putExtra("login", aLoginModel);
                        //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        //startActivity(intent);

                        //LoginActivity.context.finish();

                    } else {
                        msg(getString(R.string.register_error));
                        noSetupUi();
                    }

                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    noServerResponse();
                }*/

            }
        });

	}

	public void SetupUi() {
		if (context != null)
			((Activity) context.getActivity()).runOnUiThread(new Runnable() {

				@Override
				public void run() {

					aLayout.setVisibility(View.VISIBLE);
					aProgress.setVisibility(View.GONE);
					aRip.setVisibility(View.VISIBLE);

					LoginActivity.viewPager.setCurrentItem(0);

					edtPass.setText("");
					edtEmail .setText("");
					edtName .setText("");
					edtTel .setText("");
				}
			});
	}

	public void noSetupUi() {
		if (context != null)
			((Activity) context.getActivity()).runOnUiThread(new Runnable() {

				@Override
				public void run() {
					aLayout.setVisibility(View.VISIBLE);
					aProgress.setVisibility(View.GONE);
					aRip.setVisibility(View.VISIBLE);
				}
			});
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

	public boolean isValidEmail(CharSequence target) {
		if (TextUtils.isEmpty(target)) {
			return false;
		} else {
			return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
		}
	}

    public void HideShow(final int pro,final int layout) {
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
						context.getView().findViewById(R.id.no_server_response).setVisibility(View.GONE);
                        HideShow(View.VISIBLE, View.GONE);
						register();

					}
				});

			}
		});

	}

}
