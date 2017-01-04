package com.lalocal.lalocal.view.viewholder.find;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.lalocal.lalocal.R;
import com.lalocal.lalocal.activity.DestinationActivity;
import com.lalocal.lalocal.activity.HomeActivity;
import com.lalocal.lalocal.activity.ProductDetailsActivity;
import com.lalocal.lalocal.model.ProductDetailsResultBean;
import com.lalocal.lalocal.model.SpecialToH5Bean;
import com.lalocal.lalocal.util.AppLog;
import com.lalocal.lalocal.util.CommonUtil;
import com.lalocal.lalocal.util.DrawableUtils;
import com.lalocal.lalocal.util.QiniuUtils;
import com.lalocal.lalocal.view.SquareImageView;
import com.lalocal.lalocal.view.adapter.CommonAdapter;
import com.lalocal.lalocal.view.adapter.CommonViewHolder;

import java.util.List;

/**
 * Created by wangjie on 2016/10/25.
 */
public class ProductViewHolder extends RecyclerView.ViewHolder {

    private Context mContext;

    private TextView mTitleView;
    private TextView mSubTitleView;
    private RelativeLayout mLayoutMore;
    private GridView mGvCommodities;
//        private GridLayout mGvCommodities;
    private FrameLayout mLayoutContainer;

    private static final int MAX_PRODUCT = 4;

    public ProductViewHolder(Context context, View itemView) {
        super(itemView);
        this.mContext = context;

        mLayoutContainer = (FrameLayout) itemView.findViewById(R.id.commodity_container);
        mTitleView = (TextView) itemView.findViewById(R.id.tv_title);
        mSubTitleView = (TextView) itemView.findViewById(R.id.tv_subtitle);
        mLayoutMore = (RelativeLayout) itemView.findViewById(R.id.layout_more);
        mGvCommodities = (GridView) itemView.findViewById(R.id.gv_commodities);
//        mGvCommodities = (GridLayout) itemView.findViewById(R.id.gv_commodities);
    }

    public void initData(List<ProductDetailsResultBean> list, String title, String subtitle) {
        AppLog.i("prd", "the product list is " + list.size());
        if (list == null || list.size() == 0) {
            mLayoutContainer.setVisibility(View.GONE);
            return;
        }

        final List<ProductDetailsResultBean> commodityList = list;

        mTitleView.setText(title);
        mSubTitleView.setText(subtitle);

        // 设置适配器
        mGvCommodities.setFocusable(false);

//        int size = Math.max(4, list.size());
//        for (int i = 0; i < size; i++) {
//            ViewHolder holder = null;
//            LinearLayout container = (LinearLayout) LayoutInflater.from(mContext)
//                    .inflate(R.layout.home_recommend_product_gridview_item, null);
//            if (mGvCommodities.getTag() == null) {
//                holder = new ViewHolder();
//                holder.imgCommodity = (ImageView) container.findViewById(R.id.img_commodity);
//                holder.tvCommodityPrice = (TextView) container.findViewById(R.id.tv_commodity_price);
//                holder.tvCommodityTitle = (TextView) container.findViewById(R.id.tv_commodity_title);
//
//                mGvCommodities.setTag(holder);
//            } else {
//                holder = (ViewHolder) mGvCommodities.getTag();
//            }
//
//            ProductDetailsResultBean bean = list.get(i);
//            // 获取图片url
//            String photoUrl = bean.photo;
//            int width = holder.imgCommodity.getWidth();
//            int height = holder.imgCommodity.getHeight();
//            // 七牛云处理图片大小
//            photoUrl = QiniuUtils.centerCrop(photoUrl, width, height);
//            // 获取商品价格
//            double price = bean.price;
//            // 获取商品标题
//            String productTitle = bean.title;
//
//            // 加载图片
//            DrawableUtils.displayImg(mContext, holder.imgCommodity, photoUrl);
//
//            // 设置商品价格
//            String priceShow = "￥ " + CommonUtil.formatNumWithComma(price) + "起";
//            holder.tvCommodityPrice.setText(priceShow);
//            // 设置商品标题
//            holder.tvCommodityTitle.setText(productTitle);
//
//            AppLog.i("prd", "addView");
//            mGvCommodities.addView(container);
//        }


        mGvCommodities.setAdapter(new CommonAdapter<ProductDetailsResultBean>(mContext, commodityList, R.layout.home_recommend_product_gridview_item, MAX_PRODUCT) {

            @Override
            public void convert(CommonViewHolder holder, ProductDetailsResultBean bean) {
                // 获取图片url
                String photoUrl = bean.photo;
                // 获取图片控件宽高
                SquareImageView imgComoddity = holder.getView(R.id.img_commodity);
                int width = imgComoddity.getWidth();
                int height = imgComoddity.getHeight();
                // 七牛云处理图片大小
                photoUrl = QiniuUtils.centerCrop(photoUrl, width, height);
                // 获取商品价格
                double price = bean.price;
                // 获取商品标题
                String title = bean.title;

                // 使用Glide加载商品图片，注意：如果存在自定义计算高度的图片，Glide不是很合适
//                Glide.with(mContext)
//                        .load(photoUrl)
//                        .placeholder(R.drawable.androidloading)
//                        .centerCrop()
//                        .crossFade()
//                        // 只缓存原图，其他参数：DiskCacheStrategy.NONE不缓存到磁盘，DiskCacheStrategy.RESULT缓存处理后的图片，DiskCacheStrategy.ALL两者都缓存
//                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
//                        .into(imgComoddity);

                // 加载图片
                DrawableUtils.displayImg(mContext, imgComoddity, photoUrl);

                // 设置商品价格
                String priceShow = "￥ " + CommonUtil.formatNumWithComma(price) + "起";
                holder.setText(R.id.tv_commodity_price, priceShow);
                // 设置商品标题
                holder.setText(R.id.tv_commodity_title, title);

            }
        });

        mGvCommodities.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 跳转到商品详情界面
                ProductDetailsResultBean productBean = commodityList.get(position);
                int targetId = productBean.id;
                SpecialToH5Bean specialToH5Bean = new SpecialToH5Bean();
                specialToH5Bean.setTargetId(targetId);

                Intent intent = new Intent(mContext, ProductDetailsActivity.class);
                intent.putExtra("productdetails", specialToH5Bean);
                mContext.startActivity(intent);
            }
        });

        mLayoutMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 跳转目的地界面
                Intent intent = new Intent(mContext, DestinationActivity.class);
                mContext.startActivity(intent);
            }
        });
    }

    /**
     * 视图容器
     */
    private class ViewHolder {
        ImageView imgCommodity;
        TextView tvCommodityPrice;
        TextView tvCommodityTitle;
    }
}