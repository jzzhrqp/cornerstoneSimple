package imagebrowserlibrary


import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.view.*
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.aoshuotec.iron.cornerstone.R
import com.aoshuotec.iron.cornerstone.imagebrowserlibrary.MNGestureView
import com.aoshuotec.iron.cornerstone.imagebrowserlibrary.MNViewPager
import com.aoshuotec.iron.cornerstone.imagebrowserlibrary.ProgressWheel
import com.aoshuotec.iron.cornerstone.imagebrowserlibrary.transforms.*
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.github.chrisbanes.photoview.PhotoView
import java.util.*


/**
 * 图片浏览的页面
 */
class MNImageBrowserActivity : AppCompatActivity() {

    private var context: Context? = null

    private var mnGestureView: MNGestureView? = null
    private var viewPagerBrowser: MNViewPager? = null
    private var tvNumShow: TextView? = null
    private var rl_black_bg: RelativeLayout? = null

    private var imageUrlList = ArrayList<String>()
    private var currentPosition: Int = 0
    private var currentViewPagerTransform: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setWindowFullScreen()
        setContentView(R.layout.activity_mnimage_browser)
        context = this

        initIntent()

        initViews()

        initData()

        initViewPager()

    }

    private fun setWindowFullScreen() {
        //设置全屏
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        if (Build.VERSION.SDK_INT >= 19) {
            // 虚拟导航栏透明
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
        }
    }

    private fun initIntent() {
        currentPosition = intent.getIntExtra(IntentKey_CurrentPosition, 1)
        currentViewPagerTransform = intent.getIntExtra(IntentKey_ViewPagerTransformType, ViewPagerTransform_Default)
        imageUrlList = intent.getStringArrayListExtra(IntentKey_ImageList)

    }

    private fun initViews() {
        viewPagerBrowser = findViewById(R.id.viewPagerBrowser) as MNViewPager
        mnGestureView = findViewById(R.id.mnGestureView) as MNGestureView
        tvNumShow = findViewById(R.id.tvNumShow) as TextView
        rl_black_bg = findViewById(R.id.rl_black_bg) as RelativeLayout

    }

    private fun initData() {
        tvNumShow!!.text = ((currentPosition + 1).toString() + "/" + imageUrlList.size).toString()
    }

    private fun initViewPager() {
        viewPagerBrowser!!.adapter = MyAdapter()
        viewPagerBrowser!!.currentItem = currentPosition
        setViewPagerTransforms()
        viewPagerBrowser!!.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {
                tvNumShow!!.text = ((position + 1).toString() + "/" + imageUrlList.size).toString()
            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        })

        mnGestureView!!.setOnSwipeListener(object : MNGestureView.OnSwipeListener {
            override fun downSwipe() {
                finishBrowser()
            }

            override fun onSwiping(deltaY: Float) {
                tvNumShow!!.visibility = View.GONE

                var mAlpha = 1 - deltaY / 500
                if (mAlpha < 0.3) {
                    mAlpha = 0.3f
                }
                if (mAlpha > 1) {
                    mAlpha = 1f
                }
                rl_black_bg!!.alpha = mAlpha
            }

            override fun overSwipe() {
                tvNumShow!!.visibility = View.VISIBLE
                rl_black_bg!!.alpha = 1f
            }
        })
    }

    private fun setViewPagerTransforms() {
        when (currentViewPagerTransform) {
            ViewPagerTransform_Default -> viewPagerBrowser!!.setPageTransformer(true, DefaultTransformer())
            ViewPagerTransform_DepthPage -> viewPagerBrowser!!.setPageTransformer(true, DepthPageTransformer())
            ViewPagerTransform_RotateDown -> viewPagerBrowser!!.setPageTransformer(true, RotateDownTransformer())
            ViewPagerTransform_RotateUp -> viewPagerBrowser!!.setPageTransformer(true, RotateUpTransformer())
            ViewPagerTransform_ZoomIn -> viewPagerBrowser!!.setPageTransformer(true, ZoomInTransformer())
            ViewPagerTransform_ZoomOutSlide -> viewPagerBrowser!!.setPageTransformer(true, ZoomOutSlideTransformer())
            ViewPagerTransform_ZoomOut -> viewPagerBrowser!!.setPageTransformer(true, ZoomOutTransformer())
            else -> viewPagerBrowser!!.setPageTransformer(true, ZoomOutSlideTransformer())
        }
    }

    private fun finishBrowser() {
        tvNumShow!!.visibility = View.GONE
        rl_black_bg!!.alpha = 0f
        finish()
        this.overridePendingTransition(0, R.anim.browser_exit_anim)
    }

    override fun onBackPressed() {
        finishBrowser()
    }


    private inner class MyAdapter : PagerAdapter() {

        private val layoutInflater: LayoutInflater = LayoutInflater.from(context)

        override fun getCount(): Int {
            return imageUrlList.size
        }

        override fun isViewFromObject(view: View, `object`: Any): Boolean {
            return view === `object`
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            container.removeView(`object` as View)
        }

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val inflate = layoutInflater.inflate(R.layout.mn_image_browser_item_show_image, container, false)
            val imageView = inflate.findViewById<View>(R.id.imageView) as PhotoView
            val rl_browser_root = inflate.findViewById<View>(R.id.rl_browser_root) as RelativeLayout
            val progressWheel = inflate.findViewById<View>(R.id.progressWheel) as ProgressWheel
            val rl_image_placeholder_bg = inflate.findViewById<View>(R.id.rl_image_placeholder_bg) as RelativeLayout
            val iv_fail = inflate.findViewById<View>(R.id.iv_fail) as ImageView

            iv_fail.visibility = View.GONE

            val url = imageUrlList[position]

            //            GlideApp.with(MNImageBrowserActivity.this).applyDefaultRequestOptions(new RequestListener<ImageView>())
            Glide.with(context!!).load(url).listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(e: GlideException?, model: Any, target: Target<Drawable>, isFirstResource: Boolean): Boolean {
                    progressWheel.visibility = View.GONE
                    iv_fail.visibility = View.VISIBLE
                    return false
                }

                override fun onResourceReady(resource: Drawable, model: Any, target: Target<Drawable>, dataSource: DataSource, isFirstResource: Boolean): Boolean {
                    progressWheel.visibility = View.GONE
                    rl_image_placeholder_bg.visibility = View.GONE
                    iv_fail.visibility = View.GONE
                    return false
                }
            }).into(imageView)


            rl_browser_root.setOnClickListener { finishBrowser() }

            imageView.setOnClickListener { finishBrowser() }

            container.addView(inflate)
            return inflate
        }
    }

    companion object {

        val IntentKey_ImageList = "IntentKey_ImageList"
        val IntentKey_CurrentPosition = "IntentKey_CurrentPosition"
        val IntentKey_ViewPagerTransformType = "IntentKey_ViewPagerTransformType"
        val ViewPagerTransform_Default = 0
        val ViewPagerTransform_DepthPage = 1
        val ViewPagerTransform_RotateDown = 2
        val ViewPagerTransform_RotateUp = 3
        val ViewPagerTransform_ZoomIn = 4
        val ViewPagerTransform_ZoomOutSlide = 5
        val ViewPagerTransform_ZoomOut = 6
    }

}
