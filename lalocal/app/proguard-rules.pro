# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\Android\SDK/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}
-keep class org.apache.http.impl.client.**
-dontwarn org.apache.commons.**
-keep class com.blueware.** { *; }
-dontwarn com.blueware.**
-keep class com.oneapm.** {*;}
-dontwarn com.oneapm.**
-keepattributes Exceptions, Signature, InnerClasses
-keepattributes SourceFile, LineNumberTable
#online delete
# ProGuard configurations for Bugtags
-dontskipnonpubliclibraryclasses # 不忽略非公共的库类
-optimizationpasses 5            # 指定代码的压缩级别
-dontusemixedcaseclassnames      # 是否使用大小写混合
-dontpreverify                   # 混淆时是否做预校验
-verbose                         # 混淆时是否记录日志
-keepattributes *Annotation*     # 保持注解
-ignorewarning                   # 忽略警告
-dontoptimize                    # 优化不优化输入的类文件
-keepclassmembers class * {
   public <init> (org.json.JSONObject);
}
-keep public class [com.lalocal.lalocal].R$*{
public static final int *;
}                             #避免R文件被移除
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*  # 混淆时所采用的算法

#保持哪些类不被混淆
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class com.android.vending.licensing.ILicensingService

#生成日志数据，gradle build时在本项目根目录输出
-dump class_files.txt            #apk包内所有class的内部结构
-printseeds seeds.txt            #未混淆的类和成员
-printusage unused.txt           #打印未被使用的代码
-printmapping mapping.txt        #混淆前后的映射

-keep public class * extends android.support.** #如果有引用v4或者v7包，需添加
#-libraryjars libs/volley.jar        #混淆第三方jar包，其中xxx为jar包名
-keep class com.android.volley.**{*;}       #不混淆某个包内的所有文件
-dontwarn com.android.volley**              #忽略某个包的警告
-keepattributes Signature        #不混淆泛型
-keepnames class * implements java.io.Serializable #不混淆Serializable
#
#-keepclassmembers class **.R$* { #不混淆资源类
#　　public static <fields>;
#}
-keepclasseswithmembernames class * {  # 保持 native 方法不被混淆
    native <methods>;
}
-keepclasseswithmembers class * {      # 保持自定义控件类不被混淆
    public <init>(android.content.Context, android.util.AttributeSet);
}
-keepclasseswithmembers class * {      # 保持自定义控件类不被混淆
    public <init>(android.content.Context, android.util.AttributeSet, int);
}
-keepclassmembers class * extends android.app.Activity { # 保持自定义控件类不被混淆
    public void *(android.view.View);
}
-keepclassmembers enum * {             # 保持枚举 enum 类不被混淆
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
-keep class * implements android.os.Parcelable {         # 保持 Parcelable 不被混淆
    public static final android.os.Parcelable$Creator *;
}
#    3D 地图

    -keep   class com.amap.api.mapcore.**{*;}
    -keep   class com.amap.api.maps.**{*;}
    -keep   class com.autonavi.amap.mapcore.*{*;}

#    定位

   -keep class com.amap.api.location.**{*;}
   -keep class com.amap.api.fence.**{*;}
   -keep class com.autonavi.aps.amapapi.model.**{*;}

#    搜索

    -keep   class com.amap.api.services.**{*;}

#    2D地图

    -keep class com.amap.api.maps2d.**{*;}
    -keep class com.amap.api.mapcore2d.**{*;}

#    导航

    -keep class com.amap.api.navi.**{*;}
    -keep class com.autonavi.**{*;}
    -keep class com.easemob.** {*;}
    -keep class org.jivesoftware.** {*;}
    -keep class org.apache.** {*;}
    -dontwarn  com.easemob.**
    #2.0.9后的不需要加下面这个keep
    #-keep class org.xbill.DNS.** {*;}
    #另外，demo中发送表情的时候使用到反射，需要keep SmileUtils
    -keep class com.easemob.chatuidemo.utils.SmileUtils {*;}
    #注意前面的包名，如果把这个类复制到自己的项目底下，比如放在com.example.utils底下，应该这么写（实际要去掉#）
    #-keep class com.example.utils.SmileUtils {*;}
    #如果使用EaseUI库，需要这么写
    -keep class com.easemob.easeui.utils.EaseSmileUtils {*;}

    #2.0.9后加入语音通话功能，如需使用此功能的API，加入以下keep
    -dontwarn ch.imvs.**
    -dontwarn org.slf4j.**
    -keep class org.ice4j.** {*;}
    -keep class net.java.sip.** {*;}
    -keep class org.webrtc.voiceengine.** {*;}
    -keep class org.bitlet.** {*;}
    -keep class org.slf4j.** {*;}
    -keep class ch.imvs.** {*;}
#网易云信
-dontwarn com.netease.**
-dontwarn io.netty.**
-keep class com.netease.** {*;}
-keep class io.netty.** {*;}
-keepclasseswithmembers class cmb.pb.util.CMBKeyboardFunc {
    public <init>(android.app.Activity);
    public boolean HandleUrlCall(android.webkit.WebView,java.lang.String);
    public void callKeyBoardActivity();
}