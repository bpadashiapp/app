package ir.tahasystem.music.app.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import ir.onlinefood.app.factory.R;
import ir.tahasystem.music.app.Model.Country;
import ir.tahasystem.music.app.SmsActivity;


public class FragmentSmsContactList extends Fragment {

    public final static String ITEMS_COUNT_KEY = "PartThreeFragment$ItemsCount";
    LinearLayout aLayout;
    public static FragmentSmsContactList context;
    private com.gc.materialdesign.views.ProgressBarIndeterminate aProgress;

    RecyclerView aRecyclerView;
   // FloatingActionButton aFabUp;

    public static int cateId;

    public static FragmentSmsContactList createInstance(int itemsCount) {
        FragmentSmsContactList partThreeFragment = new FragmentSmsContactList();
        Bundle bundle = new Bundle();
        bundle.putInt(ITEMS_COUNT_KEY, itemsCount);
        partThreeFragment.setArguments(bundle);
        return partThreeFragment;

    }

    public static boolean isSelect;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        this.context = this;

        View aView = (View) inflater.inflate(R.layout.main, container, false);


        aProgress = (com.gc.materialdesign.views.ProgressBarIndeterminate) aView.findViewById(R.id.list_load);
        aLayout=(LinearLayout)aView.findViewById(R.id.layout);

        Button myButton = (Button) aView. findViewById(R.id.findSelected);
        myButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                StringBuffer responseText = new StringBuffer();
                responseText.append("The following were selected...\n");

                ArrayList<Country> aListPhone = dataAdapter.countryList;


                if (aListPhone.size() == 0)
                    return;

                StringBuilder uri = new StringBuilder("sms:");
                for (int i = 0; i < aListPhone.size(); i++) {

                    if (aListPhone.get(i).isSelected()) {
                        uri.append(aListPhone.get(i).getCode());
                        uri.append(", ");
                    }
                }
                Intent smsIntent = new Intent(Intent.ACTION_VIEW);
                smsIntent.setType("vnd.android-dir/mms-sms");
                smsIntent.setData(Uri.parse(uri.toString()));
                smsIntent.putExtra("sms_body", "Body of Message");
                startActivity(smsIntent);

                SmsActivity.context.finish();
            }
        });

        CheckBox checkBox= (CheckBox) aView.findViewById(R.id.checkbox);
        checkBox.setChecked(true);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                for(Country country:countryList){
                    country.setIsSelected(isChecked);
                }

                dataAdapter.notifyDataSetChanged();

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

                if(aProgress!=null) aProgress.setVisibility(pro);
                //if(aFabUp!=null) aFabUp.setVisibility(fab);

                aLayout.setVisibility(fab);

            }
        });
    }

    boolean isFill = false;
    private String idz;

    MyCustomAdapter dataAdapter = null;

    final ArrayList<Country> countryList = new ArrayList<Country>();

    private void getData() {

      /*  HideShow(View.VISIBLE, View.GONE);

        //Array list of countries

        countryList.clear();

        ConnectionThreadPool.getInstance().AddTask(new Runnable() {
            @Override
            public void run() {


                ContentResolver cr =context.getActivity().getContentResolver();
                Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                        null, null, null, null);

                if (cur.getCount() > 0) {
                    while (cur.moveToNext()) {
                        String id = cur.getString(
                                cur.getColumnIndex(ContactsContract.Contacts._ID));
                        String name = cur.getString(cur.getColumnIndex(
                                ContactsContract.Contacts.DISPLAY_NAME));

                        if (cur.getInt(cur.getColumnIndex(
                                ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                            Cursor pCur = cr.query(
                                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                    null,
                                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                                    new String[]{id}, null);
                            while (pCur.moveToNext()) {
                                String phoneNo = pCur.getString(pCur.getColumnIndex(
                                        ContactsContract.CommonDataKinds.Phone.NUMBER));

                                if (phoneNo.replace("+98", "0").length() == 11) {
                                    Country country = new Country(name, phoneNo, true);
                                    countryList.add(country);
                                }

                            }
                            pCur.close();
                        }
                    }
                }

                setupView(countryList);

            }
        });*/
    }


    public void setupView(final ArrayList<Country> countryList) {

        if (context == null || context.getActivity() == null || context.getView() == null || !isAdded())
            return;

        final View aView = context.getView();

        context.getActivity().runOnUiThread(new Runnable() {

            @Override
            public void run() {

                //create an ArrayAdaptar from the String Array
                dataAdapter = new MyCustomAdapter(context.getActivity(),
                        R.layout.layout_main, countryList);
                ListView listView = (ListView) context.getView().findViewById(R.id.listView1);
                // Assign adapter to ListView
                listView.setAdapter(dataAdapter);


                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        // When clicked, show a toast with the TextView text
                        Country country = (Country) parent.getItemAtPosition(position);

                    }
                });

                HideShow(View.GONE, View.VISIBLE);
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

                                //context.getActivity().mToolbarContainer.setExpanded(true);
                                HideShow(View.VISIBLE, View.VISIBLE);
                                getData();


                            }
                        });

                if (recyclerAdapter.getItemCount() != 0)
                    snackbar.show();
                else
                    context.getView().findViewById(R.id.no_server_response).setVisibility(View.VISIBLE);


                // context.getActivity().mToolbarContainer.setExpanded(true);

                context.getView().findViewById(R.id.no_server_response_retry).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        context.getView().findViewById(R.id.no_server_response).setVisibility(View.GONE);
                        //context.getActivity().mToolbarContainer.setExpanded(true);
                        HideShow(View.VISIBLE, View.GONE);
                        getData();

                    }
                });

            }
        });

    }

    private class MyCustomAdapter extends ArrayAdapter<Country> {

        private ArrayList<Country> countryList;

        public MyCustomAdapter(Context context, int textViewResourceId,
                               ArrayList<Country> countryList) {
            super(context, textViewResourceId, countryList);
            this.countryList = new ArrayList<Country>();
            this.countryList.addAll(countryList);
        }

        private class ViewHolder {
            TextView code;
            CheckBox name;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder = null;
            Log.v("ConvertView", String.valueOf(position));

            if (convertView == null) {
                LayoutInflater vi = (LayoutInflater) context.getActivity().getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);
                convertView = vi.inflate(R.layout.layout_main, null);

                holder = new ViewHolder();
                holder.code = (TextView) convertView.findViewById(R.id.code);
                holder.name = (CheckBox) convertView.findViewById(R.id.checkBox1);
                convertView.setTag(holder);

                holder.name.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        CheckBox cb = (CheckBox) v;
                        Country country = (Country) cb.getTag();
                        country.setSelected(cb.isChecked());
                    }
                });
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            Country country = countryList.get(position);
            holder.code.setText(" (" + country.getCode() + ")");
            holder.name.setText(country.getName());
            holder.name.setChecked(country.isSelected());
            holder.name.setTag(country);

            return convertView;

        }

    }

    private void checkButtonClick() {


        Button myButton = (Button) context.getView(). findViewById(R.id.findSelected);
        myButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                StringBuffer responseText = new StringBuffer();
                responseText.append("The following were selected...\n");

                ArrayList<Country> aListPhone = dataAdapter.countryList;


                if (aListPhone.size() == 0)
                    return;

                StringBuilder uri = new StringBuilder("sms:");
                for (int i = 0; i < aListPhone.size(); i++) {

                    if (aListPhone.get(i).isSelected()) {
                        uri.append(aListPhone.get(i).getCode());
                        uri.append(", ");
                    }
                }
                Intent smsIntent = new Intent(Intent.ACTION_VIEW);
                smsIntent.setType("vnd.android-dir/mms-sms");
                smsIntent.setData(Uri.parse(uri.toString()));
                smsIntent.putExtra("sms_body", "Body of Message");
                startActivity(smsIntent);

                SmsActivity.context.finish();
            }
        });

    }


}
