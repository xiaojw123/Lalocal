关闭日志MyApplication/isDebug=false;
关闭bugTag
1. lalocal/build.gradle
   dependencies{
     classpath 'com.bugtags.library:bugtags-gradle:latest.integration'
   }
2. lalocal/app/build.gradle
   apply plugin: 'com.bugtags.library.plugin'
apply plugin: 'com.bugtags.library.plugin'
bugtags {
    //自动上传符号表功能配置，如果需要根据 build varint 配置，请参考插件详细使用说明
    appKey "fa970dd98b61298053b6a9cb88597605"  //这里是你的 appKey
    appSecret "76f72474a71536c759f78f82d2121c30"    //这里是你的 appSecret，管理员在设置页可以查看
    mappingUploadEnabled true

    //网络跟踪功能配置(企业版)
    trackingNetworkEnabled true
}
dependencies {
    compile 'com.bugtags.library:bugtags-lib:latest.integration'
}
3. MyApplication/oncreate()
   Bugtags.start("fa970dd98b61298053b6a9cb88597605", this, Bugtags.BTGInvocationEventBubble);
4. BaseActivity
    @Override
    protected void onResume() {
        super.onResume();
       Bugtags.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
       Bugtags.onPause(this);
    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
      Bugtags.onDispatchTouchEvent(this, event);
        return super.dispatchTouchEvent(event);
    }