package com.lalocal.lalocal.view.viewholder;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.activity.HomeActivity;
import com.lalocal.lalocal.activity.ProductDetailsActivity;
import com.lalocal.lalocal.model.ProductDetailsResultBean;
import com.lalocal.lalocal.model.SpecialToH5Bean;
import com.lalocal.lalocal.util.CommonUtil;
import com.lalocal.lalocal.util.DrawableUtils;
import com.lalocal.lalocal.view.MyGridView;
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
    private MyGridView mGvCommodities;
    private FrameLayout mLayoutContainer;

    private static final int MAX_PRODUCT = 4;

    private boolean isEmpty;

    public ProductViewHolder(Context context, View itemView, boolean isEmpty) {
        super(itemView);
        this.mContext = context;
        this.isEmpty = isEmpty;

        mLayoutContainer = (FrameLayout) itemView.findViewById(R.id.commodity_container);
        mTitleView = (TextView) itemView.findViewById(R.id.tv_title);
        mSubTitleView = (TextView) itemView.findViewById(R.id.tv_subtitle);
        mLayoutMore = (RelativeLayout) itemView.findViewById(R.id.layout_more);
        mGvCommodities = (MyGridView) itemView.findViewById(R.id.gv_commodities);
    }

    public void initData(List<ProductDetailsResultBean> list, String title, String subtitle) {
        final List<ProductDetailsResultBean> commodityList = list;

        if (isEmpty) {
            mLayoutContainer.setVisibility(View.GONE);
            return;
        }

        mTitleView.setText(title);
        mSubTitleView.setText(subtitle);

        mGvCommodities.setFocusable(false);
        // 设置适配器
        mGvCommodities.setAdapter(new CommonAdapter<ProductDetailsResultBean>(mContext, commodityList, R.layout.home_recommend_product_gridview_item, MAX_PRODUCT) {

            @Override
            public void convert(CommonViewHolder holder, ProductDetailsResultBean bean) {
                // 设置商品图片
                SquareImageView imgComoddity = holder.getView(R.id.img_commodity);
                DrawableUtils.displayImg(mContext, imgComoddity, bean.photo, R.drawable.androidloading);

                // 设置商品价格
                String price = "￥ " + CommonUtil.formatNumWithComma(bean.price) + "起";
                holder.setText(R.id.tv_commodity_price, price);

                // 设置商品标题
                String title = bean.title;
                holder.setText(R.id.tv_commodity_title, title);

            }
        });

        mLayoutMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 跳转目的地界面
                ((HomeActivity) mContext).goToFragment(HomeActivity.FRAGMENT_DESTINATION);
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
    }
}
