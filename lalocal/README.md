三方库说明

EaseMobSDK：环信SDK，主要是是提供移动客服的功能，为客服跟客户之间架起交流的桥梁；
1.Social_Sdk：分享，用于分享到第三方平台(友盟)；
2.Ping++：主要是为支付提供支持，如微信或支付宝支付等；
3.Volley： 网络请求
4.云信SDK：直播，聊天功能
5.Imgeloader：图片加载缓存
6.Qiniu：七牛上传图片加速SDK
代码约定规则(同IOS)

URL Schemes: LLalocal

APP外部HTML打开APP

跳转指定页

llalocal://path?{"targetType":"10","targetId":"35","targetUrl":"https://dev.lalocal.cn/wechat/app_theme?id=35"}
打开app

llalocal://callback?
APP内部HTML链接解析

指定跳转页

lalocal://app?{"targetType": "10","targetId": "35","targetUrl": "https://dev.lalocal.cn/wechat/app_theme?id=35"}
用户未登录: lalocal://app?
文章中二维码点击

lalocal://codeimageclick?{"name": "10","wechatNo": "35","imageUrl": "http://7xpid3.com1.z0.glb.clouddn.com/2016052111453710470326042255"}
信程支付成功回调

myweb:lalocalcreditpaysuccessful

