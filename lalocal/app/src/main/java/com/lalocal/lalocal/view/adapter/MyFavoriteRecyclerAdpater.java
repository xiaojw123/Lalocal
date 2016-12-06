package com.lalocal.lalocal.view.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.model.Author;
import com.lalocal.lalocal.model.FavoriteItem;
import com.lalocal.lalocal.util.CommonUtil;
import com.lalocal.lalocal.util.DrawableUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by xiaojw on 2016/9/28.
 */

public class MyFavoriteRecyclerAdpater extends BaseRecyclerAdapter {

    Context context;
    List<FavoriteItem> datas;

    public MyFavoriteRecyclerAdpater(List<FavoriteItem> datas) {
        this.datas = datas;
    }

    public void updateListView(List<FavoriteItem> datas) {
        this.datas = datas;
        notifyDataSetChanged();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View itemView = LayoutInflater.from(context).inflate(R.layout.home_me_my_favorite_item, parent, false);
        return new FavoriteHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        FavoriteHolder itemHolder = (FavoriteHolder) holder;
        if (datas != null && datas.size() > 0) {
            FavoriteItem item = datas.get(position);
            if (item != null) {
                int targetType = item.getTargetType();
                Resources res = context.getResources();
                String authorText = "";
                String priceText = "";
                boolean isLive = false;
                itemHolder.author.setText(authorText);
                itemHolder.price.setText(priceText);
                switch (targetType) {
                    case 1:
                    case 13://文章、资讯 ----游记
                        itemHolder.type.setText(res.getString(R.string.travel_notes));
                        Author author = item.getAuthor();
                        if (author != null) {
                            authorText = author.getAuthorName();
                            itemHolder.author.setText(CommonUtil.getSpannelStyle(context, "—— " + authorText, R.style.AuthorNameStyle, 0, 3));
                        }
                        break;
                    case 2://商品
                        itemHolder.type.setText(res.getString(R.string.goods));
                        priceText = CommonUtil.formartOrderPrice(item.getPrice());
                        itemHolder.price.setText(priceText);
                        break;
                    case 9://路线
                        itemHolder.type.setText(res.getString(R.string.route));
                        break;
                    case 10:
                        itemHolder.type.setText(res.getString(R.string.special));
                        break;
                    case 20:
                        isLive = true;
                        itemHolder.type.setText(res.getString(R.string.liveplay));
                        break;
                }
                if (isLive) {
                    itemHolder.liveCotainer.setVisibility(View.VISIBLE);
                    itemHolder.liveLoc.setText(item.getAddress());
                    itemHolder.liveTimeStart.setText(item.getStartAt());
                } else {
                    itemHolder.liveCotainer.setVisibility(View.GONE);
                }

                if (!TextUtils.isEmpty(authorText)) {
                    itemHolder.author.setVisibility(View.VISIBLE);
                } else {
                    itemHolder.author.setVisibility(View.GONE);
                }
                if (!TextUtils.isEmpty(priceText)) {
                    itemHolder.price.setVisibility(View.VISIBLE);
                } else {
                    itemHolder.price.setVisibility(View.GONE);
                }
                itemHolder.title.setText(item.getTargetName());
                double readNum = item.getReadNum();
                String praiseNum = item.getPraiseNum();
                String commentNum = item.getCommentNum();
                if (!TextUtils.isEmpty(formartNum(readNum))) {
                    if (itemHolder.readnNum.getVisibility() != View.VISIBLE) {
                        itemHolder.readnNum.setVisibility(View.VISIBLE);
                    }
                    itemHolder.readnNum.setText(formartNum(readNum));
                } else {
                    itemHolder.readnNum.setVisibility(View.GONE);
                }
                if (!TextUtils.isEmpty(praiseNum)) {
                    if (itemHolder.praiseNum.getVisibility() != View.VISIBLE) {
                        itemHolder.praiseNum.setVisibility(View.VISIBLE);
                    }
                    itemHolder.praiseNum.setText(praiseNum);
                } else {
                    itemHolder.praiseNum.setVisibility(View.GONE);
                }
                if (!TextUtils.isEmpty(commentNum)) {
                    if (itemHolder.commentNum.getVisibility() != View.VISIBLE) {
                        itemHolder.commentNum.setVisibility(View.VISIBLE);
                    }
                    itemHolder.commentNum.setText(commentNum);
                } else {
                    itemHolder.commentNum.setVisibility(View.GONE);
                }


                DrawableUtils.displayImg(context, itemHolder.photo, item.getPhoto());
                itemHolder.itemView.setTag(item);
            }


        }

    }

    public String formartNum(double num) {

        if (num >= 1000) {
            double x = num / 1000;
            return String.valueOf(x);
        }
        return  String.valueOf(num);
    }

    @Override
    public int getItemCount() {
        return datas != null && datas.size() > 0 ? datas.size() : 0;
    }

    class FavoriteHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.my_favorite_item_type)
        TextView type;
        @BindView(R.id.my_favorite_item_img)
        ImageView photo;
        @BindView(R.id.my_favorite_item_title)
        TextView title;
        @BindView(R.id.my_favorite_item_article_author)
        TextView author;
        @BindView(R.id.my_favorite_item_goods_price)
        TextView price;
        @BindView(R.id.my_favorite_item_description)
        TextView description;
        @BindView(R.id.my_favorite_item_readernum_tv)
        TextView readnNum;
        @BindView(R.id.my_favorite_item_savenum_tv)
        TextView praiseNum;
        @BindView(R.id.my_favorite_item_commentnum_tv)
        TextView commentNum;
        @BindView(R.id.my_favorite_live_cotainer)
        LinearLayout liveCotainer;
        @BindView(R.id.my_favorite_live_loc)
        TextView liveLoc;
        @BindView(R.id.my_favorite_live_time)
        TextView liveTimeStart;

        public FavoriteHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


}
