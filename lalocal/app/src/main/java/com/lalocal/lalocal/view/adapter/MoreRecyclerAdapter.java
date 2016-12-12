package com.lalocal.lalocal.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.model.ArticleItem;
import com.lalocal.lalocal.model.ProductItem;
import com.lalocal.lalocal.model.SearchItem;
import com.lalocal.lalocal.util.CommonUtil;
import com.lalocal.lalocal.util.DrawableUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by xiaojw on 2016/11/23.
 */

public class MoreRecyclerAdapter extends BaseRecyclerAdapter {
    public static final int MODUEL_TYPE_ARTICLE = 0x10;
    public static final int MODUEL_TYPE_PRODUCT = 0x11;
    public static final int MODUEL_TYPE_ROUTE = 0x12;
    Context mContext;
    List<SearchItem> mItems;
    int modeltype;

    public MoreRecyclerAdapter(Context context, List<SearchItem> items, int type) {
        mContext = context;
        mItems = items;
        modeltype = type;
    }

    public void updateItems(List<SearchItem> items) {
        mItems = items;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return modeltype;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        if (viewType == MODUEL_TYPE_ARTICLE) {
            View view = inflater.inflate(R.layout.item_search_article, parent, false);
            return new ArticleHolder(view);
        } else if (viewType == MODUEL_TYPE_PRODUCT) {
            View view = inflater.inflate(R.layout.search_result_item, parent, false);
            return new ProductHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        super.onBindViewHolder(holder, position);
        if (mItems != null && mItems.size() > 0) {
            SearchItem searchItem = mItems.get(position);
            if (searchItem != null) {
                if (holder instanceof ArticleHolder) {
                    ArticleHolder articleHolder = (ArticleHolder) holder;
                    DrawableUtils.displayImg(mContext, articleHolder.photo, searchItem.getPhoto());
                    articleHolder.title.setText(searchItem.getTitle());
                    articleHolder.description.setText(((ArticleItem) searchItem).getDescription());
                    articleHolder.readNum.setText(searchItem.getReadNum());
                    articleHolder.praieseNum.setText(searchItem.getPraiseNum());
                    ArticleItem.AuthorVOBean authorVOBean = ((ArticleItem) searchItem).getAuthorVO();
                    articleHolder.authorName.setText(authorVOBean.getAuthorName());
                } else if (holder instanceof ProductHolder) {

                    ProductHolder productHolder = (ProductHolder) holder;
                    DrawableUtils.displayImg(mContext, productHolder.photo, searchItem.getPhoto());
                    productHolder.title.setText(searchItem.getTitle());
                    productHolder.readNum.setText(searchItem.getReadNum());
                    productHolder.praieseNum.setText(searchItem.getPraiseNum());
                    if (searchItem instanceof ProductItem) {
                        String text = CommonUtil.formartOrderPrice(((ProductItem) searchItem).getPrice());
                        productHolder.price.setText(text);
                    }

                }
                holder.itemView.setTag(searchItem);
            }
        }

    }

    @Override
    public int getItemCount() {
        return mItems != null && mItems.size() > 0 ? mItems.size() : 0;
    }

    class ArticleHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.img_article)
        ImageView photo;
        @BindView(R.id.tv_article_title)
        TextView title;
        @BindView(R.id.tv_read_num)
        TextView readNum;
        @BindView(R.id.tv_praise_num)
        TextView praieseNum;
        @BindView(R.id.tv_description)
        TextView description;
        @BindView(R.id.tv_author_name)
        TextView authorName;

        public ArticleHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class RouteHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.search_result_item_img)
        ImageView photo;
        @BindView(R.id.search_result_item_title)
        TextView title;
        @BindView(R.id.search_result_item_route_avatar)
        ImageView avatar;
        @BindView(R.id.search_result_item_route_author)
        TextView author;

        public RouteHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class ProductHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.search_result_item_img)
        ImageView photo;
        @BindView(R.id.search_result_item_title)
        TextView title;
        @BindView(R.id.search_result_item_product_price)
        TextView price;
        @BindView(R.id.search_result_item_read)
        TextView readNum;
        @BindView(R.id.search_result_item_collect)
        TextView praieseNum;

        public ProductHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
