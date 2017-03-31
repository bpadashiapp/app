package ir.tahasystem.music.app.findMap;

import android.content.Context;
import android.location.Address;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;

import java.util.List;

import ir.onlinefood.app.factory.R;


/*
* RecyclerView Adapter that allows to add a header view.
* */
public class RecyclerAdapter2 extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 2;
    private static final int TYPE_ITEM = 1;
    private List<Address> mItemList;

    private Context context;
    private Fragment2 fragmentActivity;

    public RecyclerAdapter2(final Fragment2 fragmentActivity, List<Address> itemList) {
        mItemList = itemList;
        this.fragmentActivity = fragmentActivity;


    }

    public void scaleView(View v) {

        Animation logoMoveAnimation = AnimationUtils.loadAnimation(context, R.anim.scalez_half_down);
        v.startAnimation(logoMoveAnimation);

    }


    public void addItem(List<Address> aList, int position) {
        mItemList.addAll(aList);
        notifyItemInserted(position);
    }

    public void clearItem() {
        mItemList.clear();
        this.notifyDataSetChanged();

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();


        final View view = LayoutInflater.from(context).inflate(R.layout.recycler_item_loc, parent, false);
        return RecyclerItemViewHolder2.newInstance(this, view);

    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

        RecyclerItemViewHolder2 holder = (RecyclerItemViewHolder2) viewHolder;


        Address addresses = mItemList.get(position);


        String address = addresses.getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
        String city = addresses.getLocality();
        String state = addresses.getAdminArea();
        // String country = addresses.getCountryName();

        holder.mItemTextViewName.setText(addresses.getCountryName());
        holder.mItemTextViewDes.setText(getCompleteAddressString(addresses));


    }

    public int getBasicItemCount() {
        return mItemList == null ? 0 : mItemList.size();
    }

    @Override
    public int getItemViewType(int position) {


        return TYPE_ITEM;
    }

    @Override
    public int getItemCount() {
        return getBasicItemCount(); // header
    }

    private boolean isPositionHeader(int position) {
        return position == 0;
    }

    public void onTapz(final int pos) {



        fragmentActivity.getData(mItemList.get(pos).getLatitude(), mItemList.get(pos).getLongitude());


    }

    private String getCompleteAddressString(Address returnedAddress) {

        StringBuilder strReturnedAddress = new StringBuilder("");

        for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
            strReturnedAddress.append(returnedAddress.getAddressLine(i)).append(" ");
        }

        return strReturnedAddress.toString();

    }



    public synchronized void ani(final View v) {

        Animation fadeIn = new AlphaAnimation(0.0f, 1.0f);
        fadeIn.setInterpolator(new DecelerateInterpolator());

        fadeIn.setDuration(1000);

        v.startAnimation(fadeIn);

    }


}
