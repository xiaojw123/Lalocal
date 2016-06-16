package com.lalocal.lalocal.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lalocal.lalocal.R;

/**
 * Created by xiaojw on 2016/6/11.
 */
public class MyFavoriteAdpater extends RecyclerView.Adapter<MyFavoriteAdpater.ViewHolder> {

    Context context;

    public MyFavoriteAdpater(Context context) {
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.home_me_my_favorite_item, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {


    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView typeTv;
        ImageView postImg;
        TextView titleTv;
        TextView goodsPrice;
        TextView articleAuthor;
        TextView routeCollectNum;
        TextView informationCommentNum;

        public ViewHolder(View itemView) {
            super(itemView);
            typeTv = (TextView) itemView.findViewById(R.id.my_favorite_item_type);
            postImg = (ImageView) itemView.findViewById(R.id.my_favorite_item_img);
            titleTv = (TextView) itemView.findViewById(R.id.my_favorite_item_title);
            goodsPrice = (TextView) itemView.findViewById(R.id.my_favorite_item_goods_price);
            articleAuthor = (TextView) itemView.findViewById(R.id.my_favorite_item_article_author);
            routeCollectNum = (TextView) itemView.findViewById(R.id.my_favorite_item_route_collect_num);
            informationCommentNum = (TextView) itemView.findViewById(R.id.my_favorite_item_information_comment_num);
        }
    }

}
