# 屏幕适配
>原因：Android设备碎片化，导致app的的界面元素在不同屏幕尺寸上显示不一致
>目的：让布局，布局组件，资源，用户界面流程，匹配不同屏幕尺寸

---
#### 布局适配
- 避免写死控件尺寸,使用wrap_content,match_parent
- LinearLayout xxx:layout_weight =1
- ReleaveLayout xxx:layout_centerInParent = "true"
- ContraintLayout xxx:layout_constraintLeft_toLeft = "parent"
- Percent-support-lib xxx:layout_widthPercent="30%"
---

### 一.修改系统Density
- 修改density，scaleDensity,densityDpi值-直接更改系统内部对于目标尺寸而言的像素密度

|名称|定义|翻译|
|:-:|:-:|:-:|
|density| The logical density of the display.  This is a scaling factor for the Density Independent Pixel unit, where one DIP is one pixel on an approximately 160 dpi screen (for example a 240x320, 1.5"x2" screen),providing the baseline of the system's display. Thus on a 160dpi screen this density value will be 1; on a 120 dpi screen it would be .75; etc.| 显示器的逻辑密度。 这是密度独立像素单元的缩放因子，其中一个DIP是大约160dpi屏幕上的一个像素（例如240x320,1.5“x2”屏幕），提供系统显示的基线。 因此，在160dpi屏幕上，此密度值将为1; 在120 dpi的屏幕上它将是.75 此值并不完全符合实际屏幕尺寸（由{@link #xdpi}和{@link #ydpi}给出，而是用于根据总体变化逐步缩放整个UI的大小 在显示屏dpi中。例如，240x320的屏幕密度为1即使其宽度为1.8“，1.3”等。但是，如果屏幕分辨率增加到320x480但屏幕尺寸仍然是1.5“x2”那么 密度会增加（可能增加到1.5）。|
|scaledDensity|A scaling factor for fonts displayed on the display.  This is the same as {@link #density}, except that it may be adjusted in smaller increments at runtime based on a user preference for the font size.|显示屏上显示的字体的缩放系数。 这与{@link #density}相同，不同之处在于它可以在运行时根据用户对字体大小的首选项以较小的增量进行调整。|
|densityDpi|The screen density expressed as dots-per-inch.  May be either{@link #DENSITY_LOW}, {@link #DENSITY_MEDIUM}, or {@link #DENSITY_HIGH}.|屏幕密度表示为每英寸点数。 可以是{@link #DENSITY_LOW}，{@ link #DENSITY_MEDIUM}或{@link #DENSITY_HIGH}。|

>核心直白举例简述：我的设备是320x480的，假设density=2，宽度为160dp；现在我要用640dp填满整个宽度density = **320px/640dp** =0.5 这个0.5就是修改的。


```
object DensityUtil {

    //参考设备的宽度,单位dp
    private const val WIDTH = 1920f
    private var appDensity = 0f
    private var appScaleDensity = 0f

    fun setDensity(application: Application, activity: Activity) {
        //获取当前app屏幕信息
        val displayMetrics = application.resources.displayMetrics
        if (appDensity == 0f) {
            //初始化赋值操作
            appDensity = displayMetrics.density
            appScaleDensity = displayMetrics.scaledDensity

            //计算目标值的density，scaleDensity，densityDpi，获取到的单位是px
            val targetDensity = displayMetrics.widthPixels / WIDTH
            val targetScaleDensity = targetDensity * (appScaleDensity / appDensity)
            val targetDensityDpi = (targetDensity * 160).toInt()

            //添加字体变化监听回调
            application.registerComponentCallbacks(object : ComponentCallbacks {
                override fun onLowMemory() {
                }

                override fun onConfigurationChanged(newConfig: Configuration?) {
                    //字体发生更改，重新对scaleDensity进行赋值
                    if (newConfig != null && newConfig.fontScale > 0) {
                        appScaleDensity = application.resources.displayMetrics.scaledDensity
                    }
                }

            })

            //替换Activity的density，scaleDensity,densityDpi
            val dm = activity.resources.displayMetrics
            dm.density = targetDensity
            dm.scaledDensity = targetScaleDensity
            dm.densityDpi = targetDensityDpi
        }
    }

}
```
#### 使用
可以在类似BaseActivity或Application中初始化
**1.在Application中初始化**
```
class AppApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        initDensity()
    }

    private fun initDensity() {
        registerActivityLifecycleCallbacks(object :ActivityLifecycleCallbacks{
            override fun onActivityPaused(activity: Activity?) {
            }

            override fun onActivityResumed(activity: Activity?) {
            }

            override fun onActivityStarted(activity: Activity?) {
            }

            override fun onActivityDestroyed(activity: Activity?) {
            }

            override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {
            }

            override fun onActivityStopped(activity: Activity?) {
            }

            override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {
                DensityUtil.setDensity(this@AppApplication,activity!!)
            }

        })
    }
}
```
>问题：本来觉得是个好方法，一劳永逸啊，但是。。。当我在使用webview后，再退回界面，适配失效了。所以这个方法还会有一些奇怪的问题，经不起考验。


### 二.屏幕适配-自定义View
##### 缩放比例是关键
>原理：以一个特定宽度尺寸的设备为参考，在View 的**加载过程**，根据当前设备的实际像素换算出目标像素，再作用到控件上。
##### 缩放比例工具类
>缩放中也有不同的业务如：1.只按宽度缩放，这样整体的比例不会变；2.宽一个缩放系数，高一个缩放系数，这样页面显示不会变

>有的高的缩放系数会把状态栏考虑进去。需要旋转的会有几种情况。一种是配置写死例如竖屏，自己旋转的；一种是根据重力屏幕旋转；一般有视频全屏之类的，适配需要考虑。

>例子为最简单的屏幕不旋转,全屏以宽度缩放系数适配的例子。
```
class UIUtils private constructor(context: Context) {

    private var displayMetricsWidth = 0f
    private var displayMetricsHeight = 0f

    init {
        //计算缩放系数
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val displayMetrics = DisplayMetrics()
        //得到设备的真实值
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        displayMetricsWidth = displayMetrics.widthPixels.toFloat()
        displayMetricsHeight = displayMetrics.heightPixels.toFloat()

    }

    /**
     * 获取横向缩放系数
     */
    fun getHorizontalScaleValue(): Float {
        return displayMetricsWidth / STANDARD_WIDTH
    }

    /**
     * 获取竖向缩放系数
     */
    fun getVerticalScaleValue(): Float {
        return displayMetricsHeight / STANDARD_HEIGHT
    }

    fun getWidth(width: Int): Int {
        return Math.round(width * getHorizontalScaleValue())
    }

    fun getHeight(height: Int): Int {
        return Math.round(height * getHorizontalScaleValue())
    }


    companion object {
        private lateinit var instance: UIUtils

        private const val STANDARD_WIDTH = 1080f
        private const val STANDARD_HEIGHT = 1920f


        fun getInstance():UIUtils{
            if (!::instance.isInitialized){
               throw Exception("UIUtils not init")
            }
            return instance
        }

        fun getInstance(context: Context): UIUtils {
            if (!::instance.isInitialized) {
                instance = UIUtils(context)
            }
            return instance
        }

        fun restartInstance(context: Context) {
            instance = UIUtils(context)
        }
    }
}
```
##### 2.1使用自定义布局
继承要使用的布局，并对子View进行适配，使用需要用px
```
class UIRelativeLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {
    private var flag = true

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if (flag){
            flag = false

            val scaleX = UIUtils.getInstance(context).getHorizontalScaleValue()
            val childCount = this.childCount
            for (i in 0 until childCount){
                val child = this.getChildAt(i)
                val layoutParams = child.layoutParams as LayoutParams

                layoutParams.width= (layoutParams.width*scaleX).toInt()
                layoutParams.height= (layoutParams.height*scaleX).toInt()
                layoutParams.leftMargin= (layoutParams.leftMargin*scaleX).toInt()
                layoutParams.rightMargin= (layoutParams.rightMargin*scaleX).toInt()
                layoutParams.topMargin= (layoutParams.topMargin*scaleX).toInt()
                layoutParams.bottomMargin= (layoutParams.bottomMargin*scaleX).toInt()
            }
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

    }
}
```
layout
```
 <com.ticccccc.uidesnitytest.ui.UIRelativeLayout
            android:id="@+id/mRL"
            android:layout_width="match_parent"
            android:layout_height="wrap_content">
        <View
                android:id="@+id/mViewZero"
                android:layout_width="270px"
                android:background="@color/colorAccent"
                android:layout_height="270px"/>
    </com.ticccccc.uidesnitytest.ui.UIRelativeLayout>
```

##### 2.2 使用技术算工具，在Activity初始化时候对布局适配
```
object ViewCalculateUtil {
    fun setViewLayoutParam(
        view: View,
        width: Int,
        height: Int,
        topMargin: Int,
        bottomMargin: Int,
        leftMargin: Int,
        rightMargin: Int
    ) {
        val layoutParams = view.layoutParams as RelativeLayout.LayoutParams
        if (width != RelativeLayout.LayoutParams.MATCH_PARENT
            && width != RelativeLayout.LayoutParams.WRAP_CONTENT
            && width != RelativeLayout.LayoutParams.FILL_PARENT
        ) {
            layoutParams.width = UIUtils.getInstance().getWidth(width)
        } else {
            layoutParams.width = width
        }
        if (height != RelativeLayout.LayoutParams.MATCH_PARENT
            && height != RelativeLayout.LayoutParams.WRAP_CONTENT
            && height != RelativeLayout.LayoutParams.FILL_PARENT
        ) {
            layoutParams.height = UIUtils.getInstance().getWidth(height)
        } else {
            layoutParams.height = height
        }
        layoutParams.topMargin = UIUtils.getInstance().getHeight(topMargin)
        layoutParams.bottomMargin = UIUtils.getInstance().getHeight(bottomMargin)
        layoutParams.leftMargin = UIUtils.getInstance().getHeight(leftMargin)
        layoutParams.rightMargin = UIUtils.getInstance().getHeight(rightMargin)
        view.layoutParams = layoutParams
    }

    fun setTextSize(view:TextView,size:Int){
        view.setTextSize(TypedValue.COMPLEX_UNIT_PX,UIUtils.getInstance().getHeight(size).toFloat())
    }

}
```
MainActivity
```
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        UIUtils.getInstance(applicationContext)
        initView()
    }

    private fun initView() {
        ViewCalculateUtil.setViewLayoutParam(mViewOne, 270, 270, 0, 0, 0, 0)
        ViewCalculateUtil.setViewLayoutParam(mViewTwo, 810, 270, 0, 0, 0, 0)
        ViewCalculateUtil.setViewLayoutParam(mViewThree, 540, 270, 0, 0, 0, 0)
        ViewCalculateUtil.setViewLayoutParam(mViewFour, 1080, 270, 0, 0, 0, 0)
    }
}
```
layout
```
<RelativeLayout
        xmlns:android="http://schemas.android.com/apk/res/android"
        xmlns:tools="http://schemas.android.com/tools"
        xmlns:app="http://schemas.android.com/apk/res-auto"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        tools:context=".MainActivity">
    <View android:id="@+id/mViewOne"
          android:layout_width="match_parent"
          android:background="@color/colorPrimary"
          android:layout_height="20dp"/>
    <View android:id="@+id/mViewTwo"
          android:layout_below="@id/mViewOne"
          android:layout_width="match_parent"
          android:background="@color/colorPrimary"
          android:layout_height="20dp"/>
    <View android:id="@+id/mViewThree"
          android:layout_below="@id/mViewTwo"
          android:layout_width="match_parent"
          android:background="@color/colorPrimary"
          android:layout_height="20dp"/>
    <View android:id="@+id/mViewFour"
          android:layout_below="@id/mViewThree"
          android:layout_width="match_parent"
          android:background="@color/colorPrimary"
          android:layout_height="20dp"/>
    
</RelativeLayout>
```
