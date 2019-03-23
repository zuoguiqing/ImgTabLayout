# ImgTabLayout

## 自定义可以设置图片的TabLayout

示例图片：
![](https://github.com/zuoguiqing/ImgTabLayout/blob/master/155325956.jpg)


## 在 buld.gradle 中添加依赖
### 一、
```
	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
 ```
  
### 二、
```
	dependencies {
	        implementation 'com.github.zuoguiqing:ImgTabLayout:1.0.0'
	}
```

## 在XML布局文件中添加 ImgTabLayout
```
<com.zgq.imgtablibrary.ImgTabLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"/>
```
### xml布局文件指定样式
```
<com.zgq.imgtablibrary.ImgTabLayout
        android:id="@+id/tabLayout"
        android:layout_width="match_parent"
        android:layout_height="56dp"
        app:viewHeight="56dp"
        app:viewWidth="84dp"
        app:mTextColorDef="@color/c666666"
        app:mTextColorSelect="@color/cfc9464"
        app:textSize="16sp"
        app:textSizeSel="22sp"
        app:indicatorWidth="20dp"
        app:indicatorHeight="30dp"
        app:indicatorBgResId="@mipmap/icon"
        app:mTextBgDefResId="@mipmap/ic_launcher"
        app:mTextBgSelectResId="@color/c2aa9c9"/>
	
	<!--是属性说明-->	
	<!--viewHeight， viewWidth : tablayout中TextView的宽高(注意：由于点击事件是在textView上
	，所以最好把textView的高度设置成tab的高度)-->
    	<!--mTextColorDef，mTextColorSelect ：文字的默认和选中时的颜色-->
	<!--textSize，textSizeSel ：文字的默认和选中时的大小，textSizeSel不设置则统一默认大小，如果
	 设置了不同大小，viewWidth一定要指定宽度，不然下方指标会错位-->
	<!--indicatorWidth，indicatorHeight ：文字下方指标的宽高-->
	<!--indicatorBgResId ：文字下方指标的显示资源，可以是图片也可以是颜色-->
	<!--mTextBgDefResId ， mTextBgSelectResId：文字背景设置， 默认背景和选中背景-->
```

### 上面的所有属性说明，都有开放有 set 方法，方便在java类中使用

## 最后
希望这个view对你有所帮助
具体的可以下载demo自个儿看，其实代码不多。使用时可以添加上方依赖，也可以拷贝源码。
如果有bug，或者能改进优化，请留言联系我
