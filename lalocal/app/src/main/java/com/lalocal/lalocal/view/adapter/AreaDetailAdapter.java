package com.lalocal.lalocal.view.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.model.ProductItem;
import com.lalocal.lalocal.model.RouteItem;
import com.lalocal.lalocal.model.SearchItem;
import com.lalocal.lalocal.util.AppLog;
import com.lalocal.lalocal.util.CommonUtil;
import com.lalocal.lalocal.util.DrawableUtils;

import java.util.List;

/**
 * Created by xiaojw on 2016/7/25.
 */
public class AreaDetailAdapter extends BaseAdapter {

    public static final int MODULE_TYPE_PRODUCT = 0x10;
    public static final int MODULE_TYPE_ROUTE = 0x11;
    public static final int MODULE_TYPE_TITLE = 0x12;
    public static final int PAGE_HOT = 0x110;
    public static final int PAGE_STRATEGY = 0x111;
    public static final int PAGE_PACKAGETOUR = 0x112;
    public static final int PAGE_FREEWARKER = 0x113;
    int pageType;
    Context context;
    List<SearchItem> items;
    LayoutInflater inflater;
    Resources res;

    public AreaDetailAdapter(Context context, List<SearchItem> items) {
        this.context = context;
        this.items = items;
        inflater = LayoutInflater.from(context);
        res = context.getResources();
    }

    public void updateItems(List<SearchItem> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    public void setPageType(int pageType) {
        this.pageType = pageType;
    }

    public int getPageType() {
        return pageType;
    }


    @Override
    public int getCount() {
        return items != null && items.size() > 0 ? items.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return items != null && items.size() > 0 ? items.get(position) : null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (items == null || items.size() < 1) {
            return null;
        }
        SearchItem item = items.get(position);
        AppLog.print("item___size___" + items.size() + ", positoin___" + position);
        if (item != null) {
            if (item instanceof ProductItem) {
                AppLog.print("加载商品item___");
                convertView = getModuelProduct(convertView, parent, item);
                convertView.setTag(R.id.areaDetialItem, item);

            } else if (item instanceof RouteItem) {
                AppLog.print("加载路线item___");
                convertView = getModuelRoute(convertView, parent, item);
                convertView.setTag(R.id.areaDetialItem, item);
            } else {
                AppLog.print("加载标题item___");
                convertView = getModuelTitle(item);
            }
        } else {
            if (position == items.size() - 1) {
                AppLog.print("加载不出item___");
                convertView = inflater.inflate(R.layout.load_more_layout, null);
                convertView.setFocusableInTouchMode(false);
                convertView.setFocusable(false);
            }
        }
        return convertView;
    }

    @NonNull
    private View getModuelTitle(SearchItem item) {
        View convertView;
        TextView title = new TextView(context);
        title.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        title.setPadding(0, 0, 0, (int) res.getDimension(R.dimen.dimen_size_8_dp));
        Drawable drawable = res.getDrawable(R.drawable.hot_sale_ic);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        title.setCompoundDrawables(drawable, null, null, null);
        title.setCompoundDrawablePadding((int) res.getDimension(R.dimen.dimen_size_4_dp));
        title.setTextColor(res.getColor(R.color.color_8c));
        title.setTextSize(TypedValue.COMPLEX_UNIT_PX, res.getDimension(R.dimen.text_size_12_sp));
        title.setText(item.getTitle());
        convertView = title;
        return convertView;
    }

    @NonNull
    private View getModuelRoute(View convertView, ViewGroup parent, SearchItem item) {
        if (convertView != null) {
            Object tag = convertView.getTag();
            if (tag == null || !(tag instanceof RouteHolder)) {
                convertView = null;
            }
        }
        RouteHolder holder;
        if (convertView == null) {
            holder = new RouteHolder();
            convertView = inflater.inflate(R.layout.area_detail_item_route, parent, false);
            holder.photo = (ImageView) convertView.findViewById(R.id.area_detail_item_route_img);
            holder.title = (TextView) convertView.findViewById(R.id.area_detail_item_route_title);
            convertView.setTag(holder);
        } else {
            holder = (RouteHolder) convertView.getTag();
        }
        AppLog.print("holder___" + holder + ", photo__" + holder.photo);
        DrawableUtils.displayImg(context, holder.photo, item.getPhoto());
        holder.title.setText(item.getTitle());
        return convertView;
    }

    @NonNull
    private View getModuelProduct(View convertView, ViewGroup parent, SearchItem item) {
        if (convertView != null) {
            Object tag = convertView.getTag();
            if (tag == null || !(tag instanceof ProductHolder)) {
                convertView = null;
            }
        }

        ProductHolder holder;
        if (convertView == null) {
            holder = new ProductHolder();
            convertView = inflater.inflate(R.layout.area_detail_item_product, parent, false);
            holder.photo = (ImageView) convertView.findViewById(R.id.area_detail_item_product_img);
            holder.title = (TextView) convertView.findViewById(R.id.area_detail_item_product_title);
            holder.price = (TextView) convertView.findViewById(R.id.area_detail_item_product_price);
            convertView.setTag(holder);
        } else {
            holder = (ProductHolder) convertView.getTag();
        }
        double price = ((ProductItem) item).getPrice();
        DrawableUtils.displayImg(context, holder.photo, item.getPhoto());
        holder.title.setText(item.getTitle());
        holder.price.setText(CommonUtil.formartOrderPrice(price));
        return convertView;
    }

    class ProductHolder {
        ImageView photo;
        TextView title;
        TextView price;

    }

    class RouteHolder {
        ImageView photo;
        TextView title;
    }


}
