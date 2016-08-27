package com.lalocal.lalocal.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.model.RouteDetail;
import com.lalocal.lalocal.util.CommonUtil;
import com.lalocal.lalocal.util.DrawableUtils;

import java.util.List;

/**
 * Created by xiaojw on 2016/7/28.
 */
public class DayRouteApater extends BaseRecyclerAdapter {

    private static final String DISATNCE_FORMART = "距离%1$s公里";

    Context context;
    List<RouteDetail.RouteDatesBean.RouteItemsBean> routeItems;

    public DayRouteApater(Context context, List<RouteDetail.RouteDatesBean.RouteItemsBean> routeItems) {
        this.context = context;
        this.routeItems = routeItems;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.day_route_item, parent, false);
        return new DayRouteHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (routeItems == null || routeItems.size() < 1) {
            return;
        }
        final RouteDetail.RouteDatesBean.RouteItemsBean routeItem = routeItems.get(position);
        if (routeItem != null) {
            DayRouteHolder routeHolder = (DayRouteHolder) holder;
            String productId = routeItem.getProduId();
            if (!TextUtils.isEmpty(productId)) {
                routeHolder.buyTag.setVisibility(View.VISIBLE);
            }
            DrawableUtils.displayImg(context, routeHolder.photo, routeItem.getPhoto(),-1);
            routeHolder.title.setText(routeItem.getTitle());
            if (position < routeItems.size() - 1) {
                double lat1 = routeItem.getLatitude();
                double lng1 = routeItem.getLongitude();
                RouteDetail.RouteDatesBean.RouteItemsBean nextRouteItem = routeItems.get(position + 1);
                double lat2 = nextRouteItem.getLatitude();
                double lng2 = nextRouteItem.getLongitude();
                routeHolder.distance.setText(String.format(DISATNCE_FORMART, CommonUtil.getDistance(lng1, lat1, lng2, lat2)));
            } else {
                if (routeHolder.distance.getVisibility() == View.VISIBLE) {
                    routeHolder.distance.setVisibility(View.INVISIBLE);
                }
                if (routeHolder.line.getVisibility() == View.VISIBLE) {
                    routeHolder.line.setVisibility(View.INVISIBLE);
                }

            }
            routeHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        v.setTag(routeItem);
                        listener.onItemClickListener(v, position);

                    }

                }
            });


        }


    }

    @Override
    public int getItemCount() {
        return routeItems != null && routeItems.size() > 0 ? routeItems.size() : 0;
    }

    class DayRouteHolder extends RecyclerView.ViewHolder {
        ImageView photo;
        TextView title;
        TextView distance;
        TextView line;
        TextView buyTag;

        public DayRouteHolder(View itemView) {
            super(itemView);
            photo = (ImageView) itemView.findViewById(R.id.day_route_item_img);
            title = (TextView) itemView.findViewById(R.id.day_route_title);
            distance = (TextView) itemView.findViewById(R.id.day_route_distance);
            line = (TextView) itemView.findViewById(R.id.day_route_line);
            buyTag = (TextView) itemView.findViewById(R.id.day_route_buy_tag);


        }
    }

}
