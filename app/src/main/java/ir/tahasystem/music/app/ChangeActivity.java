package ir.tahasystem.music.app;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.github.johnpersano.supertoasts.SuperActivityToast;
import com.github.johnpersano.supertoasts.SuperToast;
import com.github.johnpersano.supertoasts.util.Style;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import org.ksoap2.serialization.SoapPrimitive;

import java.io.ByteArrayOutputStream;

import ir.onlinefood.app.factory.R;
import ir.tahasystem.music.app.DialogBox.LoadBox;
import ir.tahasystem.music.app.Model.Kala;
import ir.tahasystem.music.app.Model.ModelHolder;
import ir.tahasystem.music.app.Model.ObjProduct;
import ir.tahasystem.music.app.ThreadPool.ConnectionThreadPool;
import ir.tahasystem.music.app.connection.ConnectionPool;
import ir.tahasystem.music.app.custom.CustomViewPager;
import ir.tahasystem.music.app.fragment.FragmentChangeFilter;
import ir.tahasystem.music.app.fragment.FragmentHome;
import ir.tahasystem.music.app.fragment.FragmentHomeCatesChange;
import ir.tahasystem.music.app.utils.NetworkUtil;
import ir.tahasystem.music.app.utils.TextWatchers;


public class ChangeActivity extends AppCompatActivity {

    public static ProgressDialog aProgressDialog;
    public static Kala aKala;
    public Toolbar mToolbar;
    public static ChangeActivity context;
    public NavigationView navigationView;
    public static DrawerLayout drawerLayout;

    public static String path;
    public static String pathCache;
    public LoadBox aLoadBox;

    FragmentHome home;
    public CustomViewPager viewPager;

    public TextView subTitle;

    TabLayout aTabLayout;


    ImageView mImageView;
    public EditText edtPrice, edtCPrice, edtUintInStock, edtMinOrder;

    LinearLayout aLayout;
    private com.gc.materialdesign.views.ProgressBarIndeterminate aProgress;


    private static final int ASK_MULTIPLE_PERMISSION_REQUEST_CODE = 112;
    public Uri imageUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        context = this;

        setTheme(R.style.AppThemeBlueMain);


        super.onCreate(savedInstanceState);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setContentView(R.layout.activity_change);

        if (Values.appId > 0) {
            this.findViewById(R.id.change_part2).setVisibility(View.GONE);
            this.findViewById(R.id.change_part3).setVisibility(View.VISIBLE);
        } else {
            this.findViewById(R.id.change_part2).setVisibility(View.VISIBLE);
            this.findViewById(R.id.change_part3).setVisibility(View.GONE);
        }

        aProgress = (com.gc.materialdesign.views.ProgressBarIndeterminate) findViewById(R.id.list_load);
        aLayout = (LinearLayout) findViewById(R.id.layout);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mToolbar.setTitle(null);
        mToolbar.setSubtitle(null);
        mToolbar.setTitleTextColor(getResources().getColor(android.R.color.white));

        initToolbar();

        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                System.out.println("1");

                finish();

                return false;

            }
        });

        mImageView = (ImageView) findViewById(R.id.image);

        try {
            Picasso.with(context)
                    .load(aKala.image).skipMemoryCache()
                    .into(mImageView);

        } catch (Exception e) {

        }


        this.findViewById(R.id.change_photo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                capturarFoto();


            }
        });

        edtPrice = (EditText) findViewById(R.id.chg_price);
        edtCPrice = (EditText) findViewById(R.id.chg_cprice);
        edtUintInStock = (EditText) findViewById(R.id.chg_unit_in_stock);
        edtMinOrder = (EditText) findViewById(R.id.chg_min_order);

        new TextWatchers(edtPrice);
        //new TextWatchers(edtCPrice );
        new TextWatchers(edtUintInStock);
        new TextWatchers(edtMinOrder);

        if (aKala == null)
            return;


        if (aKala.price != 0)
            aKala.priceByDiscount = aKala.price;

        edtPrice.setText(String.valueOf(aKala.priceByDiscount));
        edtCPrice.setText(String.valueOf(aKala.discount));
        edtUintInStock.setText(String.valueOf(aKala.unitsInStock));
        edtMinOrder.setText(String.valueOf(aKala.minOrder));


        final ObjProduct aObjProduct = new ObjProduct();
        aObjProduct.id = aKala.id;
        aObjProduct.minOrder = aKala.minOrder;
        aObjProduct.unitsInStock=aKala.unitsInStock;

        Button aRip = (Button) findViewById(R.id.save);
        aRip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                boolean check = true;

                if (edtPrice.getText().toString().trim().length() != 0) {
                    edtPrice.setError(null);
                    aObjProduct.price = Double.parseDouble(edtPrice.getText().toString().replace(",", ""));
                } else {
                    edtPrice.setError(v.getContext().getString(R.string.fill));
                    check = false;
                }

                if (edtCPrice.getText().toString().trim().length() != 0) {
                    edtCPrice.setError(null);
                    aObjProduct.discount = Double.parseDouble(edtCPrice.getText().toString().replace(",", ""));
                } else {
                    edtCPrice.setError(v.getContext().getString(R.string.fill));
                    check = false;
                }

                if (Values.appId == 0) {

                    if (edtUintInStock.getText().toString().trim().length() != 0) {
                        edtUintInStock.setError(null);
                        aObjProduct.unitsInStock = Integer.parseInt(edtUintInStock.getText().toString().replace(",", ""));
                    } else {
                        edtUintInStock.setError(v.getContext().getString(R.string.fill));
                        check = false;
                    }

                    if (edtMinOrder.getText().toString().trim().length() != 0) {
                        edtMinOrder.setError(null);
                        aObjProduct.minOrder = Integer.parseInt(edtMinOrder.getText().toString().replace(",", ""));
                    } else {
                        edtMinOrder.setError(v.getContext().getString(R.string.fill));
                        check = false;
                    }
                } else {
                    aObjProduct.minOrder = Integer.parseInt(edtMinOrder.getText().toString().replace(",", ""));
                   // aObjProduct.unitsInStock = Integer.parseInt(edtUintInStock.getText().toString().replace(",", ""));
                }

                if (check)
                    setData(aObjProduct);
                else
                    msg(v.getContext().getString(R.string.plz_fill));

            }
        });

        final RadioGroup radioGroup = (RadioGroup) findViewById(R.id.chg_rg_unit);
        RadioButton radioGroupHave = (RadioButton) findViewById(R.id.chg_rb_have_store);
        RadioButton radioGroupNotHave = (RadioButton) findViewById(R.id.chg_rb_not_have_store);

        if (aKala.unitsInStock > 0) {
            radioGroupHave.setChecked(true);
            radioGroupHave.setSelected(true);

            radioGroupNotHave.setChecked(false);
            radioGroupNotHave.setSelected(false);
        }


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                System.out.println(" notInPlace000000000 " + ModelHolder.getInstance().ShippingCost);


                if (checkedId == R.id.chg_rb_have_store) {
                    aObjProduct.unitsInStock = 1000;
                } else if (checkedId == R.id.chg_rb_not_have_store) {
                    aObjProduct.unitsInStock = 0;
                }


            }
        });


        HideShow(View.GONE, View.VISIBLE);
    }


    private void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        mToolbar.setTitleTextColor(getResources().getColor(android.R.color.white));

        setTitle("");
        TextView atxt = (TextView) mToolbar.findViewById(R.id.toolbar_title);
        atxt.setText(aKala.name);


    }


    private void setData(final ObjProduct aObjProduct) {

        HideShow(View.VISIBLE, View.GONE);

        ConnectionThreadPool.getInstance().AddTask(new Runnable() {

            @Override
            public void run() {

                try {


                    Bitmap bitmap = ((BitmapDrawable) mImageView.getDrawable()).getBitmap();

                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream);

                    aObjProduct.img = Base64.encodeToString(stream.toByteArray(), Base64.DEFAULT);

                    ConnectionPool aConnectionPool = new ConnectionPool();

                    SoapPrimitive aSoapObject = null;
                    if (NetworkUtil.getConnectivityStatusString(context) != null)
                        aSoapObject = aConnectionPool.ConnectAddProduct(aObjProduct);


                    if (aSoapObject == null) {
                        noServerResponse();
                        return;
                    }

                    if (aSoapObject.equals("0"))
                        msg(context.getString(R.string.not_done));
                    else {


                        setupView();
                    }

                    HideShow(View.GONE, View.VISIBLE);


                    System.out.println(aSoapObject);


                } catch (Exception e) {
                    noServerResponse();
                    e.printStackTrace();
                }


            }
        });


    }

    public void setupView() {
        if (context == null)
            return;

        ((Activity) context).runOnUiThread(new Runnable() {

            @Override
            public void run() {
                if (FragmentHomeCatesChange.context != null
                        && FragmentHomeCatesChange.context.fragmentProduct != null) {
                    if (FragmentHomeCatesChange.context.fragmentProduct.recyclerAdapter != null)
                        FragmentHomeCatesChange.context.fragmentProduct.recyclerAdapter.clearItem();
                    FragmentHomeCatesChange.context.fragmentProduct.isInit = false;
                    FragmentHomeCatesChange.context.fragmentProduct.init();
                }
                if (FragmentChangeFilter.context != null) {
                    if (FragmentChangeFilter.context.recyclerAdapter != null)
                        FragmentChangeFilter.context.recyclerAdapter.clearItem();
                    FragmentChangeFilter.context.count = 0;
                    FragmentChangeFilter.context.isFill = false;
                    FragmentChangeFilter.context.loading = true;
                    FragmentChangeFilter.context.isInit = false;
                    FragmentChangeFilter.context.init();
                }
                context.finish();
            }
        });
    }


    public void noServerResponse() {

        HideShow(View.GONE, View.VISIBLE);

        if (context == null)
            return;

        ((Activity) context).runOnUiThread(new Runnable() {

            @Override
            public void run() {


                findViewById(R.id.no_server_response).setVisibility(View.VISIBLE);
                findViewById(R.id.no_server_response_retry).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        findViewById(R.id.no_server_response).setVisibility(View.GONE);
                        HideShow(View.GONE, View.VISIBLE);
                        //pay();

                    }
                });

            }
        });

    }

    public void HideShow(final int pro, final int layout) {
        if (context == null)
            return;

        ((Activity) context).runOnUiThread(new Runnable() {

            @Override
            public void run() {
                if (aProgress != null) aProgress.setVisibility(pro);
                aLayout.setVisibility(layout);
            }
        });
    }


    public synchronized void ani(final View v) {

        Animation fadeIn = new AlphaAnimation(0.0f, 1.0f);
        fadeIn.setInterpolator(new DecelerateInterpolator());

        fadeIn.setDuration(1000);

        v.startAnimation(fadeIn);

    }

    public void msg(final String msg) {

        ((Activity) context).runOnUiThread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub

                final Style customStyle = new Style();
                customStyle.animations = SuperToast.Animations.SCALE;
                customStyle.background = SuperToast.Background.BLUE;
                customStyle.textColor = Color.parseColor("#ffffff");
                customStyle.buttonTextColor = Color.WHITE;
                customStyle.dividerColor = Color.WHITE;

                SuperActivityToast superActivityToast = new SuperActivityToast(context, customStyle);
                superActivityToast.setDuration(SuperToast.Duration.MEDIUM);
                superActivityToast.setText(msg);
                superActivityToast.show();

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.back_item, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {

        if (menuItem.getItemId() == android.R.id.home) {
            finish();
        } else if (menuItem.getItemId() == R.id.action_back) {
            finish();
        }
        return super.onOptionsItemSelected(menuItem);
    }


    public void onResume() {
        super.onResume();

        if (imageUri != null) {
            mImageView.setImageDrawable(null); // <--- added to force redraw of ImageView
            mImageView.setImageURI(imageUri);
            return;
        }


        if (context == null)
            return;


    }


    public void onDestroy() {
        super.onDestroy();
    }


    private void capturarFoto() {

       /* String file = Environment.getDownloadCacheDirectory() + "/" + DateFormat.format("yyyy-MM-dd_hhmmss", new Date()).toString() + ".jpg";


        File newfile = new File(file);
        try {
            newfile.createNewFile();
        } catch (IOException e) {
        }

       // Uri outputFileUri = Uri.fromFile(newfile);

        //Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
       // cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);

        //startActivityForResult(cameraIntent, 1001);*/

        Intent pickIntent = new Intent();
        pickIntent.setType("image/*");
        pickIntent.setAction(Intent.ACTION_GET_CONTENT);

        Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        takePhotoIntent.putExtra(MediaStore.EXTRA_SCREEN_ORIENTATION, ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        String pickTitle = getString(R.string.take_or_select_photo);
        Intent chooserIntent = Intent.createChooser(pickIntent, pickTitle);
        chooserIntent.putExtra
                (
                        Intent.EXTRA_INITIAL_INTENTS,
                        new Intent[]{takePhotoIntent}
                );

        startActivityForResult(chooserIntent, 1001);

        /*Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI.getPath());
        startActivityForResult(intent, 1001);*/
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1001 && resultCode == RESULT_OK) {
            Log.d("Demo Pic", "Picture is saved");

            if (data == null || data.getData() == null)
                return;

            Uri uri = data.getData();

            CropImageActivity.img = uri;

            startActivity(new Intent(this, CropImageActivity.class));


        } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }
}
