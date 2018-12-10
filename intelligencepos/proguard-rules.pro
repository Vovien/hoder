# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in G:\android-sdk-windows/tools/proguard/proguard-android.txt
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

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
#   通配符      描述
#   <field>     匹配类中的所有字段
#   <method>    匹配类中所有的方法
#   <init>      匹配类中所有的构造函数
#   *           匹配任意长度字符，不包含包名分隔符(.)
#   **          匹配任意长度字符，包含包名分隔符(.)
#   ***         匹配任意参数类型
#   keep                        保留类和类中的成员，防止被混淆或移除
#   keepnames                   保留类和类中的成员，防止被混淆，成员没有被引用会被移除
#   keepclassmembers            只保留类中的成员，防止被混淆或移除
#   keepclassmembernames        只保留类中的成员，防止被混淆，成员没有引用会被移除
#   keepclasseswithmembers      保留类和类中的成员，防止被混淆或移除，保留指明的成员
#   keepclasseswithmembernames  保留类和类中的成员，防止被混淆，保留指明的成员，成员没有引用会被移除


#R文件中的所有记录资源id的静态字段
-keepclassmembers class **.R$* {
    public static <fields>;
}

-keep class * extends java.lang.annotation.Annotation { *; }
-keepattributes Signature
-keep public class * extends android.app.Fragment
#-------------------------------------------基本不用动区域--------------------------------------------
#---------------------------------基本指令区----------------------------------

-optimizationpasses 5 #代码混淆的压缩比例，值在0-7之间
-dontusemixedcaseclassnames #混淆后类名都为小写
-dontskipnonpubliclibraryclasses #指定不去忽略非公共的库的类
-dontskipnonpubliclibraryclassmembers #指定不去忽略非公共的库的类的成员
-dontpreverify #不做预校验的操作
-verbose #生成原类名和混淆后的类名的映射文件
-printmapping proguardMapping.txt #生成原类名和混淆后的类名的映射文件
-optimizations !code/simplification/cast,!field/*,!class/merging/* #指定混淆是采用的算法
-keepattributes *Annotation*,InnerClasses #不混淆Annotation
-keepattributes Signature #不混淆泛型
-keepattributes SourceFile,LineNumberTable #抛出异常时保留代码行号
#----------------------------------------------------------------------------
#---------------------------------默认保留区---------------------------------
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class * extends android.view.View
#保留Google原生服务需要的类
-keep public class com.android.vending.licensing.ILicensingService
-keep class android.support.** {*;}

#保留native方法的类名和方法名
-keepclasseswithmembernames class * {
    native <methods>;
}

-keepclassmembers class * extends android.app.Activity{
    public void *(android.view.View);
}
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
-keep public class * extends android.view.View{
    *** get*();
    void set*(***);
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}
-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}
-keep class **.R$* {
 *;
}
-keepclassmembers class * {
    void *(**On*Event);
}
#----------------------------------------------------------------------------

#---------------------------------webview------------------------------------
-keepclassmembers class fqcn.of.javascript.interface.for.Webview {
   public *;
}
-keepclassmembers class * extends android.webkit.WebViewClient {
    public void *(android.webkit.WebView, java.lang.String, android.graphics.Bitmap);
    public boolean *(android.webkit.WebView, java.lang.String);
}
-keepclassmembers class * extends android.webkit.WebViewClient {
    public void *(android.webkit.WebView, jav.lang.String);
}
#----------------------------------------------------------------------------
#------------------------------------------------------------------------------------------


#okio
-dontwarn okio.**
#-keep class okio.**{*;}
#-keep interface okio.**{*;}
#okhttp3
-dontwarn com.squareup.okhttp3.**
#-keep class com.squareup.okhttp3.** { *;}

#Retrofit
#-keep class retrofit2.** { *; }
-dontwarn retrofit2.**

### greenDAO 3
-dontwarn org.greenrobot.greendao.database.**
-keepclassmembers class * extends org.greenrobot.greendao.AbstractDao {
  public static java.lang.String TABLENAME;
}
-keep class **$Properties


-dontwarn rx.**


# 删除代码中Log相关的代码
-assumenosideeffects class android.util.Log {
    public static boolean isLoggable(java.lang.String, int);
    public static int v(...);
    public static int i(...);
    public static int w(...);
    public static int d(...);
    public static int e(...);
}
#Parcelable实现类中的CREATOR字段是绝对不能改变的，包括大小写
-keepclassmembers class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator CREATOR;
}


#忽略警告
#-ignorewarning




#-------------------------------------------定制化区域----------------------------------------------
#---------------------------------1.实体类---------------------------------
####-keep class cn.hadcn.test.**
####-keep class cn.hadcn.test.*
####一颗星表示只是保持该包下的类名，而子包下的类名还是会被混淆。两颗星表示把本包和所含子包下的类名都保持
####用以上方法保持类后，你会发现类名虽然未混淆，但里面的具体方法和变量命名还是变了
#如果既想保持类名，又想保持里面的内容不被混淆，我们就需要以下方法了-keep class cn.hadcn.test.* {*;}
-keep public class  com.holderzone.intelligencepos.mvp.model.bean.** { *; }
-keep public class  com.holderzone.intelligencepad.mvp.activity.*
#-------------------------------------------------------------------------

#---------------------------------2.第三方包-------------------------------

#bugly
-dontwarn com.tencent.bugly.**
-keep public class com.tencent.bugly.**{*;}
-keep class android.support.**{*;}
#okhttp3
-dontwarn com.squareup.okhttp3.**
#百度语音
-keep class com.baidu.** { *; }

#-------------------------------------------------------------------------

#---------------------------------3.与js互相调用的类------------------------



#-------------------------------------------------------------------------

#---------------------------------4.反射相关的类和方法-----------------------



#----------------------------------------------------------------------------
#------------------------------------------------------------------------------------------
