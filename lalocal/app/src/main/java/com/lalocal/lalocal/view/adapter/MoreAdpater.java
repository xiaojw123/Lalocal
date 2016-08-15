package com.lalocal.lalocal.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.model.ArticleItem;
import com.lalocal.lalocal.model.SearchItem;
import com.lalocal.lalocal.model.ProductItem;
import com.lalocal.lalocal.model.RouteItem;
import com.lalocal.lalocal.util.AppLog;
import com.lalocal.lalocal.util.CommonUtil;
import com.lalocal.lalocal.util.DrawableUtils;

import java.util.List;

/**
 * Created by xiaojw on 2016/7/24.
 */
public class MoreAdpater extends BaseAdapter {
    public static final int MODUEL_TYPE_ARTICLE = 0x10;
    public static final int MODUEL_TYPE_PRODUCT = 0x11;
    public static final int MODUEL_TYPE_ROUTE = 0x12;

    Context mContext;
    List<SearchItem> mItems;

    public MoreAdpater(Context context, List<SearchItem> items) {
        mContext = context;
        mItems = items;

    }

    public void updateItems(List<SearchItem> items) {
        mItems = items;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mItems != null && mItems.size() > 0 ? mItems.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return mItems != null && mItems.size() > 0 ? mItems.get(position) : null;
    }

    @Override
    public long getItemId(int position) {
        return position;

    }

    @Override
    public int getItemViewType(int position) {
        int type = -1;
        if (mItems != null && mItems.size() > 0) {
            SearchItem item = mItems.get(position);
            if (item != null) {
                type = item.getModeltype();
            }
        }
        return type;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (mItems == null || mItems.size() < 1) {
            return null;
        }
        SearchItem item = mItems.get(position);
        int type = getItemViewType(position);
        LayoutInflater inflater = LayoutInflater.from(mContext);
        ViewHolder holder;
        switch (type) {
            case MODUEL_TYPE_ARTICLE:
                if (convertView == null) {
                    convertView = inflater.inflate(R.layout.search_result_item_article, parent, false);
                    holder = new ViewHolder();
                    holder.photo = (ImageView) convertView.findViewById(R.id.search_result_item_img);
                    holder.title = (TextView) convertView.findViewById(R.id.search_result_item_title);
                    holder.author = (TextView) convertView.findViewById(R.id.search_result_item_article_author);
                    holder.avatar = (ImageView) convertView.findViewById(R.id.search_result_item_article_avatar);
                    convertView.setTag(holder);
                } else {
                    holder = (ViewHolder) convertView.getTag();
                }
                if (item != null) {
                    DrawableUtils.displayImg(mContext, holder.photo, item.getPhoto());
                    holder.title.setText(item.getTitle());
                    ArticleItem.AuthorVOBean bean = ((ArticleItem) item).getAuthorVO();
                    if (bean != null) {
                        DrawableUtils.displayImg(mContext, holder.avatar, bean.getAuthorAvatar());
                        holder.author.setText(bean.getAuthorName());
                    }
                }
                break;
            case MODUEL_TYPE_PRODUCT:
                if (convertView == null) {
                    convertView = inflater.inflate(R.layout.search_result_item_product, null);
                    holder = new ViewHolder();
                    holder.photo = (ImageView) convertView.findViewById(R.id.search_result_item_img);
                    holder.title = (TextView) convertView.findViewById(R.id.search_result_item_title);
                    holder.price = (TextView) convertView.findViewById(R.id.search_result_item_product_price);
                    convertView.setTag(holder);
                } else {
                    holder = (ViewHolder) convertView.getTag();
                }
                if (item != null) {
                    AppLog.print("holder*****----****" + holder);
                    DrawableUtils.displayImg(mContext, holder.photo, item.getPhoto());
                    holder.title.setText(item.getTitle());
                    holder.price.setText(CommonUtil.formartOrderPrice(((ProductItem)item).getPrice()));
                }
                break;
            case MODUEL_TYPE_ROUTE:
                if (convertView == null) {
                    convertView = inflater.inflate(R.layout.search_result_item_route, parent, false);
                    holder = new ViewHolder();
                    holder.photo = (ImageView) convertView.findViewById(R.id.search_result_item_img);
                    holder.title = (TextView) convertView.findViewById(R.id.search_result_item_title);
                    holder.avatar = (ImageView) convertView.findViewById(R.id.search_result_item_route_avatar);
                    holder.author = (TextView) convertView.findViewById(R.id.search_result_item_route_author);
                    convertView.setTag(holder);
                } else {
                    holder = (ViewHolder) convertView.getTag();
                }
                if (item != null) {
                    DrawableUtils.displayImg(mContext, holder.photo, item.getPhoto());
                    holder.title.setText(item.getTitle());
                    RouteItem.AuthorBean bean = ((RouteItem) item).getAuthor();
                    if (bean != null) {
                        DrawableUtils.displayImg(mContext, holder.avatar, bean.getAuthorAvatar());
                        holder.author.setText(bean.getAuthorName());
                    }
                }
                break;

        }
        AppLog.print("getview end contentview___" + convertView);
        convertView.setTag(R.id.moreSearchItemId,item);
        return convertView;
    }


    class ViewHolder {
        ImageView photo;
        TextView title;
        ImageView avatar;
        TextView author;
        TextView price;

    }
}
