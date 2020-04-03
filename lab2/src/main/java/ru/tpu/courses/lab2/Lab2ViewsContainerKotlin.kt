package ru.tpu.courses.lab2

import android.content.Context
import android.content.res.TypedArray
import android.os.Parcelable
import android.util.AttributeSet
import android.widget.LinearLayout
import android.widget.TextView

import androidx.annotation.Px

/**
 * Простейший пример самописного View. В данном случае мы просто наследуемся от LinearLayout-а и
 * добавляем свою логику, но также есть возможность отнаследоваться от [android.view.ViewGroup],
 * если необходимо реализовать контейнер для View полностью с нуля, либо отнаследоваться от [android.view.View].
 *
 *
 * Здесь можно было бы добавить автоматическое сохранение и восстановление состояния, переопределив методы
 * [.onSaveInstanceState] и [.onRestoreInstanceState].
 */

class Lab2ViewsContainerKotlin
/**
 * Конструктор, вызывается при инфлейте View, когда у View указан дополнительный стиль.
 * Почитать про стили можно здесь https://developer.android.com/guide/topics/ui/look-and-feel/themes
 *
 * @param attrs атрибуты, указанные в XML. Стандартные android атрибуты обрабатываются внутри родительского класса.
 * Здесь необходимо только обработать наши атрибуты.
 */
@JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : LinearLayout(context, attrs, defStyleAttr) {

    private val minViewsCount: Int=0
    private var viewsCount: Int = 0
    private val nValue: Int = 0

//    init {
//        // Свои атрибуты описываются в файле res/values/attrs.xml
//        // Эта строчка объединяет возможные применённые к View стили
//        val a = context.obtainStyledAttributes(attrs, R.styleable.Lab2ViewsContainerKotlin, defStyleAttr, 0)
//
//        minViewsCount = a.getInt(R.styleable.Lab2ViewsContainerKotlin_lab2_minViewsCount, 0)
//        require(minViewsCount >= 0) { "minViewsCount can't be less than 0" }
//
//        // Полученный TypedArray необходимо обязательно очистить.
//        a.recycle()
//
//        setViewsCount(minViewsCount)
//    }

    /**
     * Программно создаём [TextView] и задаём его атрибуты, альтернативно можно описать его в
     * xml файле и инфлейтить его через класс LayoutInflater.
     */
    fun incrementViews() {
        val textView = TextView(context)
        textView.setPadding(dpToPx(8f), dpToPx(8f), dpToPx(8f), dpToPx(8f))
        textView.textSize = 20f
        textView.text = viewsCount++.toString()
        // У каждого View, который находится внутри ViewGroup есть LayoutParams,
        // в них содержится информация для лэйаута компонентов.
        // Базовая реализация LayoutParams содержит только определение ширины и высоты
        // (то, что мы указываем в xml в атрибутах layout_widget и layout_height).
        // Получить их можно через метод getLayoutParams у View. Метод addView смотрит, если у View
        // не установлены LayoutParams, то создаёт дефолтные, вызывая метод generateDefaultLayoutParams
        if (viewsCount == nValue) {
            val staticTextView = TextView(context)
            staticTextView.setPadding(dpToPx(8f), dpToPx(8f), dpToPx(8f), dpToPx(8f))
            staticTextView.textSize = 20f
            staticTextView.text = "Это статичный View"
        }
        addView(textView)
    }

    fun setViewsCount(viewsCount: Int) {
        var viewsCount = viewsCount
        if (this.viewsCount == viewsCount) {
            return
        }
        viewsCount = if (viewsCount < minViewsCount) minViewsCount else viewsCount

        removeAllViews()
        this.viewsCount = 0
        for (i in 0 until viewsCount) {
            incrementViews()
        }
    }

    fun getViewsCount(): Int {
        return viewsCount
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
}
/**
 * Этот конструктор используется при создании View в коде.
 */
/**
 * Этот конструктор выдывается при создании View из XML.
 */
