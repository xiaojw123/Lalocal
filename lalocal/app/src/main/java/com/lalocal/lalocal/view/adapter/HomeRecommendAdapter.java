package com.lalocal.lalocal.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.lalocal.lalocal.R;
import com.lalocal.lalocal.activity.ArticleActivity;
import com.lalocal.lalocal.activity.CarouselFigureActivity;
import com.lalocal.lalocal.activity.HomeActivity;
import com.lalocal.lalocal.activity.ProductDetailsActivity;
import com.lalocal.lalocal.activity.SpecialDetailsActivity;
import com.lalocal.lalocal.activity.ThemeActivity;
import com.lalocal.lalocal.live.entertainment.activity.AudienceActivity;
import com.lalocal.lalocal.live.im.ui.widget.CircleImageView;
import com.lalocal.lalocal.model.ArticleDetailsResultBean;
import com.lalocal.lalocal.model.LiveRowsBean;
import com.lalocal.lalocal.model.LiveUserBean;
import com.lalocal.lalocal.model.ProductDetailsResultBean;
import com.lalocal.lalocal.model.RecommendAdResultBean;
import com.lalocal.lalocal.model.RecommendCommodityDataResp;
import com.lalocal.lalocal.model.RecommendListBean;
import com.lalocal.lalocal.model.RecommendRowsBean;
import com.lalocal.lalocal.model.SpecialAuthorBean;
import com.lalocal.lalocal.model.SpecialShareVOBean;
import com.lalocal.lalocal.model.SpecialToH5Bean;
import com.lalocal.lalocal.util.AppLog;
import com.lalocal.lalocal.util.DensityUtil;
import com.lalocal.lalocal.util.DrawableUtils;
import com.lalocal.lalocal.util.ScaleAlphaPageTransformer;
import com.lalocal.lalocal.view.DisallowParentTouchSliderLayout;
import com.lalocal.lalocal.view.DisallowParentTouchViewPager;
import com.lalocal.lalocal.view.MyGridView;
import com.lalocal.lalocal.view.MyPtrClassicFrameLayout;
import com.lalocal.lalocal.view.ScaleImageView;
import com.lalocal.lalocal.view.SquareImageView;
import com.lalocal.lalocal.view.WrapContentListView;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 项目名称：lalocal
 * 模块名称：
 * 包名：com.lalocal.lalocal.view.adapter
 * 类功能：
 * 创建人：wangjie
 * 创建时间：2016 16/9/9 下午2:54
 * 联系邮箱: wjnovember@icloud.com
 */
public class HomeRecommendAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;

    private List<RecommendAdResultBean> mAdList;
    private RecommendListBean mRecommendListBean;
    private List<ArticleDetailsResultBean> mArticleList;

    private static final int MAX_HOT_LIVE = 10;
    private static final int MAX_PRODUCT = 4;
    private static final int MAX_THEME = 10;

    private RecyclerView mRvRecommendList;

    private static final int TEST_AMOUNT = 5;

    private static final String TAG = "HomeRecommendAdapter";

    // 广告
    private static final int ADVERTISEMENT = 0;
    // 分类
    private static final int CATEGORY = 1;
    // 热门直播
    private static final int LIVE = 2;
    // 迷人又可爱的商品们
    private static final int PRODUCT = 3;
    // 精彩专题
    private static final int THEME = 4;
    // 旅行笔记
    private static final int ARTICLE = 5;

    // 首页推荐列表子项数量
    private static final int AMOUNT_HOME_RECOMMEND = 6;

    private boolean adEmpty;
    private boolean liveEmpty;
    private boolean productEmpty;
    private boolean themeEmpty;
    private boolean articleEmpty;

    private MyPtrClassicFrameLayout mPtrLayout;

    public HomeRecommendAdapter(Context context) {
        this.mContext = context;
    }

    public HomeRecommendAdapter(Context context, List<RecommendAdResultBean> adList, RecommendListBean recommendListBean,
                                List<ArticleDetailsResultBean> articleList, MyPtrClassicFrameLayout ptrLayout) {
        this.mContext = context;
        this.mAdList = adList;
        this.mRecommendListBean = recommendListBean;
        this.mArticleList = articleList;
        this.mPtrLayout = ptrLayout;

        if (adList == null || adList.size() == 0) {
            AppLog.i("hehe", "ad null");
            adEmpty = true;
        }

        if (recommendListBean == null) {
            AppLog.e("hehe", "recommendListBean is null");
            liveEmpty = true;
            productEmpty = true;
            themeEmpty = true;
        } else {
            if (recommendListBean.getChannelList() == null || recommendListBean.getChannelList().size() == 0) {
                AppLog.e("hehe", "channel list null");
                liveEmpty = true;
            }
            if (recommendListBean.getProduList() == null || recommendListBean.getProduList().size() == 0) {
                AppLog.e("hehe", "product list null");
                productEmpty = true;
            }
            if (recommendListBean.getThemeList() == null || recommendListBean.getThemeList().size() == 0) {
                AppLog.e("hehe", "theme list null");
                themeEmpty = true;
            }
        }

        if (articleList == null || articleList.size() == 0) {
            AppLog.i("hehe", "article list null");
            articleEmpty = true;
        }
    }

    public void setAdData(List<RecommendAdResultBean> adList) {
        this.mAdList = adList;
        if (adList == null || adList.size() == 0) {
            AppLog.i("hehe", "ad null");
            adEmpty = true;
        } else {
            adEmpty = false;
        }
        this.notifyDataSetChanged();
    }

    public void setListData(RecommendListBean recommendListBean) {
        this.mRecommendListBean = recommendListBean;
        if (recommendListBean == null) {
            AppLog.e("hehe", "recommendListBean is null");
            liveEmpty = true;
            productEmpty = true;
            themeEmpty = true;
        } else {
            if (recommendListBean.getChannelList() == null || recommendListBean.getChannelList().size() == 0) {
                AppLog.e("hehe", "channel list null");
                liveEmpty = true;
            } else {
                liveEmpty = false;
            }
            if (recommendListBean.getProduList() == null || recommendListBean.getProduList().size() == 0) {
                AppLog.e("hehe", "product list null");
                productEmpty = true;
            } else {
                productEmpty = false;
            }
            if (recommendListBean.getThemeList() == null || recommendListBean.getThemeList().size() == 0) {
                AppLog.e("hehe", "theme list null");
                themeEmpty = true;
            } else {
                themeEmpty = false;
            }
        }
        this.notifyDataSetChanged();
    }

    public void setArticleData(List<ArticleDetailsResultBean> articleList) {
        this.mArticleList = articleList;
        if (articleList == null || articleList.size() == 0) {
            AppLog.i("hehe", "article list null");
            articleEmpty = true;
        } else {
            articleEmpty = false;
        }
        this.notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder = null;
        this.mRvRecommendList = (RecyclerView) parent;
        View view = null;
        switch (viewType) {
            case ADVERTISEMENT: // 广告
                view = LayoutInflater.from(mContext).inflate(R.layout.home_recommend_advertisement_item, parent, false);
                holder = new ADViewHolder(mContext, view);
                break;
            case CATEGORY: // 分类
                view = LayoutInflater.from(mContext).inflate(R.layout.home_recommend_category_item, parent, false);
                holder = new CategoryViewHolder(mContext, view);
                break;
            case LIVE: // 热门直播
                view = View.inflate(mContext, R.layout.home_recommend_hotlive_item, null);
                holder = new LiveViewHolder(mContext, view);
                break;
            case PRODUCT: // 迷人又可爱的商品们
                view = View.inflate(mContext, R.layout.home_recommend_product_item, null);
                holder = new ProductViewHolder(mContext, view);
                break;
            case THEME: // 精彩专题
                view = View.inflate(mContext, R.layout.home_recommend_theme_item, null);
                holder = new ThemeViewHolder(mContext, view);
                break;
            case ARTICLE: // 旅行笔记
                view = View.inflate(mContext, R.layout.home_recommend_article_item, null);
                holder = new ArticleViewHolder(mContext, view);
                break;
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (position) {
            case ADVERTISEMENT:
                ((ADViewHolder) holder).initData(mAdList);
                break;
            case LIVE:
                if (liveEmpty) {
                    ((LiveViewHolder) holder).initData(null, null, null);
                    return;
                }
                List<LiveRowsBean> liveBeanList = mRecommendListBean.getChannelList();
                String liveTitle = mRecommendListBean.getChannleCN();
                String liveSubTitle = mRecommendListBean.getChannleEN();
                ((LiveViewHolder) holder).initData(liveBeanList, liveTitle, liveSubTitle);
                break;
            case PRODUCT:
                if (productEmpty) {
                    ((ProductViewHolder) holder).initData(null, null, null);
                    return;
                }
                List<ProductDetailsResultBean> productBeanList = mRecommendListBean.getProduList();
                String comTitle = mRecommendListBean.getProduCN();
                String comSubTitle = mRecommendListBean.getProduEN();
                ((ProductViewHolder) holder).initData(productBeanList, comTitle, comSubTitle);
                break;
            case THEME:
                if (themeEmpty) {
                    ((ThemeViewHolder) holder).initData(null, null, null);
                    return;
                }
                List<RecommendRowsBean> themeBeanList = mRecommendListBean.getThemeList();
                String themeTitle = mRecommendListBean.getThemeCN();
                String themeSubTitle = mRecommendListBean.getThemeEN();
                ((ThemeViewHolder) holder).initData(themeBeanList, themeTitle, themeSubTitle);
                break;
            case ARTICLE:
                if (articleEmpty) {
                    ((ArticleViewHolder) holder).initData(null, null, null);
                    return;
                }
                String diaryTitle = mRecommendListBean.getTravelCN();
                String diarySubTitle = mRecommendListBean.getTravelEN();
                ((ArticleViewHolder) holder).initData(mArticleList, diaryTitle, diarySubTitle);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return AMOUNT_HOME_RECOMMEND;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    /**
     * 广告
     */
    class ADViewHolder extends RecyclerView.ViewHolder {

        DisallowParentTouchSliderLayout sliderLayout;
        Context context;
        List<RecommendAdResultBean> adResultList;

        public ADViewHolder(Context context, View itemView) {
            super(itemView);

            this.context = context;
            sliderLayout = (DisallowParentTouchSliderLayout) itemView.findViewById(R.id.ad_slider);
            // 传入父容器
            sliderLayout.setNestParent(mPtrLayout);
        }

        /**
         * 初始化数据
         *
         * @param ads
         */
        public void initData(final List<RecommendAdResultBean> ads) {
            // 如果不存在数据，则不显示相应控件
            if (adEmpty) {
                sliderLayout.setVisibility(View.GONE);
                return;
            }
            adResultList = ads;

            sliderLayout.removeAllSliders();
            for (int i = 0; i < adResultList.size(); i++) {
                TextSliderView textSliderView = new TextSliderView(context);
                RecommendAdResultBean ad = adResultList.get(i);
                textSliderView.image(ad.photo);
                textSliderView.setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                    @Override
                    public void onSliderClick(BaseSliderView slider) {
                        int position = sliderLayout.getCurrentPosition();
                        // 点击跳转
                        RecommendAdResultBean recommendAdResultBean = ads.get(position);
                        String url = recommendAdResultBean.url;
                        if (TextUtils.isEmpty(url)) {
                            return;
                        }
                        Intent intent = new Intent(mContext, CarouselFigureActivity.class);
                        intent.putExtra("carousefigure", recommendAdResultBean);
                        mContext.startActivity(intent);
                    }
                });
                sliderLayout.addSlider(textSliderView);
            }
        }

        /**
         * 移除所有的TextSliderView
         *
         * @param sliderLayout
         */
        private void removeSlider(SliderLayout sliderLayout) {
            int size = sliderLayout.getChildCount();
            for (int i = size - 1; i >= 0; i++) {
                sliderLayout.removeSliderAt(i);
            }
        }
    }

    /**
     * 热门直播
     */
    class LiveViewHolder extends RecyclerView.ViewHolder {

        Context context;
        TextView titleView;
        TextView subTitleView;
        DisallowParentTouchViewPager vpHotLives;
        RelativeLayout layoutMore;
        LinearLayout dotContainer;
        FrameLayout liveContainer;
        ViewGroup contentView;
        int selected = 0;
        List<Button> dotBtns = new ArrayList<>();

        public LiveViewHolder(Context context, View itemView) {
            super(itemView);
            this.context = context;
            this.contentView = (ViewGroup) itemView;

            liveContainer = (FrameLayout) itemView.findViewById(R.id.live_container);
            titleView = (TextView) itemView.findViewById(R.id.tv_title);
            subTitleView = (TextView) itemView.findViewById(R.id.tv_subtitle);
            layoutMore = (RelativeLayout) itemView.findViewById(R.id.layout_more);
            vpHotLives = (DisallowParentTouchViewPager) itemView.findViewById(R.id.vp_hot_lives);
            dotContainer = (LinearLayout) itemView.findViewById(R.id.dot_container);

            // 传入父容器
            vpHotLives.setNestParent(mPtrLayout);
            // 设置一个屏幕最多显示视频的个数
            vpHotLives.setOffscreenPageLimit(4);
            // 设置热播视频间距
            vpHotLives.setPageMargin(mContext.getResources().getDimensionPixelSize(
                    R.dimen.home_recommend_viewpager_page_margin));
        }

        /**
         * 初始化数据
         */
        public void initData(List<LiveRowsBean> list, String title, String subtitle) {
            if (!TextUtils.isEmpty(title)) {
                titleView.setText(title);
            }
            if (!TextUtils.isEmpty(subtitle)) {
                subTitleView.setText(subtitle);
            }

            final List<LiveRowsBean> hotLiveList = list;
            int size = (list == null ? 0 : list.size());

            if (liveEmpty) {
                liveContainer.setVisibility(View.GONE);
                return;
            }

            // 改变大小透明度的工具类
            ScaleAlphaPageTransformer mScaleAlphaPageTransformer = new ScaleAlphaPageTransformer();
            // TODO: 获取网络数据后，传递数据
            HotLiveAdapter hotLiveAdapter = new HotLiveAdapter(context, hotLiveList);
            // 配置适配器
            vpHotLives.setAdapter(hotLiveAdapter);
            // 设置值改变透明度，不改变大小
            mScaleAlphaPageTransformer.setType(true, false);
            // 对ViewPager进行设置改变透明度
            vpHotLives.setPageTransformer(true, mScaleAlphaPageTransformer);
            // 如果当前子项个数大于等于3，则将ViewPager定位到第2项
            if (hotLiveAdapter.getCount() >= 3) {
                // 将当前ViewPager设置到第2项
                vpHotLives.setCurrentItem(1);
            }

            selected = vpHotLives.getCurrentItem();
            AppLog.i("slidder", "selected is " + selected);

            // 初始化小圆点
            dotBtns = initDot(context, vpHotLives, dotContainer, size, selected);

            // ViewPager添加滑动事件
            vpHotLives.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    selectDotBtn(dotBtns, position);
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });

            layoutMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 跳转直播界面
                    ((HomeActivity) mContext).goToFragment(HomeActivity.FRAGMENT_NEWS);
                }
            });
        }

        class HotLiveAdapter extends PagerAdapter {
            private Context context;
            private List<LiveRowsBean> hotLiveList;

            public HotLiveAdapter(Context context, List<LiveRowsBean> hotLiveList) {
                this.context = context;
                this.hotLiveList = hotLiveList;
            }

            @Override
            public int getCount() {
                return Math.min(hotLiveList == null ? 0 : hotLiveList.size(), MAX_HOT_LIVE);
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                View view = LayoutInflater.from(context).inflate(R.layout.home_recommend_hotlive_viewpager_item, null);
                final LiveRowsBean liveRowsBean = hotLiveList.get(position);
                SubHotLiveViewHolder hotLiveViewHolder = new SubHotLiveViewHolder();
                hotLiveViewHolder.container = (FrameLayout) view.findViewById(R.id.container);
                hotLiveViewHolder.imgLivePic = (ScaleImageView) view.findViewById(R.id.img_live_pic);
                hotLiveViewHolder.tvLivePeopleAmount = (TextView) view.findViewById(R.id.tv_live_people);
                hotLiveViewHolder.tvLiveTitle = (TextView) view.findViewById(R.id.tv_live_title);
                hotLiveViewHolder.imgLiveAvatar = (CircleImageView) view.findViewById(R.id.img_live_avatar);

                // 设置直播用户头像
                LiveUserBean user = liveRowsBean.getUser();
            // 设置播放图片
                String photo = user.getAvatarOrigin();
                if (!TextUtils.isEmpty(photo)) {
                    DrawableUtils.displayImg(mContext, hotLiveViewHolder.imgLivePic, photo);
                }

                // 设置播放标题
                String liveTitle = liveRowsBean.getTitle();
                if (!TextUtils.isEmpty(liveTitle)) {
                    hotLiveViewHolder.tvLiveTitle
                            .setText(liveTitle);
                }

                // 设置在线用户人数
                int onlineUser = liveRowsBean.getOnlineUser();
                // 人数以“,”将千分位隔开
                hotLiveViewHolder.tvLivePeopleAmount.setText(formatNum(onlineUser));

                // 获取头像uri
                final String avatar = user.getAvatar();
                // 设置头像
                DrawableUtils.displayImg(mContext, hotLiveViewHolder.imgLiveAvatar, avatar, R.drawable.androidloading);

                // 获取视频播放相关数据
                final int roomId = liveRowsBean.getRoomId();
                final String pullUrl = liveRowsBean.getPullUrl();
                final String nickName = user.getNickName();
                final int userId = user.getId();
                final SpecialShareVOBean shareVO = liveRowsBean.getShareVO();
                final int type = liveRowsBean.getType();
                Object annoucement = liveRowsBean.getAnnoucement();
                String ann = null;
                if (annoucement != null) {
                    ann = annoucement.toString();
                } else {
                    ann = "这是公告哈";
                }
                final String finalAnn = ann;
                final int channelId = liveRowsBean.getId();

                final String finalAnn1 = ann;
                hotLiveViewHolder.container.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // 跳转播放界面
                        AudienceActivity.start(mContext, liveRowsBean, finalAnn1);
                    }
                });
                container.addView(view);
                return view;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((View) object);
            }


        }

        class SubHotLiveViewHolder {
            FrameLayout container;
            ScaleImageView imgLivePic;
            TextView tvLiveTitle;
            TextView tvLivePeopleAmount;
            CircleImageView imgLiveAvatar;
//            ImageView btnPlay;
        }
    }


    /**
     * 分类
     */
    class CategoryViewHolder extends RecyclerView.ViewHolder {

        Context context;

        LinearLayout layoutLive;
        LinearLayout layoutTheme;
        LinearLayout layoutArticle;
        LinearLayout layoutShop;

        /**
         * 分类
         *
         * @param itemView
         */
        public CategoryViewHolder(final Context context, View itemView) {
            super(itemView);

            this.context = context;
            // 关联控件
            this.layoutLive = (LinearLayout) itemView.findViewById(R.id.layout_live);
            this.layoutTheme = (LinearLayout) itemView.findViewById(R.id.layout_theme);
            this.layoutArticle = (LinearLayout) itemView.findViewById(R.id.layout_article);
            this.layoutShop = (LinearLayout) itemView.findViewById(R.id.layout_shop);

            // 监听事件回调
            layoutLive.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 跳转直播界面
                    ((HomeActivity) mContext).goToFragment(HomeActivity.FRAGMENT_NEWS);
                }
            });

            layoutTheme.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 跳转专题界面
                    mContext.startActivity(new Intent(mContext, ThemeActivity.class));
                }
            });

            layoutArticle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((LinearLayoutManager) mRvRecommendList.getLayoutManager()).scrollToPositionWithOffset(ARTICLE, 0);
                }
            });

            layoutShop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 跳转目的地界面
                    ((HomeActivity) mContext).goToFragment(HomeActivity.FRAGMENT_DESTINATION);
                }
            });
        }
    }

    /**
     * 可爱又迷人的商品们
     */
    class ProductViewHolder extends RecyclerView.ViewHolder {

        Context context;

        TextView titleView;
        TextView subTitleView;
        RelativeLayout layoutMore;
        MyGridView gvCommodities;
        FrameLayout layoutContainer;

        RecommendCommodityDataResp commodities;

        public ProductViewHolder(Context context, View itemView) {
            super(itemView);
            context = context;

            layoutContainer = (FrameLayout) itemView.findViewById(R.id.commodity_container);
            titleView = (TextView) itemView.findViewById(R.id.tv_title);
            subTitleView = (TextView) itemView.findViewById(R.id.tv_subtitle);
            layoutMore = (RelativeLayout) itemView.findViewById(R.id.layout_more);
            gvCommodities = (MyGridView) itemView.findViewById(R.id.gv_commodities);
        }

        public void initData(List<ProductDetailsResultBean> list, String title, String subtitle) {
            final List<ProductDetailsResultBean> commodityList = list;

            if (productEmpty) {
                layoutContainer.setVisibility(View.GONE);
                return;
            }

            titleView.setText(title);
            subTitleView.setText(subtitle);

            // 设置适配器
            gvCommodities.setAdapter(new CommonAdapter<ProductDetailsResultBean>(mContext, commodityList, R.layout.home_recommend_product_gridview_item, MAX_PRODUCT) {

                @Override
                public void convert(CommonViewHolder holder, ProductDetailsResultBean bean) {
                    // 设置商品图片
                    SquareImageView imgComoddity = holder.getView(R.id.img_commodity);
                    DrawableUtils.displayImg(mContext, imgComoddity, bean.photo, R.drawable.androidloading);

                    // 设置商品价格
                    String price = "￥ " + formatNum(bean.price) + "起";
                    holder.setText(R.id.tv_commodity_price, price);

                    // 设置商品标题
                    String title = bean.title;
                    holder.setText(R.id.tv_commodity_title, title);

                }
            });

            layoutMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 跳转目的地界面
                    ((HomeActivity) mContext).goToFragment(HomeActivity.FRAGMENT_DESTINATION);
                }
            });

            gvCommodities.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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


    /**
     * 精彩专题
     */
    class ThemeViewHolder extends RecyclerView.ViewHolder {

        Context context;

        int size = -1;
        int selected = -1;

        TextView titleView;
        TextView subtitleView;
        FrameLayout layoutContainer;
        RelativeLayout layoutMore;

        DisallowParentTouchViewPager vpTheme;
        LinearLayout dotContainer;

        List<Button> dotBtns = new ArrayList<>();

        public ThemeViewHolder(Context context, View itemView) {
            super(itemView);
            this.context = context;

            layoutContainer = (FrameLayout) itemView.findViewById(R.id.theme_container);
            titleView = (TextView) itemView.findViewById(R.id.tv_title);
            subtitleView = (TextView) itemView.findViewById(R.id.tv_subtitle);
            layoutMore = (RelativeLayout) itemView.findViewById(R.id.layout_more);
            vpTheme = (DisallowParentTouchViewPager) itemView.findViewById(R.id.vp_theme);
            dotContainer = (LinearLayout) itemView.findViewById(R.id.dot_container);

            // 传入父容器
            vpTheme.setNestParent(mPtrLayout);
            // 设置一个屏幕最多显示视频的个数
            vpTheme.setOffscreenPageLimit(4);
            // 设置热播视频间距
            vpTheme.setPageMargin(mContext.getResources().getDimensionPixelSize(
                    R.dimen.home_recommend_viewpager_page_margin));
        }

        /**
         * 初始化数据
         *
         * @param list
         * @param title
         * @param subtitle
         */
        public void initData(List<RecommendRowsBean> list, String title, String subtitle) {
            List<RecommendRowsBean> recommendSpecialList = list;

            if (themeEmpty) {
                layoutContainer.setVisibility(View.GONE);
                return;
            }

            titleView.setText(title);
            subtitleView.setText(subtitle);

            // 改变大小透明度的工具类
            ScaleAlphaPageTransformer mScaleAlphaPageTransformer = new ScaleAlphaPageTransformer();
            // 初始化适配器
            final ThemeAdapter ThemeAdapter = new ThemeAdapter(context, recommendSpecialList);
            // 配置适配器
            vpTheme.setAdapter(ThemeAdapter);
            // 设置值改变透明度，不改变大小
            mScaleAlphaPageTransformer.setType(true, false);
            // 对ViewPager进行设置改变透明度
            vpTheme.setPageTransformer(true, mScaleAlphaPageTransformer);
            // 如果当前子项个数大于等于3，则将ViewPager定位到第2项
            if (ThemeAdapter.getCount() >= 3) {
                // 将当前ViewPager设置到第2项
                vpTheme.setCurrentItem(1);
            }

            size = list.size();
            selected = vpTheme.getCurrentItem();

            // 初始化小圆点
            dotBtns = initDot(context, vpTheme, dotContainer, size, selected);


            vpTheme.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    selectDotBtn(dotBtns, position);
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });

            layoutMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 跳转专题界面
                    mContext.startActivity(new Intent(mContext, ThemeActivity.class));
                }
            });
        }

        private class ThemeAdapter extends PagerAdapter {

            private Context context;
            private List<RecommendRowsBean> themeList;

            ThemeAdapter(Context context, List<RecommendRowsBean> themeList) {
                this.context = context;
                this.themeList = themeList;
            }

            @Override
            public int getCount() {
                return Math.min(themeList == null ? 0 : themeList.size(), MAX_THEME);
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                View view = LayoutInflater.from(context).inflate(
                        R.layout.home_recommend_theme_viewpager_item, null);
                RecommendRowsBean bean = themeList.get(position);
                SubThemeViewHolder holder = new SubThemeViewHolder();
                holder.imgTheme = (ImageView) view.findViewById(R.id.img_theme);
                holder.tvSpecialName = (TextView) view.findViewById(R.id.tv_theme_name);
                holder.tvSpecialSubTitle = (TextView) view.findViewById(R.id.tv_theme_sub_title);
                holder.tvReadQuantity = (TextView) view.findViewById(R.id.tv_read_quantity);
                holder.tvSaveQuantity = (TextView) view.findViewById(R.id.tv_save_quantity);

                // 设置专题图片
                DrawableUtils.displayRadiusImg(mContext, holder.imgTheme, bean.getPhoto(),
                        DensityUtil.dip2px(mContext,3),R.drawable.androidloading);

                // 设置名字
                String name = bean.getName();
                holder.tvSpecialName.setText(name);

                // 设置副标题
                String subtitle = bean.getSubTitle();
                holder.tvSpecialSubTitle.setText(subtitle);

                // 设置阅读人数
                String readQuantity = formatNum(bean.getReadNum());
                holder.tvReadQuantity.setText(readQuantity);

                // 设置保存人数
                String saveQuantity = formatNum(bean.getPraiseNum());
                holder.tvSaveQuantity.setText(saveQuantity);

                container.addView(view);

                final String rowId = String.valueOf(bean.getId());
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, SpecialDetailsActivity.class);
                        intent.putExtra("rowId", rowId);
                        mContext.startActivity(intent);
                    }
                });
                return view;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((View) object);
            }
        }

        private class SubThemeViewHolder {
            ImageView imgTheme;
            TextView tvSpecialName;
            TextView tvSpecialSubTitle;
            TextView tvReadQuantity;
            TextView tvSaveQuantity;
        }
    }

    /**
     * 旅形笔记
     */
    class ArticleViewHolder extends RecyclerView.ViewHolder {

        Context context;

        TextView tvTitle;
        TextView tvSubTitle;
        FrameLayout layoutContainer;
        WrapContentListView lvArticle;

        public ArticleViewHolder(Context context, View itemView) {
            super(itemView);
            this.context = context;

            layoutContainer = (FrameLayout) itemView.findViewById(R.id.article_container);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            tvSubTitle = (TextView) itemView.findViewById(R.id.tv_subtitle);
            lvArticle = (WrapContentListView) itemView.findViewById(R.id.lv_article);
        }

        public void initData(List<ArticleDetailsResultBean> list, String title, String subtitle) {
            final List<ArticleDetailsResultBean> articleList = list;

            int size = list == null ? 0 : list.size();

            if (size == 0) {
                layoutContainer.setVisibility(View.GONE);
                return;
            }

            tvTitle.setText(title);
            tvSubTitle.setText(subtitle);

            lvArticle.setAdapter(new CommonAdapter<ArticleDetailsResultBean>(context, articleList, R.layout.home_recommend_article_list_item) {
                @Override
                public void convert(CommonViewHolder holder, ArticleDetailsResultBean bean) {
                    // 设置图片
                    ImageView imgArticle = holder.getView(R.id.img_article);
                    DrawableUtils.displayImg(context, imgArticle, bean.getPhoto());

                    // 设置标题
                    holder.setText(R.id.tv_article_title, bean.getTitle());

                    // 设置浏览人数
                    String readNum = formatNum(bean.getReadNum());
                    holder.setText(R.id.tv_read_num, readNum);

                    // 设置点赞人数
                    String praiseNum = formatNum(bean.getPraiseNum());
                    holder.setText(R.id.tv_praise_num, praiseNum);

                    // 设置描述内容
                    String description = bean.getDescription();
                    holder.setText(R.id.tv_description, description);

                    // 设置作者名字
                    SpecialAuthorBean author = bean.getAuthorVO();
                    String authorName = author.authorName;
                    holder.setText(R.id.tv_author_name, authorName);
                }
            });

            lvArticle.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(mContext, ArticleActivity.class);
                    String targetId = String.valueOf(articleList.get(position).getId());
                    intent.putExtra("targetID", targetId);
                    mContext.startActivity(intent);
                }
            });
        }
    }

    /**
     * 初始化小圆点
     *
     * @param context
     * @param viewPager
     * @param dotContainer
     * @param size
     * @param selected
     * @return
     */
    private List<Button> initDot(Context context, final ViewPager viewPager, LinearLayout dotContainer, int size, int selected) {
        final List<Button> dotBtns = new ArrayList<>();
        if (size > 0 && selected >= 0 && selected < size) {
            // 移除所有视图
            ((ViewGroup) dotContainer).removeAllViews();
            for (int i = 0; i < size; i++) {
                // 新建一个按钮
                Button btn = new Button(context);
                // 点的大小
                int dipSize = (int) context.getResources().getDimension(R.dimen.dot_size);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(dipSize, dipSize);
                int marginHorizontal = DensityUtil.dip2px(context, 4);
                int marginVertical = DensityUtil.dip2px(context, 15);
                // 设置点的边距
                params.setMargins(marginHorizontal, marginVertical, marginHorizontal, marginVertical);
                // 设置按钮的大小属性
                btn.setLayoutParams(params);
                if (i == selected) {
                    btn.setBackgroundResource(R.drawable.icon_dot_selected);
                } else {
                    btn.setBackgroundResource(R.drawable.icon_dot_normal);
                }
                dotBtns.add(btn);
                dotContainer.addView(btn);
            }

            for (int i = 0; i < dotBtns.size(); i++) {
                final int finalI = i;
                dotBtns.get(i).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewPager.setCurrentItem(finalI);
                        selectDotBtn(dotBtns, finalI);
                    }
                });
            }
        }
        return dotBtns;
    }

    /**
     * 选择按钮
     *
     * @param finalI
     */
    private void selectDotBtn(List<Button> dotBtns, int finalI) {
        for (int i = 0; i < dotBtns.size(); i++) {
            if (i == finalI) {
                dotBtns.get(i).setBackgroundResource(R.drawable.icon_dot_selected);
            } else {
                dotBtns.get(i).setBackgroundResource(R.drawable.icon_dot_normal);
            }
        }
    }

    /**
     * 给数字加上","分隔符
     *
     * @param num
     * @return
     */
    private String formatNum(double num) {
        NumberFormat nf = NumberFormat.getNumberInstance();
        return nf.format(num);
    }
}

