package ru.tpu.courses.lab2
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.widget.TextView
import android.widget.LinearLayout.LayoutParams
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import kotlinx.android.synthetic.main.lab2_activity_kotlin.*

import android.graphics.Point


import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.annotation.Px




class Lab2KotlinActivity : AppCompatActivity() {

    private val minViewsCount: Int = 0
    private var viewsCount: Int = 0
    private val nValue: Int = 7
    private var viewId: Int = 0
    private var startViewY: Float = -1f


    private lateinit var view: View

    companion object {
        private val STATE_VIEWS_COUNT = "views_count"
    }

    //    private var lab2ViewsContainer: Lab2ViewsContainerKotlin? = null
    fun newIntent(context: Context): Intent {
        return Intent(context, Lab2KotlinActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.lab2_activity_kotlin)
        val display = windowManager.defaultDisplay
        val point = Point()
        display.getSize(point)
        val screenWidth = point.x
        val screenHeight = point.y
        var startStateBottom = true
        var startStateUp = true
        if (savedInstanceState != null) {
            setViewsCount(savedInstanceState.getInt(STATE_VIEWS_COUNT))
        }

        scroll_view.viewTreeObserver.addOnScrollChangedListener {
            val scrollY = scroll_view.scrollY

            //если создан n view
            if (viewId != 0) {
                //стартовая позиция
                if (startViewY == -1f)
                    this.startViewY = view.y
                var viewTop = view.y
                var viewBottom = view.y + view.height
                var scrollViewTop = scroll_view.y
                var scrollViewBottom = scroll_view.y + scroll_view.height
                var scrollObr = scroll_view.getChildAt(0).getHeight() - scroll_view.height - scrollY
                var translationY = view.translationY

                var value = scrollViewBottom - view.height + scrollY
                var upState=false

                lab2_textview.text = "scrollY = $scrollY"
                lab2_textview3.text = "startStateUp = $startStateUp"
                lab2_textview4.text = "startStateBottom = $startStateBottom"
                lab2_textview5.text = "величина = $value"

                //прилипание к краям
                //к верхнему
                //если блок только появился и он выше экрана, то не надо ничего делать
                if(startStateUp){
                    if (startViewY >= scrollY) {
                        startStateUp = false
                    }
                }else {
                    if (startViewY <= scrollY) {
                        view.y = Math.max(scrollY.toFloat(), startViewY)
                        upState = true
                    } else {
                        upState = false
                    }
                }
                //к нижнему
                //если блок только появился и он ниже экрана, то не надо ничего делать
                if (startStateBottom) {
                    if (value >= startViewY) {
                        startStateBottom = false
                    }
                } else {
                    if (view.y + view.height >= scrollViewBottom && !upState) {
                        view.y = Math.min(startViewY, scrollViewBottom - view.height + scrollY)
                    }
                }
            }
        }
        // Set a click listener for button widget
        btn_add_view.setOnClickListener {
            incrementViews()
        }

    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.run {
            putInt(STATE_VIEWS_COUNT, getViewsCount())
        }
        super.onSaveInstanceState(outState)
    }

    private fun getViewsCount(): Int {
        return viewsCount
    }

    private fun setViewsCount(viewsCount: Int) {
        var viewsCount = viewsCount
        if (this.viewsCount == viewsCount) {
            return
        }
        viewsCount = if (viewsCount < minViewsCount) minViewsCount else viewsCount

        this.viewsCount = 0
        for (i in 0 until viewsCount) {
            incrementViews()
        }
    }


    @SuppressLint("SetTextI18n")
    private fun incrementViews() {

        createView()
        if (viewsCount == nValue) {
            createAdditionalView()
        }
        viewsCount++
    }

    private fun createView() {
        // Create a new TextView instance programmatically
        val textView = TextView(this)

        val linearLayout = LinearLayout(this)

        // Creating a LinearLayout.LayoutParams object for text view
        var params = RelativeLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, // This will define text view width
                LayoutParams.WRAP_CONTENT // This will define text view height
        )
        params.setMargins(10, 10, 10, 10)

        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16F)
        textView.setTypeface(textView.typeface, Typeface.BOLD_ITALIC)
        textView.setTypeface(Typeface.MONOSPACE)
        textView.setPadding(50, 10, 10, 10)
        textView.setOnClickListener {
            Toast.makeText(this, textView.text, Toast.LENGTH_SHORT).show()
        }
        textView.text = "Hi, i am a TextView. Number : $viewsCount"
        textView.setTextColor(Color.RED)
        textView.setBackgroundColor(Color.YELLOW)
        textView.id = ViewCompat.generateViewId()
        if (textView.id > 0) {
            params.addRule(RelativeLayout.BELOW, textView.id - 1)
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT)
        }

        textView.layoutParams = params

        linearLayout1.addView(textView)


    }

    private fun createAdditionalView() {
        val additionalTextView = TextView(this)

        var params = RelativeLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, // This will define text view width
                LayoutParams.WRAP_CONTENT // This will define text view height
        )
        params.setMargins(10, 10, 10, 10)

        additionalTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16F)
        additionalTextView.setTypeface(additionalTextView.typeface, Typeface.BOLD_ITALIC)
        additionalTextView.typeface = Typeface.MONOSPACE
        additionalTextView.setPadding(50, 10, 10, 10)
        additionalTextView.setOnClickListener {
            Toast.makeText(this, "Id of this view is $viewId", Toast.LENGTH_SHORT).show()
        }
        additionalTextView.text = "Hey! It's additional view! I'm located over number : $viewsCount"
        additionalTextView.setTextColor(Color.BLACK)
        additionalTextView.setBackgroundColor(Color.LTGRAY)
        additionalTextView.setId(ViewCompat.generateViewId())
        this.viewId = additionalTextView.id
        if (additionalTextView.id > 0) {
            params.addRule(RelativeLayout.BELOW, this.viewId - 2)
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT)
        }
        additionalTextView.layoutParams = params
        additionalTextView.z = 2f
        linearLayout1.addView(additionalTextView)
        this.view = findViewById<TextView>(viewId)
    }

    /**
     * Метод трансформирует указанные dp в пиксели, используя density экрана.
     */
    @Px
    fun dpToPx(dp: Float): Int {
        if (dp == 0f) {
            return 0
        }
        val density = resources.displayMetrics.density
        return Math.ceil((density * dp).toDouble()).toInt()
    }

    //узнать ориентацию экрана
    private fun getScreenOrientation(): Boolean {
        if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT)
            return true
        else if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE)
            return false
        return false
    }
}
