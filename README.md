<h1 dir="rtl"><a name="PersianSectionStart"></a>نوتیفود</h1>
<div dir="rtl">
نوتیفود یک لایبرری جهت مشاهده منوی رستوران‌ها و ثبت سفارش از آنها می‌باشد. با استفاده از قابلیت وب فیزیکی (physical web) سیگنال‌های مورد نظر از رستوران‌ها دریافت شده و پس از تحلیل این سیگنال‌ها در نرم‌افزار موبایلی، نوتیفیکیشنی برای مشاهده منوی آن رستوران به کاربر نشان داده می‌شود.
<br/>
جهت کسب اطلاعات بیشتر، به وب سایت نوتیفود مراجعه نمایید

[http://notifood.com][1]
</div>



<p align="center">

![Notification](http://s6.uplod.ir/i/00914/zta4dvnhsqz6.png "Notification of the app")
</p>

<h2 dir="rtl">استفاده از نوتیفود</h2>
<h3 dir="rtl">پیش نیاز</h3>
<div dir="rtl">
برای استفاده از نوتیفود می‌بایست ابتدا به بخش توسعه دهندگان وبسلیت نوتیفود مراجعه کنید و پس از ثبت Package Name و شماره حساب جهت تسویه حساب، کد DeveloperKey را دریافت نمایید.
</div>
<h3 dir="rtl">نصب</h3>
<div dir="rtl">
شما می‌توانید به روش زیر Notifood را به پروژه اندرویدی خود اضافه کنید
<br/><br/>
</div>

```sh
It will be update ASAP
```
<div dir="rtl">
توجه داشته باشید minSdkVersion این لایبرری 14 می‌باشد
</div>

<h3 dir="rtl">توضیح متدهای نوتیفود</h3>


```java
setDevKey(String devKey)
```
<div dir="rtl">
ثبت کد developerKey در لایبرری
<br/><br/><br/>
</div>


```java
initialize(Context context)
```
<div dir="rtl">
راه‌اندازی لایبرری نوتیفود
<br/><br/><br/>
</div>

```java
enableNotifood()
```
<div dir="rtl">
فعالسازی لایبرری
<br/><br/><br/>
</div>

```java
disableNotifood()
```
<div dir="rtl">
غیرفعالسازی لایبرری
<br/><br/><br/>
</div>

```java
enableDebugMode()
```
<div dir="rtl">
فعالسازی حالت دیباگ، در این حالت لاگ‌های مربوطه با تگ Notifood در logcat نشان داده می‌شوند
<br/><br/><br/>
</div>

```java
disableDebugMode()
```
<div dir="rtl">
غیرفعالسازی حالت دیباگ، در این حالت لاگی نشان داده نمی‌شود
<br/><br/><br/>
</div>

<h3 dir="rtl">دسترسی‌های لازم برای نوتیفود</h3>
<div dir="rtl">
این لایبرری برای شناسایی سیگنال‌های BLE و ذخیره اطلاعات نیاز به داشتن دسترسی‌های زیر دارد.
</div>

```java
android.permission.INTERNET
android.permission.ACCESS_COARSE_LOCATION
android.permission.WRITE_EXTERNAL_STORAGE
```

<div dir="rtl">
این دسترسی ها در Manifest لایبرری وجود دارد و شما می‌بایست برای نسخه‌های جدید اندروید، در حین اجرای نرم‌افزار از کاربر دسترسی لازم را بگیرد
<br/>
نمونه کد برای انجام اینکار در ادامه وجود دارد.
<br/>
<b>توجه داشته باشید که هیچگونه استفاده‌ای از فایل‌های شخصی افراد نخواهد شد و برای شفافیت این موضوع کدهای لایبرری بصورت متن باز در اختیار شما قرار گرفته است</b>
</div>

<h3 dir="rtl">نمونه کد فعالسازی نوتیفود در پروژه</h3>
<div dir="rtl">
برای فعالسازی نوتیفود در پروژه می‌بایست دسترسی‌های لازم را از کاربر بگیرید، لایبرری را instantiate بکنید، کد DeveloperKey را به لایبرری بدهید و آن را initialize بکنید.
<br/>
در نمونه کد زیر تمام موارد گفته شده پیاده سازی شده است
<br/>
لطفا دقت کنید که این کد برای استفاده در یک Activity که از AppCompatActivity ساپورت لایبرری ارث بری کرده نوشته شده است. بنا به استفاده شما بخشی از این کد ممکن است نیاز به تغییر داشته باشد
</div>

```java
import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.notifood.notifoodlibrary.Notifood;


public class MainActivity extends AppCompatActivity {
    final int MY_PERMISSIONS_REQUEST = 100;
    final String[] PERMISSIONS = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_COARSE_LOCATION};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            int haveLocationPermission = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION);
            int haveWritePermission = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (haveLocationPermission!=PackageManager.PERMISSION_GRANTED || haveWritePermission!=PackageManager.PERMISSION_GRANTED) {

                boolean showLocationPermissionReason = ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION);
                boolean showWritePermissionReason = ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                if (showLocationPermissionReason || showWritePermissionReason) {
                    AlertDialog.Builder builder;

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        builder = new AlertDialog.Builder(MainActivity.this, android.R.style.Theme_Material_Dialog_Alert);
                    } else {
                        builder = new AlertDialog.Builder(MainActivity.this);
                    }

                    builder.setTitle(getString(R.string.request_permission_title))
                            .setMessage(getString(R.string.explanation_for_permissions))
                            .setPositiveButton(getString(R.string.close_btn), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    ActivityCompat.requestPermissions(
                                            MainActivity.this,
                                            PERMISSIONS,
                                            MY_PERMISSIONS_REQUEST);
                                    dialog.cancel();
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_info)
                            .show();
                } else {
                    ActivityCompat.requestPermissions(
                            MainActivity.this,
                            PERMISSIONS,
                            MY_PERMISSIONS_REQUEST);
                }
            } else {
                startNotifood();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST: {
                if (grantResults.length >= 2
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    startNotifood();
                }
                return;
            }
        }
    }

    private void startNotifood(){
        Notifood notifood = new Notifood(); // Instantiate the library
        notifood.setDevKey("YourDeveloperKey"); // Set your developer key
        notifood.enableDebugMode();	// Enable debug mode
        notifood.enableNotifood();	// Enable notifood
        notifood.initialize(this); // Initialize notifood
    }
}
```

<h2 dir="rtl">سوالات متداول</h2>
<div dir="rtl">
این بخش بزودی با سوالاتی که ممکن است با آن مواجه شوید تکمیل می‌شود
</div>


<h2 dir="rtl">تغییرات برای نسخه‌های آینده</h2>

- [ ] Add library to maven repository
- [ ] Add sample project in github
- [ ] Remove need for `WRITE_EXTERNAL_STORAGE` permission
- [ ] Support for IOS
- [ ] Support for Unity
- [ ] Add test case


<h2 dir="rtl">طرح مشکلات</h2>
<div dir="rtl">
لطفا هر مشکلی در رابطه با این لایبرری را در بخش issue tracker این لایبرری مطرح نمایید
</div>

<br/><br/><br/><br/><br/>

<h2 dir="rtl"></h2>
<div dir="rtl">
<p align="center">

تمامی حقوق این لایبرری متعلق به شرکت Notifood به آدرس زیر می‌باشد

[http://notifood.com][1]
</p>
</div>


[1]: http://notifood.com