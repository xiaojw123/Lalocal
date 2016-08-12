package com.lalocal.lalocal.model;

public class BaseEnum {
	// 对象类型 -1URL，0用户，1文章，2产品，3订单，4评论，5评价，6标签，7动态, 8画报,
	// 9线路,10专题,11广告,12作者主页,13资讯,14分享会 15.视频直播频道,16我的粉丝页面
	public enum TargetType {
		URL((byte) -1), USER((byte) 0), ARTICLE((byte) 1), PRODUCTION((byte) 2), ORDER((byte) 3), COMMENT(
				(byte) 4), APPRAISE((byte) 5), TAG((byte) 6), DYNAMICS((byte) 7), BANNER((byte) 8), ROUTE(
						(byte) 9), THEME((byte) 10), ADVERTISING((byte) 11), AUTHOR((byte) 12), INFOMATION(
								(byte) 13), LIVESHOW((byte) 14),CHANNEL((byte)15),FANS_PAGE((byte)16);

		private byte value;

		TargetType(byte value) {
			this.value = value;
		}

		public byte getValue() {
			return value;
		}
	}

	// 放置类型 0内容 1产品
	public enum AdvertisePlaceType {
		INDEX((byte) 0), PRODUCE((byte) 1);

		private byte value;

		AdvertisePlaceType(byte value) {
			this.value = value;
		}

		public byte getValue() {
			return value;
		}

		public void setValue(byte value) {
			this.value = value;
		}
	}

	// 排序方式:
	public enum SortType {
		TIME, READNUM, REALREADNUM, PRAISENUM;
		public static String getValue(int key) {
			String value = null;
			switch (key) {
			case 0:
				value = "created_at desc";
				break;
			case 1:
				value = "read_num desc";
				break;
			case 2:
				value = "real_read_num desc";
				break;
			case 3:
				value = "praise_num desc";
				break;
			case 4:
				value = "price desc";
				break;
			case 5:
				value = "price";
				break;
			case 6:
				value = "published_at desc";
				break;
			}
			return value;
		}
	}

	// 操作类型，评论，点赞，回复,删除评论，取消赞
	public enum OperationType {
		COMMENT, PRAISE, REPLY, DEL_COMMENT, DEL_PRAISE
	}

	// 动态状态，0被删除 1正常
	public enum DynamicsStatus {
		DELETE, Normal
	}

	// 评论状态 0被删除，1正常，2被禁用
	public enum CommentStatus {
		DELETE, Normal, DISABLED
	}

	// 媒体素材类型，图片，视频
	public enum MediaType {
		IMG((byte) 0), VIDEO((byte) 1);

		private byte value;

		MediaType(byte value) {
			this.value = value;
		}

		/**
		 * @return the value
		 */
		public byte getValue() {
			return value;
		}

		/**
		 * @param value
		 *            the value to set
		 */
		public void setValue(byte value) {
			this.value = value;
		}
	}

	// tag状态，正常, 推荐
	public enum TagStatus {
		Normal((byte) 0), RECOMMEND((byte) 1);

		private byte value;

		TagStatus(byte value) {
			this.value = value;
		}

		public byte getValue() {
			return value;
		}

		public void setValue(byte value) {
			this.value = value;
		}
	}

	// 订单状态 已取消/已预订(未支付)/已支付/已消费/已评价/申请退款/退款中/已退款/退款失败
	public enum OrderStatus {
		CANCELED((byte) 0), ORDERED((byte) 1), PAYED((byte) 2), USED((byte) 3), APPRAISED((byte) 4),REFUNDAPPLY((byte) 5),
		REFUNDING((byte) 6), REFUNDED((byte) 7),REFUNDFAILED((byte) 8);
		private byte value;

		OrderStatus(byte value) {
			this.value = value;
		}

		public byte getValue() {
			return value;
		}

		public void setValue(byte value) {
			this.value = value;
		}
	}

	// 用户类型 管理员/普通用户/专栏作者/工作人员/路线提供者/马甲
	public enum UserType {
		ADMIN((byte) -1), NORMAL((byte) 0), AUTHOR((byte) 1), STAFF((byte) 2), ROUTER((byte) 3), MAJIA(
				(byte) 4), TRANSLATOR((byte) 5),PARTNER((byte)6);
		private byte value;

		UserType(byte value) {
			this.value = value;
		}

		public byte getValue() {
			return value;
		}

		public void setValue(byte value) {
			this.value = value;
		}
	}

	// 退款状态 可退款/申请退款/退款中/已受理/退款成功/退款失败
	public enum RefundStatus {
		CAN_REFUND((byte) 0),REFUNDAPPLY((byte) 1), REFUNDING((byte) 2), ACCEPTED((byte) 3), SUCCESS((byte) 4), FAILED((byte) 5);
		private byte value;

		RefundStatus(byte value) {
			this.value = value;
		}

		public byte getValue() {
			return value;
		}

		public void setValue(byte value) {
			this.value = value;
		}
	}

	// 支付方式 0：支付宝app支付 1：微信app支付 2：支付宝手机网页支付 3：微信公众号支付 4:信程支付 5：优惠券支付
	public enum PayType {
		ALIPAY_APP, WECHAT_APP, APLIPAY_WAP, WECHAT_WEB, XINCHENG_PAY,COUPON_PAY;
		public static String getValue(int key) {
			String value = null;
			switch (key) {
			case 0:
				value = "支付宝app支付";
				break;
			case 1:
				value = "微信支付";
				break;
			case 2:
				value = "支付宝网页支付";
				break;
			case 3:
				value = "微信公众号支付";
				break;
			case 4:
				value = "分期支付";
				break;
			case 5:
				value = "优惠券支付";
				break;
			}
			return value;
		}
	}

	// 0目的地 1交通 2餐饮 3住宿 4项目 5路线 6其它(比较特殊)
	public enum TagType {
		MDD, TRAFFIC, FOOD, HOTEL, PROJECT, ROUTE, OTHER;
		public static String getValue(int key) {
			String value = null;
			switch (key) {
			case 0:
				value = "目的地";
				break;
			case 1:
				value = "交通";
				break;
			case 2:
				value = "餐饮";
				break;
			case 3:
				value = "住宿";
				break;
			case 4:
				value = "项目";
				break;
			case 5:
				value = "路线";
				break;
			case 6:
				value = "其它";
				break;
			default:
				value = "";
				break;
			}
			return value;
		}
	}

	// 用户状态，未激活（邮箱未认证）,正常，拉黑，禁用
	public enum UserStatus {
		UN_ACTIVED((byte) -1), NORMAL((byte) 0), BLACK((byte) 1), FORBIDEN((byte) 2);

		private byte value;

		UserStatus(byte value) {
			this.value = value;
		}

		public byte getValue() {
			return value;
		}

		public void setValue(byte value) {
			this.value = value;
		}
	}

	// 产品状态：正常，售罄，过期，已删除
	public enum ProductionStatus {
		NORMAL((byte) 0), SOLDOUT((byte) 1), EXPIRED((byte) 2), DELETED((byte) -1);
		private byte value;

		ProductionStatus(byte value) {
			this.value = value;
		}

		public byte getValue() {
			return value;
		}

		public void setValue(byte value) {
			this.value = value;
		}
	}

	// 产品翻译状态：未翻译，翻译中,待审核,已翻译（审核通过）
	public enum ProductionTranslateStatus {
		PRETRANSLATE, TRANSLATING, PRECHECK, TRANSLATED;
	}

	// 广告类型 url，文章详情，产品详情，集合
	public enum AdvertisingType {
		URL((byte) 0), ARTICLE((byte) 1), PRODUCTION((byte) 2), GATHER((byte) 3);

		private byte value;

		AdvertisingType(byte value) {
			this.value = value;
		}

		public byte getValue() {
			return value;
		}

		public void setValue(byte value) {
			this.value = value;
		}
	}

	// 0未审核，1审核成功，2审核中，-1审核失败 , 3 推荐
	public enum ArticleStatus {
		UN_CHECK((byte) 0), SUCCESS((byte) 1), CHECKING((byte) 2), CHECK_FAIL((byte) -1), RECOMMEND((byte) 3);
		private byte value;

		ArticleStatus(byte value) {
			this.value = value;
		}

		public byte getValue() {
			return value;
		}

		public void setValue(byte value) {
			this.value = value;
		}
	}

	// 请求头部信息
	public enum HttpHeaderParm {
		USER_ID, TOKEN, APP_VERSION, DEVICE, DEVICE_ID, LATITUDE, LONGITUDE, DEVICE_WIDTH, DEVICE_HEIGHT;
		public String getValue() {
			switch (this) {
			case USER_ID:
				return "USER_ID";
			case TOKEN:
				return "TOKEN";
			case APP_VERSION:
				return "APP_VERSION";
			case DEVICE:
				return "DEVICE";
			case DEVICE_ID:
				return "DEVICE_ID";
			case LATITUDE:
				return "LATITUDE";
			case LONGITUDE:
				return "LONGITUDE";
			case DEVICE_WIDTH:
				return "DEVICE_WIDTH";
			case DEVICE_HEIGHT:
				return "DEVICE_HEIGHT";
			default:
				return null;
			}
		}
	}

	// 地区
	public enum Area {
		SOUTHEASTASIA("日韩", "2016012111173614956789999138"), KOREA("东南亚", "201601211118314745657262910"), EUROPE("欧洲",
				"201601211118528808090257058"), JAPAN("北美洲", "20160121111916472309402495"), AMERICA("大洋洲",
						"201601211119349024441940061");

		private String name;
		private String photo;

		Area(String name, String photo) {
			this.name = name;
			this.photo = photo;
		}

		public String getName() {
			return name;
		}

		public String getPhoto() {
			return photo;
		}
	}

	// 集合
	public enum Collections {
		FOOD("饕餮美食", "2016012111252717317416512224", "201601211139106251139060507"), PARK("主题乐园",
				"201601211128009489907572777", "2016012111393317325446788075"), SPORTS("户外运动",
						"201601211128483269683580320", "2016012111401110564228182598"), RECREATION("休闲娱乐",
								"2016012111291118010764187672", "2016012111403510801256900792"), TOUR("经典观光",
										"2016012111302418333673612499", "20160121114100881398126245"), HISTORY("文化历史",
												"20160121113135487823157247", "20160121114119263031262625");

		private String name;
		private String photo;
		private String photoPre;

		Collections(String name, String photo, String photoPre) {
			this.name = name;
			this.photo = photo;
			this.photoPre = photoPre;
		}

		public String getName() {
			return name;
		}

		public String getPhoto() {
			return photo;
		}

		public String getPhotoPre() {
			return photoPre;
		}
	}

	public enum ShareUrl {
		BANNER("/banner?id="), ARTICLE("/article?id="), PRODUCTION("/production?id="), ROUTE("/route?id="), THEME(
				"/theme?id="), LIVE("/live?id="), CHANNEL("/channel?id="), INVITE("/invite"), CHALLENGE("/challenge?id=");

		private String targetUrl;

		ShareUrl(String targetUrl) {
			this.targetUrl = targetUrl;
		}

		public String getTargetUrl() {
			return targetUrl;
		}
	}

	// 联系人字段类型：0字符串 1性别 2日期 3电话（"格式为+86 15868855709"） 4数字 5姓名 6拼音,7下拉选择,8 布尔类型（是否领队)，9 姓
	public enum ContactFieldType {
		STRING, SEX, DATE, PHONE, NUMBER, NAME,PINYIN,SELECT, BOOLEAN,SURNAME;
	}

	// 产品选项类型：类型，0、其他，1:出行人群，2、出行时间，3、集合地点，4、套餐选择，5、销售项目
	public enum ProduItemType {
		OTHER, PEOPLE, TIME, LOCATION, ROUTE, SALEITEM;
	}

	// 分享的类型，0 纯图片，1纯文字，2视屏，3应用分享，即有图片，有文字，能点击跳转
	public enum ShareType {
		IMAGE, TEXT, MOVIE, URL
	}

	// 用户未激活 -100 邮箱不存在 10,云信游客帐户分配完了 11，
	public enum ErrorCode {
		USER_STATUS((int) -100), EMAIL_NOT_EXIST((int) 10), IM_USER_FULL((int) 11);
		private int value;

		ErrorCode(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}

		public void setValue(int value) {
			this.value = value;
		}
	}



	// 亮点介绍 服务介绍 购买须知 凭证说明 退款政策 
	public enum ProductionDetailType {
		HIGH_LIGHTS((byte) 1), SERVICE((byte) 2), BUY_NOTE((byte) 3),
		VOUCHER_REQUIREMENT((byte)4),REFUND_POLICY((byte)5);
		private byte value;

		ProductionDetailType(byte value) {
			this.value = value;
		}

		public byte getValue() {
			return value;
		}

		public void setValue(byte value) {
			this.value = value;
		}
	}

	public enum ProductionSupplier {
		LIXING, VIATOR, GTA;
	}

	public enum JobStatus {
		UN_ACTIVE((byte) 0), ACTIVED((byte) 1), END((byte) 2);
		private byte value;

		JobStatus(byte value) {
			this.value = value;
		}

		public byte getValue() {
			return value;
		}

		public void setValue(byte value) {
			this.value = value;
		}
	}

	// 优惠券状态 0：未使用 1：已使用 2：过期
	public enum CouponStatus {
		UNUSED((byte) 0), USED((byte) 1), EXPIRED((byte) 2);
		private byte value;

		CouponStatus(byte value) {
			this.value = value;
		}

		public byte getValue() {
			return value;
		}

		public void setValue(byte value) {
			this.value = value;
		}
	}

	// 优惠券类型，0普通优惠券 1作者优惠券
	public enum CouponType {
		NORMAL((byte) 0), AUTHOR((byte) 1);
		private byte value;

		CouponType(byte value) {
			this.value = value;
		}

		public byte getValue() {
			return value;
		}

		public void setValue(byte value) {
			this.value = value;
		}
	}

	// 文章类型，0原创文章，1资讯
	public enum ArticleType {
		NORMAL((byte) 0), INFOMATION((byte) 1);
		private byte value;

		ArticleType(byte value) {
			this.value = value;
		}

		public byte getValue() {
			return value;
		}

		public void setValue(byte value) {
			this.value = value;
		}
	}

	/*
	 * 专题类型，0 综合 1 商品 2作者 3 文章
	 */
	public enum ThemeType {
		NORMAL((byte) 0), PRODUCTION((byte) 1), AUTHOR((byte) 2), ARTICLE((byte)3);
		private byte value;

		ThemeType(byte value) {
			this.value = value;
		}

		public byte getValue() {
			return value;
		}

		public void setValue(byte value) {
			this.value = value;
		}
	}

	/*
	 * 任务状态 0未开始 1进行中 2已完成 3失败
	 */
	public enum ImportStatus {
		NOT_START, RUNNING, DONE, FAIL
	}

	/*
	 * 优惠码类型 0:生成的优惠券编码和优惠码一致;且截止使用日期与优惠码一致 1：生成的优惠券编码随机
	 */
	public enum CodeType {
		NUMBER_SAME, NUMBER_RANDOM
	}

	/*
	 * 分享会状态：0未开始 1进行中 -1已结束
	 */
	public enum LiveShowStatus {
		NOT_START((byte) 0), STARTED((byte) 1), END((byte) -1);
		private byte value;

		LiveShowStatus(byte value) {
			this.value = value;
		}

		public byte getValue() {
			return value;
		}

		public void setValue(byte value) {
			this.value = value;
		}
	}
	
	// 信程账单状态 已还清，未还清，逾期
	public enum CreditStatus{
		CLEAN,REMAIN,EXPIRED
	}

	/*
	 * 产品类型，0为跟团游，1为自由行
	 */
	public enum ProductionType {
		GROUP, FREE
	}
	/*
	 * 用户关注，0未关注，1单方面关注 2 互相关注
	 */
	public enum UserRelationStatus{
		NONE,SINGLE,BOTH
	}
	/*
	 * 联系人信息类型，姓，名，邮箱，证件类型，证件有效期，微信号，性别，生日，国籍,当地电话，出发地电话
	 */
	public enum ContactItemType{
		ISLEADER((byte)-1),SURNAME((byte)0),FIRSTNAME((byte)1),EMAIL((byte)2),IDENTITYTYPE((byte)3),
		IDENTITYNUM((byte)4),IDENTITYEXPIRDATE((byte)5),WEIXIN((byte)6),GENDER((byte)7),
		BIRTHDAY((byte)8),NATIONALITY((byte)9),LOCALTEL((byte)10),DEPARTURETEL((byte)11)
		,PHONE((byte)12);
		private byte value;
		ContactItemType(byte value){
			this.value = value;
		}
		
		public Byte getValue(){
			return value;
		}
		
		public void setValue(byte value){
			this.value = value;
		}
	}
	
	/*
	 * 2 预订已提交,3 预订成功,4 预订失败，5 确认已提交，6 出票成功，7 出票失败，8 配送提交，9 配送失败
	 * 10 已完成 11 申请退订 18 退订失败 19 退订成功 20 已取消
	 */
	public enum LixingOrderStatus{
		BOOKSUBMITED((byte)2),BOOKED((byte)3),BOOKFAIL((byte)4),CONFIRMSUBMITED((byte)5),
		TICKETSUCCESS((byte)6),TICKETFAIL((byte)7),DELIVERYSUBMITED((byte)8),DELIVERYFAIL((byte)9),
		COMPLETED((byte)10),APPLYREFUND((byte)11),REFUNDFAIL((byte)18),REFUNDSUCCESS((byte)19),CANCELED((byte)20);
		
		private Byte value;
		
		LixingOrderStatus(Byte value){
			this.value = value;
		}
		
		public Byte getValue(){
			return value;
		}
		
		public void setValue(byte value){
			this.value = value;
		}
	}
	
	/*
	 * 0 等待预订,2 预订流程中,3 预订成功,4、等待预订,5、预订失败,6、预订设置流程暂停
	 * 7 预订流程中因为预订设置
	 */
	public enum ViatorBookingStatus{
		WAITING((byte)0),BEINGBOOKED((byte)2),CONFIRMED((byte)3),PENDINGBOOKING((byte)4),FAILED((byte)5),
		BOOKINGSET((byte)6),BEINGBOOKEDSET((byte)7),BEINGBOOKEDCOMMIT((byte)8),BEINGCANCELLED((byte)9),
		CANCELLED((byte)10),PENDINGCANCELLATION((byte)11),AMENDED((byte)12),PENGDINGAMENDMENT((byte)13);
		private Byte value;
		ViatorBookingStatus(Byte value) {
			this.value = value;
		}
		
		public Byte getValue(){
			return value;
		}
		
		public void setValue(byte value){
			this.value = value;
		}
	}
	
	//0 等待下单，1 已确认，2 不可用，3 已提交(等待供应商回应)，4 预订失败，5 退订成功，
	//6 已过期，7、行程变更 8、已提交同时行程变更(可退订)
	public enum ViatorItemStatus{
		WAITING((byte)0),CONFIRMED((byte)1),UNAVAILABLE((byte)2),PENDING((byte)3),FAILED((byte)4),CANCELLED((byte)5),
		EXPIRED((byte)6),AMENDED((byte)7),PENDINGAMEND((byte)8);
		private Byte value;
		ViatorItemStatus(Byte value) {
			this.value = value;
		}
		
		public Byte getValue(){
			return value;
		}
		
		public void setValue(byte value){
			this.value = value;
		}
	}
	//供应商下单确认方法，立即确认，需要确认,在一定时间内立即确认
	public enum SupplierOrderMethod{
		NOT_CONFIRM((byte)0),NEET_CONFIRM((byte)1),NO_COMFIRM_ON_REQUEST((byte)2);
		private Byte value;
		SupplierOrderMethod(byte value){
			this.value = value;
		}
		
		public Byte getValue(){
			return value;
		}
		
		public void setValue(byte value){
			this.value = value;
		}
	}
	
	// 注册，签到，直播，礼物，评价，分享应用，h5活动,完成挑战
	public enum ScoreRule{
		REGISTER, QIANDAO, ZHIBO,GIFT, APPRAISE, SHARE, H5, CHALLENGE
	}
	
	// 充值，积分兑换，退还
	public enum GoldInRule{
		RECHARGE("充值"), SCORE_EXCHANGE("积分兑换"), RETURN("退款");
		private String msg;
		public String getMsg() {
			return msg;
		}
		GoldInRule(String msg){
			this.msg = msg;
		}
	}
	
	// 购买礼物 挑战投钱 取现 下单支付
	public enum GoldOutRule{
		GIFT("购买礼物"), CHALLENGE("挑战众筹"), DISCOUNTED("取现"), ORDER_PAY("下单支付");
		private String msg;
		GoldOutRule(String msg){
			this.msg = msg;
		}
		public String getMsg() {
			return msg;
		}
	}
	
	// 状态，0新发起，1进行中，2已完成，3已拒绝，4已放弃
	public enum ChallengeStatus{
		NEW, STARTED, COMPLATED, REJECTED, FAILED
	}
	
	// 专题 0新发起 1已同意 2跳过
	public enum ChannelReportStatus{
		NEW, ACCEPTED, SKIPED
	}
	
	// 分享用的缩略图参数
	public static final String THUMBNAILS_SHARED = "?imageMogr2/auto-orient/strip/thumbnail/!200x200r/gravity/Center/crop/200x200";

	// 激活链接
	public static final String ACTIVE_URL = "/active?userKey=";
	// app 嵌入的页面链接
	public static final String ARTICLE_URL = "/app_article?id=";
	// app 产品详情页面
	public static final String PROD_ITINERARY = "/app_itinerary?id=";
	// app 专题详情页面
	public static final String THEME_URL = "/app_theme?id=";
	// 分享会结束后h5地址
	public static final String LIVE_URL = "/live_end?id=";
	// 支付成功跳转页面
	public static final String SUCCESS_URL = "/paySuccess";
	// 支付发起页面
	public static final String PAY_URL = "/payStart?orderId=";
	// 账单发起页面
	public static final String BILL_URL = "/bill?xcAccount=";
	// app h5下单页面
	public static final String ORDER_SELECT = "/order_select?id=";
	// lalocal icon
	public static final String ICON = "icon.jpg";
	
	public static final int ORDER_EXPIRE_MIN = 120; 
}

