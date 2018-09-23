package com.kwunai.github.widgets

import android.content.Context
import android.support.v7.widget.AppCompatImageView
import android.util.AttributeSet
import android.support.v7.content.res.AppCompatResources
import android.graphics.drawable.Drawable
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.BitmapDrawable
import android.graphics.*
import com.kwunai.github.R
import android.text.TextUtils


class AvatarImageView : AppCompatImageView {

    private val colors: IntArray = intArrayOf(-0xbb449a, -0xaa3323, -0x4488cd, -0x99ab, -0x44bc, -0xbb5501)
    private val colorsNum = colors.size
    private val defaultTextColor = -0x1
    private val defaultBoarderColor = -0x1
    private val defaultBoarderWidth = 4
    private val defaultTypeBitmap = 0
    private val defaultTypeText = 1
    private val defaultText = ""
    private val defaultBoarderDimension = 1
    private val defaultTextSizeRatio = 0.4f
    private val defaultTextMaskRatio = 0.8f
    private val defaultBoarderShow = false

    private val bitmapConfig8888 = Bitmap.Config.ARGB_8888
    @Suppress("DEPRECATION")
    private val bitmapConfig4444 = Bitmap.Config.ARGB_4444

    private var mRadius: Int = 0//the circle's radius
    private var mCenterX: Int = 0
    private var mCenterY: Int = 0
    private var mType = defaultTypeBitmap
    private var mBgColor = colors[0]//background color when show text
    private var mTextColor = defaultTextColor
    private var mBoarderColor = defaultBoarderColor
    private var mBoarderWidth = defaultBoarderWidth
    private var mTextSizeRatio = defaultTextSizeRatio//the text size divides (2 * mRadius)
    private var mTextMaskRatio = defaultTextMaskRatio//the inner-radius text divides outer-radius text
    private var mShowBoarder = defaultBoarderShow
    private var mText: String? = defaultText

    private var mPaintTextForeground: Paint? = null//draw text, in text mode
    private var mPaintTextBackground: Paint? = null//draw circle, in text mode
    private var mPaintDraw: Paint? = null//draw bitmap, int bitmap mode
    private var mPaintCircle: Paint? = null//draw boarder
    private var mFontMetrics: Paint.FontMetrics? = null

    private var mBitmap: Bitmap? = null//the pic
    private var mBitmapShader: BitmapShader? = null//used to adjust position of bitmap
    private var mMatrix: Matrix? = null//used to adjust position of bitmap

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initAttr(context, attrs)
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initAttr(context, attrs)
        init()
    }

    private fun initAttr(context: Context, attrs: AttributeSet?) {
        if (attrs == null) {
            return
        }
        val a = context.obtainStyledAttributes(attrs, R.styleable.AvatarImageView) ?: return
        val n = a.indexCount
        for (i in 0 until n) {
            val attr = a.getIndex(i)
            when (attr) {
                R.styleable.AvatarImageView_aiv_TextSizeRatio -> mTextSizeRatio = a.getFloat(attr, defaultTextSizeRatio)
                R.styleable.AvatarImageView_aiv_TextMaskRatio -> mTextMaskRatio = a.getFloat(attr, defaultTextMaskRatio)
                R.styleable.AvatarImageView_aiv_BoarderWidth -> mBoarderWidth = a.getDimensionPixelSize(attr, defaultBoarderWidth)
                R.styleable.AvatarImageView_aiv_BoarderColor -> mBoarderColor = a.getColor(attr, defaultBoarderColor)
                R.styleable.AvatarImageView_aiv_TextColor -> mTextColor = a.getColor(attr, defaultTextColor)
                R.styleable.AvatarImageView_aiv_ShowBoarder -> mShowBoarder = a.getBoolean(attr, defaultBoarderShow)
            }
        }
        a.recycle()
    }

    private fun init() {
        mMatrix = Matrix()

        mPaintTextForeground = Paint()
        mPaintTextForeground!!.color = mTextColor
        mPaintTextForeground!!.isAntiAlias = true
        mPaintTextForeground!!.textAlign = Paint.Align.CENTER

        mPaintTextBackground = Paint()
        mPaintTextBackground!!.isAntiAlias = true
        mPaintTextBackground!!.style = Paint.Style.FILL

        mPaintDraw = Paint()
        mPaintDraw!!.isAntiAlias = true
        mPaintDraw!!.style = Paint.Style.FILL

        mPaintCircle = Paint()
        mPaintCircle!!.isAntiAlias = true
        mPaintCircle!!.style = Paint.Style.STROKE
        mPaintCircle!!.color = mBoarderColor
        mPaintCircle!!.strokeWidth = mBoarderWidth.toFloat()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        val paddingLeft = paddingLeft
        val paddingTop = paddingTop
        val contentWidth = w - paddingLeft - paddingRight
        val contentHeight = h - paddingTop - paddingBottom

        mRadius = if (contentWidth < contentHeight) contentWidth / 2 else contentHeight / 2
        mCenterX = paddingLeft + mRadius
        mCenterY = paddingTop + mRadius
        refreshTextSizeConfig()
    }

    private fun refreshTextSizeConfig() {
        mPaintTextForeground!!.textSize = mTextSizeRatio * 2f * mRadius.toFloat()
        mFontMetrics = mPaintTextForeground!!.fontMetrics
    }

    private fun refreshTextConfig() {
        if (mBgColor != mPaintTextBackground!!.color) {
            mPaintTextBackground!!.color = mBgColor
        }
        if (mTextColor != mPaintTextForeground!!.color) {
            mPaintTextForeground!!.color = mTextColor
        }
    }

    private fun setTextAndColor(text: String, bgColor: Int) {
        if (!stringEqual(text, this.mText) || bgColor != this.mBgColor) {
            this.mText = text
            this.mBgColor = bgColor
            this.mType = defaultTypeText
            invalidate()
        }
    }

    fun setTextAndColorSeed(text: String, colorSeed: String) {
        setTextAndColor(text, getColorBySeed(colorSeed))
    }


    private fun setBitmap(bitmap: Bitmap?) {
        if (bitmap == null) {
            return
        }
        if (this.mType != defaultTypeBitmap || bitmap != this.mBitmap) {
            this.mBitmap = bitmap
            this.mType = defaultTypeBitmap
            invalidate()
        }
    }

    private fun getColorBySeed(seed: String): Int {
        return if (TextUtils.isEmpty(seed)) {
            colors[0]
        } else colors[Math.abs(seed.hashCode() % colorsNum)]
    }

    private fun setDrawable(drawable: Drawable?) {
        val bitmap = getBitmapFromDrawable(drawable)
        setBitmap(bitmap)
    }

    private fun getBitmapFromDrawable(drawable: Drawable?): Bitmap? {
        if (drawable == null) {
            return null
        }
        if (drawable is BitmapDrawable) {
            return drawable.bitmap
        }
        return try {
            val bitmap: Bitmap = if (drawable is ColorDrawable) {
                Bitmap.createBitmap(defaultBoarderDimension, defaultBoarderDimension, bitmapConfig8888)
            } else {
                Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, bitmapConfig8888)
            }
            val canvas = Canvas(bitmap)
            drawable.setBounds(0, 0, canvas.width, canvas.height)
            drawable.draw(canvas)
            bitmap
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }

    }

    override fun onDraw(canvas: Canvas) {
        if (mBitmap != null && mType == defaultTypeBitmap) {
            toDrawBitmap(canvas)
        } else if (mText != null && mType == defaultTypeText) {
            toDrawText(canvas)
        }
        if (mShowBoarder) {
            drawBoarder(canvas)
        }
    }

    private fun toDrawText(canvas: Canvas) {
        if (mText!!.length == 1) {
            drawText(canvas)
        } else {
            drawBitmap(canvas, createClipTextBitmap((mRadius / mTextMaskRatio).toInt()), false)
        }
    }

    private fun toDrawBitmap(canvas: Canvas) {
        if (mBitmap == null) return
        drawBitmap(canvas, mBitmap!!, true)
    }

    private fun drawBitmap(canvas: Canvas, bitmap: Bitmap, adjustScale: Boolean) {
        refreshBitmapShaderConfig(bitmap, adjustScale)
        mPaintDraw!!.shader = mBitmapShader
        canvas.drawCircle(mCenterX.toFloat(), mCenterY.toFloat(), mRadius.toFloat(), mPaintDraw)
    }

    private fun refreshBitmapShaderConfig(bitmap: Bitmap, adjustScale: Boolean) {
        mBitmapShader = BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        mMatrix!!.reset()
        val bitmapWidth = bitmap.width
        val bitmapHeight = bitmap.height
        if (adjustScale) {
            val bSize = Math.min(bitmapWidth, bitmapHeight)
            val scale = mRadius * 2.0f / bSize
            mMatrix!!.setScale(scale, scale)
            if (bitmapWidth > bitmapHeight) {
                mMatrix!!.postTranslate(-(bitmapWidth * scale / 2 - mRadius.toFloat() - paddingLeft.toFloat()), paddingTop.toFloat())
            } else {
                mMatrix!!.postTranslate(paddingLeft.toFloat(), -(bitmapHeight * scale / 2 - mRadius.toFloat() - paddingTop.toFloat()))
            }
        } else {
            mMatrix!!.postTranslate((-(bitmapWidth / 2 - mRadius - paddingLeft)).toFloat(), (-(bitmapHeight * 1 / 2 - mRadius - paddingTop)).toFloat())
        }

        mBitmapShader!!.setLocalMatrix(mMatrix)
    }

    private fun createClipTextBitmap(bitmapRadius: Int): Bitmap {
        val bitmapClipText = Bitmap.createBitmap(bitmapRadius * 2, bitmapRadius * 2, bitmapConfig4444)
        val canvasClipText = Canvas(bitmapClipText)
        val paintClipText = Paint()
        paintClipText.style = Paint.Style.FILL
        paintClipText.isAntiAlias = true
        paintClipText.color = mBgColor
        canvasClipText.drawCircle(bitmapRadius.toFloat(), bitmapRadius.toFloat(), bitmapRadius.toFloat(), paintClipText)

        paintClipText.textSize = mTextSizeRatio * mRadius.toFloat() * 2f
        paintClipText.color = mTextColor
        paintClipText.textAlign = Paint.Align.CENTER
        val fontMetrics = paintClipText.fontMetrics
        canvasClipText.drawText(mText, 0, mText!!.length, bitmapRadius.toFloat(),
                bitmapRadius + Math.abs(fontMetrics.top + fontMetrics.bottom) / 2, paintClipText)
        return bitmapClipText
    }

    private fun drawText(canvas: Canvas) {
        refreshTextConfig()
        canvas.drawCircle(mCenterX.toFloat(), mCenterY.toFloat(), mRadius.toFloat(), mPaintTextBackground)
        canvas.drawText(mText!!, 0, mText!!.length, mCenterX.toFloat(), mCenterY + Math.abs(mFontMetrics!!.top + mFontMetrics!!.bottom) / 2, mPaintTextForeground)
    }

    private fun drawBoarder(canvas: Canvas) {
        canvas.drawCircle(mCenterX.toFloat(), mCenterY.toFloat(), (mRadius - mBoarderWidth / 2).toFloat(), mPaintCircle)
    }

    override fun setImageDrawable(drawable: Drawable?) {
        setDrawable(drawable)
    }

    override fun setImageResource(resId: Int) {
        setDrawable(AppCompatResources.getDrawable(context, resId))
    }


    private fun stringEqual(a: String?, b: String?): Boolean {
        return if (a == null) {
            b == null
        } else {
            if (b == null) {
                false
            } else a == b
        }
    }

}


