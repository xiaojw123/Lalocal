package com.lalocal.lalocal.view.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.model.Author;
import com.lalocal.lalocal.model.FavoriteItem;
import com.lalocal.lalocal.util.CommonUtil;
import com.lalocal.lalocal.util.DrawableUtils;
import com.lalocal.lalocal.view.xlistview.XListView;

import java.util.List;

/**
 * Created by xiaojw on 2016/6/21.
 * 我的收藏适配器 目前共五种类型 targeType  1文章 2产品 9线路 10专题 13资讯
 */
public class MyFavoriteAdapter extends BaseAdapter {
    Context context;
    List<FavoriteItem> datas;


    public MyFavoriteAdapter(Context context, List<FavoriteItem> datas) {
        this.context = context;
        this.datas = datas;

    }

    public void updateListView(List<FavoriteItem> datas) {
        this.datas = datas;
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return datas == null || datas.size() < 1 ? 1 : datas.size();
    }

    @Override
    public Object getItem(int position) {
        return datas == null || datas.size() < 1 ? null : datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        XListView xListView = (XListView) parent;
        LayoutInflater inflater = LayoutInflater.from(context);
        if (position == 0 && (datas == null || datas.size() < 1)) {
            xListView.setEnabled(false);
            xListView.setPullLoadEnable(false);
            convertView = inflater.inflate(R.layout.home_me_my_oder_nothing, null);
            TextView noting_tv = (TextView) convertView.findViewById(R.id.home_me_nothing_tv);
            noting_tv.setText("没有喜欢");
        } else if (datas != null && position == datas.size() - 1 && datas.get(position) == null) {
            xListView.setEnabled(true);
            convertView = inflater.inflate(R.layout.load_more_layout, null);
            convertView.setFocusableInTouchMode(false);
            convertView.setFocusable(false);
        } else {
            xListView.setEnabled(true);
            FavoriteItem item = datas.get(position);
            ViewHolder holder = null;
            if (holder == null) {
                xListView.setPullLoadEnable(true);
                holder = new ViewHolder();
                convertView = inflater.inflate(R.layout.home_me_my_favorite_item, null);
                if (position==0){
                    convertView.setPadding(0,(int) context.getResources().getDimension(R.dimen.dimen_size_15_dp),0,0);
                }
                holder.type = (TextView) convertView.findViewById(R.id.my_favorite_item_type);
                holder.photo = (ImageView) convertView.findViewById(R.id.my_favorite_item_img);
                holder.title = (TextView) convertView.findViewById(R.id.my_favorite_item_title);
                holder.commentNum = (TextView) convertView.findViewById(R.id.my_favorite_item_information_comment_num);
                holder.praiseNum = (TextView) convertView.findViewById(R.id.my_favorite_item_route_collect_num);
                holder.author = (TextView) convertView.findViewById(R.id.my_favorite_item_article_author);
                holder.authorAvatar = (ImageView) convertView.findViewById(R.id.my_favorite_item_authoravatar);
                holder.price = (TextView) convertView.findViewById(R.id.my_favorite_item_goods_price);
                if (item != null) {
                    int targetType = item.getTargetType();
                    Resources res = context.getResources();
                    if (targetType == 1) {
                        //文章类型
                        holder.type.setText(res.getString(R.string.article));
                        holder.type.setBackgroundColor(res.getColor(R.color.color_4a90e2));
                        Author author = item.getAuthor();
                        if (author != null) {
                            holder.author.setText(author.getAuthorName());
                            DrawableUtils.displayImg(context, holder.authorAvatar, author.getAuthorAvatar());
                        }
                        holder.author.setVisibility(View.VISIBLE);
                        holder.authorAvatar.setVisibility(View.VISIBLE);
                        holder.praiseNum.setVisibility(View.GONE);
                        holder.commentNum.setVisibility(View.GONE);
                        holder.price.setVisibility(View.GONE);

                    } else if (targetType == 2) {
                        //产品类型
                        holder.type.setText(res.getString(R.string.goods));
                        holder.type.setBackgroundColor(res.getColor(R.color.color_ffaa2a));
                        holder.price.setText(CommonUtil.formartOrderPrice(item.getPrice()));
                        holder.author.setVisibility(View.GONE);
                        holder.authorAvatar.setVisibility(View.GONE);
                        holder.praiseNum.setVisibility(View.GONE);
                        holder.commentNum.setVisibility(View.GONE);
                        holder.price.setVisibility(View.VISIBLE);
                    } else if (targetType == 9) {
                        //路线类型
                        holder.type.setText(res.getString(R.string.route));
                        holder.type.setBackgroundColor(res.getColor(R.color.color_f07e3c));
                        holder.praiseNum.setText(String.valueOf(item.getPraiseNum()));
                        holder.author.setVisibility(View.GONE);
                        holder.authorAvatar.setVisibility(View.GONE);
                        holder.praiseNum.setVisibility(View.VISIBLE);
                        holder.commentNum.setVisibility(View.GONE);
                        holder.price.setVisibility(View.GONE);
                    } else if (targetType == 10) {
                        //专题类型
                        holder.type.setText(res.getString(R.string.special));
                        holder.type.setBackgroundColor(res.getColor(R.color.color_bd10e0));
                        holder.praiseNum.setText(String.valueOf(item.getPraiseNum()));
                        holder.author.setVisibility(View.GONE);
                        holder.authorAvatar.setVisibility(View.GONE);
                        holder.praiseNum.setVisibility(View.VISIBLE);
                        holder.commentNum.setVisibility(View.GONE);
                        holder.price.setVisibility(View.GONE);
                    } else if (targetType == 13) {
                        //资讯类型
                        holder.type.setText(res.getString(R.string.information));
                        holder.type.setBackgroundColor(res.getColor(R.color.color_bd10e0));
                        holder.commentNum.setText(String.valueOf(item.getCommentNum()));
                        holder.author.setVisibility(View.GONE);
                        holder.authorAvatar.setVisibility(View.GONE);
                        holder.praiseNum.setVisibility(View.GONE);
                        holder.commentNum.setVisibility(View.VISIBLE);
                        holder.price.setVisibility(View.GONE);
                    }
                    holder.photo.setTag(item.getPhoto());
                    String tag = (String) holder.photo.getTag();
                    if (tag != null && tag.equals(item.getPhoto())) {
                        DrawableUtils.displayImg(context, holder.photo, item.getPhoto());
                    }
//                    DrawableUtils.displayImg(context, holder.photo, item.getPhoto());
                    holder.title.setText(item.getTargetName());
                }
                convertView.setTag(R.id.targetId, item);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
        }
        return convertView;
    }

    class ViewHolder {
        //common
        TextView type;
        ImageView photo;
        TextView title;
        //资讯
        TextView commentNum;
        //路线
        TextView praiseNum;
        //文章
        TextView author;
        ImageView authorAvatar;
        //产品
        TextView price;
    }
}
