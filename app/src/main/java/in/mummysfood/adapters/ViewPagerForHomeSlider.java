package in.mummysfood.adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import in.mummysfood.R;
import in.mummysfood.widgets.CkdTextview;

/**
 * Created by acer on 4/28/2018.
 */

public class ViewPagerForHomeSlider extends PagerAdapter {

    private Context mContext;
    private LayoutInflater mLayoutInflater;
     private List<String>imageUrl = new ArrayList<>();
      private int Position;
      private int mainPostion;
      private int selectedTrue = 0;


    public ViewPagerForHomeSlider(Context context, List<String>imageUrl) {

        mContext = context;
        this.imageUrl = imageUrl;
        mLayoutInflater  = LayoutInflater.from(context);


    }

    @Override
    public int getCount() {
        return imageUrl.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((RelativeLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position)
    {

        View itemView = mLayoutInflater.inflate(R.layout.quotes_text_layout, container, false);

        CkdTextview quoteTag = (CkdTextview) itemView.findViewById(R.id.quoteTag);

        quoteTag.setText(imageUrl.get(position));

        container.addView(itemView);

        return itemView;

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout) object);
    }


}
