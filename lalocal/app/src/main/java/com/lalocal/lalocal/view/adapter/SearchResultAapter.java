package com.lalocal.lalocal.view.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.model.ArticleItem;
import com.lalocal.lalocal.model.ProductItem;
import com.lalocal.lalocal.model.RouteItem;
import com.lalocal.lalocal.model.SearchItem;
import com.lalocal.lalocal.util.AppLog;
import com.lalocal.lalocal.util.CommonUtil;
import com.lalocal.lalocal.util.DrawableUtils;

import java.util.List;

/**
 * Created by xiaojw on 2016/7/21.
 */
public class SearchResultAapter extends BaseRecyclerAdapter {
    public static final int MODE_TYPE_TITLE = 0x01;
    public static final int MODE_TYPE_MORE = 0x02;
    public static final int MODE_TYPE_ARTICLE = 0x03;
    public static final int MODE_TYPE_PRODUCT = 0x04;
    public static final int MODE_TYPE_ROUTE = 0x05;
    Context mContext;
    List<SearchItem> mItems;


    public SearchResultAapter(Context context, List<SearchItem> items) {
        mContext = context;
        mItems = items;
    }
    public void updateItems(List<SearchItem> items){
        mItems = items;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        AppLog.print("SearchResultAapter___onCreateViewHolder_viewType_"+viewType);
        Resources res = mContext.getResources();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        switch (viewType) {
            case MODE_TYPE_TITLE:
                TextView title = new TextView(mContext);
                title.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) res.getDimension(R.dimen.search_history_title_Height)));
//                title.setTextColor(res.getColor(R.color.color_b3));
                title.setTextColor(res.getColor(R.color.black));
                title.setTextSize(TypedValue.COMPLEX_UNIT_PX, res.getDimension(R.dimen.text_size_12_sp));
                title.setGravity(Gravity.CENTER_VERTICAL);
                return new TitleHolder(title);

            case MODE_TYPE_MORE:
                View titleView = inflater.inflate(R.layout.search_result_item_more, parent, false);
                return new MoreHolder(titleView);
            case MODE_TYPE_ARTICLE:
                View articleView = inflater.inflate(R.layout.search_result_item_article, parent, false);
                return new ArticleHolder(articleView);
            case MODE_TYPE_PRODUCT:
                View productView = inflater.inflate(R.layout.search_result_item_product, parent, false);
                return new ProductHolder(productView);
            case MODE_TYPE_ROUTE:
                View routeView = inflater.inflate(R.layout.search_result_item_route, parent, false);
                return new RouteHolder(routeView);
            case -1:
                View view=inflater.inflate(R.layout.common_adapter_null,parent,false);
                return  new EmptViewHolder(view);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (mItems == null || mItems.size() < 1) {
            return;
        }
        final SearchItem item = mItems.get(position);
        if (item != null) {
            if (holder instanceof TitleHolder) {
                ((TextView) holder.itemView).setText(item.getTitle());
            } else {
                if (holder instanceof MoreHolder) {
                    ((MoreHolder) holder).name.setText(item.getTitle());
                } else if (holder instanceof ArticleHolder) {
                    DrawableUtils.displayImg(mContext, ((ArticleHolder) holder).photo, item.getPhoto());
                    ((ArticleHolder) holder).title.setText(item.getTitle());
                    ArticleItem.AuthorVOBean authorVOBean = ((ArticleItem) item).getAuthorVO();
                    if (authorVOBean != null) {
                        DrawableUtils.displayImg(mContext, ((ArticleHolder) holder).avatar, authorVOBean.getAuthorAvatar());
                        ((ArticleHolder) holder).author.setText(authorVOBean.getAuthorName());
                    }
                } else if (holder instanceof ProductHolder) {
                    DrawableUtils.displayImg(mContext, ((ProductHolder) holder).photo, item.getPhoto());
                    ((ProductHolder) holder).title.setText(item.getTitle());
                    String text=CommonUtil.fomartStartOrderPrice( ((ProductItem) item).getPrice());
                    int len=text.length();
                    ((ProductHolder) holder).price.setText(CommonUtil.getSpannelStyle(mContext,text,R.style.SearchOrderPriceStyle,len-1,len));
                } else if (holder instanceof RouteHolder) {
                    DrawableUtils.displayImg(mContext, ((RouteHolder) holder).photo, item.getPhoto());
                    ((RouteHolder) holder).title.setText(item.getTitle());
                    RouteItem.AuthorBean authorBean = ((RouteItem) item).getAuthor();
                    if (authorBean != null) {
                        ((RouteHolder) holder).author.setText(authorBean.getAuthorName());
                        DrawableUtils.displayImg(mContext, ((RouteHolder) holder).avatar, authorBean.getAuthorAvatar());
                    }
                }
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (listener != null) {
                            v.setTag(item);
                            listener.onItemClickListener(v, position);
                        }
                    }
                });

            }

        }


    }

    @Override
    public int getItemViewType(int position) {
        return mItems != null && mItems.size() > 0 ? mItems.get(position).getModeltype() : -1;
    }

    @Override
    public int getItemCount() {
        return mItems != null && mItems.size() > 0 ? mItems.size() : 1;
    }

    class TitleHolder extends RecyclerView.ViewHolder {

        public TitleHolder(View itemView) {
            super(itemView);
        }
    }

    class MoreHolder extends RecyclerView.ViewHolder {
        TextView name;

        public MoreHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.search_result_item_more_name);
        }
    }

    class ArticleHolder extends RecyclerView.ViewHolder {

        ImageView photo;
        TextView title;
        TextView author;
        ImageView avatar;

        public ArticleHolder(View itemView) {
            super(itemView);
            photo = (ImageView) itemView.findViewById(R.id.search_result_item_img);
            title = (TextView) itemView.findViewById(R.id.search_result_item_title);
            author = (TextView) itemView.findViewById(R.id.search_result_item_article_author);
            avatar = (ImageView) itemView.findViewById(R.id.search_result_item_article_avatar);
        }
    }

    class ProductHolder extends RecyclerView.ViewHolder {
        ImageView photo;
        TextView title;
        TextView price;

        public ProductHolder(View itemView) {
            super(itemView);
            photo = (ImageView) itemView.findViewById(R.id.search_result_item_img);
            title = (TextView) itemView.findViewById(R.id.search_result_item_title);
            price = (TextView) itemView.findViewById(R.id.search_result_item_product_price);
        }
    }

    class RouteHolder extends RecyclerView.ViewHolder {
        ImageView photo;
        TextView title;
        ImageView avatar;
        TextView author;

        public RouteHolder(View itemView) {
            super(itemView);
            photo = (ImageView) itemView.findViewById(R.id.search_result_item_img);
            title = (TextView) itemView.findViewById(R.id.search_result_item_title);
            avatar = (ImageView) itemView.findViewById(R.id.search_result_item_route_avatar);
            author = (TextView) itemView.findViewById(R.id.search_result_item_route_author);
        }
    }


}
